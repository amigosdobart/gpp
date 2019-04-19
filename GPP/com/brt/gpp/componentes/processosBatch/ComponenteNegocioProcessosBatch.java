// Definicao do Pacote
package com.brt.gpp.componentes.processosBatch;

// Classes de POA de Processos Batch
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.componentes.processosBatch.orb.processosBatchPOA;

import com.brt.gpp.aplicacoes.enviarUsuariosShutdown.*;
//import com.brt.gpp.aplicacoes.promocao.GerentePromocao;
import com.brt.gpp.aplicacoes.promocao.GerentePromocaoLondrina;
//import com.brt.gpp.aplicacoes.promocao.PromocaoPrepago;
import com.brt.gpp.aplicacoes.enviarInfosRecarga.*;
import com.brt.gpp.aplicacoes.enviarBonusCSP14.*;
//import com.brt.gpp.aplicacoes.estornarRetirarCredito.EstornoBonusSobreBonus;
import com.brt.gpp.aplicacoes.cobilling.GerarArquivoCobilling;
import com.brt.gpp.aplicacoes.conciliar.*;
import com.brt.gpp.aplicacoes.importacaoUsuarioPortalNDS.ImportarUsuarioPortalNDS;
import com.brt.gpp.aplicacoes.contestar.*;
import com.brt.gpp.aplicacoes.enviarComprovanteServico.*;
import com.brt.gpp.aplicacoes.enviarInfosCartaoUnico.EnvioInfosCartaoUnico;
import com.brt.gpp.aplicacoes.importacaoAssinantes.*;
import com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio.calcularDiasSemRecarga;
import com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio.sumarizacaoProdutoPlano;
import com.brt.gpp.aplicacoes.calcularSumarizacaoRelatorio.sumarizacaoAjustes;
//import com.brt.gpp.aplicacoes.contabilizarControladoria.sumarizacaoContabil;
import com.brt.gpp.aplicacoes.enviarUsuariosStatusNormal.*;
import com.brt.gpp.aplicacoes.exportacaoDW.*;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.*;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.util.VoucherUtils;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
//import com.brt.gpp.aplicacoes.contabilizarControladoria.*;
import com.brt.gpp.aplicacoes.bloquear.*;
import com.brt.gpp.aplicacoes.aprovisionar.AprovisionamentoMMS;
import com.brt.gpp.aplicacoes.aprovisionar.EnviarContingenciaCRMPendente;
import com.brt.gpp.aplicacoes.gerenteFeliz.*;
import com.brt.gpp.aplicacoes.atualizarLimiteCredito.AtualizarLimiteCredito;
import com.brt.gpp.aplicacoes.importacaoEstoqueSap.ImportarEstoqueSap;
import com.brt.gpp.aplicacoes.recarregar.RecargaMicrosiga;
import com.brt.gpp.aplicacoes.retomadaCicloTres.NovoCiclo;
import com.brt.gpp.aplicacoes.notificacaoExpiracao.NotificacaoExpiracao;
import com.brt.gpp.aplicacoes.contabilidade.*;
import com.brt.gpp.aplicacoes.sumarizarAssinantesShutdown.SumarizacaoAssinantesShutdown;

//Arquivo de Imports de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerenteArquivosCDR;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchDelegate;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.gppExceptions.*;

/**
  *
  * Este arquivo contem a definicao da classe componente de negocio de Processos Batch.
  * Ela e responsavel pela logica de negocio para a execucao de processos batch na base de dados do GPP
  * e na plataforma Tecnomen
  *
  * @autor:				Camile Cardoso Couto
  * @Data:				23/03/2004
  *
  * Modificado Por: Marcos C. Magalhães
  * Data: 04/08/2005
  * Razao: Inclusão do método gravaNotificacaoSMS
  */
public class ComponenteNegocioProcessosBatch extends processosBatchPOA
{
	//	Variaveis Membro
	protected GerentePoolLog Log = null; // Gerente de LOG

	/**
	 * Metodo...: ComponenteNegocioProcessosBatch
	 * Descricao: Construtor
	 * @param
	 * @return
	 */
	public ComponenteNegocioProcessosBatch ( )
	{
		//Obtem referencia ao gerente de LOG
		this.Log = GerentePoolLog.getInstancia(this.getClass());
		Log.logComponente (Definicoes.INFO, Definicoes.CN_PROCESSOSBATCH, ": Componente de Negocio ativado..." );
	}

	public void gerarArquivoCobilling()
	{
	}

	/**
	 * Metodo...: importaDadosEntradaEti
	 * Descricao: Metodo que chama a classe que importa os dados inseridos pelo ETI
	 * 			  na TBL_INT_ETI_IN na TBL_ESTOQUE_SAP.
	 * @return	short	 	- Se o processo for realizado com sucesso, retorna 0
	 * 						caso contrário retorna outro valor.
	 * @throws 	GPPInternalErrorException
	 */

	public short importaEstoqueSap () throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaDadosEntradaEti", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			ImportarEstoqueSap importarEstoqueSap = ImportarEstoqueSap.getInstancia(idProcesso);

			retorno= importarEstoqueSap.importar();
			if (retorno==0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaDadosEntradaEti", "Fim RETORNO "+retorno);
		return retorno;
	}





	/**
	 * Metodo...: gravaMensagemSMS
	 * Descricao: Grava a mensagem a ser enviada a um MSISDN em tabela do BD Prep
	 * @param	aMSISDN 	- Numero que vai receber o SMS
	 * @param	aMensagem	- Mensagem que vai ser enviada
	 * @param	aPrioridade	- Identifica se o envio do SMS e OnLine ou somente
	 * 						  no horario comercial
	 * @return	boolean 	- Se gravar registro, retorna TRUE, senao retorna FALSE
	 * @throws 	GPPInternalErrorException
	 */
	public boolean gravaMensagemSMS ( String aMsisdn, String aMensagem, short aPrioridade, String aTipo)
										throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		boolean retorno = false;
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gravaMensagemSMS", "Inicio MSISDN "+aMsisdn);

