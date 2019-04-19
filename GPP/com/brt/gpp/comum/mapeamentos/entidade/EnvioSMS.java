package com.brt.gpp.comum.mapeamentos.entidade;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entidade <code>EnvioSMS</code>. Referência: TBL_GER_ENVIO_SMS
 * 
 * @author Bernardo Vergne Dias
 * Criado em: 10/12/2007 
 */
public class EnvioSMS 
{
 
    private int idRegistro;
    private String idtMsisdn;
    private String idtMsisdnOrigem;
    private String desMensagem;
    private int numPrioridade;
    private Date dataEnvioSMS;
    private Date dataProcessamento;
    private int statusProcessamento;
    private String tipoSMS;
    
    /**
     * @return Data agendada para envio
     */
	public Date getDataEnvioSMS()
    {
        return dataEnvioSMS;
    }

    /**
     * @param dataEnvioSMS Data agendada para envio
     */
    public void setDataEnvioSMS(Date dataEnvioSMS)
    {
        this.dataEnvioSMS = dataEnvioSMS;
    }

    public Date getDataProcessamento()
    {
        return dataProcessamento;
    }

    public void setDataProcessamento(Date dataProcessamento)
    {
        this.dataProcessamento = dataProcessamento;
    }

    /**
     * @return Conteudo texto do SMS
     */
    public String getDesMensagem()
    {
        return desMensagem;
    }

    /**
     * @param desMensagem Conteudo texto do SMS
     */
    public void setDesMensagem(String desMensagem)
    {
        this.desMensagem = desMensagem;
    }

    public int getIdRegistro()
    {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro)
    {
        this.idRegistro = idRegistro;
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

    public int getNumPrioridade()
    {
        return numPrioridade;
    }

    public void setNumPrioridade(int numPrioridade)
    {
        this.numPrioridade = numPrioridade;
    }

    public int getStatusProcessamento()
    {
        return statusProcessamento;
    }

    public void setStatusProcessamento(int statusProcessamento)
    {
        this.statusProcessamento = statusProcessamento;
    }

    public String getTipoSMS()
    {
        return tipoSMS;
    }

    public void setTipoSMS(String tipoSMS)
    {
        this.tipoSMS = tipoSMS;
    }

    public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof EnvioSMS))
			return false;
		
		if (obj == this || obj.hashCode() == this.hashCode())
			return true;
		
		return false;
	}
	
	public int hashCode() 
	{
		return this.idRegistro;
	}
	
	public String toString() 
	{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuffer result = new StringBuffer();
		
		result.append("[EnvioSMS]");
		result.append("ID=" + this.idRegistro);
		if (idtMsisdn != null) 	      result.append(";MSISDN_DESTINO=" + this.idtMsisdn);
		if (idtMsisdnOrigem != null)  result.append(";MSISDN_ORIGEM="  + this.idtMsisdnOrigem);
        if (desMensagem != null)      result.append(";MENSAGEM="       + this.desMensagem);
        
        result.append(";STATUS_PROCESSAMENTO=" + this.statusProcessamento);
        
        if (dataProcessamento != null)    result.append(";DATA_PROCESSAMENTO=" + sdf.format(this.dataProcessamento));
        if (dataEnvioSMS != null)         result.append(";DATA_ENVIO=" + sdf.format(this.dataEnvioSMS));
        
		return result.toString();
	}

}
 
