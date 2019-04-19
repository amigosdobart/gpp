package com.brt.gpp.aplicacoes.enviarComprovanteServico;

import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
* Este arquivo refere-se a classe ComprovanteServico, responsavel pela implementacao da
* logica de envio de comprovantes de servicos de assinantes para o DOC1
*
* <P> Versao:			1.0
*
* @Autor: 			Camile Cardoso Couto
* Data: 				19/04/2004
*
* Modificado por:   Geraldo Palmeira
* Data:			    28/11/2005
* Razao:		    Essa classe foi adaptada ao modelo Produtor Consumidor.
*/

public class ComprovanteServicoProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	private String sql;
	private ResultSet rs;
	private PREPConexao conexaoPrep;
    private String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	private int numRegistros;
	
	/**
	 * Metodo...: ComprovanteServico
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	public ComprovanteServicoProdutor (long logId) 
	{
		super(logId, Definicoes.CL_COMPROVANTE_SERVICO);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_COMPROVANTE_SERVICO;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso() 
	{
	    if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
			return "Foram processados " + numRegistros + " registros.";
	    else
	        return "Erro no processo.";
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso() 
	{
		return statusProcesso;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status) 
	{
		statusProcesso = status;	
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception 
	{
	    super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio do Processo");

			// Seleciona conexao do pool Prep Conexao
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
					
			// Seleciona dados para gerar comprovante
			sql = " SELECT DAT_REQUISICAO, IDT_MSISDN, " +
				  " TO_CHAR(DAT_PERIODO_INICIAL, 'DD/MM/YYYY HH24:MI:SS') as Data_Inicial, " +
			      " TO_CHAR(DAT_PERIODO_FINAL, 'DD/MM/YYYY HH24:MI:SS') as Data_Final " +
			      " FROM TBL_GER_DADOS_COMPROVANTE " +
			      " WHERE IDT_STATUS_PROCESSAMENTO = ? " +
			      " ORDER BY DAT_REQUISICAO ";			
			Object param[] = {Definicoes.IND_LINHA_NAO_PROCESSADA};
			rs = conexaoPrep.executaPreparedQuery(sql, param, logId);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception 
	{
		ComprovanteServicoVO vo = null;
		// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
		if(rs.next())
		{
			vo = new ComprovanteServicoVO();
			vo.setDataRequisicao(rs.getTimestamp("DAT_REQUISICAO"));
			vo.setMsisdn(rs.getString("IDT_MSISDN"));
			vo.setDataInicial(rs.getString("Data_Inicial"));
			vo.setDataFinal(rs.getString("Data_Final"));
			
			//	contador de registros processados
			  numRegistros++;
		}
		return(vo);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception 
	{
        // Libera a conexao de banco de dados
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		/*
		super.log(Definicoes.DEBUG, "enviarComprovanteServico", "Fim");
		*/
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim do Processo");
	}
	
	
	/**
	* Metodo...: limpaProcessoOk
	* Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	* 			  ou seja, os registros que já foram processados pelo ETI.
	* @param
	* @return	void										
	* @throws GPPInternalErrorException									
	*/
	
	public void limpaProcessoOk() throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Produtor.limpaProcessoOk", "Inicio");
		
		try
		{		
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
			
			// Deleta da tabela os registros que já foram processados pelo ETI
			String sql_processo = " DELETE FROM TBL_INT_COMPROVANTE_OUT " +
								  " WHERE IND_PROCESSADO = ? " +
								  " and dat_cadastro < (sysdate - ?) ";
		
			Object paramProcesso[] = {Definicoes.IND_PROCESSADO,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			int numLinhas = conexaoPrep.executaPreparedUpdate(sql_processo,paramProcesso, super.logId);
			super.log(Definicoes.DEBUG, "Produtor.limpaProcessoOk", numLinhas + " registro(s) deletado(s) com sucesso.");
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "Produtor.limpaProcessoOk", "Erro: Nao conseguiu pegar conexao com banco de dados.");
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
			super.log(Definicoes.DEBUG, "Produtor.limpaProcessoOk", "Fim");
		}
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return null;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
}
