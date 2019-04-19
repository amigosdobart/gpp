package com.brt.gpp.aplicacoes.campanha.entidade;

import com.brt.gpp.aplicacoes.campanha.entidade.SMSCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collection;

/**
 * Esta classe representa as informacoes de uma Campanha promocional.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class Campanha 
{
	private long id;
	private String nomeCampanha;
	private Date validadeInicial;
	private Date validadeFinal;
	private Date validadeIniConcessao;
	private Date validadeFimConcessao;
	
	/**
	 *	Tipo de transacao do bonus concedido pela campanha.
	 */
	private String tipoTransacao;
	
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
	 * Retorna a classe que irah agir como o produtor de
	 * assinantes a serem processados na inscricao de campanha 
	 */
	private ProdutorCampanha produtorCampanha;
	
	private static final String PRODUTOR_DEFAULT = "com.brt.gpp.aplicacoes.campanha.GerInscricaoDefaultCampanhaProdutor";
	
	/**
	 * Metodo....:Campanha
	 * Descricao.:Construtor
	 *
	 */
	public Campanha()
	{
		parametrosInscricao = new ArrayList();
		condicoesConcessao  = new ArrayList();
		smCampanha		    = new ArrayList();
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
	 * Access method for the condicoesConcessao property.
	 * 
	 * @return   the current value of the condicoesConcessao property
	 */
	public Collection getCondicoesConcessao() 
	{
		return condicoesConcessao;    
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
	 * Metodo....:getvalidadeFimConcessao
	 * Descricao.:Retorna o valor de validadeFimConcessao
	 * @return validadeFimConcessao.
	 */
	public Date getValidadeFimConcessao()
	{
		return validadeFimConcessao;
	}
	
	/**
	 * Metodo....:setvalidadeFimConcessao
	 * Descricao.:Define o valor de validadeFimConcessao
	 * @param validadeFimConcessao o valor a ser definido para validadeFimConcessao
	 */
	public void setValidadeFimConcessao(Date validadeFimConcessao)
	{
		this.validadeFimConcessao = validadeFimConcessao;
	}
	
	/**
	 * Metodo....:getvalidadeIniConcessao
	 * Descricao.:Retorna o valor de validadeIniConcessao
	 * @return validadeIniConcessao.
	 */
	public Date getValidadeIniConcessao()
	{
		return validadeIniConcessao;
	}
	
	/**
	 *	Retorna o tipo de transacao do bonus concedido pela campanha.
	 *
	 *	@return		Tipo de transacao do bonus concedido pela campanha.
	 */
	public String getTipoTransacao()
	{
		return this.tipoTransacao;
	}
	
	/**
	 *	Atribui o tipo de transacao do bonus concedido pela campanha.
	 *
	 *	@param		tipoTransacao			Tipo de transacao do bonus concedido pela campanha.
	 */
	public void setTipoTransacao(String tipoTransacao)
	{
		this.tipoTransacao = tipoTransacao;
	}
	
	/**
	 * Metodo....:setvalidadeIniConcessao
	 * Descricao.:Define o valor de validadeIniConcessao
	 * @param validadeIniConcessao o valor a ser definido para validadeIniConcessao
	 */
	public void setValidadeIniConcessao(Date validadeIniConcessao)
	{
		this.validadeIniConcessao = validadeIniConcessao;
	}
	
	/**
	 * Metodo....:getprodutorCampanha
	 * Descricao.:Retorna o valor de produtorCampanha
	 * @return produtorCampanha.
	 */
	public ProdutorCampanha getProdutorCampanha()
	{
		return produtorCampanha;
	}

	/**
	 * Metodo....:setprodutorCampanha
	 * Descricao.:Define o valor de produtorCampanha
	 * @param produtorCampanha o valor a ser definido para produtorCampanha
	 */
	public void setProdutorCampanha(String nomeProdutorCampanha)
	{
		// Tenta instanciar a classe produtor para a campanha. No CAST utilizado
		// caso a classe nao implemente a interface ProdutorCampanha entao um erro
		// acontecerah e o produtor serah definido nulo, utilizando entao o produtor
		// default.
		if (nomeProdutorCampanha == null)
			nomeProdutorCampanha = PRODUTOR_DEFAULT;
		try
		{
			Class prodClass         = Class.forName(nomeProdutorCampanha);
			Class  initParamClass[] = {long.class};
			Object initParamValue[] = {new Long(0)};
			Constructor construtor   = prodClass.getConstructor(initParamClass);
			this.produtorCampanha = (ProdutorCampanha)construtor.newInstance(initParamValue);
		}
		catch(Exception e)
		{
			// Em caso de excessao entao nao foi possivel adicionar o produtor a campanha
			// O produtor desta campanha fica nulo e serah utilizado o produtor default
			GerentePoolLog gerLog = GerentePoolLog.getInstancia(this.getClass());
			gerLog.log(0,Definicoes.WARN,"Campanha","setProdutorCampanha","Erro ao instanciar ProdutorCampanha:"+e);
		}
	}
	
	/**
	 * Metodo.....:addParametro
	 * Descricao.:Adiciona o nome da classe parametro a lista de parametros de 
	 * inscricao da campanha
	 * @param parametro
	 */
	public void addParametro(String parametro) 
	{
		// Adiciona ao array somente o nome da classe do parametro de inscricao
		// tanto o parametro quanto a condicao de concessao serah instanciados
		// no processamento de inscricao e concessao respectivamente, pois assim
		// concede mais liberadade para a implementacao destas condicoes, uma vez
		// que cada objeto serah independente entre os consumidores
		if (parametro != null)
			parametrosInscricao.add(parametro);
	}
	
	/**
	 * Metodo.....:addCondicao
	 * Descricao.:Adiciona nome da classe CondicaoConcessao a lista de condicoes da 
	 *            campanha
	 * @param condicao
	 */
	public void addCondicao(String condicao) 
	{
		if (condicao != null)
			condicoesConcessao.add(condicao);
	}
	
	/**
	 * Metodo....:addSmCampanha
	 * Descricao.:Adiciona uma mensagem SMS a campanha promocional
	 * @param sms - SMS a ser adicionado
	 */
	public void addSmCampanha(SMSCampanha sms)
	{
		if (sms != null)
			smCampanha.add(sms);
	}
}
