package br.com.brasiltelecom.ppp.action.relatorioFaleGratisOrelhao;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.CodigoNacionalDAO;
import br.com.brasiltelecom.ppp.dao.OperadoraLDDAO;

/**
 * Mostra a tela de Filtro do Relat�rio Fale de Graca no Orelhao
 * 
 * @author Jorge Abreu
 * @since 16/11/2007
 */
public class ShowFiltroRelFaleGratisOrelhaoAction extends ShowActionHibernate 
{
	private String codOperacao = Constantes.MENU_FALE_GRATIS_ORELHAO;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroRelFaleGratisOrelhao.vm";
	}


	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		Session session = null;
		
		try
		{
			session = sessionFactory.getCurrentSession();
	        session.beginTransaction();
	        
	        
			TreeMenu menu = new TreeMenu("MenuParametros");
			
			GrupoItem grupoCod 		 = new GrupoItem("Codigos Nacionais");
			GrupoItem grupoCategorias 	 = new GrupoItem("Categorias");
			GrupoItem grupoCsp 	 = new GrupoItem("CSP");
			
			// Monta Categorias
			Object[] categorias = CategoriaDAO.findAll(session).toArray();
			Categoria categoria = null;
			
			
			for (int i = 0; i < categorias.length; i++)
			{
				categoria = ((Categoria)categorias [i]);
				grupoCategorias.addItem(new OpcaoItem(categoria.getDesCategoria(), "CATEGORIA", ""+categoria.getIdCategoria(), false));
			
			}
			
			// Monta C�digos Nacionais
			CodigoNacional cod = null;
			Object[] codigos = CodigoNacionalDAO.findByRegiaoBrt(session, 1).toArray();
			
			for (int i = 0; i < codigos.length; i++)
			{
				cod = ((CodigoNacional)codigos[i]);
				grupoCod.addItem(new OpcaoItem(cod.getIdtCodigoNacional() + " - " + cod.getIdtUf(), 
											   "CN", "" + cod.getIdtCodigoNacional(), false));
			}
			
			// Monta CSP
			OperadoraLD oper = null;
			Object[] operadoras = OperadoraLDDAO.findAll(session).toArray();
			
			for (int i = 0; i < operadoras.length; i++)
			{
				oper = ((OperadoraLD)operadoras[i]);
				grupoCsp.addItem(new OpcaoItem( oper.getNomeOperadora() + " (" + oper.getCsp() + ")", "CSP", "" + oper.getCsp(), false));
			}
			
			menu.addItem(grupoCategorias);
			menu.addItem(grupoCod);
			menu.addItem(grupoCsp);
			
			context.put("menuParametros", menu.escapeToJS(menu.toXML()));
			
			session.getTransaction().commit();	
		}
		catch (Exception e)
		{
			e.printStackTrace();
			context.put("erro", "N�o foi poss�vel realizar a consulta pelos dados de filtros, problemas na conex�o com o banco de dados. " + e.getMessage());
			logger.error("N�o foi poss�vel realizar a consulta pelos dados de filtros, problemas na conex�o com o banco de dados. " + e);	
			if (session != null && session.isOpen()) 
				session.getTransaction().rollback();
		}
		
	}

	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}