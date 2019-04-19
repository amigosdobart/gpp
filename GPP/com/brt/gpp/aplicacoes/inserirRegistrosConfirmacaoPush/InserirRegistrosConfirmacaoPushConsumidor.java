package com.brt.gpp.aplicacoes.inserirRegistrosConfirmacaoPush;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
*
* Essa classe refere-se ao processo de inserção de dados 
* para o processo de envio de confirmacao de push do WIG 
* 
* @Autor: 			Geraldo Palmeira
* Data: 			01/03/2007
*/
public class InserirRegistrosConfirmacaoPushConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	// Parametros necessarios para o processo batch
	private PREPConexao	conexaoPrep;
	private InserirRegistrosConfirmacaoPushProdutor produtor;

	public InserirRegistrosConfirmacaoPushConsumidor() 
	{
		super(GerentePoolLog.getInstancia(InserirRegistrosConfirmacaoPushConsumidor.class).getIdProcesso(Definicoes.CL_INSERIR_REGISTROS_PUSH)
			     ,Definicoes.CL_INSERIR_REGISTROS_PUSH);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception 
	{
		conexaoPrep = produtor.getConexao();	
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception 
	{
		this.produtor = (InserirRegistrosConfirmacaoPushProdutor)produtor;
		startup();
	}
	

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = (InserirRegistrosConfirmacaoPushProdutor)produtor;
        startup();
    }

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj)
	{	
		// Faz o cast do objeto para o ValueObject desejado
		// apos isso realiza o insert na tabela de interface
		InserirRegistrosConfirmacaoPushVO confirmacao = (InserirRegistrosConfirmacaoPushVO)obj;
		
		try 
		{
			// Insere os valores do objeto no mapeamento
			// para montar o XML que será inserido na tabela 
			Map valores = new HashMap();
			valores.put("cpf",confirmacao.getCpf());
			valores.put("cliente",confirmacao.getNome());
			valores.put("cod",confirmacao.getCod());
			valores.put("promocao",confirmacao.getPromocao());
			valores.put("bonus",confirmacao.getBonus());
			
		    // Query que insere na tabela WIGC_CONFIRMACAO_PUSH os assinantes e dados para o envio de push
		    String sqlInsert = " INSERT INTO hsid.wigc_confirmacao_push" +
		    		           "  (NU_MSISDN,ID_PUSH,DT_ENVIO,DT_RECEBIMENTO,DT_CONFIRMACAO," +
		    		           "  DS_PARAMETROS,DS_DADOS_CONFIRMACAO,NU_TENTATIVAS)" +
		    		           " VALUES" +
		    		           "  ( ? , ? ,NULL,NULL,NULL, ? ,NULL,0)";
		    
		    // Parametros da consulta
		    Object paramInsert[] = {confirmacao.getMsisdn(),
		    						confirmacao.getIdPush(),
		    						getXML(valores, confirmacao.getIdPush(), confirmacao.getMsisdn())};
		    
		    // Executa o insert na tabela
		    conexaoPrep.executaPreparedUpdate(sqlInsert, paramInsert, super.getIdLog());
		    // Contador de registros inseridos
		    produtor.incrementaRegistos();
		    super.log(Definicoes.DEBUG,"consumidor.execute","Insert realizado para o msisdn: "+confirmacao.getMsisdn());
		}		
		catch (Exception e)
		{
			super.log(Definicoes.WARN, "consumidor.execute", "Erro ao inserir o msisdn: " + confirmacao.getMsisdn() +" Erro GPP:"+ e);
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
	
	/**
	 * Metodo....:getXML
	 * Descricao.:Este metodo gera o XML que será inserido na tabela
	 * @param  Map    valores - Valores que foram lidos no arquivo
	 * @param  String idPush  - Id push do envio de confirmacao de push
	 * @param  String Msisdn  - Numero do assinante
	 * @return String XML
	 */
	public String getXML(Map valores, String idPush, String msisdn)
	{
		StringBuffer xml = new StringBuffer("<?xml version=\"1.0\"?>");
		xml.append("<G-OTA>");
			xml.append("<envioPush>");
				xml.append("<idPush>" + idPush + "</idPush>");
				xml.append("<msisdn>" + msisdn + "</msisdn>");
				xml.append("<parametros>");
				for (Iterator i = valores.entrySet().iterator(); i.hasNext();)
				{
					Entry entry = (Entry)i.next();
					xml.append("<parametro nome=\"" + entry.getKey() + "\">" + entry.getValue() + "</parametro>");
				}
				xml.append("</parametros>");
			xml.append("</envioPush>");
		xml.append("</G-OTA>");
		return xml.toString();
	}
}
