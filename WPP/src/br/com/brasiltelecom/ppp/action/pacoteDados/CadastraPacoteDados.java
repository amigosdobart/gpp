package br.com.brasiltelecom.ppp.action.pacoteDados;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.dao.AssinanteOfertaPacoteDadosDAO;
import br.com.brasiltelecom.ppp.dao.FiltroMsisdnTempoWigDAO;
import br.com.brasiltelecom.ppp.dao.OfertaPacoteDadosDAO;
import br.com.brasiltelecom.ppp.dao.PacoteServicoDAO;
import br.com.brasiltelecom.ppp.dao.TipoSaldoDAO;
import br.com.brasiltelecom.ppp.interfacegpp.EnviaSMSTeste;
import br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnTempoWig;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.MultipartParser;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.AssinanteOfertaPacoteDados;
import com.brt.gpp.comum.mapeamentos.entidade.OfertaPacoteDados;

/**
 * Classe responsavel por cadastrar a Oferta de Pacote de Dados
 * 
 * Atualizado por: Bernardo Dias
 * Suporte o monitoramento de status (Ajax)
 * Data: 23/01/2008
 * 
 * @author João Paulo Galvagni
 * @since  13/09/2007
 */
public class CadastraPacoteDados extends ShowActionHibernate
{
    private String codOperacao = Constantes.COD_CADASTRAR_PACOTE_DADOS;
    Logger logger = Logger.getLogger(this.getClass());
    
    public String getTela() 
    {
        return "mensagemCadastraPacoteDados.vm";
    }

    public String getOperacao()
    {
        return codOperacao;
    }
    
    /**
     * Implementa a lógica de negócio dessa ação.
     * 
     * @param context           Contexto do Velocity.
     * @param request           Requisição HTTP que originou a execução dessa ação.
     * @param sessionFactory    Factory de sessões para acesso ao banco de dados (Hibernate).
     */
    public void updateVelocityContext(VelocityContext context,
                                      HttpServletRequest request,
                                      SessionFactory sessionFactory) throws Exception
    {
        // Caso exista o parametro OBTER_STATUS deve-se retornar (na VM) apenas um dos status:
        // 0 = CONCLUIDO
        // 1 = PROCESSANDO
        // 2 = ERRO NA VALIDACAO DA LISTA DE MSISDN
        // 3 = STATUS NAO ENCONTRADO
        // 4 = EXCECAO JAVA (nesse caso a VM retorna "4;descricao do erro")
        
        if (request.getParameter("OBTER_STATUS") != null)
        {
            Object status = request.getSession().getAttribute("statusCadastroPacoteDados");
            context.put("statusCadastroPacoteDados", status == null ? "3" : status);
            return;
        }
        
        // Caso NAO exista o parametro OBTER_STATUS, devemos entao iniciar o processo 
        // de cadastramento do pacote de dados e retornar uma pagina completa do WPP 
        // (atraves da mesma VM acima) contendo o script que monitora status.
                
        MonitorCadastro monitor = new MonitorCadastro();        
        monitor.parametros = MultipartParser.parseStream(request);
        monitor.httpSession = request.getSession();
        monitor.sessionFactory = sessionFactory;
        
        request.getSession().setAttribute("statusCadastroPacoteDados", "1");
        (new Thread(monitor)).start();
    }
    
    /**
     * Thread para execucao do cadastro de pacote de dados de forma paralela
     * @author Bernardo
     *
     */
    class MonitorCadastro implements Runnable
    {
        HashMap parametros;
        HttpSession httpSession;
        SessionFactory sessionFactory;
        ;
        ;
        
