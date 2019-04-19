package com.brt.gpp.aplicacoes.campanha;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.campanha.dao.CampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.InscricaoAssinanteVO;
import com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha;

/**
 * Esta classe representa o procedimento Produtor do processo de inscricao de 
 * assinantes em campanhas promocionais. Este produtor lista todos os assinantes 
 * existentes na base de dados, sendo que para cada assinante o processo consumidor 
 * 
 * 
 * 
 * irah realizar o trabalho de verificacao da inscricao.
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class GerInscricaoCampanhaProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private PREPConexao	conexaoPrep;
	private Map			produtoresCampanha;
	private int			numeroProdutores;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public GerInscricaoCampanhaProdutor(long idProcesso) 
	{
		super(idProcesso, Definicoes.CL_GER_INSCRICAO_ASSIN_CAMP_PROD);
	}

	/**
	 * @param params[]
	 * @throws java.lang.Exception
	 */
	public void startup(String params[]) throws Exception 
	{
		// O inicio deste produtor, consiste em identificar quais campanhas utilizam produtores definidos
		// dinamicamente atraves de reflection. Esta identificacao eh necessaria afim de reutilizar um
		// mesmo ProdutorCampanha quando mais de uma campanha o utiliza. Apos esta definicao, um mapeamento
		// entao eh criado contendo como chave o nome da classe do ProdutorCampanha e como valor uma lista
		// de campanhas que o utiliza.
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio Gerenciamento de Inscricao de Assinantes para Campanhas");
		// A conexao de banco de dados eh obtida para uso pelo consumidor, dessa forma eh possivel
		// a implementacao de uma unica transacao para todo o processamento
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		produtoresCampanha = new HashMap();
		
		// Realiza a iteracao em todas as campanhas com vigencia atual
		for (Iterator i = CampanhaDAO.getCampanhasVigentes().iterator(); i.hasNext();)
		{
			Campanha campanha = (Campanha)i.next();
			// Verifica se no mapeamento existe a colecao de campanhas para o nome da classe
			// produtor utilizado pela campanha sendo processada. Se nulo, entao cria a colecao
			// e adiciona a campanha, se jah existe entao somente adiciona a campanha e volta
			// a incluir a informacao no mapeamento
			Collection campanhas = (Collection)produtoresCampanha.get(campanha.getProdutorCampanha());
			if (campanhas == null)
				campanhas = new ArrayList();
			
			// Inicializa o Produtor antes de adiciona-lo ao mapeamento
			campanha.getProdutorCampanha().startup(this.adicionarParametro(params, String.valueOf(campanha.getId())));
			
			campanhas.add(campanha);
			produtoresCampanha.put(campanha.getProdutorCampanha(),campanhas);
		}
		// Armazena o numero de produtores que serah executados
		// pois apos o processamento estes sao removidos do mapeamento
		numeroProdutores = produtoresCampanha.size();
	}
	
	/**
	 * Este metodo disponibiliza os assinantes da base de dados a serem verificados 
	 * para a inscricao em campanhas promocionais
	 * 
	 * @return obj - Assinante a ser possivelmente inscrito em campanhas
	 */
	public Object next() throws Exception
	{
		InscricaoAssinanteVO vo = null;
		// Serah processado todos os produtores associados a
		// todas as campanhas em vigencia, portanto uma iteracao
		// eh feita no mapeamento e um produtor, que pode atender a 
		// mais de uma campanha, eh executado por vez. Quando um
		// produtor retornar nulo, entao ele eh retirado do mapeamento
		// para que esta classe saiba quando este nao deve mais ser
		// executado. Quando nao houver mais nenhum produtor entao
		// o processo terminou.
		for (Iterator i = produtoresCampanha.keySet().iterator(); i.hasNext();)
		{
			ProdutorCampanha prodCampanha = (ProdutorCampanha)i.next();
			// Busca o next do produtor definido para campanha
			Assinante assinante = (Assinante)prodCampanha.next();
			if (assinante == null)
			{
				// Se o next deste produtor retornou nulo entao o
				// produtor eh removido da lista de processamento
				i.remove();
				continue;
			}
			vo = new InscricaoAssinanteVO();
			vo.setProdutorCampanha(prodCampanha);
			vo.setAssinante(assinante);
			vo.setListaCampanhas((Collection)produtoresCampanha.get(prodCampanha));
			
			// Retorna o VO pois nao pode realizar mais nenhuma
			// iteracao na lista
			return vo;
		}
		return vo;
	}
	
	/**
	 * @return int - Id do processo batch
	 */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_GER_INSCRICAO_ASSINANTES_CAMP;
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getDescricaoProcesso() 
	{
		return "Gerenciador de inscricao de assinantes processou "+numeroProdutores+" Produtores de Campanha.";
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getStatusProcesso() 
	{
		return statusProcesso;
	}
	
	/**
	 * @param status
	 */
	public void setStatusProcesso(String status) 
	{
		statusProcesso = status;
	}
	
	/**
	 * @return java.lang.String
	 */
	public String getDataProcessamento() 
	{
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * @return com.brt.gpp.comum.conexoes.bancoDados.PREPConexao
	 */
	public PREPConexao getConexao() 
	{
		return conexaoPrep;
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	public void finish() throws Exception 
	{
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}
	
	public void handleException() 
	{
	}
	
	//Outros metodos.
	
	/**
	 *	Adiciona um objeto String a lista de parametros.
	 *
	 *	@param		parametros				Lista de parametros.
	 *	@param		string					Objeto String a ser adicionado a lista
	 */
	public String[] adicionarParametro(String[] parametros, String string) throws Exception 
	{
		String[] result = null;
		
		if(parametros != null)
		{
			result = new String[parametros.length + 1];
			for(int i = 0; i < parametros.length; i++)
				result[i] = parametros[i];
			result[parametros.length] = string;
		}
		
		return result;
	}
	
}