		try
		{
			// Criando uma classe de aplicacao
			ConsumidorSMS consumidorSMS = ConsumidorSMS.getInstance(idProcesso);

			retorno= consumidorSMS.gravaMensagemSMS(aMsisdn, aMensagem, aPrioridade, aTipo, idProcesso);
			if (retorno)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gravaMensagemSMS", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: gravaNotificacaoSMS
	 * Descricao: Grava a mensagem de aviso de expiração de saldos a ser enviada a um MSISDN em tabela do BD Prep
	 * @return	boolean 	- Se gravar registro, retorna TRUE, senao retorna FALSE
	 * @throws 	GPPInternalErrorException
	 */
	public void gravaNotificacaoSMS()
			throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		boolean retorno = false;
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gravaNotificacaoSMS", "Inicio Método gravaNotificacaoSMS");

		try
		{
			// Criando uma classe de aplicacao
			NotificacaoExpiracao notificacaoExpiracao = new NotificacaoExpiracao(idProcesso);

			retorno = notificacaoExpiracao.GravaNotificacaoExpiracao(idProcesso);

			if (retorno)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gravaNotificacaoSMS", "Fim RETORNO "+retorno);
	}

	/**
	 * Metodo...: enviaUsuariosShutdown
	 * Descricao: Envia Usuarios com status Shutdown
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short enviaUsuariosShutdown (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaUsuariosShutdown", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			EnvioUsuariosShutdown envioUsuariosShutdown = new EnvioUsuariosShutdown (idProcesso);

			retorno = envioUsuariosShutdown.enviaUsuariosShutdown(aData);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaUsuariosShutdown", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: enviarUsuariosStatusNormal
	 * Descricao: Envia Usuarios com status Shutdown
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short enviarUsuariosStatusNormal (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarUsuariosStatusNormal", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			EnviarUsuariosStatusNormal envioUsuariosNormal = new EnviarUsuariosStatusNormal (idProcesso);

			retorno = envioUsuariosNormal.enviaUsuariosNormal(aData);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarUsuariosStatusNormal", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...:	enviaInfosCartaoUnico
	 * Descricao:	Atualiza a tabela de relatorio com as informacoes consolidadas das ligacoes com utilizacao de
	 * 				Cartao Unico
	 * @return	short						Sucesso (0) ou erro (diferente de 0)
	 * @throws	GPPInternalErrorException
	 */
	public short enviaInfosCartaoUnico() throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaInfosCartaoUnico", "INICIO");

		try
		{
			// Criando uma classe de aplicacao
			EnvioInfosCartaoUnico envio = new EnvioInfosCartaoUnico(idProcesso);

			retorno = envio.enviaInfosCartaoUnico();

			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaInfosCartaoUnico", "FIM RETORNO " + retorno);
		return retorno;
	}

	/**
	 * Metodo...:	sumarizaAssinantesShutdown
	 * Descricao:	Atualiza a tabela de relatorio com as informacoes referentes as mudancas diarias de status de
	 * 				assinantes para Shutdown
	 * @param	String		dataAnalise		Dia de analise a serem obtidas as desativacoes
	 * @return	short						Sucesso (0) ou erro (diferente de 0)
	 * @throws	GPPInternalErrorException
	 */
	public short sumarizaAssinantesShutdown(String dataAnalise) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizaAssinantesShutdown", "INICIO");

		try
		{
			// Criando uma classe de aplicacao
			SumarizacaoAssinantesShutdown sumarizacao = new SumarizacaoAssinantesShutdown(idProcesso);

			retorno = sumarizacao.sumarizaAssinantesShutdown(dataAnalise);

			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizaAssinantesShutdown", "FIM RETORNO " + retorno);
		return retorno;
	}

	/**
	 * Metodo...: executaRecargaRecorrente
	 * Descricao: Executa o processo de recargas recorrentes
	 * @param
	 * @return	boolean - Se execucao do processo com sucesso, retorna TRUE, senao retorna FALSE
	 * @throws 	GPPInternalErrorException
	 * @deprecated
	 */
	public boolean executaRecargaRecorrente()
	{
		// Inicializa variaveis do metodo
		boolean retorno = true;
		/*long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaRecargaRecorrente", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			RecargaRecorrente recargaGeneva = new RecargaRecorrente (idProcesso);

			recargaGeneva.efetuaRecargaRecorrente();
			if (retorno)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaRecargaRecorrente", "Fim RETORNO "+retorno);*/
		return retorno;
	}

	/**
	 * Metodo...: executaRecargaMicrosiga
	 * Descricao: Processo de execucao de solicitacoes assincronas de recargas requisitadas pelo Microsiga
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short executaRecargaMicrosiga() throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaRecargaMicrosiga", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			RecargaMicrosiga recarga = new RecargaMicrosiga(idProcesso);

			retorno = recarga.executaRecargaMicrosiga();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaRecargaMicrosiga", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: enviaInfosRecarga
	 * Descricao: Envia de informacoes de recarga para Irmao 14
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short enviaInfosRecarga (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaInfosRecarga", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			EnvioInfosRecarga envioInfosRecarga = new EnvioInfosRecarga (idProcesso);

			retorno = envioInfosRecarga.enviaInfosRecarga(GPPData.datahoraFormatada(aData));
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaInfosRecarga", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: enviaBonusCSP14
	 * Descricao: Execucao do processo de bonus por chamada sainte com CSP 14
	 *	      Bonus Toma La Da Ca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short enviaBonusCSP14 (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaBonusCSP14", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			//EnvioBonusCSP14 enviaBonusCSP14 = new EnvioBonusCSP14 (idProcesso);
			//retorno = enviaBonusCSP14.enviaBonusCSP14(aData);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaBonusCSP14", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: liberaBumerangue
	 * Descricao: Execucao do processo de bonus por chamada sainte com CSP 14
	 *	      Bonus Toma La Da Ca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public void liberaBumerangue (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"liberaBumerangue", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			//EnvioBonusCSP14 enviaBonusCSP14 = new EnvioBonusCSP14 (idProcesso);
			//enviaBonusCSP14.liberaBonus(aData);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaBonusCSP14", "Fim RETORNO "+retorno);
	}


	/**
	 * Metodo...: enviaBonusCSP14
	 * Descricao: Execucao do processo de bonus por chamada sainte com CSP 14
	 *	      Bonus Toma La Da Ca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short emiteNFBonusTLDC (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"emiteNFBonusTLDC", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			EmitirNFBonusTLDC emiteNFTLDC = new EmitirNFBonusTLDC (idProcesso);

			retorno = emiteNFTLDC.selecionaBonusTLDC(aData);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"emiteNFBonusTLDC", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: estornaBonusSobreBonus
	 * Descricao: Processo de estorno de bônus caso tenha sido adquirido com
	 *	          utilização créditos decorrentes de bônus anterior
	 * @param
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short estornaBonusSobreBonus()
	{
		// inicializa variaveis do metodo
		//long idProcesso=0;
		short retorno = 0;

		/* Execucao do Processo foi SUSPENSA
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"estornaBonusSobreBonus", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			EstornoBonusSobreBonus estornaBonusSobreBonus = new EstornoBonusSobreBonus (idProcesso);

			retorno = estornaBonusSobreBonus.estornaBonusSobreBonus();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"estornaBonusSobreBonus", "Fim RETORNO "+retorno);
		*/
		return retorno;
	}

