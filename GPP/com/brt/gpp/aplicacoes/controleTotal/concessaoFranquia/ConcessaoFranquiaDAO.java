package com.brt.gpp.aplicacoes.controleTotal.concessaoFranquia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;

import com.brt.gpp.aplicacoes.controleTotal.expiracaoFranquia.ExpiracaoFranquiaMaquinaDeEstados;
import com.brt.gpp.aplicacoes.recarregar.ParametrosRecarga;
import com.brt.gpp.aplicacoes.recarregar.RecargaDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;

/**
 * Classe responsavel por todos os acessos ao banco de dados do GPP para a Concessao de Franquia Total
 * @author Magno Batista Corrêa
 * @since 2007/05/17 
 */
public class ConcessaoFranquiaDAO
{
    private PREPConexao 		conexao;
	private long 				idProcesso;
	private MapCodigosRetorno 	mapCodigosRetorno;
	
	private final static String SQL_ASSINANTE_CONCESSAO = " SELECT                                                       " +
														  "        ROWID                            AS ID_PROCESSAMENTO, " +
														  "        RR.idt_msisdn_pre                AS MSISDN,           " +
														  "        SUBSTR(RR.COD_FATURAMENTO,201,4) AS PLANO_PRECO,      " +
														  "        RR.vlr_recarga                   AS VALOR_FRANQUIA,   " +
														  "        RR.cod_recarga                   AS NUMERO_RECARGA    " +
														  "   FROM tbl_int_recarga_recorrente RR                         " +
														  "  WHERE                                                       " +
														  "        RR.IDT_STATUS_PROCESSAMENTO      = ?                  " +
														  "    AND RR.tip_envio                     = ?                  " +
														  "   ORDER BY DAT_PROCESSAMENTO                                 " ;
	
	private final static String SQL_ATUALIZAR_CONCESSAO_FRANQUIA = " UPDATE tbl_int_recarga_recorrente    " +
																   "    SET IDT_STATUS_PROCESSAMENTO = ?, " +
																   "        COD_RETORNO = ?,              " +
																   "        DES_RETORNO = ?               " +
																   "  WHERE ROWID 		= ?               " ;
	
	private final static String SQL_INSERIR_CONTROLE_TOTAL = " INSERT INTO TBL_APR_CONTROLE_TOTAL(         " +
															 "             IDT_MSISDN, DAT_PROCESSAMENTO,  " +
															 "             COD_CONCESSAO, IDT_PLANO_PRECO, " +
															 "             VLR_FRANQUIA_CONCEDIDA)         " +
															 " VALUES      (                               " +
															 "             ?,SYSDATE,?,?,?                 " +
															 "             )                               " ;
	
	private final static String SQL_ATUALIZAR_CONTROLE_TOTAL = " UPDATE TBL_APR_CONTROLE_TOTAL       " +
															   "    SET DAT_PROCESSAMENTO = SYSDATE, " +
															   "        COD_CONCESSAO 			= ?, " +
															   "        IDT_PLANO_PRECO 		= ?, " +
															   "        VLR_FRANQUIA_CONCEDIDA 	= ?  " +
															   "  WHERE IDT_MSISDN 				= ?  " ;
	
	private final static String SQL_SELECIONAR_ULTIMA_RECARGA = " SELECT COD_CONCESSAO          " +
															    "   FROM TBL_APR_CONTROLE_TOTAL " +
															    "  WHERE IDT_MSISDN = ?         " ;
	
	private final static String SQL_SELECIONAR_ID_RECARGA = "SELECT TO_CHAR(SEQ_RECARGA_ID.NEXTVAL) ID_RECARGA " +
															"FROM DUAL";
	
    private static final String SQL_INSERIR_CONCESSAO_PARA_EXPIRACAO = " INSERT INTO TBL_GER_EXPIRACAO_FRANQUIA        " +
																       "     (IDT_MSISDN,                              " +
																       "      DAT_INCLUSAO,                            " +
																       "      DAT_EXPIRACAO,                           " +
																       "      VLR_FRANQUIA,                            " +
																       "      DAT_PROCESSAMENTO,                       " +
																       "      IDT_STATUS_EXPIRACAO,                    " +
																       "      QTD_TENTATIVAS)                          " +
																       " VALUES                                        " +
																       "     (?,TRUNC(SYSDATE),?,?,TRUNC(SYSDATE),?,0) " ;
	
