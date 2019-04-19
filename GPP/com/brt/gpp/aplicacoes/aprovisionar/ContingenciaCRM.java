package com.brt.gpp.aplicacoes.aprovisionar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.CabecalhoXMLApr;
import com.brt.gpp.aplicacoes.aprovisionar.GerarXMLAprovisionamento;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.aplicacoes.aprovisionar.ElementoXMLApr;
import com.brt.gpp.aplicacoes.aprovisionar.ParserXMLApr;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.socket.GPPSocketCliente;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Collection;
import java.text.DecimalFormat;
import java.sql.ResultSet;
import java.sql.Timestamp;


/**
  * Esta classe gerencia o tratamento da contingencia com o sistema de
  * relacionamento com o cliente CRM. Esta possui metodos de contingencia
  * para ativar/desativar servicos de aprovisionamento do assinante tendo
  * uma interface com o sistema responsavel (ASAP)
  *   
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				23/08/2004
  *
  * Modificado por: Joao Carlos
  * Data: 05/10/2004
  * Razao: Pedido por Luciano Vilela para envio da categoria Pre-Pago para todos
  *        os assinantes que forem desbloqueados pelo sistema GPP.
  *
  * Modificado por: Joao Carlos
  * Data: 06/10/2004
  * Razao: Retirada da categoria fixa.
  */

public class ContingenciaCRM extends Aplicacoes
{
	private GerentePoolBancoDados gerBancoDados;

	/**
	 * Metodo....:ContingenciaCRM
	 * Descricao.:Construtor da classe
	 * @param idProcesso - Id do processo
	 */
	public ContingenciaCRM(long idProcesso)
	{
		super(idProcesso,Definicoes.CL_IMPORTACAO_DADOS_CDR);
		gerBancoDados = GerentePoolBancoDados.getInstancia(idProcesso);		
	}

	/**
	 * Metodo....:bloquearServicos
	 * Descricao.:Este metodo realiza o bloqueio de servicos para um assinante realizando
	 *            a pesquisa da categoria deste primeiro
	 * @param msisdn	- MSISDN do assinante
	 * @return long		- ID da OS gerada do aprovisionamento
	 * @throws GPPInternalErrorException
	 */
	public long bloquearServicos(String msisdn) throws GPPInternalErrorException
	{
		return bloquearServicos(msisdn,getCategoriaAssinante(msisdn)); 
	}
	
	/**
	 * Metodo....:bloquearServicos
	 * Descricao.:Este metodo realiza o bloqueio de servicos do assinante
	 * @param msisdn	- MSISDN a ser bloqueado
	 * @param categoria - Categoria do assinante
	 * @return long		- Numero do identificador do registro do bloqueio 
	 * @throws GPPInternalErrorException
	 */
	public long bloquearServicos(String msisdn, String categoria) throws GPPInternalErrorException
	{
		// Busca o proximo id da OS a ser utilizado. Nova OS
		long numeroOS = getProximoValorId();
		// Envia o XMl de bloqueio do servico
		bloquearServicos(numeroOS, msisdn,categoria);
		// Retorna o ID da OS criado
		return numeroOS;
	}

