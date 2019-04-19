package br.com.brasiltelecom.ppp.interfacegpp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuffer;
import java.util.Collection;
import java.util.Iterator;

import org.omg.CORBA.ORB;

// Importações do GPP
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;

/**
 * Efetua a conexão com o GPP a fim de enviar sms para o cliente
 * do assinante 
 * 
 * @author 	Marcelo Alves Araujo
 * @since   29/06/2005
 * 
 * Modificado por: Joao Paulo Galvagni
 * @since   10/09/2007
 * @motivo  Inclusao do metodo de envio para uma lista (Collection) de assinantes
 */
public class EnviaSMSTeste
{	
	/**
	 * Método para envio de mensagem ao usuário
	 * 
	 * @param msisdn 	- MSISDN do assinante
	 * @param servidor 	- Endereço do servidor
	 * @param porta 	- Porta do servidor
	 * @throws Exception
	 */
	public static void enviaSMS(String msisdn,String servidor, String porta) throws Exception
	{			
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);

			pPOA.enviarSMS(msisdn, "Mensagem de teste. Responda essa mensagem para finalizacao do teste");
		}
		catch (Exception e) 
		{
			if(("" + e).indexOf("SMSC") != -1)
				throw new Exception("Erro ao conectar à SMSC. Por favor contactar o suporte.");
			else if(("" + e).indexOf("CORBA") != -1)
				throw new Exception("Erro ao conectar ao GPP");
		}
	}
	
	/**
	 * Metodo....: enviaSMS
	 * Descricao.: Envia uma mensagem de SMS para os destinatarios contidos no arquivo
	 * 
	 * @param listaMsisdn	- Localização e nome do arquivo que contém os destinatários
	 * @param mensagem	 	- Mensagem a ser enviada
	 * @param servidor 		- Endereço do servidor
	 * @param porta 		- Porta do servidor
	 * @param operador		- Operador que enviou o broadcast de sms
	 * @return Código de sucesso ou retorno
	 * @throws Exception
	 */
	public static String enviaSMS(String arquivoDestinatarios, String mensagem, String servidor, String porta, String operador) throws Exception
	{			
		String resultado = "success";
		
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
						
			// Abre o arquivo para leitura
			FileReader arqRead = null;
			BufferedReader canalLeitura = null;
			arqRead = new FileReader(arquivoDestinatarios);
			canalLeitura = new BufferedReader(arqRead);
			String destino = null;
			
			StringBuffer destinatarios = new StringBuffer(canalLeitura.readLine());
			
			// Lê o primeiro número ou a lista de msisdn separada por ;  e testa se há algo no arquivo
			if( destinatarios == null )
				resultado = "error";
			
			int i = 1;
			
			// Recebe os destinatários
			while( (destino = canalLeitura.readLine()) != null )
			{
				destinatarios = destinatarios.append(";").append(destino);
				if(i++ >= 50000)
				{
					pPOA.enviarSMSMulti(destinatarios.toString(), mensagem, operador);
					destinatarios = new StringBuffer(canalLeitura.readLine());
					i = 1;
				}
			}
			
			pPOA.enviarSMSMulti(destinatarios.toString(), mensagem, operador);
		}
		catch (Exception e) 
		{
			if(("" + e).indexOf("SMSC") != -1)
				throw new Exception("Erro ao conectar à SMSC. Por favor contactar o suporte.");
			else if(("" + e).indexOf("CORBA") != -1)
				throw new Exception("Erro ao conectar ao GPP");
		}
		
		return resultado;
	}
	
	/**
	 * Metodo....: enviaSMS
	 * Descricao.: Envia uma mensagem de SMS para uma lista de destinatarios
	 * 
	 * @param  listaMsisdn	- Colecao contendo os numeros de destino
	 * @param  mensagem	 	- Mensagem a ser enviada
	 * @param  servidor 	- Endereço do servidor
	 * @param  porta 		- Porta do servidor
	 * @param  operador		- Operador que enviou o broadcast de sms
	 * @return resultado	- Sucesso ou Erro no envio
	 * @throws Exception
	 */
	public static String enviaSMS(Collection listaDestinatarios, String mensagem, String servidor, String porta, String operador) throws Exception
	{
		String resultado = "success";
		
		try
		{
			ORB orb = GerenteORB.getORB(servidor, porta);
			
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
			
			aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			
			StringBuffer destinatarios = new StringBuffer();
			int i = 1;
			
			// Varre a lista de destinatarios e envia o SMS a cada 50000 assinantes
			for (Iterator j = listaDestinatarios.iterator(); j.hasNext(); )
			{
				// Monta a string para envio do SMS
				destinatarios.append((String)j.next()).append(";");
				
				if(i++ >= 50000)
				{
					pPOA.enviarSMSMulti(destinatarios.toString(), mensagem, operador);
					destinatarios = new StringBuffer();
					i = 1;
				}
			}
			
			pPOA.enviarSMSMulti(destinatarios.toString(), mensagem, operador);
		}
		catch (Exception e) 
		{
			resultado = "error";
			if(("" + e).indexOf("SMSC") != -1)
				throw new Exception("Erro ao conectar à SMSC. Por favor contactar o suporte.");
			else if(("" + e).indexOf("CORBA") != -1)
				throw new Exception("Erro ao conectar ao GPP");
		}
		
		return resultado;
	}
}