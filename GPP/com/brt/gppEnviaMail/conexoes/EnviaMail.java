package com.brt.gppEnviaMail.conexoes;

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
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.activation.URLDataSource;

import com.brt.gppEnviaMail.GPPEnviaMailDefinicoes;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Calendar;
import java.util.Iterator;
import java.io.File;
import java.net.URL;

public class EnviaMail
{
	private InternetAddress enderecoOrigem;
	private String 			assunto;
	private String 			mensagem;
	private String			tipoMensagem;
	private String			servidorSMTP;
	private Collection 		enderecosDestino;
	private Collection		anexos;

	/**
	 * Metodo....:GPPMail
	 * Descricao.:Construtor da classe
	 * @param enderecoOrigem - Endereco originador da mensagem
	 */
	public EnviaMail(String enderecoOrigem, String servidorSMTP)
	{
		// Impossibilita o envio de mensagem com o endereco de origem nulo
		if (enderecoOrigem == null)
			throw new IllegalArgumentException("Endereco de origem deve ser diferente de nulo");

		if (servidorSMTP == null)
			throw new IllegalArgumentException("Servidor SMTP nao pode ser nulo");
		
		try
		{
			this.enderecoOrigem = new InternetAddress(enderecoOrigem);
			this.servidorSMTP	= servidorSMTP;
		}
		catch(AddressException e)
		{
			throw new IllegalArgumentException("Endereco no formato invalido. "+enderecoOrigem);
		}
		enderecosDestino    = new LinkedList();
		anexos				= new LinkedList();
		tipoMensagem		= GPPEnviaMailDefinicoes.TIPO_TEXTO_PLAIN;
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
			throw new IllegalArgumentException("Endereco de destino nao pode ser nulo");

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
			throw new IllegalArgumentException("Endereco de destino esta em formato invalido");
		}
		return adicionou;
	}
	
	/**
	 * Metodo....:addArquivoAnexo
	 * Descricao.:Adiciona arquivo anexo ao e-mail
	 * @param arquivo - Arquivo a ser anexado
	 */
	public boolean addArquivoAnexo(File arquivo) throws MessagingException
	{
		if (!arquivo.exists())
			throw new IllegalArgumentException("Arquivo anexo nao existe");
		
		FileDataSource fds = new FileDataSource(arquivo);
		return anexos.add(getMimePorDataSource((DataSource)fds,arquivo.getName()));
	}

	/**
	 * Metodo....:addUrlAnexo
	 * Descricao.:Adiciona o resultado de uma URL como anexo ao e-mail
	 * @param url	- URL a ser adicionada
	 * @param nome	- Nome a ser associado ao anexo
	 * @return boolean - Indica se conseguiu incluir a URL ou nao
	 * @throws MessagingException
	 */
	public boolean addUrlAnexo(URL url, String nome) throws MessagingException
	{
		URLDataSource uds = new URLDataSource(url);
		return anexos.add(getMimePorDataSource((DataSource)uds, nome));
	}
	
	/**
	 * Metodo....:getEnderecosDestino
	 * Descricao.:Retorna um array contendo os enderecos de destino da mensagem
	 * @return InternetAddress[] - Array de enderecos de destino
	 */	
	public InternetAddress[] getEnderecosDestino()
	{
		return (InternetAddress[])enderecosDestino.toArray(new InternetAddress[0]);
	}

	/**
	 * Metodo....:getMimePorDataSource
	 * Descricao.:Retorna o objeto Mime a ser utilizado no anexo do e-mail
	 *            por um DataSource que poder arquivo (FileDataSource) ou URL (URLDataSource)
	 * @param ds	- DataSource a ser utilizado para criacao do anexo
	 * @param nome	- Nome a ser associado ao anexo
	 * @return
	 * @throws MessagingException
	 */
	private MimeBodyPart getMimePorDataSource(DataSource ds, String nome) throws MessagingException
	{
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setDataHandler(new DataHandler(ds));
		mbp.setFileName(nome);
		
		return mbp;
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
	public void enviaMail()
	{
		// Define o servidor SMTP no qual a mensagem sera enviada
		Properties props = new Properties();
		props.put("mail.smtp.host",servidorSMTP);

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

			// Realiza a iteracao entre os objetos que foram criados como anexo
			for (Iterator i = anexos.iterator(); i.hasNext();)
			{
				MimeBodyPart mbp = (MimeBodyPart)i.next();
				mp.addBodyPart(mbp);
			}

			msg.setContent(mp);
			msg.setSentDate(Calendar.getInstance().getTime());
			
			// Envia a Mensagem
			Transport.send(msg);
		}
		catch(MessagingException e)
		{
			e.printStackTrace();
		}
	}
}
