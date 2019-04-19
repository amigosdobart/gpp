package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.brt.gpp.comum.ConsoleUtil;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Campo;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Relatorio;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.interfaceEscrita.InterfaceConfiguracao;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
import com.brt.gpp.comum.operacaoArquivo.OperadorDeArquivo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * Gera relatorios a partir da linha de comando.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 09/10/2007
 *
 *  Atualizado por Bernardo Vergne
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 *
 *  Atualizado por Leone Parise
 *  Descrição: Bug fix na formatacao do nome do relatorio
 *  Data: 20/11/2007
 */
public class ProdutorFabricaSQL extends ProdutorFabrica implements ProcessoBatchProdutor
{
	private ResultSet	registros;
	private String 		idRelatorioEntrada;
	private String		idRelatorioSaida;
	private Relatorio	relatorioEntrada;
	private Relatorio	relatorioSaida;
	private List		parametrosSQL;
	private String		pathRaiz;
	private String		pathTemp;
	private String		nomeArquivo;
	private InterfaceConfiguracao configArquivo;

	public static final String NULO = "0";

	public ProdutorFabricaSQL(long logId)
	{
		super(logId, Definicoes.CL_PRODUTOR_FABRICA_SQL);
		status	= Definicoes.PROCESSO_SUCESSO;
	}

