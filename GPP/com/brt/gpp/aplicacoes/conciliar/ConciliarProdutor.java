package com.brt.gpp.aplicacoes.conciliar;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
*
* Essa classe refere-se ao processo de envio de informações de recargas feitas via banco
* ou pagas com cartão de crédito para o Sistema de Conciliação (MCR) 
* 
* @Autor: 			Denys Oliveira
* Data: 				30/03/2004
*
* Modificado por:  Geraldo Palmeira
* Data:			   28/11/2005
* Razao:		   Essa classe foi adaptada ao modelo Produtor Consumidor.
*
*/
public class ConciliarProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	// Parametros necessarios para o processo batch
	private int			numRecargas;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private String      dataProcessamento;
	
	// Recuros utilizados no produtor
	private ResultSet 	rs;
	private PREPConexao	conexaoPrep;
	
	/**
	 * @param aLogId
	 * @param aNomeClasse
	 */
	public ConciliarProdutor(long logId) 
	{
		super(logId, Definicoes.CL_ENVIO_REC_CONCILIACAO);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_CONCILIACAO;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso() 
	{
	    if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
			return numRecargas + " Recargas disponibilizadas em TBL_INT_CONCILIACAO_OUT. Param:"+dataProcessamento;
	    else
	        return "Erro no Processo.";
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso() 
	{
		return statusProcesso;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status) 
	{
		statusProcesso = status;
	}
	
	public void incrementaRecarga()
	{
		numRecargas++;
	}

	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - data de processamento no formato dd/mm/aaaa
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public void parseParametros(String params[]) throws Exception
	{
		if (params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor:"+params[0]);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception 
	{
		parseParametros(params);
		
		// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
		MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
		
		// Determina o Range de Pesquisa
		String dataInicialRange = dataProcessamento +" "+ Definicoes.HORA_INICIO_DIA;  // "00:00:00"
		String dataFinalRange = dataProcessamento +" "+ Definicoes.HORA_FINAL_DIA;	// "23:59:59"
		
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		//Query que remove da tabela TBL_INT_CONCILIACAO_OUT todas as linhas já
		//enviadas para o Sistema de Conciliação
		String queryClean = " DELETE FROM TBL_INT_CONCILIACAO_OUT WHERE IDT_STATUS_PROCESSAMENTO = ? " +
							" and DAT_TRANSACAO < (sysdate - ? ) ";
		
		Object param0[] = {Definicoes.IND_LINHA_TRANSFERIDA,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};		

		super.log(Definicoes.DEBUG,"Produtor.start",conexaoPrep.executaPreparedUpdate(queryClean,param0,super.getIdLog())+" recargas removidas TBL_INT_CONCILIACAO_OUT");

		String	queryRecargas =
			" SELECT TRIM (id_recarga)    AS idt_nsu                                          " +
			"        ,idt_nsu_instituicao                                                     " +
			"        ,TRIM (idt_terminal) AS idt_terminal                                     " +
			"        ,tip_transacao                                                           " +
			"        ,nom_operador                                                            " +
			"        ,R.id_sistema_origem                                                     " +
			"        ,dat_origem                                                              " +
			"        ,NVL(R.vlr_credito_principal,0) 										  " +
			"       + NVL(R.vlr_credito_bonus,0) 											  " +
			"       + NVL(r.vlr_credito_periodico,0) AS vlr_pago 							  " +
			"        ,idt_msisdn                                                              " +
			"        ,c.nom_empresa AS id_empresa                      						  " + 
			"   FROM tbl_rec_recargas         R                                               " +
			"        ,tbl_ger_plano_preco     PP                                              " +
			" 		 ,tbl_ger_categoria       C												  " +
			"        ,tbl_con_recarga_servico RS                                              " +
			"  WHERE r.idt_plano_preco      = pp.idt_plano_preco                              " +
			"    AND c.idt_categoria        = pp.idt_categoria								  " +
			"    AND (TRIM(rs.idt_tipo_servico) = ? OR 										  " +
			"		 (rs.idt_tipo_servico is null and r.id_tipo_recarga = 'R'))           	  " +
			"    AND rs.id_canal                           (+) = r.id_canal                   " +
			"    AND rs.id_origem                          (+) = r.id_origem                  " +
			"    AND rs.idt_plano_preco                    (+) = nvl(r.idt_plano_preco,0)     " +
			"    AND rs.id_sistema_origem                  (+) = r.id_sistema_origem          " +
			"    AND dat_inclusao          >= to_date( ? ,'dd/mm/yyyy hh24:mi:ss')            " +
			"    AND dat_inclusao          <= to_date( ? ,'dd/mm/yyyy hh24:mi:ss')            " +
			" UNION                                                                           " +
			" SELECT TRIM (id_recarga)    AS idt_nsu                                          " +
			"        ,idt_nsu_instituicao                                                     " +
			"        ,TRIM (idt_terminal) AS idt_terminal                                     " +
			"        ,tip_transacao                                                           " +
			"        ,nom_operador                                                            " +
			"        ,R.id_sistema_origem                                                     " +
			"        ,dat_origem                                                              " +
			"        ,NVL(R.vlr_credito_principal,0) + NVL(R.vlr_credito_bonus,0) AS vlr_pago " +
			"        ,idt_msisdn                                                              " +
			"        ,?              AS id_empresa                                            " +
			"   FROM tbl_rec_recargas_tfpp    R                                               " +
			"        ,tbl_con_recarga_servico RS                                              " +
			"  WHERE (TRIM(rs.idt_tipo_servico) = ? OR rs.idt_tipo_servico is null)           " +
			"    AND rs.id_canal                           (+) = r.id_canal                   " +
			"    AND rs.id_origem                          (+) = r.id_origem                  " +
			"    AND rs.idt_plano_preco                    (+) = nvl(r.idt_plano_preco,0)     " +
			"    AND rs.id_sistema_origem                  (+) = r.id_sistema_origem          " +
			"    AND dat_inclusao          >= to_date( ? ,'dd/mm/yyyy hh24:mi:ss')            " +
			"    AND dat_inclusao          <= to_date( ? ,'dd/mm/yyyy hh24:mi:ss')            " ;
		Object param1[] = {Definicoes.TIPO_SERVICO_RECARGA,
						   dataInicialRange,
						   dataFinalRange,
						   Definicoes.IDT_EMPRESA_RECARGAS_TFPP,
						   Definicoes.TIPO_SERVICO_RECARGA,
						   dataInicialRange,
						   dataFinalRange};
		rs = conexaoPrep.executaPreparedQuery(queryRecargas, param1, super.getIdLog());
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		ConciliarVO vo = null;
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
			if (rs.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				vo = new ConciliarVO();
				vo.setIdRecarga(rs.getString("idt_nsu"));
				vo.setIdNsuInstituicao(rs.getString("idt_nsu_instituicao"));
				vo.setIdtTerminal(rs.getString("idt_terminal"));
				vo.setTipoTransacao(rs.getString("tip_transacao"));
				vo.setNomeOperador(rs.getString("nom_operador"));
				vo.setIdSistemaOrigem(rs.getString("id_sistema_origem"));
				vo.setDataRecarga(rs.getTimestamp("dat_origem"));
				vo.setVlrPago(rs.getDouble("vlr_pago"));
				vo.setIdtMsisdn(rs.getString("idt_msisdn"));
				vo.setIdEmpresa(rs.getString("id_empresa"));
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro"+se);
			throw new GPPInternalErrorException(se.toString());
		}
		return vo;
	}
	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception 
	{
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());	
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException() 
	{

	}
	
	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return dataProcessamento;
	}

    /* (non-Javadoc)
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }

}
