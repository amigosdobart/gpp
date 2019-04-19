package com.brt.gpp.aplicacoes.controleTotal.expiracaoFranquia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Classe responsavel por todos os acessos ao banco de dados do GPP para a Expiracao de Franquia Total
 * @author Magno Batista Corrêa
 * @since 2007/05/23 
 */
public class ExpiracaoFranquiaDAO
{
    private static final String SQL_ASSINANTE_EXPIRACAO =
        " SELECT IDT_MSISDN,                " +
        "        DAT_INCLUSAO,              " +
        "        DAT_EXPIRACAO,             " +
        "        VLR_FRANQUIA,              " +
        "        DAT_PROCESSAMENTO,         " +
        "        IDT_STATUS_EXPIRACAO,      " +
        "        QTD_TENTATIVAS             " +
        "   FROM TBL_GER_EXPIRACAO_FRANQUIA " +
        "  WHERE DAT_EXPIRACAO = ?          " ;
    
    private static final String SQL_VALOR_NAO_EXPIRADO =
        " SELECT SUM(VLR_FRANQUIA) AS VALOR_NAO_EXPIRADO " +
        "   FROM TBL_GER_EXPIRACAO_FRANQUIA              " +
        "  WHERE IDT_MSISDN     = ?                      " +
        "    AND DAT_EXPIRACAO >  ?                      " ;

    private static final String SQL_SELECIONAR_QTD_TENTATIVAS =
        " SELECT QTD_TENTATIVAS           " +
        " FROM TBL_GER_EXPIRACAO_FRANQUIA " +
        "  WHERE IDT_MSISDN           = ? " +
        "    AND DAT_INCLUSAO         = ? " ;

    private static final String SQL_INSERIR_FALHA = 
        " INSERT INTO TBL_GER_EXPIRACAO_FRANQUIA " +
        "     (IDT_MSISDN,                       " +
        "      DAT_INCLUSAO,                     " +
        "      DAT_EXPIRACAO,                    " +
        "      VLR_FRANQUIA,                     " +
        "      DAT_PROCESSAMENTO,                " +
        "      IDT_STATUS_EXPIRACAO,             " +
        "      QTD_TENTATIVAS)                   " +
        " VALUES                                 " +
        "     (?,?,?,?,TRUNC(SYSDATE),?,?)       " ;

    private static final String SQL_ATUALIZAR_FALHA = 
        " UPDATE TBL_GER_EXPIRACAO_FRANQUIA             " +
        "    SET DAT_PROCESSAMENTO    = TRUNC(SYSDATE), " +
        "        IDT_STATUS_EXPIRACAO = ?,              " +
        "        QTD_TENTATIVAS       = ?               " +
        "  WHERE IDT_MSISDN           = ?               " +
        "    AND DAT_INCLUSAO         = ?               " ;
    
	private PREPConexao conexao;
	private long idProcesso;
	
	/**
	 * Contrutor
	 * @param conexao
	 * @param idProcesso
	 */
	public ExpiracaoFranquiaDAO(PREPConexao conexao, long idProcesso)
	{
		this.conexao = conexao;
		this.idProcesso = idProcesso;
	}
	/**
	 * Retorna um ResultSet com as informacoes de todos as Expiracoes a serem efetuadas
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public ResultSet getAssinanteParaExpiracao(java.util.Date dataProcessada) throws GPPInternalErrorException
	{
        Date data = new Date(dataProcessada.getTime());
		Object[] parametros = {data};
		return conexao.executaPreparedQuery(SQL_ASSINANTE_EXPIRACAO, parametros, idProcesso);
	}
	
	/**
	 * Consome a proxima entrada do resultset transformando-o em um VO do tipo ExpiracaoFranquiaVO
     * Tambem faz uma nova selecao em busca do valor ainda nao expirado, com base no MSISDN e
     * na dataProcessada (Que deve ser igual a dataExpiracao com base na caracteristica
     * do select SQL_ASSINANTE_EXPIRACAO). 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 * @throws GPPInternalErrorException 
	 */
	public ExpiracaoFranquiaVO getExpiracaoFranquiaVO(ResultSet resultSet) throws SQLException, GPPInternalErrorException
	{
        ExpiracaoFranquiaVO vo = null;
		// Avanca o ResultSet e testa se existe o próximo registro
		if (resultSet.next())
		{
            String msisdn = resultSet.getString("IDT_MSISDN");
            Date dataExpiracao = resultSet.getDate  ("DAT_EXPIRACAO");
            double valorNaoExpirado = this.getValorNaoExpirado(msisdn, dataExpiracao);
            vo = new ExpiracaoFranquiaVO(
					msisdn,
                    resultSet.getInt   ("IDT_STATUS_EXPIRACAO"),
                    resultSet.getDate  ("DAT_INCLUSAO"),
                    resultSet.getDate  ("DAT_PROCESSAMENTO"),
                    dataExpiracao,
                    resultSet.getInt   ("QTD_TENTATIVAS"),
					resultSet.getDouble("VLR_FRANQUIA"),
                    valorNaoExpirado
                    );
 
		}
		return vo;
	}
	
