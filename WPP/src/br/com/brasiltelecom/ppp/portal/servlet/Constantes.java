package br.com.brasiltelecom.ppp.portal.servlet;

/**
 * Possui todas as configurações do portal. É utilizado para gerar nomes de variáveis de ambientes.
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 */
public interface Constantes
{
	public final String JDO = "br.com.brasiltelecom.ppp.portal.jdo";
	public final String MAPPING = "br.com.brasiltelecom.ppp.portal.mapping";
	public final String USUARIO = "br.com.brasiltelecom.ppp.portal.usuario";
	public final String LOGIN = "br.com.brasiltelecom.ppp.portal.login";
	public final String PARAMETRO = "br.com.brasiltelecom.ppp.portal.home.parametro";
	public final String OPERACOES = "br.com.brasiltelecom.ppp.portal.home.operacoes";
	public final String GRUPOS = "br.com.brasiltelecom.ppp.portal.home.grupos";
	public final String HIBERNATE = "br.com.brasiltelecom.ppp.portal.hibernate";

	public final String MENU_VERTICAL = "MENU_VERTICAL";
	public final String AUTENTICADOR="br.com.brasiltelecom.ppp.portal.autenticador";
	public final String LOGIN_HEADER="X-USUARIO";
	public final String GRUPO_HEADER="X-PERFIL";
	public final String GPP_NOME_SERVIDOR="GPPNomeServidor";
	public final String GPP_PORTA_SERVIDOR="GPPPortaServidor";
	public final String GPP_PERIODO_PING="GPPPeriodoPing";
	public final String INTEGRACAO_ICHAIN="br.com.brasiltelecom.ppp.portal.integracaoiChain";

	public final String REPORTS_NOME_SERVIDOR="ReportsNomeServidor";
	public final String REPORTS_PORTA_SERVIDOR="ReportsPortaServidor";

	public final String CLIENTE_POSSUI_SALDO="CRED=S";
	public final String CLIENTE_SEM_SALDO	="CRED=N";

	// Controle de Versão
	public final String TIPO_DEPLOY="TipoDeploy";
	public final String VERSAO_DEPLOY="VersaoDeploy";
	public final String BUILD_DEPLOY="BuildDeploy";

	//Constans da consulta de Boomerang
	public static final int DIA_INICIO_BOOMERANG = 28;
	public static final int DIA_FIM_BOOMERANG = 27;

	// Constantes Vitria
	public final String VITRIA_NOME_SERVIDOR="VitriaNomeServidor";
	public final String VITRIA_PORTA_SERVIDOR="VitriaPortaServidor";
	public final String	VITRIA_TIMEOUT_SERVIDOR="VitriaTimeoutServidor";
	public static final int VITRIA_NUM_TENTATIVAS = 5;
	public static final int VITRIA_TEMPO_ESPERA = 1000;
	public static final String VITRIA_TAG_NUM_BS = "numeroBS";
	public static final String VITRIA_TAG_ROOT = "root";

	// Constante servidor de email
	public final String SERVIDOR_SMTP="br.com.brasiltelecom.ppp.portal.servidorSmtp";
	public final String CONTA_EMAIL="br.com.brasiltelecom.ppp.portal.contaEmail";

	// Constantes para Contestacao
	public final String EVENTO_NEGOCIO_CONTESTACAO_PPPOUT	= "WPP_crItemBS";

	//Constantes de geracao de Comprovante de Servicos
	public static final String DIRETORIO_COMPROVANTES = "diretorioComprovantes";

	// Classes de Servico SFA
	public final String  CLASSE_SFA_RECARGAS = "0";
	public final String  CLASSE_SFA_AJUSTES  = "1";
	public final String  CLASSE_SFA_CONSUMO  = "2";
	public final String  CLASSE_SFA_SALDOS   = "3";

	// Operações Contingencia
	public final String  CONTINGENCIA_BLOQUEIO_ROUBO				= "00";
	public final String  CONTINGENCIA_BLOQUEIO_PERDA_EXTRAVIO		= "01";
	public final String  CONTINGENCIA_BLOQUEIO_PEDIDO_JUDICIAL  	= "02";
	public final String  CONTINGENCIA_DESATIVACAO_HOTLINE   		= "03";
	// Status Contingencia
	public final String  CONTINGENCIA_BLOQUEIO_CONFIRMADO			= "BC";
	public final String  CONTINGENCIA_BLOQUEIO_SOLICITADO			= "BS";
	public final String  CONTINGENCIA_BLOQUEIO_OK					= "Ok";

	// Constantes para o Brasil Vantagens GPP x SIB
	public static final String GPP_ACAO_ATUALIZACAO_ATH 		= "ATUALIZA_ATH";
	public static final String GPP_ACAO_COBRANCA_BRT_VANTAGENS	= "COBRAR_SERVICO";
	public static final String GPP_ACAO_CADASTRO_BUMERANGUE 	= "ATIVAR_BUMERANGUE_14";

	// Constantes referentes a XML
	public static final String XML_PRO_LOG 				 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String WIG_XML_EMPRESA		 	 = "BRG";
	public static final String WIG_XML_SISTEMA		 	 = "SIB";
	public static final String WIG_XML_PROCESSO_GPP  	 = "RELBRVALTTARBTM";
	public static final String WIG_XML_COD_ERRO_GENERICO = "-9999";
	public static final String WIG_XML_ERRO_GENERICO 	 = "Erro inesperado.";

	// Constantes para definicao de acesso ao servidor LDAP
	public final String LDAP_NOME_SERVIDOR 				= "LDAPNomeServidor";
	public final String LDAP_PORTA_SERVIDOR 			= "LDAPPortaServidor";
	public final String LDAP_NOME_DOMAIN 				= "LDAPNomeDomain";
	public final String LDAP_DOMAIN_PASSWORD 			= "LDAPDomainPassword";
	public final String LDAP_BASE_SEARCH 				= "LDAPBaseSearch";
	public final String LDAP_NOME_ARQUIVO_CERTIFICADO	= "LDAPNomeArquivoCertificado";

	// Constantes para definicao de acesso ao servidor WIG
	public final String WIG_NOME_SERVIDOR 				= "WIGNomeServidor";
	public final String WIG_PORTA_SERVIDOR 				= "WIGPortaServidor";

	public final String TELA = "TELA";
	public final String ACAO = "ACAO";
	public final String SHOW = "SHOW";
	public final String MENSAGEM = "mensagem";
	public final String RESULT = "result";

	public static final String DATA_HORA_FORMATO		= "dd/MM/yyyy HH:mm:ss";
	public static final String DATA_FORMATO				= "dd/MM/yyyy";
	public static final String MES_FORMATO				= "MMM/yyyy";
	public static final String MES_FORMATO_CURTO		= "MM/yyyy";
	public static final String DAT_MES_FORMATO			= "yyyyMM";
	public static final String DOUBLE_FORMATO			= "##,##0.00";
	public static final String CODIGO_RETORNO_FORMATO	= "0000";

	//Constantes para montagem de url com passagem de parâmetros
	public static final String URL_SEPARADOR			= "&";
	public static final String URL_SEPARADOR_MAIS		= "+";
	public static final String URL_ATRIBUIDOR			= "=";

	public final int PROBLEMA_BANCO_DADOS = -500;
	public final int OK = 1;

	public final int USUARIO_LOGADO_COM_SUSCESSO = 0;
	public final int USUARIO_SENHA_INVALIDA = 1;
	public final int USUARIO_DATA_VALIDADE_EXPIRADA = 2;
	public final int USUARIO_BLOQUEADO = 3;
	public final int USUARIO_NAO_CADASTRADO = 4;
	public final int USUARIO_CADASTRADO_COM_SUCESSO = 5;
	public final int USUARIO_CRIADO_COM_SUCESSO = 6;
	public final int USUARIO_SENHA_EXPIRADA=7;
	public final int USUARIO_SENHA_JA_CADASTRADA=8;
	public final int USUARIO_TAMANHO_SENHA_INVALIDO=9;
	public final int EMPRESA_CADASTRADA_COM_SUCESSO=10;
	public final int EMPRESA_CRIADA_COM_SUCESSO=11;

	public final String PARAM_NUM_DIAS_EXPIRACAO = "LISTA_DIAS_EXPIRACAO_PORTAL";
	public final String TIPO_CREDITO_PRINCIPAL	 = "00";
	public final String TIPO_CREDITO_BONUS	 	 = "01";
	public final String TIPO_CREDITO_SMS	     = "02";
	public final String TIPO_CREDITO_DADOS	     = "03";

	public final String PLANO_PRECO_BASICO_POS_PAGO = "20";

	//Chave do arquivo de propriedades com o caminho do diretório para upload
	public final String KEY_DIRETORIO_UPLOAD = "diretorio.path";

	//grupo de pessoas responsáveis pelo recebimento dos emails do Fale Conosco
	public final String FALECONOSCO = "gnf@brasiltelecom.com.br";

	//Produção
	public final String IPSERVIDOREMAIL = "poarelay01.brasiltelecom.com.br";//IP (10.61.241.4)

	//Homologação
	//public	final String IPSERVIDOREMAIL = "bsbrelay01.brasiltelecom.com.br";//IP (10.61.241.4)

	//Desenv
	//public final String IPSERVIDOREMAIL = "matwkprojeto";//IP (10.61.241.4)

	//E-mail do admin

