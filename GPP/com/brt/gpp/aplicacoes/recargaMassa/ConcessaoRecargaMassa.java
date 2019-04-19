package com.brt.gpp.aplicacoes.recargaMassa;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela aprovação / rejeição de lote de recarga em massa
 *
 *	@author	Bernardo Vergne Dias
 *	Data:	09/08/2007
 */
public class ConcessaoRecargaMassa extends Aplicacoes
{
	/**
     *	Construtor da classe.
     *
     *	@param idLog Identificador de LOG.
     */
	public ConcessaoRecargaMassa(long logId)
	{
		super(logId, Definicoes.CL_CONCESSAO_RECARGA_MASSA);
	}
	
	/**
	 * <p><b>Metodo...:</b> aprovarLoteRecarga
	 * <p><b>Descricao:</b> Aprova um lote de recarga em massa
	 * @param numLote Identificador do lote
	 * @param usuario Usuario responsavel pela aprovação
	 */
	public short aprovarLoteRecarga(String numLote, String usuario)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
	    
		PREPConexao conexaoPrep = null;
	    super.log(Definicoes.INFO, "aprovarLoteRecarga", "Inicio.");
				
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    RecargaMassaDAO dao = new RecargaMassaDAO(super.getIdLog());
		    dao.aprovarLote(numLote, usuario, conexaoPrep);
	    }
	    catch (Exception e)
	    {
	        super.log(Definicoes.ERRO, "aprovarLoteRecarga", "Lote: "+numLote+". Erro: " + e.getMessage());
	        retorno = Definicoes.RET_ERRO_TECNICO;
	    }
	    finally
	    {
	    	gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
	    }
	
	    super.log(Definicoes.INFO, "aprovarLoteRecarga", "Fim.");
		return retorno;
	}
	
	/**
	 * <p><b>Metodo...:</b> rejeitarLoteRecarga
	 * <p><b>Descricao:</b> Rejeita um lote de recarga em massa
	 * @param numLote Identificador do lote
	 * @param usuario Usuario responsavel pela rejeição
	 */
	public short rejeitarLoteRecarga(String numLote, String usuario)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
	    
		PREPConexao conexaoPrep = null;
	    super.log(Definicoes.INFO, "rejeitarLoteRecarga", "Inicio.");
				
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    RecargaMassaDAO dao = new RecargaMassaDAO(super.getIdLog());
		    dao.rejeitarLote(numLote, usuario, conexaoPrep);
	    }
	    catch (Exception e)
	    {
	        super.log(Definicoes.ERRO, "rejeitarLoteRecarga", "Lote: "+numLote+". Erro: " + e.getMessage());
	        retorno = Definicoes.RET_ERRO_TECNICO;
	    }
	    finally
	    {
	    	gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
	    }
	
	    super.log(Definicoes.INFO, "rejeitarLoteRecarga", "Fim.");
		return retorno;
	}
}
