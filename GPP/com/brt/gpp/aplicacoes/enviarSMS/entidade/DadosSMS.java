package com.brt.gpp.aplicacoes.enviarSMS.entidade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.comum.conexoes.smpp.RespostaSMSC;

/**
 * A classe <code>DadosSMS</code> que representa a tabela <code>TBL_GER_ENVIO_SMS</code>.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 02/01/2008
 */
public class DadosSMS implements Comparable
{
	private long	idRegistro;
	private String	idtMsisdn;
	private String	idtMsisdnOrigem;
	private String 	desMensagem;
	private int		numPrioridade;
	private Date	datEnvioSMS;
	private Date	datProcessamento;
	private int		idtStatusProcessamento;
	private Long	idRegistroSMSC;
	private Integer idtStatusSMSC;
	private TipoSMS tipSMS;

	/**
	 * Define os valores padroes para os seguintes campos:
	 * <pre>
	 * <b>idtStatusProcessamento</b>     Definicoes.SMS_PENDENTE
	 * <b>datEnvioSMS</b>                Calendar.getInstance().getTime()
	 * <b>tipSMS</b>                     new TipoSMS()
	 * </pre>
	 *
	 * O atributo <code>idtStatusProcessamento</code> eh definido como SMS nao enviado (Status 1),
	 * <code>datEnvioSMS</code> eh definido com a hora atual e tipSMS eh definido
	 * com a uma nova instancia da Classe <code>TipoSMS</code>.
	 *
	 * @see com.brt.gpp.comum.Definicoes#SMS_PENDENTE
	 * @see com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS
	 */
	public DadosSMS ()
	{
		// Define um valor padrao para o idtStatusProcessamento como SMS nao enviado
		idtStatusProcessamento = RespostaSMSC.SMS_STATUS_NAO_ENVIADO;
		// Define o valor padrao para o data de envio como a hora atual
		datEnvioSMS = Calendar.getInstance().getTime();
		// Instancia o TipoSMS padrao.
		tipSMS = new TipoSMS();
	}

	public Date getDatEnvioSMS()
	{
		return datEnvioSMS;
	}

	public void setDatEnvioSMS(Date datEnvioSMS)
	{
		this.datEnvioSMS = datEnvioSMS;
	}

	public Date getDatProcessamento()
	{
		return datProcessamento;
	}

	public void setDatProcessamento(Date datProcessamento)
	{
		this.datProcessamento = datProcessamento;
	}

	public String getDesMensagem()
	{
		return desMensagem;
	}

	public void setDesMensagem(String desMensagem)
	{
		this.desMensagem = desMensagem;
	}

	public long getIdRegistro()
	{
		return idRegistro;
	}

	public void setIdRegistro(long idRegistro)
	{
		this.idRegistro = idRegistro;
	}

	public Long getIdRegistroSMSC()
	{
		return idRegistroSMSC;
	}

	public void setIdRegistroSMSC(Long idRegistroSMSC)
	{
		this.idRegistroSMSC = idRegistroSMSC;
	}

	public String getIdtMsisdn()
	{
		return idtMsisdn;
	}

	public void setIdtMsisdn(String idtMsisdn)
	{
		this.idtMsisdn = idtMsisdn;
	}

	public String getIdtMsisdnOrigem()
	{
		return idtMsisdnOrigem;
	}

	public void setIdtMsisdnOrigem(String idtMsisdnOrigem)
	{
		this.idtMsisdnOrigem = idtMsisdnOrigem;
	}

	public int getIdtStatusProcessamento()
	{
		return idtStatusProcessamento;
	}

	public void setIdtStatusProcessamento(int idtStatusProcessamento)
	{
		this.idtStatusProcessamento = idtStatusProcessamento;
	}

	public Integer getIdtStatusSMSC()
	{
		return idtStatusSMSC;
	}

	public void setIdtStatusSMSC(Integer idtStatusSMSC)
	{
		this.idtStatusSMSC = idtStatusSMSC;
	}

	public int getNumPrioridade()
	{
		return numPrioridade;
	}

	public void setNumPrioridade(int numPrioridade)
	{
		this.numPrioridade = numPrioridade;
	}

	public TipoSMS getTipoSMS()
	{
		return tipSMS;
	}

	public void setTipoSMS(TipoSMS tipSMS)
	{
		this.tipSMS = tipSMS;
	}

	public String toString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuffer sb = new StringBuffer("[ DadosSMS ]");

		sb.append(" -idRegistro: ").append(idRegistro);
		sb.append(" -idtMsisdn: ").append(idtMsisdn);
		sb.append(" -idtMsisdnOrigem: ").append(idtMsisdnOrigem);
		sb.append(" -desMensagem: ").append(desMensagem);
		sb.append(" -numPrioridade: ").append(numPrioridade);
		sb.append(" -datEnvioSMS: ").append(datEnvioSMS != null ? sdf.format(datEnvioSMS) : null);
		sb.append(" -datProcessamento: ").append(datProcessamento != null ? sdf.format(datProcessamento) : null);
		sb.append(" -idtStatusProcessamento: ").append(idtStatusProcessamento);
		sb.append(" -idRegistroSMSC: ").append(idRegistroSMSC);
		sb.append(" -idtStatusSMSC: ").append(idtStatusSMSC);
		sb.append(" -tipSMS: ").append(tipSMS.toString());

		return sb.toString();
	}

	public int hashCode()
	{
		return new Long(getIdRegistro()).hashCode();
	}

	/**
	 * @see java.lang.Object#equals(Object o)
	 */
	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof DadosSMS)
			return ((DadosSMS)obj).getIdRegistro() == this.idRegistro;

		return false;
	}

	public int compareTo(Object obj)
	{
		if (!(obj instanceof DadosSMS))
			throw new IllegalArgumentException("Classe de dados "+ obj.getClass().getName()+ " Nao pode ser comparada.");

		if ( this.idRegistro > ((DadosSMS)obj).getIdRegistro() )
			return 1;
		else
			if ( this.idRegistro < ((DadosSMS)obj).getIdRegistro() )
				return -1;
			else return 0;
	}
}
