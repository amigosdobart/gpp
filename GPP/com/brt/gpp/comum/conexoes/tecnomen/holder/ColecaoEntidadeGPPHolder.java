package com.brt.gpp.comum.conexoes.tecnomen.holder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.omg.CORBA.ORB;

import TINC.recordSeqHolder;
import TINC.tincRecord;
import TINC.tincSeqHolder;

import com.brt.gpp.comum.conexoes.tecnomen.holder.EntidadeGPPHolder;

/**
 *	Conversor de colecoes de entidades entre o GPP e a Tecnomen. Esta classe implementa a transformacao entre colecoes
 *	de entidades do GPP para arrays de estruturas utilizadas pelos servidores da Tecnomen, e vice-versa. 
 *
 *	O conversor possui internamente os objetos referentes as entidades do GPP e a Tecnomen. Cada vez que um metodo de 
 *	conversao e chamado, sao retornadas as entidades convertidas para as classes correspondentes. Por exemplo, caso o 
 *	metodo de toColecaoEntidadeGPP() seja chamado, e criada e retornada uma colecao de entidades do GPP com os valores da 
 *	estrutura da Tecnomen. A reciproca e verdadeira. Assim e possivel que objetos passados pelo GPP para a Tecnomen 
 *	sejam devidamente retornados apos a resposta da operacao. Para que o objeto possa obter corretamente o conversor 
 *	de entidade GPP/TEC, e necessario que a conexao informe a classe do conversor no construtor 
 *	ColecaoEntidadeGPPHolder() e que esta classe possua dois construtores: Um recebe a entidade do GPP como parametro 
 *	e o outro recebe a entidade da Tecnomen como parametro. 
 * 
 *	NOTA: O holder parte do principio que todas as entidades na colecao sao da mesma classe. Caso isto nao seja 
 *	verdadeiro e necessario implementar as alteracoes.
 * 
 *	@author		Daniel Ferreira
 *	@since		02/03/2007
 */
public class ColecaoEntidadeGPPHolder 
{

	/**
	 *	Referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 */
	private ORB orb;
	
	/**
	 *	Objeto representando a classe de conversor de entidades do GPP.
	 */
	private Class classeGPPHolder;
	
	/**
	 *	Lista de conversores de entidades do GPP.
	 */
	private ArrayList entidadeGPPHolders;
	
	/**
	 *	Lista de estruturas de entidades utilizadas pelos servicos da Tecnomen.
	 */
	private recordSeqHolder entidadeTECHolders;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		classeGPPHolder			Classe de conversor de entidades do GPP.
	 */
	public ColecaoEntidadeGPPHolder(ORB orb, Class classeGPPHolder)
	{
		this.orb				= orb;
		this.classeGPPHolder	= classeGPPHolder;
		this.entidadeGPPHolders	= new ArrayList();
		this.entidadeTECHolders	= new recordSeqHolder(new tincRecord[0][0]);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		orb						Referencia ao servico da Tecnomen para estabelecimento de conexoes 
	 *										(Object Request Broker).
	 *	@param		classeGPPHolder			Classe de conversor de entidades do GPP.
	 */
	public ColecaoEntidadeGPPHolder(ORB orb, Class classeGPPHolder, Collection entidadesGPP)
	{
		this(orb, classeGPPHolder);
		
		this.entidadeGPPHolders.addAll(this.extrairEntidadeGPPHolders(entidadesGPP));
	}

	/**
	 *	Retorna uma colecao de conversores de entidades do GPP contendo os objetos fornecidos.
	 *
	 *	@param		entidadesGPP			Colecao de entidades do GPP.
	 *	@return		Colecao de conversores de entidades do GPP.
	 */
	private ArrayList extrairEntidadeGPPHolders(Collection entidadesGPP)
	{
		ArrayList result = new ArrayList();
		
		//Obtendo o construtor da entidade do GPP.
		try
		{
			Constructor constructor = this.classeGPPHolder.getConstructor(new Class[]{ORB.class, Object.class});
			
			if(entidadesGPP != null)
				for(Iterator iterator = entidadesGPP.iterator(); iterator.hasNext();)
					result.add(constructor.newInstance(new Object[]{this.orb, iterator.next()}));
		}
		//Caso o construtor nao for encontrado, o holder nao foi implementado corretamente e a lista sera retornada 
		//como vazia. Se algum objeto fornecido na colecao for invalido, sera ignorado.
		catch(Exception ignored){}
		
		return result;
	}
	
	/**
	 *	Converte a colecao de estruturas da Tecnomen em uma colecao de entidades do GPP.
	 *
	 *	@return		Colecao de entidades do GPP.
	 */
	public Collection toColecaoEntidadeGPP()
	{
		ArrayList result = new ArrayList();
		
		for(int i = 0; (i < this.entidadeTECHolders.value.length); i++)
		{
			tincSeqHolder seqHolder = new tincSeqHolder(this.entidadeTECHolders.value[i]);
			
			try
			{
				Constructor constructor = this.classeGPPHolder.getConstructor(new Class[]{ORB.class, tincSeqHolder.class});
				EntidadeGPPHolder entidadeGPPHolder = (EntidadeGPPHolder)constructor.newInstance(new Object[]{this.orb, seqHolder});
				result.add(entidadeGPPHolder.toEntidadeGPP());
			}
			//Caso o construtor nao for encontrado, o conversor nao foi implementado corretamente e a lista sera 
			//retornada como vazia. Se algum objeto fornecido na colecao for invalido, sera ignorado.
			catch(Exception ignore){}
		}
		
		return result;
	}
	
	/**
	 *	Converte a colecao de estruturas do GPP em uma colecao de entidades da Tecnomen.
	 *	
	 *	NOTA: Cada vez que este metodo e chamado a colecao de entidades da Tecnomen e atualizada de acordo com a 
	 *	colecao de entidades do GPP. Isto porque o objeto e serializavel, sendo retornado pela Tecnomen com o 
	 *	resultado.
	 *
	 *	@return		Colecao de entidades da Tecnomen.
	 */
	public recordSeqHolder toColecaoEntidadeTEC()
	{
		this.entidadeTECHolders.value = new tincRecord[this.entidadeGPPHolders.size()][];
		
		for(int i = 0; i < this.entidadeGPPHolders.size(); i++)
			this.entidadeTECHolders.value[i] = ((EntidadeGPPHolder)this.entidadeGPPHolders.get(i)).toEntidadeTEC().value;
		
		return this.entidadeTECHolders;
	}
	
	
	/**
	 *		@see 	java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer	result	= new StringBuffer("Colecao de Holders: " + this.classeGPPHolder.getName() + " [");
		recordSeqHolder	holder	= this.toColecaoEntidadeTEC();

		for(int i = 0; i < holder.value.length; i++)
		{
			result.append("Holder #: " + i + " [");
			
			for (int j = 0; j < holder.value[i].length; j++)
			{
				result.append("[ID: ");
				result.append(holder.value[i][j].id);
				result.append(" - Valor: ");
				result.append(holder.value[i][j].value + "]");
			}
			
			result.append("]");
		}
		
		result.append("]");

		return result.toString();
	}
	
}
