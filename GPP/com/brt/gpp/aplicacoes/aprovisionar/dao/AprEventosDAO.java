package com.brt.gpp.aplicacoes.aprovisionar.dao;

import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.entidade.AssinantePosPago;

/**
 * Interface de acesso a tabela TBL_APR_EVENTOS.
 * 
 * @author Jorge Abreu
 * Criado em: 31/10/2007
 */
public class AprEventosDAO extends Aplicacoes
{
	public AprEventosDAO(long logId) 
	{
		super(logId, "AprEventosDAO");
	}
	
	//SQL de insercao da tabela TBL_APR_ASSINANTE_POSPAGO
	private String sqlInsert = 
		" INSERT INTO tbl_apr_eventos (DAT_APROVISIONAMENTO, "+
									 " IDT_MSISDN, "+
									 " TIP_OPERACAO, "+
									 " IDT_IMSI, "+
									 " IDT_PLANO_PRECO, "+
									 " VLR_CREDITO_INICIAL, "+
									 " IDT_IDIOMA, "+
									 " IDT_ANTIGO_CAMPO, "+
									 " IDT_NOVO_CAMPO, "+
									 " IDT_TARIFA, "+
									 " DES_LISTA_FF, "+
									 " IDT_MOTIVO, "+
									 " NOM_OPERADOR, "+
									 " DES_STATUS, "+
									 " COD_RETORNO) "+
									 " VALUES "+
									 " (sysdate, ?, ?, ?, ?, ?, ?, substr(?,1,30), substr(?,1,30), ?, ?, ?, ?, ?, ?) ";

	
	/**
	 * Descricao: Metodo para registrar o evento de atualizacao dos amigos FGN 4
	 * @param MSISDN - Assinante pospago para o qual se realizou a atualizacao dos amigos
	 * @param tipoOperacao - Tipo da operacao que foi realizada (Ex.: ATUALIZACAO_AMIGOS)
	 * @param nomeOperador - Nome do Operador que realizou a atualizacao 
	 * @param codRet - codigo de retorno da atualizacao
	 */
	public void registraAtualizacaoAmigosDeGraca(PREPConexao conexao, AssinantePosPago assinanteNovo, AssinantePosPago assinanteAntigo, String nomeOperador, short codRet) throws GPPInternalErrorException, SQLException
	{
		String msisdn = assinanteNovo.getMsisdn();
		
		try
		{	
			Object[] parametrosInsert =
			{
				msisdn
				,Definicoes.TIPO_APR_ATUALIZA_AMIGOS_GRATIS
				,null ,null ,null ,null 
				,(assinanteAntigo == null) ? null : assinanteAntigo.getListaAmigos() 
				,assinanteNovo.getListaAmigos()
				,null ,null ,null
				,(nomeOperador == null || nomeOperador.equals("")) ? Definicoes.GPP_OPERADOR : nomeOperador                                  
				,(codRet == Definicoes.RET_OPERACAO_OK) ? Definicoes.TIPO_OPER_SUCESSO : Definicoes.TIPO_OPER_ERRO
				,new Integer(codRet)	
			};
		
			conexao.executaPreparedUpdate(sqlInsert, parametrosInsert, super.logId);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "registraAtulizacaoAmigosDeGraca", "Erro ao registrar o evento de atualizacao dos amigos do assinante "+msisdn+" na TBL_APR_EVENTOS.");
		}
	}
}
	