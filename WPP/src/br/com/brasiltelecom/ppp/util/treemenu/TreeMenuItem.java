package br.com.brasiltelecom.ppp.util.treemenu;

import java.util.Vector;

/**
 * Representa um item do menu.
 * Todo item pode conter um ou mais itens.
 * 
 * Essa classe é um apoio para geração do XML a ser
 * interpretado pelo Javascript TreeMenu
 * 
 * @author Bernardo Vergne Dias
 * @since 17/01/2007
 */
public abstract class TreeMenuItem 
{
	private TreeMenuItem parent = null;
	private Vector childs = new Vector();
	
	public void addItem(TreeMenuItem item)
	{
		item.setParent(this);
		childs.add(item);
	}
	
	public Vector getChilds() 
	{
		return childs;
	}
	
	public TreeMenuItem getParent() 
	{
		return parent;
	}
	
	public void setParent(TreeMenuItem parent) 
	{
		this.parent = parent;
	}
	
	public abstract String toXML();
	
	/**
	 * Faz o escape de caracteres para os atributos XML
	 */
	public String escape(String str)
	{
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("\"", "&quot;");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		return str;
	}
}