	/**
	 * Metodo....:bloquearServicos
	 * Descricao.:Este metodo realiza o bloqueio de servicos do assinante
	 * @param idOS		- Identificador da OS a ser utilizada
	 * @param msisdn	- MSISDN a ser bloqueado
	 * @throws GPPInternalErrorException
	 */
	public void bloquearServicos(long idOS, String msisdn, String categoria) throws GPPInternalErrorException
	{
		// Para enviar a solicitacao de bloqueio de servicos ao ASAP e preciso
		// primeiro gerar um XML contendo as informacoes de quais servicos
		// serao bloqueados. Portanto primeiro cria-se os elementos necessarios
		// para entao gerar o XML e entao enviar para a classe de socket de conexao com o ASAP
		super.log(Definicoes.INFO,"bloquearServicos","Inicio OS "+idOS+" MSISDN "+msisdn+" Categoria "+categoria);
		// Define dados do cabecalho
		CabecalhoXMLApr cabecalhoXML = new CabecalhoXMLApr(formataIdOSAprovisionamento(idOS),
														   Definicoes.XML_OS_CASE_TYPE_BLOQUEIO,
														   Definicoes.XML_OS_CASE_TYPE_BLOQUEIO,
														   categoria,
														   Definicoes.XML_OS_ORDER_HIGH_PRIORITY,
														   msisdn);
		
		// Define os elementos
		LinkedList elementos = new LinkedList();
		ElementoXMLApr eleBloqueioGeral = new ElementoXMLApr(Definicoes.SERVICO_INFO_SIMCARD,
															 Definicoes.XML_OS_OPERACAO_BLOQUEAR,
															 Definicoes.XML_OS_STATUS,
															 msisdn);

		ElementoXMLApr eleBloqueioSMS   = new ElementoXMLApr(Definicoes.SERVICO_BLACK_LIST,
		                                                     Definicoes.XML_OS_OPERACAO_BLOQUEAR,
															 Definicoes.XML_OS_STATUS,
															 msisdn);
		elementos.add(eleBloqueioGeral);
		elementos.add(eleBloqueioSMS);
		
		// Realiza o envio para o servidor de socket
		enviaParaServidorSocket(cabecalhoXML,elementos);
		super.log(Definicoes.INFO,"bloquearServicos","Fim");
		
	}

	/**
	 * Metodo....:desativarHotLine
	 * Descricao.:Este metodo realiza o desbloqueio HotLine realizando primeiro
	 *            a consulta da categoria do assinante
	 * @param msisdn	- MSISDN a ter o Hot Line desabilitado
	 * @throws GPPInternalErrorException
	 */
	public long desativarHotLine(String msisdn) throws GPPInternalErrorException
	{
		return desativarHotLine(msisdn,getCategoriaAssinante(msisdn));
	}
	
	/**
	 * Metodo....:desativarHotLine
	 * Descricao.:Este metodo realiza o desbloqueio HotLine sem realizar a busca
	 *            da categoria do assinante, ja que esta e passada como parametro
	 * @param msisdn		- MSISDN do assinante
	 * @param categoria		- Categoria do assinante
	 * @return long			- ID da OS gerada
	 * @throws GPPInternalErrorException
	 */
	public long desativarHotLine(String msisdn, String categoria) throws GPPInternalErrorException
	{
		// Este metodo e utilizado quando a OS e criada. Para isto o metodo retorna
		// o numero que foi utilizado para envio do XML.
		long numeroOS = getProximoValorId();
		// Envia o XML de desativacao para o ASAP
		desativarHotLine(numeroOS,msisdn,categoria);
		// Retorna o numero da OS criado
		return numeroOS;
	}

	/**
	 * Metodo....:desativarHotLineContingencia
	 * Descricao.:Este metodo realiza a requisicao de desativacao do HotLine para
	 *            o assinante. Este metodo e utilizado para criar a OS do servico
	 * @param msisdn	- MSISDN a ter o Hot Line desabilitado
	 * @throws GPPInternalErrorException
	 */
	public long desativarHotLineContingencia(String msisdn) throws GPPInternalErrorException
	{
		// Este metodo e utilizado quando a OS e criada. Para isto o metodo retorna
		// o numero que foi utilizado para envio do XML.
		long numeroOS = getProximoValorId();
		
		// Envia o XML de desativacao para o ASAP
		desativarHotLineContingencia(numeroOS,msisdn);
		
		// Retorna o numero da OS criado
		return numeroOS;
	}

