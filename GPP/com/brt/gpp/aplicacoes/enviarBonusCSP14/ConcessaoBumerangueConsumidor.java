package com.brt.gpp.aplicacoes.enviarBonusCSP14;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ConcessaoBumerangueConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor 	produtor;
	private PREPConexao				conexaoPrep;
	private static SimpleDateFormat formatadorDatMes = new SimpleDateFormat("yyyyMM");
	
	public ConcessaoBumerangueConsumidor()
	{
		super(GerentePoolLog.getInstancia(ConcessaoBumerangueConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_BONUS_CSP14),Definicoes.CL_ENVIO_BONUS_CSP14);
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
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		try
		{
			ConcessaoBumerangueVO bumVO = (ConcessaoBumerangueVO)obj;
			// Comentado por Joao Carlos em 17/07/06 para utilizar novo metodo que realiza
			// o insert select na fila de recargas jah realizando a concessao e verificacao
			// em um soh comando por CN afim de melhorar a performance.
			//processarTotalizacoes(bumVO.getDatMes(), "55"+bumVO.getCn().getIdtCodigoNacional());
			
			processarTotalizacoesInserindo(bumVO.getDatMes(), "55"+bumVO.getCn().getIdtCodigoNacional());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"execute","Erro ao realizar a concessao do bonus bumerangue. Erro:"+e);
		}
	}

	/**
	 * Metodo....:processarTotalizacoesInserindo
	 * Descricao.:Este metodo realiza a concessao de bonus calculando e agendando na fila de recargas
	 *            em um soh comando por CN. Isto eh para melhorar a performance do processo que estah
	 *            muito lento pela forma atual do programa.
	 * @param datMes		- DatMes da concessao
	 * @param cn			- Codigo nacional a ser processado
	 * @throws Exception
	 */
	private void processarTotalizacoesInserindo(String datMes, String cn) throws Exception
	{
		String sql = 
			"insert into tbl_rec_fila_recargas " +
	        "(idt_msisdn " +
			",tip_transacao " +
			",dat_cadastro " +
			",dat_execucao " +
			",dat_processamento " +
			",des_mensagem " +
			",tip_sms " +
			",ind_envia_sms " +
			",idt_status_processamento " +
			",idt_codigo_retorno " +
			",vlr_credito_principal " +
			",vlr_credito_bonus " +
			",vlr_credito_sms " +
			",vlr_credito_gprs " +
			",num_dias_exp_principal " +
			",num_dias_exp_bonus " +
			",num_dias_exp_sms " +
			",num_dias_exp_gprs " +
			",ind_zerar_saldo_bonus " +
			",ind_zerar_saldo_sms " +
			",ind_zerar_saldo_gprs) " +
			"select  t.idt_msisdn as msisdn, " +
					"? as tip_transacao, " +
					"sysdate as dat_cadastro, " +
					"? as dat_execucao, " +
					"null as dat_processamento, " +
					"'BrTGSM Informa. Prezado Cliente: Voce recebeu R$'||TO_CHAR(round(decode(p.idt_categoria,0,(t.num_segundos/60/3),(t.num_segundos/60/2))*b.vlr_bonus,2),'9990.00')||' do Bumerangue 14, por utilizar o CSP 14 nas suas chamadas' as des_mensagem, " +
					"'BUMERANGUE14' as tip_sms, " +
					"1 as ind_envia_sms, " +
					"8 as idt_status_processamento, " +
					"null as idt_codigo_retorno, " +
					"0 as vlr_credito_principal, " +
					"round(decode(p.idt_categoria,0,(t.num_segundos/60/3),(t.num_segundos/60/2))*b.vlr_bonus,2) as vlr_credito_bonus, " +
					"0 as vlr_credito_sms, " +
					"0 as vlr_credito_gprs, " +
					"0 as num_dias_exp_principal, " +
					"0 as num_dias_exp_bonus, " +
					"0 as num_dias_exp_sms, " +
					"0 as num_dias_exp_gprs, " +
					"0 as ind_zerar_saldo_bonus, " +
					"0 as ind_zerar_saldo_sms, " +
					"0 as ind_zerar_saldo_gprs " +
			  "from tbl_pro_totalizacao_bumerangue t " + 
					",tbl_apr_assinante a " +
					",tbl_ger_plano_preco p " +
					",tbl_ger_bonus_csp14 b " +
			  "where t.dat_mes = ? " +
			    "and a.idt_msisdn = t.idt_msisdn " +
			    "and p.idt_plano_preco = a.idt_plano_preco " +
			    "and t.idt_msisdn like '"+ cn +"%' " +
			    "and b.idt_plano = p.idt_plano_preco " +
			    "and b.idt_codigo_nacional = substr(t.idt_msisdn,3,2) " +
			    "and (p.idt_categoria  = ? or " +
			        "(p.idt_categoria not in (?,?) and exists   (select /*+index (r xie9tbl_rec_recargas)*/ " +
			                                                           "1 " +
					                                              "from tbl_rec_recargas r " +
					                                             "where r.dat_origem >= ? " +
					                                               "and r.dat_origem <  ? " +
					                                               "and r.id_tipo_recarga = ? " +
					                                               "and r.idt_msisdn      = t.idt_msisdn))) " +
			    "and not exists (select 1 " +
			                      "from tbl_rec_fila_recargas f " +
			                     "where f.dat_execucao = ? " +
			                       "and f.tip_transacao = ? " +
			                       "and f.idt_msisdn    = t.idt_msisdn) ";
		
		Object param[] = {Definicoes.RECARGA_BONUS_CSP14						// tip_transacao
				         ,new java.sql.Date(getDataIniConcessao().getTime())	// dat_execucao
				         ,datMes
				         ,new Integer(Definicoes.CATEGORIA_HIBRIDO)
				         ,new Integer(Definicoes.CATEGORIA_HIBRIDO)
				         ,new Integer(Definicoes.CATEGORIA_LIGMIX)
				         ,new java.sql.Date (getDataIniPeriodo(datMes).getTime())
				         ,new java.sql.Date (getDataFimPeriodo(datMes).getTime())
				         ,Definicoes.TIPO_RECARGA
				         ,new java.sql.Date(getDataIniConcessao().getTime())	// dat_execucao
				         ,Definicoes.RECARGA_BONUS_CSP14						// tip_transacao
				         };
		
		// Executa o insert de todos os valores a serem concedidos por CN.
		// Este metodo eh para melhorar a performance do processo de concessao
		// do bonus bumerangue14
		conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
	}
	
	/**
	 * Metodo....:processarTotalizacoes
	 * Descricao.:Processa os objetos totalizados que devem receber o bonus. Este metodo recebe um codigo nacional
	 *            para divisao do processamento
	 * @param datMes 	- DatMes da totalizacao a ser processada
	 * @param cn		- Codigo nacional a ser processado
	 * @throws Exception
	 */
	public void processarTotalizacoes(String datMes, String cn) throws Exception
	{
		// Realiza a pesquisa dos assinantes que possuiram totalizacao bumerangue
		// no perido sendo processado. Para esses assinantes o comando abaixo jah
		// verifica se estes realizaram a recarga necessaria para receber o bonus
		// ou se o assinante eh do plano Controle na qual nao precisa realizar tal
		// recarga. Para todos os assinantes tambem eh verificado se o registro de
		// concessao do bonus jah existe na tabela afim de evitar duplicidade
		// OBS: A data de pesquisa da concessao eh sempre o primeiro dia de cada
		// mes sendo que o processo de concessao eh responsavel por manter essa
		// data dessa forma sem a configuracao de hora,minuto e segundo afim de
		// melhorar a performance de acesso
		String sql ="select t.idt_msisdn, t.num_segundos, a.idt_plano_preco " +
					  "from tbl_pro_totalizacao_bumerangue t " + 
					      ",tbl_apr_assinante a " +
					      ",tbl_ger_plano_preco p " +
					 "where t.dat_mes = ? " +
					   "and a.idt_msisdn = t.idt_msisdn " +
					   "and t.idt_msisdn like '"+ cn +"%' " +
					   "and p.idt_plano_preco = a.idt_plano_preco " +
					   "and (p.idt_categoria  = ? or " +
					       "(p.idt_categoria not in (?,?) and not exists (select 1 " +
					                                                       "from tbl_rec_fila_recargas f " +
					                                                      "where f.dat_execucao = ? " +
					                                                        "and f.tip_transacao = ? " +
					                                                        "and f.idt_msisdn    = t.idt_msisdn) " +
					                                       "and exists (select /*+index (r xie9tbl_rec_recargas)*/ 1 " +
					                                                     "from tbl_rec_recargas r " +
					                                                    "where r.dat_origem >= ? " +
					                                                      "and r.dat_origem <  ? " +
					                                                      "and r.id_tipo_recarga = ? " +
					                                                      "and r.idt_msisdn      = t.idt_msisdn))) ";
		
		Object param[] = {datMes
				         ,new Integer(Definicoes.CATEGORIA_HIBRIDO)
				         ,new Integer(Definicoes.CATEGORIA_HIBRIDO)
				         ,new Integer(Definicoes.CATEGORIA_LIGMIX)
				         ,new java.sql.Date(getDataIniConcessao().getTime())
				         ,Definicoes.RECARGA_BONUS_CSP14
				         ,new java.sql.Date (getDataIniPeriodo(datMes).getTime())
				         ,new java.sql.Date (getDataFimPeriodo(datMes).getTime())
				         ,Definicoes.TIPO_RECARGA
				         };
		
		ResultSet rs = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
		// Para cada registro resultado do comando entao calcula-se o valor a ser
		// concedido e entao eh realizado o agendamento do credito na fila de recargas
		// Esses registros ficam lah esperando o processo de liberacao do bonus
		while (rs.next())
		{
			TotalizacaoBumerangue tot = new TotalizacaoBumerangue(datMes);
			tot.setMsisdn		(rs.getString("idt_msisdn"));
			tot.setNumSegundos	(rs.getLong("num_segundos"));
			tot.setPlanoPreco	(rs.getInt("idt_plano_preco"));
			
			// Realiza a concessao do bonus executando o comando de insert na tabela
			// de fila de recargas. Nesse metodo o valor a ser concedido eh calculado
			// dependendo do plano e dos minutos realizados pelo assinante
			realizaConcessao(tot);
		}
	}
	
	/**
	 * Metodo....:getDataIniConcessao
	 * Descricao.:Retorna da data inicio do processo de concessao do mes atual
	 * @return
	 */
	private Date getDataIniConcessao()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		
		return cal.getTime();
	}
	
	/**
	 * Metodo....:getDataIniPeriodo
	 * Descricao.:Retorna a data inicio do periodo sendo processado
	 * @param datMes - Data mes a ser verificado o periodo
	 * @return Date - Data inicial do periodo
	 */
	public static Date getDataIniPeriodo(String datMes) throws Exception
	{
		// O perido inicial eh o dia 28 do mes anterior ao sendo processado
		// na totalizacao. Portanto define a data pela configuracao do datmes
		// passado como parametro e define o dia 28 e volta um mes
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatadorDatMes.parse(datMes));
		cal.set(Calendar.DAY_OF_MONTH,28);
		cal.add(Calendar.MONTH,-1);
		
		return cal.getTime();
	}
	
	/**
	 * Metodo....:getDataFimPeriodo
	 * Descricao.:Retorna a data fim do periodo sendo processado
	 * @param datMes - Data mes a ser verificado o periodo
	 * @return Date - Data final do periodo
	 * @throws Exception
	 */
	public static Date getDataFimPeriodo(String datMes) throws Exception
	{
		// O periodo final eh o dia 28 do mes definido no campo DatMes da totalizacao
		// Portanto simplesmente realiza o parse da string e define o dia 28 para a data
		Calendar cal = Calendar.getInstance();
		cal.setTime(formatadorDatMes.parse(datMes));
		cal.set(Calendar.DAY_OF_MONTH,28);
		
		return cal.getTime();
	}
	
	/**
	 * Metodo....:realizaConcessao
	 * Descricao.:Este metodo realiza a concessao de dados do bonus bumerangue
	 *            chamando o metodo de pesquisa de valor a ser concedido e inserindo
	 *            os dados na tabela de fila de recargas
	 * @param totBum - Totalizacao Bumerangue
	 * @throws Exception
	 */
	private void realizaConcessao(TotalizacaoBumerangue totBum) throws Exception
	{
		// Cria instancia para formatar o valor a ser concedido
		DecimalFormat df = new DecimalFormat("###0.00");
		// Realiza a regra para validar o valor a ser concedido para o bonus bumerangue
		double valorConceder = getValorConcessao(totBum);
		
		// Realiza o comando para inserir os dados da promocao na fila de recargas
		// os dados inseridos sao agendados
		String sql = "insert into tbl_rec_fila_recargas " +
					"(idt_msisdn " +
					",tip_transacao " +
					",dat_cadastro " +
					",dat_execucao " +
					",dat_processamento " +
					",des_mensagem " +
					",tip_sms " +
					",ind_envia_sms " +
					",idt_status_processamento " +
					",idt_codigo_retorno " +
					",vlr_credito_principal " +
					",vlr_credito_bonus " +
					",vlr_credito_sms " +
					",vlr_credito_gprs " +
					",num_dias_exp_principal " +
					",num_dias_exp_bonus " +
					",num_dias_exp_sms " +
					",num_dias_exp_gprs " +
					",ind_zerar_saldo_bonus " +
					",ind_zerar_saldo_sms " +
					",ind_zerar_saldo_gprs) " +
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Object param[] = {totBum.getMsisdn()										// Msisdn do assinante
						 ,Definicoes.RECARGA_BONUS_CSP14							// Tipo de transacao
						 ,new Timestamp(Calendar.getInstance().getTimeInMillis())	// Data do cadastro
						 ,new Timestamp(Calendar.getInstance().getTimeInMillis())	// Data de agendamento
						 ,null														// Data de processamento
						 ,"BrTGSM Informa. Prezado Cliente: Voce recebeu R$ "+df.format(valorConceder)+" do Bumerangue 14, por utilizar o CSP 14 nas suas chamadas de longa distancia" // Mensagem
						 ,"BUMERANGUE14" 											// Tipo do SMS
						 ,new Integer(1)											// Indica o envio do sms 1 = true
						 ,new Integer(Definicoes.STATUS_RECARGA_AGENDAMENTO_BUMERANGUE) // Indica status agendado o bumerangue
						 ,null														// Codigo de erro
						 ,new Double(0)												// Valor de credito no saldo principal
						 ,new Double(valorConceder)									// Valor de credito no saldo de bonus (bonus bumerangue)
						 ,new Double(0)												// Valor de credito no saldo de sms
						 ,new Double(0)												// Valor de credito no saldo de dados
						 ,new Integer(0)											// Dias de exp do saldo principal
						 ,new Integer(0)											// Dias de exp do saldo bonus
						 ,new Integer(0)											// Dias de exp do saldo sms
						 ,new Integer(0)											// Dias de exp do saldo dados
						 ,new Integer(0)											// Indica se Zera o saldo de bonus
						 ,new Integer(0)											// Indica se Zera o saldo de sms
						 ,new Integer(0)											// Indica se Zera o saldo de dados
						};
		
		this.conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
	}
	
	/**
	 * Metodo....:getValorConcessao
	 * Descricao.:Este metodo identifica os valores a serem concedidos
	 * @param totBum		- Totalizador da promocao
	 * @return double		- Valor a ser concedido
	 * @throws Exception
	 */
	private double getValorConcessao(TotalizacaoBumerangue totBum) throws Exception
	{
		double valor=0;
		// Realiza a pesquisa dos valores possiveis de bonus a serem concedidos
		// baseado nos valores de plano de preco, CN do assinante e no total de
		// duracao sumarizado
		// Primeiro entao realiza a pesquisa dos valores basicos para posterior
		// analise das faixas de valores
		String sql = "select num_minutos_minimo,num_minutos_maximo,vlr_bonus " +
		               "from tbl_ger_bonus_csp14 " +
		              "where idt_plano = ? " +
		                "and idt_codigo_nacional = ? " +
		              "order by num_minutos_minimo";

		Object param[] = {new Integer(totBum.getPlanoPreco()), totBum.getMsisdn().substring(2,4)};
		ResultSet rs = null;
		try
		{
			rs = this.conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
			// Realiza a iteracao entre os valores configurados. Para cada valor
			// utiliza-se da seguinte formula:
			// Realiza a comparacao entre os minutos realizados e o minuto maximo sendo processado
			// na iteracao. Se a diferenca for negativa, significa que deve ser aplicado o valor maximo
			// de minutos vezes o valor do bonus e a diferenca deve ser aplicada na proxima iteracao
			// Caso o valor seja positivo entao utiliza-se esse valor de diferenca para ser aplicada
			// ao valor de bonus e deve ser zerado a variavel contendo a diferenca para que seja interrompido
			// o processamento
			double minutos = new Double(totBum.getNumSegundos()).doubleValue()/60;
			while (rs.next() && minutos != 0)
			{
				double maxMinutos = rs.getDouble("num_minutos_maximo");
				double diferenca  = maxMinutos - minutos;
				if (diferenca < 0)
				{
					valor += maxMinutos*rs.getDouble("vlr_bonus");
					// Acerta a variavel para conter o proximo valor a ser
					// utilizado para identificar o montante em minutos a
					// ser utilizado para o somatorio do bonus
					minutos = Math.abs(diferenca);
				}
				else if(diferenca > 0)
				{
					valor += minutos*rs.getDouble("vlr_bonus");
					// Indica que o processamento acabou, independente de outras
					// faixas de valores de bonus
					minutos = 0;
				}
			}
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
		return valor;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
}
