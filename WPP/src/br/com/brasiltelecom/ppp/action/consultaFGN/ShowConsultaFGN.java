package br.com.brasiltelecom.ppp.action.consultaFGN;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteSegundos;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.PromocaoDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoDiaExecucaoDAO;
import br.com.brasiltelecom.ppp.dao.PromocaoLimiteSegundosDAO;
import br.com.brasiltelecom.ppp.dao.TotalizacaoFaleGratisDAO;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaPromocaoPulaPulaGPP;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.model.RetornoPulaPula;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta informações de promoção Fale de Graça à Noite.
 *
 * @author Bernardo Vergne Dias
 * Data: 30-05-2007
 */
public class ShowConsultaFGN extends ShowActionHibernate
{
	private String codOperacao = Constantes.COD_CONSULTA_FGN;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "consultaFGN.vm";
	}

	/**
	 * Implementa a lógica de negócio dessa ação.
	 *
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;

		try
		{
         	String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
			String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);

			String msisdn = request.getParameter("msisdn");
			context.put("msisdn", msisdn);

			// Realiza consulta de assinante no GPP
			consultaAssinanteGPP(context, msisdn, servidor, porta);

			// Realiza consulta de limite Fale Gratis via Hibernate
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
			// Realiza consulta de pula pula no GPP
			RetornoPulaPula pulaPula = consultaPulaPulaGPP(context, msisdn, servidor, porta, session);
			consultaDadosFGN(context, session, pulaPula, msisdn);

	        session.getTransaction().commit();
		}
		catch (Exception e)
		{
			context.put("erro", "Erro ao realizar a consulta. " + e.getMessage());
			logger.error("Erro ao realizar a consulta. " + e);
			if (session != null && session.isOpen())
				session.getTransaction().rollback();
		}
	}

	/**
	 * Realiza consulta de assinante no GPP. Transfere para a VM os dados obtidos.
	 */
	private void consultaAssinanteGPP(VelocityContext context, String msisdn,
			String servidor, String porta) throws Exception
	{
		Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante("55" + msisdn, servidor, porta);

		if (assinante == null)
			throw new Exception("Problemas de comunicação com a plataforma.");

		if (assinante.getRetorno().equals("0002"))
			throw new Exception("Assinante não encontrado.");

		if (!assinante.getRetorno().equals("0000"))
			throw new Exception("");

		assinante.setMsisdn(msisdn); // Fix: método comentado dentro de getAssinante()
		context.put("assinante", assinante);
	}

	/**
	 * Realiza consulta de pula pula no GPP. Transfere para a VM os dados obtidos.
	 */
	private RetornoPulaPula consultaPulaPulaGPP(VelocityContext context, String msisdn,
			String servidor, String porta, Session session) throws Exception
	{
		RetornoPulaPula pulaPula = ConsultaPromocaoPulaPulaGPP.getPromocao("55" + msisdn, servidor, porta, session);

		if(pulaPula == null)
			throw new Exception("Problemas de comunicação com a plataforma.");

		if(pulaPula.getRetorno().equals("0065"))
		{
			context.put("msgPulaPula", "Assinante não pertence a uma promoção Pula-Pula.");
			pulaPula = null;
		}
		else if (pulaPula.getRetorno().equals("0058"))
		{
			context.put("msgPulaPula", "Assinante pendente de primeira recarga.");
		}
		else if (!pulaPula.getRetorno().equals("0000"))
		{
			// qualquer erro diferente de 0065 e 0058
			context.put("msgPulaPula", "Erro ao consultar dados de Pula-Pula. Código de retorno: " +
					pulaPula.getRetorno());
			pulaPula = null;
		}

		context.put("pulaPula", pulaPula);

		return pulaPula;
	}

	/**
	 * Realiza consulta de limite Fale Gratis via Hibernate.
	 * Transfere para a VM os dados obtidos.
	 */
	private void consultaDadosFGN(VelocityContext context, Session session,
			RetornoPulaPula pulaPula, String msisdn) throws Exception
	{
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
     	SimpleDateFormat sdf2 = new SimpleDateFormat("MM/yyyy");
     	SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
     	DecimalFormat nf = new DecimalFormat("##0.00", new DecimalFormatSymbols(new Locale("pt","BR")));

		if (pulaPula == null) return;

        Promocao promocao = PromocaoDAO.findById(session, Integer.parseInt(pulaPula.getIdentificador()));
        PromocaoLimiteSegundos limiteMaximo = PromocaoLimiteSegundosDAO.findLimiteMaximoByPromocao(session, promocao);

        if (limiteMaximo != null && limiteMaximo.getNumSegundos() > 0)
        {
        	Calendar cal = Calendar.getInstance();

        	// Determina o dia de execução do fale gratis (para calculo do periodo do assinante)

        	PromocaoDiaExecucao promocaoDiaExecucao = PromocaoDiaExecucaoDAO.findById(
        			session, promocao.getIdtPromocao(),
        			Integer.parseInt(pulaPula.getDataEntrada().substring(0,2)),
        			Constantes.PROMOCAO_TIPO_EXECUCAO);

        	if (promocaoDiaExecucao == null)
        		throw new Exception("Erro ao obter período de referência do assinante.");

        	// Verifica se o periodo do assinante é o mes passado

        	int diaExecucao = promocaoDiaExecucao.getNumDiaExecucao().intValue();
        	int diaHoje = cal.get(Calendar.DAY_OF_MONTH);
        	if (diaExecucao > diaHoje) cal.add(Calendar.MONTH, -1);
        	cal.set(Calendar.DAY_OF_MONTH, diaExecucao);

        	// Consulta a totalização

        	TotalizacaoFaleGratis totalizacaoFGN = TotalizacaoFaleGratisDAO.findById(
        			session, "55" + msisdn, sdf1.format(cal.getTime()));

        	// Cria uma totalização Fake caso nao exista contabilização para o periodo corrente

        	if (totalizacaoFGN == null) totalizacaoFGN = new TotalizacaoFaleGratis();
        	double porcentagem = (100.0 * totalizacaoFGN.getNumSegundos()) / limiteMaximo.getNumSegundos();

        	// Gera dados para a VM

        	context.put("diaReferencia", ((diaExecucao < 10) ? "0" : "") + diaExecucao);
        	context.put("periodo", sdf2.format(cal.getTime()));
        	context.put("progresso", new Integer((porcentagem > 100) ? 100 : (int)(porcentagem)));
        	context.put("porcentagemFormatado" , nf.format((porcentagem > 100) ? 100 : porcentagem));
        	context.put("limiteMaximo", limiteMaximo);
        	context.put("limiteMaximoFormatado", new Integer((int)(limiteMaximo.getNumSegundos() / 60)));
        	context.put("totalizacaoFormatado",
        			"" + ((int)(totalizacaoFGN.getNumSegundos() / 60)) + " minutos e " +
        			((int)(totalizacaoFGN.getNumSegundos() % 60)) + " segundos");

        	if (porcentagem < 100)
        		context.put("dataSaida", "Não se aplica. O assinante não atingiu o limite.");
        	else if (totalizacaoFGN.getDatRetiradaFGN() == null)
        		context.put("dataSaida", "Pendente de processamento.");
        	else
        		context.put("dataSaida", sdf3.format(totalizacaoFGN.getDatRetiradaFGN()));
        }
	}

	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
