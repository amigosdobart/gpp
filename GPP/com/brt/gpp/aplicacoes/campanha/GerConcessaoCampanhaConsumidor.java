package com.brt.gpp.aplicacoes.campanha;

import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Calendar;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.campanha.GerConcessaoCampanhaProdutor;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.aplicacoes.campanha.entidade.SMSCampanha;
import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.dao.HistoricoConcessaoDAO;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe representa o processo de concessao de creditos. A responsabilidade 
 * desta, eh de para um determinado objeto AssinanteCampanha, verificar se o mesmo 
 * deve ser bonificado por alguma condicaoConcessao existente para a campanha e 
 * entao aplicar o bonus devido. A aplicacao de bonus utiliza a API de ajuste do 
 * sistema, sendo que se esse for bonificado entao um registro da concessao deverah 
 * 
 * ser criado para identifica-lo. Por fim o assinante tambem deverah ser removido 
 * da promocao caso ganhe o bonus afim de nao processar esse registro novamente.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class GerConcessaoCampanhaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor 
{
	private ProcessoBatchProdutor produtor;
	private Ajustar ajustar;
	private DecimalFormat df = new DecimalFormat("###0.00");
	
	public GerConcessaoCampanhaConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerConcessaoCampanhaConsumidor.class).getIdProcesso(Definicoes.CL_GER_CONCESSAO_CREDITOS_CAMP_CONS),Definicoes.CL_GER_CONCESSAO_CREDITOS_CAMP_CONS);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception 
	{
		// Inicializa uma instancia da classe que serah utilizada
		// para conceder bonus aos assinantes que tiveram suas
		// condicoes satisfeitas pela campanha
		this.ajustar = new Ajustar(((GerConcessaoCampanhaProdutor)this.produtor).getIdLog());
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
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception 
	{
	}
	
	/**
	 * Metodo....:getCondicao
	 * Descricao.:Retorna o objeto instanciado da classe de condicao de concessao passada como parametro
	 * @param nomeCondicao - Nome da classe a ser instanciada
	 * @return CondicaConcessao
	 */
	private CondicaoConcessao getCondicao(String nomeCondicao, Campanha campanha)
	{
		CondicaoConcessao condicao = null;
		try
		{
			   Class condClass = Class.forName(nomeCondicao);
			   Class  initParamClass[] = {Campanha.class};
			   Object initParamValue[] = {campanha};
			   Constructor construtor  = condClass.getConstructor(initParamClass);
			   condicao = (CondicaoConcessao)construtor.newInstance(initParamValue);
		}
		catch(Exception e)
		{
			super.log(Definicoes.INFO,"getCondicao","Erro ao instanciar a Condicao de Concessao. Erro:"+e);
		}
		return condicao;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception 
	{
		// Realiza o CAST da informacao passada como parametro para
		// a classe que armazena as informacoes de assinantes inscritos
		// em campanhas
		AssinanteCampanha assinante = (AssinanteCampanha)obj;
		try
		{
			// Para a campanha na qual esse assinante estah inscrito eh
			// verificado quais as classes implementam as CondicoesConcessao
			// para identificar se o assinante deve ou nao receber o bonus
			for (Iterator i=assinante.getCampanha().getCondicoesConcessao().iterator(); i.hasNext();)
			{
				// Instancia a classe que implementarah a CondicaoConcessao
				CondicaoConcessao condicao = getCondicao((String)i.next(),assinante.getCampanha());
				// Verifica se para a implementacao da condicao o assinante
				// deve receber o bonus para essa campanha
				if (condicao.deveSerBonificado(assinante,this.produtor.getConexao()))
				{
					// Executa o ajuste de bonus e caso o ajuste for executado com sucesso
					// entao registra o historico de concessao
					short codRetorno = Definicoes.RET_OPERACAO_OK;
					if ( (codRetorno=executaAjuste(condicao,assinante)) == Definicoes.RET_OPERACAO_OK)
					{
						// Realizado o ajuste o assinante jah pode ser removido da campanha
						// indicando entao a data de retirada. Dessa forma o assinante nao
						// serah mais processado
						AssinanteCampanhaDAO.retiraAssinante(assinante);
						// Registra a concessao de credito
						HistoricoConcessaoDAO.registraConcessao(assinante
								                               ,condicao.getValorConcederSM()
								                               ,condicao.getValorConcederDados()
								                               ,condicao.getValorConcederBonus()
								                               ,condicao.getNomeCondicao()
								                               ,condicao.getDataSatisfacaoCondicao());
						
						// Se o assinante foi bonificado corretamente entao o script
						// de pos-bonificacao eh chamado caso a classe que implemente
						// a condicao de concessao necessite de realizar algo.
						condicao.executarPosBonificacao(assinante,this.produtor.getConexao());
						
						// Busca a instancia do gerente que irah incluir 
						// a mensagem SMS na fila para envio.
						ConsumidorSMS consSMS = ConsumidorSMS.getInstance(super.getIdLog());
						for (Iterator k=assinante.getCampanha().getSmCampanha().iterator(); k.hasNext();)
						{
							SMSCampanha sms = (SMSCampanha)k.next();
							// Insere a mensagem somente se esta for do tipo indicando para ser enviada
							// na concessao de credito para o assinante
							if (Definicoes.TIP_SMS_CAMPANHA_CONCESSAO.equals(sms.getTipoSmsCampanha()))
								consSMS.gravaMensagemSMS(assinante.getMsisdn(),alterarMensagemSMS(sms.getMensagemSMS(),condicao),Definicoes.SMS_PRIORIDADE_UM,"Campanha",super.getIdLog());
						}
					}
					else
						super.log(Definicoes.WARN,"execute","Assinante "+assinante.getMsisdn()+" deve receber bonus por campanha e retornou erro:"+codRetorno);
				}
				else
					// Se o assinante nao deve ser bonificado, significa que o mesmo nao
					// obteve as condicoes necessarias para receber os bonus entao envia
					// uma mensagem SMS avisando da campanha e o que precisa para receber
					// os bonus correspondentes
					trataEnvioMensagem(assinante);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"execute"
					 ,"Erro ao processar concessao de bonus para o assinante "+assinante.getMsisdn()+
					  " da campanha "+assinante.getCampanha().getNomeCampanha()+". Erro:"+e);
		}
	}
	
	/**
	 * Metodo....:alterarMensagemSMS
	 * Descricao.:Altera a mensagem SMS original procurando na mensagem TAGs preconfiguradas para trocar
	 *            pelos valores de condicao de concessao obtidos na campanha
	 * @param msgOriginal	- String contendo a mensagem original
	 * @param condicao		- Condicao de concessao satisfeita para buscar os valores
	 * @return String		- Mensagem SMS com as TAGs substituidas por valores
	 */
	private String alterarMensagemSMS(String msgOriginal, CondicaoConcessao condicao)
	{
		// Realiza uma substituicao de certas TAGs encontradas na mensagem texto para
		// os valores respectivos de bonus a serem associados. A substituicao eh repetitiva
		// no metodo para evitar criar um Map para cada assinante que satisfazer a condicao
		// de concessao tentando entao melhorar a performance
		msgOriginal = msgOriginal.replaceAll("<valorBonus>"		, this.df.format(condicao.getValorConcederBonus()));
		msgOriginal = msgOriginal.replaceAll("<valorBonusSM>"	, this.df.format(condicao.getValorConcederSM()));
		msgOriginal = msgOriginal.replaceAll("<valorBonusDados>", this.df.format(condicao.getValorConcederDados()));
		
		return msgOriginal;
	}
	
	/**
	 * Metodo....:executaAjuste
	 * Descricao.:Executa o ajuste na plataforma Tecnomen registrando a concessao na tabela de recargas
	 * @param condicao	- Condicao que estah sendo satisfeita pelo assinante
	 * @param assinante - Qual assinante e campanha concedendo o bonus
	 * @return short	- Codigo de retorno da execucao do ajuste
	 * @throws Exception
	 */
	private short executaAjuste(CondicaoConcessao condicao, AssinanteCampanha assinante) throws Exception
	{
		// Valida se os valores sao diferentes de 0.0, caso contrario,
		// retorna Operacao_OK
		if (condicao.getValorConcederBonus() != 0 ||
			condicao.getValorConcederDados() != 0 ||
			condicao.getValorConcederSM()	 != 0 )
		{
			// Jah que o assinante entao satisfez a condicao que a campanha
			// impos, entao realiza o ajuste nos saldos de SMS e de Dados 
			// para a concessao de bonus
			
			
			ValoresRecarga valoresRecarga = new ValoresRecarga(0.0, 
															   0.0, 
															   condicao.getValorConcederBonus(),
															   condicao.getValorConcederSM(),
															   condicao.getValorConcederDados());
			return this.ajustar.executarAjuste(assinante.getMsisdn(),
											   assinante.getCampanha().getTipoTransacao(),
											   Definicoes.TIPO_CREDITO_REAIS,
											   valoresRecarga,
											   Definicoes.TIPO_AJUSTE_CREDITO,
											   Calendar.getInstance().getTime(),
											   Definicoes.SO_GPP,
											   Definicoes.GPP_OPERADOR,
											   null,
											   "Ajuste para campanha promocional",
											   true,
                                               null);
		}
		
		return Definicoes.RET_OPERACAO_OK;
	}

	/**
	 * Metodo....:trataEnvioMensagem
	 * Descricao.:Este metodo realiza a verificacao se o assinante
	 *            deve receber mensagem SMS para avisa-lo sobre
	 *            a campanha
	 * @param assinante a receber o SMS
	 */
	private void trataEnvioMensagem(AssinanteCampanha assinante)
	{
		// Verifica se entre a data atual e a data do ultimo envio de sms para o assinante se 
		// este completou o numero de dias de periodicidade cadastrado para essa campanha. Se
		// esse valor foi alcancado entao envia o SMS de aviso (o mesmo enviado na inscricao
		// da campanha) e registra na tabela esse ultimo envio
		// Esse procedimento eh executado para todas as mensagens cadastradas para a campanha
		try
		{
			ConsumidorSMS consSMS = ConsumidorSMS.getInstance(super.getIdLog());
			long msPorDia = 1000 * 60 * 60 * 24;
			long dias = (Calendar.getInstance().getTimeInMillis() - assinante.getDataUltimoSMS().getTime()) / msPorDia;
			// Realiza a iteracao entre todas as mensagens cadastradas para 
			// a campanha. Para cada uma verifica a questao do dia de periodicidade
			// e envia a mensagem ao assinante se for preciso
			for (Iterator i=assinante.getCampanha().getSmCampanha().iterator(); i.hasNext();)
			{
				SMSCampanha sm = (SMSCampanha)i.next();
				if (dias >= sm.getDiasPeriodicidade() && Definicoes.TIP_SMS_CAMPANHA_INSCRICAO.equals(sm.getTipoSmsCampanha()))
				{
					consSMS.gravaMensagemSMS(assinante.getMsisdn()
							                ,sm.getMensagemSMS()
							                ,Definicoes.SMS_PRIORIDADE_UM
							                ,"Campanha"
							                ,super.getIdLog());
					// Apos o envio do SMS, marca a data de envio para
					// processamento futuro caso esse assinante ainda
					// nao obtenha as condicoes necessarias para receber
					// o bonus da campanha
					AssinanteCampanhaDAO.marcaEnvioSMS(assinante);
				}
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"trataEnvioMensagem","Erro ao enviar SM de aviso sa campanha para o assinante "+assinante.getMsisdn()+".Erro:"+e);
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
	}
}
