package com.brt.gpp.aplicacoes.enviarBonusCSP14;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Classe responsavel por liberar o Bumerangue 14 para os 
 * assinantes do plano controle que atendam aos requisitos
 * 
 * @author	Joao Paulo Galvagni Junior
 * @since	29/03/2008
 * @modify	Atualizacao do metodo liberarBumerangueComPulaPula() para
 * 			adequacao a Promocao do Dia das Maes 2008
 *
 */
public class LiberacaoBumerangueProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private PREPConexao	conexaoPrep;
	private long		numRegistros = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public LiberacaoBumerangueProdutor(long id)
	{
		super(id,Definicoes.CL_LIBERA_BONUS_CSP14);
	}
	
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_LIBERA_BONUS_CSP14;
	}
	
	public String getDescricaoProcesso()
	{
		return "Bonus liberado para "+numRegistros+" assinantes";
	}
	
	public String getStatusProcesso()
	{
		return statusProcesso;
	}
	
	public void setStatusProcesso(String status)
	{
		statusProcesso = status;
	}
	
	public String getDataProcessamento()
	{
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception
	{
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		conexaoPrep.setAutoCommit(false);
		
		try
		{
			// Realiza o comando para atualizar os registros na fila de recargas
			// para os assinantes que jah receberam o bonus pula pula ou a recarga
			// ou o bonus da franquia do plano controle
			numRegistros += liberarBumerangueComPulaPula();
			
			// Realiza o comando para atualizar os registros na fila de recargas
			// para os assinantes da promocao pula pula que nao receberam o bonus
			// e que hoje eh o ultimo dia da promocao, ou seja, nao receberao mais
			// o bonus e portanto o bumerangue deve ser liberado
			numRegistros += liberarBumerangueUltimoDiaPromocao();
			
			conexaoPrep.commit();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"startup","Erro ao executar a liberacao do bonus bumerangue. Erro:"+e);
			conexaoPrep.rollback();
		}
	}
	
	/**
	 * Metodo....:liberarBumerangueComPulaPula
	 * Descricao.:Este metodo realiza a liberacao do bonus bumerangue para os assinantes que
	 *            jah receberam o bonus pula pula ou jah fizeram a recarga ou receberam o bonus
	 *            pula pula do plano controle
	 * 
	 * @author	Joao Paulo Galvagni
	 * @since	29/03/2008
	 * @modify	Inclusao dos novos tipos de transacao referentes a Promocao
	 * 			do Dia das Maes 2008 (Bonus On-Net, Off-Net e de Recarga)
	 * 			Parametros: 10, 11 e 12
	 * 
	 * @return int - Numero de linhas atualizadas
	 * @throws Exception
	 */
	private int liberarBumerangueComPulaPula() throws Exception
	{
		String sql = "update tbl_rec_fila_recargas f 								" + // Parametros
				   	 "   set f.idt_status_processamento = ? 						" + // 01
				   	 " where f.dat_execucao  = ?									" + // 02
				   	 "   and f.tip_transacao = ? 									" + // 03
				   	 "   and f.idt_status_processamento = ? 						" + // 04
				   	 "   and exists (select 1 										" +
				     "				   from tbl_rec_recargas r 						" +
				     "				  where r.dat_origem >= ? 						" + // 05
				     "					and r.dat_origem <  ? 						" + // 06
				     "					and r.tip_transacao in (?, ?, ?, ?, ?, ?)	" + // 07, 08, 09, 10, 11, 12
				     "					and r.idt_msisdn = f.idt_msisdn) 			" +
				     "   and exists (select 1 										" +
				     "				   from tbl_pro_assinante p 					" +
				     "				  where p.idt_msisdn = f.idt_msisdn 			" +
				     "					and p.idt_promocao = ?)						" ; // 13
		
		// Os parametros para a busca na fila de recargas eh o primeiro dia do mes
		// sendo que o processo de concessao sempre agenda esse dia sem considerar
		// a hora afimd e melhorar o acesso (performance) dessas atualizacoes
		Object params[] = {new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA)		// 01
				          ,new java.sql.Date(primeiroDiaPesquisa().getTime())			// 02
				          ,Definicoes.RECARGA_BONUS_CSP14								// 03
				          ,new Integer(Definicoes.STATUS_RECARGA_AGENDAMENTO_BUMERANGUE)// 04
				          ,new java.sql.Date(primeiroDiaPesquisa().getTime())			// 05
				          ,new java.sql.Date(ultimoDiaPesquisa().getTime())				// 06
				          ,Definicoes.AJUSTE_BONUS_PULA_PULA							// 07
				          ,Definicoes.RECARGA_FRANQUIA_BONUS							// 08
				          ,Definicoes.RECARGA_FRANQUIA									// 09
				          ,Definicoes.AJUSTE_BONUS_RECARGA								// 10
				          ,Definicoes.AJUSTE_BONUS_ON_NET								// 11
				          ,Definicoes.AJUSTE_BONUS_OFF_NET								// 12
				          ,new Integer(Definicoes.PROMOCAO_BUMERANGUE14)				// 13
						  };
		
		return conexaoPrep.executaPreparedUpdate(sql,params,super.getIdLog());
	}
	
	/**
	 * Metodo....:liberarBumerangueUltimoDiaPromocao
	 * Descricao.:Liberar o bonus bumerangue para os assinantes que nao receberam
	 *            o bonus pula pula sendo que hoje eh o ultimo dia da promocao desses
	 * @return int - Numero de linhas atualizadas
	 * @throws Exception
	 */
	private int liberarBumerangueUltimoDiaPromocao() throws Exception
	{
		String sql = "update tbl_rec_fila_recargas f 													" +
					 "   set f.idt_status_processamento = ? 											" + // 01
					 " where f.dat_execucao  = ?														" + // 02
					 "   and f.tip_transacao = ?														" + // 03
					 "   and f.idt_status_processamento = ?												" + // 04
					 "   and exists ( select 1 															" +
					 "					from tbl_pro_assinante a 										" +
					 "						,(select idt_promocao 										" +
					 "								,max(num_dia_execucao_recarga) as num_dia_execucao	" +
					 "							from tbl_pro_dia_execucao								" +
					 "						   where tip_execucao = ?									" + // 05
					 "						   group by idt_promocao) d									" +
					 "				   where a.idt_promocao = d.idt_promocao							" +
					 "					 and to_char(sysdate,'dd') >= d.num_dia_execucao				" +
					 "					 and a.idt_msisdn   = f.idt_msisdn )							" +
					 "   and exists (select 1 															" +
					 "				   from tbl_pro_assinante p											" +
					 "				  where p.idt_msisdn = f.idt_msisdn									" +
					 "					and p.idt_promocao = ?)											" ; // 06
		
		Object param[] = {new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA)			// 01
				         ,new java.sql.Date(primeiroDiaPesquisa().getTime())			// 02
				         ,Definicoes.RECARGA_BONUS_CSP14								// 03
				         ,new Integer(Definicoes.STATUS_RECARGA_AGENDAMENTO_BUMERANGUE)	// 04
				         ,Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA				// 05
				         ,new Integer(Definicoes.PROMOCAO_BUMERANGUE14)					// 06
				         };
		
		return conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
	}
	
	/**
	 * Metodo....:primeiroDiaPesquisa
	 * Descricao.:Retorna o primeiro dia para pesquisa de registros na fila
	 * @return Timestamp - Primeiro dia a ser utilizado na pesquisa de registros na fila
	 */
	public Date primeiroDiaPesquisa()
	{
		Calendar primeiroDia = Calendar.getInstance();
		primeiroDia.set(Calendar.DAY_OF_MONTH,primeiroDia.getActualMinimum(Calendar.DAY_OF_MONTH));
		primeiroDia.set(Calendar.HOUR_OF_DAY,0);
		primeiroDia.set(Calendar.MINUTE,0);
		primeiroDia.set(Calendar.SECOND,0);
		primeiroDia.set(Calendar.MILLISECOND,0);
		
		return primeiroDia.getTime();
	}
	
	/**
	 * Metodo....:ultimoDiaPesquisa
	 * Descricao.:Retorna o ultimo dia para pesquisa de registros na fila
	 * @return Timestamp - Ultimo dia a ser utilizado na pesquisa de registros na fila
	 */
	public Date ultimoDiaPesquisa()
	{
		// Como a pesquisa nao utiliza hora entao o ultimo dia eh o primeiro
		// dia do mes seguinte, portanto no ultimo dia do mes atualiza mais um dia
		Calendar dia = Calendar.getInstance();
		dia.setTime(primeiroDiaPesquisa());
		dia.add(Calendar.MONTH,1);

		return dia.getTime();
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		// O processo consumidor do bonus bumerangue nao deve realizar nenhuma
		// acao, somente o produtor
		return null;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		conexaoPrep.setAutoCommit(true);
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());		
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException()
	{
	}
}