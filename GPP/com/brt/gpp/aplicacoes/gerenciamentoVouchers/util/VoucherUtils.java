package com.brt.gpp.aplicacoes.gerenciamentoVouchers.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 * Classe utilitária que possui métodos diversos utilizados 
 * no processo de criação de vouchers.
 * 
 * @author Gustavo Gusmao
 * @since 24/03/2006
 *
 */
public class VoucherUtils extends Aplicacoes
{
	public VoucherUtils(long aLogId)
	{
		super(aLogId, "AindaNaoSeiONome");
	}

	/**
	 * Metodo....:atualizaNumLotePedido
	 * Descricao.:Atualiza as informacoes de numeracao de lotes iniciais e finais para um item de pedido
	 * @param numOrdem		- Numero da ordem
	 * @param numItem		- Numero do item
	 * @param numLoteIni	- Numero inicial do lote
	 * @param numLoteFim	- Numero final do lote
	 * @throws GPPInternalErrorException
	 */
	public void atualizaNumLotePedido(long numOrdem, int numItem, long numLoteIni, long numLoteFim) throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlUpdate = "UPDATE TBL_REC_VOUCHER_PEDIDO_ITEM " +
			                      "SET NUM_CAIXA_LOTE_INICIAL = ?," +
								      "NUM_CAIXA_LOTE_FINAL = ? " +
							    "WHERE NUM_PEDIDO = (SELECT NUM_PEDIDO " +
								                      "FROM TBL_REC_VOUCHER_PEDIDO " +
													 "WHERE NUM_ORDEM = ?" +
												    ") " +
								  "AND NUM_ITEM = ?";
			Object param[] = {new Long(numLoteIni), new Long(numLoteFim), new Long(numOrdem), new Integer(numItem)};
			conexaoPrep.executaPreparedUpdate(sqlUpdate,param,super.getIdLog());
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.WARN,"atualizaNumLotePedido","Erro atualizando informacoes de lote da Ordem:"+numOrdem+" Item:"+numItem+" LoteIni:"+numLoteIni+" LoteFim:"+numLoteFim+".Erro"+e);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
	}
	
	/**
	 * Metodo....:getQtdeCartoes
	 * Descricao.:Retorna a quantidade de cartoes para um item de ordem
	 * OBS: Este metodo e executado devido a quantidade de cartoes do item no arquivo
	 *      nao correspondendo ao valor exato ja que o sistema GPP pode realizar um
	 *      split deste item quando esta quantidade nao for divisivel por 1000 (exigido pela Tecnomen)
	 * @param numOrdem	- Numero da ordem
	 * @param numItem	- Numero do item
	 * @return long		- Quantidade de cartoes
	 */
	public long getQtdeCartoes(long numOrdem, int numItem)
	{
		long qtdeCartoes = 0;
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlPesquisa = "SELECT QTD_CARTOES " +
			                       "FROM TBL_REC_VOUCHER_PEDIDO_ITEM " +
							      "WHERE NUM_PEDIDO = (SELECT NUM_PEDIDO " +
								                        "FROM TBL_REC_VOUCHER_PEDIDO " +
									  				   "WHERE NUM_ORDEM = ?" +
												      ") " +
								    "AND NUM_ITEM = ?";
			Object param[] = {new Long(numOrdem), new Integer(numItem)};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,param,super.getIdLog());
			if (rs.next())
				qtdeCartoes = rs.getLong("QTD_CARTOES");
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN,"getQtdeCartoes","Erro ao buscar quantidade de cartoes para o item "+numItem+" da ordem nro:"+numOrdem+". Erro"+e);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		return qtdeCartoes;
	}
	
	/**
	 * Metodo....:getUserIDRequisitante
	 * Descricao.:Realiza a busca do UserID a ser usado no utilitario PGP para
	 *            criptografar os arquivos de ordens de vouchers
	 * @param numOrdem	- Numero da ordem a ser criptografada
	 * @return String	- UserID do requisitante
	 */
	public String getUserIDRequisitante(long numOrdem)
	{
		super.log(Definicoes.DEBUG, "getUserIDRequisitante", "Inicio NRO-ORDEM "+numOrdem);
		PREPConexao conexaoPrep = null;
		String userID = "Nao Disponivel";
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlConsulta =  "SELECT IDT_USERID_PGP " +
									"FROM TBL_REC_REQMAIL        M, " +
										 "TBL_REC_VOUCHER_PEDIDO P  " +
								   "WHERE M.ID_REQUISITANTE = P.ID_REQUISITANTE " +
									 "AND P.NUM_ORDEM       = ? ";
									 
			Object param[] = {new Long(numOrdem)};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlConsulta,param,super.getIdLog());
			if (rs.next())
				userID = rs.getString("IDT_USERID_PGP");
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"getUserIDRequisitante","Erro ao executar a consulta de busca do UserID Requisitante.. Erro:"+e);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "getUserIDRequisitante", "Fim");
		return userID;
	}
	
	/**
	 * Metodo....:gravaDadosArquivoOrdem
	 * Descricao.:Grava informacoes no arquivo abrindo-o no diretorio de ordens e fechando em seguida
	 * @param nomeArquivo	- Nome do arquivo a qual a informacao sera gravada
	 * @param buff			- Buffer de dados a serem gravados
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public boolean gravaDadosArquivoOrdem(String nomeArquivo,byte[] buff) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gravaDadosArquivoOrdem", "Inicio ARQUIVO  "+nomeArquivo);
		boolean gravou=true;
		MapConfiguracaoGPP mapConfig = MapConfiguracaoGPP.getInstancia();
		String dirName = mapConfig.getMapValorConfiguracaoGPP(Definicoes.DIR_IMPORTACAO_ORDENS_VOUCHER);
		try
		{
			/* Cria o arquivo no diretorio configurado para as ordens de vouchers */
			File ordem = new File(dirName+System.getProperty("file.separator")+nomeArquivo);
			FileOutputStream fileStream = new FileOutputStream(ordem);

			/* Se conseguiu criar o stream, entao agora grava
			 * as informacoes que estao em buffer
			 */
			if (fileStream != null)
			{
				fileStream.write(buff);
				fileStream.close();
			}
			else gravou=false;
		}
		catch(IOException e)
		{
			super.log(Definicoes.WARN,"gravaDadosArquivoOrdem","Erro ao gravar arquivo:"+nomeArquivo);
			gravou=false;
			throw new GPPInternalErrorException(e.getMessage());
		}
		super.log(Definicoes.DEBUG, "gravaDadosArquivoOrdem", "Fim GRAVOU="+gravou);
		return gravou;
	}
}