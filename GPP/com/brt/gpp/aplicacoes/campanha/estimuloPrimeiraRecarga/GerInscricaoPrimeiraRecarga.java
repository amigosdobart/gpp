package com.brt.gpp.aplicacoes.campanha.estimuloPrimeiraRecarga;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.campanha.entidade.ProdutorCampanha;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 * Esta classe implementa a solucao de pesquisar assinantes
 * que sao elegiveis para a campanha promocional de primeira recarga.
 * Os assinantes elegiveis para a campanha devem ativar uma recarga
 * em ateh 48 horas apos a ativacao do acesso (troca de status FirstTime - Normal).
 * portanto esta classe realiza esta implementacao sendo uma classe produtor
 * que simplesmente fornece as informacoes para o produtor de inscricao de assinantes
 * em campanhas promocionais.
 * 
 * Observe que esta classe nao possui consumidor, nem foi registrada como um processo
 * batch, pois o processo de inscricao de assinantes realiza o trabalho, este simplesmente
 * fornece as informacoes de assinantes a serem processados pelo Consumidor do gerenciador
 * de inscricao de assinantes (GerInscricaoAssinantesConsumidor).
 * 
 * @author joao.lemgruber
 *
 */
public class GerInscricaoPrimeiraRecarga extends Aplicacoes implements ProdutorCampanha
{
	private PREPConexao	conexaoPrep;
	private ResultSet   listaAssinantes;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public GerInscricaoPrimeiraRecarga(long aLogId)
	{
		super(aLogId, GerInscricaoPrimeiraRecarga.class.getName());
	}

	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio Gerenciamento de Inscricao de Assinantes para a Campanha de Primeira Recarga");
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		// Realiza a pesquisa de quais assinantes realizaram a ativacao
		// sendo que eh considerado como ativacao o registro de credito
		// inicial de ativacao na tabela de recargas
		String sql ="select r.idt_msisdn,a.idt_plano_preco,r.dat_origem " +
					  "from tbl_rec_recargas r " +
					      ",tbl_apr_assinante a " +
					      ",tbl_ger_plano_preco p " +
					 "where r.dat_origem     >= ? " +
					   "and r.dat_origem     <  ? " +
					   "and r.tip_transacao   = ? " +
					   "and a.idt_msisdn      = r.idt_msisdn " +
					   "and p.idt_plano_preco = a.idt_plano_preco " +
					   "and p.idt_categoria   = ? ";
		
		// OBS: O objeto java.sql.Date eh utilizado, pois desta forma a parte
		// horaria da data nao eh utilizado na pesquisa.
		Object param[] = { new java.sql.Date(getDataInicialProcessamento().getTime())
				          ,new java.sql.Date(Calendar.getInstance().getTimeInMillis())
				          ,Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL
				          ,new Integer(Definicoes.CATEGORIA_PREPAGO)
				         };
		
		listaAssinantes = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
	}
	
	public Object next() throws Exception
	{
		Assinante assinante = null;
		if (listaAssinantes.next())
		{
			assinante = new Assinante();
			assinante.setMSISDN			(listaAssinantes.getString("IDT_MSISDN"));
			assinante.setPlanoPreco		(listaAssinantes.getShort("IDT_PLANO_PRECO"));
			// OBS: Serah utilizado como data de ativacao a data 
			// de ativacao do acesso e nao da ativacao (criacao)
			// do assinante na plataforma. Isso porque a pesquisa
			// deve ser feita a partir da troca de status FirstTime-Normal
			assinante.setDataAtivacao	(listaAssinantes.getTimestamp("DAT_ORIGEM"));
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
	
	/**
	 * Metodo....:getDataInicialProcessamento
	 * Descricao.:Retorna a data inicial para processamento das ativacoes
	 * @return - Data Inicial para considerar as ativacoes
	 */
	private Date getDataInicialProcessamento()
	{
		// A data inicial de processamento eh calculada para tres dias anteriores
		// a data atual para que o processamento possa identificar sempre todos
		// os assinantes que em ateh 48hs desta data de ativacao fizeram recarga
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,-3);
		
		return cal.getTime();
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
