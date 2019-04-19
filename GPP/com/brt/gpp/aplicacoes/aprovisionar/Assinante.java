package com.brt.gpp.aplicacoes.aprovisionar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;

/**
 * Este arquivo contem a definicao da classe de Assinante 
 * <P> Versao:        	1.0
 *
 * @Autor:            	Daniel Cintra Abib
 * Data:               15/03/2002
 *
 * Modificado por: 
 * Data: 
 * Razao:
 *
 */
public class Assinante
{
	private int					userId;
	private short 				retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
	private String 				MSISDN;
	private String 				IMSI;
	private Date				dataAtivacao;
	private short 				idioma;
	private short 				planoPreco;
	private short 				statusAssinante;
	private short				statusPeriodico;
	private short 				statusServico;
	private int					subOptions;
	private String  			friendFamily;
	private String				listaFriendsFamily[];
	private String 				naturezaAcesso;		//TFPP, GSM, LIGMIX
	private int					numRecargas;
	private boolean				fezRetornoCiclo3;
	private CodigoNacional		codigoNacional;
	private String				senha;
	private Date				dataCongelamento;
	private double				saldoPrincipal;
	private double				saldoPeriodico;
	private double				saldoBonus;
	private double				saldoTorpedos;
	private double				saldoDados;
	private Date				dataExpPrincipal;
	private Date				dataExpPeriodico;
	private Date				dataExpBonus;
	private Date				dataExpTorpedos;
	private Date				dataExpDados;
	private SimpleDateFormat	conversorDate;
	
	/**
	 * Metodo...: Assinante
	 * Descricao: Construtor que inicializa todos seus atributos
	 */
	public Assinante()
	{
		this.retorno			= 0;
		this.MSISDN				= null;
		this.IMSI				= null;
		this.dataAtivacao		= null;
		this.idioma				= 0;
		this.planoPreco			= 0;
		this.statusAssinante	= 0;
		this.statusPeriodico	= 0;
		this.statusServico		= 0;
		this.numRecargas		= 0;
		this.codigoNacional		= null;
		this.senha				= null;
		this.dataCongelamento	= null;
		this.saldoPrincipal		= 0.0;
		this.saldoPeriodico		= 0.0;
		this.saldoBonus			= 0.0;
		this.saldoTorpedos		= 0.0;
		this.saldoDados			= 0.0;
		this.dataExpPrincipal	= null;
		this.dataExpPeriodico	= null;
		this.dataExpBonus		= null;
		this.dataExpTorpedos	= null;
		this.dataExpDados		= null;
		this.conversorDate		= new SimpleDateFormat(Definicoes.MASCARA_DATE);
	}
	
	// MultiploSaldo
	
	/**
	 * @return Returns the naturezaAcesso.
	 */
	public String getNaturezaAcesso()
	{
		return this.naturezaAcesso;
	}
	
	/**
	 * @param naturezaAcesso The naturezaAcesso to set.
	 */
	public void setNaturezaAcesso(String naturezaAcesso)
	{
		this.naturezaAcesso = naturezaAcesso;
	}
	
	public void setFriendFamily(String ff)
	{
		this.friendFamily = ff;
		this.listaFriendsFamily = ff.split(Definicoes.SEPARADOR_FRIENDS_FAMILY);
	}
	
	public String getFriendFamily()
	{
		return this.friendFamily;
	}

	/**
	 * Metodo....:possuiDestinoComoFF
	 * Descricao.:Verifica se o destino passado como parametro existe na lista de FF
	 *            do assinante
	 * @param destino - Numero de destino
	 * @return boolean - Indica se o destino faz parte do amigos toda hora do assinante
	 */
	public boolean possuiDestinoComoFF(String destino)
	{
		// Verifica em cada elemento da lista FF se o destino
		// estah contido em algum elemento, retorna verdadeiro
		// se encontrou
		for (int i=0; i < this.listaFriendsFamily.length; i++)
			if (destino.equals("55"+Long.parseLong(this.listaFriendsFamily[i])))
				return true;
		return false;
	}

	/**
	 * Metodo...: setRetorno
	 * Descricao: Seta Código de Retorno
	 * @param short	aRetorno	Código de Retorno
	 */
	public void setRetorno (short aRetorno)
	{
		this.retorno = aRetorno;
	}
	
