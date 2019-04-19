package br.com.brasiltelecom.sasc.dao;

import br.com.brasiltelecom.sasc.entity.WsmAtualizacaoSim;
import br.com.brasiltelecom.sasc.entity.WsmServico;
import br.com.brasiltelecom.wig.util.Definicoes;

import com.smarttrust.dp.wsm.api.WsmError;
import com.smarttrust.dp.wsm.api.WsmService;
import com.smarttrust.dp.wsm.api.WsmServiceList;
import com.smarttrust.dp.wsm.api.WsmServiceManagerJni;
import com.smarttrust.dp.wsm.api.WsmSimcardIdentifierList;
import com.smarttrust.dp.wsm.api.WsmSimUpdateActionList;
import com.smarttrust.dp.wsm.api.WsmDownloadParameters;
import com.smarttrust.dp.wsm.api.WsmSimcardIdentifier;
import com.smarttrust.dp.wsm.api.WsmSimUpdateAction;
import com.smarttrust.dp.wsm.api.WsmEvent;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Classe responsavel pela consulta e/ou atualizacao dos
 * servicos do simcard junto a plataforma OTA
 * 
 * @author JOAO PAULO GALVAGNI
 * @since  12/10/2006
 * 
 */
public class WsmDAO
{
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Metodo....: getServicosWsmByIccid
	 * Descricao.: Realiza uma consulta na plataforma OTA, atraves
	 *	     	   do WSM, dos servicos do simcard informado
	 *
	 * @param iccid		 - ICCID do simcard a ser consultado
	 * @return resultado - Objeto contendo todos os servicos e seus respectivos status
	 */
	public WsmAtualizacaoSim getServicosWsmByIccid(String iccid)
	{
		// Nova instancia do Gerenciador de Servicos WSM
		WsmServiceManagerJni gerenciadorServicos = new WsmServiceManagerJni();
		
		// Nova instancia da lista que ira receber os servicos relacionados
		// ao simcard em questao
		WsmServiceList listaDeServicos = new WsmServiceList();
		WsmSimcardIdentifier simId = getIdentificadorSimcardByIccid(iccid);
		
		try
		{
			// Grava no LOG uma entrada DEBUG
			logger.debug("Iniciando a selecao dos servicos para o simcard " + iccid);
			
			// Realiza a consulta dos servicos do simcard na plataforma OTA
			// atraves do WSM com o Status indeterminado, para que todos os
			// servicos sejam selecionados, independente da situacao de cada um
			listaDeServicos = gerenciadorServicos.getServices(simId, WsmEvent.STATUS_UNDETERMINED);
		}
		catch (Exception e)
		{
			WsmError erro = gerenciadorServicos.getError();
			logger.error("Erro na selecao dos servicos do assinante " + iccid + ": " + erro.getErrorDescription());
		}
		
		// Cria um objeto que contera todos os objetos servicos
		// individualmente em suas respectivas listas. Inicia um
		// contador para varrer os servicos da lista completa
		WsmAtualizacaoSim resultado = new WsmAtualizacaoSim();
		resultado.setIccid(iccid);
		
		// Move o cursor da lista de servicos para o primeiro servico
		listaDeServicos.moveToFirst();
		int qtdeDeServicos = listaDeServicos.getCount();
		logger.debug("Quantidade de servicos para o simcard " + iccid + ": " + qtdeDeServicos);
		
		// Varre a lista de todos os servicos para selecionar cada
		// servico e o colocar em sua respectiva lista no resultado
		for (int i = 1; i <= qtdeDeServicos; i++)
		{
			// Seleciona o proximo servico da lista completa
			WsmService servicoWSM = listaDeServicos.getNext();
			// Cria uma nova instancia do objeto WsmServico
			WsmServico wsmServico = new WsmServico();
			
			// Seta os valores necessarios para uma possivel atualizacao
			wsmServico.setIdGrupo(servicoWSM.getParentGroupId());
			wsmServico.setIdServico(servicoWSM.getId());
			wsmServico.setStatusServico(servicoWSM.getStatus());
			
			// Considerando o status do servico com relacao ao simcard,
			// cada servico sera incluido em sua lista no resultado
			switch(wsmServico.getStatusServico())
			{
				// Status INDETERMINADO
				// Para os servicos com esse status nada sera feito
				case WsmService.STATUS_UNDETERMINED:
				{
					resultado.addServicoStatusIndeterminado(wsmServico);
					break;
				}
				// Status DISPONIVEL
				// Servicos que nao estao no simcard, mas podem
				// ser inseridos no mesmo
				case WsmService.STATUS_AVAILABLE:
				{
					resultado.addServicoDisponivel(wsmServico);
					break;
				}
				// status PENDENTE
				// Servicos com alguma pendencia ou que estao sendo
				// baixados para o simcard
				case WsmService.STATUS_PENDING:
				{
					resultado.addServicoPendente(wsmServico);
					break;
				}
				// status NO SIMCARD
				// Servicos que estao atualmente no simcard
				case WsmService.STATUS_ONSIM:
				{
					resultado.addServicoNoSimcard(wsmServico);
					break;
				}
				default:
				{
					logger.error("Status do servico invalido.");
					break;
				}
			}
		}
		
		// Retorno do resultado
		return resultado;
	}
	
