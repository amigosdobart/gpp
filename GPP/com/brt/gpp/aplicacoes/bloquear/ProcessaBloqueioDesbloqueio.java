package com.brt.gpp.aplicacoes.bloquear;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ProcessaBloqueioDesbloqueio
{
	private SolicitacaoBloqueio solicitacao;
	private long				idProcesso;
	private GerentePoolLog		poolLog;
	private java.sql.Timestamp 	dataHora;
	private AcumularSolicitacoesBloqueio acumuladorBloqueio;
	
	/**
	 * Metodo....:ProcessaBloqueioDesbloqueio
	 * Descricao.:Construtor da classe
	 * @param solicitacao - Dados da solicitacao de Bloqueio/Desbloqueio
	 */
	public ProcessaBloqueioDesbloqueio(	SolicitacaoBloqueio solicitacao, 
										java.sql.Timestamp dataHora,
										long idProcesso)
	{
		this.solicitacao = solicitacao;
		this.idProcesso  = idProcesso;
		this.poolLog	 = GerentePoolLog.getInstancia(this.getClass());
		this.acumuladorBloqueio = AcumularSolicitacoesBloqueio.getInstancia();
		this.dataHora = dataHora;
	}

	/**
	 * Metodo...: processarBloqueioDesbloqueio
	 * @return	short 	0(bloqueio/desbl concluido com sucesso)
	 * 					1,2: imposs�vel bloquear n�mero
	 */
	public short processarBloqueioDesbloqueio()
	{
		PREPConexao conexaoPrep = null;
		short retorno = 0;
		
		try
		{
			// Pega conexao com banco de dados
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			
			// Pega os candidatos ao bloqueio/desbloqueio, o servi�o e a a��o
			String tipo = solicitacao.getAcao().startsWith("Bloquear")?"B":"D";
			String msisdn = solicitacao.getMsisdn();
			String servico = solicitacao.getServico();

			// Solicita o bloqueio/desbloqueio do servi�o para um determinado usu�rio
			retorno = this.solicitaBloqueioDesbloqueio(tipo,msisdn,"01",servico,Definicoes.GPP_OPERADOR,dataHora,conexaoPrep);
		}
		catch (GPPInternalErrorException eGPP)
		{
			poolLog.log(idProcesso, Definicoes.ERRO,"ProcessaBloqueioDesbloqueio","run","Nao foi possivel pegar conexao com Banco de Dados");
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: solicitaBloqueioDesbloqueio
	 * Descricao: Verifica se � um bloqueio ou um desbloqueio e chama o m�todo para efetu�-lo 
	 * @param 	String				tipo	"B" para bloqueio, "D" para desbloqueio
	 * @param 	String				msisdn	Msisdn do usu�rio
	 * @param 	String				motivo	Identificador do Motivo do Bloqueio
	 * @param 	String				servico	Identificador do Servi�o
	 * @param 	String				usuario	Usu�rio que solicitou o bloqueio/desbloqueio
	 * @param 	java.sql.Timestamp	data	Data da solicita��o
	 * @return	short	0:	solicita��o de bloqueio/desbloqueio de servi�o realizada com sucesso
						1:	servi�o n�o bloqueado/desbloqueado pois usu�rio n�o pode ter o servi�o bloqueado
						2:	servi�o n�o bloqueado/desbloqueado pois usu�rio j� se encontra bloqueado/desbloqueado ou com bloqueio/desbloqueio em andamento
						3:	servi�o n�o bloqueado/desbloqueado pois h� um desbloqueio/bloqueio pendente. Nova tentativa na pr�xima execu��o
	 * @throws GPPInternalErrorException
	 */
	private short solicitaBloqueioDesbloqueio(String tipo, String msisdn, String motivo, String servico, String usuario, java.sql.Timestamp data, PREPConexao conexaoPrep) 
													throws GPPInternalErrorException
	{
		short retorno = 0;
		
		// Antes de mais nada, verificar o status de bloqueio do servi�o para o usu�rio
		//String statusBloqueio = this.buscaStatusBloqueio(msisdn, servico, conexaoPrep);
		
		// O sevi�o s� poder� ser bloqueado/desbloqueado automaticamente pelo GPP se:
		// 1 - O bloqueio/desbloqueio do servi�o � de atribui��o exclusiva do GPP (FREE_CALL, por exemplo)
		// 2 - Caso a condi��o '1' n�o seja satisfeita, o GPP dever� verificar se 
		// 		 o usu�rio possui o servi�o (usu�rio/servi�o consta na TBL_APR_SERVICOS_ASSINANTE)
		//boolean servicoExclusivoGPP = this.eServicoExclusivoGPP(servico);

		// Prossegue com o bloqueio/desbloqueio
		// Verifica se a a��o � de bloqueio de servi�o
		if(tipo.equals(Definicoes.IND_BLOQUEAR))
		{
			// Solicita Bloqueio do servi�o para o usu�rio
			retorno = this.bloquearServico(msisdn,servico,motivo,usuario,data,conexaoPrep);
		}
		else
		{
			// Verifica se a a��o � de desbloqueio de servi�o
			if(tipo.equals(Definicoes.IND_DESBLOQUEAR))
			{
				// Solicita Desbloqueio do servi�o para o usu�rio
				retorno = this.desbloquearServico(msisdn,servico,motivo,usuario,data,conexaoPrep);
			}
			else
			{
				poolLog.log(idProcesso, Definicoes.WARN,"ProcessaBloqueioDesbloqueio","solicitaBloqueioDesbloqueio","Acao Invalida (Nao eh bloqueio nem desbloqueio)");
			}
		}
		return retorno;	
	}
	
	/**
	 * Metodo...: bloquearServico
	 * Descricao: bloqueia um servi�o, se poss�vel
	 * @param 	String	msisdn	Msisdn do usu�rio que ter� seu servi�o bloquedo
	 * @param 	String	servico	Identificador do servi�o a ser bloqueado
	 * @param 	String	motivo	Motivo do Bloqueio
	 * @param 	String	usuario	Usuario que solicitou o bloqueio
	 * @return	short	0:	solicita��o de bloqueio de servi�o realizada com sucesso
						1:	servi�o n�o bloqueado pois usu�rio n�o pode ter o servi�o bloqueado
						2:	servi�o n�o bloqueado pois usu�rio j� se encontra bloqueado ou com bloqueio em andamento
						3:	servi�o n�o bloqueado pois h� um desbloqueio pendente. Nova tentativa na pr�xima execu��o
	 */	
	public short bloquearServico(String msisdn, String servico, String motivo, String usuario, java.sql.Timestamp dataBloqueio, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		short retorno = 0;
		
		// Busca o status do bloqueio
		String statusBloqueio = this.buscaStatusBloqueio(msisdn, servico, conexaoPrep);
		
		// Verifica se o usu�rio n�o se encontra bloqueado nem em pendencia de interface
		if(statusBloqueio.equals(Definicoes.STATUS_DESBLOQUEIO_CONCLUIDO))
		{
			// Manda bloqueio para TreeMap acumulador de solicita��es
			acumuladorBloqueio.enfiaNoHash(msisdn, servico,Definicoes.IND_BLOQUEAR);
			poolLog.log(idProcesso, Definicoes.DEBUG,"ProcessaBloqueioDesbloqueio","bloquearServico","Bloqueado: "+servico+":"+msisdn);

			// Marca usu�rio/servi�o como em processo de bloqueio
			this.insertTblBloqueados(msisdn, servico, usuario, motivo, dataBloqueio,conexaoPrep);
			retorno = 0;
		}
		else
		{
			// Servi�o n�o bloqueado pois usu�rio j� se encontra bloqueado
			retorno = 2;
		}
		
		return retorno;
	}
	
	/**
	* Metodo...: desbloquearServico
	* Descricao: desbloqueia um servi�o, se poss�vel
	* @param 	String	msisdn	Msisdn do usu�rio que ter� seu servi�o desbloquedo
	* @param 	String	servico	Identificador do servi�o a ser desbloqueado
	* @param 	String	motivo	Motivo do desbloqueio
	* @param 	String	usuario	Usuario que solicitou o desbloqueio
	* @return	short	0:	solicita��o de desbloqueio realizada com sucesso
						  	1:	servi�o n�o desbloqueado pois usu�rio n�o pode ter o servi�o desbloqueado
						  	2:	servi�o n�o desbloqueado pois usu�rio j� se encontra desbloqueado ou com desbloqueio em andamento
						  	3:	servi�o n�o desbloqueado pois h� um bloqueio pendente. Nova tentativa na pr�xima execu��o
	*/
	public short desbloquearServico(String msisdn, String servico, String motivo, String usuario, java.sql.Timestamp dataDesbloqueio, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		short retorno = 0;
		
		// Verifica se usu�rio est� bloqueado
/*		if(statusBloqueio.equals(Definicoes.STATUS_BLOQUEIO_CONCLUIDO))
		{
			// Solicita seu desbloqueio e atualiza seu status para "DS" na tabela de bloqueios
			// Manda desbloqueio para TreeMap acumulador de solicita��es
			tmAcoes.put(msisdn+servico+Definicoes.IND_DESBLOQUEAR, new ElementoXMLApr(servico, Definicoes.IND_DESBLOQUEAR, null, msisdn));
			
			// Deleta o registro da tabela de bloqueios
			String sqlDelete = "DELETE TBL_APR_BLOQUEIO_SERVICO WHERE IDT_MSISDN = ? " +
				"AND ID_SERVICO = ? AND ID_STATUS = ?";
		
			Object sqlDeleteParams[] = {msisdn,
										servico,
										Definicoes.STATUS_BLOQUEIO_CONCLUIDO,
										};
			conexaoPrep.executaPreparedUpdate(sqlDelete,sqlDeleteParams,super.getIdLog());
			
			// solicita��o de desbloqueio realizada com sucesso
			retorno = 0;
		}
		else
		{
			// servi�o n�o desbloqueado pois usu�rio j� se encontra desbloqueado ou com desbloqueio em andamento
			retorno = 2;
		}*/
		
		//Tenta apagar registro de bloqueio da tbl_apr_bloqueio_servico
		String sqlDelete = "DELETE TBL_APR_BLOQUEIO_SERVICO WHERE IDT_MSISDN = ? " +
			"AND ID_SERVICO = ? AND ID_STATUS = ?";
	
		Object sqlDeleteParams[] = {msisdn,
									servico,
									Definicoes.STATUS_BLOQUEIO_CONCLUIDO
									};
		int nroLinhasApagadas = conexaoPrep.executaPreparedUpdate(sqlDelete,sqlDeleteParams,idProcesso);
		
		// Se alguma linha foi apagada, o usu�rio estava bloqueado e requisi��o de desbloqueio deve ser enviado � interface
		// Caso contr�rio, o usu�rio j� n�o se encontrava desbloqueado (nada deve ser mandado para o ASAP)
		if(nroLinhasApagadas > 0)
		{
			retorno = 0;
			// Manda desbloqueio para TreeMap acumulador de solicita��es
			acumuladorBloqueio.enfiaNoHash(msisdn,servico,Definicoes.IND_DESBLOQUEAR);
			poolLog.log(idProcesso, Definicoes.DEBUG,"ProcessaBloqueioDesbloqueio","desbloquearServico","Desbloqueado: "+servico+":"+msisdn);
		}
		else
		{
			retorno = 2;
		}

		return retorno;
	}
	
	/**
	 * Metodo...: buscaStatusBloqueio
	 * Descricao: Retorna o status do usuario/servi�o segundo TBL_APR_BLOQUEIO_SERVICO
	 * @param 	String		msisdn		Msisdn do usu�rio
	 * @param 	String		servico		Identificador do servi�o
	 * @param 	PREPConexao	conexaoPrep	Conex�o com Banco de Dados
	 * @return	String		Status de Bloqueio do usu�rio/servi�o
	 * 						BC:	bloqueio completo
	 * 						BS:	bloqueio solicitado
	 * 						DC:	desbloqueio completo
	 * 						DS:	desb
	 * @throws GPPInternalErrorException
	 */
	private String buscaStatusBloqueio(String msisdn, String servico, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		String retorno;
		
		// Verifica se o usu�rio/servi�o em quest�o j� est� bloqueado
		String sqlBloqueado = "SELECT ID_STATUS FROM TBL_APR_BLOQUEIO_SERVICO " +
			"WHERE IDT_MSISDN = ? AND ID_SERVICO = ?";
			
		Object sqlParams[] = {msisdn, servico};
			
		try
		{
			ResultSet rs = conexaoPrep.executaPreparedQuery1(sqlBloqueado, sqlParams, this.idProcesso);
			
			// Se n�o constar esse msisdn na tabela de bloqueados, o usu�rio est� desbloqueado
			if(!rs.next())
			{
				retorno = Definicoes.STATUS_DESBLOQUEIO_CONCLUIDO;
			}
			// Verifica qual o status do usu�rio/servi�o
			else
			{
				retorno = rs.getString("ID_STATUS");
			}
		}
		catch (SQLException eSql)
		{
			poolLog.log(this.idProcesso, Definicoes.WARN,"ProcessaBloqueioDesbloqueio","buscaStatusBloqueio","Exce��o SQL: "+eSql);
			throw new GPPInternalErrorException("Erro Interno GPP: "+eSql);
		}
				
		return retorno;		
	}
	
	/**
	 * Metodo...: insertTblBloqueadas
	 * Descricao: Insere Registro na TBL_APR_BLOQUEIO_SERVICO
	 * @param 	String		msisdn		Msisdn do usu�rio
	 * @param 	String		servico		Identificador do Servi�o
	 * @param 	String 		usuario		Usu�rio que solicitou o bloqueio
	 * @param 	String		motivo		C�digo do Motivo do Bloqueio
	 * @param	String		status		Status do bloqueio (bloqueio conclu�do/solicitado (BC/BS) ou desb. solicitado (DS)
	 * @param 	PREPConexao	conexaoPrep Conex�o com Banco de Dados
	 * @return	0 se ok; 2 se bloqueio j� est� cadastrado na tbl_apr_bloqueio_servico
	 * @throws GPPInternalErrorException
	 */
	private void insertTblBloqueados(String msisdn, String servico, String usuario, String motivo, java.sql.Timestamp data, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		String sqlInsertTblBloqueados = "INSERT INTO TBL_APR_BLOQUEIO_SERVICO "+
			"(IDT_MSISDN, ID_SERVICO, IDT_USUARIO, ID_MOTIVO, ID_STATUS, DAT_ATUALIZACAO) "+
			"VALUES (?,?,?,?,?,?)";		

		try
		{
			Object sqlInsertTblBloqueadosParams[] =	{	msisdn,
														servico,
														usuario,
														motivo,
														Definicoes.STATUS_BLOQUEIO_CONCLUIDO,
														data };
						
			conexaoPrep.executaPreparedUpdate(sqlInsertTblBloqueados, sqlInsertTblBloqueadosParams,this.idProcesso);
		} 
		catch (GPPInternalErrorException gppE)
		{
			//super.log(Definicoes.DEBUG,"insertTblBloqueados","Servico ja bloqueado");
		}
	}
}
