package br.com.brasiltelecom.ppp.action.consultaExtratoGPP;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

import br.com.brasiltelecom.ppp.interfacegpp.GerenteORB;
import br.com.brasiltelecom.ppp.portal.entity.CodigosRetorno;
import br.com.brasiltelecom.ppp.portal.entity.RequisicaoExtrato;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

public class ConsultaExtratoGPP
{
	private Document		extrato;
	private CodigosRetorno	retorno;
	private String			servidor;
	private String			porta;

	public ConsultaExtratoGPP(String servidor, String porta, RequisicaoExtrato requisicao)
	{
		this.servidor = servidor;
		this.porta = porta;
		this.retorno = new CodigosRetorno();
		carregarExtratoGPP(requisicao);
	}

	public void carregarExtratoGPP(RequisicaoExtrato req)
	{
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosConsulta".getBytes();
			consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			String stringXML = pPOA.consultarExtratoGPP(req.getIdtMsisdn(),
					new SimpleDateFormat("yyyyMMdd").format(req.getDatPeriodoInicial()),
					new SimpleDateFormat("yyyyMMdd").format(req.getDatPeriodoFinal()),
					req.isCobrarExtrato());

			// Cria um documentBuilder para fazer o parse do XML retornado pelo GPP
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource inputSrc = new InputSource(new StringReader(stringXML));

			// Faz o parse do XML do GPP e completa com as informacoes enviadas pelo Clarify
			extrato = docBuilder.parse(inputSrc);
			carregarXMLRequisicao(req);
		}
		catch(Exception e)
		{
			retorno.setVlrRetorno("99");
			retorno.setDescRetorno("Erro ao consultar extrato no GPP. Tente novamente mais tarde.");
		}
	}

	private void carregarXMLRequisicao(RequisicaoExtrato req)
	{
		Element nodeReq = null;
		NodeList nodeList = extrato.getDocumentElement().getElementsByTagName("requisicao");

		if(nodeList != null && nodeList.item(0) != null)
			nodeReq = (Element)nodeList.item(0);
		else
		{
			retorno.setVlrRetorno(Integer.toString(Constantes.GPP_RET_ERRO_TECNICO));
			retorno.setDescRetorno("Formato de XML invalido");
			return;
		}

		if(nodeReq.getAttribute("codigo-retorno").equals(Integer.toString(Constantes.GPP_RET_OPERACAO_OK)))
		{
			nodeReq.setAttribute("data-requisicao",
					new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
			nodeReq.setAttribute("data-inicial",
					new SimpleDateFormat("dd/MM/yyyy").format(req.getDatPeriodoInicial()));
			nodeReq.setAttribute("data-final",
					new SimpleDateFormat("dd/MM/yyyy").format(req.getDatPeriodoFinal()));

			nodeReq.setAttribute("filial", Integer.toString(req.getIdtFilial()));

			Element nodeCad = extrato.createElement("cadastro");
			nodeCad.setAttribute("nome"			, req.getNomCliente());
			nodeCad.setAttribute("endereco"		, req.getDesLogradouro());
			nodeCad.setAttribute("fachada"		, req.getDesFachada());
			nodeCad.setAttribute("bairro"		, req.getDesBairro());
			nodeCad.setAttribute("complemento"	, req.getDesComplemento().replaceAll(";", " "));
			nodeCad.setAttribute("cidade"		, req.getDesCidade());
			nodeCad.setAttribute("uf"			, req.getDesUf());
			nodeCad.setAttribute("cep"			, req.getDesCep());
			nodeCad.setAttribute("contrato"		, req.getDesContrato());

			nodeReq.appendChild(nodeCad);
		}

		retorno.setVlrRetorno(nodeReq.getAttribute("codigo-retorno"));
		retorno.setDescRetorno(nodeReq.getAttribute("descricao-retorno"));
	}

	public Document getXmlExtrato()
	{
		return extrato;
	}

	public CodigosRetorno getRetorno()
	{
		return retorno;
	}
}
