package com.brt.gpp.aplicacoes.contestar.dao;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

public class PublicacaoBSDAO extends Aplicacoes
{
	/**
	 * Construtor da Classe
	 * 
	 * @param 		idProcesso		Identificador do processo
	 */
	public PublicacaoBSDAO(long idProcesso)
	{
		super(idProcesso, "PublicacaoBSDAO");
	}
	
	/**
	 * Metodo....: getNumeroAssinante
	 * Descricao.: Consulta o numero do assinante a partir do BS
	 * 
	 * @param  conexaoPrep - Instancia do Banco de Dados
	 * @param  numeroBS	   - Numero do BS
	 * @return msisdn	   - Numero do Assinnante
	 *//*
	public String getNumeroAssinante(PREPConexao conexaoPrep, String numeroBS)
	{
		String msisdn = null;
		
		try
		{
			// SQL de consulta
			String sqlConsultaAssinante = "SELECT idt_msisdn " +
										  "  FROM tbl_ger_contestacao " +
										  " WHERE idt_nu_bs = ? ";
			
			// Seta os parametros para o insert
			Object params[] = { numeroBS };
			
			// Realizacao da consulta
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlConsultaAssinante, params, super.getIdLog());
			
			// Seleciona o resultado
			if (rs.next())
				msisdn = rs.getString("idt_msisdn");
			
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"inserePublicacao","Erro do GPP: "+e);
		}
		
		// Retorna o MSISDN do assinante
		return msisdn;
	}*/
	
	/**
	 * Metodo....: inserePublicacao
	 * Descricao.: Insere as informacoes para publicar o BS
	 * 
	 * @param  conexaoPrep	- Instancia do Banco de Dados
	 * @param  processo		- Proccesso
	 * @param  xml			- XML de publicacao
	 */
	public void inserePublicacao(PREPConexao conexaoPrep, String processo, String xml)
	{
		try
		{
			// SQL de insercao
			String sqlInsertInterfaceVitriaOut = "INSERT INTO interface_vitria_out	a				" +
												 "            (a.sequencial,  						" +
												 "			   a.processo,  						" +
												 "			   a.id_sistema, 						" +
												 "			   a.data_inclusao,						" +
												 "             a.data_leitura, 						" +
												 "			   a.nr_prioridade, 					" +
												 "			   a.xml, 								" +
												 "			   a.controle_vitria)					" +
												 "     VALUES (SEQ_INT_VITRIA_OUT.NEXTVAL,			 " +
												 "			   ?, 									" +
												 "			   ?, 									" +
												 "			   SYSDATE,								" +
												 "             NULL, 								" +
												 "			   0, 									" +
												 "			   ?, 									" +
												 "			   ?)									" ;
			
			// Seta os parametros para o insert
			Object params[] = {processo, Definicoes.SO_GPP, xml, Definicoes.IND_LINHA_NAO_PROCESSADA};
			
			// Realizacao da consulta
			conexaoPrep.executaPreparedUpdate(sqlInsertInterfaceVitriaOut, params, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"inserePublicacao","Erro do GPP: "+e);
		}
	}
}