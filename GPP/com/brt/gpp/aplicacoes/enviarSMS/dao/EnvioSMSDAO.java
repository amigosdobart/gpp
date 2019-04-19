package com.brt.gpp.aplicacoes.enviarSMS.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;
import com.brt.gpp.comum.Cronometro;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.smpp.RequisicaoSMSC;
import com.brt.gpp.comum.conexoes.smpp.RespostaSMSC;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * A Classe <code>EnvioSMSDAO</code> e utilizada para manipular dados na tabela <code>TBL_GER_ENVIO_SMS</code>.
 * Contem medotods para inserir, alterar e buscar dados na tabela.<br>
 * Muito utilizada pelo GPP no processo de Consumo de SMS.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 20/12/2007
 */
public class EnvioSMSDAO
{
	private PREPConexao conexao;
	/**
	 * Utilizado pelo pool de conexoes abrir obter uma
	 * conexao com o banco de dados.
	 */
	private long idProcesso;

	private ArquivoConfiguracaoGPP arqConf;

	/**
	 * Instancia da classe <code>EnvioSMSDAO</code> com uma conexao dada.
	 *
	 * @param idProcesso Numero utilizado para executar operacoes no banco de dados e no
	 *                   pool de conexoes.
	 * @param conexao Conexao a ser utilizada pelo DAO
	 */
	public EnvioSMSDAO(long idProcesso, PREPConexao conexao)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
		arqConf = ArquivoConfiguracaoGPP.getInstance();
	}
	/**
	 * Insere uma entidade <code>DadosSMS</code> no banco de dados.<br>
	 * <hr>
	 * <b>OBSERVACOES:</b>
	 * <p>
	 * Para insersao de SMS os campos abaixo sao <b>OBRIGATORIOS</b>:
	 * <p>
	 * <ul>
	 *   <li><b>idtMsisdn</b>
	 *   <li><b>desMensagem</b>
	 * </ul>
	 * os campos abaixo sao <b>OPTATIVOS</b>:
	 * <p>
	 * <ul>
	 *   <li><b>idtMsisdnOrigem</b>
	 *   <li><b>datEnvioSMS</b>
	 *   <li><b>numPrioridade</b>
	 *   <li><b>tipoSMS</b>
	 * </ul>
	 * e os demais campos nao sao necessarios.
	 * <hr>
	 * <p>
	 * @return Numero de linhas adicionadas
	 * @param  sms Entidade <code>DadosSMS</code> contendo os dados a serem inseridos
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de insersao
	 */
	public int inserirSMS(DadosSMS sms) throws GPPInternalErrorException
	{
		String sql =
			"INSERT INTO TBL_GER_ENVIO_SMS (ID_REGISTRO,               "+ //0
			"                               IDT_MSISDN_ORIGEM,         "+ //1
			"                               IDT_MSISDN,                "+ //2
			"                               DES_MENSAGEM,              "+ //3
			"                               NUM_PRIORIDADE,            "+ //4
			"                               TIP_SMS,                   "+ //5
			"                               DAT_ENVIO_SMS,             "+ //6
			"                               IDT_STATUS_PROCESSAMENTO)  "+ //7
			"VALUES (SEQ_ENVIO_SMS.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)       ";
            //                 0            1  2  3  4  5  6  7

		if(sms.getIdtMsisdnOrigem() == null)
			sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));
		if(sms.getDatEnvioSMS() == null)
			sms.setDatEnvioSMS(Calendar.getInstance().getTime());
		if(sms.getTipoSMS() == null)
			sms.setTipoSMS(new TipoSMS());

		Object[] params =
			{
				sms.getIdtMsisdnOrigem(),
				sms.getIdtMsisdn(),
				sms.getDesMensagem(),
				new Integer(sms.getNumPrioridade()),
				sms.getTipoSMS().getIdtTipoSMS(),
				new java.sql.Timestamp(sms.getDatEnvioSMS().getTime()),
				new Integer(RespostaSMSC.SMS_STATUS_NAO_ENVIADO),
			};

		return conexao.executaPreparedUpdate(sql, params, idProcesso);
	}
	/**
	 * Altera dados de um SMS na tabela <code>TBL_GER_ENVIO_SMS</code> utilizando os dados
	 * da entidade <code>DadosSMS</code>
	 *
	 * @return Numero de linhas modificadas
	 * @param  sms Entidade <code>DadosSMS</code> contendo os dados a serem modificados
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de alteracao
	 */
	public int alterarSMS(DadosSMS sms) throws GPPInternalErrorException
	{
		String sql =
			"UPDATE TBL_GER_ENVIO_SMS            "+
			"SET IDT_MSISDN                = ?  ,"+ // 0
			"    IDT_MSISDN_ORIGEM         = ?  ,"+ // 1
			"    DES_MENSAGEM              = ?  ,"+ // 2
			"    NUM_PRIORIDADE            = ?  ,"+ // 3
			"    DAT_ENVIO_SMS             = ?  ,"+ // 4
			"    IDT_STATUS_PROCESSAMENTO  = ?  ,"+ // 5
			"    DAT_PROCESSAMENTO         = ?  ,"+ // 6
			"    TIP_SMS                   = ?  ,"+ // 7
			"    ID_REGISTRO_SMSC          = ?  ,"+ // 8
			"    IDT_STATUS_SMSC           = ?   "+ // 9
			"WHERE ID_REGISTRO = ?               "; // 10

		if(sms.getIdtMsisdnOrigem() == null)
			sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));
		if(sms.getDatEnvioSMS() == null)
			sms.setDatEnvioSMS(Calendar.getInstance().getTime());
		if(sms.getTipoSMS() == null)
			sms.setTipoSMS(new TipoSMS());

		Object[] params =
			{
				sms.getIdtMsisdn(),
				sms.getIdtMsisdnOrigem(),
				sms.getDesMensagem(),
				new Integer(sms.getNumPrioridade()),
				new java.sql.Timestamp(sms.getDatEnvioSMS().getTime()),
				new Integer(sms.getIdtStatusProcessamento()),
				sms.getDatProcessamento() != null ?
						new java.sql.Timestamp(sms.getDatProcessamento().getTime()) : null,
				sms.getTipoSMS().getIdtTipoSMS(),
				sms.getIdRegistroSMSC(),
				sms.getIdtStatusSMSC(),
				new Long(sms.getIdRegistro())
			};

		return conexao.executaPreparedUpdate(sql, params, idProcesso);
	}
	/**
	 * Seleciona um único SMS da tabela <code>TBL_GER_ENVIO_SMS</code> baseado
	 * na chave primaria <code>ID_REGISTRO</code>
	 * e retorna uma entiade <code>DadosSMS</code>.
	 *
	 * @param idRegistro Id do SMS cadastrado na tabela
	 * @return Entidade <code>DadosSMS</code> preenchida.<br>
	 *         <code>null</code> caso ocorra algum erro ou a busca nao retorne nada.
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 * @throws SQLException Caso ocorra algum erro ao percorrer o <code>ResultSet</code>
	 */
	public DadosSMS buscarSMSPorIdRegistro(long idRegistro) throws GPPInternalErrorException, SQLException
	{
		DadosSMS sms = null;
		String sql =
			"SELECT ENV.ID_REGISTRO                           AS ID_REGISTRO               , "+ // 0
			"       ENV.IDT_MSISDN                            AS IDT_MSISDN                , "+ // 1
			"       ENV.IDT_MSISDN_ORIGEM                     AS IDT_MSISDN_ORIGEM         , "+ // 2
			"       ENV.DES_MENSAGEM                          AS DES_MENSAGEM              , "+ // 3
			"       ENV.NUM_PRIORIDADE                        AS NUM_PRIORIDADE            , "+ // 4
			"       ENV.DAT_ENVIO_SMS                         AS DAT_ENVIO_SMS             , "+ // 5
			"       ENV.DAT_PROCESSAMENTO                     AS DAT_PROCESSAMENTO         , "+ // 6
			"       ENV.IDT_STATUS_PROCESSAMENTO              AS IDT_STATUS_PROCESSAMENTO  , "+ // 7
			"       ENV.TIP_SMS                               AS TIP_SMS                   , "+ // 8
			"       ENV.ID_REGISTRO_SMSC                      AS ID_REGISTRO_SMSC          , "+ // 9
			"       ENV.IDT_STATUS_SMSC                       AS IDT_STATUS_SMSC           , "+ // 10
			"       NVL(TIP.IND_NOTIFICAR_ENTREGA, 0)         AS IND_NOTIFICAR_ENTREGA     , "+ // 11
			"       NVL(TIP.NUM_INI_PERIODO_ENVIO_SMS, 0)     AS NUM_INI_PERIODO_ENVIO_SMS , "+ // 12
			"       NVL(TIP.NUM_FIM_PERIODO_ENVIO_SMS, 86399) AS NUM_FIM_PERIODO_ENVIO_SMS , "+ // 13
			"       NVL(TIP.TGCS_TGTS_ID_CONEXAO, 0) 	      AS ID_CONEXAO                  "+ // 14
			" FROM TBL_GER_ENVIO_SMS ENV,                                                    "+
			"      TBL_GER_TIPO_SMS  TIP                                                     "+
			"WHERE ENV.ID_REGISTRO = ?                                                       "+
			"  AND ENV.TIP_SMS = TIP.IDT_TIPO_SMS(+)                                         ";

		Object[] params =
		{
			new Long(idRegistro)
		};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				if(registros.next())
				{
					sms = new DadosSMS();
					sms.setIdRegistro(registros.getLong("ID_REGISTRO"));
					sms.setIdtMsisdn(registros.getString("IDT_MSISDN"));
					sms.setIdtMsisdnOrigem(registros.getString("IDT_MSISDN_ORIGEM"));
					sms.setDesMensagem(registros.getString("DES_MENSAGEM"));
					sms.setNumPrioridade(registros.getInt("NUM_PRIORIDADE"));
					sms.setDatEnvioSMS(registros.getTimestamp("DAT_ENVIO_SMS"));
					sms.setDatProcessamento(registros.getTimestamp("DAT_PROCESSAMENTO"));
					sms.setIdtStatusProcessamento(registros.getInt("IDT_STATUS_PROCESSAMENTO"));
					sms.setIdRegistroSMSC(registros.getObject("ID_REGISTRO_SMSC") != null ?
						new Long(registros.getLong("ID_REGISTRO_SMSC")) : null);
					sms.setIdtStatusSMSC(registros.getObject("IDT_STATUS_SMSC") != null ?
						new Integer(registros.getInt("IDT_STATUS_SMSC")) : null);
					sms.getTipoSMS().setIdtTipoSMS(registros.getString("TIP_SMS"));
					sms.getTipoSMS().setIndNotificarEntrega(registros.getInt("IND_NOTIFICAR_ENTREGA"));
					sms.getTipoSMS().setNumIniPeriodoEnvioSMS(registros.getInt("NUM_INI_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setNumFimPeriodoEnvioSMS(registros.getInt("NUM_FIM_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setIdConexao(registros.getInt("ID_CONEXAO"));

					if(sms.getIdtMsisdnOrigem() == null)
						sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));
				}
			}
			finally
			{
				registros.close();
			}
		}

		return sms;
	}
	/**
	 * Seleciona um único SMS da tabela <code>TBL_GER_ENVIO_SMS</code> baseado
	 * no campo <code>ID_REGISTRO_SMSC</code> e retorna uma entiade <code>DadosSMS</code>.
	 *
	 * @param idRegistro Id do SMS cadastrado na tabela
	 * @return Entidade <code>DadosSMS</code> preenchida.<br>
	 *         <code>null</code> caso ocorra algum erro ou a busca nao retorne nada.
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 * @throws SQLException Caso ocorra algum erro ao percorrer o <code>ResultSet</code>
	 */
	public DadosSMS buscarSMSPorIdRegistroSMSC(long idRegistroSMSC) throws GPPInternalErrorException, SQLException
	{
		DadosSMS sms = null;
		String sql =
			"SELECT ENV.ID_REGISTRO                           AS ID_REGISTRO               , "+
			"       ENV.IDT_MSISDN                            AS IDT_MSISDN                , "+
			"       ENV.IDT_MSISDN_ORIGEM                     AS IDT_MSISDN_ORIGEM         , "+
			"       ENV.DES_MENSAGEM                          AS DES_MENSAGEM              , "+
			"       ENV.NUM_PRIORIDADE                        AS NUM_PRIORIDADE            , "+
			"       ENV.DAT_ENVIO_SMS                         AS DAT_ENVIO_SMS             , "+
			"       ENV.DAT_PROCESSAMENTO                     AS DAT_PROCESSAMENTO         , "+
			"       ENV.IDT_STATUS_PROCESSAMENTO              AS IDT_STATUS_PROCESSAMENTO  , "+
			"       ENV.TIP_SMS                               AS TIP_SMS                   , "+
			"       ENV.ID_REGISTRO_SMSC                      AS ID_REGISTRO_SMSC          , "+
			"       ENV.IDT_STATUS_SMSC                       AS IDT_STATUS_SMSC           , "+
			"       NVL(TIP.IND_NOTIFICAR_ENTREGA, 0)         AS IND_NOTIFICAR_ENTREGA     , "+
			"       NVL(TIP.NUM_INI_PERIODO_ENVIO_SMS, 0)     AS NUM_INI_PERIODO_ENVIO_SMS , "+
			"       NVL(TIP.NUM_FIM_PERIODO_ENVIO_SMS, 86399) AS NUM_FIM_PERIODO_ENVIO_SMS , "+
			"       NVL(TIP.TGCS_TGTS_ID_CONEXAO, 0) 	      AS ID_CONEXAO                  "+
			" FROM TBL_GER_ENVIO_SMS ENV,                                                    "+
			"      TBL_GER_TIPO_SMS  TIP                                                     "+
			"WHERE ENV.ID_REGISTRO_SMSC = ?                                                  "+
			"  AND ENV.IDT_STATUS_PROCESSAMENTO = ?                                          "+
			"  AND ENV.TIP_SMS = TIP.IDT_TIPO_SMS(+)                                         ";

		Object[] params =
		{
			new Long(idRegistroSMSC),
			new Integer(RespostaSMSC.SMS_STATUS_ENVIANDO)
		};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				if(registros.next())
				{
					sms = new DadosSMS();
					sms.setIdRegistro(registros.getLong("ID_REGISTRO"));
					sms.setIdtMsisdn(registros.getString("IDT_MSISDN"));
					sms.setIdtMsisdnOrigem(registros.getString("IDT_MSISDN_ORIGEM"));
					sms.setDesMensagem(registros.getString("DES_MENSAGEM"));
					sms.setNumPrioridade(registros.getInt("NUM_PRIORIDADE"));
					sms.setDatEnvioSMS(registros.getTimestamp("DAT_ENVIO_SMS"));
					sms.setDatProcessamento(registros.getTimestamp("DAT_PROCESSAMENTO"));
					sms.setIdtStatusProcessamento(registros.getInt("IDT_STATUS_PROCESSAMENTO"));
					sms.setIdRegistroSMSC(registros.getObject("ID_REGISTRO_SMSC") != null ?
						new Long(registros.getLong("ID_REGISTRO_SMSC")) : null);
					sms.setIdtStatusSMSC(registros.getObject("IDT_STATUS_SMSC") != null ?
						new Integer(registros.getInt("IDT_STATUS_SMSC")) : null);
					sms.getTipoSMS().setIdtTipoSMS(registros.getString("TIP_SMS"));
					sms.getTipoSMS().setIndNotificarEntrega(registros.getInt("IND_NOTIFICAR_ENTREGA"));
					sms.getTipoSMS().setNumIniPeriodoEnvioSMS(registros.getInt("NUM_INI_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setNumFimPeriodoEnvioSMS(registros.getInt("NUM_FIM_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setIdConexao(registros.getInt("ID_CONEXAO"));

					if(sms.getIdtMsisdnOrigem() == null)
						sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));
				}
			}
			finally
			{
				registros.close();
			}
		}

		return sms;
	}
	/**
	 * Seleciona uma lista de SMS da tabela <code>TBL_GER_ENVIO_SMS</code>
	 * baseado no campo <code>IDT_STATUS_PROCESSAMENTO</code> e retorna uma <code>List</code>
	 * de entidades <code>DadosSMS</code>.
	 *
	 * @param status Status a ser pesquisado
	 * @return Lista de entidade <code>DadosSMS</code> preenchida.<br>
	 * 		   <code>null</code> caso ocorra algum erro ou
	 * 		   <code>Lista</code> vazia caso busca nao retorne nada.
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 * @throws SQLException Caso ocorra algum erro ao percorrer o <code>ResultSet</code>
	 */
	public List buscarSMSPorStatusProcessamento(int status) throws GPPInternalErrorException, SQLException
	{
		List lista = null;
		String sql =
			"SELECT ENV.ID_REGISTRO                           AS ID_REGISTRO               , "+
			"       ENV.IDT_MSISDN                            AS IDT_MSISDN                , "+
			"       ENV.IDT_MSISDN_ORIGEM                     AS IDT_MSISDN_ORIGEM         , "+
			"       ENV.DES_MENSAGEM                          AS DES_MENSAGEM              , "+
			"       ENV.NUM_PRIORIDADE                        AS NUM_PRIORIDADE            , "+
			"       ENV.DAT_ENVIO_SMS                         AS DAT_ENVIO_SMS             , "+
			"       ENV.DAT_PROCESSAMENTO                     AS DAT_PROCESSAMENTO         , "+
			"       ENV.IDT_STATUS_PROCESSAMENTO              AS IDT_STATUS_PROCESSAMENTO  , "+
			"       ENV.TIP_SMS                               AS TIP_SMS                   , "+
			"       ENV.ID_REGISTRO_SMSC                      AS ID_REGISTRO_SMSC          , "+
			"       ENV.IDT_STATUS_SMSC                       AS IDT_STATUS_SMSC           , "+
			"       NVL(TIP.IND_NOTIFICAR_ENTREGA, 0)         AS IND_NOTIFICAR_ENTREGA     , "+
			"       NVL(TIP.NUM_INI_PERIODO_ENVIO_SMS, 0)     AS NUM_INI_PERIODO_ENVIO_SMS , "+
			"       NVL(TIP.NUM_FIM_PERIODO_ENVIO_SMS, 86399) AS NUM_FIM_PERIODO_ENVIO_SMS , "+
			"       NVL(TIP.TGCS_TGTS_ID_CONEXAO, 0) 	      AS ID_CONEXAO                  "+
			" FROM TBL_GER_ENVIO_SMS ENV,                                                    "+
			"      TBL_GER_TIPO_SMS  TIP                                                     "+
			"WHERE ENV.DAT_ENVIO_SMS <= ?                                                    "+
			"  AND ENV.IDT_STATUS_PROCESSAMENTO = ?                                          "+
			"  AND ENV.TIP_SMS = TIP.IDT_TIPO_SMS(+)                                         ";

		Object[] params =
			{
				new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()),
				new Integer(status)
			};
		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		if(registros != null)
		{
			try
			{
				lista = new ArrayList();
				while(registros.next())
				{
					DadosSMS sms = new DadosSMS();
					sms.setIdRegistro(registros.getLong("ID_REGISTRO"));
					sms.setIdtMsisdn(registros.getString("IDT_MSISDN"));
					sms.setIdtMsisdnOrigem(registros.getString("IDT_MSISDN_ORIGEM"));
					sms.setDesMensagem(registros.getString("DES_MENSAGEM"));
					sms.setNumPrioridade(registros.getInt("NUM_PRIORIDADE"));
					sms.setDatEnvioSMS(registros.getTimestamp("DAT_ENVIO_SMS"));
					sms.setDatProcessamento(registros.getTimestamp("DAT_PROCESSAMENTO"));
					sms.setIdtStatusProcessamento(registros.getInt("IDT_STATUS_PROCESSAMENTO"));
					sms.setIdRegistroSMSC(registros.getObject("ID_REGISTRO_SMSC") != null ?
						new Long(registros.getLong("ID_REGISTRO_SMSC")) : null);
					sms.setIdtStatusSMSC(registros.getObject("IDT_STATUS_SMSC") != null ?
						new Integer(registros.getInt("IDT_STATUS_SMSC")) : null);
					sms.getTipoSMS().setIdtTipoSMS(registros.getString("TIP_SMS"));
					sms.getTipoSMS().setIndNotificarEntrega(registros.getInt("IND_NOTIFICAR_ENTREGA"));
					sms.getTipoSMS().setNumIniPeriodoEnvioSMS(registros.getInt("NUM_INI_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setNumFimPeriodoEnvioSMS(registros.getInt("NUM_FIM_PERIODO_ENVIO_SMS"));
					sms.getTipoSMS().setIdConexao(registros.getInt("ID_CONEXAO"));

					if(sms.getIdtMsisdnOrigem() == null)
						sms.setIdtMsisdnOrigem(arqConf.getString(RequisicaoSMSC.SMSC_ORIGINADOR));

					lista.add(sms);
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
	 * Metodo utilizado pelo produtor da fila de SMS.
	 *
	 * @return <code>ResultSet</code> contendo os dados que serao inseridos na fila de SMS.
	 *
	 * @throws GPPInternalErrorException Caso ocorra algum erro no processo de busca
	 */
	public ResultSet carregarFilaSMS(int tamanhoFila) throws GPPInternalErrorException
	{
		String sql =
			"SELECT ID_REGISTRO               ,                                                      "+
			"       IDT_MSISDN                ,                                                      "+
			"       IDT_MSISDN_ORIGEM         ,                                                      "+
			"       DES_MENSAGEM              ,                                                      "+
			"       NUM_PRIORIDADE            ,                                                      "+
			"       DAT_ENVIO_SMS             ,                                                      "+
			"       DAT_PROCESSAMENTO         ,                                                      "+
			"       IDT_STATUS_PROCESSAMENTO  ,                                                      "+
			"       TIP_SMS                   ,                                                      "+
			"       IND_NOTIFICAR_ENTREGA     ,                                                      "+
			"       ID_CONEXAO                                                                       "+
			"FROM (SELECT /*+index (ENV XIE1TBL_GER_ENVIO_SMS)*/                                     "+
			"              ENV.ID_REGISTRO                           AS ID_REGISTRO               ,  "+
			"              ENV.IDT_MSISDN                            AS IDT_MSISDN                ,  "+
			"              ENV.IDT_MSISDN_ORIGEM                     AS IDT_MSISDN_ORIGEM         ,  "+
			"              ENV.DES_MENSAGEM                          AS DES_MENSAGEM              ,  "+
			"              ENV.NUM_PRIORIDADE                        AS NUM_PRIORIDADE            ,  "+
			"              ENV.DAT_ENVIO_SMS                         AS DAT_ENVIO_SMS             ,  "+
			"              ENV.DAT_PROCESSAMENTO                     AS DAT_PROCESSAMENTO         ,  "+
			"              ENV.IDT_STATUS_PROCESSAMENTO              AS IDT_STATUS_PROCESSAMENTO  ,  "+
			"              ENV.TIP_SMS                               AS TIP_SMS                   ,  "+
			"              NVL(TIP.IND_NOTIFICAR_ENTREGA, 0)         AS IND_NOTIFICAR_ENTREGA     ,  "+
			"              NVL(TIP.TGCS_TGTS_ID_CONEXAO, 0)          AS ID_CONEXAO                   "+
			"      FROM TBL_GER_ENVIO_SMS ENV,                                                       "+
			"           TBL_GER_TIPO_SMS  TIP                                                        "+
			"      WHERE ENV.DAT_ENVIO_SMS <= SYSDATE                                                "+
			"        AND ENV.IDT_STATUS_PROCESSAMENTO = ?                                            "+
			"        AND ENV.TIP_SMS = TIP.IDT_TIPO_SMS(+)                                           "+
			"        AND (     ? >= TIP.NUM_INI_PERIODO_ENVIO_SMS                                    "+
			"              AND ? <= TIP.NUM_FIM_PERIODO_ENVIO_SMS                                    "+
			"              OR ENV.TIP_SMS IS NULL)                                                   "+
			"      ORDER BY ENV.NUM_PRIORIDADE, ENV.ID_REGISTRO)                                     "+
			"WHERE ROWNUM <= ?                                                                       ";

		Cronometro cron = new Cronometro();
		cron.zerarCampos(Calendar.HOUR);
		int segundoAtual = (int)cron.getTempoDecorrido(Calendar.SECOND);

		Object[] params =
			{
				new Integer(RespostaSMSC.SMS_STATUS_NAO_ENVIADO),
				new Integer(segundoAtual),
				new Integer(segundoAtual),
				new Integer(tamanhoFila)
			};

		ResultSet registros = conexao.executaPreparedQuery(sql, params, idProcesso);

		return registros;
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