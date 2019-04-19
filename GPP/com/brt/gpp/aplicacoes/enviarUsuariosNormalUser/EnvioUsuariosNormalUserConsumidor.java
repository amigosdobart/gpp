package com.brt.gpp.aplicacoes.enviarUsuariosNormalUser;

import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.comum.xmlVitria.DadosXMLVitria;
import com.brt.gpp.comum.xmlVitria.GerarXMLVitria;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *
 * Este arquivo contem a definicao da classe de EnvioUsuariosNormalUserConsumidor.
 * Responsável pela processamento dos usuários que entraram no status NORMAL_USER vindos de RECHARGE_EXPIRED
 * de acordo com a Promocao ou o "Plano Preco"
 * 
 * @Autor:		Magno Batista Corrêa
 * @since:      20070712
 * @version:    1.0
 */
public class EnvioUsuariosNormalUserConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao           conexaoPrep;
	//Entradas na tabela TBL_GER_CONFIGURACAO_GPP
	private static final String   PROMOCOES_QUE_PERDEM_FGN            = "PROMOCOES_QUE_PERDEM_FGN";
	private static final String   PROMOCOES_QUE_RETORNAM_COM_FGN_NOVO = "PROMOCOES_QUE_RETORNAM_COM_FGN_NOVO";
	private static final String   PROMOCOES_DE_RETORNO_DO_FGN_NOVO    = "PROMOCOES_DE_RETORNO_DO_FGN_NOVO";
	private static final String   DELIMITADOR_CONFIGURACAO            = ";";
	// Transformacoes dos grupos de promocoes em array de inteiros para um melhor processamento
    private int[]                 promocoesQuePerdemFGN;
    private int[]                 promocoesQueRetornamComFGNnovo;
    private int[]                 promocoesDeRetornoDoFGNnovo;
	private ControlePulaPula      controlePulaPula;
	private ConsumidorSMS         consumidorSMS;

	/**
	 * Construtor
	 */
	public EnvioUsuariosNormalUserConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioUsuariosNormalUserConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_NORMAL_USER),Definicoes.CL_ENVIO_USUARIO_NORMAL_USER);
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
		super.log(Definicoes.INFO,"Consumidor.startup","Envio de Usuario no status NORMAL_USER com processamento por promoção ou plano." );
		this.conexaoPrep = this.produtor.getConexao();
        this.promocoesQuePerdemFGN          = this.montaArrayInteirosDeEntradaEmConfiguracao(
        										PROMOCOES_QUE_PERDEM_FGN,DELIMITADOR_CONFIGURACAO);
        this.promocoesQueRetornamComFGNnovo = this.montaArrayInteirosDeEntradaEmConfiguracao(
        										PROMOCOES_QUE_RETORNAM_COM_FGN_NOVO,DELIMITADOR_CONFIGURACAO);
        this.promocoesDeRetornoDoFGNnovo    = this.montaArrayInteirosDeEntradaEmConfiguracao(
        										PROMOCOES_DE_RETORNO_DO_FGN_NOVO,DELIMITADOR_CONFIGURACAO);
		//Confere a configuracao das promocoes
		if(this.promocoesQueRetornamComFGNnovo.length != this.promocoesDeRetornoDoFGNnovo.length)
		{
			String descricaoErro = "Falha na tabela TBL_GER_CONFIGURACAO_GPP Rever Configuracoes: " +
									PROMOCOES_QUE_RETORNAM_COM_FGN_NOVO + " e " +
									PROMOCOES_DE_RETORNO_DO_FGN_NOVO;
			super.log(Definicoes.ERRO,"espelhamentoPromocao", descricaoErro);
			throw new GPPInternalErrorException(descricaoErro);
		}

		this.controlePulaPula = new ControlePulaPula(super.logId);
		this.consumidorSMS = ConsumidorSMS.getInstance(super.logId);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		UsuariosNormalUserVO vo      = (UsuariosNormalUserVO)obj;

		int retornoDesbloqueio  = Definicoes.RET_ERRO_GENERICO_GPP;
		GerarXMLVitria geradorXML    = null;
		boolean inserirVitria        = false;
		boolean enviarSMS            = false;
		String tipoExecucao          = null;
		
		super.log(Definicoes.DEBUG,"Consumidor.execute","Processando o VO: " + vo );

		String msisdn                = vo.getMsisdn();
		AssinantePulaPula assinantePulaPula = null;
        //Estuda o caso de cada promocao ou Plano_de_Preco que deve ser tratado
        
        // O primeiro teste eh o caso de perder o FGN
		if (encontraInteiroEmArray(vo.getIdtPromocao(), this.promocoesQuePerdemFGN))
		{
			enviarSMS = true; // Envia SMS para todos os que perdem FGN
			tipoExecucao = Definicoes.FGN_REATIVACAO;//Seta o tipo do SMS
			
			assinantePulaPula = this.controlePulaPula.consultaPromocaoPulaPula(msisdn, 
																			   null, 
																			   false,
																			   false,
																			   this.conexaoPrep);
			// Seta o Status da Promocao
            int statusPromocaoAtual = assinantePulaPula.getStatus().getIdtStatus();
			int statusPromocaoNovo  = statusPromocaoAtual;

	        //Testa se precisa trocar a promocao
			if (encontraInteiroEmArray(vo.getIdtPromocao(), this.promocoesQueRetornamComFGNnovo))
			{
				int novaPromocao = vo.getIdtPromocao();
				// Encontra a nova promocao de retorno do assinante.
				novaPromocao = espelhamentoPromocao(vo.getIdtPromocao());
				this.controlePulaPula.trocaPulaPula( // Ignoro o retorno pois o Clarify irah tambem fazer a migracao
						msisdn,
						novaPromocao,
						new Timestamp(new Date().getTime()),
						Definicoes.GPP_OPERADOR,
						Definicoes.CTRL_PROMOCAO_MOTIVO_RECARGA_EFETUADA,
						this.conexaoPrep);
				
				// Montando o XML para o Vitria
				DadosXMLVitria dadosXML = new DadosXMLVitria(DadosXMLVitria.VERSAO_XML_VITRIA_003,
						 gerarXMLUsuarioNormalUser(vo),
						 DadosXMLVitria.ID_SISTEMA_GPP,
						 Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT,
						 vo.getData(),
						 vo.getMsisdn());
				geradorXML = new GerarXMLVitria(dadosXML,this.conexaoPrep, super.getIdLog());
				inserirVitria = true;
				
				vo.setIdtPromocao(novaPromocao);  //Atualiza a promocao no VO. Se nao o fizer, implica diretamente no
				// plano de preco do desbloquearFGN(vo)
			}

			// Se o Status da Promocao for: 
			//    SUSPENSO_RECARGA_EXPIRADA envia para ATIVO 
			//    e muda o Plano Preco
			// Se o Status da Promocao for:
			//    SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA envia para SUSPENSO_LIMITE_ALCANCADO
			switch(statusPromocaoAtual)
			{
				case PromocaoStatusAssinante.SUSPENSO_RECARGA_EXPIRADA:
					statusPromocaoNovo = PromocaoStatusAssinante.ATIVO;
					retornoDesbloqueio = this.desbloquearFGN(vo);
					break;
				case PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO_E_RECARGA_EXPIRADA:
					statusPromocaoNovo = PromocaoStatusAssinante.SUSPENSO_LIMITE_ALCANCADO;
					retornoDesbloqueio = Definicoes.RET_OPERACAO_OK;
					break;
				default: 
						;
			}

			// Conseguiu alterar a promocao do assinante na Tecnomen e o status precisa ser alterado
	        if(retornoDesbloqueio == Definicoes.RET_OPERACAO_OK && statusPromocaoNovo != statusPromocaoAtual)
	        {
	        	this.controlePulaPula.trocarStatusPulaPula(msisdn, statusPromocaoNovo, this.conexaoPrep);
	        }
		}

		//Insere o dado na tabela de interface de saida do vitria
		//A insercao estah condicionada a existencia do assinante na Plataforma Tecnomen
		if (inserirVitria && retornoDesbloqueio == Definicoes.RET_OPERACAO_OK)// Caso tenha ocorrido alguma falha externa
			                                         // pode reprocessar o assinante sem reenvio
		{
			int numLinhas = geradorXML.InserirXMLVitriaBanco();
			super.log(Definicoes.DEBUG,"execute","Usuario " + msisdn + 
					(numLinhas > 0 ? " incluido na interface VITRIA" : " nao incluido na interface VITRIA" ));
		}
		//enviar SMS
		if ( enviarSMS && retornoDesbloqueio == Definicoes.RET_OPERACAO_OK)
		{
			//Mesmo com a troca de promocao, o assinante (deve) continua(r) intacto
			boolean enviado = this.enviarSMS(assinantePulaPula, tipoExecucao);
			super.log(Definicoes.DEBUG,"execute","Usuario " + msisdn + 
					(enviado ? " Enviado SMS" : " nao Enviado SMS" ));
		}
	}
	
	/**
	 * Envia um SMS para um dado assinante informando a entrada em NormalUser
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
	 * Encontra a promocao Nova com base numa promocao de entrada
	 * @param idtPromocao
	 * @return
	 */
	private int espelhamentoPromocao(int idtPromocao)
	{
		int[] base = this.promocoesQueRetornamComFGNnovo;
		int[] resultados = this.promocoesDeRetornoDoFGNnovo;
		int size = base.length;
		for(int i = 0 ; i < size ; i++)
		{
			if (idtPromocao == base[i])
			{
				return resultados[i];
			}
		}
		return idtPromocao; //Caso encontra a promocao para ser espelhada, mantem a promocao
	}

	/**
	 * Modifica o Plano Preco do assinante para que ele nao FGN
	 * Troca a Promocao do Assinante, caso seja necessario
	 * e troca o Plano de Preco, caso seja necessario
	 * @param vo
	 * @return
	 */
	private short desbloquearFGN(UsuariosNormalUserVO vo)
	{
		// Cria uma nova instancia de Assinante para o processo de troca
        // de plano Fale Gratis para o Pula-Pula vigente
        Assinante assinante = new Assinante();
        assinante.setMSISDN(vo.getMsisdn());
        assinante.setPlanoPreco((short)vo.getIdtPlanoPreco());

        String tipoEspelhamento = Definicoes.CTRL_PROMOCAO_TIPO_ESPELHAMENTO_FGN_RETORNO + vo.getIdtPromocao();

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
	 * Metodo....:gerarXMLUsuarioNormalUser
	 * Descricao.:Monta o XML que sera armazenado na tabela de interface
	 *            resultante do processamento consumidor
	 * @param VO            - ValueObject contendo informacoes de Usuarios que entraram no status NormalUser
	 * @return String       - XML a ser gravado na tabela de interface
	 */
	private String gerarXMLUsuarioNormalUser(UsuariosNormalUserVO vo)
	{
		GerarXML geradorXML = new GerarXML("GPPStatus");
		// Criação das Tags Defaults do XML
		geradorXML.adicionaTag("msisdn",vo.getMsisdn());
		geradorXML.adicionaTag("status",Definicoes.S_RET_FGN1_FGN2);
		geradorXML.adicionaTag("data"  ,vo.getData());

		//Retorno do XML completo
		return GerarXML.PROLOG_XML_VERSAO_1_0_UTF8 + geradorXML.getXML(); 
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
		super.log(Definicoes.INFO,"Consumidor.finish","Envio de Usuario no status NORMAL_USER com processamento por promoção ou plano." );
	}
}
