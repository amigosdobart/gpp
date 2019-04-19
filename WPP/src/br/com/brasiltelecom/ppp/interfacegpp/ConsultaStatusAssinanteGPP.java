package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.omg.CORBA.ORB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import br.com.brasiltelecom.ppp.dao.PlanoPrecoDAO;
import br.com.brasiltelecom.ppp.model.Assinante;

import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.SaldoCategoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 * Efetua a conexão com o GPP a fim de obter o status do assinante
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 * 
 * Alterado por: Bernardo Vergne 
 * Descrição: Novo XML de consulta completa, removido a segunda consulta
 * ao GPP, removidos* os acessos ao banco de dados (* cache)
 * Data: 08/10/2007
 */
public class ConsultaStatusAssinanteGPP 
{	
	private static Logger logger = Logger.getLogger(ConsultaStatusAssinanteGPP.class);
	/**
	 * Obtém o XML de status do assinante vindo do GPP
	 * 
	 * @param msisdn
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return código XML
	 * @throws Exception
	 */
	public static String getXml(String msisdn,String servidor, String porta) throws Exception
	{
		String ret = "";
		ORB orb = GerenteORB.getORB(servidor, porta);
	
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
	
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.consultaAssinante(msisdn);
		}
		catch (Exception e) 
		{
			logger.error("Erro ao conectar no GPP.", e);
			throw new Exception("Erro ao conectar no GPP");
		}
		return ret;
	}
	
	public static String getXmlSimples(String msisdn,String servidor, String porta) throws Exception
	{
		String ret = "";
		ORB orb = GerenteORB.getORB(servidor, porta);
	
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
	
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		try
		{
			ret = pPOA.consultaAssinanteSimples(msisdn);
		}
		catch (Exception e) 
		{
			logger.error("Erro ao conectar no GPP.", e);
			throw new Exception("Erro ao conectar no GPP");
		}
		return ret;
	}
	
	/**
	 * Retorna dados de um assinante 
	 *
	 * @param msisdn msisdn
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return objeto do tipo Assinante
	 * @throws Exception
	 */
	public static Assinante getAssinante(String msisdn, String servidor, String porta) throws Exception
	{
		// Cria um parse de XML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(getXml(msisdn, servidor, porta)));
		Document doc = parse.parse(is);

		// Interpreta as tags
		Element root = (Element) doc.getElementsByTagName( "GPPConsultaAssinante" ).item(0);
		Assinante assinante = new Assinante();
		parseAssinante(root, assinante);
		
		return assinante;
	}
	
	/**
	 * Retorna dados de um assinante com os devidos tipos de saldos 
	 *
	 * @param msisdn msisdn
	 * @param servidor endereço do servidor
	 * @param porta porta do servidor
	 * @return objeto do tipo Assinante
	 * @throws Exception
	 */
	public static Assinante getAssinanteSaldos(String msisdn, String servidor, 
			String porta, Session session) throws Exception
	{
		// Cria um parse de XML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(getXml(msisdn, servidor, porta)));
		Document doc = parse.parse(is);

		// Interpreta as tags
		Element root = (Element) doc.getElementsByTagName( "GPPConsultaAssinante" ).item(0);
		Assinante assinante = new Assinante();
		parseAssinante(root, assinante);
		
		// Gera a collection de saldos
		ArrayList list = new ArrayList();
		PlanoPreco plano = PlanoPrecoDAO.findById(session, assinante.getPlanoPreco());
		
		if (isAplicavel(root, "saldoCreditosPrincipal"))
		{
			TipoSaldo saldo = findTipoSaldo(plano, TipoSaldo.PRINCIPAL);
			SaldoAssinante saldoAssinante = new SaldoAssinante(msisdn, saldo);
			saldoAssinante.setCreditos(assinante.getSaldoPrincipalDouble());
			saldoAssinante.setDataExpiracao(assinante.getDataExpiracaoPrincipalDate());
			list.add(saldoAssinante);
		}
		
		if (isAplicavel(root, "saldoCreditosPeriodico"))
		{
			TipoSaldo saldo = findTipoSaldo(plano, TipoSaldo.PERIODICO);
			SaldoAssinante saldoAssinante = new SaldoAssinante(msisdn, saldo);
			saldoAssinante.setCreditos(assinante.getSaldoPeriodicoDouble());
			saldoAssinante.setDataExpiracao(assinante.getDataExpiracaoPeridicoDate());
			list.add(saldoAssinante);
		}
		
		if (isAplicavel(root, "saldoCreditosDados"))
		{
			TipoSaldo saldo = findTipoSaldo(plano, TipoSaldo.DADOS);
			SaldoAssinante saldoAssinante = new SaldoAssinante(msisdn, saldo);
			saldoAssinante.setCreditos(assinante.getSaldoDadosDouble());
			saldoAssinante.setDataExpiracao(assinante.getDataExpiracaoDadosDate());
			list.add(saldoAssinante);
		}
		
		if (isAplicavel(root, "saldoCreditosSms"))
		{
			TipoSaldo saldo = findTipoSaldo(plano, TipoSaldo.TORPEDOS);
			SaldoAssinante saldoAssinante = new SaldoAssinante(msisdn, saldo);
			saldoAssinante.setCreditos(assinante.getSaldoSmsDouble());
			saldoAssinante.setDataExpiracao(assinante.getDataExpiracaoSmsDate());
			list.add(saldoAssinante);
		}
		
		if (isAplicavel(root, "saldoCreditosBonus"))
		{
			TipoSaldo saldo = findTipoSaldo(plano, TipoSaldo.BONUS);
			SaldoAssinante saldoAssinante = new SaldoAssinante(msisdn, saldo);
			saldoAssinante.setCreditos(assinante.getSaldoBonusDouble());
			saldoAssinante.setDataExpiracao(assinante.getDataExpiracaoBonusDate());
			list.add(saldoAssinante);
		}
		
		assinante.setSaldoAssinante(list);		
		return assinante;
	}
    
    /**
     * Busca o objeto TipoSaldo dentro do planoPreco informado. <br>
     * O tipoSaldo retornado possui o nomeSaldo preenchido de acordo com o "nome fantasia" relacionado 'a categoria do planoPreco.
     * 
     * @return TipoSaldo
     */
    private static TipoSaldo findTipoSaldo(PlanoPreco planoPreco, short IdtTipoSaldo)
    {
        TipoSaldo saldo = new TipoSaldo();
        for (Iterator i = planoPreco.getCategoria().getTiposSaldo().iterator(); i.hasNext();)
        {
            SaldoCategoria saldoCategoria = (SaldoCategoria)i.next();
            if (saldoCategoria.getTipoSaldo().getIdtTipoSaldo() == IdtTipoSaldo)
            {
                saldo.setIdtTipoSaldo(TipoSaldo.PRINCIPAL);
                saldo.setNomTipoSaldo(saldoCategoria.getNomSaldo());
            }
        }
        return saldo;
    }
	
	/**
	 * Retorna o conteudo de uma tag
	 */
	private static String getTagValue(Element serviceElement, String tag)
	{
		NodeList itemNodes = serviceElement.getElementsByTagName(tag);
		if (itemNodes != null && itemNodes.item(0) != null)
			if (itemNodes.item(0).getChildNodes().item(0) != null)
				return itemNodes.item(0).getChildNodes().item(0).getNodeValue();

		return null;
	}
	
	/**
	 * Retorna o valor (em boolean) do atributo 'aplicavel' de uma tag
	 */
	private static boolean isAplicavel(Element serviceElement, String tag)
	{
		NodeList itemNodes = serviceElement.getElementsByTagName(tag);
		if (itemNodes != null && itemNodes.item(0) != null)
		{
			String aplicavel = ((Element)itemNodes.item(0)).getAttribute("aplicavel");
			if (aplicavel != null)
				return aplicavel.toLowerCase().equals("false") ? false : true;
		}
		
		return false;
	}
	
	/**
	 * Interpreta o XML de consulta completa de assinante, gravando os dados
	 * na entidade 'assinante'
	 * 
	 * @param root Referencia para a tag 'GPPConsultaAssinante'
	 */
	private static void parseAssinante(Element root, Assinante assinante)
		throws Exception
	{		
		assinante.setRetorno(getTagValue(root,"retorno"));
		assinante.setMsisdn(getTagValue(root,"msisdn"));
		assinante.setPlanoPreco(getTagValue(root,"planoPreco"));
		assinante.setDescPlanoPreco(getTagValue(root,"descPlanoPreco"));
		assinante.setSaldoPrincipal(getTagValue(root,"saldoCreditosPrincipal"));
		assinante.setSaldoPeriodico(getTagValue(root,"saldoCreditosPeriodico"));
		assinante.setSaldoBonus(getTagValue(root,"saldoCreditosBonus"));
		assinante.setSaldoSms(getTagValue(root,"saldoCreditosSms"));
		assinante.setSaldoDados(getTagValue(root,"saldoCreditosDados"));
		assinante.setStatusAssinante(getTagValue(root,"statusAssinante"));
		assinante.setDescStatusAssinante(getTagValue(root,"descStatusAssinante"));
		assinante.setStatusPeriodico(getTagValue(root,"statusPeriodico"));
		assinante.setDescStatusPeriodico(getTagValue(root,"descStatusPeriodico"));
		assinante.setStatusServico(getTagValue(root,"statusServico"));
		assinante.setDescStatusServico(getTagValue(root,"descStatusServico"));
		assinante.setDataExpiracaoPrincipal(getTagValue(root,"dataExpiracaoPrincipal"));
		assinante.setDataExpiracaoPeridico(getTagValue(root,"dataExpiracaoPeridico"));
		assinante.setDataExpiracaoBonus(getTagValue(root,"dataExpiracaoBonus"));
		assinante.setDataExpiracaoSms(getTagValue(root,"dataExpiracaoSms"));
		assinante.setDataExpiracaoDados(getTagValue(root,"dataExpiracaoDados"));
		assinante.setDataAtivacao(getTagValue(root,"dataAtivacao"));
		assinante.setImsi(getTagValue(root,"imsi"));
		String ff = getTagValue(root,"friendFamily");
		assinante.setFriendFamily(ff == null ? ";" : ff);
		assinante.setIndPossuiImsi(isAplicavel(root, "imsi"));
		assinante.setIndPossuiAth(isAplicavel(root, "friendFamily"));
	}
}
