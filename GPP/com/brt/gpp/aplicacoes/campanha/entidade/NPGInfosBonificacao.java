package com.brt.gpp.aplicacoes.campanha.entidade;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import com.brt.gpp.aplicacoes.campanha.util.ParametroCampanhaXMLParser;

/**
 *	Classe que representa informacoes referentes a bonificacao Natal Pague e Ganhe de um assinante.
 * 
 *	@author		Daniel Ferreira
 *	@since		31/10/2006
 */
public class NPGInfosBonificacao 
{
	
	/**
	 *	Data de subida do TSD.
	 */
	private Date dataSubidaTSD;
	
	/**
	 *	MSISDN do assinante.
	 */
	private String msisdn;
	
	/**
	 *	Codigo SAP do aparelho.
	 */
	private String codSAP;
	
	/**
	 *	ICCID do SIMCard utilizado na subida de TSD.
	 */
	private String iccid;
	
	/**
	 *	IMEI do aparelho utilizado na subida de TSD.
	 */
	private String imei;
	
	/**
	 *	Codigo do Fabricante do aparelho.
	 */
	private String codFabricante;
	
	/**
	 *	Descricao do Fabricante do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	private String descFabricante;
	
	/**
	 *	Codigo do Modelo do aparelho.
	 */
	private String codModelo;
	
	/**
	 *	Descricao do Modelo do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	private String descModelo;
	
	/**
	 *	Numero total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 */
	private int numRecargas;
	
	/**
	 *	Valor total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 */
	private double valorRecargas;
	
	/**
	 *	Valor do aparelho, ou seja, valor do bonus a ser concedido.
	 */
	private double valorAparelho;
	
	/**
	 *	Data de recebimento do bonus.
	 */
	private Date dataRecebimento;
	
	/**
	 *	Codigo de status do cadastro.
	 */
	private int codStatus;
	
	/**
	 *	Descricao do status do cadastro.
	 */
	private String descStatus;
	
	/**
	 *	Codigo de retorno da ultima operacao.
	 */
	private int codRetorno;
	
	/**
	 *	Descricao do retorno da ultima operacao.
	 */
	private String descRetorno;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public NPGInfosBonificacao() 
	{
		this.dataSubidaTSD		= null;
		this.msisdn				= null;
		this.codSAP				= "N/D";
		this.iccid				= null;
		this.imei				= null;
		this.codFabricante		= "N/D";
		this.descFabricante		= "Nao definido";
		this.codModelo			= "N/D";
		this.descModelo			= "Nao definido";
		this.numRecargas		= 0;
		this.valorRecargas		= 0.0;
		this.valorAparelho		= 0.0;
		this.dataRecebimento	= null;
		this.codStatus			= -1;
		this.descStatus			= null;
		this.codRetorno			= -1;
		this.descRetorno		= null;
	}
	
	//Getters.
	
	/**
	 *	Retorna da data de subida do TSD.
	 *
	 *	@return		Data de subida do TSD.
	 */
	public Date getDataSubidaTSD() 
	{
		return this.dataSubidaTSD;
	}
	
	/**
	 *	Retorna o MSISDN do assinante.
	 *
	 *	@return		MSISDN do assinante.
	 */
	public String getMsisdn()
	{
		return this.msisdn;
	}
	
	/**
	 *	Retorna o codigo SAP do aparelho.
	 *
	 *	@return		Codigo SAP do aparelho.
	 */
	public String getCodSAP()
	{
		return this.codSAP;
	}
	
	/**
	 *	Retorna o ICCID do SIMCard utilizado na subida de TSD.
	 *
	 *	@return		ICCID do SIMCard utilizado na subida de TSD.
	 */
	public String getICCID()
	{
		return this.iccid;
	}
	
	/**
	 *	Retorna o IMEI do aparelho utilizado na subida de TSD.
	 *
	 *	@return		IMEI do aparelho utilizado na subida de TSD.
	 */
	public String getIMEI()
	{
		return this.imei;
	}
	
	/**
	 *	Retorna o codigo do Fabricante do aparelho.
	 *
	 *	@return		Codigo do Fabricante do aparelho.
	 */
	public String getCodFabricante()
	{
		return this.codFabricante;
	}
	
