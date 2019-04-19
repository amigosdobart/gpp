package com.brt.gppEnviaMail.aplicacao;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.brt.gppEnviaMail.conexoes.ConexaoBancoDados;

public class ProcessadorResultadoCSV implements ProcessadorArquivo
{
	private Writer writer;

	public StringBuffer getResultadoComoBuffer(String sql, String separador) throws IOException
	{
		// Utiliza como writer do resultado um buffer de string
		writer = new StringWriter();
		try
		{
			// Cria a conexao com o banco de dados executa o comando SQL 
			// e utiliza o Resultset para processar as informacoes
			// No final fecha os objetos para poderem ser reutilizados
			ConexaoBancoDados conexao = ConexaoBancoDados.getInstance();
			Statement stm = conexao.getConnection().createStatement();
			ResultSet rs = stm.executeQuery(sql);
			// Executa o processamento
			processaResultadoComando(rs,separador);
			// Fecha os objetos de conexao
			rs.close();
			stm.close();
			conexao.close();
		}
		catch(SQLException se)
		{
			escreveNoArquivo("Erro durante a execucao da consulta ao banco de dados: " + se.toString());
		}
		StringBuffer resultado = ((StringWriter)writer).getBuffer();
		return resultado;
	}

	public StringBuffer getResultadoComoBuffer(ResultSet rs, String separador) throws IOException
	{
		// Utiliza como writer do resultado um buffer de string
		writer = new StringWriter();
		processaResultadoComando(rs,separador);
		StringBuffer resultado = ((StringWriter)writer).getBuffer();
		return resultado;
	}
	
	public File getResultadoComoArquivo(String sql, String nomeArquivo, String separador) throws IOException
	{
		File arquivoRetorno = new File(nomeArquivo);
		// Utiliza como writer do output um arquivo
		writer = new FileWriter(arquivoRetorno);
		try
		{
			// Cria a conexao com o banco de dados executa o comando SQL 
			// e utiliza o Resultset para processar as informacoes
			// No final fecha os objetos para poderem ser reutilizados
			ConexaoBancoDados conexao = ConexaoBancoDados.getInstance();
			Statement stm = conexao.getConnection().createStatement();
			ResultSet rs = stm.executeQuery(sql);
			// Executa o processamento
			processaResultadoComando(rs,separador);
			// Fecha os objetos de conexao
			rs.close();
			stm.close();
			conexao.close();
		}
		catch(SQLException se)
		{
			escreveNoArquivo("Erro durante a execucao da consulta ao banco de dados: " + se.toString());
		}
		writer.flush();
		writer.close();
		return arquivoRetorno;
	}

	public File getResultadoComoArquivo(ResultSet rs, String nomeArquivo, String separador) throws IOException
	{
		File arquivoRetorno = new File(nomeArquivo);
		// Utiliza como writer do output um arquivo
		writer = new FileWriter(arquivoRetorno);
		processaResultadoComando(rs,separador);
		writer.flush();
		writer.close();
		return arquivoRetorno;
	}

	public String getResultadoComando(String sql, String separador) throws IOException
	{
		return getResultadoComoBuffer(sql,null).toString();
	}

	public void escreveNoArquivo(String texto) throws IOException
	{
		writer.write(texto.toCharArray(),0,texto.toCharArray().length);
	}

	public void processaResultadoComando(ResultSet rs, String separador) throws IOException
	{
		try
		{
			ResultSetMetaData rsMeta = rs.getMetaData();
			for (int i=1; i <= rsMeta.getColumnCount(); i++)
				escreveNoArquivo(i != 1 ? separador + rsMeta.getColumnName(i) : rsMeta.getColumnName(i));

			escreveNoArquivo("\n");
			while (rs.next())
			{
				for (int i=1; i <= rsMeta.getColumnCount(); i++)
					escreveNoArquivo(i != 1 ? separador + rs.getString(i) : rs.getString(i));

				escreveNoArquivo("\n");
			}
		}
		catch(SQLException e)
		{
			escreveNoArquivo("Erro durante consulta no banco de dados: " + e.toString());
		}
	}
}
