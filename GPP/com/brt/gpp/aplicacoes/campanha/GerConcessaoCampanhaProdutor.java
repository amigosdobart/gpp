package com.brt.gpp.aplicacoes.campanha;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.campanha.dao.CampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.util.ParametroCampanhaXMLParser;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Esta classe representa o inicio do processamento de concessao de credito, 
 * buscando informacoes de campanhas vigentes a serem processadas e seus 
 * respectivos assinantes inscritos. Para cada assinante o processo Consumidor irah 
 * 
 * executar o trabalho.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class GerConcessaoCampanhaProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{

	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private Map			assinantesCampanha;
	private PREPConexao	conexaoPrep;
	private long		numAssinantes;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public GerConcessaoCampanhaProdutor(long idProcesso)
	{
		super(idProcesso,Definicoes.CL_GER_CONCESSAO_CREDITOS_CAMP_PROD);
	}

	/**
	 * @param params[]
	 * @throws java.lang.Exception
	 */
	public void startup(String params[]) throws Exception 
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio Gerenciamento de Concessao de Creditos de Campanhas");
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		// Como podem existir varias campanhas vigentes, entao busca todas
		// essas campanhas e para cada uma delas busca-se os assinantes
		// inscritos. Todos esses "ponteiros" para os resultados sao armazenados
		// numa lista e entao os consumidores consomem esses assinantes independente
		// da campanha. O produtor soh termina apos o processamento de todos os
		// assinantes de todas as campanhas vigentes
		assinantesCampanha = new HashMap();
		for (Iterator i=CampanhaDAO.getCampanhasVigentes().iterator();i.hasNext();)
		{
			Campanha campanha = (Campanha)i.next();
			assinantesCampanha.put(campanha,AssinanteCampanhaDAO.getAssinantesCampanha(campanha,conexaoPrep));
		}
	}
	
	/**
	 * Disponibiliza o AssinanteCampanha a ser processado
	 * 
	 * @return Object - Informacoes do AssinanteCampanha a ser processado a concessao 
	 * de creditos
	 */
	public Object next() throws Exception
	{
		AssinanteCampanha assinante = null;
		// Realiza uma iteracao em todos os resultSets disponiveis
		// contendo os assinantes das campanhas vigentes. Para cada
		// resultSet entao os registros sao disponibilizados, todos
		// os registros de uma campanha sao processados primeiro antes
		// de passar para a proxima
		for (Iterator i=assinantesCampanha.keySet().iterator(); i.hasNext();)
		{
			Campanha campanha = (Campanha)i.next();
			ResultSet rs = (ResultSet)assinantesCampanha.get(campanha);
			if (rs != null && rs.next())
			{
				assinante = new AssinanteCampanha();
				assinante.setMsisdn					(rs.getString("IDT_MSISDN")			);
				assinante.setDataInclusao			(rs.getDate("DAT_INCLUSAO")			);
				assinante.setDataRetiradaCampanha	(rs.getDate("DAT_RETIRADA_CAMPANHA"));
				assinante.setDataUltimoSMS			(rs.getDate("DAT_ULT_ENVIO_SMS")	);
				assinante.setCampanha				(campanha							);
				String xml = getClobAsString(rs.getClob("XML_DOCUMENT"));
				assinante.setParametros				(ParametroCampanhaXMLParser.parseXMLCampanha(xml));
				
				numAssinantes++;
				break;
			}
		}
		return assinante;
	}
	
	/**
	 * Metodo....:getClobAsString
	 * Descricao.:Realiza a leitura de um campo do tipo Clob para uma string
	 * @param xmlClob
	 * @return
	 */
	private String getClobAsString(Clob xmlClob)
	{
		String xml = null;
		try
		{
		    Reader chr_instream;
		    char chr_buffer[] = new char[(int)xmlClob.length()];
			chr_instream = xmlClob.getCharacterStream();
			chr_instream.read(chr_buffer);
			xml = new String(chr_buffer);
		}
		catch(Exception e)
		{
		}
		return xml;
	}
	
	/**
	 * @return int
	 */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_GER_CONCESSAO_CREDITOS_CAMP;
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getDescricaoProcesso() 
	{
		return "Gerente de Concessao de Creditos processou "+numAssinantes+" assinantes.";
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getStatusProcesso() 
	{
		return statusProcesso;
	}
	
	/**
	 * @param status
	 */
	public void setStatusProcesso(String status) 
	{
		statusProcesso = status;
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getDataProcessamento() 
	{
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * @return com.brt.gpp.comum.conexoes.bancoDados.PREPConexao
	 */
	public PREPConexao getConexao() 
	{
		return conexaoPrep;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	public void finish() throws Exception 
	{
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}
	
	public void handleException() 
	{
	}
}
