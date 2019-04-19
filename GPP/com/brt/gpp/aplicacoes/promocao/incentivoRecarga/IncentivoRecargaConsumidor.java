package com.brt.gpp.aplicacoes.promocao.incentivoRecarga;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinanteIncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusIncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.IncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.MensagemBonusIncentivoRecarga;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargasDAO;
import com.brt.gpp.aplicacoes.recarregar.InsercaoCreditos;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *
 * @author João Paulo Galvagni
 * @since  19/11/2007
 */
public class IncentivoRecargaConsumidor extends InsercaoCreditos
			 implements ProcessoBatchConsumidor
{
	private PREPConexao		  conexaoPrep		= null;

	// SQL PARA ATUALIZACAO DA DATA DE RETIRADA DA CAMPANHA
	private final String SQL_ATUALIZACAO_DATA_RETIRADA = "UPDATE TBL_CAM_ASS_INCENTIVO_RECARGA  " +
														 "   SET dat_retirada = ? 				" +
														 " WHERE idt_incentivo_recarga = ? 		" +
														 "   AND idt_msisdn = ? 				" ;

	/**
	 * Construtor da Classe
	 *
	 */
	public IncentivoRecargaConsumidor()
	{
		super(GerentePoolLog.getInstancia(IncentivoRecargaConsumidor.class).getIdProcesso(Definicoes.CL_INCENTIVO_RECARGA_CONSUMIDOR),
										  Definicoes.CL_INCENTIVO_RECARGA_CONSUMIDOR);
	}

	/**
	 * Descricao.: Inicializa o objeto
	 *
	 * @param produtor - Produtor
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		startup();
	}

	/**
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
	 * Descricao.: Metodo responsavel pela execucao de cada assinantes
	 * 			   que sera removido da Promocao Fale Gratis
	 *
	 * @param obj	- Objeto contendo as informacoes para processar
	 */
	public void execute(Object obj)
	{
		AssinanteIncentivoRecarga assinanteIncentivo = (AssinanteIncentivoRecarga)obj;
		IncentivoRecarga incentivoRecarga = assinanteIncentivo.getIncentivoRecarga();
		Calendar cal = Calendar.getInstance();

		try
		{
			// Seta o AutoCommit da conexao para false
			conexaoPrep.setAutoCommit(false);

			// Varre a lista de Bonificacoes do incentivo associado ao assinante e realiza
			// o agendamento na Fila de Recargas para cada bonus. Quando houver uma mensagem
			// associada, sera agendada tambem na tabela de SMS (TBL_GER_ENVIO_SMS)
			for (Iterator i = incentivoRecarga.getListaBonusIncentivo().iterator(); i.hasNext(); )
			{
				// Seleciona o Bonus de Incentivo atual para processamento do bonus e SMS
				BonusIncentivoRecarga bonusIncentivo = (BonusIncentivoRecarga)i.next();

				// Eh montada a data de bonificacao do assinante, levando em consideracao
				// a data de inicio da concessao (YYYYMM) e o dia de execucao.
				// Caso essa data de bonificacao seja nula, significa que a bonificacao
				// ira ocorrer de forma online, ou seja, o agendamento ocorrera para o dia atual
				Date dataBonificacao = getDataBonificacao(bonusIncentivo.getDataInicioConcessaoBonus(),
														  assinanteIncentivo.getDiaExecucao());

				// Aqui eh preenchida a entidade FilaRecargas para o agendamento do bonus
				// na Fila de Recargas
				FilaRecargas filaRecargas = getFilaRecargas(assinanteIncentivo.getMsisdn(),
															bonusIncentivo,
															incentivoRecarga.getTipTransacao(),
															dataBonificacao,
															cal);

				DadosSMS dadosSms = getDadosSms(assinanteIncentivo.getMsisdn(), bonusIncentivo, dataBonificacao);

				// Valida se sera enviado um SMS
				if (dadosSms != null)
				{
					EnvioSMSDAO dao = new EnvioSMSDAO(super.getIdLog(), conexaoPrep);
					dao.inserirSMS(dadosSms);
				}

				// Insere na fila de recargas o agendamento do bonus a ser recebido,
				// de acordo com o tipo de saldo e o respectivo valor
                FilaRecargasDAO filaRecargasDao = new FilaRecargasDAO(conexaoPrep);
                filaRecargasDao.insereRecargaNaFila(filaRecargas);
			}

			// Sao setados os parametros para a atualizacao de retirada do
			// assinante do incentivo de recargas
			Object[] param = {new Timestamp(Calendar.getInstance().getTimeInMillis())
							 ,new Integer(assinanteIncentivo.getIncentivoRecarga().getIdIncentivoRecarga())
							 ,assinanteIncentivo.getMsisdn()};

			// Atualizacao da data de retirada do assinante da campanha, por ja ter recebido
			// a devida bonificacao
			conexaoPrep.executaPreparedUpdate(SQL_ATUALIZACAO_DATA_RETIRADA, param, super.getIdLog());

			// Apos o sucesso de todas as operacoes, um commit eh executado
			conexaoPrep.commit();
		}
		catch (Exception e)
		{
			log(Definicoes.ERRO, "execute", "Erro no processo de concessao de bonus de incentivo " +
											"de recargas para o MSISDN " + assinanteIncentivo.getMsisdn() +
											"Erro: " + e);
			try
			{
				conexaoPrep.rollback();
			}
			catch (Exception eSql)
			{
				log(Definicoes.ERRO, "execute", "Erro no roolback da bonificacao de incentivo de recargas (" +
												assinanteIncentivo.getMsisdn() + ")Erro: " + eSql);
			}
		}
	}

	/**
	 * Constroi a entidade FilaRecargas com os parametros
	 * em comum entre os tipos de saldo
	 *
	 * @param  msisdn		- Numero do assinante
	 * @return filaRecargas - Instancia de <code>FilaRecargas</code>
	 */
	private FilaRecargas getFilaRecargas(String msisdn, BonusIncentivoRecarga bonusIncentivo, String tipTransacao, Date dataBonificacao, Calendar cal)
	{
		Double vlrConcessao = new Double(bonusIncentivo.getValorConcessaoBonus());
		Double vlrZero		= new Double(0.0);
		int idtTipoSaldo	= bonusIncentivo.getTipoSaldo().getIdtTipoSaldo();

		// Verifica se a data de bonificacao eh diferente de null, o que significa
		// agendamento da bonificacao e dependencia do bonus pula-pula, caso contrario
		// a bonificacao podera ser realizada no momento da inclusao da fila de recargas
		int statusRecarga 	= (dataBonificacao != null ? Definicoes.STATUS_RECARGA_AGENDAMENTO_INCENTIVO :
							   Definicoes.STATUS_RECARGA_NAO_PROCESSADA);

		FilaRecargas filaRecargas = new FilaRecargas();
		filaRecargas.setIdtMsisdn(msisdn);
		filaRecargas.setIdtStatusProcessamento(new Integer(statusRecarga));

		filaRecargas.setTipOperacao(Definicoes.TIPO_AJUSTE);
		filaRecargas.setTipTransacao(tipTransacao);
		filaRecargas.setDatCadastro(new Timestamp(cal.getTimeInMillis()));
		filaRecargas.setDatExecucao(new Timestamp(dataBonificacao != null ? dataBonificacao.getTime() : new Date().getTime()));
		// Seta o valor de credito a ser concedido de acordo com
		// o Identificador do Tipo de Saldo
		filaRecargas.setVlrCreditoPrincipal(idtTipoSaldo == TipoSaldo.PRINCIPAL ? vlrConcessao : vlrZero);
		filaRecargas.setVlrCreditoBonus	   (idtTipoSaldo == TipoSaldo.BONUS 	? vlrConcessao : vlrZero);
		filaRecargas.setVlrCreditoSms      (idtTipoSaldo == TipoSaldo.TORPEDOS 	? vlrConcessao : vlrZero);
		filaRecargas.setVlrCreditoGprs     (idtTipoSaldo == TipoSaldo.DADOS 	? vlrConcessao : vlrZero);
		filaRecargas.setVlrCreditoPeriodico(idtTipoSaldo == TipoSaldo.PERIODICO ? vlrConcessao : vlrZero);
		filaRecargas.setNumDiasExpPrincipal(new Integer(0));
		filaRecargas.setNumDiasExpBonus(new Integer(0));
		filaRecargas.setNumDiasExpSms(new Integer(0));
		filaRecargas.setNumDiasExpGprs(new Integer(0));
		filaRecargas.setNumPrioridade(new Integer(Definicoes.SMS_PRIORIDADE_UM));
		filaRecargas.setIndEnviaSms(new Integer(0)); // Nao sera enviado SMS apos a bonificacao
		filaRecargas.setIndZerarSaldoBonus(new Integer(0));
		filaRecargas.setIndZerarSaldoGprs(new Integer(0));
		filaRecargas.setIndZerarSaldoSms(new Integer(0));

		// Soma-se 1 segundo no cal para evitar a violacao de chave
		// primaria (composta) da Fila de recargas (msisdn, tipTransacao e dataCadastro)
		cal.add(Calendar.SECOND, 1);

		return filaRecargas;
	}

	/**
	 * Preenche os valores para o envio do SMS, quando necessario,
	 * incluindo a data de agendamento do SMS (Alteracao no processo
	 * de envio de SMS foi realizado para contemplar esse agd.)
	 *
	 * @param  msisdn			- Numero do assinante que recebera a msg
	 * @param  msgBonusIncentivo- Instancia de <code>MensagemBonusIncentivoRecarga</code>
	 * @param  dataBonificacao	- Data em que o SMS devera ser enviado
	 * @return dadosSms			- Instancia de <code>DadosSMS</code>
	 */
	private DadosSMS getDadosSms(String msisdn, BonusIncentivoRecarga bonusIncentivo, Date dataBonificacao)
	{
		DadosSMS dadosSms = null;

		TipoSMS tipo = new TipoSMS();
		tipo.setIdtTipoSMS(Definicoes.TIP_SMS_CAMPANHA_CONCESSAO);

		MensagemBonusIncentivoRecarga msgBonusIncentivo = bonusIncentivo.getMensagemBonusIncentivo();

		if (msgBonusIncentivo.getMensagem() != null)
		{
			java.sql.Date dataAgendamento = null;

			// Uma nova instancia do DadosSMS eh criada


			dadosSms = new DadosSMS();
			dadosSms.setDesMensagem(msgBonusIncentivo.getMensagem());
			dadosSms.setIdtMsisdn(msisdn);
			dadosSms.setNumPrioridade(Definicoes.SMS_PRIORIDADE_ZERO);
			dadosSms.setTipoSMS(tipo);

			// Calcula a data de agendamento do SMS, caso haja dias de retroatividade
			// Se nao houver, significa que a mensagem sera enviada imediatamente
			if (dataBonificacao != null && msgBonusIncentivo.getNumDiasAntecedeBonus() > 0)
			{
				Calendar dataBase = Calendar.getInstance();
				dataBase.setTime(dataBonificacao);
				dataBase.add(Calendar.DAY_OF_MONTH, -msgBonusIncentivo.getNumDiasAntecedeBonus());
				dataAgendamento = new java.sql.Date(dataBase.getTimeInMillis());
			}

			dadosSms.setDatEnvioSMS(dataAgendamento);
		}

		return dadosSms;
	}

	/**
	 * Retorna a data para a bonificacao, caso exista. Nao existindo,
	 * a data da concessao sera a atual
	 *
	 * @param  datMesBonificacao - DatMes para bonificacao
	 * @param  diaExecucao		 - Dia de execucao do Pula-Pula do assinante
	 * @return dataBonificacao	 - Data da bonificacao
	 */
	private Date getDataBonificacao(String datMesBonificacao, int diaExecucao)
	{
		Date dataBonificacao = null;

		if (datMesBonificacao != null && !"".equals(datMesBonificacao))
		{
			Calendar cal = Calendar.getInstance();
			String ano = datMesBonificacao.substring(0, 4);
			String mes = datMesBonificacao.substring(4);

			cal.set(Calendar.YEAR, 		   Integer.parseInt(ano));
			cal.set(Calendar.MONTH, 	   Integer.parseInt(mes));
			// Diminui-se 1 do mes, pois Janeiro = 0 e nao 1
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DAY_OF_MONTH, diaExecucao);

			dataBonificacao = cal.getTime();
		}

		return dataBonificacao;
	}

	/**
	 * Descricao.: Finaliza o necessario do objeto
	 *
	 */
	public void finish()
	{
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, getIdLog());
	}
}