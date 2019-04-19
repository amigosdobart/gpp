package br.com.brasiltelecom.wig.entity;

import br.com.brasiltelecom.wig.filtrosResposta.FiltroConteudo;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.sql.Connection;

/**
 * Esta classe representa as informacoes de filtros associados
 * a resposta e conteudo
 * 
 * @author Joao Carlos
 * Data..: 3-OUT-2005
 *
 */
public class RespostaFiltro
{
	private Resposta resposta;
	private Collection filtros;
	private int tipoRespostaFiltro;
	private boolean aplicaEmParalelo;
	private int ordem;
	
	public RespostaFiltro()
	{
		filtros = new ArrayList();
	}

	public void addFiltro(FiltroConteudo filtro)
	{
		if (filtros != null)
			filtros.add(filtro);
	}

	public Collection getFiltros()
	{
		return filtros;
	}

	public void setFiltros(Collection filtros)
	{
		this.filtros = filtros;
	}

	public int getOrdem()
	{
		return ordem;
	}

	public void setOrdem(int ordem)
	{
		this.ordem = ordem;
	}
	
	public Resposta getResposta()
	{
		return resposta;
	}

	public void setResposta(Resposta resposta)
	{
		this.resposta = resposta;
	}

	public boolean isAplicaEmParalelo()
	{
		return aplicaEmParalelo;
	}

	public void setAplicaEmParalelo(boolean aplicaEmParalelo)
	{
		this.aplicaEmParalelo = aplicaEmParalelo;
	}

	public int getTipoRespostaFiltro()
	{
		return tipoRespostaFiltro;
	}

	public void setTipoRespostaFiltro(int tipoRespostaFiltro)
	{
		this.tipoRespostaFiltro = tipoRespostaFiltro;
	}
	
	/**
	 * Metodo....:getRepostaAnexa
	 * Descricao.:Retorna o valor da resposta a ser anexada a resposta default
	 * @param con		 - Conexao de banco de dados a ser utilizada
	 * @param parameters - Parametros a serem repassados aos filtros
	 * @return String	 - Valor da resposta a ser anexada
	 */
	public String getRepostaAnexa(Connection con, Map parameters)
	{
		String anexo = "";
		// Realiza a aplicacao dos filtros dessa resposta,
		// caso retorne verdadeiro a resposta entao sera retornada
		// senao somente a string vazia vai para ser anexada
		if (aplicaEmParalelo)
		{
			if (aplicouQualquerFiltro(con,parameters))
				anexo = resposta.getDescricaoResposta();
		}
		else
			if (aplicouTodosFiltros(con,parameters))
				anexo = resposta.getDescricaoResposta();
		return anexo;
	}

	/**
	 * Metodo....:aplicouTodosFiltros
	 * Descricao.:Aplica os filtros para os parametros recebidos para identificar
	 *            se a resposta serah ou nao anexada a resposta default. Nesse caso
	 *            todos os filtros devem retornar verdadeiro para que a resposta seja
	 *            utilizada
	 * @param con		 - Conexao de banco de dados
	 * @param parameters - Parametros a serem utilizados
	 * @return boolean	 - indica se aplicou todos os filtros ou se algum deles falhou
	 */
	private boolean aplicouTodosFiltros(Connection con, Map parameters)
	{
		// Realiza a iteracao entre todos os filtros existentes para
		// essa resposta. Caso um dos filtros retorne falso entao
		// o resultado do metodo eh falso e a resposta nao deve ser
		// anexada a resposta default. Para ser uma resposta ok todos
		// os filtros devem retornar verdadeiro
		for (Iterator i=getFiltros().iterator(); i.hasNext();)
		{
			FiltroConteudo filtro = (FiltroConteudo)i.next();
			if (!filtro.validarParametros(parameters) || !filtro.aplicarFiltro(resposta,con))
				return false;
		}
		return true;
	}
	
	/**
	 * Metodo....:aplicouQualquerFiltro
	 * Descricao.:Aplica os filtros para os parametros recebidos para identificar
	 *            se a resposta serah ou nao anexada a resposta default. Nesse caso
	 *            qualquer filtro que retorne verdadeiro entao jah possibilita a inclusao
	 *            da resposta
	 * @param con		 - Conexao de banco de dados
	 * @param parameters - Parametros a serem validados
	 * @return boolean	 - Indica se aplicou qualquer um dos filtros cadastrados
	 */
	private boolean aplicouQualquerFiltro(Connection con, Map parameters)
	{
		for (Iterator i=getFiltros().iterator(); i.hasNext();)
		{
			FiltroConteudo filtro = (FiltroConteudo)i.next();
			if (filtro.validarParametros(parameters) && filtro.aplicarFiltro(resposta,con))
				return true;
		}
		return false;
	}

	public int hashCode()
	{
		return resposta.hashCode()+getFiltros().hashCode();
	}
	
	public String toString()
	{
		return resposta.getCodigoResposta()+"->"+getFiltros();
	}
	
	public boolean equals(Object obj)
	{
		if (!(obj instanceof RespostaFiltro))
			return false;
		
		RespostaFiltro objComp = (RespostaFiltro)obj;
		return this.getResposta().equals(objComp.getResposta()) && this.getOrdem() == objComp.getOrdem();
	}
	
	public int compareTo(Object obj)
	{
		if (!(obj instanceof RespostaFiltro))
			return 0;
		
		if ( this.getResposta().equals(((RespostaFiltro)obj).getResposta()) )
		{
			if ( this.getOrdem() > ((RespostaFiltro)obj).getOrdem() )
				return 1;
			
			if ( this.getOrdem() < ((RespostaFiltro)obj).getOrdem() )
				return -1;
			
			return 0;
		}
		
		return this.getResposta().compareTo(((RespostaFiltro)obj).getResposta());
	}
}
