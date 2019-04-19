package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela execucao do processo de Consolidacao Contabil Diaria
 *
 *	@author	Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 *  Criado em 23/11/2007
 */
public final class ConsolidacaoContabilDiariaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    /**
     *	Conexao com o banco de dados. Uma para cada consumidor
     */
    private PREPConexao conexaoPrep;
    private ConsolidacaoContabilDiariaDAO dao;
    
	public ConsolidacaoContabilDiariaConsumidor()
	{
		super(GerentePoolLog.getInstancia(ConsolidacaoContabilDiariaConsumidor.class).getIdProcesso(
				"ConsolidacaoContabilDiariaConsumidor"), 
				"ConsolidacaoContabilDiariaConsumidor");
		
		this.conexaoPrep = null;
	}

	//Implementacao de Consumidor.
    /**
     *	Executa o processo de consolidacao contabil por dia para cada cada VO passado, procssa a concessao.
     *
     *	@param		obj		VO a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
	 *  @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.INFO, "execute", "Inicio do processamento. VO: " + obj);
		ConsolidacaoContabilDiariaVO vo = (ConsolidacaoContabilDiariaVO) obj;
		try
		{
			// Preenche tabela de CONSOLIDACAO MENSAL para acessos GSM
			this.dao.rodaQueryConsolidacao("SP", vo);
            super.log(Definicoes.INFO, "execute", "Concluido processamento para SP. VO: "+obj);
            
			this.dao.rodaQueryConsolidacao("SB", vo);
            super.log(Definicoes.INFO, "execute", "Concluido processamento para SB. VO: "+obj);
            
			this.dao.rodaQueryConsolidacao("SS", vo);
            super.log(Definicoes.INFO, "execute", "Concluido processamento para SS. VO: "+obj);
            
			this.dao.rodaQueryConsolidacao("SD", vo);
            super.log(Definicoes.INFO, "execute", "Concluido processamento para SD. VO: "+obj);
            
			this.dao.rodaQueryConsolidacao("SF", vo);
            super.log(Definicoes.INFO, "execute", "Concluido processamento para SF. VO: "+obj);

		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Erro no processamento. VO: " + obj + ". Excecao: " + e);
		}
		finally
		{
		    super.log(Definicoes.INFO, "execute", "Fim do processamento. VO: "+obj);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		//Liberando a conexao
		this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,super.logId);
	}

	//Implementacao de ProcessoBatchConsumidor.

	/**
	 *  @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		this.startup((ProcessoBatchProdutor) produtor);
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		this.startup();
	}
	
    /**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		this.dao = new ConsolidacaoContabilDiariaDAO(this.conexaoPrep,super.logId);
	}
}