package br.com.brasiltelecom;

import java.io.*;
import java.net.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class DesbloqueioHL {

	private InitialContext ctx = null;
	private Logger log = null;
	private String servidor;
	private int porta;
	public DesbloqueioHL(InitialContext ctx, String servidor, int porta) {
		this.ctx = ctx;
		this.servidor = servidor;
		this.porta = porta;
		log = Logger.getLogger(this.getClass());
	}

	public static void main(String[] args) throws NamingException {

		String msisdn = args[0];
		String operacao = args[1];
		DesbloqueioHL desbloqHL = new DesbloqueioHL(new InitialContext(), "10.61.176.25", 8621);
		System.out.println(desbloqHL.DesbloqueioHotline(msisdn, operacao, "99999"));
	}



	public String DesbloqueioHotline(String msisdn, String operacao, String chave) {

		String Resposta = "";
		Socket socket = null;
		String xml = getXml(msisdn, operacao, chave);
		try {

			// Create a socket to the host
			if(log.isDebugEnabled()){
				log.debug("Conectando ao servidor " + servidor);
			}
			socket = new Socket(servidor, porta);
			if(log.isDebugEnabled()){
				log.debug("Conexao OK : " + servidor);
			}
			
			socket.setSoTimeout(30 * 60 * 1000); // 30 segundos

			// Send header
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
			// Send data
			if(log.isDebugEnabled()){
				log.debug("Enviando dados para " + servidor);
			}

			wr.write(xml);
			wr.flush();
			if(log.isDebugEnabled()){
				log.debug("Envio de dados OK " + servidor);
			}

			
			// Get response

			if(log.isDebugEnabled()){
				log.debug("Recebendo Dados " + servidor);
			}

			InputStream rd = socket.getInputStream();
			byte line[] = new byte[1024];
			int cont = 0;
			while ((cont = rd.read(line, 0, 1024)) > 0) {
				Resposta = Resposta + new String(line, 0, cont);
			}
			if(log.isDebugEnabled()){
				log.debug("Dados Recebidos : " + Resposta);
			}

			if (Resposta.indexOf("<codigo_retorno>0</codigo_retorno>") >= 0) {
				Resposta = "OK";
			}

			wr.close();
			rd.close();
		} catch (Exception e) {
			log.warn("Entrando em modo de contigencia", e);
			DAOGPP daoGpp = new DAOGPP(ctx);
			daoGpp.salvaDesbloqueio(xml);
			Resposta = "CONTINGENCIA";
			log.warn("Envio para contigencia {" + xml + "}");
		} finally {
			if (socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return Resposta;

	}
	private  String getXml(String msisdn, String operacao, String chave) {
		StringBuffer data = new StringBuffer("<root>");
		data.append("<idorigem><sistema>URA</sistema></idorigem>");
		data.append("<id_os>").append(chave).append("</id_os> ");
		data.append("<msisdn_novo>").append(msisdn).append("</msisdn_novo> ");
		data.append("<msisdn_antigo>").append(msisdn).append("</msisdn_antigo> ");
		data.append("<case_type>Retirada de bloqueio Hotline</case_type> ");
		data.append("<case_sub_type>Retirada de bloqueio Hotline</case_sub_type> ");
		data.append("<fluxo>DesbloqueioURA</fluxo> <order_priority>alta</order_priority> ");
		data.append("<categoria>F2</categoria><categoria_anterior>F1</categoria_anterior> ");
		data.append("<case_id>").append(chave).append("</case_id> ");
		data.append("<nome_produto_smp>Produto Acesso Movel</nome_produto_smp>");
		data.append("<identificador_produto_smp>").append(msisdn).append("</identificador_produto_smp> ");
		data.append("<provision><ELM_INFO_SIMCARD><macro_servico>ELM_INFO_SIMCARD</macro_servico> <operacao>nao_alterado</operacao> <x_tipo>SIMCARD</x_tipo> <status>NAO_FEITO</status> ");
		data.append("<parametros><simcard_msisdn>").append(msisdn).append("</simcard_msisdn></parametros>");
		data.append("</ELM_INFO_SIMCARD><ELM_BLOQ_HOT_LINE><macro_servico>ELM_BLOQ_HOT_LINE</macro_servico>");
		data.append("<operacao>").append(operacao).append("</operacao>");
		data.append("<x_tipo>SERVICO DE BLOQUEIO</x_tipo> <status>NAO_FEITO</status> </ELM_BLOQ_HOT_LINE></provision></root>\n");
		return data.toString();
	}
}