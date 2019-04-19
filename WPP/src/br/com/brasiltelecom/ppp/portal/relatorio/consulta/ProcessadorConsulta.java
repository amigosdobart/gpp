package br.com.brasiltelecom.ppp.portal.relatorio.consulta;

//Imports Java
import java.util.Map;

//Imports DOM
import org.w3c.dom.Element;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Interface utilizada pelos geradores de relatorios para executar a consulta na fonte de dados 
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public interface ProcessadorConsulta 
{
	
	/**
	 *	Metodo responsavel pela execucao da consulta pelos dados do relatorio
	 * 
	 *	@param		Map			params			Mapeamento de parametros do relatorio (nome, parametros de consulta)	
	 *	@param		Element		elmConsulta		Objeto DOM que representa as propriedades da consulta
	 *	@param		Object		conexao			Conexao com a fonte de dados. O tipo de conexao ira depender do
	 *											tipo da consulta conforme especificado nas propriedades da consulta.
	 *	@return		ResultSet					ResultSet gerado pela execucao da consulta pelos dados do relatorio
	 *	@throws		RelatorioException		
	 */
	public ResultSet processaConsulta(Map params, Element elmConsulta, Object conexao) throws RelatorioException;
	
}
