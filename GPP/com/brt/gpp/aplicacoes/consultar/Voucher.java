package com.brt.gpp.aplicacoes.consultar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;

/**
  *
  * Este arquivo contem a definicao da classe de Voucher 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Camile Cardoso Couto
  * Data:               16/03/2002
  *
  * Modificado por:	Alberto Magno
  * Data: 11/10/2004
  * Razao: Implementação Plataforma em Múltiplo Saldo
  *
  * Modificado por:	Marcos C. Magalhães
  * Data: 28/10/2005
  * Razao: Inclusao do atributo, dos métodos de consulta de PIN (security code) dos vouchers
  * e alteração do xml de retorno do método getVoucherXML para retornar o PIN
  * 
  * Adaptado para controle total por: Bernardo Vergne Dias
  * 06/07/2007
  *
  */
public class Voucher
{
	/* Atributos principais */
	private short 	codRetorno;
	private String 	numeroVoucher;
	private String  codigoSeguranca;
	private short   tipoVoucher;
	private String	numeroBatch;
	private Date 	dataUltimaAtualizacao;
	private Date	dataExpiracao;
	
	/* Valores de voucher. Formato: Map <CategoriaTEC, ValorVoucher> */
	private Map 	valoresVoucherPrincipal;	
	private Map 	valoresVoucherBonus;		
	private Map 	valoresVoucherSm;			
	private Map 	valoresVoucherDados;
	
	/* Utilização de voucher */
	private String 	usadoPor;
	private Date 	dataUtilizacao;
	
	/* Cancelamento de voucher */
	private String	canceladoPor;
	private Date	dataCancelamento;
	
	/* Bloqueio de voucher */
	//private String	motivoBloqueio;
	//private String	descMotivoBloqueio;
	
	/* Status */
	private short 	codStatusVoucher;
	private String 	descStatusVoucher;
	
	private SimpleDateFormat formatadorData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	/**
	 * Metodo...: setCodRetorno
	 * Descricao: Armazena o codigo do retorno para o Voucher
	 * @param aCodRetorno - O codigo do retorno para o Voucher
	 */
	public void setCodRetorno (String aCodRetorno)
	{
		this.codRetorno = Short.parseShort(aCodRetorno);	
	}

	/**
	 * Metodo...: setCodRetorno
	 * Descricao: Armazena o codigo do retorno para o Voucher
	 * @param codRetorno - O codigo do retorno para o Voucher
	 */
	public void setCodRetorno(short codRetorno)
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * Metodo...: setNumeroVoucher
	 * Descricao: Armazena o numero do Voucher
	 * @param aNumeroVoucher - O numero do Voucher
	 */
	public void setNumeroVoucher (String aNumeroVoucher)
	{
		this.numeroVoucher = aNumeroVoucher;	
	}	

	/**
	 * Metodo...: setCodStatusVoucher
	 * Descricao: Armazena o codigo do status do Voucher
	 * @param aCodStatusVoucher - O codigo do status do Voucher
	 */
	public void setCodStatusVoucher (short aCodStatusVoucher)
	{
		this.codStatusVoucher = aCodStatusVoucher;	
	}

	/**
	 * Metodo...: setDescStatusVoucher
	 * Descricao: Armazena a descricao do status do Voucher
	 * @param aDescStatusVoucher - A descricao do status do Voucher
	 */
	public void setDescStatusVoucher (String aDescStatusVoucher)
	{
		this.descStatusVoucher = aDescStatusVoucher;	
	}

	/**
	 * Metodo...: setVoucherUsadoPor
	 * Descricao: Armazena quem utilizou o Voucher
	 * @param usadoPor - MSISDN de quem utilizou o Voucher
	 */
	public void setUsadoPor (String usadoPor)
	{
		this.usadoPor = null;
		
		if (usadoPor != null) 
		{
			this.usadoPor = usadoPor.trim();
			
			if (usadoPor.matches("^0*$"))
				this.usadoPor = null;
		}
				
	}

	/**
	 * Metodo....:setDataUltimaAtualizacao
	 * Descricao.:Define a data de utilizacao
	 * @param dataUtilizacao - Data de quando o voucher foi utilizado
	 */
	public void setDataUtilizacao(Date dataUtilizacao)
	{
		this.dataUtilizacao = dataUtilizacao;
	}