	/**
	 * Metodo...: enviaRecargasConciliacao
	 * Descricao: Execucao do processo de envio de recargas feitas via banco
	 * 				ou pagas com cartão de crédito para o Sistema de Conciliação
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short enviarRecargasConciliacao (String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarRecargasConciliacao", "Inicio DATA "+data);

		try
		{
			// Criando uma classe de aplicacao
			Conciliar aConciliacao = new Conciliar (idProcesso);

			retorno = aConciliacao.enviarRecargas(data);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarRecargasConciliacao", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: executaTratamentoVoucher
	 * Descricao: Envia de informacoes de Solicitacao de Voucher
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short executaTratamentoVoucher (String aData)
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaTratamentoVoucher", "Inicio DATA "+aData);

		String statusProcessoBatch = null;
		try
		{
			executaProcessoBatch(4, null);

			// Indica que o processo foi executado com êxito
			statusProcessoBatch = Definicoes.PROCESSO_SUCESSO;
		}
		catch (GPPInternalErrorException gppE)
		{
			statusProcessoBatch = Definicoes.PROCESSO_ERRO;
			retorno = 1;
		}
		finally
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,statusProcessoBatch);
		}

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaTratamentoVoucher", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: importacaoCDR
	 * Descricao: Execucao do processo de importacao de CDR via SQLLoader
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short importaArquivosCDR (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaArquivosCDR", "Inicio DATA "+aData);

		try
		{
			// Busca a referencia para o singleton da classe que e uma thread
			// para o processamento de arquivos de CDR. Atraves dessa classe
			// que a importacao de CDR e disparada, porem sem metodo explicito
			// ja que a importacao depende da existencia de arquivos a serem
			// processados
			GerenteArquivosCDR.getInstance(idProcesso);
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		catch (Exception e){
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException ("Excecao Interna GPP:" +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaArquivosCDR", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: executaContestacao
	 * Descricao: Executa Contestacao de Cobranca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short executaContestacao (String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaContestacao", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			Contestar contestar = new Contestar (idProcesso);

			retorno = contestar.executaContestacao(GPPData.datahoraFormatada(aData));
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaContestacao", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: envioComprovanteServico
	 * Descricao: Executa o processo de envio de Comprovante de Servico para DOC 1
	 * @param
	 * @return	boolean - Se execucao do processo com sucesso, retorna TRUE, senao retorna FALSE
	 * @throws 	GPPInternalErrorException
	 */
	public boolean envioComprovanteServico() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		boolean retorno = false;
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"envioComprovanteServico", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			ComprovanteServico comprovanteServico = new ComprovanteServico (idProcesso);

			retorno= comprovanteServico.geraComprovanteServico();
			if (retorno)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"envioComprovanteServico", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: importacaoAssinantes
	 * Descricao: Execucao do processo de importacao de Assinantes via SQLLoader
	 * @param
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short importaAssinantes () throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaAssinantes", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			ImportaAssinantes impASS = new ImportaAssinantes(idProcesso);

			retorno = impASS.processaImportacao();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaAssinantes", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: atualizaDiasSemRecarga
	 * Descricao: Atualiza o n.o dias há que um assinante não faz recarga
	 * @param
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short atualizaDiasSemRecarga () throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"atualizaDiasSemRecarga", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			calcularDiasSemRecarga cDSR = new calcularDiasSemRecarga(idProcesso);

