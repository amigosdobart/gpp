package br.com.brasiltelecom.ppp.portal.entity;


/**
 * Esta classe realiza a implementacao da interface de ParametroInscricao para a 
 * campanha de incentivo a recargas bonficando no saldo de sms. Esta classe 
 * identifica se o assinante possui as caracteristicas necessarias para esta 
 * campanha analisando as informacoes de status, numero de dias para a expiracao do 
 * 
 * 
 * 
 * status e indicativo de que o assinante fez o roll-out de status 3 ou nao. 
 * Dependendo desses valores o assinante entao eh inserido na campanha.
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class ParamIncentivoRecargas implements org.exolab.castor.jdo.TimeStampable
{
	private int statusAssinante;
	private int diasExpiracaoIni;
	private int diasExpiracaoFim;
	private int fezRollOut;
	private int id;
	private Campanha campanha;
	private long jdoTimestamp;
	private boolean deveRemover=false;
	
	/**
	 * Access method for the statusAssinante property.
	 * 
	 * @return   the current value of the statusAssinante property
	 */
	public int getStatusAssinante() 
	{
		return statusAssinante;
	}
	
	/**
	 * Sets the value of the statusAssinante property.
	 * 
	 * @param aStatusAssinante the new value of the statusAssinante property
	 */
	public void setStatusAssinante(int aStatusAssinante) 
	{
		statusAssinante = aStatusAssinante;
	}
	
	/**
	 * Access method for the diasExpiracao property.
	 * 
	 * @return   the current value of the diasExpiracao property
	 */
	public int getDiasExpiracaoIni() 
	{
		return diasExpiracaoIni;
	}
	
	/**
	 * Sets the value of the diasExpiracao property.
	 * 
	 * @param aDiasExpiracao the new value of the diasExpiracao property
	 */
	public void setDiasExpiracaoIni(int aDiasExpiracao) 
	{
		diasExpiracaoIni = aDiasExpiracao;
	}

	/**
	 * Access method for the diasExpiracao property.
	 * 
	 * @return   the current value of the diasExpiracao property
	 */
	public int getDiasExpiracaoFim() 
	{
		return diasExpiracaoFim;
	}
	
	/**
	 * Sets the value of the diasExpiracao property.
	 * 
	 * @param aDiasExpiracao the new value of the diasExpiracao property
	 */
	public void setDiasExpiracaoFim(int aDiasExpiracao) 
	{
		diasExpiracaoFim = aDiasExpiracao;
	}
	
	/**
	 * Determines if the fezRollOut property is true.
	 * 
	 * @return   <code>true<code> if the fezRollOut property is true
	 */
	public int getFezRollOut() 
	{
		return fezRollOut;
	}
	
	public boolean isRollOut()
	{
		if (getFezRollOut()==1)
			return true;
		
		return false;
	}
	
	/**
	 * Sets the value of the fezRollOut property.
	 * 
	 * @param aFezRollOut the new value of the fezRollOut property
	 */
	public void setFezRollOut(int aFezRollOut) 
	{
		fezRollOut = aFezRollOut;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Campanha getCampanha() {
		return campanha;
	}

	public void setCampanha(Campanha campanha) {
		this.campanha = campanha;
	}
	
	public boolean deveRemover()
	{
		return deveRemover;
	}

	public void setDeveRemover(boolean deveRemover)
	{
		this.deveRemover = deveRemover;
	}
	
	// Metodos necessarios para implementacao 
	public long jdoGetTimeStamp()
	{
		return jdoTimestamp;
	}
	
	public void jdoSetTimeStamp(long jdoTimestamp)
	{
		this.jdoTimestamp = jdoTimestamp;
	}
}
