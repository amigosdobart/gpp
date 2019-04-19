package com.brt.gpp.aplicacoes.importacaoAssinantes.importacaoPospago;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchDelegate;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*
* Seleciona os clientes a importar para a tabela de 
* clientes pós-pagos que entraram ou saíram do plano
* fale grátis. 
* 
* @version	1.0
* @author	Marcelo Alves Araujo
* @since	22/11/2006
*
*/
public class ImportaPospagoProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	private int			numInclusao;
	private int			numExclusao;
	private String		statusProcesso;
	private PREPConexao conexaoBanco;
	private ResultSet	rsImporta;
	
	//	Filtra a primeira ocorrencia de cada registro nao processado na interface pospago
    //  e o considera apenas somente se o anterior (para o mesmo msisdn) tenha sido processado com sucesso 
	//  (ou nao exista anterior)
	private static final String SQL_IMPORTACAO	=    
	      "    SELECT 				    															               "
		 +"     y.idt_msisdn,                                                                                      "      
	     +"      y.dat_inclusao,                                                                                   "    		
	     +"      y.idt_processamento,                                                                              "         
	     +"      nvl(x.tppe_idt_promocao_espelho, nvl(y.idt_promocao,  ?)) as idt_promocao_espelho,                "          
	     +"      decode(x.tppe_idt_promocao_espelho, null, y.idt_acao, 'I') as idt_acao_espelho                    "         
	     +"    FROM                                                                                                "         
	     +"      tbl_pro_promocao_espelho x,                                                                       "         
	     +"      (SELECT                                                                                           "         
	     +"        a.idt_msisdn,                                                                                   "         
	     +"        a.dat_inclusao,                                                                                 "         
	     +"        a.idt_processamento,                                                                            "         
	     +"        a.idt_acao as idt_acao,                                                                         "         
	     +"        b.idt_promocao as idt_promocao,                                                                 "         
	     +"        c.idt_promocao as idt_promocao_assinante													       "
	     +"       FROM                                                                                             "         
	     +"        tbl_int_assinante_pospago a,                                                                    "         
	     +"        tbl_pro_promocao b,                                                                             "         
	     +"        tbl_apr_assinante_pospago c                                                                     "         
	     +"       WHERE                                                                                            "         
	     +"         a.idt_status_processamento = ?                                                                 "           
	     +"         AND a.idt_promocao_crm = b.idt_promocao_crm(+)                                                 "         
	     +"         AND a.idt_msisdn = c.idt_msisdn(+)                                                             "       
	     +"         AND NVL((select n.idt_status_processamento                                                     "
	     +"              from tbl_int_assinante_pospago n                                                          "
	     +"              where n.idt_processamento =                                                               "
	     +"                 (select max(m.idt_processamento) from tbl_int_assinante_pospago m                      "                                 
	     +"                  where m.idt_msisdn = a.idt_msisdn and m.idt_processamento < a.idt_processamento)      "
	     +"              ), ?) =  ?                                                                                "
	     +"       ) y                                                                                              "
	     +"    WHERE                                  							                                   " 
	     +"          x.tppe_idt_promocao_assinante(+) = y.idt_promocao_assinante                                   "         
	     +"      AND x.tppe_idt_acao(+) = y.idt_acao                                                               "         
	     +"      AND x.tppe_idt_promocao(+) = y.idt_promocao                                                       "         
	     +"    ORDER BY                                                                                            "         
	     +"      y.idt_processamento asc                                                                           ";
	
	private static final String SQL_TOTAL	=    
		  " SELECT 	count(1) as total		           "
		 +" FROM    tbl_int_assinante_pospago a        "
	 	 +" WHERE   a.idt_status_processamento = ?     "
	     +"         AND NVL((select n.idt_status_processamento                                                     "
	     +"              from tbl_int_assinante_pospago n                                                          "
	     +"              where n.idt_processamento =                                                               "
	     +"                 (select max(m.idt_processamento) from tbl_int_assinante_pospago m                      "                                 
	     +"                  where m.idt_msisdn = a.idt_msisdn and m.idt_processamento < a.idt_processamento)      "
	     +"              ), ?) =  ?                                                                                "
	;
	
	/**
	 * Construtor
	 * @param long - ID de log do processo
	 */
	public ImportaPospagoProdutor(long idLog) 
	{
		super(idLog, Definicoes.CL_IMPORTACAO_POSPAGO);
		numInclusao = 0;
		numExclusao = 0;
		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	}

	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_IMPORTACAO_POSPAGO;
	}

	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso() 
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return "Foram incluidos: " + numInclusao + " e excluidos: " + numExclusao;
        
        return "Erro na execucao";
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

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento() 
	{
		return null;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao() 
	{
		return conexaoBanco;
	}

    /**
     * Consulta os assinantes que expiram a franquia ou bônus na data de referência
     * @param String[] - Data de referência para consulta
     * @throws GPPInternalErrorException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws GPPInternalErrorException
	{
    	conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    	
    	Object[] paramImporta =	{ Integer.toString(Definicoes.PROMOCAO_FALE_GRATIS_POSPAGO)
    							, Definicoes.IND_LINHA_NAO_PROCESSADA
    							, Definicoes.IDT_PROC_OK
    							, Definicoes.IDT_PROC_OK};
	    
    	// O select abaixo considera apenas a primeira ocorrencia de cada msisdn.
    	// As demais ocorrencias so poderadao ser processadas apos a conclusao da anterior,
    	// pois para cada linha da interface devemos fazer um calculo de transicao de estado
    	// (espelhamento de promocao) e isso depende do estado (promocao) anterior.
    	// No final do processo batch existe uma verificacao se ha itens pendentes de processamento
    	// na tabela de interface. Caso existir, o processo batch eh reiniciado automaticamente.
    	// Observacao:
    	// Fazer o select de todas as linhas de uma unica vez e calcular as transicoes de estado
    	// via Java eh um processo muito mais lento.
		rsImporta = conexaoBanco.executaPreparedQuery(SQL_IMPORTACAO, paramImporta, super.getIdLog());
	}

    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws Exception
	{
    	ImportaPospagoVO importa = null;
    	
        if(rsImporta.next())
        {
        	importa = new ImportaPospagoVO();
        	
        	importa.setMsisdn(rsImporta.getString("idt_msisdn"));
        	importa.setIdtProcessamento(rsImporta.getLong("idt_processamento"));
        	importa.setDataInclusao(rsImporta.getTimestamp("dat_inclusao"));
        	importa.setAcao(rsImporta.getString("idt_acao_espelho"));
        	importa.setPromocao(rsImporta.getInt("idt_promocao_espelho"));
        	
        	if (Definicoes.INCLUSAO_POSPAGO.equals(importa.getAcao()))
        		numInclusao++;
        	else
        		numExclusao++;
        }
        
		return importa;
	}

    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
	{
    	if (conexaoBanco == null)
    		return;
    	
    	boolean reiniciar = false;
    	
    	limparHistorico();
    	rsImporta.close();
    	
    	Object[] paramTotal = {Definicoes.IND_LINHA_NAO_PROCESSADA
							 , Definicoes.IDT_PROC_OK
							 , Definicoes.IDT_PROC_OK};
    	ResultSet rs = conexaoBanco.executaPreparedQuery(SQL_TOTAL, paramTotal, super.getIdLog());
    	if (rs.next() && rs.getInt("total") > 0)
    		reiniciar = true;
    	rs.close();
    	
    	super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());
    	
    	if (reiniciar)
    	{
	    	ProcessoBatchDelegate delegate = new ProcessoBatchDelegate(super.logId);
			delegate.executaProcessoBatch(Definicoes.IND_IMPORTACAO_POSPAGO, null);
    	}
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException() 
	{
	}
    
    /**
     * Limpa o histórico de registros da tabela de interface
     * @throws GPPInternalErrorException 
     */
    public void limparHistorico() throws GPPInternalErrorException 
	{
    	String sqlApaga	= "DELETE FROM tbl_int_assinante_pospago " 
						+ "WHERE                                 " 
						+ " idt_status_processamento = ?         " 
						+ " AND dat_inclusao <= sysdate - ?      ";
				
    	MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
		Object[] paramApaga =	{ Definicoes.IDT_PROC_OK
								, map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
    	
    	conexaoBanco.executaPreparedUpdate(sqlApaga, paramApaga, super.getIdLog());
	}
}
