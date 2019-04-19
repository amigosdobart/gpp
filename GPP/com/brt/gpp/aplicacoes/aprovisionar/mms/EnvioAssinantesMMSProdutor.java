package com.brt.gpp.aplicacoes.aprovisionar.mms;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

public class EnvioAssinantesMMSProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    private PREPConexao conexao;
    private String status = Definicoes.TIPO_OPER_SUCESSO;
    ResultSet rsAprovisionamento;
    private long numRegistros;
    
    /**
     * @param aLogId
     */
    public EnvioAssinantesMMSProdutor(long aLogId)
    {
        super(aLogId, Definicoes.CL_ENVIO_ASSINANTES_MMS);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
    public int getIdProcessoBatch()
    {
        return Definicoes.IND_APROVISIONAMENTO_MMS;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
    {
        if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return numRegistros + " assinantes enviados";
        else
            return "Erro ao enviar assinantes para o Vitria";
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
        return null;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexao;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws Exception
    {
        super.log(Definicoes.INFO, "Produtor.startup", "Inicio do envio de assinantes para o Vitria");
        conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
        conexao.setAutoCommit(false);
		ResultSet rsAssinantes = verificarModelosDesconhecidos(conexao);
		while(rsAssinantes.next())
		{
			String rowid = rsAssinantes.getString("id");
			String statusRegistro = rsAssinantes.getString("status");
			String tipOperacao = rsAssinantes.getString("tip_comando");
			atualizarAssinante(conexao, rowid, tipOperacao, statusRegistro);
		}
		rsAssinantes.close();
		
		// seleciona as informacoes (MSISDN, categoria e tipo de operacao) dos assinantes que
		// serao aprovisionados/desaprovisionados
		String sql = 	"SELECT mms.idt_msisdn AS msisdn, mms.tip_operacao AS tip_operacao, " +
							"		decode(ass.idt_msisdn, null, ?, ?) AS categoria " +
							" FROM tbl_apr_mms mms, tbl_apr_assinante ass " +
							" WHERE mms.idt_msisdn = ass.idt_msisdn (+) " +
							" AND mms.idt_status_registro <> ? " +
							" AND mms.tip_operacao IS NOT NULL";

		Object[] param = {Definicoes.XML_OS_CATEGORIA_POSPAGO, Definicoes.XML_OS_CATEGORIA_PREPAGO, Definicoes.MMS_REGISTRO_ENVIADO};
		rsAprovisionamento = conexao.executaPreparedQuery(sql, param, super.getIdLog());
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws Exception
    {
        EnvioAssinantesMMSVO vo = null;
        if(rsAprovisionamento.next())
        {
            vo = new EnvioAssinantesMMSVO();
            vo.setMsisdn(rsAprovisionamento.getString("msisdn"));
            vo.setCategoria(rsAprovisionamento.getString("categoria"));
            vo.setOperacao(rsAprovisionamento.getString("tip_operacao"));
            numRegistros++;
        }
        return vo;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
    {
        rsAprovisionamento.close();
        // atualiza os registros que foram enviados, alterando o campo idt_status_regitro para E
		String sql = " UPDATE tbl_apr_mms " +
				" SET idt_status_registro = ? " +
				" WHERE idt_status_registro = ? " +
				" AND tip_operacao is not NULL ";
		Object[] params1 = {Definicoes.MMS_REGISTRO_ENVIADO, Definicoes.MMS_REGISTRO_CONCLUIDO};
		conexao.executaPreparedUpdate(sql, params1, super.getIdLog());
        conexao.commit();
        super.gerenteBancoDados.liberaConexaoPREP(conexao, super.getIdLog());
        super.log(Definicoes.INFO, "Produtor.finish", "Fim do envio de assinantes para o Vitria");
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
        try
        {
            conexao.rollback();
        }
        catch(SQLException sqlException)
        {
            super.log(Definicoes.DEBUG, "Produtor.handleException", "Erro ao executar rollback: " + sqlException);
        }
    }

    /**
	 * Metodo...: verificarAssinantes
	 * Descricao: retorna um objeto do tipo ResulSet que contem informacoes sobre os usuarios cujos modelos
	 * foram cadastrados na base do HSID
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * @return	ResultSet		objeto que contem todos as informacoes retornadas pela consulta
	 * @throws GPPInternalErrorException
	 */	
	private ResultSet verificarModelosDesconhecidos(PREPConexao conexao) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG, "Produtor.verificarModelosDesconhecidos", "Inicio da verificacao de modelos desconhecidos");
		ResultSet resultado = null;
		try
		{
			// seleciona informacoes (rowid do registro, tipo do comando a ser enviado, novo status do registro) 
			// sobre os assinantes que foram inseridos na tabela TBL_APR_MMS como D e que tiveram o modelo cadastrado
			// na base do HSID
			String sql = 	" SELECT id, status, decode(status, ?, ?, null) as tip_comando " +
							" FROM ( " +
							" 			SELECT " +
                            "        			mms.rowid as id, " +
                            "					decode(" +
                            "							(" +
                            "								select count(1) " +
                            " 									FROM hsid.hsid_modelo_capacidade mod " +
                            "	 								WHERE mod.co_modelo = tac.co_modelo " +
                            " 									AND mod.co_capacidade = ? " + 
                            " 									AND upper(mod.ds_caracteristica) like 'SIM%'" +
                            "							), 0, ?, ?) as status " +
							" 				FROM tbl_apr_mms mms, hsid.hsid_cliente cli, hsid.hsid_modelo_tac tac " +
                            " 				WHERE mms.idt_msisdn = cli.nu_msisdn " +
							" 				AND tac.co_tac = to_number(substr(cli.nu_imei,1,6)) " +
							" 				AND mms.idt_status_registro = ? " +
							"	    )";
				
			Object[] params={Definicoes.MMS_REGISTRO_CONCLUIDO, Definicoes.XML_OS_DESBLOQUEAR, new Integer(Definicoes.MMS_CODIGO_CAPACIDADE), 
								Definicoes.MMS_REGISTRO_ENVIADO, Definicoes.MMS_REGISTRO_CONCLUIDO, Definicoes.MMS_MODELO_DESCONHECIDO};
			
			resultado = conexao.executaPreparedQuery(sql, params, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "Produtor.verificarModelosDesconhecidos", "Erro SQL: " + e);
			throw new GPPInternalErrorException(e.toString());
		}
		super.log(Definicoes.DEBUG, "Produtor.verificarModelosDesconhecidos", "Fim da verificacao de modelos desconhecidos");
		return(resultado);
	}
	
	/**
	 * Metodo...: atualizarAssinante
	 * Descricao: atualiza um registro na tabela TBL_APR_MMS
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			rowid			campo rowid do registro a ser atualizado
	 * 			operacao		comando que sera enviado a plataforma de MMS (Adicionar/Retirar/null)
	 * 			statusRegistro	novo status do registro (C = Registro Concluido / D = MSISDN Desconhecido/ E = Registro Enviado)
	 * @return	int				indica o sucesso ou erro da atualizacao
	 * @throws GPPInternalErrorException
	 */
	private int atualizarAssinante(PREPConexao conexao, String rowid, String operacao, String statusRegistro) throws GPPInternalErrorException
	{
		int resultado = 0;
		String sql = null;
		try
		{
			sql = "UPDATE tbl_apr_mms " +
					" SET tip_operacao = ?, idt_status_registro = ? " +
					" WHERE rowid = ? "; 
			Object[] params = {operacao, statusRegistro, rowid};
			resultado = conexao.executaPreparedUpdate(sql, params, super.getIdLog());				
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "Produtor.atualizarAssinante", "Erro SQL: "+ e);
			throw new GPPInternalErrorException(e.toString());
		}
		return(resultado);
	}
}
