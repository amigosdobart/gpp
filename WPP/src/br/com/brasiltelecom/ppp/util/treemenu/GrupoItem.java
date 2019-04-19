package br.com.brasiltelecom.ppp.util.treemenu;

import java.util.Vector;

/**
 * Representa um item do tipo grupo (caixa que abre e fecha).
 * 
 * Essa classe é um apoio para geração do XML a ser
 * interpretado pelo Javascript TreeMenu
 * 
 * @author Bernardo Vergne Dias
 * @since 17/01/2007
 */
public class GrupoItem extends TreeMenuItem 
{
	private String texto;

	public GrupoItem(String texto) 
	{
		this.texto = texto;
	}
	
	public String getTexto() 
	{
		return texto;
	}

	public void setTexto(String texto) 
	{
		this.texto = texto;
	}
	
	public String toXML() {
		Vector childs = getChilds();
		
		String buff = "<grupo texto=\"" + this.escape(texto) + "\">\n";
		
		for (int i = 0; i < childs.size(); i++)
		{
			buff += ((TreeMenuItem)childs.elementAt(i)).toXML() + "\n";
		}
		
		buff += "</grupo>";

		return buff;
	}
}
