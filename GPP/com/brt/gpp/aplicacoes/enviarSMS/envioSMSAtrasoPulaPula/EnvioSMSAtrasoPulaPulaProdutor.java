package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoPulaPula;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *	Produtor de registros para envio de SMS em caso de atraso na concessao de bonus Pula-Pula. Caso o processo de
 *	concessao nao tiver processado registros apos um horario definido, deve ser enviada uma mensagem SMS para o
 *	assinante informando que o bonus esta pendente de processamento.
 *
 *	@author	Daniel Ferreira
 *	@since	22/05/2006
 */
public class EnvioSMSAtrasoPulaPulaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{

    //Constantes internas.

    /**
     *	Statement SQL para consulta pelos registros com atraso de concessao de bonus Pula-Pula.
     */
    private static final String SQL_SMS_ATRASO_PULA_PULA =
        "SELECT a.idt_msisdn " +
        "  FROM tbl_pro_assinante a, " +
        "       tbl_pro_promocao p, " +
        "       tbl_pro_tipo_transacao t " +
        " WHERE a.idt_promocao = p.idt_promocao " +
        "   AND t.idt_promocao = p.idt_promocao " +
        "   AND p.idt_categoria = ? " +
        "   AND a.dat_execucao <= ? " +
        "   AND t.tip_execucao = ? " +
        "   AND NOT EXISTS(SELECT 1 " +
        "                    FROM tbl_rec_fila_recargas f " +
        "                   WHERE f.idt_msisdn = a.idt_msisdn " +
        "                     AND f.tip_transacao = t.tip_transacao " +
        "                     AND f.idt_status_processamento in (?,?,?,?)" +
        "                     AND f.dat_execucao >= ? " +
        "                     AND f.dat_execucao <  ?) ";

    //Atributos.

    /**
     *	Result Set contendo os registros para processamento pelos consumidores.
     */
    private	ResultSet registros;

    /**
     *	Conexao com o Banco de Dados.
     */
    private	PREPConexao conexaoPrep;

    /**
     *	Contador de numero de registros processados.
     */
    private int numRegistros;

    /**
     *	Contador de numero de registros processados e com SMS enviados.
     */
    private int numEnviados;

    /**
     *	Status geral do processamento.
     */
    private String status;

    /**
     *	Mensagem informativa referente ao status do processo batch.
     */
    private	String mensagem;

    /**
     *	Data de processamento.
     */
    private	Date dataProcessamento;

    //Construtores.

    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public EnvioSMSAtrasoPulaPulaProdutor(long logId)
	{
		super(logId, Definicoes.CL_ENVIO_SMS_ATRASO_PULA_PULA_PROD);

		this.conexaoPrep		= null;
		this.registros			= null;
		this.numRegistros		= 0;
		this.numEnviados		= 0;
		this.status				= Definicoes.PROCESSO_SUCESSO;
		this.mensagem			= "Numero de SMS's enviados: ";
		this.dataProcessamento	= null;
	}

	//Implementacao de Produtor.

