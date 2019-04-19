package com.brt.gpp.comum.produtorConsumidor;

/**
 * @author TR027589
 * @since  07/12/2005
 */
public interface ProcessoBatchConsumidor extends Consumidor
{
    public void startup(ProcessoBatchProdutor produtor) throws Exception;
}