	/**
	 *	Retorna a descricao do Fabricante do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 *
	 *	@return		Descricao do Fabricante do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	public String getDescFabricante()
	{
		return this.descFabricante;
	}
	
	/**
	 *	Retorna o codigo do Modelo do aparelho.
	 *
	 *	@return		Codigo do Modelo do aparelho.
	 */
	public String getCodModelo()
	{
		return this.codModelo;
	}
	
	/**
	 *	Retorna a descricao do Modelo do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 *
	 *	@return		Descricao do Modelo do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	public String getDescModelo()
	{
		return this.descModelo;
	}
	
	/**
	 *	Retorna o numero total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 *
	 *	@return		Numero total de recargas efetuadas.
	 */
	public int getNumRecargas()
	{
		return this.numRecargas;
	}
	
	/**
	 *	Retorna o valor total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 *
	 *	@return		Valor total de recargas efetuadas.
	 */
	public double getValorRecargas()
	{
		return this.valorRecargas;
	}
	
	/**
	 *	Retorna o valor do aparelho, ou seja, valor do bonus a ser concedido.
	 *
	 *	@return		Valor do aparelho, ou seja, valor do bonus a ser concedido.
	 */
	public double getValorAparelho()
	{
		return this.valorAparelho;
	}
	
	/**
	 *	Retorna a data de recebimento do bonus.
	 *
	 *	@return		Data de recebimento do bonus.
	 */
	public Date getDataRecebimento()
	{
		return this.dataRecebimento;
	}
	
	/**
	 *	Retorna o codigo de status do cadastro.
	 *
	 *	@return		Codigo de status do cadastro.
	 */
	public int getCodStatus()
	{
		return this.codStatus;
	}
	
	/**
	 *	Retorna a descricao do status do cadastro.
	 *
	 *	@return		Descricao do status do cadastro.	
	 */
	public String getDescStatus()
	{
		return this.descStatus;
	}
	
	/**
	 *	Retorna o codigo de retorno da ultima operacao.
	 *
	 *	@return		Codigo de retorno da ultima operacao.
	 */
	public int getCodRetorno()
	{
		return this.codRetorno;
	}
	
	/**
	 *	Retorna a descricao do retorno da ultima operacao.
	 *
	 *	@return		Descricao do retorno da ultima operacao.
	 */
	public String getDescRetorno()
	{
		return this.descRetorno;
	}
	
	//Setters.
	
	/**
	 *	Atribui da data de subida do TSD.
	 *
	 *	@param		dataSubidaTSD			Data de subida do TSD.
	 */
	public void setDataSubidaTSD(Date dataSubidaTSD) 
	{
		this.dataSubidaTSD = dataSubidaTSD;
	}
	
	/**
	 *	Atribui o MSISDN do assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 */
	public void setMsisdn(String msisdn)
	{
		this.msisdn = msisdn;
	}
	
	/**
	 *	Atribui o codigo SAP do aparelho.
	 *
	 *	@param		codSAP					Codigo SAP do aparelho.
	 */
	public void setCodSAP(String codSAP)
	{
		this.codSAP = codSAP;
	}
	
	/**
	 *	Atribui o ICCID do SIMCard utilizado na subida de TSD.
	 *
	 *	@param		iccid					ICCID do SIMCard utilizado na subida de TSD.
	 */
	public void setICCID(String iccid)
	{
		this.iccid = iccid;
	}
	
	/**
	 *	Atribui o IMEI do aparelho utilizado na subida de TSD.
	 *
	 *	@param		imei					IMEI do aparelho utilizado na subida de TSD.
	 */
	public void setIMEI(String imei)
	{
		this.imei = imei;
	}
	
	/**
	 *	Atribui o codigo do Fabricante do aparelho.
	 *
	 *	@param		codFabricante			Codigo do Fabricante do aparelho.
	 */
	public void setCodFabricante(String codFabricante)
	{
		this.codFabricante = codFabricante;
	}
	
	/**
	 *	Atribui a descricao do Fabricante do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 *
	 *	@param		descFabricante			Descricao do Fabricante do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	public void setDescFabricante(String descFabricante)
	{
		this.descFabricante = descFabricante;
	}
	
	/**
	 *	Atribui o codigo do Modelo do aparelho.
	 *
	 *	@param		codModelo				Codigo do Modelo do aparelho.
	 */
	public void setCodModelo(String codModelo)
	{
		this.codModelo = codModelo;
	}
	