			//Realiza as Atualizações na TBL_REL_SEM_RECARGA
			retorno = cDSR.atualizaRecargas();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"atualizaDiasSemRecarga", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: sumarizarProdutoPlano
	 * Descricao: Insere totalizações na TBL_REL_PLANO_PRODUTO
	 * @param	parametroData 	- Data de referencia para execução (dd/mm/yyyy)
	 * @return 	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short sumarizarProdutoPlano (String parametroData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarProdutoPlano", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			sumarizacaoProdutoPlano sPP = new sumarizacaoProdutoPlano(idProcesso, parametroData);

			//Realiza as Atualizações na TBL_REL_SEM_RECARGA
			retorno = sPP.cruzarAssinantesChamadas();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarProdutoPlano", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: sumarizarAjustes
	 * Descricao: Insere totalizações na TBL_REL_AJUSTES
	 * @param	parametroData 	- Data de referencia para execução (dd/mm/yyyy)
	 * @return 	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short sumarizarAjustes (String parametroData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarAjustes", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			sumarizacaoAjustes sAj = new sumarizacaoAjustes(idProcesso, parametroData);

			//Realiza as Atualizações na TBL_REL_SEM_RECARGA
			retorno = sAj.sumarizarAjustes();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarAjustes", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: importaUsuarioPortalNDS
	 * Descricao: Executa Importacao de Usuarios do NDS para o Portal
	 * @param
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short importaUsuarioPortalNDS ( ) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaUsuarioPortalNDS", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			ImportarUsuarioPortalNDS importacaoUsuarioPortalNDS = new ImportarUsuarioPortalNDS(idProcesso);

			retorno = importacaoUsuarioPortalNDS.importaUsuarioPortalNDS();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaUsuarioPortalNDS", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: exportarTabelasDW
	 * Descricao: Executa a exportacao de tabelas para o DW
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short exportarTabelasDW() throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"exportaTabelasDW", "Inicio");
		try
		{
			ExportarTabelasDW expDW = new ExportarTabelasDW(idProcesso);
			expDW.exportarTabelas();
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException ("Excecao Interna GPP:" +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"exportaTabelasDW", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: importaPedidosCriacaoVoucher
	 * Descricao: Executa a importacao de pedidos para criacao de vouchers
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short importaPedidosCriacaoVoucher() throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaPedidosCriacaoVoucher", "Inicio");
		try
		{
			executaProcessoBatch(25, null);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException ("Excecao Interna GPP:" +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"importaPedidosCriacaoVoucher", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: gravaDadosArquivoOrdem
	 * Descricao: Faz a gravacao de um buffer de dados no arquivo de ordem de criacao de vouchers
	 * @param String	- Nome do arquivo a ser gravado
	 * @param byte[]	- Buffer de dados a ser gravado
	 * @return 	boolean - Indica se conseguiu gravar ou nao o buffer
	 * @throws 	GPPInternalErrorException
	 */
	public boolean gravaDadosArquivoOrdem(String nomeArquivo,byte[] buff) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		boolean retorno = true;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"ativaPedidosVoucher", "Inicio ARQ "+nomeArquivo);
		try
		{
			VoucherUtils utils = new VoucherUtils(idProcesso);
			retorno = utils.gravaDadosArquivoOrdem(nomeArquivo, buff);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			retorno = false;
			throw new GPPInternalErrorException ("Excecao Interna GPP:" +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"ativaPedidosVoucher", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo....:getUserIDRequisitante
	 * Descricao.:Busca o ID do requisitante da ordem
	 * @param numOrdem - Numero da Ordem
	 * @return String  - Id do Requisitante
	 */
	public String getUserIDRequisitante(long numOrdem)
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		String retorno = null;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"ativaPedidosVoucher", "Inicio NUM-ORDEM "+numOrdem);
		try
		{
			VoucherUtils utils = new VoucherUtils(idProcesso);
			retorno = utils.getUserIDRequisitante(numOrdem);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"ativaPedidosVoucher", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: sumarizarContabilidade
	 * Descricao: Insere totalizações na TBL_REL_CDR_HORA/DIA
	 * @param	parametroData 	- Data de referencia para execução (dd/mm/yyyy)
	 * @return 	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short sumarizarContabilidade (String parametroData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidade", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			sumarizacaoContabilParalelizada sC = new sumarizacaoContabilParalelizada(idProcesso, parametroData);

			// Verifica se Período Contábil encontra-se fechado
			if(!sC.cicloFechado())
			{
				//Realiza as Atualizações na TBL_REL_CDR_DIA/HORA
				retorno = sC.sumarizarContabilidade();
				if (retorno == 0)
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
				}
				else
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
				}
			}
			else
			{
				Log.log(idProcesso, Definicoes.WARN,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidade", "Periodo Contabil Fechado");

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidade", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: sumarizarContabilidadeCN
	 * Descricao: Insere totalizações na TBL_REL_CDR_HORA/DIA
	 * @param	parametroData 	- Data de referencia para execução (dd/mm/yyyy)
	 * @return 	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short sumarizarContabilidadeCN (String parametroData, String cn) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidade", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			ProcessadorSumarizacaoChamadas sCn = new ProcessadorSumarizacaoChamadas(idProcesso, parametroData, cn);

			// Verifica se o Período Contábil encontra-se fechado
			if(!sCn.cicloFechado())
			{
				//Realiza as Atualizações na TBL_REL_CDR_DIA/HORA
				sCn.sumarizaChamadas();

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				retorno = Definicoes.RET_PERIODO_CONTABIL_FECHADO;
				Log.log(idProcesso, Definicoes.WARN,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidadeCN", "Periodo Contabil Fechado");

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			retorno = -1;

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizarContabilidade", "Fim RETORNO");

		return retorno;
	}


	/**
	 * Metodo...: enviarConsolidacaoSCR
	 * Descricao: Enviar Consolidação de Receita para o SCR
	 * @param parametroData		Data de Referência para Histórico de Processo
	 * @return	short	0=ok; <>0=nok
	 * @throws GPPInternalErrorException
	 */
	public short enviarConsolidacaoSCR (String parametroData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarConsolidacaoSCR", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			EnviarConsolidacaoSCR consolidaSCR = new EnviarConsolidacaoSCR(idProcesso, parametroData);

			// Verifica se o período contábil encontra-se aberto
			if(!consolidaSCR.cicloFechado())
			{
				//Realiza as Atualizações na TBL_REL_CONTABIL
				retorno = consolidaSCR.gerarConsolidacaoSCR();
				if (retorno == 0)
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
				}
				else
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
				}
			}
			else
			{
				retorno = Definicoes.RET_PERIODO_CONTABIL_FECHADO;
				Log.log(idProcesso, Definicoes.WARN,Definicoes.CN_PROCESSOSBATCH,"enviarConsolidacaoSCR", "Periodo Contabil Fechado");

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviarConsolidacaoSCR", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: calcularIndiceBonificacao
	 * Descricao: Calcular Índices de Bonificação
	 * @param String	dataInicial		Data Inicial do Período de Pesquisa  (DD/MM/YYYY)
	 * @param String	dataFinal		Data Final do Período de Pesquisa	(DD/MM/YYYY)
	 * @param String	dataReferencia	Data de Referência para Histórico de Processo (DD/MM/YYYY)
	 * @return	short	0=ok; <>0=nok
	 * @throws GPPInternalErrorException
	 */
	public short calcularIndiceBonificacao (String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"calcularIndiceBonificacao", "Inicio DATA "+data);

		try
		{
			// Criando uma classe de aplicacao
			CalcularIndiceBonificacaoMS indiceBonificacao = new CalcularIndiceBonificacaoMS(idProcesso, data);

			// Verifica se o período contábil encontra-se aberto
			if(!indiceBonificacao.cicloFechado())
			{
				//Realiza as Atualizações na TBL_GER_INDICE_BONIFICACAO
				retorno = indiceBonificacao.determinarIndiceBonificacao();
				if (retorno == 0)
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
				}
				else
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
				}
			}
			else
			{
				retorno = Definicoes.RET_PERIODO_CONTABIL_FECHADO;
				Log.log(idProcesso, Definicoes.WARN,Definicoes.CN_PROCESSOSBATCH,"calcularIndiceBonificacao", "Periodo Contabil Fechado");

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"calcularIndiceBonificacao", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: sumarizarContabilidade
	 * Descricao: Insere totalizações na TBL_REL_CONTABIL
	 * @param	parametroData 	- Data de referencia para execução (dd/mm/yyyy)
	 * @return 	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short consolidarContabilidade (String parametroData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"consolidarContabilidade", "Inicio DATA "+parametroData);

		try
		{
			// Criando uma classe de aplicacao
			ConsolidacaoContabil cC = new ConsolidacaoContabil(idProcesso, parametroData);

			// Verifica se período contábil encontra-se fechado
			if(!cC.cicloFechado())
			{
				//Realiza as Atualizações na TBL_REL_CONTABIL
				retorno = cC.consolidarContabilidade();
				if (retorno == 0)
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
				}
				else
				{
					// Libera o ID de processo para o LOG
					Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
				}
			}
			else
			{
				retorno = Definicoes.RET_PERIODO_CONTABIL_FECHADO;
				Log.log(idProcesso, Definicoes.WARN,Definicoes.CN_PROCESSOSBATCH,"consolidarContabilidade", "Periodo Contabil Fechado");

				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"consolidarContabilidade", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: bloqueioAutomaticoServicoPorSaldo
	 * Descricao: Verifica se algum usuário deve ter seu FREE-CALL e Identificador de chamadas bloqueados ou
	 * 				desbloqueados por saldo
	 * @param 	String	dataReferencia	Data para efeitos de Histórico de Processos
	 * @return	short	0, ok; <>0 se houver erro
	 * @throws GPPInternalErrorException
	 */
	public short bloqueioAutomaticoServicoPorSaldo (String dataReferencia) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"bloqueioAutomaticoServicoPorSaldo", "Inicio DATA "+dataReferencia);

		try
		{
			// Criando uma classe de aplicacao
			BloquearServico bS = new BloquearServico(idProcesso);

			// Executa Processo para bloquear/desbloquear FREE-CALL e ID.Chamadas
			// sem verificar quem passou para Recharge Expired
			retorno = bS.bloquearServicosBatch(dataReferencia);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"bloqueioAutomaticoServicoPorSaldo", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: bloqueioAutomaticoServicoIncluindoRE
	 * Descricao: Verifica se algum usuário deve ter seu FREE-CALL e Identificador de chamadas bloqueados ou
	 * 				desbloqueados por saldo e por mudança de status RE
	 * @param 	String	dataReferencia	Data para efeitos de Histórico de Processos
	 * @return	short	0, ok; <>0 se houver erro
	 * @throws GPPInternalErrorException
	 */
	public short bloqueioAutomaticoServicoIncluindoRE (String dataReferencia) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"bloqueioAutomaticoServicoIncluindoRE", "Inicio DATA "+dataReferencia);

		try
		{
			// Criando uma classe de aplicacao
			BloquearDEHStatusRE bS = new BloquearDEHStatusRE(idProcesso);

			// Executa Processo para bloquear/desbloquear FREE-CALL e ID.Chamadas
			// sem verificar quem passou para Recharge Expired
			retorno = bS.gerarArquivosBloqueioRE(dataReferencia);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"bloqueioAutomaticoServicoIncluindoRE", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: enviaPedidoPorEMail
	 * Descricao: Envia o pedido de criacao de voucher por e-mail
	 * @param long	- Numero da ordem
	 * @throws 	GPPInternalErrorException
	 */
	public void enviaPedidoPorEMail(long numeroOrdem) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaPedidoPorEMail", "Inicio NRO-ORDEM "+numeroOrdem);
		try
		{
			TransmitePedidoVoucher transmitePedido = new TransmitePedidoVoucher(idProcesso);
			transmitePedido.enviaPedidoPorEMail(numeroOrdem);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"ativaPedidosVoucher", "Fim");
	}

	/**
	 * Metodo...: enviaContingenciaSolicitada
	 * Descricao: Envia os dados de contingencia CRM que estao com status de solicitado
	 * @throws GPPInternalErrorException
	 */
	public void enviaContingenciaSolicitada()
	{
		// inicializa variaveis do metodo
		long  idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaContingenciaSolicitada", "Inicio");
		try
		{
			EnviarContingenciaCRMPendente contingencia = new EnviarContingenciaCRMPendente(idProcesso);
			contingencia.enviaContingenciaSolicitada();
		}
		catch (Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"enviaContingenciaSolicitada","Erro ao processar contingencia. Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaContingenciaSolicitada", "Fim");
	}

	/**
	 * Metodo...: contingenciaDaContingencia
	 * Descricao: Envia os dados de contingencia CRM que estao com status de solicitado
	 *
	 * @return 0 se sucesso, ou diferente de 0 se falha
	 * @throws GPPInternalErrorException
	 */
	public short contingenciaDaContingencia(String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_PROCESSOSBATCH, "contingenciaDaContingencia", "Inicio DATA "+aData);

		try
		{
			// Criando uma classe de aplicacao
			DesbloquearContingenciaContingencia desHotline  = new DesbloquearContingenciaContingencia(idProcesso);

			//Realiza as Atualizações na TBL_REL_CONTABIL
			retorno = desHotline.desbloquearHotlineContingencia(aData);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (GPPInternalErrorException e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_PROCESSOSBATCH, "contingenciaDaContingencia", "Fim RETORNO "+retorno);

		return retorno;
	}

	public short gerenciarPromocao(String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenciarPromocao", "Inicio DATA "+data);

		try
		{
			// Criando uma classe de aplicacao
			//GerentePromocao gerentePromocao = new GerentePromocao(idProcesso);

			//Realiza as Atualizações na TBL_REL_CONTABIL
			//retorno = gerentePromocao.gerenciaPromocao(data);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Excecao: "+ e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenciarPromocao", "Fim RETORNO "+retorno);

		return retorno;
	}

	public short promocaoPrepago(String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		boolean retorno = false;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenciarPromocao", "Inicio DATA "+data);

		try
		{
			// Criando uma classe de aplicacao
			//PromocaoPrepago promocaoPrepago = new PromocaoPrepago(idProcesso);

			//Realiza as Atualizações na TBL_REL_CONTABIL
			//retorno = promocaoPrepago.processaPromocao(data);
			if (retorno)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException("Exceção Interna GPP:"+e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenciarPromocao", "Fim");
		return 1;
	}

	/**
	 * Metodo...: cadastraAssinantesPromocaoLondrina
	 * Descricao: Cadastra os assinantes elegiveis para a promocao londrina
	 * @throws GPPInternalErrorException
	 */
	public void cadastraAssinantesPromocaoLondrina(double valorBonus) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long  idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"cadastraAssinantesPromocaoLondrina", "Inicio VLR_BONUS "+valorBonus);
		try
		{
			GerentePromocaoLondrina promLondrina = new GerentePromocaoLondrina(idProcesso);
			promLondrina.cadastraAssinantes(valorBonus);
		}
		catch (Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"cadastraAssinantesPromocaoLondrina","Erro ao cadastrar assinantes na promocao. Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"cadastraAssinantesPromocaoLondrina", "Fim");
	}

	/**
	 * Metodo...: executarPromocaoLondrina
	 * Descricao: Executa periodicamente a analise e credito dos assinantes da promocao londrina
	 * @throws GPPInternalErrorException
	 */
	public void executarPromocaoLondrina(String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long  idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executarPromocaoLondrina", "Inicio DATA "+data);
		try
		{
			GerentePromocaoLondrina promLondrina = new GerentePromocaoLondrina(idProcesso);
			promLondrina.executarPromocaoLondrina(data);
		}
		catch (Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"executarPromocaoLondrina","Erro processar promocao Londrina. Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executarPromocaoLondrina", "Fim");
	}

	/**
	 * Metodo...: avisaRecargaPromocaoLondrina
	 * Descricao: Avisa aos assinantes que nao fizeram recarga no periodo que nao irao receber o bonus
	 * @throws GPPInternalErrorException
	 */
	public void avisaRecargaPromocaoLondrina(String data) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long  idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"avisaRecargaPromocaoLondrina", "Inicio DATA "+data);
		try
		{
			GerentePromocaoLondrina promLondrina = new GerentePromocaoLondrina(idProcesso);
			promLondrina.avisaRecargaPromocaoLondrina(data);
		}
		catch (Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"avisaRecargaPromocaoLondrina","Erro processar o aviso de recarga da promocao Londrina. Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"avisaRecargaPromocaoLondrina", "Fim");
	}

	/**
	 * Metodo....:atualizaNumLotePedido
	 * Descricao.:Atualiza as informacoes de numeracao de lotes iniciais e finais para um item de pedido
	 * @param numPedido		- Numero do pedido
	 * @param numItem		- Numero do item
	 * @param numLoteIni	- Numero inicial do lote
	 * @param numLoteFim	- Numero final do lote
	 * @throws GPPInternalErrorException
	 */
	public void atualizaNumLotePedido(long numOrdem, int numItem, long numLoteIni, long numLoteFim) throws GPPInternalErrorException
	{
		long idProcesso = 0;
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_PROCESSOSBATCH,"atualizaNumLotePedido", "Ordem:"+numOrdem+" Item:"+numItem+" LoteIni:"+numLoteIni+" LoteFim:"+numLoteFim);
		try
		{
			VoucherUtils utils = new VoucherUtils(idProcesso);
			utils.atualizaNumLotePedido(numOrdem,numItem,numLoteIni,numLoteFim);
		}
		catch(Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"atualizaNumLotePedido","Erro ao atualizar numeracao de lote da ordem "+numOrdem+". Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_PROCESSOSBATCH,"atualizaNumLotePedido", "Fim");
	}

	/**
	 * Metodo....:getQtdeCartoes
	 * Descricao.:Retorna a quantidade de cartoes de um item da ordem
	 * @param numOrdem	- Numero da ordem
	 * @param numItem	- Numero do item
	 * @return long		- Quantidade de cartoes
	 * @throws GPPInternalErrorException
	 */
	public long getQtdeCartoes(long numOrdem, int numItem) throws GPPInternalErrorException
	{
		long idProcesso  = 0;
		long qtdeCartoes = 0;
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_PROCESSOSBATCH,"getQtdeCartoes", "Buscando a quantidade de cartoes para a ordem "+numOrdem+" item "+numItem);
		try
		{
			VoucherUtils utils = new VoucherUtils(idProcesso);
			qtdeCartoes = utils.getQtdeCartoes(numOrdem, numItem);
		}
		catch(Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"getQtdeCartoes","Erro retornar a quantidade de cartoes da ordem "+numOrdem+". Erro:"+e);
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			throw new GPPInternalErrorException(e.toString());
		}
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_PROCESSOSBATCH,"getQtdeCartoes", "Fim");
		return qtdeCartoes;
	}
	/**
	 * Metodo...: gerenteFeliz
	 * Descricao: Processo de manutenção dos créditos de certos clientes
	 *	          conhecidos como gerentes felizes
	 * @param
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public short gerenteFeliz() throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenteFeliz", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			GerenteFeliz gerenteFeliz = new GerenteFeliz (idProcesso);

			retorno = gerenteFeliz.mantemCreditos();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenteFeliz", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: atualizaLimiteCreditoVarejo
	 * Descricao: Atualiza os Limites de Crédito do Varejo com as informações enviadas pelo SAP
	 * @return	short	0, se ok; senão, código de erro funcional
	 */
	public short atualizaLimiteCreditoVarejo()throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"atualizaLimiteCreditoVarejo", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			AtualizarLimiteCredito atualizadorLimite = new AtualizarLimiteCredito (idProcesso);

			retorno = atualizadorLimite.atualizaLimites();
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerenteFeliz", "Fim RETORNO "+retorno);
		return retorno;
	}

	/**
	 * Metodo...: marretaGPP
	 * Descricao: Método de utilidade pública para fazer Marretas no GPP
	 * @param	String	parIn	Parametro Genérico de Entrada
	 * @return short	Retorna algum código de erro
	 */
	public short marretaGPP(String parIn) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"marretaGPP", "Inicio " + parIn);

		/*
		try
		{
			// Criando uma classe de aplicacao
			Intervir interventor = new Intervir (idProcesso);

			if(parIn.equals("Migrar"))
			{
				// Realizar a migração
				interventor.migrarResiduosSegundaVersao();
			}

			if(parIn.equals("SincDEH"))
			{
				// Realizar o sincronismo
				interventor.sincronizarBloqueioDEH();
			}

			if(parIn.equals("RecargaBanco"))
			{
				// Reenvia recargas de banco perdidas
				RecarregarNaMarra rec = new RecarregarNaMarra(idProcesso);
				rec.fazRecargaNaMarra("c:\\recargas.txt");
			}

			if(parIn.equals("Eventual"))
			{
				MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();

				Eventual ev = new Eventual(idProcesso);
				ev.ativaTeleMag(map.getMapValorConfiguracaoGPP("CAMINHO_MARRETA"));
			}

			if(parIn.equals("AjusteMASC"))
			{
				RecarregarNaMarra rec = new RecarregarNaMarra(idProcesso);
				rec.ajusteViaArquivo("c:\\telemag.txt");
			}

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"marretaGPP", "Fim RETORNO "+retorno);
		*/
		return retorno;
	}

	/**
	 * Metodo...: gerarCobilling
	 * Descricao: Processo de manutenção dos créditos de certos clientes
	 *	          conhecidos como gerentes felizes
	 * @param	csp		- CSP da operadora de longa distância
	 * @param	inicio	- Data inicial a ser pesquisada	(Formato: "dd/mm/aaaa")
	 * @param	fim		- Data inicial a ser pesquisada	(Formato: "dd/mm/aaaa")
	 * @return
	 * @throws 	GPPInternalErrorException
	 */
	public void gerarArquivoCobilling(String csp, String inicio, String fim, String UF) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso	= 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerarArquivoCobilling", "Inicio");

		try
		{
		    // Criando uma classe de aplicacao
			GerarArquivoCobilling gerarCobilling = new GerarArquivoCobilling(idProcesso, csp, inicio, fim, UF);

			if(UF.equals(Definicoes.TODAS_UF))
			    gerarCobilling.comporBatimentoGeral();
			else
			    gerarCobilling.comporBatimento();
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"gerarArquivoCobilling", "Fim");
	}

	/**
	 * Metodo...: enviaDadosPulaPulaDW
	 * Descricao: Execucao do processo de bonus por chamada sainte com CSP 14
	 *	      Bonus Toma La Da Ca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public void enviaDadosPulaPulaDW (String aData, short aPromocao) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaDadosPulaPulaDW", "Inicio DATA "+aData);

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
			Date dataParametro = sdf.parse(aData);

			// Criando uma classe de aplicacao
			EnvioDadosPulaPulaDW envioDados = new EnvioDadosPulaPulaDW (idProcesso);

			retorno = envioDados.enviaDadosPulaPulaDW(dataParametro, aPromocao);
			if (retorno == 0)
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			}
			else
			{
				// Libera o ID de processo para o LOG
				Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);
			}
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"enviaDadosPulaPulaDW", "Fim RETORNO "+retorno);

	}

	/**
	 * Metodo...: reiniciaCicloTres
	 * Descricao: Execucao do processo de bonus por chamada sainte com CSP 14
	 *	      Bonus Toma La Da Ca
	 * @param	aData - Data de criacao do processo
	 * @return 	short - Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public void reiniciaCicloTres () throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"reiniciaCicloTres", "Inicio");

		try
		{
			// Criando uma classe de aplicacao
			NovoCiclo reinicia = new NovoCiclo (idProcesso);

			reinicia.recarregaNovoCiclo();

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		catch (Exception e)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_ERRO);

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"reiniciaCicloTres", "Fim");

	}

	/**
	 * Metodo....:processaBumerangue14Dia
	 * Descricao.:Realiza a execucao da totalizacao para o bumerangue14 para um determinado dia
	 * @param aData	- Dia a ser processado
	 * @throws GPPInternalErrorException
	 */
	public void processaBumerangue14Dia(String aData) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"processaBumerangue14Dia","Inicio");
		try
		{
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			//GerenciadorPromocaoBumerangue gerente = new GerenciadorPromocaoBumerangue();
			//gerente.executaPromocao(sdf.parse(aData),idProcesso);
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"processaBumerangue14Dia","Fim");
	}

