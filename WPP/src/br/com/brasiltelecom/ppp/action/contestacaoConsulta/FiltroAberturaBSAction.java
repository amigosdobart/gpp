package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteDAO;
import br.com.brasiltelecom.ppp.dao.CanalOrigemBSDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Assinante;
import com.brt.gpp.comum.mapeamentos.entidade.MotivoContestacao;

/**
 * @author Bernardo Vergne Dias
 * Data: 30-01-2008
 */
public class FiltroAberturaBSAction extends ShowActionHibernate 
{
    private String codOperacao = Constantes.COD_CONSULTA_BS;
    Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * @return Nome da tela de apresenta��o (VM).
     */
    public String getTela() 
    {
        return "filtroAberturaBS.vm";
    }
    
    /**
     * Implementa a l�gica de neg�cio dessa a��o.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisi��o HTTP que originou a execu��o dessa a��o.
     * @param sessionFactory    Factory de sess�es para acesso ao banco de dados (Hibernate).
     */
    public void updateVelocityContext(VelocityContext context,
           HttpServletRequest request, SessionFactory sessionFactory) throws Exception
    {
        Session session = null;
        
        try 
        {
            String msisdn = request.getParameter("msisdn");
            
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            context.put("canaisOrigemBS",   CanalOrigemBSDAO.findAll(session));    
        
            if (msisdn != null && !msisdn.equals(""))
            {
                Assinante assinante = AssinanteDAO.findByMsisdn(session, msisdn);                
                
                if (assinante != null)
                {
                    List motivos = Arrays.asList(assinante.getPlanoPreco().getMotivosContestacao().toArray());
                    Collections.sort(motivos, new Comparator()
                    {
                        public int compare(Object o1, Object o2)
                        {
                            return ((MotivoContestacao)o1).getDesMotivoContestacao().compareTo(
                                    ((MotivoContestacao)o2).getDesMotivoContestacao());
                        }
                    });
                    
                    if (motivos != null && motivos.iterator().hasNext()) // traz a collection lazy
                        context.put("motivosContestacao", motivos);                 
                    else
                        context.put("erro", "Erro: n�o existem motivos de contesta��o configurados <br> para o plano de pre�o " +
                                "desse assinante.");
                }
                else
                {
                    context.put("erro", "Erro: problemas ao consultar informa��es do assinante.");
                }
            }
            else
                context.put("erro", "N�o � poss�vel encaminhar contesta��o. N�mero de acesso inv�lido.");
            
            session.getTransaction().rollback();
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao abrir o formul�rio de abertua de BS. <br>" + e);
            logger.error("Erro ao abrir o formul�rio de abertua de BS. " + e);  
            
            if (session != null && session.getTransaction() != null) 
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