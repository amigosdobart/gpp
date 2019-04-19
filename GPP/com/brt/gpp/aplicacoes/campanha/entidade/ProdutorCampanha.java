package com.brt.gpp.aplicacoes.campanha.entidade;

import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

public interface ProdutorCampanha extends ProcessoBatchProdutor
{
	/**
	 * Este metodo retorna os dados do assinante que serah utilizado
	 * pelo consumidor para identificao se deve ou nao ser inscrito
	 * na campanha promocional
	 * 
	 * @return Assinante - Objeto assinante a ser processado
	 */
	public Assinante retornarAssinante() throws Exception;
}
