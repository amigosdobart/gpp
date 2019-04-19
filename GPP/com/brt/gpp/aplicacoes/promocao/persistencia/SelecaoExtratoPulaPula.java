package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.entidade.Detalhe;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapDescontoPulaPula;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pela consulta e obtencao dos detalhes do extrato Pula-Pula.
 *
 *	@version	1.0
 *	@author		Daniel Ferreira
 *	@since		13/09/2005
 *	@modify		Primeira versao.
 *
 *	@version	2.0
 *	@author		Daniel Ferreira
 *	@since		28/03/2008
 *	@modify		Adaptacao para a oferta de dia das maes.
 */
public class SelecaoExtratoPulaPula
{

    /**
     *	Statement SQL para consulta pelas ligacoes recebidas pelo assinante que geram bonus Pula-Pula.
     */
    private static final String SQL_EXTRATO =
        "SELECT /*+ index(c xpktbl_ger_cdr)*/ " +
        "       c.call_id, " +
        "       c.timestamp, " +
        "       c.call_duration, " +
        "       c.ff_discount, " +
        "       r.des_operacao " +
        "  FROM tbl_ger_cdr c," +
        "       tbl_ger_rating r, " +
        "       tbl_pro_transaction t, " +
        "       tbl_pro_rate_name n, " +
        "       tbl_pro_desconto_pula_pula d " +
        " WHERE c.transaction_type = t.transaction_type " +
        "   AND c.tip_chamada = r.rate_name " +
        "   AND c.tip_chamada = n.rate_name " +
        "   AND c.ff_discount = d.id_desconto " +
        "   AND c.sub_id = ? " +
        "   AND c.timestamp >= to_date(?,'dd/mm/yyyy') " +
        "   AND c.timestamp <  to_date(?,'dd/mm/yyyy') " +
        "   AND t.idt_promocao = ? " +
        "   AND n.idt_promocao = ? " +
        "   AND (d.ind_disponivel_extrato <> 0 " +
        "    OR  1 = ?) " +
        " ORDER BY c.timestamp "; 
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    /**
     *	Result set de registros de ligacoes recebidas.
     */
    private ResultSet registros;
    
    /**
     *	Construtor da classe.
     *
     *	@param		conexaoPrep				Conexao com o banco de dados.
     */
    public SelecaoExtratoPulaPula(PREPConexao conexaoPrep)
    {
    	this.conexaoPrep	= conexaoPrep;
    	this.registros		= null;
    }
    
    /**
     *	Executa a consulta no banco de dados pelas ligacoes recebidas pelo assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		idtPromocao				Identificador da promocao do assinante.
     *	@param		dataIni					Data de inicio de analise.
     *	@param		dataFim					Data de fim de analise.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@throws		GPPInternalErrorException
     */
	public void execute(String	msisdn, 
						int		idtPromocao, 
						Date	dataIni, 
						Date	dataFim, 
						boolean	isConsultaCheia) throws GPPInternalErrorException
	{
		//Ajustando a data final de analise para que tenha um dia a mais (vide SQL).
        Calendar calFim = Calendar.getInstance();
        calFim.setTime(dataFim);
        calFim.add(Calendar.DAY_OF_MONTH, 1);
        dataFim = calFim.getTime();
        
        SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
        
        Object[] parametros =
        {
        	msisdn,
        	conversorDate.format(dataIni),
        	conversorDate.format(dataFim),
        	new Integer(idtPromocao),
        	new Integer(idtPromocao),
        	isConsultaCheia ? new Integer(1) : new Integer(0)
        };
    	        
        this.registros = this.conexaoPrep.executaPreparedQuery(SelecaoExtratoPulaPula.SQL_EXTRATO, 
        													   parametros, 
        													   this.conexaoPrep.getIdProcesso());
	}
    
    /**
     *	Busca a proxima ligacao no resultado da consulta.
     *
     *	@return		Informacoes da ligacao recebida.
     *	@throws		SQLException
     */
	public Detalhe next() throws SQLException
	{
        if((this.registros != null) && (this.registros.next()))
        {
        	Detalhe result = new Detalhe();

        	result.setIndEvento(false);
        	result.setOriginador(this.registros.getString("call_id"));
        	result.setTimestamp(this.registros.getTimestamp("timestamp"));
        	result.setDescricao(this.registros.getString("des_operacao"));
        	result.setDuracao(this.registros.getLong("call_duration"));
        	result.setDesconto(MapDescontoPulaPula.getInstance().getDescontoPulaPula(this.registros.getShort("ff_discount")));
        	
        	return result;
        }
        
	    return null;
	}
    
    /**
     *	Fecha a consulta.
     *
     *	@throws		SQLException
     */
	public void close() throws SQLException
	{
        if(this.registros != null)
            this.registros.close();
	}
    
}
