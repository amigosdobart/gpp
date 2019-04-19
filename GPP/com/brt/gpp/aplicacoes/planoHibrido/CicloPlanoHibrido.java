package com.brt.gpp.aplicacoes.planoHibrido;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
  *
  * Este arquivo refere-se a classe CicloPlanoHibrido,
  * responsavel pela implementacao da logica de manutencao 
  * do saldo de assinantes de planos hibridos na plataforma 
  * Tecnomen e no banco de dados do GPP
  *
  * @autor  Camile Cardoso Couto
  * @since 	24/03/2004
  *
  * @author Joao Paulo Galvagni Junior
  * @since  01/04/2008
  * @modify Alteracao do metodo atualizaCicloPlanoHibrido para receber um
  * 		Assinante como parametro, para que nao seja necessaria uma nova
  * 		consulta de Assinante na Tecnomen
  */
public final class CicloPlanoHibrido extends Aplicacoes
{
	// Gerente de conexoes Tecnomen
	protected GerentePoolTecnomen 	gerenteTecnomen = null;
	
	/**
	 * Descricao: Construtor que recebe o Id para efeitos de log
	 * @param 	long	logId	Id do Log
	 */
	 public CicloPlanoHibrido (long logId)
	 {
		super(logId, Definicoes.CL_CICLO_PLANO_HIBRIDO);
		
		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
	 }
	 
	/**
	  * Descricao: Faz o calculo das recargas efetuadas pelo hibrido no periodo
	  * 
	  * @param	String	aMSISDN					- Numero do Assinante que deve ter as recargas somadas
	  * @param PREPConexaoPrep	aConexaoPrep	- Conexao Prep a ser utlizada
	  * @return	double - Somatorio das recargas
	 *  @throws GPPInternalErrorException
	 */
	 public Object[] calculaRecarga(String aMSISDN, PREPConexao aConexaoPrep) throws GPPInternalErrorException
	 {
		super.log(Definicoes.DEBUG, "calculaRecarga", "Inicio MSISDN "+aMSISDN);
		
		double somaRecargas = 0;
		ResultSet rs2 = null;
		Timestamp datUltimaRecargaProcessada=null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DATA_VITRIA);
		SimpleDateFormat sdfPort = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
	 	
		try
		{
			// Calcula as recargas e ajustes efetuados no periodo pelo assinante (no saldo principal)
			String sql = " SELECT SUM(VLR_CREDITO_PRINCIPAL), 						" +
						 "		  MAX(A.DAT_ORIGEM)+ 1/24/60/60 					" +
						 "   FROM TBL_REC_RECARGAS A,								" +
						 " 		  TBL_APR_PLANO_HIBRIDO B							" +
						 "  WHERE A.IDT_MSISDN = ? 									" +
						 "    AND  A.DAT_ORIGEM > B.DAT_ULTIMA_RECARGA_PROCESSADA	" +
						 "    AND A.IDT_MSISDN = B.IDT_MSISDN						" ;
			
			Object paramRecarga[] = {aMSISDN};
			
			rs2 = aConexaoPrep.executaPreparedQuery2(sql, paramRecarga, super.getIdLog());
			if (rs2.next())
			{
				somaRecargas = rs2.getDouble(1);
				String s_datUltimaRecargaProcessada = rs2.getString(2);
				
				if(s_datUltimaRecargaProcessada == null)
					datUltimaRecargaProcessada = new Timestamp(sdfPort.parse(GPPData.dataCompletaForamtada()).getTime());
				else
					datUltimaRecargaProcessada = new Timestamp(sdf.parse(s_datUltimaRecargaProcessada).getTime());
			}
			
			rs2.close();
			rs2 = null;
		}
		catch(SQLException eSQL)
		{
			super.log(Definicoes.ERRO,"calculaRecarga","Erro na sumarização de recargas");
			throw new GPPInternalErrorException("Erro na sumarização de recargas:"+eSQL);
		}
		catch(ParseException eParse)
		{
			super.log(Definicoes.ERRO,"calculaRecarga","Erro de Formatação de Strings/Datas");
			throw new GPPInternalErrorException("Erro de Formatação de Strings/Datas:"+eParse);		
		}
		super.log(Definicoes.DEBUG, "calculaRecarga", "Fim RETORNO:"+somaRecargas);

		// Constrói o array de retorno
		Object[] retorno = {new Double(somaRecargas), datUltimaRecargaProcessada};
	 	
