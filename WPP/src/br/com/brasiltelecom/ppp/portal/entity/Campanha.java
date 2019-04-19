package br.com.brasiltelecom.ppp.portal.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;

/**
 * Esta classe representa as informacoes de uma Campanha promocional.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class Campanha implements org.exolab.castor.jdo.TimeStampable
{
	private long id;
	private String nomeCampanha;
	private Date validadeInicial;
	private Date validadeFinal;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private long jdoTimestamp;
	
	/**
	 * Define uma lista de classes que implementam a interface de ParametroInscricao 
	 * para indicar se um determinado assinante deve ser ou nao registrado na campanha. 
	 * 
	 * 
	 * O registro ainda nao significa bonificacao, para isso deve ser verificado as 
	 * Condicoes de Concessao.
	 */
	private Collection parametrosInscricao;
	
	/**
	 * Define as classes que implementam uma CondicaoConcessao para serem utilizadas 
	 * pela campanha afim de realizar se um assinante registrado em tal campanha deve 
	 * ser ou nao bonificado. Caso verdade indica tambem os valores a serem creditados
	 */
	private Collection condicoesConcessao;
	
	/**
	 * Retorna uma lista de mensagens SMS (Template) deverao ser enviadas ao assinante para que 
	 * o mesmo seja avisado de como este poderah obter o bonus da campanha.
	 */
	private Collection smCampanha;
	
	
	/**
	 * Metodo....:Campanha
	 * Descricao.:Construtor
	 *
	 */
	public Campanha()
	{
		parametrosInscricao = new ArrayList();
		condicoesConcessao  = new ArrayList();
		smCampanha		   = new ArrayList();
	}
	
	/**
	 * Access method for the id property.
	 * 
	 * @return   the current value of the id property
	 */
	public long getId() 
	{
		return id;    
	}
	
	/**
	 * Sets the value of the id property.
	 * 
	 * @param aId the new value of the id property
	 */
	public void setId(long aId) 
	{
		id = aId;    
	}
	
	/**
	 * Access method for the nomeCampanha property.
	 * 
	 * @return   the current value of the nomeCampanha property
	 */
	public String getNomeCampanha() 
	{
		return nomeCampanha;    
	}
	
	/**
	 * Sets the value of the nomeCampanha property.
	 * 
	 * @param aNomeCampanha the new value of the nomeCampanha property
	 */
	public void setNomeCampanha(String aNomeCampanha) 
	{
		nomeCampanha = aNomeCampanha;    
	}
	
	/**
	 * Access method for the validadeInicial property.
	 * 
	 * @return   the current value of the validadeInicial property
	 */
	public Date getValidadeInicial() 
	{
		return validadeInicial;    
	}
	
	public String getValidadeInicialStr()
	{
		return sdf.format(this.getValidadeInicial());
	}
	
	/**
	 * Sets the value of the validadeInicial property.
	 * 
	 * @param aValidadeInicial the new value of the validadeInicial property
	 */
	public void setValidadeInicial(Date aValidadeInicial) 
	{
		validadeInicial = aValidadeInicial;    
	}
	
	/**
	 * Access method for the validadeFinal property.
	 * 
	 * @return   the current value of the validadeFinal property
	 */
	public Date getValidadeFinal() 
	{
		return validadeFinal;    
	}
	
	public String getValidadeFinalStr()
	{
		return sdf.format(this.getValidadeFinal());
	}
	
	/**
	 * Sets the value of the validadeFinal property.
	 * 
	 * @param aValidadeFinal the new value of the validadeFinal property
	 */
	public void setValidadeFinal(Date aValidadeFinal) 
	{
		validadeFinal = aValidadeFinal;    
	}
	
	/**
	 * Access method for the parametrosInscricao property.
	 * 
	 * @return   the current value of the parametrosInscricao property
	 */
	public Collection getParametrosInscricao() 
	{
		return parametrosInscricao;    
	}
	
	/**
	 * Access method for the parametrosInscricao property.
	 * 
	 * @return   the current value of the parametrosInscricao property
	 */
	public void setParametrosInscricao(Collection parametrosInscricao) 
	{
		this.parametrosInscricao = parametrosInscricao;    
	}
	
	/**
	 * Access method for the condicoesConcessao property.
	 * 
	 * @return   the current value of the condicoesConcessao property
	 */
	public Collection getCondicoesConcessao() 
	{
		return condicoesConcessao;    
	}
	
	/**
	 * Access method for the parametrosInscricao property.
	 * 
	 * @return   the current value of the parametrosInscricao property
	 */
	public void setCondicoesConcessao(Collection condicoesConcessao) 
	{
		this.condicoesConcessao = condicoesConcessao;    
	}
	
	/**
	 * Access method for the smCampanha property.
	 * 
	 * @return   the current value of the smCampanha property
	 */
	public Collection getSmCampanha() 
	{
		return smCampanha;    
	}
	
	/**
	 * Access method for the parametrosInscricao property.
	 * 
	 * @return   the current value of the parametrosInscricao property
	 */
	public void setSmCampanha(Collection smCampanha) 
	{
		this.smCampanha = smCampanha;    
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
