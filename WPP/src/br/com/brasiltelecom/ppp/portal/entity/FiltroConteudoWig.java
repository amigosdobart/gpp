package br.com.brasiltelecom.ppp.portal.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class FiltroConteudoWig implements Serializable
{
	private int codConteudo;
	private boolean aplicaEmParalelo;
	private int tipoRespostaFiltro;
	private int ordem;

	private FiltroWig filtro;
	private RespostaWig resposta;
	private Collection filtrosResposta;

	public static final String CLASSE_FILTRO_MSISDN    = "br.com.brasiltelecom.wig.filtrosResposta.FiltroMsisdn";
	public static final String CLASSE_FILTRO_TEMPO     = "br.com.brasiltelecom.wig.filtrosResposta.FiltroRestricaoTempo";
	public static final String CLASSE_FILTRO_ERB       = "br.com.brasiltelecom.wig.filtrosResposta.FiltroERB";
	public static final String CLASSE_FILTRO_CATEGORIA = "br.com.brasiltelecom.wig.filtrosResposta.FiltroCategoria";
	public static final String CLASSE_FILTRO_MODELO    = "br.com.brasiltelecom.wig.filtrosResposta.FiltroModelo";

	public static final String CLASSE_FILTRO_MSISDN_DB    = "br.com.brasiltelecom.ppp.portal.entity.FiltroMsisdnWig";
	public static final String CLASSE_FILTRO_TEMPO_DB     = "br.com.brasiltelecom.ppp.portal.entity.FiltroRestricaoTempoWig";
	public static final String CLASSE_FILTRO_ERB_DB       = "br.com.brasiltelecom.ppp.portal.entity.FiltroERBWig";
	public static final String CLASSE_FILTRO_CATEGORIA_DB = "br.com.brasiltelecom.ppp.portal.entity.FiltroCategoriaWig";
	public static final String CLASSE_FILTRO_MODELO_DB    = "br.com.brasiltelecom.ppp.portal.entity.FiltroModeloWig";

	public FiltroConteudoWig()
	{
		filtrosResposta = new ArrayList();
	}
	
	public FiltroWig getFiltro()
	{
		return filtro;
	}

	public void setFiltro(FiltroWig filtro)
	{
		this.filtro = filtro;
	}

	public boolean isAplicaEmParalelo()
	{
		return aplicaEmParalelo;
	}
	
	public void setAplicaEmParalelo(boolean aplicaEmParalelo)
	{
		this.aplicaEmParalelo = aplicaEmParalelo;
	}
		
	public RespostaWig getResposta()
	{
		return resposta;
	}
	
	public void setResposta(RespostaWig resposta)
	{
		this.resposta = resposta;
	}
	
	public int getTipoRespostaFiltro()
	{
		return tipoRespostaFiltro;
	}
	
	public void setTipoRespostaFiltro(int tipoRespostaFiltro)
	{
		this.tipoRespostaFiltro = tipoRespostaFiltro;
	}

	public int getCodConteudo()
	{
		return codConteudo;
	}

	public void setCodConteudo(int codConteudo)
	{
		this.codConteudo = codConteudo;
	}
	
	public boolean addFiltroResposta(FiltroRespostaWig filtroResposta)
	{
		return filtrosResposta.add(filtroResposta);
	}
	
	public boolean addFiltroResposta(Collection listaFiltrosResposta)
	{
		return filtrosResposta.addAll(listaFiltrosResposta);
	}
	
	public Collection getFiltrosResposta()
	{
		return filtrosResposta;
	}
	
	public int getOrdem()
	{
		return ordem;
	}

	public void setOrdem(int ordem)
	{
		this.ordem = ordem;
	}
	
	/**
	 * Metodo....:getNomeClasseCastor
	 * Descricao.:Realiza o De-Para da classe Wig para a classe entidade Castor.
	 * @param classeWig - Classe Wig contendo inclusive o path completo
	 * @return String   - Classe Castor a ser utilizada na pesquisa do BD
	 */
	public static String getNomeClasseCastor(String classeWig)
	{
		Map listaClasses = new HashMap();
		listaClasses.put(FiltroConteudoWig.CLASSE_FILTRO_MSISDN		, FiltroConteudoWig.CLASSE_FILTRO_MSISDN_DB);
		listaClasses.put(FiltroConteudoWig.CLASSE_FILTRO_TEMPO		, FiltroConteudoWig.CLASSE_FILTRO_TEMPO_DB);
		listaClasses.put(FiltroConteudoWig.CLASSE_FILTRO_CATEGORIA	, FiltroConteudoWig.CLASSE_FILTRO_CATEGORIA_DB);
		listaClasses.put(FiltroConteudoWig.CLASSE_FILTRO_MODELO		, FiltroConteudoWig.CLASSE_FILTRO_MODELO_DB);
		listaClasses.put(FiltroConteudoWig.CLASSE_FILTRO_ERB		, FiltroConteudoWig.CLASSE_FILTRO_ERB_DB);
		
		return (String)listaClasses.get(classeWig);
	}
	
	public String getCabecalhoTabelaHTML()
	{
		// Realiza o cast dos valores para um array destes objetos
		// e utiliza o primeiro objeto existente para retornar o 
		// cabecalho
		FiltroRespostaWig[] valores = (FiltroRespostaWig[])getFiltrosResposta().toArray(new FiltroRespostaWig[0]);
		if (valores.length > 0)
			return valores[0].getCabecalhoTabelaHTML();
		
		return "<tr><th><th></tr>";
	}
}
