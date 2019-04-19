package br.com.brasiltelecom.ppp.action.consultaExtratoBoomerang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.PromocaoAssinanteDAO;
import br.com.brasiltelecom.ppp.model.RetornoExtratoBoomerang;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.session.util.Util;

/**
 * Mostra a tela com o comprovante de serviços gerado
 * 
 * @author Henrique Canto
 * @since 21/05/2004
 */
public class ShowConsultaExtratoBoomerangAction extends ShowActionHibernate {
	
	Logger logger = Logger.getLogger(this.getClass());
	public String getTela() 
	{
		return "consultaExtratoBoomerang.vm";
	}
		
	public String getOperacao() 
	{
		return null;
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		String msisdn = (String) request.getAttribute("msisdn");
		String indCadastroPromocaoBumerangue = "NAO";
		int page = 0 ;
		int pagesize = 90;
		
		// recupera pagina atual
		if (request.getAttribute("page") != null && !"".equals(request.getAttribute("page")))
		{
		   page = Integer.parseInt(request.getAttribute("page").toString());
		}
		
		RetornoExtratoBoomerang result = (RetornoExtratoBoomerang) request.getAttribute(Constantes.RESULT);
		
		try
		{
			// Inicia a sessão do hibernate
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			// Pegua as promoções do Assinante consultado
			Collection c = PromocaoAssinanteDAO.findByMsisdn(session, msisdn);
			// Para cada promoção conultada no banco de dados, verifica se 
			// alguma delas pertence a categoria Bumerangue 14.
			for (Iterator i = c.iterator();i.hasNext();)
			{
				// Monta o objeto promocao
				Promocao promocao = (Promocao) i.next(); 
				// Verifica se a categoria da promoção é a Bumerangue 14
				if (promocao.getCategoria().getIdtCategoria() == Constantes.CATEGORIA_BUMERANGUE_14)
					indCadastroPromocaoBumerangue = "SIM";
			}
			// Fecha a sessão do Hibernate
			session.close();
		}
		catch(Exception e)
		{
			context.put("erro", "Erro ao consultar Informações de promoção Bumerangue . <br><br>" + e);
			logger.error("Erro ao consultar Informações de promoção Bumerangue. " + e.getMessage());	
			if (session != null) session.getTransaction().rollback();
		}
		
		context.put("indCadastroPromocaoBumerangue", indCadastroPromocaoBumerangue);
		context.put("totalBoomerang", result.getTotalBoomerang());
		context.put("numeroChamadas", result.getNumeroChamadas());
		context.put("numeroMinutos" , result.getNumeroMinutos());
		context.put("indRecarga"   , result.getIndicieRecarga().toUpperCase());
		context.put("msisdn"    	 	 , msisdn);
		context.put("obr_msisdn"  		 , request.getAttribute("obr_msisdn"));
		context.put("mes"			, request.getAttribute("mes"));

		Iterator it = result.getExtratos().iterator();
		
		ArrayList al =  new ArrayList();
		
		while (it.hasNext())
		{
			al.add(it.next());
		}
		
		context.put("extratos",Util.getPagedItem(page,pagesize,al));
		//recupera collection de paginas
		context.put("paginas",Util.getPages(result.getExtratos().size(),pagesize)); 
	    //coloca a página atual
		context.put("page",new Integer(page)); 
	    //coloca o total
		context.put("total",new Integer(result.getExtratos().size()));
		context.put("tamanho",new Integer(result.getExtratos().size()));
		
		if (page == Util.getPages(result.getExtratos().size(),pagesize).size() - 1)
		{
			context.put("mostra","S");
		}
		else 
		{
			context.put("mostra","N");
		}
	}
}

