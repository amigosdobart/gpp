package com.brt.gpp.comum.fabricaDeRelatorio.entidade;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.fabricaDeRelatorio.RelatorioException;
import com.brt.gpp.comum.interfaceEscrita.ArquivoDelimitadoConfig;
import com.brt.gpp.comum.interfaceEscrita.ArquivoInterfaceConfig;
import com.brt.gpp.comum.interfaceEscrita.InterfaceConfiguracao;

/**
 * @author Magno Batista Corrêa
 * @version 1.0
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 */
public class Relatorio
{
	public static final String DELIMITADO		= "DELIMITADO";
	public static final String INTERFACE		= "INTERFACE";
	public static final String INTERFACE_DEL	= "INTERFACE_DEL";

	public static final int CONSULTA_TIPO_PREPR	= 0;
	public static final int CONSULTA_TIPO_SQL	= 1;

	/**
	 * Id do relatorio no banco
	 */
	private String idRelatorio;
	/**
	 * O formato no qual o arquivo deva ser gerado. <BR>
	 * Pode ser uma das constantes: <code>DELIMITADO, HTML, XML</code>.</BR>
	 */
	private InterfaceConfiguracao	formatoArquivo;
	/**
	 * Nome pelo qual o campo é reconhecido dentro da aplicação.
	 */
	private String		nomeInterno;
	/**
	 * Nome pelo qual o campo é reconhecido pelo usuário.
	 */
	private String		nomeExterno;
	/**
	 * Se é para mostrar o título do relatório
	 */
	private boolean		mostraTitulo;
	/**
	 * Se é para mostrar a lista de parâmetros de entrada.
	 */
	private boolean		mostraParametro;
	/**
	 * Se é para mostrar o cabeçalho com os nomes dos campos.
	 */
	private boolean		mostraCabecalho;
	/**
	 * Se é para compactar o arquivo
	 */
	private boolean		arquivoCompactado;
	/**
	 * O delimitador do arquivo de saída. Para o formato <code>DELIMITADO</code>.
	 */
	private String		delimitador;
	/**
	 * O tipo de consulta a ser feito na base. <BR>
	 * Pode ser uma das constantes:
	 * <code>CONSULTA_TIPO_PREPR, CONSULTA_TIPO_SQL</code>.</BR>
	 */
	private int			consultaTipo;
	/**
	 * Os parâmetros de entrada para a geração do relatório. <BR>
	 * Deve ser os nomes internos dos campos.</BR>
	 */
	private Campo[]		camposEntrada;
	/**
	 * Posicoes dos campos de entrada
	 */
	private int[]		camposEntradaPosicoes;
	/**
	 * A pesquiza a ser realizada dentro do banco para a geração do relatório.
	 */
	private String		sql;
	/**
	 * Uma série de campos de saída do relatório.
	 */
	private Campo[]		camposSaida;
	/**
	 * Flag que indica se o consumidor irá escrever o valor do parametro na saída
	 */
	private boolean		flagEscreveParametroSaida;

	public Relatorio(String nomeInterno, String nomeExterno,
			boolean mostraTitulo, boolean mostraParametro,
			boolean mostraCabecalho, String formatoArquivo,
			boolean arquivoCompactado, String delimitador, String consultaTipo,
			Campo[] camposEntrada, int[] camposEntradaPosicoes, String sql, Campo[] camposSaida,
			boolean flagEscreveParametroSaida)
	{
		this.nomeInterno				= nomeInterno;
		this.nomeExterno				= nomeExterno;
		this.mostraTitulo				= mostraTitulo;
		this.mostraParametro			= mostraParametro;
		this.mostraCabecalho			= mostraCabecalho;
		this.arquivoCompactado			= arquivoCompactado;
		this.flagEscreveParametroSaida 	= flagEscreveParametroSaida;

		String[] cabecalho = null;

		if (flagEscreveParametroSaida)
		{
			cabecalho = new String[camposSaida.length + camposEntrada.length];
			for(int i = 0; i < camposEntrada.length; i++)
				cabecalho[i] = camposEntrada[i].getNomeExterno();
			for(int i = 0; i < camposSaida.length; i++)
				cabecalho[camposEntrada.length+i] = camposSaida[i].getNomeExterno();
		}
		else
		{
			cabecalho = new String[camposSaida.length];
			for(int i = 0; i < camposSaida.length; i++)
				cabecalho[i] = camposSaida[i].getNomeExterno();
		}

		if (DELIMITADO.equalsIgnoreCase(formatoArquivo))
		{
			this.formatoArquivo = new ArquivoDelimitadoConfig(delimitador, nomeExterno, cabecalho);
			((ArquivoDelimitadoConfig)this.formatoArquivo).setMostraCabecalho(mostraCabecalho);
			((ArquivoDelimitadoConfig)this.formatoArquivo).setMostraTitulo(mostraTitulo);
		}
		else if(INTERFACE.equalsIgnoreCase(formatoArquivo))
		{
			this.formatoArquivo = new ArquivoInterfaceConfig(ArquivoInterfaceConfig.REMESSA,
					nomeExterno, Definicoes.SO_GPP,"");
		}
		else if(INTERFACE_DEL.equalsIgnoreCase(formatoArquivo))
		{
			this.formatoArquivo = new ArquivoInterfaceConfig(ArquivoInterfaceConfig.REMESSA,
					nomeExterno, Definicoes.SO_GPP,"");
			((ArquivoInterfaceConfig)this.formatoArquivo).setDelimitado(true);
		}

		if ("PREPR".equalsIgnoreCase(consultaTipo))
		{
			this.consultaTipo = Relatorio.CONSULTA_TIPO_PREPR;
		}
		else if("SQL".equalsIgnoreCase(consultaTipo))
		{
			this.consultaTipo = Relatorio.CONSULTA_TIPO_SQL;
		}

		this.delimitador			= delimitador;
		this.camposEntrada			= camposEntrada;
		this.camposEntradaPosicoes	= camposEntradaPosicoes;
		this.sql					= sql;
		this.camposSaida			= camposSaida;
	}

