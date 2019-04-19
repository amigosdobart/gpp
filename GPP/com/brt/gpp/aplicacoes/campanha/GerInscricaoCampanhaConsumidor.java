package com.brt.gpp.aplicacoes.campanha;

import java.lang.reflect.Constructor;
import java.util.Iterator;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.campanha.GerInscricaoCampanhaProdutor;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.InscricaoAssinanteVO;
import com.brt.gpp.aplicacoes.campanha.entidade.SMSCampanha;
import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.ConsumidorSMS;

/**
 * Esta classe representa o processo de inscricao de assinantes em campanhas 
 * promocionais. Esta classe tem responsabilidade de verificar se um determinado 
 * assinante, sendo processado, tem condicoes de participar da campanha atraves de 
 * seus parametrosInscricao cadastrados para a campanha. Se caso algum parametro 
 * cadastra-lo entao o registro eh efetuado na tabela AssinanteCampanha.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class GerInscricaoCampanhaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor 
{
	private ProcessoBatchProdutor produtor;

	public GerInscricaoCampanhaConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerInscricaoCampanhaConsumidor.class).getIdProcesso(Definicoes.CL_GER_INSCRICAO_ASSIN_CAMP_CONS),Definicoes.CL_GER_INSCRICAO_ASSIN_CAMP_CONS);
	}
	
	/**
	 * @see ProcessoBatchConsumidor.startup
	 * @throws java.lang.Exception
	 */
	public void startup() throws Exception 
	{
	}
	
	/**
	 * @see ProcessoBatchConsumidor.startup
	 * @param produtor
	 * @throws java.lang.Exception
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception 
	{
		this.produtor = produtor;
		startup();
	}
	
	/**
	 * @see ProcessoBatchConsumidor.startup
	 * @param produtor
	 * @throws java.lang.Exception
	 */
	public void startup(Produtor produtor) throws Exception 
	{
		startup();
	}
	
	/**
	 * Metodo....:getParametroInscricao
	 * Descricao.:Retorna uma instancia da classe parametro inscricao
	 * @param nomeParametro
	 * @param campanha
	 * @return
	 */
	private ParametroInscricao getParametroInscricao(String nomeParametro, Campanha campanha)
	{
		ParametroInscricao parametro = null;
		try
		{
			Class paramClass = Class.forName(nomeParametro);
			Class  initParamClass[] = {Campanha.class};
			Object initParamValue[] = {campanha};
			Constructor construtor  = paramClass.getConstructor(initParamClass);
			parametro = (ParametroInscricao)construtor.newInstance(initParamValue);
		}
		catch(Exception e)
		{
			super.log(Definicoes.INFO,"getParametroInscricao","Erro ao instanciar o Parametro de Inscricao"+nomeParametro+". Erro:"+e);
		}
		return parametro;
	}
	
	/**
	 * @see ProcessoBatchConsumidor.execute
	 * @param obj
	 * @throws java.lang.Exception
	 */
	public void execute(Object obj) throws Exception 
	{
		// Realiza o CAST para o objeto passado como parametro para a classe Assinante
		InscricaoAssinanteVO vo = (InscricaoAssinanteVO)obj;
		// Para cada campanha, verifica se o assinante pode ser inscrito
		// nas classes de parametro de inscricao. Caso positivo entao
		// o assinante eh registrado na tabela de AssinantesCampanha indicando
		// que o mesmo pode receber entao os bonus caso as condicoes forem
		// satisfeitas
		for (Iterator i=vo.getListaCampanhas().iterator(); i.hasNext();)
		{
			Campanha campanha = (Campanha)i.next();
			// Busca a lista de parametros de inscricao
			for (Iterator j=campanha.getParametrosInscricao().iterator(); j.hasNext();)
			{
				// Busca o objeto jah instanciado da classe de parametro inscricao
				// para validar se o assinante pode ser inscrito na campanha
				ParametroInscricao parametro = getParametroInscricao((String)j.next(), campanha);
				try
				{
					// Se o assinante pode ser inscrito na campanha entao realiza isso incluindo
					// o numero MSISDN na tabela relacionando com qual campanha eh a inscricao.
					// Apos a inscricao um mensagem sms eh enviado ao assinante para que este saiba
					// que jah estah elegivel pela campanha
					if ( parametro.podeSerInscrito(vo.getAssinante(), this.produtor.getConexao()) )
					{
						// Registra o assinante na campanha
						AssinanteCampanhaDAO.insereAssinante(vo.getAssinante().getMSISDN(),campanha,parametro.getParametros());
						
						// Envia o SMS de aviso. Note que o SMS de aviso na inscricao do
						// assinante será enviado somente quando os parametros de inscrição
						// indicarem que deve ser enviado.
						long idProcesso = ((GerInscricaoCampanhaProdutor)produtor).getIdLog();
						// Verifica se o SMS deverá ser enviado.
						if (parametro.enviaSMS(vo.getAssinante(), this.produtor.getConexao())) 
						{
							// Busca a instancia do gerente que irah incluir 
							// a mensagem SMS na fila para envio.
							ConsumidorSMS consSMS = ConsumidorSMS.getInstance(idProcesso);
							for (Iterator k=campanha.getSmCampanha().iterator(); k.hasNext();)
							{
								SMSCampanha sms = (SMSCampanha)k.next();
								// Insere a mensagem somente se esta for do tipo indicando para ser enviada
								// na inscricao do assinante
								if (Definicoes.TIP_SMS_CAMPANHA_INSCRICAO.equals(sms.getTipoSmsCampanha()))
									consSMS.gravaMensagemSMS(vo.getAssinante().getMSISDN(),sms.getMensagemSMS(),Definicoes.SMS_PRIORIDADE_UM,"Campanha",idProcesso);
							}
						}
					}
				}
				catch(Exception e)
				{
					super.log(Definicoes.INFO,"execute","Parametro:"+parametro.getClass().getName()+". Parametro nao conseguiu inscrever o assinante:"+vo.getAssinante().getMSISDN()+".Erro:"+e);
				}
			}
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
	}
}
