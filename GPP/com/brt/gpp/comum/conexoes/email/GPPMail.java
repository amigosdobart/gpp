package com.brt.gpp.comum.conexoes.email;

import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.Transport;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;

import java.util.Collection;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Calendar;
import java.util.Iterator;
import java.io.File;

/**
  * Esta classe representa uma mensagem de e-mail a ser enviada
  * pelo sistema GPP. Nesta classe define-se os elementos que compoem a
  * mensagem, como endereco de origem, os recipientes de destino, assim
  * como a mensagem e os anexos desta. Esta classe tambem possui conhecimento,
  * atraves dos mapeamentos de configuracao do sistema, qual SMTP devera ser
  * utilizado para envio desta mensagem assim como a logica necessaria para
  * isso.
  * <P> Versao:        	1.0
  *
  * @Autor:            	Joao Carlos
  * Data:               30/07/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class GPPMail
{
	private long			idProcesso;
	private InternetAddress enderecoOrigem;
	private String 			assunto;
	private String 			mensagem;
	private String			tipoMensagem;
	private Collection 		enderecosDestino;
	private Collection 		arquivosAnexos;

	public static final String TIPO_TEXTO_PLAIN = "text/plain";
	public static final String TIPO_TEXTO_HTML  = "text/html";
	 	
	private MapConfiguracaoGPP	mapConfig = null; //Mapeamento da TBL_GER_CONFIG_GPP
	private	GerentePoolLog gerLog = GerentePoolLog.getInstancia(this.getClass());

	/**
	 * Metodo....:GPPMail
	 * Descricao.:Construtor da classe
	 * @param enderecoOrigem - Endereco originador da mensagem
	 */
	public GPPMail(String enderecoOrigem, long aIdProcesso)
	{
		idProcesso = aIdProcesso;
		// Impossibilita o envio de mensagem com o endereco de origem nulo
		if (enderecoOrigem == null)
		{
			gerLog.log(idProcesso,Definicoes.WARN,"GPPMail","GPPMail","Endereco de origem NULO");
			throw new IllegalArgumentException("Endereco de origem NULO");
		}
		try
		{
			this.enderecoOrigem = new InternetAddress(enderecoOrigem);
			mapConfig = MapConfiguracaoGPP.getInstancia();
		}
		catch(AddressException e)
		{
			gerLog.log(idProcesso,Definicoes.WARN,"GPPMail","GPPMail","Endereco no formato INVALIDO. "+enderecoOrigem);
			throw new IllegalArgumentException("Endereco no formato invalido. "+enderecoOrigem);
		}
		catch(GPPInternalErrorException e)
		{
			gerLog.log(idProcesso,Definicoes.ERRO,"GPPMail","GPPMail","Problemas com a classe de mapeamento do GPP");
			throw new IllegalArgumentException("Problemas com a classe de mapeamento do GPP");
		}

		enderecosDestino    = new TreeSet();
		arquivosAnexos      = new TreeSet();
		tipoMensagem		= GPPMail.TIPO_TEXTO_PLAIN;
	}
	
	/**
	 * Metodo....:setMensagem
	 * Descricao.:Define o corpo da mensagem a ser enviada
	 * @param msg - Mensagem
	 */
	public void setMensagem(String msg)
	{
		this.mensagem = msg;
	}
	
	/**
	 * Metodo....:setAssunto
	 * Descricao.:Define o assunto do e-mail
	 * @param assunto - Assunto
	 */
	public void setAssunto(String assunto)
	{
		this.assunto = assunto;
	}

	/**
	 * Metodo....:setTipoMensagem
	 * Descricao.:Define o tipo de mensagem
	 * @param tipo - Tipo da mensagem ("text/plain" ou "text/html")
	 */	
	public void setTipoMensagem(String tipo)
	{
		this.tipoMensagem = tipo;
	}

	/**
	 * Metodo....:addEnderecoDestino
	 * Descricao.:Adiciona um endereco de destino para o e-mail
	 * @param endereco - Endereco de destino
	 * @return boolean - Indica se conseguiu inserir ou nao o endereco de destino
	 */
	public boolean addEnderecoDestino(String endereco)
	{
		boolean adicionou=false;
		if (endereco == null)
		{
			gerLog.log(idProcesso,Definicoes.WARN,"GPPMail","addEnderecoDestino","Endereco de destino NULO");
			throw new IllegalArgumentException("Endereco de destino NULO");
		}
		// Verifica se o endereco destino esta compativel com um
		// endereco valido, armazenando na colecao somente o objeto
		// InternetAddress
		try
		{
			InternetAddress destAddr = new InternetAddress(endereco);
			adicionou = enderecosDestino.add(destAddr);
		}
		catch(AddressException a)
		{
			gerLog.log(idProcesso,Definicoes.WARN,"GPPMail","addEnderecoDestino","Endereco de destino com formato INVALIDO");
			throw new IllegalArgumentException("Endereco de destino com formato INVALIDO");
		}
		return adicionou;
	}
	
	/**
	 * Metodo....:addArquivoAnexo
	 * Descricao.:Adiciona arquivo anexo ao e-mail
	 * @param arquivo - Arquivo a ser anexado
	 */
	public boolean addArquivoAnexo(File arquivo)
	{
		if (!arquivo.exists())
			throw new IllegalArgumentException("Arquivo anexo nao existe");
			
		return arquivosAnexos.add(arquivo);
	}

	/**
	 * Metodo....:getEnderecosDestino
	 * Descricao.:Retorna um array contendo os enderecos de destino da mensagem
	 * @return InternetAddress[] - Array de enderecos de destino
	 */	
	private InternetAddress[] getEnderecosDestino()
	{
		return (InternetAddress[])enderecosDestino.toArray(new InternetAddress[0]);
	}

	/**
	 * Metodo....:getEnderecosDestinoFormatados
	 * Descricao.:Retorna uma colecao de enderecos destinatarios formatados
	 *            no padrao user@host.domain
	 * @return Collection - Colecao de enderecos da lista de destinatarios
	 */
	private Collection getEnderecosDestinoFormatados()
	{
		Collection enderecos = new LinkedList();
		for (Iterator i=enderecosDestino.iterator();i.hasNext();)
		{
			InternetAddress endereco = (InternetAddress)i.next();
			enderecos.add(endereco.getAddress());
		}
		return enderecos;
	}
	
	/**
	 * Metodo....:getArquivosAnexos
	 * Descricao.:Retorna uma colecao de objetos MimeBodyPart para integralização
	 *            com o corpo da mensagem
	 * @return MimeBodyPart[] - Array contendo os arquivos anexos
	 * @throws MessagingException
	 */
	private MimeBodyPart[] getArquivosAnexos() throws MessagingException
	{
		Collection partes = new LinkedList();
		for (Iterator i = arquivosAnexos.iterator(); i.hasNext();)
		{
			File arquivo = (File)i.next();
			MimeBodyPart 	mbp = new MimeBodyPart();
			FileDataSource 	fds = new FileDataSource(arquivo.getAbsolutePath());

			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
			partes.add(mbp);
		}
		return (MimeBodyPart[])partes.toArray(new MimeBodyPart[0]);
	}
	
	/**
	 * Metodo....:enviaMail
	 * Descricao.:Este metodo realiza o envio de e-Mail sendo utilizados
	 *            os campos previamente preenchidos como o Assunto,Destinatarios
	 *            e arquivos anexos. Este metodo realiza a busca do servidor SMTP
	 *            configurado no GPP para realizar a tarefa, executando um log
	 *            apos execucao.
	 * 
	 * 			OBS: Por ser um envio SMTP nao tem como o GPP saber se a mensagem
	 *               chegou corretamente ao seu destino
	 *
	 *@throws GPPInternalErrorException
	 */
	public void enviaMail() throws GPPInternalErrorException
	{
		gerLog.log(idProcesso,Definicoes.DEBUG,"GPPMail","enviaMail","Inicio");
		// Define o servidor SMTP no qual a mensagem sera enviada
		Properties props = new Properties();
		props.put("mail.smtp.host",mapConfig.getMapValorConfiguracaoGPP("GPP_SERVIDOR_SMTP"));

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);
		try
		{
			// Criando a Mensagem
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject   (this.assunto);
			msg.setFrom      (this.enderecoOrigem);
			msg.setRecipients(Message.RecipientType.TO, getEnderecosDestino());

			// Cria as partes da mensagem e anexos
			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setContent(this.mensagem,this.tipoMensagem);
			//mbp1.setText(this.mensagem);

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);

			MimeBodyPart anexos[] = getArquivosAnexos();
			for (int i=0; i < anexos.length; i++)
				mp.addBodyPart(anexos[i]);

			msg.setContent(mp);
			msg.setSentDate(Calendar.getInstance().getTime());
			
			// Envia a Mensagem
			Transport.send(msg);
			
			// Realiza o Log do envio de mensagem
			gerLog.log(idProcesso,Definicoes.INFO,"GPPMail","enviaMail","Mensagem enviada de:"+enderecoOrigem.getAddress() +" para:"+getEnderecosDestinoFormatados()+" com o assunto:"+assunto);
		}
		catch(MessagingException e)
		{
			gerLog.log(idProcesso,Definicoes.WARN,"GPPMail","enviaMail","Erro no Envio de Email - Mensagem de:"+enderecoOrigem.getAddress() +" para:"+getEnderecosDestinoFormatados()+" com o assunto:"+assunto);
			throw new GPPInternalErrorException("Erro Interno no GPP:"+e.getMessage());
		}
	}
}
