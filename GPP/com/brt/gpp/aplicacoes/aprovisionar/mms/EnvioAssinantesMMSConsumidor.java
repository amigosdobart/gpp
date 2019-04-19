package com.brt.gpp.aplicacoes.aprovisionar.mms;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class EnvioAssinantesMMSConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
    private PREPConexao conexao;
    
    /**
     */
    public EnvioAssinantesMMSConsumidor()
    {
        super(GerentePoolLog.getInstancia(EnvioAssinantesMMSConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_ASSINANTES_MMS), 
                Definicoes.CL_ENVIO_ASSINANTES_MMS);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = produtor;
        startup();
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
    public void startup() throws Exception
    {
        conexao = produtor.getConexao();
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
     */
    public void startup(Produtor produtor) throws Exception
    {
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
    public void execute(Object obj) throws Exception
    {
        EnvioAssinantesMMSVO vo = (EnvioAssinantesMMSVO) obj;
        // montar o xml
		String os = getNumeroOS();
		String msisdn = vo.getMsisdn();
		String categoria = vo.getCategoria();
		String operacao = vo.getOperacao();				
		String sql = "INSERT INTO TBL_INT_PPP_OUT " +
				" (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) VALUES " +
				" (?, SYSDATE, ?, ?, ?)";

		Object[] param = {os, Definicoes.IDT_EVENTO_NEGOCIO_APR_MMS, montarXML(Definicoes.SO_MMS + os, msisdn, categoria, operacao),Definicoes.IND_LINHA_NAO_PROCESSADA};
		conexao.executaPreparedUpdate(sql, param, super.getIdLog());
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
    public void finish()
    {
    }

    /**
	 * Metodo...: getNumeroOS
	 * Descricao: retorna um número de OS para o XML do provision (pega numa sequence do banco de dados)
	 * @return	String	identificador da OS do provision
	 * @throws GPPInternalErrorException
	 */
	private String getNumeroOS() throws GPPInternalErrorException
	{
		String retorno = null;
		ResultSet rsSeq = null;
		
		String sqlSequence = "SELECT SEQ_OS_PROVISION.NEXTVAL AS ID_OS FROM DUAL";
		
		try
		{
			// Retorna o próximo elemento da sequence do Banco de Dados
			rsSeq = conexao.executaQuery(sqlSequence,super.getIdLog());
			rsSeq.next();
			retorno = rsSeq.getString("ID_OS");
			rsSeq.close();
		}
		catch(SQLException sqlException)
		{
			super.log(Definicoes.WARN, "Consumidor.getNumeroOS", "Erro SQL: "+ sqlException);
			throw new GPPInternalErrorException(sqlException.toString());
		}
		return retorno;
	}
	
	/**
	 * Metodo...: montarXML
	 * Descricao: metodo que retorna o XML de aprovisionamento/desaprovisionamento
	 * @param 	os			numero da OS
	 * 			msisdn		MSISDN do assinante
	 * 			categoria	categoria do assinante (F1 = pos-pago, F2 = pre-pago/hibrido)
	 * 			operacao	operacao a ser realizada
	 * @return	String		XML de retorno
	 */
	private String montarXML(String os, String msisdn, String categoria, String operacao)
	{
		GerarXML gerador = new GerarXML("root");
		gerador.adicionaTag("id_os", os);
		gerador.adicionaTag("case_id", os);
		gerador.adicionaTag("order_priority", Definicoes.XML_OS_ORDER_LOW_PRIORITY);
		gerador.adicionaTag("categoria", categoria);
		gerador.adicionaTag("categoria_anterior", categoria);
		gerador.adicionaTag("case_type", "IMEI - MMS");
		gerador.adicionaTag("case_sub_type", operacao);
		gerador.abreNo("provision");
		gerador.abreNo("ELM_INFO_SIMCARD");
		gerador.adicionaTag("macro_servico", "ELM_INFO_SIMCARD");
		gerador.adicionaTag("operacao", "nao_alterado");
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.adicionaTag("x_tipo", "SIMCARD");
		gerador.abreNo("parametros");
		gerador.adicionaTag("simcard_msisdn", msisdn);
		gerador.fechaNo();
		gerador.fechaNo();
		gerador.abreNo("ELM_MMS_IMEI");
		gerador.adicionaTag("macro_servico", "ELM_MMS_IMEI");
		gerador.adicionaTag("operacao", operacao);
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.fechaNo();
		gerador.fechaNo();
		return(gerador.getXML());
	}
}