	/**
	 *	Atribui a descricao do Modelo do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 *
	 *	@param		descModelo				Descricao do Modelo do aparelho, conforme tabela de precos disponibilizada ao GPP.
	 */
	public void setDescModelo(String descModelo)
	{
		this.descModelo = descModelo;
	}
	
	/**
	 *	Atribui o numero total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 *
	 *	@param		numRecargas				Numero total de recargas efetuadas.
	 */
	public void setNumRecargas(int numRecargas)
	{
		this.numRecargas = numRecargas;
	}
	
	/**
	 *	Atribui o valor total de recargas efetuadas pelo assinante no processo de validacao para liberacao do bonus.
	 *
	 *	@param		valorRecargas			Valor total de recargas efetuadas.
	 */
	public void setValorRecargas(double valorRecargas)
	{
		this.valorRecargas = valorRecargas;
	}
	
	/**
	 *	Atribui o valor do aparelho, ou seja, valor do bonus a ser concedido.
	 *
	 *	@param		valorAparelho			Valor do aparelho, ou seja, valor do bonus a ser concedido.
	 */
	public void setValorAparelho(double valorAparelho)
	{
		this.valorAparelho = valorAparelho;
	}
	
	/**
	 *	Atribui a data de recebimento do bonus.
	 *
	 *	@param		dataRecebimento			Data de recebimento do bonus.
	 */
	public void setDataRecebimento(Date dataRecebimento)
	{
		this.dataRecebimento = dataRecebimento;
	}
	
	/**
	 *	Atribui o codigo de status do cadastro.
	 *
	 *	@param		codStatus				Codigo de status do cadastro.
	 */
	public void setCodStatus(int codStatus)
	{
		this.codStatus = codStatus;
	}
	
	/**
	 *	Atribui a descricao do status do cadastro.
	 *
	 *	@param		descStatus				Descricao do status do cadastro.	
	 */
	public void setDescStatus(String descStatus)
	{
		this.descStatus = descStatus;
	}
	
	/**
	 *	Atribui o codigo de retorno da ultima operacao.
	 *
	 *	@param		int						Codigo de retorno da ultima operacao.
	 */
	public void setCodRetorno(int codRetorno)
	{
		this.codRetorno = codRetorno;
	}
	
	/**
	 *	Atribui a descricao do retorno da ultima operacao.
	 *
	 *	@param		descRetorno				Descricao do retorno da ultima operacao.
	 */
	public void setDescRetorno(String descRetorno)
	{
		this.descRetorno = descRetorno;
	}
	
	//Outros metodos.
	
	/**
	 *	Retorna um mapeamento representando as informacoes de bonificacao Natal Pague e Ganhe.
	 *
	 *	@return		Mapeamento representando as informacoes de bonificacao Natal Pague e Ganhe.
	 */
	public Map toMap()
	{
		LinkedHashMap result = new LinkedHashMap();
		SimpleDateFormat conversorTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		result.put("dataSubidaTSD"	, (this.dataSubidaTSD != null) ? conversorTimestamp.format(this.dataSubidaTSD) : null);
		result.put("msisdn"			, this.msisdn);
		result.put("codSAP"			, this.codSAP);
		result.put("ICCID"			, this.iccid);
		result.put("IMEI"			, this.imei);
		result.put("codFabricante"	, this.codFabricante);
		result.put("descFabricante"	, this.descFabricante);
		result.put("codModelo"		, this.codModelo);
		result.put("descModelo"		, this.descModelo);
		result.put("numRecargas"	, String.valueOf(this.numRecargas));
		result.put("valorRecargas"	, String.valueOf(this.valorRecargas));
		result.put("valorAparelho"	, String.valueOf(this.valorAparelho));
		result.put("dataRecebimento", (this.dataRecebimento != null) ? conversorTimestamp.format(this.dataRecebimento) : null);
		result.put("codStatus"		, String.valueOf(this.codStatus));
		result.put("descStatus"		, this.descStatus);
		result.put("codRetorno"		, String.valueOf(this.codRetorno));
		result.put("descRetorno"	, this.descRetorno);
		
		return result;
	}
	
