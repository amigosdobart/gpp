package com.brt.gpp.aplicacoes.recargaMassa;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela carga de um registro do arquivo de lote da recarga em massa.
 *  
 *  Formato do registro:
 *  
 *  Sintaxe: <MSISDN>;<AJUSTE_BONUS>;<AJUSTE_SM>;<AJUSTE_DADOS>[;<MENSAGEM_SMS>];
 *  
 *  Onde:
 *  
 *    MSISDN 			:= MSISDN do assinante
 *    AJUSTE_BONUS 		:= Valor (sempre positivo, sem sinal, com vírgula) a ser creditado no saldo de Bonus.
 *    AJUSTE_SM			:= Valor (sempre positivo, sem sinal, com vírgula) a ser creditado no saldo de SM.
 *    AJUSTE_DADOS		:= Valor (sempre positivo, sem sinal, com vírgula) a ser creditado no saldo de Dados.
 *    MENSAGEM_SMS		:= Frase a ser enviada para o assinante no momento do ajuste. Maximo de 120 caracteres.
 *                         Nao usar caracteres especiais nem acentuacao grafica. Pode usar espaço.
 *                         
 *  Exemplo:
 *  
 *    556184002233;05,00;00,00;12,03;Voce obteve uma recarga de 5 reais;
 * 
 * 
 *  Observacao:
 *  
 *    O lote é o nome do arquivo (sem extensão)
 *  
 *  
 *	@author	Bernardo Vergne Dias
 *	Data:	09/08/2007
 */
public class ImportacaoRecargaMassaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private ImportacaoRecargaMassaProdutor produtor = null;
	
    /**
     *	Construtor da classe.
     */
	public ImportacaoRecargaMassaConsumidor()
	{
		super(GerentePoolLog.getInstancia(ImportacaoRecargaMassaConsumidor.class).
				getIdProcesso(Definicoes.CL_IMPORTACAO_RECARGA_MASSA_PRODUTOR), 
		        Definicoes.CL_IMPORTACAO_RECARGA_MASSA_CONSUMIDOR);
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
	public void startup() throws Exception
	{
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
	 /**
     *	@see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.produtor = (ImportacaoRecargaMassaProdutor)produtor;
	}
	
    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
     */
	public void execute(Object obj) throws Exception
	{
		ImportacaoRecargaMassaVO vo = (ImportacaoRecargaMassaVO)obj;

		try
		{
			RegistroLote registro = RegistroLoteParser.parse(
					vo, produtor.getRecargaMassaDAO(), produtor.getConexao()); // valida os campos
			
			if (registro != null)
				produtor.getRecargaMassaDAO().insereSolicitacaoRecarga(registro, produtor.getConexao());
	
			produtor.getConexao().commit();
		}
		catch(Exception e)
		{
			vo.setMensagemErro("Erro ao processar esse registro. Código: " + ImportacaoRecargaMassaProdutor.ERRO_BANCO);
			String erro = (e.getMessage() == null) ? "" : e.getMessage();
			super.log(Definicoes.ERRO, "execute", "Erro ao inserir o registro " + vo.getNumLinha() 
		    		+ " [" + vo.getRegistro() + "] no banco. " + erro);
		}
		finally
		{
			produtor.gravarRegistroLote(vo);
		}
	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
	public void finish()
	{
	}
}