	/**
	 * Startup do processo batch. Este metodo executa um SQL e captura os resultados
	 * para serem utilizados como parametros nos consumidores.
	 *
	 * @param params Lista de parametros<br>
	 * <pre>
	 * [0] - Id do relatorio de Entrada.(Obrigatorio)
	 * [1] - Id do relatorio de Saida.(Obrigatorio)
	 *       Para desativar o relatorio de saida utilize o caracter 0.
	 * [2]
	 *  .  - Parametros do SQL que sera executado no relatorio de entrada.
	 * [n]
	 * </pre>
	 */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "ProdutorFabricaSQL.startup", "Inicio");

		try
		{
			validarParametros(params);
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.logId);

			/*
			 * O Relatorio de entrada eh obrigatorio.
			 * Caso o ID do Relatorio de Saida seja NULO("0"), criar um novo relatorio
			 * baseado no Relatorio de Entrada mas SEM SQL. Este objeto vai armazenar
			 * as configuracoes necessarias para a geracao dos arquivos de saida.
			 */
			relatorioEntrada = geraRelatorio(idRelatorioEntrada);
			if(idRelatorioSaida.equals(NULO))
			{
				relatorioSaida = geraRelatorio(idRelatorioEntrada);
				relatorioSaida.setSql(null);
				relatorioSaida.setIdRelatorio(NULO);
			}
			else
				relatorioSaida = geraRelatorio(idRelatorioSaida);

			if (relatorioEntrada == null || relatorioSaida == null)
				throw new GPPInternalErrorException("Informe ID(s) de relatorio(s) valido(s).");

			// Recupera parametros para Execucao do SQL de acordo com a quantidade de campos
			int numCamposEntrada = relatorioEntrada.getCamposEntrada().length;
			if (parametrosSQL.size() < numCamposEntrada)
				throw new GPPInternalErrorException("Parametros insuficientes para execucao do Relatorio.");

			// Filtra os paramentros de entrada do processo batch de acordo com a quantidade
			// esperada pelo relatorio de entrada
			while(parametrosSQL.size() > numCamposEntrada)
				parametrosSQL.remove(parametrosSQL.size()-1);

			if(idRelatorioSaida.equals(NULO))
				setPathRaiz(idRelatorioEntrada, conexaoPrep);
			else
				setPathRaiz(idRelatorioSaida, conexaoPrep);

			// Define Paths de Saida
			String fileSeparator = System.getProperty("file.separator");
			pathSaida = pathRaiz + "SAIDA" + fileSeparator;
			pathErro  = pathRaiz + "ERRO"  + fileSeparator;
			pathTemp  = pathRaiz + "TEMP"  + fileSeparator;

			// Abre Arquivos de Erro
			arquivoErroTemp = new ArquivoEscrita(pathErro + nomeArquivo + ".xml");
			arquivoErroTemp.escrever("<ROOT>");

			// Define Configuracoes do Arquivo de Saida
			configArquivo = relatorioSaida.getFormatoArquivo();
			configArquivo.setAtributo("path", pathTemp + nomeArquivo);
			configArquivo.setAtributo("observacao", "Processo executado com SUCESSO!");
			arquivoSaidaTemp = configArquivo.getInterfaceEscrita();

			// Lista de Registros que serao processados no Relatorio de Saida.
			registros = conexaoPrep.executaPreparedQuery(
					relatorioEntrada.getSql(),
					montarParametros(
							parametrosSQL.toArray(),
							relatorioEntrada.getCamposEntrada().length,
							relatorioEntrada.getCamposEntradaPosicoes()),
					super.logId);

			// Abre Arquivo de Saida
			arquivoSaidaTemp.abrir();
		}
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao: " + e;
			throw e;
		}
		finally
		{
			super.log(Definicoes.INFO, "ProdutorFabricaSQL.startup", "Fim");
		}
	}

	/**
	 * Valida os parametros dados. Preenche os atributos necessarios.
	 *
	 * @param params Lista de parametros
	 * @throws GPPInternalErrorException Caso algum parametro seja invalido
	 */
	private void validarParametros(String[] params) throws GPPInternalErrorException
	{
		if(params == null || params.length < 2)
			throw new GPPInternalErrorException("Os dois primeiros parametros sao necessarios para a " +
					"execucao do processo batch");

		if(params[0].equals(NULO))
			throw new GPPInternalErrorException("O ID do Relatorio de Entrada NAO deve ser NULO ('0')");

		idRelatorioEntrada = params[0];
		idRelatorioSaida   = params[1];

		ConsoleUtil paramUtil = new ConsoleUtil(params);
		nomeArquivo = ConsoleUtil.formatarString((String)paramUtil.getMapaParametros().get("arqSaida"),
															   paramUtil.getListaParametros());
		parametrosSQL = paramUtil.getListaParametros().subList(2, paramUtil.getListaParametros().size());
		//Caso o parametro -formato nao seja informado usar nome de arquivo Default
		if(nomeArquivo == null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			nomeArquivo = idRelatorioEntrada + "." + sdf.format(new Date()) + ".csv";
		}
		else
			nomeArquivo = nomeArquivo.replaceAll("[\\/\\\\]", "");
	}

	/**
	 * Recupera path de saida de acordo com o Id do Relatorio
	 *
	 * @param idRelatorio					Id do Relatorio
	 * @throws GPPInternalErrorException	Caso ocorra algum erro na execucao da Query
	 * @throws SQLException					Caso ocorra algum erro na execucao do metodo next() do ResultSet
	 */
	private void setPathRaiz(String idRelatorio, PREPConexao conexao) throws GPPInternalErrorException, SQLException
	{
		String sql =
		    " SELECT								" +
			"	path_trabalho AS PATH				" +
			" FROM TBL_REL_FABRICA_RELATORIO		" +
			" WHERE	IDT_RELATORIO = ?				" ;

		ResultSet rs = conexao.executaPreparedQuery(sql, new String[]{idRelatorio}, super.logId);

		if(rs.next())
		{
			StringBuffer sb = new StringBuffer();
			sb.append(ArquivoConfiguracaoGPP.getInstance().getString("DIR_ROOT_FABRICA_RELATORIO"));
			sb.append(System.getProperty("file.separator"));
			sb.append(rs.getString("PATH"));
			sb.append(System.getProperty("file.separator"));

			pathRaiz = sb.toString();
		}

		rs.close();
	}

	public Object next() throws Exception
	{
		VOFabrica vo = null;
		Campo[] campos = relatorioEntrada.getCamposSaida();

		try
		{
			if(registros.next())
			{
				vo = new VOFabrica();

				// Obtém os valores do banco (resultado do select)
				Object[] valores = new Object[campos.length];
				for(int i = 0; i < valores.length; i++)
					valores[i] = registros.getObject(campos[i].getNomeInterno());

				// Se nao existe o relatorio de saida, entao o consumidor nao
				// fara qualquer processamento, de forma que os paramentros enviados
				// para ele serao gravados diretamente no arquivo de saida. Dessa forma,
				// verifica-se a flag 'EscreveParametroSaida' para incluir os parametros
				// de entrada do relatorio
				if (idRelatorioSaida.equals(NULO))
				{
					if (relatorioSaida.getFlagEscreveParametroSaida())
					{
						Object[] saida = new Object[parametrosSQL.size() + campos.length];

						System.arraycopy(parametrosSQL.toArray(), 0, saida, 0, parametrosSQL.size());
						System.arraycopy(valores, 0, saida, parametrosSQL.size(), relatorioSaida.getCamposSaida().length);

						vo.setParametros(saida);
					}
					else
						vo.setParametros(valores);
				}
				else
				{
					vo.setParametros(montarParametros(valores,
							relatorioSaida.getCamposEntrada().length,
							relatorioSaida.getCamposEntradaPosicoes()));
				}

				numRegistros++;
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "ProdutorFabricaSQL.next", "Excecao: " + e.getMessage());
		    status = Definicoes.PROCESSO_ERRO;
		}
		return vo;
	}

	public void finish() throws Exception
	{
		mensagem = "Registros processados: "+numRegistros;
		try
		{
		    finalizacoes();
		    mensagem = mensagem.concat(String.valueOf(numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "Produtor.finish", "Excecao: " + e);
		    status = Definicoes.PROCESSO_ERRO;
		    mensagem = "Excecao: " + e;
		}
		finally
		{
			//Liberando a conexao
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		}
	}

	/**
	 * Fecha e compacta Arquivo de saida e remove Arquivo de Erro.
	 */
	private void finalizacoes() throws Exception
	{
		arquivoErroTemp.escrever("</ROOT>");
		arquivoErroTemp.fechar();
		arquivoSaidaTemp.fechar();

		if(!status.equals(Definicoes.PROCESSO_SUCESSO))
			configArquivo.setAtributo("observacao", "Processo executado com ERRO!");

		if (relatorioSaida != null  && relatorioSaida.isArquivoCompactado())
		{
			try
			{
				OperadorDeArquivo.compactaArquivo((String)configArquivo.getAtributo("path"), pathSaida);
				removerArquivo((String)configArquivo.getAtributo("path"));
			}
			catch (Exception e)
			{
				OperadorDeArquivo.moverArquivo((String)configArquivo.getAtributo("path"), pathSaida);
			}
		}
		else
			OperadorDeArquivo.moverArquivo((String)configArquivo.getAtributo("path"), pathSaida);

		File fileErro = new File(arquivoErroTemp.getPath());

		//Exclui os arquivos de erro no caso de estarem vazios
		long bits = "<ROOT></ROOT>".getBytes().length;
		if (fileErro.length() == bits)
		{
			fileErro.delete();
		}
		else
		{
			removerArquivo(this.pathErro + this.nomeArquivoErro);
			OperadorDeArquivo.moverArquivo(this.arquivoErroTemp.getPath(),this.pathErro);
		}
	}
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_FABRICA_RELATORIO_SQL;
	}

	public void handleException()
	{
	}

	public Relatorio getRelatorio()
	{
		return relatorioSaida;
	}
}