	/**
	 * Faz o parse de um <code>Element</code> para objeto
	 * <code>Relatorio</code>. É uma forma de construtor da classe.
	 */
	public static Relatorio parseXML(Element elmRelatorio) throws RelatorioException
	{
		String	nomeInterno				= null;
		String	nomeExterno				= null;
		boolean	mostraTitulo			= false;
		boolean	mostraParametro			= false;
		boolean	mostraCabecalho			= false;
		boolean	arquivoCompactado		= false;
		String	formatoArquivo			= null;
		String	delimitador				= null;
		String	consultaTipo    		= null;
		Campo[]	camposEntrada      	 	= null;
		int[]	camposEntradaPosicoes 	= null;
		String	sql             		= null;
		Campo[]	camposSaida        		= null;
		boolean flagRegistroDireto		= false;

		// Obtendo as informações do relatório
		nomeInterno 		= getValue(elmRelatorio.getAttributeNode("nomeInterno"));
		nomeExterno 		= getValue(elmRelatorio.getAttributeNode("nomeExterno"));
		formatoArquivo 		= getValue(elmRelatorio.getAttributeNode("formatoArquivo"));
		delimitador 		= getValue(elmRelatorio.getAttributeNode("delimitador"));
		mostraTitulo 		= isTrue(elmRelatorio.getAttributeNode("mostraTitulo"));
		mostraParametro 	= isTrue(elmRelatorio.getAttributeNode("mostraParametro"));
		mostraCabecalho 	= isTrue(elmRelatorio.getAttributeNode("mostraCabecalho"));
		arquivoCompactado 	= isTrue(elmRelatorio.getAttributeNode("arquivoCompactado"));
		flagRegistroDireto 	= isTrue(elmRelatorio.getAttributeNode("escreveParametroSaida"));

		Element elmConsulta = getElement(elmRelatorio, "consulta");
		if(elmConsulta != null)
		{
			consultaTipo = getValue(elmConsulta.getAttributeNode("tipo"));

			NodeList ndlstCampo = elmConsulta.getElementsByTagName("campo");
			camposEntrada = Campo.parseXML(ndlstCampo);

			sql = getNodeValue(elmConsulta, "sql");

			String posicoes = getNodeValue(elmConsulta, "posicoes");

			if (posicoes != null)
			{
				String[] posicoesSplit = posicoes.split(";");
				int sizePos = posicoesSplit.length;
				camposEntradaPosicoes = new int[sizePos];
				for (int pos = 0 ; pos < sizePos ; pos++)
					camposEntradaPosicoes[pos] = Integer.parseInt(posicoesSplit[pos]);
			}
			else
			{
				int sizePos = camposEntrada.length;
				camposEntradaPosicoes = new int[sizePos];
				for (int pos = 0 ; pos < sizePos ; pos++)
					camposEntradaPosicoes[pos] = pos;
			}
		}
		else
			throw new RelatorioException("Propriedade de consulta nao encontrada");

		Element elmDado = getElement(elmRelatorio, "dado");
		if(elmDado != null)
		{
			Element elmRegistro = getElement(elmDado, "registro");
			if(elmRegistro != null)
			{
				NodeList ndlstCampo = elmRegistro.getElementsByTagName("campo");
				camposSaida = Campo.parseXML(ndlstCampo);
			}
		}

		return new  Relatorio(nomeInterno, nomeExterno,	mostraTitulo, mostraParametro,
				mostraCabecalho, formatoArquivo, arquivoCompactado, delimitador,
				consultaTipo,	camposEntrada, camposEntradaPosicoes, sql, camposSaida, flagRegistroDireto);
	}


	public String getIdRelatorio()
	{
		return idRelatorio;
	}

	public boolean isArquivoCompactado()
	{
		return arquivoCompactado;
	}

