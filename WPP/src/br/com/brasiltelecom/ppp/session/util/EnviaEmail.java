package br.com.brasiltelecom.ppp.session.util;

import java.net.URL;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *  Esta classe � respons�vel pelo envio dos e-mails.
 *
 * @author   �ric Silva
 * @since    11 de Abril de 2002
 */

public class EnviaEmail
{
        /* Declara��o de vari�veis que ser�o visualizadas por toda a classe*/
          private Session session;
          private String mailFrom;
          private String rcptTo;
          //private String subject;
          private String strErro = null;
          private MimeMessage msg;
          private Multipart mp;
          private InternetAddress from;
          private MimeBodyPart mbp1;
          private MimeBodyPart anexo;
        /********************************************************/

        /**
        *  M�todo para pegar o Erro da Classe.
        *  @return Retorna <b>0</b> se n�o teve nenhum erro. Caso contr�rio retorna o Erro que ocorreu.        
        */
        public String getErro()
        {
			if (strErro == null)
			{
				this.strErro = "0";
			}

            return this.strErro;
        }

        /**
        *  M�todo para pegar o endere�o de origem do E-mail.
        *  @return Retorna uma string com o  endere�o de origem do E-mail.
        */
        public String getMailFrom()
        {
                return this.mailFrom;
        }

        /**
        *  M�todo para pegar o endere�o de destino do E-mail.
        *  @return Retorna uma string com o  endere�o de destino do E-mail.
        */
        public String getRcptTo()
        {
                return this.rcptTo;
        }        

        /**
        *  M�todo construtor da Classe.<br>
        *  Este m�todo � respons�vel em setar as vari�veis de sess�o email de origem e destino e abrir a Sess�o.
        *  
        *  @param mailfrom � uma vari�vel do tipo String que reprenta o email de origem.
        *  @param mailto � uma vari�vel do tipo String que reprenta o email de destino.
        *  @param host � uma vari�vel do tipo String que reprenta o host do Servidor de email.
        */
        public EnviaEmail(String mailfrom, String host)
        {

                this.session = null;
                this.mailFrom = mailfrom;
                this.openSession(host);
        }

        /**
        *  M�todo para verificar se a sess�o do JavaMail est� aberta.
        */
        public boolean isOpen()
        {
                if (!this.session.equals(null))
                {
                        return true;
                }

                return false;
        }

        /**
        *  M�todo para abrir a sess�o do JavaMail.
        *
        *  @param host � uma vari�vel do tipo String que reprenta o host do Servidor de E-mail.
        */
        public void openSession(String host)
        {
                Properties props = new Properties();
                props.put("mail.smtp.host", host);
                this.session = Session.getDefaultInstance(props, null);
        }

        /**
        *  M�todo para fechar a sess�o do JavaMail.
        */
        public void closeSession()
        {
                this.session = null;
        }

	/**
	*   M�todo para adicionar o Multipart na mensagem.
	*/
        private void setContent()
        {

                try
                {
                        /* Adiciona o Multipart na mensagem */
                        this.msg.setContent(this.mp);
                }
                catch(MessagingException me)
                {
                        String strErroContent = "Falha ao configurar conte�do.";
                        strErroContent = strErroContent + "\nExce��o: MessagingException. Classe: email.EnviaEmail";
                        this.strErro=strErroContent;

                        //me.printStackTrace();
                }

        }

        /**
        *  M�todo para enviar o E-mail.
        */
        private void send()
        {

                try
                {
                        /* Finalmente envia a mensagem */
                        Transport.send(this.msg);

                }
                catch(MessagingException me)
                {
                        String strErroSend = "Falha ao enviar e-mail.";
                        strErroSend = strErroSend + "\nExce��o: MessagingException. Classe: email.EnviaEmail";
                        this.strErro=strErroSend;
                }
        }

        public void adicionaURLAnexo(URL url, String nome) throws Exception
        {
        	this.anexo = new MimeBodyPart();
        	URLDataSource urlDS = new URLDataSource(url);
        	
        	this.anexo.setDataHandler(new DataHandler(urlDS));
        	this.anexo.setFileName(nome);
        }
        
        /**
        *  M�todo inicial para o envio de email. Este � o m�todo que deve ser chamado
        *  para a realiza��o do envio de E-mail.
        */
        public void sendMessage(String assunto,String mensagem, String mailto)
        {
     		    this.rcptTo = mailto;

				this.strErro = "0";
        	
                try
                {
                        /* Instancia uma nova mensagem */
                        msg = new MimeMessage(this.session);

                        /* Instancia o emissor da mensagem */
                        from = new InternetAddress(this.getMailFrom());
                        msg.setFrom(from);

                        /* Instancia o(s) destinatario(s) da mensagem */
                        InternetAddress[] address = {new InternetAddress(this.getRcptTo())};

                        msg.setRecipients(Message.RecipientType.TO, address);

                        /* Adiciona o subject a mensagem */
                        msg.setSubject(assunto);

                        /* Cria e adiciona as partes do corpo da mensagem */
                        mbp1 = new MimeBodyPart();
                        mbp1.setContent(mensagem, "text/html");

                        /* Instancia um MimeMultipart e adiciona as partes */
                        mp = new MimeMultipart();
                        mp.addBodyPart(mbp1);
                        mp.addBodyPart(anexo);

                        /* Adiciona o Multipart na mensagem */
                        setContent();

                        /* Finalmente envia a mensagem */
                        send();
                }
                catch (MessagingException me)
                {
                        String strErroMessage = "Falha ao enviar a mensagem.";
                        strErroMessage = strErroMessage + "Exce��o: MessagingException. Classe: email.EnviaEmail";
                        this.strErro=strErroMessage;
                }
        }


}