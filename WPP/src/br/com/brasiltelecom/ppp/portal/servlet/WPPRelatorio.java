
package br.com.brasiltelecom.ppp.portal.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.relatorio.ProcessaRelatorioInterno;
import br.com.brasiltelecom.ppp.portal.relatorio.ProcessaRelatorioReports;
import br.com.brasiltelecom.ppp.portal.relatorio.ResultadoRelatorio;

/**
 *  Servlet para geracao de relatorios executados pelo WPP.
 * 
 *  @author Daniel Ferreira
 *  @since  21/07/2005
 *
 *  Atualizado por Bernardo Vergne Dias
 *  Descrição: Reformulação completa. Suporte a inicialização por thread
 *  (sem bloqueio da requisição), acompanhamento de status de processamento 
 *  e download.
 *  Data: 22/10/2007
 *  
 */
public class WPPRelatorio extends HttpServlet 
{
    private static final long serialVersionUID = 610435960042311013L;
    
    private Logger logger;
    private ServletContext context;
    private InitialContext initContext;

    public WPPRelatorio()
    {
        this.logger = Logger.getLogger(this.getClass());
        this.context = null;
        this.initContext = null;
    }

    public void init(ServletConfig config) throws ServletException 
    {
        this.context = config.getServletContext();
        
        try
        {
            this.initContext = new InitialContext();
        }
        catch(NamingException e)
        {
            throw new ServletException(e);
        }
    }
    
    /**
     *  Executa o relatorio e escreve o resultado na resposta HTTP.
     *
     *  A ação dessa servlet depende do paramentro EXECUTAR_ACAO passado no request.
     *  
     *  Caso default (parametro nao informado): inicia o processamento de um relatorio.
     *  Caso "OBTER_STATUS": retorna o status de processamento de um relatorio.
     *  Caso "DOWNLOAD_RELATORIO": retorna o stream do arquivo ZIP de resultado.
     */
    protected void service(HttpServletRequest request, HttpServletResponse response)
    {
        // Verifica se o usuario foi autenticado.
        HttpSession session = request.getSession(true);
        Usuario user = (Usuario)session.getAttribute(Constantes.USUARIO);
        
        if (user == null)
            this.escreveMensagem(response, "1;Não existe usuário autenticado.");
        else
        {
            Map properties = this.parse(request.getQueryString());
            
            if (request.getQueryString().indexOf("DECODE_URL") >= 0)
                for (Iterator it = properties.keySet().iterator(); it.hasNext(); )
                {
                    try
                    {
                        String key = (String)it.next();
                        properties.put(key, URLDecoder.decode((String)properties.get(key), "iso-8859-1"));
                    }
                    catch (Exception ignored)
                    {}
                }
            
            String acao = (String)properties.get("EXECUTAR_ACAO");
            
            //Executa uma das seguintes acoes conforme o parametro EXECUTAR_ACAO
            if (acao == null)
            {
                iniciarProcessamento(request, response, properties);
            }
            else if (acao.equalsIgnoreCase("OBTER_STATUS"))
            {
                obterStatusProcessamento(request, response, properties);
            }
            else if (acao.equalsIgnoreCase("DOWNLOAD_RELATORIO"))
            {
                downloadRelatorio(request, response, properties);
            }
            else if (acao.equalsIgnoreCase("KEEP_ALIVE"))
            {
                manterSessao(response);
            }
        }
    }
    
    /**
     * Retorna o stream do arquivo para o cliente. Metodo executado quanto o status do relatorio vale CONCLUIDO.
     * 
     * Parametro necessário: NOME_RELATORIO
     */
    public void downloadRelatorio(HttpServletRequest request, HttpServletResponse response, Map properties)
    {
        //Capturando o mapeamento de relatorios
        //Chave: nomeRelatorio
        //Valor: ResultadoRelatorio
        HashMap map = (HashMap)request.getSession().getAttribute("MAP_RELATORIO_WPP");
        ResultadoRelatorio resultado = null;
        
        if(map != null && properties.get("NOME_RELATORIO") != null)
            resultado = (ResultadoRelatorio)map.get(properties.get("NOME_RELATORIO"));
        
        if (resultado != null && resultado.getCodStatus() == ResultadoRelatorio.CONCLUIDO)
        {
            try
            {
                if (resultado.getContentDisposition() != null)
                    response.setHeader("Content-disposition", resultado.getContentDisposition());
                response.setContentType(resultado.getContentType());
                response.getOutputStream().write(resultado.lerArquivo());
                response.getOutputStream().flush();
            }
            catch (Exception e)
            {
                this.logger.error("Excecao ao fazer download: " + e);
            }
        }
        else
            escreveMensagem(response, "Erro ao obter arquivo para download.");
    }   
    
    /**
     * Retorna, para o cliente, uma string indicando o status de geração do relatorio. Metodo executado apos
     * inicio da geracao do relatorio.
     * 
     * Sintaxe: <CodigoNumerico>;<DescriçãoDoStatus>
     * 
     * Parametro necessário: NOME_RELATORIO
     */
    public void obterStatusProcessamento(HttpServletRequest request, HttpServletResponse response, Map properties)
    {
        //Capturando o mapeamento de relatorios
        //Chave: nomeRelatorio
        //Valor: ResultadoRelatorio
        HashMap map = (HashMap)request.getSession().getAttribute("MAP_RELATORIO_WPP");
        ResultadoRelatorio resultado = null;
                
        if (map != null)
            resultado = (ResultadoRelatorio)map.get(properties.get("NOME_RELATORIO"));
            
        if (resultado != null)
            escreveMensagem(response, "" + resultado.getCodStatus() + ";" + resultado.getDesStatus());
        else
            escreveMensagem(response, "" + ResultadoRelatorio.ERRO + ";Não existe relatório em " +
                    "processamento ou o sistema parou inesperadamente.");
    }
    
