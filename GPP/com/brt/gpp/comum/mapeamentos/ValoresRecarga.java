package com.brt.gpp.comum.mapeamentos;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorRecarga;

/**
 * Esta classe representa os valores de recarga possiveis para uso do sistema.
 * 
 *	@version		1.0		22/09/2004		Primeira versao.
 *	@author 		Joao Carlos
 *
 *	@version		2.0		03/05/2007		Adaptacao para inclusao do Saldo Periodico.
 *	@author			Daniel Ferreira
 */
public class ValoresRecarga
{
	
	/**
	 *	Identificador do valor.
	 */
	private double idValor;
	
	/**
	 *	Indicador de valor de face. Utilizado para determinar as recargas ativas.
	 */
	private boolean indValorFace;
	
	/**
	 *	Valor efetivo pago.
	 */
	private double valorEfetivoPago;
	
	/**
	 *	Valor de credito no Saldo Principal.
	 */
	private double saldoPrincipal;
	
	/**
	 *	Valor de credito no Saldo Periodico.
	 */
	private double saldoPeriodico;
	
	/**
	 *	Valor de credito no Saldo de Bonus.
	 */
	private double saldoBonus;
	 
	/**
	 *	Valor de credito no Saldo de Torpedos.
	 */
	private double saldoSMS;
	
	/**
	 *	Valor de credito no Saldo de Dados.
	 */
	private double saldoGPRS;
	
	/**
	 *	Valor de bonus no Saldo Principal.
	 */
	private double valorBonusPrincipal;
	
	/**
	 *	Valor de bonus no Saldo Periodico.
	 */
	private double valorBonusPeriodico;
	
	/**
	 *	Valor de bonus no Saldo de Bonus.
	 */
	private double valorBonusBonus;
	
	/**
	 *	Valor de bonus no Saldo de Torpedos.
	 */
	private double valorBonusSMS;
	
	/**
	 *	Valor de bonus no Saldo de Dados.
	 */
	private double valorBonusGPRS;
	
	/**
	 *	Numero de dias de expiracao do Saldo Principal.
	 */
	private short numDiasExpPrincipal;
	
	/**
	 *	Numero de dias de expiracao do Saldo Periodico.
	 */
	private short numDiasExpPeriodico;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Bonus.
	 */
	private short numDiasExpBonus;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Torpedos.
	 */
	private short numDiasExpSMS;
	
	/**
	 *	Numero de dias de expiracao do Saldo de Dados.
	 */
	private short numDiasExpGPRS;
	
	/**
	 *	Data de expiracao do Saldo Principal.
	 */
	private Date dataExpPrincipal;
	
	/**
	 *	Data de expiracao do Saldo Periodico.
	 */
	private Date dataExpPeriodico;
	
	/**
	 *	Data de expiracao do Saldo de Bonus.
	 */
	private Date dataExpBonus;
	
	/**
	 *	Data de expiracao do Saldo de Torpedos.
	 */
	private Date dataExpSMS;
	
	/**
	 *	Data de expiracao do Saldo de Dados.
	 */
	private Date dataExpGPRS;
	
