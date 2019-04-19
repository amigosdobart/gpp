package com.brt.gppEnviaMail.aplicacao;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;
import java.sql.*;

import com.brt.gppEnviaMail.conexoes.ConexaoBancoDados;

public class ProcessadorResultadoHTML implements ProcessadorArquivo
{
	private Writer writer;

	public StringBuffer getResultadoComoBuffer(String sql,String separador) throws IOException
	{
		// O caracter separador nao tem funcao nenhuma para a classe
		// que gera o resultado me HTML portanto esse e desconsiderado
		// Utiliza como writer do output um string para ser utilizado como buffer
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
			processaResultadoComando(rs);
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

	public StringBuffer getResultadoComoBuffer(ResultSet rs,String separador) throws IOException
	{
		// O caracter separador nao tem funcao nenhuma para a classe
		// que gera o resultado me HTML portanto esse e desconsiderado
		// Utiliza como writer do output um string para ser utilizado como buffer
		writer = new StringWriter();
		// Executa o processamento
		processaResultadoComando(rs);
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
			processaResultadoComando(rs);
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
		// Executa o processamento
		processaResultadoComando(rs);
		writer.flush();
		writer.close();
		return arquivoRetorno;
	}
	
	public String getResultadoComando(String sql) throws IOException
	{
		return getResultadoComoBuffer(sql,null).toString();
	}

	public void escreveNoArquivo(String texto) throws IOException
	{
		writer.write(texto.toCharArray(),0,texto.toCharArray().length);
	}

	public void processaResultadoComando(ResultSet rs) throws IOException
	{
		escreveNoArquivo("<html>");
		try
		{
			escreveNoArquivo("<table width=100% border=1>\n");
			escreveNoArquivo("<tr>\n");
			ResultSetMetaData rsMeta = rs.getMetaData();
			for (int i=1; i <= rsMeta.getColumnCount(); i++)
				escreveNoArquivo("\t<td><b>"+ rsMeta.getColumnName(i) +"</b></td>\n");

			escreveNoArquivo("</tr>\n");
			while (rs.next())
			{
				escreveNoArquivo("<tr>\n");
				for (int i=1; i <= rsMeta.getColumnCount(); i++)
					escreveNoArquivo("\t<td>" + rs.getString(i) + "</td>\n");

				escreveNoArquivo("</tr>\n");
			}
			escreveNoArquivo("</table>\n");
			escreveNoArquivo("</html>");
		}
		catch(SQLException e)
		{
			escreveNoArquivo("<html>Erro durante consulta no banco de dados: " + e.toString() + "</html>");
		}
	}
}
