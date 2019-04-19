package com.brt.gppEnviaMail.aplicacao;

import java.io.*;

public class ProcessaComandos
{
	public String getConteudoXML(String arquivo) throws IOException
	{
		String dadosArquivo 		= "";
		FileReader fReader 			= new FileReader(arquivo);
		BufferedReader buffReader 	= new BufferedReader(fReader);
		String dados = "";
		
		while ( (dados=buffReader.readLine())!= null)
			dadosArquivo += dados;
		
		//Retirando os espacos em branco no final
		dadosArquivo = dadosArquivo.trim();
		return dadosArquivo;
	}

	public void processa(String arquivo) throws Exception
	{
		// Realiza um parse no XML de requisicao de envio de comandos por e-mail
		// antes de passar como parametro para o responsavel por enviar os resultados
		RequisicaoSQLMail requisicao = new RequisicaoSQLMailParser(getConteudoXML(arquivo)).getRequisicao();

		// Cria a referencia para processamento e envio da requisicao
		EnviaRequisicaoSQLMail envRequisicao = new EnviaRequisicaoSQLMail();
		envRequisicao.processaRequisicao(requisicao);
	}

	public static void main(String args[])
	{
		ProcessaComandos pComandos = new ProcessaComandos();
		try
		{
			if (args == null || args.length == 0)
			{
				System.out.println("Execucao deve ser: java ProcessaComandos nomeArquivoXML");
				System.exit(1);
			}
			System.out.println("Processando arquivo XML:"+args[0]);
			pComandos.processa(args[0]);
			System.out.println("Termino do envio de execucao de comandos SQL por e-mail.");
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