	/**
	 *	Construtor que reseta todos os atributos da classe.
	 */
	public ValoresRecarga()
	{
		this.idValor				= 0;
		this.indValorFace			= false;
		this.valorEfetivoPago		= 0.0;
		this.saldoPrincipal			= 0.0;
		this.saldoPeriodico			= 0.0;
		this.saldoBonus				= 0.0;
		this.saldoSMS				= 0.0;
		this.saldoGPRS				= 0.0;
		this.valorBonusPrincipal	= 0.0;
		this.valorBonusPeriodico	= 0.0;
		this.valorBonusBonus		= 0.0;
		this.valorBonusSMS			= 0.0;
		this.valorBonusGPRS			= 0.0;
		this.valorEfetivoPago		= 0.0;
		this.numDiasExpPrincipal	= 0;
		this.numDiasExpPeriodico	= 0;
		this.numDiasExpBonus		= 0;
		this.numDiasExpSMS 			= 0;
		this.numDiasExpGPRS			= 0;
		this.dataExpPrincipal		= null;		
		this.dataExpBonus			= null;			
		this.dataExpSMS				= null;				
		this.dataExpGPRS			= null;			
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		listaValores			Lista de objetos de representam a entidade da tabela TBL_REC_VALOR.
	 */
	public ValoresRecarga(Collection listaValores)
	{
		if((listaValores != null) && (listaValores.size() > 0))
			for(Iterator iterator = listaValores.iterator(); iterator.hasNext();)
				this.extrairValor((ValorRecarga)iterator.next());
	}
	
	/**
	 *	Construtor que recebe os valores dos creditos.
	 *
	 *	@param		saldoPrincipal			Valor de credito no Saldo Principal.
	 *	@param		saldoPeriodico			Valor de credito no Saldo Periodico.
	 *	@param		saldoBonus				Valor de credito no Saldo de Bonus.
	 *	@param		saldoSMS				Valor de credito no Saldo de Torpedos.
	 *	@param		saldoGPRS				Valor de credito no Saldo de Dados.
	 */
	public ValoresRecarga(double saldoPrincipal, double saldoPeriodico, double saldoBonus, double saldoSMS, double saldoGPRS)
	{
		this();
		
		this.saldoPrincipal	= saldoPrincipal;
		this.saldoPeriodico	= saldoPeriodico;
		this.saldoBonus		= saldoBonus;
		this.saldoSMS		= saldoSMS;
		this.saldoGPRS		= saldoGPRS;
	}

	/**
	 *	Construtor que recebe os saldos e as datas de expiracao dos creditos.
	 *
	 *	@param		saldoPrincipal			Valor de credito no Saldo Principal.
	 *	@param		saldoPeriodico			Valor de credito no Saldo Periodico.
	 *	@param		saldoBonus				Valor de credito no Saldo de Bonus.
	 *	@param		saldoSMS				Valor de credito no Saldo de Torpedos.
	 *	@param		saldoGPRS				Valor de credito no Saldo de Dados.
	 *	@param		dataExpPrincipal		Data Expiração do Saldo Principal.
	 *	@param		dataExpPeriodico		Data Expiração do Saldo Periodico.
	 *	@param		dataExpBonus			Data Expiração do Saldo de Bonus.
	 *	@param		dataExpSMS				Data Expiração do Saldo de Torpedos.
	 *	@param		dataExpGPRS				Data Expiração do Saldo de Dados.
	 */
	public ValoresRecarga(double saldoPrincipal,
						  double saldoPeriodico,
						  double saldoBonus, 
						  double saldoSMS, 
						  double saldoGPRS,
						  Date dataExpPrincipal,
						  Date dataExpPeriodico,
						  Date dataExpBonus,
						  Date dataExpSMS,
						  Date dataExpGPRS)
	{
		this(saldoPrincipal, saldoPeriodico, saldoBonus, saldoSMS, saldoGPRS);
		
		this.dataExpPrincipal	= dataExpPrincipal;
		this.dataExpPeriodico	= dataExpPeriodico;
		this.dataExpBonus		= dataExpBonus;
		this.dataExpSMS			= dataExpSMS;
		this.dataExpGPRS		= dataExpGPRS;
	}	

	/**
	 *	Retorna o identificador do valor.
	 *
	 *	@return		Identificador do valor.
	 */
	public double getIdValor()
	{
		return this.idValor;
	}
	
	/**
	 *	Indica se os valores de recarga correspondem a um valor de face.
	 *
	 *	@return		True se corresponde a um valor de face e false caso contrario.
	 */
	public boolean isValorFace()
	{
		return this.indValorFace;
	}
	
	/**
	 *	Retorna o valor efetivo pago.
	 *
	 *	@return		Valor efetivo pago.
	 */
	public double getValorEfetivoPago()
	{
		return this.valorEfetivoPago;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo Principal.
	 *
	 *	@return		Valor de credito no Saldo Principal.
	 */
	public double getSaldoPrincipal()
	{
		return this.saldoPrincipal;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo Periodico.
	 *
	 *	@return		Valor de credito no Saldo Periodico.
	 */
	public double getSaldoPeriodico()
	{
		return this.saldoPeriodico;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Bonus.
	 *
	 *	@return		Valor de credito no Saldo de Bonus.
	 */
	public double getSaldoBonus()
	{
		return this.saldoBonus;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Torpedos.
	 *
	 *	@return		Valor de credito no Saldo de Torpedos.
	 */
	public double getSaldoSMS()
	{
		return this.saldoSMS;
	}
	
	/**
	 *	Retorna o valor de credito no Saldo de Dados.
	 *
	 *	@return		Valor de credito no Saldo de Dados.
	 */
	public double getSaldoGPRS()
	{
		return this.saldoGPRS;
	}
	
	/**
	 *	Retorna o valor de bonus no Saldo Principal.
	 *
	 *	@return		Valor de bonus no Saldo Principal.
	 */
	public double getValorBonusPrincipal()
	{
		return this.valorBonusPrincipal;
	}
	
	/**
	 *	Retorna o valor de bonus no Saldo Periodico.
	 *
	 *	@return		Valor de bonus no Saldo Periodico.
	 */
	public double getValorBonusPeriodico()
	{
		return this.valorBonusPeriodico;
	}
	
	/**
	 *	Retorna o valor de bonus no Saldo de Bonus.
	 *
	 *	@return		Valor de bonus no Saldo de Bonus.
	 */
	public double getValorBonusBonus()
	{
		return this.valorBonusBonus;
	}
	
	/**
	 *	Retorna o valor de bonus no Saldo de Torpedos.
	 *
	 *	@return		Valor de bonus no Saldo de Torpedos.
	 */
	public double getValorBonusSMS()
	{
		return this.valorBonusSMS;
	}
	
	/**
	 *	Retorna o valor de bonus no Saldo de Dados.
	 *
	 *	@return		Valor de bonus no Saldo de Dados.
	 */
	public double getValorBonusGPRS()
	{
		return this.valorBonusGPRS;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Principal.
	 */
	public int getNumDiasExpiracaoPrincipal()
	{
		return this.getNumDiasExpPrincipal();
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Principal.
	 */
	public short getNumDiasExpPrincipal()
	{
		return this.numDiasExpPrincipal;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Periodico.
	 */
	public int getNumDiasExpiracaoPeriodico()
	{
		return this.getNumDiasExpPeriodico();
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@return		Numero de dias de expiracao do Saldo Periodico.
	 */
	public short getNumDiasExpPeriodico()
	{
		return this.numDiasExpPeriodico;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Bonus.
	 */
	public int getNumDiasExpiracaoBonus()
	{
		return this.getNumDiasExpBonus();
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Bonus.
	 */
	public short getNumDiasExpBonus()
	{
		return this.numDiasExpBonus;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public int getNumDiasExpiracaoSMS()
	{
		return this.getNumDiasExpSMS();
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public short getNumDiasExpSMS()
	{
		return this.numDiasExpSMS;
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Dados.
	 */
	public int getNumDiasExpiracaoGPRS()
	{
		return this.getNumDiasExpGPRS();
	}
	
	/**
	 *	Retorna o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@return		Numero de dias de expiracao do Saldo de Dados.
	 */
	public short getNumDiasExpGPRS()
	{
		return this.numDiasExpGPRS;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo Principal.
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
	public Date getDataExpPeriodico()
	{
		return this.dataExpPeriodico;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Bonus.
	 *
	 *	@return		Data de expiracao do Saldo de Bonus.
	 */
	public Date getDataExpBonus()
	{
		return this.dataExpBonus;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Torpedos.
	 *
	 *	@return		Data de expiracao do Saldo de Torpedos.
	 */
	public Date getDataExpSMS()
	{
		return this.dataExpSMS;
	}
	
	/**
	 *	Retorna a data de expiracao do Saldo de Dados.
	 *
	 *	@return		Data de expiracao do Saldo de Dados.
	 */
	public Date getDataExpGPRS()
	{
		return this.dataExpGPRS;
	}
	
	/**
	 *	Atribui o identificador do valor.
	 *
	 *	@param		idValor					Identificador do valor.
	 */
	public void setIdValor(double idValor)
	{
		this.idValor = idValor;
	}
	
	/**
	 *	Atribui o indicador de que os valores de recarga correspondem a um valor de face.
	 *
	 *	@param		indValorFace			Indicador de valor de face.
	 */
	public void setIndValorFace(boolean indValorFace)
	{
		this.indValorFace = indValorFace;
	}
	
	/**
	 *	Atribui o valor efetivo pago.
	 *
	 *	@param		valorEfetivoPago		Valor efetivo pago.
	 */
	public void setValorEfetivoPago(double valorEfetivoPago)
	{
		this.valorEfetivoPago = valorEfetivoPago;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo Principal.
	 *
	 *	@param		saldoPrincipal			Valor de credito no Saldo Principal.
	 */
	public void setSaldoPrincipal(double saldoPrincipal)
	{
		this.saldoPrincipal = saldoPrincipal;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo Periodico.
	 *
	 *	@param		saldoPeriodico			Valor de credito no Saldo Periodico.
	 */
	public void setSaldoPeriodico(double saldoPeriodico)
	{
		this.saldoPeriodico = saldoPeriodico;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Bonus.
	 *
	 *	@param		saldoBonus				Valor de credito no Saldo de Bonus.
	 */
	public void setSaldoBonus(double saldoBonus)
	{
		this.saldoBonus = saldoBonus;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Torpedos.
	 *
	 *	@param		saldoSMS				Valor de credito no Saldo de Torpedos.
	 */
	public void setSaldoSMS(double saldoSMS)
	{
		this.saldoSMS = saldoSMS;
	}
	
	/**
	 *	Atribui o valor de credito no Saldo de Dados.
	 *
	 *	@param		saldoGPRS				Valor de credito no Saldo de Dados.
	 */
	public void setSaldoGPRS(double saldoGPRS)
	{
		this.saldoGPRS = saldoGPRS;
	}
	
	/**
	 *	Atribui o valor de bonus no Saldo Principal.
	 *
	 *	@param		valorBonusPrincipal		Valor de bonus no Saldo Principal.
	 */
	public void setValorBonusPrincipal(double valorBonusPrincipal)
	{
		this.valorBonusPrincipal = valorBonusPrincipal;
	}
	
	/**
	 *	Atribui o valor de bonus no Saldo Periodico.
	 *
	 *	@param		valorBonusPeriodico		Valor de bonus no Saldo Periodico.
	 */
	public void setValorBonusPeriodico(double valorBonusPeriodico)
	{
		this.valorBonusPeriodico = valorBonusPeriodico;
	}
	
	/**
	 *	Atribui o valor de bonus no Saldo de Bonus.
	 *
	 *	@param		valorBonusBonus			Valor de bonus no Saldo de Bonus.
	 */
	public void setValorBonusBonus(double valorBonusBonus)
	{
		this.valorBonusBonus = valorBonusBonus;
	}
	
	/**
	 *	Atribui o valor de bonus no Saldo de Torpedos.
	 *
	 *	@param		Valor de bonus no Saldo de Torpedos.
	 */
	public void setValorBonusSMS(double valorBonusSMS)
	{
		this.valorBonusSMS = valorBonusSMS;
	}
	
	/**
	 *	Atribui o valor de bonus no Saldo de Dados.
	 *
	 *	@param		Valor de bonus no Saldo de Dados.
	 */
	public void setValorBonusGPRS(double valorBonusGPRS)
	{
		this.valorBonusGPRS = valorBonusGPRS;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@param		numDiasExpPrincipal		Numero de dias de expiracao do Saldo Principal.
	 */
	public void setNumDiasExpiracaoPrincipal(int numDiasExpPrincipal)
	{
		this.setNumDiasExpPrincipal((short)numDiasExpPrincipal);
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Principal.
	 *
	 *	@param		numDiasExpPrincipal		Numero de dias de expiracao do Saldo Principal.
	 */
	public void setNumDiasExpPrincipal(short numDiasExpPrincipal)
	{
		this.numDiasExpPrincipal = numDiasExpPrincipal;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@param		numDiasExpPeriodico		Numero de dias de expiracao do Saldo Periodico.
	 */
	public void setNumDiasExpiracaoPeriodico(int numDiasExpPeriodico)
	{
		this.setNumDiasExpPeriodico((short)numDiasExpPeriodico);
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo Periodico.
	 *
	 *	@param		numDiasExpPeriodico		Numero de dias de expiracao do Saldo Periodico.
	 */
	public void setNumDiasExpPeriodico(short numDiasExpPeriodico)
	{
		this.numDiasExpPeriodico = numDiasExpPeriodico;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@param		numDiasExpBonus			Numero de dias de expiracao do Saldo de Bonus.
	 */
	public void setNumDiasExpiracaoBonus(int numDiasExpBonus)
	{
		this.setNumDiasExpBonus((short)numDiasExpBonus);
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Bonus.
	 *
	 *	@param		numDiasExpBonus			Numero de dias de expiracao do Saldo de Bonus.
	 */
	public void setNumDiasExpBonus(short numDiasExpBonus)
	{
		this.numDiasExpBonus = numDiasExpBonus;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@param		numDiasExpTorpedos		Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public void setNumDiasExpiracaoSMS(int numDiasExpSMS)
	{
		this.setNumDiasExpSMS((short)numDiasExpSMS);
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Torpedos.
	 *
	 *	@param		numDiasExpSMS			Numero de dias de expiracao do Saldo de Torpedos.
	 */
	public void setNumDiasExpSMS(short numDiasExpSMS)
	{
		this.numDiasExpSMS = numDiasExpSMS;
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@param		numDiasExpGPRS			Numero de dias de expiracao do Saldo de Dados.
	 */
	public void setNumDiasExpiracaoGPRS(int numDiasExpGPRS)
	{
		this.setNumDiasExpGPRS((short)numDiasExpGPRS);
	}
	
	/**
	 *	Atribui o numero de dias de expiracao do Saldo de Dados.
	 *
	 *	@param		numDiasExpGPRS			Numero de dias de expiracao do Saldo de Dados.
	 */
	public void setNumDiasExpGPRS(short numDiasExpGPRS)
	{
		this.numDiasExpGPRS = numDiasExpGPRS;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Principal.
	 *
	 *	@param		dataExpPrincipal		Data de expiracao do Saldo Principal.
	 */
	public void setDataExpPrincipal(Date dataExpPrincipal)
	{
		this.dataExpPrincipal = dataExpPrincipal;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo Periodico.
	 *
	 *	@param		dataExpPeriodico		Data de expiracao do Saldo Periodico.
	 */
	public void setDataExpPeriodico(Date dataExpPeriodico)
	{
		this.dataExpPeriodico = dataExpPeriodico;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Bonus.
	 *
	 *	@param		dataExpBonus			Data de expiracao do Saldo de Bonus.
	 */
	public void setDataExpBonus(Date dataExpBonus)
	{
		this.dataExpBonus = dataExpBonus;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Torpedos.
	 *
	 *	@param		dataExpSMS				Data de expiracao do Saldo de Torpedos.
	 */
	public void setDataExpSMS(Date dataExpSMS)
	{
		this.dataExpSMS = dataExpSMS;
	}
	
	/**
	 *	Atribui a data de expiracao do Saldo de Dados.
	 *
	 *	@param		dataExpGPRS				Data de expiracao do Saldo de Dados.
	 */
	public void setDataExpGPRS(Date dataExpGPRS)
	{
		this.dataExpGPRS = dataExpGPRS;
	}
	
	/**
	 *	Extrai os valores a partir do objeto representando os valores de recarga.
	 *
	 *	@param		valor					Objeto representando os valores de recarga.
	 */
	private void extrairValor(ValorRecarga valor)
	{
		if((valor !=  null) && (valor.getTipoSaldo() != null))
		{
			//Extraindo os valores.
			TipoSaldo	tipoSaldo		= valor.getTipoSaldo();
			double		idValor			= valor.getIdValor();
			boolean		indValorFace	= valor.isValorFace();
			double		valorPago		= valor.getVlrEfetivoPago();
			double		valorCredito	= valor.getVlrCredito();
			double		valorBonus		= valor.getVlrBonus();
			short		numDiasExp		= valor.getNumDiasExpiracao();
			
			//O valorEfetivoPago sera atribuido em funcao da soma dos valores pagos de cada saldo.
			this.setValorEfetivoPago(this.getValorEfetivoPago() + valorPago);
			
			//Definindo em qual saldo atribuir os valores extraidos. Para a atribuicao do identificador do valor,
			//serao levados em consideracao somente os saldos Principal e Periodico (sao estes os saldos que definem
			//as recargas). Ja para o indicador de valor de face sera levado em consideracao somente o Saldo 
			//Principal, uma vez que nao ha recarga de cartao que insira creditos em saldos de franquia.
			switch(tipoSaldo.getIdtTipoSaldo())
			{
				case TipoSaldo.PRINCIPAL:
					this.setIdValor(idValor);
					this.setIndValorFace(indValorFace);
					this.setSaldoPrincipal(valorCredito);
					this.setValorBonusPrincipal(valorBonus);
					this.setNumDiasExpPrincipal(numDiasExp);
					break;
				case TipoSaldo.PERIODICO:
					this.setIdValor(idValor);
					this.setSaldoPeriodico(valorCredito);
					this.setValorBonusPeriodico(valorBonus);
					this.setNumDiasExpPeriodico(numDiasExp);
					break;
				case TipoSaldo.BONUS:
					this.setSaldoBonus(valorCredito);
					this.setValorBonusBonus(valorBonus);
					this.setNumDiasExpBonus(numDiasExp);
					break;
				case TipoSaldo.TORPEDOS:
					this.setSaldoSMS(valorCredito);
					this.setValorBonusSMS(valorBonus);
					this.setNumDiasExpSMS(numDiasExp);
					break;
				case TipoSaldo.DADOS:
					this.setSaldoGPRS(valorCredito);
					this.setValorBonusGPRS(valorBonus);
					this.setNumDiasExpGPRS(numDiasExp);
					break;
				default: break;
			}
		}
	}
	
	/**
	 *	Extrai e retorna os valores de bonus da recarga. Caso nao hava nenhum valor de bonus definido, retorna NULL.
	 *
	 *	@return		Valore de bonus da recarga.
	 */
	public ValoresRecarga getBonus()
	{
		ValoresRecarga result = null;
		
		if((this.getValorBonusPrincipal() > 0.0) || 
		   (this.getValorBonusPeriodico() > 0.0) ||
		   (this.getValorBonusSMS      () > 0.0) ||
		   (this.getValorBonusGPRS     () > 0.0))
		{
			result = new ValoresRecarga();
			
			result.setSaldoPrincipal(this.getValorBonusPrincipal());
			result.setSaldoPeriodico(this.getValorBonusPeriodico());
			result.setSaldoBonus    (this.getValorBonusBonus    ());
			result.setSaldoSMS      (this.getValorBonusSMS      ());
			result.setSaldoGPRS     (this.getValorBonusGPRS     ());
			
			result.setDataExpPrincipal(this.getDataExpPrincipal());
			result.setDataExpPeriodico(this.getDataExpPeriodico());
			result.setDataExpSMS      (this.getDataExpSMS      ());
			result.setDataExpGPRS     (this.getDataExpGPRS     ());
		}
		
		return result;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "ID: "    + this.getIdValor       () + 
		       " - SP: " + this.getSaldoPrincipal() +
		       " - SF: " + this.getSaldoPeriodico() +
		       " - SB: " + this.getSaldoBonus    () +
		       " - ST: " + this.getSaldoSMS      () +
		       " - SD: " + this.getSaldoGPRS     ();
	}
	
	/**
	 *	Atribui o valor de credito no saldo especificado.
	 *
	 *	@param 		bonificacao				 Instancia de <code>BonificacaoPulaPula</code>
	 *	@throws		IllegalArgumentException Caso o tipo de saldo nao esteja definido.
	 */
	public void setValoresRecarga(BonificacaoPulaPula bonificacao)
	{
		switch(bonificacao.getTipoSaldo().getIdtTipoSaldo())
		{
			case TipoSaldo.PRINCIPAL:
				this.setSaldoPrincipal(bonificacao.getValorAReceber());
				break;
			case TipoSaldo.PERIODICO:
				this.setSaldoPeriodico(bonificacao.getValorAReceber());
				break;
			case TipoSaldo.BONUS:
				this.setSaldoBonus(bonificacao.getValorAReceber());
				break;
			case TipoSaldo.TORPEDOS:
				this.setSaldoSMS(bonificacao.getValorAReceber());
				break;
			case TipoSaldo.DADOS:
				this.setSaldoGPRS(bonificacao.getValorAReceber());
				break;
			
			default: 
				throw new IllegalArgumentException("Tipo de Saldo nao definido" + bonificacao.getTipoSaldo());
		}
	}
}