    /**
     *	Inicia a execucao do processo de envio de SMS. O metodo obtem os registros que sao processados pelas threads
     *	consumidoras.
     *
     *	@param		params					Lista de parametros.
     *	@throws		Exception
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "startup", "Inicio");

		try
		{
		    //Obtendo a data de processamento.
		    Calendar calProcessamento = Calendar.getInstance();
		    this.dataProcessamento = calProcessamento.getTime();

		    //Obtendo datas de analise da consulta na fila de recargas para passagem de parametro. A consulta nao
		    //deve retornar assinantes que possuem registros validos de bonus na fila de recargas.
		    calProcessamento.set(Calendar.DAY_OF_MONTH, calProcessamento.getActualMinimum(Calendar.DAY_OF_MONTH));
		    Date dataInicio = calProcessamento.getTime();
		    calProcessamento.add(Calendar.MONTH, 1);
		    Date dataFim = calProcessamento.getTime();

		    //Executando a consulta pelos assinantes.
		    this.conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    Object[] parametros =
		    {
		        new Integer(Definicoes.CTRL_PROMOCAO_CATEGORIA_PULA_PULA),
		        new java.sql.Date(dataProcessamento.getTime()),
		        Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT,
		        new Integer(Definicoes.STATUS_RECARGA_NAO_PROCESSADA),
		        new Integer(Definicoes.STATUS_RECARGA_CONCLUIDA),
		        new Integer(Definicoes.STATUS_RECARGA_SMS_CONCLUIDOS),
		        new Integer(Definicoes.STATUS_RECARGA_TESTE_PULA_PULA),
		        new java.sql.Date(dataInicio.getTime()),
		        new java.sql.Date(dataFim.getTime())
		    };
		    this.registros = this.conexaoPrep.executaPreparedQuery(EnvioSMSAtrasoPulaPulaProdutor.SQL_SMS_ATRASO_PULA_PULA,
		                                                           parametros, super.logId);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "startup", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		    super.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		}
		finally
		{
		    super.log(Definicoes.INFO, "startup", "Fim");
		}
	}

    /**
     *	Retorna para as threads consumidoras o proximo registro a ser processado.
     *
     *	@return								MSISDN do assinante.
     *	@throws		Exception
     */
	public synchronized Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "next", "Inicio");

		try
		{
		    if(this.registros.next())
		    {
		        this.numRegistros++;

		        return this.registros.getString("idt_msisdn");
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "next", "Fim");
		}

		return null;
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
		    if(this.registros != null)
		    	this.registros.close();

		    if(this.numEnviados > 0)
		        this.notificarAtrasoPulaPula();

		    this.mensagem = this.mensagem.concat(String.valueOf(this.numEnviados));
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
     */
	public void handleException()
	{
	    this.status = Definicoes.PROCESSO_ERRO;
	    this.mensagem = "Excecao lancada por consumidor. " + this.mensagem;
	}

	//Implementacao de ProcessoBatchProdutor.

    /**
     *	Retorna o identificador do processo batch.
     *
     *	@return								Identificador do processo batch.
     */
	public int getIdProcessoBatch()
	{
	    return Definicoes.IND_ENVIO_SMS_ATRASO_PULA_PULA;
	}

    /**
     *	Retorna a mensagem informativa sobre a execucao do processo batch.
     *
     *	@return								Mensagem informativa sobre a execucao.
     */
	public String getDescricaoProcesso()
	{
	    return this.mensagem;
	}

    /**
     *	Retorna o status da execucao do processo.
     *
     *	@return								Status da execucao do processo.
     */
	public String getStatusProcesso()
	{
	    return this.status;
	}

    /**
     *	Atribui o status da execucao do processo.
     *
     *	@param		status					Status da execucao do processo.
     */
	public void setStatusProcesso(String status)
	{
	    this.status = status;
	}

    /**
     *	Retorna a data de processamento.
     *
     *	@return								Data de execucao no formato dd/mm/yyyy.
     */
	public String getDataProcessamento()
	{
	    return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(this.dataProcessamento);
	}

    /**
     *	Retorna a conexao PREP para os consumidores.
     *
     *	@return								Conexao PREP.
     */
	public PREPConexao getConexao()
	{
	    return this.conexaoPrep;
	}

	//Outros metodos.

    /**
     *	Notifica para o produtor a existencia de registro cujo SMS foi enviado.
     */
	public synchronized void notificarEnvioSMS()
	{
	    this.numEnviados++;
	}

    /**
     *	Notifica a existencia de atraso de consumo de recargas para o suporte.
     */
	private void notificarAtrasoPulaPula()
	{
	    try
	    {
	        //Obtendo os numeros de suporte e a mensagem de notificacao.
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
	        super.log(Definicoes.ERRO, "notificarAtrasoPulaPula", "Excecao: " + e);
	        super.log(Definicoes.WARN, "notificarAtrasoPulaPula", "Nao foi possivel enviar notificacao para o suporte");
	    }
	}

}