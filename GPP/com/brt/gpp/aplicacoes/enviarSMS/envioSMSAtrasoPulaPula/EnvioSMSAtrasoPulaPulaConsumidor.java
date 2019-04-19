package com.brt.gpp.aplicacoes.enviarSMS.envioSMSAtrasoPulaPula;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pelo consumo e execucao do processo de envio de SMS para atraso de concessao de bonus Pula-Pula.
 *
 *	@author	Daniel Ferreira
 *	@since	22/05/2006
 */
public class EnvioSMSAtrasoPulaPulaConsumidor extends ControlePulaPula implements ProcessoBatchConsumidor
{

    //Atributos.

    /**
     *	Produtor do processo batch.
     */
    private ProcessoBatchProdutor produtor;

    /**
     *	Conexao com o Middleware de SMS.
     */
    private ConsumidorSMS consumidorSMS;

    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;

    /**
     *	Mensagem a ser enviada ao assinante.
     */
    private String mensagem;

    /**
     *	Data de processamento.
     */
    private Date dataReferencia;

    /**
     *	Formatador do valor de bonus.
     */
    private static final DecimalFormat conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE, new DecimalFormatSymbols(new Locale("pt", "BR")));

    /**
     *	Formatador da data de referencia.
     */
    private static final SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);

    //Construtores.

    /**
     *	Construtor da classe.
     */
	public EnvioSMSAtrasoPulaPulaConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioSMSAtrasoPulaPulaConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_SMS_ATRASO_PULA_PULA_PROD),
		      Definicoes.CL_ENVIO_SMS_ATRASO_PULA_PULA_CONS);

		this.consumidorSMS	= null;
		this.conexaoPrep	= null;
		this.dataReferencia	= null;
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
     *	@return		Object					obj							MSISDN do assinante a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");

		try
		{
		    //Obtendo as informacoes para a consulta da promocao Pula-Pula do assinante.
		    String msisdn = (String)obj;
            int[] detalhes =
            {
                ControlePulaPula.DATAS_CREDITO,
                ControlePulaPula.TOTALIZACAO,
                ControlePulaPula.BONUS_PULA_PULA,
                ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
                ControlePulaPula.BONUS_AGENDADOS_PERIODO,
                ControlePulaPula.SALDO_PULA_PULA
            };
		    //Executando a consulta da promocao Pula-Pula do assinante.
            AssinantePulaPula pAssinante = super.consultaPromocaoPulaPula(msisdn, 
            															  detalhes, 
            															  this.dataReferencia, 
            															  false, 
            															  false, 
            															  this.conexaoPrep);

            //Validando o assinante. Se o assinante for validado e ainda nao tiver recebido o bonus, deve receber
            //o SMS de notificacao. A confirmacao do nao recebimento do bonus pode ser feita verificando se as
            //colecoes de bonus agendados e concedidos do assinante, obtidas apos a consulta Pula-Pula, estao vazias.
            //Para evitar envios desnecessarios por problemas nas datas de execucao do assinante, o assinante recebera
            //somente se a data de referencia coincidir com a data de credito do bonus. Para isto e necessario o
            //truncamento da data de credito, uma vez que possui informacao de hora.
            String tipoExecucao = Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT;
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
            if((super.validaAssinante(tipoExecucao, pAssinante, this.dataReferencia) == Definicoes.RET_OPERACAO_OK) &&
               (pAssinante.getBonusAgendados(tipoExecucao).size() == 0) &&
               (pAssinante.getBonusConcedidos(tipoExecucao).size() == 0))
            {
                Date dataCredito = conversorDate.parse(conversorDate.format(pAssinante.getDataCredito(tipoExecucao)));
            	if (dataCredito.equals(this.dataReferencia))
            	{
                    //Configurando a mensagem a ser enviada ao assinante.
                    String bonus = EnvioSMSAtrasoPulaPulaConsumidor.conversorDouble.format(pAssinante.getSaldo().getValorAReceber());
                    String mensagem = this.mensagem.replaceAll(Definicoes.PATTERN_VALOR, bonus);
                    //Enviando a mensagem ao assinante.
            		boolean gravou = this.consumidorSMS.gravaMensagemSMS(msisdn,
            				mensagem, Definicoes.SMS_PRIORIDADE_ZERO,
            				Definicoes.SMS_ATRASO_PULAPULA, this.conexaoPrep, super.logId);
                    if (gravou)//Notificando o produtor sobre o envio do SMS.
                         ((EnvioSMSAtrasoPulaPulaProdutor)this.produtor).notificarEnvioSMS();
                    else
                    	super.log(Definicoes.ERRO, "execute", "Info SMS nao gravada na tabela MSISDN: "+msisdn);
            	}
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
	    this.produtor = produtor;
	    this.consumidorSMS = ConsumidorSMS.getInstance(super.logId);
	    this.conexaoPrep = produtor.getConexao();
	    this.mensagem = MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("ENVIO_SMS_ATRASO_PULA_PULA_MENSAGEM");
	    this.dataReferencia = EnvioSMSAtrasoPulaPulaConsumidor.conversorDate.parse(produtor.getDataProcessamento());
	}

}