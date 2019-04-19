package com.brt.gpp.aplicacoes.promocao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;

/**
 *	Builder de objetos responsaveis pela execucao de operacoes relacionadas ao processo de Estorno de Bonus Pula-Pula por Fraude.
 * 
 *	@author Daniel Ferreira
 *	@since	20/11/2006
 */
public class OperacoesEstornoPPFactory 
{

	/**
	 *	Interface para operacoes no arquivo de escrita.
	 */
	private ArquivoEscrita arquivo;
	
	/**
	 *	Lista de objetos responsaveis pela execucao das operacoes relacionadas ao processo de Estorno de Bonus Pula-Pula por Fraude.
	 */
	private HashMap listaOperacoes;
	
	/**
	 *	Classe para criacao dos objetos que executam as operacoes do processo de estorno.
	 */
	private Class classeOperacoes;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		tipoExecucao			Tipo de execucao do estorno, que pode ser a execucao normal ou uma simulacao.
	 *	@param		nomeArquivo				Nome completo do arquivo onde sera disponibilizado o resultado do processo.
	 *	@throws		Exception
	 */
	public OperacoesEstornoPPFactory(String tipoExecucao, String nomeArquivo) throws Exception
	{
		//O tipo de execucao deve ser passado por parametro no momento da chamada do processo batch de Estorno de
		//Bonus Pula-Pula por Fraude. Se o tipo de execucao nao for especificado, por default sera considerada a 
		// execucao normal do processo.
		if((tipoExecucao != null) && (tipoExecucao.equalsIgnoreCase(Definicoes.IDT_PROCESSAMENTO_VALIDACAO)))
			this.classeOperacoes = Class.forName("com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPPValidacao");
		else
			this.classeOperacoes = Class.forName("com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPPDefault");
		
		//Se especificado, sera criado um arquivo no diretorio com o resultado da operacao.
		if(nomeArquivo != null)
		{
			this.arquivo = new ArquivoEscrita(nomeArquivo);
			this.arquivo.escrever(PromocaoEstornoPulaPula.NOMES_ATRIBUTOS_DELIMITADO + "\n");
		}
		
		this.listaOperacoes = new HashMap();
	}
	
	/**
	 *	Retorna um objeto pela execucao das operacoes relacionadas ao processo de Estorno de Bonus Pula-Pula por Fraude.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@return		Objeto responsavel pela execucao das operacoes relacionadas ao processo de Estorno de Bonus Pula-Pula por Fraude.
	 *	@throws		Exception
	 */
	public OperacoesEstornoPulaPula newOperacoes(long idProcesso) throws Exception
	{
		//O metodo espera que a classe que implementa as operacoes tenha um construtor que receba um objeto 
		//ArquivoEscrita, um objeto PREPConexao e um objeto OperacoesEstornoPPFactory.
		Class[] classes =
		{
			ArquivoEscrita.class,
			PREPConexao.class,
			OperacoesEstornoPPFactory.class
		};
		
		Constructor constructor = this.classeOperacoes.getConstructor(classes);
		
		Object[] parametros = 
		{
			this.arquivo,
			GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso),
			this
		};
		
		OperacoesEstornoPulaPula result = (OperacoesEstornoPulaPula)constructor.newInstance(parametros);
		
		this.listaOperacoes.put(result, result);
		
		return result;
	}

	/**
	 *	Realiza as operacoes de finalizacao do objeto.
	 *
	 *	@param		operacoes.
	 */
	public synchronized void close(OperacoesEstornoPulaPula operacoes)
	{
		if(operacoes != null)
		{
			PREPConexao conexaoPrep = operacoes.getConexao();
			//Liberando a conexao com o banco de dados.
			GerentePoolBancoDados.getInstancia(conexaoPrep.getIdProcesso()).liberaConexaoPREP(conexaoPrep, conexaoPrep.getIdProcesso());
			//Removendo o objeto da lista.
			this.listaOperacoes.remove(operacoes);
		}
	}
	
	/**
	 *	Finaliza o factory. O metodo libera todas as conexoes ainda pendentes e fecha o arquivo de escrita, caso
	 *	tenha sido criado.
	 *
	 *	@throws		Exception
	 */
	public void finish() throws Exception
	{
		for(Iterator iterator = this.listaOperacoes.values().iterator(); iterator.hasNext();)
			this.close((OperacoesEstornoPulaPula)iterator.next());
		
		//Fechando o arquivo de escrita.
		if(this.arquivo != null)
			this.arquivo.fechar();
	}
	
}