	/**
	 * Metodo...: setUserId
	 * Descricao: Seta o UserId do assinante
	 * @param userId	UserId do assinante
	 */
	public void setUserId (int userId)
	{
		this.userId = userId;	
	}
	
	/**
	 * Metodo...: setMSISDN
	 * Descricao: Seta o MSISDN do assinante
	 * @param 	String	aMSISDN	MSISDN do assinante
	 */
	public void setMSISDN (String aMSISDN)
	{
		this.MSISDN = aMSISDN;	
	}

	/**
	 * Metodo...: setCodigoNacional
	 * Descricao: Seta o CodigoNacional do assinante
	 * @param 	String	CodigoNacional	MSISDN do assinante
	 */
	public void setCodigoNacional (CodigoNacional codigoNacional)
	{
		this.codigoNacional = codigoNacional;	
	}

	/**
	 * Metodo...: setIMSI
	 * Descricao: Seta o IMSI
	 * @param 	String	aIMSI	IMSI
	 */
	public void setIMSI (String aIMSI)
	{
		this.IMSI = aIMSI;	
	}
	
	/**
	 * Metodo...:	setDataAtivacao
	 * Descricao:	Atribui a data de ativacao do assinante.
	 * 
	 * @param	String						dataAtivacao				Data de ativacao do assinante.
	 */
	public void setDataAtivacao (Date dataAtivacao)
	{
		this.dataAtivacao = dataAtivacao;	
	}
	
	/***
	 * Metodo...: setIdioma
	 * Descricao: Seta Idioma
	 * @param 	short	aIdioma		Código do Idioma
	 */
	public void setIdioma (short aIdioma)
	{
		this.idioma = aIdioma;	
	}

	/**
	 * Metodo...: setPlanoPreco
	 * Descricao: Seta o Plano de Preço
	 * @param 	String	aPlanoPreco		Plano de Preço
	 */
	public void setPlanoPreco (short aPlanoPreco)
	{
		this.planoPreco = aPlanoPreco;
	}
	
	/**
	 * Metodo...: setSaldoCreditosPrincipal
	 * Descrição: Seta o Saldo de Creditos Principal
	 * @param 	double	aSaldoCreditos		Saldo de Creditos
	 */
	public void setSaldoCreditosPrincipal(double saldoPrincipal)
	{
		this.saldoPrincipal = saldoPrincipal;	
	}
	
	/**
	 *	Atribui o Saldo Periodico.
	 *
	 *	@param		saldoPeriodico			Saldo Periodico.
	 */
	public void setSaldoCreditosPeriodico(double saldoPeriodico)
	{
		this.saldoPeriodico = saldoPeriodico;
	}

	/**
	 * Metodo...: setSaldoCreditosBonus
	 * Descrição: Seta o Saldo de Creditos de bonus
	 * @param 	double	aSaldoCreditos		Saldo de Creditos
	 */
	public void setSaldoCreditosBonus(double saldoBonus)
	{
		this.saldoBonus = saldoBonus;	
	}

	/**
	 * Metodo...: setSaldoCreditosSMS
	 * Descrição: Seta o Saldo de Creditos de sms
	 * @param 	double	aSaldoCreditos		Saldo de Creditos
	 */
	public void setSaldoCreditosSMS(double saldoTorpedos)
	{
		this.saldoTorpedos = saldoTorpedos;	
	}

	/**
	 * Metodo...: setSaldoCreditosDados
	 * Descrição: Seta o Saldo de Creditos de dados
	 * @param 	double	aSaldoCreditos		Saldo de Creditos
	 */
	public void setSaldoCreditosDados(double saldoDados)
	{
		this.saldoDados = saldoDados;	
	}
	
	/**
	 * Metodo...: setStatusAssinante
	 * Descrição: Seta o Status do Assinante
	 * @param 	short	aStatusAssinante	Status do Assinante
	 */
	public void setStatusAssinante(short aStatusAssinante)
	{
		this.statusAssinante = aStatusAssinante;	
	}
	
	/**
	 *	Atribui o status periodico do assinante.
	 *
	 *	@param		statusPeriodico			Status periodico do assinante.
	 */
	public void setStatusPeriodico(short statusPeriodico)
	{
		this.statusPeriodico = statusPeriodico;	
	}
	
