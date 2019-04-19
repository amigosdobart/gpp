//Definicao do Pacote
package com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.aplicacoes.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

/**
  * Essa classe refere-se ao processo de Sumarização de Produtos
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				20/05/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class sumarizacaoProdutoPlano extends Aplicacoes{

	GerentePoolBancoDados gerenteBanco = null;
	String dataReferencia = null;

	/**
	 * Metodo...: sumarizacaoProdutoPlano
	 * Descricao: Construtor
	 * @param long		aIdProcesso		IdProcesso para efeitos de Log
	 * @param String	aDataReferencia	Data de Referencia
	 */
	public sumarizacaoProdutoPlano(long aIdProcesso, String aDataReferencia) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_SUM_PRODUTO_PLANO);

		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
		
		//Registra Data de Referencia
		this.dataReferencia = aDataReferencia;

	}
	
	/**
	 * Metodo...: cruzarAssinantesChamadas
	 * Descricao: Preenche a tabela com os dados a serem cruzados
	 * @param
	 * @return short - Se for sucesso retorna zero (0), caso contrario diferente de zero
	 * @throws GPPInternalErrorException
	 */
	public short cruzarAssinantesChamadas() throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"cruzarAssinantesChamadas","Inicio");

		//Inicialização de variáveis
		short retorno = -1;
		ConexaoBancoDados DBConexao = null;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;

		try
		{
			// Pega a data/hora atual no formato DD/MM/AAAA HH:MM:SS
			dataInicialProcesso = GPPData.dataCompletaForamtada();
				
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());
			
			//Query para selecionar os MMO da TBL_GER_CDR
			String queryMMO = "SELECT TP.DES_TIPO_PRODUTO AS PRODUTO,"+
				"COUNT(*), SUM(CALL_VALUE), TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"FROM TBL_GER_CDR F, TBL_GER_TIPO_PRODUTO TP "+ 
				"WHERE F.TIP_CDR = ? "+ 	// Indicador de Produto MMO ('MO')
				"AND TRANSACTION_TYPE = ? " +	// Indicador de MMO enviado (não é refund), TT = '90'
				"AND F.TIMESTAMP BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1" +
				"AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1 " +
				//"AND F.TIMESTAMP = TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"AND TP.IDT_TIPO_PRODUTO=F.TIP_CHAMADA "+
				"AND CALL_VALUE <>0 "+	//Ignorar CDRs com valor nulo
				"GROUP BY TP.DES_TIPO_PRODUTO";
				
			Object paramMMO[] = {dataReferencia, Definicoes.IND_PROD_MMO, 
				Definicoes.TT_MO_ENVIADO, dataReferencia+" "+Definicoes.HORA_INICIO_DIA, 
				dataReferencia+" "+Definicoes.HORA_FINAL_DIA};
				
			//Query para selecionar os MMT e MMS da TBL_GER_CDR
			String queryMMT_MMS = "SELECT TP.DES_TIPO_PRODUTO AS PRODUTO,"+
				"COUNT(*), SUM(-CALL_VALUE), TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"FROM TBL_GER_CDR F, TBL_GER_TIPO_PRODUTO TP "+ 
				"WHERE (F.TIP_CDR = ? OR F.TIP_CDR = ?) "+	// Produtos MMT ('MT') e MMS ('MMS')
				"AND TRANSACTION_TYPE = ? "+	// TT de MT/MMS enviado ('0')
				"AND F.TIMESTAMP BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1" +
				"AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1 " +
				//"AND F.TIMESTAMP = TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"AND TP.IDT_TIPO_PRODUTO=F.TIP_CHAMADA "+
				"AND CALL_VALUE <>0 "+	//Ignorar cdrs com valores nulos
				"GROUP BY TP.DES_TIPO_PRODUTO";
	
			Object paramMMT_MMS[] = {dataReferencia, Definicoes.IND_PROD_MT, 
				Definicoes.IND_PROD_MMS, Definicoes.TT_MT_MMS_ENVIADO, 
				dataReferencia+" "+Definicoes.HORA_INICIO_DIA, 
				dataReferencia+" "+Definicoes.HORA_FINAL_DIA};
				
			// Query para Totalizar Refunds
			String queryRefund = "SELECT 'ESTORNO', COUNT(*),SUM(CALL_VALUE),"+
				"TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"FROM TBL_GER_CDR F "+
				"WHERE ((F.TIP_CDR = ? AND F.TRANSACTION_TYPE = ?) OR "+	// TT mms estornado = '1'
				"(F.TIP_CDR = ? AND F.TRANSACTION_TYPE = ?) OR "+
				"(F.TIP_CDR = ? AND F.TRANSACTION_TYPE = ?)) "+
				"AND F.TIMESTAMP BETWEEN TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1" +
				"AND TO_DATE(?,'DD/MM/YYYY HH24:MI:SS')-1 " +
				//"AND F.TIMESTAMP = TRUNC(TO_DATE(?,'DD/MM/YYYY')-1) "+ 
				"AND CALL_VALUE <>0";
			
			Object paramRefund[] = {dataReferencia, Definicoes.IND_PROD_MT,Definicoes.TT_MT_MMS_ESTORNADO,
				Definicoes.IND_PROD_MMS, Definicoes.TT_MT_MMS_ESTORNADO, Definicoes.IND_PROD_MMO,
				Definicoes.TT_MO_ESTORNADO, dataReferencia+" "+Definicoes.HORA_INICIO_DIA, 
				dataReferencia+" "+Definicoes.HORA_FINAL_DIA};
			
			//Esqueleto do Insert na TBL_REL_PLANO_PRODUTO
			String insertBasico = "INSERT INTO TBL_REL_PLANO_PRODUTO "+
				"(IDT_PRODUTO, VLR_QUANTIDADE, VLR_TOTAL, DAT_SUMARIO) ";
			
			// Executa queries
			DBConexao.executaPreparedQuery(insertBasico + queryMMO,paramMMO,super.getIdLog());
			DBConexao.executaPreparedQuery(insertBasico + queryMMT_MMS, paramMMT_MMS, super.getIdLog());
			DBConexao.executaPreparedQuery(insertBasico + queryRefund, paramRefund, super.getIdLog());
					
			// Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			retorno = 0;
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
			super.log(Definicoes.ERRO,"cruzarAssinantesChamadas","ERRO:"+e);
			throw new GPPInternalErrorException("Erro Interno GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			//Logar processo batch
			super.gravaHistoricoProcessos(Definicoes.IND_CRUZAMENTO_ASSINANTES,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Totalizações para Cruzamento Finalizada",GPPData.dataCompletaForamtada());
		}
		super.log(Definicoes.INFO,"cruzarAssinantesChamadas","Fim");
		return retorno;
	}
}