	//Codigos de retorno do GPP
	public static final int		GPP_RET_OPERACAO_OK					 	=  0;
	public static final int		GPP_RET_MSISDN_JA_ATIVO				 	=  1;
	public static final int		GPP_RET_MSISDN_NAO_ATIVO 			 	=  2;
	public static final int		GPP_RET_VALOR_CREDITO_INVALIDO		 	=  3;
	public static final int		GPP_RET_PLANO_PRECO_INVALIDO		 	=  4;
	public static final int		GPP_RET_CREDITO_INSUFICIENTE		 	=  5;
	public static final int		GPP_RET_CODIGO_BLOQUEIO_INVALIDO	 	=  6;
	public static final int		GPP_RET_NOVO_MSISDN_JA_ATIVO		 	=  7;
	public static final int		GPP_RET_TARIFA_TROCA_PLANO_INVALIDA	 	=  8;
	public static final int		GPP_RET_RECARGA_NAO_IDENTIFICADA	 	=  9;
	public static final int		GPP_RET_STATUS_MSISDN_INVALIDO		 	= 10;
	public static final int		GPP_RET_TIPO_TRANSACAO_INVALIDO		 	= 11;
	public static final int		GPP_RET_LIMITE_CREDITO_ULTRAPASSADO	 	= 12;
	public static final int		GPP_RET_USUARIO_NAO_BLOQUEADO		 	= 13;
	public static final int		GPP_RET_MSISDN_BLOQUEADO			 	= 14;
	public static final int		GPP_RET_VOUCHER_NAO_EXISTE			 	= 15;
	public static final int		GPP_RET_IMSI_JA_EM_USO				 	= 16;
	public static final int		GPP_RET_RECARGA_JA_EFETUADA			 	= 17;
	public static final int		GPP_RET_SEM_CHAMADAS_A_B			 	= 18;
	public static final int		GPP_RET_DADOS_ASSINANTE_INVALIDOS	 	= 19;
	public static final int		GPP_RET_TIPO_CREDITO_INVALIDO		 	= 20;
	public static final int		GPP_RET_VALOR_CC_ULTRAPASSADO		 	= 21;
	public static final int		GPP_RET_QTD_CC_ULTRAPASSADA			 	= 22;
	public static final int		GPP_RET_QTD_MSISDN_ULTRAPASSADA		 	= 23;
	public static final int		GPP_RET_STATUS_MSISDN_VALIDO		 	= 24;
	public static final int		GPP_RET_ID_RECARGA_AJUSTE_INVALIDO	 	= 25;
	public static final int		GPP_RET_DATA_RECARGA_AJUSTE_INVALIDO 	= 26;
	public static final int		GPP_RET_SENHA_ASSINANTE_INVALIDA 	 	= 27;
	public static final int		GPP_RET_CAMPO_OBRIGATORIO	 		 	= 28;
	public static final int		GPP_RET_INCLUSAO_USUARIO_EXISTENTE	 	= 29;
	public static final int		GPP_RET_ALTERACAO_USUARIO_INEXISTENTE 	= 30;
	public static final int		GPP_RET_EXCLUSAO_USUARIO_INEXISTENTE 	= 31;
	public static final int		GPP_RET_FORMATO_DATA_INVALIDA        	= 32;
	public static final int		GPP_RET_ACAO_INEXISTENTE        	 	= 33;
	public static final int		GPP_RET_SISTEMA_INEXISTENTE     	 	= 34;
	public static final int		GPP_RET_NUMERO_BS_INEXISTENTE     	 	= 34;
	public static final int		GPP_RET_SEM_REGISTRO_ULTIMA_EXECUCAO 	= 36;
	public static final int		GPP_RET_BLOQDESBLOQ_NAO_SOLICITADO	 	= 37;
	public static final int		GPP_RET_PARAMETO_NAO_INFORMADO		 	= 38;
	public static final int		GPP_RET_SERVICO_INEXISTENTE			 	= 39;
	public static final int		GPP_RET_ERRO_CRIACAO_BONUS_RECARGA	 	= 40;
	public static final int		GPP_RET_ERRO_NAO_HA_BONUS_RECARGA	 	= 41;
	public static final int		GPP_RET_ERRO_ATUALIZ_BONUS_RECARGA	 	= 42;
	public static final int		GPP_RET_ERRO_ELIMINA_BONUS_RECARGA	 	= 43;
	public static final int		GPP_RET_PERCENT_BONUS_RECARGA_INV	 	= 44;
	public static final int		GPP_RET_ERRO_TEC_TECNOMEN_BONUS_REC	 	= 45;
	public static final int		GPP_RET_ERRO_CONSULTA_PRE_RECARGA	 	= 46;
	public static final int		GPP_RET_FRANQUIA_PARA_NAO_HIBRIDO    	= 47;
	public static final int		GPP_RET_CICLO_PLANO_HIBRIDO_JA_RODADO	= 48;
	public static final int		GPP_RET_XML_MAL_FORMADO				 	= 49;
	public static final int		GPP_RET_PULA_PULA_PROMO_VALIDADE_NOK	= 50;
	public static final int		GPP_RET_PULA_PULA_LIGACOES_NOK	 		= 51;
	public static final int		GPP_RET_CODIGO_REVENDA_NAO_CADASTRADO	= 52;
	public static final int		GPP_RET_ASSINANTE_LIGMIX             	= 53;
	public static final int		GPP_RET_LIMITE_REVENDA_ULTRAPASSADO	 	= 54;
	public static final int		GPP_RET_REVENDA_BLOQUEADA			 	= 55;
	public static final int		GPP_RET_NENHUM_REGISTRO					= 81;
	public static final int		GPP_RET_ERRO_TECNICO		     	 	= 99;
	public static final int		GPP_RET_ERRO_GENERICO_TECNOMEN		 	= 9999;
	public static final int		GPP_RET_ERRO_GENERICO_GPP			 	= 9998;
	public static final int		GPP_RET_ERRO_GENERICO_MASC 			 	= 9997;

	// Promoções
	public final int  ID_PROMOCAO_PEND_REC   		= 0;
	public final int  ID_PROMOCAO_PULAPULA   		= 1;
	public final int  ID_PROMOCAO_PULAPULA_2 		= 2;
	public final int  ID_PROMOCAO_LONDRINA   		= 3;
	public final int  ID_PROMOCAO_PULAPULA_3 		= 4;
	public final int  ID_PROMOCAO_PULAPULA_4 		= 5;
	public final int  ID_PROMOCAO_PULAPULA_CONTROLE	= 9;
	public final String TIP_TRANSACAO_PULA_PULA 	= "08001";

	// Categorias
	public final int  ID_CATEGORIA_PULAPULA  = 1;
	public final int  ID_CATEGORIA_LIGMIX    = 2;

	// CONSTANTES PAGUE E GANHE
	// Id das campanhas promocionais
	public final long ID_CAMPANHA_PAGUE_GANHE    = 3;
	// Id da tabela de configuração pague e ganhe
	public final String ID_CONFIG_CAMPANHA_NPG 	 = "CAMPANHA_NPG_DIAS_ESPERA";

	//Constantes de status de processamento na fila de recargas
	public static final int ID_FILA_RECARGAS_NAO_EXECUTADO   = 0;
	public static final int ID_FILA_RECARGAS_OK              = 3;
	public static final int ID_FILA_RECARGAS_TESTE_PULA_PULA = 4;
	public static final int ID_FILA_RECARGAS_NOK             = 9;

	public final String ID_MOTIVO_CONTESTACAO_RECARGA_PULAPULA = "ID_MOTIVO_CONTESTACAO_RECARGA_PULAPULA";
	public final String LISTA_COD_SRV_SFA_PULAPULA = "IDT_COD_SRV_SFA_PULAPULA";
	public final String VLR_MAXIMO_BONUS_PULAPULA = "VLR_MAXIMO_BONUS_PULAPULA";

	public final String STATUS_SERVICO = "STATUS_SERVICO";
	public final String STATUS_ASSINANTE = "STATUS_ASSINANTE";

	public final String TROCA_STATUS_SERVICO = "TROCA_STATUS_SERVICO";
	public final int TIPO_BLOQUEIO = 1;

	public final String EMAILADMIN = "vipaulo@hotmail.com";
	
	public final double VLR_MAX_TRANSFERENCIA_SALDO_SMS = 500.0;
	public final double VLR_MAX_SALDO_OFFNET_TRANSFERENCIA_SALDO = 0.34;

