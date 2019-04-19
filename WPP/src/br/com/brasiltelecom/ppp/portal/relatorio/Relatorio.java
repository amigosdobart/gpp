package br.com.brasiltelecom.ppp.portal.relatorio;

//Imports Java
import java.io.OutputStream;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;

/**
 *	Interface utilizada para geracao de relatorios
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public interface Relatorio 
{
	
	/**
	 *	Executa o relatorio, inserindo o resultado no objeto output passado por parametro.
	 *
	 *	@param	OutputStream		output				String representando o relatorio
	 */
	public void execute(OutputStream output) throws RelatorioException;
	
	/**
	 *	Adiciona um parametro a lista de parametros do relatorio. Existem 3 parametros especiais que devem ser 
	 *	levados em consideracao antes de serem adicionados os parametros das consultas:
	 *			NOME_RELATORIO				Nome do relatorio a ser gerado.
	 *			ARQUIVO_PROPRIEDADES		Localizador (nome e caminho completo) do arquivo de propriedades do
	 *										relatorio.
	 *			CONEXAO						Objeto utilizado para estabelecer conexao com a fonte de dados.
	 *	Os nomes de parametro acima nao devem ser utilizados com parametros de consulta.	
	 *
	 *	@param	String		paramName		Nome do parametro
	 *	@param	Object		paramValue		Valor do parametro
	 */
	public void addParam(String paramName, Object paramValue);
	
}
