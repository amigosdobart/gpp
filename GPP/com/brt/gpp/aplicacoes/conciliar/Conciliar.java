//Definicao do Pacote
package com.brt.gpp.aplicacoes.conciliar;

//Arquivos de JAVA
import com.brt.gpp.aplicacoes.*;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;


/**
  *
  * Essa classe refere-se ao processo de envio de informações de recargas feitas via banco
  * ou pagas com cartão de crédito para o Sistema de Conciliação (MCR) 
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				30/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class Conciliar extends Aplicacoes {

	GerentePoolBancoDados gerenteBanco = null;
	
	/**
	 * Metodo...: Conciliar
	 * Descricao: Construtor
	 * @param 	long	aIdProcesso		Id Processo para efeitos de log
	 */
	public Conciliar(long aIdProcesso) 
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_ENVIO_REC_CONCILIACAO);
		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: enviarRecargas
	 * Descricao: Preenche a tabela com as recargas a serem disponbilizadas para Conciliacao
	 * @param 	String	parametroData 	- data de processamento no formato dd/mm/aaaa
	 * @return	short 					- Sucesso(0) ou erro (diferente de 0)
	 * @throws	Exception
	 */
	public short enviarRecargas(String parametroData)throws Exception
	{
		super.log(Definicoes.INFO,"enviarRecargas","Inicio DATA "+parametroData);

		//Inicialização de variáveis
		short retorno = 0;
		ConexaoBancoDados DBConexao = null;
		String dataInicialProcesso = null;
		String dataFinalProcesso = null;
		
		// Determina o Range de Pesquisa
		String dataInicialRange = parametroData +" "+ Definicoes.HORA_INICIO_DIA;  // "00:00:00"
		String dataFinalRange = parametroData +" "+ Definicoes.HORA_FINAL_DIA;	// "23:59:59"

		try
		{
			// Pega a data/hora atual no formato DD/MM/AAAA HH:MM:SS
			dataInicialProcesso = GPPData.dataCompletaForamtada();
			
			//Pega conexão com Banco de Dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			//Query que remove da tabela TBL_INT_CONCIL_OUT todas as linhas já
			//enviadas para o Sistema de Conciliação
			String queryClean = "DELETE FROM TBL_INT_CONCIL_OUT WHERE IDT_STATUS_PROCESSAMENTO = ? " +
					" and  	to_date(tms_transacao,'YYYYMMDDHH24MISS')< (sysdate - ?)";
			
			Object param[] = {Definicoes.IND_LINHA_TRANSFERIDA,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			
			//Limpa tabela TBL_INT_CONCIL_OUT de recargas já transferidas para o MCR

			super.log(Definicoes.INFO,"enviarRecargas",DBConexao.executaPreparedUpdate(queryClean,param,super.getIdLog())+" recargas removidas TBL_INT_CONCIL_OUT");
			
			//Query que procuras pelas recargas feitas via banco ou pagas com cartão de crédito
			//na tabela TBL_REC_RECARGAS e as insere na tabela TBL_INT_CONCIL_OUT
			String queryRecargas = "INSERT INTO TBL_INT_CONCIL_OUT "+
					"(IDT_NSU, IDT_NSU_INSTITUICAO, IDT_TERMINAL, "+
					"TIP_TERMINAL, "+
					"IDT_ORIGEM, "+
					"IDT_LOJA, "+
					"TMS_TRANSACAO,VLR_TRANSACAO,IDT_MSISDN,IDT_STATUS_PROCESSAMENTO) "+
					 "SELECT "+  
					"ID_RECARGA,IDT_NSU_INSTITUICAO,IDT_TERMINAL,TIP_TERMINAL, "+ 
					"DECODE "+
					"( "+
	  					"ID_CANAL, "+
                        "?, lpad(ID_ORIGEM,1+length(ID_ORIGEM),?), "+ // Parametros 0 e 1
                        "?, lpad(ID_ORIGEM,1+length(ID_ORIGEM),?), "+ // Parametros 2 e 3
                        "?, lpad(ID_ORIGEM,1+length(ID_ORIGEM),?), "+ // Parametros 4 e 5
                        "?, lpad(ID_ORIGEM,1+length(ID_ORIGEM),?), "+ // Parametros 6 e 7
                        "ID_ORIGEM "+
                    ") IDT_ORIGEM, "+
                    "DECODE "+
                    "( "+
                        "ID_SISTEMA_ORIGEM, "+
                        "?, ?, "+ // Parametro 8, 9
                        "?, ?, "+ // Parametro 10, 11
                        "?, NOM_OPERADOR, "+ // Parametro 12
                        "IDT_LOJA "+
                    ") IDT_LOJA, "+ 
					"to_char(DAT_ORIGEM,'yyyymmddHH24MISS'), VLR_PAGO,IDT_MSISDN,? "+ // Parametro 13 
					"FROM TBL_REC_RECARGAS "+
					"WHERE (ID_CANAL=? OR ID_CANAL=? OR ID_CANAL=? OR ID_CANAL = ?) "+ // Parametros 14, 15, 16, 17 
					"AND DAT_ORIGEM>=to_date(?,'DD/MM/YYYY HH24:MI:SS') "+ // Parametro 18 
					"AND DAT_ORIGEM<=to_date(?,'DD/MM/YYYY HH24:MI:SS') "+ // Parametro 19  
					"AND VLR_PAGO IS NOT NULL";
			
			super.log(Definicoes.INFO,"enviarRecargas","Recargas disponibilizadas em TBL_INT_CONCIL_OUT");
				Object paramRecarga[] = {Definicoes.CANAL_BANCO,	// Parametro 0
										 Definicoes.CARAC_FILL_BANCO,	// Parametro 1
										 "09",	// Parametro 2 ('09')
										 "9",	// Parametro 3
										 Definicoes.CANAL_CC,	// Parametro 4
										 Definicoes.CARAC_FILL_CC,	// Parametro 5
										 Definicoes.CANAL_CD,	// PArametro 6
										 Definicoes.CARAC_FILL_CC,	// Parametro 7
										 Definicoes.SO_CRM,	// Parametro 8
										 Definicoes.IDT_LOJA_CRM,	// Parametro 9
										 Definicoes.SO_PBT,	// Parametro 10
										 Definicoes.IDT_LOJA_PORTALBRT,	// Parametro 11
										 Definicoes.SO_MIC,	// PArametro 12
										 Definicoes.IND_LINHA_NAO_TRANSFERIDA,	// Parametro 13
										 Definicoes.CANAL_BANCO, // Parametro 14
										 Definicoes.CANAL_CC, // Parametro 15
										 Definicoes.CANAL_CD,	// PArametro 16
										 "09",	// PArametro 17 ('09')
										 dataInicialRange,	// Parametro 18
										 dataFinalRange};	// Parametro 19

				// Popula a tabela TBL_INT_CONCIL_OUT com recargas
				DBConexao.executaPreparedUpdate(queryRecargas,paramRecarga,super.getIdLog());
				
				// Pega data/hora final do processo batch
				dataFinalProcesso = GPPData.dataCompletaForamtada();
		}
		catch (Exception e)
		{
			//Pega data/hora final do processo batch
			dataFinalProcesso = GPPData.dataCompletaForamtada();
				
			//Logar inicio processo batch
			super.gravaHistoricoProcessos(Definicoes.IND_CONCILIACAO,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,e.getMessage(),parametroData);
			super.log(Definicoes.ERRO,"enviarRecargas","Erro:"+e);
			throw new Exception(e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());

			//Logar inicio processo batch
			super.gravaHistoricoProcessos(Definicoes.IND_CONCILIACAO,dataInicialProcesso,dataFinalProcesso,Definicoes.PROCESSO_SUCESSO,"Recargas Disponibilizadas para Envio para Sistema Conciliação",parametroData);
		}
			
		super.log(Definicoes.INFO,"enviarRecargas","Fim");
		return retorno;
	}
}

