// Definicao do Pacote
package com.brt.gpp.aplicacoes.contabilidade;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Imports de java
import java.sql.ResultSet;

/**
  * Essa classe refere-se ao processo de consolidação Contábil:
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				19/09/2005
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class Contabilidade extends Aplicacoes{
	
	protected GerentePoolBancoDados gerenteBanco = null;
	protected String dataReferencia = null;
	protected String periodoContabil = null;
	protected String dataInicialProcesso = null;
	protected String dataFinalProcesso = null;
	
	/**
	 * Metodo...: Contabilidade
	 * Descricao: Construtor
	 * @param long		aIdProcesso		ID do processo
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public Contabilidade(long aIdProcesso, String nomProcesso, String parametroData) throws GPPInternalErrorException
	{
		//Define parâmetros de Log
		super(aIdProcesso, nomProcesso);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);
		
		// Inicializa a data de referencia/Periodo Contábil
		this.dataReferencia = parametroData;
		this.periodoContabil = this.determinaPeriodoContabil();
	}

	/**
	 * Metodo...: cicloFechado
	 * Descricao: Verifica se um ciclo contábil está fechado
	 * @param	String		periodoContabil (mm/yyyy)
	 * @return boolean  	true - Periodo Fechado
	 */	
	public boolean cicloFechado() throws GPPInternalErrorException
	{
		boolean retorno;
		ConexaoBancoDados DBConexao = null;

		try
		{
			//Pega conexão com Banco de Dados
			DBConexao = this.gerenteBanco.getConexaoPREP(super.getIdLog());

			// Verifica se periodo está fechado
			String sqlPeriodoContabil = "SELECT IND_FECHADO FROM TBL_CON_PERIODO_CONTABIL WHERE " +
					"IDT_PERIODO_CONTABIL = ?";
			
			Object[] parPeriodoContabil = {this.periodoContabil};
			
			ResultSet rsPeriodoContabil = DBConexao.executaPreparedQuery(sqlPeriodoContabil, parPeriodoContabil, super.getIdLog());
			
			if(rsPeriodoContabil.next())
			{
				retorno = rsPeriodoContabil.getInt("IND_FECHADO")==1?true:false; 
			}
			else
			{
				// Caso o período contábil não se encontre cadastrado, será considerado aberto
				retorno = false;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "cicloFechado", "Erro Banco Dados: "+e);
			throw new GPPInternalErrorException("Erro GPP: "+e);
		}
		finally
		{
			this.gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		return retorno;	
	}
	
	/**
	 * Método...: determinaPeriodoContabil
	 * Descricao: Dada uma data, retorna o respectivo período contábil 
	 * @return String	Período Contábil
	 */
	private String determinaPeriodoContabil() throws GPPInternalErrorException
	{
		String retPeriodoContabil = this.dataReferencia.substring(3);
		
		// Se o dia de execução for maior que 28, considera-se o periodo contabil do mes seguinte
		if(Integer.parseInt(this.dataReferencia.substring(0,2)) > 28)
		{
			retPeriodoContabil = GPPData.primeiroDiaMesSeguinte(this.dataReferencia).substring(3);
			
		}
			
		return retPeriodoContabil;
	}
}