	//Operacoes utilizadas no log
	public final String[][] operacoesLog =
	{
		{"54", "ABRIR_BS"},
		{"37", "AJUSTAR_CREDITO"},
		{"38", "AJUSTAR_DEBITO"},
		{"55", "ALTERAR_CONFIG_CONTEST"},
		{"3",  "ALTERAR_GRUPO"},
		{"45", "ALTERAR_MOTIVO_AJUSTE"},
		{"84", "ALTERAR_STATUS_VOUCHER"},
		{"33", "ALTERAR_TIPO_EXTRATO"},
		{"8",  "ALTERAR_USUARIO"},
		{"10", "ALTERA_SENHA"},
		{"79", "CONSULTAR_AJU_CONTESTACAO"},
		{"57", "CONSULTAR_AJUST_CONS_ATEN"},
		{"76", "CONSULTAR_AJUS_ACORDO_JUD"},
		{"77", "CONSULTAR_AJUS_CARRY_OVER"},
		{"78", "CONSULTAR_AJUS_CONCATEN"},
		{"56", "CONSULTAR_AJUSTE_ATENDENT"},
		{"53", "CONSULTAR_BS"},
		{"50", "CONSULTAR_BS_ATENDENTE"},
		{"51", "CONSULTAR_BS_CLIENTE"},
		{"52", "CONSULTAR_BS_FILIAL"},
		{"39", "CONSULTAR_CARTAO"},
		{"41", "CONSULTAR_COMPROVANTE"},
		{"108","CONSULTAR_CONTABIL_EXTRAT"},
		{"87", "CONSULTAR_CONTABIL_RECARG"},
		{"133", "CONSULTAR_CONTABIL_IB"},
		{"98", "CONSULTAR_CONTABIL_RCFACE"},
		{"89", "CONSULTAR_CONTABIL_AJUSTE"},
		{"91", "CONSULTAR_CONTABIL_CONSUM"},
		{"93", "CONSULTAR_CONTABIL_SALDO"},
		{"122", "CONSULTAR_HIGHLIGHT_TSD"},
		{"124", "CONSULTAR_MODELOS_BASE"},
		{"126", "CONSULTAR_NOVOS_TSD"},
		{"113","CONSULTAR_CONTINGEN_CRM"},
		{"83", "CONSULTAR_DETALHE_PED_VOU"},
		{"47", "CONSULTAR_GRUPO"},
		{"85", "CONSULTAR_HISTORICO_VOUCH"},
		{"42", "CONSULTAR_HISTORICO"},
		{"68", "CONSULTAR_HISTORICO_BS"},
		{"43", "CONSULTAR_MOTIVO_AJUSTE"},
		{"69", "CONSULTAR_NUMS_CORRELATOS"},
		{"82", "CONSULTAR_PEDIDOS_VOUCHER"},
		{"59", "CONSULTAR_PERIODO_RECARGA"},
		{"40", "CONSULTAR_RECARGAS"},
		{"58", "CONSULTAR_RECEITA_PL_PROD"},
		{"71", "CONSULTAR_SO_SEU_MSISDN"},
		{"32", "CONSULTAR_TIPO_EXTRATO"},
		{"49", "CONSULTAR_USUARIO"},
		{"2",  "EXCLUIR_GRUPO"},
		{"46", "EXCLUIR_MOTIVO_AJUSTE"},
		{"34", "EXCLUIR_TIPO_EXTRATO"},
		{"7",  "EXCLUIR_USUARIO"},
		{"4",  "GRAVAR_GRUPO"},
		{"44", "GRAVAR_MOTIVO_AJUSTE"},
		{"6",  "SALVAR_USUARIO"},
		{"101","CONSULTAR_MOD_OP"},
		{"102","ALTERAR_MOD_OP"},
		{"104","BLOQUEIO_CONTINGENCIA"},
		{"106","DESBLOQUEIO_CONTINGENCIA"},
		{"109","CONSULTAR_BLQ_DESBLQ"},
		{"116","CONSULTAR_STATUS_BLOQUEIO"},
		{"131","CONSULTAR_REL_TRAFEGO_CSP"},
		{"128","TRANSFERIR_SALDO"},
		{"135","CONSULTAR_OR_MINUTOS"},
		{"139","CONSULTAR_HIS_PULA_PULA"},
		{"140","CONSULTAR_PULA_PULA"},
		{"141","SALVAR_TROCA_PULA_PULA"},
		{"178", "SALVAR_ISENCAO_LIM_PULA"},
		{"144","CONSULTAR_CARTAO_UNICO"},
		{"151","CONSULTAR_AMIGOS_TH"},
		{"159","CONSULTAR_RECARGAS_VF"},
		{"167","CONSULTAR_CARAC_ASS"},
		{"181","CONSULTAR_CONTABIL_RCFACE"},
		{"182","CONSULTAR_RECARGAS_TFPP"},
		{"183","BROADCAST_SMS"},
		{"185","CONSULTAR_CLIENTES_SD"},
		{"193","BAT_MKT_CLIENTES_SD"},
		{"207","INSERIR_FF"},
		{"213","CONSULTAR_ESTORNO_PULA"},
		{"230","CONSULTA_REC_PLAN_CONTROL"},
		{"230","CONSULTA_REC_RECORRENTE"},
		{"235","CONSULTAR_EXPURGO_ESTORNO"},
		{"252","CONSULTA_EXTRATO_CHEIO"},
		{"279","CADASTRO_PRECO_APARELHO"}
	};

	//Tipos de Transação para Transferência de Créditos entre Saldos.
	public static final String TRANSF_SALDO_CANAL_AJUSTE_CREDITO = "05";
	public static final String TRANSF_SALDO_CANAL_AJUSTE_DEBITO = "06";
	public static final String TRANSF_SALDO_ORIGEM = "022";

	//Caracteres "entity" do XML
	public final String[][] caracteresXML =
	{
		{"&","&#38;"},
		{"\"","&#34;"},
		{"<","&#60;"},
		{">","&#62;"},
		{"¤","&#164;"},
		{"¢","&#162;"},
		{"£","&#163;"},
		{"¥","&#165;"},
		{"¦","&#166;"},
		{"§","&#167;"},
		{"¨","&#168;"},
		{"©","&#169;"},
		{"ª","&#170;"},
		{"«","&#171;"},
		{"¬","&#172;"},
		{"®","&#174;"},
		{"°","&#176;"},
		{"²","&#178;"},
		{"¹","&#185;"},
		{"µ","&#181;"},
		{"»","&#187;"},
		{"¼","&#188;"},
		{"½","&#189;"},
		{"ø","&#248;"},
		{"Ø","&#216;"},
		{"´","&#180;"},
		{"¿","&#191;"}

	};

	// Representa o status das notas e faturas q não terminaram o processamento WEB <-> VITRIA <-> SAP, e que não serão visualizadas na WEB.
	public final String STATUS_CONTROL_WEB_SAP = "99";

	//Numero maximo de registros por pagina nas funcionalidades de consulta
	public final int PAGE_SIZE = 90;

