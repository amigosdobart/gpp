package br.com.brasiltelecom.sasc.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.sasc.dao.SascDAO;
import br.com.brasiltelecom.sasc.dao.WsmDAO;
import br.com.brasiltelecom.sasc.entity.SascAtualizacaoSim;
import br.com.brasiltelecom.sasc.entity.WsmAtualizacaoSim;
import br.com.brasiltelecom.sasc.entity.WsmServico;
import br.com.brasiltelecom.wig.util.Definicoes;

import com.brt.gpp.comum.produtorConsumidor.Consumidor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.smarttrust.dp.wsm.api.WsmSimUpdateAction;

public class AtualizadorSimcardConsumidor implements Consumidor
{
	private AtualizadorSimcardProdutor produtor;
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void startup() throws Exception
	{
	}
	
	public void startup(Produtor produtor) throws Exception
	{
		this.produtor = (AtualizadorSimcardProdutor)produtor;
		startup();
	}
	
	public void execute(Object obj) throws Exception
	{
		// Recebe o objeto SascAtualizacaoSim como parametro
		SascAtualizacaoSim sascAtualizacaoSim = (SascAtualizacaoSim) obj;
		
		// Cria uma nova instancia do DAO para acessos ao WSM e realiza
		// a consulta aos servicos ligados ao simcard (ICCID) em questao
		WsmDAO wsmDAO = new WsmDAO();
		WsmAtualizacaoSim wsmAtualizacaoSim = wsmDAO.getServicosWsmByIccid(sascAtualizacaoSim.getIccid());
		
		// Realiza as devidas validacoes para realizacao de atualizacao
		if (existePendencia(wsmAtualizacaoSim.getServicosPendentes(), wsmAtualizacaoSim.getServicosStatusIndeterminado()) )
		{
			// Seleciona a lista de alteracoes a serem realizadas no simcard
			Collection listaDeAcoes = getListaServicosAAlterar(wsmAtualizacaoSim, sascAtualizacaoSim.getServicosBlackList());
			
			// Valida se existem alteracoes a serem realizadas para o simcard
			// if (existeAtualizacao(wsmAtualizacaoSim.getServicosDisponiveis(), listaDeAcoes) )
			if (existeAtualizacao(listaDeAcoes))
			{
				wsmAtualizacaoSim.setAcoes(listaDeAcoes);
				
				// Envia a requisicao para atualizacao e recebe um inteiro como retorno
				int resultadoAtualizacao = wsmDAO.enviaAtualizacaoSimcard(wsmAtualizacaoSim);
				
				// Retornos possiveis:
				// == 1: OK
				// <> 1: NOK
				if ( resultadoAtualizacao == 1 )
				{
					// Seta o status de atualizacao do simcard para Aguardando Atualizacao
					sascAtualizacaoSim.setStatus(Definicoes.SASC_STATUS_AGUARDANDO_ATUALIZACAO);
					atualizaStatusSimcard(sascAtualizacaoSim);
				}
				else
				{
					// Seta o status de atualizacao do simcard para Atualizacao Falhou
					sascAtualizacaoSim.setStatus(Definicoes.SASC_STATUS_ATUALIZACAO_FALHOU);
					atualizaStatusSimcard(sascAtualizacaoSim);
				}
			}
			else
			{
				// Nao ha atualizacoes a serem realizadas no simcard
				// Seta o status de atualizacao do simcard para Simcard Atualizado
				sascAtualizacaoSim.setStatus(Definicoes.SASC_STATUS_SIMCARD_ATUALIZADO);
				atualizaStatusSimcard(sascAtualizacaoSim);
			}
		}
	}
	
