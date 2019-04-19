package br.com.brasiltelecom.sasc.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.sasc.dao.SascDAO;
import br.com.brasiltelecom.sasc.entity.SascAtualizacaoSim;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.smarttrust.dp.wsm.api.WsmApiJni;
import com.smarttrust.dp.wsm.api.WsmError;

public class AtualizadorSimcardProdutor implements Produtor
{
	private DataSource dataSourceWIG;
	private ResultSet  resultSet;
	private String     servidorWSM;
	private String     portaServidorWSM;
	private String	   usuarioWSM;
	private String	   senhaWSM;
	private int		   qtdeMaxTentativas;
	private Connection con;
	private WsmApiJni  gerenciadorWsm;
	private Collection servicosBlackList;
	private Logger     logger;
	
	public AtualizadorSimcardProdutor(DataSource dataSourceWIG, String servidor, String porta, String usuario, String psw, int qtdeMaxTentativas)
	{
		this.qtdeMaxTentativas = qtdeMaxTentativas;
		this.servicosBlackList = new ArrayList();
		this.portaServidorWSM  = porta;
		this.dataSourceWIG	   = dataSourceWIG; 
		this.servidorWSM       = servidor;
		this.usuarioWSM		   = usuario;
		this.senhaWSM		   = psw;
		this.logger			   = Logger.getLogger(this.getClass());
	}
	
	public Connection getConnection()
	{
		return this.con;
	}
	
	public void initApi()
	{
		WsmError error;
		
		if (gerenciadorWsm.init(servidorWSM, portaServidorWSM, usuarioWSM, senhaWSM))
			logger.debug("API do WSM inicializada com sucesso.");
		else
		{
			error = gerenciadorWsm.getError();
			logger.error("Erro na inicializacao da API do WSM. Cod: " + error.getErrorCode() + 
						 " Descricao: " + error.getErrorDescription());
		}
	}
	
	public void startup(String[] arg0) throws Exception
	{
		// Cria uma nova instancia do WsmApiJni, a qual ira
		// gerenciar a inicializacao do WSM e conexao com a plataforma OTA 
		System.load("C:/GALVAGNI/BrT/Sistemas/WIG/WEB-INF/lib/AsapCore.dll");
		System.load("C:/GALVAGNI/BrT/Sistemas/WIG/WEB-INF/lib/wsmapi.dll");
		System.load("C:/GALVAGNI/BrT/Sistemas/WIG/WEB-INF/lib/WsmApiJni.dll");
		
		// XXX: Gera um erro aqui!!!
		this.gerenciadorWsm = new WsmApiJni();
		
		this.initApi();
		
		/*if (!WsmApiJni.isInitialised())
			gerenciadorWsm.init(servidorWSM, portaServidorWSM, usuarioWSM, senhaWSM);*/
		
		this.con = dataSourceWIG.getConnection();
		SascDAO sascDAO = new SascDAO();
		
		// Seta a lista dos servicos contidos na Black List
		this.servicosBlackList = sascDAO.getServicosBlackList(con);
		
		// Realiza a consulta dos simcards para atualizacao e seta o 
		// resultSet da classe com o resultado
		setResultSet(sascDAO.getSimcardsParaAtualizacao(con, qtdeMaxTentativas));
	}
	
	public void handleException()
	{
	}
	
	public Object next() throws Exception
	{
		// Cria uma nova instancia do SascAtualizacaoSim
		SascAtualizacaoSim sascAtualizacaoSim = null;
		
		// Caso o ResultSet possua algum dado, o mesmo sera
		// utilizado para popular um objeto SascAtualizacaoSim
		if (this.resultSet.next())
		{
			// Cria uma nova instancia do objeto SascAtualizacaoSim
			// e popula seus atributos com os valores do Banco de Dados
			sascAtualizacaoSim = new SascAtualizacaoSim();
			sascAtualizacaoSim.setIccid(this.resultSet.getString("nu_iccid"));
			sascAtualizacaoSim.setMsisdn(this.resultSet.getString("nu_msisdn"));
			sascAtualizacaoSim.setQtdeTentativas(this.resultSet.getInt("qt_tentativas"));
			sascAtualizacaoSim.setStatus(this.resultSet.getInt("co_atualizacao_sim"));
			// Seta o atributo do objeto sascAtualizacaoSim com a lista dos 
			// servicos contidos na Black List
			sascAtualizacaoSim.setServicosBlackList(this.servicosBlackList);
		}
		
		// Retorna um objeto SascAtualizacaoSim ja populado
		return sascAtualizacaoSim;
	}

	public void finish() throws Exception
	{
		// Fecha a conexao com o Banco de Dados, caso
		// essa nao seja nula e aidna nao esteja fechada
		if (con != null && !con.isClosed())
			con.close();
		
		// Caso a api do WSM ainda esteja inicializada,
		// a mesma eh finalizada
		if (WsmApiJni.isInitialised())
			gerenciadorWsm.close();
	}
	
	public void setResultSet(ResultSet resultSet)
	{
		this.resultSet = resultSet;
	}
	
	public String getPortaServidorWSM()
	{
		return portaServidorWSM;
	}
	
	public String getSenhaWSM()
	{
		return senhaWSM;
	}
	
	public String getServidorWSM()
	{
		return servidorWSM;
	}
	
	public String getUsuarioWSM()
	{
		return usuarioWSM;
	}
}
