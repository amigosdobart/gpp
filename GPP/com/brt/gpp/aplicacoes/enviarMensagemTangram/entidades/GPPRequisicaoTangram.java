package com.brt.gpp.aplicacoes.enviarMensagemTangram.entidades;

/**
 * Entidade do XML de Requisicao de envio de SMS via Tangram
 * 
 * @author: Jorge Abreu
 * @since:  15/10/2007
 * 
 * <idServico>5</idServico>
 * <idCanal>5<idCanal>
 * <idtOrigem>20050</idtOrigem>
 * <idtMsisdnDestino>556184001000;556184001001;556184001002</idtOrigem>
 * <textoConteudo>Olá!</textoConteudo>
 * <urlNotificacao>http://dominio:porta/pasta/script</urlNotificacao>	[opcional]
 * <dataAgendamento>20071010202020</dataAgendamento>          			[opcional]
 * <indAgendamentoRel>true</indAgendamentoRel>							[opcional]
 * <numTentativasEntrega>3</numTentativasEntrega>						[opcional]
 * <intervaloTentativa>10</intervaloTentativa>							[opcional]
 * <appSpecific>Texto qualquer</appSpecific>							[opcional]
 * <appRequestId>Numero sequencial</appRequestId>						[opcional]
 */

public class GPPRequisicaoTangram 
{
	private String idServico;
	private String idCanal;
	private String idtOrigem;
	private String idtMsisdnDestino;
	private String textoConteudo;
	private String urlNotificacao;
	private String dataAgendamento;
	private String indAgendamentoRel;
	private String numTentativasEntrega;
	private String intervaloTentativa;
	private String appSpecific;
	private String appRequestId;
	
	public void setIdServico(String idServico)
	{
		this.idServico = idServico;
	}
	
	public String getIdServico()
	{
		return this.idServico;
	}
	
	public void setIdCanal(String idCanal)
	{
		this.idCanal = idCanal;
	}
	
	public String getIdCanal()
	{
		return this.idCanal;
	}
	
	public void setIdtOrigem(String idtOrigem)
	{
		this.idtOrigem = idtOrigem;
	}
	
	public String getIdtOrigem()
	{
		return this.idtOrigem;
	}
	
	public void setIdtMsisdnDestino(String idtMsisdnDestino)
	{
		this.idtMsisdnDestino = idtMsisdnDestino;
	}
	
	public String getIdtMsisdnDestino()
	{
		return this.idtMsisdnDestino;
	}
	
	public void setTextoConteudo(String textoConteudo)
	{
		this.textoConteudo = textoConteudo;
	}
	
	public String getTextoConteudo()
	{
		return this.textoConteudo;
	}
	
	public void setUrlNotificacao(String urlNotificacao)
	{
		this.urlNotificacao = urlNotificacao;
	}
	
	public String getUrlNotificacao()
	{
		return this.urlNotificacao;
	}
	
	public void setDataAgendamento(String dataAgendamento)
	{
		this.dataAgendamento = dataAgendamento;
	}
	
	public String getDataAgendamento()
	{
		return this.dataAgendamento;
	}
	
	public void setIndAgendamentoRel(String indAgendamentoRel)
	{
		this.indAgendamentoRel = indAgendamentoRel;
	}
	
	public String getIndAgendamentoRel()
	{
		return this.indAgendamentoRel;
	}
	
	public void setNumtentativasEntrega(String numTentativasEntrega)
	{
		this.numTentativasEntrega = numTentativasEntrega;
	}
	
	public String getNumTentativasEntrega()
	{
		return this.numTentativasEntrega;
	}
	
	public void setIntervaloTentativa(String intervaloTentativas)
	{
		this.intervaloTentativa = intervaloTentativas;
	}
	
	public String getIntervaloTentativa()
	{
		return this.intervaloTentativa;
	}
	
	public void setAppSpecific(String appSpecific)
	{
		this.appSpecific = appSpecific;
	}
	
	public String getAppSpecific()
	{
		return this.appSpecific;
	}
	
	public void setAppRequestId(String appRequestId)
	{
		this.appRequestId = appRequestId;
	}
	
	public String getAppRequestId()
	{
		return this.appRequestId;
	}
	
	public String toString() 
	{
		StringBuffer result = new StringBuffer();
		
		result.append("[GPPRequisicaoTangram]");
		result.append("SERVICO=" + this.idServico);
		result.append(";CANAL=" + this.idCanal);
		result.append(";ORIGEM=" + this.idtOrigem);
		result.append(";MSISDNDESTINO=" + this.idtMsisdnDestino);
		result.append(";TEXTOCONTEUDO=" + this.textoConteudo);
	
		return result.toString();
	}
	
}