package br.com.brasiltelecom.ppp.portal.relatorio.resultado;

//Imports Java
import java.io.OutputStream;
import java.util.Map;

//Imports DOM
import org.w3c.dom.Element;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Interface utilizada pelos geradores de relatorios para obter o objeto String contendo o resultado do relatorio.
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public interface GeradorResultado 
{
	
	/**
	 *	Metodo responsavel pela formatacao do relatorio conforme especificado nas propriedades do relatorio
	 * 
	 *	@param		Map						params			Mapeamento de parametros do relatorio (nome, parametros  
	 *														de consulta)	
	 *	@param		ResultSet				dados			ResultSet gerado pela execucao da consulta pelos dados do 
	 *														relatorio
	 *	@param		Element					elmRelatorio	Objeto DOM que representa as propriedades do relatorio
	 *	@param		OutputStream			output			Objeto onde o relatorio e inserido
	 *	@throws		RelatorioException		
	 */
	public void geraResultado(Map params, ResultSet dados, Element elmRelatorio, OutputStream output) 
		throws RelatorioException;
	
}