	/**
	 *	Obtem uma instancia da classe a partir de um XML de parametros.
	 *
	 *	@param		xml						XML representando as informacoes de bonificacao Natal Pague e Ganhe.
	 *	@return		Objeto NPGInfosBonificacao com informacoes disponibilizadas pelos parametros.
	 *	@throws		Exception
	 */
	public static NPGInfosBonificacao newInstance(String xml) throws Exception
	{
		return NPGInfosBonificacao.newInstance(ParametroCampanhaXMLParser.parseXMLCampanha(xml));
	}
	
	/**
	 *	Obtem uma instancia da classe a partir de um mapeamento de parametros.
	 *
	 *	@param		parametros				Mapeamento representando as informacoes de bonificacao Natal Pague e Ganhe.
	 *	@return		Objeto NPGInfosBonificacao com informacoes disponibilizadas pelos parametros.
	 *	@throws		Exception
	 */
	public static NPGInfosBonificacao newInstance(Map parametros) throws Exception
	{
		NPGInfosBonificacao	result				= new NPGInfosBonificacao();
		SimpleDateFormat	conversorTimestamp	= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		if(parametros != null)
		{
			//Executando parse de campos de data.
			String strDataSubidaTSD	= (String)parametros.get("dataSubidaTSD");
			Date dataSubidaTSD = ((strDataSubidaTSD != null) && (!strDataSubidaTSD.equalsIgnoreCase("NULL"))) ?
				conversorTimestamp.parse(strDataSubidaTSD) : null;
			String strDataRecebimento = (String)parametros.get("dataRecebimento");
			Date dataRecebimento = ((strDataRecebimento != null) && (!strDataRecebimento.equalsIgnoreCase("NULL"))) ?
				conversorTimestamp.parse(strDataRecebimento) : null;
			
			//Executando parse de campos inteiros.
			String strNumRecargas = (String)parametros.get("numRecargas");
			int numRecargas = ((strNumRecargas != null) && (!strNumRecargas.equalsIgnoreCase("NULL"))) ?
				Integer.parseInt(strNumRecargas) : 0;
			String strCodStatus = (String)parametros.get("codStatus");
			int codStatus = ((strCodStatus != null) && (!strCodStatus.equalsIgnoreCase("NULL"))) ?
				Integer.parseInt(strCodStatus) : -1;
			String strCodRetorno = (String)parametros.get("codRetorno");
			int codRetorno = ((strCodRetorno != null) && (!strCodRetorno.equalsIgnoreCase("NULL"))) ?
				Integer.parseInt(strCodRetorno) : -1;
				
			//Executando parse de campos double.
			String strValorRecargas = (String)parametros.get("valorRecargas");
			double valorRecargas = ((strValorRecargas != null) && (!strValorRecargas.equalsIgnoreCase("NULL"))) ?
				Double.parseDouble(strValorRecargas) : 0.0;
			String strValorAparelho = (String)parametros.get("valorAparelho");
			double valorAparelho = ((strValorAparelho != null) && (!strValorAparelho.equalsIgnoreCase("NULL"))) ?
				Double.parseDouble(strValorAparelho) : 0.0;
			
			//Preenchendo o objeto.
			result.setDataSubidaTSD(dataSubidaTSD);
			result.setMsisdn((String)parametros.get("msisdn"));
			result.setCodSAP((String)parametros.get("codSAP"));
			result.setICCID((String)parametros.get("ICCID"));
			result.setIMEI((String)parametros.get("IMEI"));
			result.setCodFabricante((String)parametros.get("codFabricante"));
			result.setDescFabricante((String)parametros.get("descFabricante"));
			result.setCodModelo((String)parametros.get("codModelo"));
			result.setDescModelo((String)parametros.get("descModelo"));
			result.setNumRecargas(numRecargas);
			result.setValorRecargas(valorRecargas);
			result.setValorAparelho(valorAparelho);
			result.setDataRecebimento(dataRecebimento);
			result.setCodStatus(codStatus);
			result.setDescStatus((String)parametros.get("descStatus"));
			result.setCodRetorno(codRetorno);
			result.setDescRetorno((String)parametros.get("descRetorno"));
		}
		
		return result;
	}
	
}
