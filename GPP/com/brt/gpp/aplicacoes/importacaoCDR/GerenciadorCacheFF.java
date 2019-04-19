package com.brt.gpp.aplicacoes.importacaoCDR;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapPromocaoWhiteList;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerenteArquivosCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.AssinanteCache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.sql.ResultSet;

/**
 * Esta classe tem por objetivo gerenciar o cache de dados para identificar a lista de Family & Friends a ser
 * utilizado na importacao de CDRs. Para a totalizacao do PulaPula, o CDR recebido nao indica se o numero que
 * efetuou a ligacao utilizou o Amigos toda hora. Caso tenha utilizado entao o assinante que estah recebendo
 * esta chamada deve ter seu valor de pulapula reduzido, afim de evitar fraudes utilizando o desconto FF.
 * Este Singleton gerencia um cache de dados que tem como chave o numero do assinante. Para cada valor um objeto
 * FamilyFriendsVO eh iniciado para conter alem da lista FF, a data de inicializacao do cache. Se o valor expirar
 * este deve ser novamente consultado na base de dados para atualizacao da lista.
 * 
 * @author Joao Carlos
 * Data..: 04/10/2005
 * 
 * Alteracoes: 18/10/2005
 * 			   As informacoes sao inicializadas no cache atraves da leitura da tabela de assinantes que possuem
 *             status igual a NORMAL USER. As informacoes permanecem lah ateh o proximo processamento definido
 *             na configuracao do sistema. Se o assinante nao existir no cache eh considerado que este nao possui
 *             nenhuma restricao de FF
 *
 */
public class GerenciadorCacheFF extends Thread
{
	private 		Map					cacheDados;
	private 		long				hits;
	private			long				numeroChamadas;
	private			int					tempoEspera;
	private			Collection			planosPosPagoFaleGratis		= new ArrayList();
	private			Collection			planosPrePagoFaleGratis		= new ArrayList();
	private			Collection			planosControleFaleGratis	= new ArrayList();
    private         Collection          promocoesAmigosDeGraca      = new ArrayList();
	
	private static 	GerenciadorCacheFF	instance;
	private static String sqlPesquisa = 
		"select a.idt_msisdn " +
		      ",idt_plano_preco " +
		      ",p.idt_promocao " +
		      ",nvl((select d.num_dia_execucao " +
	                  "from tbl_pro_dia_execucao d " + 
	                 "where d.idt_promocao = pa.idt_promocao " +
	                   "and d.tip_execucao = ? " +
	                   "and d.num_dia_entrada = to_number(to_char(pa.dat_entrada_promocao,'dd')) " +
	                "),1) as dia_entrada_fgn " +
              ", null as idt_amigos_gratis " +
              ", 1 as ind_ativo " +
	      "from tbl_apr_assinante a, tbl_pro_assinante pa, tbl_pro_promocao p " + 
	     "where pa.idt_msisdn (+) = a.idt_msisdn " +
	       "and p.idt_promocao (+) = pa.idt_promocao " + 
	       "and a.idt_status != ? " +
	       "and (p.idt_categoria = ? or p.idt_promocao is null ) " +
	   "union all  " +
	   "select idt_msisdn " +
              ",? " +
              ",idt_promocao " +
              ",nvl(to_number(to_char(dat_inclusao,'dd')),1) as dia_entrada_fgn " +
              ",idt_amigos_gratis " +
              ",ind_ativo " + 
	     "from tbl_apr_assinante_pospago ";
		

