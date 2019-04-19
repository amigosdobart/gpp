package com.brt.gpp.aplicacoes.notificacaoExpiracao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
  *
  * Este arquivo refere-se a classe NotificacaoExpiracao, responsavel por notificar via mensagem
  * SMS assinantes cuja data de expiração dos saldos está proxima
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Marcos Castelo Magalhães
  * Data: 				04/08/2005
  *
  * Modificado por:		Magno Batista Corrêa
  * Data:				2006/09/06
  * Razao:				Evolução para novas tags de modificação; Corrigir select de assinantes
  *
  * Modificado por:		Vitor Murilo Rodrigues Marcelino Dias
  * Data: 				2007/06/29
  * Razao:				Evolução para o modelo Produtor/Consumidor
  *
  */

public class NotificacaoExpiracaoProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parâmetros necessarios para o processo batch
	private String				statusProcesso = Definicoes.TIPO_OPER_SUCESSO; // Guarda o tipo de operação
	private String				dataInicial = GPPData.dataCompletaForamtada(); // Guarda a data inicial do processamento
	private int					contador = 0;  // Variável estática que guarda a quantidade de registros
	private boolean 			podeProcessar; // Flag que indica se o processo pode continuar ou não
	private PREPConexao			conexaoPrep;
	private ResultSet			rs;
	private MapConfiguracaoGPP	mapConfig = null;

	
	/**
	 * Metodo....: EnvioUsuariosShutdownProdutor
	 * Descricao.: Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public NotificacaoExpiracaoProdutor(long aLogId) 
	{
		
		super(aLogId, Definicoes.CL_NOTIFICACAO_EXPIRACAO);
		
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "NotificacaoExpiracao",
						"Problemas Mapeamento das Configurações GPP");
		}// Fim do try
		
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "NotificacaoExpiracao", 
					"Problemas Mapeamento das Configurações GPP");
		}// Fim do catch
		
	}// Fim da método construtor 
		
	/**
	 * Metodo....: startup
	 * Descricao.: Este metodo busca uma conexão para o processo e pega as informações necessárias ao processamento no banco de dados
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws Exception
	 */
	public void startup(String[] params) throws Exception 
	{
		
		super.log(Definicoes.DEBUG,"GravaNotificacaoExpiracao",
				"Inicio do método GravaNotificacaoExpiracao");
		
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		
		// Chama o método foiProcessado() que verifica se o 
		// processo batch já foi processado com sucesso no mesmo dia
		if ( !foiProcessado() )
		{
			
			String sql = " SELECT DISTINCT *" +
			" FROM (SELECT          /*+USE_NL(A, PA)*/" +
			" DISTINCT ex.idt_status_assinante, ex.num_dias," +
			" a.dat_expiracao_principal, a.idt_msisdn," +
			" ex.idt_promocao promo_notif," +
			" ex.des_mensagem," +
			" ex.num_prioridade, a.vlr_saldo_principal," +
			" a.vlr_saldo_bonus, a.vlr_saldo_sm, a.vlr_saldo_dados," +
			" RANK () OVER (PARTITION BY a.idt_msisdn ORDER BY ex.idt_promocao) AS ordem" +
			" FROM tbl_ger_notificacao_expiracao ex," +
			" tbl_apr_assinante a, tbl_pro_assinante pa," +
			" tbl_pro_promocao p, tbl_ger_plano_preco pl" +
			" WHERE a.dat_expiracao_principal >= TRUNC (SYSDATE) + ex.num_dias" +
			" AND a.dat_expiracao_principal < TRUNC (SYSDATE) + ex.num_dias + 1" +
			" AND ex.idt_status_assinante = a.idt_status" +
			" AND pa.idt_msisdn = a.idt_msisdn" +
			" AND (ex.idt_promocao = pa.idt_promocao OR ex.idt_promocao IS NULL)" +
			" AND pl.idt_plano_preco = a.idt_plano_preco" +
			" AND pl.idt_categoria = ?)" +
			" WHERE ordem = 1";
			
			Object parametros[] = {new Integer(Definicoes.CATEGORIA_PREPAGO)};
			
			//	verifica quais msisdns estao com saldo a expirar
			rs = conexaoPrep.executaPreparedQuery(sql, parametros, super.getIdLog());
			
		}// Fim do if
								
	}// Fim do método startup
	
	
	public Object next()
	{
		
		NotificacaoExpiracaoVO notifExpiracao = null;
				
		try 
		{
			if (podeProcessar && rs.next())
			{
				
				notifExpiracao = new NotificacaoExpiracaoVO();
				notifExpiracao.setDataExpiracaoPrincipal(rs.getDate("dat_expiracao_principal"));
				notifExpiracao.setMsisdn(rs.getString("idt_msisdn"));
				notifExpiracao.setNotificacaoSMS(rs.getString("des_mensagem"));
				notifExpiracao.setPrioridade(rs.getInt("num_prioridade"));
				notifExpiracao.setSmsStatus(Definicoes.SMS_INF_STATUS);
				notifExpiracao.setNDias(rs.getString("num_dias"));
				notifExpiracao.setValorSaldoPrincipal(rs.getString("vlr_saldo_principal"));
				notifExpiracao.setValorSaldoBonus(rs.getString("vlr_saldo_bonus"));
				notifExpiracao.setValorSaldoSM(rs.getString("vlr_saldo_sm"));
				notifExpiracao.setValorSaldoDados(rs.getString("vlr_saldo_dados"));
			
			}// Fim do if
			
		}// Fim do try  
		
		catch (SQLException e) 
		{
			super.log(Definicoes.ERRO,"next",
					"Erro ao processar proximo registro no produtor. Erro"+e);
			notifExpiracao = null;
		}// Fim do catch
		
		return notifExpiracao;
		
	}// Fim do método next
	
	
	/**
	 * Metodo....: finish
	 * Descricao.: Este metodo libera a conexão e registra o fim do processamento no log
	 * @throws Exception
	 */
	public void finish() throws Exception 
	{		
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG, "notificaExpiracao", "Fim");
	}// Fim do método finish
	
	
	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}
	
	
	public void handleException()
	{
		
	}
	
	
	public String getDataProcessamento() 
	{
		return dataInicial;
	}
	
	
	public String getDescricaoProcesso() 
	{
		return "Foram gravadas " + contador + " notificações de expiração.";
	}
	
	
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_NOTIFICACAO_EXPIRACAO;
	}
	
	
	public String getStatusProcesso()
	{
		return statusProcesso;
	}
	
	
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}
	
	
	/**
	 * Metodo....: foiProcessado
	 * Descricao.: Este metodo verifica se o processo batch já foi processado com sucesso no mesmo dia
	 * @throws GPPInternalErrorException
	 */
	public boolean foiProcessado() throws GPPInternalErrorException
	{
		
		try 
		{
			String sqle = " select * from tbl_ger_historico_proc_batch " +
			" where id_processo_batch = 53 " +
			" and idt_status_execucao = 'SUCESSO' " +
			" and dat_inicial_execucao between trunc(sysdate) and trunc(sysdate + 1) ";
			
			Object param[] = {};
			
			ResultSet rse = conexaoPrep.executaPreparedQuery(sqle, param, super.logId);
			
			if (!rse.next())
			{
				podeProcessar = true;
				return false;
			}// Fim do if
			
			else 
			{
				super.log(Definicoes.WARN, "notificacaoExpiracao",
						"Processo ja foi executado com sucesso no mesmo dia.");
				statusProcesso = Definicoes.TIPO_OPER_ERRO;
				podeProcessar = false;
				return true;
			}// Fim do else
			
		}// Fim do try
		
		catch (Exception e) 
		{
			super.log(Definicoes.ERRO,"gravaNotificacaoSMS","Erro interno. Excecao:"+e);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException(e.getMessage());
		} 

		
	}// Fim do método foiProcessado
	
	
	/**
	 * Metodo....: incrementaContador
	 * Descricao.: Este metodo será acessado pelos Consumidores para atualizar o número de mensagens processadas
	 */
	public synchronized void incrementaContador()
	{
		contador++;
	}
	
}// Fim da classe pública
