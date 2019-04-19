package com.brt.gpp.aplicacoes.concederCreditoTerceiro;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*
* Este arquivo refere-se a classe ConcessaoCreditoTerceiroProdutor, 
* responsável pela consulta dos supervisores e promotores aptos a
* receber créditos mensalmente
*
* @version				1.0
* @author				Marcelo Alves Araujo
* @since	 			10/07/2006
*
*/
public class ConcessaoCreditoTerceiroProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	private PREPConexao conexaoBanco;
	private ResultSet 	rsRecarga;
	private String		statusProcesso;
		
	/**
	 * Construtor
	 * @param long - ID de log do processo
	 */
	public ConcessaoCreditoTerceiroProdutor(long aLogId) 
	{
		super(aLogId, Definicoes.CL_CONCESSAO_CREDITO_TERCEIRO);
		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	}

	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_CONCESSAO_TERCEIRO;
	}

	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso() 
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return "Foram processados todos os promotores e supervisores";
        
        return "Erro durante o processo";
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
     * Consulta a lista de terceiros a receber os créditos
     * @param String[] - Não há parâmetros
     * @throws GPPInternalErrorException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
    public void startup(String[] params) throws GPPInternalErrorException
	{
    	conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    	
    	// Consulta todos os terceiros que devem receber os créditos
		String sqlRecarga	= "SELECT idt_msisdn AS msisdn        "
							+ "      ,idt_plano_terceiro AS plano "
							+ "  FROM tbl_apr_plano_terceiro      ";
	
    	rsRecarga = conexaoBanco.executaPreparedQuery(sqlRecarga, null, super.getIdLog());		
	}

    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws SQLException
	{
    	ConcessaoCreditoTerceiroVO concessao = null;
        
    	if(rsRecarga.next())
        	concessao = new ConcessaoCreditoTerceiroVO(rsRecarga.getString("msisdn"),rsRecarga.getString("plano"));
        
		return concessao;
	}

    /**
     * @throws SQLException 
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws SQLException 
	{
		rsRecarga.close();
    	super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());		
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException() 
	{
    	setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
	}
}