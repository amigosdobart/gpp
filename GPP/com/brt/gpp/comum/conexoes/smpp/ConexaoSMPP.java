package com.brt.gpp.comum.conexoes.smpp;


import com.brt.gpp.aplicacoes.enviarSMS.entidade.ConexaoSMPPConf;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.Timer;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.logica.smpp.Connection;
import com.logica.smpp.Data;
import com.logica.smpp.Session;
import com.logica.smpp.TCPIPConnection;
import com.logica.smpp.pdu.Address;
import com.logica.smpp.pdu.BindRequest;
import com.logica.smpp.pdu.BindResponse;
import com.logica.smpp.pdu.BindTransciever;
import com.logica.smpp.pdu.UnbindResp;

/**
 * A classe <code>ConexaoSMPP</code> e responsavel pela administracao da conexao com a plataforma SMSC.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 06/12/2007
 */
public class ConexaoSMPP
{
	private Session					sessao;
	private ArquivoConfiguracaoGPP	arqConf;
	private TCPIPConnection			socket;
	private RespostaSMSC			respostaSMSC;
	private RequisicaoSMSC			requisicaoSMSC;
			boolean 				conectado;
			Timer					timer;
	private ConexaoSMPPConf			conexaoConfig;

	// Atributos necessarios para o funcionamento do LOG
	private GerentePoolLog	log;
	private long 			idProcesso;
	private static String 	classe;

	// Parametros presentes no arquivo de configuracoes do GPP.
	public static final String SMPP_NOME_MAQUINA	= "SMPP_NOME_MAQUINA";
	public static final String SMPP_PORTA_MAQUINA	= "SMPP_PORTA_MAQUINA";
	public static final String SMPP_NOME_USUARIO	= "SMPP_NOME_USUARIO";
	public static final String SMPP_SENHA_USUARIO	= "SMPP_SENHA_USUARIO";
	public static final String SMPP_MAX_REQUISICOES	= "SMPP_MAX_REQUISICOES";

	// Constantes utilizadas para abrir conexao com a SMSC
	public static final String SMPP_SYSTEM_TYPE			= "MMSTECMNCTA";
	// Expressao regular (UNIX) representando os formatos de numeros suportados pela SMSC
	public static final String SMPP_ADDRESS_RANGE		= "";

	public ConexaoSMPP(long idProcesso, ConexaoSMPPConf conexaoConfig) throws GPPInternalErrorException
	{
		// Captura o nome da classe
		StringBuffer sb 	= new StringBuffer(this.getClass().getName());
		classe 				= sb.substring(sb.lastIndexOf(".")+1);
		this.idProcesso 	= idProcesso;
		this.log	 		= GerentePoolLog.getInstancia(this.getClass());

		this.conexaoConfig 	= conexaoConfig;

		this.arqConf		= ArquivoConfiguracaoGPP.getInstance();
		this.timer			= new Timer(arqConf.getInt(SMPP_MAX_REQUISICOES), 1000);

		this.respostaSMSC	= new RespostaSMSC(this);
		this.requisicaoSMSC = new RequisicaoSMSC(this);
	}
	/**
	 * Abre uma conexao com a plataforma SMSC.
	 *
	 * @param ip		 IP da maquina SMSC
	 * @param porta		 Porta da maquina SMSC
	 * @throws Exception Caso ocorra algum erro na abertura da conexao.
	 */
	public synchronized void abrirConexao() throws Exception
	{
		String metodo = "abrirConexao";

		if(conectado)
			return;

		log.log(idProcesso, Definicoes.INFO, classe, metodo, "Efetudando conexao com a plataforma SMSC...");

		// Instancia conexao socket
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo,
				"Abrindo conexao TCP-IP: " + conexaoConfig.getIpMaquina() +":"+conexaoConfig.getPorta());

		socket = new TCPIPConnection(conexaoConfig.getIpMaquina(), conexaoConfig.getPorta());
		socket.setReceiveTimeout(Data.CONNECTION_RECEIVE_TIMEOUT);

		// Instancia Sessao SMPP
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Abrindo sessao SMPP...");
		this.sessao = new Session(socket);

		// Instancia requisicao para abrir conexao com a plataforma SMSC
		BindRequest requisicao = new BindTransciever();

		requisicao.setSystemId(conexaoConfig.getUsuario());
		requisicao.setPassword(conexaoConfig.getSenha());
		requisicao.setSystemType(conexaoConfig.getTipoSistema());
		requisicao.setInterfaceVersion(Data.SMPP_V34);
		requisicao.setAddressRange(Data.GSM_TON_INTERNATIONAL, Data.GSM_NPI_ISDN, SMPP_ADDRESS_RANGE);

