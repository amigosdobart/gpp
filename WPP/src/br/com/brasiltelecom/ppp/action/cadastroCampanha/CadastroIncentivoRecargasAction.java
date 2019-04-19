package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CampanhaHome;
import br.com.brasiltelecom.ppp.portal.entity.Campanha;
import br.com.brasiltelecom.ppp.portal.entity.CondIncentivoRecargas;
import br.com.brasiltelecom.ppp.portal.entity.CondicaoCampanha;
import br.com.brasiltelecom.ppp.portal.entity.ParamIncentivoRecargas;
import br.com.brasiltelecom.ppp.portal.entity.ParametroCampanha;
import br.com.brasiltelecom.ppp.portal.entity.SMSCampanha;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;


/**
 * @author Joao Carlos
 */
public class CadastroIncentivoRecargasAction extends ActionPortal
{
	private Logger logger = Logger.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		logger.info("Cadastra a campanha de incentivo de recargas");
		String msgRetorno = "Campanha inserida/alterada com sucesso.";
		int    codRetorno = 0;
		try
		{
			// Identifica os valores cadastrados para declaracao da campanha
			// e cria o objeto que irah armazenar os valores para insercao
			Campanha campanha = new Campanha();
			String idCampanha   = request.getParameter("idCampanha");
			String timeCampanha = request.getParameter("timeCampanha");
			campanha.setId				(!idCampanha.equals("")   ? Long.parseLong(idCampanha)                           :0);
			campanha.jdoSetTimeStamp	(!timeCampanha.equals("") ? Long.parseLong(request.getParameter("timeCampanha")) :0);
			campanha.setNomeCampanha	(request.getParameter("nomeCampanha"));
			campanha.setValidadeInicial	(sdf.parse(request.getParameter("dataInicial")));
			campanha.setValidadeFinal	(sdf.parse(request.getParameter("dataFinal")));
			
			// Identifica os valores preenchidos para tratamento dos parametros
			// de inscricao de assinantes nas campanhas. O array para a informacao
			// de status indica o numero de linhas inseridos na tabela para esses
			// parametros, portanto utiliza o tamanho desse array como base para
			// a pesquisa das outras informacoes
			Collection paramIncentRecargas = getParametrosIncentivo(campanha,request);
			
			// Identifica os valores preenchidos para o tratamento de condicoes
			// de concessao para os assinantes registrados nas campanhas. Essas
			// informacoes sao a respeito da campanha de incentivo de recargas
			// O array contendo os valores de recarga sao base para identificar
			// as linhas cadastradas sobre os bonus
			Collection condIncentRecargas = getCondicoesIncentivo(campanha,request);
			
			// Identifica os valores cadastrados para serem as mensagens SMS
			// a serem enviadas para os assinantes registrados nas campanhas
			// essas mensagens sao independentes da campanha apesar do seu
			// cadastramento ser especifico (como o caso da campanha de incentivo
			// de recargas, esta tela)
			campanha.setSmCampanha(getSMSCampanha(campanha,request));
			
			// Define de forma estatica as classes que implementam a interface
			// de Parametro de inscricao e de Concessao de credito para a campanha
			// de incentivo de recargas que serah utilizado pelo processo de 
			// gerenciamento de inscricao e concessao respectivamente
			campanha.setParametrosInscricao(getParametrosInscricao(campanha));
			
			// Define de forma estatica tambem as classes que 
			// implementam a interface de Condicoes de concessao
			campanha.setCondicoesConcessao(getCondicoesConcessao(campanha));
			
			// Realiza o insert no banco de dados atraves do CASTOR
			// caso o ID da campanha seja 0 indicando um novo registro,
			// senao realiza a alteracao dos dados cadastrados
			if (campanha.getId() != 0)
				CampanhaHome.atualizaIncentivoRecargas(campanha,paramIncentRecargas,condIncentRecargas,db);
			else 
				CampanhaHome.insereIncentivoRecargas(campanha,paramIncentRecargas,condIncentRecargas,db);
		}
		catch(Exception e)
		{
			msgRetorno = "Erro ao realizar tratamento para insercao/alteracao da campanha de incentivo de recargas. Erro:"+e.getMessage();
			codRetorno = 1;
			logger.error(msgRetorno);
		}
		request.setAttribute("codRetorno",new Integer(codRetorno));
		request.setAttribute("msgRetorno",msgRetorno);
		return actionMapping.findForward("success");
	}

	/**
	 * Metodo....:getCondicoesConcessao
	 * Descricao.:Retorna as classes que implementam as condicoes de concessao para essa campanha
	 * @param campanha - Campanha
	 * @return
	 */
	private Collection getCondicoesConcessao(Campanha campanha) throws Exception
	{
		Collection condicoesConcessao = new ArrayList();
		CondicaoCampanha cConc = new CondicaoCampanha();
		cConc.setCampanha(campanha);
		cConc.setNomeClasse("com.brt.gpp.aplicacoes.campanha.incentivoRecargas.CondIncentivoRecargasImpl");
		condicoesConcessao.add(cConc);
		
		return condicoesConcessao;
	}

	/**
	 * Metodo....:getParametrosInscricao
	 * Descricao.:
	 * @param campanha
	 * @return
	 * @throws Exception
	 */
	private Collection getParametrosInscricao(Campanha campanha) throws Exception
	{
		Collection parametrosInscricao = new ArrayList();
		ParametroCampanha pInsc = new ParametroCampanha();
		pInsc.setCampanha(campanha);
		pInsc.setNomeClasse("com.brt.gpp.aplicacoes.campanha.incentivoRecargas.ParamIncentivoRecargasImpl");
		parametrosInscricao.add(pInsc);
		
		return parametrosInscricao;
	}

	/**
	 * Metodo....:getParametrosIncentivo
	 * Descricao.:Retorna a lista de parametros para o incentivo de recarga
	 *            que o assinante incluiu na lista pela tela de cadastro
	 * @param request 
	 * @return
	 * @throws Exception
	 */
	private Collection getParametrosIncentivo(Campanha camp,HttpServletRequest request) throws Exception
	{
		Collection params = new ArrayList();
		String status[] = request.getParameterValues("statusAssinante");
		String diasIni[]= request.getParameterValues("diasExpiracaoIni");
		String diasFim[]= request.getParameterValues("diasExpiracaoFim");
		String rollOut[]= request.getParameterValues("indRollOut");
		String id[]     = request.getParameterValues("idParametro");
		String remover[]= request.getParameterValues("remParametro");
		
		if (status == null)
			throw new IllegalArgumentException("E obrigatorio o cadastro de parametros de inscricao para a campanha");
		
		for (int i=0; i < status.length; i++)
		{
			ParamIncentivoRecargas param = new ParamIncentivoRecargas();
			param.setCampanha			(camp);
			param.setId					(Integer.parseInt(id[i]));
			param.setStatusAssinante	(Integer.parseInt(status[i]));
			param.setDiasExpiracaoIni	(Integer.parseInt(diasIni[i]));
			param.setDiasExpiracaoFim	(Integer.parseInt(diasFim[i]));
			param.setFezRollOut			(rollOut[i].equals("S") ? 1 : 0);
			param.setDeveRemover		((remover != null && remover[i].equals("true")) ? true : false);
			
			params.add(param);
		}
		return params;
	}
	
	/**
	 * Metodo....:getCondicoesIncentivo
	 * Descricao.:Retorna a lista de condicoes de concessao para a campanha de incentivo
	 *            de recargas que o assinante cadastrou na tela
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Collection getCondicoesIncentivo(Campanha camp,HttpServletRequest request) throws Exception
	{
		Collection conds = new ArrayList();
		String valorRecarga[] 	= request.getParameterValues("valorRecarga");
		String valorBonus[]     = request.getParameterValues("valorBonus");
		String valorBonusSM[]   = request.getParameterValues("valorBonusSM");
		String valorBonusDados[]= request.getParameterValues("valorBonusDados");
		String id[]				= request.getParameterValues("idCondicao");
		String remover[]		= request.getParameterValues("remCondicao");
		
		if (valorRecarga == null)
			throw new IllegalArgumentException("E obrigatorio o cadastro de condicoes de concessao para a campanha");
		
		for (int i=0; i < valorRecarga.length; i++)
		{
			// Os campos de valores de recarga e bonus digitados na tela pelo
			// usuario possuem o caracter ',' como separador de decimais, portanto
			// o codigo irah realizar a troca desse caracter para o caracter '.'
			// afim de que o java entenda esse valor como Double. Nenhum outro
			// artificio de conversao como o DecimalFormat eh utilizado pois como
			// o campo tem tamanho fixo entao nao existe o separador de milhares 
			// existindo somente esse separador de decimais evitando entao qq problema
			CondIncentivoRecargas cond = new CondIncentivoRecargas();
			cond.setCampanha		(camp);
			cond.setId				(Integer.parseInt(id[i]));
			cond.setValorRecarga	(Double.parseDouble(valorRecarga[i].replace(',','.')));
			cond.setValorBonus  	(!valorBonus[i].equals("")      ? Double.parseDouble(valorBonus[i].replace(',','.'))      : 0);
			cond.setValorBonusSM	(!valorBonusSM[i].equals("")    ? Double.parseDouble(valorBonusSM[i].replace(',','.'))    : 0);
			cond.setValorBonusDados	(!valorBonusDados[i].equals("") ? Double.parseDouble(valorBonusDados[i].replace(',','.')) : 0);
			cond.setDeveRemover		((remover != null && remover[i].equals("true")) ? true : false);
			
			conds.add(cond);
		}
		return conds;
	}
	
	/**
	 * Metodo....:getSMSCampanha
	 * Descricao.:Retorna as mensagens cadastradas para a campanha pelo usuario
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Collection getSMSCampanha(Campanha camp,HttpServletRequest request) throws Exception
	{
		Collection sms = new ArrayList();
		String mensagemSMS[] 		= request.getParameterValues("mensagemSMS");
		String diasPeriodicidade[]  = request.getParameterValues("diasPeriodicidade");
		String id[]					= request.getParameterValues("idMensagem");
		//String remover[]			= request.getParameterValues("remMensagem");
		
		for (int i=0; i < (mensagemSMS != null ? mensagemSMS.length :0); i++)
		{
			SMSCampanha smsCamp = new SMSCampanha();
			smsCamp.setId				(i);
			smsCamp.setCampanha			(camp);
			smsCamp.setMensagemSMS		(mensagemSMS[i]);
			smsCamp.setDiasPeriodicidade(Integer.parseInt(diasPeriodicidade[i]));
			smsCamp.setId				(Integer.parseInt(id[i]));
			
			sms.add(smsCamp);
		}
		return sms;
	}
	
	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CADASTRAR_CAMPANHA;
	}
}