        public void run()
        {      
            Collection listaMsisdn = null;
            Session sessionHibernate = null;
            int idtOferta = (int)(System.currentTimeMillis()/1000);
            
            logger.info("Inicio do cadastro de Pacote de Dados [idtOferta = " + idtOferta + "]");
            
            // VALIDA A LISTA DE MSISDN
            
            try
            {
                listaMsisdn = validaListaMSISDN(httpSession, (FileItem)parametros.get("arquivo"));
            }
            catch (Exception e)
            {
                httpSession.setAttribute("statusCadastroPacoteDados", "2");
                logger.error("Erro ao cadastrar Pacote de Dados [idtOferta = " + idtOferta + "]", e);        
                return;
            }
            
            // EFETUTA O CADASTRO
            
            try
            {
                sessionHibernate = sessionFactory.getCurrentSession();
                sessionHibernate.beginTransaction();
                cadastrarPacoteDados(httpSession, parametros, sessionHibernate, listaMsisdn, idtOferta);        
                sessionHibernate.getTransaction().commit();
                
                httpSession.setAttribute("statusCadastroPacoteDados", "0");
                logger.info("Fim do cadastro de Pacote de Dados [idtOferta = " + idtOferta + "]");
            }
            catch (Exception e)
            {
                httpSession.setAttribute("statusCadastroPacoteDados", "4;" + e.getMessage());
                logger.error("Erro ao cadastrar Pacote de Dados [idtOferta = " + idtOferta + "]", e);        
                if (sessionHibernate != null)
                    sessionHibernate.getTransaction().rollback();
            }
        }
    }    
    
    /**
     * Inicia o cadastro do pacote de dados.
     */
    private void cadastrarPacoteDados(HttpSession httpSession, HashMap parametros, 
            Session sessionHibernate, Collection listaMsisdn, int idtOferta) throws Exception
    {
        SimpleDateFormat sdf    = new SimpleDateFormat(Constantes.DATA_FORMATO);
        String tipoSaldo        = ((FileItem)parametros.get("tipoSaldo")).getString();
        String desServico       = ((FileItem)parametros.get("desServico")).getString();
        int idPacoteServico     = Integer.parseInt(((FileItem)parametros.get("idPacoteServico")).getString());
        Date dataInicial        = sdf.parse(((FileItem)parametros.get("dataInicial")).getString());
        Date dataFinal          = sdf.parse(((FileItem)parametros.get("dataFinal")).getString());
        boolean enviarBroadcast = (parametros.get("enviarBroadcast") == null ? false : true);
        
        // Seta os parametros da Oferta para cadastro/atualizacao
        OfertaPacoteDados ofertaPacoteDados = new OfertaPacoteDados();
        
        // Seta o Identificador da Oferta
        ofertaPacoteDados.setIdtOferta(idtOferta);
        ofertaPacoteDados.setDesOferta(desServico);
        ofertaPacoteDados.setDataInicioOferta(dataInicial);
        ofertaPacoteDados.setDataFimOferta(dataFinal);
        ofertaPacoteDados.setEnviaBroadcast(enviarBroadcast);
        ofertaPacoteDados.setPacoteDados(PacoteServicoDAO.findById(sessionHibernate, idPacoteServico));
        ofertaPacoteDados.setTipoSaldo(TipoSaldoDAO.findByIdTipoSaldo(sessionHibernate, Short.parseShort(tipoSaldo)));
        ofertaPacoteDados.setDataCadastro(Calendar.getInstance().getTime());
        HashSet assinantesOferta = getAssinantesOferta(listaMsisdn, ofertaPacoteDados);
        ofertaPacoteDados.setAssinantes(assinantesOferta);
        
        // Realiza o cadastro da Oferta de Pacote de Dados e dos assinantes 
        // elegiveis para partiripar da Oferta
        OfertaPacoteDadosDAO.incluiOferta(sessionHibernate, ofertaPacoteDados);
        
        AssinanteOfertaPacoteDadosDAO.insereAssinanteOferta(sessionHibernate, assinantesOferta);
        
        // Realiza a configuracao do Menu BrT
        configuraMenuBrT(ofertaPacoteDados, sessionHibernate);
        
        // Caso o Usuario do Portal queira enviar um Broadcast para os
        // usuarios, a acao eh feita ao final da configuracao do Pacote
        if (enviarBroadcast)
        {
            Usuario login = (Usuario) httpSession.getAttribute(Constantes.LOGIN);
            
            String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
            String porta    = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
            EnviaSMSTeste.enviaSMS(listaMsisdn,
                                   "BrT Informa: Promocao especial pra vc: WAP e MMS ilimitado! Acesse Menu BrT>Promoces>Pacote de Dados. Mais informacoes acesse regulamento no site ou ligue 1053.", 
                                   servidor,
                                   porta,
                                   login.getNome());
        }
    }
    
