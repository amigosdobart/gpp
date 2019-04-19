package com.brt.gppEnviaMail.aplicacao;

import com.brt.gppEnviaMail.conexoes.Configuracao;
import com.brt.gppEnviaMail.conexoes.EnviaMail;
import com.brt.gppEnviaMail.GPPEnviaMailDefinicoes;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collection;
import java.lang.reflect.*;

public class EnviaRequisicaoSQLMail
{
	private String getCorpoMensagem(RequisicaoSQLMail reqSQLMail)
							throws IOException,ClassNotFoundException,IllegalAccessException,
								   InstantiationException,NoSuchMethodException,InvocationTargetException
	{
		System.out.println("Buscando informacoes que serao parte do corpo da mensagem...");
		// O resultado do corpo da mensagem sera a mensagem definida no XML de requisicao
		// em conjunto com o resultado de todos os comandos SQL dessa requisicao que foram
		// definidos para nao serem enviados como anexo
		StringBuffer corpoMensagem = new StringBuffer(reqSQLMail.getMensagem()+"\n");
		// Faz a iteracao em todos os comandos da requisicao
		for (Iterator i = reqSQLMail.getListaComandosSQL().iterator(); i.hasNext();)
		{
			ComandoSQL comando = (ComandoSQL)i.next();
			if (!comando.enviarComoAnexo())
			{
				// Identifica por Reflection qual a classe que ira produzir os resultados
				// desse comando SQL, sendo que nesse ponto todas as classes que desejarem
				// realizar essa tarefa terao que implementar uma interface em comum
				// para garantir que o resultado sera padronizado
				Class    			procClass 	= Class.forName(comando.getProcessadorArquivo());
				ProcessadorArquivo  processador	= (ProcessadorArquivo)procClass.newInstance();
				
				// Adiciona o resultado ao corpo da mensagem
				corpoMensagem.append(processador.getResultadoComoBuffer(comando.getSql(),comando.getSeparador()));
				corpoMensagem.append("\n");
			}
		}
		return corpoMensagem.toString();
	}

	private String getNomeArquivoCompactado(String nomeArquivo)
	{
		// Define o nome do arquivo compactado realizando a substituicao
		// da extensao do nome do arquivo passado como parametro
		// pela extensao definido para o arquivo compactado
		Configuracao conf = Configuracao.getInstance();
		String tempDir = conf.getPropriedade("com.brt.gppEnviaMail.diretorioTemporario");

		String nomeArqCompactado = new String(nomeArquivo);
		String zipFileName = tempDir + System.getProperty("file.separator") +
 			                 nomeArqCompactado.substring(0,nomeArqCompactado.indexOf("."))+
							    GPPEnviaMailDefinicoes.EXTENSAO_ARQUIVO_COMPACTADO;
		return zipFileName;
	}

	private File[] getListaAnexos(RequisicaoSQLMail reqSQLMail) 
				throws IOException,ClassNotFoundException,IllegalAccessException,
				       InstantiationException,NoSuchMethodException,InvocationTargetException
	{
		System.out.println("Buscando informacoes que serao arquivadas em anexo ao e-Mail...");
		// Define uma lista que sera o resultado de todos os comandos SQL
		// definicos para essa requisicao
		Collection listaArquivos = new LinkedList();
		// Faz a iteracao entre todos os comandos SQL da requisicao
		for (Iterator i = reqSQLMail.getListaComandosSQL().iterator(); i.hasNext();)
		{
			ComandoSQL comando = (ComandoSQL)i.next();
			// Caso o comando tenha sido definido para ser enviado como anexo, entao
			// instancia um processador de arquivos tambem definido na requisicao
			// para prover o resultado da execucao desse comando sql e retornar como
			// um arquivo a ser adicionado na lista final
			if (comando.enviarComoAnexo())
			{
				System.out.println("Processando arquivo - "+comando.getNomeArquivo());
				Class    			procClass 	= Class.forName(comando.getProcessadorArquivo());
				ProcessadorArquivo  processador	= (ProcessadorArquivo)procClass.newInstance();
				
				File arqRetorno[] = {processador.getResultadoComoArquivo(comando.getSql()
						            ,comando.getNomeArquivoCompleto()
									,comando.getSeparador())
									};
				// Caso tenha tambem sido definido que este resultado alem de ser um arquivo anexo
				// tambem deva ser compactado entao utiliza uma classe para a compactacao do arquivo
				// sendo que o nome deste sera o mesmo nome definido na requisicao trocando somente
				// a extensao para a extensao de arquivos compactados
				if (comando.compactaArquivo())
					listaArquivos.add(CompactadorArquivos.getArquivoCompactado
							                    (getNomeArquivoCompactado(comando.getNomeArquivo()),arqRetorno));
				else
					listaArquivos.add(arqRetorno[0]);
			}
		}
		// Caso no cabecalho do XML da requisicao tenha sido definido
		// que todos os arquivos anexos devam ser compactados em um
		// unico arquivo entao cria este arquivo sendo que somente
		// esse sera anexo
		File anexos[] = (File[])listaArquivos.toArray(new File[0]); 
		if (reqSQLMail.compactarArquivos())
		{
			String nomArquivo = getNomeArquivoCompactado(reqSQLMail.getNomeArquivoCompactado());
			File compactado[] = {CompactadorArquivos.getArquivoCompactado(nomArquivo,anexos)};
			anexos = compactado;
			System.out.println("Arquivos anexos compactados no arquivo - "+nomArquivo);
		}
		return anexos;
	}
	
	public void processaRequisicao(RequisicaoSQLMail reqSQLMail) throws Exception
	{
		Configuracao conf = Configuracao.getInstance();
		String emailOrig 	= conf.getPropriedade("com.brt.gppEnviaMail.emailOrigem");
		String servidorSMTP = conf.getPropriedade("com.brt.gppEnviaMail.nomeServidorSMTP");

		// Cria o objeto que sera responsavel pelo envio do e-mail do resultado
		// definindo atributos como mensagem, assunto e e-mail originador
		// para o tipo da mensagem verifica-se se ha algum resultado que sera incorporado
		// no corpo da mensagem, porque ai o tipo sera TEXTO/HTML senao sera TEXTO/PLAIN
		EnviaMail enviaMail = new EnviaMail(emailOrig,servidorSMTP);
		enviaMail.setTipoMensagem(reqSQLMail.haResultadoNaoAnexo() ? GPPEnviaMailDefinicoes.TIPO_TEXTO_HTML : GPPEnviaMailDefinicoes.TIPO_TEXTO_PLAIN);
		enviaMail.setAssunto 	 (reqSQLMail.getAssuntoMail());

		// Realiza uma busca na solicitacao para identificar quais
		// sao os enderecos de destino e adiciona no objeto de envio
		// de e-mail
		String endDestino[] = reqSQLMail.getEnderecosDestino();
		for (int i=0; i < endDestino.length; i++)
		{
			enviaMail.addEnderecoDestino(endDestino[i].trim());
			System.out.println("Realizando composicao do e-Mail para o destino:"+endDestino[i].trim());
		}

		// Define o conteudo da mensagem
		enviaMail.setMensagem(getCorpoMensagem(reqSQLMail));

		// Adiciona os arquivos que por ventura serao anexados
		File listaAnexos[] = getListaAnexos(reqSQLMail);
		for (int i=0; i < listaAnexos.length; i++)
			enviaMail.addArquivoAnexo(listaAnexos[i]);

		System.out.println("Enviando o e-Mail...");
		enviaMail.enviaMail();
	}
}
