package br.com.brasiltelecom.ppp.action.enviaSMSviaTangram;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.velocity.VelocityContext;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.interfacegpp.EnviaRequisicaoTangramGPP;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.MultipartParser;

import com.brt.gpp.aplicacoes.enviarMensagemTangram.ParserGPPRequisicaoTangram;
import com.brt.gpp.aplicacoes.enviarMensagemTangram.entidades.GPPRequisicaoTangram;
import com.brt.gpp.comum.Definicoes;

/**
 * Action de envio de SMS via Tangram
 * 
 * @author Jorge Abreu	
 * @since 15/10/2007
 */
public class EnviaSMSviaTangramAction extends ShowActionHibernate 
{		
	//TODO: Definir c�digo de operacao para o envio de SMS via Tangram e substituir onde h� COD_BROADCAST_SMS
	private String codOperacao = Constantes.COD_BROADCAST_SMS;
	
	/**
	 * @return Nome da tela de apresenta��o (VM).
	 */
	public String getTela() 
	{
		return "resultadoEnvioSMSviaTangram.vm";
	}
	
	/**
	 * Implementa a l�gica de neg�cio dessa a��o.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisi��o HTTP que originou a execu��o dessa a��o.
	 * @param sessionFactory	Factory de sess�es para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
			   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{		
		// Par�metros para envio de SMS		
		String servidor  = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta 	 = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		
		
		String xml = ParserGPPRequisicaoTangram.gerarXML(montaObjeto(request));		
		
		// Envia o SMS para uma lista de assinantes 
		if(EnviaRequisicaoTangramGPP.enviarRequisicaoTangram(xml,servidor,porta) != 0)
			context.put(Constantes.MENSAGEM, "Falha ao enviar Mensagem.");
		else
			context.put(Constantes.MENSAGEM, "Mensagem Enviada com Sucesso!");		
	}
		
	/**
	 * @return Nome da opera��o (permiss�o) associada a essa a��o.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
	
	private GPPRequisicaoTangram montaObjeto(HttpServletRequest request) throws Exception
	{
		HashMap params = MultipartParser.parseStream(request);
		
		GPPRequisicaoTangram gppRequisicaoTangram = new GPPRequisicaoTangram();
		
		gppRequisicaoTangram.setIdtMsisdnDestino(getDestinatarios(params));
		gppRequisicaoTangram.setTextoConteudo(((FileItem)params.get("sms")).getString());
		
		if(((FileItem)params.get("tipoData")).getString().equals("Imediato"))
			gppRequisicaoTangram.setDataAgendamento("");
		if(((FileItem)params.get("tipoData")).getString().equals("Agendado"))
		{
		    SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DATE);
		    Date data = sdf.parse(((FileItem)params.get("dataA")).getString());
			sdf.applyPattern(Definicoes.MASCARA_DATA_HORA_GPP);
			gppRequisicaoTangram.setDataAgendamento(sdf.format(data));
		}
			

		gppRequisicaoTangram.setIdtOrigem(((FileItem)params.get("origem")).getString());
		gppRequisicaoTangram.setIndAgendamentoRel("false");
		//TODO: Setar Canal e Servi�o de acordo com o real
		gppRequisicaoTangram.setIdCanal("010");
		gppRequisicaoTangram.setIdServico("090");
		
		return gppRequisicaoTangram;
	}
	
	private String getDestinatarios(HashMap params) throws Exception
	{
		FileItem arquivo = (FileItem)params.get("arquivo");
		StringBuffer buffer = new StringBuffer();
	
		LineNumberReader lnr = new LineNumberReader(new InputStreamReader(arquivo.getInputStream()));
		String line = null;
		
		while ((line = lnr.readLine()) != null)
		{
			buffer.append(line + ";");
		}
		
		return buffer.toString();
	}
}
