package com.brt.gpp.comum.mapeamentos;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.Mapeamento;

/**
 *	Gerente de mapeamentos de entidades do GPP em memoria.
 *
 *	@version	1.0		31/03/2004		Primeira versao.
 *	@author		Camile Cardoso Couto
 *
 *	@version	2.0		02/07/2007		Melhorias no gerenciamento dos mapeamentos.
 *	@author		Daniel Ferreira
 */
public class Mapeamentos 
{

	/**
	 *	Instancia do singleton.
	 */
	private static Mapeamentos instance;

	/**
	 *	Container de mapeamentos de entidades do GPP carregados em memoria.
	 */
	private Map container;
	
	/**
	 *	Construtor da classe.
	 */
	private Mapeamentos()
	{
		this.container = Collections.synchronizedMap(new HashMap());
		this.log(Definicoes.INFO, "Mapeamentos", "Gerente de Mapeamentos inicializado.");
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static Mapeamentos getInstance()
	{
		if(Mapeamentos.instance == null)
			Mapeamentos.instance = new Mapeamentos();
		
		return Mapeamentos.instance;
	}
	
	/**
	 *	Registra o mapeamento no container.
	 *
	 *	@param		mapeamento				Mapeamento de entidades do GPP em memoria.
	 */
	public void registrar(Mapeamento mapeamento)
	{
		this.container.put(mapeamento.getClass().getName(), mapeamento);
		this.log(Definicoes.DEBUG, "registrar", "Mapeamento registrado: " + mapeamento.getClass().getName());
	}
	
	/**
	 *	Atualiza os mapeamentos previamente carregados em memoria.
	 *
	 *	@param		limpar					Flag indicando se o mapeamento deve ser limpo antes da atualizacao.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short atualizarMapeamentos(boolean limpar)
	{
        for(Iterator iterator = this.container.values().iterator(); iterator.hasNext();)
        {
            Mapeamento mapeamento = (Mapeamento)iterator.next();
            
            try
			{
                if(limpar)
                    mapeamento.reload();
                else
		            mapeamento.refresh();
			}
            catch(Exception e)
			{
            	this.log(Definicoes.WARN, "atualizarMapeamentos", "Excecao: " + e);
            }
        }
	    
	    return Definicoes.RET_OPERACAO_OK;
	}
	
	/**
	 *	Atualiza o mapeamento previamente carregado em memoria.
	 *
	 *	@param		nome					Nome da classe do mapeamento.
	 *	@param		limpar					Flag indicando se o mapeamento deve ser limpo antes da atualizacao.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short atualizarMapeamento(String nome, boolean limpar)
	{
		Mapeamento mapeamento = (Mapeamento)this.container.get(nome);
	            
        if(mapeamento != null)
        {
            try
            {
                if(limpar)
                    mapeamento.reload();
                else
		            mapeamento.refresh();
                
	            return Definicoes.RET_OPERACAO_OK;
            }
            catch(Exception e)
            {
            	this.log(Definicoes.ERRO, "atualizarMapeamento", "Mapeamento: " + nome + " - Excecao: " + e);
                return Definicoes.RET_ERRO_TECNICO;
            }
        }
	    
	    return Definicoes.RET_ACAO_INEXISTENTE;
	}
	
	/**
	 *	Exibe o mapeamento previamente carregado em memoria.
	 *
	 *	@param		nome					Nome da classe do mapeamento.
	 *	@return		Representacao do conteudo do mapeamento.
	 */
	public String exibirMapeamento(String nome)
	{
        Mapeamento mapeamento = (Mapeamento)this.container.get(nome);
        
        if(mapeamento != null)
        	return mapeamento.toString();
	    
	    return "Mapeamento nao encontrado";
	}
	
	/**
	 *	Loga uma mensagem.
	 *
	 *	@param		tipo					Grau de severidade do log.
	 *	@param		metodo					Nome do metodo que loga a mensagem.
	 *	@param		mensagem				Mensagem logada. 
	 */
	private void log(int tipo, String metodo, String mensagem)
	{
		GerentePoolLog.getInstancia(this.getClass()).log(0, tipo, Definicoes.CL_GERENTE_TECNOMEN, metodo, mensagem);
	}
	
}