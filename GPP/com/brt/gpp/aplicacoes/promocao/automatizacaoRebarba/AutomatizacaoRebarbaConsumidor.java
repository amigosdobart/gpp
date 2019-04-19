package com.brt.gpp.aplicacoes.promocao.automatizacaoRebarba;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela execucao do processo de Automatização da rebarba.
 *
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/19 (yyyy/mm/dd)
 */
public final class AutomatizacaoRebarbaConsumidor extends ControlePulaPula implements ProcessoBatchConsumidor
{
    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    /**
     * Sql para realizar o update na tabela de assinantes com os novos Status e datas de expiração.
     */
    public static final String SQL_UPDATE = 
    	"UPDATE tbl_apr_assinante ASS             " +
    	"    SET                                  " +
    	"    	ASS.idt_status               = ?, " +
    	"        ASS.dat_expiracao_principal = ?, " +
    	"        ASS.dat_expiracao_bonus     = ?, " +
    	"        ASS.dat_expiracao_sm        = ?, " +
    	"        ASS.dat_expiracao_dados     = ?  " +
    	"    WHERE                                " +
    	"    ASS.idt_msisdn = ?                   " ;
    //Construtores.
    
    /**
     *	Construtor da classe.
     */
	public AutomatizacaoRebarbaConsumidor()
	{
		super(GerentePoolLog.getInstancia(AutomatizacaoRebarbaConsumidor.class).getIdProcesso(Definicoes.CL_SUMARIZACAO_PULA_PULA_PROD), 
		      Definicoes.CL_AUTO_REBARBA_CONS);
		
		this.conexaoPrep = null;
	}

	//Implementacao de Consumidor.
    /**
     *	Executa o processo de Auto Rebarba.
     *	<BR> Para cada cada MSISDN altera o status e as datas de expiração.</BR>
     *
     *	@return		obj						VO a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");
		
		try
		{
			AutomatizacaoRebarbaVO VO = (AutomatizacaoRebarbaVO) obj;
			Object[] parametros = new Object[5];
			parametros[0] = new Integer(Definicoes.STATUS_NORMAL_USER);
			parametros[1] = VO.getDatExpPrinipal();
			parametros[2] = VO.getDatExpBonus();
			parametros[3] = VO.getDatExpSMS();
			parametros[4] = VO.getDatExpDados();
			parametros[5] = VO.getMsisdn();
			
			this.conexaoPrep.executaPreparedUpdate(SQL_UPDATE,parametros,this.conexaoPrep.getIdProcesso());
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		    throw e;
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}

	//Implementacao de ProcessoBatchConsumidor.
	
    /**
     *	Inicializa o objeto.
     *
     *	@param		ProcessoBatchProdutor	produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.conexaoPrep = produtor.getConexao();
	}
	
    /**
     *	Inicializa o objeto. Uma vez que a unica operacao necessaria para o startup e a atribuicao do produtor, neste
     *	caso o metodo nao faz nada. 
     *
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
	}

	/**
     *	@param		Produtor				produtor					Produtor de registros para execucao.
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
	}
}