	// Variáveis de menu
	public static final String MENU_CARTOES_PP         		= "MENU_CARTOES_PP";
	public static final String MENU_RECARGAS           		= "MENU_RECARGAS";
	public static final String MENU_DIG_VERIF          		= "MENU_DIG_VERIF";
	public static final String MENU_CONSULTA_EXT_PULA  		= "MENU_CONSULTA_EXT_PULA";
	public static final String MENU_COMP_SERVICOS      		= "MENU_COMP_SERVICOS";
	public static final String MENU_HIS_EXTRATO        		= "MENU_HIS_EXTRATO";
	public static final String MENU_HIS_PULA_PULA      		= "MENU_HIS_PULA_PULA";
	public static final String MENU_AJUSTE_CREDITO     		= "MENU_AJUSTE_CREDITO";
	public static final String MENU_AJUSTE_DEBITO      		= "MENU_AJUSTE_DEBITO";
	public static final String MENU_CONTESTACAO_DEB    		= "MENU_CONTESTACAO_DEB";
	public static final String MENU_CONTESTACAO_HIS    		= "MENU_CONTESTACAO_HIS";
	public static final String MENU_CADASTRO_AJU       		= "MENU_CADASTRO_AJU";
	public static final String MENU_CONFIG_CONTESTACAO 		= "MENU_CONFIG_CONTESTACAO";
	public static final String MENU_TIPO_EXTRATO       		= "MENU_TIPO_EXTRATO";
	public static final String MENU_CADASTRO_PERFIL    		= "MENU_CADASTRO_PERFIL";
	public static final String MENU_PERFIL_USUARIO     		= "MENU_PERFIL_USUARIO";
	public static final String MENU_LOG_OPERACOES      		= "MENU_LOG_OPERACOES";
	public static final String MENU_BS_CLIENTE         		= "MENU_BS_CLIENTE";
	public static final String MENU_BS_ATENDENTE       		= "MENU_BS_ATENDENTE";
	public static final String MENU_BS_FILIAL          		= "MENU_BS_FILIAL";
	public static final String MENU_MKT_ATENDENTE      		= "MENU_MKT_ATENDENTE";
	public static final String MENU_MKT_CONSOLIDADO    		= "MENU_MKT_CONSOLIDADO";
	public static final String MENU_MKT_RECEITA        		= "MENU_MKT_RECEITA";
	public static final String MENU_MKT_PERIODO        		= "MENU_MKT_PERIODO";
	public static final String MENU_FIN_JUDICIAL      		= "MENU_FIN_JUDICIAL";
	public static final String MENU_FIN_CARRYOVER      		= "MENU_FIN_CARRYOVER";
	public static final String MENU_FIN_CONCATENACAO   		= "MENU_FIN_CONCATENACAO";
	public static final String MENU_FIN_CONTESTACAO    		= "MENU_FIN_CONTESTACAO";
	public static final String MENU_FIN_AMIGOS_TH	   		= "MENU_FIN_AMIGOS_TH";
	public static final String MENU_FIN_ATIVACOES_MAN  		= "MENU_FIN_ATIVACOES_MAN";
	public static final String MENU_FIN_ATIV_CART_FV   		= "MENU_FIN_ATIV_CART_FV";
	public static final String MENU_FIN_BASE_USUARIOS  		= "MENU_FIN_BASE_USUARIOS";
	public static final String MENU_FIN_TRAF_E_FINAN   		= "MENU_FIN_TRAF_E_FINAN";
	public static final String MENU_FIN_TRAF_E_FIN_CSP 		= "MENU_FIN_TRAF_E_FIN_CSP";
	public static final String MENU_FIN_CREDITOS_PROM  		= "MENU_FIN_CREDITOS_PROM";
	public static final String MENU_REL_TRAFEGO_DIARIO 		= "MENU_REL_TRAFEGO_DIARIO";
	public static final String MENU_REL_TRAFEGO_CSP    		= "MENU_REL_TRAFEGO_CSP";
	public static final String MENU_REL_TRAFEGO    		    = "MENU_REL_TRAFEGO";
	public static final String MENU_REL_BASE_APARELHOS	    = "MENU_REL_BASE_APARELHOS";
	public static final String MENU_REL_TRAFEGO_CSP_14 		= "MENU_REL_TRAFEGO_CSP_14";
	public static final String MENU_PEDIDO_VOUCHER 	   		= "MENU_PEDIDO_VOUCHER";
	public static final String MENU_CAMPANHA	 	   		= "MENU_CAMPANHA";
	public static final String MENU_CONTABIL_RECARGA   		= "MENU_REL_CONTABIL_RECARGA";
	public static final String MENU_REL_MKT_REC_TFPP   		= "MENU_REL_MKT_REC_TFPP";
	public static final String MENU_CONTABIL_AJUSTE    		= "MENU_REL_CONTABIL_AJUSTE";
	public static final String MENU_CONTABIL_CONSUMO   		= "MENU_REL_CONTABIL_CONSUMO";
	public static final String MENU_CONTABIL_SALDO     		= "MENU_REL_CONTABIL_SALDO";
	public static final String MENU_CONTABIL_RCFACE	   		= "MENU_REL_CONTABIL_RCFACE";
	public static final String MENU_CONTABIL_EXTRATO   		= "MENU_REL_CONTABIL_EXTRATO";
	public static final String MENU_CONSULTAR_MOD_OP   		= "MENU_CONSULTAR_MOD_OP";
	public static final String MENU_CONT_BLOQUEIO	   		= "MENU_CONT_BLOQUEIO";
	public static final String MENU_CONT_DESBLOQUEIO   		= "MENU_CONT_DESBLOQUEIO";
	public static final String MENU_CONT_RELATORIO_CRM 		= "MENU_CONT_RELATORIO_CRM";
	public static final String MENU_CONT_RECARGA_DIA		= "MENU_CONT_RECARGA_DIA";
	public static final String MENU_CONSULTA_SALDO_BLOQ 	= "MENU_CONSULTA_SALDO_BLOQ";
	public static final String MENU_MKT_CARTAO_UNICO    	= "MENU_REL_MKT_CARTAO_UNICO";
	public static final String MENU_MKT_HIGHLIGHTS 			= "MENU_REL_MKT_HIGHLIGHTS";
	public static final String MENU_MKT_MODELOS_BASE 		= "MENU_REL_MKT_MODELOS_BASE";
	public static final String MENU_MKT_NOVOS_TSD			= "MENU_REL_MKT_NOVOS_TSD";
	public static final String MENU_MKT_RECARGAS_FACE		= "MENU_MKT_RECARGA_FACE";
	public static final String MENU_MKT_CLIENTES_SD			= "MENU_MKT_CLIENTES_SD";
	public static final String MENU_REL_MKT_PROM_X_STAT		= "MENU_REL_MKT_PROM_X_STAT";
	public static final String MENU_MKT_TROCA_PROM_X_STA 	= "MENU_MKT_TROCA_PROM_X_STA";
	public static final String MENU_MKT_BONUS_PROM_FAIXA 	= "MENU_MKT_BONUS_PROM_FAIXA";
	public static final String MENU_REL_MKT_MIGRACOES_PP 	= "MENU_REL_MKT_MIGRACOES_PP";
	public static final String MENU_MKT_ESTORNO_EXPURGO  	= "MENU_MKT_ESTORNO_EXPURGO";
	public static final String MENU_REL_MKT_MOBILE_MKT		= "MENU_MKT_MOBILE_MARKETING";
	public static final String MENU_CONTABIL_IB   			= "MENU_REL_CONTABIL_IB";
	public static final String MENU_TRANSF_SALDO   			= "MENU_TRANSF_SALDO";
	public static final String MENU_REL_OR_MINUTOS   		= "MENU_REL_OR_MINUTOS";
	public static final String MENU_CONSULTAR_CARAC_ASS 	= "MENU_CONSULTAR_CARAC_ASS";
	public static final String MENU_CONSULTA_PULA_PULA  	= "MENU_CONSULTA_PULA";
	public static final String MENU_ENVIA_SMS				= "MENU_ENVIA_SMS";
	public static final String MENU_ENVIA_SMS_TANGRAM   	= "MENU_ENVIA_SMS_TANGRAM";
	public static final String MENU_MKT_BASE_APARELHOS		= "MENU_BASE_APARELHOS";
	public static final String MENU_BAT_MKT_CLIENTES_SD		= "MENU_BAT_MKT_CLIENTES_SD";
	public static final String MENU_MKT_RESUMO_CRED_PULA 	= "MENU_MKT_RESUMO_CRED_PULA";
	public static final String MENU_MKT_SALDO_ASSINANTES 	= "MENU_MKT_SALDO_ASSINANTES";
	public static final String MENU_RELATORIO_CONCLUIDO 	= "MENU_RELATORIO_CONCLUIDO";
	public static final String MENU_CONS_ESTORNO_PULA		= "MENU_CONS_ESTORNO_PULA";
	public static final String MENU_MKT_REC_PLAN_CONTROL	= "MENU_MKT_REC_PLAN_CONTROL";
	public static final String MENU_RECARGA_RECORRENTE		= "MENU_RECARGA_RECORRENTE";
	public static final String MENU_CTRL_RECEITA			= "MENU_CRTL_RECEITA";
	public static final String MENU_CTR_EXPURGO_ESTORNO		= "MENU_CTR_EXPURGO_ESTORNO";
	public static final String MENU_CONT_COD_SERVICOS		= "MENU_CONT_COD_SERVICOS";
	public static final String MENU_MKT_PULA_PULA_2004_DETALHADO = "MENU_MKT_PP2004_DETALHADO";
	public static final String MENU_MKT_STATUS_ASS_OPT_IN 		= "MENU_MKT_STATUS_ASS_OPTIN";
	public static final String MENU_REL_TRF_CHAMADAS_COBRAR 	= "MENU_TRF_CHAMADAS_COBRAR";
	public static final String MENU_MKT_CATEG_ASS_OPT_IN 		= "MENU_MKT_CATEG_ASS_OPTIN";
	public static final String MENU_MKT_OPERACIONAL_OPT_IN 		= "MENU_MKT_OPERACION_OPTIN";
	public static final String MENU_MKT_GERENCIAL_OPT_IN 		= "MENU_MKT_GERENCIAL_OPTIN";
	public static final String MENU_CON_AMIGOS_TD_H 			= "MENU_CON_AMIGOS_TD_H";
	public static final String MENU_CONSULTA_ASS_OPTIN 			= "MENU_CON_ASS_OPTIN";
	public static final String MENU_CONSULTA_GERENTE_FELIZ		= "MENU_CON_GERENTE_FELIZ";
	public static final String MENU_CONT_CHAMADAS_SAINTES		= "MENU_CON_CHAMADAS_SAINTES";
	public static final String MENU_CONSULTA_ASS_RECARREGARAM	= "MENU_CON_ASS_RECARREGARAM";
	public static final String MENU_CONSULTA_ASS_NPG			= "MENU_CON_ASS_NPG";
	public static final String MENU_MKT_BONIFICACAO_CAMPANHA	= "MENU_MKT_BONIFIC_CAMPANHA";
	public static final String MENU_CADASTRO_PRECO_APARELHO		= "MENU_CAD_PRECO_APARELHO";
	public static final String MENU_CONSULTA_PREVIA_CONTABIL	= "MENU_CON_PREVIA_CONT";
	public static final String MENU_ATUALIZACAO_BLACK_LIST 		= "MENU_ATUALIZA_BLACKLIST";
	public static final String MENU_REL_MKT_DEMONSTRATIVO_BONUS	= "MENU_MKT_DEMONSTRAT_BONUS";
	public static final String MENU_RELATORIO_TESTE 			= "MENU_RELATORIO_TESTE";
	public static final String MENU_FABRICA_RELATORIOS 			= "MENU_FABRICA_RELATORIOS";
	public static final String MENU_ESTORNO_EXPURGO_PP			= "MENU_ESTORNO_EXPURGO_PP";
	public static final String MENU_GERENCIAMENTO_ARQ 			= "MENU_GERENCIAMENTO_ARQ";
	public static final String MENU_APROVACAO_PREVIAS 			= "MENU_APROVACAO_PREVIAS";
	public static final String MENU_ORIGENS_REQUISICAO 			= "MENU_ORIGENS_REQUISICAO";
	public static final String MENU_TARIFACAO 					= "MENU_TARIFACAO";
	public static final String MENU_ALIQUOTA_IMPOSTO 			= "MENU_ALIQUOTA_IMPOSTO";
	public static final String MENU_DESTINO 					= "MENU_DESTINO";
	public static final String MENU_REGIAO_DESTINO 				= "MENU_REGIAO_DESTINO";
	public static final String MENU_LAC 						= "MENU_LAC";
	public static final String MENU_REGIAO_LAC 					= "MENU_REGIAO_LAC";
	public static final String MENU_PAIS 						= "MENU_PAIS";
	public static final String MENU_PROPRIEDADE					= "MENU_PROPRIEDADE";
	public static final String MENU_SERVICO						= "MENU_SERVICO";
	public static final String MENU_DESCONTO					= "MENU_DESCONTO";
	public static final String MENU_TIPO_CATEGORIA				= "MENU_TIPO_CATEGORIA";
	public static final String MENU_TIPO_EVENTO					= "MENU_TIPO_EVENTO";
	public static final String MENU_TIPO_DESTINO				= "MENU_TIPO_DESTINO";
	public static final String MENU_TIPO_OPERADORA				= "MENU_TIPO_OPERADORA";
	public static final String MENU_GRUPO_BONIFICACAO			= "MENU_GRUPO_BONIFICACAO";
	public static final String MENU_PLANO_TARIFACAO				= "MENU_PLANO_TARIFACAO";
	public static final String MENU_DIA_SEMANA					= "MENU_DIA_SEMANA";
	public static final String MENU_VALOR_DESLOCAMENTO			= "MENU_VALOR_DESLOCAMENTO";
	public static final String MENU_OPERADORA_GSM				= "MENU_OPERADORA_GSM";
	public static final String MENU_BONIFICACAO_PLANO			= "MENU_BONIFICACAO_PLANO";
	public static final String MENU_DESCONTO_PLANO				= "MENU_DESCONTO_PLANO";
	public static final String MENU_TIPO_CHAMADA				= "MENU_TIPO_CHAMADA";
	public static final String MENU_VALOR_CHAMADA				= "MENU_VALOR_CHAMADA";
	public static final String MENU_CON_TIPODOC_FILIAL			= "MENU_CON_TIPODOC_FILIAL";
	public static final String MENU_CON_SERVICO_102				= "MENU_CON_SERVICO_102";
	public static final String MENU_CON_ACOMPANHAMENTO_SALDOS	= "MENU_CON_ACOMP_SALDOS";
	public static final String MENU_CONSULTA_FGN				= "MENU_CONSULTA_FGN";
	public static final String MENU_CON_ERR_REC_CONTROLE		= "MENU_CON_ERR_REC_CONTROLE";
	public static final String MENU_RECARGA_MASSA				= "MENU_RECARGA_MASSA";
	public static final String MENU_ARQUIVOS_RECARGA_MASSA		= "MENU_ARQUIVOS_RECARGA_MAS";
	public static final String MENU_APROVACAO_LT_RECARGA		= "MENU_APROVACAO_LT_RECARGA";
	public static final String MENU_CADASTRA_PACOTE_DADOS		= "MENU_CAD_PACOTE_DADOS";
	public static final String MENU_CONSULTA_PACOTE_DADOS		= "MENU_CONS_ASS_PCT_DADOS";
	public static final String MENU_MKT_PCT_DADOS				= "MENU_REL_MKT_PACOTE_DADOS";
	public static final String MENU_GERENCIAMENTO_GPP			= "MENU_GERENCIAMENTO_GPP";
	public static final String MENU_SERVICO_SFA					= "MENU_SERVICO_SFA";
	public static final String MENU_VINCULACAO_SFA				= "MENU_VINCULACAO_SFA";
	public static final String MENU_VINCULACAO_SFA_CDR			= "MENU_VINCULACAO_SFA_CDR";
	public static final String MENU_ORIGEM_RECARGA				= "MENU_ORIGEM_RECARGA";
	public static final String MENU_ORIGEM_CDR					= "MENU_ORIGEM_CDR";
	public static final String MENU_FALE_GRATIS_ORELHAO     	= "MENU_FALE_GRATIS_ORELHAO";
    public static final String MENU_CON_HISTORICO_SMS           = "MENU_CON_HISTORICO_SMS";
    public static final String MENU_REL_CARTAO_RECARGA          = "MENU_REL_CARTAO_RECARGA";
    public static final String MENU_REL_SUBIDA_TSD              = "MENU_REL_SUBIDA_TSD";
    public static final String MENU_ADESAO_OFERTA_NATAL         = "MENU_ADESAO_OFERTA_NATAL";
    public static final String MENU_ADES_OFERTA_NATAL_M         = "MENU_ADES_OFERTA_NATAL_M";
    public static final String MENU_CONSULTA_BS_ABERTOS         = "MENU_CONSULTA_BS_ABERTOS";
    public static final String MENU_AJUSTE_CREDITO_BANCARIO     = "MENU_AJUSTE_CRED_BANCO";
    public static final String MENU_AJUSTA_STAT_ASS				= "MENU_AJUSTA_STAT_ASS";

