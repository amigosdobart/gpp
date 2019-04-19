package com.brt.gpp.comum;

// Arquivos Java
import java.io.*;

// Arquivos GPP
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.controle.analiseInconsistencia.MovimentacaoCredito;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
* Este arquivo refere-se a classe GeradorArquivos, responsavel por enviar linhas
* de texto para um arquivo
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				29/12/2004
*
*/
public class ManipuladorArquivos extends Aplicacoes
{
	private FileWriter arqWrite = null;
	private FileReader arqRead = null;
	private PrintWriter canalEscrita = null;
	private BufferedReader canalLeitura = null;
	
	/**
	 * Metodo...: Construtor
	 * @param String	caminhoNomeArquivo		Path+Nome do Arquivo que dever ser criado
	 * @param boolean	isOutput				true, para arquivo de escrita; false, para arquivo de leitura
	 * @param long		aIdProcesso				ID do Processo para efeitos de log
	 */
	public ManipuladorArquivos(String caminhoNomeArquivo, boolean isOutput, long aIdProcesso) throws GPPInternalErrorException
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_GERADOR_ARQUIVOS);
		
		try
		{
			// Se for um arquivo de saída, usa FileWriter e PrintWriter
			if(isOutput)
			{
				// Cria arquivo com nome informado no caminho informado
				this.arqWrite = new FileWriter(caminhoNomeArquivo, true);	
				
				canalEscrita = new PrintWriter(this.arqWrite);
			}
			else
			{
				// Se for um arquivo de leitura, usa FileReader e BufferedReader
				this.arqRead = new FileReader(caminhoNomeArquivo);
				canalLeitura = new BufferedReader(this.arqRead);
			}
		}
		catch(IOException ioE)
		{
			super.log(Definicoes.ERRO,"GeradorArquivos","Impossivel abrir arquivo "+caminhoNomeArquivo+". Excecao: "+ioE);
			throw new GPPInternalErrorException("Erro GPP: "+ioE);
		}
	}
	
	/**
	 * Metodo...: escreveLinha
	 * Descricao: Escreve uma linha indicando a discrepância de saldos
	 * 			  Método usado pelo Analisador de Inconsistencias
	 * 				"Entre os momentos T1 e T2 há uma inconsistência de R$ xxxx,xx"
	 * @param 	MovimentacaoCredito		_estadoInicial		Dados do saldo num momento T1
	 * @param 	MovimentacaoCredito		_estadoFinal		Dados do saldo num momento T2 (T2>T1)
	 */
	public void escreveLinha(String msisdn,MovimentacaoCredito _estadoInicial, MovimentacaoCredito _estadoFinal, double _gapSaldo)
	{
		canalEscrita.println("Assinante "+msisdn+" entre os momentos "+_estadoInicial.getDataHora()+" e "+_estadoFinal.getDataHora()+" possui " +
				"uma inconsistencia de R$ "+_gapSaldo);
	}
	
	/**
	 * Metodo...: escreveLinha
	 * Descricao: Escreve uma linha genérica no arquivo
	 * @param String 	aLinha		Linha a ser escrita
	 */
	public void escreveLinha(String aLinha)
	{
		canalEscrita.println(aLinha);
	}
	
	public void escreveLinhaTrocaSenha(String _msisdn, String _novaSenha)
	{
		canalEscrita.println(GPPData.dataCompletaForamtada()+ " " + _msisdn + " " + _novaSenha);
	}
	
	/**
	 * Metodo...: leLinha
	 * Descricao: Lê uma linha do arquivo de entrada
	 * @return	String	Linha lida do arquivo de entrada
	 * @throws IOException
	 */
	public String leLinha() throws IOException
	{
		return canalLeitura.readLine();
	}
	
	/**
	 * Metodo...: fecharArquivo
	 * Descricao: Termina trabalho com arquivo
	 */
	public void fechaArquivo()
	{
		try
		{
			// Fecha canais de escrita/leitura e abstrações do arquivo
			if(canalEscrita != null)
			{
				canalEscrita.close();
			}
			
			if(arqWrite != null)
			{
				arqWrite.close();
			}
			
			if(canalLeitura != null)
			{
				canalLeitura.close();
			}
			
			if(arqRead != null)
			{
				arqRead.close();
			}
		}
		catch (IOException ioE)
		{
			super.log(Definicoes.ERRO,"fechaArquivo","Erro ao fechar arquivo");
		}
	}
	
	/**
	 * Metodo...: moveArquivo
	 * Descricao: Move um arquivo de um diretório para outro
	 * @param File		arq							Arquivo a ser movido
	 * @param String	pathSeparadorNomeDestino	path+separador+nome do arq destino (//dir//arquivo.com)
	 * @return	short 	0 se ok; 1 se houver erro de sistema
	 * @throws GPPInternalErrorException
	 */
	public static short moveArquivo(File arq, String pathSeparadorNomeDestino)
	{
		short retorno = 0;
		File fDest = new File(pathSeparadorNomeDestino);

		//super.log(Definicoes.DEBUG, "moveArquivo", "Movendo Arquivo : " + arq + " para o diretório historico: " + fDest);
		if (!arq.renameTo(fDest))
		{
			//super.log(Definicoes.ERRO, "moveArquivo", "Não foi possível mover o arquivo "+ arq);
			retorno = 1;
		}
		return retorno;
	}
	
	public void flush()
	{
		canalEscrita.flush();
	}
}