	/**
	 * Metodo....:setDataUltimaAtualizacao
	 * Descricao.:Define a data de utilizacao
	 * @param aDataUltimaAtualizacao - Data de quando o voucher foi utilizado
	 */
	public void setDataUtilizacao (String aDataUtilizacao)
	{
		try
		{
			if (aDataUtilizacao == null || aDataUtilizacao.equals("null") ) 
				this.dataUtilizacao = null;
			else
				this.dataUtilizacao = formatadorData.parse(aDataUtilizacao);
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data de utilizacao voucher nao esta no formato valido.");
		}
	}

	/**
	 * Metodo...: setMotivoBloqueio
	 * Descricao: Armazena o codigo do motivo do bloqueio do Voucher
	 * @param aMotivoBloqueio - O codigo do motivo do bloqueio do Voucher
	 *
	public void setMotivoBloqueio (String aMotivoBloqueio)
	{
		this.motivoBloqueio = aMotivoBloqueio;	
	}

	/**
	 * Metodo...: setDescMotivoBloqueio
	 * Descricao: Armazena a descricao do motivo do bloqueio do Voucher
	 * @param aDescMotivoBloqueio - A descricao do motivo do bloqueio do Voucher
	 *
	public void setDescMotivoBloqueio (String aDescMotivoBloqueio)
	{
		this.descMotivoBloqueio = aDescMotivoBloqueio;	
	}*/

	/**
	 * Metodo....:setNumeroBatch
	 * Descricao.:Define o numero de batch do voucher
	 * @param numeroBatch - Numero do batch
	 */
	public void setNumeroBatch(String numeroBatch)
	{
		this.numeroBatch = numeroBatch;
	}

	/**
	 * Metodo....:setSecurityCode
	 * Descricao.:Define o codigo de segurança PIN (security code) do voucher
	 * @param codigoSeguranca - PIN ou codigo de segurança
	 */
	public void setCodigoSeguranca(String codigoSeguranca)
	{
		this.codigoSeguranca = codigoSeguranca;
	}
	
	/**
	 * Metodo...: setTipoVoucher
	 * Descricao: Armazena o tipo de voucher
	 * @param tipoVoucher - O tipo de voucher (1-celular, 103-ligmix)
	 */
	public void setTipoVoucher (short tipoVoucher)
	{
		this.tipoVoucher = tipoVoucher;	
	}
	
	/**
	 * Metodo...: setCanceladoPor
	 * Descricao: Armazena quem cancelou o voucher
	 */
	public void setCanceladoPor(String canceladoPor) 
	{
		this.canceladoPor = canceladoPor;
	}

	/**
	 * Metodo...: setDataCancelamento
	 * Descricao: Armazena o data do cancelamento do voucher
	 */
	public void setDataCancelamento(Date dataCancelamento)
	{
		this.dataCancelamento = dataCancelamento;
	}
	
	/**
	 * Metodo...: setDataCancelamento
	 * Descricao: Armazena o data do cancelamento do voucher
	 */
	public void setDataCancelamento(String dataCancelamento)
	{
		try
		{
			if (dataCancelamento == null || dataCancelamento.equals("null") ) 
				this.dataCancelamento = null;
			else
				this.dataCancelamento = formatadorData.parse(dataCancelamento);
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data de cancelamento do voucher nao esta no formato valido.");
		}
	}
                           
