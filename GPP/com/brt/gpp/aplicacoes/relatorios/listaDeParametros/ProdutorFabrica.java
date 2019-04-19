package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.fabricaDeRelatorio.GeradorDeRelatorio;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Relatorio;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.interfaceEscrita.InterfaceEscrita;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
import com.brt.gpp.comum.operacaoArquivo.ArquivoLeitura;

/**
 *	Classe produtora da fábrica de relatórios.
 *	<BR>Consome um arquivo e gera uma série de VOs para serem processados pelos consumidores.</BR>
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 */
public class ProdutorFabrica extends Aplicacoes
{
    protected int							numRegistros;
    protected String						status;
    protected String						mensagem;
    protected PREPConexao					conexaoPrep;
    
    protected ArquivoLeitura				arquivoEntrada;
    protected InterfaceEscrita				arquivoSaidaTemp;
    protected ArquivoEscrita				arquivoErroTemp;
    protected String						pathSaida;		
    protected String						pathErro;		
    protected String						nomeArquivoSaida;
    protected String						nomeArquivoErro;
    
    protected Relatorio						relatorio;

	public ProdutorFabrica(long logId)
	{
		super(logId, Definicoes.CL_PRODUTOR_FABRICA);

		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

	public ProdutorFabrica(long logId, String aNomeClasse) 
	{
		super(logId, aNomeClasse);
	}

	/**
	 * Requer para a fábrica de relatórios encontrar e gerar um Relatório com base no ID do relatório
	 * @return Relatorio O objeto <code>Relatorio</code> com os dados definidos pelo idRelatorio
	 */
	protected Relatorio geraRelatorio(String idRelatorio) throws Exception
	{
		GeradorDeRelatorio gRelatorio= new GeradorDeRelatorio();
		return gRelatorio.novoRelatorioDoBanco(this.conexaoPrep, idRelatorio);
	}
	
 	/**
	 * Rearranja os parametros de entrada (campos) conforme as posições informadas no XML.
	 * 
	 * @param parametros Parametros de entrada, na ordem especificada pelas tags CAMPO do XML
	 * @param numCampos  Numero de itens a ser considerado no array de parametros de entrada
	 * @param posicoes   Mapeamento dos parametros do SQL para os paramentros de entrada.
	 * 					 No exemplo 'posicao[i] = j' temos que o paramentro i do SQL receberá
	 * 					 o parametro de entrada j.
	 * 
	 * @return Lista de paramentros a ser utilizada no SQL
	 */
	protected Object[] montarParametros(Object[] parametros, int numCampos, int[] posicoes)
		throws GPPInternalErrorException
	{
		/*
		 * No caso de relatorio sem campos de entrada
		 */
		if (parametros == null || posicoes == null || posicoes.length == 0)
			return null;
		
		/*
		 * Verifica se a quantidade de parametros é menor que a quantidade de
		 * campos de entrada do relatorio. Evita ArrayOutOfBoundException
		 */
		if (parametros.length < numCampos)
			return null;
		
		Object[] saida = new Object[posicoes.length];
		for(int i = 0; i < posicoes.length; i++)
		{
			if (posicoes[i] >= numCampos)
				throw new GPPInternalErrorException("O conteudo da tag <posicoes> invalido.");
			
			saida[i] = parametros[posicoes[i]];
		}
		return saida;
	}

	/**
	 * Retorna o relatorio para ser utilizada pelos clientes
	 */
	public Relatorio getRelatorio()
	{
		return this.relatorio;
	}

	/**
	 * Seta o relatorio para ser utilizada pelos clientes
	 */
	protected void setRelatorio(Relatorio relatorio)
	{
		this.relatorio = relatorio;
	}

   /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch.
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}

    /**
     *	Retorna o status da execucao do processo.
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}

    /**
     *	Atribui o status da execucao do processo.
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}

    /**
     *	Retorna a data de processamento (data de referencia).
     *  O processo retorna a data efetiva de execucao.
     *
     *	@param		Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
	}

    /**
     *	Retorna a conexao PREP para os consumidores.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}

	/**
	 * @return Returns the arquivoSaida.
	 */
	public InterfaceEscrita getArquivoSaida()
	{
		return arquivoSaidaTemp;
	}

	/**
	 * @return Retorna o arquivoErro.
	 */
	public ArquivoEscrita getArquivoErro() 
	{
		return arquivoErroTemp;
	}

	protected void removerArquivo(String url)
	{
		File f = new File(url);
		if (f.exists())	f.delete();
	}
}