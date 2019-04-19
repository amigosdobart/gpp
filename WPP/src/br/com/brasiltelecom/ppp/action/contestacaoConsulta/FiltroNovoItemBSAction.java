package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteDAO;
import br.com.brasiltelecom.ppp.dao.OrigemRecargaDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.mapeamentos.entidade.Assinante;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Filtro para inclusao de novo item de BS,
 * 
 * @author Bernardo Vergne Dias
 * Data: 25-02-2008
 */
public class FiltroNovoItemBSAction extends ShowActionHibernate 
{
    private String codOperacao = Constantes.COD_CONSULTA_BS;
    Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * @return Nome da tela de apresenta��o (VM).
     */
    public String getTela() 
    {
        return "filtroNovoItemBS.vm";
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
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            String msisdn       = request.getParameter("msisdn");
            Assinante assinante = AssinanteDAO.findByMsisdn(session, msisdn);
            
            if (assinante == null)
                throw new RuntimeException("Problemas ao consultar informa��es do assinante");
            
            Collection tiposSaldo = assinante.getPlanoPreco().getCategoria().getTiposSaldo();
            context.put("mostraPrincipal",  new Boolean(tiposSaldo.contains(new TipoSaldo(TipoSaldo.PRINCIPAL,null,false))));
            context.put("mostraBonus",      new Boolean(tiposSaldo.contains(new TipoSaldo(TipoSaldo.BONUS,null,false))));
            context.put("mostraSMS",        new Boolean(tiposSaldo.contains(new TipoSaldo(TipoSaldo.TORPEDOS,null,false))));
            context.put("mostraDados",      new Boolean(tiposSaldo.contains(new TipoSaldo(TipoSaldo.DADOS,null,false))));
            context.put("mostraPeriodico",  new Boolean(tiposSaldo.contains(new TipoSaldo(TipoSaldo.PERIODICO,null,false))));
            
            context.put("origens",  OrigemRecargaDAO.findAllDispNovoItemConestacao(session));
            context.put("numSaldos", new Integer(tiposSaldo.size()));
            
            session.getTransaction().rollback();
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao abrir o formul�rio de cria��o de item de BS. <br>" + e);
            logger.error("Erro ao abrir o formul�rio de cria��o de item de BS. " + e);  
            
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