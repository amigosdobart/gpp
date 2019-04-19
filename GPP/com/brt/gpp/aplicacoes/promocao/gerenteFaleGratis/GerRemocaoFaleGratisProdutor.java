package com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimiteSegundos;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * Classe responsavel por consultar e controlar o processaemnto
 * dos assinantes que devem ser removidos da Promocao Fale Gratis
 * 
 * @author João Paulo Galvagni
 * @since  31/05/2007
 */
public class GerRemocaoFaleGratisProdutor extends Aplicacoes
			 implements ProcessoBatchProdutor
{
	private String			  status 	 		= null;
	private PREPConexao 	  conexaoPrep 		= null;
	private ResultSet 		  rs			 	= null;
	private GerFaleGratisDAO  dao		 		= null;
	private String 			  dataInicio	 	= null;
	private int				  idPromocao		= 0;
	private int				  numRegistros		= 0;
	MapPromocaoLimiteSegundos mapLimiteSegundos = null;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param aLogId - Id para controle de log
	 */
	public GerRemocaoFaleGratisProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_GER_REMOCAO_FALE_GRATIS_PROD);
	}
	
	/**
	 * Metodo....: startup
	 * Descricao.: Realiza a consulta dos elegiveis a troca do Plano
	 * 			   Fale Gratis para o Pula-Pula vigente. O ID da promocao
	 * 			   eh passado como parametro para esse processo
	 * 
	 * @param params[0] - Id da promocao a ser processada
	 */
	public void startup(String[] params) throws Exception
	{
		parseParametros(params);
		
		super.log(Definicoes.DEBUG, "startup", "Inicio IDT_PROMOCAO=" + idPromocao);
		
		// Seleciona a instancia do Mapeamento de Limite de Segundos
		this.mapLimiteSegundos = MapPromocaoLimiteSegundos.getInstance();
		
		try
		{
			// Obtem conexao com o Banco de Dados
            this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
    		this.dao 		 = new GerFaleGratisDAO(this.getIdLog());
            
            super.log(Definicoes.INFO, "startup", "Selecionando os elegiveis a troca do Plano FaleGratis para PulaPula");
            
            // Monta o ResultSet a ser consumido
            // Alterar o metodo abaixo
            this.rs = this.dao.getAssinantesParaRemocaoFaleGratis(idPromocao, this.conexaoPrep);
		}
		catch (GPPInternalErrorException e)
		{
			setStatusProcesso(Definicoes.PROCESSO_ERRO);
		    super.log(Definicoes.ERRO, "startup", "Erro GPP: " + e);
		}
		
		setStatusProcesso(Definicoes.PROCESSO_SUCESSO);
		super.log(Definicoes.DEBUG, "startup", "Fim IDT_PROMOCAO=" + idPromocao);
	}
	
	/**
	 * Metodo....: parseParametros
	 * Descricao.: Este metodo realiza a verificacao de parametros
	 * 
	 * @param  params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws GPPInternalErrorException
	{
		// Para o processo batch de remocao na promocao FaleGratis,
		// os parametros sao obrigatorios e devem ser numericos
		if (params == null || params.length == 0 || params[0] == null )
			throw new GPPInternalErrorException("Parametro de promocao obrigatorio.");
		
		this.dataInicio = getDataProcessamento();
		
		try
		{
			this.idPromocao  = Integer.parseInt(params[0]);
		}
		catch(NumberFormatException ne)
		{
			throw new GPPInternalErrorException("ID da promocao deve ser numerico. Valor:" + params[0]);
		}
	}
	
	/**
	 * Metodo....: next
	 * Descricao.: Retorna o proximo registro a ser processado
	 * 			   pelo Consumidor
	 * 
	 * @return Object - Objeto populado com as informacoes de Banco
	 */
	public Object next() throws Exception
	{
		GerFaleGratisVO vo = this.dao.getGerFaleGratisVO(rs);
		
		if (vo != null)
			numRegistros++;
		
		return vo;
	}
	
	/**
	 * Metodo....: getConexao
	 * Descricao.: Retorna a conexao do Banco de Dados do Produtor
	 * 
	 * @return conexaoPrep - Conexao com o Banco de Dados
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
	
	/**
	 * Metodo....: getDataProcessamento
	 * Descricao.: Retorna a data de processamento atual
	 * 
	 * @return String - Data atual de processamento
	 */
	public String getDataProcessamento()
	{
		 return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}
	
	/**
	 * Metodo....: getDescricaoProcesso
	 * Descricao.: Retorna a descricao do Processo
	 * 
	 * @return String - Detalhe do processo
	 */
	public String getDescricaoProcesso()
	{
		return "Remocao do FGN. Promocao:" + idPromocao +
		   	   "; Qtde registros:" + numRegistros;
	}
	
	/**
	 * Metodo....: getIdProcessoBatch
	 * Descricao.: Retorna o ID do processo batch
	 * 
	 * @return idProcessoBatch - ID do processo batch
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_GER_REMOCAO_FALE_GRATIS;
	}
	
	/**
	 * Metodo....: getStatusProcesso
	 * Descricao.: Retorna o status do processo
	 * 
	 * @return status - Status atual do processo
	 */
	public String getStatusProcesso()
	{
		return this.status;
	}
	
	/**
	 * Metodo....: setStatusProcesso
	 * Descricao.: Seta o status do processo
	 * 
	 * @param status - Novo status do processo
	 */
	public void setStatusProcesso(String status)
	{
		this.status = status;
	}
	
	/**
	 * Metodo....: finish
	 * Descricao.: Realiza as acoes apos o termino do 
	 * 			   processamento do produtor
	 * 
	 */
	public void finish() throws Exception
	{
		// Chama a funcao para gravar no historico o Processo em questao
		super.gravaHistoricoProcessos(Definicoes.IND_GER_REMOCAO_FALE_GRATIS, this.dataInicio,
									  this.getDataProcessamento(), this.getStatusProcesso(),
									  this.getDescricaoProcesso(), dataInicio);
		
		this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		
		super.log(Definicoes.DEBUG, "finish", "Status de processamento: " + this.getStatusProcesso());
		super.log(Definicoes.DEBUG, "finish", "Descricao do processamento: " + this.getDescricaoProcesso());
	}
	
	/**
	 * Metodo....: handleException
	 * Descricao.: Trata as excecoes geradas pelo Produtor
	 * 
	 */
	public void handleException()
	{
	}
}