	/**
	 * Metodo....: enviaAtualizacaoSimcard
	 * Descricao.: Envia uma requisicao de atualizacao para a
	 *			   plataforma OTA atraves do WSM
	 * 
	 * @param wsmAtualizacao - Objeto contendo os dados para atualizacao do simcard
	 * @return resultado	 - Inteiro resultante da atualizacao do simcard
	 *
	 */
	public int enviaAtualizacaoSimcard(WsmAtualizacaoSim wsmAtualizacao)
	{
		// CLASSES NECESSARIAS PARA REALIZAR UMA ATUALIZACAO
		// ActionList actionList
		// WsmSimcardIdentifierList + WsmSimcardIdentifier
		// WsmServiceManagerJni.getServiceId()
		// WsmDownloadParameters
		
		// Inicializa o gerenciador de servicos do WSM
		WsmServiceManagerJni gerenciadorServicos = new WsmServiceManagerJni();
		// Cria os seguintes objetos para envio das atualizacos:
		// (1) - WsmSimUpdateActionList
		// (2) - WsmSimcardIdentifierList
		// (3) - requestId
		// (4) - WsmDownloadParameters
		WsmSimUpdateActionList acionList = getListaDeAcoes(wsmAtualizacao.getAcoes());
		WsmSimcardIdentifierList simIdList = getListaIdentificadorSimcardByIccid(wsmAtualizacao.getIccid());
		int requestId = gerenciadorServicos.getNewRequestId();
		WsmDownloadParameters parameters = getParametros();
		
		// Realizacao do download dos servicos para atualizacao
		// XXX: ERRO FATAL :XXX
		int retorno = gerenciadorServicos.downloadSimUpdateActions(acionList, simIdList, requestId, parameters);
		
		// Procedimento de LOG apos a atualizacao do simcard
		// == 1 - OK
		// <> 1 - NOK
		if (retorno == 1)
			logger.debug("Atualizacao de simcard realizada com sucesso para o iccid: " + wsmAtualizacao.getIccid());
		else
		{
			WsmError wsmError = gerenciadorServicos.getError();
			logger.error("Erro na tentativa de atualizar o simcard " + wsmAtualizacao.getIccid() + ".\n" +
						 "Descricao do erro no WSM: " + wsmError.getErrorDescription());
		}
		
		// Retorna o codigo de atualizacao no WSM
		return retorno;
	}
	
	/**
	 * Metodo....: getListaIdentificadorSimcardByIccid
	 * Descricao.: Seta a lista de identificadores para a
	 * 			   colecao de ICCIDs informada
	 * 
	 * @param  iccid 						- ICCID para montar a Lista de Identificador
	 * @return listaIdentificadoresSimcard	- Lista contendo o Identificador informado
	 */
	public WsmSimcardIdentifierList getListaIdentificadorSimcardByIccid(String iccid)
	{
		// Nova instancia do WsmSimcardIdentifierList
		WsmSimcardIdentifierList listaIdentificadoresSimcard = new WsmSimcardIdentifierList();
		WsmSimcardIdentifier identificadorSimcard = null;
		
		// Monta a lista de Identificador com o ICCID informado
		listaIdentificadoresSimcard.add(getIdentificadorSimcardByIccid(iccid));
		
		// Retorno do objeto
		return listaIdentificadoresSimcard;
	}
	
	/**
	 * Metodo....: getListaDeAcoes
	 * Descricao.: Monta e retorna a lista de acoes a
	 * 			   serem executadas no simcard
	 *  
	 * @param  acoes		- Acoes a serem realizadas no simcard
	 * @return listaDeAcoes	- Lista contendo as acoes a serem realizadas no simcard
	 *
	 */
	public WsmSimUpdateActionList getListaDeAcoes(Collection acoes)
	{
		WsmSimUpdateActionList listaDeAcoes = new WsmSimUpdateActionList();
		WsmSimUpdateAction wsmAction = null;
		
		for ( Iterator i = acoes.iterator(); i.hasNext(); )
		{
			// Seleciona o primeiro servico e sua respectiva acao
			wsmAction = (WsmSimUpdateAction) i.next();
			
			// Adiciona a acao a Lista de Acoes
			listaDeAcoes.add(wsmAction);
		}
		
		// Retorno da Lista de Acoes
		return listaDeAcoes;
	}
	
