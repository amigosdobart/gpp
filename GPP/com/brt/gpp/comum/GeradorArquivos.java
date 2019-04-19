package com.brt.gpp.comum;

// Arquivos Java
import java.io.*;

// Arquivos GPP
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.controle.analiseInconsistencia.MovimentacaoCredito;
import com.brt.gpp.comum.GPPData;

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
public class GeradorArquivos extends Aplicacoes
{
	private FileWriter arquivo;
	private PrintWriter canalEscrita;
	
	/**
	 * Metodo...: Construtor
	 * @param String	caminhoNomeArquivo		Path+Nome do Arquivo que dever ser criado
	 * @param long		aIdProcesso				ID do Processo para efeitos de log
	 */
	public GeradorArquivos(String caminhoNomeArquivo, long aIdProcesso)
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_GERADOR_ARQUIVOS);
		
		try
		{
			// Cria arquivo com nome informado no caminho informado
			this.arquivo = new FileWriter(caminhoNomeArquivo, true);	
			
			canalEscrita = new PrintWriter(arquivo);
		}
		catch(IOException ioE)
		{
			super.log(Definicoes.ERRO,"GeradorArquivos","Impossivel abrir arquivo "+caminhoNomeArquivo+". Excecao: "+ioE);
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
	
	public void escreveLinhaTrocaSenha(String _msisdn, String _novaSenha)
	{
		canalEscrita.println(GPPData.dataCompletaForamtada()+ " " + _msisdn + " " + _novaSenha);
	}
	
	/**
	 * Metodo...: fecharArquivo
	 * Descricao: Termina trabalho com arquivo
	 */
	public void fechaArquivo()
	{
		canalEscrita.close();
	}
}
