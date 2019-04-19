package com.brt.gppAlarmes.aplicacao;

import com.brt.gppAlarmes.entity.Alarme;
import com.brt.gppAlarmes.entity.Evento;
import com.brt.gppAlarmes.dao.EventoDAO;
import com.brt.gppAlarmes.dao.AlarmeDAO;
import com.brt.gppAlarmes.conexoes.Configuracao;
import com.brt.gppAlarmes.conexoes.GerentePoolConexoes;

import java.util.TimerTask;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 
 * @author Joao Carlos
 * Data..: 07-Abr-2005
 *
 */
public class ProcessadorSQLContador extends TimerTask
{
	private Alarme alarme;
	private Logger logger;
	
	/**
	 * Metodo....:ProcessadorSQLContador
	 * Descricao.:Constroi a thread que sera a tarefa a ser executada
	 *            utilizando o SQL definido no alarme passado como parametro
	 * @param Alarme - Alarme a ser processado na tarefa 
	 */
	public ProcessadorSQLContador(Alarme alarme)
	{
		this.alarme = alarme;
		logger = Logger.getLogger("Alarmes");
	}

	/**
	 * Metodo....:getResultadoComando
	 * Descricao.:Retorna os valores como Evento executados a partir do sql cadastrado
	 * @return Evento - Evento criado a partir da execucao do comando
	 * @throws Exception
	 */
	private Evento getResultadoComando() throws Exception
	{
		// Pega uma conexao com o pool de conexoes de BD. 
		Connection conexao = GerentePoolConexoes.getInstance().getConexao();
		logger.debug("SQL a ser executado:"+alarme.getSQLBuscaContador());
		PreparedStatement pstm = conexao.prepareStatement(alarme.getSQLBuscaContador());
		ResultSet rs = pstm.executeQuery();
		Evento evt = null;
		if (rs.next())
		{
			// Cria a referencia para o evento somente se houver resultado do comando executado
			// O comando deve retornar na primeira coluna o valor do codigo de retorno e na
			// segunda o valor do contador. Caso o comando retorne mais de uma linha, somente a
			// primeira sera considerada
			evt = new Evento();
			evt.setAlarme		(alarme);
			evt.setDataExecucao	(Calendar.getInstance().getTime());
			evt.setCodigoRetorno(rs.getInt(1));
			evt.setValorContador(rs.getDouble(2));
		}
		// Devolve a conexao pro pool
		GerentePoolConexoes.getInstance().releaseConexao(conexao);
		return evt;
	}

	/**
	 * Metodo....:run
	 * Descricao.:Este metodo realiza a execucao da tarefa buscando o valor
	 *            definido para o SQL cadastrado para o alarme
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		try
		{
			// Busca o objeto Evento que sera construido atraves da execucao do comando SQL definido
			// para o alarme e insere este na tabela no banco de dados
			logger.info("Executando SQL e inserindo os valores como Evento para o Alarme "+alarme);
			EventoDAO.getInstance().add(getResultadoComando());
		}
		catch(Exception e)
		{
			logger.error("Erro no processamento da tarefa de busca do contador para o alarme "+alarme+". Erro:"+e);
		}
	}
	
	/**
	 * Metodo....:main
	 * Descricao.:Este metodo existe para permitir que o comando SQL cadastrado para o alarme seja
	 *            executado imediatamente sem a espera do agendamento
	 * @param args - Id do alarme no qual o SQL deve ser executado
	 */
	public static void main(String args[])
	{
		// Inicia LOG4J
		Configuracao conf = Configuracao.getInstance();
		PropertyConfigurator.configure(conf.getPropriedadesLog());
		Logger logger = Logger.getLogger("Alarmes");

		if (args == null || args.length == 0 || (args!=null && args[0] == null))
		{
			System.out.println("Execucao deve ser: java com.brt.gppAlarmes.aplicacao.ProcessadorSQLContador IDAlarme");
			System.exit(1);
		}
		try
		{
			// Realiza a pesquisa do Alarme buscando os valores do registro e tambem o SQL cadastrado
			Alarme alarme = AlarmeDAO.getInstance().findById(args[0]);
			// Executa a classe de execucao do SQL como uma classe comum e nao como uma thread
			ProcessadorSQLContador proc = new ProcessadorSQLContador(alarme);
			proc.run();
			
			// Caso exista a instancia das classes DAO entao a conexao foi criada e
			// agora sera fechada
			AlarmeDAO.getInstance().close();
			EventoDAO.getInstance().close();
		}
		catch(Exception e)
		{
			logger.error("Erro ao executar SQL do alarme "+args[0]);
			System.exit(1);
		}
		System.exit(0);
	}
}
