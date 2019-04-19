package br.com.brasiltelecom.ppp.action.cadastroCampanha;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.CampanhaHome;
import br.com.brasiltelecom.ppp.home.HistoricoConcessaoHome;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteCampanha;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra informações sobre uma campanha do assinante (bonus e parametros)
 * 
 * @author Bernardo Vergne Dias
 * Data: 03/05/2007
 */
public class ShowDetalhesAssinanteCampanhaAction extends ShowAction
{
	private String codOperacao = Constantes.COD_CONSULTAR_ASSIN_CAMPANHA;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @return Nome da tela de apresentação (VM).
	 */
	public String getTela()
	{
		return "detalhesAssinanteCampanha.vm";
	}
	
	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param Database			Banco de dados Castor.
	 */
	public void updateVelocityContext(VelocityContext context,
			HttpServletRequest request, Database db) throws Exception
	{
		try
		{
			db.begin();
			
			String msisdn	= request.getParameter("msisdn");
			long campanha 	= Long.parseLong(request.getParameter("campanha"));
			
			/*
			 * Consulta informações de histórico de bonus 
			 */
			
			Collection historicos = HistoricoConcessaoHome.findByAssinanteCampanha(msisdn, campanha, db);
			if (historicos != null && historicos.size() >= 1) context.put("historicosConcessao", historicos);
			
			/*
			 * Consulta informações sobre parametros da campanha
			 */
			
			AssinanteCampanha assinanteCampanha = CampanhaHome.findByAssinanteCampanha(msisdn, campanha, db);
			if (assinanteCampanha == null) throw new Exception("Dados não encontrados");

			if (assinanteCampanha.getXmlDocument() != null && !assinanteCampanha.getXmlDocument().equals(""))
			{
				// Inicializa o Parser XML
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringComments(true);
				DocumentBuilder builder = factory.newDocumentBuilder();
				InputStream is = new ByteArrayInputStream(assinanteCampanha.getXmlDocument().getBytes("UTF8"));
				Document document = builder.parse(is);
				
				// Captura as tags "parametro"
				
				List listaParametros = new ArrayList();
				
				Element root = document.getDocumentElement();
				NodeList parametros = root.getElementsByTagName("parametro");
				for (int i = 0; i < parametros.getLength(); i++)
					listaParametros.add(parametros.item(i));
								
				context.put("listaParametros", listaParametros);
			}
			
			db.commit();
		}
		catch(Exception e)
		{
			db.rollback();
			context.put("erro", "Erro ao consultar campanha de assinante. <br><br>" + e.getMessage());
			logger.error(e);
		}
	}
	
	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}	
}
