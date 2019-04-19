package com.brt.gpp.aplicacoes.contestar.publicacaoBS;

import java.text.DecimalFormat;

import com.brt.gpp.aplicacoes.contestar.consultaStatusBS.ConsultaStatusBSXMLParser;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;

/**
 *	Parser dos XML's envolvidos no processo de publicacao de BS para o UNIPRO.
 *
 *	@author		Daniel Ferreira
 *	@since		21/12/2006
 */
public abstract class PublicadorBSXMLParser 
{
	/**
	 *	Retorna o XML de retorno da operacao de publicacao de BS para o UNIPRO.
	 *
	 *	@param		numeroBS				Numero do BS gerado pelo SFA.
	 *	@param		numeroIP				Endereco IP do solicitante da abertura da contestacao.
	 *	@param		numeroProtocolo			Numero do Protocolo Unico gerado pelo Toolbar e publicado no UNIPRO.
	 *	@param		mensagem				Mensagem informativa referente a execucao do processo
	 *	@param		codigoRetorno			Codigo de retorno da operacao.
	 *	@return		XML de retorno da operacao de publicacao de BS para o UNIPRO.
	 */
	public static String getXMLRetorno(String numeroBS, String numeroIP, long numeroProtocolo, 
									   String mensagem, short codigoRetorno, String processo)
	{
		StringBuffer xmlRetorno = new StringBuffer(ConsultaStatusBSXMLParser.getCabecalhoXML(processo, null, String.valueOf(numeroProtocolo)));
		
		DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
		GerarXML gerador = new GerarXML("GPPRetornoPublicacaoBS");
		
		gerador.adicionaTag("retorno"			, conversorRetorno.format(codigoRetorno));
		gerador.adicionaTag("mensagem"			, mensagem);
		gerador.adicionaTag("numeroBS"			, numeroBS);
		gerador.adicionaTag("numeroIP"			, numeroIP);
		gerador.adicionaTag("numeroProtocolo"	, String.valueOf(numeroProtocolo));
		
		xmlRetorno.append(gerador.getXML()).append(ConsultaStatusBSXMLParser.getRodapeXML());
		
		return xmlRetorno.toString();
	}
	
	/**
	 * Metodo....: getXMLPublicacao
	 * Descricao.: Retorna o XML utilizado para publicacao do
	 * 			   Protocolo Unico
	 * 
	 * @since  17/04/2007
	 * @param  processo			 - Processo a ser utilizado pelo Vitria
	 * @param  numeroProtocolo	 - Numero do protocolo unico
	 * @param  numeroTerminal	 - Numero do assinante
	 * @param  numeroBS			 - Numero do BS
	 * @param  matriculaOperador - Matricula do Operador
	 * @return xmlPublicacao	 - XML devidamente montado
	 */
	public static String getXMLPublicacao(String processo, long numeroProtocolo, String numeroTerminal, String numeroBS, String matriculaOperador)
	{
		StringBuffer xmlPublicacao = new StringBuffer(ConsultaStatusBSXMLParser.getCabecalhoXML(processo, null, String.valueOf(numeroProtocolo)));
		
		GerarXML gerador = new GerarXML("publicacao-protocolos-nativos");
		
		gerador.adicionaTag("protocolo-unico"		, String.valueOf(numeroProtocolo));
		gerador.abreNo("protocolo-nativo");
			gerador.adicionaTag("protocolo"			, numeroBS);
			gerador.adicionaTag("tipo"	   			, Definicoes.STATUS_BLOQUEIO_SOLICITADO);
			gerador.adicionaTag("matricula-agente"	, matriculaOperador);
			gerador.adicionaTag("numero-terminal"	, numeroTerminal);
		gerador.fechaNo(); // Fecha o no <protocolo-nativo>
		
		xmlPublicacao.append(gerador.getXML()).append(ConsultaStatusBSXMLParser.getRodapeXML());
		
		return xmlPublicacao.toString();
	}
}