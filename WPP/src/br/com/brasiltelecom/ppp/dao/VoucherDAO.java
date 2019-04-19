package br.com.brasiltelecom.ppp.dao;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.brasiltelecom.ppp.interfacegpp.ConsultaVoucherGPP;

import com.brt.gpp.aplicacoes.consultar.Voucher;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.CategoriaTEC;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;

/**
 * Interface para consulta de Voucher.
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 10/07/2007
 */
public class VoucherDAO 
{
 	
	/**
	 * Consulta um Voucher na Technomen. Os relacionamentos
	 * com TipoSaldo e CategoriaTEC são obtidos via banco de dados.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param numero 				Número do voucher
	 * @return 						Instancia de <code>Voucher</code>.
	 */
	public static Voucher findById(Session session, String servidorGPP, String portaGPP, String numeroVoucher)
		throws Exception
	{		
		String xml = ConsultaVoucherGPP.getXml(numeroVoucher, servidorGPP, portaGPP);
		return parseXML(session, xml);
	}
	
	/**
	 * Realiza efetivamente o parser: XML -> Entidade
	 * 
	 * @param xml Voucher em formato XML
	 */
	private static Voucher parseXML(Session session, String xml) throws Exception
	{
		Voucher voucher = new Voucher();
		String temp = null;
		
		if (xml == null) 
		{
			voucher.setCodRetorno((short)Definicoes.RET_ERRO_GENERICO_GPP);
			return voucher;
		}
		
		// Cria um parser de XML
		
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		
		// Executa o parser 
		
		Document doc = parse.parse(is);

		// Interpreta os elementos
		
		Element root = (Element) doc.getElementsByTagName( "GPPConsultaVoucherRetorno" ).item(0);
		voucher.setCodRetorno(getNodeValue(root, "CodigoRetorno"));
		
		// if (!(voucher.getCodRetorno() == 15)) {
		
		// Preenche os atributos do tipo String
		
		voucher.setNumeroVoucher(getNodeValue(root, "NumeroVoucher"));
		voucher.setCodigoSeguranca(getNodeValue(root, "CodigoSeguranca"));
		voucher.setDataExpiracao(getNodeValue(root, "DataExpiracao"));
		voucher.setDataUltimaAtualizacao(getNodeValue(root, "DataUltimaAtualizacao"));
		voucher.setDescStatusVoucher(getNodeValue(root, "DescricaoStatusVoucher"));
		voucher.setUsadoPor(getNodeValue(root, "UsadoPor"));
		voucher.setDataUtilizacao(getNodeValue(root, "DataUtilizacao"));
		voucher.setCanceladoPor(getNodeValue(root, "CanceladoPor"));
		voucher.setDataCancelamento(getNodeValue(root, "DataCancelamento"));
		
		// Preenche os atributos numéricos primitivos
		
		temp = getNodeValue(root, "CodigoStatusVoucher");
		if (temp != null && !temp.equals("")) 
			voucher.setCodStatusVoucher(Short.parseShort(temp));
		
		temp = getNodeValue(root, "TipoVoucher");
		if (temp != null && !temp.equals("")) 
			voucher.setTipoVoucher(Short.parseShort(temp));
		
		// Interpreta a lista de valoresVoucher
		
		// Esse método faz a aquisição via banco dados para completar
		// as informações das entidades CategoriaTEC e TipoSaldo. Os maps abaixo guardam
		// referencia das entidades que já foram buscadas no banco
		Map cacheCategoriasTEC = new HashMap();
		Map cacheTiposSaldo = new HashMap();
		
		NodeList valoresVoucher = root.getElementsByTagName("ValorVoucher");
		voucher.setValoresVoucherPrincipal(new HashMap());
		voucher.setValoresVoucherBonus(new HashMap());
		voucher.setValoresVoucherSm(new HashMap());
		voucher.setValoresVoucherDados(new HashMap());
		
		// Cria um ValorVoucher para cada item da lista
		
		for (int i = 0; i < valoresVoucher.getLength(); i++)
		{
			ValorVoucher valorVoucher = new ValorVoucher();
			Node valorVoucherNode = valoresVoucher.item(i);
			
			// Atributo CategoriaTEC (espera-se que a categoria esteja preenchida
			// com um numero válido, pois ela eh chave do map de valoresVoucher !!!)
			
			String idtCategoriaTEC = valorVoucherNode.getAttributes().getNamedItem("categoriaTEC").getNodeValue();
			if (idtCategoriaTEC != null && !idtCategoriaTEC.equals(""))
			{
				CategoriaTEC categoriaTEC = (CategoriaTEC)cacheCategoriasTEC.get(idtCategoriaTEC);
				if (categoriaTEC == null)
				{
					categoriaTEC = CategoriaTECDAO.findById(session, Short.parseShort(idtCategoriaTEC));
					cacheCategoriasTEC.put(idtCategoriaTEC, categoriaTEC);
				}
				valorVoucher.setCategoriaTEC(categoriaTEC);
			}
			
			// Atributo TipoSaldo			
			
			String idTipoSaldo = valorVoucherNode.getAttributes().getNamedItem("tipoSaldo").getNodeValue();
			if (idTipoSaldo != null && !idTipoSaldo.equals(""))
			{
				TipoSaldo tipoSaldo  = (TipoSaldo)cacheTiposSaldo.get(idTipoSaldo);
				if (tipoSaldo == null)
				{
					tipoSaldo = TipoSaldoDAO.findByIdTipoSaldo(session, Short.parseShort(idTipoSaldo));
					cacheTiposSaldo.put(idTipoSaldo, tipoSaldo);
				}
				valorVoucher.setTipoSaldo(tipoSaldo);
				
				if (valorVoucher.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.PRINCIPAL)
					voucher.getValoresVoucherPrincipal().put(valorVoucher.getCategoriaTEC(), valorVoucher);
				
				if (valorVoucher.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.BONUS)
					voucher.getValoresVoucherBonus().put(valorVoucher.getCategoriaTEC(), valorVoucher);
				
				if (valorVoucher.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.TORPEDOS)
					voucher.getValoresVoucherSm().put(valorVoucher.getCategoriaTEC(), valorVoucher);
				
				if (valorVoucher.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.DADOS)
					voucher.getValoresVoucherDados().put(valorVoucher.getCategoriaTEC(), valorVoucher);
				
			}
			
			// Atributo ValorFace				
			
			String valorFace = valorVoucherNode.getAttributes().getNamedItem("valorFace").getNodeValue();
			if (valorFace != null && !valorFace.equals(""))
			{
				valorVoucher.setValorFace(Integer.parseInt(valorFace));
			}
			
			// Atributos ValorAtual e expiracao (atributos nao-chave da entidade ValorVoucher)
			
			NodeList list = valorVoucherNode.getChildNodes();
			
			for (int j = 0; j < list.getLength(); j++)
			{
				if (list.item(j).getNodeType() != Node.ELEMENT_NODE) continue;
				
				if (list.item(j).getNodeName().equals("ValorAtual"))  
					valorVoucher.setValorAtual(Double.parseDouble(list.item(j).getFirstChild().getNodeValue()));
				
				if (list.item(j).getNodeName().equals("DiasExpiracao"))  
					valorVoucher.setDiasExpiracao(Integer.parseInt(list.item(j).getFirstChild().getNodeValue()));
			}
		}
		
		//}		
		
		return voucher;
	}
	
	/**
	 * Retorna o conteúdo texto de um elemento xml
	 * 
	 * @param from			Instancia de <code>Element</code>
	 * @param nodeName		TagName
	 * @return	Conteúdo texto do primeiro elemento de nome 'nodeName'
	 */
	private static String getNodeValue(Element from, String nodeName)
	{
		if (from.getElementsByTagName(nodeName).item(0).hasChildNodes())
			return from.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
		return null;
	}
}
 
