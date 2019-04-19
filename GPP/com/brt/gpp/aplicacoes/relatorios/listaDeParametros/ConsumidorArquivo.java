package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchDelegate;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Consumidor de Arquivo
 *	Esta classe � respons�vel pelo consumo dos paths de arquivo passado pelo produtor
 *  e a chamada do produtor da f�brica de relat�rios para o seu devido processamento.
 *	@author	Magno Batista Corr�a
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descri��o: Reestrutura��o completa, v�rios fixes
 *  Data: 15/10/2007
 */
public final class ConsumidorArquivo extends Aplicacoes implements ProcessoBatchConsumidor
{
	public ConsumidorArquivo()
	{
		super(GerentePoolLog.getInstancia(ConsumidorArquivo.class).getIdProcesso(Definicoes.CL_PRODUTOR_ARQUIVO), 
		      Definicoes.CL_CONSUMIDOR_ARQUIVO);
	}

	/**
	 * Chama o processo batch de f�brica de relat�rios
	 */
	public void execute(Object obj) throws Exception
	{
		ProcessoBatchDelegate delegate = new ProcessoBatchDelegate(super.logId);
		delegate.executaProcessoBatch(Definicoes.IND_FABRICA_RELATORIO,(String[]) obj);
	}

	/**
	 *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor
	 *  #startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor
     *  #startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
     */
  	public void startup(Produtor produtor) throws Exception
	{
	}

  	/**
	 *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
	}

	/**
 	 *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
}