	public Campo[] getCamposEntrada()
	{
		return camposEntrada;
	}

	public int[] getCamposEntradaPosicoes()
	{
		return camposEntradaPosicoes;
	}

	public Campo[] getCamposSaida()
	{
		return camposSaida;
	}

	public int getConsultaTipo()
	{
		return consultaTipo;
	}

	public String getDelimitador()
	{
		return delimitador;
	}

	public boolean getFlagEscreveParametroSaida()
	{
		return flagEscreveParametroSaida;
	}

	public InterfaceConfiguracao getFormatoArquivo()
	{
		return formatoArquivo;
	}

	public boolean isMostraCabecalho()
	{
		return mostraCabecalho;
	}

	public boolean isMostraParametro()
	{
		return mostraParametro;
	}

	public boolean isMostraTitulo()
	{
		return mostraTitulo;
	}

	public String getNomeExterno()
	{
		return nomeExterno;
	}

	public String getNomeInterno()
	{
		return nomeInterno;
	}

	public String getSql()
	{
		return sql;
	}

	public void setIdRelatorio(String idRelatorio)
	{
		this.idRelatorio = idRelatorio;
	}

	public void setSql(String sql)
	{
		this.sql = sql;
	}

	/*                                                            *
	 * =================== METODOS UTILITARIOS ===================*
	 *                                                            */

	/**
	 * Torna esta classe em uma <code>String</code> no formato xml
	 */
	public String toString()
	{
		StringBuffer saida = new StringBuffer();
		saida.append("<");
		saida.append(this.getClass().getName());
		saida.append(">");
		Field[] fields = this.getClass().getDeclaredFields();
		int size = fields.length;

		for(int i = 0 ; i < size ; i++)
		{
			Field tmp = fields[i];
			saida.append("<");
			saida.append(tmp.getName());
			saida.append(">");

			try
			{
				Object tmpObj = tmp.get(this);
				Class tmpClass = tmpObj.getClass();
				if(tmpClass.isArray())
				{
					int sizeArray = Array.getLength(tmpObj);
					for(int j = 0 ; j < sizeArray ; j++)
					{
						saida.append(Array.get(tmpObj,j).toString());
					}
				}
				else
				{
					saida.append(tmp.get(this).toString());
				}
			}
			catch (Exception e)
			{
				saida.append("NULL");
			}

			saida.append("</");
			saida.append(tmp.getName());
			saida.append(">");
		}

		saida.append("</");
		saida.append(this.getClass().getName());
		saida.append(">");

		return saida.toString();
	}

	/**
	 * Captura o valor de um atributo
	 */
	private static String getValue(Attr entrada)
	{
		if (entrada != null)
			return entrada.getNodeValue();

		return null;
	}

	/**
	 * Testa se uma string é true, True ou TRUE e retorna um boleano se sim
	 */
	private static boolean isTrue(Attr entrada)
	{
		if (entrada != null)
			return Boolean.valueOf(entrada.getNodeValue()).booleanValue();

		return false;
	}
	/**
	 * Retorna uma Collection de Strings contendo os Valores de um determinado no.
	 *
	 * @param root		No raiz
	 * @param nodeName	Nome do no que contem os valores
	 * @return			Nulo caso o no nao exista ou Collection vazia caso nao haja valores
	 */
	public static Collection getNodeListValues(Element root, String nodeName)
	{
		Collection values = new ArrayList();
		if(root.getElementsByTagName(nodeName) != null)
		{
			NodeList nodeList = root.getElementsByTagName(nodeName);
			for(int i = 0; i < nodeList.getLength(); i++)
				if(nodeList.item(i) != null &&
				   nodeList.item(i).getFirstChild() != null &&
				   nodeList.item(i).getFirstChild().getNodeValue() != null)
					   values.add(nodeList.item(i).getFirstChild().getNodeValue());
		}

		return values;
	}

	/**
	 * Retorna uma String contendo o valor do no especificado
	 *
	 * @param root		Elemento raiz
	 * @param nodeName	Nome do no que contem os valores
	 * @return			<code>null</code> caso o no nao exista ou uma String
	 */
	public static String getNodeValue(Element root, String nodeName)
	{
		if(root.getElementsByTagName(nodeName) != null &&
		   root.getElementsByTagName(nodeName).item(0) != null &&
		   root.getElementsByTagName(nodeName).item(0).getFirstChild() != null)
			return root.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();

		return null;
	}

	/**
	 * Retorna um No de acordo com o Nome e o Elemento raiz dado
	 *
	 * @param root		Elemento raiz
	 * @param nodeName	Nome do No que sera retornado
	 * @return			<code>null</code> caso o NO nao exista ou um objeto do tipo Node.
	 */
	public static Element getElement(Element root, String nodeName)
	{
		if(root.getElementsByTagName(nodeName) != null &&
		   root.getElementsByTagName(nodeName).item(0) != null)
			return (Element)root.getElementsByTagName(nodeName).item(0);

		return null;
	}
}