	/**
	 * Metodo....:desativarHotLine
	 * Descricao.:Este metodo realiza a requisicao de desativacao do HotLine para
	 *            o assinante
	 * @param idOS		- Identificador da OS a ser enviada
	 * @param msisdn	- MSISDN a ter o Hot Line desabilitado
	 * @throws GPPInternalErrorException
	 */
	public void desativarHotLine(long idOS, String msisdn, String categoria) throws GPPInternalErrorException
	{
		// Para enviar a solicitacao de bloqueio de servicos ao ASAP e preciso
		// primeiro gerar um XML contendo as informacoes de quais servicos
		// serao bloqueados. Portanto primeiro cria-se os elementos necessarios
		// para entao gerar o XML e entao enviar para a classe de socket de conexao com o ASAP
		super.log(Definicoes.INFO,"desativarHotLine","Inicio OS "+idOS+" MSISDN "+msisdn+" Categoria "+categoria);
		// ALTERACAO TEMPORARIA - ENVIO FIXO DA CATEGORIA PRE-PAGO
		categoria = Definicoes.XML_OS_CATEGORIA_PREPAGO;

		// Define dados do cabecalho 
		CabecalhoXMLApr cabecalhoXML = new CabecalhoXMLApr(formataIdOSAprovisionamento(idOS),
														   Definicoes.XML_OS_CASE_TYPE_BLOQUEIO,
														   Definicoes.XML_OS_CASE_TYPE_BLOQUEIO,
														   categoria,
														   Definicoes.XML_OS_ORDER_HIGH_PRIORITY,
														   msisdn);
		
		// Define os elementos
		LinkedList elementos = new LinkedList();
		ElementoXMLApr eleBloqueioGeral = new ElementoXMLApr(Definicoes.SERVICO_INFO_SIMCARD,
															 Definicoes.XML_OS_OP_SIMCARD,
															 Definicoes.XML_OS_STATUS,
															 msisdn);

		ElementoXMLApr eleBloqueioSMS   = new ElementoXMLApr(Definicoes.SERVICO_BLOQ_HOTLINE,
															 Definicoes.XML_OS_BLOQUEAR,
															 Definicoes.XML_OS_STATUS,
															 msisdn);
		elementos.add(eleBloqueioGeral);
		elementos.add(eleBloqueioSMS);

		// Realiza o envio para o servidor de socket
		enviaParaServidorSocket(cabecalhoXML,elementos);
		super.log(Definicoes.INFO,"desativarHotLine","Fim");

	}

	/**
	 * Metodo....:desativarHotLineContingencia
	 * Descricao.:Este metodo realiza a requisicao de desativacao do HotLine para
	 *            o assinante
	 * @param idOS		- Identificador da OS a ser enviada
	 * @param msisdn	- MSISDN a ter o Hot Line desabilitado
	 * @throws GPPInternalErrorException
	 */
	public void desativarHotLineContingencia(long idOS, String msisdn) throws GPPInternalErrorException
	{
		// Para enviar a solicitacao de bloqueio de servicos ao ASAP e preciso
		// primeiro gerar um XML contendo as informacoes de quais servicos
		// serao bloqueados. Portanto primeiro cria-se os elementos necessarios
		// para entao gerar o XML e entao enviar para a classe de socket de conexao com o ASAP
		super.log(Definicoes.INFO,"desativarHotLineContingencia","Inicio OS "+idOS+" MSISDN "+msisdn);
		// Define dados do cabecalho 
		String 	categoria 	= getCategoriaAssinante(msisdn);

		CabecalhoXMLApr cabecalhoXML = new CabecalhoXMLApr(formataIdOSAprovisionamento(idOS),
														   Definicoes.XML_OS_CASE_TYPE_DESBLOQUEIO,
														   Definicoes.XML_OS_CASE_TYPE_DESBLOQUEIO,
														   categoria,
														   Definicoes.XML_OS_ORDER_HIGH_PRIORITY,
														   msisdn);
		
		// Define os elementos
		LinkedList elementos = new LinkedList();
		ElementoXMLApr eleBloqueioGeral = new ElementoXMLApr(Definicoes.SERVICO_INFO_SIMCARD,
															 Definicoes.XML_OS_OPERACAO_DESBLOQUEAR,
															 Definicoes.XML_OS_STATUS,
															 msisdn);

		elementos.add(eleBloqueioGeral);

		// Realiza o envio para o servidor de socket
		enviaParaServidorSocket(cabecalhoXML,elementos);
		super.log(Definicoes.INFO,"desativarHotLineContingencia","Fim");
		
	}

