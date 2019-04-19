package com.brt.gpp.aplicacoes.planoHibrido.gravarContratoSACRecarga;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.planoHibrido.RecargaRecorrenteConsumidor;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  22/01/2007
 */
public class GravaContratoSACRecargaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private PREPConexao 		conexaoPrep;
	
	/**
	 * Construtor da Classe
	 *
	 */
	public GravaContratoSACRecargaConsumidor()
	{
		super(GerentePoolLog.getInstancia(RecargaRecorrenteConsumidor.class).getIdProcesso(Definicoes.CL_GRAVA_CONTRATO_SAC_RECARGA), Definicoes.CL_GRAVA_CONTRATO_SAC_RECARGA);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicia o processamento do Consumidor
	 * 
	 */
	public void startup() throws Exception
	{
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicia o processamento do Consumidor
	 * 
	 * @param produtor - Instancia da classe Produtor
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicia o processamento do Consumidor
	 * 
	 * @param produtor - Instancia do ProcessoBatchProdutor
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		conexaoPrep = produtor.getConexao();
		startup();
	}
	
	/**
	 * Metodo....: execute
	 * Descricao.: Realiza a execucao das tarefas de atualizacao
	 * 
	 * @param Object - Objeto contendo as informacoes para atualizacao
	 */
	public void execute(Object obj) throws Exception
	{
		GravaContratoSACRecargaVO contratoSACRecarga = (GravaContratoSACRecargaVO) obj;
		
		super.log(Definicoes.DEBUG, "Consumidor.execute", "MSISDN: " + contratoSACRecarga.getMsisdn() + " Contrato: " + contratoSACRecarga.getIdContrato());
		
		String sqlAtualizacao = "UPDATE tbl_rec_recargas a 			" +
								"   SET a.idt_nsu_instituicao = ?	" +
								" WHERE a.idt_msisdn = ? 			" +
								"   AND a.dat_origem = ?			" +
								"   AND a.tip_transacao in ( ?, ? )	" ;
		
		// Seta os parametros a serem utilizados na atualizacao
		Object params[] = { (new Long(contratoSACRecarga.getIdContrato())).toString()+'|'+Definicoes.ID_RECARGA_CRED_INI_ATIVACAO, 
							contratoSACRecarga.getMsisdn(), contratoSACRecarga.getDataOrigem(), Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL, 
							Definicoes.RECARGA_FRANQUIA};
		
		// Execucao da atualizacao no Banco de Dados
		conexaoPrep.executaPreparedUpdate(sqlAtualizacao, params, super.getIdLog());
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Finaliza o Consumidor
	 * 
	 */
	public void finish()
	{
		super.log(Definicoes.INFO, "Consumidor.finish", "Fim dos consumidores");
	}
}