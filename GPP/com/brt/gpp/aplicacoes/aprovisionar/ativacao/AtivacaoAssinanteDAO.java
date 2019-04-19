/**
 * 
 */
package com.brt.gpp.aplicacoes.aprovisionar.ativacao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * @author Magno Batista Corrêa
 *
 */
public class AtivacaoAssinanteDAO {

	private PREPConexao conexaoPrep;
	private long idProcesso;

	/**
	 * 
	 */
	public AtivacaoAssinanteDAO(PREPConexao conexaoPrep, long idProcesso) {
		this.conexaoPrep = conexaoPrep;
		this.idProcesso = idProcesso;
	}
	
	/**
	 * Grava o evento de ativacao
	 * @param gPPAtivacaoAssinante
	 * @param retornoOperacao
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public boolean gravarDadosAtivacao(GPPAtivacaoAssinante gPPAtivacaoAssinante,short retornoOperacao) throws GPPInternalErrorException {
		Assinante assinante = gPPAtivacaoAssinante.getAssinante();
		double creditoInicial = 0.0;
		//Soma todos os creditos como credito inicial
		for (Iterator iterator = assinante.getSaldo().iterator(); iterator.hasNext();)
			creditoInicial += ((SaldoAssinante) iterator.next()).getCreditos();

		String msisdn = assinante.getMSISDN();
		String imsi   = assinante.getIMSI();
		String planoPreco = String.valueOf(assinante.getPlanoPreco());
		short idioma = assinante.getIdioma();
		String operador = gPPAtivacaoAssinante.getOperador();
		String status = String.valueOf(assinante.getStatusAssinante());
		return this.gravaDadosAtivacao(msisdn, imsi, planoPreco, creditoInicial, idioma, operador, status, retornoOperacao);
	}

	/**
	 * Grava as informações pertinentes ao assinante hibrido
	 * @param gPPAtivacaoAssinante
	 * @param dataAtivacao
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public boolean gravarHibrido(GPPAtivacaoAssinante gPPAtivacaoAssinante, Date dataAtivacao) throws GPPInternalErrorException {
		Assinante assinante   = gPPAtivacaoAssinante.getAssinante();
		String msisdn         = assinante.getMSISDN();
		double creditoInicial = assinante.getCreditosPrincipal();
		double saldoAtual     = assinante.getCreditosPrincipal();

		return this.gravaHibrido(msisdn, creditoInicial, saldoAtual, dataAtivacao); 
	}
	
    /** 
     * Metodo...: gravaDadosAtivacao
     * Descricao: Grava dados em tabela referente a ativacao
     * @param   String  msisdn         - Numero do MSISDN ativado
     * @param   String  imsi           - Numero do SimCard (IMSI)
     * @param   String  planoPreco     - Plano do preco a ser ativado
     * @param   double  creditoInicial - Credito Inicial do assinante
     * @param   short   idioma         - Idioma do assinante
     * @param   String  operador       - Operador que realizou a ativacao
     * @param   String  status         - Status da Operacao
     * @param   short   retornoOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     * @throws GPPInternalErrorException 
     */
    public boolean gravaDadosAtivacao (String msisdn, String imsi, String planoPreco, double creditoInicial, short idioma, String operador, String status, short retornoOperacao) throws GPPInternalErrorException
    {
        boolean retorno = false;        
        String comandoSQL = "" 
          	+ "INSERT INTO TBL_APR_EVENTOS                                      "
          	+ "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_IMSI,       "
           	+ " IDT_PLANO_PRECO, VLR_CREDITO_INICIAL, IDT_IDIOMA, NOM_OPERADOR, "
           	+ " DES_STATUS, COD_RETORNO)                                        "
            + "VALUES (?,?,?,?,?,?,?,?,?,?)                                     ";
            
        Object params[] = {new Timestamp(new Date().getTime()), msisdn, Definicoes.TIPO_APR_ATIVACAO, imsi,
                               planoPreco, new Double(creditoInicial), new Integer(idioma), operador, status, 
                               new Integer(retornoOperacao)};
                                
        if (this.conexaoPrep.executaPreparedUpdate(comandoSQL, params, idProcesso) > 0)
        	retorno = true;
        return retorno;
    }
    
    /** 
     * Metodo...: gravaHibrido
     * Descricao: Grava dados em tabela referente a ativacao de hibrido
     * @param       String  msisdn         - Numero do MSISDN ativado
     * @param       double  creditoInicial - Credito Inicial do assinante
     * @param       double  saldoAtual      - Saldo atual do cliente
     * @param       Date    dataAtivacao    - Data de ativação
     * @return      boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     * @throws GPPInternalErrorException 
     */
    public boolean gravaHibrido (String msisdn, double creditoInicial, double saldoAtual, Date dataAtivacao) throws GPPInternalErrorException
    {
        String comandoSQL   = 
        		"INSERT INTO tbl_apr_plano_hibrido                                   "
              + "(idt_msisdn, vlr_cred_fatura, vlr_saldo_inicial, dat_ciclo,         "
              + " num_mes_execucao, dat_ultima_recarga_processada, dat_ativacao_gpp, "
              + " dat_ativacao_geneva, num_contrato, ind_novo_controle)              "
              + "values                                                              "
              + "(?,?,?,null,0,sysdate,trunc(?),null,null,1)                         ";
            
        Object params[] = { msisdn, new Double(creditoInicial),
                            new Double(saldoAtual), new Timestamp(dataAtivacao.getTime())};
        return (this.conexaoPrep.executaPreparedUpdate(comandoSQL, params, idProcesso) > 0);
    }
}
