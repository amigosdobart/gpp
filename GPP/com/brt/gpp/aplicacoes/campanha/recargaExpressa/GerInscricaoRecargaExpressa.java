package com.brt.gpp.aplicacoes.campanha.recargaExpressa;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Esta classe implementa a solucao de pesquisar assinantes que são
 * elegíveis para a campanha promocional de recarga expressa.
 * 
 * Os assinantes elegíveis para a campanha devem ativar uma recarga
 * em até 48 horas após o recebimento da mensagem (que o informa que
 * ele está participando da promoção) ou efetuar uma recarga no ato
 * da compra do terminal.
 * Esta classe realiza esta implementacao sendo uma classe produtor
 * que simplesmente fornece as informacoes para o produtor de inscricao
 * de assinantes em campanhas promocionais.
 * 
 * @author Bernardo Pina
 *
 */
public class GerInscricaoRecargaExpressa extends Aplicacoes implements ProdutorCampanha
{
	private PREPConexao	conexaoPrep;
	private ResultSet   listaAssinantes;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public GerInscricaoRecargaExpressa(long aLogId)
	{
		super(aLogId, GerInscricaoRecargaExpressa.class.getName());
	}

	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio Gerenciamento de Inscricao de Assinantes para a Campanha de Primeira Recarga");
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		// Realiza a pesquisa de quais assinantes realizaram a ativacao
		// sendo que eh considerado como ativacao o registro de credito
		// inicial de ativacao na tabela de recargas
		String sql =	"SELECT ah.sub_id, ah.account_status, ah.dat_importacao " +
						"  FROM tbl_apr_assinante_tecnomen ah, tbl_apr_assinante_tecnomen ao " +
						" WHERE ah.dat_importacao = TRUNC (SYSDATE) " +
						"   AND ao.dat_importacao = TRUNC (SYSDATE - 1) " +
						"   AND ao.account_status = ? " +
						"   AND ah.account_status = ? " +
						"   AND ao.sub_id = ah.sub_id ";

		Object param[] = { Integer.toString(Definicoes.STATUS_FIRST_TIME_USER)
				          ,Integer.toString(Definicoes.STATUS_NORMAL_USER)
				         };
		
		listaAssinantes = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
	}
	
	public Object next() throws Exception
	{
		Assinante assinante = null;
		if (listaAssinantes.next())
		{
			assinante = new Assinante();
			assinante.setMSISDN(listaAssinantes.getString("SUB_ID"));
			assinante.setStatusAssinante(listaAssinantes.getShort("ACCOUNT_STATUS"));
			// É necessário passar a data de importação do assinante para que
			// sejam verificadas todas as recargas efetuadas nas últimas 24 horas.
			assinante.setDataAtivacao(listaAssinantes.getDate("DAT_IMPORTACAO"));
		}
		return assinante;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha#retornarAssinante()
	 */
	public Assinante retornarAssinante() throws Exception
	{
		return (Assinante)next();
	}
	
	public int getIdProcessoBatch()
	{
		return 0;
	}
	
	public String getDescricaoProcesso()
	{
		return null;
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
}
