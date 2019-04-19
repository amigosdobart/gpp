package com.brt.gpp.comum.mapeamentos.entidade;

/**
 *	Entidade da tabela TBL_APR_SERVICO_TECNOMEN. Estas entidades representam os servicos que devem ser informados no 
 *	processo de aprovisionamento para que sejam corretamente ativados. Exemplos de servico para ativacao sao: SMMO, 
 *	SMMT, GPRS, WAP, etc. O documento PP 4.4 Provisioning Server Interface 5035617_1.pdf possui mais informacoes 
 *	referentes a ativacao de servicos pela Tecnomen. Os servicos a serem ativados dependem da categoria do plano ao 
 *	qual o assinante encontra-se cadastrado. Por exemplo, assinantes GSM devem ter servicos de SMS e dados ativados, 
 *	porem assinantes da Fixa nao.
 *
 * 	@version	1.0		Versao inicial. 
 *	@author		Daniel Ferreira
 */
public class ServicoTecnomen 
{

	/**
	 *	Identificador do servico. 
	 */
	private short idtServico;
	
	/**
	 *	Nome do servico. 
	 */
	private String nomServico;
	
	/**
	 *	Tipo de servico (Evento ou Dados). Os valores de tipo de servico sao definidos pela Tecnomen. 
	 */
	private short tipServico;
	
	/**
	 *	Status default do servico. 
	 */
	private short idtStatusDefault;
	
	/**
	 *	Status de servico default. 
	 */
	private short idtStatusServicoDefault;
	
	/**
	 *	Construtor da classe.
	 *
	 * 	@param		idtServico				Identificador do servico.
	 * 	@param		nomServico				Nome do servico.
	 * 	@param		tipServico				Tipo do servico.
	 * 	@param		idtStatusDefault		Status default do servico.
	 * 	@param		idtStatusServicoDefault	Status de servico default. 
	 */
	public ServicoTecnomen(short idtServico, String nomServico, short tipServico, short idtStatusDefault, short idtStatusServicoDefault)
	{
		this.idtServico					= idtServico;
		this.nomServico					= nomServico;
		this.tipServico					= tipServico;
		this.idtStatusDefault			= idtStatusDefault;
		this.idtStatusServicoDefault	= idtStatusServicoDefault;
	}
	
	/**
	 *	Retorna o identificador do servico.
	 *
	 * 	@return		Identificador do servico. 
	 */
	public short getIdtServico()
	{
		return this.idtServico;
	}
	
	/**
	 *	Retorna o nome do servico.
	 *
	 * 	@return		Nome do servico. 
	 */
	public String getNomServico()
	{
		return this.nomServico;
	}
	
	/**
	 *	Retorna o tipo de servico (Evento ou Dados).
	 *
	 * 	@return		Tipo de servico (Evento ou Dados).  
	 */
	public short getTipServico()
	{
		return this.tipServico;
	}
	
	/**
	 *	Retorna o status default do servico.
	 *
	 * 	@return		Status default do servico. 
	 */
	public short getIdtStatusDefault()
	{
		return this.idtStatusDefault;
	}
	
	/**
	 *	Retorna o status de servico default. 
	 *
	 *	@return		Status de servico default.
	 */
	public short getIdtStatusServicoDefault()
	{
		return this.idtStatusServicoDefault;
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return "Servico: " + ((this.nomServico != null) ? this.nomServico : "NULL");
	}
	
}
