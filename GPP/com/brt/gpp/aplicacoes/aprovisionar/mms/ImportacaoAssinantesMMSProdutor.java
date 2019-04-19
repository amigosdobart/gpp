package com.brt.gpp.aplicacoes.aprovisionar.mms;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

public class ImportacaoAssinantesMMSProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    private String status = Definicoes.TIPO_OPER_SUCESSO;
    private PREPConexao conexaoPrep;
    private Timestamp dataExecucao;
    private String dataProcessamento;
    ResultSet rsAssinantes;
    private long numRegistros;
    private String mascara;
    
    /**
     * @param aLogId
     */
    public ImportacaoAssinantesMMSProdutor(long aLogId)
    {
        super(aLogId, Definicoes.CL_IMPORTACAO_ASSINANTES_MMS);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
    public int getIdProcessoBatch()
    {
        return Definicoes.IND_IMPORTACAO_ASSINANTES_MMS;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
    {
        if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return numRegistros + " Assinantes Importados";
        
        return "Erro ao Importar Assinantes do HSID";
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
    public String getStatusProcesso()
    {
        return status;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
     */
    public void setStatusProcesso(String status)
    {
        this.status = status;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
    	return dataProcessamento;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws Exception
    {
        parseParametros(params);
        super.log(Definicoes.INFO, "Produtor.startup", "Inicio da importacao de Assinantes da base do HSID");
        conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
        conexaoPrep.setAutoCommit(false);
        
        // Seleciona a ultima data na qual o processo foi executado com sucesso
		String sql = " SELECT max(dat_processamento) AS data_execucao " +
						" FROM tbl_ger_historico_proc_batch " + 
						" WHERE id_processo_batch = ? " +
						" AND idt_status_execucao = ? ";
		Object[] paramsDataExecucao = {new Integer(Definicoes.IND_IMPORTACAO_ASSINANTES_MMS), Definicoes.PROCESSO_SUCESSO};
		ResultSet rsDataInclusao = conexaoPrep.executaPreparedQuery(sql, paramsDataExecucao, super.getIdLog());
		
		if(rsDataInclusao.next())
		{
			dataExecucao = rsDataInclusao.getTimestamp("data_execucao");
			rsDataInclusao.close();
		}
		
		rsAssinantes = verificarAssinantes(conexaoPrep, dataExecucao, dataProcessamento);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws Exception
    {
        ImportacaoAssinantesMMSVO vo = null;
        if(rsAssinantes.next())
        {
            vo = new ImportacaoAssinantesMMSVO();
            vo.setMsisdn(rsAssinantes.getString("msisdn"));
            vo.setDataInclusao(rsAssinantes.getTimestamp("datainclusao"));
            vo.setModelo(rsAssinantes.getString("ind_mms"));
            numRegistros++;
        }
        return vo;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
    {
        rsAssinantes.close();
        conexaoPrep.commit();
        super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
        super.log(Definicoes.INFO, "Produtor.finish", "Fim da importacao de assinantes da base do HSID");
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
        try
        {
            conexaoPrep.rollback();
        }
        catch(SQLException sqlException)
        {
            super.log(Definicoes.DEBUG, "Produtor.handleException", "Erro ao executar rollback: " + sqlException);
        }
    }
    
    /**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws Exception
	{
		if(params != null && params.length > 0)
		{
			mascara = params[0].length() == 10 ? Definicoes.MASCARA_DATE:Definicoes.MASCARA_DATA_HORA_GPP;
		    SimpleDateFormat sdf = new SimpleDateFormat(mascara);
		    
			try
			{
				sdf.parse(params[0]);
				dataProcessamento = params[0].length() == 10 ? params[0]:GPPData.formataStringData(params[0]);
			}
			catch(ParseException pe)
			{
			    super.log(Definicoes.ERRO, "Produtor.parseParametros", "Data invalida ou esta no formato invalido. Valor: "+params[0]);
				throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor: "+params[0]);
			}
		}		
	}
    
    /**
	 * Metodo...: verificarAssinantes
	 * Descricao: retorna um objeto do tipo ResulSet que contem informacoes sobre os usuarios que subiram TSD
	 * desde a ultima execucao do processo
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			dataInclusao	data da ultima execucao do processo
	 * @return	ResultSet		objeto que contem todos as informacoes retornadas pela consulta
	 * @throws GPPInternalErrorException
	 */	
	private ResultSet verificarAssinantes(PREPConexao conexao, Timestamp dataInclusao, String aDataFinalParam) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "Produtor.verificarAssinantes", "Inicio da verificacao de assinantes que subiram TSD");
		ResultSet resultado = null;
		try
		{
			if(aDataFinalParam.equals(""))
			{
				aDataFinalParam = GPPData.getDataAcrescidaDias("1");
			}			
			
			// seleciona todos o MSISDN, a data de inclusao e a capacidade do aparelho
			// para todos os assinantes que subiram TSD desde a ultima execucao do processo
			String sql =	"SELECT cl.nu_msisdn AS msisdn ,dt_inclusao AS datainclusao " +
							" 		,decode(co_modelo,0,?, " +
							"			decode( " +
							"				(SELECT count(1) " +
							" 					FROM hsid.hsid_modelo_capacidade cap " +
							" 					WHERE cap.co_modelo = cl.co_modelo " +
							" 					AND cap.co_capacidade = ? " + 
							" 					AND upper(cap.ds_caracteristica) LIKE 'SIM%' " +
							" 				), 0, ?, ?)) AS ind_mms " +
							" FROM hsid.hsid_cliente cl " +
							" WHERE dt_inclusao > ? " +
							" AND dt_inclusao <= to_date(?,'dd/mm/yyyy hh24:mi:ss') " +
							" ORDER BY dt_inclusao ";
			
			
			Object[] params = {Definicoes.MMS_MODELO_DESCONHECIDO, Definicoes.MMS_CODIGO_CAPACIDADE, Definicoes.MMS_MODELO_NAO_COMPATIVEL, Definicoes.MMS_MODELO_COMPATIVEL, dataInclusao, aDataFinalParam};
			resultado = conexao.executaPreparedQuery(sql, params, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "Produtor.verificaAssinantes", "Erro SQL: " + e);
			throw new GPPInternalErrorException(e.toString());
		}
		super.log(Definicoes.DEBUG, "Produtor.verificarAssinantes", "Fim da verificacao de assinantes que subiram TSD");
		return(resultado);
	}
}