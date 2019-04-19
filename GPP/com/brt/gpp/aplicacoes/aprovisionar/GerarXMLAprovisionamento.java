package com.brt.gpp.aplicacoes.aprovisionar;

import java.util.Collection;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.aprovisionar.CabecalhoXMLApr;
import com.brt.gpp.aplicacoes.aprovisionar.ElementoXMLApr;
import java.util.Iterator;

public class GerarXMLAprovisionamento extends com.brt.gpp.comum.GerarXML 
{
	// Parâmetros da Classe
	CabecalhoXMLApr cab;		// Objeto contendo os dados para se montar o cabeçalho
	Collection elmAprLista;		// Objeto contendo a lista de elementos a serem colocados no XML
	
	/**
	 * Metodo...: GerarXMLAprovisionamento
	 * Descricao: Construtor que inicializa o nó principal do XML (root) e recebe os dados
	 * para se construir o cabeçalho e os elementos da sessão 'provison' do XML
	 * @param 	CabecalhoXMLApr	aCab	Objeto contendo os dados necessários para se construir o cabeçalho do XML
	 * @param 	Collection		aElmApr	Collection com objetos da classe ElementoXMLApr, contendo os dados para se construir a sessão 'provision' do XML
	 */
	public GerarXMLAprovisionamento(CabecalhoXMLApr aCab, Collection aElmApr)
	{
		super("root");
		this.cab = aCab;
		this.elmAprLista = aElmApr;
	}
	
	/***
	 * Metodo...: getAprXML
	 * Descricao: retorna o XML montado
	 * @return	String	XML montado com as informações passadas na construção
	 */
	public String getAprXML()
	{
		ElementoXMLApr elmApr = new ElementoXMLApr();
				
		// Monta o cabeçalho comum
		super.adicionaTag("id_os",cab.getIdOs());	
		super.adicionaTag("case_id",cab.getIdOs());	
		super.adicionaTag("msisdn_novo",cab.getMsisdn());
		super.adicionaTag("msisdn_antigo",cab.getMsisdn());
		super.adicionaTag("case_type",cab.getCaseType());
		super.adicionaTag("case_sub_type",cab.getCaseSubType());
		super.adicionaTag("order_priority",cab.getOrderPriority());
		super.adicionaTag("categoria",cab.getCategoria());
		super.adicionaTag("categoria_anterior",cab.getCategoria());
		super.adicionaTag("nome_produto_smp", "Produto Acesso Movel");
		super.adicionaTag("identificador_produto_smp", cab.getMsisdn());
		
		// Verifica se trata-se do processo de bloqueio por saldo para inserir as tags particulares
		if(cab.getCaseType().equals("BLOQUEIO_ASAP"))
		{
			super.adicionaTag("fluxo",Definicoes.XML_OS_FLUXO);	
		}
		
		// *** A tag case_id deve ser SEMPRE informada, de acordo com requisicao do ASAP - Daniel Abib
		// *** A tag esta sendo preenchida acima 
		// Verifica se trata-se do processo de contigencia ao CRM para inserir as tags particulares
		//if(cab.getCaseType().equals(Definicoes.XML_OS_CASE_TYPE_BLOQUEIO) || cab.getCaseType().equals(Definicoes.XML_OS_CASE_TYPE_HOTLINE))
		//{
		//	super.adicionaTag("case_id", cab.getIdOs());
		//}
				
		// Abre nó de provision
		super.abreNo("provision");	
		
		// Monta os nós de solicitações do xml para esse msisdn
		for (Iterator iT = elmAprLista.iterator(); iT.hasNext();)
		{
			// Pega um elemento da lista de elementos
			elmApr = (ElementoXMLApr) iT.next();
						
			// Alteração do identificador de serviço para o padrão que o ASAP usa (inserindo string _GPP_)
			// Adiciona os tags do campo ELM_xxx dependendo do serviço
			String servicoGPP = elmApr.getMacroServico();
			
			// Se o serviço não for hotline, colocar "GPP" no meio de seu nome
			if(!servicoGPP.equals(Definicoes.SERVICO_BLOQ_HOTLINE) && !servicoGPP.equals(Definicoes.SERVICO_INFO_SIMCARD))
			{
				//this.xmlProvisionAddElmNode(cab.getMsisdn(), servicoGPP.substring(0,4) + Definicoes.XML_OS_FLUXO + servicoGPP.substring(3), elmApr.getOperacao());				
				this.xmlProvisionAddElmNode(cab.getMsisdn(), servicoGPP.substring(0,4) + Definicoes.XML_OS_FLUXO + servicoGPP.substring(3), elmApr.getMsisdn());	// o campo msisdn contem se é 'B' ou 'D'
			}
			else
			{
				this.xmlProvisionAddElmNode(cab.getMsisdn(), servicoGPP, elmApr.getOperacao());				
			}
		};

		// Fecha nó de provision
		super.fechaNo();
				
		// Fecha xml
		return (super.getXML());
	}	
	
	/**
	 * Metodo...: xmlProvisionAddElmNode
	 * Descricao: Adiciona um nó de ELM ao xml de provison
	 * @param 	String		msisdn		msisdn que terá o serviço bloqueado/desbloqueado
	 * @param 	String		servico		serviço a ser bloqueado/desbloqueado
	 * @param 	String		acao		B/D para bloqueio/desbloqueio
	 * @param 	GerarXML	geradorXml	XML a que deverá ser acrescentado esse nó
	 * @return	GerarXML	xml acrescido do nó
	 * 						null, caso o serviço solicitado não seja FREE_CALL, GPP_BLACK_LIST, ID_CHAMADA ou ELM_INFO_SIMCARD
	 */
	private void xmlProvisionAddElmNode(String msisdn, String servico, String operacao)
	{
		// Caso seja um serviço de SIMCARD
		if(servico.equals(Definicoes.SERVICO_INFO_SIMCARD))
		{
			super.abreNo(servico);
			super.adicionaTag("macro_servico",servico);
			super.adicionaTag("operacao",Definicoes.XML_OS_OP_SIMCARD);
			super.adicionaTag("x_tipo",Definicoes.XML_OS_X_TIPO_SIMCARD);
			super.adicionaTag("status",Definicoes.XML_OS_STATUS);
			
			// Abre nó de parametros dentro do serviço
			super.abreNo("parametros");
			super.adicionaTag("simcard_msisdn",msisdn);
			super.fechaNo();
			super.fechaNo();
		}
		else
		// Caso seja um serviço diferente de SIMCARD (idChamada, FreeCall, Hotline etc)
		{
			super.abreNo(servico);
			super.adicionaTag("macro_servico",servico);

			// Para o caso de XML mandado para o ASAP, no processo de contingencia ao CRM,
			// o campo "operacao" virá com "Retirar"  e o x_tipo será "Servico de Bloqueio"
			if(!operacao.equals("B") && !operacao.equals("D"))
			{
				super.adicionaTag("operacao",operacao);	
				super.adicionaTag("x_tipo",Definicoes.XML_OS_X_TIPO_BLOQUEIO);		
			}
			else
			{
				super.adicionaTag("operacao",operacao.equals(Definicoes.IND_BLOQUEAR)?Definicoes.XML_OS_BLOQUEAR:Definicoes.XML_OS_DESBLOQUEAR);				
				super.adicionaTag("x_tipo",Definicoes.XML_OS_X_TIPO);
			}
			super.adicionaTag("status", Definicoes.XML_OS_STATUS);
			super.fechaNo();
		}
	}	
}
