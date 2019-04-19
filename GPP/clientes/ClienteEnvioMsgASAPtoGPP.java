package clientes;

//import com.brt.gppSocketGateway.comum.ArqConfigGPPServer;
import com.brt.gppSocketGateway.comum.GSocket;

public class ClienteEnvioMsgASAPtoGPP
{

	public static void main(String[] args) 
	{
		try
		{
//			ArqConfigGPPServer conf = ArqConfigGPPServer.getInstance("c:/desenvolvimento/class/com/brt/gppSocketGateway/ConfigGPPServer.cfg");
			GSocket cliSockASAP_GPP = new GSocket("127.0.0.1","12550",0);

			String retorno = null;

			// Envia retorno de bloqueio para o servidor do ASAP (esse cliente simula ser o ASAP
			// retornado um XML para o servidor ASAP retorná-lo ao GPP)
			System.out.println("Iniciando HandShake");
			cliSockASAP_GPP.sendMsg("{Start}");

			// Espera o retorno do HandShake
			retorno = cliSockASAP_GPP.getMsg(0);

			System.out.println("Final do HandShake. Retorno: " + retorno);
			
			//while (true)
			{
				//System.out.println ("Valor retornado: " + retorno);
				cliSockASAP_GPP.sendMsg(getXMLASAP());	
				retorno = cliSockASAP_GPP.getMsg(0);
					
				System.out.println ("Valor retornado: " + retorno);
				
				try 
				{
					Thread.sleep(6000);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static String getXMLASAP ()
	{
		String ret = "";
		ret += "<root>";
		ret += "<id_os>GPP0000000002633</id_os>";
		ret += "<msisdn_novo>556184012000</msisdn_novo>";
		ret += "<msisdn_antigo>556184012000</msisdn_antigo>";
		ret += "<case_type>Bloqueio</case_type>";
		ret += "<case_sub_type>Bloqueio</case_sub_type>";
		ret += "<order_priority>baixa</order_priority>";
		ret += "<categoria>F3</categoria>";
		ret += "	<categoria_anterior>F3</categoria_anterior>";
		ret += "	<case_id>GPP0000000002633</case_id>";
		ret += "	<provision>";
	    ret += "		<ELM_INFO_SIMCARD>";
	    ret += "			<macro_servico>ELM_INFO_SIMCARD</macro_servico>";
		ret += "			<operacao>bloquear</operacao>";
		ret += "			<x_tipo>SIMCARD</x_tipo>";
		ret += "			<status>OK</status>";
		ret += "			<parametros>";
		ret += "				<simcard_msisdn>556184012000</simcard_msisdn>";
		ret += "			</parametros>";
		ret += "			<historico>17/09/2004 03:33:48 Initial Reception of Work Order";
		ret += "17/09/2004 03:33:49 Current CSDL 'CSDL_HLR_BLQ_TOT' is under provision";
		ret += "17/09/2004 03:33:49 ASDL ASDL_HLR_BLQ_TOT for ERICSSON Route to NE ERICSSON";
		ret += "17/09/2004 03:33:49 ASDL ASDL_HLR_BLQ_TOT Provisioning Request to ERICSSON";
		ret += "17/09/2004 03:34:00 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:01 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBO-1;";
		ret += "17/09/2004 03:34:02 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBI-1;";
		ret += "17/09/2004 03:34:03 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:03 Q Info: Queued: 15:34:00, Start: 15:34:00, Comp: 15:34:03";
		ret += "17/09/2004 03:34:03 ASDL ASDL_HLR_BLQ_TOT_BKP for ERIC_BKP Route to NE ERIC_BKP";
		ret += "17/09/2004 03:34:03 ASDL ASDL_HLR_BLQ_TOT_BKP Provisioning Request to ERIC_BKP";
		ret += "17/09/2004 03:34:14 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:15 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBO-1;";
		ret += "17/09/2004 03:34:16 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBI-1;";
		ret += "17/09/2004 03:34:17 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:17 Q Info: Queued: 15:34:14, Start: 15:34:14, Comp: 15:34:17";
		ret += "17/09/2004 03:34:17 ASDL ASDL_NDS_BLQ for NDS Route to NE NDS";
		ret += "17/09/2004 03:34:17 ASDL ASDL_NDS_BLQ Provisioning Request to NDS";
		ret += "17/09/2004 03:34:20 ASDL Exit (SUCCEED) Msg : Operacao executada com sucesso";
		ret += "17/09/2004 03:34:20 Q Info: Queued: 15:34:19, Start: 15:34:19, Comp: 15:34:20";
		ret += "17/09/2004 03:34:20 ASDL ASDL_GPP_BLOQ for GPP Route to NE GPP";
		ret += "17/09/2004 03:34:20 ASDL ASDL_GPP_BLOQ Provisioning Request to GPP";
		ret += "17/09/2004 03:34:20 Inicio do bloqueio do telefone no NDS";
		ret += "17/09/2004 03:34:20 Loopback mode - Retornando sucesso";
		ret += "17/09/2004 03:34:21 Inicio do bloqueio de assinante Pre-Pago no GPP";
		ret += "17/09/2004 03:34:21 Loopback mode - Retornando sucesso";
		ret += "17/09/2004 03:34:21 ASDL Exit (SUCCEED) Msg : Operacao executada com sucesso";
		ret += "17/09/2004 03:34:21 Q Info: Queued: 15:34:21, Start: 15:34:21, Comp: 15:34:21";
		ret += "17/09/2004 03:34:21 SRQ 10037 (Last CSDL CSDL_HLR_BLQ_TOT) has Completed</historico>";
		ret += "			<descricao_erro>Loopback_mode_-_Ordem_executada_com_sucesso</descricao_erro>";
		ret += "			<codigo_erro>0000</codigo_erro>";
		ret += "		</ELM_INFO_SIMCARD>";
		ret += "		<ELM_GPP_BLACK_LIST>";
		ret += "			<macro_servico>ELM_GPP_BLACK_LIST</macro_servico>";
		ret += "			<operacao>bloquear</operacao>";
		ret += "			<x_tipo>SERVICODE BLOQUEIO</x_tipo>";
		ret += "			<status>NOK</status>";
		ret += "			<codigo_erro>9090</codigo_erro>";
		ret += "			<descricao_erro>Servico ELM_GPP_BLACK_LIST NAO implementado</descricao_erro>";
		ret += "		</ELM_GPP_BLACK_LIST>";
		ret += "	</provision>";
		ret += "	<historico_erro>17/09/2004 03:33:48 Initial Reception of Work Order";
		ret += "17/09/2004 03:33:49 Current CSDL 'CSDL_HLR_BLQ_TOT' is under provision";
		ret += "17/09/2004 03:33:49 ASDL ASDL_HLR_BLQ_TOT for ERICSSON Route to NE ERICSSON";
		ret += "17/09/2004 03:33:49 ASDL ASDL_HLR_BLQ_TOT Provisioning Request to ERICSSON";
		ret += "17/09/2004 03:34:00 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:01 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBO-1;";
		ret += "17/09/2004 03:34:02 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBI-1;";
		ret += "17/09/2004 03:34:03 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:03 Q Info: Queued: 15:34:00, Start: 15:34:00, Comp: 15:34:03";
		ret += "17/09/2004 03:34:03 ASDL ASDL_HLR_BLQ_TOT_BKP for ERIC_BKP Route to NE ERIC_BKP";
		ret += "17/09/2004 03:34:03 ASDL ASDL_HLR_BLQ_TOT_BKP Provisioning Request to ERIC_BKP";
		ret += "17/09/2004 03:34:14 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:15 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBO-1;";
		ret += "17/09/2004 03:34:16 NE Cmd: HGSDC:MSISDN=556184012000,SUD=OBI-1;";
		ret += "17/09/2004 03:34:17 ASDL Exit (SUCCEED)";
		ret += "17/09/2004 03:34:17 Q Info: Queued: 15:34:14, Start: 15:34:14, Comp: 15:34:17";
		ret += "17/09/2004 03:34:17 ASDL ASDL_NDS_BLQ for NDS Route to NE NDS";
		ret += "17/09/2004 03:34:17 ASDL ASDL_NDS_BLQ Provisioning Request to NDS";
		ret += "17/09/2004 03:34:20 ASDL Exit (SUCCEED) Msg : Operacao executada com sucesso";
		ret += "17/09/2004 03:34:20 Q Info: Queued: 15:34:19, Start: 15:34:19, Comp: 15:34:20";
		ret += "17/09/2004 03:34:20 ASDL ASDL_GPP_BLOQ for GPP Route to NE GPP";
		ret += "17/09/2004 03:34:20 ASDL ASDL_GPP_BLOQ Provisioning Request to GPP";
		ret += "17/09/2004 03:34:20 Inicio do bloqueio do telefone no NDS";
		ret += "17/09/2004 03:34:20 Loopback mode - Retornando sucesso";
		ret += "17/09/2004 03:34:21 Inicio do bloqueio de assinante Pre-Pago no GPP";
		ret += "17/09/2004 03:34:21 Loopback mode - Retornando sucesso";
		ret += "17/09/2004 03:34:21 ASDL Exit (SUCCEED) Msg : Operacao executada com sucesso";
		ret += "17/09/2004 03:34:21 Q Info: Queued: 15:34:21, Start: 15:34:21, Comp: 15:34:21";
		ret += "17/09/2004 03:34:21 SRQ 10037 (Last CSDL CSDL_HLR_BLQ_TOT) has Completed";
		ret += "</historico_erro>";
		ret += "		<descricao_erro>Servico ELM_GPP_BLACK_LIST NAO implementado</descricao_erro>";
		ret += "		<codigo_erro>9090</codigo_erro>";
		ret += "		<status>NOK</status>";
		ret += "</root>";
		
		return ret;
	}
}

