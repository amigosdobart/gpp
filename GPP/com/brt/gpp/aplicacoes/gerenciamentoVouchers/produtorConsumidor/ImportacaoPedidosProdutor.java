package com.brt.gpp.aplicacoes.gerenciamentoVouchers.produtorConsumidor;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedidoItem;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

public class ImportacaoPedidosProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private PREPConexao conexaoPrep;
	private List listaPedidos;
	private long contador;
	private String status;
	
	public ImportacaoPedidosProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_IMPORTACAO_PEDIDOS_VOUCHER);
	}

	public int getIdProcessoBatch()
	{
		return Definicoes.IND_IMP_PEDIDO_VOUCHER;
	}

	public String getDescricaoProcesso()
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return "Processo de importacao de pedidos de vouchers executado com sucesso";
	    else
	        return "Erro no processo de importacao de pedidos de vouchers";
	}

	public String getStatusProcesso()
	{
		return status;
	}

	public void setStatusProcesso(String status)
	{
		this.status = status;
	}

	public String getDataProcessamento()
	{
		return null;
	}

	public PREPConexao getConexao()
	{
		return null;
	}

	public void startup(String[] params) throws Exception
	{
		listaPedidos = new ArrayList();
		ResultSet rSet = null;
		super.log(Definicoes.INFO, "Produtor.startup", "Inicio do processo de Importacao de Pedidos de Vouchers");
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlPedidos = "SELECT p.num_pedido        AS num_pedido " +
									",p.dat_pedido   	    AS dat_pedido " +
									",p.dat_criacao     	AS dat_criacao " +
									",p.tip_cartao        	AS tip_cartao " +
									",p.id_requisitante   	AS id_requisitante " +
									",p.idt_status_pedido 	AS idt_status_pedido " +
									",i.num_item          	AS num_item " +
									",i.qtd_cartoes       	AS qtd_cartoes " +
									",i.idt_estampa       	AS idt_estampa " +
									",i.vlr_face          	AS vlr_face " +
									"FROM tbl_int_voucher_pedido p "+
									",tbl_int_voucher_pedido_item i " +
									"WHERE i.num_pedido = p.num_pedido " +
									"AND p.idt_status_pedido = ? ";
			Object parametros[] = {Definicoes.STATUS_PEDIDO_VOUCHER_SOLICITADO};
			rSet = conexaoPrep.executaPreparedQuery(sqlPedidos, parametros, super.getIdLog());
			
			popularColecao(rSet);
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "Produtor.startup", "Erro ao importar pedidos de voucher: " + e);
			throw(e);
		}
		finally
		{
			if(rSet != null)
				rSet.close();
		}
	}

	public Object next() throws Exception
	{
		if(listaPedidos.size() > 0)
		{
			contador++;
			return(listaPedidos.remove(0));
		}
		else
			return(null);
	}

	public void finish() throws Exception
	{
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		super.log(Definicoes.INFO, "Produtor.finish", "Fim do processo de Importacao de Pedidos de Vouchers");
	}

	public void handleException()
	{
		
	}
	
	/**
	 * Metodo....: popularColecao
	 * Descricao.: Metodo que utiliza um ResultSet para gerar uma coleção
	 * de objetos VoucherPedido, com seus respectivos itens.
	 * @param rSet 
	 * @throws Exception
	 */
	private void popularColecao(ResultSet rSet) throws Exception
	{
		// Busca a referencia para o mapeamento dos valores de recarga
		// O campo da tabela VLR_FACE indica o ID contendo os valores
		// para multiplos saldos
		MapValoresRecarga valRec = MapValoresRecarga.getInstancia();
		
		// Define uma data de expiracao default a ser utilizada
		Calendar dtExpDefault = Calendar.getInstance();
		dtExpDefault.add(Calendar.DAY_OF_MONTH, 1);
		
		long numPedidoAnterior = 0;
		VoucherPedido pedido = null;
		if(rSet.next())
		{
			while(true)
			{
				long numPedido = rSet.getLong("num_pedido");
				if(numPedido != numPedidoAnterior) // É o 1o item do pedido
				{
					pedido = new VoucherPedido(rSet.getLong("num_pedido"));
					listaPedidos.add(pedido);
					pedido.setDataCriacao	((java.util.Date)	rSet.getTimestamp("dat_criacao")	);
					pedido.setDataPedido 	((java.util.Date)	rSet.getTimestamp("dat_pedido")		);
					pedido.setTipCartao  	(					rSet.getString("tip_cartao")		);
					pedido.setStatusPedido	(					rSet.getString("idt_status_pedido")	);
					pedido.setIdRequisitante(					rSet.getString("id_requisitante")	);
				}
				
				// Busca os dados de multiplos saldos para o valor de face correspondente
				ValoresRecarga rec = valRec.getValoresRecarga(rSet.getLong("vlr_face"), Calendar.getInstance().getTime());
				Date dtExp 		= rec.getNumDiasExpiracaoPrincipal() != 0 ? GPPData.getDataAcrescidaDias(rec.getNumDiasExpiracaoPrincipal()) : dtExpDefault.getTime();
				Date dtExpBon 	= rec.getNumDiasExpiracaoBonus() 	 != 0 ? GPPData.getDataAcrescidaDias(rec.getNumDiasExpiracaoBonus()) 	 : dtExpDefault.getTime();
				Date dtExpSm 	= rec.getNumDiasExpiracaoSMS() 		 != 0 ? GPPData.getDataAcrescidaDias(rec.getNumDiasExpiracaoSMS()) 		 : dtExpDefault.getTime();
				Date dtExpDados	= rec.getNumDiasExpiracaoGPRS() 	 != 0 ? GPPData.getDataAcrescidaDias(rec.getNumDiasExpiracaoGPRS()) 	 : dtExpDefault.getTime();

				// Define a quantidade maxima de cartoes em uma caixa e verifica se ha sub-itens a serem criados
				long qtdCartoes = rSet.getLong("qtd_cartoes");
				long perBoxQty  = qtdCartoes;
				long qtdItem 	= qtdCartoes;
				long qtdSubItem = 0;
				if (qtdCartoes >= Definicoes.NUMERO_BATCHES_POR_CAIXA)
				{
					perBoxQty = Definicoes.NUMERO_BATCHES_POR_CAIXA;
					qtdItem   = (qtdCartoes / Definicoes.NUMERO_BATCHES_POR_CAIXA) * Definicoes.NUMERO_BATCHES_POR_CAIXA;
					qtdSubItem = qtdCartoes % qtdItem;	
				}

				VoucherPedidoItem item = new VoucherPedidoItem(pedido,rSet.getInt("num_item"));
				item.setQtdeCartoes			(qtdItem											);
				item.setQtdePorBatch    	(Definicoes.NUMERO_VOUCHERS_POR_BATCH				);
				item.setQtdeBatchPorCaixa	(perBoxQty											);
				item.setEstampa				(rSet.getString("idt_estampa")						);
				item.setValorFace			(rec.getSaldoPrincipal()							);
				item.setValorFaceBonus		(rec.getSaldoBonus()								);
				item.setValorFaceSm			((rec.getSaldoSMS()	+ rec.getValorBonusSMS() )		);
				item.setValorFaceDados		((rec.getSaldoGPRS()	+ rec.getValorBonusGPRS())	);
				item.setExpiracao			(dtExp												);
				item.setExpiracaoBonus		(dtExpBon											);
				item.setExpiracaoSm			(dtExpSm											);
				item.setExpiracaoDados		(dtExpDados											);

				// Se a quantidade for maior que 1000 porem nao seja divisivel por 1000 a plataforma
				// nao deixa que esta quantidade seja criada, portanto o sistema ira realizar um split
				// deste item sendo que o primeiro item sera criado com a quantidade mais proxima do
				// divisivel por 1000 e o restante sera criado como um sub-item (novo item da ordem)
				// porem somente para a plataforma, para o sistema continua como um unico item.
				if (qtdSubItem > 0)
				{
					VoucherPedidoItem subItem = new VoucherPedidoItem(pedido,rSet.getInt("num_item"));
					subItem.setQtdeCartoes			(qtdSubItem												);
					subItem.setQtdePorBatch    		(Definicoes.NUMERO_VOUCHERS_POR_BATCH					);
					subItem.setQtdeBatchPorCaixa	(qtdSubItem												);
					subItem.setEstampa				(rSet.getString("idt_estampa")							);
					subItem.setValorFace			((long)rec.getSaldoPrincipal()							);
					subItem.setValorFaceBonus		((long)rec.getSaldoBonus()								);
					subItem.setValorFaceSm			((long)(rec.getSaldoSMS()	+ rec.getValorBonusSMS() )	);
					subItem.setValorFaceDados		((long)(rec.getSaldoGPRS()	+ rec.getValorBonusGPRS())	);
					subItem.setExpiracao			(dtExp													);
					subItem.setExpiracaoBonus		(dtExpBon												);
					subItem.setExpiracaoSm			(dtExpSm												);
					subItem.setExpiracaoDados		(dtExpDados												);
					// Define este sub-item para o item sendo processado
					item.setSubItem(subItem);
				}
				pedido.addVoucherItem(item);
				
				numPedidoAnterior = numPedido;
				if(!rSet.next())
					break;
			}
		}
	}
}