package com.brt.gpp.aplicacoes.promocao.incentivoRecarga;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Classe responsavel por validar a concessao do Pula-Pula 
 * do assinante e setar o identificador da bonificacao de
 * incentivo de recargas para nao processado.
 * 
 * @author Joao Paulo Galvagni
 * @since  06/12/2007
 */
public class LiberacaoBonusIncentivoRecarga extends Aplicacoes implements ProcessoBatchProdutor
{
	private String			 statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private PREPConexao		 conexaoPrep;
	private long			 numRegistros = 0;
	
	// SQL para atualizar o status de processamento para as bonificacoes
	// referentes aos incentivos de recarga previamente agendados e 
	// dependentes do bonus pula-pula para concessao
	private final String SQL_LIBERA_BONUS = "UPDATE tbl_rec_fila_recargas f 									" +
						   	  				"   SET f.idt_status_processamento = ? 								" +
						   	  				" WHERE f.dat_execucao  < trunc(sysdate)+1 							" +
						   	  				"   AND f.idt_status_processamento = ? 								" +
						   	  				"   AND EXISTS (SELECT 1 											" +
						   	  				"				  FROM tbl_cam_incentivo_recarga i					" +
						   	  				"				 WHERE i.id_canal || i.id_origem = f.tip_transacao)	" +
						   	  				"   AND EXISTS (SELECT 1 											" +
						   	  				"				  FROM tbl_rec_recargas r 							" +
						   	  				"			     WHERE r.dat_origem    >= ? 						" +
						   	  				"   			   AND r.dat_origem    <  ? 						" +
						   	  				"   			   AND r.tip_transacao =  ? 						" +
						   	  				"   			   AND r.idt_msisdn    =  f.idt_msisdn)				" ;
	
	// SQL para cancelar o bonus de incentivo de recargas para assinantes que nao 
	// receberam o bonus pula-pula
	private final String SQL_CANCELA_BONUS_INCENTIVO = "UPDATE tbl_rec_fila_recargas f 										" +
	     											   "   SET f.idt_status_processamento = ? 								" +
	     											   "      ,f.idt_codigo_retorno = ?										" +
	     											   " WHERE f.dat_execucao  >= ? 										" +
	     											   "   AND f.dat_execucao  <  ? 										" +
	     											   "   AND f.idt_status_processamento = ?								" +
	     											   "   AND EXISTS (SELECT 1 											" +
	     											   "			     FROM tbl_cam_incentivo_recarga i					" +
	     											   "			    WHERE i.id_canal || i.id_origem = f.tip_transacao)	" ;
	
	public LiberacaoBonusIncentivoRecarga(long id)
	{
		super(id,Definicoes.CL_LIBERA_BONUS_INCENTIVO_RECARGA);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception
	{
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		try
		{
			// Realiza o comando para atualizar os registros na fila de recargas
			// para os assinantes que jah receberam o bonus pula pula ou a recarga
			// ou o bonus da franquia do plano controle
			numRegistros = liberarBonusIncentivoRecargaPosPulaPula();
			
			// Cancela todos os agendamentos do mes anterior que nao foram 
			// concedidos devido ao nao recebimento do bonus pula-pula
			log(Definicoes.INFO, "startup", "Quantidade de assinantes com bonus cancelado do mes anterior: " + 
											 cancelarBonusNaoConcedidos());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"startup","Erro ao executar a liberacao do bonus bumerangue. Erro:"+e);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		// O processo consumidor do bonus de incentivo de recargas nao 
		// deve realizar nenhuma acao, somente o produtor
		return null;
	}
	
	/**
	 * Este metodo realiza a liberacao do bonus bumerangue para os assinantes que
	 * jah receberam o bonus pula pula ou jah fizeram a recarga ou receberam o bonus
	 * pula pula do plano controle
	 *            
	 * @return int - Numero de linhas atualizadas
	 * @throws Exception
	 */
	private int liberarBonusIncentivoRecargaPosPulaPula() throws Exception
	{
		// Os parametros para a busca na fila de recargas eh o primeiro dia do mes
		// sendo que o processo de concessao sempre agenda esse dia sem considerar
		// a hora afim de melhorar o acesso (performance) dessas atualizacoes
		Object params[] = {new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA)
						  ,new Integer(Definicoes.STATUS_RECARGA_AGENDAMENTO_INCENTIVO)
				          ,new java.sql.Date(primeiroDiaMesAtual().getTime())
				          ,new java.sql.Date(primeiroDiaMesSeguinte().getTime())
				          ,Definicoes.AJUSTE_BONUS_PULA_PULA
						  };
		
		return conexaoPrep.executaPreparedUpdate(SQL_LIBERA_BONUS,params,super.getIdLog());
	}
	
