package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.ContestacaoChamadasDAO;
import br.com.brasiltelecom.ppp.dao.ContestacaoRecargasDAO;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.HttpRequestParser;

import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;

/**
 * Efetua uma consulta de contestações relacionadas a um número de acesso ou BS
 * 
 * @author Bernardo Vergne Dias
 * Data: 30-01-2008
 */
public class ShowConsultaContestacaoAction extends ShowActionHibernate 
{
    public  final static String ID_SESSAO_ATUAIS      = "ConsultaContestacao_ItensAtuais";    
    public  final static String ID_SESSAO_NOVOS       = "ConsultaContestacao_NovosItens"; 
    
    private final static String codOperacao   = Constantes.COD_CONSULTA_BS;
    private final static Logger logger        = Logger.getLogger(ShowConsultaContestacaoAction.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     
    // Mascaras de bits
    private final int ORIGEM_CHAMADAS   = 0x0001; // VOZ E DADOS
    private final int ORIGEM_RECARGAS   = 0x0002;
    
    private final int PAGESIZE          = 40;
    
    public String getTela() 
    {
        return "consultaContestacao.vm";
    }
    
    /**
     * Implementa a lógica de negócio dessa ação.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisição HTTP que originou a execução dessa ação.
     * @param sessionFactory    Factory de sessões para acesso ao banco de dados (Hibernate).
     */
    public void updateVelocityContext(VelocityContext context,
           HttpServletRequest request, SessionFactory sessionFactory) throws Exception
    {
        HttpSession httpSession = request.getSession();
        Properties params       = HttpRequestParser.getParametros(request);
        Session session         = null;
        
        try 
        {
            // CAPTURA OS PARAMETROS
        
            String tmpDataInicial    = params.getProperty("dataInicial");
            String tmpDataFinal      = params.getProperty("dataFinal");
            String tmpPeriodo        = params.getProperty("periodo");
            String tmpContestadas    = params.getProperty("contestadas");
           
            String msisdn            = params.getProperty("msisdn");            
            String numBS             = params.getProperty("numBS");            
            String tipoPeriodo       = params.getProperty("tipoPeriodo");
            int origem               = Integer.parseInt(params.getProperty("origem"));
            int pagina               = params.containsKey("pagina") ? Integer.parseInt(params.getProperty("pagina")) : 0;
            int periodo              = (tmpPeriodo != null) ? Integer.parseInt(tmpPeriodo) : 0;
            Date dataInicial         = getDataInicial(tmpDataInicial, tipoPeriodo, periodo);
            Date dataFinal           = getDataFinal(tmpDataFinal, tipoPeriodo, periodo);      
            boolean contestadas      = (tmpContestadas != null && tmpContestadas.equals("TRUE")) ? true : false;
            List itensContestacaoAtuais     = (List)httpSession.getAttribute(ID_SESSAO_ATUAIS);
            List itensContestacaoNovos      = (List)httpSession.getAttribute(ID_SESSAO_NOVOS);
            
            // REALIZA A CONSULTA
            
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            // Verifica se eh necessário realizar efetivamente a consulta
            if (!params.containsKey("pagina") || itensContestacaoAtuais == null)
            {
                itensContestacaoAtuais = new ArrayList();
                itensContestacaoNovos = new ArrayList();
                httpSession.setAttribute(ID_SESSAO_ATUAIS, itensContestacaoAtuais);
                httpSession.setAttribute(ID_SESSAO_NOVOS, itensContestacaoNovos);
                
                // Consulta os itens de chamada contestados ou passíveis de contestacao
                if ((origem & ORIGEM_CHAMADAS) > 0)
                    itensContestacaoAtuais.addAll(ContestacaoChamadasDAO.findByCaracteristicas(
                            session, msisdn, numBS, dataInicial, dataFinal, contestadas));

                // Consulta os itens de recarga contestados ou passíveis de contestacao
                if ((origem & ORIGEM_RECARGAS) > 0)
                    itensContestacaoAtuais.addAll(ContestacaoRecargasDAO.findByCaracteristicas(
                            session, msisdn, numBS, dataInicial, dataFinal, contestadas));
                
                try { 
                    Collections.sort(itensContestacaoAtuais);
                }
                catch (Exception e) {
                    logger.warn("Erro ao ordernar a lista de itens de contestacao." + e);
                }
            }
                            
            // Faz a paginacao dos itens atuais
            int fromIndex = pagina * PAGESIZE;
            int toIndex = Math.min((pagina + 1) * PAGESIZE, itensContestacaoAtuais.size());
            List itensPagina = (toIndex > 0 && fromIndex <= toIndex) ? itensContestacaoAtuais.subList(fromIndex, toIndex) : new ArrayList();

            // Captura o msisdn (em caso de busca por BS)
            if (itensPagina.size() > 0)
                msisdn = ((ItemContestacao)itensPagina.get(0)).getSubId();
            else if (msisdn == null || msisdn.equals(""))
            	itensPagina = null;
            	
            	
            // PERSISTE OS RESULTADOS
            
            context.put("msisdn",           msisdn);
            context.put("numBS",            numBS);
            context.put("tipoPeriodo",      tipoPeriodo);
            context.put("origem",           new Integer(origem));            
            context.put("pagina",           new Integer(pagina)); 
            context.put("periodo",          tmpPeriodo);
            context.put("dataInicial",      tmpDataInicial);
            context.put("dataFinal",        tmpDataFinal);
            context.put("contestadas",      tmpContestadas);
            
            context.put("pagesize",         new Integer(PAGESIZE));
            context.put("itensPagina",      itensPagina); 
            context.put("itensNovos",       itensContestacaoNovos);             
            context.put("paginas",          new Integer((int)Math.ceil(itensContestacaoAtuais.size() * 1. / PAGESIZE)));
            context.put("totalItens",       new Integer(itensContestacaoAtuais.size()));
            context.put("totalItensMarcados", new Integer(getTotalItensMarcados(itensContestacaoAtuais) + 
                                                          getTotalItensMarcados(itensContestacaoNovos)));
            context.put("sdfData",          new SimpleDateFormat("dd/MM/yyyy"));
            context.put("sdfHora",          new SimpleDateFormat("HH:mm:ss"));

            session.getTransaction().rollback();      
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao realizar a consulta de BS. " + e);
            logger.error("Erro ao realizar a consulta de BS. " + e);  
            
            if (session != null && session.getTransaction() != null) 
                session.getTransaction().rollback();
        }    
    }
    
    /**
     * Configura a data inicial de acordo com os parametros informados.
     * @throws ParseException 
     */
    private Date getDataInicial(String strDataInicial, String tipoPeriodo, int periodo) throws ParseException
    {
        if (tipoPeriodo == null) // Nao ha restricao de data
            return null;
        
        if (tipoPeriodo.equals("P") && periodo > 0) // Limitamos a data inicial da consulta
        {  
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, - periodo);
            return cal.getTime(); 
        }
        else if (strDataInicial != null) 
        {
            return sdf.parse(strDataInicial);
        }
        
        return null;
    }
  
    /**
     * Configura a data final de acordo com os parametros informados.
     * @throws ParseException 
     */
    private Date getDataFinal(String strDataFinal, String tipoPeriodo, int periodo) throws ParseException
    {
        if (tipoPeriodo == null) // Nao ha restricao de data
            return null;
        
        if (tipoPeriodo.equals("D") && strDataFinal != null) // Corrige a data final em +1 dia
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(strDataFinal));
            cal.add(Calendar.DAY_OF_YEAR, 1);
            return cal.getTime();
        }
        
        return null;
    }
    
    /**
     * Soma o total de itens marcados
     */
    private int getTotalItensMarcados(List itens) throws Exception
    {
        int total = 0;        
        if (itens != null)
            for (Iterator it = itens.iterator(); it.hasNext(); )
                total += ((ItemContestacao)it.next()).isChecked() ? 1 : 0;     
        return total;
    }
    
    public String getOperacao() 
    {
        return codOperacao;
    }
}