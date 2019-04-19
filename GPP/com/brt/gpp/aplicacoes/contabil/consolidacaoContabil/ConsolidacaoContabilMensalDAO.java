package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Classe responsavel por todos os acessos ao banco de dados do GPP para a Consolidacao Contabil Mensal.
 * 
 * @author Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 * Criado em 23/11/2007
 */
public class ConsolidacaoContabilMensalDAO
{
	private PREPConexao conexao;
	private long idProcesso;
    
	/**
	 * Contrutor
	 * @param conexao
	 * @param idProcesso
	 */
	public ConsolidacaoContabilMensalDAO(PREPConexao conexao, long idProcesso)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
	}
	
/*************************************************************************************************************/
    
    private static final String SQL_INSERT_SELECT =
    	" INSERT INTO TBL_CON_CONTABIL                                                " + // Nao somar o indice de bonificacao
    	" (           IDT_CODIGO_NACIONAL, VLR_TOTAL, VLR_DURACAO,                    " +
    	"             VLR_TOTAL_SI, IDT_ROAMING_ORIGIN, IDT_CODIGO_SERVICO_SFA,       " +
    	"             VLR_INDICE_BONIFICACAO, QTD_REGISTRO, DAT_PROCESSAMENTO,        " +
    	"             IDT_PERIODO_CONTABIL, IDT_TIPO_SERVICO, IDT_PLANO_PRECO,        " +
    	"             IDT_TIPO_SALDO, ID_CHAVE1, ID_CHAVE2, ID_CHAVE3,                " +
    	"             IDT_EOT_ORIGEM, IDT_EOT_DESTINO                                 " +
    	"            ,IND_VLR_TOTAL_ZERO                                              " +
    	"            ,IND_VLR_DURACAO_ZERO                                            " +
    	"            ,IND_TIPO_SALDO_SP                                               " +
    	" )                                                                           " +
    	" (                                                                           " +
    	"   SELECT IDT_CODIGO_NACIONAL, VLR_TOTAL, VLR_DURACAO,                       " +
    	"          VLR_TOTAL_SI, IDT_ROAMING_ORIGIN, IDT_CODIGO_SERVICO_SFA,          " +
    	"          VLR_INDICE_BONIFICACAO, QTD_REGISTRO, SYSDATE,                     " +
    	"          IDT_PERIODO_CONTABIL, IDT_TIPO_SERVICO, IDT_PLANO_PRECO,           " +
    	"          IDT_TIPO_SALDO, ID_CHAVE1, ID_CHAVE2, ID_CHAVE3,                   " +
    	"          IDT_EOT_ORIGEM, IDT_EOT_DESTINO                                    " +
    	"         ,DECODE(VLR_TOTAL,        0,1,0) AS IND_VLR_TOTAL_ZERO              " +
    	"         ,DECODE(VLR_DURACAO,      0,1,0) AS IND_VLR_DURACAO_ZERO            " +
    	"         ,DECODE(IDT_TIPO_SALDO,'SP',1,0) AS IND_TIPO_SALDO_SP               " +
    	"   FROM                                                                      " +
    	"   (                                                                         " +
    	"     SELECT   A.IDT_CODIGO_NACIONAL, SUM(A.VLR_TOTAL) AS VLR_TOTAL,          " +
    	"              SUM(A.VLR_DURACAO) AS VLR_DURACAO,                             " +
    	"              SUM(A.VLR_TOTAL_SI) AS VLR_TOTAL_SI, A.IDT_ROAMING_ORIGIN,     " +
    	"              A.IDT_CODIGO_SERVICO_SFA, A.VLR_INDICE_BONIFICACAO,            " +
    	"              SUM(A.QTD_REGISTRO) AS QTD_REGISTRO, SYSDATE,                  " +
    	"              A.IDT_PERIODO_CONTABIL, A.IDT_TIPO_SERVICO, A.IDT_PLANO_PRECO, " +
    	"              A.IDT_TIPO_SALDO, A.ID_CHAVE1, A.ID_CHAVE2, A.ID_CHAVE3,       " +
    	"              A.IDT_EOT_ORIGEM, A.IDT_EOT_DESTINO                            " +
    	"     FROM     TBL_CON_CONTABIL_DIA A                                         " +
    	"     GROUP BY A.IDT_CODIGO_NACIONAL, A.IDT_ROAMING_ORIGIN,                   " +
    	"              A.IDT_CODIGO_SERVICO_SFA, A.IDT_PERIODO_CONTABIL,              " +
    	"              A.IDT_TIPO_SALDO, A.IDT_TIPO_SERVICO, A.IDT_PLANO_PRECO,       " +
    	"              A.ID_CHAVE1, A.ID_CHAVE2, A.ID_CHAVE3,                         " +
    	"              A.IDT_EOT_ORIGEM,A.IDT_EOT_DESTINO,A.VLR_INDICE_BONIFICACAO    " +
    	"    )                                                                        " +
    	" )                                                                           " ;
    
    /**
     * Realiza a sumarizacao da CON_CONTABIL_DIA para a CON_CONTABIL
     * 
     * @param vo nstancia de ConsolidacaoContabilDiariaVO
     * @return Retorno do SQl
     * @throws GPPInternalErrorException
     */
    public int consolidarPeriodoContabil(ConsolidacaoContabilDiariaVO vo) throws GPPInternalErrorException
    {
        return this.conexao.executaPreparedUpdate(SQL_INSERT_SELECT, new Object[] {}, this.idProcesso);
    }
    
	/***
	 * Determina  o valor referente à perda de CDRs
     * 
     * @param vo nstancia de ConsolidacaoContabilDiariaVO
	 * @throws GPPInternalErrorException
	 */
	public void incluiPerdaCDRs( ConsolidacaoContabilDiariaVO vo) throws GPPInternalErrorException, SQLException
	{
        String periodoContabil = vo.getPeriodoContabil();
        
        SimpleDateFormat sdfPeriodoContabil = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat sdfPeriodoContabilParticao = new SimpleDateFormat("yyyyMM");
         
        String periodoContabilParticao = null;
        try
        {
            periodoContabilParticao = sdfPeriodoContabilParticao.format(
                    sdfPeriodoContabil.parse(periodoContabil)); 
        }
        catch (Exception e)
        {
            throw new GPPInternalErrorException("Periodo contabil invalido!");
        }
		
		String sqlPerdaCDRs = 
			" SELECT IDT_CODIGO_NACIONAL, IDT_PRE_HIBRIDO, IDT_TIPO_SALDO,                     " +
			"        VLR_PERDAS_CDR, VLR_INDICE_BONIFICACAO_GSM                                " +
			"   FROM TBL_CON_INDICE_BONIFICACAO PARTITION(PCIB" + periodoContabilParticao + ") " +
			"  WHERE VLR_PERDAS_CDR <> 0                                                       ";
		
		ResultSet rsPerdaCdrs = this.conexao.executaPreparedQuery(sqlPerdaCDRs, null, this.idProcesso);
		
		while(rsPerdaCdrs.next())
		{
			String idtCodigoNacional = rsPerdaCdrs.getString("idt_codigo_nacional");
			String idtPreHibrido     = rsPerdaCdrs.getString("idt_pre_hibrido");
			String idtTipoSaldo      = rsPerdaCdrs.getString("idt_tipo_saldo");
			double vlrPerdaCdrs      = rsPerdaCdrs.getDouble("vlr_perdas_cdr");
			double indiceBonifGSM    = rsPerdaCdrs.getDouble("vlr_indice_bonificacao_gsm");
			
			// Se a perda de CDRs for negativa <=> está faltando cdr's de consumo
			if(vlrPerdaCdrs < 0)
			{
				// Insere parcela de CBG referente a perda de cdr's de chamada na tbl_con_contabil
				this.inserePerdaTblContabil(idtTipoSaldo, -vlrPerdaCdrs*indiceBonifGSM, idtCodigoNacional, idtPreHibrido, "CBG", indiceBonifGSM,vo); 
				
				// Insere parcela de CRG referente a perda de cdr's de chamada na tbl_con_contabil
				this.inserePerdaTblContabil(idtTipoSaldo, -vlrPerdaCdrs*(1-indiceBonifGSM), idtCodigoNacional, idtPreHibrido, "CRG", indiceBonifGSM,vo); 
			}
			// Se a perda de CDRs for positiva <=> está faltando cdr's de recarga/bonus
			else
			{
				if(vlrPerdaCdrs > 0)
				{
					// Insere perda de recargas/bonus na tbl_con_contabil
					this.inserePerdaTblContabil(idtTipoSaldo, vlrPerdaCdrs, idtCodigoNacional, idtPreHibrido, "BO", 0,vo);
				}
			}
		}			

	}
    
    /**
     * Insere registros de perda de cdrs na tbl_con_contabil
     * 
     * @param tipoSaldo
     * @param perdaCDR
     * @param cn
     * @param indPH
     * @param tipoServicoSfa
     * @param indiceBonifGSM
     * @param vo
     * @throws GPPInternalErrorException
     * @throws SQLException
     */
	private void inserePerdaTblContabil(String tipoSaldo, double perdaCDR, String cn, String indPH,
			String tipoServicoSfa, double indiceBonifGSM, ConsolidacaoContabilDiariaVO vo
			) throws GPPInternalErrorException, SQLException
	{
		 SimpleDateFormat sdfDataReferencia          = new SimpleDateFormat("dd/MM/yyyy");
		 SimpleDateFormat sdfPeriodoContabil         = new SimpleDateFormat("MM/yyyy");
		 SimpleDateFormat sdfPeriodoContabilParticao = new SimpleDateFormat("yyyyMM");

		 String dataReferencia          = sdfDataReferencia.format(vo.getDataAProcessar());
		 String periodoContabil         = vo.getPeriodoContabil();
		 String periodoContabilParticao = null;
		 try{
			 periodoContabilParticao = sdfPeriodoContabilParticao.format(
					 sdfPeriodoContabil.parse(vo.getPeriodoContabil())); 
		 }
		 catch (Exception e)
		 {
			 throw new GPPInternalErrorException("Periodo contabil invalido!");
		 }
	 	double tax = 0;
        
	 	// Determinar valor com imposto (bruto)
	 	String sqlTax = 
	 		" SELECT VLR_ALIQUOTA FROM TBL_GER_ALIQUOTA               " +
			"  WHERE IDT_CODIGO_NACIONAL = ?                          " + //Parametro 0 (cn)
			"    AND idt_imposto = ?                                  " + // Parametro 1 (imposto - ICMS)
			"    AND dat_inicial_validade <=  to_date(?,'DD/MM/YYYY') " + //Parametro 2 -  Data final Periodo
			"    AND (dat_final_validade >= add_months(to_date('27'||substr(?,4,7),'DD/MM/YYYY'),-1)+2  or dat_final_validade is null) "; //Parametro 3 - Data Inicial Período
	 	
	 	Object[] parTax = {cn, Definicoes.IMPOSTO_TLDC, dataReferencia, dataReferencia };
	 	
	 	ResultSet rsTax = this.conexao.executaPreparedQuery(sqlTax, parTax, this.idProcesso);
	 
	 	if (rsTax.next())
	 		tax = rsTax.getDouble("VLR_ALIQUOTA");

	 	String sqlInsert =
	 		" INSERT INTO TBL_CON_CONTABIL PARTITION(PCC" + periodoContabilParticao + ")" +
	 		" (IDT_CODIGO_NACIONAL, VLR_TOTAL, VLR_DURACAO, VLR_TOTAL_SI, IDT_ROAMING_ORIGIN,                                       " +
	 		"  IDT_CODIGO_SERVICO_SFA, VLR_INDICE_BONIFICACAO, QTD_REGISTRO, DAT_PROCESSAMENTO, IDT_PERIODO_CONTABIL,               " +
	 		"  IDT_TIPO_SERVICO, IDT_PLANO_PRECO, IDT_TIPO_SALDO, ID_CHAVE1, ID_CHAVE2, ID_CHAVE3, IDT_EOT_ORIGEM, IDT_EOT_DESTINO) " +
			" VALUES                                                                                                                " +
	 		"  (?, ?, 0, ?, ?, ?, ?, 0, sysdate, ?, ?, ?, ?, '-', '-', '-',?,?)                                                     ";
        
	 	Object[] pars = {	cn,
	 						new Double(perdaCDR),
							new Double(perdaCDR*(1-tax)),
							cn,
							this.getCodigoSfaPerdaCdrs(indPH, tipoServicoSfa),
							new Double(indiceBonifGSM),
							periodoContabil,
							tipoServicoSfa,
							indPH.equals("H")?"4":"1",
							tipoSaldo,
							cn,
							cn
	 					};
	 	
	 	this.conexao.executaPreparedUpdate(sqlInsert, pars, this.idProcesso);
	 }
	 
	/***
	 * Elimina Registros com valores zerados (deixando apenas o do saldo principal,
	 * (para a informação de "existência" do evento gratuito não se perder)
     * 
	 * @param vo   Instancia de ConsolidacaoContabilDiariaVO
	 * @throws GPPInternalErrorException
	 */
	 public void eliminaZeros(ConsolidacaoContabilDiariaVO vo) throws GPPInternalErrorException
	 {
		 SimpleDateFormat sdfPeriodoContabil         = new SimpleDateFormat("MM/yyyy");
		 SimpleDateFormat sdfPeriodoContabilParticao = new SimpleDateFormat("yyyyMM");

		 String periodoContabilParticao = null;
		 try{
			 periodoContabilParticao = sdfPeriodoContabilParticao.format(
					 sdfPeriodoContabil.parse(vo.getPeriodoContabil())); 
		 }
		 catch (Exception e)
		 {
			 throw new GPPInternalErrorException("Periodo contabil invalido! Falha em eliminarZeros");
		 }
        
	 	String sqlZeros = 
	 		"delete  /*+ index(c TCCO_I2)*/ tbl_con_contabil  " +
	 		" PARTITION(PCC" + periodoContabilParticao + ") c " +
			" where IND_VLR_TOTAL_ZERO = 1                    " +
			"   and IND_TIPO_SALDO_SP  = 0                    "; 

		this.conexao.executaPreparedUpdate(sqlZeros, null, this.idProcesso);
		
	 	String sqlZeros1 	=
	 		"DELETE tbl_con_contabil  PARTITION(PCC" +periodoContabilParticao+") c " +
			" WHERE c.IND_VLR_TOTAL_ZERO   = 1                                     " +
			"   AND c.IND_TIPO_SALDO_SP    = 1                                     " +
			"   AND c.IND_VLR_DURACAO_ZERO = 1                                     " +
			"   AND EXISTS (                                                       " +
			"          SELECT 1                                                    " + 
			"            FROM tbl_con_contabil PARTITION(PCC"+periodoContabilParticao+") ci " +
			"           WHERE ci.IND_TIPO_SALDO_SP = 0                             " +
			"             AND ci.qtd_registro = c.qtd_registro                     " +
			"             AND ci.idt_codigo_servico_sfa = c.idt_codigo_servico_sfa " +
			"             AND ci.id_chave1 = c.id_chave1                           " +
			"             AND ci.id_chave2 = c.id_chave2                           " +
			"             AND ci.id_chave3 = c.id_chave3                           " +
			"             AND ci.idt_codigo_nacional = c.idt_codigo_nacional       " +
			"             AND ci.idt_roaming_origin  = c.idt_roaming_origin        " +
			"             AND ci.idt_plano_preco     = c.idt_plano_preco           " +
			"             AND ci.idt_eot_origem      = c.idt_eot_origem            " +
			"             AND ci.idt_eot_destino     = c.idt_eot_destino           " +
			"             AND ci.idt_tipo_servico    = c.idt_tipo_servico          " +
			"             AND ci.IND_VLR_TOTAL_ZERO   = 0)                         "; 

	 	this.conexao.executaPreparedUpdate(sqlZeros1, null, this.idProcesso);
	 }

	 /***
	  * Retorna o código de serviço associado ao tipo de serviço/plano preço para a perda de Cdrs
      * 
	  * @param indPH			P: pré-pago normal; H: Híbrido
	  * @param tipoServicoSfa	Tipo de Serviço (BO/CRG/CBG)
	  * @return	Codigo de Servico
	  */
	 private String getCodigoSfaPerdaCdrs(String indPH, String tipoServicoSfa)
	 {
	 	String retorno = null;
	 	
	 	// Se tipo de serviço for Consumo de Bônus GSM
	 	if(tipoServicoSfa.equals("CBG"))
	 	{
	 		if(indPH.equals("P"))
	 			retorno = "71688";
	 		else
	 			retorno = "82276";
	 	}
	 	
	 	// Se tipo de serviço for consumo de Recarga GSM
	 	if(tipoServicoSfa.equals("CRG"))
	 	{
	 		if(indPH.equals("P"))
	 			retorno = "71687";
	 		else
	 			retorno = "82275";
	 	}
	 	
	 	// Se tipo de Serviço for Bonus
	 	if(tipoServicoSfa.equals("BO"))
	 	{
	 		if(indPH.equals("P"))
	 			retorno = "71686";
	 		else
	 			retorno = "82274";
	 	}
	 	
	 	return retorno;
	 }
}
