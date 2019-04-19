/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaComprovanteServico;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.PlanoPrecoDAO;
import br.com.brasiltelecom.ppp.model.RetornoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Mostra a tela com o comprovante de serviços gerado
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowConsultaComprovanteServicoAction extends ShowActionHibernate 
{
    
	private String codOperacao = Constantes.MENU_COMP_SERVICOS;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "consultaComprovanteServico.vm";
	}

	public void updateVelocityContext(VelocityContext context, 
			HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try 
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        //HttpSession session = request.getSession();		
			int page = 0;
			int pageSize = Constantes.PAGE_SIZE;
			//int excel = 0;
			
			//Recupera pagina atual
			if ((request.getAttribute("page") != null) && !("".equals(request.getAttribute("page"))))
			{
			   page = Integer.parseInt(request.getAttribute("page").toString());
			}
			
			//Recuperando o extrato
			RetornoExtrato retornoExtrato = (RetornoExtrato) request.getAttribute(Constantes.RESULT);
			
			//Inserindo os objetos no contexto
			context.put("eventos"				, retornoExtrato.getEventos());

			//Se o assinante for LigMix, colocar no context do velocity
			if (Integer.parseInt(retornoExtrato.getIndAssinanteLigMix())==1)
			{
				context.put("assinanteLigMix"	,retornoExtrato.getIndAssinanteLigMix());
			}
	
			context.put("saldoInicialPrincipal"	, retornoExtrato.getSaldoInicialPrincipalString());
			context.put("saldoFinalPrincipal"	, retornoExtrato.getSaldoFinalPrincipalString());
			context.put("totalCreditosPrincipal", retornoExtrato.getTotalCreditosPrincipalString());
			context.put("totalDebitosPrincipal"	, retornoExtrato.getTotalDebitosPrincipalString());
			
			context.put("saldoInicialBonus"		, retornoExtrato.getSaldoInicialBonusString());
			context.put("saldoFinalBonus"		, retornoExtrato.getSaldoFinalBonusString());
			context.put("totalCreditosBonus"	, retornoExtrato.getTotalCreditosBonusString());
			context.put("totalDebitosBonus"		, retornoExtrato.getTotalDebitosBonusString());
			
			context.put("saldoInicialSMS"		, retornoExtrato.getSaldoInicialSMSString());
			context.put("saldoFinalSMS"			, retornoExtrato.getSaldoFinalSMSString());
			context.put("totalCreditosSMS"		, retornoExtrato.getTotalCreditosSMSString());
			context.put("totalDebitosSMS"		, retornoExtrato.getTotalDebitosSMSString());
			
			context.put("saldoInicialGPRS"		, retornoExtrato.getSaldoInicialGPRSString());
			context.put("saldoFinalGPRS"		, retornoExtrato.getSaldoFinalGPRSString());
			context.put("totalCreditosGPRS"		, retornoExtrato.getTotalCreditosGPRSString());
			context.put("totalDebitosGPRS"		, retornoExtrato.getTotalDebitosGPRSString());
			
			context.put("saldoInicialPeriodico"		, retornoExtrato.getSaldoInicialPeriodicoString());
			context.put("saldoFinalPeriodico"		, retornoExtrato.getSaldoFinalPeriodicoString());
			context.put("totalCreditosPeriodico"	, retornoExtrato.getTotalCreditosPeriodicoString());
			context.put("totalDebitosPeriodico"		, retornoExtrato.getTotalDebitosPeriodicoString());

			context.put("saldoInicialTotal"		, retornoExtrato.getSaldoInicialTotalString());
			context.put("saldoFinalTotal"		, retornoExtrato.getSaldoFinalTotalString());
			context.put("totalCreditosTotal"	, retornoExtrato.getTotalCreditosTotalString());
			context.put("totalDebitosTotal"		, retornoExtrato.getTotalDebitosTotalString());

			context.put("plano"					, retornoExtrato.getPlano());
			context.put("dataAtivacao"			, retornoExtrato.getDataAtivacao());
			context.put("msisdn"				, request.getAttribute("msisdn"));
			context.put("msisdnFormatado"		, request.getAttribute("msisdnFormatado"));
			context.put("dataInicial"			, request.getAttribute("dataInicial"));
			context.put("dataFinal"				, request.getAttribute("dataFinal"));

			Iterator extratos = retornoExtrato.getExtratos().iterator();
			ArrayList al =  new ArrayList();
			while (extratos.hasNext())
			{
				al.add(extratos.next());
			}
			context.put("extratos"				, Util.getPagedItem(page,pageSize,al));
			
		    //Recupera collection de paginas
			context.put("paginas"				, Util.getPages(retornoExtrato.getExtratos().size(),pageSize)); 
		    //Coloca a pagina atual
			context.put("page"					, new Integer(page));
			//Coloca o tamanho da pagina
			context.put("pageSize"				, new Integer(pageSize));
		    //coloca o total
			context.put("total"					, new Integer(retornoExtrato.getExtratos().size()));
			context.put("tamanho"				, new Integer(retornoExtrato.getExtratos().size()+retornoExtrato.getEventos().size()));
			
			if (page == Util.getPages(retornoExtrato.getExtratos().size(),pageSize).size() - 1)
				context.put("mostra"			,"S");
			else 
				context.put("mostra"			,"N");
			
	        //	Cria o objeto PlanoPreco e verifica quais tipos de saldos pertencem a categoria do assinante
			PlanoPreco planoPreco = PlanoPrecoDAO.findById(session, retornoExtrato.getPlanoPreco());
			Collection tipoSaldo = planoPreco.getCategoria().getTiposSaldo();

			for(Iterator iterator = tipoSaldo.iterator(); iterator.hasNext();)
			{
                SaldoCategoria saldoCat = (SaldoCategoria)iterator.next();
				TipoSaldo saldo = saldoCat.getTipoSaldo();
				
				// Verifica quais tipos de saldos deve preencher
				if(saldo.getIdtTipoSaldo() == TipoSaldo.PRINCIPAL)	
                {
                    context.put("mostrarPrincipal", "true");
                    context.put("nomeSaldoPrincipal", saldoCat.getNomSaldo());
                }
				if(saldo.getIdtTipoSaldo() == TipoSaldo.BONUS)	
                {
                    context.put("mostrarBonus", "true");
                    context.put("nomeSaldoBonus", saldoCat.getNomSaldo());
                }
				if(saldo.getIdtTipoSaldo() == TipoSaldo.PERIODICO)	
                {
                    context.put("mostrarPeriodico", "true");
                    context.put("nomeSaldoPeriodico", saldoCat.getNomSaldo());
                }
				if(saldo.getIdtTipoSaldo() == TipoSaldo.TORPEDOS)	
                {
                    context.put("mostrarTorpedos", "true");
                    context.put("nomeSaldoTorpedos", saldoCat.getNomSaldo());
                }
				if(saldo.getIdtTipoSaldo() == TipoSaldo.DADOS)		
                {
                    context.put("mostrarDados", "true");	
                    context.put("nomeSaldoDados", saldoCat.getNomSaldo());
                }
			}
			
	        session.getTransaction().commit();
		}
		catch(Exception e)
		{
			context.put("erro", "Erro ao consultar comprovante de servico. <br><br>" + e);
			logger.error("Erro ao consultar comprovante de servico. " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
}
