package br.com.brasiltelecom.wig.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Possui as configurações do WIG.
 * 
 * @author Joao Paulo Galvagni
 * @since 19/05/2006
 */
public class Definicoes
{
	// Informacoes de XML
	public static final String WIG_XML_PROLOG 		 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String WIG_XML_EMPRESA		 = "BRG";
	public static final String WIG_XML_SISTEMA		 = "SIB";
	public static final String WIG_XML_COD_OPERADORA = "0000";
	public static final String WIG_XML_PROCESSO_CLY  = "REL000CONCLIENT";
	public static final String WIG_XML_PROCESSO_SAC  = "REL000CONCTOBRV";
	public static final String WIG_XML_PROCESSO_OS   = "REL051GERAOS";
	
	// Codigos de retorno do OptIn
	public static final int WIG_RET_OK					  = 0;
	public static final int WIG_RET_CADASTRADO		  	  = 1;
	public static final int WIG_RET_CADASTRADO_FIDELIZADO = 2;
	public static final int WIG_RET_NAO_CADASTRADO 		  = 3;
	public static final int WIG_RETORNO_ERRO_TECNICO	  = 99;
	
	// Codigos das categorias do OptIn
	public static final int WIG_QTDE_CATEGORIAS_PAI			= 6;
	public static final int WIG_ID_CATEGORIA_CD_DVD_LIVROS	= 1;
	public static final int WIG_ID_CATEGORIA_MODA_MASCULINA = 2;
	public static final int WIG_ID_CATEGORIA_MODA_FEMININA	= 3;
	public static final int WIG_ID_CATEGORIA_ELETRONICOS	= 4;
	public static final int WIG_ID_CATEGORIA_SHOWS_EVENTOS	= 5;
	public static final int WIG_ID_CATEGORIA_BARES_REST		= 6;

	// Codigo de retorno OK e respectiva mensagem de sucesso (SMS)
	// e mensagem de alerta de assinante ja cadastrado
	public static final String WIG_RET_OK_STR			  = "00";
	public static final String WIG_SMS_OPTIN_SUCESSO	  = "Bem vindo ao canal de promocoes. Para escolher ou modificar suas preferencias" +
	" ou se desejar desativar va ao Menu BrT GSM e selecione promocoes e ofertas gratis.";
	public static final String WIG_MSG_OPTIN_NAO_CADASTRADO = "Voce ainda nao esta cadastrado, quer se cadastrar agora? Click OK.\n";
	public static final String WIG_MSG_OPTIN_JA_CADASTRADO = "Voce ainda nao esta cadastrado, quer se cadastrar agora? Click OK.\n";
	
	// Elementos do Clarify e status do acesso
	public static final String WIG_STATUS_ATIVO_CLARIFY 	= "Ativo";
	public static final String WIG_STATUS_PENDENTE_CLARIFY 	= "Pendente";
	public static final String WIG_ELM_NOVISSIMO_ATH 		= "ELM_AMGS_TDHORA_V02_ID1885";
	public static final String WIG_ELM_NOVO_ATH		 		= "ELM_G14_ID0990";
	public static final String WIG_ELM_NOVISSIMO_BTM		= "ELM_IRMAO14_ID1014";
	public static final String WIG_ELM_NOVISSIMO_B14		= "ELM_TOMALADACA_ID1015";
	public static final String WIG_PLANO_PREPAGO_CLY		= "Pre-pago";
	public static final String WIG_PLANO_POSPAGO_CLY		= "Pos-pago";
	public static final String WIG_PLANO_CONTROLE_CLY		= "Hibrido";
	
	// Validacoes do Novissimo Amigos Toda Hora
	public static final int ATH_RET_OK				= 0;
	public static final int ATH_RET_CONSULTA 		= 1;
	public static final int ATH_RET_NUMERO_INVALIDO = 10;
	
