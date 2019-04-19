package br.com.brasiltelecom.ppp.action.relatorioAdesaoOfertaNatal;


/**
 * ###### NAO USADO!! #####
 * 
 * Monta uma ComboBox de Servicos para o filtro do relatorioAdesaoOfertaNatal.
 * 
 * @author Jorge Abreu
 * @since 15/12/2007
 */
public class ShowComboServico //extends ShowActionHibernate 
{	    
    /*
	private String codOperacao = Constantes.MENU_ADESAO_OFERTA_NATAL;	
	Logger logger = Logger.getLogger(this.getClass());
	    
	public String getTela() 
	{
		return "montaComboServico.vm";
	}

	public void updateVelocityContext(VelocityContext context, HttpServletRequest request, SessionFactory sessionFactory) throws Exception 
	{
		Session session = null;
			
		try
		{
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			
			Collection colecao = new ArrayList();
			colecao = ServicoAssinanteDAO.findServicos(session, request.getParameter("tipo"));
	        
			context.put("resultado", colecao);
		
			session.close();
		}
		catch(Exception e)
		{
			if(session != null)
				session.close();
			logger.info("Erro ao inicializar filtro de consulta para o relatorio de Adesao da Oferta de Natal.");
		}
	}		

	public String getOperacao() 
	{
		return codOperacao;
	}
    */

}
