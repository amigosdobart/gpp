package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * Classe responsavel por realizar a publicacao do Protocolo
 * Unico baseado no numero do BS
 * 
 * @author João Paulo Galvagni
 * @since  09/03/2007
 */
public class ContestacaoGPP
{
	private static Logger logger = Logger.getLogger(ContestacaoGPP.class);
    
	/**
	 * Metodo....: publicarBS
	 * Descricao.: Envia para o GPP os dados para publicacao
	 * 			   do Protocolo Unico
	 * 
	 * @param  servidor	- Servicor do gerente ORB
	 * @param  porta	- Porta para o gerente ORB
	 * @param  numeroBS	- Numero do BS
	 * @param  numeroIP	- Numero do IP do TooBar do atendente
	 * @return result	- XML utilizado na publicacao
	 */
	public static String publicarBS(String servidor, String porta, String numeroBS, String numeroIP, 
								    String numeroAssinante, String matriculaOperador)
	{
		Logger logger = Logger.getLogger(ContestacaoGPP.class);
		String result = null;
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosConsulta".getBytes();
			
			consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			
			// Realiza a publicacao do Protocolo Unico
			result = pPOA.publicarBS(numeroBS, numeroIP, numeroAssinante, matriculaOperador);
			logger.debug("XML retornado do GPP:"+result);
		}
		catch (Exception e)
		{
			logger.error("Erro ao conectar ao GPP para publicacao do BS:"+numeroBS,e);
		}
		// Retorno do resultado
		return result;
	}
    
    /**
     * Extrai a mensagem de retorno do XML de publicacao de BS
     * 
     * @param xmlPublicacaoPS
     * @return
     */
    public static String getMensagemRetorno(String xmlPublicacaoPS)
    {
        try
        {
            // Obtendo os objetos necessarios para a execucao do parse do xml
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            DocumentBuilder parse = docBuilder.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlPublicacaoPS));
            Document doc = parse.parse(is);
            
            // Cria um NodeList da <mensagem> e do <conteudo>
            NodeList mensagem  = ((Element)doc.getElementsByTagName("mensagem").item(0)).getChildNodes();
            NodeList conteudo  = ((Element)mensagem).getElementsByTagName("conteudo").item(0).getChildNodes();
            
            // Realiza o Parse do XML contido na TAG <![CDATA[ xml ]]>
            // Obtendo os objetos necessarios para a execucao do parse do xml
            InputSource xmlInterno = new InputSource(new StringReader(conteudo.item(0).getNodeValue()));
            Document docInterno = parse.parse(xmlInterno);
    
            // Cria um NodeList da tag principal <tagPrincipal>
            NodeList gppRetornoPublicacaoBS = ((Element)docInterno.getElementsByTagName("GPPRetornoPublicacaoBS")).getChildNodes();
            Node nodeMensagem = ((Element)gppRetornoPublicacaoBS).getElementsByTagName("mensagem").item(0);
            
            if (nodeMensagem != null && nodeMensagem.getChildNodes().item(0) != null )
                return nodeMensagem.getChildNodes().item(0).getNodeValue();
        }
        catch (Exception e)
        {
            logger.error("Erro ao ler mensagem no xml GPPRetornoPublicacaoBS", e);
        } 
        
        return null;
    }
}