    /**
     * Inicia a geração do relatorio. 
     * 
     * Parametros necessários: NOME_RELATORIO, ARQUIVO_PROPRIEDADES, Parametros do Relatorio..
     * No caso de SUCESSO, retorna para o cliente a seguinte string: 
     * 
     *   "0;0;NomeDoRelatorio" se o relatorio foi iniciado e a sessao NAO POSSUIA nenhum relatorio com esse 
     *                         nome em andamento.
     *   "0;1;NomeDoRelatorio" se o relatorio foi iniciado e  a sessao POSSUIA nenhum relatorio com esse 
     *                         nome em processamento. Um novo eh disparado e o anterior eh desprezado (a thread
     *                         antiga terminara o processamento ateh morrer)

     * No caso de ERRO, retorna para o cliente a seguinte string: 
     *   "1;Descricao da Exceção" se o relatorio nao foi iniciado
     */
    public void iniciarProcessamento(HttpServletRequest request, HttpServletResponse response, Map properties)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        try 
        {   
            String nomeRelatorio = (String)properties.get("NOME_RELATORIO");
            
            // Remove extensao
            if (nomeRelatorio.indexOf('.') >= 0)
                nomeRelatorio = nomeRelatorio.substring(0,nomeRelatorio.lastIndexOf('.'));
            
            //Capturando o mapeamento de relatorios em execução
            //Chave: nomeRelatorio
            //Valor: ResultadoRelatorio
            HashMap map = (HashMap)request.getSession().getAttribute("MAP_RELATORIO_WPP");
            if(map == null)
            {
                map = new HashMap();
                request.getSession().setAttribute("MAP_RELATORIO_WPP", map);
            }
            
            // Verifica se o relatorio ainda está em execução
            String retorno = "0;0;" + nomeRelatorio;
            if (map.get(nomeRelatorio) != null)
            {
                if (((ResultadoRelatorio)map.get(nomeRelatorio)).getCodStatus() 
                        == ResultadoRelatorio.EM_PROCESSAMENTO)
                    retorno = "0;1;" + nomeRelatorio;
            }
            
            List diretorios = (List)context.getAttribute(Constantes.DIRETORIO_COMPROVANTES);
            String diretorioComprovante = null;
            for (Iterator it = diretorios.iterator(); it.hasNext();) 
            {
                diretorioComprovante = (String)it.next();
                if ((new File(diretorioComprovante)).exists())
                    break;
            }
            
            // Cria o VO para representar o relatorio sendo processado
            ResultadoRelatorio resultado = new ResultadoRelatorio();
            resultado.setRootFabricaWPP(diretorioComprovante);
            resultado.setNomeRelatorio(nomeRelatorio);
            resultado.setNomeArquivo(nomeRelatorio + "_" + sdf.format(Calendar.getInstance().getTime()));
            
            // Se existir uma thread em execução, a referência será perdida
            // Somente a ultima requisição ficará disponivel para download
            map.put(nomeRelatorio, resultado);
            
            // Verifica qual deve ser a thread para iniciar o processamento do relatorio
            if (request.getQueryString().indexOf("REPORTS") >= 0)
                (new Thread(new ProcessaRelatorioReports(resultado, 
                        request.getServerName() + ":" + request.getServerPort(),
                        request.getQueryString(), context, properties))).start();
            else
                (new Thread(new ProcessaRelatorioInterno(resultado, initContext, context, properties))).start();

            escreveMensagem(response, retorno);
        }       
        catch(Exception e) 
        {
            this.logger.error("Excecao: " + e);
            this.escreveMensagem(response, "1;Exceção: " + e.getMessage());
        }
    }
    
    /**
     * Após disponivel o download, esse metodo eh executado periodicamente para manter a sessao ativa.
     * 
     * Apenas retorna uma string fake
     */
    public void manterSessao(HttpServletResponse response)
    {
        escreveMensagem(response, "" + System.currentTimeMillis());
    }
    
    /**
     *  Interpreta os parametros passados pela URL.
     */     
    private Map parse(String parametros)
    {
        Map result = new HashMap();
        if(parametros != null)
        {
            //Separando os parametros
            String[] paramList = parametros.trim().split("\\+");
            
            for(int i = 0; i < paramList.length; i++)
            {
                //Para cada parametro, separar o nome do parametro de seu valor
                String[] parametro = paramList[i].trim().split("=");
                
                //Inserindo o parametro no resultado
                if (parametro.length == 2)
                    result.put(parametro[0], parametro[1]);
            }
        }
        return result;
    }
    
    /**
     * Escreve uma mensagem no response.
     */
    private void escreveMensagem(HttpServletResponse response, String mensagem)
    {
        try 
        {
            response.setContentType("text/html;charset=ISO-8859-1");
            ServletOutputStream output = response.getOutputStream();
            output.println(mensagem);
            output.flush();
            output.close();
        } 
        catch(IOException e) 
        {
            this.logger.error("Excecao de I/O: " + e);
        }
    }
}