	private GerenciadorCacheFF()
	{
		cacheDados = Collections.synchronizedMap(new TreeMap());
		try
		{
			// Busca o valor de espera para o novo processamento de insercao no cache
			// das informacoes de assinantes.
			MapConfiguracaoGPP conf = MapConfiguracaoGPP.getInstancia();
			tempoEspera = Integer.parseInt(conf.getMapValorConfiguracaoGPP("TEMPO_ESPERA_GERENTE_CACHE_FF"));
			
			String strControleFaleGratis[]       = conf.getMapValorConfiguracaoGPP("PLANOS_CONTROLE_FALEGRATIS").split("[,]");
			String strPosPagoFaleGratis[]        = conf.getMapValorConfiguracaoGPP("PLANOS_POSPAGO_FALEGRATIS").split("[,]");
			String strPrePagoFaleGratis[]        = conf.getMapValorConfiguracaoGPP("PLANOS_PREPAGO_FALEGRATIS").split("[,]");
            String strPromocoesAmigosDeGraca[]   = conf.getMapValorConfiguracaoGPP("PROMOCOES_AMIGOS_GRACA").split("[,]");
			
			adicionaItens(planosPosPagoFaleGratis	,strPosPagoFaleGratis);
            adicionaItens(planosControleFaleGratis	,strControleFaleGratis);
            adicionaItens(planosPrePagoFaleGratis	,strPrePagoFaleGratis);
            adicionaItens(promocoesAmigosDeGraca    ,strPromocoesAmigosDeGraca);
			
			start();
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.ERRO,"GerenciadorCacheFF","run","Erro nos parametros de gerenciamento do cache FF. Erro:"+e);
		}
	}

	/**
	 * Metodo....:getInstance
	 * Descricao.:Retorna a instancia desse objeto singleton
	 * @return GerenciadorCacheFF - Singleton do gerenciador do cache
	 */
	public synchronized static GerenciadorCacheFF getInstance()
	{
		// Se a instancia do sistema GPP permitir a importacao de CDRs, entao o cache
		// eh construido e inicializado, senao nada eh feito e a referencia para o objeto
		// retorna nulo
		ArquivoConfiguracaoGPP conf = ArquivoConfiguracaoGPP.getInstance();
		if (conf.deveImportarCDR())
			if (instance == null)
				instance = new GerenciadorCacheFF();
		
		return instance;
	}
	
	/**
	 * Metodo....:isPromocaoFaleGratis
	 * Descricao.:Identifica se o assinante participa da promocao FaleGratis
	 *            Esta promocao eh configurada para assinantes Controle de um
	 *            determinado plano de preco configurado na plataforma de prepago
	 *            assim como assinantes pospago configurados em uma tabela
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @return boolean - Se o assinante possui o plano PosPago ou Controle da promocao fale gratis
	 */
	public boolean isPromocaoFaleGratis(String msisdn)
	{
		AssinanteCache assinante = getAssinante(msisdn);
		if (assinante != null)
		{
			String planoAssinante = String.valueOf(assinante.getIdPlanoPreco());
            String promoAssinante = String.valueOf(assinante.getIdPromocao());
            
			return (   (planosPosPagoFaleGratis.contains(planoAssinante) && !promocoesAmigosDeGraca.contains(promoAssinante))
                    ||	planosPrePagoFaleGratis.contains(planoAssinante) 
                    ||  planosControleFaleGratis.contains(planoAssinante)); 
		}
		return false;
	}
	
	/**
	 * Metodo....:isPlanoFaleGratis
	 * Descricao.:Identifica se o plano de preco passado como parametro
	 *            faz parte dos planos de preco registrados como participantes
	 *            da promocao fale gratis a noite
	 * @param planoPreco	- Plano de preco a ser analisado
	 * @return boolean
	 */
	public boolean isPlanoFaleGratis(long planoPreco)
	{
		String planoStr = String.valueOf(planoPreco);
		return (planosPosPagoFaleGratis.contains(planoStr) ||
				planosPrePagoFaleGratis.contains(planoStr) ||
				planosControleFaleGratis.contains(planoStr));
	}
	
	/**
	 * Metodo....:getAssinante
	 * Descricao.:Retorna as informacoes do assinante que estao no cache em memoria
	 * 			  OBS: Se o assinante nao existir em memoria nao significa que o 
	 *                 assinante nao existe. Verificar condicoes para os assinantes em memoria
	 * @param msisdn - Msisdn desejado
	 * @return Assinante - Informacoes do assinante
	 */
	public AssinanteCache getAssinante(String msisdn)
	{
		return (AssinanteCache)cacheDados.get(msisdn);
	}

	/**
	 * Metodo....:isPrePagoOuControle
	 * Descricao.:Este metodo informa se o assinante sendo pesquisado eh um assinante
	 *            do plano pre-pago ou controle. Para isso precisamos identificar primeiro
	 *            se o assinante estah no cache de dados. Se estiver precisamos identificar
	 *            se a promocao pula-pula eh relativa ao falegratis, jah que eh a unica forma
	 *            de um assinante pos-pago existir no cache.
	 *            Caso o assinante nao exista no cache entao eh considerado um pos-pago.
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @return boolean - indica se o assinante eh um pre-pago ou controle
	 */
	public boolean isPrepagoOuControle(String msisdn)
	{
		AssinanteCache assinante = getAssinante(msisdn);
		if ( assinante != null && !planosPosPagoFaleGratis.contains(String.valueOf(assinante.getIdPlanoPreco())) )
			return true;
		
		return false;
	}
	
	/**
	 * Metodo....:isControleTotal
	 * Descricao.:Este metodo informa se o assinante pertence a um plano de preco Controle Total
	 * @param msisdn - Msisdn do assinante a ser pesquisado
	 * @return boolean - indica se o assinante eh CT
	 */
	public boolean isControleTotal(String msisdn)
	{
		AssinanteCache assinante = getAssinante(msisdn);
		if ( assinante != null)
		{
			MapPlanoPreco map = MapPlanoPreco.getInstancia();
			PlanoPreco plano = map.getPlanoPreco(assinante.getIdPlanoPreco());
			
			return (plano != null && plano.getCategoria() != null && 
					plano.getCategoria().getIdCategoria() == Definicoes.CATEGORIA_CT);
		}
		
		return false;
	}
    
    /**
     * Metodo....:isPromocaoAmigosDeGraca
     * Descricao.:Este metodo informa se o assinante pertence a uma promocao FGN4 (amigos de graca)
     * @param msisdn - Msisdn do assinante a ser pesquisado
     * @return boolean - indica se o assinante eh FGN4
     */
    public boolean isPromocaoAmigosDeGraca(String msisdn)
    {
        AssinanteCache assinante = getAssinante(msisdn);
        if ( assinante != null)
        {
            // Verifica se o assinante possui uma promocao FGN4
            if (promocoesAmigosDeGraca.contains(String.valueOf(assinante.getIdPromocao())))
                return true;
        }
        
        return false;
    }
	
	/**
	 * Metodo....:pertenceWhiteList
	 * Descricao.:Este metodo verifica se o assinante estah na WhiteList
	 *            para um determinado tipo de desconto de bonificacao pula pula.
	 * @param msisdn	- Msisdn do assinante
	 * @param motivo    - Motivo de desconto
	 * @return boolean
	 */
	public boolean pertenceWhiteList(String msisdn, int motivo) throws Exception
	{
		AssinanteCache assinante = getAssinante(msisdn);
		if (assinante != null)
		{
			MapPromocaoWhiteList whiteList = MapPromocaoWhiteList.getInstance();
			if (whiteList.isWhiteList(motivo, msisdn, assinante.getIdPromocao()))
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo....:limpaCacheDados
	 * Descricao.:Remove todas as entradas no cache de dados
	 *
	 */
	public void limpaCacheDados()
	{
		cacheDados.clear();
	}

	/**
	 * Metodo....:limpaDadosAssinante
	 * Descricao.:Remove as informacoes no cache para um determinado assinante
	 * @param msisdn - Assinante a ter seus dados removidos do cache
	 */
	public void limpaDadosAssinante(String msisdn)
	{
		cacheDados.remove(msisdn);
	}

	/**
	 * Metodo....:percentualHits
	 * Descricao.:Retorna o percentual de acerto no cache de dados
	 * @return long - Percentual de acerto no cache
	 */
	public double percentualHits()
	{
		if (numeroChamadas != 0)
			return (hits*100/numeroChamadas);
		else return 0;
	}

	/**
	 * Metodo....:iniciaCacheDados
	 * Descricao.:Inicia o cache de dados contendo as informacoes da tabela de assinantes
	 *
	 */
	public void iniciaCacheDados()
	{
		// Realiza a pesquisa dos assinantes e disponibiliza em memoria
		// as informacoes de plano de preco e a lista de family & friends
		// Somente serao iniciados os assinantes que possuirem lista de FF
		// e que estejam em status igual a NORMAL USER
		GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.INFO,"GerenciadorCacheFF","run","Inicio da carga dos assinantes para a memoria.");
		PREPConexao conexaoPrep = null;
		try
		{
			// Em adicao aos usuarios em status NORMAL os assinantes que possuirem 
			// os planos 3 e 8 (Plano Noturno) tambem serao carregados na memoria
			// para a identificacao de tarifacao reduzida
			String planoDefaultPosPago = ((String[])planosPosPagoFaleGratis.toArray(new String[0]))[0];
			conexaoPrep = GerentePoolBancoDados.getInstancia(0).getConexaoPREP(0);
			Object params[] = {"FALEGRATIS"
					          ,new Integer(Definicoes.STATUS_SHUTDOWN)
			                  ,new Integer(Definicoes.CATEGORIA_PULA_PULA)
							  ,new Integer(planoDefaultPosPago)
							  };
			ResultSet rs = conexaoPrep.executaPreparedQuery(sqlPesquisa,params,0);
			rs.setFetchSize(1000);
			rs.setFetchDirection(ResultSet.FETCH_FORWARD);
			while (rs.next())
			{
				AssinanteCache assinante = new AssinanteCache(rs.getString("idt_msisdn"));
				assinante.setIdPlanoPreco(rs.getByte("idt_plano_preco"));
				assinante.setIdPromocao	 (rs.getByte("idt_promocao"));
				assinante.setDiaEntradaFaleGratis(rs.getByte("dia_entrada_fgn"));
                assinante.setAmigosGratis(rs.getString("idt_amigos_gratis"));
                assinante.setAtivo(rs.getBoolean("ind_ativo"));

                if (!assinante.isAtivo())
                    cacheDados.remove(rs.getString(1));
                else
                    cacheDados.put(rs.getString(1),assinante);
			}
			GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.INFO,"GerenciadorCacheFF","run","Fim da carga dos assinantes para a memoria. NroObjetos:"+cacheDados.size());
			// Apos a inicializacao dos assinantes em memoria a importacao de CDRs pode ser iniciada
			// Enquanto o cache estiver vazio a importacao NAO sera inicializada
			GerenteArquivosCDR.getInstance(0);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(this.getClass()).log(0,Definicoes.ERRO,"GerenciadorCacheFF","run","Erro ao iniciar assinantes na memoria. Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(0).liberaConexaoPREP(conexaoPrep,0);
		}
	}

	/**
	 * Metodo....:adicionaPlanosFaleGratis
	 * Descricao.:Adiciona os planos configurados para uma colecao dos planos
	 *            existentes na fale gratis.
	 * @param planos - Array de strings contendo os planos a serem adicionados
	 */
	private void adicionaItens(Collection lista, String valores[])
	{
		for (int i=0; i < valores.length; i++)
            lista.add(valores[i]);
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		while(true)
		{
			iniciaCacheDados();
			try{
				// Apos processar o cache entra em espera para novo processamento
				synchronized(this){this.wait(tempoEspera*1000);}
			}catch(Exception e){};
		}
	}
}
