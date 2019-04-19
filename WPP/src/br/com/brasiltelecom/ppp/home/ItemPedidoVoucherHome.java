package br.com.brasiltelecom.ppp.home;

import java.util.ArrayList;
import java.util.Collection;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.DetalhePedidoVoucher;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas a pedidos de voucher
 * 
 * @author Alberto Magno
 * @since 28/06/2004
 */
public class ItemPedidoVoucherHome {

	/**
	 * Obt�m todas pedidos de acordo com o n�mero do pedido
	 * 
	 * @param db Conex�o com o banco de dados
	 * @param nroPedido o n�mero do pedido a se buscar
	 * @return Cole��o de objetos do tipo PedidoVoucher
	 * @throws PersistenceException
	 */
	public static Collection findByPedido(Database db, String nroPedido) throws PersistenceException {

		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			StringBuffer filtro = new StringBuffer();
				
			if(nroPedido != null && !"".equals(nroPedido))
			{
				StringBuffer consulta = new StringBuffer("select a from ");
				consulta.append("br.com.brasiltelecom.ppp.portal.entity.DetalhePedidoVoucher a ");
				
				filtro.append("where a.nroPedido = " + nroPedido);
				
				query = db.getOQLQuery(consulta.toString()+ filtro.toString() + " order by a.nroItem");

				rs = query.execute();
				while(rs.hasMore())
				{
					DetalhePedidoVoucher pedido = (DetalhePedidoVoucher) rs.next();					 
					result.add(pedido);						
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(rs != null) rs.close();
			if(query!=null){
				query.close();
			}
		}
		return result;
   }
	  
}
