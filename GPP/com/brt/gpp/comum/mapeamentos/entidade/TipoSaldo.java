package com.brt.gpp.comum.mapeamentos.entidade;

/**
 *	Entidade da tabela TBL_APR_TIPO_SALDO, que define os tipos de saldo de assinantes na plataforma Tecnomen. Cada 
 *	tipo de saldo tem um mapeamento com a categoria de plano de preco do assinante, definindo quais os saldos que a 
 *	categoria pode possuir.
 * 
 *	@author		Daniel Ferreira
 *	@since		14/03/2007
 */
public class TipoSaldo 
{

	/**
	 *	Constante que define o Saldo Principal. Definida pela Tecnomen. 
	 */
	public static final short PRINCIPAL = 0;
	
	/**
	 *	Constante que define o Saldo de Torpedos. Definida pela Tecnomen.
	 */
	public static final short TORPEDOS = 1;
	
	/**
	 *	Constante que define o Saldo de Dados. Definida pela Tecnomen.
	 */
	public static final short DADOS = 2;
	
	/**
	 *	Constante que define o Saldo de Bonus.
	 */
	public static final short BONUS = 3;
	
	/**
	 *	Constante que define o Saldo Periodico.
	 */
	public static final short PERIODICO = 4;
	
	/**
	 *	Identificador do tipo de saldo. 
	 *  Este identificador corresponde ao definido no Provision Server da Tecnomen.
	 */
	private short idtTipoSaldo;
	
	/**
	 *	Identificador do tipo de saldo. 
	 *  Este identificador corresponde ao definido na tabela TPECreditAmountMap da Tecnomen.
	 */
	private short idtTipoSaldoVoucher = -1;
	
	/**
	 *	Nome do tipo de saldo. 
	 */
	private String nomTipoSaldo;
	
	/**
	 *	Flag que indica se o saldo deve ser definido durante o processo de ativacao do assinante. 
	 */
	private boolean indDispAtivacao;
	
	/**
	 *	Tipo de Credito relacionado ao tipo de saldo.
	 */
	private TipoCredito tipoCredito;
	
	/**
	 *	Construtor da classe.
	 *
	 * 	@param		idtTipoSaldo			Identificador do tipo de saldo.
	 * 	@param		nomTipoSaldo			Nome do tipo de saldo.
	 * 	@param		indDispAtivacao			Flag indicando se o saldo deve ser definido durante a ativacao. 
	 */
	public TipoSaldo(short idtTipoSaldo, String nomTipoSaldo, boolean indDispAtivacao)
	{
		this(idtTipoSaldo, nomTipoSaldo, indDispAtivacao, (short)-1);
	}
	
	/**
	 *	Construtor da classe.
	 *
	 * 	@param		idtTipoSaldo			Identificador do tipo de saldo.
	 * 	@param		nomTipoSaldo			Nome do tipo de saldo.
	 * 	@param		indDispAtivacao			Flag indicando se o saldo deve ser definido durante a ativacao. 
	 *  @param		idtTipoSaldoVoucher     Identificador do tipo de saldo para a tabela TPECreditAmountMap da Tecnomen.
	 */
	public TipoSaldo(short idtTipoSaldo, String nomTipoSaldo, boolean indDispAtivacao, short idtTipoSaldoVoucher)
	{
		this.idtTipoSaldo			= idtTipoSaldo;
		this.nomTipoSaldo			= nomTipoSaldo;
		this.indDispAtivacao		= indDispAtivacao;
		this.idtTipoSaldoVoucher	= idtTipoSaldoVoucher;
	}
	
	public TipoSaldo()
	{
	}
	
	/**
	 *	Retorna o identificador do tipo de saldo.
	 *
	 * 	@return		 Identificador do tipo de saldo.
	 */
	public short getIdtTipoSaldo()
	{
		return this.idtTipoSaldo;
	}
	
	/**
	 *	Retorna o nome do saldo. 
	 *
	 *	@return		Nome do tipo de saldo.
	 */
	public String getNomTipoSaldo()
	{
		return this.nomTipoSaldo;
	}
	
	/**
	 *	Indica se o saldo deve ser definido durante o processo de ativacao do assinante.
	 *
	 * 	@return		True se o saldo deve ser definido durante o processo de ativacao do assinante e false caso contrario. 
	 */
	public boolean isDisponivelAtivacao()
	{
		return this.indDispAtivacao;
	}
	
	/**
	 *	@see		java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		return (this.hashCode() == obj.hashCode());
	}
	
	/**
	 *	@see		java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return (this.getClass().getName() + "||" + this.idtTipoSaldo).hashCode();
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Saldo: " + ((this.nomTipoSaldo != null) ? this.nomTipoSaldo : "NULL");
	}

	/**
	 * @param idtTipoSaldo the idtTipoSaldo to set
	 */
	public void setIdtTipoSaldo(short idtTipoSaldo) 
	{
		this.idtTipoSaldo = idtTipoSaldo;
	}

	/**
	 * @param indDispAtivacao the indDispAtivacao to set
	 */
	public void setIndDispAtivacao(boolean indDispAtivacao) 
	{
		this.indDispAtivacao = indDispAtivacao;
	}

	/**
	 * @param nomTipoSaldo the nomTipoSaldo to set
	 */
	public void setNomTipoSaldo(String nomTipoSaldo) 
	{
		this.nomTipoSaldo = nomTipoSaldo;
	}

	/**
	 * @return the indDispAtivacao
	 */
	public boolean isIndDispAtivacao() 
	{
		
		return indDispAtivacao;
	}

	/**
	 * @return Identificador de tipo de saldo para a tabela TPECreditAmountMap da Tecnomen. Valores menores
	 * que zero devem ser desprezados
	 */
	public short getIdtTipoSaldoVoucher() 
	{
		return idtTipoSaldoVoucher;
	}

	/**
	 * @param idtTipoSaldoVoucher Identificador de tipo de saldo para a tabela TPECreditAmountMap da Tecnomen.
	 * Valores menores que zero são desprezados
	 */
	public void setIdtTipoSaldoVoucher(short idtTipoSaldoVoucher) 
	{
		
		this.idtTipoSaldoVoucher = idtTipoSaldoVoucher;
	}

	/**
	 * @return the tipoCredito
	 */
	public TipoCredito getTipoCredito() 
	{
		return tipoCredito;
	}

	/**
	 * @param tipoCredito the tipoCredito to set
	 */
	public void setTipoCredito(TipoCredito tipoCredito) 
	{
		this.tipoCredito = tipoCredito;
	}
	
	
	
}
