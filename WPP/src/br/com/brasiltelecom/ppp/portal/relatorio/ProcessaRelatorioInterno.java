package br.com.brasiltelecom.ppp.portal.relatorio;

import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Thread para disparar o processamento de um relatorio via engine interna do WPP.
 * 
 * Os parametros devem ser passados via URL (da mesma forma que o reports). Por exemplo:
 * ./relatoriosWPP?NOME_RELATORIO=nomeRelatorio+CNS=61,62,63 
 * 
 *	NOME_RELATORIO			Nome do relatorio a ser gerado.
 *	ARQUIVO_PROPRIEDADES	Localizador (nome e caminho completo) do arquivo de propriedades do relatorio.
 *	CONEXAO					Objeto utilizado para estabelecer conexao com a fonte de dados.
 *
 * @author Bernardo Vergne Dias
 * Data: 18/10/2007
 */
public class ProcessaRelatorioInterno implements Runnable
{
	private Logger logger;
	private ResultadoRelatorio resultado;
	private InitialContext initContext;
	private ServletContext context;
	private Map properties;
	
	public ProcessaRelatorioInterno(ResultadoRelatorio resultado, 
			InitialContext initContext, ServletContext context, Map properties) 
	{
		this.logger = Logger.getLogger(this.getClass());
		this.resultado = resultado;
		this.initContext = initContext;
		this.context = context;
		this.properties = properties;
	}
	
	public void run() 
	{
		Connection connection = null;
		
		try
		{
			resultado.setContentType("application/zip");
			resultado.setContentDisposition("attachment; filename=" + resultado.getNomeArquivo() + ".zip");
			
			String localizador = this.context.getRealPath((String)properties.get("ARQUIVO_PROPRIEDADES"));
			RelatorioFactory factory = new RelatorioFactory(this.context.getRealPath("/WEB-INF/RelatorioFactory.properties"));
			Relatorio relatorio = factory.newRelatorio((String)properties.get("NOME_RELATORIO"), localizador);
			
			//Inserindo os parametros no relatorio
			for (Iterator i = properties.keySet().iterator(); i.hasNext();)
			{
				String nomeParametro = (String)i.next();
				if((!nomeParametro.equals("NOME_RELATORIO")) && (!nomeParametro.equals("ARQUIVO_PROPRIEDADES")) &&
				   (!nomeParametro.equals("CONEXAO")))
				{
					relatorio.addParam(nomeParametro, properties.get(nomeParametro));
				}
			}
			
			//Capturando uma conexao do pool
			String dsName = (String)context.getAttribute(Constantes.DATASOURCE_NAME);
			DataSource source = (DataSource)initContext.lookup(dsName);
			connection = source.getConnection();
			relatorio.addParam("CONEXAO", connection);
			
			ZipOutputStream zipOutput = new ZipOutputStream(resultado.criarArquivo());
			ZipEntry entry = new ZipEntry(resultado.getNomeArquivo() + ".txt");
			zipOutput.putNextEntry(entry);
			relatorio.execute(zipOutput);
			zipOutput.flush();
			zipOutput.close();
			
			resultado.setCodStatus(ResultadoRelatorio.CONCLUIDO);
		}
		catch (Exception e)
		{
			logger.error("Erro em ProcessaRelatorioInterno: " + e);
			resultado.setCodStatus(ResultadoRelatorio.ERRO);
			resultado.setDesStatus(e.getMessage());
			
			try
			{
				connection.close();
			}
			catch (Exception ignore) {}
		}
	}
}