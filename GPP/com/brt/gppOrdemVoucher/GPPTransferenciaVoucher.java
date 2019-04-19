package com.brt.gppOrdemVoucher;

import com.brt.gpp.componentes.processosBatch.orb.*;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.*;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdem;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemItem;

import java.net.URL;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.Collection;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Iterator;
import java.text.DecimalFormat;

import org.apache.oro.text.regex.*;
import org.apache.oro.text.awk.*;

/**
  * Esta classe contem o processamento de arquivos de criacao de voucher
  * O processamento e feito atraves da leitura do diretorio onde estes
  * arquivos residem sendo entao criptografados pelo utilitario PGP, compactados
  * e transferidos para o GPP para processamento dos pedidos de criacao de voucher
  * 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Joao Carlos
  * Data:               30/06/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class GPPTransferenciaVoucher
{
	private 		Properties 						arquivoProps;
	private 		processosBatch 					procBatchPOA;

	private static final String REQUISITANTE_NAO_DIPONIVEL = "Nao Disponivel";
	private static final int FLAG_PRIMEIRA_LINHA = 1;
	private static final int FLAG_ULTIMA_LINHA   = 9;
	
	/**
	 * Metodo....:TransfereArquivosOrdemVoucher
	 * Descricao.:Construtor da classe - Faz a inicializacao do arquivo de propriedades
	 *
	 */
	public GPPTransferenciaVoucher(String nomeArquivoProps)
	{
		try
		{
			/* Faz o load do arquivo de propriedades */
			arquivoProps = new Properties();
			InputStream recurso = null;
			if (nomeArquivoProps != null)
				recurso = new FileInputStream(nomeArquivoProps);
			else
			{
				URL url = ClassLoader.getSystemResource("ordemVoucher.properties");
				recurso = url.openStream();
			}
			
			arquivoProps.load(recurso);
			
			/* Por ter informacoes de conexao CORBA, entao
			 * os dados tambem sao inseridos no System Properties
			 */
			Properties propsSystem = System.getProperties();
			propsSystem.putAll((Map)arquivoProps);
			System.setProperties(propsSystem);

			/* Inicializa objeto CORBA para acesso ao GPP */
			String args[] = {getPropriedade("vbroker.agent.port"),getPropriedade("vbroker.agent.addr")};
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, propsSystem);
			byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();	
			procBatchPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		}
		catch(Exception e)
		{
			System.out.println("Erro ao iniciar o programa de Transferencia de arquivos de ordem de voucher. "+e);
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Metodo....:getNumeroOrdem
	 * Descricao.:Busca o numero da ordem regitrada na primeira linha do arquivo
	 * @param arquivo	- Arquivo a ser lido
	 * @return	long	- Numero da ordem de criacao de voucher
	 */
	private long getNumeroOrdem(File arquivo) throws IOException
	{
		FileReader 		fReader 	= new FileReader(arquivo);
		BufferedReader 	buffReader 	= new BufferedReader(fReader);
		long numeroOrdem=0;
		String linha=null;
		/* Faz a leitura das linhas ate encontrar a linha que contem o numero da ordem
		 * Geralmente esta e a primeira linha do arquivo
		 */
		while ( (linha=buffReader.readLine()) != null )
		{
			String patternNumOrdem = getPropriedade("ordemVoucher.stringNumeroOrdem"); 
			if (linha.indexOf(patternNumOrdem) > -1)
			{
				numeroOrdem = Long.parseLong( linha.substring(patternNumOrdem.length()).trim() );
				break;
			}
		}
		buffReader.close();
		fReader.close();
		return numeroOrdem;
	}
	
	/**
	 * Metodo....:getNomeArquivosCaixa
	 * Descricao.:Faz a leitura do arquivo para identificar os nomes dos
	 *            arquivos de caixa contendo a numeracao de vouchers
	 * 
	 * Obs: Estes arquivos serao criptografados e compactados antes de serem
	 *      enviados ao GPP
	 * @param ordem		- Arquivo da ordem de criacao de vouchers
	 * @return String[] - Array contendo os nomes dos arquivos 
	 * @throws IOException
	 */
	private String[] getNomeArquivosCaixa(File ordem) throws IOException
	{
		Collection arquivos = new LinkedList();
		FileReader 		fReader 	= new FileReader(ordem);
		BufferedReader 	buffReader 	= new BufferedReader(fReader);
		String patternNomArqCaixa = getPropriedade("ordemVoucher.patternArquivosCaixa");
		String linha=null;
		while ( (linha=buffReader.readLine()) != null )
			/* Verifica se o pattern dos nomes de arquivos foi encontrado */
			if (linha.indexOf(patternNomArqCaixa) > -1)
			{
				/* Faz o split da linha que contem diferentes campos separados por ","
				 * Utiliza a classe abaixo devido a maquina da Tecnomen possuir
				 * a versao 1.3 do Java
				 */
				//String campos[] = linha.split(",");
				StringTokenizer strToken = new StringTokenizer(linha,",");
				String campos[] = new String[strToken.countTokens()];
				for (int i=0; i < campos.length; i++)
					campos[i] = strToken.nextToken();

				/* O primeiro campo e o nome do arquivo */
				arquivos.add(campos[0]);
			}

		buffReader.close();
		fReader.close();
		return (String[])arquivos.toArray(new String[0]);
	}

	/**
	 * Metodo....:moveArquivos
	 * Descricao.:Move os arquivos processados da ordem para um diretorio historico
	 * @param arquivoOrdem	- Arquivo da ordem
	 * @throws IOException
	 */
	private void moveArquivos(File arquivoOrdem) throws IOException
	{
		/* Busca o nome do diretorio destino definido nas configuracoes */
		String dirHistorico = getPropriedade("ordemVoucher.diretorioHistorico");
		
		/* Move todos os arquivos de caixa da ordem */
		String arquivosCaixa[] = getNomeArquivosCaixa(arquivoOrdem);
		for (int i=0; i < arquivosCaixa.length; i++)
		{
			/* Cria a referencia para o arquivo origem */
			String dirOrigem    = getPropriedade("ordemVoucher.dirArquivos");
			String nomArqOrigem	= dirOrigem+System.getProperty("file.separator")+arquivosCaixa[i];
			File arquivoOrigem  = new File(nomArqOrigem);
			/* Cria a referencia para o arquivo destino */
			File arquivoDestino = new File(dirHistorico+System.getProperty("file.separator")+arquivoOrigem.getName());
			/* Move o arquivo origem para destino */
			if (!renomeiaArquivo(arquivoOrigem,arquivoDestino))
				System.out.println("Nao foi possivel mover o arquivo de:"+arquivoOrigem.getAbsolutePath()+" para:"+arquivoDestino.getAbsolutePath());
			else
				System.out.println("Arquivo "+arquivoOrigem.getName()+" foi movido para: "+arquivoDestino.getAbsolutePath());
		}

		/* Move o arquivo da ordem */
		File destino = new File(dirHistorico+System.getProperty("file.separator")+arquivoOrdem.getName());
		if (!renomeiaArquivo(arquivoOrdem,destino))
			System.out.println("Nao foi possivel mover o arquivo de:"+arquivoOrdem.getAbsolutePath()+" para:"+destino.getAbsolutePath());
		else
			System.out.println("Arquivo "+arquivoOrdem.getName()+" foi movido para: "+destino.getAbsolutePath()); 
	}
	
	/**
	 * Metodo....:removeArquivos
	 * Descricao.:Metodo que remove os arquivos criptografados e compactados da ordem
	 *            de criacao de voucher desde que o arquivo compactado contendo os arquivos
	 *            criptografados tenha sido enviado para o GPP com sucesso.
	 * @param arquivoOrdem	- Arquivo de Ordem
	 * @param arquivosCaixa - Array contendo os arquivos que foram criptografados
	 * @throws IOException
	 */
	private void removeArquivos(File arquivoOrdem, String arquivosCaixa[]) throws IOException
	{
		for (int i=0; i < arquivosCaixa.length; i++)
		{
			// Remove o arquivo concatenado que foi criptografado
			String extArqCriptografado 	= getPropriedade("ordemVoucher.extensaoArquivoCriptografado");
//			String dirOrigem    		= getPropriedade("ordemVoucher.dirArquivos");
			String nomArqCriptografado	= arquivosCaixa[i]+extArqCriptografado;
			File   arquivoCriptografado	= new File(nomArqCriptografado);
			System.out.println("Removendo arquivo "+nomArqCriptografado);
			if (!arquivoCriptografado.delete())
				System.out.println("Nao foi possivel remover o arquivo "+nomArqCriptografado);

			// Remove o arquivo concatenado original
			String nomArqOrigem			= arquivosCaixa[i];
			File   arquivoOrigem		= new File(nomArqOrigem);
			System.out.println("Removendo arquivo "+nomArqOrigem);
			if (!arquivoOrigem.delete())
				System.out.println("Nao foi possivel remover o arquivo "+nomArqOrigem);
		}
		
		/* Remove tambem o arquivo compactado que foi enviado para o GPP */
		File arqCompactado = new File(getNomeArquivoCompactado(arquivoOrdem));
		System.out.println("Removendo arquivo "+arqCompactado.getName());
		if (!arqCompactado.delete())
			System.out.println("Nao foi possivel remover o arquivo "+arqCompactado.getName());
	}

	/**
	 * Metodo....:enviaArquivosGPP
	 * Descricao.:Envia um arquivo para o GPP
	 * @param arquivo	- Arquivo a ser enviado
	 * @throws GPPInternalErrorException
	 */
	private void enviaArquivoParaGPP(File arquivo) throws GPPInternalErrorException
	{
		double timestampInicial = (double)Calendar.getInstance().getTime().getTime();
		try
		{
			procBatchPOA.gravaDadosArquivoOrdem(arquivo.getName(),getStreamArquivo(arquivo));
			double timestampFinal = (double)Calendar.getInstance().getTime().getTime();
			double tempoProcessamento = (timestampFinal - timestampInicial)/1000;
			DecimalFormat formatoDecimal = new DecimalFormat("###0.0000");  
			System.out.println("Arquivo "+arquivo.getName()+" foi enviado para o GPP. Tempo de processamento:"+formatoDecimal.format(tempoProcessamento)+" segundos.");
		}
		catch(IOException e)
		{
			System.out.println("Nao foi possivel ler o arquivo de origem "+arquivo+" Erro:"+e);
		}
	}

	/**
	 * Metodo....:getStreamArquivo
	 * Descricao.:Retorna um array de bytes sendo todo o arquivo
	 * @param arquivo	- Arquivo a ser lido
	 * @return byte[]	- Array de bytes lidos do arquivo
	 * @throws IOException
	 */
	private byte[] getStreamArquivo(File arquivo) throws IOException
	{
		/* Define o tamanho do buffer a ser lido do arquivo (max 32kb),
		 * faz a criacao de um buffer em memoria para ir armazenando os dados
		 * lidos e entao apos a leitura faz o envio dos dados para o GPP
		 */
		int sizeBuffer = Integer.parseInt(getPropriedade("ordemVoucher.tamanhoBufferArquivos"));
		FileInputStream fileInput = new FileInputStream(arquivo);
		ByteArrayOutputStream bufferArquivo = new ByteArrayOutputStream();

		byte[] data = new byte[sizeBuffer];
		int count=0;
		while ( (count = fileInput.read(data)) != -1 )
			bufferArquivo.write(data,0,count);
		
		return bufferArquivo.toByteArray();
	}

	/**
	 * Metodo....:getIdRequisitante
	 * Descricao.:Este metodo busca no sistema GPP qual o userID de requisitante
	 *            para autenticacao de chave PGP(Pretty Good Privacy)
	 * @param numOrdem	- Numero da ordem a ser consultada
	 * @return String	- Id do requisitante para uso do PGP
	 */
	private String getIdRequisitante(long numOrdem)
	{
		return procBatchPOA.getUserIDRequisitante(numOrdem);
	}

	/**
	 * Metodo....:getNomeArquivoCompactado
	 * Descricao.:Retorna o nome do arquivo que sera o arquivo
	 *            compactado de todos os arquivos de caixa de ordens
	 *            de voucher
	 * @param ordem		- Ordem de criacao de vouchers
	 * @return String 	- Nome do arquivo que sera o conjunto de arquivos de caixas de vouchers
	 */
	private String getNomeArquivoCompactado(File ordem)
	{
		String			nomeArquivo	= null;
		Pattern 		pattern 	= null;
		PatternMatcher 	matcher 	= new AwkMatcher();
		PatternCompiler compiler 	= new AwkCompiler();
		Substitution 	sub			= new StringSubstitution(".zip");
		try
		{
			pattern = compiler.compile("[.]dat");
			nomeArquivo = Util.substitute(matcher,pattern,sub,ordem.getAbsolutePath());
		}
		catch(Exception e)
		{
			/* Caso algum erro ocorra na troca da extensao do arquivo, entao
			 * o arquivo fica com o nome original acrescido da extensao .zip
			 */
			System.out.println("Erro ao criar o nome do arquivo compactado. "+e);
			nomeArquivo = ordem.getAbsolutePath()+".zip";
		}
		//String nomeArquivo = ordem.getAbsolutePath().replaceAll("[.]dat",".zip");
		return nomeArquivo;
	}

	/**
	 * Metodo....:compactaOrdem
	 * Descricao.:Realiza a compactacao dos arquivos criptografados das caixas
	 *            de vouchers de uma determinada Ordem criando um unico 
	 *            arquivo a ser transferido para o GPP
	 * @param ordem
	 * @param arquivosCaixa
	 * @return
	 */
	private String compactaOrdem(File ordem, String[] arquivosCaixa) throws IOException
	{
		/* Define referencias aos streams de arquivos a serem utilizados */
		/* Buffer a ser utilizado para leitura dos arquivos de caixa */
		BufferedInputStream buffOrigem 	= null;
		/* Esta referencia e do arquivo final (zip) do processo */
		FileOutputStream 	arqDestino 	= new FileOutputStream(getNomeArquivoCompactado(ordem));
		ZipOutputStream 	arqSaida 	= new ZipOutputStream (new BufferedOutputStream(arqDestino));

		/* Define o buffer de dados com o tamanho sendo definido no
		 * arquivo de configuracao
		 */
		int sizeBuffer = Integer.parseInt(getPropriedade("ordemVoucher.tamanhoBufferArquivos"));
		byte data[] = new byte[sizeBuffer];

		String extArqCriptografado = getPropriedade("ordemVoucher.extensaoArquivoCriptografado");
		
		/* Faz a varredura dos arquivos de caixa que serao utilizados
		 * para a compactacao. Lembrando que o nome e o mesmo do arquivo da
		 * ordem com a extensao pgp devido ao utilitario de criptografia
		 */
		System.out.println("Iniciando compactacao para o arquivo:"+getNomeArquivoCompactado(ordem));
		for (int i=0; i<arquivosCaixa.length; i++) 
		{
			String nomArqOrigem			= arquivosCaixa[i] + extArqCriptografado;
			System.out.println("Incluindo arquivo de ordem "+nomArqOrigem+" no arquivo compactado...");

			File arquivoOrigem			= new File(nomArqOrigem);
			FileInputStream fileInput 	= new FileInputStream(arquivoOrigem);
		   	buffOrigem 					= new BufferedInputStream(fileInput, sizeBuffer);
		   	ZipEntry entry 				= new ZipEntry(arquivoOrigem.getName());
		   	arqSaida.putNextEntry(entry);
		   	int count;
			while((count = buffOrigem.read(data, 0, sizeBuffer)) != -1) 
			  arqSaida.write(data, 0, count);
			  
		   buffOrigem.close();
		}
		arqSaida.close();
		System.out.println("Termino da compactacao do arquivo.");	
		return getNomeArquivoCompactado(ordem); 
	}
	
	/**
	 * Metodo....:getNomeArquivoCaixa
	 * Descricao.:Retorna o nome do arquivo da numeracao de caixa desejado
	 *            pesquisando em um array contendo todos os nomes de arquivos
	 *            de caixa para a ordem
	 * @param arquivosCaixa	- Array contendo todos os nomes de caixas da ordem
	 * @param numeroCaixa	- Numero da caixa desejado a ser pesquisado no array
	 * @return String		- Nome completo do arquivo da caixa
	 */
//	private String getNomeArquivoCaixa(String arquivosCaixa[],long numeroCaixa)
//	{
//		// Para cada nome de arquivo das caixas da ordem verifica se o 
//		// nome contempla o numero da caixa apos o caracter "_"
//		for (int i=0; i < arquivosCaixa.length; i++)
//			if (arquivosCaixa[i].indexOf("_"+numeroCaixa) > -1)
//				return arquivosCaixa[i];
//				
//		return null;
//	}

	/**
	 * Metodo....:getArquivosCaixaPorItem
	 * Descricao.:Retorna um array contendo todas os arquivos de caixas
	 *            da ordem que compoem exatamente um item
	 * @param ordemVoucher		- Ordem de criacao de voucher
	 * @param itemVoucher		- Item desejado
	 * @param arquivosCaixa		- Nome de todos os arquivos de caixa da ordem
	 * @return File[]			- Array de arquivos de caixa de um determinado item
	 */
	private String[] getArquivosCaixaPorItem(VoucherOrdem ordemVoucher,
	                                       VoucherOrdemItem itemVoucher,
	                                       String arquivosCaixa[])
	{
		// Utiliza uma colecao para ir criando os arquivos
		// deste item para posteriormente retornar o array
		Collection arquivosCaixaPorItem = new LinkedList();

		long numCaixaInicial = 0;
		long numCaixaFinal 	 = 0;
		for (Iterator i = ordemVoucher.getItensOrdem().iterator(); i.hasNext();)
		{
			VoucherOrdemItem item = (VoucherOrdemItem)i.next();
			if (item.getQtdeCartoes() != 0)
			{
				// Define a posicao final, ou seja, a posicao no array contendo os nomes
				// de arquivos das caixas baseando-se na posicao deste em relacao ao item
				// item 1 comeca na primeira posicao, porem o restante so e possivel saber
				// calculando
				numCaixaFinal = numCaixaInicial + (item.getNumeroCaixas()-1);
				if ( !item.equals(itemVoucher) )
					// A posicao inicial agora vai ser a ultima posicao do item pesquisado
					// ja que nao era este o item desejado
					numCaixaInicial = numCaixaFinal + 1;
				else
					break;
			}
		}
		// Incrementa de 1 o numero da caixa ate a ultima deste item
		// e adiciona o nome da caixa na colecao de caixas que compoem
		// o item
		for (long numeroCaixa=numCaixaInicial; numeroCaixa <= numCaixaFinal; numeroCaixa++)
			arquivosCaixaPorItem.add(arquivosCaixa[(int)numeroCaixa]);
			
		return (String[])arquivosCaixaPorItem.toArray(new String[0]);
	}

	/**
	 * Metodo....:getNomeArquivoItem
	 * Descricao.:Retorna o nome do arquivo do item que sera o agrupado
	 *            de arquivos das diferentes caixas da ordem de criacao
	 *            de vouchers
	 * @param nomeArquivo 	- Nome do arquivo da ordem
	 * @param numItem		- Numero do item
	 * @return String		- Nome do arquivo
	 */
	private String getNomeArquivoItem(File ordem, long numItem)
	{
		DecimalFormat 	df 			= new DecimalFormat("000");
		String			nomeArquivo	= null;
		Pattern 		pattern 	= null;
		PatternMatcher 	matcher 	= new AwkMatcher();
		PatternCompiler compiler 	= new AwkCompiler();
		Substitution 	sub			= new StringSubstitution("_"+df.format(numItem)+".dat");
		try
		{
			pattern = compiler.compile("[.]dat");
			nomeArquivo = Util.substitute(matcher,pattern,sub,ordem.getAbsolutePath());
		}
		catch(Exception e)
		{
			/* Caso algum erro ocorra na troca da extensao do arquivo, entao
			 * o arquivo fica com o nome original acrescido da extensao .zip
			 */
			System.out.println("Erro ao criar o nome do arquivo do item para concatenacao. "+e);
			nomeArquivo = ordem.getAbsolutePath()+"."+df.format(numItem);
		}
		return nomeArquivo;
		//return nomeArquivo.replaceAll("[.]dat","_"+df.format(numItem)+".dat");
	}

	/**
	 * Metodo....:concatenaArquivos
	 * Descricao.:Concatena os diversos arquivos de caixa da ordem 
	 *            de criacao de voucher agrupando-as baseando-se no 
	 *            item
	 * @param ordem			- Ordem a ser processada
	 * @return String[] 	- Array de nomes de arquivos apos a concatenacao 
	 * @throws Exception
	 */
	private String[] concatenaArquivos(File ordem) throws Exception
	{
		// Cria referencia para os arquivos que serao retornados
		// apos o processamento
		Collection arquivosAposConcatenacao = new LinkedList();
		
		// Busca o objeto VoucherOrdem contendo os dados da ordem
		// que foram lidos a partir do arquivo cabecalho da ordem
		ArquivoPedidoVoucherParser ordemParser = new ArquivoPedidoVoucherParser();
		VoucherOrdem ordemVoucher = ordemParser.parse(ordem);
		
		// Busca agora todas os nomes de arquivos de caixa
		// existentes gravados tambem no arquivo cabecalho
		String arquivosCaixa[] = getNomeArquivosCaixa(ordem);
		String dirOrigem       = getPropriedade("ordemVoucher.dirArquivos");
				
		// Faz a iteracao nos itens da ordem para entao criar um arquivo 
		// para cada item no formato PrintOrder#[NUMORDEM]_[NUMITEM].dat
		// e para cada item entao identifica quais arquivos serao concatenados
		int realNumItem 		= 1;
		long qtdeSomaCartoes 	= 0;
		boolean deveConcatenar 	= true;
		for (Iterator i=ordemVoucher.getItensOrdem().iterator(); i.hasNext();)
		{
			VoucherOrdemItem itemOrdem = (VoucherOrdemItem)i.next();
			// O numero do item no sistema GPP comeca a partir do numero zero (0)
			// portanto como na Tecnomen e criado com item 1 o valor e decrescido de um
			long qtdCartoes = procBatchPOA.getQtdeCartoes(ordemVoucher.getNumeroOrdem(),realNumItem-1);//(int)itemOrdem.getNumItem()-1);
			qtdeSomaCartoes += itemOrdem.getQtdeCartoes();
			if (qtdCartoes != 0 && deveConcatenar)
			{
				itemOrdem.setQtdeCartoes( qtdCartoes );
				File arquivoItem = new File(getNomeArquivoItem(ordem,realNumItem));//itemOrdem.getNumItem()));
				FileOutputStream itemStream = new FileOutputStream(arquivoItem);
				if (itemStream != null)
				{
					// Identifica quais sao os arquivos que devem ser concatenados
					// para este item. Para cada arquivo encontrado , este e lido
					// e entao concatenado ao arquivo do item que foi criado
					String caixasDoItem[] = getArquivosCaixaPorItem(ordemVoucher,itemOrdem,arquivosCaixa);
					// Atualiza informacoes de numeracao de lote do pedido no GPP
					atualizaNumeracaoLote(ordemVoucher.getNumeroOrdem(),realNumItem,caixasDoItem);//itemOrdem.getNumItem(),caixasDoItem);
					for (int j=0; j < caixasDoItem.length; j++)
						itemStream.write(getStreamArquivo(new File(dirOrigem+
						                                           System.getProperty("file.separator")+
						                                           caixasDoItem[j])));
	
					itemStream.close();
					arquivosAposConcatenacao.add(arquivoItem.getAbsolutePath());
				}
			}
			else
				itemOrdem.setQtdeCartoes(0);

			deveConcatenar  = (qtdCartoes == qtdeSomaCartoes);
			if (qtdCartoes == qtdeSomaCartoes)
			{
				qtdeSomaCartoes = 0;
				realNumItem++;
			}
		}
		return (String[])arquivosAposConcatenacao.toArray(new String[0]);
	}

	/**
	 * Metodo....:atualizaNumeracaoLote
	 * Descricao.:Atualiza numeracao de lotes iniciais e finais para o item
	 * @param numOrdem		- Numero da Ordem
	 * @param numItem		- Numero do Item
	 * @param caixasDoItem	- Arquivos contendo as informacoes das caixas deste item
	 * @throws Exception
	 */
	private void atualizaNumeracaoLote(long numOrdem, long numItem,String caixasDoItem[]) throws Exception
	{
		String valPrimLinha[] = getValoresLinhaItem(GPPTransferenciaVoucher.FLAG_PRIMEIRA_LINHA,caixasDoItem[0]);
		String valUltLinha[]  = getValoresLinhaItem(GPPTransferenciaVoucher.FLAG_ULTIMA_LINHA  ,caixasDoItem[caixasDoItem.length-1]);
		
		// O numero do item no sistema GPP comeca a partir do numero zero (0)
		// portanto como na Tecnomen e criado com item 1 o valor e decrescido de um
		procBatchPOA.atualizaNumLotePedido(numOrdem,(int)numItem-1,Long.parseLong(valPrimLinha[0]),Long.parseLong(valUltLinha[0]));
	}

	/**
	 * Metodo....:getValoresLinhaItem
	 * Descricao.:Retorna os valores de uma linha do arquivo de itens
	 * @param flag			- Indica se e a primeira ou ultima linha desejada
	 * @param nomArquivo	- Nome do arquivo a ser lido
	 * @return	String[]	- Campos encontrados na linha que foram separados por ","
	 * @throws Exception
	 */
	private String[] getValoresLinhaItem(int flag, String nomArquivo) throws Exception
	{
		String dirOrigem    = getPropriedade("ordemVoucher.dirArquivos");
		String nomArqItem	= dirOrigem+System.getProperty("file.separator")+nomArquivo;

		FileReader 		fReader = new FileReader(nomArqItem);
		BufferedReader 	bReader	= new BufferedReader(fReader);
		String linha 			= null;
		String linhaResultado 	= null;
		// Realiza a leitura do arquivo, caso a linha desejada seja a primeira
		// entao ja para o laco na primeira iteracao senao continua lendo o arquivo
		// ate o final sendo entao a ultima linha lida o resultado
		while ( (linha = bReader.readLine()) != null)
		{
			linhaResultado = linha;
			if (flag == GPPTransferenciaVoucher.FLAG_PRIMEIRA_LINHA)
				break;
		}
		// Quebra os valores da linha resultado e retorna tais valores
		StringTokenizer strToken = new StringTokenizer(linhaResultado,",");
		String campos[] = new String[strToken.countTokens()];
		for (int i=0; i < campos.length; i++)
			campos[i] = strToken.nextToken();
			
		return campos;
	}

	/**
	 * Metodo....:renomeiaArquivo
	 * Descricao.:Renomeia o nome de um arquivo, primeiro utilizando o metodo renameTo da classe File,porem
	 *            caso este metodo falhe por exe por os arquivos estarem em filesystems diferentes, entao 
	 *            faz um copy do mesmo para posteriormente apagar
	 * @param origem	- Arquivo de origem
	 * @param destino	- Arquivo de destino
	 * @return boolean	- Indica se conseguiu renomear o arquivo
	 */
	private boolean renomeiaArquivo(File origem, File destino)
	{
		// Tenta executar o renameTo da classe File. Caso o destino seja em filesystem diferente entao
		// este comando ira falhar, portanto realiza a copia do arquivo, executa o delete para entao
		// retornar a funcao
		boolean renomeou = origem.renameTo(destino);
		// Se o comando falhou entao tenta copiar e apagar o arquivo
		if (!renomeou)
		{
			renomeou = copiaArquivo(origem,destino);
			// Caso tenha conseguido copiar o arquivo origem para o destino, entao remove o arquivo
			// origem. Simulacao do move.
			if (renomeou)
				renomeou = origem.delete();
		}
		return renomeou;
	}

	/**
	 * Metodo....:copiaArquivo
	 * Descricao.:Copia um arquivo para o destino 
	 * @param origem	- Arquivo de origem
	 * @param destino	- Arquivo de destino
	 * @return boolean	- Indica se conseguiu copiar o arquivo
	 */
	private boolean copiaArquivo(File origem, File destino)
	{
		boolean copiou = false;
		try
		{
			FileInputStream input 	= new FileInputStream(origem);
			FileOutputStream output = new FileOutputStream(destino);
			int sizeBuffer = Integer.parseInt(getPropriedade("ordemVoucher.tamanhoBufferArquivos"));
			byte[] buffer = new byte[sizeBuffer];
			int count=0;
			while( (count=input.read(buffer, 0, sizeBuffer)) != -1)
				output.write(buffer,0,count);

			output.close();
			input.close();
			copiou = true;
		}
		catch(IOException e)
		{
			copiou = false;
		}
		return copiou;
	}

	/**
	 * Metodo....:criptografaECompactaOrdem
	 * Descricao.:Realiza a criptografia de todos os arquivos que foram referenciados
	 *            como parametro chamando em seguida o metodo para compactacao desses 
	 *            arquivos
	 * @param ordem 	- Arquivo da ordem de criacao de voucher
	 * @param String[]  - Arquivos a serem criptografados
	 * @return String 	- Nome do arquivo compactado
	 */
	private String criptografaECompactaOrdem(File ordem, String arqAProcessar[]) throws Exception
	{
		long numOrdem = getNumeroOrdem(ordem);

		String requisitante = getIdRequisitante(numOrdem);
		/* Se o requisitante ou a ordem nao for encontrada
		 * o GPP retorna o valor NAO_DISPONIVEL para o id do
		 * requisitante. Neste caso o metodo termina, pois
		 * nada pode ser feito com os arquivos da ordem. Caso
		 * os arquivos a serem processados tambem seja uma
		 * referencia nula, o procedimento e o mesmo
		 */
		if (requisitante.equals(GPPTransferenciaVoucher.REQUISITANTE_NAO_DIPONIVEL) || arqAProcessar == null)
			return null;
			
		String nomeSoftware = getPropriedade("ordemVoucher.pgp");
		String userId       = getPropriedade("ordemVoucher.pgp.userID");
		String verbPhrase   = getPropriedade("ordemVoucher.pgp.verbPhrase");
		for (int i=0; i < arqAProcessar.length; i++)
		{
			/* Define a execucao do comando de criptografia */
			String nomeArquivo  = arqAProcessar[i];			
//			String cmd[] = {nomeSoftware,"+force","-es",nomeArquivo,requisitante,"-u",userId,"-z",verbPhrase};
			String cmd[] = {nomeSoftware,verbPhrase,userId,requisitante,nomeArquivo};

			System.out.println("Criptografando arquivo:"+nomeArquivo+" ....");
			Runtime rt = Runtime.getRuntime();
			Process p  = rt.exec(cmd);
			p.waitFor();
			if (p.exitValue() != 0)
				throw new Exception("Processo de criptografia nao foi executado corretamente para o arquivo:"+arqAProcessar[i]+" requisitante:"+requisitante+" valor de saida:"+p.exitValue());
			
			/* Fecha os "Stream" da classe Process
			 * Isso evita o erro "Too Many Open Files" no sistema operacional
			 */
			p.getOutputStream().close();
			p.getErrorStream().close();
			p.getInputStream().close();
		}
		
		/* Como este metodo ja possui o nome das caixas (arquivos de voucher)
		 * que foram criptografados, entao chama o metodo responsavel 
		 * em compactar estes arquivos em um so com o mesmo nome do arquivo
		 * de cabecalho da Ordem porem com extensao diferente
		 */
		String nomeArqCompactado = compactaOrdem(ordem,arqAProcessar);
		return nomeArqCompactado;
	}
	
	/**
	 * Metodo....:existeArquivos
	 * Descricao.:Verifica se os arquivos da ordem existem no diretorio de arquivos
	 * @param arquivoOrdem	- Arquivo da ordem a ser processada
	 * @return	boolean		- Indica se todos os arquivos da ordem existem no diretorio
	 * @throws IOException
	 */
	private boolean existeArquivos(File arquivoOrdem) throws IOException
	{
		boolean existe=true;
		// Define o diretorio de origem dos arquivos
		String dirOrigem    = getPropriedade("ordemVoucher.dirArquivos");
		// Busca agora todas os nomes de arquivos de caixa
		// existentes gravados tambem no arquivo cabecalho
		String arquivosCaixa[] = getNomeArquivosCaixa(arquivoOrdem);
		// Para cada arquivo de caixa verifica entao se o arquivo fisico realmente existe
		for (int i=0; i < arquivosCaixa.length; i++)
		{
			File arquivo = new File(dirOrigem+System.getProperty("file.separator")+arquivosCaixa[i]);
			if (!arquivo.exists())
			{
				System.out.println("Arquivo da ordem:"+arquivosCaixa[i]+" nao existe fisicamente no diretorio.");
				existe=false;
				break;
			}
		}
		return existe;
	}

	/**
	 * Metodo....:processaArquivos
	 * Descricao.:Este metodo executa a identificacao dos arquivos
	 *            de ordem de criacao de voucher
	 * @param dirName - Nome do diretorio a ser lido
	 */
	public void processaArquivos() throws Exception
	{
		/* Busca o nome do diretorio no arquivo de propriedades */
		String nomeDiretorio = getPropriedade("ordemVoucher.dirArquivos");
		File dirArquivos = new File(nomeDiretorio);
		if (!dirArquivos.isDirectory())
			throw new IOException("Diretorio invalido... "+nomeDiretorio);

		/* Busca os arquivos das ordems (arquivos com informacoes de caixa) */
		FileFilter filtro = new OrdemVoucherFileFilter(getPropriedade("ordemVoucher.patternArquivos"),
													   getPropriedade("ordemVoucher.extensaoArquivos"));
		File arquivosOrdem[] = dirArquivos.listFiles(filtro);
		
		/* Faz a varredura dos arquivos de ordem de voucher encontrados */
		for (int i=0; i < arquivosOrdem.length; i++)
		{
			try
			{
				System.out.println("Processando ordem:"+arquivosOrdem[i].getName());
				if (existeArquivos(arquivosOrdem[i]))
				{
					// Realiza a concatenacao dos arquivos da ordem agrupando-as por item
					// e entao processa-os para a criptografia e compactacao antes de 
					// envia-los ao GPP
					String arqAProcessar[] = concatenaArquivos(arquivosOrdem[i]);
					
					// Para cada arquivo encontrado de ordem de criacao de voucher
					// faz se a criptografia dos arquivos de caixa correspondentes
					// para entao compacta-los em um unico arquivo para posteriormente
					// ser enviado ao GPP juntamente com este arquivo de capa
					// da ordem de criacao do voucher
					String arquivoCompactado = criptografaECompactaOrdem(arquivosOrdem[i],arqAProcessar);
					
					// Caso ao criar os arquivos criptografados e compacta-los aconteca algum
					// erro entao o retorno do metodo e um nome de arquivo nulo. Se este nome
					// for nulo, entao nada e executado com esta ordem passando entao para a
					// proxima
					if (arquivoCompactado != null)
					{
						// Envia o arquivo do cabecalho da ordem 
						// e em seguida envia o arquivo compactado
						enviaArquivoParaGPP(arquivosOrdem[i]);
						enviaArquivoParaGPP(new File(arquivoCompactado));
						
						// Remove os arquivos criptografados e compactados que foram enviados
						// para o GPP
						removeArquivos(arquivosOrdem[i],arqAProcessar);
						
						// Move os arquivos de origem das informacoes de voucher para um diretorio
						// historico desses arquivos
						// Obs: Os arquivos concatenados foram removidos do diretorio
						moveArquivos(arquivosOrdem[i]);
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Erro ao processar ordem:"+arquivosOrdem[i].getName()+" Erro:"+e);
			}
		}
	}

	/**
	 * Metodo....:getPropriedade
	 * Descricao.:Retorna uma propriedade definida no arquivo de configuracao do programa
	 * @param nomePropriedade	- Nome da propriedade desejada
	 * @return String			- Valor da propriedade
	 */
	public String getPropriedade(String nomePropriedade)
	{
		return arquivoProps.getProperty(nomePropriedade);
	}

	/**
	 * Metodo....:main
	 * Descricao.:Metodo de execucao do programa
	 * 
	 * Obs: Se o primeiro argumento for inserido, entao sera considerado
	 * que o nome do diretorio de importacao e este argumento.
	 *  
	 * @param args	- Argumentos da linha de comando
	 */
	public static void main(String args[])
	{
		String nomeArquivo = args.length > 0 ? args[0] : null;
		GPPTransferenciaVoucher transfArquivos = new GPPTransferenciaVoucher(nomeArquivo);

		try
		{
			int segundos = Integer.parseInt(transfArquivos.getPropriedade("ordemVoucher.intervaloProcessamento"));
			/* O programa fica em um loop infinito executando o processamento 
			 * de leitura do diretorio. O intervalo entre uma execucao e outra
			 * e definido no arquivo de propriedades
			 */
			while(true)
			{
				System.out.println("Iniciando processamento dos arquivos...");
				transfArquivos.processaArquivos();
				System.out.println("Fim do processamento dos arquivos...espera de "+segundos+" segundos.");
				Thread.sleep(segundos*1000);
			}
		}
		catch(Exception e)
		{
			System.out.println("Erro ao executar programa.."+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
