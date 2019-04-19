package com.brt.gpp.aplicacoes.enviarUsuariosRechargeExpired;

import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *
 * Este arquivo contem a definicao da classe de EnvioUsuariosRechargeExpiredConsumidor.
 * Responsável pela envio dos usuários que entraram no status RECHARGE_EXPIRED
 * e o processamento de acordo com a Promocao ou o "Plano Preco"
 * 
 * @Autor:		Magno Batista Corrêa
 * @since:      11/04/2007
 * @version:    1.0
 */
public class EnvioUsuariosRechargeExpiredConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao           conexaoPrep;
    private int[]                 promocoesQuePerdemFGN;
	private static final String   PROMOCOES_QUE_PERDEM_FGN            = "PROMOCOES_QUE_PERDEM_FGN";
	private static final String   DELIMITADOR_CONFIGURACAO            = ";";
	private ControlePulaPula      controlePulaPula;
	private ConsumidorSMS         consumidorSMS;

	/**
	 * Construtor
	 */
	public EnvioUsuariosRechargeExpiredConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioUsuariosRechargeExpiredConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_RECHARGE_EXPIRED),Definicoes.CL_ENVIO_USUARIO_RECHARGE_EXPIRED);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor)produtor);
	}	

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = produtor;
        startup();
    }

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		super.log(Definicoes.INFO,"Consumidor.startup","Envio de Usuario no status RECHARGE_EXPIRED com processamento por promoção ou plano." );
		this.conexaoPrep = this.produtor.getConexao();
        this.promocoesQuePerdemFGN = this.montaArrayInteirosDeEntradaEmConfiguracao(PROMOCOES_QUE_PERDEM_FGN,DELIMITADOR_CONFIGURACAO);

        this.controlePulaPula = new ControlePulaPula(super.logId);
		this.consumidorSMS = ConsumidorSMS.getInstance(super.logId);

	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		UsuariosRechargeExpiredVO vo = (UsuariosRechargeExpiredVO)obj;
		int retornoBloqueio = Definicoes.RET_OPERACAO_OK;
		
		boolean enviarSMS            = false;
		String tipoExecucao          = null;

		super.log(Definicoes.DEBUG,"Consumidor.execute","Processando o VO: " + vo );

		String msisdn = vo.getMsisdn();
		AssinantePulaPula assinantePulaPula = null;

		//Estuda o caso de cada promocao ou Plano_de_Preco que deve ser tratado
        
        // O primeiro teste eh o caso de perder o FGN
		if (encontraInteiroEmArray(vo.getIdtPromocao(), this.promocoesQuePerdemFGN))
		{
			enviarSMS = true; // Envia SMS para todos os que perdem FGN
			tipoExecucao = Definicoes.SMS_FGN_EXPIRACAO;//Seta o tipo do SMS
            assinantePulaPula = this.controlePulaPula.consultaPromocaoPulaPula(msisdn, 
            																   null, 
            																   false, 
            																   false,
            																   this.conexaoPrep);
			// Seta o Status da Promocao
            int statusPromocaoAtual = assinantePulaPula.getStatus().getIdtStatus();
			int statusPromocaoNovo  = statusPromocaoAtual;

			// Se o Status da Promocao for: 
			//    ATIVO ou PENDENTE_RECARGA envia para SUSPENSO_RECARGA_EXPIRADA
			//    e muda o Plano Preco
			// Se o Status da Promocao for:
			//    SUSPENSO_LIMITE_ALCANCADO envia para SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA
			switch(statusPromocaoAtual)
			{
				case PromocaoStatusAssinante.ATIVO:
				case PromocaoStatusAssinante.PENDENTE_RECARGA:
					statusPromocaoNovo = PromocaoStatusAssinante.SUSPENSO_RECARGA_EXPIRADA;
					retornoBloqueio = this.bloquearFGN(vo);
					break;
				case PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO:
					statusPromocaoNovo = PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA;
					break;
				default: 
						;
			}

			// Conseguiu alterar a promocao do assinante na Tecnomen e o status precisa ser alterado
            if(retornoBloqueio == Definicoes.RET_OPERACAO_OK && statusPromocaoNovo != statusPromocaoAtual)
            {
            	this.controlePulaPula.trocarStatusPulaPula(msisdn, statusPromocaoNovo, this.conexaoPrep);
            }
		}
		//enviar SMS
		if ( enviarSMS && retornoBloqueio == Definicoes.RET_OPERACAO_OK)
		{
			//Mesmo com a troca de promocao, o assinante (deve) continua(r) intacto
			boolean enviado = this.enviarSMS(assinantePulaPula,tipoExecucao);
			super.log(Definicoes.DEBUG,"execute","Usuario " + msisdn + 
					(enviado ? " Enviado SMS" : " nao Enviado SMS" ));
		}
	}

	/**
	 * Envia um SMS para um dado assinante informando a entrada em RechargeExpired
	 * @param assinantePulaPula
	 * @throws Exception
	 */
	private boolean enviarSMS(AssinantePulaPula assinantePulaPula, String tipoExecucao) throws Exception
	{
		boolean retorno = false;
		String msisdn = assinantePulaPula.getIdtMsisdn();
        PromocaoInfosSms infosSms = 
        	assinantePulaPula.getInfosSms(tipoExecucao).iterator().hasNext() ?
        		(PromocaoInfosSms)assinantePulaPula.getInfosSms(tipoExecucao).iterator().next() : null;
		String mensagem = null;
		int prioridade  = 0;
		String tipo     = null;
        if((infosSms != null) && (infosSms.enviaSms()))
        {
            tipo        = infosSms.getTipSms();
            prioridade  = infosSms.getNumPrioridade();
            mensagem    = infosSms.getDesMensagem();

            retorno = this.consumidorSMS.gravarMensagemSMS(msisdn, mensagem, prioridade, tipo, this.conexaoPrep);
        }

        return retorno;
	}
	

