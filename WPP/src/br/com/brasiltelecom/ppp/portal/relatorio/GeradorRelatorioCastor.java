package br.com.brasiltelecom.ppp.portal.relatorio;

//Imports do Castor
import org.exolab.castor.jdo.Database;

//Imports do WPP
import br.com.brasiltelecom.ppp.portal.relatorio.GeradorRelatorio;
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;

/**
 *	Gerador de Relatorios com conexao atraves de objetos do Castor
 * 
 *	@author		Daniel Ferreira
 *	@since		20/06/2005
 */
public class GeradorRelatorioCastor extends GeradorRelatorio
{
	
	/**
	 *	Construtor da classe
	 */
	public GeradorRelatorioCastor() throws RelatorioException
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
		Database database = (Database)this.params.get("CONEXAO");
		
		if(database != null)
		{
			database.begin();
			this.conexao = database;
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
		Database database = (Database)this.conexao;
		database.commit();
		database.close();
	}
	
}