    /**
     * Retorna o valor total das franquias nao expiradas de um dado assinante.
     * @param msisdn
     * @param dataProcessamento
     * @return
     * @throws GPPInternalErrorException
     */
     private double getValorNaoExpirado(String msisdn, Date dataProcessamento) throws GPPInternalErrorException
     {
         Object parametro[] = { msisdn, dataProcessamento };
         ResultSet resultSet;
         double valorNaoExpirado = 0.0;
         resultSet = conexao.executaPreparedQuery(SQL_VALOR_NAO_EXPIRADO, parametro, idProcesso);
         try
         {
             if (resultSet.next())
                 valorNaoExpirado = resultSet.getDouble("VALOR_NAO_EXPIRADO");
             resultSet.close();
         }
         catch (SQLException e)
         {
             throw new GPPInternalErrorException("Falha ao obter o valor da franquia nao expirada. Erro SQL: "+e.toString());
         }
         return valorNaoExpirado;
     }

     /**
      * Retorna o numero de tentativas com falha para uma dada expiracao.
      * @param vo
      * @return
      * @throws GPPInternalErrorException
      */
      public int getQtdTentativas(ExpiracaoFranquiaVO vo) throws GPPInternalErrorException
      {
          int qtdTentativas = 0;
          Object parametro[] = { vo.getMsisdn(), vo.getDatInclusao() };
          // Seleciona um identificador para p registro de recarga
          ResultSet resultSet;
          resultSet = conexao.executaPreparedQuery(SQL_SELECIONAR_QTD_TENTATIVAS, parametro, idProcesso);
          try
          {
              if (resultSet.next())
                  qtdTentativas = resultSet.getInt("QTD_TENTATIVAS");
              resultSet.close();
          }
          catch (SQLException e)
          {
              throw new GPPInternalErrorException("Falha ao obter a quantidade de tentativas. Erro SQL: "+e.toString());
          }
          return qtdTentativas;
      }

     /**
      * Atualiza o numero de tentativa com erro para uma dada expiracao
      * @param vo
      * @throws GPPInternalErrorException
      */
     public void atualizarNumeroDeTentativas(ExpiracaoFranquiaVO vo) throws GPPInternalErrorException
     {
         Object parametro[] = {
                 new Integer(vo.getIdtStatusExpiracao()),
                 new Integer(vo.getQtdTentativas()),
                 vo.getMsisdn(),
                 vo.getDatInclusao()};
         // Seleciona um identificador para p registro de recarga
         int ocorrencia = conexao.executaPreparedUpdate(SQL_ATUALIZAR_FALHA, parametro, idProcesso);

         // caso o registro ainda nao existe (caso quase impossivel)
         if (ocorrencia < 1)
         {
               Object parametroTmp[] = {
                       vo.getMsisdn(),
                       vo.getDatInclusao(),
                       vo.getDatExpiracao(),
                       new Double(vo.getVlrFranquia()),
                       new Integer(vo.getIdtStatusExpiracao()),
                       new Integer(vo.getQtdTentativas())};
               parametro = parametroTmp;
               conexao.executaPreparedUpdate(SQL_INSERIR_FALHA, parametro, idProcesso);
         }
     }

     /**
        * Remove a ocorrencia de falha sem remorer o registro.
        * Guarda a informacao do proximo estado com sucesso.
        * @param vo
        * @throws GPPInternalErrorException
        */
       public void removerFalha(ExpiracaoFranquiaVO vo) throws GPPInternalErrorException
       {
           Object parametro[] = {
                   new Integer(vo.getIdtStatusExpiracao()),
                   new Integer(0), // Reinicia o numero de falhas
                   vo.getMsisdn(),
                   vo.getDatInclusao()};
           conexao.executaPreparedUpdate(SQL_ATUALIZAR_FALHA, parametro, idProcesso);
    }
}
