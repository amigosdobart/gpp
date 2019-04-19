package com.brt.gpp.aplicacoes.importacaoAssinantes.atualizacaoAssinantes;

import java.util.List;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela atualizacao da tabela TBL_APR_ASSINANTE
 *  de acordo com a tabela TBL_APR_SALDO_TECNOMEN
 *
 *	@author	Bernardo Vergne Dias
 *	Data:	27/08/2007
 */
public class AtualizadorAssinantesConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private AtualizadorAssinantesProdutor produtor = null;
	private PREPConexao conexaoPrepConsumidor;		//	Conexão com o banco.
	
	public static final String UPDATE_SALDO_SM =
		"UPDATE   tbl_apr_assinante a									" +
		"SET    ( vlr_saldo_sm, dat_expiracao_sm ) 						" +
		"     = ( 														" +
		"         SELECT balance_amount_0 / 100000, balance_expiry_0   	" +
		"         FROM   tbl_apr_saldo_tecnomen b 						" +
		"         WHERE  b.dat_importacao = ?							" +
		"           and  b.account_type = 1								" +
		"           and  b.user_id = a.id_usuario_tec					" +
		"        )														" +
		"WHERE   a.idt_msisdn like ?									";
	
	public static final String UPDATE_SALDO_DADOS =
		"UPDATE   tbl_apr_assinante a									" +
		"SET    ( vlr_saldo_dados, dat_expiracao_dados ) 				" +
		"     = ( 														" +
		"         SELECT balance_amount_0 / 100000, balance_expiry_0   	" +
		"         FROM   tbl_apr_saldo_tecnomen b 						" +
		"         WHERE  b.dat_importacao = ?							" +
		"           and  b.account_type = 2								" +
		"           and  b.user_id = a.id_usuario_tec					" +
		"        )														" +
		"WHERE   a.idt_msisdn like ?									";	
	
    /**
     *	Construtor da classe.
     */
	public AtualizadorAssinantesConsumidor()
	{
		super(GerentePoolLog.getInstancia(AtualizadorAssinantesConsumidor.class).
				getIdProcesso(Definicoes.CL_ATUALIZADOR_ASSINANTES_PRODUTOR), 
		        Definicoes.CL_ATUALIZADOR_ASSINANTES_CONSUMIDOR);
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
	public void startup() throws Exception
	{
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
	 /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.produtor = (AtualizadorAssinantesProdutor)produtor;

		// seleciona conexão do pool Prep Conexão		
		try
		{
			conexaoPrepConsumidor = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			conexaoPrepConsumidor.setAutoCommit(true);	
		}
		catch(Exception e)
		{
		    produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
		    super.log(Definicoes.ERRO, "startup", "Erro ao abrir conexao. Excecao: " + e);
		    throw e;
		}
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
     */
	public void execute(Object obj) throws Exception
	{
		// O VO é um array de String contendo os parametros do UPDATE
		// params[0] = data importacao
		// params[1] = prefixo msisdn 
		
		List params = (List)obj;

		/**
		 * Processa a atualizacao de assinante
		 */
		try
		{
			long tempo = System.currentTimeMillis();
			
			conexaoPrepConsumidor.executaPreparedUpdate(UPDATE_SALDO_SM, params.toArray(), super.getIdLog());
			conexaoPrepConsumidor.executaPreparedUpdate(UPDATE_SALDO_DADOS, params.toArray(), super.getIdLog());
			
			tempo = System.currentTimeMillis() - tempo;
			produtor.incrementaNumSucessos();
			
			super.log(Definicoes.INFO, "execute", 
					"Atualizacao de '" + params.get(1).toString() + "' concluida em " + tempo / 1000 + 
					"seg. [SUCESSOS: " + produtor.getNumSucessos() + 
					", ERROS: " + produtor.getNumErros() + "]");
			
		}
		catch(Exception e)
		{
			produtor.incrementaNumErros();
			produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "execute", 
					"Erro na atualizacao de '" + params.get(1).toString() + "'. Excecao: " + e);
		}
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
	public void finish()
	{
		// Libera a conexao com o banco
		try
		{
			if (conexaoPrepConsumidor != null)
				conexaoPrepConsumidor.commit();
			gerenteBancoDados.liberaConexaoPREP(conexaoPrepConsumidor, super.getIdLog());
		}
		catch(Exception e)
		{
			produtor.setStatusProcesso(Definicoes.PROCESSO_ERRO);
			super.log(Definicoes.ERRO, "finish",  "Erro ao finalizar conexao. Excecao: " + e);
		}		
	}
}
