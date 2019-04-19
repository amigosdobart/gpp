package com.brt.gpp.aplicacoes.promocao.gerentePromocao;

import java.util.Calendar;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe consumidora responsavel pela execucao do processo de gerenciamento de promocoes de assinantes pendentes de
 *	primeira recarga. O processo verifica dentre os assinantes que estao pendentes de primeira recarga quem a 
 *	realizou. Os que tiverem realizado sao alterados para o status normal.
 *
 *	@author		Daniel Ferreira
 *	@since		07/06/2006
 *
 *  Classe alterada para substituição da FNC_FEZ_RECARGA do Banco de Dados por novas rotinas nas classes Java.
 *  @author     Jorge Abreu
 *  @since      28/08/2007 
 */
public final class GerenciadorPendenciaRecargaConsumidor extends ControlePromocao implements ProcessoBatchConsumidor
{

    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    /**
     *	Construtor da classe.
     */
	public GerenciadorPendenciaRecargaConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerenciadorPendenciaRecargaConsumidor.class).getIdProcesso(Definicoes.CL_PROMOCAO_GER_PEND_REC_PROD), 
		      Definicoes.CL_PROMOCAO_GER_PEND_REC_CONS);
		
		this.conexaoPrep = null;
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
	public void startup() throws Exception
	{
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
     */
	public void execute(Object obj) throws Exception
	{
		short retorno = -1;
		
		try
		{
			PromocaoAssinante pAssinante = (PromocaoAssinante)obj;
		    
			if(pAssinante != null)
			{
				PromocaoStatusAssinante status = 
					super.consulta.getPromocaoStatusAssinante(new Integer(PromocaoStatusAssinante.ATIVO));
				
				retorno = super.trocarStatusPromocaoAssinante(pAssinante, 
															  status, 
															  Calendar.getInstance().getTime(), 
															  this.conexaoPrep);
			}
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", obj + " - Excecao: " + e);
		    retorno = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
		    super.log(Definicoes.INFO, 
		    		  "GerenciadorPendenciaRecarga.execute", 
					  obj + " - Codigo de retorno da operacao: " + retorno);
		}
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
	public void finish()
	{
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	    this.conexaoPrep = produtor.getConexao();
	}
	
}