	//Codigos das Operacoes de inclusão e alteração do administrativo
	public static final String COD_CONSULTAR_MOTIVO_AJUSTE 		= "CONSULTAR_MOTIVO_AJUSTE";
	public static final String COD_ALTERAR_MOTIVO_AJUSTE 		= "ALTERAR_MOTIVO_AJUSTE";
	public static final String COD_GRAVAR_MOTIVO_AJUSTE 		= "GRAVAR_MOTIVO_AJUSTE";
	public static final String COD_EXCLUIR_MOTIVO_AJUSTE 		= "EXCLUIR_MOTIVO_AJUSTE";
	public static final String COD_CONSULTAR_TIPO_EXTRATO 		= "CONSULTAR_TIPO_EXTRATO";
	public static final String COD_ALTERAR_TIPO_EXTRATO 		= "ALTERAR_TIPO_EXTRATO";
	public static final String COD_EXCLUIR_TIPO_EXTRATO 		= "EXCLUIR_TIPO_EXTRATO";
	public static final String COD_ALTERAR_CONFIG_CONTESTACAO 	= "ALTERAR_CONFIG_CONTEST";
	public static final String COD_CONSULTAR_PEDIDOS_VOUCHER 	= "CONSULTAR_PEDIDOS_VOUCHER";
	public static final String COD_BROADCAST_SMS				= "BROADCAST_SMS";
	public static final String COD_CADASTRAR_PRECO_APARELHO		= "CADASTRO_PRECO_APARELHO";

	//Operacoes referentes à Contingencia Crm
	public static final String COD_CONSULTAR_MOD_OP         = "CONSULTAR_MOD_OP";
	public static final String COD_ALTERAR_MOD_OP           = "ALTERAR_MOD_OP";
	public static final String COD_BLOQUEIO_CONTINGENCIA    = "BLOQUEIO_CONTINGENCIA";
	public static final String COD_DESBLOQUEIO_CONTINGENCIA = "DESBLOQUEIO_CONTINGENCIA";
	public static final String COD_CONSULTAR_BLQ_DESBLQ     = "CONSULTAR_BLQ_DESBLQ";

	//Operacoes referentes ao Grupo
	public final String COD_MENU_GRUPO = "MENU_GRUPO";
	public final String COD_CONSULTAR_GRUPO = "CONSULTAR_GRUPO";
	public final String COD_EXCLUIR_GRUPO = "EXCLUIR_GRUPO";
	public final String COD_ALTERAR_GRUPO = "ALTERAR_GRUPO";
	public final String COD_GRAVAR_GRUPO = "GRAVAR_GRUPO";

	// Operacoes referentes a Ajustes
	public final String COD_AJUSTE_CREDITO = "AJUSTAR_CREDITO";
	public final String COD_AJUSTE_DEBITO = "AJUSTAR_DEBITO";
	public final String COD_TRANSF_SALDO = "TRANSFERIR_SALDO";
    public final String COD_AJUSTE_CREDITO_BANCARIO = "AJUSTAR_CRED_BANCO";
    public final String COD_AJUSTA_STATUS_ASSINANTE = "AJUSTA_STATUS_ASSINANTE";
    public final String COD_AJUSTA_STAT_ASS_SD_PRINC = "AJUSTA_STAT_ASS_SD_PRINC";

	// Operacoes referentes a gerenciamento de vouchers
	public final String COD_CONSULTA_PEDIDOS_VOUCHER = "CONSULTAR_PEDIDOS_VOUCHER";
	public final String COD_CONSULTA_DETALHE_PEDIDOS_VOUCHER = "CONSULTAR_DETALHE_PED_VOU";
	public final String COD_ALTERAR_STATUS_VOUCHER = "ALTERAR_STATUS_VOUCHER";
	public final String COD_CONSULTA_HISTORICO_STATUS_VOUCHER = "CONSULTAR_HISTORICO_VOUCH";

	// Operacoes referentes a campanhas promocionais
	public final String COD_CONSULTAR_CAMPANHA 				= "CONSULTAR_CAMPANHA";
	public final String COD_CADASTRAR_CAMPANHA	 			= "CADASTRAR_CAMPANHA";
	public final String COD_CONSULTAR_ASSIN_CAMPANHA 		= "CONSULTAR_ASSIN_CAMPANHA";
	public final String COD_CONSULTA_ANALITICO_ASS_CAMPANHA = "CONSULTA_ANALITICO_ASS_CA";
	public final String COD_CONSULTA_SUMARIZACAO_CAMPANHA	= "CONSULTA_SUMARIZACAO_CAMP";
	public final String COD_CADASTRAR_PACOTE_DADOS			= "CADASTRAR_PACOTE_DADOS";
	public final String COD_CONS_ASS_PCT_DADOS				= "CONS_ASS_PCT_DADOS";

	// Operacoes referentes a Consultas
	public final String COD_CONSULTA_CARTAO           	= "CONSULTAR_CARTAO";
	public final String COD_CONSULTA_RECARGAS         	= "CONSULTAR_RECARGAS";
	public final String COD_CONSULTA_RECARGAS_TFPP	  	= "CONSULTAR_RECARGAS_TFPP";
	public final String COD_CONSULTA_COMPROVANTE      	= "CONSULTAR_COMPROVANTE";
	public final String COD_CONSULTA_HISTORICO        	= "CONSULTAR_HISTORICO";
	public final String COD_CONSULTAR_STATUS_BLOQUEIO 	= "CONSULTAR_STATUS_BLOQUEIO";
	public final String COD_CONSULTA_INF_ASSINANTE    	= "CONSULTAR_INF_ASSINANTE";
	public final String COD_CONSULTA_EXTRATO_PULA     	= "CONSULTAR_EXTRATO_PULA";
	public final String COD_CONSULTA_PULA_PULA        	= "CONSULTAR_PULA_PULA";
	public final String COD_CONSULTA_HIS_PULA_PULA    	= "CONSULTAR_HIS_PULA_PULA";
	public final String COD_SALVAR_TROCA_PULA_PULA    	= "SALVAR_TROCA_PULA_PULA";
	public final String COD_SALVAR_ISENCAO_LIM_PULA   	= "SALVAR_ISENCAO_LIM_PULA";
	public final String COD_CONSULTAR_CARAC_ASS		  	= "CONSULTAR_CARAC_ASS";
	public final String COD_CONSULTAR_VOUCHER_PIN	  	= "CONSULTAR_VOUCHER_PIN";
	public final String COD_CANCELAR_VOUCHER	      	= "CANCELAR_VOUCHER";
	public final String COD_INSERIR_FF			      	= "INSERIR_FF";
	public final String COD_CONSULTA_ESTORNO_PULA	  	= "CONSULTAR_ESTORNO_PULA";
	public final String COD_CONSULTA_REC_RECORRENTE		= "CONSULTA_REC_RECORRENTE";
	public final String COD_CONSULTA_EXTRATO_CHEIO		= "CONSULTA_EXTRATO_CHEIO";
	public final String COD_CONSULTAR_ASS_OPTIN			= "CONSULTAR_ASS_OPTIN";
	public final String COD_CONSULTAR_GERENTE_FELIZ		= "CONSULTAR_GEREMTE_FELIZ";
	public final String COD_CONSULTAR_ASS_NPG			= "CONSULTAR_ASS_NPG";
	public final String	COD_VER_FORMULARIO_PIN			= "VER_FORMULARIO_PIN";
	public final String COD_CONSULTA_FGN				= "CONSULTA_FGN";
    public final String COD_CONSULTA_HISTORICO_SMS      = "CONSULTA_HISTORICO_SMS";
    public final String COD_CONSULTA_EXTRATO_GPP		= "CONSULTA_EXTRATO_GPP";

