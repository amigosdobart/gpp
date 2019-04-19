package br.com.brasiltelecom;

import java.io.*;
import java.util.*;
import java.net.*;
import java.text.*;

import br.com.brasiltelecom.wig.entity.cadpre.Pessoa;

public class APIConsultaCPF {
	public static String resposta;

	public static void main(String[] args) {

		String cpf = args[0];
		System.out.println(consultacpf(cpf));
	}

	public static Pessoa consultacpf(String cpf) {

		String Resposta = "";
		Pessoa Retorno = null;

		try {

			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd/MM/yyyy HH:mm:ss");
			Date currentTime_1 = new Date();
			String dateString = formatter.format(currentTime_1);

			// Create a socket to the host
			int port = 8108;
			String addr = "entirex.brasiltelecom.com.br";
			Socket socket = new Socket(addr, port);

			// Send header
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream(), "UTF8"));
			StringBuffer data = new StringBuffer(
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			data.append("<mensagem>");
			data.append("<cabecalho>");
			data.append("<empresa>BRG</empresa>");
			data.append("<sistema>SIB</sistema>");
			data.append("<processo>GESCONPESSOA</processo>");
			data.append("<data>").append(dateString).append("</data>");
			data.append("<identificador_requisicao>").append(cpf).append(
					"</identificador_requisicao>");
			data.append("</cabecalho>");
			data.append("<conteudo><![CDATA[<?xml version=\"1.0\"?>");
			data.append("<root>");
			data.append("<tipo_pessoa>1</tipo_pessoa>");
			data.append("<numero_documento>").append(cpf).append(
					"</numero_documento>");
			data.append("</root>]]>");
			data.append("</conteudo>");
			data.append("</mensagem>\n");

			// Send data
			wr.write(data.toString());
			wr.flush();

			// Get response
			InputStream rd = socket.getInputStream();
			byte line[] = new byte[1024];
			resposta = "";
			int cont = 0;
			while ((cont = rd.read(line, 0, 1024)) > 0) {
				Resposta = Resposta + new String(line, 0, cont);
			}
			Retorno = new Pessoa();
			Retorno.loadFromXml(Resposta);
			/*
			 * if ((Resposta.indexOf("<sit_cadastral>0</sit_cadastral>")>=0) ||
			 * (Resposta.indexOf("<sit_cadastral>1</sit_cadastral>")>=0) )
			 * Retorno = Resposta.substring(Resposta.indexOf("<nome>")+6,Resposta.indexOf("</nome>"));
			 */
			wr.close();
			rd.close();

		} catch (Exception e) {
			Resposta = e.toString();
		}

		return Retorno;

	}

}