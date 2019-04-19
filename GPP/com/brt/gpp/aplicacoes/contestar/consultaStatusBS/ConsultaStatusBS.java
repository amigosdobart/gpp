package com.brt.gpp.aplicacoes.contestar.consultaStatusBS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.contestar.entidade.Contestacao;
import com.brt.gpp.aplicacoes.contestar.entidade.ProtocoloNativo;

import com.brt.gpp.aplicacoes.contestar.dao.ContestacaoDAO;

/**
 * Classe responsavel por consultar o status do BS informado
 * 
 * @author João Paulo Galvagni
 * @since  07/03/2007
 */
public class ConsultaStatusBS extends Aplicacoes
{
	protected GerentePoolBancoDados	gerenteBancoDados = null;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param 		idProcesso		Identificador do processo
	 */
	public ConsultaStatusBS(long idProcesso)
	{
		super(idProcesso, "ConsultaStatusBS");
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(idProcesso);
	}
	
	/**
	 * Metodo....: consultarStatusBS
	 * Descricao.: Realiza a consulta do status do BS
	 * 
	 * @param 	xmlConsulta	- XML contendo os dados para consulta
	 * @return	xmlRetorno	- XML contendo o resultado da consulta
	 */
	public String consultarStatusBS(String xmlConsulta)
	{
		super.log(Definicoes.INFO, "consultarStatusBS", "Iniciando o metodo de consulta de Status de BS..." + xmlConsulta);
		
		String xmlRetorno = "";
		PREPConexao prepConexao = null;
		Contestacao contestacao = new Contestacao();
		
		try
		{
			// Busca uma conexao de banco de dados
			super.log(Definicoes.DEBUG, "consultarStatusBS", "XML para consulta de status de BS recebido:\n" + xmlConsulta);
			prepConexao = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			ContestacaoDAO contestacaoDAO = new ContestacaoDAO(super.getIdLog());
			
			// Realiza o parse do XML de consulta, selecionando os numeros de BS
			contestacao = ConsultaStatusBSXMLParser.parseXMLConsulta(xmlConsulta);
			super.log(Definicoes.DEBUG, "consultaStatusBS", "Parse do XML realizado com sucesso.");
			
			// Seleciona a lista de protocolos nativos para consulta de status
			Collection protocolosNativos = new ArrayList();
			protocolosNativos.addAll(contestacao.getListaProtocolosNativos());
			contestacao.getListaProtocolosNativos().clear();
			
			// Realiza a consulta para cada BS contido na lista de Protocolos Nativos
			for (Iterator i = protocolosNativos.iterator(); i.hasNext(); )
			{
				// Seleciona o primeiro protocolo a ser consultado
				ProtocoloNativo protocoloNativo = (ProtocoloNativo)i.next();
				super.log(Definicoes.DEBUG, "consultarStatusBS", "Protocolo a ser consultado: " + protocoloNativo.getProtocoloNativo());
				
				// Realiza a consulta do protocolo e retorna um novo objeto
				ProtocoloNativo protocolo = contestacaoDAO.findStatusBSById(prepConexao, protocoloNativo.getProtocoloNativo());
				
				// Adiciona o objeto ja consultado a lista de Protocolos Nativos
				contestacao.addListaProtocolosNativos(protocolo);
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "consultarStatusBS", "Erro na consulta de status de BS: " + e.getMessage());
		}
		finally
		{
			// Monta o XML de retorno da consulta
			xmlRetorno = ConsultaStatusBSXMLParser.getXMLRetornoConsulta(contestacao);
			super.log(Definicoes.DEBUG, "consultarStatusBS", "XML de retorno da consulta: " + xmlRetorno);
			
			// Libera a conexao com o Banco de Dados
			gerenteBancoDados.liberaConexaoPREP(prepConexao, super.getIdLog());
		}
		
		// Retorna o XML com o resultado da consulta
		return xmlRetorno;
	}
}