	// Operacoes referentes ao Relatórios Gerenciais
	public final String COD_CONSULTA_BS_ATENDENTE 			 = "CONSULTAR_BS_ATENDENTE";
	public final String COD_CONSULTA_BS_CLIENTE 			 = "CONSULTAR_BS_CLIENTE";
	public final String COD_CONSULTA_BS_FILIAL 				 = "CONSULTAR_BS_FILIAL";
	public final String COD_CONSULTA_AJUSTE_ATENDENTE 		 = "CONSULTAR_AJUSTE_ATENDENT";
	public final String COD_CONSULTA_AJUSTE_CONS_ATENDENTE	 = "CONSULTAR_AJUST_CONS_ATEN";
	public final String COD_CONSULTA_RECEITA_PLANO_PROD 	 = "CONSULTAR_RECEITA_PL_PROD";
	public final String COD_CONSULTA_PERIODO_RECARGAS 		 = "CONSULTAR_PERIODO_RECARGA";
	public final String COD_CONSULTA_AJUS_ACORDO_JUD 		 = "CONSULTAR_AJUS_ACORDO_JUD";
	public final String COD_CONSULTAR_AJUS_CARRY_OVER 		 = "CONSULTAR_AJUS_CARRY_OVER";
	public final String COD_CONSULTAR_AJUS_CONCATEN 		 = "CONSULTAR_AJUS_CONCATEN";
	public final String COD_CONSULTAR_AJU_CONTESTACAO 		 = "CONSULTAR_AJU_CONTESTACAO";
	public final String COD_CONSULTAR_AMIGOS_TH		 		 = "CONSULTAR_AMIGOS_TH";
	public final String COD_CONSULTAR_ATIVACOES_MAN		 	 = "CONSULTAR_ATIVACOES_MAN";
	public final String COD_CONSULTAR_ATIV_CART_FV		 	 = "CONSULTAR_ATIV_CART_FV";
	public final String COD_CONSULTAR_BASE_USUARIOS		 	 = "CONSULTAR_BASE_USUARIOS";
	public final String COD_CONSULTAR_TRAFEGO_E_FINAN	 	 = "CONSULTAR_TRAFEGO_E_FINAN";
	public final String COD_CONSULTAR_TRAFEGO_FIN_CSP	 	 = "CONSULTAR_TRAFEGO_FIN_CSP";
	public final String COD_CONSULTAR_CREDITOS_PROM	    	 = "CONSULTAR_CREDITOS_PROM";
	public final String COD_CONSULTAR_CONTABIL_RECARG 		 = "CONSULTAR_CONTABIL_RECARG";
	public final String COD_CONSULTAR_REC_TFPP_OK_NOK	     = "CONSULTAR_REC_TFPP_OK_NOK";
	public final String COD_CONSULTAR_CONTABIL_AJUSTE 		 = "CONSULTAR_CONTABIL_AJUSTE";
	public final String COD_CONSULTAR_CONTABIL_CONSUMO 		 = "CONSULTAR_CONTABIL_CONSUM";
	public final String COD_CONSULTAR_CONTABIL_SALDO 		 = "CONSULTAR_CONTABIL_SALDO";
	public final String COD_CONSULTAR_CONTABIL_RCFACE		 = "CONSULTAR_CONTABIL_RCFACE";
	public final String COD_CONSULTAR_CONTABIL_EXTRATO		 = "CONSULTAR_CONTABIL_EXTRAT";
	public final String COD_CONSULTAR_CONTINGENCIA_CRM 		 = "CONSULTAR_CONTINGEN_CRM";
	public final String COD_CONSULTAR_CARTAO_UNICO           = "CONSULTAR_CARTAO_UNICO";
	public final String COD_CONSULTAR_HIGHLIGHT_TSD 		 = "CONSULTAR_HIGHLIGHT_TSD";
	public final String COD_CONSULTAR_MODELOS_BASE 			 = "CONSULTAR_MODELOS_BASE";
	public final String COD_CONSULTAR_NOVOS_TSD 			 = "CONSULTAR_NOVOS_TSD";
	public final String COD_CONSULTAR_CONTABIL_IB 			 = "CONSULTAR_CONTABIL_IB";
	public final String COD_CONSULTAR_OR_MINUTOS 			 = "CONSULTAR_OR_MINUTOS";
	public final String COD_CONSULTAR_RECARGAS_VF			 = "CONSULTAR_RECARGAS_VF";
	public final String COD_CONSULTAR_RECARGA_DIA			 = "CONSULTAR_RECARGA_DIA";
	public final String COD_CONSULTAR_CLIENTES_SD			 = "CONSULTAR_CLIENTES_SD";
	public static final String COD_CONSULTAR_REL_TRAFEGO_CSP = "CONSULTAR_REL_TRAFEGO_CSP";
	public static final String COD_CONSULTAR_REL_TRAF_CSP_14 = "CONSULTAR_REL_TRAF_CSP_14";
	public static final String COD_CONSULTAR_REL_TRAF        = "CONSULTAR_REL_TRAF";
	public static final String CONS_BASE_APARELHOS_GSM       = "CONS_BASE_APARELHOS_GSM";
	public final String COD_CONSULTAR_BASE_APARELHOS		 = "CONSULTAR_BASE_APARELHOS";
	public final String COD_CONSULTAR_CRED_PULA_PULA		 = "CONSULTAR_CRED_PULA_PULA";
	public final String COD_CONSULTAR_MIGRACOES_PP			 = "CONSULTAR_MIGRACOES_PP";
	public final String COD_CONSULTAR_PROM_X_STATUS			 = "CONSULTAR_PROM_X_STATUS";
	public final String COD_CONSULTAR_TROCA_PROM_X_ST		 = "CONSULTAR_TROCA_PROM_X_ST";
	public final String COD_CONSULTAR_BONUS_PROM_FAIX		 = "COD_CONSULTAR_BONUS_PROM_FAIX";
	public final String COD_CONSULTAR_RESUMO_CRED_PP		 = "CONSULTAR_RESUMO_CRED_PP";
	public final String COD_CONSULTAR_SALDO_ASSINANTES		 = "CONSULTAR_SALDO_ASSINANTE";
	public final String COD_CONSULTAR_ESTORNO_EXPURGO		 = "CONSULTAR_ESTORNO_EXPURGO";
	public final String COD_CONSULTA_REC_PLAN_CONTROL		 = "CONSULTA_REC_PLAN_CONTROL";
	public final String	COD_CONSULTAR_EXPURGO_ESTORNO		 = "CONSULTAR_EXPURGO_ESTORNO";
	public final String	COD_CONSULTA_CONT_COD_SERVICO		 = "CONSULTA_CONT_COD_SERVICO";
	public final String COD_CONSULTA_PULA_PULA_2004_DETALHADO = "CONSULTA_PP2004_DETALHADO";
	public final String	COD_CONSULTAR_STATUS_ASS_OPT_IN		 = "CONSULTA_STATUS_ASS_OPTIN";
	public final String COD_CONSULTAR_REL_CHAMADAS_COBRAR    = "CONSULTA_CHAMADAS_COBRAR";
	public final String	COD_CONSULTAR_CATEG_ASS_OPT_IN		 = "CONSULTA_CATEG_ASS_OPTIN";
	public final String COD_CONSULTAR_REL_OPERACIONAL_OPT_IN = "CONSULTA_OPERACION_OPTIN";
	public final String COD_CONSULTA_GERENCIAL_OPTIN 		 = "CONSULTA_GERENCIAL_OPTIN";
	public final String CONSULTA_AMIGOS_TD_H 		 		 = "CONSULTA_AMIGOS_TD_H";
	public final String COD_CONSULTA_CONT_CHAMADAS_SAINTES	 = "CONSULTA_CHAMADAS_SAINTES";
	public final String COD_CONSULTA_CONT_ASS_RECARREGARAM	 = "CONSULTA_ASS_RECARREGARAM";
	public final String COD_CONSULTA_BONIFICACAO_CAMPANHA	 = "CONSULTA_BONIFICACAO_CAMP";
	public final String COD_CONSULTA_CONT_PREVIA_CONTABIL	 = "CONSULTA_PREVIA_CONT";
	public final String COD_ATUALIZACAO_BLACK_LIST			 = "ATUALIZACAO_BLACKLIST";
	public final String COD_CONSULTA_MOBILE_MARKETING		 = "CONSULTA_MOBILE_MARKETING";
	public final String COD_CONSULTA_DEMONSTRATIVO_BONUS	 = "CONSULTA_DEMONSTRAT_BONUS";
	public final String COD_CONSULTA_CONT_TIPODOC_FILIAL	 = "CONSULTAR_REL_TIPODOC_FIL";
	public final String COD_CONSULTA_COBRANCA_SERVICO_102	 = "CONSULTA_SERVICO_102";
	public final String COD_CONSULTA_ACOMPANHAMENTO_SALDOS	 = "CONSULTA_CON_ACOMP_SALDOS";
	public final String COD_CONSULTA_ERR_REC_CONTROLE		 = "CONSULTA_ERR_REC_CONTROLE";
	public final String COD_CONSULTAR_PCT_DADOS				 = "CONSULTAR_PACOTE_DADOS";
	public final String COD_CONSULTA_FALE_GRATIS_ORELHAO     = "ACAO_FALE_GRATIS_ORELHAO";
	public final String CONSULTA_CARTAO_RECARGA              = "CONSULTA_CARTAO_RECARGA";
	public final String CONSULTA_SUBIDA_TSD                  = "CONSULTA_SUBIDA_TSD";
	public final String ACAO_ADESAO_OFERTA_NATAL             = "ACAO_ADESAO_OFERTA_NATAL";
	public final String ACAO_ADES_OFERTA_NATAL_M             = "ACAO_ADES_OFERTA_NATAL_M";
	public static final String COD_CONSULTAR_BS_ABERTOS 	 = "CONSULTAR_BS_ABERTOS";