		// Abre conexao com a plataforma SMSC
		StringBuffer debug = new StringBuffer();
		debug.append("Enviando requisicao BIND_TRANSCEIVER.");
		debug.append(" -System Id: ").append(requisicao.getSystemId());
		debug.append(" -Password: ").append(requisicao.getPassword());
		debug.append(" -System Type: ").append(requisicao.getSystemType());
		debug.append(" -Addr TON: ").append(requisicao.getAddressRange().getTon());
		debug.append(" -Addr NPI: ").append(requisicao.getAddressRange().getNpi());
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, debug.toString());

		BindResponse resposta = sessao.bind(requisicao, respostaSMSC);
		if(resposta == null)
		{
			log.log(idProcesso, Definicoes.ERRO, classe, metodo, "Falha ao efetuar conexao com a plataforma SMSC. Nao houve resposta.");
			return;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Resposta BIND_TRANSCEIVER_RESP recebida. -Status: "+resposta.getCommandStatus());

		if(resposta.getCommandStatus() == Data.ESME_ROK)
		{
			log.log(idProcesso, Definicoes.INFO, classe, metodo, "Conexao com a plataforma SMSC efetuada com sucesso!");
			conectado = true;
		}
		else
			log.log(idProcesso, Definicoes.ERRO, classe, metodo,
					"Falha ao efetuar conexao com a plataforma SMSC. Status da resposta invalido: "+resposta.getCommandStatus());
	}
	/**
	 * Fecha conexao com a plataforma
	 *
	 * @throws Exception Caso ocorra algum erro no encerramento da execucao.
	 */
	public synchronized void fecharConexao() throws Exception
	{
		String metodo = "fecharConexao";

		if(!conectado)
			return;

		log.log(idProcesso, Definicoes.INFO, classe, metodo, "Encerrando conexao com a plataforma SMSC...");
		// Fecha conexao com a plataforma SMSC
		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Enviando requisicao UNBIND a plataforma SMSC...");
		UnbindResp resposta = sessao.unbind();

		if(resposta == null)
		{
			log.log(idProcesso, Definicoes.ERRO, classe, metodo, "Falha ao encerrar conexao com a plataforma SMSC. Nao houve resposta.");
			return;
		}

		log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Resposta UNBIND_RESP recebida. -Status: "+resposta.getCommandStatus());

		if(resposta.isOk())
		{
			log.log(idProcesso, Definicoes.DEBUG, classe, metodo, "Fechando sessao SMPP...");
			sessao.close();
			log.log(idProcesso, Definicoes.INFO, classe, metodo, "Conexao com a plataforma SMSC encerrada com sucesso!");
			// Finaliza Timer
			timer.shutdown();
			conectado = false;
		}
		else
			log.log(idProcesso, Definicoes.ERRO, classe, metodo,
					"Falha ao encerrar conexao com a plataforma SMSC. Status da resposta invalido: "+resposta.getCommandStatus());
	}
	/**
	 * Retorna a conexao estabelecida com a SMSC.
	 *
	 * @return Objecto <code>com.logica.smpp.Connection</code> representando a conexao estabelecida
	 */
	public Connection getConexao()
	{
		if(sessao != null)
			return sessao.getConnection();
		return null;
	}
	/**
	 * Retorna a sessao estabelecida com a SMSC.
	 *
	 * @return Objecto <code>com.logica.smpp.Session</code> representando a sessao estabelecida
	 */
	public Session getSessao()
	{
		return sessao;
	}
	/**
	 * Retorna o status da conexao.
	 *
	 * @return <code>true</code> se a conexao estiver estabelecida, <code>false</code> caso contrario.
	 */
	public boolean isConectado()
	{
		return conectado;
	}
	/**
	 * Retorna o idProcesso da Aplicacao.
	 *
	 * @return	idProcesso
	 */
	public long getIdProcesso()
	{
		return idProcesso;
	}
	/**
	 * Objeto responsavel por tratar as resposta desta conexao.
	 *
	 * @return objeto RespostaSMSC
	 */
	public RespostaSMSC getRespostaSMSC()
	{
		return respostaSMSC;
	}
	/**
	 * Objeto responsavel por executar requisicoes nesta conexao.
	 *
	 * @return objeto RequisicaoSMSC
	 */
	public RequisicaoSMSC getRequisicaoSMSC()
	{
		return requisicaoSMSC;
	}
	/**
	 * Objeto responsavel por limitar o numero maximo de requisicoes
	 * a SMSC por segundo.
	 *
	 * @return objeto Timer
	 */
	public Timer getTimer()
	{
		return this.timer;
	}
	/**
	 * Retorna as configuracoes desta Conexao mapeada na tabela TBL_GER_CONEXAO_SMPP.
	 *
	 * @return Objeto <code>ConexaoSMPPConf</code> contendo as configuracoes da conexao com a plataforma.
	 */
	public ConexaoSMPPConf getConexaoConfig()
	{
		return conexaoConfig;
	}
	/**
	 * Retorna uma String com o Endereco formatado. Utilizado somente para DEBUG
	 *
	 * @param addr	Endereco a ser formatado
	 * @return	Endereco formatado
	 */
	String formatAddress(Address addr)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(" (");
		sb.append(addr.getTon());
		sb.append(" ");
		sb.append(addr.getNpi());
		sb.append(") ");
		sb.append(addr.getAddress());
		return sb.toString();
	}
}
