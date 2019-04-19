package com.brt.gpp.aplicacoes.contestar.dao;

import java.sql.ResultSet;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.contestar.entidade.ProtocoloNativo;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 * Classe responsavel por realizar as consultas de contestacao no GPP
 * 
 * @author João Paulo Galvagni
 * @since  07/03/2007
 */
public class ContestacaoDAO extends Aplicacoes
{
	/**
	 * Construtor da Classe
	 * 
	 * @param 		idProcesso		Identificador do processo
	 */
	public ContestacaoDAO(long idProcesso)
	{
		super(idProcesso, "ContestacaoDAO");
	}
	
	/**
	 * Metodo....: findStatusBSById
	 * Descricao.: Consulta o status do BS atraves do ID (protocolo)
	 * 
	 * @param  conexaoPrep		- Instancia de conexao com o BD
	 * @param  protocolo		- Numero do protocolo (ID)
	 * @return protocoloNativo	- Objeto contendo o resultado da consulta
	 */
	public ProtocoloNativo findStatusBSById(PREPConexao conexaoPrep, String protocolo)
	{
		// Cria uma nova instancia do objeto ProtocoloNativo para 
		// conter o resultado da consulta
		ProtocoloNativo protocoloNativo = new ProtocoloNativo();
		protocoloNativo.setProtocoloNativo(protocolo);
		
		try
		{
			// SQL de consulta
			String sqlConsultaStatusBS = "SELECT a.idt_nu_bs AS BS, a.dat_fechamento AS DATA_FECHAMENTO, " +
			 							 "	     DECODE(a.idt_status_analise, " +
			 							 "				'A' ,'Aberto', " +
			 							 "	   		    'P' ,'Procedente', " +
			 							 "			    'PP','Parcialmente procedente', " +
			 							 "			  	'I' ,'Improcedente') AS STATUS " +
			 							 "  FROM tbl_ger_contestacao a " +
			 							 " WHERE a.idt_nu_bs = ? ";
			
			// Seta o parametro
			Object params[] = {(protocolo)};
			
			// Realizacao da consulta
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlConsultaStatusBS, params, super.getIdLog());
			
			if (rs.next())
			{
				protocoloNativo.setFechado(rs.getString("DATA_FECHAMENTO"));
				protocoloNativo.setStatus(rs.getString("STATUS"));
			}
			else
				protocoloNativo.setMsgErro("Protocolo " + protocolo + " nao encontrado.");
			
			// Fecha o ResultSet
			rs.close();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"findStatusBSById","Erro do GPP: "+e);
		}
		
		// Retorna o objeto populado
		return protocoloNativo;
	}
}