	// Relatórios Batch
	public final String COD_BAT_MKT_CLIENTES_SD			 	 = "BAT_MKT_CLIENTES_SD";

	// Operacoes referentes a Contestação
	public final String COD_CONSULTA_BS 					 = "CONSULTAR_BS";
	public final String COD_CONSULTA_HISTORICO_BS			 = "CONSULTAR_HISTORICO_BS";
	public final String COD_CONSULTA_NUMS_CORRELATOS		 = "CONSULTAR_NUMS_CORRELATOS";
	public final String COD_ABERTURA_BS						 = "ABRIR_BS";

	//Operacoes referentes ao Usuario
	public final String COD_MENU_USUARIO 					 = "MENU_USUARIO";
	public final String COD_CONSULTAR_USUARIO 				 = "CONSULTAR_USUARIO";
	public final String COD_GRAVAR_USUARIO 					 = "SALVAR_USUARIO";
	public final String COD_EXCLUIR_USUARIO 				 = "EXCLUIR_USUARIO";
	public final String COD_ALTERAR_USUARIO 				 = "ALTERAR_USUARIO";
	public final String COD_DESBLOQUEAR_USUARIO 			 = "DESBLOQUEAR_USUARIO";
	public final String COD_ALTERA_SENHA 					 = "ALTERA_SENHA";
	public final String COD_INICIALIZAR_SENHA 				 = "INICIALIZAR_SENHA";
	public final String COD_PERFIL_GSM                       = "PERFIL_GSM";
	public final String COD_PERFIL_LIGMIX                    = "PERFIL_LIGMIX";

	//Operacoes references a Consulta de assinantes Bloqueados
	public final String COD_CONSULTA_ASSINANTES_BLOQUEADOS 	= "CONSULTAR_ASSINANTES_BLOQUEADOS";

	//Acoes para cadastro de conteudo WIG e de servico SASC
	public final String COD_CONSULTAR_ANEXO_RESPOSTA		 = "CONSULTAR_ANEXO_RESPOSTA";
	public final String COD_CADASTRAR_ANEXO_RESPOSTA		 = "CADASTRAR_ANEXO_RESPOSTA";
	public final String COD_REMOVER_ANEXO_RESPOSTA		 	 = "REMOVER_ANEXO_RESPOSTA";
	public final String COD_CONSULTAR_PERFIS_SERVICO		 = "CONSULTAR_PERFIS_SERVICO";
	public final String COD_CADASTRAR_NOVO_SERV_SASC		 = "CADASTRAR_NOVO_SERV_SASC";
	public final String CADASTRAR_NOVO_SERV_SASC			 = "CADASTRAR_NOVO_SERV_SASC";

	//Codigos das Operacoes do Estorno/Expurgo de Bonus Pula-Pula
	public static final String COD_CARREGAR_ARQUIVO_DB 		= "CARREGAR_ARQUIVO_DB";
	public static final String COD_OBTER_ARQUIVO 			= "OBTER_ARQUIVO";
	public static final String COD_APROVAR_PREVIA 			= "APROVAR_PREVIA";
	public static final String COD_REJEITAR_PREVIA 			= "REJEITAR_PREVIA";
	public static final String COD_CADASTRAR_ORIGEM 		= "CADASTRAR_ORIGEM";
	public static final String COD_EXCLUIR_ORIGEM 			= "EXCLUIR_ORIGEM";
	public static final String COD_ATUALIZAR_ORIGEM 		= "ATUALIZAR_ORIGEM";

	//Codigos das Operacoes do Sistema de Tarifação
	public static final String COD_CADASTRAR_ALIQUOTA			= "CADASTRAR_ALIQUOTA";
	public static final String COD_REMOVER_ALIQUOTA				= "REMOVER_ALIQUOTA";
	public static final String COD_CADASTRAR_DESTINO			= "CADASTRAR_DESTINO";
	public static final String COD_REMOVER_DESTINO				= "REMOVER_DESTINO";
	public static final String COD_ALTERAR_DESTINO				= "ALTERAR_DESTINO";
	public static final String COD_CADASTRAR_LAC				= "CADASTRAR_LAC";
	public static final String COD_REMOVER_LAC					= "REMOVER_LAC";
	public static final String COD_ALTERAR_LAC					= "ALTERAR_LAC";
	public static final String COD_CADASTRAR_PAIS				= "CADASTRAR_PAIS";
	public static final String COD_REMOVER_PAIS					= "REMOVER_PAIS";
	public static final String COD_CADASTRAR_PROPRIEDADE		= "CADASTRAR_PROPRIEDADE";
	public static final String COD_REMOVER_PROPRIEDADE			= "REMOVER_PROPRIEDADE";
	public static final String COD_ALTERAR_PROPRIEDADE			= "ALTERAR_PROPRIEDADE";
	public static final String COD_CADASTRAR_SERVICO			= "CADASTRAR_SERVICO";
	public static final String COD_REMOVER_SERVICO				= "REMOVER_SERVICO";
	public static final String COD_ALTERAR_SERVICO				= "ALTERAR_SERVICO";
	public static final String COD_CADASTRAR_DESCONTO			= "CADASTRAR_DESCONTO";
	public static final String COD_REMOVER_DESCONTO				= "REMOVER_DESCONTO";
	public static final String COD_ALTERAR_DESCONTO				= "ALTERAR_DESCONTO";
	public static final String COD_CADASTRAR_TIPO_CATEGORIA		= "CADASTRAR_TIPO_CATEGORIA";
	public static final String COD_REMOVER_TIPO_CATEGORIA		= "REMOVER_TIPO_CATEGORIA";
	public static final String COD_ALTERAR_TIPO_CATEGORIA		= "ALTERAR_TIPO_CATEGORIA";
	public static final String COD_CADASTRAR_TIPO_EVENTO		= "CADASTRAR_TIPO_EVENTO";
	public static final String COD_REMOVER_TIPO_EVENTO			= "REMOVER_TIPO_EVENTO";
	public static final String COD_ALTERAR_TIPO_EVENTO			= "ALTERAR_TIPO_EVENTO";
	public static final String COD_CADASTRAR_TIPO_DESTINO		= "CADASTRAR_TIPO_DESTINO";
	public static final String COD_REMOVER_TIPO_DESTINO			= "REMOVER_TIPO_DESTINO";
	public static final String COD_ALTERAR_TIPO_DESTINO			= "ALTERAR_TIPO_DESTINO";
	public static final String COD_CADASTRAR_TIPO_OPERADORA		= "CADASTRAR_TIPO_OPERADORA";
	public static final String COD_REMOVER_TIPO_OPERADORA		= "REMOVER_TIPO_OPERADORA";
	public static final String COD_ALTERAR_TIPO_OPERADORA		= "ALTERAR_TIPO_OPERADORA";
	public static final String COD_CADASTRAR_GRUPO_BONIFICACAO	= "CADASTRAR_GRUPO_BONIFICAC";
	public static final String COD_REMOVER_GRUPO_BONIFICACAO	= "REMOVER_GRUPO_BONIFICACAO";
	public static final String COD_ALTERAR_GRUPO_BONIFICACAO	= "ALTERAR_GRUPO_BONIFICACAO";
	public static final String COD_CADASTRAR_PLANO_TARIFACAO	= "CADASTRAR_PLANO_TARIFACAO";
	public static final String COD_REMOVER_PLANO_TARIFACAO		= "REMOVER_PLANO_TARIFACAO";
	public static final String COD_ALTERAR_PLANO_TARIFACAO		= "ALTERAR_PLANO_TARIFACAO";
	public static final String COD_CADASTRAR_DIA_SEMANA			= "CADASTRAR_DIA_SEMANA";
	public static final String COD_REMOVER_DIA_SEMANA			= "REMOVER_DIA_SEMANA";
	public static final String COD_ALTERAR_DIA_SEMANA			= "ALTERAR_DIA_SEMANA";
	public static final String COD_CADASTRAR_VALOR_DESLOCAMENTO	= "CADASTRAR_VALOR_DESLOCAME";
	public static final String COD_REMOVER_VALOR_DESLOCAMENTO	= "REMOVER_VALOR_DESLOCAMEN";
	public static final String COD_ALTERAR_VALOR_DESLOCAMENTO	= "ALTERAR_VALOR_DESLOCAMEN";
	public static final String COD_CADASTRAR_OPERADORA_GSM		= "CADASTRAR_OPERADORA_GSM";
	public static final String COD_REMOVER_OPERADORA_GSM		= "REMOVER_OPERADORA_GSM";
	public static final String COD_ALTERAR_OPERADORA_GSM		= "ALTERAR_OPERADORA_GSM";
	public static final String COD_CADASTRAR_BONIFICACAO_PLANO	= "CADASTRAR_BONIFICACAO_PLA";
	public static final String COD_REMOVER_BONIFICACAO_PLANO	= "REMOVER_BONIFICACAO_PLANO";
	public static final String COD_ALTERAR_BONIFICACAO_PLANO	= "ALTERAR_BONIFICACAO_PLANO";
	public static final String COD_CADASTRAR_DESCONTO_PLANO		= "CADASTRAR_DESCONTO_PLANO";
	public static final String COD_REMOVER_DESCONTO_PLANO		= "REMOVER_DESCONTO_PLANO";
	public static final String COD_ALTERAR_DESCONTO_PLANO		= "ALTERAR_DESCONTO_PLANO";
	public static final String COD_CADASTRAR_TIPO_CHAMADA		= "CADASTRAR_TIPO_CHAMADA";
	public static final String COD_REMOVER_TIPO_CHAMADA			= "REMOVER_TIPO_CHAMADA";
	public static final String COD_ALTERAR_TIPO_CHAMADA			= "ALTERAR_TIPO_CHAMADA";
	public static final String COD_CADASTRAR_VALOR_CHAMADA		= "CADASTRAR_TIPO_CHAMADA";
	public static final String COD_REMOVER_VALOR_CHAMADA		= "REMOVER_VALOR_CHAMADA";
	public static final String COD_ALTERAR_VALOR_CHAMADA		= "ALTERAR_VALOR_CHAMADA";

