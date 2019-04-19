package com.brt.gpp.comum.conexoes.toolbar;

import com.brt.gpp.comum.conexoes.toolbar.ElementoProtocoloToolbar;

/**
 *	Parser das linhas enviadas/recebidas durante a execucao do protocolo via socket com o Toolbar.
 *
 *	@author		Daniel Ferreira
 *	@since		21/12/2006
 */
public abstract class ParserLinhaProtocoloToolbar 
{
	/**
	 *	Executa o parse das linha enviada/recebida pelo socket durante a execucao do protocolo e retorna um objeto 
	 *	contendo as informacoes transferidas.
	 *
	 *	@param		linha					Linha contendo as informacoes.
	 *	@return		Objeto contendo as informacoes pertinentes a execucao do protocolo.
	 *	@throws		Exception
	 */
	public static ElementoProtocoloToolbar parse(String linha) throws Exception
	{
		String[] dadosLinha = linha.split("#");
		String[] dadosRetorno = dadosLinha[0].split("\\|");
		
		int codigoRetorno = Integer.parseInt(dadosRetorno[0]);
		String mensagemRetorno = dadosRetorno[1];
		
		// Caso algum erro ocorre entao a string nao vem com duas posicoes
		// portanto a posicao 2 do array eh invalida. Somente pesquisa
		// se o tamanho do array possuir esta posicao
		if (dadosLinha.length < 2)
			throw new Exception("Erro ao pesquisar o numero do protocolo unico. Cod:"+codigoRetorno+" Msg:"+mensagemRetorno);
		
		long numeroProtocolo = Long.parseLong(dadosLinha[1]);
		
		ElementoProtocoloToolbar result = new ElementoProtocoloToolbar(ElementoProtocoloToolbar.MSG_REQ_NUM_PROT_UNICO, 
																	   codigoRetorno, 
																	   mensagemRetorno, 
																	   numeroProtocolo);
		return result;
	}
	
	/**
	 *	Formata a linha para envio pelo socket a partir das informacoes do elemento do protocolo. 
	 *
	 *	@param		elemento				Objeto contendo as informacoes pertinentes a execucao do protocolo.
	 *	@return		Linha para envio pelo socket durante a execucao do protocolo.
	 */
	public static String format(ElementoProtocoloToolbar elemento)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(elemento.getMensagemProtocolo());
		buffer.append("#");
		buffer.append("GPP");
		buffer.append("#");
		buffer.append("UNIPRO_PROTOCOLO");
		buffer.append("#");
		
		return buffer.toString();
	}
}