	/**
	 * Metodo...: setDataUltimaAtualizacao
	 * Descricao: Data da última atualização do voucher
	 */
	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao)
	{
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}
	
	/**
	 * Metodo...: setDataUltimaAtualizacao
	 * Descricao: Data da última atualização do voucher
	 */
	public void setDataUltimaAtualizacao(String dataUltimaAtualizacao)
	{
		try
		{
			if (dataUltimaAtualizacao == null || dataUltimaAtualizacao.equals("null") ) 
				this.dataUltimaAtualizacao = null;
			else
				this.dataUltimaAtualizacao = formatadorData.parse(dataUltimaAtualizacao);
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data da última atualização do voucher nao esta no formato valido.");
		}
	}
	
	/**
	 * Metodo...: setDataExpiracao
	 * Descricao: Data da última atualização do voucher
	 */
	public void setDataExpiracao(Date dataExpiracao)
	{
		this.dataExpiracao = dataExpiracao;
	}
	
	/**
	 * Metodo...: setDataExpiracao
	 * Descricao: Data de expiração do voucher
	 */
	public void setDataExpiracao(String dataExpiracao)
	{
		try
		{
			if (dataExpiracao == null || dataExpiracao.equals("null") ) 
				this.dataExpiracao = null;
			else
				this.dataExpiracao = formatadorData.parse(dataExpiracao);
		}
		catch(ParseException pe)
		{
			throw new IllegalArgumentException("Data de expiração do voucher nao esta no formato valido.");
		}
	}
	
	/**
	 * Metodo...: getCodRetorno
	 * Descricao: Busca o codigo do retorno para o Voucher
	 * @return	Codigo do retorno para o Voucher
	 */
	public short getCodRetorno ()
	{
		return this.codRetorno;	
	}

	/**
	 * Metodo...: getNumeroVoucher
	 * Descricao: Busca o numero do Voucher
	 * @param Retorna o numero do Voucher
	 */
	public String getNumeroVoucher ()
	{
		return this.numeroVoucher;	
	}	

	/**
	 * Metodo...: getCodStatusVoucher
	 * Descricao: Busca o codigo do status do Voucher
	 * @param Retorna o codigo do status do Voucher
	 */
	public short getCodStatusVoucher ()
	{
		return this.codStatusVoucher;	
	}

	/**
	 * Metodo...: getDescStatusVoucher
	 * Descricao: Busca a descricao do status do Voucher
	 * @param Retorna a descricao do status do Voucher
	 */
	public String getDescStatusVoucher ()
	{
		return this.descStatusVoucher;	
	}

	/**
	 * Metodo...: getVoucherUsadoPor
	 * Descricao: Busca quem esta utilizando o Voucher
	 * @param Retorna quem esta utilizando o Voucher
	 */
	public String getUsadoPor ()
	{
		return this.usadoPor;	
	}

	/**
	 * Metodo...: getDataUltimaAtualizacao
	 * Descricao: Retorna a data de utilizacao do Voucher
	 * @param Retorna a data de utilizacao do Voucher
	 */
	public Date getDataUtilizacao ()
	{
		return this.dataUtilizacao;
	}

	/**
	 * Metodo...: getMotivoBloqueio
	 * Descricao: Busca o codigo motivo do bloqueio Voucher
	 * @param Retorna o motivo do bloqueio Voucher
	 *
	public String getMotivoBloqueio ()
	{
		return this.motivoBloqueio;	
	}

	/**
	 * Metodo...: getDescMotivoBloqueio
	 * Descricao: Busca a descricao do motivo do bloqueio Voucher
	 * @param Retorna a descricao do motivo do bloqueio Voucher
	 *
	public String getDescMotivoBloqueio ()
	{
		return this.descMotivoBloqueio;	
	}*/
	
	/**
	 * Metodo....:getNumeroBatch
	 * Descricao.:Retorna o numero de batch na qual esse voucher esta associado
	 * @return Numero do batch
	 */
	public String getNumeroBatch()
	{
		return numeroBatch;
	}
	
	/**
	 * Metodo....:getSecurityCode
	 * Descricao.:Retorna o codigo de segurança PIN (security code) do voucher
	 * @return PIN ou codigo de segurança
	 */
	public String getCodigoSeguranca()
	{
		return codigoSeguranca;
	}

	/**
	 * Metodo....:getTipoVoucher
	 * Descricao.:Retorna o tipo de voucher 
	 * @return Tipo de voucher (1-celular, 103-ligmix)
	 */
	public short getTipoVoucher()
	{
		return tipoVoucher;
	}
	
	/**
	 * Metodo....:getCanceladoPor
	 * Descricao.:Retorna quem cancelou o voucher 
	 * @return Cancelado por
	 */
	public String getCanceladoPor() 
	{
		return canceladoPor;
	}

	/**
	 * Metodo....:getDataCancelamento
	 * Descricao.:Retorna o tipo de voucher 
	 * @return Data Cancelamento
	 */
	public Date getDataCancelamento() 
	{
		return dataCancelamento;
	}
	
	/**
	 * @return Mapeamento de ValorVoucher para tipo de saldo Bonus. Chave: CategoriaTEC
	 */
	public Map getValoresVoucherBonus() 
	{
		return valoresVoucherBonus;
	}

	/**
	 * @param valoresVoucherBonus Mapeamento de ValorVoucher para tipo de saldo Bonus. Chave: CategoriaTEC
	 */
	public void setValoresVoucherBonus(Map valoresVoucherBonus) 
	{
		this.valoresVoucherBonus = valoresVoucherBonus;
	}

	/**
	 * @return Mapeamento de ValorVoucher para tipo de saldo Dados. Chave: CategoriaTEC
	 */
	public Map getValoresVoucherDados() 
	{
		return valoresVoucherDados;
	}

	/**
	 * @param valoresVoucherDados Mapeamento de ValorVoucher para tipo de saldo Dados. Chave: CategoriaTEC
	 */
	public void setValoresVoucherDados(Map valoresVoucherDados) 
	{
		this.valoresVoucherDados = valoresVoucherDados;
	}

	/**
	 * @return Mapeamento de ValorVoucher para tipo de saldo Principal. Chave: CategoriaTEC
	 */
	public Map getValoresVoucherPrincipal() 
	{
		return valoresVoucherPrincipal;
	}

	/**
	 * @param valoresVoucherPrincipal Mapeamento de ValorVoucher para tipo de saldo Principal. Chave: CategoriaTEC
	 */
	public void setValoresVoucherPrincipal(Map valoresVoucherPrincipal) 
	{
		this.valoresVoucherPrincipal = valoresVoucherPrincipal;
	}

	/**
	 * @return Mapeamento de ValorVoucher para tipo de saldo SM. Chave: CategoriaTEC
	 */
	public Map getValoresVoucherSm() 
	{
		return valoresVoucherSm;
	}

	/**
	 * @param valoresVoucherSm Mapeamento de ValorVoucher para tipo de saldo SM. Chave: CategoriaTEC
	 */
	public void setValoresVoucherSm(Map valoresVoucherSm) 
	{
		this.valoresVoucherSm = valoresVoucherSm;
	}
	
	/**
	 * @return Data de expiracao do voucher
	 */
	public Date getDataExpiracao() 
	{
		return dataExpiracao;
	}

	/**
	 * @return Data da ultima atualizacao do voucher
	 */
	public Date getDataUltimaAtualizacao() 
	{
		return dataUltimaAtualizacao;
	}

	/**
	 * Metodo...: getVoucherXML
	 * Descricao: Armazena os dados do Voucher para retornar o XML
	 * @param Retorna o xml de retorno com os dados do Voucher
	 */
	public String getVoucherXML ()
	{
		StringBuffer retorno = new StringBuffer();
		
		retorno.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		retorno.append("<GPPConsultaVoucherRetorno>"); 
		
		/* Principais atributos */
		retorno.append("<CodigoRetorno>" 			+ this.getCodRetorno() 								+ "</CodigoRetorno>");
		retorno.append("<NumeroVoucher>" 			+ normalizaString(this.getNumeroVoucher())			+ "</NumeroVoucher>");
		retorno.append("<CodigoSeguranca>"			+ normalizaString(this.getCodigoSeguranca())   		+ "</CodigoSeguranca>");
		retorno.append("<TipoVoucher>"              + this.getTipoVoucher()                             + "</TipoVoucher>");
		retorno.append("<DataUltimaAtualizacao>" 	+ getDataFormatada(this.getDataUltimaAtualizacao()) + "</DataUltimaAtualizacao>");
		retorno.append("<DataExpiracao>" 			+ getDataFormatada(this.getDataExpiracao()) 		+ "</DataExpiracao>");
		
		/* Valores de voucher */
		retorno.append("<ValoresVoucher>");
		valoresVoucher2XML(this.valoresVoucherPrincipal, 	retorno);
		valoresVoucher2XML(this.valoresVoucherBonus, 		retorno);
		valoresVoucher2XML(this.valoresVoucherSm, 			retorno);
		valoresVoucher2XML(this.valoresVoucherDados, 		retorno);
		retorno.append("</ValoresVoucher>");
		
		/* Utilização de voucher */
		retorno.append("<UsadoPor>" 				+ normalizaString(this.getUsadoPor())				+ "</UsadoPor>");
		retorno.append("<DataUtilizacao>" 			+ getDataFormatada(this.getDataUtilizacao()) 		+ "</DataUtilizacao>");
		
		/* Status */
		retorno.append("<CodigoStatusVoucher>" 		+ this.codStatusVoucher 							+ "</CodigoStatusVoucher>");
		retorno.append("<DescricaoStatusVoucher>" 	+ normalizaString(this.getDescStatusVoucher())		+ "</DescricaoStatusVoucher>");
		
		/* Cancelamento de voucher */
		retorno.append("<CanceladoPor>"             + normalizaString(this.getCanceladoPor())           + "</CanceladoPor>");
		retorno.append("<DataCancelamento>"         + getDataFormatada(this.getDataCancelamento())      + "</DataCancelamento>");
		
		/* Bloqueio de voucher */
		//retorno.append("<MotivoBloqueio>"			+ normalizaString(this.getMotivoBloqueio())			+ "</MotivoBloqueio>");
		//retorno.append("<DescricaoMotivoBloqueio>"	+ normalizaString(this.getDescMotivoBloqueio())		+ "</DescricaoMotivoBloqueio>");
		
		retorno.append("</GPPConsultaVoucherRetorno>");
	
		return retorno.toString();  
	}

	/**
	 * Método usado por getVoucherXML para gerar partes do XML do voucher
	 */
	private void valoresVoucher2XML(Map lista, StringBuffer retorno)
	{
		if (lista == null) return;
		
 		for (Iterator it = lista.values().iterator(); it.hasNext(); )
		{
			ValorVoucher vv = (ValorVoucher)it.next();
			String categoriaTEC = "";
			String tipoSaldo = "";
			
			if (vv.getCategoriaTEC() != null) 	categoriaTEC += vv.getCategoriaTEC().getIdtCategoria();
			if (vv.getTipoSaldo() != null) 		tipoSaldo += vv.getTipoSaldo().getIdtTipoSaldo();
			
			retorno.append("<ValorVoucher categoriaTEC=\"" + categoriaTEC + "\" " +
					                         "tipoSaldo=\"" + tipoSaldo +"\" " +
					                         "valorFace=\"" + vv.getValorFace() + "\">");
			retorno.append("<ValorAtual>"		+ vv.getValorAtual()			+ "</ValorAtual>");
			retorno.append("<DiasExpiracao>"		+ vv.getDiasExpiracao()			+ "</DiasExpiracao>");
			retorno.append("</ValorVoucher>");
		}
	}
	/**
	 * Metodo....:getDataFormatada
	 * Descricao.:Formata a apresentacao dos campos de data sendo que estes sejam diferentes de nulo
	 * @param data - Data a ser formatada
	 * @return Data formatada
	 */
	public String getDataFormatada(Date data)
	{
		if (data == null)
			return "";
		else 
			return formatadorData.format(data); 
	}
	
	/**
	 * Retorna o MSISDN formatado (MSISDN = UsadoPor)
	 */
	public String getMsisdnFormatado() 
	{	
		try
		{
			if (usadoPor != null && !usadoPor.trim().equals("")) 
				return "(" + usadoPor.substring(2,4) + ") " + usadoPor.substring(4,8) + "-" + usadoPor.substring(8,12);
			else 
				return "";
		}
		catch(Exception e)
		{
			return usadoPor;					
		}
	}
	
	/**
	* Retorna normalização de string
	* @param s String a ser normalizada
	*/
	private static String normalizaString(String s)
	{
		return s==null||s.equals("null")?"":s;
	}

	public String toString()
	{
		return getVoucherXML();
	}
	
	public int hashCode()
	{
		StringBuffer buff = new StringBuffer();
		buff.append(numeroVoucher);
		return buff.hashCode();
	}
	
	public boolean equals(Object obj) 
	{
		if (obj == null || !(obj instanceof Voucher))
			return false;
		
		if (obj == this)
			return true;
				
		return (this.hashCode() == obj.hashCode());
	}
	
}