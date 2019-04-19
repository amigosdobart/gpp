package clientes;

import com.brt.gppSocketGateway.comum.GSocket;
//import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;

public class ClienteEnvioMsgGPPtoASAP 
{

	public static void main(String[] args) 
	{
		String retorno = null;
		try
		{
//			ArqConfigGPPServer conf = ArqConfigGPPServer.getInstance("c:/desenvolvimento/class/com/brt/gppSocketGateway/ConfigGPPServer.cfg");
			GSocket cliSockGPP_ASAP = new GSocket("127.0.0.1","12551",0);
			// Inicia o hand shake
			cliSockGPP_ASAP.sendMsg("{Start}");

			// Espera o retorno do HandShake
			retorno = cliSockGPP_ASAP.getMsg(0);

			System.out.println("Final do HandShake. Retorno: " + retorno);
			
			// Envia solicitação de bloqueio para o servidor GPP mandar para o ASAP
			cliSockGPP_ASAP.sendMsg("{GPP-APROVISIONAMENTO}<root><str1>conteudo</str1></root>");

			retorno = cliSockGPP_ASAP.getMsg(0);

			System.out.println("Final do HandShake. Retorno: " + retorno);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
