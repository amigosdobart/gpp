package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas;

//Imports Java.

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

//Imports GPP.
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas.AtrasoRecarga;
import com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoRecargas.SelecaoSMSAtrasoRecargas;
import com.brt.gpp.aplicacoes.planoHibrido.RecargaRecorrenteConsumidor;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pelo consumo e execucao do processo de envio de SMS para atraso no consumo de recargas. Caso
 *	haja alguma recarga com pendencia de processamento, deve ser enviado um SMS ao assinantes notificando que a
 *	recarga esta em processamento.
 *
 *	@author	Daniel Ferreira
 *	@since	17/03/2006
 */
public class EnvioSMSAtrasoRecargasConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{

    private PREPConexao					conexaoPrep;
    private Consulta					consulta;
    private DecimalFormat				conversorDouble;
    private Map							mensagens;
    private RecargaRecorrenteConsumidor	validadorControle;
    private EnvioSMSDAO					envioSMSDAO;

    //Construtores.

    /**
     *	Construtor da classe.
     */
	public EnvioSMSAtrasoRecargasConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioSMSAtrasoRecargasConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_SMS_ATRASO_REC_PROD),
		      Definicoes.CL_ENVIO_SMS_ATRASO_REC_CONS);

		this.conexaoPrep		= null;
		this.consulta			= null;
		this.conversorDouble	= null;
		this.mensagens			= null;
		this.validadorControle	= null;
	}

	//Implementacao de Consumidor.

    /**
     *	Inicializa o objeto. Nao utilizado.
     *
     *	@throws		Exception
     */
	public void startup() throws Exception
	{
	}

    /**
     *	Inicializa o objeto. Nao utilizado.
     *
     *	@param		produtor				Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(Produtor produtor) throws Exception
	{
	}

    /**
     *	Executa o processo de Gerenciamento de assinantes com status Recharge Expired em promocao Pula-Pula.
     *
     *	@return		obj						MSISDN do assinante a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");

		try
		{
            //Validando o atraso do consumo da recarga. Se a recarga ainda estiver atrasada e o assinante for validado,
		    //deve ser enviado um SMS de notificacao para o assinante.
		    AtrasoRecarga atraso = (AtrasoRecarga)obj;

            if(this.validaAtraso(atraso))
            {
                //Configurando a mensagem a ser enviada ao assinante.
                String mensagem = (String)this.mensagens.get(atraso.getTipTransacao());
                mensagem = mensagem.replaceAll(Definicoes.PATTERN_VALOR, this.conversorDouble.format(atraso.getVlrRecarga()));

                //Enviando a mensagem ao assinante.
                DadosSMS sms = new DadosSMS();
                sms.setIdtMsisdn(atraso.getIdtMsisdn());
                sms.setDesMensagem(mensagem);
                sms.setNumPrioridade(0);

                envioSMSDAO.inserirSMS(sms);
            }
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}

    /**
     *	Finaliza a execucao do processo batch. Nao utilizado pelo Estorno de Bonus Pula-Pula por Fraude.
     */
	public void finish()
	{
	}

	//Implementacao de ProcessoBatchConsumidor.

    /**
     *	Inicializa o objeto.
     *
     *	@param		produtor				Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	    this.conexaoPrep = produtor.getConexao();
	    this.consulta = new Consulta(super.logId);
	    this.conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR")));
	    this.mensagens = this.parse(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("ENVIO_SMS_ATRASO_RECARGAS_MENSAGEM"));
	    this.validadorControle = new RecargaRecorrenteConsumidor();
	    this.envioSMSDAO = new EnvioSMSDAO(produtor.getIdProcessoBatch(), conexaoPrep);
	}

	//Outros metodos.

    /**
     *	Valida se o assinante deve mudar para o status Disconnected.
     *
     *	@param		atraso					Informacoes referentes a recarga com atraso.
     *	@return								True se o atraso foi validado ou false caso contrario.
     *	@throws		Exception
     */
	private boolean validaAtraso(AtrasoRecarga atraso) throws Exception
	{
	    if(atraso.getIdtOrigem().equals(Definicoes.PATTERN_CONTROLE))
	    {
	        Assinante assinante = this.consulta.getAssinanteGPP(atraso.getIdtMsisdn(), false, this.conexaoPrep);
	        if(this.validadorControle.validaAssinante(assinante, atraso.getVlrRecarga(), Integer.parseInt(atraso.getCodRecarga())) != Definicoes.RET_OPERACAO_OK)
	        {
	            return false;
	        }
	    }

	    return SelecaoSMSAtrasoRecargas.confirmaAtraso(atraso, this.conexaoPrep);
	}

    /**
     *	Interpreta a configuracao do GPP referente as mensagens de SMS a serem enviadas aos assinantes.
     *
     *	@param		configuracao			Configuracao do GPP contendo as mensagens.
     *	@return								Mensagens a serem enviadas de acordo com o tipo de transacao da recarga.
     *	@throws		Exception
     */
	private Map parse(String configuracao) throws Exception
	{
	    TreeMap result = new TreeMap();

	    String[] tiposTransacao = configuracao.split("#");

	    for(int i = 0; i < tiposTransacao.length; i++)
	    {
	        String[] mensagem = tiposTransacao[i].split("::");
	        result.put(mensagem[0], mensagem[1]);
	    }

	    return result;
	}

}