package br.com.brasiltelecom.ppp.home;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.OQLQuery;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.QueryResults;

import br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoPedidosVoucher;

import com.brt.gpp.comum.Definicoes;

/**
 * Classe responsável pelas consultas/atualizações no banco de dados relativas a pedidos de voucher
 * 
 * @author Alberto Magno
 * @since 28/06/2004
 */
public class PedidoVoucherHome {

	/**
	 * Obtém os pedidos de voucher de acordo com o parâmetros
	 * 
	 * @param db Conexão com o banco de dados
	 * @param param Parâmetros de consulta dos pedidos
	 * @return Coleção de objetos do tipo PedidoVoucher
	 * @throws PersistenceException
	 */
	public static Collection findByStatus(Database db, Map param) throws PersistenceException {

		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			String status = (String) param.get("status");
			String tipoPeriodo = (String) param.get("tipoPeriodo");
			String periodo = (String) param.get("periodo");
			String dataInicial = (String) param.get("dataInicial");
			String dataFinal = (String) param.get("dataFinal");
			
			StringBuffer filtro = new StringBuffer();
			
			StringBuffer consulta = new StringBuffer("select a from ");
			consulta.append("br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher a where ");
			
			if(status != null && !"".equals(status))
			{				
				if (! status.equals("T") ) //caso não seja para selecionar todos os status
				{
					filtro.append("a.statusVoucher=\"" + status + "\"");
				}
			}
			
			if(tipoPeriodo != null && !"".equals(tipoPeriodo)) {
				
				if(tipoPeriodo.equals("P")) {
							
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}	
					
					Calendar c = Calendar.getInstance();
				    c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(periodo));
				    Date _dataInicial = c.getTime();
				    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				    
					filtro.append("a.dataPedido between to_date(\"").append(sdf.format(_dataInicial)).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(sdf.format(new Date())).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
				}
			}
			
			if(tipoPeriodo != null && !"".equals(tipoPeriodo)){
				
				if(tipoPeriodo.equals("D")) {
					
					if(filtro.length() > 0){
					   filtro.append(" and ");	
					}
					
					filtro.append("a.dataPedido between to_date(\"").append(dataInicial).append(" 00:00:00\",\"DD/MM/YYYY hh24:mi:ss\") and to_date(\"").append(dataFinal).append(" 23:59:59\",\"DD/MM/YYYY hh24:mi:ss\")");
				}
			}
			
			consulta.append(filtro);
			consulta.append(" order by a.dataPedido");
			
			query = db.getOQLQuery(consulta.toString());

			rs = query.execute();
			while(rs.hasMore())
			{
				PedidoVoucher pedido = (PedidoVoucher) rs.next();
				result.add(pedido);						
			}

			// Caso a opcao por status seja selecionada e este opcao for de pedidos SOLICITADOS
			// entao os dados para esta situacao nao se encontram ainda na tabela de pedidos de
			// vouchers e portanto o mapeamento utilizando nao conseguira retornar nenhuma linha
			// Para buscar tais informacoes outra tabela e utilizada a tabela de interface de
			// pedidos de voucher TBL_INT_VOUCHER_PEDIDO. Portanto um append aqui e inserido para
			// caso esta solicitacao seja feita entao as informacoes destes pedidos sao pesquisados
			// e retornados para complementarem a consulta. OBS se todos os pedidos forem desejados
			// entao tal metodo tambem e invocado.
			if ( status.equals("T") || status.equals(String.valueOf(Definicoes.STATUS_VOUCHER_GERACAO_SOLICITADA)) )
				result.addAll(buscaPedidosSolicitados(db));
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
	
	/**
	 * Obtém os pedidos de voucher de acordo com o número do pedido
	 * 
	 * @param db Conexão com o banco de dados
	 * @param numeroPedido O número do pedido
	 * @return Objeto do tipo PedidoVoucher
	 * @throws PersistenceException
	 */
	public static PedidoVoucher findByNumeroPedido(Database db, long numeroPedido) throws PersistenceException {

		OQLQuery query =null;
		PedidoVoucher temp = null;
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher a " 
				+ "where a.nroPedido = " + numeroPedido;
			
			query = db.getOQLQuery(consulta);

			rs = query.execute();
			temp = (PedidoVoucher) rs.next();
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
		return temp;
   }
	
	/**
	 * Obtém o histórico de pedidos de voucher de acordo com o número do pedido
	 * 
	 * @param db Conexão com o banco de dados
	 * @param numeroPedido O número do pedido
	 * @return Coleção de objetos do tipo HistoricoPedidosVoucher
	 * @throws PersistenceException
	 */
	public static Collection findHistoricoByNumeroPedido(Database db, long numeroPedido) throws PersistenceException {

		OQLQuery query =null;
		Collection result = new ArrayList();
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoPedidosVoucher a " 
				+ "where a.nroPedido = " + numeroPedido + "order by a.dataAtualizacaoStatus";
			
			query = db.getOQLQuery(consulta);

			rs = query.execute();
			while(rs.hasMore())
			{
				HistoricoPedidosVoucher histPedido = (HistoricoPedidosVoucher) rs.next();
				result.add(histPedido);
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
	
	/**
	 *  Atualiza os dados de pedido de voucher no banco de dados
	 *  @param pedidoVoucher Entidade de pedido de voucher com os atributos atualizados
	 *  @param request Requisição WEB com os dados para a Empresa
	 *  @param db Conexão com o Banco de Dados
	 *  @throws Exception Exceção lançada para possíveis erros de consulta dos dados.
	 */

	public static void setByRequest(PedidoVoucher pedidoVoucher, HttpServletRequest request, 
			Database db)throws Exception{	
		
	}
	
	/**
	 *  Atualiza os dados de histórico de pedidos de voucher no banco de dados
	 *  @param histPedidoVoucher Entidade de histórico de pedido de voucher com os atributos atualizados
	 *  @param request Requisição WEB com os dados para a Empresa
	 *  @param db Conexão com o Banco de Dados
	 *  @throws Exception Exceção lançada para possíveis erros de consulta dos dados.
	 */
	public static void setHistorico(HistoricoPedidosVoucher histPedidosVoucher, HttpServletRequest request, 
			Database db) throws Exception{
		
		db.create(histPedidosVoucher);
	}
	
	/**
	 * Retorna uma lista de pedidos de voucher existentes na tabela de interface na qual identifica
	 * os pedidos como SOLICITADOS. Este complemente e devido a mudanca no gerenciamento de vouchers
	 * na qual os pedidos SOLICITADOS nao mais ficam na tabela aonde o mapeamento do objeto indica.
	 * @param db Conexao com o banco de dados
	 * @return Collection - Lista de pedidos que esteja na situacao de SOLICITADOS
	 */
	public static Collection buscaPedidosSolicitados(Database db)
	{
		Collection result = new ArrayList();
		QueryResults rs = null;
		// Realiza a consulta na tabela de interface de pedidos de voucher realizando
		// no objeto de persistencia um CAST para a classe responsavel por armazenar
		// as informacoes de pedido.
		String oSQL =  "CALL SQL select num_pedido as nroPedido " +
								      ",dat_pedido as dataPedido " +
								      ",tip_cartao as tipoCartao " +
								      ",0 as nroOrdem " +
								      ",idt_status_pedido as statusPedido " +
								      ",0 as statusVoucher " +
									  ",0 as nroJobTecnomen " +
								      ",id_requisitante as requisitante " +
								  "from tbl_int_voucher_pedido " +
								 "where idt_status_pedido = '" + Definicoes.STATUS_PEDIDO_VOUCHER_SOLICITADO +"'"+
								 " AS br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher";

		OQLQuery query = null;
		try
		{
			// Executa a consulta e iterage entre os resultados adicionando na lista
			// de pedidos que sera o resultado do metodo
			query = db.getOQLQuery(oSQL);
			rs = query.execute();
			while(rs.hasMore())
			{
				PedidoVoucher pedido = (PedidoVoucher) rs.next();
				result.add(pedido);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs != null) rs.close();
			if (query != null)
				query.close();
		}
		return result;
	}
	
	/**
	 * Metodo....:findByStatusPedido
	 * Descricao.:Encontra pedido de voucher pelo status
	 * @param db			- Objeto de banco de dados
	 * @param statusPedido	- Status do pedido
	 * @param readOnly		- Flag indicando se a consulta eh readOnly
	 * @return
	 * @throws PersistenceException
	 */
	public static Collection findByStatusPedido(Database db, String statusPedido, boolean readOnly) throws PersistenceException
	{
		Collection lista = new ArrayList();
		OQLQuery query  = null;
		QueryResults rs = null;
		try
		{
			String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.PedidoVoucher a " 
							+ "where a.statusPedido = $1 order by a.nroPedido";
			
			query = db.getOQLQuery(consulta);
			query.bind(statusPedido);
			
			rs = query.execute(readOnly ? Database.ReadOnly : Database.Shared);
			while (rs.hasMore())
				lista.add((PedidoVoucher) rs.next());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs != null) rs.close();
			if(query!=null){
				query.close();
			}
		}
		return lista;
   }
}