	private void atualizaStatusSimcard(SascAtualizacaoSim sascAtualizacaoSim)
	{
		Connection con = produtor.getConnection();
		SascDAO sascDAO = new SascDAO();
		
		try
		{
			// Acresce 1 a quantidade de tentativas
			int qtdeTentativas = sascAtualizacaoSim.getQtdeTentativas() + 1;
			
			// Atualiza o status e a quantidade de tentativas de atualizacao para o simcard e grava uma entrada de DEBUG no Log
			sascDAO.atualizaStatusSimcard(sascAtualizacaoSim.getStatus(), sascAtualizacaoSim.getIccid(), qtdeTentativas, con);
			logger.debug("Simcard " + sascAtualizacaoSim.getIccid() + " atualizado no Banco de Dados.\n" +
						 "Status: " + Definicoes.SASC_STATUS_ATUALIZACAO_FALHOU + "Qtde de tentativas: " + qtdeTentativas);
		}
		catch(SQLException e)
		{
			// Caso haja alguma excecao na atualizacao, a mesma sera gravada no Log
			logger.error("Erro na atualizacao de status para o simcard " + sascAtualizacaoSim.getIccid() + ". Erro: ", e);
		}
	}
	
	/**
	 * Metodo....: existeAtualizacao
	 * Descricao.: Valida se ha a necessidade de alguma atualizacao no simcard
	 * 
	 * @param  listaServicosDisponiveis	- Lista dos servicos disponiveis para download na plataforma OTA
	 * @param  listaServicosAAlterar	- Lista dos servicos a alterar, de acordo com a Black List
	 * @return resultado				- Resultado da validacao
	 */
	private boolean existeAtualizacao (Collection listaServicosAAlterar)
	{
		boolean resultado = true;
		
		// Caso as listas dos servicos disponiveis e dos servicos a alterar sejam vazias,
		// significa que nao ha atualizacoes a serem realizadas para o simcard
		if (listaServicosAAlterar.isEmpty())
			resultado = false;
		
		// Retorno do resultado
		return resultado;
	}
	
	/**
	 * Metodo....: existePendencia
	 * Descricao.: Valida se ha algum servico com pendencia/status 
	 * 			   indeterminado na plataforma OTA
	 * 
	 * @param  listaServicosPendentes		- Lista dos servicos pendentes a ser verificada
	 * @param  listaServicosIndeterminados	- Lista dos servicos com status indeterminado
	 * @return resultado					- Resultado da validacao
	 */
	private boolean existePendencia (Collection listaServicosPendentes, Collection listaServicosIndeterminados)
	{
		boolean resultado = true;
		
		// Se as listas dos servicos pendentes ou indeterminado nao estiverem vazias, 
		// significa que ha servicos ainda pendentes. Falha na validacao
		if ( !listaServicosPendentes.isEmpty() || !listaServicosIndeterminados.isEmpty())
			resultado = false;
		
		// Retorno da validacao
		return resultado;
	}
	
	/**
	 * Metodo....: getListaServicosAExcluir
	 * Descricao.: Define os servicos que devem ser EXCLUIDOS do simcard
	 * 
	 * @param  listaServicosNoSimcard - Lista dos servicos contidos no simcard
	 * @param  listaServicosBlackList - Lista dos servicos que devem ser excluidos
	 * @return listaServicosAExcluir  - Lista dos servicos que serao realmente excluidos
	 */
	private Collection getListaServicosAExcluir(Collection listaServicosNoSimcard, Collection listaServicosBlackList)
	{
		// Cria uma lista para os servicos que serao EXCLUIDOS
		Collection listaServicosAExcluir = new ArrayList();
		WsmSimUpdateAction wsmAction = null;
		WsmServico servicoBlackList = null;
		
		// Varre os servicos que o simcard nao deveria possuir, baseado na BlackList
		// e os inclui na lista de servicos para EXCLUSAO
		for (Iterator i = listaServicosBlackList.iterator(); i.hasNext(); )
		{
			// Seleciona um servico da BlackList
			servicoBlackList = (WsmServico) i.next();
			
			// Verifica se o servico da BlackList esta entre os
			// da lista de servicos contidos no simcard e, caso 
			// positivo, o mesmo sera EXCLUIDO do mesmo
			if ( listaServicosNoSimcard.contains(servicoBlackList) )
			{
				// Insere na lista de EXCLUSAO o servico em questao
				wsmAction = new WsmSimUpdateAction();
				
				// Gera uma acao de REMOCAO para retirada do servico do simcard
				wsmAction.removeService(servicoBlackList.getIdServico(), servicoBlackList.getIdGrupo());
				
				// Retira o servico para exclusao da lista da OTA
				listaServicosNoSimcard.remove(wsmAction);
			}
		}
		
		// Retorna a lista de servicos a serem EXCLUIDOS
		return listaServicosAExcluir;
	}
	