	/**
	 * Metodo....:enviaParaServidorSocket
	 * Descricao.:Este metodo realiza o envio do XML de aprovisionamento para 
	 *            o servidor socket GPP que entao envia para o sistema de aprovisionamento
	 *            (ASAP). Este metodo realiza a escrita no socket sendo que o retorno nao
	 *            e considerado.
	 * @param cabecalhoXML	- Cabecalho XML que sera enviado para o aprovisionamento
	 * @param elementosXML	- Elementos que compoem o XML a ser enviado para o aprovisionamento
	 * @throws GPPInternalErrorException
	 */
	private void enviaParaServidorSocket(CabecalhoXMLApr cabecalhoXML, Collection elementosXML) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"enviaParaServidorSocket","Inicio");
		try
		{
			// Busca referencia para o arquivo de configuracao do GPP
			ArquivoConfiguracaoGPP arqConf = ArquivoConfiguracaoGPP.getInstance();

			// Cria referencia da classe para gerar o XML necessario a ser enviado
			// ao ASAP
			GerarXMLAprovisionamento xmlAprov = new GerarXMLAprovisionamento(cabecalhoXML, elementosXML);

			// Busca referencia para o cliente socket para comunicacao
			// com o ASAP e envio da requisicao
			// Realiza a conexao e abertura do socket
			GPPSocketCliente socket = new GPPSocketCliente(arqConf.getNomeServidorSocket(),arqConf.getPortaServidorSocket());

			// Inicia o Hand-Shake com o servidor de socket
			socket.writeString(Definicoes.STR_INICIO_HANDSHAKE_SOCKET);
			
			if (socket.readString().equals(Definicoes.STR_CONFIRMACAO_HANDSHAKE_SOCKET))
			{
				// Envia para o socket o XML de aprovisionamento
				// A mensagem a ser enviada vai com um identificador no cabecalho desta
				// para que o servidor socket saiba que esta e uma requisicao para o ASAP
				socket.writeString(Definicoes.CABECALHO_XML_APROVISIONAMENTO+xmlAprov.getAprXML());
				//System.out.println(Definicoes.CABECALHO_XML_APROVISIONAMENTO+xmlAprov.getAprXML());
			}
			// Fecha o socket e entao a conexao
			socket.close();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"enviaParaServidorSocket","Erro ao enviar XML de aprovisionamento."+e);
			throw new GPPInternalErrorException(e.getMessage());
		}
		super.log(Definicoes.DEBUG,"enviaParaServidorSocket","Fim");

	}

	/**
	 * Metodo....:confirmaBloqueioDesbloqueioServicos
	 * Descricao.:Este metodo recebe o XML de confirmacao do sistema de aprovisionamento
	 *            acertando no registro de bloqueio/desbloqueio que a acao foi confirmada
	 *            por este sistema
	 * @param xmlAprovisionamento
	 * @throws GPPInternalErrorException
	 */
	public void confirmaBloqueioDesbloqueioServicos(String xmlAprovisionamento) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"confirmaBloqueioDesbloqueioServicos","Inicio");
		try
		{
			// Cria uma referencia para a classe que realizara o parse do xml de retorno
			// do sistema de aprovisionamento e retorna uma objeto cabecalho
			// contendo os valores informados do bloqueio/desbloqueio 
			ParserXMLApr xmlParser = new ParserXMLApr();
			CabecalhoXMLApr cabecalhoXML = xmlParser.parseCabecalhoXMLBloqueio(xmlAprovisionamento);
			
			// Busca o ID da OS no cabecalho do XML e verifica se este contem a string GPP
			// que e enviada para o sistema ao bloquear o servico. Caso a string exista entao
			// o numero apos esta e o numero da atividade gravado no sistema
			String idOs 		= cabecalhoXML.getIdOs();
			String idAtividade 	= idOs;
			if (idOs.indexOf(Definicoes.SO_GPP) > -1)
			{
				idAtividade = idOs.substring( idOs.indexOf(Definicoes.SO_GPP)+Definicoes.SO_GPP.length(),idOs.length() );
			}
			
			// Acerta o status da atividade (Contingencia CRM gravada) na base de dados
			// indicando a confirmacao da execucao do servico
			acertaStatusAtividade(Long.parseLong(idAtividade),
					              Definicoes.STATUS_BLOQUEIO_CONCLUIDO,
								  cabecalhoXML.getCodigoErro(),
								  cabecalhoXML.getStatusProcessamento());
		}
		catch(GPPBadXMLFormatException bad)
		{
			super.log(Definicoes.WARN,"confirmaBloqueioDesbloqueioServicos","XML mal formatado. Erro:"+bad.getMessage());
			throw new GPPInternalErrorException("XML nao esta formatado corretamento. Erro:"+bad.getMessage()+" XML:"+xmlAprovisionamento);
		}
		super.log(Definicoes.DEBUG,"confirmaBloqueioDesbloqueioServicos","Fim");
	}

	/**
	 * Metodo....:formataIdOSOSAprovisionamento
	 * Descricao.:Format o numero da ID da OS a ser enviada ao aprovisionamento
	 * @param  idOS		- Numero da OS a ser formatada
	 * @return String 	- Numero da os a ser enviado para o ASAP
	 * @throws GPPInternalErrorException
	 */
	private String formataIdOSAprovisionamento(long idOS) throws GPPInternalErrorException
	{
		DecimalFormat decFormat = new DecimalFormat("0000000000000");
		return Definicoes.SO_GPP + decFormat.format(idOS);
	}

	/**
	 * Metodo....:getProximoValorId
	 * Descricao.:Este metodo retorna o proximo valor da sequencia
	 *            de identificacao do Id do aprovisionamento. Este
	 *            valor e de extrema necessidade para o XML a ser
	 *            enviado ao sistema de aprovisionamento (ASAP)
	 * 
	 * 	Obs: Caso a sequence nao seja encontrada ou algum outro erro ocorrer
	 *       entao o metodo retorna o valor 0
	 * @return long - Proximo valor do Id
	 * @throws GPPInternalErrorException
	 */	
	private long getProximoValorId() throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"getProximoValorId","Inicio");
		long valorId = 0;
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep  = gerBancoDados.getConexaoPREP(super.getIdLog());
			String sql   = "SELECT SEQ_OS_PROVISION.NEXTVAL AS NEXTVAL FROM DUAL";
			ResultSet rs = conexaoPrep.executaQuery(sql,super.getIdLog());
			if (rs.next())
			{
				valorId = rs.getLong("NEXTVAL");
			}
		}
		catch(java.sql.SQLException e)
		{
			super.log(Definicoes.WARN,"getProximoValorId","Erro ao buscar o valor da sequencia de aprovisionamento. Erro:"+e);
		}
		finally
		{
			gerBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"getProximoValorId","Fim");
		return valorId;
	}

	/**
	 * Metodo....:getCategoriaAssinante
	 * Descricao.:Este metodo retorna a categoria definida para o assinante
	 *            A categoria define se este assinante e Pos-Pago, Pre-Pago ou plano Hibrido
	 * @param msisdn	- Assinante a ser pesquisado
	 * @return String	- Identificacao da categoria
	 * @throws GPPInternalErrorException
	 */	
	public String getCategoriaAssinante(String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"getCategoriaAssinante","Inicio MSISDN "+msisdn);
		// A categoria padrao e a categoria de pos pago que e definido
		// caso o assinante nao exista na plataforma de pre-pago. Caso
		// o assinante exista entao a categoria e definida baseando
		// se este possui plano hibrido ou nao
		String categoria=Definicoes.XML_OS_CATEGORIA_POSPAGO;

		// Busca a referencia para a classe de consulta do assinante na tecnomen
		// e entao com o objeto contendo as informacoes deste realiza o teste
		// para identificar em qual categoria este pertence
		ConsultaAssinante 	consulta  = new ConsultaAssinante(super.getIdLog());
		Assinante 			assinante = consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
		
		if (assinante.isPrePago())
		{
			categoria=Definicoes.XML_OS_CATEGORIA_PREPAGO;
		} 
		else
		{
			if (assinante.isHibrido())
			{
				categoria=Definicoes.XML_OS_CATEGORIA_HIBRIDO;
			}
		}
		super.log(Definicoes.DEBUG,"getCategoriaAssinante","Fim");
		return categoria;
	}

	/**
	 * Metodo....:acertaStatusAtividade
	 * Descricao.:Este metodo realiza o acerto do status da atividade de bloqueio/desbloqueio
	 *            de acordo com o parametro informado
	 * @param idAtividade		- Atividade a ter seu status alterado
	 * @param statusAtividade	- Status da atividade
	 * @param codErro			- Codigo de erro do processamento do ASAP
	 * @param statusProc		- Status do processamento
	 * @throws GPPInternalErrorException
	 */	
	private void acertaStatusAtividade(long idAtividade,String statusAtividade, String codErro, String statusProc) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"acertaStatusAtividade","Inicio ATIVIDADE "+idAtividade);
		PREPConexao conexaoPrep=null;
		conexaoPrep  = gerBancoDados.getConexaoPREP(super.getIdLog());
		String sql = "UPDATE TBL_EXT_CONTINGENCIA_CRM " +
		                "SET IDT_STATUS_ATIVIDADE     = ?, " +
						    "COD_RETORNO              = ?, " +
							"IDT_STATUS_PROCESSAMENTO = ?  " +
					  "WHERE ID_ATIVIDADE = ?";
		Object param[] = {statusAtividade,codErro,statusProc,new Long(idAtividade)};
		conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
		gerBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG,"acertaStatusAtividade","Fim");
	}
	
	/**
	 * Metodo....:getPlanoPrecoAssinante
	 * Descricao.:Retorna o plano de preco definido para o assinante realizando uma consulta na Tecnomen
	 * @param msisdn	- MSISDN do assinante
	 * @return int		- Plano de preco do assinante ou fixo para os assinantes pos-pago
	 * @throws GPPInternalErrorException
	 */
	public int getPlanoPrecoAssinante(String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"getPlanoPrecoAssinante","Inicio MSISDN "+msisdn);
		int planoPreco = 0;

		// Busca a referencia para a classe de consulta do assinante na tecnomen
		ConsultaAssinante 	consulta  = new ConsultaAssinante(super.getIdLog());
		Assinante 			assinante = consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
		
		// Realiza um teste para identificar se o assinante nao foi encontrado na plataforma
		// caso seja verdadeiro entao este sera considerado como Pos-Pago
		if (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO)
			planoPreco = Definicoes.PLANO_DEFAULT_POSPAGO;
		else
			planoPreco = assinante.getPlanoPreco();
		
		super.log(Definicoes.DEBUG,"getPlanoPrecoAssinante","Fim");
		// Retorna o plano de preco do assinate e um defaul para os assinantes pos-pago
		return planoPreco;
	}

	/**
	 * Metodo....:insereOSDesativaHotLine
	 * Descricao.:Insere os dados da desativacao de hot-line. Este metodo deve ser chamado excepcionalmente
	 *            pelo componente de negocio Aprovisionamento no metodo que disponibiliza essa funcionalidade
	 *            para a URA, pelo Portal Pre-Pago este passo ja e realizado pelo proprio portal
	 * @param idAtividade	- Id da OS
	 * @param msisdn		- Numero MSISDN do assinante
	 * @param planoPreco	- Plano de preco do assinante
	 * @throws GPPInternalErrorException
	 */
	public void insereOSDesativaHotLine(long idAtividade,String msisdn, int planoPreco) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"insereOSDesativaHotLine","Inicio MSISDN "+msisdn);
		PREPConexao conexaoPrep=null;
		conexaoPrep  = gerBancoDados.getConexaoPREP(super.getIdLog());
		String sql = "INSERT INTO TBL_EXT_CONTINGENCIA_CRM " +
		             "(ID_ATIVIDADE, ID_OPERACAO, IDT_MSISDN, DAT_ATIVIDADE, IDT_ATENDENTE, " +
					 " IDT_STATUS_ATIVIDADE, COD_RETORNO, IDT_STATUS_PROCESSAMENTO, IDT_PLANO_PRECO) " +
					 " VALUES (?,?,?,?,?,?,?,?,?)";

		Object param[] = {new Long(idAtividade),
                          Definicoes.ID_OPERACAO_DESATIVA_HOTLINE,
						  msisdn,
						  new Timestamp(Calendar.getInstance().getTimeInMillis()),
						  Definicoes.SO_GPP,
						  Definicoes.STATUS_BLOQUEIO_SOLICITADO,
						  null,
						  null,
						  String.valueOf(planoPreco)
						};
		conexaoPrep.executaPreparedUpdate(sql,param,super.getIdLog());
		gerBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG,"insereOSDesativaHotLine","Fim");
	}
}
