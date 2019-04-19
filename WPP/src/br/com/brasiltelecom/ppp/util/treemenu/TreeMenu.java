package br.com.brasiltelecom.ppp.util.treemenu;

import java.util.Vector;

/**
 * Representa um menu em árvore, composto por itens
 * do tipo Grupo ou Opcao (vide classes)
 * 
 * Essa classe é um apoio para geração do XML a ser
 * interpretado pelo Javascript TreeMenu
 * 
 * @author Bernardo Vergne Dias
 * @since 17/01/2007
 */
public class TreeMenu extends TreeMenuItem 
{
	private String idMenu;

	public TreeMenu(String idMenu) {
		this.idMenu = idMenu;
	}
	
	public String getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(String idMenu) {
		this.idMenu = idMenu;
	}
	
	// Retorna um XML representando esse menu e seus nodos filhos.
	// O JS TreeMenu irá transformar o XML numa estrutura de menu DHTML

	public String toXML() {
		Vector childs = getChilds();
		
		String buff = "<menu id=\"" + this.escape(idMenu) + "\">\n";
		
		for (int i = 0; i < childs.size(); i++)
		{
			buff += ((TreeMenuItem)childs.elementAt(i)).toXML() + "\n";
		}
		
		buff += "</menu>";

		return buff;
	}
	
	//	 Prepara o XML para impressao em JavaScript
	
	public String escapeToJS(String xml) 
	{
		xml = xml.replaceAll("\\\\", "\\\\\\\\");
		xml = xml.replaceAll("\"", "\\\\\"");
		xml = xml.replaceAll("'", "\\\\'");		
		xml = "\"" + xml.replaceAll("\n", "\"\n+ \"") + "\"";
		return xml;
	}
}