	/**
	 * Metodo....: getListaServicosAIncluir
	 * Descricao.: Define os servicos que devem ser INCLUIDOS no simcard
	 * 
	 * @param  listaServicosDisponiveis	- Lista dos servicos disponiveis para download
	 * @param  listaServicosBlackList 	- Lista dos servicos que nao devem ser incluidos
	 * @return listaServicosAIncluir  	- Lista dos servicos que serao realmente incluidos
	 */
	private Collection getListaServicosAIncluir(Collection listaServicosDisponiveis, Collection listaServicosBlackList)
	{
		// Cria uma lista para o servicos a serem INCLUIDOS
		Collection listaServicosAIncluir = new ArrayList();
		// Objeto que contera o servico e a acao a ser tomada
		WsmServico servicoAIncluir = null;
		
		// Cria uma variavel para conter as acoes de atualizacao
		WsmSimUpdateAction wsmAction = null;
		
		// Varre os servicos da lista originada na OTA e os inclui
		// na lista de servicos para INCLUSAO
		for (Iterator j = listaServicosDisponiveis.iterator(); j.hasNext(); )
		{
			// Recebe o objeto WsmAcao para INCLUSAO do servico
			servicoAIncluir = (WsmServico) j.next();
			
			// Verifica se o servico disponivel para download
			// nao consta na Black List e, caso conste, nao
			// devera ser INCLUIDO
			if ( !listaServicosBlackList.contains(servicoAIncluir))
			{
				// Cria uma nova instancia para INCLUSAO do servico
				wsmAction = new WsmSimUpdateAction();
				
				wsmAction.addService(servicoAIncluir.getIdServico(), servicoAIncluir.getIdGrupo());
				
				// Adiciona o servico na Lista de INCLUSAO
				listaServicosAIncluir.add(wsmAction);
			}
		}
		
		// Retorna a lista dos servicos a serem INCLUIDOS
		return listaServicosAIncluir;
	}
	
	/**
	 * Metodo....: getListaServicosAAlterar
	 * Descricao.: Realiza a comparacao dos servicos que constam no
	 *             simcard e os que nao devem constar no mesmo
	 *
	 * @param  wsmAtualizacaoSim		- Objeto que contem as listas dos servicos da OTA,
	 * 									  relacionados ao simcard a ser atualizado
	 * @param  listaServicosBlackList	- Lista dos servicos provenientes da Black List
	 * @return listaServicosAAlterar	- Lista de objetos para atualizacao, caso exista
	 */
	private Collection getListaServicosAAlterar(WsmAtualizacaoSim wsmAtualizacaoSim, Collection listaServicosBlackList)
	{
		// Cria a lista para os servicos com alteracao
		Collection listaServicosAAlterar = new ArrayList();
		
		// Seleciona a lista dos servicos que irao sofrer algum tipo de alteracao
		listaServicosAAlterar.addAll(getListaServicosAExcluir(wsmAtualizacaoSim.getServicosNoSimcard(), listaServicosBlackList));
		listaServicosAAlterar.addAll(getListaServicosAIncluir(wsmAtualizacaoSim.getServicosDisponiveis(), listaServicosBlackList));
		
		// Retorno da lista dos servicos que irao sofrer 
		// qualquer tipo de alteracao (inclusao ou exclusao)
		return listaServicosAAlterar;
	}
	
	public void finish()
	{
	}
}