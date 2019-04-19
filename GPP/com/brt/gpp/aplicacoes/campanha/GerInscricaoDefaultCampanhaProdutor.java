package com.brt.gpp.aplicacoes.campanha;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
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
public class GerInscricaoDefaultCampanhaProdutor extends Aplicacoes implements ProdutorCampanha 
{
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private ResultSet 	listaUsuarios;
	private PREPConexao	conexaoPrep;
	private long		numAssinantes;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	public GerInscricaoDefaultCampanhaProdutor(long idProcesso) 
	{
		super(idProcesso, Definicoes.CL_GER_INSCRICAO_ASSIN_CAMP_PROD);
	}

	/**
	 * @param params[]
	 * @throws java.lang.Exception
	 */
	public void startup(String params[]) throws Exception 
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio Gerenciamento de Inscricao de Assinantes para Campanhas");
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		// Define o comando para busca dos assinantes disponiveis na base de dados
		// afim de buscar entre eles quais estao aptos a serem inscritos nas campanhas
		String sql = "SELECT IDT_MSISDN," +
		                    "DAT_ATIVACAO," +
		                    "DAT_EXPIRACAO_PRINCIPAL," +
		                    "IDT_STATUS," +
		                    "IDT_PLANO_PRECO," +
		                    "IDT_STATUS_SERVICO," +
		                    "IND_RETORNO_CICLO3 " +
				       "FROM TBL_APR_ASSINANTE " +
				      "WHERE IDT_STATUS IN (?,?,?) ";
		Object param[] = {new Integer(Definicoes.STATUS_NORMAL_USER)
				         ,new Integer(Definicoes.STATUS_RECHARGE_EXPIRED)
				         ,new Integer(Definicoes.STATUS_DISCONNECTED)};
		listaUsuarios = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
	}
	
	public Assinante retornarAssinante() throws Exception
	{
		return (Assinante)next();
	}
	
	/**
	 * Este metodo disponibiliza os assinantes da base de dados a serem verificados 
	 * para a inscricao em campanhas promocionais
	 * 
	 * @return obj - Assinante a ser possivelmente inscrito em campanhas
	 */
	public Object next() throws Exception
	{
		Assinante assinante = null;
		if (listaUsuarios.next())
		{
			assinante = new Assinante();
			assinante.setMSISDN					(listaUsuarios.getString("IDT_MSISDN")							);
			assinante.setDataAtivacao			(listaUsuarios.getDate("DAT_ATIVACAO")							);
			assinante.setDataExpiracaoPrincipal	(sdf.format(listaUsuarios.getDate("DAT_EXPIRACAO_PRINCIPAL"))	);
			assinante.setStatusAssinante		(listaUsuarios.getShort("IDT_STATUS")							);
			assinante.setStatusServico			(listaUsuarios.getShort("IDT_STATUS_SERVICO")					);
			assinante.setPlanoPreco				(listaUsuarios.getShort("IDT_PLANO_PRECO")						);
			assinante.setFezRetornoCiclo3		((listaUsuarios.getInt("IND_RETORNO_CICLO3") == 1) ? true:false	);
			
			numAssinantes++;
		}
		return assinante;
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
		return "Gerenciador de inscricao default de assinantes analisou "+numAssinantes+" assinantes.";
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
		if (listaUsuarios != null)
			listaUsuarios.close();
		
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}
	
	public void handleException() 
	{
	}
}