	/**
	 * Metodo....:processaBumerangue14Mes
	 * Descricao.:Realiza a execucao da totalizacao para o bumerangue14 para um determinado mes
	 * @param mes	- Mes a ser processado
	 * @throws GPPInternalErrorException
	 */
	public void processaBumerangue14Mes(short mes) throws GPPInternalErrorException
	{
		// inicializa variaveis do metodo
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"processaBumerangue14Mes","Inicio");
		try
		{
			//GerenciadorPromocaoBumerangue gerente = new GerenciadorPromocaoBumerangue();
			//gerente.executaPromocao(mes,idProcesso);
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"processaBumerangue14Mes","Fim");
	}

	/**
	 * Metodo....: aprovisionarAssinantesMMS
	 * Descricao.: executa o processo de aprovisionamento/desaprovisionamento de assinantes na plataforma de MMS
	 * @throws GPPInternalErrorException
	 * @throws
	 *
	 */
	public void aprovisionarAssinantesMMS(String aDataFinalParam) throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"aprovisionarAssinantesMMS","Inicio");
		AprovisionamentoMMS apr = new AprovisionamentoMMS(idProcesso);
		try
		{
			apr.importarAssinantesHSID(aDataFinalParam);
			apr.enviarAprovisionamentoMMS();
		}
		catch (GPPInternalErrorException e)
		{
			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"aprovisionarAssinantesMMS","Fim");
	}

	/**
	 *	Metodo....:	executaConcessaoPulaPula
	 *	Descricao.:	Executa o processo de concessao do bonus Pula-Pula.
	 *
	 *	@param		String					tipoExecucao				Tipo de execucao da concessao (DEFAULT, REBARBA, PARCIAL).
	 *	@param		String					referencia					Data de referencia para concessao do bonus, em formato DD/MM/YYYY.
	 *	@param		long					promocao					Identificador da promocao. Utilizado somente para o tipo de execucao REBARBA.
	 *	@return		short					retorno						Codigo de retorno da operacao.
	 */
	public short executaConcessaoPulaPula(String tipoExecucao, String referencia, int promocao)
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_PROCESSOSBATCH, "executaConcessaoPulaPula", "Inicio Tipo de Execucao " + tipoExecucao + " Data de Referencia " + referencia);

		String[] params =
		{
			tipoExecucao,
			referencia,
			String.valueOf(promocao)
		};

		try
		{
			this.executaProcessoBatch(Definicoes.IND_GERENTE_PULA_PULA, params);
		}
		catch(Exception e)
		{
			retorno = Definicoes.RET_ERRO_TECNICO;
		}

		if (retorno == Definicoes.RET_OPERACAO_OK)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH, Definicoes.PROCESSO_SUCESSO);
		}
		else
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH, Definicoes.PROCESSO_ERRO);
		}

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaConcessaoPulaPula", "Fim retorno " + retorno);

		return retorno;
	}

	/**
	 *	Metodo....:	sumarizaRecargasAssinantes
	 *	Descricao.:	Sumariza as recargas diarias efetuadas pelos assinantes para calculo de limite dinamico do bonus Pula-Pula.
	 *
	 *	@param		String					referencia					Data de referencia para concessao do bonus, em formato DD/MM/YYYY.
	 *	@return		short					retorno						Codigo de retorno da operacao.
	 */
	public short sumarizaRecargasAssinantes(String referencia)
	{
		// inicializa variaveis do metodo
		long idProcesso=0;
		short retorno = 0;

		/* Nao mais utilizado. Migrado para o modelo Produtor-Consumidor.
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_PROCESSOSBATCH, "sumarizaRecargasAssinantes", "Inicio Data de Referencia " + referencia);

		// Criando uma classe de aplicacao
		SumarizacaoRecargasAssinantes sumarizador = new SumarizacaoRecargasAssinantes(idProcesso);

		retorno = sumarizador.sumarizaRecargasAssinantes(referencia);

		if (retorno == Definicoes.RET_OPERACAO_OK)
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH, Definicoes.PROCESSO_SUCESSO);
		}
		else
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH, Definicoes.PROCESSO_ERRO);
		}

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"sumarizaRecargasAssinantes", "Fim retorno " + retorno);
		*/

		return retorno;
	}

	/**
	 * Metodo....:executaProcessoBatch
	 * Descricao.:Executa o processo batch identificado no ID
	 * @param idProcessoBatch		- Id do processo batch
	 * @param params				- Parametros a serem repassados para este processo batch
	 * @throws GPPInternalErrorException
	 */
	public void executaProcessoBatch(int idProcessoBatch, String params[]) throws GPPInternalErrorException
	{
		// Busca o id do processo para identificacao no log
		long idProcesso = Log.getIdProcesso(Definicoes.CN_PROCESSOSBATCH);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaProcessoBatch","Inicio da execucao do processo batch:"+idProcessoBatch);
		try
		{
			ProcessoBatchDelegate delegate = new ProcessoBatchDelegate(idProcesso);
			delegate.executaProcessoBatch(idProcessoBatch,params);
		}
		catch(Exception e)
		{
			Log.log(idProcesso,Definicoes.ERRO,Definicoes.CN_PROCESSOSBATCH,"executaProcessoBatch","Erro no processo batch id="+idProcessoBatch+" Erro:"+e);
			throw new GPPInternalErrorException(e.toString());
		}
		finally
		{
			// Devolve o id indicando que este jah pode ser reaproveitado
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_PROCESSOSBATCH,Definicoes.PROCESSO_SUCESSO);
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_PROCESSOSBATCH,"executaProcessoBatch","Fim da execucao do processo batch:"+idProcessoBatch);
		}
	}
}