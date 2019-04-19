package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteDAO;
import br.com.brasiltelecom.ppp.dao.CanalOrigemBSDAO;
import br.com.brasiltelecom.ppp.dao.ContestacaoDAO;
import br.com.brasiltelecom.ppp.dao.IntPPPOutDAO;
import br.com.brasiltelecom.ppp.dao.ItemContestacaoDAO;
import br.com.brasiltelecom.ppp.dao.MotivoContestacaoDAO;
import br.com.brasiltelecom.ppp.interfacegpp.ContestacaoGPP;
import br.com.brasiltelecom.ppp.portal.entity.IntPPPOut;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.BS.ClienteSocket;
import br.com.brasiltelecom.ppp.util.BS.IntPPPOutXml;

import com.brt.gpp.comum.mapeamentos.entidade.Assinante;
import com.brt.gpp.comum.mapeamentos.entidade.Contestacao;
import com.brt.gpp.comum.mapeamentos.entidade.ContestacaoRecargas;
import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;

/**
 * Abre um boletim de sindicância
 * 
 * @author Bernardo Vergne Dias
 * Data: 07-02-2008
 */
public class ShowResultadoBSAction extends ShowActionHibernate 
{
    private String codOperacao              = Constantes.COD_ABERTURA_BS;
    private Logger logger                   = Logger.getLogger(this.getClass());
    
    public String getTela() 
    {
        return "resultadoBSContestacao.vm";
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
        Session session = null;
        
        try 
        {
            // Captura os parametros para abertura da BS
            
            String servidorGPP              = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
            String portaGPP                 = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
            String maquinaVitria            = (String)servlet.getServletContext().getAttribute(Constantes.VITRIA_NOME_SERVIDOR);
            String portaVitria              = (String)servlet.getServletContext().getAttribute(Constantes.VITRIA_PORTA_SERVIDOR);             
            String idUsuario                = ((Usuario)httpSession.getAttribute(Constantes.USUARIO)).getMatricula();
            List itensContestacaoAtuais     = (List)httpSession.getAttribute(ShowConsultaContestacaoAction.ID_SESSAO_ATUAIS);
            List itensContestacaoNovos      = (List)httpSession.getAttribute(ShowConsultaContestacaoAction.ID_SESSAO_NOVOS);
            String ipToolbar                = (String)httpSession.getAttribute("ipToolbar"); 
            int timeoutVitria               = Integer.parseInt((String)servlet.getServletContext().getAttribute(Constantes.VITRIA_TIMEOUT_SERVIDOR));
            String msisdn                   = request.getParameter("msisdn");            
            String strCanalContestacao      = request.getParameter("canalContestacao");
            String strMotivoContestacao     = request.getParameter("motivoContestacao");
            String descricaoContestacao     = request.getParameter("descricaoContestacao");            
            int canalContestacao            = Integer.parseInt(strCanalContestacao);
            int motivoContestacao           = Integer.parseInt(strMotivoContestacao);
            
            // Consulta o Vitria (numeroBS)
            
            String xmlVitria                = ClienteSocket.getXmlVitria(maquinaVitria, portaVitria, timeoutVitria); 
            //String xmlVitria                ="<numeroBS>404040404</numeroBS><evento>TST PU</evento><descricao>Teste</descricao>";
            String numeroBS                 = xmlVitria.substring(xmlVitria.indexOf("<numeroBS>") + 10, xmlVitria.indexOf("</numeroBS>"));     
            
            // Cria a contestacao
            
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            
            Contestacao contestacao = new Contestacao();
            contestacao.setMotivoContestacao(MotivoContestacaoDAO.findById(session, motivoContestacao));
            contestacao.setCanalOrigemBS(CanalOrigemBSDAO.findById(session, canalContestacao));
            contestacao.setDesContestacao(descricaoContestacao);
            contestacao.setDataAbertura(new Date());             
            contestacao.setNumeroBS(numeroBS);
            contestacao.setIdUsuario(idUsuario);
            contestacao.setIdtMsisdn(msisdn);
            contestacao.setIdtStatusAnalise(Contestacao.STATUS_ABERTA);
            contestacao.setItensContestacao(new ArrayList());
            
            // Filtra os itens contestados e insere-os na contestacao
            
            List todosItens = new ArrayList(itensContestacaoAtuais);            
            todosItens.addAll(itensContestacaoNovos);
            
            for (Iterator it = todosItens.iterator(); it.hasNext(); )
            {
                ItemContestacao item = (ItemContestacao)it.next();        
                if (item.isChecked()) 
                {                    
                    item.setIdtOperadorSfa(""); // Sera atualizado quando do retorno de fechamento pelo vitria
                    item.setVlrContestadoPrincipal(item.getVlrPrincipal());
                    item.setVlrContestadoBonus(item.getVlrBonus());
                    item.setVlrContestadoSms(item.getVlrSms());
                    item.setVlrContestadoDados(item.getVlrDados());
                    item.setVlrContestadoPeriodico(item.getVlrPeriodico());
                    item.setIdtStatusItemContestacao(ItemContestacao.STATUS_ABERTO);
                    item.setContestacao(contestacao);
                    
                    if(item instanceof ContestacaoRecargas) 
                        item.setCallId(((ContestacaoRecargas)item).getIdRecarga()); 

                    contestacao.getItensContestacao().add(item); 
                }
            }
            
            // Envia a contestacao para o Vitria, GPP, e banco de dados
            
            Assinante assinante = AssinanteDAO.findByMsisdn(session, msisdn);
            if (assinante == null)
                throw new RuntimeException("Problemas ao consultar informações do assinante");
            
            enviaContestacaoVitria(session, contestacao, assinante.getPlanoPreco().getIdtPlanoPreco());
            String resultadoBS = enviaContestacaoGPP(session, contestacao, servidorGPP, portaGPP, ipToolbar, idUsuario);

            // Salva a contestacao em banco
            
            Collection itens = contestacao.getItensContestacao();
            contestacao.setItensContestacao(null);
            ContestacaoDAO.incluirContestacao(session, contestacao);
            for (Iterator it = itens.iterator(); it.hasNext(); )                 
                ItemContestacaoDAO.incluirItem(session, (ItemContestacao)it.next()); 
            
            // Finaliza o processo
            
            context.put("numeroBS",    numeroBS);
            context.put("resultadoBS", resultadoBS);
            context.put("mensagem",    "Abertura do BS numero '" +  numeroBS + "' realizada com sucesso!");
            
            session.getTransaction().commit();
            logger.info("BS gerado com sucesso :" + numeroBS);
            httpSession.removeAttribute(ShowConsultaContestacaoAction.ID_SESSAO_ATUAIS);
            httpSession.removeAttribute(ShowConsultaContestacaoAction.ID_SESSAO_NOVOS);
        }
        catch (Exception e)
        {
            context.put("erro", "Erro ao abrir BS. " + (e.getMessage() != null ? e.getMessage() : e.toString()));
            logger.error("Erro ao abrir BS. " + e);  
            
            if (session != null && session.getTransaction() != null) 
                session.getTransaction().rollback();
        }    
    } 
    
