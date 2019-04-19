package com.brt.gpp.aplicacoes.enviarSMS.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

public class TipoSMSDAO
{
	private PREPConexao conexao;
	/**
	 * Utilizado pelo pool de conexoes abrir obter uma
	 * conexao com o banco de dados.
	 */
	private long idProcesso;
	/**
	 * Instancia da classe <code>TipoSMSDAO</code> com uma conexao dada.
	 *
	 * @param idProcesso Numero utilizado para executar operacoes no banco de dados e no
	 *                   pool de conexoes.
	 * @param conexao Conexao a ser utilizada pelo DAO
	 */
	public TipoSMSDAO(long idProcesso, PREPConexao conexao)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
	}
	/**
	 * Insere uma entidade do tipo <code>TipoSMS</code> na tabela <code>TBL_GER_TIPO_SMS</code>.
	 * Os atributos obrigatorios sao <code>idtTipoSMS</code> e <code>idConexao</code>
	 * os demais sao optativos.
	 *
	 * @param tipoSMS Entidade <code>TipoSMS</code> contendo os dados a serem inseridos
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de insersao
	 */
	public void inserirTipoSMS(TipoSMS tipoSMS) throws GPPInternalErrorException
	{
		String sql =
			"INSERT INTO TBL_GER_TIPO_SMS ( IDT_TIPO_SMS              , "+
			"                               IND_NOTIFICAR_ENTREGA     , "+
			"                               NUM_INI_PERIODO_ENVIO_SMS , "+
			"                               NUM_FIM_PERIODO_ENVIO_SMS , "+
			"                               TGCS_TGTS_ID_CONEXAO      ) "+
			"VALUES (?, ?, ?, ?, ?)                                     ";

		Object[] params =
			{
				tipoSMS.getIdtTipoSMS(),
				new Integer(tipoSMS.getIndNotificarEntrega()),
				new Integer(tipoSMS.getNumIniPeriodoEnvioSMS()),
				new Integer(tipoSMS.getNumFimPeriodoEnvioSMS()),
				new Integer(tipoSMS.getIdConexao())
			};

		conexao.executaPreparedUpdate(sql, params, idProcesso);
	}
	/**
	 * Altera dados de um tipo de SMS na tabela <code>TBL_GER_TIPO_SMS</code>.
	 *
	 * @param tipoSMS Entidade <code>TipoSMS</code> que contera os dados a serem alterados na tabela.
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de alteracao
	 */
	public void alterarTipoSMS(TipoSMS tipoSMS) throws GPPInternalErrorException
	{
		String sql =
			"UPDATE TBL_GER_TIPO_SMS                  "+
			"   SET IND_NOTIFICAR_ENTREGA       = ? , "+
			"       NUM_INI_PERIODO_ENVIO_SMS   = ? , "+
			"       NUM_FIM_PERIODO_ENVIO_SMS   = ? , "+
			"       TGCS_TGTS_ID_CONEXAO        = ?   "+
			" WHERE IDT_TIPO_SMS = ?                  ";

		Object[] params =
			{
				new Integer(tipoSMS.getIndNotificarEntrega()),
				new Integer(tipoSMS.getNumIniPeriodoEnvioSMS()),
				new Integer(tipoSMS.getNumFimPeriodoEnvioSMS()),
				new Integer(tipoSMS.getIdConexao()),
				tipoSMS.getIdtTipoSMS()
			};

		conexao.executaPreparedUpdate(sql, params, idProcesso);
	}
	/**
	 * Excluir tipo SMS da tabela <code>TBL_GER_TIPO_SMS</code>.
	 *
	 * @param idtTipoSMS Id do tipo de SMS a ser excluido.
	  * @throws GPPInternalErrorException Caso ocorra algum erro no processo de exclusao
	 */
	public void excluirTipoSMS(String idtTipoSMS) throws GPPInternalErrorException
	{
		String sql =
			"DELETE FROM TBL_GER_TIPO_SMS WHERE IDT_TIPO_SMS = ?";

		Object[] params =
			{
				idtTipoSMS
			};

		conexao.executaPreparedUpdate(sql, params, idProcesso);
	}
	/**
	 * Retorna uma instancia da classe <code>TipoSMS</code> preenchido com os dados da tabela
	 * <code>TBL_GER_TIPO_SMS</code>.
	 *
	 * @param  idtTipoSMS Id to tipo de SMS cadastrado na tabela
	 * @return <code>TipoSMS</code> preenchido com os dados contidos na tabela em caso de sucesso.<br>
	 *         <code>TipoSMS</code> padrao caso os dados nao sejam encontrados ou ocorra algum erro.
	 *
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 * @throws SQLException Caso ocorra algum erro ao percorrer o <code>ResultSet</code>
	 */
	public TipoSMS buscarTipoSMS(String idtTipoSMS) throws GPPInternalErrorException, SQLException
	{
		TipoSMS tipoSMS = new TipoSMS();

		if(idtTipoSMS == null)
			return tipoSMS;

		String sql =
			"SELECT IDT_TIPO_SMS              , "+
			"       IND_NOTIFICAR_ENTREGA     , "+
			"       NUM_INI_PERIODO_ENVIO_SMS , "+
			"       NUM_FIM_PERIODO_ENVIO_SMS , "+
			"       TGCS_TGTS_ID_CONEXAO        "+
			" FROM TBL_GER_TIPO_SMS             "+
			"WHERE IDT_TIPO_SMS = ?             ";

		Object[] params =
		{
				idtTipoSMS
		};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				if(registros.next())
				{
					tipoSMS.setIdtTipoSMS(registros.getString("IDT_TIPO_SMS"));
					tipoSMS.setIndNotificarEntrega(registros.getInt("IND_NOTIFICAR_ENTREGA"));
					tipoSMS.setNumIniPeriodoEnvioSMS(registros.getInt("NUM_INI_PERIODO_ENVIO_SMS"));
					tipoSMS.setNumFimPeriodoEnvioSMS(registros.getInt("NUM_FIM_PERIODO_ENVIO_SMS"));
					tipoSMS.setIdConexao(registros.getInt("TGCS_TGTS_ID_CONEXAO"));
				}
			}
			finally
			{
				registros.close();
			}
		}

		return tipoSMS;
	}
	/**
	 * Retorna o idProcesso
	 *
	 * @return idProcesso
	 */
	public long getIdProcesso()
	{
		return idProcesso;
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