	/**
	 * Contrutor
	 * 
	 * @param conexao
	 * @param idProcesso
	 */
	public ConcessaoFranquiaDAO(PREPConexao conexao, long idProcesso)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
		this.mapCodigosRetorno = MapCodigosRetorno.getInstance();
	}
	
	/**
	 * Retorna um ResultSet com as informacoes de todos as Concessoes a serem efetuadas
	 * 
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public ResultSet getAssinanteParaConcessao() throws GPPInternalErrorException
	{
		Object[] parametros = {Definicoes.IND_LINHA_DISPONIBILIZADA, Definicoes.TIPO_CONTROLE_TOTAL_FRANQUIA};
		return conexao.executaPreparedQuery(SQL_ASSINANTE_CONCESSAO, parametros, idProcesso);
	}
	
	/**
	 * Consome a proxima entrada do resultset transformando-o em um VO do tipo ConcessaoFranquiaVO
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws GPPInternalErrorException 
	 */
	public ConcessaoFranquiaVO getConcessaoFranquiaVO(ResultSet resultSet) throws SQLException
	{
		ConcessaoFranquiaVO vo = null;
		
		// Avanca o ResultSet e testa se existe o próximo registro
		if (resultSet.next())
		{
			vo = new ConcessaoFranquiaVO(resultSet.getString("ID_PROCESSAMENTO"),
										 resultSet.getString("MSISDN"),
										 resultSet.getString("PLANO_PRECO"),
										 resultSet.getDouble("VALOR_FRANQUIA"),
										 resultSet.getInt   ("NUMERO_RECARGA"));
		}
		
		return vo;
	}
	
	/**
	 * Atualiza os dados processados pelo VO, informando que o registro jah foi processado e com que retorno.
	 * Atualiza todas as tabelas finais do GPP, sendo que serah atualizado a TBL_APR_CONTROLE_TOTAL no caso de sucesso
	 * ou a TBL_REC_RECARGAS_NOK para o caso de falhas
	 * 
	 * @param vo
	 * @param conexao
	 * @param idProcesso
	 * @throws GPPInternalErrorException
	 */
	public void atualizarConcessaoFranquia(ConcessaoFranquiaVO vo) throws GPPInternalErrorException
	{
		Object[] parametros = {vo.getRetorno() == Definicoes.RET_OPERACAO_OK 		? 
												  Definicoes.IDT_REC_RECORRENTE_OK 	: 
												  Definicoes.IDT_REC_RECORRENTE_ERRO,
							   new Short(vo.getRetorno()),
							   getCodMotivo(vo.getRetorno()),
							   vo.getIdProcessamento()};
		
		conexao.executaPreparedUpdate(SQL_ATUALIZAR_CONCESSAO_FRANQUIA, parametros, idProcesso);
		
		// Registrando para rastreamento do GPP
		if( vo.getRetorno() == Definicoes.RET_OPERACAO_OK )
		{
			// Registrar na TBL_APR_CONTROLE_TOTAL
			Object[] parametrosAtualiza = {new Integer(vo.getNumeroRecarga()),
										   new Short(vo.getPlanoPrecoGPP()),
										   new Double(vo.getValorFranquia()),
										   vo.getMsisdn()};
			
			int qtdAtualizacao = conexao.executaPreparedUpdate(SQL_ATUALIZAR_CONTROLE_TOTAL, parametrosAtualiza, idProcesso);
			if (qtdAtualizacao < 1)
			{
				// O Assinante ainda nao havia feito nenhuma recarga
				Object[] parametrosInsere = {vo.getMsisdn(),
											 new Integer(vo.getNumeroRecarga()),
											 new Short(vo.getPlanoPrecoGPP()),
											 new Double(vo.getValorFranquia())};
				
				conexao.executaPreparedUpdate(SQL_INSERIR_CONTROLE_TOTAL, parametrosInsere, idProcesso);
			}
            
            // Registrar na TBL_GER_EXPIRACAO_FRANQUIA
            // para ser possivel a expiracao
            Object[] parametrosExpiracao = {vo.getMsisdn(),
						                    new Date(vo.getNovaDataExpiracao().getTime()), 
						                    new Double(vo.getValorFranquia()),
						                    new Integer(ExpiracaoFranquiaMaquinaDeEstados.AINDA_NAO_PENDENTE)};
            
            conexao.executaPreparedUpdate(SQL_INSERIR_CONCESSAO_PARA_EXPIRACAO, parametrosExpiracao, idProcesso);
		}
		else
		{
			java.util.Date dataAtual = Calendar.getInstance().getTime();
			
			String tipoCredito   = Definicoes.TIPO_CREDITO_FRANQUIA;
			String sistemaOrigem = Definicoes.SO_GNV;
			String operador      = Definicoes.GPP_OPERADOR;
			String tipoTransacao = Definicoes.RECARGA_FRANQUIA_CONTROLE_TOTAL;
			
			// Registrar na TBL_REC_RECARGAS_NOK
			ParametrosRecarga parametrosRecarga = new ParametrosRecarga();
			
			parametrosRecarga.setCodigoErro(vo.getRetorno());
			parametrosRecarga.setIdValor(vo.getValorFranquia());
			parametrosRecarga.setMSISDN(vo.getMsisdn());
			parametrosRecarga.setIdOperacao(Definicoes.TIPO_RECARGA);
			parametrosRecarga.setTipoCredito(tipoCredito);
			parametrosRecarga.setSistemaOrigem(sistemaOrigem);
			parametrosRecarga.setOperador(operador);
			parametrosRecarga.setTipoTransacao(tipoTransacao);
			parametrosRecarga.setDatOrigem(dataAtual);
			parametrosRecarga.setDatRecarga(dataAtual);
			
			try
			{
				parametrosRecarga.setIdentificacaoRecarga(RecargaDAO.newIdRecarga(this.conexao));
				RecargaDAO.inserirRecargaNokGSM(parametrosRecarga, vo.getRetorno(), this.conexao);
			}
			catch (Exception e)
			{
				// Caso der erro, eh falha de banco logo vai parar em outro local
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Verifica qual a msg relacionada ao codigo do motivo
	 * 
	 * @param short CodMotivo Código do Motivo
	 * @return String msg do motivo
	 * @throws GPPInternalErrorException
	 */
	private String getCodMotivo(short codMotivo)
	{
		CodigoRetorno codigoRetorno = mapCodigosRetorno.getRetorno(codMotivo);
		
		if(codigoRetorno != null)
			return codigoRetorno.getDescRetorno();
		
		// Caso nao encontre o motivo, pelo menos o enumera
		return ""+codMotivo;
	}
	
	/**
	 * Retorna o numero da ultima Franquia Concedida de Controle Total do assinante informado
	 * 
	 * @param msisdn
	 * @return
	 * @throws GPPInternalErrorException 
	 */
	public int getNumeroUltimaFranquiaConcedida(String msisdn) throws GPPInternalErrorException
	{
		// Caso receba algum msisdn invalido
		if(msisdn == null || msisdn.equals(""))
			throw new GPPInternalErrorException("Falha ao obter o Ultima da Recarga. MSISDN invalido");
		
		Object[] parametros = {msisdn};
		ResultSet resultSet = conexao.executaPreparedQuery(SQL_SELECIONAR_ULTIMA_RECARGA, parametros, idProcesso);
		
		int numeroUltimaFranquiaConcedida = 1;
		
		try
		{
			if (resultSet.next())
				numeroUltimaFranquiaConcedida = resultSet.getInt("COD_CONCESSAO");
		}
		catch (SQLException e)
		{
			throw new GPPInternalErrorException("Falha ao obter o Ultima da Recarga. Erro SQL: "+e.toString());
		}
		finally
		{
			if(resultSet != null)
			{
				try
				{
					resultSet.close();
				}
				catch (SQLException e)
				{
					throw new GPPInternalErrorException("Falha ao fechar resultset.ERRO: "+e);
				}
			}
		}
		
		return numeroUltimaFranquiaConcedida;
	}
	
	/**
	 * Retorna a Identificacao Unica da Recarga
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public String getIdentificacaoRecarga() throws GPPInternalErrorException
	{
		String identificacaoRecarga = null;
		
		// Seleciona um identificador para p registro de recarga
		ResultSet resultSet = conexao.executaQuery(SQL_SELECIONAR_ID_RECARGA,idProcesso);
		
		try
		{
			if (resultSet.next())
				identificacaoRecarga = resultSet.getString("ID_RECARGA");
		}
		catch (SQLException e)
		{
			throw new GPPInternalErrorException("Falha ao obter o Identificador da Recarga. Erro SQL: "+e.toString());
		}
		finally
		{
			if(resultSet != null)
			{
				try
				{
					resultSet.close();
				}
				catch (SQLException e)
				{
					throw new GPPInternalErrorException("Falha ao fechar resultset.ERRO: "+e);
				}
			}
		}
		
		return identificacaoRecarga;
	}
}