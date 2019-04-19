/*
 * Created on 19/03/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package br.com.brasiltelecom.ppp.action.consultaRecarga;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.dao.AssinanteDAO;
import br.com.brasiltelecom.ppp.dao.HibernateHelper;
import br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargas;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Assinante;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Mostra a tela com o resultado da consulta de recargas/ajustes
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ShowConsultaRecargaAction extends ShowAction 
{	
	private String codOperacao = Constantes.MENU_RECARGAS;
    Logger logger = Logger.getLogger(this.getClass());
    
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "consultaRecarga.vm";
	}
	
	private int contaOK(Collection col, String tipo){
		int contador=0;
		for(Iterator it = col.iterator(); it.hasNext();){
			InterfaceRecargas recarga = (InterfaceRecargas) it.next();
			if(tipo.equalsIgnoreCase(recarga.getStatus())){
				contador++;
			}	
		}
		return contador;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception {
			//HttpSession session = request.getSession();
			Collection result = (Collection) request.getAttribute(Constantes.RESULT);
			// fazer getAttibute para o objeto que vai receber o atributo TODOS
			String canalRecarga = (String) request.getAttribute("canalRecarga");
			Iterator i = result.iterator();
			InterfaceRecargas r = null;
			
			if (i.hasNext()){
				 r = (InterfaceRecargas)i.next();
			}
			
			if (r != null){
				context.put("msisdn",r.getMsisdnString());
				if (!canalRecarga.equals("TODOS"))
				{
					context.put("canal",r.getOrigem().getCanal().getDescCanal());
					context.put("origem",r.getOrigem().getDescOrigem());
				}	
				else
				{
					context.put("canal","TODOS");
					context.put("origem","");
				}			
			}
			
			context.put("dadosRecarga", request.getAttribute(Constantes.RESULT));			
			context.put("tamanho",String.valueOf(result.size()));
			context.put("recargaok", new Integer(contaOK(result, "ok")));
			context.put("recarganok", new Integer(contaOK(result, "nok")));
            
            context.put("nomeSaldoPrincipal", "Principal");
            context.put("nomeSaldoPeriodico", "Periódico");
            context.put("nomeSaldoBonus",     "Bônus");
            context.put("nomeSaldoTorpedos",  "Torpedos");
            context.put("nomeSaldoDados",     "Dados");
            
            SessionFactory sessionFactory = HibernateHelper.getSessionFactory();
            Session session = null;

            try
            {
                if (r != null)
                {
                    session = sessionFactory.getCurrentSession();
                    session.beginTransaction();
                    
                    Assinante assinante = AssinanteDAO.findByMsisdn(session, r.getMsisdn());
                    
                    if (assinante != null && assinante.getPlanoPreco() != null)
                    {
                        for (Iterator i2 = assinante.getPlanoPreco().getCategoria().getTiposSaldo().iterator(); i2.hasNext();)
                        {
                            SaldoCategoria saldoCategoria = (SaldoCategoria)i2.next();
                            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.PRINCIPAL)
                                context.put("nomeSaldoPrincipal", saldoCategoria.getNomSaldo());
                            
                            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.PERIODICO)
                                context.put("nomeSaldoPeriodico", saldoCategoria.getNomSaldo());
                            
                            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.BONUS)
                                context.put("nomeSaldoBonus", saldoCategoria.getNomSaldo());
                            
                            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.TORPEDOS)
                                context.put("nomeSaldoTorpedos", saldoCategoria.getNomSaldo());
                            
                            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.DADOS)
                                context.put("nomeSaldoDados", saldoCategoria.getNomSaldo());
                        }
                    }
                    session.getTransaction().commit();
                    
                }
            }
            catch (Exception e)
            {
                logger.warn("Erro ao consultar assinante no ShowConsultaRecargaAction. Os nomes dos tipos de saldo não serão atualizados. " + e);
                if (session != null)
                    session.getTransaction().rollback();
            }
            
		}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return codOperacao;
	}
}
