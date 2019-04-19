package com.brt.gpp.gerentesPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.brt.gpp.aplicacoes.enviarSMS.dao.ConexaoSMPPDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.ConexaoSMPPConf;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.smpp.ConexaoSMPP;

/**
 * A classe <code>GerentePoolSMPP</code> e responsavel pelo gerenciamento do pool
 * de conexoes SMPP.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 14/02/2008
 */
public class GerentePoolSMPP
{
	private Map pool;
	private static GerentePoolSMPP	instancia;
	private long idProcesso;
	private GerentePoolLog log;

	public static final String CLASSE = "GerentePoolSMPP";

	private GerentePoolSMPP(long idProcesso)
	{
		log = GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso = idProcesso;
	}
	/**
	 * Metodo singleton para instancia do Pool.
	 *
	 * @return Insancia unica da classe <code>GerentePoolSMPP</code>
	 */
	public synchronized static GerentePoolSMPP getInstancia(long idProcesso)
	{
		if(instancia == null)
			instancia = new GerentePoolSMPP(idProcesso);

		return instancia;
	}
	/**
	 * Cria o pool de conexoes com a plataforma SMSC. As conexoes sao apenas instanciadas
	 * e armazenadas no pool com as devidas configuracoes. <br>
	 *
	 * @throws Exception Caso ocorra algum erro nas operacoes de banco de dados.
	 */
	public synchronized void criarPool() throws Exception
	{
		String metodo = "criarPool";

		if(pool != null)
		{
			log.log(idProcesso, Definicoes.WARN, CLASSE, metodo, "Pool de conexoes ja instanciado. " +
					"Favor executar o metodo destruirPool() antes de tentar recria-lo.");
			return;
		}

		ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();
		if(!arqConf.getBoolean("GPP_DEVE_CONSUMIR_SMS"))
		{
			log.log(idProcesso, Definicoes.INFO, CLASSE, metodo, "A variavel 'GPP_DEVE_CONSUMIR_SMS' deve ser TRUE" +
					" para inicializar o pool de conexoes SMPP.");
			return;
		}

		// Recupera a lista de conexoes configuradas na tabela TBL_GER_CONEXAO_SMPP.
		List listaCon = null;
		GerentePoolBancoDados poolBanco = GerentePoolBancoDados.getInstancia(idProcesso);
		PREPConexao conexaoBanco = null;
		try
		{
			// Recuperando conexao com o banco de dados
			conexaoBanco = poolBanco.getConexaoPREP(idProcesso);
			ConexaoSMPPDAO dao = new ConexaoSMPPDAO(idProcesso, conexaoBanco);
			listaCon = dao.listarConexoes();
		}
		catch (Exception e)
		{
			log.log(idProcesso, Definicoes.FATAL, CLASSE, metodo, "Erro ao recuperar dados do banco: "+ e);
			throw e;
		}
		finally
		{
			if(conexaoBanco != null)
				poolBanco.liberaConexaoPREP(conexaoBanco, idProcesso);
		}

		// Inicializa o pool e preenche com as lista de conexoes SMPP recuperadas do banco.
		pool = Collections.synchronizedMap(new TreeMap());
		for(Iterator it = listaCon.iterator(); it.hasNext();)
		{
			ConexaoSMPPConf conexaoConf = (ConexaoSMPPConf)it.next();
			ConexaoSMPP conexaoSMPP = new ConexaoSMPP(idProcesso, conexaoConf);

			try
			{
				conexaoSMPP.abrirConexao();
			}
			catch(Exception e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, metodo,
						"Erro ao abrir conexao com a platafroma SMSC: "+ e);
			}

			pool.put(new Integer(conexaoConf.getIdConexao()), conexaoSMPP);
		}
		log.log(idProcesso, Definicoes.DEBUG, CLASSE, metodo,
				"Criado pool de conexoes SMPP com "+pool.values().size()+" conexoes.");
	}
	/**
	 * Retorna do pool a <code>ConexaoSMPP</code> associada ao <code>idConexao</code> fornecido.
	 *
	 * @param idConexao Id da conexao a ser recuperada.
	 * @return Objeto <code>ConexaoSMPP</code>
	 * @throws NoSuchElementException Caso o pool esteja vazio.
	 */
	public ConexaoSMPP getConexao(int idConexao) throws NoSuchElementException
	{
		String metodo = "getConexao";

		if(pool == null)
		{
			log.log(idProcesso, Definicoes.WARN, CLASSE,
					metodo, "Nao foi possivel recuperar a conexao "+idConexao+
					" do pool de conexoes SMPP. O pool nao foi iniciado.");
			return null;
		}

		ConexaoSMPP conexao = null;
		// Verifica se a conexao existe.
		if(pool.containsKey(new Integer(idConexao)))
			conexao = (ConexaoSMPP)pool.get(new Integer(idConexao));
		else
		{
			// Caso nao exista pegar a primeira conexao.
			Iterator it = pool.values().iterator();

			if(it.hasNext())
				conexao = (ConexaoSMPP)it.next();
			else
				throw new NoSuchElementException("Nao existe conexoes SMPP instanciadas no pool.");
		}

		return conexao;
	}
	/**
	 * Metodo responsavel por o pool de conexoes com a plataforma SMSC.
	 */
	public synchronized void destruirPool()
	{
		String metodo = "destruirPool";

		if(pool == null)
			return;

		log.log(idProcesso, Definicoes.DEBUG, CLASSE, metodo, "Fechando conexoes com a plataforma SMSC.");

		Iterator it = pool.keySet().iterator();
		List temp = new ArrayList();

		while(it.hasNext())
		{
			Integer idConexao = (Integer)it.next();
			try
			{
				((ConexaoSMPP)pool.get(idConexao)).fecharConexao();
			}
			catch (Exception e)
			{
				log.log(idProcesso, Definicoes.ERRO, CLASSE, metodo,
						"Erro ao fechar conexao com a plataforma SMSC. " +
						"Id da conexao: "+idConexao+". Mensagem de erro: "+e);
			}

			temp.add(idConexao);
		}

		pool.keySet().removeAll(temp);

		pool = null;
	}
	/**
	 * Retorna o Identificador do processo.
	 *
	 * @return Identificador do processo.
	 */
	public long getIdProcesso()
	{
		return idProcesso;
	}
}
