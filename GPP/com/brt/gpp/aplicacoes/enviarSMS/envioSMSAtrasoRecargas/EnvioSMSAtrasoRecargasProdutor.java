package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas;

//Imports Java.
import java.text.SimpleDateFormat;
import java.util.Date;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Produtor de registros para envio de SMS em caso de atraso no consumo de recargas. Caso o processo de consumo
 *	nao tiver processado registros apos uma data definida, deve ser enviada uma mensagem SMS para o assinante
 *	informando que a recarga esta pendente de processamento.
 *
 *	@author	Daniel Ferreira
 *	@since	15/03/2006
 */
public class EnvioSMSAtrasoRecargasProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    private	SelecaoSMSAtrasoRecargas	selecao;
    private	PREPConexao					conexaoPrep;
    private int							numRegistros;
    private String						status;
    private	String						mensagem;
    private	String						dataProcessamento;
    private boolean						notificar;

    //Construtores.

    /**
     *	Construtor da classe.
     *
     *	@param		long					idLog						Identificador de LOG.
     */
	public EnvioSMSAtrasoRecargasProdutor(long logId)
	{
		super(logId, Definicoes.CL_ENVIO_SMS_ATRASO_REC_PROD);

		this.selecao			= SelecaoSMSAtrasoRecargas.getInstance();
		this.conexaoPrep		= null;
		this.numRegistros		= 0;
		this.status				= Definicoes.PROCESSO_SUCESSO;
		this.mensagem			= "Numero de registros processados: ";
		this.dataProcessamento	= null;
	}

	//Implementacao de Produtor.

    /**
     *	Inicia a execucao do processo de envio de SMS aos assinantes com recargas com atraso de processamento.
     *	O metodo obtem os registros que sao processados pelas threads consumidoras.
     *
     *	@param		String[]				params						Lista de parametros. Nao utilizado.
     *	@throws		Exception
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");

		try
		{
		    //Obtendo a data de processamento.
		    this.dataProcessamento = new SimpleDateFormat(Definicoes.MASCARA_DATE).format(new Date());

		    //Executando a consulta pelos assinantes.
		    this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    this.selecao.execute(conexaoPrep);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		    try
		    {
			    this.selecao.close();
		    }
		    finally
		    {
		        super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		    }
		}
		finally
		{
		    super.log(Definicoes.INFO, "startup", "Fim");
		}
	}

    /**
     *	Retorna para as threads consumidoras o proximo registro a ser processado.
     *
     *	@return		Object												MSISDN do assinante.
     *	@throws		Exception
     */
	public synchronized Object next() throws Exception
	{
	    Object result = null;

		super.log(Definicoes.DEBUG, "next", "Inicio");

		try
		{
		    result = this.selecao.next();

		    if(result != null)
		    {
		        this.numRegistros++;
		        this.notificar = true;
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}

		return result;
	}

    /**
     *	Fecha o result set e a conexao com o banco de dados.
     *
     *	@throws		Exception
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "finish", "Inicio");

		try
		{
		    if(this.notificar)
		    {
		        this.notificaAtrasoRecargas();
		    }

		    this.selecao.close();
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
		    super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		    super.log(Definicoes.INFO, "finish", "Fim");
		}
	}

    /**
     *	Trata excecoes lancadas pelos consumidores.
     *
     *	@throws		Exception
     */
	public void handleException()
	{
	    this.mensagem = "Excecao lancada por consumidor";
	}

	//Implementacao de ProcessoBatchProdutor.

    /**
     *	Retorna o identificador do processo batch.
     *
     *	@return		int													Identificador do processo batch.
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_ENVIO_SMS_ATRASO_RECARGAS;
	}

    /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch.
     *
     *	@return		String					mensagem					Mensagem informativa sobre a execucao.
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}

    /**
     *	Retorna o status da execucao do processo.
     *
     *	@return		String					status						Status da execucao do processo.
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}

    /**
     *	Atribui o status da execucao do processo.
     *
     *	@param		String					status						Status da execucao do processo.
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}

    /**
     *	Retorna a data de processamento.
     *
     *	@param		String												Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return this.dataProcessamento;
	}

    /**
     *	Retorna a conexao PREP para os consumidores.
     *
     *	@param		PREPConexao											Conexao PREP.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}

	//Outros metodos.

    /**
     *	Notifica a existencia de atraso de consumo de recargas para o suporte.
     */
	private void notificaAtrasoRecargas()
	{
	    try
	    {
//	    	Obtendo os numeros de suporte e a mensagem de notificacao.
	        MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
	        String[] msisdn = mapConfiguracao.getMapValorConfiguracaoGPP("ENVIO_SMS_ATRASO_PULA_PULA_MSISDN_SUP").split(";");
	        String mensagem = mapConfiguracao.getMapValorConfiguracaoGPP("ENVIO_SMS_ATRASO_PULA_PULA_MSG_SUP");

	        //Enviando notificacao para o suporte.
	        for(int i = 0; i < msisdn.length; i++)
	        {
	            EnvioSMSDAO dao = new EnvioSMSDAO(0,conexaoPrep);

	            DadosSMS sms = new DadosSMS();
	            sms.setIdtMsisdn(msisdn[i]);
	            sms.setDesMensagem(mensagem);
	            sms.setNumPrioridade(-1);

	            dao.inserirSMS(sms);
	        }
	    }
	    catch(Exception e)
	    {
	        super.log(Definicoes.ERRO, "notificaAtrasoRecargas", "Excecao: " + e);
	        super.log(Definicoes.WARN, "notificaAtrasoRecargas", "Nao foi possivel enviar notificacao para o suporte");
	    }
	}

}