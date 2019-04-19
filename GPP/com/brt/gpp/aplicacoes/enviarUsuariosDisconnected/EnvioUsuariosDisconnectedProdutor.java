package com.brt.gpp.aplicacoes.enviarUsuariosDisconnected;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 *
 * Este arquivo contem a definicao da classe de EnvioUsuariosDisconnectedConsumidor.
 * Responsável pela seleção dos usuários que entraram ou sairam do status Disconnected 
 * para que eles sejam processados pelo Clarify e ASAP e tenham suas promoções retiradas 
 * ou colocadas, assim como o selective bypass.
 * 
 * <P> Versao:	1.0
 *
 * @Autor:		Marcelo Alves Araujo
 * Data:		13/10/2005
 *
 */
public class EnvioUsuariosDisconnectedProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private String 		dataProcessamento;
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

	// Recuros utilizados no produtor
	private ResultSet 	listaUsuarios;
	private PREPConexao	conexaoPrep;

	/**
	 * Metodo....:EnvioUsuariosDisconnectedProdutor
	 * Descricao.:Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public EnvioUsuariosDisconnectedProdutor (long logId)
	{
		super(logId, Definicoes.CL_ENVIO_USUARIO_DISCONNECTED);
	}
	
	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws GPPInternalErrorException
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
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio DATA: "+dataProcessamento);
		
		// Executa a verificacao de parametros
		parseParametros(params);
		
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa dos
		// assinantes que entram no status DISCONNECTED. O resultSet é atualizado ficando 
		// disponivel para as threads consumidoras atraves do metodo next
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		conexaoPrep.setAutoCommit(false);
		
		// Seleciona os usuários que passaram de recarga expirada para desconectado
		// e os usuários que passaram do status desconectado para normal
		String sql = 	"SELECT  " + 
						" TBL_APR1.SUB_ID AS MSISDN,  " + 
						" TBL_APR1.ACCOUNT_STATUS AS STATUS  " + 
						"FROM  " + 
						" (SELECT  " + 
						"   SUB_ID,  " + 
						"   ACCOUNT_STATUS  " + 
						"  FROM  " + 
						"   TBL_APR_ASSINANTE_TECNOMEN a, " + 
						"   tbl_ger_plano_preco b " + 
						"  WHERE  " + 
						"   a.profile_id = b.idt_plano_preco AND " + 
						"   ACCOUNT_STATUS = ? AND  " + 
						"   b.idt_categoria = 0 AND  " + 
						"   DAT_IMPORTACAO = to_date(?,'dd/mm/yyyy')) TBL_APR1,  " + 
						" (SELECT  " + 
						"   SUB_ID,  " + 
						"   ACCOUNT_STATUS  " + 
						"  FROM  " + 
						"   TBL_APR_ASSINANTE_TECNOMEN a, " + 
						"   tbl_ger_plano_preco b " + 
						"  WHERE  " + 
						"   a.profile_id = b.idt_plano_preco AND " + 
						"   ACCOUNT_STATUS = ? AND  " + 
						"   b.idt_categoria = 0 AND  " + 
						"   DAT_IMPORTACAO = to_date(?,'dd/mm/yyyy')-1) TBL_APR2  " + 
						"WHERE  " + 
						" TBL_APR2.SUB_ID = TBL_APR1.SUB_ID   " + 
						"UNION ALL  " + 
						"SELECT  " + 
						" TBL_APR3.SUB_ID AS MSISDN, " + 
						" TBL_APR3.ACCOUNT_STATUS AS STATUS  " + 
						"FROM  " + 
						" (SELECT  " + 
						"   SUB_ID,  " +                  
						"   ACCOUNT_STATUS  " +                  
						"  FROM  " +                  
						"   TBL_APR_ASSINANTE_TECNOMEN a, " +                 
						"   tbl_ger_plano_preco b " +                  
						"  WHERE  " +                  
						"   a.profile_id = b.idt_plano_preco AND " +          
						"   ACCOUNT_STATUS = ? AND  " +                  
						"   b.idt_categoria = 0 AND  " +                  
						"   DAT_IMPORTACAO = to_date(?,'dd/mm/yyyy')) TBL_APR3,  " + 
						" (SELECT  " +                  
						"   SUB_ID,  " +                  
						"   ACCOUNT_STATUS  " +                  
						"  FROM  " +                  
						"   TBL_APR_ASSINANTE_TECNOMEN a, " +                 
						"   tbl_ger_plano_preco b " +                  
						"  WHERE  " +                  
						"   a.profile_id = b.idt_plano_preco AND " +          
						"   ACCOUNT_STATUS = ? AND  " +                  
						"   b.idt_categoria = 0 AND  " +                  
						"   DAT_IMPORTACAO = to_date(?,'dd/mm/yyyy')-1) TBL_APR4  " + 
						"WHERE  " +                  
						" TBL_APR3.SUB_ID = TBL_APR4.SUB_ID";  
		
		Object param[] = {new Integer(Definicoes.DISCONNECTED)
						 ,dataProcessamento
						 ,new Integer(Definicoes.RECHARGE_EXPIRED)
						 ,dataProcessamento
						 ,new Integer(Definicoes.NORMAL)
						 ,dataProcessamento
						 ,new Integer(Definicoes.DISCONNECTED)
						 ,dataProcessamento};
		listaUsuarios = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws GPPInternalErrorException
	{
		UsuariosDisconnectedVO usrDisconnected = null;
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que irá armazenar seus dados
			if (listaUsuarios.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				// O segundo o status do assinante
				// O terceiro o plano de preço
				// O quarto a data de processamento
				usrDisconnected = new UsuariosDisconnectedVO(listaUsuarios.getString(1),listaUsuarios.getInt(2),dataProcessamento);
				numRegistros++;
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro: " + se);
			throw new GPPInternalErrorException(se.toString());
		}
		return usrDisconnected;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_USUARIO_DISCONNECTED;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		if(getStatusProcesso().equals(Definicoes.PROCESSO_SUCESSO))
			return "Foram registrados " + numRegistros + " xmls de usuarios em Disconnected";
		return "Erro ao executar registro";
	}
	
	/**
	 *  (non-Javadoc)
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
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// Deleta todos os registros processados com sucesso 
		limpaProcessoOk();
		// Fecha o ResultSet
		listaUsuarios.close();
		
		// Grava no banco as atualizações
		conexaoPrep.commit();
		
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());

		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return dataProcessamento;
	}

	/**
	 * Metodo....:limpaProcessoOk
	 * Descricao.:Remove registros já processados na tabela de interface
	 * @throws GPPInternalErrorException
	 */
	public void limpaProcessoOk () throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Produtor.limpaProcessoOk", "Inicio");
		
		try
		{
			String idt_processamento_ok = Definicoes.IDT_PROCESSAMENTO_OK;
			
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Deleta da tabela os usuarios processados com sucesso
			String sql_limpa_ppp_out =	"DELETE FROM " +
										" TBL_INT_PPP_OUT " +
								  		"WHERE " +
								  		" IDT_STATUS_PROCESSAMENTO = ? AND " +
								  		" IDT_EVENTO_NEGOCIO IN (?,?) AND " +
								  		" DAT_CADASTRO < (SYSDATE - ?)";
				
			Object paramLimpaOut[] = {idt_processamento_ok
									 ,Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT
									 ,Definicoes.IDT_EVT_SELECTIVE_BYPASS
									 ,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
		 	
		 	int linhasPPPOut = conexaoPrep.executaPreparedUpdate(sql_limpa_ppp_out,paramLimpaOut, super.logId);
		 	super.log(Definicoes.DEBUG,"Produtor.limpaProcessosOk","Deletados "+linhasPPPOut+" Registros da TBL_INT_PPP_OUT");
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "Produtor.limpaProcessoOk", "Erro no limpaProcessoOk");
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
 			super.log(Definicoes.DEBUG, "Produtor.limpaProcessoOk", "Fim");
		}
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
        try
        {
            conexaoPrep.rollback();
        }
        catch(SQLException sqlException)
        {  
        	super.log(Definicoes.ERRO, "handleException", "Erro ao executar o rollback");
        }
    }
}
