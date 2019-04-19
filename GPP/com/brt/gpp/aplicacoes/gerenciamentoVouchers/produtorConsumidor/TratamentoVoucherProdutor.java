package com.brt.gpp.aplicacoes.gerenciamentoVouchers.produtorConsumidor;

import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.SolicitacaoAtivacao;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.util.ManipuladorXMLVoucher;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

public class TratamentoVoucherProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	private ResultSet rSet;
	private long contador;
	private String status;
	private PREPConexao conexaoPrep;
	
	public TratamentoVoucherProdutor(long logId)
	{
		super(logId, Definicoes.CL_ATIVACAO_VOUCHER_FISICO);
	}

	public int getIdProcessoBatch()
	{
		return Definicoes.IND_SOLICITACAO_VOUCHER;
	}

	public String getDescricaoProcesso()
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return "Processo de tratamento de vouchers executado com sucesso";
	    else
	        return "Erro no processo de tratamento de vouchers";
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
		super.log(Definicoes.INFO, "Produtor.startup", "Inicio do processo de Tratamento de Vouchers");
		try
		{
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlPedidos = "SELECT ID_PROCESSAMENTO, XML_DOCUMENT " +
			                      "FROM TBL_INT_PPP_IN " +
								 "WHERE IDT_STATUS_PROCESSAMENTO = ? " +
								   "AND IDT_EVENTO_NEGOCIO = ? " +
								"ORDER BY DAT_CADASTRO";
			Object parametros[] = {Definicoes.IDT_PROCESSAMENTO_NOT_OK, Definicoes.IDT_EVT_NEGOCIO_VOUCHER};
			rSet = conexaoPrep.executaPreparedQuery(sqlPedidos, parametros, super.getIdLog());
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.WARN, "Produtor.startup", "Erro ao ativar vouchers fisicos. Erro:"+e);
			throw(e); 
		}
	}

	public Object next() throws Exception
	{
		SolicitacaoAtivacao solicitacao = null;
		if(rSet.next())
		{
			solicitacao = ManipuladorXMLVoucher.getDadosSolicitacao(super.getIdLog(), 
					rSet.getLong("ID_PROCESSAMENTO"), rSet.getClob("XML_DOCUMENT"));
			contador++;
		}
		return solicitacao;
	}

	public void finish() throws Exception
	{
		if(rSet != null)
			rSet.close();
		super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		super.log(Definicoes.INFO, "Produtor.finish", "Fim do processo de Tratamento de Vouchers");
	}

	public void handleException()
	{
		
	}
}
