package com.brt.gpp.aplicacoes.enviarComprovanteServico;

import java.io.StringReader;
import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.GerarExtrato;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
* @Autor: 			Camile Cardoso Couto
* Data: 				19/04/2004
*
* Modificado por:   Geraldo Palmeira
* Data:			    28/11/2005
* Razao:		    Essa classe foi adaptada ao modelo Produtor Consumidor.
*/

public class ComprovanteServicoConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao	conexaoPrep;
	
	private String xmlExtrato;
	
	/**
	 * @param aLogId
	 * @param aNomeClasse
	 */
	public ComprovanteServicoConsumidor() 
	{
		super(GerentePoolLog.getInstancia(ComprovanteServicoConsumidor.class).getIdProcesso(Definicoes.CL_COMPROVANTE_SERVICO)
			     ,Definicoes.CL_COMPROVANTE_SERVICO);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception 
	{
		conexaoPrep = produtor.getConexao();	
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception 
	{
		startup();	
	}
	
	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = produtor;
        startup();
    }

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception 
	{	
		String statusProcessamentoRegistro = Definicoes.IND_LINHA_NAO_PROCESSADA;
		xmlExtrato = null;
		
		ComprovanteServicoVO vo = (ComprovanteServicoVO) obj;
		Timestamp dataRequisicao = vo.getDataRequisicao();
		String msisdn = vo.getMsisdn();
		String dataInicial = vo.getDataInicial();
		String dataFinal = vo.getDataFinal();	
		
		
		super.log(Definicoes.INFO, "Consumidor.execute", "Data requisicao: " + dataRequisicao + " MSISDN: " + msisdn + " Data Ini.: " + dataInicial + " Data Fim: " + dataFinal);
		
        //	Chama processo de extrato passando true na variavel eComprovanteServico
		GerarExtrato extrato = new GerarExtrato(super.getIdLog());
		xmlExtrato = extrato.comporExtrato(msisdn, dataInicial, dataFinal, true, dataRequisicao);
		
		super.log(Definicoes.DEBUG, "Consumidor.execute", "XMLExtrato: " + xmlExtrato);
		
		// Se conseguir gerar xml de extrato, insere registro na tabela de saida
		if (xmlExtrato != null)
		{
			// grava dados na tabela de saida
			if (gravaComprovanteServico(msisdn, xmlExtrato))
			{
				super.log(Definicoes.INFO, "Consumidor.execute", "Gravou registro");
				statusProcessamentoRegistro = Definicoes.IND_PROCESSADO;
			}	
			else
			{
				super.log(Definicoes.WARN, "Consumidor.execute", "Nao gravou registro");
				statusProcessamentoRegistro = Definicoes.IDT_PROCESSAMENTO_ERRO;
			}
		}
		else
		{
			statusProcessamentoRegistro = Definicoes.IDT_PROCESSAMENTO_ERRO;
		}
		
		// Atualiza tabela original com resultado do processamento do registro
		atualizaProcessamentoRegistro(msisdn, dataRequisicao, statusProcessamentoRegistro);
		
		super.log(Definicoes.INFO, "Consumidor.execute", "Atualizou registro");						

		// Deleta da tabela de saida todos os registros ja processados pelo Vitria
		/*
		String sqlDelete = " DELETE FROM TBL_GER_DADOS_COMPROVANTE WHERE IDT_STATUS_PROCESSAMENTO = ? ";
		Object param1[] = {Definicoes.IND_PROCESSADO};
		
		try 
		{
		    conexaoPrep.executaPreparedUpdate (sqlDelete, param1, super.getIdLog());		
		}
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "Consumidor.execute", "Excecao Interna GPP:"+ e1);								
			throw new GPPInternalErrorException (e1.toString());	
		}
		*/
	}
	
		/**
		* Metodo...: gravaComprovanteServico
		* Descricao: Insere na tabela o registro processado a ter o comprovante impresso
		* @param	aComprovante 	- Estrutura com dados do comprovante processado
		* @return	boolean			- TRUE (Gravou registro) ou FALSE (Nao gravou registro)									
		* @throws GPPInternalErrorException									
		*/
		private boolean gravaComprovanteServico(String aMSISDN, String aXMLExtrato) throws GPPInternalErrorException
		{
		super.log(Definicoes.DEBUG, "Consumidor.gravaComprovanteServico", "Inicio MSISDN " + aMSISDN);
		
		// Inicializa variaveis do metodo
		boolean retorno = true;
		String sql;
		
		try
		{	
			// Grava registro no banco de dados
			sql = " INSERT INTO TBL_INT_COMPROVANTE_OUT " +
			      " (DAT_CADASTRO, IDT_MSISDN, XML_DOCUMENT, IND_PROCESSADO) " +
				  " VALUES " +
				  " (SYSDATE, ?, ?, ?) ";
			
			Object param[] = {aMSISDN, new StringReader(aXMLExtrato), Definicoes.IND_LINHA_NAO_PROCESSADA};
			conexaoPrep.executaPreparedUpdate(sql, param, super.logId);							
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "Consumidor.gravaComprovanteServico", "Excecao:" + e);				
			throw new GPPInternalErrorException (e.toString());			 
		}	
		finally
		{
			super.log(Definicoes.DEBUG, "Consumidor.gravaComprovanteServico", "Fim");
		}
		return retorno;
		}
		
		
	/**
	* Metodo...: atualizaProcessamentoRegistro
	* Descricao: Atualiza o registro processado com o status do seu processamento
	* @param	aMSISDN			- Numero do MSISDN processado
	* @param	aDataRequisicao - Data em que foi solicitado o comprovante
	* @param 	aStatus			- Status do processamento
	* @return
	* @throws GPPInternalErrorException									
	*/
	public void atualizaProcessamentoRegistro(String aMSISDN, Timestamp aDataRequisicao, String aStatus) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Consumidor.atualizaProcessamentoRegistro", "Inicio MSISDN " + aMSISDN);
		
		// Inicializa variaveis do metodo
		String sqlUpdate;
		
		try
		{	
			// Atualiza registro no banco de dados
			sqlUpdate = " UPDATE TBL_GER_DADOS_COMPROVANTE " +
						" SET IDT_STATUS_PROCESSAMENTO = ? " +
						" WHERE IDT_MSISDN = ? AND DAT_REQUISICAO = ? " +
						" AND IDT_STATUS_PROCESSAMENTO = ? "; 
			Object param[] = {aStatus, aMSISDN, aDataRequisicao, Definicoes.IND_LINHA_NAO_PROCESSADA};
			conexaoPrep.executaPreparedUpdate(sqlUpdate, param, super.logId);
		}	
		
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "Consumidor.atualizaProcessamentoRegistro", "Excecao:" + e);				
			throw new GPPInternalErrorException (e.toString());			 
		}
		finally
		{
			super.log(Definicoes.DEBUG, "Consumidor.atualizaProcessamentoRegistro", "Fim");
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
	}
}