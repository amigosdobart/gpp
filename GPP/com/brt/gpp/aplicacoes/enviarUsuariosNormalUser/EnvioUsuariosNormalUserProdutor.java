package com.brt.gpp.aplicacoes.enviarUsuariosNormalUser;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Este arquivo contem a definicao da classe de EnvioUsuariosNormalUserProdutor.
 * Responsável pela seleção dos usuários que entraram no status NormalUser 
 * para que eles sejam processados.
 * 
 * @Autor:		Magno Batista Correa
 * @since:      20070711
 * @version:    1.0
 */

public class EnvioUsuariosNormalUserProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private String 		dataProcessamento;
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

	// Recuros utilizados no produtor
	private ResultSet 	listaUsuarios;
	private PREPConexao	conexaoPrep;

	public EnvioUsuariosNormalUserProdutor (long logId)
	{
		  super(logId, Definicoes.CL_ENVIO_USUARIO_NORMAL_USER);
	}
	
	/**
	 * Metodo....:validaParametro
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void validaParametro(String params[]) throws GPPInternalErrorException
	{
		// Para o processo batch de envio de usuarios em shutdown o unico
		// parametro utilizado eh a data de processamento. Portanto a verificacao
		// feita nos parametros sao em relacao a esta data sendo que basta ser
		// uma data valida
		if (params == null || params.length == 0 ||params[0] == null)
			throw new GPPInternalErrorException("Parametro de data (dd/mm/aaaa) obrigatorio para o processo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data invalida ou esta em formato invalido. Valor: "+params[0]);
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String params[]) throws Exception
	{
		// Executa a verificacao de parametros
		validaParametro(params);
		super.log(Definicoes.INFO, "Produtor.startup", "Inicio DATA: "+dataProcessamento);
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa dos
		// assinantes que entram no status NORMAL_USER vindos de RECHARGE_EXPIRED. O resultSet é atualizado ficando 
		// disponivel para as threads consumidoras atraves do metodo next
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		// Seleciona os usuários que passaram de Recarga Expirada para NormalUser
		String sql = 
			" SELECT HOJE.MSISDN,                                                   " +
			"        HOJE.IDT_PLANO_PRECO,                                          " +
			"        PRO.IDT_PROMOCAO                                               " +
			" FROM                                                                  " +
			"     (                                                                 " +
			"     SELECT ass.sub_id              as MSISDN,                         " +
			"            ass.profile_id          as IDT_PLANO_PRECO                 " +
			"       FROM tbl_apr_assinante_tecnomen ass                             " +
			"      WHERE ass.account_status       = ?                               " +
			"        AND ass.dat_importacao       = TO_DATE (?, 'dd/mm/yyyy')       " +
			"     ) HOJE,                                                           " +
			"     (                                                                 " +
			"     SELECT ass.sub_id              as MSISDN                          " +
			"       FROM tbl_apr_assinante_tecnomen ass                             " +
			"      WHERE ass.account_status       = ?                               " +
			"        AND ass.dat_importacao       = TO_DATE (?, 'dd/mm/yyyy')-1     " +
			"     ) ONTEM,                                                          " +
			"     TBL_PRO_ASSINANTE PRO                                             " +
			"                                                                       " +
			" WHERE HOJE.MSISDN      = ONTEM.MSISDN                                 " +
			"   AND PRO.IDT_MSISDN   = HOJE.MSISDN                                  " ;
		
		Object param[] = {new Integer(Definicoes.NORMAL)
						 ,dataProcessamento
						 ,new Integer(Definicoes.RECHARGE_EXPIRED)
						 ,dataProcessamento};
		listaUsuarios = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws GPPInternalErrorException
	{
		UsuariosNormalUserVO vo = null;
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que irá armazenar seus dados
			if (listaUsuarios.next())
			{
				vo = new UsuariosNormalUserVO(listaUsuarios.getString("MSISDN"),
													listaUsuarios.getInt("IDT_PLANO_PRECO"),
													listaUsuarios.getInt("IDT_PROMOCAO"),
													dataProcessamento);
				numRegistros++;
			}
		}
		catch(SQLException e)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro: " + e);
			throw new GPPInternalErrorException(e.toString());
		}
		return vo;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_USUARIO_NORMAL_USER ;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		if(getStatusProcesso().equals(Definicoes.PROCESSO_SUCESSO))
			return "Foram registrados " + numRegistros + " usuarios em NormalUser em todas promoções.";
		return "Erro ao executar extracao de usuarios em status de NORMAL USER.";
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// Fecha o ResultSet
		this.listaUsuarios.close();
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());

		super.log(Definicoes.INFO, "Produtor.finish", "Fim");
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return dataProcessamento;
	}


	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso()
	{
		return statusProcesso;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
    }
}