    /**
     * Metodo....: getAssinantesOferta
     * Descricao.: Retorna a lista dos assinantes que serao cadastrados na Oferta
     * 
     * @param listaAssinantes   - Lista contendo os assinantes
     * @param ofertaPacoteDados - Oferta em que os assinantes serao cadastrados
     */
    private HashSet getAssinantesOferta(Collection listaAssinantes, OfertaPacoteDados ofertaPacoteDados)
    {
        HashSet listaAssinantesOferta = new HashSet();
        
        // Para cada assinante da Lista, eh realizada a insercao no
        // Banco de Dados, pois o HashSet os dados contidos no HashSet
        // nao sao inseridos automaticamente pelo Hibernate
        for (Iterator i = listaAssinantes.iterator(); i.hasNext(); )
        {
            AssinanteOfertaPacoteDados assinanteOferta = new AssinanteOfertaPacoteDados();
            assinanteOferta.setMsisdn((String)i.next());
            assinanteOferta.setOfertaPacoteDados(ofertaPacoteDados);
            
            listaAssinantesOferta.add(assinanteOferta);
        }
        
        return listaAssinantesOferta;
    }
    
    /**
     * Metodo....: configuraMenuBrT
     * Descricao.: Realiza o cadastro/atualizacao das informacoes
     *             do Pacote de Dados para o assinante
     * 
     * @param ofertaPacoteDados - Entidade com as informacoes para cadastro/atualizacao
     * @param session           - Sessao do Hibernate
     */
    private void configuraMenuBrT(OfertaPacoteDados ofertaPacoteDados, Session session)
    {
        // Varre a lista dos assinantes para configuracao da Oferta do Pacote de Dados
        for (Iterator assinante = ofertaPacoteDados.getAssinantes().iterator(); assinante.hasNext(); )
        {
            AssinanteOfertaPacoteDados assinanteOferta = (AssinanteOfertaPacoteDados)assinante.next();
            
            FiltroMsisdnTempoWig filtro = FiltroMsisdnTempoWigDAO.findByMsisdnResposta(session, assinanteOferta.getMsisdn(), 9999);
            
            if(filtro == null)
            {
                filtro = new FiltroMsisdnTempoWig();
                filtro.setMsisdn(assinanteOferta.getMsisdn());
                filtro.setCodResposta(Definicoes.CO_RESPOSTA_PCT_DADOS);
            }
            
            filtro.setDataInicio(ofertaPacoteDados.getDataInicioOferta());
            filtro.setDataFim(ofertaPacoteDados.getDataFimOferta());
            
            FiltroMsisdnTempoWigDAO.atualizaFiltroMsisdnTempoWig(session, filtro);
        }
    }
    
    /**
     * Metodo....: validaListaMSISDN
     * Descricao.: Valida se a lista de MSISDN contem apenas celulares
     *             da Brasil Telecom (Mascara)
     * 
     * @param  context      - Contexto do Velocity
     * @param  request      - Request da requisicao
     * @param  arquivo      - Arquivo contendo a lista de Msisdn
     * @return listaMsisdn  - Lista de Msisdn valida
     */
    private Collection validaListaMSISDN(HttpSession httpSession, FileItem arquivo) throws Exception
    {
        BufferedReader buffer   = null;
        StringBuffer erros      = new StringBuffer();
        Collection listaMsisdn  = new ArrayList();
        
        httpSession.setAttribute("errosPacoteDados", null);
        
        buffer = new BufferedReader(new InputStreamReader(arquivo.getInputStream()));
        String linha = "";
        
        while( (linha = buffer.readLine()) != null)
        {
            if (!linha.matches(Definicoes.MASCARA_MSISDN_MOVEL))
                erros.append(linha).append(" - Formato do número (MSISDN) inválido!\n");
            else
                listaMsisdn.add(linha);
        }
        
        buffer.close();
        
        if (erros.length() > 0)
        {
            httpSession.setAttribute("errosPacoteDados", erros.toString());
            throw new Exception("Erro na validação da lista de assinantes.");
        }

        return listaMsisdn;
    }
}