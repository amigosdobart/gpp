// Cliente de Consulta de Voucher
package clientes;

//import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.componentes.recarga.orb.*;

public class ClienteCancelaVoucherGPP
{
	public ClienteCancelaVoucherGPP ( )
	{
		System.out.println ("Cancelando voucher...");
	}
	
	public static void main(String[] args) 
	{
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		String motivo = "GPP";
		try
		{
			String vouchers[] = args[2].split(",");
			long voucherIni = Long.parseLong(vouchers[0]);
			long voucherFim = vouchers.length > 1 ? Long.parseLong(vouchers[1]) : voucherIni;
			while (voucherIni <= voucherFim)
			{
				short ret = pPOA.alteraStatusVoucher (String.valueOf(voucherIni), 11, motivo);
				
				System.out.println("Cancelamento voucher:"+voucherIni+" retorno:"+ret);
				voucherIni++;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}
	}
}