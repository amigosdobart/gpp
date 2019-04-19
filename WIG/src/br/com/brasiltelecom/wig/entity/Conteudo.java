package br.com.brasiltelecom.wig.entity;

import br.com.brasiltelecom.wig.dao.ValidadorConteudo;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Iterator;
import java.sql.Connection;

/**
 * Esta classe define a estrutura de armazenamento da entidade conteudo. Definindo
 * aqui o tratamento inicial do WML que sera utilizado na resposta para o chamador
 * do wigControl
 * 
 * @author Joao Carlos
 * Data..:31/05/2005
 *
 */

public class Conteudo
{
	private int 		codigoConteudo;
	private Servico		servico;
	private String		descricaoConteudo;
	private boolean 	bloqueiaConcorrente;
	private Resposta 	resposta;
	private Collection	agrupamentoConteudo;
	private Collection	validadores;
	private Collection  respostasFiltros;
	private boolean		registraAcesso;
	private int			billingCode;
	
	private static final int TIPO_RESP_FILTRO_OPTION 	= 1;
	private static final int TIPO_RESP_FILTRO_MENSAGEM 	= 2;
	
	public Conteudo(Servico servico, int codigoConteudo)
	{
		this.servico 				= servico;
		this.codigoConteudo 		= codigoConteudo;
		this.agrupamentoConteudo 	= new LinkedHashSet();
		this.validadores			= new LinkedHashSet();
	}

	public boolean bloqueiaConcorrente()
	{
		return bloqueiaConcorrente;
	}
	
	public int getCodigoConteudo()
	{
		return codigoConteudo;
	}
	
	public String getDescricaoConteudo()
	{
		return descricaoConteudo;
	}
	
	public Resposta getResposta()
	{
		return resposta;
	}
	
	public Servico getServico()
	{
		return servico;
	}

	public boolean deveValidar()
	{
		return validadores.size() > 0;
	}

	public Collection getAgrupamentoConteudo()
	{
		return agrupamentoConteudo;
	}

	public Collection getValidadores()
	{
		return validadores;
	}

	public Collection getRespostasFiltros()
	{
		return respostasFiltros;
	}
	
	public boolean registraAcesso()
	{
		return registraAcesso;
	}
	
	public int getBillingCode()
	{
		return billingCode;
	}

	public void setBillingCode(int billingCode)
	{
		this.billingCode = billingCode;
	}
	
	/**
	 * Metodo....:getRespostasAnexas
	 * Descricao.:Retorna o valor a ser anexado a resposta default
	 *            dos filtros de conteudo aplicando os valores dos
	 *            parametros
	 * @param con		 - Conexao de banco de dados a ser utilizada
	 * @param parameters - Parametros recebidos
	 * @return String	 - Valor da respostas a serem anexadas a resposta default
	 */
	public String getRespostasAnexas(Connection con, Map parameters)
	{
		StringBuffer resposta = new StringBuffer("");
		// Realiza a iteracao entre todas as respostas filtros existentes
		// para o conteudo. Para cada uma delas o valor da resposta anexa
		// sera adicionada ao buffer.. caso a resposta seja vazia entao
		// o buffer continuara sem algum valor significante
		for (Iterator i=getRespostasFiltros().iterator(); i.hasNext();)
		{
			RespostaFiltro respFiltro = (RespostaFiltro)i.next();
			// Insere a resposta somente se o tipo da resposta filtro indicar
			// uma resposta que seja para a utilizacao de opcoes de menu (select-options)
			if (respFiltro.getTipoRespostaFiltro() == TIPO_RESP_FILTRO_OPTION)
				resposta.append(respFiltro.getRepostaAnexa(con,parameters) + "\n");
		}
		return resposta.toString();
	}
	
	/**
	 * Metodo....:getMensagensAnexas
	 * Descricao.:Retorna o valor a ser anexado a resposta default
	 *            dos filtros de conteudo aplicando os valores dos
	 *            parametros
	 * @param con		 - Conexao de banco de dados a ser utilizada
	 * @param parameters - Parametros recebidos
	 * @return String	 - Valor da respostas a serem anexadas a resposta default
	 */
	public String getMensagensAnexas(Connection con, Map parameters)
	{
		StringBuffer resposta = new StringBuffer("");
		// Realiza a iteracao entre todas as respostas filtros existentes
		// para o conteudo. Para cada uma delas o valor da resposta anexa
		// sera adicionada ao buffer.. caso a resposta seja vazia entao
		// o buffer continuara sem algum valor significante
		for (Iterator i=getRespostasFiltros().iterator(); i.hasNext();)
		{
			RespostaFiltro respFiltro = (RespostaFiltro)i.next();
			// Insere a resposta somente se o tipo da resposta filtro indicar
			// uma resposta que seja para a utilizacao de mensagens
			if (respFiltro.getTipoRespostaFiltro() == TIPO_RESP_FILTRO_MENSAGEM)
				resposta.append(respFiltro.getRepostaAnexa(con,parameters) + "\n");
		}
		return resposta.toString();
	}

	public void setBloqueiaConcorrente(boolean bloqueiaConcorrente)
	{
		this.bloqueiaConcorrente = bloqueiaConcorrente;
	}
	
	public void setCodigoConteudo(int codigoConteudo)
	{
		this.codigoConteudo = codigoConteudo;
	}
	
	public void setDescricaoConteudo(String descricaoConteudo)
	{
		this.descricaoConteudo = descricaoConteudo;
	}
	
	public void setResposta(Resposta resposta)
	{
		this.resposta = resposta;
	}

	public void setAgrupamentoConteudo(Collection agrupamento)
	{
		this.agrupamentoConteudo = agrupamento;
	}

	public void setValidadores(Collection validadores)
	{
		this.validadores = validadores;
	}

	public void setRespostasFiltros(Collection respostasFiltros)
	{
		this.respostasFiltros = respostasFiltros;
	}
	
	public void setRegistraAcesso(boolean registraAcesso)
	{
		this.registraAcesso = registraAcesso;
	}

	public boolean addConteudo(Conteudo conteudo)
	{
		return agrupamentoConteudo.add(conteudo);
	}
	
	public boolean addValidador(ValidadorConteudo validador)
	{
		return validadores.add(validador);
	}

	public boolean equals(Object obj)
	{
		if (!(obj instanceof Conteudo))
			return false;
		
		if ( ((Conteudo)obj).getCodigoConteudo() == this.getCodigoConteudo() )
			return true;
		return false;
	}

	public int hashCode()
	{
		return this.getCodigoConteudo();
	}

	public String toString()
	{
		return this.getDescricaoConteudo();
	}
}
