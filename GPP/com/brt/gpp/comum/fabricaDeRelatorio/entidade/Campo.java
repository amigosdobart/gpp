package com.brt.gpp.comum.fabricaDeRelatorio.entidade;

import java.lang.reflect.Field;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Entidade que define um campo do objeto <code>Relatorio</code>
 * @author Magno Batista Corrêa
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 *
 *  Atualizado Leone Parise
 *  Descrição: Suporta a formatacao Multi-thread
 *  Data: 20/11/2007
 */
public class Campo
{
	public static final String DATE = "java.sql.Date";
	public static final String STRING = "java.lang.String";
	public static final String NUMBER = "java.lang.Number";

	/**
	 * Nome pelo qual o campo é reconhecido dentro da aplicação.
	 */
	private String	nomeInterno		;
	/**
	 * Nome pelo qual o campo é reconhecido pelo usuário.
	 */
	private String	nomeExterno		;
	/**
	 * Tipo do Campo. Pode ser uma das constantes: <code>DATE,STRING,NUMBER</code>.
	 */
	private String	tipo			;
	/**
	 * Define uma máscara para o formato. Se for o tipo for <code>DATE</code>,
	 * então a <code>mascaraFormato</code> pode ser <code>DD/MM/YYYY</code>.
	 */
	private String	mascaraFormato	;

	public Campo(String nomeInterno, String nomeExterno, String tipoCurto, String mascaraFormato)
	{
		this.nomeInterno	= nomeInterno;
		this.nomeExterno	= nomeExterno;
		this.mascaraFormato	= mascaraFormato;

		if("DATE".equalsIgnoreCase(tipoCurto))
		{
			this.tipo			= DATE;
		}
		else if("STRING".equalsIgnoreCase(tipoCurto))
		{
			this.tipo			= STRING;
		}
		else if("NUMBER".equalsIgnoreCase(tipoCurto))
		{
			this.tipo			= NUMBER;
		}
	}

	/**
	 * Faz o parse de um <code>Element</code> para objeto <code>Campo</code>.
	 * É uma forma de construtor da classe.
	 * @param elmCampo
	 * @return
	 */
	public static Campo parseXML(Element elmCampo)
	{
		if(elmCampo == null)
			return null;

		String	campoNomeInterno	= getValue(elmCampo.getAttributeNode("nomeInterno"));
		String	campoNomeExterno	= getValue(elmCampo.getAttributeNode("nomeExterno"));
		String	campoTipoCurto		= getValue(elmCampo.getAttributeNode("tipo"));
		String	campoMascaraFormato	= getValue(elmCampo.getAttributeNode("mascaraFormato"));

		return new Campo(campoNomeInterno,campoNomeExterno,campoTipoCurto,campoMascaraFormato);

	}
	/**
	 * Faz o parse de um <code>NodeList</code> para uma lista de <code>Campo</code>.
	 * É uma forma de construtor da classe em blocos.
	 *
	 * @return Campo[] Uma lista com todos os campos dentro de um <code>NodeList</code>.
	 */
	public static Campo[] parseXML(NodeList ndlstCampo)
	{
		if(ndlstCampo == null)
			return new Campo[0];

		int size = ndlstCampo.getLength();
		Campo[] saida = new Campo[size];

		for(int i = 0; i < size; i++)
		{
			Element elmCampo = (Element)ndlstCampo.item(i);
			saida[i] = parseXML(elmCampo);
		}

		return saida;
	}

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
				saida.append(tmp.get(this).toString());
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

	public String getMascaraFormato()
	{
		return mascaraFormato;
	}

	public void setMascaraFormato(String mascaraFormato)
	{
		this.mascaraFormato = mascaraFormato;
	}

	public String getNomeExterno()
	{
		return nomeExterno;
	}

	public void setNomeExterno(String nomeExterno)
	{
		this.nomeExterno = nomeExterno;
	}

	public String getNomeInterno()
	{
		return nomeInterno;
	}

	public void setNomeInterno(String nomeInterno)
	{
		this.nomeInterno = nomeInterno;
	}

	public String getTipo()
	{
		return tipo;
	}

	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
}
