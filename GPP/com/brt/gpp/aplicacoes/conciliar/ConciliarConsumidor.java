package com.brt.gpp.aplicacoes.conciliar;



import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
* @Autor: 			Denys Oliveira
* Data: 				30/03/2004
*
* Modificado por:  Geraldo Palmeira
* Data:			   28/11/2005
* Razao:		   Essa classe foi adaptada ao modelo Produtor Consumidor.
*/
public class ConciliarConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	
	private PREPConexao			conexaoPrep;
	private ConciliarProdutor 	produtor;

	public ConciliarConsumidor() 
	{
		super(GerentePoolLog.getInstancia(ConciliarConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_REC_CONCILIACAO)
			     ,Definicoes.CL_ENVIO_REC_CONCILIACAO);
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
		this.produtor = (ConciliarProdutor)produtor;
		startup();
	}
	

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = (ConciliarProdutor)produtor;
        startup();
    }

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj)
	{	
		// Faz o cast do objeto para o ValueObject desejado
		// apos isso realiza o insert na tabela de interface
		ConciliarVO conciliar = (ConciliarVO)obj;
		
		try 
		{
		    //Query que insere na tabela TBL_INT_CONCIL_OUT as recargas feitas via banco ou pagas com cartão de crédito
		    String sqlInsert = " INSERT INTO TBL_INT_CONCILIACAO_OUT " +
						   " (IDT_NSU," +
						   " IDT_NSU_INSTITUICAO," +
						   " ID_TERMINAL," +
						   " TIP_TRANSACAO," +
						   " NOM_OPERADOR," +
						   " ID_SISTEMA_ORIGEM," +
						   " DAT_TRANSACAO," +
						   " VLR_TRANSACAO," +
						   " IDT_MSISDN," +
						   " IDT_STATUS_PROCESSAMENTO," +
						   " ID_RECARGA," +
						   " IDT_EMPRESA," +
						   " DAT_CARGA)" +
						   " VALUES" +
						   " ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
		    Object paramInsert[] = {conciliar.getIdRecarga(),
				                conciliar.getIdNsuInstituicao(),
								conciliar.getIdtTerminal(),
								conciliar.getTipoTransacao(),
								conciliar.getNomeOperador(),
								conciliar.getIdSistemaOrigem(),
								conciliar.getDataRecarga(),
								new Double(conciliar.getVlrPago()),
								conciliar.getIdtMsisdn(),
								Definicoes.IND_LINHA_NAO_PROCESSADA,
								conciliar.getIdRecarga(),
								conciliar.getIdEmpresa(),
								null};
		    conexaoPrep.executaPreparedUpdate(sqlInsert, paramInsert, super.getIdLog());
		
		    produtor.incrementaRecarga();
		    super.log(Definicoes.DEBUG,"consumidor.execute","Recarga id:"+conciliar.getIdRecarga()+" enviada com sucesso para conciliacao.");
		}		
		catch (Exception e)
		{
			super.log(Definicoes.WARN, "consumidor.execute", "Erro ao inserir recarga para conciliacao. Id:"+conciliar.getIdRecarga()+" Erro GPP:"+ e);
		}
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
}