		return retorno;
	}
	/**
	  * Descricao: calcula o carryOver e o Drop
	  * 
	  * @param 	String		aMSISDN			Msisdn do usuário
	  * @param 	double		totalRecarga	Soma das recarga/ajustes que o usuário tem no período
	  * @param 	double		saldoFinal		Saldo atual do usuário (lido da plataforma)
	  * @param 	PREPConexao	aConexaoPrep	Conexão com banco de dados
	  * @return	Object[]					[0] - drop
	  * 									[1] - carry Over
	  * 									[3] - saldo do assinante
	  * 									[4] - data da última recarga do assinante
	  * @throws GPPInternalErrorException
	  */
	public Object[] calculaCarryOver_Drop(Assinante dadosAssinante, PREPConexao aConexaoPrep) throws GPPInternalErrorException
	{
		double saldoInicial=0;
 		double carryOver=0;
	 	double credFatura=0;
	 	double credDrop=0;
 		
		// Consulta o Saldo Final do usuário
		double saldoFinal = dadosAssinante.getCreditosPrincipal();
	 	
	 	// Soma todas as recargas/ajustes do assinante no período
	 	Object[] processamentoRecargas = this.calculaRecarga(dadosAssinante.getMSISDN(), aConexaoPrep);
		double totalRecarga = ((Double) processamentoRecargas[0]).doubleValue();
		Timestamp dataUltimaRecargaProcessada = (Timestamp) processamentoRecargas[1];
	 	
	 	// Seleciona na tabela de hibridos os valores de saldo inicial, carry over
 		// e cred fatura para esse msisdn
 		String sql = "SELECT VLR_SALDO_INICIAL, 	" +
 					 " 		 VLR_CRED_CARRY_OVER,	" +
 					 "		 VLR_CRED_FATURA 		" +
 					 "  FROM TBL_APR_PLANO_HIBRIDO	" +
 					 " WHERE IDT_MSISDN = ?			" ;
 		
 		Object param[] = {dadosAssinante.getMSISDN()};
 		ResultSet rs3 = aConexaoPrep.executaPreparedQuery3(sql, param, super.getIdLog());
 		
 		// Pega o saldoInicial, o carry over e o crédito da fatura do assinante
 		try
		{
 	 		if (rs3.next())
 	 		{
 	 			saldoInicial = rs3.getDouble(1);
 	 			carryOver = rs3.getDouble(2);
 	 			credFatura = rs3.getDouble(3);
 	 		}
 	 		
 	 		rs3.close();
		}
 		catch (SQLException sqlE)
		{
 			super.log(Definicoes.ERRO,"calculaCarryOver_Drop","Problemas ao ler TBL_APR_PLANO_HIBRIDO");
 			throw new GPPInternalErrorException("Problemas ao ler TBL_APR_PLANO_HIBRIDO: "+sqlE);
		}
 		
 		// Calcula gasto do periodo
 		double gasto = (saldoInicial + totalRecarga) - saldoFinal;
 		
 		// se o cliente ja gastou o carryOver, carryOver deve ser 0
 		// e nao tem ajuste negativo	 		
 		if ( gasto > (carryOver + credFatura) )
 		{
 			credDrop = 0;
 			carryOver = 0;
 		}
 		else
 		{
 			// Se o cliente ainda tem saldo de carryOver, o ajuste negativo
 			// deve ser o valor que sobrou de carryOver menos o que o cliente gastou
 			// e o carryOver deve passar a ser o valor da fatura
 			if ( (carryOver - gasto) >= 0 )
 			{
 				credDrop = carryOver - gasto;
 				carryOver = credFatura;
 			}
 			// Se o carryOver menos o gasto nao for maior que 0, 
 			// o carryOver deve ser o valor da fatura mais o valor do carryOver menos o gasto
 			// e nao deve ter ajuste negativo
 			else
 			{
 				credDrop = 0;
 				carryOver = (credFatura + carryOver) - gasto;
 			}
 		}
 		
 		// retorna credDrop e carryOver
 		Object[] retorno = {new Double(credDrop), new Double(carryOver), new Double(saldoFinal), dataUltimaRecargaProcessada};
 		return retorno;
	 }
	 
	 /**
	 * Metodo...: cicloExecutado
	 * Descricao: Consulta se o ciclo já foi executado 
	 * 
	 * @param String		aMSISDN 		- Numero que deve ter o ciclo rodado
	 * @param int			codRecarga 		- Identificador da recarga
	 * @return	boolean - false caso o ciclo não tenha sido executado, true caso tenha
	 * @throws GPPInternalErrorException
	 */
	 public boolean cicloExecutado (String aMSISDN, int codRecarga) throws GPPInternalErrorException
	 {
		 super.log(Definicoes.DEBUG, "consultaCicloPlanoHibrido", "Inicio MSISDN "+aMSISDN);
		 PREPConexao aConexaoPrep = super.gerenteBancoDados.getConexaoPREP(getIdLog());
		 boolean retorno	= true;
		 
		 try
		 {			
		 	// Seleciona o MSISDN a rodar o ciclo na tabela de planos hibridos
		 	String sql = "SELECT NUM_MES_EXECUCAO 		" +
		 				 "  FROM TBL_APR_PLANO_HIBRIDO	" +
		 				 " WHERE IDT_MSISDN = ?			" ;
		 	
		 	Object param[] = {aMSISDN};
			
		 	ResultSet rs1 = aConexaoPrep.executaPreparedQuery1(sql, param, super.getIdLog());
		 	
			if (rs1.next())
			{
				// Se o identificador da recarga (codRecarga) for maior que o existente na tbl_apr_plano_hibrido no campo NUM_MES_EXECUCAO
				if (codRecarga > rs1.getInt("NUM_MES_EXECUCAO"))
					retorno = false;
				
				rs1.close();
			}
		 }
		 catch (SQLException e1)
		 {
			 super.log(Definicoes.ERRO, "consultaCicloPlanoHibrido", "Excecao SQL:"+ e1);				
			 throw new GPPInternalErrorException ("Excecao SQL:" + e1);			 
		 }
		 finally
		 {
			 super.gerenteBancoDados.liberaConexaoPREP(aConexaoPrep, super.getIdLog());
			 super.log(Definicoes.DEBUG, "consultaCicloPlanoHibrido", "Fim");	    	
		 }
		 
		 return retorno;
	}
	
	/**
	 * Descricao: Faz a manutencao do saldo de acessos dos planos hibridos
	 * 
	 * @param String		aMSISDN 		- Numero que deve ter o ciclo rodado
	 * @param double		valorRecarga	- Valor da Recarga
	 * @param int			codRecarga 		- Identificador da recarga
	 * @param PREPConexao	aConexaoPrep	- Conexão com o banco de dados
	 * @param Date			dataRecarga 	- Data do pagamento da fatura 
	 * @throws GPPInternalErrorException
	 */
	public void atualizaCicloPlanoHibrido (Assinante dadosAssinante, double valorRecarga, int codRecarga, PREPConexao aConexaoPrep, Date dataRecarga) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "atualizaCicloPlanoHibrido", "Inicio MSISDN "+
				  dadosAssinante.getMSISDN()+" ValorRecarga "+valorRecarga);
		
	 	// Grava último registro na tabela de históricos
	 	// Calcula carry over e drop
		Object carryOverDropSaldoFinalDtUltRecarga[] = this.calculaCarryOver_Drop(dadosAssinante, aConexaoPrep);
		Double carryOver 							 = (Double)	   carryOverDropSaldoFinalDtUltRecarga[1];
		Double saldoInicial 						 = (Double)    carryOverDropSaldoFinalDtUltRecarga[2];
		Timestamp dataUltimaRecarga 				 = (Timestamp) carryOverDropSaldoFinalDtUltRecarga[3];
		
		//Atualiza valor dos campos saldo inicial, cred recarga, carry over 
		// e mes execucao na tabela de hibridos		
		String sql = "UPDATE TBL_APR_PLANO_HIBRIDO 				" + 
		 			 "   SET VLR_SALDO_INICIAL   		   = ?, " +
		 			 "	     VLR_CRED_CARRY_OVER 		   = ?, " +
		 			 "		 VLR_CRED_FATURA     		   = ?,	" +
		 			 "		 NUM_MES_EXECUCAO    		   = ?, " + 
		 			 " 		 DAT_CICLO 			 		   = ?, " +
		 			 "		 DAT_ULTIMA_RECARGA_PROCESSADA = ?, " +
		 			 "		 IND_DROP 					   = 0  " + 
		 			 " WHERE IDT_MSISDN = ?						" ;
		
		Object paramUpdate[] = {saldoInicial, carryOver, new Double(valorRecarga), new Integer(codRecarga),
								dataRecarga, dataUltimaRecarga, dadosAssinante.getMSISDN()};
		
		aConexaoPrep.executaPreparedUpdate(sql, paramUpdate, super.getIdLog());
		
 		super.log(Definicoes.DEBUG, "atualizaCicloPlanoHibrido", "Tabela de hibridos atualizada");	
 	}
}