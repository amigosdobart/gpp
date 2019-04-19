// Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar;

//Arquivos de Import Internos
import java.util.*;
import java.sql.*;
import java.text.*;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.comum.*;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.conexoes.bancoDados.*;
import com.brt.gpp.aplicacoes.Aplicacoes;

/**
  *
  * Este arquivo refere-se a classe ConsultaHistoricoProcessoBatch, 
  * responsavel pela implementacao da logica de Consulta a tabela de historicos
  * de execucao dos processos batch
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				27/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class ConsultaHistoricoProcessoBatch extends Aplicacoes 
{
	private GerentePoolBancoDados gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	private GerentePoolLog		  Log				= null; // Gerente de LOG
	private Map parametros;
	private String sqlBusca = 
				"SELECT  H.ID_PROCESSO_BATCH    AS ID_PROCESSO_BATCH "		+
					   ",P.NOM_PROCESSO_BATCH   AS NOM_PROCESSO_BATCH "		+
					   ",H.DAT_INICIAL_EXECUCAO AS DAT_INICIAL_EXECUCAO "	+
					   ",H.DAT_FINAL_EXECUCAO   AS DAT_FINAL_EXECUCAO "		+
					   ",H.IDT_STATUS_EXECUCAO  AS IDT_STATUS_EXECUCAO "	+
					   ",H.DES_OBSERVACAO       AS DES_OBSERVACAO "			+
					   ",H.DAT_PROCESSAMENTO    AS DAT_PROCESSAMENTO "		+
				  "FROM  TBL_GER_PROCESSOS_BATCH P "						+
					   ",TBL_GER_HISTORICO_PROC_BATCH H "					+
				 "WHERE P.ID_PROCESSO_BATCH     = H.ID_PROCESSO_BATCH ";
				 
	private String sqlOrder = " ORDER BY H.DAT_INICIAL_EXECUCAO,H.DAT_FINAL_EXECUCAO";
		
	/**
	 * Metodo...: ConsultaHistoricoProcessoBatch
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	public ConsultaHistoricoProcessoBatch(long aLogId)
	{
		super(aLogId, Definicoes.CL_CONSULTA_HISTORICO_PROC_BATCH);
		parametros = new HashMap();
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(aLogId);
		this.Log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 * Metodo...: setIdProcessoBatch
	 * Descricao: Este metodo atribui o valor desejado para o campo IdProcesso
	 * @param  aIdProcesso - Id do processo desejado
	 * @return 
	 */
	public void setIdProcessoBatch(int aIdProcesso)
	{
		parametros.put("AND H.ID_PROCESSO_BATCH = ? ", new Integer(aIdProcesso));
	}

	/**
	 * Metodo...: setDataInicial
	 * Descricao: Este metodo atribui o valor desejado para o campo DataInicial
	 * @param 	aDataInicial  - Data inicial de execucao do processo desejado
	 * @return 
	 */
	public void setDataInicial(java.util.Date aDataInicial)
	{
		if (aDataInicial != null)
			parametros.put("AND H.DAT_INICIAL_EXECUCAO >= ? ", new Timestamp(aDataInicial.getTime()));
	}

	/**
	 * Metodo...: setDataFinal
	 * Descricao: Este metodo atribui o valor desejado para o campo DataFinal
	 * @param 	aDataFinal - Data final de execucao do processo desejado
	 * @return 
	 */	
	public void setDataFinal(java.util.Date aDataFinal)
	{
		if (aDataFinal != null)
			parametros.put("AND H.DAT_FINAL_EXECUCAO <= ? ", new Timestamp(aDataFinal.getTime()));
	}
	
	/**
	 * Metodo...: setStatus
	 * Descricao: Este metodo atribui o valor desejado para o campo Status
	 * @param 	aIdtStatus  - Status de execucao do processo desejado
	 * @return 
	 */
	public void setStatus(String aIdtStatus)
	{
		if (aIdtStatus != null)
			parametros.put("AND H.IDT_STATUS_EXECUCAO = ? ", aIdtStatus);
	}
	
	/**
	 * Metodo...: executaConsulta
	 * Descricao: Este metodo tem a finalidade de executar a consulta a tabela de 
	 * 			  historicos levando em consideracao todos os parametros utilizados
	 * @param 
	 * @return 	String  - Lista dos valores da execucao do processo batch
	 * @throws  GPPInternalErrorException
	 */
	public String executaConsulta() throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "executaConsulta", "Inicio do metodo que executa a consulta a tabela de historicos.");

		String resultadoConsulta="";
		Object paramSQL[] = new Object[parametros.entrySet().size()];
		int posicao = 0;
		for (Iterator i=parametros.entrySet().iterator(); i.hasNext();)
		{
			Map.Entry entry = (Map.Entry)i.next();

			sqlBusca += (String)entry.getKey();
			paramSQL[posicao] = entry.getValue();
			posicao++;
			Log.log(super.getIdLog(), Definicoes.DEBUG, Definicoes.CL_CONSULTA_HISTORICO_PROC_BATCH, "executaConsulta", "Parametro da Consulta: " + (String)entry.getKey() + " Valor: " + entry.getValue());
		}
		
		PREPConexao conexaoPrep = null;		
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			sqlBusca += sqlOrder;
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlBusca,paramSQL,super.getIdLog());
			while (rs.next())
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

				resultadoConsulta += rs.getString("ID_PROCESSO_BATCH") 											+ "#";
				resultadoConsulta += rs.getString("NOM_PROCESSO_BATCH") 										+ "#";
				resultadoConsulta += dateFormat.format((java.util.Date)rs.getTimestamp("DAT_INICIAL_EXECUCAO")) + "#";
				resultadoConsulta += dateFormat.format((java.util.Date)rs.getTimestamp("DAT_FINAL_EXECUCAO")) 	+ "#";
				resultadoConsulta += rs.getString("IDT_STATUS_EXECUCAO") 										+ "#";
				resultadoConsulta += rs.getString("DES_OBSERVACAO")		 										+ "#";
				resultadoConsulta += dateFormat.format((java.util.Date)rs.getTimestamp("DAT_PROCESSAMENTO"))	+ "\n";
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO, "executaConsulta", "Excecao de SQL ocorrida: "+ se);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + se);
		}
		finally
		{
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			super.log(Definicoes.DEBUG, "executaConsulta", "Fim do metodo que executa a consulta a tabela de historicos.");
		}
		return resultadoConsulta;
	}
	
	/**
	 * Metodo...: consultaProcessosBatch
	 * Descricao: Este metodo tem a finalidade de executar a consulta a tabela de 
	 * 			  historicos levando em consideracao todos os parametros utilizados
	 * @param 	aIdProcesso - Identificador do processo
	 * @return 	String  	- Lista dos valores da execucao do processo batch
	 * @throws  GPPInternalErrorException
	 */
	public static String consultaProcessosBatch(long aIdProcesso) throws GPPInternalErrorException
	{
		String resultadoConsulta="";
		PREPConexao conexaoPrep = null;
		GerentePoolBancoDados gerenteBancoDados = GerentePoolBancoDados.getInstancia(aIdProcesso);
		try
		{	 
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			String sqlCons = "SELECT ID_PROCESSO_BATCH, NOM_PROCESSO_BATCH FROM TBL_GER_PROCESSOS_BATCH";
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlCons,null,aIdProcesso);
			while (rs.next())
			{
				resultadoConsulta += rs.getString("ID_PROCESSO_BATCH") 	+ "#";
				resultadoConsulta += rs.getString("NOM_PROCESSO_BATCH")	+ "\n";
			}
		}
		catch(SQLException se)
		{
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + se);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, aIdProcesso);
		}
		return resultadoConsulta;
	}
}
