package com.brt.gpp.aplicacoes.enviarUsuariosShutdown;

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
 * @author Vanessa Andrade
 * Adaptacao Joao Carlos
 *
 */
public class EnvioUsuariosShutdownProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private String 		dataProcessamento;
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

	// Recuros utilizados no produtor
	private ResultSet 	listaUsuarios;
	private PREPConexao	conexaoPrep;

	/**
	 * Metodo....:EnvioUsuariosShutdownProdutor
	 * Descricao.:Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public EnvioUsuariosShutdownProdutor (long logId)
	{
		super(logId, Definicoes.CL_ENVIO_USUARIO_SHUTDOWN);
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
		if (params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor:"+params[0]);
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String params[]) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "enviaUsuariosShutdown", "Inicio DATA "+dataProcessamento);
		// Executa a verificacao de parametros
		parseParametros(params);
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa dos
		// assinantes que entram no status SHUTDOWN. O resultSet eh atualizado ficando disponivel
		// para as threads consumidoras atraves do metodo next
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		String sql 	=	"SELECT sub_id													" 
					+ 	"  FROM tbl_apr_assinante_tecnomen a, tbl_ger_plano_preco b     " 
					+	" WHERE a.account_status = ?                                    " 
					+	"   AND a.profile_id = idt_plano_preco                          " 
					+	"   AND b.idt_categoria = ?                                     " 
					+	"   AND a.dat_importacao = TO_DATE (?, 'dd/mm/yyyy')          	" 
					+	"   AND EXISTS (                                                " 
					+	"          SELECT 1                                             " 
					+	"            FROM tbl_apr_assinante_tecnomen                    " 
					+	"           WHERE sub_id = a.sub_id                             " 
					+	"             AND dat_importacao = TO_DATE (?, 'dd/mm/yyyy')-1	" 
					+	"             AND account_status != ?)                          ";

		Object param[] = 	{new Integer(Definicoes.STATUS_SHUTDOWN)
				 			,new Integer(Definicoes.CATEGORIA_PREPAGO)
				 			,dataProcessamento
				 			,dataProcessamento
				 			,new Integer(Definicoes.STATUS_SHUTDOWN)};
		
		listaUsuarios = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next()
	{
		UsuariosShutdownVO usrShutdown = null;
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
			if (listaUsuarios.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				usrShutdown = new UsuariosShutdownVO(listaUsuarios.getString(1),Definicoes.S_SHUT_DOWN,dataProcessamento);
				numRegistros++;
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"next","Erro ao processar proximo registro no produtor. Erro"+se);
			usrShutdown = null;
		}
		return usrShutdown;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_USUARIO_SHUTDOWN;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return "Foram registrados " + numRegistros + " xmls de usuarios em Shutdown";
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
		
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());

		super.log(Definicoes.DEBUG, "enviaUsuariosShutdown", "Fim");
	}

	/**
	 * Metodo....:limpaProcessoOk
	 * Descricao.:Remove registros jah processados na tabela de interface
	 * @throws GPPInternalErrorException
	 */
	public void limpaProcessoOk ()
	{
		super.log(Definicoes.DEBUG, "limpaProcessoOk", "Inicio");
		try
		{
			// Busca uma conexao de banco de dados		
	
			String idt_processamento_ok = Definicoes.IDT_PROCESSAMENTO_OK;
			
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Deleta da tabela os usuarios processados com sucesso
			String sql_limpa_ppp_out = "DELETE FROM TBL_INT_PPP_OUT " +
								  "WHERE IDT_STATUS_PROCESSAMENTO = ? AND IDT_EVENTO_NEGOCIO = ? " +
								  " and dat_cadastro < (sysdate - ?)";
				
			// Deleta da TBL_INT_PPP_IN dados que o ETI vai colocar, que não serve para o GPP
			// Mas que o GPP deve apagar
			String sql_limpa_ppp_in = "DELETE FROM TBL_INT_PPP_IN "+
									"WHERE IDT_STATUS_PROCESSAMENTO = ? AND IDT_EVENTO_NEGOCIO = ? " +
									" and dat_cadastro < (sysdate - ?)";
	
			Object paramLimpaIn[] = {idt_processamento_ok, Definicoes.IND_EVENTO_SHUTDOWN_PPP_IN,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			Object paramLimpaOut[] = {idt_processamento_ok, Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
		 	
		 	int linhasPPPOut = conexaoPrep.executaPreparedUpdate(sql_limpa_ppp_out,paramLimpaOut, super.logId);
		 	super.log(Definicoes.DEBUG,"limpaProcessosOk","Deletados "+linhasPPPOut+" Registros da TBL_INT_PPP_OUT");
		 	
		 	int linhasPPPIn = conexaoPrep.executaPreparedUpdate(sql_limpa_ppp_in,paramLimpaIn, super.logId);
			super.log(Definicoes.DEBUG,"limpaProcessosOk","Deletados "+linhasPPPIn+" Registros da TBL_INT_PPP_OUT");
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "limpaProcessoOk", "Erro no limpaProcessoOk");
		}
		finally
		{
			super.log(Definicoes.DEBUG, "limpaProcessoOk", "Fim");
		}
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return dataProcessamento;
    }

    /* (non-Javadoc)
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
}