	/**
	 * Realiza o cancelamento de todos os agendamentos do mes
	 * anterior que nao foram concedidos, por nao haver concessao
	 * do bonus pula-pula no mes para o assinante
	 * 
	 * @return int		 - Quantidade de assinantes atualizados
	 * @throws Exception
	 */
	private int cancelarBonusNaoConcedidos() throws Exception
	{
		Object params[] = {new Integer(Definicoes.STATUS_RECARGA_COM_ERRO)
						  ,new Integer(Definicoes.RET_BONUS_PP_NAO_CONCEDIDO)
						  ,new java.sql.Date(primeiroDiaMesAnterior().getTime())
						  ,new java.sql.Date(primeiroDiaMesAtual().getTime())
						  ,new Integer(Definicoes.STATUS_RECARGA_AGENDAMENTO_INCENTIVO)
						  };
		
		return conexaoPrep.executaPreparedUpdate(SQL_CANCELA_BONUS_INCENTIVO, params, getIdLog());
	}
	
	/**
	 * Retorna o primeiro dia para pesquisa de registros na fila
	 * 
	 * @return Timestamp - Primeiro dia a ser utilizado na pesquisa de registros na fila
	 */
	private Date primeiroDiaMesAtual()
	{
		Calendar data = Calendar.getInstance();
		data.set(Calendar.DAY_OF_MONTH,data.getActualMinimum(Calendar.DAY_OF_MONTH));
		data.set(Calendar.HOUR_OF_DAY, 0);
		data.set(Calendar.MINUTE		, 0);
		data.set(Calendar.SECOND		, 0);
		data.set(Calendar.MILLISECOND, 0);
		
		return data.getTime();
	}
	
	/**
	 * Retorna o primeiro dia do mes anterior
	 * 
	 * @return Timestamp - Primeiro dia do mes anterior
	 */
	private Date primeiroDiaMesAnterior()
	{
		Calendar data = Calendar.getInstance();
		
		data.setTime(primeiroDiaMesAtual());
		data.add(Calendar.MONTH, -1);
		
		return data.getTime();
	}
	
	/**
	 * Retorna o ultimo dia para pesquisa de registros na fila
	 * 
	 * @return Timestamp - Ultimo dia a ser utilizado na pesquisa de registros na fila
	 */
	private Date primeiroDiaMesSeguinte()
	{
		// Como a pesquisa nao utiliza hora entao o ultimo dia eh o primeiro
		// dia do mes seguinte, portanto no ultimo dia do mes atualiza mais um dia
		Calendar data = Calendar.getInstance();
		data.setTime(primeiroDiaMesAtual());
		data.add(Calendar.MONTH, 1);
		
		return data.getTime();
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_LIBERA_BONUS_INCENTIVO_RECARGA;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return "Bonus de incentivo de recargas liberado para "+numRegistros+" assinantes";
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
		return new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(Calendar.getInstance().getTime());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
	 */
	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		log(Definicoes.INFO, "finish", getDescricaoProcesso());
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());		
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
	}
}