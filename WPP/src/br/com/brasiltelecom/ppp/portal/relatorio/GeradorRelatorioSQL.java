package br.com.brasiltelecom.ppp.portal.relatorio;

//Imports Java
import java.sql.Connection;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.GeradorRelatorio;
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;

/**
 *	Gerador de Relatorios com conexao atraves de objetos do Castor
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class GeradorRelatorioSQL extends GeradorRelatorio
{
	
	/**
	 *	Construtor da classe
	 */
	public GeradorRelatorioSQL() throws RelatorioException
	{
		super();
	}
	
	/**
	 *	Inicia a conexao atraves de objetos do Castor para consultas OQL. O metodo utiliza o parametro CONEXAO para
	 *	obter um objeto Database para iniciar a conexao com o banco de dados.
	 *
	 *	@throws		Exception
	 */	
	public void iniciaConexao() throws Exception
	{
		Connection connection = (Connection)this.params.get("CONEXAO");
		
		if(connection != null)
		{
			this.conexao = connection;
		}
		else
		{
			throw new RelatorioException("Conexao com o banco de dados nao encontrada.");
		}
	}
	
	/**
	 *	Finaliza a conexao apos executar a consulta. O metodo executa commit antes de fechar a conexao com o banco.
	 *
	 *	@throws		Exception
	 */
	public void finalizaConexao() throws Exception
	{
		if(this.conexao != null)
		{
			((Connection)this.conexao).close();
		}
	}
	
}
