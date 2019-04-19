package com.brt.gppEnviaMail.aplicacao;

import com.brt.gppEnviaMail.GPPEnviaMailDefinicoes;
import com.brt.gppEnviaMail.conexoes.Configuracao;

public class ComandoSQL
{
	private String 	sql;
	private String 	nomeArquivo;
	private String 	processadorArquivo;
	private String 	separador;
	private boolean	enviarComoAnexo;
	private boolean	compactaArquivo;

	public ComandoSQL(String sql)
	{
		setSql(sql);
		enviarComoAnexo  	= true;
		compactaArquivo  	= true;
		separador 		 	= GPPEnviaMailDefinicoes.DELIMITADOR_DEFAULT;
		processadorArquivo 	= GPPEnviaMailDefinicoes.PROCESSADOR_DEFAULT_ARQUIVOS;
		nomeArquivo 	 	= "Resultado-"+sql.hashCode()+GPPEnviaMailDefinicoes.EXTENSAO_DEFAULT_ARQUIVO;
	}

	public String getNomeArquivoCompleto()
	{
		Configuracao conf = Configuracao.getInstance();
		String tempDir = conf.getPropriedade("com.brt.gppEnviaMail.diretorioTemporario");
		return tempDir+System.getProperty("file.separator")+getNomeArquivo();
	}

	public String getNomeArquivo()
	{
		return RequisicaoSQLMail.converteVariavelData(nomeArquivo);
	}

	public String getSeparador()
	{
		return separador;
	}

	public String getSql()
	{
		return sql;
	}

	public String getProcessadorArquivo()
	{
		return processadorArquivo;
	}

	public boolean enviarComoAnexo()
	{
		return enviarComoAnexo;
	}

	public boolean compactaArquivo()
	{
		return compactaArquivo;
	}

	public void setNomeArquivo(String nomeArquivo)
	{
		if (nomeArquivo != null || !nomeArquivo.equals(""))
			this.nomeArquivo = nomeArquivo;
	}

	public void setSeparador(String separador)
	{
		if (separador != null || !separador.equals(""))
			this.separador = separador;
	}

	public void setSql(String sql)
	{
		if (sql == null || sql.equals(""))
			throw new IllegalArgumentException("Comando SQL nao pode ser vazio");

		this.sql = sql;
	}

	public void setProcessadorArquivo(String processadorArquivo)
	{
		if (processadorArquivo != null || !processadorArquivo.equals(""))
			this.processadorArquivo = processadorArquivo;
	}

	public void setEnviarComoAnexo(boolean enviarComoAnexo)
	{
		this.enviarComoAnexo = enviarComoAnexo;
	}

	public void setCompactaArquivo(boolean compactaArquivo)
	{
		this.compactaArquivo = compactaArquivo;
	}
}
