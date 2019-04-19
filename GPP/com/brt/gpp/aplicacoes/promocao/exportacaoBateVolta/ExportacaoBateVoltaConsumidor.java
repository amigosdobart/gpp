package com.brt.gpp.aplicacoes.promocao.exportacaoBateVolta;

import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.planoHibrido.expiracaoSaldoControle.ExpiracaoSaldoControleConsumidor;
import com.brt.gpp.aplicacoes.promocao.entidade.ArquivoBateVolta;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ExportacaoBateVoltaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	// Pega uma instância do produtor para o consumidor
	private ExportacaoBateVoltaProdutor	produtor;
	
	// Contém os dados a inserir no arquivo Bate-Volta
	private ArquivoBateVolta 			detalhesRegistro;
	
	/**
	 * Construtor da classe
	 */
	public ExportacaoBateVoltaConsumidor()
    {
		super(GerentePoolLog.getInstancia(ExpiracaoSaldoControleConsumidor.class).getIdProcesso(Definicoes.CL_EXPORTACAO_BATE_VOLTA), Definicoes.CL_EXPORTACAO_BATE_VOLTA);
    }

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup()
	 */
	public void startup()
	{
		// Não há implementações no startup
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor)
	{
		startup();
		
		this.produtor = (ExportacaoBateVoltaProdutor)produtor;		
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor)
	{
		startup((ProcessoBatchProdutor) produtor);			
	}
	
	
	/**
	 * Insere um registro no arquivo
	 * @param Objeto com os dados gerados pelo Produtor
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{	
        TotalizacaoPulaPula totalizacao = (TotalizacaoPulaPula)obj;
        
	    detalhesRegistro = new ArquivoBateVolta();
        
        detalhesRegistro.setMsisdn(totalizacao.getIdtMsisdn());
        detalhesRegistro.setData(new Date());
        detalhesRegistro.setMesReferencia(totalizacao.getDatMes());
        detalhesRegistro.setDuracao(totalizacao.getNumSegundosOffNet());
        detalhesRegistro.setTipoChamada(ArquivoBateVolta.TIPO_CHAMADA_OFFNET);
        
		//Esse método foi colocado no produtor, pois no arquivo é necessário
		//incluir um contador incremental. Colocá-lo no produtor foi a forma
		//encontrada de sincronizar a inserção desse contador.
		produtor.escreveLinha(detalhesRegistro);		
        
        detalhesRegistro.setDuracao(totalizacao.getNumSegundosOnNet());
        detalhesRegistro.setTipoChamada(ArquivoBateVolta.TIPO_CHAMADA_ONNET);
        produtor.escreveLinha(detalhesRegistro);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		// Não há processamento ao finalizar o consumidor
	}	
}