	// TODO: DELETAR O METODO ABAIXO
	/**
	 * Metodo....: setSimUpdateAction
	 * Descricao.: Seta os atributos do objeto com os valores do
	 * 			   grupo e servico a ser alterado (inserido ou excluido)
	 * 
	 * @param  idServico	- ID do servico a ser alterado
	 * @param  idGrupo		- ID do grupo de servico a ser alterado
	 * @param  acao			- Acao a ser tomada com relacao ao servico
	 * @return action		- Objeto com os atributos devidamente setados
	 *//*
	public WsmServico setSimUpdateAction (int idServico, int idGrupo, int acao)
	{
		// Cria uma nova instancia do objeto WsmAcao
		WsmServico action = new WsmServico();
		
		// Seta os atributos necessarios para uma
		// acao em um servico
		action.setIdServico(idServico);
		action.setIdGrupo(idGrupo);
		action.setAcao(acao);
		
		// Retorno do objeto
		return action;
	}*/
	
	
	/**
	 * Metodo....: getParametros
	 * Descricao.: Seta e retorna os parametros a serem utilizados
	 * 			   no envio da requisicao para atualizacao via WSM
	 * 
	 * @return parametros	- Objeto ja com os valores devidamente setados
	 *
	 */
	public WsmDownloadParameters getParametros()
	{
		// Nova instancia do WsmDownloadParameters
		WsmDownloadParameters parametros = new WsmDownloadParameters();
		
		// Setando os parametros default para o objeto
		parametros.setNotificationType(Definicoes.SASC_NOTIFICATION_TYPE);
		parametros.setSMSCId(Definicoes.SASC_SMSC_ID);
		parametros.setSMSCConnection(Definicoes.SASC_SMSC_CONNECTION);
		parametros.setSMValidityPeriod(Definicoes.SASC_SM_VALIDATION_PERIOD);
		
		// Retorno do objeto
		return parametros;
	}
	
	/**
	 * Metodo....: getParametros
	 * Descricao.: Seleciona os parametros para download, setando-os
	 * 			   com os valores informados
	 * 
	 * @param notificationType	- Tipo de notificacao
	 * @param smscId			- Id de SMSC
	 * @param smscConnection	- Conexao com a SMSC
	 * @param smValidityPeriod	- Periodo de validade do SMS
	 * @param displayText		- Texto a ser mostrado no aparelho
	 * @return parametros		- Objeto contendo os parametros informados
	 * 
	 */
	private WsmDownloadParameters getParametros(int notificationType, int smscId, int smscConnection, int smValidityPeriod, String displayText)
	{
		// Nova instancia do WsmDownloadParameters
		WsmDownloadParameters parametros = new WsmDownloadParameters();
		
		// Setando os parametros no objeto criado
		parametros.setNotificationType(notificationType);
		parametros.setSMSCId(smscId);
		parametros.setSMSCConnection(smscConnection);
		parametros.setSMValidityPeriod(smValidityPeriod);
		parametros.setDisplayText(displayText);
		
		// Retorno do objeto
		return parametros;
	}
	
	/**
	 * Metodo....: getIdentificadorSimcardByIccid
	 * Descricao.: Seleciona o identificador a ser utilizado 
	 * 			   para atualizacao dos servicos atraves do ICCID
	 * 
	 * @param  iccid		- ICCID do simcard a ser atualizado
	 * @return idSimcard	- Identificador para o WSM
	 *
	 */
	public WsmSimcardIdentifier getIdentificadorSimcardByIccid(String iccid)
	{
		// Nova instancia do WsmSimcardIdentifier, ja com os parametros informados
		WsmSimcardIdentifier identificadorSimcard = new WsmSimcardIdentifier(iccid, WsmSimcardIdentifier.SIM_TYPE_ICCID);
		
		// Retorno do objeto
		return identificadorSimcard;
	}
	
	/**
	 * Metodo....: getIdentificadorSimcardByMsisdn
	 * Descricao.: Seleciona o identificador a ser utilizado
	 * 			   para atualizacao dos servicos atraves do MSISDN
	 * 
	 * @param msisdn		- MSISDN do simcard a ser atualizado
	 * @return idSimcard	- Identificador para o WSM
	 */
	public WsmSimcardIdentifier getIdentificadorSimcardByMsisdn (String msisdn)
	{
		// Nova instancia do WsmSimcardIdentifier, ja com os parametros informados
		WsmSimcardIdentifier identificadorSimcard = new WsmSimcardIdentifier(msisdn, WsmSimcardIdentifier.SIM_TYPE_MSISDN);
		
		// Retorno do objeto
		return identificadorSimcard;
	}
	
	/**
	 * Metodo....: getIdentificadorSimcardByImsi
	 * Descricao.: Seleciona o identificador a ser utilizado
	 * 			   para atualizacao dos servicos atraves do IMSI
	 * 
	 * @param msisdn		- MSISDN do simcard a ser atualizado
	 * @return idSimcard	- Identificador para o WSM
	 */
	public WsmSimcardIdentifier getIdentificadorSimcardByImsi(String imsi)
	{
		// Nova instancia do WsmSimcardIdentifier, ja com os parametros informados
		WsmSimcardIdentifier identificadorSimcard = new WsmSimcardIdentifier(imsi, WsmSimcardIdentifier.SIM_TYPE_IMSI);
		
		// Retorno do objeto
		return identificadorSimcard;
	}
}

