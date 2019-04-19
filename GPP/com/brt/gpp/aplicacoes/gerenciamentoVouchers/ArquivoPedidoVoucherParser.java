package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import java.util.Collection;
import java.util.LinkedList;

import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdem;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemCaixa;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherOrdemItem;

/**
  * Este arquivo contem as definicoes de regras para parse do arquivo de dados
  * provenientes da Tecnomen com informacoes de pedidos de voucher. Como as informacoes
  * vem atraves de Ordens, entao essa classe faz o parse do arquivo para um objeto VoucherOrdem
  * que contem informacoes de linhas (as mesmas do pedido e informacoes de caixa dos vouchers)
  * <P> Versao:        	1.0
  *
  * @Autor:            	Joao Carlos
  * Data:               24/06/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class ArquivoPedidoVoucherParser 
{
	private static final int NUMERO_CAMPOS_ITEM		= 14;
	private static final int NUMERO_CAMPOS_CAIXA	= 5;
	
	/**
	 * Metodo....:ArquivoPedidoVoucherParser
	 * Descricao.:Metodo (construtor) da classe
	 *
	 */
	public ArquivoPedidoVoucherParser()
	{
	}
	
	/**
	 * Metodo....:parse
	 * Descricao.:Faz o parse do arquivo de criacao de voucher recebendo um BufferedReader
	 *            do arquivo 
	 * @param arquivoVoucher- Arquivo de criacao de voucher a ser processado
	 * @return VoucherOrdem	- Objeto VoucherOrdem com as informacoes do arquivo
	 * @throws IOException
	 */
	public VoucherOrdem parse(File arquivoVoucher) throws IOException
	{
		if (!arquivoVoucher.isFile())
			throw new IllegalArgumentException("Arquivo invalido.");
		
		FileReader 		fReader 	= new FileReader(arquivoVoucher);
		BufferedReader 	buffReader 	= new BufferedReader(fReader);

		String strLinhasOrdem = "#Line Items in this Order:";
		String strLinhasCaixa = "#Boxes in this Order:";

		String linha;
		VoucherOrdem ordemVoucher = null;
		while ( (linha = buffReader.readLine()) != null)
		{
			/* Caso o objeto esteja nulo, entao faz o parse da linha buscando
			 * o numero da ordem para que entao inicialize o objeto. Somente apos
			 * a identificacao da ordem e que sera possivel realizar o parse dos
			 * outros dados.
			 */
			if (ordemVoucher == null)
				ordemVoucher = parseOrdem(linha);
			
			/* Caso o numero da ordem ja tenha sido identificado, entao agora
			 * busca o restante das informacoes
			 */
			if (ordemVoucher != null)
			{
				/* Se a condicao for satisfeita, entao entra em processamento de
				 * parse das linhas dessa ordem
				 */
				if (linha.indexOf(strLinhasOrdem) > -1)
				{
					/* Primeiro faz a leitura de tres linhas desprezando seu conteudo, pois
					 * as informacoes que interessam da linha da ordem comecam na quarta linha 
					 * do arquivo a partir da string de busca
					 */
					buffReader.readLine();
					buffReader.readLine();
					buffReader.readLine();
					
					/* Agora comeca o processamento das linhas de ordem 
					 * O caracter "#" indica que as linhas de ordem terminaram no arquivo 
					 */
					while ( ((linha = buffReader.readLine()) != null) && (linha.indexOf("#") == -1) )
					{
						/* Busca informacoes sobre os itens */
						VoucherOrdemItem itemOrdem = parseItem(ordemVoucher,linha);
						/* Se o item for valido, entao adiciona-o a ordem */
						if (itemOrdem != null)
							ordemVoucher.addItemOrdem(itemOrdem);
					}
				}

				/* Se a condicao for satisfeita, entao inicia-se o processo de parse
				 * dos numeros de caixa para a ordem dentro do arquivo
				 */
				if (linha.indexOf(strLinhasCaixa) > -1)
				{
					/* Primeiro faz a leitura de tres linhas desprezando seu conteudo, pois
					 * as informacoes que interessam da linha da ordem comecam na quarta linha 
					 * do arquivo a partir da string de busca
					 */
					buffReader.readLine();
					buffReader.readLine();
					buffReader.readLine();

					/* Agora comeca o processamento das caixas dessa ordem */
					while ( ((linha = buffReader.readLine()) != null) )
					{
						/* Busca informacoes sobre os caixas */
						VoucherOrdemCaixa ordemCaixa = parseCaixa(ordemVoucher,linha);
						/* Se o item for valido, entao adiciona-o a ordem */
						if (ordemCaixa != null)
							ordemVoucher.addCaixaOrdem(ordemCaixa);
					}
				}
			}
		}
		return ordemVoucher;
	}
	
	/**
	 * Metodo....:parsePedido
	 * Descricao.:Retorna o pedido de criacao de voucher lendo as informacoes da linha
	 * @param 	aLinha 			- Linha de informacoes do pedido do arquivo texto
	 * @return	VoucherPedido	- Pedido de voucher carregado com as informacoes no arquivo
	 */
	private VoucherOrdem parseOrdem(String aLinha)
	{
		VoucherOrdem ordemVoucher = null;
		/* O objeto sendo lido do arquivo nao possui numeracao de pedido,
		 * portanto o numero da ordem sera utilizado, pois futuramente
		 * sera necessario buscar o pedido que esta associado a esta ordem
		 */
		String strOrderNumber = "Order Number:";
		if (aLinha.indexOf(strOrderNumber) > -1)
		{
			long numeroOrdem = Long.parseLong( aLinha.substring(strOrderNumber.length()).trim() );
			ordemVoucher = new VoucherOrdem(numeroOrdem);
		}

		return ordemVoucher;
	}
	
	/**
	 * Metodo....:parseItem
	 * Descricao.:Identifica os valores de informacoes do item da ordem
	 * @param ordem  - Ordem a ser associada ao item
	 * @param aLinha - Linha a ser lida
	 * @return VoucherOrdemItem - Item da ordem
	 */
	private VoucherOrdemItem parseItem(VoucherOrdem ordem,String aLinha)
	{
		VoucherOrdemItem itemPedido = null;
		//String campos[] = aLinha.split(",");
		String campos[] = split(aLinha,",");
		/* Se o array tiver o numero de campos previamente definidos entao
		 * a linha que esta sendo feito o parse realmente eh a linha do item
		 * da ordem, entao faz-se o parse dos campos na ordem que eles aparecem
		 * Ordem  		Nome do Campo
		 * 0 		- 	Numero do item
		 * 1		-	Quantidade de cartoes
		 * 2		-	Valor de face
		 * 3		-	Currency (desprezado)
		 * 4		-	Data de expiracao (desprezado)
		 * 5		-	Quantidade por batch
		 * 6		-	Quantidade por caixa
		 */
		if (campos.length == ArquivoPedidoVoucherParser.NUMERO_CAMPOS_ITEM)
		{
			itemPedido = new VoucherOrdemItem(ordem,Long.parseLong(campos[0].trim()));
			itemPedido.setQtdeCartoes ( Long.parseLong(campos[1].trim()) );
			itemPedido.setValorFace   ( Long.parseLong(campos[2].trim()) );
			itemPedido.setQtdePorBatch( Long.parseLong(campos[5].trim()) );
			itemPedido.setQtdePorCaixa( Long.parseLong(campos[6].trim()) );
		}
		else
			throw new IllegalArgumentException("Definicao de itens com problemas no numero de colunas");

		return itemPedido;
	}
	
	/**
	 * Metodo....:parseCaixa
	 * Descricao.:Identifica os valores de informacoes sobre as caixas de voucher dessa ordem
	 * @param ordem		- Ordem a ser associada a caixa
	 * @param aLinha	- Linha do arquivo a ser lida
	 * @return VoucherOrdemCaixa - Objeto contendo informacoes sobre a caixa
	 */
	private VoucherOrdemCaixa parseCaixa(VoucherOrdem ordem,String aLinha)
	{
		VoucherOrdemCaixa ordemCaixa = null;
		//String campos[] = aLinha.split(",");
		String campos[] = split(aLinha,",");
		if (campos.length == ArquivoPedidoVoucherParser.NUMERO_CAMPOS_CAIXA)
		{
			/* O numero da caixa possui o seguinte formato no arquivo:
			 * PrintOrder#NUMORDEM_NUMCAIXA.dat, portanto o caracter "_" e o
			 * ".dat" sao utilizados como fronteiras do numero da caixa.
			 */
			int posInicial = campos[0].indexOf("_")+1;
			int posFinal   = campos[0].indexOf(".dat");
			long numeroCaixa = Long.parseLong(campos[0].substring(posInicial,posFinal));
			
			ordemCaixa = new VoucherOrdemCaixa(ordem,numeroCaixa);
			ordemCaixa.setQtdePorBatch( Long.parseLong(campos[1].trim()) );
			ordemCaixa.setQtdePorCaixa( Long.parseLong(campos[2].trim()) );
		}
		return ordemCaixa;
	}
	
	/**
	 * Metodo....:split
	 * Descricao.:Este metodo faz a quebra de uma string baseada no parametro
	 *            separador retornando entao um array com a string "quebrada"
	 *            
	 * 
	 * OBS: Este metodo esta sendo desenvolvido para suprir a necessidade do 
	 *      programa de envio de arquivos de voucher que utiliza esta classe
	 *      e e executado na versao 1.3 da virtual machine. Sendo que o GPP
	 *      nao precisa deste metodo por ja utilizar a versao 1.4 e portanto
	 *      existir o metodo na classe String (String.split())
	 * 
	 * @param origem	- String original
	 * @param separador	- String separador das sub-strings
	 * @return String[] - Array de string da string que foi quebrada
	 */
	private String[] split(String origem, String separador)
	{
		Collection lista = new LinkedList();
		int posFinal  =-1;
		int posInicial=0;
		while ( (posFinal=origem.indexOf(separador)) > -1)
		{
			lista.add(origem.substring(posInicial,posFinal));
			origem = origem.substring(posFinal+separador.length(),origem.length());
		}
		lista.add(origem);
		return (String[])lista.toArray(new String[0]);
	}
}
