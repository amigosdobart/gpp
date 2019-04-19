package com.brt.gpp.aplicacoes.promocao.pacoteDados;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPromocaoInfosSms;
import com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados;
import com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe responsavel por executar o processo de gerenciamento
 * dos creditos do saldo principal do assinante que esta utilizando
 * um Pacote de Servico de Dados
 * 
 * @author Joao Paulo Galvagni
 * @since  11/09/2007
 */
public class GerentePacoteDadosConsumidor extends Aplicacoes
			 							  implements ProcessoBatchConsumidor
{
	private PREPConexao		  conexaoPrep		= null;
	private ConsultaAssinante consultaAssinante	= null;
	private double			  valorRestaurado	;
	
	// Atualizacao da tabela de AssinanteOferta com a data da retirada da oferta
	private final String SQL_ATUALIZACAO_RETIRADA_OFERTA = "UPDATE TBL_PRO_ASS_OFERTA_PCT_DADOS a 	" +
														   "   SET a.DAT_RETIRADA_OFERTA = ? 		" +
														   " WHERE a.IDT_MSISDN = ? 				" +
														   "   AND a.IDT_OFERTA = ?					" ;
	
	// Atualizacao da tabela de AssinanteOferta com novos valores do saldo de DADOS e TORPEDOS
	private final String SQL_ATUALIZACAO_SALDOS = "UPDATE TBL_PRO_ASS_OFERTA_PCT_DADOS a	" +
												  "   SET a.VLR_SALDO_TORPEDOS = ? 			" +
												  "      ,a.VLR_SALDO_DADOS    = ? 			" +
												  "      ,a.IND_SUSPENSO 	   = ? 			" +
												  " WHERE a.IDT_MSISDN 		   = ?			" +
												  "	  AND a.IDT_OFERTA 		   = ?			" ;
	
	// Atualizacao do status da Oferta do assinante (Suspenso ou Ativo)
	private final String SQL_ATUALIZACAO_STATUS_OFERTA = "UPDATE tbl_pro_ass_oferta_pct_dados a " +
													 	 "   SET a.IND_SUSPENSO = ? 			" +
													 	 " WHERE a.IDT_MSISDN   = ? 			" +
													 	 "   AND a.IDT_OFERTA   = ?				" ;
	
	/**
	 * Construtor da Classe
	 *
	 */
	public GerentePacoteDadosConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerentePacoteDadosConsumidor.class).getIdProcesso(Definicoes.CL_GER_PACOTE_DADOS_CONS),
			  Definicoes.CL_GER_PACOTE_DADOS_CONS);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicializa o objeto
	 * 
	 * @param produtor - Produtor
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		conexaoPrep = gerenteBancoDados.getConexaoPREP(getIdLog());
		consultaAssinante = new ConsultaAssinante(getIdLog());
		startup();
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Inicializa o objeto
	 * 
	 * @param produtor - Classe produtora
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Seta as instancias necessarias da classe
	 * 
	 */
	public void startup() throws Exception
	{
	}
	
	/**
	 * Metodo....: execute
	 * Descricao.: Metodo responsavel pela execucao de cada assinantes
	 * 			   que sera removido da Promocao Fale Gratis
	 * 
	 * @param obj	- Objeto contendo as informacoes para processar
	 */
	public void execute(Object obj) throws Exception
	{
		// Seleciona o assinante e a respectiva oferta
		AssinanteOfertaPacoteDados assinanteOferta = (AssinanteOfertaPacoteDados)obj;
		// Zera o valor restaurado sempre no inicio do metodo
		valorRestaurado = 0.0;
		
		// Realiza uma consulta das informacoes do assinante na Tecnomen
		Assinante dadosAssinante = consultaAssinante.consultarAssinantePlataforma(assinanteOferta.getMsisdn());
		
		// Clona a data da contratacao do assinante na Oferta, zera-se a hora, minuto e segundos
		// e adiciona a quantidade de dias do Pacote ao resultado
		Calendar fimDaOferta = Calendar.getInstance();
		fimDaOferta.setTime((Date)assinanteOferta.getDataContratacao().clone());
		fimDaOferta.set(Calendar.HOUR, 	 0);
		fimDaOferta.set(Calendar.MINUTE, 0);
		fimDaOferta.set(Calendar.SECOND, 0);
		fimDaOferta.add(Calendar.DAY_OF_MONTH, assinanteOferta.getOfertaPacoteDados().getPacoteDados().getNumDias()+1);
		
		// Valida se a data de hoje eh menor do que a data do final da oferta + 1
		// atraves do long da Data (getTime().getTime() -> long)
		if ( Calendar.getInstance().getTime().getTime() < fimDaOferta.getTime().getTime() )
		{
			// Valida se o assinante esta com status Normal User na Plataforma
			if (dadosAssinante.getStatusAssinante() == Definicoes.STATUS_NORMAL_USER)
			{
				// Valida se a Oferta do Assinante esta suspensa e, caso positivo,
				// a mesma deve ser setada para Ativa, pois o assinante ja esta
				// com status Normal User e os saldos do assinante tambem deverao
				// ser atualizados de acordo com os atuais, uma vez que o 
				// assinante pode ter realizado alguma transferencia de bonus
				// para o saldo de Torpedo e/ou Dados
				if (assinanteOferta.isAssinanteSuspenso())
					atualizaSaldosAssinante(dadosAssinante, assinanteOferta.getOfertaPacoteDados().getIdtOferta());
				
				// Realiza o ajuste necessario no saldo especifico do assinante,
				// mantendo o valor do saldo do Pacote de Dados alto para que
				// o mesmo possa usufruir da promocao
				efetuaManutencaoCreditos(dadosAssinante, assinanteOferta);
			}
			else
				// Valida se o status do assinante eh Recarga Expirada e se o Status da Oferta 
				// esta Ativo e, caso positivo, o Status da Oferta devera ser atualizado para 
				// Suspensa, ate que o assinante volte para o Status Normal User
				// Caso o Status da Oferta ja seja Suspenso, nenhuma acao sera realizada
				if (dadosAssinante.getStatusAssinante() == Definicoes.STATUS_RECHARGE_EXPIRED && !assinanteOferta.isAssinanteSuspenso())
					atualizaStatusOfertaAssinante(assinanteOferta, Definicoes.IND_OFERTA_PCT_DADOS_SUSPENSA);
		}
		else
		{
			// Inicializa o retorno para manipulacao interna
			short retorno = Definicoes.RET_ERRO_GENERICO_GPP;
			
			if (dadosAssinante.getStatusAssinante() == Definicoes.STATUS_NORMAL_USER)
				// Restaura o saldo do assinante, creditando ou debitando a
				// diferenca entre o Saldo Atual e o Saldo Original
				retorno = restauraSaldoAssinante(assinanteOferta, dadosAssinante);
			else
			{
				super.log(Definicoes.DEBUG, "execute", "Assinante " + assinanteOferta + " esta com status " +
						  dadosAssinante.getStatusAssinante() + " e nao tera seu saldo restaurado");
				retorno = Definicoes.RET_OPERACAO_OK;
			}
			
			// Caso o ajuste no saldo tenha sido Ok, a data de retirada da Oferta 
			// eh atualizada no Banco e uma mensagem de SMS eh enviada para o 
			// assinante avisando do final do Pacote de Dados
			if (retorno == Definicoes.RET_OPERACAO_OK)
			{
				atualizaDataRetiradaOferta(assinanteOferta);
				DecimalFormat conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE_S_PONTO, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
				SimpleDateFormat sdfData = new SimpleDateFormat(Definicoes.MASCARA_DATE);
				SimpleDateFormat sdfHora = new SimpleDateFormat(Definicoes.MASCARA_TIME);
				
				// Inicia a montagem da mensagem a ser enviada para o assinante
				StringBuffer msg = new StringBuffer("BrT Informa: Seu periodo de participacao no Pacote de Dados expirou em ");
				msg.append(sdfData.format(Calendar.getInstance().getTime()));
				msg.append(" as ").append(sdfHora.format(Calendar.getInstance().getTime()));
				msg.append(". Seu Saldo de ").append(assinanteOferta.getOfertaPacoteDados().getTipoSaldo().getNomTipoSaldo());
				msg.append(" restaurado foi de R$ ").append(conversorDouble.format(valorRestaurado)).append(".");
				super.log(Definicoes.DEBUG, "execute", "Msg '" + msg.toString() + "' enviada para o assinante " + assinanteOferta.getMsisdn());
				
				ConsumidorSMS.getInstance(getIdLog()).gravarMensagemSMS(dadosAssinante.getMSISDN(),
																		msg.toString(),
																		Definicoes.SMS_PRIORIDADE_ZERO,
																		Definicoes.SMS_PACOTE_DADOS,
																		conexaoPrep);
			}
		}
	}
	
	/**
	 * Metodo....: efetuaManutencaoCreditos
	 * Descricao.: Realiza a manutencao dos saldos do assinante atraves
	 * 			   do ajuste de Credito no saldo da Oferta
	 * 
	 * @param  assinante		- Instancia de <code>Assinante</code>
	 * @param  assinanteOferta	- Instancia de <code>AssinanteOfertaPacoteDados</code>
	 * @return short			- Resultado do ajuste
	 */
	private short efetuaManutencaoCreditos(Assinante assinante, AssinanteOfertaPacoteDados assinanteOferta) throws Exception
	{
		// As validacoes abaixo sao necessarias pelo fato de ambos os saldos (Dados e Torpedos) serem
		// armazenados no Banco de Dados. Assim, existe a necessidade de validar o "tipoSaldo" da Oferta
		// do assinante para que sejam selecionados os respectivos valores para ajuste de manutencao de creditos
		// valorMinimoAjute		 - Valor minimo que o assinante pode possuir, abaixo disso, o ajuste eh realizado
		// tipoSaldo			 - Tipo de Saldo da Oferta do assinante (Dados ou Torpedos)
		// saldoAtual			 - De acordo com o tipoSaldo, define o valor atual do respectivo saldo
		// saldoParaCredito		 - De acordo com o tipoSaldo, define em qual saldo sera realizado o ajuste
		double valorMinimoAjuste = Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("VLR_MIN_AJT_PCT_DADOS"));
		short tipoSaldo 		 = assinanteOferta.getOfertaPacoteDados().getTipoSaldo().getIdtTipoSaldo();
		double saldoAtual 		 = tipoSaldo == TipoSaldo.DADOS ? assinante.getCreditosDados() : assinante.getCreditosSms();
		String saldoParaCredito  = tipoSaldo == TipoSaldo.DADOS ? Definicoes.TIPO_CREDITO_VOLUME_DADOS : Definicoes.TIPO_CREDITO_SMS;
		
		// Caso o assinante possua saldo superior ou igual ao limite minimo,
		// nao sera realizado o ajuste e, portanto, nao ha a necessidade de
		// calcular o valor do ajuste no respectivo saldo da Oferta
		if (saldoAtual >= valorMinimoAjuste)
			return Definicoes.RET_OPERACAO_OK;
		
		// Aqui sao calculados os valores de ajuste para cada saldo
		// valorAjusteDados			- Define o valor do ajuste para o saldo de Dados
		// valorAjusteTorpedos		- Define o valor do ajuste para o saldo de Torpedos
		// valorAjuste				- De acordo com o tipoSaldo, define o valor do ajuste
		double valorAjusteDados		= Math.abs(Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("VLR_AJUSTE_PCT_DADOS_DADOS")) - 
								  	  assinante.getCreditosDados()); 
		double valorAjusteTorpedos	= Math.abs(Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("VLR_AJUSTE_PCT_DADOS_SMS")) - 
			  	  				  	  assinante.getCreditosSms());
		double valorAjuste 		= tipoSaldo == TipoSaldo.DADOS ? valorAjusteDados : valorAjusteTorpedos;
		
		super.log(Definicoes.DEBUG, "efetuaManutencaoCreditos", "Manutencao de creditos (tipo:"+saldoParaCredito+
				  "-valor:"+valorAjuste+") para o assinante " + assinanteOferta.getMsisdn());
		
		// Cria uma nova instancia de Ajustar
		Ajustar ajustar = new Ajustar(getIdLog());
		// Realiza o ajuste no saldo do assinante
		return ajustar.executarAjuste(assinanteOferta.getMsisdn(),
									  Definicoes.AJUSTE_MANUTENCAO_PACOTE_DADOS,
									  saldoParaCredito,
									  valorAjuste,
									  Definicoes.TIPO_AJUSTE_CREDITO,
									  Calendar.getInstance().getTime(),
									  Definicoes.SO_GPP,
									  Definicoes.GPP_OPERADOR,
									  null, // A data eh null para nao alterar a Data de vencto
									  assinante,
									  "Manutencao da Oferta de Pacote de Dados",
									  Definicoes.AJUSTE_NORMAL);
	}
	
	/**
	 * Metodo....: restauraSaldoAssinante
	 * Descricao.: Realiza um ajuste de Credito/Debito no saldo utilizado
	 * 			   pelo assinante na Oferta
	 * 
	 * @param assinanteOferta - Instancia de <code>AssinanteOfertaPacoteDados</code>
	 * @param assinante		  - Instancia de <code>Assinante</code>
	 */
	private short restauraSaldoAssinante(AssinanteOfertaPacoteDados assinanteOferta, Assinante assinante)
	{
		// As validacoes abaixo sao necessarias pelo fato de ambos os saldos (Dados e Torpedos) serem
		// armazenados no Banco de Dados. Assim, existe a necessidade de validar o "tipoSaldo" da Oferta
		// do assinante para que sejam selecionados os respectivos valores para ajuste.
		// nomeSaldo 			- Selecionado para montar a descricao do ajuste
		// tipoSaldo			- Determinante do saldo da oferta do assinante (Dados ou Torpedos)
		// deltaValorAjuste*	- Sao os valores referentes a diferenca entre o saldo armazenado e o atual
		// nomeLancamento		- Selecao do lancamento para inclusao na descricao
		// tipoCredito			- De acordo com o tipoSaldo, determina o saldo que recebera o ajuste
		// tipoDeAjuste			- De acordo com o deltaValorAjuste, determina se eh Credito ou Debito
		// tipoTransacao		- Tambem de acordo como deltaValorAjuste, determina o TipoTransacao (C:05051,D:06051)
		// descricao			- Descricao do ajuste realizado (motivo do ajuste)
		String nomeSaldo				= assinanteOferta.getOfertaPacoteDados().getTipoSaldo().getNomTipoSaldo();
		short tipoSaldo 				= assinanteOferta.getOfertaPacoteDados().getTipoSaldo().getIdtTipoSaldo();
		double deltaValorAjusteDados 	= assinanteOferta.getValorSaldoDadosDouble() - assinante.getCreditosDados();
		double deltaValorAjusteTorpedos = assinanteOferta.getValorSaldoTorpedoDouble() - assinante.getCreditosSms();
		double deltaValorAjuste 		= tipoSaldo == TipoSaldo.DADOS ? deltaValorAjusteDados : deltaValorAjusteTorpedos;
		String nomeLancamento			= deltaValorAjuste < 0 ? "Debito" : "Credito";
		String tipoCredito 				= tipoSaldo == TipoSaldo.DADOS ? Definicoes.TIPO_CREDITO_VOLUME_DADOS : Definicoes.TIPO_CREDITO_SMS;
		String tipoDeAjuste 			= deltaValorAjuste < 0 ? Definicoes.TIPO_AJUSTE_DEBITO : Definicoes.TIPO_AJUSTE_CREDITO;
		String tipoTransacao			= deltaValorAjuste < 0 ? Definicoes.AJUSTE_DEB_PACOTE_DADOS_RESTAURACAO : Definicoes.AJUSTE_CRED_PACOTE_DADOS_RESTAURACAO;
		StringBuffer descricao			= new StringBuffer(nomeLancamento).append(" pelo Fim da Oferta de Pacote de ").append(nomeSaldo);
		
		// Valida se o valor do ajuste eh diferente de 0
		// Caso seja 0 (zero), nao ha a necessidade do ajuste e o retorno eh OK
		if (deltaValorAjuste == 0)
			return Definicoes.RET_OPERACAO_OK;
		
		// Caso o deltaValorAjuste seja diferente de 0, significa que
		// o valorRestaurado devera ser atualizado para o envio do SMS
		// de alerta do fim da Oferta para o assinante
		valorRestaurado	= Math.abs((tipoSaldo == TipoSaldo.DADOS ? assinante.getCreditosDados():
																   assinante.getCreditosSms()  )+ deltaValorAjuste);
		
		// Executa o ajuste de restauracao de saldo
		return executaAjusteRestauracao(assinante,
										tipoTransacao,
										tipoCredito,
										tipoDeAjuste,
										deltaValorAjuste,
										descricao.toString());
	}
	
	/**
	 * Metodo....: executaAjsuteRestauracao
	 * Descricao.: Realiza o ajuste para restaurar o Saldo Original do assinante
	 * 			   apos o Fim da Oferta do Pacote de Dados
	 * 
	 * @param  assinante	- Instancia de <code>Assinante</code>
	 * @param  tipoCredito	- Saldo a ser creditado (SMS ou Dados)
	 * @param  tipoAjuste	- C: Credito, D: Debito
	 * @param  valorAjuste	- Valor a ser debitado/creditado
	 * @param  descricao	- Descricao do ajuste
	 * @return short		- Resultado da operacao
	 */
	private short executaAjusteRestauracao(Assinante assinante, String tipoTransacao,
										   String tipoCredito, String tipoAjuste, double valorAjuste,
										   String descricao)
	{
		super.log(Definicoes.DEBUG, "executaAjusteRestauracao", "Ajuste de saldo para " + assinante.getMSISDN() + "(tipoCredito:" + 
				  tipoCredito + "-tipoAjuste:" + tipoAjuste + "-valor:" + valorAjuste + "-descricao:" + descricao);
		
		Ajustar ajustar = new Ajustar(getIdLog());
		
		// Realiza o ajuste no saldo do assinante, de acordo com a necessidade
		return ajustar.executarAjuste(assinante.getMSISDN(),
				   					  tipoTransacao,
				   					  tipoCredito,
				   					  Math.abs(valorAjuste),
				   					  tipoAjuste,
				   					  Calendar.getInstance().getTime(),
				   					  Definicoes.SO_GPP,
				   					  Definicoes.GPP_OPERADOR,
				   					  null, // A data eh null para nao haver alteracao
				   					  assinante,
				   					  descricao,
				   					  Definicoes.AJUSTE_NORMAL);
	}
	
	/**
	 * Metodo....: atualizaDataRetiradaOferta
	 * Descricao.: Realiza a atualizacao da data de retirada
	 * 			   da Oferta para o assinante informado
	 * 
	 * @param  assinanteOferta - Instancia de <code>AssinanteOfertaPacoteDados</code>
	 * @throws Exception
	 */
	private void atualizaDataRetiradaOferta(AssinanteOfertaPacoteDados assinanteOferta) throws Exception
	{
		Object[] parametro = {new Timestamp(Calendar.getInstance().getTimeInMillis())
							 ,assinanteOferta.getMsisdn()
							 ,new Integer(assinanteOferta.getOfertaPacoteDados().getIdtOferta())
							 };
		
		conexaoPrep.executaPreparedUpdate(SQL_ATUALIZACAO_RETIRADA_OFERTA, parametro, getIdLog());
		
		super.log(Definicoes.DEBUG, "atualizaDataRetiradaOferta", "Data de retirada da oferta atualizada (MSISDN:" + assinanteOferta.getMsisdn() + ").");
	}
	
	/**
	 * Metodo....: atualizaSaldosAssinante
	 * Descricao.: Realiza a atualizacao dos saldos de Torpedo
	 * 			   e SMS na tabela da Oferta o qual o assinante
	 * 			   esta cadastrado
	 * 
	 * @param  assinante - Instancia de <code>Assinante</code>
	 * @throws Exception
	 */
	private void atualizaSaldosAssinante(Assinante assinante, int idtOferta) throws Exception
	{
		Object[] parametros = {new Double(assinante.getCreditosSms())
							  ,new Double(assinante.getCreditosDados())
							  ,new Integer(Definicoes.IND_OFERTA_PCT_DADOS_ATIVA)
							  ,assinante.getMSISDN()
							  ,new Integer(idtOferta)};
		
		conexaoPrep.executaPreparedUpdate(SQL_ATUALIZACAO_SALDOS, parametros, getIdLog());
		
		super.log(Definicoes.DEBUG, "atualizaSaldosAssinante", "Saldo do assinante " + assinante.getMSISDN() + " atualizado na tabela de Oferta");
	}
	
	/**
	 * Metodo....: atualizaStatusOfertaAssinante
	 * Descricao.: Atualiza o status da oferta do assinante
	 * 
	 * @param  assinanteOferta	- Instancia de <code>AssinanteOfertaPacoteDados</code>
	 * @param  statusOferta		- Novo status da Oferta
	 * @throws Exception
	 */
	private void atualizaStatusOfertaAssinante(AssinanteOfertaPacoteDados assinanteOferta, int statusOferta) throws Exception
	{
		Object[] parametros = {new Integer(statusOferta)
							  ,assinanteOferta.getMsisdn()
							  ,new Integer(assinanteOferta.getOfertaPacoteDados().getIdtOferta())};
		
		conexaoPrep.executaPreparedUpdate(SQL_ATUALIZACAO_STATUS_OFERTA, parametros, getIdLog());
		
		super.log(Definicoes.DEBUG, "atualizaStatusOfertaAssinante", "Status da Oferta do assinante " + assinanteOferta.getMsisdn() + " atualizado para " + statusOferta);
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Finaliza o necessario do objeto
	 * 
	 */
	public void finish()
	{
		gerenteBancoDados.liberaConexaoPREP(conexaoPrep, getIdLog());
	}
}