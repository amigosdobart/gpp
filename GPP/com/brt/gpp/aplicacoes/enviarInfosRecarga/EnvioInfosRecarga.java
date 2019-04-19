//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarInfosRecarga;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import java.sql.*;

/**
  *
  * Este arquivo refere-se a classe EnvioInfosRecarga, responsavel pela implementacao da
  * logica de envio de informacoes de recarga para Irmao 14
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Vanessa Andrade
  * Data: 				25/03/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class EnvioInfosRecarga extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	/**
	 * Metodo...: EnvioInfosRecarga
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public EnvioInfosRecarga (long logId)
	 {
		super(logId, Definicoes.CL_ENVIO_INFOS_RECARGA);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: enviaInfosRecarga
	 * Descricao: Le os dados da tabela imputados pelo ETI e promove
	 * 			  alteracoes nas informacoes de recarga para Irmao 14
	 * @param 	aData		- Data do processamento do batch (formato DD/MM/AAAA)
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha		
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException							
	 */
	public short enviaInfosRecarga (String aData) throws GPPInternalErrorException, GPPTecnomenException
	{
        //Inicializa variaveis do metodo
		short retorno = 0;
		long aIdProcesso = super.getIdLog();

		PREPConexao conexaoPrep = null;
		ResultSet rs_pesquisa;
		String dataProcessamento = aData;
		String msg = "";
		String status = "";

		String dataInicial = GPPData.dataCompletaForamtada();

		super.log(Definicoes.INFO, "enviaInfosRecarga", "Inicio DATA "+aData);
				
		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			
			// Verifica o periodo exato para existir bonus
			short periodo = getPeriodoRecarga(conexaoPrep);
			
			short ind_fez_recarga_ok = Definicoes.TIPO_IND_FEZ_RECARGA_OK;
			short ind_fez_recarga_nok = Definicoes.TIPO_IND_FEZ_RECARGA_NOK;
			String idt_irmao14_recarregado = Definicoes.IDT_IRMAO14_RECARREGADO;
			String idt_irmao14_gravado = Definicoes.IDT_IRMAO14_GRAVADO;
			String tipo_recarga = Definicoes.TIPO_RECARGA;
			
			// Faz a pesquisa no banco para saber se o campo fez_recarga esta nulo e eh diferente dos possiveis valores
			String sql_pesquisa = "SELECT * FROM TBL_INT_ETI_RECARGA_IRMAO14 " +
								"WHERE IND_FEZ_RECARGA <> ? AND IND_FEZ_RECARGA <> ?" +
 								"OR IND_FEZ_RECARGA IS NULL AND IDT_STATUS_PROCESSAMENTO = ?";
			
			
			// Indica qual registro da tabela Irmao14 fez uma recarga nos 30 dias anteriores a data de corte
			String sql_sim = "UPDATE TBL_INT_ETI_RECARGA_IRMAO14 B SET" +
							" IND_FEZ_RECARGA = ?, IDT_STATUS_PROCESSAMENTO = ?" +
							" WHERE IDT_STATUS_PROCESSAMENTO = ? AND " +
							" EXISTS (SELECT 1 " +
							" FROM TBL_REC_RECARGAS A " +
							" WHERE A.IDT_MSISDN = B.IDT_MSISDN AND " +
							" ID_TIPO_RECARGA = ? AND" + 
							" TO_DATE(TO_CHAR(A.DAT_ORIGEM, 'DD/MM/YYYY'), 'DD/MM/YYYY') BETWEEN " +
							" TO_DATE(TO_CHAR((B.DAT_CORTE - ? ), 'DD/MM/YYYY'), 'DD/MM/YYYY') AND " + 
							" TO_DATE(TO_CHAR(B.DAT_CORTE, 'DD/MM/YYYY'), 'DD/MM/YYYY')) " ;

			//Indica qual registro da tabela Irmao14 nao fez uma recarga nos 30 dias anteriores a data de corte
			String sql_nao = "UPDATE TBL_INT_ETI_RECARGA_IRMAO14 SET IND_FEZ_RECARGA = ?, IDT_STATUS_PROCESSAMENTO = ? " +
							" WHERE (IND_FEZ_RECARGA <> ? OR IND_FEZ_RECARGA IS NULL) AND IDT_STATUS_PROCESSAMENTO = ?";
			
			Object param_pesquisa[] = {new Short(ind_fez_recarga_ok),
										new Short(ind_fez_recarga_nok),
										idt_irmao14_gravado};
			
			Object param_sim[] = {new Short(ind_fez_recarga_ok),
									idt_irmao14_recarregado,
									idt_irmao14_gravado,
									tipo_recarga,
									new Short(periodo)};
										
			Object param_nao[] = {new Short(ind_fez_recarga_nok),
									idt_irmao14_recarregado,
									new Short(ind_fez_recarga_ok),
									idt_irmao14_gravado};
			
			rs_pesquisa = conexaoPrep.executaPreparedQuery(sql_pesquisa, param_pesquisa, super.logId);
			
			if (rs_pesquisa.next())
			{
				msg = "Assinantes com indicador de recarga nulo";
				super.log(Definicoes.DEBUG, "enviaInfosRecarga", msg);

				if (conexaoPrep.executaPreparedUpdate(sql_sim, param_sim, super.logId) > 0)
				{
					msg = "Houve Assinantes que realizaram recarga";
					super.log(Definicoes.DEBUG, "enviaInfosRecarga", msg);

					if (conexaoPrep.executaPreparedUpdate(sql_nao, param_nao, super.logId) > 0)
					{
						msg = "Houve Assinantes que nao realizaram recarga";
						super.log(Definicoes.DEBUG, "enviaInfosRecarga", msg);
					}
					else
					{
						msg = "Nenhum asinante deixou de fazer recarga no período";
						super.log(Definicoes.WARN, "enviaInfosRecarga", msg);
					}

				}
				else
				{
					msg = "Nenhum asinante fez recarga no período";
					super.log(Definicoes.WARN, "enviaInfosRecarga", msg);
				}
			}
			else
			{
				msg = "Nenhum assinante com indicador de recarga nulo";
			 	super.log(Definicoes.WARN, "enviaInfosRecarga", msg);
			}

			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = msg;
			status = Definicoes.TIPO_OPER_SUCESSO;
			
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_INFOS_RECARGA, dataInicial, dataFinal, status, descricao, dataProcessamento);

			// Deleta todos os registros processados com sucesso 
			limpaRegistrosProcessados();

		}		
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "enviaInfosRecarga", "Excecao Interna GPP:"+ e1);
			retorno = 1;
			status = Definicoes.TIPO_OPER_ERRO;				
		}

		catch (SQLException e2)
		{
			super.log(Definicoes.WARN, "enviaInfosRecarga", "Excecao SQL: "+ e2);
			throw new GPPInternalErrorException ("Excecao GPP: " + e2);				
		}
		finally
		{
			super.log(Definicoes.INFO, "enviaInfosRecarga", "Fim");
		}
		return retorno;
	}
	
	
	/**
	 * Metodo...: limpaRegistrosProcessados
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os usuario que ja foram processados pelo ETI
	 * @param
	 * @return	void									
	 * @throws GPPInternalErrorException
	 */
	public void limpaRegistrosProcessados () throws GPPInternalErrorException
	{

		super.log(Definicoes.DEBUG, "limpaRegistrosProcessados", "Inicio");

		try
		{
			// Busca uma conexao de banco de dados		
			PREPConexao conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			String idt_irmao14_processado = Definicoes.IDT_IRMAO14_PROCESSADO;
			
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Deleta da tabela os registros processados com sucesso
			String sql_processo = "DELETE FROM TBL_INT_ETI_RECARGA_IRMAO14 " +
								  "WHERE IDT_STATUS_PROCESSAMENTO = ? " +
								  " and DAT_REGISTRO < (sysdate - ?)";

			Object paramProcesso[] = {idt_irmao14_processado,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};

			super.log(Definicoes.DEBUG, "limpaRegistrosProcessados", conexaoPREP.executaPreparedUpdate(sql_processo,paramProcesso, super.logId)+ " registro(s) processado(s) deletado(s).");
		
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "limpaRegistrosProcessados", "Erro no processo de status: Nao conseguiu pegar conexao com banco de dados.");
		}
		finally
		{
			super.log(Definicoes.DEBUG, "limpaRegistrosProcessados", "Fim");
		}
	}

	/**
	 * Metodo...: getPeriodoRecarga
	 * Descricao: Verifica qual o periodo para poder aplicar a recarga dos dados
	 * @param 	conexaoPrep	- Conexao com o Banco de Dados
	 * @return	short		- Valor do periodo 									
	 * @throws 	GPPInternalErrorException
	 */
	private short getPeriodoRecarga(PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		short retorno = 0;
		super.log(Definicoes.DEBUG, "getPeriodoRecarga", "Inicio");
		try
		{
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			retorno = Short.parseShort(confGPP.getMapValorConfiguracaoGPP(Definicoes.PERIODO_RECARGA_IRMAO14));
		}
		catch (Exception e3)
		{
			super.log(Definicoes.ERRO, "enviaInfosRecarga", "Excecao SQL:"+ e3);
			throw new GPPInternalErrorException ("Excecao GPP:" + e3);				
		}
		finally
		{
			super.log(Definicoes.DEBUG, "getPeriodoRecarga", "Fim");
		}
		return retorno;
	}
}


