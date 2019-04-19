package com.brt.gpp.aplicacoes.importacaoAssinantes.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.entidade.AssinantePosPago;

/**
 * Interface de acesso a tabela TBL_APR_ASSINANTE_POSPAGO.
 * 
 * @author Jorge Abreu
 * Criado em: 31/10/2007
 */
public class AssinantePosPagoDAO extends Aplicacoes
{
	public AssinantePosPagoDAO(long logId) 
	{
		super(logId, "AssinantePosPagoDAO");
	}
	
	//SQL de atualizacao da tabela TBL_APR_ASSINANTE_POSPAGO
	private String sqlUpdate = 
		"update tbl_apr_assinante_pospago set dat_inclusao = sysdate , "+ 
        									" idt_promocao = ? , "+
        									" idt_amigos_gratis = ? "+
        									" where idt_msisdn = ? ";
	
	//SQL de insercao da tabela TBL_APR_ASSINANTE_POSPAGO
	private String sqlInsert = 
		"insert into tbl_apr_assinante_pospago(idt_msisdn, "+
											 " dat_inclusao, "+
											 " idt_promocao, "+
											 " idt_amigos_gratis, "+
											 " ind_ativo) "+
											 " values( ?, sysdate, ?, ?, ? ) ";
	
	//SQL de consulta da tabela TBL_APR_ASSINANTE_POSPAGO
	private String sqlConsulta = 
		"select idt_msisdn as MSISDN,        "+
			  " dat_inclusao as DAT_INCLUSAO,"+
			  " idt_promocao as PROMOCAO,    "+
			  " idt_amigos_gratis as AMIGOS, "+
			  " ind_ativo as IND_ATIVO       "+
	      "from tbl_apr_assinante_pospago    "+
	     "where idt_msisdn = ?               ";
	
	/**
	 * Descricao: Metodo para atualizar os dados de amigos para falar de graca
	 * @param MSISDN - Assinante pospago para o qual se deseja atualizar os amigos
	 * @param amigo1 - Msisdn do amigo 1 que será atualizado
	 * @param amigo2 - Msisdn do amigo 2 que será atualizado
	 * @param operador - Operador que esta executando a atualizacao
	 * @param codigoServico - codigo de Servico que esta vinculado a essa promocao
	 * @throws Exception 
	 */
	public void atualizarAmigosDeGraca(PREPConexao conexao, AssinantePosPago assinantePosPago) throws Exception 
	{  	
		Object[] parametrosUpdate =
		{
			new Integer(assinantePosPago.getPromocao().getIdtPromocao())
			,assinantePosPago.getListaAmigos()
			,assinantePosPago.getMsisdn()
		};
		
		//Realiza a atualizacao dos dados na tabela para o assinante MSISDN
		conexao.executaPreparedUpdate(sqlUpdate, parametrosUpdate, super.logId);
	}
	
	/**
	 * Descricao: Metodo para inserir os dados de amigos para falar de graca
	 * @param MSISDN - Assinante pospago para o qual se deseja atualizar os amigos
	 * @param amigo1 - Msisdn do amigo 1 que será atualizado
	 * @param amigo2 - Msisdn do amigo 2 que será atualizado
	 * @param operador - Operador que esta executando a atualizacao
	 * @param codigoServico - codigo de Servico que esta vinculado a essa promocao
	 * @throws Exception 
	 */
	public void inserirAmigosDeGraca(PREPConexao conexao, AssinantePosPago assinantePosPago) throws Exception 
	{  	
			Object[] parametrosInsert =
			{
				assinantePosPago.getMsisdn()
				,new Integer(assinantePosPago.getPromocao().getIdtPromocao())
				,assinantePosPago.getListaAmigos()
				,assinantePosPago.isAtivo() ? new Integer(1) : new Integer(0)
			};
			
			//Realiza a insercao dos dados do assinante MSISDN caso nao tenha encontrado nenhum registro do assinante.
			conexao.executaPreparedUpdate(sqlInsert, parametrosInsert, super.logId);
	}
	
	/**
	 * Metodo para consultar os dados de um assinante msisdn e retornar um AssinantePosPago
	 * @param conexao
	 * @param msisdn
	 * @return
	 * @throws GPPInternalErrorException 
	 * @throws SQLException 
	 */
	public AssinantePosPago consultarAssinante(PREPConexao conexao, String msisdn) throws GPPInternalErrorException, SQLException
	{
		AssinantePosPago assinante = null;
		
		Object[] param = {msisdn};
		
		ResultSet result = conexao.executaPreparedQuery(sqlConsulta, param, super.logId);
		
		if(result.next())
		{	
			assinante = new AssinantePosPago();
			assinante.setMsisdn(result.getString("MSISDN"));
			assinante.setDataInclusao(result.getDate("DAT_INCLUSAO"));
			assinante.setListaAmigos(result.getString("AMIGOS"));
			assinante.setAtivo(result.getInt("IND_ATIVO") == 1 ? true : false);
			assinante.setPromocao(MapPromocao.getInstancia().getPromocao(result.getInt("PROMOCAO")));
		}
		
		return assinante;
	}

}
	