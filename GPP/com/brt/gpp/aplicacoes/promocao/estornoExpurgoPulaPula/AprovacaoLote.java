// Definicao do Pacote
package com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula;

//Arquivos de Java

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.persistencia.Operacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
  *
  * Realiza aprovação / rejeição de prévia de estorno/expurgo de bonus pula-pula
  *
  * <P> Versao		:	1.0
  *
  * @Autor				:	Bernardo Vergne Dias
  * <p>Data				:	21/12/2006
  * 
  * 
  * <p>Modificado por 	:
  * <p>Data 			:
  * <p>Razao 			:
  * 
  */
public class AprovacaoLote extends Aplicacoes 
{	
	/**
	 * <p><b>Metodo...:</b> AprovacaoLote
	 * <p><b>Descricao:</b> Construtor 
	 * @param aIdProcesso	- Identificador do processo
	 */
	public AprovacaoLote(long aIdProcesso) 
	{
		
		// Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_APROVACAO_LOTE);	
	}
	
	/**
	 * Por: Bernardo Vergne Dias
	 * <p><b>Metodo...:</b> aprovarLote
	 * <p><b>Descricao:</b> Aprova um lote de estorno/expurgo pula-pula 
	 * @param lote Identificador do lote
	 */
	public short aprovarLote(String lote)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
	    
		PREPConexao conexaoPrep = null;
	    super.log(Definicoes.INFO, "aprovarLote", "Inicio.");
				
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    Operacoes opr = new Operacoes(super.getIdLog());
		    opr.aprovarLote(lote, conexaoPrep);
	    }
	    catch (Exception e)
	    {
	        super.log(Definicoes.ERRO, "aprovarLote", "Lote: "+lote+". Erro: " + e.getMessage());
	        retorno = Definicoes.RET_ERRO_TECNICO;
	    }
	    finally
	    {
	    	gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
	    }
	
	    super.log(Definicoes.INFO, "aprovarLote", "Fim.");
		return retorno;
	}
	
	/**
	 * Por: Bernardo Vergne Dias
	 * <p><b>Metodo...:</b> rejeitarLote
	 * <p><b>Descricao:</b> Rejeita um lote de estorno/expurgo pula-pula 
	 * @param lote Identificador do lote
	 */
	public short rejeitarLote(String lote)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		PREPConexao conexaoPrep = null;
	    super.log(Definicoes.INFO, "rejeitarLote", "Inicio");
				
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    Operacoes opr = new Operacoes(super.getIdLog());
		    opr.rejeitarLote(lote, conexaoPrep);
	    }
	    catch (Exception e)
	    {
	        super.log(Definicoes.ERRO, "rejeitarLote", "Lote: "+lote+". Erro : " + e.getMessage());
	        retorno = Definicoes.RET_ERRO_TECNICO;
	    }
	    finally
	    {
	    	gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
	    }
	    
	    super.log(Definicoes.INFO, "rejeitarLote", "Fim");		
	    return retorno;
	}
	
}