package com.brt.gpp.comum.conexoes.tecnomen.entidade;

/**
 *	Objeto que encapsula um MSISDN. E responsavel pela conversao entre os formatos de MSISDN da Tecnomen e do GPP. As 
 *	regras de formato variam de acordo com o servico do assinante. Caso seja um assinante GSM, o formato do MSISDN na  
 *	Tecnomen e igual ao do GPP. Caso seja um assinante da Fixa, o formato do MSISDN na Tecnomen comeca com "0" e no 
 *	GPP comeca com "55". Desta forma o objeto tem condicoes de fazer as devidas conversoes dependendo da operacao.
 *
 *	Esta implementacao preve a portabilidade numerica entre numeros da mesma categoria, mas nao de categorias 
 *	diferentes. Por exemplo, nao sera considerado o caso de numeros com mascara de Terminal Fixo que migraram para 
 *	acessos moveis. Porem sera considerado acessos moveis com range de outras operadoras.
 *
 *	@author		Daniel Ferreira
 *	@since		05/03/2007
 */
public class MSISDN 
{

	/**
	 *	Mascara de numero movel.
	 */
	private static final String MASCARA_MOVEL = "55..[789].......";
	
	/**
	 *	Mascara de Terminal Fixo no formato do GPP.
	 */
	private static final String MASCARA_FIXO_GPP = "55..[23456].......";
	
	/**
	 *	Mascara de Terminal Fixo no formato do Tecnomen.
	 */
	private static final String MASCARA_FIXO_TEC = "0..[23456].......";
	
	/**
	 *	MSISDN do assinante.
	 */
	private String msisdn;

	/**
	 *	Construtor da classe.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public MSISDN(String msisdn)
	{
		this.msisdn = msisdn;
	}

	/**
	 *	Converte e retorna o MSISDN no formato do GPP.
	 *
	 *	@return		MSISDN no formato do GPP.
	 */
	public String toMsisdnGPP()
	{
		if(this.msisdn != null)
			if(this.msisdn.matches(MSISDN.MASCARA_MOVEL) || this.msisdn.matches(MSISDN.MASCARA_FIXO_GPP))
				return this.msisdn;
			else if(this.msisdn.matches(MSISDN.MASCARA_FIXO_TEC))
				return "55" + this.msisdn.substring(1);
			
		return this.msisdn;
	}
	
	/**
	 *	Converte e retorna o MSISDN no formato da Tecnomen.
	 *
	 *	@return		MSISDN no formato da Tecnomen.
	 */
	public String toMsisdnTEC()
	{
		if(this.msisdn != null)
			if(this.msisdn.matches(MSISDN.MASCARA_MOVEL) || this.msisdn.matches(MSISDN.MASCARA_FIXO_TEC))
				return this.msisdn;
			else if(this.msisdn.matches(MSISDN.MASCARA_FIXO_GPP))
				return "0" + this.msisdn.substring(2);
			
		return this.msisdn;
	}

	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return this.toMsisdnGPP();
	}
	
}
