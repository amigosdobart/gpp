package com.brt.gpp.aplicacoes.promocao.persistencia;

//Imports Java.
import java.sql.ResultSet;

//Imports GPP.
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
//import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela consulta e obtencao de informacoes sumarizadas do Pula Pula.
 *  Esta classe tem como única finalidade a obtenção dos MSISDNs dos assinantes de promoção
 *  ordenados por CN, para se processar corretamente a sumarização do Pula Pula 
 *
 *	@author	Magno Batista Corrêa
 *	@since	18/04/2005 (dd/mm/yyyy)
 *	@deprecated
 */
public class SelecaoSumarizacaoPulaPula {

	private String nomeClasse;
//	private GerentePoolBancoDados gerenteBancoDados;
	private GerentePoolLog logger;
	private PREPConexao conexaoPrep;
	private long idLog;
	private ResultSet result;
	private boolean isOpen;
	private Object mutex;

	//Statements SQL.
	private static final String SQL_SELECAO = "    SELECT DISTINCT tbl.idt_msisdn as MSISDN	"
			+ "    FROM tbl_pro_assinante tbl					"
			+ "    WHERE tbl.idt_promocao = ?					"
			+ "    ORDER BY SUBSTR (tbl.idt_msisdn, 3, 2)		";

	//Construtores.
	/**
	 *	Construtor da classe.
	 */
	public SelecaoSumarizacaoPulaPula() {
		this.nomeClasse = Definicoes.CL_SELECAO_SUMARIZACAO_PULA_PULA;
		this.logger = GerentePoolLog.getInstancia(SelecaoPulaPula.class);
//		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(0);
		this.conexaoPrep = null;
		this.idLog = 0;
		this.result = null;
		this.isOpen = false;
		this.mutex = new Object();
	}

	//Metodos.
	/**
	 *	Executa a consulta no banco de dados definidos pelo SQL_SELECAO.
	 *  Extrai do Banco todos os MSISDN de uma dada promocao
	 *
	 *	@param		long					idLog						Identificador de LOG.
	 *	@throws		Exception
	 */
	public synchronized void execute(long idLog,PREPConexao conexaoPrep, int idtPromocao)
			throws Exception {
		synchronized (this.mutex) {
			if (!this.isOpen) {
				this.idLog = idLog;
				String sqlQuery = null;
				Object[] parametros = null;

				this.log(Definicoes.DEBUG, "execute", "Inicio");

				try {
					sqlQuery = SelecaoSumarizacaoPulaPula.SQL_SELECAO;

					//Obtendo os parametros.
					parametros = new Object[1];
					parametros[0] = new Integer(idtPromocao);

					//Executando a consulta.
					this.conexaoPrep = conexaoPrep;
					this.result = this.conexaoPrep.executaPreparedQuery(sqlQuery, parametros, this.idLog);
					this.isOpen = true;
				} catch (Exception e) {
//					this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,this.idLog);
					throw e;
				} finally {
					this.log(Definicoes.DEBUG, "execute", "Fim");
				}
			} else {
				throw new GPPInternalErrorException("Consulta ja esta aberta.");
			}
		}
	}

	/**
	 *	Busca o proximo MSISDN a ser processado.
	 *
	 *	@return		String					result						MSISDN a ser processado.
	 *	@throws		Exception
	 */
	public synchronized String next() throws Exception {
		synchronized (this.mutex) {
			this.log(Definicoes.DEBUG, "next", "Inicio");

			try {
				if ((this.isOpen) && (this.result.next())) {
					return this.result.getString("MSISDN");
				}
			} finally {
				this.log(Definicoes.DEBUG, "next", "Fim");
			}
			return null;
		}
	}

	/**
	 *	Fecha a consulta e a conexao com o banco de dados.
	 *
	 *	@throws		Exception
	 */
	public synchronized void close() throws Exception {
		synchronized (this.mutex) {
			this.log(Definicoes.DEBUG, "close", "Inicio");

			try {
				if (this.result != null)
					this.result.close();
			} finally {
//				this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep,this.idLog);
				this.log(Definicoes.DEBUG, "close", "Fim");
				this.idLog = 0;
				this.isOpen = false;
			}
		}
	}

	/**
	 *	Indica se a consulta esta aberta.
	 *
	 *	@return		boolean					isOpen						True se a consulta esta aberta e 
	 *																	false caso contrario.
	 */
	public boolean isOpen() {
		return this.isOpen;
	}

	//Metodo de LOG.
	/**
	 *	Registra mensagem de LOG.
	 *
	 *	@param		int						tipo						Tipo de mensagem.
	 *	@param		String					metodo						Nome do metodo.
	 *	@param		String					mensagem					Mensagem a ser registrada.
	 */
	private void log(int tipo, String metodo, String mensagem) {
		logger.log(this.idLog, tipo, this.nomeClasse, metodo, mensagem);
	}
}
