package br.com.brasiltelecom.ppp.util.BS;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Interface de acesso a API de BS do Vitria
 * 
 * @author Ricardo Albuquerque
 * @since 20/05/2004
 * 
 * Atualizado por: Bernardo Dias
 * Data: 12/02/2008
 * Inclusao de novos metodos
 */
public class ClienteSocket 
{
    private static Logger logger  = Logger.getLogger(ClienteSocket.class);

    /**
     * Faz conexão com o Vitria para obter o XML de numeroBS.
     * Formato: "<numeroBS>34432</numeroBS><evento>TST PU</evento><descricao>Teste</descricao>"
     */
    public static String obtemBS (String maquinaVitria, String portaVitria, int timeoutVitria )
    {
        String retorno = null;
        try
        {           
            Socket sock = new Socket(maquinaVitria, new Integer(portaVitria).intValue());
            sock.setSoTimeout(timeoutVitria);
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            BufferedReader bis = new BufferedReader(new InputStreamReader(dis));
            PrintStream ps = new PrintStream(sock.getOutputStream());
            ps.println("RQNUMEROBS");
            retorno =  bis.readLine();
        }
        catch (Exception e) {retorno = e.getMessage();}
        
        return retorno;
    }

    /**
     * Obtém o número do BS no Vitria. Inclui tratamento do retorno e tentativas de conexao
     */
    public static String getXmlVitria(String maquinaVitria, String portaVitria, 
            int timeoutVitria) throws RuntimeException
    {        
        int vitNumTentativas    = Constantes.VITRIA_NUM_TENTATIVAS;        
        String xmlRetorno       = null;
        long   numBs            = -1;
        
        for(int i = 1; i <= vitNumTentativas; i++)
        {
            xmlRetorno = ClienteSocket.obtemBS(maquinaVitria, portaVitria,timeoutVitria);
            numBs = parseNumeroBS(xmlRetorno);

            if((xmlRetorno == null) || (numBs <= 0))
                try
                {
                    //Aguarda um tempo aleatorio para tentar receber novamente
                    Thread.sleep((int) (Math.random() * Constantes.VITRIA_TEMPO_ESPERA)); 
                }
                catch(Exception ignore){}       
            else 
                break;
        }
        
        if (xmlRetorno == null)
        {
            logger.error("Erro ao conectar no vitria para recebimento do numero de BS.");
            throw new RuntimeException("Sistema indisponível. Erro código 02: problemas ao obter o número de BS");
        }
        
        if (parseNumeroBS(xmlRetorno) <= 0)
        {
            logger.error("Erro durante recebimento do numero de BS do Vitria. Numero de BS invalido.");
            logger.error("XML enviado pelo Vitria: " + xmlRetorno);
            throw new RuntimeException("Erro código 03: problemas ao validar o número de BS");
        }
        
        return xmlRetorno;        
    }
    
    /**
     *  Efetua o parse do XML do Vitria e retorna o numero do BS. 
     * 
     * @param   xmlVitria    XML de retorno do Vitria com o numero do BS.
     * @return  result      Numero do BS ou -1 em caso de erro.
     * 
     */
    public static long parseNumeroBS(String xmlVitria)
    {
        long result = 0;        
        try
        {
            Pattern p = Pattern.compile("(.*<numeroBS>(\\d*)</numeroBS>.*)");           
            Matcher m = p.matcher(xmlVitria);
            
            if(m.matches())
                result = Long.parseLong(m.group(2));
        }
        catch(Exception e)
        {
            result = -1;
        }        
        return result;
    }
}