    /**
     * Envia a contestacao para o Vitria atraves 
     * da tabela de interface PPP_OUT
     */
    private void enviaContestacaoVitria(Session session, Contestacao contestacao, String planoPreco)
    {
        for (Iterator it = contestacao.getItensContestacao().iterator(); it.hasNext(); )
        {
            ItemContestacao item = (ItemContestacao)it.next();  
            
            IntPPPOut intPPPOut = new IntPPPOut();            
            long idProcessamento = IntPPPOutDAO.getIdProcessamento(session);
            
            intPPPOut.setDatCadastro(contestacao.getDataAbertura());
            intPPPOut.setIdtEventoNegocio(Constantes.EVENTO_NEGOCIO_CONTESTACAO_PPPOUT);
            intPPPOut.setIdtStatusProcessamento("N"); 
            intPPPOut.setIdProcessamento(idProcessamento); 
            intPPPOut.setXmlDocument(IntPPPOutXml.getXml(contestacao, item, planoPreco, idProcessamento));            
            IntPPPOutDAO.incluir(session, intPPPOut);
        }
    }
    
    /**
     * Faz a publicacao do BS como Protocolo Unico no GPP
     * 
     * @return Mensagem de resultado da publicacao
     */
    private String enviaContestacaoGPP(Session session, Contestacao contestacao,
            String servidorGPP, String portaGPP, String ipToolbar, String idUsuario)
    {
        String resultadoBS = contestacao.getCanalOrigemBS().getDesMensagem();
        
        if (ipToolbar == null || ipToolbar.equals(""))
        {
            logger.info("Publicacao do BS nao realizado. IP invalido: " + ipToolbar);
            return resultadoBS;
        }
        
        logger.debug("Publicando o BS no GPP. Servidor:" + servidorGPP + ":" + portaGPP + " ipToolbar:" + ipToolbar);     
        
        String xmlRetornoGPP = ContestacaoGPP.publicarBS(servidorGPP, portaGPP, 
                            contestacao.getNumeroBS(), ipToolbar, contestacao.getIdtMsisdn(), idUsuario);
 
        String msgRetornoGPP = ContestacaoGPP.getMensagemRetorno(xmlRetornoGPP);
        return resultadoBS + ((msgRetornoGPP == null) ? "" : ("<br><br>" + msgRetornoGPP));
    }

    public String getOperacao() 
    {
        return codOperacao;
    }
}