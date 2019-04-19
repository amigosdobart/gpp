package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author 	Marcelo Alves Araujo
 * @since 	14/09/2005
 */
public class SolicitacoesRelatorio
{
	private int		solicitacao;
	private String	operador;
	private Date	dataSolicitacao;
	private String	relatorio;
	private Date	dataExecucao;
	private Date	dataAgendamento;
	private String	status;
	private String	nomeArquivo;
	private String	tipoSolicitacao;
	private String	email;
	
	private SimpleDateFormat sdF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	/**
	 * Construtor
	 */
	public SolicitacoesRelatorio()
	{
	}
	
	/**
	 * Construtor
	 */
	public SolicitacoesRelatorio(int solicitacao, String operador, Date dataAgendamento, String relatorio, String arquivo, String email)
	{
		this.solicitacao = solicitacao;
		this.operador = operador;
		this.dataAgendamento = dataAgendamento;
		this.dataSolicitacao = new Date();
		this.email = email;
		this.nomeArquivo = arquivo;
		this.relatorio = relatorio;
		this.status = Constantes.STATUS_NAO_PROCESSADO;
		this.tipoSolicitacao = Constantes.CONSULTA_TEMPORARIA;
		this.dataExecucao = null;
		this.sdF = null;
	}
	
	// Métodos get
	
	/**
	 * @return String
	 */
	public String getFDataAgendamento() 
	{	
		if(dataAgendamento != null)
			return sdF.format(dataAgendamento);
		else
			return null;
	}
	
	/**
	 * @return String
	 */
	public String getFDataExecucao() 
	{	
		if(dataExecucao != null)
			return sdF.format(dataExecucao);
		else
			return null;		
	}
	
	/**
	 * @return String
	 */
	public String getFDataSolicitacao() 
	{	
		if(dataSolicitacao != null)
			return sdF.format(dataSolicitacao);
		else
			return null;		
	}
	
	/**
	 * @return Date
	 */
	public Date getDataAgendamento()
	{
		return dataAgendamento;
	}
	
	/**
	 * @return Date
	 */
	public Date getDataExecucao()
	{
		return dataExecucao;
	}
	
	/**
	 * @return Date
	 */
	public Date getDataSolicitacao()
	{
		return dataSolicitacao;
	}
	
	/**
	 * @return String
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * @return String
	 */
	public String getNomeArquivo()
	{
		return nomeArquivo;
	}
	
	/**
	 * @return String
	 */
	public String getOperador()
	{
		return operador;
	}
	
	/**
	 * @return String
	 */
	public String getRelatorio()
	{
		return relatorio;
	}
	/**
	 * @return int
	 */
	public int getSolicitacao()
	{
		return solicitacao;
	}
	
	/**
	 * @return String
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @return String
	 */
	public String getTipoSolicitacao()
	{
		return tipoSolicitacao;
	}
	
	
	// Métodos set
	/**
	 * @param dataAgendamento - Data a ser executada a consulta
	 */
	public void setDataAgendamento(Date dataAgendamento)
	{
		this.dataAgendamento = dataAgendamento;
	}
	
	/**
	 * @param dataExecucao - Data em que foi concluída a execução
	 */
	public void setDataExecucao(Date dataExecucao)
	{
		this.dataExecucao = dataExecucao;
	}
	
	/**
	 * @param dataSolicitacao - Data em que foi solicitada a consulta
	 */
	public void setDataSolicitacao(Date dataSolicitacao)
	{
		this.dataSolicitacao = dataSolicitacao;
	}
	
	/**
	 * @param email - E-mail para onde deve ser enviada mensagem para avisar da conclusão do relatório
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * @param nomeArquivo - Nome do arquivo
	 */
	public void setNomeArquivo(String nomeArquivo)
	{
		this.nomeArquivo = nomeArquivo;
	}
	
	/**
	 * @param operador - Nome do operador que solicitou o relatório
	 */
	public void setOperador(String operador)
	{
		this.operador = operador;
	}
	
	/**
	 * @param relatorio - Consulta a ser realizada
	 */
	public void setRelatorio(String relatorio)
	{
		this.relatorio = relatorio;
	}
	
	/**
	 * @param solicitacao - Id da solicitação
	 */
	public void setSolicitacao(int solicitacao)
	{
		this.solicitacao = solicitacao;
	}
	
	/**
	 * @param status - Status do relatório
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @param tipoSolicitacao - Tipo de solicitação temporária (T) ou permanente (P)
	 */
	public void setTipoSolicitacao(String tipoSolicitacao)
	{
		this.tipoSolicitacao = tipoSolicitacao;
	}	
}