package com.brt.gpp.aplicacoes.enviarSMS.entidade;

/**
 * A classe <code>TipoSMS</code> representa os dados da tabela <code>TBL_GER_TIPO_SMS</code>.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 02/01/2008
 */
public class TipoSMS
{
	private String	idtTipoSMS;
	private int		indNotificarEntrega;
	private int		numIniPeriodoEnvioSMS;
	private int		numFimPeriodoEnvioSMS;
	private int		idConexao;

	/**
	 * Define os valores padroes dos atributos :<br>
	 * <pre>
	 * <b>idtTipoSMS</b>              "PADRAO"
	 * <b>indNotificarEntrega</b>     0
	 * <b>numIniPeriodoEnvioSMS</b>   0
	 * <b>numFimPeriodoEnvioSMS</b>   86399
	 * </pre>
	 * O valor padrao <code>86399</code> do atributo <code>numFimPeriodoEnvioSMS</code>
	 * equivale ao segundo 23:59:59.
	 */
	public TipoSMS()
	{
		idtTipoSMS				= "PADRAO";
		indNotificarEntrega 	= 0;
		// Hora correspondente a 00:00:00
		numIniPeriodoEnvioSMS	= 0;
		// Hora correspondente a 23:59:59
		numFimPeriodoEnvioSMS	= 86399;
	}

	public String getIdtTipoSMS()
	{
		return idtTipoSMS;
	}

	public void setIdtTipoSMS(String idtTipoSMS)
	{
		this.idtTipoSMS = idtTipoSMS;
	}

	public int getIndNotificarEntrega()
	{
		return indNotificarEntrega;
	}

	public void setIndNotificarEntrega(int indNotificarEntrega)
	{
		this.indNotificarEntrega = indNotificarEntrega;
	}

	public int getNumFimPeriodoEnvioSMS()
	{
		return numFimPeriodoEnvioSMS;
	}

	public void setNumFimPeriodoEnvioSMS(int numFimPeriodoEnvioSMS)
	{
		this.numFimPeriodoEnvioSMS = numFimPeriodoEnvioSMS;
	}

	public int getNumIniPeriodoEnvioSMS()
	{
		return numIniPeriodoEnvioSMS;
	}

	public void setNumIniPeriodoEnvioSMS(int numIniPeriodoEnvioSMS)
	{
		this.numIniPeriodoEnvioSMS = numIniPeriodoEnvioSMS;
	}

	public int getIdConexao()
	{
		return idConexao;
	}

	public void setIdConexao(int idConexao)
	{
		this.idConexao = idConexao;
	}

	public boolean deveNotificarEntrega()
	{
		if(indNotificarEntrega == 0)
			return false;

		return true;
	}

	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof TipoSMS)
			return ((TipoSMS)obj).getIdtTipoSMS() == this.idtTipoSMS;

		return false;
	}

	public int hashCode()
	{
		return idtTipoSMS.hashCode();
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ TipoSMS ]");

		sb.append(" -idtTipoSMS: ").append(idtTipoSMS);
		sb.append(" -indNotificarEntrega: ").append(indNotificarEntrega);
		sb.append(" -numIniPeriodoEnvioSMS: ").append(numIniPeriodoEnvioSMS);
		sb.append(" -numFimPeriodoEnvioSMS: ").append(numFimPeriodoEnvioSMS);
		sb.append(" -idConexao: ").append(idConexao);

		return sb.toString();
	}
}