	/**
	 * Metodo...: setStatusServico
	 * Descricao: Seta Status de Serviço do Usuário
	 * @param 	short	aStatusServico		Status do serviço
	 */
	public void setStatusServico (short aStatusServico)
	{
		this.statusServico = aStatusServico;	
	}
	
	/**
	 *	Atribui o byte de opcoes do assinante (Sub Options).
	 *
	 *	@param		subOptions				Byte de opcoes do assinante (Sub Options).
	 */
	public void setSubOptions(int subOptions)
	{
		this.subOptions = subOptions;	
	}
	
	/**
	 * Metodo...: setDataExpiracaoPrincipal
	 * Descricao: Seta Data de Expiracao do saldo principal
	 * @param 	String	aDataExpiracao	Data de Expiracao
	 */
	public void setDataExpiracaoPrincipal(String dataExpPrincipal)
	{
		try
		{
			this.dataExpPrincipal = this.conversorDate.parse(dataExpPrincipal);	
		}
		catch(Exception ignore){}
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Principal.
	 *
	 *	@param		dataExpPrincipal		Data de expiracao do Saldo Principal.
	 */
	public void setDataExpiracaoPrincipal(Date dataExpPrincipal)
	{
		this.dataExpPrincipal = dataExpPrincipal;	
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Periodico.
	 * 
	 *	@param		dataExpPeriodico		Data de expiracao do Saldo Periodico.
	 */
	public void setDataExpiracaoPeriodico(String dataExpPeriodico)
	{
		try
		{
			this.dataExpPeriodico = this.conversorDate.parse(dataExpPeriodico);	
		}
		catch(Exception ignore){}
	}

	/**
	 *	Atribui a data de expiracao do Saldo Periodico.
	 *
	 *	@param		dataExpPeriodico		Data de expiracao do Saldo Periodico.
	 */
	public void setDataExpiracaoPeriodico(Date dataExpPeriodico)
	{
		this.dataExpPeriodico = dataExpPeriodico;
	}
	
	/**
	 * Metodo...: setDataExpiracaoBonus
	 * Descricao: Seta Data de Expiracao do saldo de bonus
	 * @param 	String	aDataExpiracao	Data de Expiracao
	 */
	public void setDataExpiracaoBonus(String dataExpBonus)
	{
		try
		{
			this.dataExpBonus = this.conversorDate.parse(dataExpBonus);	
		}
		catch(Exception ignore){}
	}

	/**
	 *	Atribui a data de expiracao do Saldo de Bonus.
	 *
	 *	@param		dataExpBonus			Data de expiracao do Saldo Bonus.
	 */
	public void setDataExpiracaoBonus(Date dataExpBonus)
	{
		this.dataExpBonus = dataExpBonus;
	}
	
	/**
	 * Metodo...: setDataExpiracaoSms
	 * Descricao: Seta Data de Expiracao do saldo de SMS
	 * @param 	String	aDataExpiracao	Data de Expiracao
	 */
	public void setDataExpiracaoSMS(String dataExpTorpedos)
	{
		try
		{
			this.dataExpTorpedos = this.conversorDate.parse(dataExpTorpedos);	
		}
		catch(Exception ignore){}
	}

	/**
	 *	Atribui a data de expiracao do Saldo de Torpedos.
	 *
	 *	@param		dataExpTorpedos			Data de expiracao do Saldo de Torpedos.
	 */
	public void setDataExpiracaoSMS(Date dataExpTorpedos)
	{
		this.dataExpTorpedos = dataExpTorpedos;
	}
	
	/**
	 * Metodo...: setDataExpiracaoDados
	 * Descricao: Seta Data de Expiracao do saldo de SMS
	 * @param 	String	aDataExpiracao	Data de Expiracao
	 */
	public void setDataExpiracaoDados(String dataExpDados)
	{
		try
		{
			this.dataExpDados = this.conversorDate.parse(dataExpDados);	
		}
		catch(Exception ignore){}
	}

	/**
	 *	Atribui a data de expiracao do Saldo de Dados.
	 *
	 *	@param		dataExpDados			Data de expiracao do Saldo de Dados.
	 */
	public void setDataExpiracaoDados(Date dataExpDados)
	{
		this.dataExpDados = dataExpDados;
	}
	
	/**
	 *	Atribui o numero de recargas que o assinante ja efetuou recarga durante o ultimo ciclo de vida de seu MSISDN. 
	 *	Se o valor for igual a 0, o assinante nao efetuou nenhuma recarga.
	 * 
	 *	@param		numRecargas				Numero de recargas efetuadas pelo assinante.
	 */
	public void setNumRecargas(int numRecargas) 
	{
		this.numRecargas = numRecargas; 
	}
	
	/**
	 * Metodo....:setFezRetornoCiclo3
	 * Descricao.:Acerta a informacao de ciclo3 do assinante
	 * @param fezRetornoCiclo3
	 */
	public void setFezRetornoCiclo3(boolean fezRetornoCiclo3)
	{
		this.fezRetornoCiclo3 = fezRetornoCiclo3;
	}
	
	// Metodos getters

	/**
	 * Metodo...: getRetorno
	 * Descricao: Retorna o retorno da operação
	 * @return	short
	 */
	public short getRetorno ( )
	{
		return this.retorno;
	}

	/**
	 * Metodo...: getValoresRecarga
	 * Descricao: Retorna os dados de multiplo saldo
	 * @return	ValoresRecarga	- Dados de Multiplo Saldo
	 */
	public ValoresRecarga getValoresRecarga()
	{
		ValoresRecarga result = new ValoresRecarga();
		
		result.setSaldoPrincipal(this.getCreditosPrincipal());
		result.setSaldoPeriodico(this.getCreditosPeriodico());
		result.setSaldoBonus    (this.getCreditosBonus    ());
		result.setSaldoSMS      (this.getCreditosSms      ());
		result.setSaldoGPRS     (this.getCreditosDados    ());

		result.setDataExpPrincipal(this.getDataExpPrincipal());
		result.setDataExpPeriodico(this.getDataExpPeriodico());
		result.setDataExpBonus    (this.getDataExpBonus    ());
		result.setDataExpSMS      (this.getDataExpTorpedos ());
		result.setDataExpGPRS     (this.getDataExpDados    ());
		
		return result;
	}
	
	/**
	 * Metodo...: getUserId
	 * Descricao: Retorna UserId do assinante
	 * @return 	int
	 */
	public int getUserId ()
	{
		return userId;	
	}

	/**
	 * Metodo...: getMSISDN
	 * Descricao: Retorna o MSISDN
	 * @return	String
	 */
	public String getMSISDN ( )
	{
		return this.MSISDN;	
	}

	/**
	 * Metodo...: getCodigoNacional
	 * Descricao: Retorna o CodigoNacional
	 * @return	CodigoNacional
	 */
	public CodigoNacional getCodigoNacional ( )
	{
		return this.codigoNacional;	
	}

	/**
	 * Metodo...: getIMSI
	 * Descricao: Retorna o IMSI
	 * @return	String
	 */
	public String getIMSI ( )
	{
		return this.IMSI;	
	}
	
	/**
	 *	Retorna a data de ativacao do assinante.
	 * 
	 *	@return		Data de ativacao do assinante.
	 */
	public Date getDataAtivacao()
	{
		return this.dataAtivacao;	
	}
	
	/**
	 * Metodo...: getIdioma
	 * Descricao: Retorna o Idioma
	 * @return	short
	 */
	public short getIdioma ( )
	{
		return this.idioma;	
	}

	/**
	 * Metodo...: getPlanoPreco
	 * Descricao: Retorna o Plano de Preço
	 * @return	short
	 */
	public short getPlanoPreco ( )
	{
		return this.planoPreco;	
	}
	
	/**
	 * Metodo...: getSaldoCreditosPrincipal
	 * Descricao: Retorna o Saldo de Creditos (Principal)
	 * @return	double
	 */
	public double getCreditosPrincipal( )
	{
		return this.saldoPrincipal;
	}

	/**
	 *	Retorna o Saldo Periodico.
	 *
	 *	@return		Saldo Periodico.
	 */
	public double getCreditosPeriodico()
	{
		return this.saldoPeriodico;
	}

	/**
	 * Metodo...: getSaldoCreditosBonus
	 * Descricao: Retorna o Saldo de Creditos (Bonus)
	 * @return	double
	 */
	public double getCreditosBonus()
	{
		return this.saldoBonus;
	}

	/**
	 * Metodo...: getSaldoCreditosSms
	 * Descricao: Retorna o Saldo de Creditos (Sms)
	 * @return	double
	 */
	public double getCreditosSms()
	{
		return this.saldoTorpedos;
	}

	/**
	 * Metodo...: getSaldoCreditosDados
	 * Descricao: Retorna o Saldo de Creditos (Dados)
	 * @return	double
	 */
	public double getCreditosDados()
	{
		return this.saldoDados;
	}

	/**
	 * Metodo...: getStatusAssinante
	 * Descricao: Retorna o Status do Assinante
	 * @return	short
	 */
	public short getStatusAssinante ( )
	{
		return this.statusAssinante;	
	}
	
	/**
	 *	Retorna o status periodico do assinante.
	 *
	 *	@return		Status periodico do assinante.
	 */
	public short getStatusPeriodico()
	{
		return this.statusPeriodico;	
	}
	
	/***
	 * Metodo...: getStatusServico
	 * Descricao: Retorna o status do serviço
	 * @return	short
	 */
	public short getStatusServico ( )
	{
		return this.statusServico;	
	}
	
	/**
	 *	Retorna o byte de opcoes do assinante (Sub Options).
	 *
	 *	@return		Byte de opcoes do assinante (Sub Options).
	 */
	public int getSubOptions()
	{
		return this.subOptions;	
	}
	
	/**
	 * Metodo...: getDataExpiracaoPrincipal
	 * Descricao: Retorna a Data de Expiracao do credito principal 
	 * @return	String  Data de expiracao
	 */
	public String getDataExpiracaoPrincipal()
	{
		if(this.dataExpPrincipal != null)
			return this.conversorDate.format(this.dataExpPrincipal);
		
		return null;
	}

	/**
	 *	Retorna a data de expiracao do Saldo Principal como objeto Date.
	 * 
	 *	@return		Data de expiracao do Saldo Principal.
	 */
	public Date getDataExpPrincipal()
	{
		return this.dataExpPrincipal;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Periodico.
	 * 
	 *	@return		Data de expiracao do Saldo Periodico.
	 */
	public String getDataExpiracaoPeriodico()
	{
		if(this.dataExpPeriodico != null)
			return this.conversorDate.format(this.dataExpPeriodico);
		
		return null;
	}

	/**
	 *	Retorna a data de expiracao do Saldo Periodico como objeto Date.
	 * 
	 *	@return		Data de expiracao do Saldo Periodico.
	 */
	public Date getDataExpPeriodico()
	{
		return this.dataExpPeriodico;
	}
	
	/**
	 * Metodo...: getDataExpiracaoBonus
	 * Descricao: Retorna a Data de Expiracao do credito de bonus 
	 * @return	String  Data de expiracao
	 */
	public String getDataExpiracaoBonus( )
	{
		if(this.dataExpBonus != null)
			return this.conversorDate.format(this.dataExpBonus);
		
		return null;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Bonus como objeto Date.
	 * 
	 *	@return		Data de expiracao do Saldo de Bonus.
	 */
	public Date getDataExpBonus()
	{
		return this.dataExpBonus;
	}
	
	/**
	 * Metodo...: getDataExpiracaoSms
	 * Descricao: Retorna a Data de Expiracao do credito de sms 
	 * @return	String  Data de expiracao
	 */
	public String getDataExpiracaoSms()
	{
		if(this.dataExpTorpedos != null)
			return this.conversorDate.format(this.dataExpTorpedos);
		
		return null;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Torpedos como objeto Date.
	 * 
	 *	@return		Data de expiracao do Saldo de Torpedos.
	 */
	public Date getDataExpTorpedos()
	{
		return this.dataExpTorpedos;
	}
	
	/**
	 * Metodo...: getDataExpiracaoDados
	 * Descricao: Retorna a Data de Expiracao do credito de dados 
	 * @return	String  Data de expiracao
	 */
	public String getDataExpiracaoDados()
	{
		if(this.dataExpDados != null)
			return this.conversorDate.format(this.dataExpDados);
		
		return null;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Dados como objeto Date.
	 * 
	 *	@return		Data de expiracao do Saldo de Dados.
	 */
	public Date getDataExpDados()
	{
		return this.dataExpDados;
	}
	
	/**
	 *	Retorna a senha (PIN) do assinante.
	 * 
	 *	@return		Senha (PIN) do assinante.
	 */
	public String getSenha()
	{
		return this.senha;	
	}
	
	/**
	 *	Atribui a senha (PIN) do assinante.
	 * 
	 *	@param		senha					Senha (PIN) do assinante.
	 */
	public void setSenha(String senha)
	{
		this.senha = senha;	
	}
	
	/**
	 *	Retorna a ultima data de congelamento (bloqueio) do assinante.
	 * 
	 *	@return		Data de congelamento do assinante.
	 */
	public Date getDataCongelamento()
	{
		return this.dataCongelamento;
	}
	
	/**
	 *	Atribui a ultima data de congelamento (bloqueio) do assinante.
	 * 
	 *	@param		dataCongelamento		Data de congelamento do assinante.
	 */
	public void setDataCongelamento(Date dataCongelamento)
	{
		this.dataCongelamento = dataCongelamento;
	}

	/**
	 *	Retorna a data de expiracao do saldo principal do assinante na forma de objeto Date. 
	 *	Se o valor for igual a 0, o assinante nao efetuou nenhuma recarga.
	 * 
	 *	@return		Numero de recargas efetuadas pelo assinante.
	 */
	
	/**
	 *	Retorna o numero de recargas que o assinante ja efetuou recarga durante o ultimo ciclo de vida de seu MSISDN. 
	 *	Se o valor for igual a 0, o assinante nao efetuou nenhuma recarga.
	 * 
	 *	@return		Numero de recargas efetuadas pelo assinante.
	 */
	public int getNumRecargas() 
	{
		return this.numRecargas; 
	}
	
	/**
	 * Metodo....:isPrePago
	 * Descricao.:Este metodo indica se o assinante e assinante
	 *            de pre-pago. Uma forma de verificar isso e identificando
	 *            se o assinante
	 * @return boolean - Indica se o assinante e pre-pago ou nao
	 */	
	public boolean isPrePago()
	{
		// Caso o Assinante nao esteja ativo, portanto nao pode ser
		// considerado um assinante pre-pago.
		return (getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO) ;

//		if ( getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO )
//		{
//			return false;
//		}
//		else 
//		{
//			return true;
//		}
	}

	/**
	 * Metodo....:fezRetornoCiclo3
	 * Descricao.:Indica se um assinante jah realizou retorno de ciclo 3
	 * @return boolean - Indica se o assinante jah realizou retorno do ciclo 3
	 */
	public boolean fezRetornoCiclo3()
	{
		return this.fezRetornoCiclo3;
	}
	
	/**
	 * Metodo....:isHibrido
	 * Descricao.:Identifica se o cliente faz parte de um plano hibrido ou nao
	 * @return boolean - Indica se o cliente possui um plano hibrido
	 */
	public boolean isHibrido()
	{
		boolean hibrido = false;
		if(isPrePago())
		{
			// Faz a busca da referencia da classe de mapeamentos para identificar
			// o plano de preco do assinante
			MapPlanoPreco map = MapPlanoPreco.getInstancia();
			String indHibrido = map.getMapHibrido(String.valueOf(getPlanoPreco()));
			// Se o mapeamento retornar uma string com o valor 1 entao o cliente possui plano
			// hibrido se possuir o valor 0 entao o plano nao e hibrido
			if(indHibrido.equals("1"))
				hibrido = true;
		}
		
		return hibrido;
	}

	/**
	 *	Retorna se o assinante fez alguma recarga durante o ultimo ciclo de vida de seu MSISDN.
	 * 
	 *	@return		True se o assinante fez recarga e false caso contrario.
	 */
	public boolean fezRecarga()
	{
	    return (this.numRecargas > 0);
	}
	
	/**
	 *	Extrai os saldos do assinante a partir da lista de saldos.
	 *
	 * 	@param		saldos					Lista de saldos do assinante. 
	 */
	public void extrairSaldos(Collection saldos)
	{
		for(Iterator iterator = saldos.iterator(); iterator.hasNext();)
		{
			SaldoAssinante saldo = (SaldoAssinante)iterator.next();
			
			switch(saldo.getTipo().getIdtTipoSaldo())
			{
				case TipoSaldo.PRINCIPAL:
					this.setSaldoCreditosPrincipal(saldo.getCreditos());
					this.setDataExpiracaoPrincipal(saldo.getDataExpiracao());
					break;
				case TipoSaldo.TORPEDOS:
					this.setSaldoCreditosSMS(saldo.getCreditos());
					this.setDataExpiracaoSMS(saldo.getDataExpiracao());
					break;
				case TipoSaldo.DADOS:
					this.setSaldoCreditosDados(saldo.getCreditos());
					this.setDataExpiracaoDados(saldo.getDataExpiracao());
					break;
				default: break;
			}
		}
	}

	/**
	 *	Atualiza o saldo do assinante a partir das informacoes de recarga ou ajuste.
	 *
	 * 	@param		valores					Informacoes de recarga ou ajuste. 
	 */
	public void atualizarSaldos(ValoresRecarga valores)
	{
		if(valores != null)
		{
			//Atualizando os creditos em cada saldo.
			this.setSaldoCreditosPrincipal(this.getCreditosPrincipal() + valores.getSaldoPrincipal());
			this.setSaldoCreditosPeriodico(this.getCreditosPeriodico() + valores.getSaldoPeriodico());
			this.setSaldoCreditosBonus    (this.getCreditosBonus    () + valores.getSaldoBonus    ());
			this.setSaldoCreditosSMS      (this.getCreditosSms      () + valores.getSaldoSMS      ());
			this.setSaldoCreditosDados    (this.getCreditosDados    () + valores.getSaldoGPRS     ());
			
			//Para cada saldo, e verificado se a nova data de expiracao esta definida. Se estiver, a data de expiracao
			//do assinante e atualizada de acordo com a nova. Caso contrario, a data e atualizada de acordo com o 
			//numero de dias de expiracao.
			if(valores.getDataExpPrincipal() != null)
				this.setDataExpiracaoPrincipal(valores.getDataExpPrincipal());
			else if(valores.getNumDiasExpPrincipal() > 0)
				this.setDataExpiracaoPrincipal(this.newDataExpiracao(TipoSaldo.PRINCIPAL, valores.getNumDiasExpPrincipal()));
			
			if(valores.getDataExpPeriodico() != null)
				this.setDataExpiracaoPeriodico(valores.getDataExpPeriodico());
			else if(valores.getNumDiasExpPeriodico() > 0)
				this.setDataExpiracaoPeriodico(this.newDataExpiracao(TipoSaldo.PERIODICO, valores.getNumDiasExpPeriodico()));
			
			if(valores.getDataExpBonus() != null)
				this.setDataExpiracaoBonus(valores.getDataExpBonus());
			else if(valores.getNumDiasExpBonus() > 0)
				this.setDataExpiracaoBonus(this.newDataExpiracao(TipoSaldo.BONUS, valores.getNumDiasExpBonus()));
			
			if(valores.getDataExpSMS() != null)
				this.setDataExpiracaoSMS(valores.getDataExpSMS());
			else if(valores.getNumDiasExpSMS() > 0)
				this.setDataExpiracaoSMS(this.newDataExpiracao(TipoSaldo.TORPEDOS, valores.getNumDiasExpSMS()));
			
			if(valores.getDataExpGPRS() != null)
				this.setDataExpiracaoDados(valores.getDataExpGPRS());
			else if(valores.getNumDiasExpGPRS() > 0)
				this.setDataExpiracaoDados(this.newDataExpiracao(TipoSaldo.DADOS, valores.getNumDiasExpGPRS()));
		}
	}
	
	/**
	 *	Calcula e retorna uma nova data de expiracao baseada no saldo e no numero de dias informados.
	 *
	 *	@param		tipoSaldo				Tipo de saldo do assinante.
	 * 	@param		numDiasExpiracao		Numero de dias de expiracao.
	 * 	@return		Nova data de expiracao. 
	 */
	public Date newDataExpiracao(short tipoSaldo, short numDiasExpiracao)
	{
		Calendar	calExpiracao	= Calendar.getInstance();
		Date		dataExpiracao	= null;
		
		//Obtendo a data de expiracao de acordo com o saldo informado. Se a data de expiracao nao estiver definida, 
		//nao ha como calcular uma nova data. Desta forma, e retornado null. 
		switch(tipoSaldo)
		{
			case TipoSaldo.PRINCIPAL:
				dataExpiracao = this.getDataExpPrincipal();
				break;
			case TipoSaldo.PERIODICO:
				dataExpiracao = this.getDataExpPeriodico();
				break;
			case TipoSaldo.BONUS:
				dataExpiracao = this.getDataExpBonus();
				break;
			case TipoSaldo.TORPEDOS:
				dataExpiracao = this.getDataExpTorpedos();
				break;
			case TipoSaldo.DADOS:
				dataExpiracao = this.getDataExpDados();
				break;
			default: return null;
		}

		if(dataExpiracao == null)
			return null;
		
		//Fazendo a comparacao entre a nova data de expiracao, calculada a partir da data atual
		//e somada ao numero de dias informado, e a data de expiracao do assinante. Sera retornada a maior.
		calExpiracao.add(Calendar.DAY_OF_MONTH, numDiasExpiracao);
		return (dataExpiracao.compareTo(calExpiracao.getTime()) >= 0) ? dataExpiracao : calExpiracao.getTime();
	}

	/**
	 * Retorna o Saldo de um dado tipo de saldo
	 * @param tipoSaldo
	 * @return
	 */
	public SaldoAssinante getSaldo(TipoSaldo tipoSaldo)
	{
		SaldoAssinante saldo = new SaldoAssinante(this.getMSISDN(), tipoSaldo);
		saldo.setTipo(tipoSaldo);
		saldo.setMsisdn(this.getMSISDN());
		switch(tipoSaldo.getIdtTipoSaldo())
		{
			case TipoSaldo.PRINCIPAL:
				saldo.setCreditos(this.getCreditosPrincipal());
				saldo.setDataExpiracao(this.getDataExpPrincipal());
				break;
			case TipoSaldo.PERIODICO:
				saldo.setCreditos(this.getCreditosPeriodico());
				saldo.setDataExpiracao(this.getDataExpPeriodico());
				break;
			case TipoSaldo.BONUS:
				saldo.setCreditos(this.getCreditosBonus());
				saldo.setDataExpiracao(this.getDataExpBonus());
				break;
			case TipoSaldo.TORPEDOS:
				saldo.setCreditos(this.getCreditosSms());
				saldo.setDataExpiracao(this.getDataExpTorpedos());
				break;
			case TipoSaldo.DADOS:
				saldo.setCreditos(this.getCreditosDados());
				saldo.setDataExpiracao(this.getDataExpDados());
				break;
			default: break;
			}
		return saldo;
	}
	
	/**
	 * Retorna todos os saldos do assinante
	 * @return SaldoAssinante
	 */
	public Collection getSaldo()
	{
		MapTipoSaldo map = MapTipoSaldo.getInstance();
		Collection saldo = new ArrayList();
		Collection tiposSaldos = map.getAll();
		for(Iterator iterator = tiposSaldos.iterator(); iterator.hasNext();)
		{
			TipoSaldo tipoSaldo = (TipoSaldo)iterator.next();
			SaldoAssinante saldoAssinante = this.getSaldo(tipoSaldo);
			saldo.add(saldoAssinante);
		}
		return saldo;
	}
	
	
	/**
	 * Ajusta varios saldos de um dado assinante
	 * @param saldo
	 */
	public void setSaldo(Collection saldo)
	{
		for(Iterator iterator = saldo.iterator(); iterator.hasNext();)
			this.setSaldo((SaldoAssinante)iterator.next());
	}
	/**
	 * Ajusta um dados saldo do assinante
	 * @param saldo
	 */
	public void setSaldo(SaldoAssinante saldo)
	{
		switch(saldo.getTipo().getIdtTipoSaldo())
		{
			case TipoSaldo.PRINCIPAL:
				this.setDataExpiracaoPrincipal(saldo.getDataExpiracao());
				this.setSaldoCreditosPrincipal(saldo.getCreditos());
				break;
			case TipoSaldo.PERIODICO:
				this.setDataExpiracaoPeriodico(saldo.getDataExpiracao());
				this.setSaldoCreditosPeriodico(saldo.getCreditos());
				break;
			case TipoSaldo.BONUS:
				this.setDataExpiracaoBonus(saldo.getDataExpiracao());
				this.setSaldoCreditosBonus(saldo.getCreditos());
				break;
			case TipoSaldo.TORPEDOS:
				this.setDataExpiracaoSMS(saldo.getDataExpiracao());
				this.setSaldoCreditosSMS(saldo.getCreditos());
				break;
			case TipoSaldo.DADOS:
				this.setDataExpiracaoDados(saldo.getDataExpiracao());
				this.setSaldoCreditosDados(saldo.getCreditos());
				break;
			default: break;
		}
	}
	/**
	 * @see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "MSISDN: " + this.getMSISDN();
	}
	
}