	// Validacoes do Bonus Todo Mes
	public static final int BTM_RET_OK				= 0;
	public static final int BTM_RET_CONSULTA		= 1;
	public static final int BTM_RET_NUMERO_INVALIDO = 10;
	public static final int BTM_RET_CN_DIFERENTE	= 99;
	
	// Validacoes do Bumerangue14
	public static final int B14_RET_OK			 = 0;
	public static final int B14_RET_NULO		 = 1;
	public static final int B14_RET_PENDENTE_CLY = 2;
	public static final int B14_RET_JA_ATIVO	 = 3;
	
	// Categorias de plano no GPP
	public static final int WIG_CATEGORIA_PREPAGO  = 0;
	public static final int WIG_CATEGORIA_CONTROLE = 1;
	public static final int WIG_CATEGORIA_POSPAGO  = 3;
	
	// Codigo de retorno do GPP e alteracoes / cobranca
	public static final String WIG_RET_GPP_OK	= "0000";
	
	// Acoes relacionadas as alteracoes do Brasil Vantagens no Clarify
	public static final String GPP_ACAO_ATUALIZACAO_ATH 		= "ATUALIZA_ATH";
	public static final String GPP_ACAO_COBRANCA_BRT_VANTAGENS	= "COBRAR_SERVICO";
	public static final String GPP_ACAO_CADASTRO_BUMERANGUE 	= "ATIVAR_BUMERANGUE_14";
	
	// Regras do Novo Brasil Vantatens
	public static final String SAC_REGRA_ATH = "ATH";
	public static final String SAC_REGRA_BTM = "BTM";
	
	// Acoes a serem tomada com relacao a atualizacoes no Brasil Vantagens
	public static final String CLY_ACAO_ALTERACAO 	= "Modificar";
	public static final String CLY_ACAO_INCLUSAO 	= "Adicionar";
	public static final String CLY_ACAO_EXCLUSAO 	= "Remover";
	
	// Operacoes possiveis nas acoes do GPP
	public static final String WIG_OPERACAO_DEBITO		= "D";
	public static final String WIG_OPERACAO_ESTORNO		= "E";
	public static final String WIG_OPERACAO_CONSULTA	= "C";
	
	// Mensagens de retorno do SAC de acordo com os respectivos codigos
	public static Map mensagemSAC;
	static
	{
		mensagemSAC = new HashMap();
		mensagemSAC.put(new Integer(0),"");
		mensagemSAC.put(new Integer(9), "Inclusao nao autorizada - Telefone informado e de uso Publico.");
		mensagemSAC.put(new Integer(10), "Inclusao nao autorizada para o numero informado.");
		mensagemSAC.put(new Integer(11), "Inclusao nao autorizada - Telefone informado nao e da BRT.");
		mensagemSAC.put(new Integer(13), "O telefone fixo indicado ultrapassou o limite de 75 pulsos. Indique outro numero.");
		mensagemSAC.put(new Integer(99), "O numero a ser cadastrado devera ser um numero de terminal fixo e de mesmo DDD.");
	}
	
	// Parametros padroes para atualizacao de simcard via SASC / WSM
	public static final int SASC_SM_VALIDATION_PERIOD = 3700;
	public static final int SASC_NOTIFICATION_TYPE	  = 0;
	public static final int SASC_SMSC_CONNECTION	  = 0;
	public static final int SASC_SMSC_ID			  = 0;
	
	// Status de atualizacao de simcards
	public static final int SASC_STATUS_REGISTRO_INCLUIDO 	   = 1;
	public static final int SASC_STATUS_AGUARDANDO_ATUALIZACAO = 2;
	public static final int SASC_STATUS_SIMCARD_ATUALIZADO	   = 3;
	public static final int SASC_STATUS_ATUALIZACAO_FALHOU 	   = 4;
	
	// Separador do campo endereco para o Cadastro Pre-Pago
	public static final String CAD_PRE_SEPARADOR 	 = "|";
	public static final String CAD_PRE_DOCUMENTO_CPF = "CPF";
	public static final String CAD_PRE_DOCUMENTO_RG	 = "RG";
}


