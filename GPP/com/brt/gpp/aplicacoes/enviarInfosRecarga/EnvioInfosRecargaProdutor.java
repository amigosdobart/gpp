package com.brt.gpp.aplicacoes.enviarInfosRecarga;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*
* Este arquivo refere-se a classe EnvioInfosRecarga, responsavel pela implementacao da
* logica de envio de informacoes de recarga para Irmao 14
*
* <P> Versao:			1.0
*
* @Autor: 			Vanessa Andrade
* Data: 				25/03/2004
*
* Modificado por:	Gustavo Gusmao
* Data:				10/10/2005
* Razao:			Remodelagem produtor-consumidor
*
*/

public class EnvioInfosRecargaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    private String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
    private String dataProcessamento;
    private PREPConexao conexaoPrep;
    
    public EnvioInfosRecargaProdutor(long aLogId)
    {
        super(aLogId, Definicoes.CL_ENVIO_INFOS_RECARGA);
    }
    
    public void startup(String[] params) throws Exception
	{
        super.log(Definicoes.DEBUG, "Produtor.Startup", "Inicio do Envio de Info de Recarga");
        parseParametros(params);
		conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		enviaInfosRecarga(dataProcessamento);
		limpaRegistrosProcessados();
	}

	public Object next() throws Exception
	{
		return null;
	}

	public void finish() throws Exception
	{
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		super.log(Definicoes.DEBUG, "Produtor.Finish", "Fim do Envio de Info de Recarga");
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
    }
	
	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws Exception
	{
		// Para o processo batch de envio de usuarios em shutdown o unico
		// parametro utilizado eh a data de processamento. Portanto a verificacao
		// feita nos parametros sao em relacao a esta data sendo que basta ser
		// uma data valida
		if(params == null || params.length == 0 || params[0] == null)
		{
		    super.log(Definicoes.ERRO, "Produtor.parseParametros", "Parametro de data obrigatorio para o processo.");
		    setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
		    throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pException)
		{
		    super.log(Definicoes.ERRO, "Produtor.parseParametros", "Data invalida ou esta no formato invalido.");
		    this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor:"+params[0]);
		}
	}
	
	private void enviaInfosRecarga(String aData) throws GPPInternalErrorException, GPPTecnomenException
	{
		super.log(Definicoes.INFO, "Produtor.enviaInfosRecarga", "Inicio DATA: " + aData);				
		try
		{
		    short periodo = getPeriodoRecarga(conexaoPrep);
			// Faz a pesquisa no banco para saber se o campo fez_recarga esta nulo e eh diferente dos possiveis valores
			String sql =  	"UPDATE tbl_int_eti_recarga_irmao14 eti " +
									" SET (eti.ind_fez_recarga, eti.idt_status_processamento) = " + 
									" 		(SELECT DECODE(NVL(COUNT(1), 0), 0, ?, ?), ? " +
									" 			FROM tbl_rec_recargas rec " +
						            " 			WHERE rec.idt_msisdn = eti.idt_msisdn " +
						            " 			AND rec.id_tipo_recarga = ? " +
						            " 			AND rec.dat_origem >= eti.DAT_CORTE - ? " +
						            " 			AND rec.dat_origem < eti.DAT_CORTE + 1 " +
	          						" 		) " +
	          						" WHERE eti.idt_status_processamento = ? ";
			Object[] param_sql = {new Integer(Definicoes.TIPO_IND_FEZ_RECARGA_NOK), new Integer(Definicoes.TIPO_IND_FEZ_RECARGA_OK), Definicoes.IDT_IRMAO14_RECARREGADO, 
			        Definicoes.TIPO_RECARGA, new Integer(periodo), Definicoes.IDT_IRMAO14_GRAVADO};
			int linhasProcessadas = conexaoPrep.executaPreparedUpdate(sql, param_sql, super.getIdLog());
			super.log(Definicoes.INFO, "Produtor.enviaInfosRecarga", linhasProcessadas + " registro(s) processado(s).");
		}		
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "Produtor.enviaInfosRecarga", "Excecao Interna do GPP: " + e);
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
			super.log(Definicoes.INFO, "Produtor.enviaInfosRecarga", "Fim");
		}
	}
	
	/**
	 * Metodo...: limpaRegistrosProcessados
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os usuario que ja foram processados pelo ETI
	 * @param
	 * @return	void									
	 * @throws GPPInternalErrorException
	 */
	private void limpaRegistrosProcessados () throws GPPInternalErrorException
	{

		super.log(Definicoes.DEBUG, "Produtor.limpaRegistrosProcessados", "Inicio");
		try
		{
			String idt_irmao14_processado = Definicoes.IDT_IRMAO14_PROCESSADO;
			
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Deleta da tabela os registros processados com sucesso
			String sql_processo = "DELETE FROM TBL_INT_ETI_RECARGA_IRMAO14 " +
								  " WHERE IDT_STATUS_PROCESSAMENTO = ? " +
								  " AND DAT_REGISTRO < (sysdate - ?)";
			Object paramProcesso[] = {idt_irmao14_processado,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			int numRegistrosProcessados = conexaoPrep.executaPreparedUpdate(sql_processo,paramProcesso, super.logId);
			super.log(Definicoes.DEBUG, "Produtor.limpaRegistrosProcessados", numRegistrosProcessados + " registro(s) processado(s) deletado(s).");
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "Produtor.limpaRegistrosProcessados", "Excecao Interna do GPP: " + e);
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
			super.log(Definicoes.DEBUG, "Produtor.limpaRegistrosProcessados", "Fim");
		}
	}
	
	/**
	 * Metodo...: getPeriodoRecarga
	 * Descricao: Verifica qual o periodo para poder aplicar a recarga dos dados
	 * @param 	conexaoPrep	- Conexao com o Banco de Dados
	 * @return	short		- Valor do periodo 									
	 * @throws 	GPPInternalErrorException
	 */
	private short getPeriodoRecarga(PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		short retorno = 0;
		super.log(Definicoes.DEBUG, "Produtor.getPeriodoRecarga", "Inicio");
		try
		{
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			retorno = Short.parseShort(confGPP.getMapValorConfiguracaoGPP(Definicoes.PERIODO_RECARGA_IRMAO14));
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "Produtor.getPeriodoRecarga", "Excecao SQL: " + e);
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException (e.toString());				
		}
		finally
		{
			super.log(Definicoes.DEBUG, "Produtor.getPeriodoRecarga", "Fim");
		}
		return retorno;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
    public int getIdProcessoBatch()
    {
        return Definicoes.IND_INFOS_RECARGA;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
    {
        if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return "Informacoes de recarga enviadas com sucesso";
	    else
	        return "Nao foi possivel enviar informacoes de recarga";
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
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return(dataProcessamento);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return null;
    }
}