/**
	 * Modifica o Plano de Preco de um assinante para  
	 * @param vo
	 * @return
	 */
	private short bloquearFGN(UsuariosRechargeExpiredVO vo)
	{
		// Cria uma nova instancia de Assinante para o processo de troca
        // de plano Fale Gratis para o Pula-Pula vigente
        Assinante assinante = new Assinante();
        assinante.setMSISDN(vo.getMsisdn());
        assinante.setPlanoPreco((short)vo.getIdtPlanoPreco());

        String tipoEspelhamento = Definicoes.CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN + vo.getIdtPromocao();

        // Realiza uma chamada ao metodo de Troca de Plano, utilizando para tanto
        // o controle pelo Espelhamento, que eh o que define para qual plano o 
        // assinante deve ser 'migrado'. O processo de troca de plano espelho ja
        // realiza a atualizacao do objeto Assinante com o novo Plano de Preco
        short retorno = this.controlePulaPula.trocarPlanoEspelho(assinante, 
        														 tipoEspelhamento,
																 (short)-1,
																 new Date(),
																 this.conexaoPrep);
        
        return retorno;
	}
	
	/**
     * Retorna um array de inteiros de uma entrada na Configuracao
	 * @param entradaConfiguracao 
	 * @param delimitador 
     * @return Array contendo as promocoes
	 * @throws GPPInternalErrorException 
	 */
    private int[] montaArrayInteirosDeEntradaEmConfiguracao(String entradaConfiguracao, String delimitador) throws GPPInternalErrorException
    {
        MapConfiguracaoGPP mapConfiguracaoGPP = MapConfiguracaoGPP.getInstancia();
        String entrada = (mapConfiguracaoGPP.getMapValorConfiguracaoGPP(entradaConfiguracao));
        String[] promocoes = entrada.split(delimitador);
        int size = promocoes.length;
        int[] saida = new int[size];
        for (int i = 0 ; i < size ; i++)
        {
            saida[i] = Integer.parseInt(promocoes[i]);
        }
        return saida;
    }

    /**
     * Encontra um dado inteiro em um array de inteiros
     * @param procurado  Inteiro procurado
     * @param lista      Lista de inteiros onde deve-se procurar o inteiro
     * @return           <code>true</code> se encontrou
     */
    private boolean encontraInteiroEmArray(int procurado, int[] lista)
    {
        boolean retorno = false;
        int size = lista.length;
        for (int i = 0 ; i < size && retorno == false ; i++)
        {
            retorno = (procurado == lista[i]);
        }
        return retorno;
    }
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		super.log(Definicoes.INFO,"Consumidor.finish","Envio de Usuario no status RECHARGE_EXPIRED com processamento por promoção ou plano." );
	}
}
