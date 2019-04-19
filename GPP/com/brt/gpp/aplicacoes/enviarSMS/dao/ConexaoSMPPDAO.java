package com.brt.gpp.aplicacoes.enviarSMS.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.ConexaoSMPPConf;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
/**
 * A Classe <code>ConexaoSMPPDAO</code> e utilizada para manipular dados na tabela
 * <code>TBL_GER_CONEXAO_SMPP</code>.
 * Contem medotods para inserir, alterar e buscar dados na tabela.<br>
 * Muito utilizada pelo GPP no <i>pool</i> de Conexoes SMPP.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 20/12/2007
 */
public class ConexaoSMPPDAO
{
	/**
	 * Conexao com Banco de Dados
	 */
	private PREPConexao conexao;

	/**
	 * Utilizado pelo pool de conexoes abrir obter uma
	 * conexao com o banco de dados.
	 */
	private long idProcesso;

	/**
	 * Instancia da classe <code>ConexaoSMPPDAO</code> com uma conexao dada.
	 *
	 * @param idProcesso Numero utilizado para executar operacoes no banco de dados e no
	 *                   pool de conexoes.
	 * @param conexao Conexao a ser utilizada pelo DAO
	 */
	public ConexaoSMPPDAO(long idProcesso, PREPConexao conexao)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
	}
	/**
	 * Seleciona uma unica Conexao na tabela baseado no campo
	 * <code>TGCS_ID_CONEXAO</code>.
	 *
	 * @param  idConexao id da conexao a ser pesquisada.
	 *
	 * @return Entidade <code>ConexaoSMPPConf</code> preenchida.<br>
	 *         <code>null</code> caso ocorra algum erro ou a busca nao retorne nada.

	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 * @throws SQLException Caso ocorra algum erro ao percorrer o <code>ResultSet</code>
	 */
	public ConexaoSMPPConf buscarConexaoPorId(int idConexao) throws GPPInternalErrorException, SQLException
	{
		ConexaoSMPPConf conexaoSMPPConf = null;
		String sql =
			"SELECT TGCS_ID_CONEXAO,     "+
			"       TGCS_IP_MAQUINA,     "+
			"       TGCS_PORTA,          "+
			"       TGCS_USUARIO,        "+
			"       TGCS_SENHA,          "+
			"       TGCS_TIPO_SISTEMA    "+
			"FROM TBL_GER_CONEXAO_SMPP   "+
			"WHERE TGCS_ID_CONEXAO = ?   ";

		Object[] params =
		{
			new Integer(idConexao),
		};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				if(registros.next())
				{
					conexaoSMPPConf = new ConexaoSMPPConf();
					conexaoSMPPConf.setIdConexao(registros.getInt("TGCS_ID_CONEXAO"));
					conexaoSMPPConf.setIpMaquina(registros.getString("TGCS_IP_MAQUINA"));
					conexaoSMPPConf.setPorta(registros.getInt("TGCS_PORTA"));
					conexaoSMPPConf.setUsuario(registros.getString("TGCS_USUARIO"));
					conexaoSMPPConf.setSenha(registros.getString("TGCS_SENHA"));
					conexaoSMPPConf.setTipoSistema(registros.getString("TGCS_TIPO_SISTEMA"));
				}
			}
			finally
			{
				registros.close();
			}
		}

		return conexaoSMPPConf;
	}
	/**
	 * Lista todas as conexoes SMPP registradas no banco de dados.
	 *
	 * @return <code>List</code> de entidades <code>ConexaoSMPPConf</code> preenchida.<br>
	 * 		   <code>null</code> caso ocorra algum erro ou
	 * 		   <code>List</code> vazia caso busca nao retorne nada.
	 */
	public List listarConexoes() throws GPPInternalErrorException, SQLException
	{
		List lista = null;
		String sql =
			"SELECT TGCS_ID_CONEXAO,     "+
			"       TGCS_IP_MAQUINA,     "+
			"       TGCS_PORTA,          "+
			"       TGCS_USUARIO,        "+
			"       TGCS_SENHA,          "+
			"       TGCS_TIPO_SISTEMA    "+
			"FROM TBL_GER_CONEXAO_SMPP   ";

		Object[] params = {	};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				lista = new ArrayList();
				while(registros.next())
				{
					ConexaoSMPPConf conexaoSMPPConf = new ConexaoSMPPConf();
					conexaoSMPPConf.setIdConexao(registros.getInt("TGCS_ID_CONEXAO"));
					conexaoSMPPConf.setIpMaquina(registros.getString("TGCS_IP_MAQUINA"));
					conexaoSMPPConf.setPorta(registros.getInt("TGCS_PORTA"));
					conexaoSMPPConf.setUsuario(registros.getString("TGCS_USUARIO"));
					conexaoSMPPConf.setSenha(registros.getString("TGCS_SENHA"));
					conexaoSMPPConf.setTipoSistema(registros.getString("TGCS_TIPO_SISTEMA"));

					lista.add(conexaoSMPPConf);
				}
			}
			finally
			{
				registros.close();
			}
		}
		return lista;
	}
	/**
	 * Retorna o objeto PREPConexao utilizado pelo DAO
	 *
	 * @return Objeto PREPConexao
	 */
	public PREPConexao getConexao()
	{
		return conexao;
	}
}