	// Codigos das Operacoes de Recarga em Massa
	public static final String COD_UPLOAD_RECARGA_MASSA 		= "UPLOAD_RECARGA_MASSA";
	public static final String COD_DOWNLOAD_RECARGA_MASSA 		= "DOWNLOAD_RECARGA_MASSA";
	public static final String COD_IMPORTACAO_RECARGA_MASSA		= "IMPORTACAO_RECARGA_MASSA";
	public static final String COD_APROVAR_LT_RECARGA 			= "APROVAR_LT_RECARGA_MASSA";
	public static final String COD_REJEITAR_LT_RECARGA 			= "REJEITAR_LT_RECARGA_MASSA";

	// Codigos das Operacoes de Gerenciamento GPP
	public static final String COD_CADASTRAR_SERVICO_SFA 		= "CADASTRAR_SERVICO_SFA";
	public static final String COD_ALTERAR_SERVICO_SFA 			= "ALTERAR_SERVICO_SFA";
	public static final String COD_CADASTRAR_VINCULACAO_SFA		= "CADASTRAR_VINCULACAO_SFA";
	public static final String COD_REMOVER_VINCULACAO_SFA 		= "REMOVER_VINCULACAO_SFA";
	public static final String COD_CADASTRAR_ORIGEM_RECARGA 	= "CADASTRAR_ORIGEM_RECARGA";
	public static final String COD_ALTERAR_ORIGEM_RECARGA 		= "ALTERAR_ORIGEM_RECARGA";
	public static final String COD_CADASTRAR_CANAL 				= "CADASTRAR_CANAL";
	public static final String COD_CADASTRAR_VINCULACAO_SFA_CDR = "CADASTRAR_VINC_SFA_CDR";
	public static final String COD_REMOVER_VINCULACAO_SFA_CDR 	= "REMOVER_VINC_SFA_CDR";
	public static final String COD_CADASTRAR_ORIGEM_CDR 		= "CADASTRAR_ORIGEM_CDR";
	public static final String COD_ALTERAR_ORIGEM_CDR			= "ALTERAR_ORIGEM_CDR";

	// Constantes usadas nos relatórios batch
	public static final String STATUS_NAO_PROCESSADO	= "N";
	public static final String STATUS_VALIDACAO			= "V";
	public static final String CONSULTA_TEMPORARIA 		= "T";
	public static final String DIRETORIO_RELATORIOS 	= "diretorioRelatorios";
	public static final String ENDERECO_PORTAL			= "enderecoPortal";
	public static final String EXTENSAO_ZIP 			= ".zip";
	public static final String EXTENSAO_TXT 			= ".txt";

	// Mensagens de Status
	public static final String STATUS_SUCESSO 	= "success";
	public static final String STATUS_ERRO 		= "error";

	// Safra Plano Controle
	public static final String SAFRA_NOVA 		= "1";
	public static final String SAFRA_ANTIGA		= "0";

	// Tipos de Transacao Controle
	public static final String FRANQUIA_CONTROLE			= "04005";
	public static final String BONUS_CONTROLE				= "08004";
	public static final String EXPIRACAO_CREDITOS 			= "06027";
	public static final String EXPIRACAO_BONUS				= "06019";
	public static final String EXPIRACAO_FRANQUIA_CONTROLE	= "06037";
	public static final String EXPIRACAO_BONUS_CONTROLE		= "06038";

	// Tipos de Concessões do Controle
	public static final char CONCESSAO_FRANQUIA				= 'F';
	public static final char CONCESSAO_BONUS				= 'B';
	public static final char CONCESSAO_BONUS_FRANQUIA 		= 'D';

	// Constantes para definicao do parâmentro que mostra a tela de login
	public final String IDT_MOSTRA_LOGIN 				= "MostraLogin";

	// Nome dos arquivos de do processo de importação de preço de aparelho
	public final String ARQUIVO_IMPORTACAO_PRECO		= "PrecoAparelho";
	public final String ARQUIVO_ERRO_IMPORTACAO			= "ErroRegistroPreco";

	public final String IND_MODIFICA_PRECO				= "1";

	// Nome dos arquivos do processo de cadastro na Black List
	public final String ARQUIVO_ERRO_BLACK_LIST			= "ErroRegistroBlackList";


	/*** Constantes utilizadas no Upload do arquivo de carga do Fornecedor da BrT *****/

	/** Array com os campos do Cabeçalho da Nota Fiscal. **/
	public static final String[] FIELDS_NC = {"cnpjFornecedor","inscricaoMunicipal","numeroDoc",
												  "serie","dataEmissao","periodoRealizacao","numeroContrato", "numeroProjeto",
												  "cnpjFilial","cnpjEmissorFatura","valorTotal","encaminhadoPara","codigoCNL",
												  "codigoCFOP","observacao"};

	/** Array com os campos do Item da Nota Fiscal. **/
	public static final String[] FIELDS_NI = {"numPedidoCompra","numeroItem","codServFornecedor","descricao",
												  "quantidade","valorTotalImposto","aliquotaISS","baseCalculoISS","valorISS"};

	/** Array com os campos do Cabeçalho da Fatura. **/
	public static final String[] FIELDS_FC = {"cnpjFornecedor","numeroDoc","dataEmissao","valorTotal",
												  "encaminhamento","descricao","observacao"};

	/** Array com os campos do Item da Fatura. **/
	public static final String[] FIELDS_FI = {"numReferencia","dataEmissao","valorReferencia"};

	/** Array com os campos do Sumário do arquivo do fornecedor. */
	public static final String[] FIELDS_SU = {"layout","qtde","versao"};

	/** Constante que descreve o Cabeçalho da Nota Fiscal. */
	public static final String CABECALHO_NOTA = "NC";

	/** Constante que descreve os Itens da Nota Fiscal. */
	public static final String ITENS_NOTA = "NI";

	/** Constante que descreve o Cabeçalho da Fatura. */
	public static final String CABECALHO_FATURA = "FC";

	/** Constante que descreve os Itens da Fatura. */
	public static final String ITENS_FATURA = "FI";

	/** Constante que descreve o Sumário. */
	public static final String SUMARIO = "SU";

	//	 Limite de assinantes
	public static final int LIMITEFF_PREPAGO = 7;
	public static final int LIMITEFF_HIBRIDO = 14;

	/*** Fim das contantes utilizadas no Upload ***/
	public static final String DATASOURCE_NAME = "DATASOURCE_NAME";

	/** Pastas da Fábrica de Relatorios */
	public static final String PATH_FABRICA_REL					 = "pathFabricaRel"; // Configurar no web.xml
	public static final String PATH_FABRICA_REL_ENTRADA			 = "ENTRADA";
	public static final String PATH_FABRICA_REL_SAIDA			 = "SAIDA";
	public static final String PATH_FABRICA_REL_ERRO			 = "ERRO";
	public static final String PATH_FABRICA_REL_HISTORICO		 = "HISTORICO";

	/** Pastas do sistema de Estorno/Expurgo de Bonus Pula-Pula */
	public static final String PATH_ESTORNO_EXPURGO_PP			 = "pathEstornoExpurgoPP";	// Configurar no web.xml
	public static final String PATH_ESTORNO_EXPURGO_PP_ENTRADA	 = "ENTRADA";
	public static final String PATH_ESTORNO_EXPURGO_PP_SAIDA	 = "SAIDA";
	public static final String PATH_ESTORNO_EXPURGO_PP_TEMP		 = "TEMP";
	public static final String PATH_ESTORNO_EXPURGO_PP_ERROS	 = "ERROS";
	public static final String PATH_ESTORNO_EXPURGO_PP_PREVIAS	 = "PREVIAS";
	public static final String PATH_ESTORNO_EXPURGO_PP_EFETIVADO = "EFETIVADO";
	public static final String PATH_ESTORNO_EXPURGO_PP_HISTORICO = "HISTORICO";

	/** Pastas do sistema de Recarga em Massa */
	public static final String PATH_RECARGA_MASSA			 = "pathRecargaMassa";	// Configurar no web.xml
	public static final String PATH_RECARGA_MASSA_ENTRADA	 = "ENTRADA";
	public static final String PATH_RECARGA_MASSA_SAIDA	 	 = "SAIDA";
	public static final String PATH_RECARGA_MASSA_TEMP		 = "TEMP";
	public static final String PATH_RECARGA_MASSA_ERROS	 	 = "ERROS";

	/** Constantes Fale de Graça à Noite (Fale Gratis, FGN) */
	public static final String PROMOCAO_TIPO_EXECUCAO			 = "FALEGRATIS";

	/** Categorias Promoções */
	public static final int 	CATEGORIA_PULA_PULA			 	= 1;
	public static final int 	CATEGORIA_PROMOCOES_REGIONAIS   = 2;
	public static final int 	CATEGORIA_BUMERANGUE_14		 	= 3;
	public static final int 	CATEGORIA_PULA_PULA_POSPAGO		= 4;

	//	Codigos das Operacoes da Fabrica de Relatorios
	public static final String COD_DOWNLOAD_ENTRADA_RELATORIO_TESTE	= "DOWNLOAD_ENTRADA_REL_TES";
	public static final String COD_DOWNLOAD_SAIDA_RELATORIO_TESTE	= "DOWNLOAD_SAIDA_REL_TESTE";
	public static final String COD_UPLOAD_ENTRADA_RELATORIO_TESTE	= "UPLOAD_ENTRADA_REL_TESTE";
}