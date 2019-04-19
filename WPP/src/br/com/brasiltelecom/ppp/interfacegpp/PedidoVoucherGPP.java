package br.com.brasiltelecom.ppp.interfacegpp;

import org.omg.CORBA.ORB;
import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;

/**
 * Realiza operacoes no sistema GPP relacionados ao pedido de cartoes (Vouchers)
 * 
 * @author Joao Carlos
 * @since 29/12/2006
 */
public class PedidoVoucherGPP
{
	/**
	 * Metodo....:resubmetePedidoVoucher
	 * Descricao.:Resubmete pedido de voucher ao sistema GPP
	 * @param numPedido	- Numero do Pedido de Voucher
	 * @param servidor	- Endereco do servidor GPP
	 * @param porta		- Porta do servidor GPP
	 * @throws Exception
	 */
	public static void resubmetePedidoVoucher(long numPedido, String servidor, String porta) throws Exception
	{
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();

		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		pPOA.resubmeterPedidoVoucher(numPedido);
	}
}