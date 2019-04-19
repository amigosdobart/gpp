//Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.masc.ConexaoMASC;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.conexoes.bancoDados.ConexaoBancoDados;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.mapeamentos.*;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.SistemaOrigem;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.AssinanteXMLParser;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.*;

//Arquivos de Import Internos
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//XML
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.*;

/**
 *	Este arquivo refere-se a classe ConsultaAssinante, responsavel pela implementacao da logica de consulta de dados 
 *	de assinantes na plataforma Tecnomen e no banco de dados do GPP.
 *
 *	@version		1.0		15/03/2004		Primeira versao.
 *	@author 		Camile Cardoso Couto
 *
 *	@version		2.0		25/11/2004		Adaptacao para multiplos saldos.
 *	@author			Denys Oliveira				
 *
 *	@version		2.1		13/04/2005		Todas as strings de sql foram declaradas como final static com o objetivo 
 *											de melhorar a utilizacao de memoria.
 *	@author 		Luciano Vilela
 * 
 *	@version		2.2		26/01/2006		Retirar o objeto PREPConexao do metodo setaDescricaoAssinante, uma vez que
 *											este objeto nao tinha utilidade.
 *	@author			Gustavo Gusmao 
 * 
 *	@version		3.0		08/03/2007		Consulta de assinante somente a partir do metodo 
 *											TecnomenAprovisionamento#consultaAssinante(). Alteracao feita devido a 
 *											migracao para a Tecnomen 4.4.6.
 *	@author			Daniel Ferreira
 *
 * 	@version		3.1		06/12/2007		Consultas e atualizações de assinantes passaram a ser feitas através
 * 											do PaymentInterface. Conexões foram atualizadas.
 *	@author			Vitor Murilo
 */

public final class ConsultaAssinante extends Aplicacoes
{
	//Definicao de sql
	private final static String queryRecargaStatic = "SELECT SUM(VLR_PAGO) FROM TBL_REC_RECARGAS " +
	" WHERE ID_TIPO_RECARGA = ? AND " + 
	" ID_CANAL = ? AND " +
	" IDT_CPF = ? AND " +
	" DAT_ORIGEM BETWEEN SYSDATE - ? AND SYSDATE ";

	private final static String queryCartaoCreditoStatic = "SELECT COUNT(*) AS QTD_CC FROM (" +
	"SELECT DISTINCT(NUM_HASH_CC) FROM TBL_REC_RECARGAS " +
	" WHERE ID_TIPO_RECARGA = ? AND " + 
	" ID_CANAL = ? AND " +
	" IDT_CPF = ? AND " +
	" DAT_ORIGEM BETWEEN SYSDATE - ? AND SYSDATE " +
	" AND NUM_HASH_CC <> ? )";


	// Variaveis Membro
	protected GerentePoolTecnomen 	gerenteTecnomen = null; // Gerente de conexoes Tecnomen
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados

	/**
	 * Metodo...: ConsultaAssinante
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	public ConsultaAssinante (long logId)
	{
		super(logId, Definicoes.CL_CONSULTA_ASSINANTE);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);

		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	}

	/**
	 *	Executa a consulta do assinante pelas plataformas.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Informacoes do assinante na plataforma Tecnomen ou Alcatel.
	 *	@throws		GPPInternalErrorException, GPPTecnomenException
	 */
	public Assinante consultarAssinantePlataforma(String msisdn) throws GPPInternalErrorException, GPPTecnomenException
	{
		Assinante result = null;

		//Consulta na plataforma Tecnomen.
		result = this.consultarAssinante(msisdn);
		if((result != null) && (result.getRetorno() == Definicoes.RET_OPERACAO_OK))
		{
			result.setNaturezaAcesso("GSM");
			return result;
		}

		//Consulta na plataforma Alcatel.
		result = this.executaConsultaMASC(msisdn);
		if((result != null) && (result.getRetorno() == Definicoes.RET_OPERACAO_OK))
		{
			result.setNaturezaAcesso("TFPP");
			return result;
		}

		//Assinante nao existente em nenhuma plataforma.
		result = new Assinante();
		result.setRetorno((short)Definicoes.RET_MSISDN_NAO_ATIVO);
		result.setMSISDN(msisdn);
		result.setNaturezaAcesso("INEXISTENTE");

		return result;
	}


	/**
	 * Metodo...: executaConsultaAssinante
	 * Descricao: Consulta dados de assinantes na plataforma Tecnomen e no banco de dados do GPP
	 * @param	aMsisdn 			- Numero do telefone a ser consultado
	 * @param 	aTipoConsulta	 	- Tipo da consulta (simples ou completa)
	 * @return	string 				- Codigo de retorno + MSISDN + Codigo do Plano de Preco + 
	 * 							Descricao do Plano de Preco + Saldo de Creditos + 
	 * 							Codigo do Status MSISDN + Descricao do Status do MSISDN +
	 * 							Codigo do Status do Servico + Descricao do Status do Servico +
	 * 							Data de Expiracao							
	 * @throws GPPInternalErrorException		
	 */
	public String executaConsultaAssinante(String aMsisdn, int aTipoConsulta) throws GPPInternalErrorException
	{
		Assinante assinante = null;
		String retorno = ""; // Melhor utilizacao de memoria do que String retorno = new String(); 

		super.log(Definicoes.INFO, "executaConsultaAssinante", "Consulta MSISDN: "+aMsisdn);
		// Verica o tipo de consulta (Simples ou Completa)
		switch (aTipoConsulta)
		{
		case Definicoes.CONSULTA_ASSINANTE_SIMPLES:
		{
			assinante = executaConsultaSimplesAssinanteTecnomen(aMsisdn);
			retorno = AssinanteXMLParser.getXMLSimples(assinante);
			break;
		}
		case Definicoes.CONSULTA_ASSINANTE_COMPLETA:
		{
			assinante = executaConsultaCompletaAssinanteTecnomen(aMsisdn);
			retorno = AssinanteXMLParser.getXML(assinante);
			break;
		}
		}

		return retorno; 
	}

	/**
	 * Metodo...: executaConsultaCompletaAssinanteTecnomen
	 * Descricao: Consulta dados de assinantes na plataforma Tecnomen e no banco de dados do GPP
	 * @param	aMsisdn 			- Numero do telefone a ser consultado
	 * @return	Assinante			- Objeto contendo informacoes do assinante 
	 * @throws GPPInternalErrorException		
	 */
	public Assinante executaConsultaCompletaAssinanteTecnomen(String aMsisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "executaConsultaCompletaAssinanteTecnomen", "Inicio MSISDN "+aMsisdn);

		// Inicializa variaveis do metodo
		long aIdProcesso = super.getIdLog();

		Assinante					dadosAssinante	= null;		 		 
		TecnomenAprovisionamento	conexao			= null;

		try
		{			
			//Seleciona conexao do pool de Aprovisionamento da Tecnomen.
			conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(aIdProcesso);

			//Efetua a consulta dos dados do assinante recebido como parametro
			dadosAssinante = conexao.consultarAssinante(aMsisdn);

			//Se o assinante existe
			if(dadosAssinante != null)
				super.log(Definicoes.DEBUG, "executaConsultaCompletaAssinanteTecnomen", "SUCESSO");
			// Se o assinante nao existe
			else
			{
				dadosAssinante = new Assinante();

				//Seta codigo de retorno na estrutura do assinante
				dadosAssinante.setRetorno(Short.parseShort(Definicoes.RET_S_MSISDN_NAO_ATIVO));			

				//seta o atributo msisdn na estrutura Assinante	
				dadosAssinante.setMSISDN(aMsisdn);		 

				super.log(Definicoes.WARN, "executaConsultaCompletaAssinanteTecnomen", "Assinante nao encontrado.");
			}
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "executaConsultaCompletaAssinanteTecnomen", "Excecao: "+ e1);				
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);			 
		}
		finally
		{
			this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, aIdProcesso);
		}		 

		super.log(Definicoes.DEBUG, "executaConsultaAssinanteTecnomen", "Fim");

		return dadosAssinante;
	}

	/**
	 * Metodo...: consultarAssinante
	 * Descricao: Consulta dados de assinantes na plataforma Tecnomen e no banco de dados do GPP
	 *            Este metodo consulta a API accountQuery da PaymentInterface
	 *            e será utilizado somente localmente para ajustes e recargas.  
	 * @param	msisdn 			- Numero do telefone a ser consultado
	 * @return	Assinante		- Objeto contendo informacoes do assinante 
	 * @throws GPPInternalErrorException, GPPTecnomenException		
	 */
	private Assinante consultarAssinante(String msisdn) throws GPPInternalErrorException, GPPTecnomenException
	{
		Assinante		dadosAssinante	= null;		 		 
		TecnomenRecarga	conexao			= null;
		
		try
		{
			conexao = this.gerenteTecnomen.getTecnomenRecarga(super.getIdLog());

			//Efetua a consulta dos dados do assinante recebido como parametro
			dadosAssinante = conexao.consultarAssinante(msisdn);

			//Se o assinante existe
			if(dadosAssinante == null)
			{
				dadosAssinante = new Assinante();

				//Seta codigo de retorno na estrutura do assinante
				dadosAssinante.setRetorno(Short.parseShort(Definicoes.RET_S_MSISDN_NAO_ATIVO));			

				//seta o atributo msisdn na estrutura Assinante	
				dadosAssinante.setMSISDN(msisdn);		 

				super.log(Definicoes.DEBUG, "consultarAssinante", "MSISDN: " + msisdn + " - Assinante nao encontrado.");
			}
		}
		finally
		{
			this.gerenteTecnomen.liberaConexaoRecarga(conexao, super.getIdLog());
		}		 

		return dadosAssinante;
	}

	/**
	 * Metodo...: executaConsultaSimplesAssinanteTecnomen
	 * Descricao: Consulta simples dados de assinantes na plataforma Tecnomen e no banco de dados do GPP
	 * @param	aMsisdn 			- Numero do telefone a ser consultado
	 * @return	Assinante			- Objeto contendo informacoes do assinante 
	 * @throws GPPInternalErrorException		
	 */
	public Assinante executaConsultaSimplesAssinanteTecnomen(String aMsisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "executaConsultaSimplesAssinanteTecnomen", "Inicio MSISDN "+aMsisdn);

		// Inicializa variaveis do metodo
		long aIdProcesso = super.getIdLog();

		Assinante					dadosAssinante	= null;		 		 
		TecnomenAprovisionamento	conexao			= null;

		try
		{			
			//Seleciona conexao do pool de Aprovisionamento da Tecnomen.
			conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(aIdProcesso);
			
			//Efetua a consulta dos dados do assinante recebido como parametro
			dadosAssinante = conexao.consultarAssinante(aMsisdn);

			//Se o assinante existe
			if (dadosAssinante != null)
			{	
				// Na consulta simples, nao sao preenchidos as descricoes de plano, stauts, etc.
				//Seta valores de descrição de plano de preco, status do assinante e do servico
				//setaDescricaoAssinante(dadosAssinante);
				dadosAssinante.setRetorno((short)Definicoes.RET_OPERACAO_OK);

				super.log(Definicoes.DEBUG, "executaConsultaSimplesAssinanteTecnomen", "SUCESSO.");
			}
			// Se o assinante nao existe
			else
			{
				dadosAssinante = new Assinante();

				//Seta codigo de retorno na estrutura do assinante
				dadosAssinante.setRetorno(Short.parseShort(Definicoes.RET_S_MSISDN_NAO_ATIVO));			

				//seta o atributo msisdn na estrutura Assinante	
				dadosAssinante.setMSISDN(aMsisdn);		 

				super.log(Definicoes.DEBUG, "executaConsultaSimplesAssinanteTecnomen", "Assinante nao encontrado.");
			}
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "executaConsultaSimplesAssinanteTecnomen", "Excecao ocorrida: "+ e1);				
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);			 
		}
		finally
		{
			//Libera conexao com do pool de recarga da Tecnomen
			this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, aIdProcesso);

		}

		super.log(Definicoes.DEBUG, "executaConsultaSimplesAssinanteTecnomen", "Fim");
		return dadosAssinante;
	}


	/**
	 * Metodo...: consultaAssinantePreRecarga
	 * Descricao: Consulta Pré-Recarga feita por alguns sistemas (MIC, CRM e PortalBrt)
	 * @param	aXML	 			- Xml com os parâmetros da consulta
	 * @param	tipoRecarga 		- "Unica" ou "Multipla", indicando uma recarga ou várias
	 * @return	string 				- xml com o retorno da(s) consulta(s)		
	 * @throws 	GPPInternalErrorException						
	 */
	public String executaConsultaAssinantePreRecarga(String aXML, String tipoRecarga, DadosConsultaRecarga dadosRecarga) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO, "executaConsultaAssinantePreRecarga", "XML Entrada: "+aXML);
		String xmlRetorno = null;

		//Seleciona conexao do pool de recarga da Tecnomen
		TecnomenAprovisionamento conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());

		try
		{
			//Verifica se o Sistema de origem exige validação envolvendo cpf/cnpj e saldo máximo
			boolean validaSO = false;
			boolean validaSaldoMaximo = false;
			MapValoresRecargaPlanoPreco mapVlrRecargaPlano = MapValoresRecargaPlanoPreco.getInstancia();
			SistemaOrigem sistema = MapSistemaOrigem.getInstancia().getSistemaOrigem(dadosRecarga.getSistemaOrigem());
			if(sistema != null)
			{
				if(sistema.validaCc())
					validaSO = true;

				if(sistema.validaSaldoMaximo())
					validaSaldoMaximo = true;
			}

			// Analisa a validade de cada uma das recargas solicitadas
			ArrayList dadosRetorno = new ArrayList();					// armazena os retornos de cada consutla pré-recarga

			short retornoGlobal = Definicoes.RET_OPERACAO_OK;		// Variável de retorno que vai indicar se houve, pelo menos, uma recarga com erro
			for(int indiceVetorRecargas=0;indiceVetorRecargas<dadosRecarga.getRecargas().size();indiceVetorRecargas++)
			{
				short retorno = Definicoes.RET_OPERACAO_OK;
				String msisdn = dadosRecarga.getRecarga(indiceVetorRecargas).getMsisdn();
				super.log(Definicoes.INFO, "executaConsultaAssinantePreRecarga", "Assinante: " + msisdn);

				//Efetua a consulta dos dados do assinante recebido como parametro
				Assinante dadosAssinante = conexao.consultarAssinante(msisdn);

				//Valida dados do assinante
				short retValidacaoAssinante = Definicoes.RET_OPERACAO_OK;
				if((dadosAssinante != null) && (dadosAssinante.getRetorno() == Definicoes.RET_OPERACAO_OK))
				{	
					//validacao de status do assinante e do servico
					if ( (retValidacaoAssinante = validaStatus(dadosAssinante)) != Definicoes.RET_OPERACAO_OK)
					{	
						retorno = retValidacaoAssinante;				
					}
					else if(validaSaldoMaximo)
					{
						if((retValidacaoAssinante = validaSaldoMaximo(dadosAssinante, dadosRecarga.getRecarga(indiceVetorRecargas))) != Definicoes.RET_OPERACAO_OK)
						{
							retorno = retValidacaoAssinante;							
						}
					}
					else if(!mapVlrRecargaPlano.existeValorRecargaPlanoPreco(dadosRecarga.getRecarga(indiceVetorRecargas).getIdValor(), dadosAssinante.getPlanoPreco()))
					{
						retorno = Definicoes.RET_VALOR_CREDITO_INVALIDO;
					}
				}
				//Se o assinante nao existe
				else
				{
					//Retorna código de assinante não ativo
					retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
				}


				// Popula estrutura assinante recarga (para xml) com dados de retorno
				retornoGenerico retornoRecarga = new retornoGenerico();
				retornoRecarga.msisdn = msisdn;
				retornoRecarga.codigoRetorno = retorno;
				retornoRecarga.descricao = this.getDescricaoRetorno(retorno);
				dadosRetorno.add(retornoRecarga);

				// Indica que houve, pelo menos, uma recarga com erro
				if(retorno != Definicoes.RET_OPERACAO_OK)
				{
					retornoGlobal = Definicoes.RET_ERRO_CONSULTA_PRE_RECARGA;
				}
			}

			// Se o cada uma das recargas são para assinantes que existem, têm status válido e não vão extrapolar seus limites de saldo,
			// Validar as condições de compra via Cartão de Crédito, caso o sistema de origem assim exija
			if (validaSO && retornoGlobal == Definicoes.RET_OPERACAO_OK)
			{
				//Aplica regras de validacao de CPF, Categoria e Cartão de Crédito
				retornoGlobal = this.validaCPFAssinante(dadosRecarga);
			}

			// Constrói o XML de retorno
			xmlRetorno = this.constroiXMLRetornoConsultaPreRecarga(dadosRetorno, retornoGlobal, tipoRecarga);
			super.log(Definicoes.INFO, "executaConsultaAssinantePreRecarga","Retorno: "+xmlRetorno);
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "executaConsultaAssinanteRecarga", "Excecao: "+ e1);				
			throw new GPPInternalErrorException ("Excecao GPP: " + e1);			 
		}
		finally
		{
			//Libera conexao com do pool de recarga da Tecnomen
			this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, super.getIdLog());
		}		 
		super.log(Definicoes.DEBUG, "executaConsultaAssinanteRecarga", "Fim");

		return xmlRetorno;
	}

	/**
	 * Metodo...: validaStatus
	 * Descricao: Valida se o status do Assinante e do Servico sao validos para recarga 
	 * @param	adadosAssinante	- Estrutura Assinante a ser preenchida
	 * @return	short 			- Codigo de retorno 								
	 * @throws 	GPPInternalErrorException
	 */
	private short validaStatus(Assinante dadosAssinante)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "validaStatus", "Inicio MSISDN "+dadosAssinante.getMSISDN());

		short retorno = Definicoes.RET_OPERACAO_OK;

		try
		{
			// Se status do MSISDN = Shutdown, retorna STATUS MSISDN INVALIDO
			if ( dadosAssinante.getStatusAssinante() == Definicoes.SHUT_DOWN )
			{
				super.log( Definicoes.DEBUG, "validaStatus", "Status do Assinante: " + dadosAssinante.getStatusAssinante());			
				retorno = Short.parseShort(Definicoes.RET_S_STATUS_MSISDN_INVALIDO);  
			}
			// Se status do servico = bloqueado, retorna STATUS servico bloqueado
			else if ( (dadosAssinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO))
			{
				super.log( Definicoes.DEBUG, "validaStatus", "Status do Servico: " + dadosAssinante.getStatusServico());			
				retorno = Short.parseShort(Definicoes.RET_S_STATUS_MSISDN_INVALIDO);  
			}			
		}
		catch (NumberFormatException e)
		{
			super.log(Definicoes.WARN, "validaStatus", "Excecao:"+ e);				
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		super.log(Definicoes.DEBUG, "validaStatus", "Fim");
		return retorno;
	}

	/**
	 *	Valida se o saldo atual do assinante somado ao valor da recarga nao ultrapassam o valor do saldo maximo.
	 *
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e dos valores de recarga.
	 *
	 *	@param		assinante				Informacoes do assinante na plataforma.
	 *	@param		valores					Informacoes de valores de recarga.
	 */
	private short validaSaldoMaximo(Assinante assinante, SolicitacaoRecarga solicitacao)
	{
		short result = Definicoes.RET_OPERACAO_OK;

		try
		{
			if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
				return Definicoes.RET_MSISDN_NAO_ATIVO;

			//Obtendo os valores de recarga.
			Categoria categoria = MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco()).getCategoria();
			ValoresRecarga valores = MapValoresRecarga.getInstance().getValoresRecarga(solicitacao.getIdValor(), categoria, Calendar.getInstance().getTime());
			if(valores == null)
				return Definicoes.RET_VALOR_CREDITO_INVALIDO;

			double limite = Double.parseDouble(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("SALDO_MAXIMO"));

			if((limite < valores.getSaldoPrincipal() + valores.getValorBonusPrincipal() + assinante.getCreditosPrincipal()) ||
					(limite < valores.getSaldoPeriodico() + valores.getValorBonusPeriodico() + assinante.getCreditosPeriodico()) ||
					(limite < valores.getSaldoBonus    () + valores.getValorBonusBonus    () + assinante.getCreditosBonus    ()) ||
					(limite < valores.getSaldoSMS      () + valores.getValorBonusSMS      () + assinante.getCreditosSms      ()) ||
					(limite < valores.getSaldoGPRS     () + valores.getValorBonusGPRS     () + assinante.getCreditosDados    ()))
				result = Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO;
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "validaSaldoMaximo", "Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}

		return result;
	}

	/**
	 * Metodo...: validaCPFAssinante
	 * Descricao: Valida se o CPF que deseja efetuar a recarga via CC pode faze-la
	 * @param 	dadosRecarga			- Objeto da classe DadosConsultaRecarga contendo os parâmetros da consulta
	 * 									pré-recarga (cpf, hashCC, valores de recarga etc)
	 * @return	short 					- Codigo de retorno (0, se ok ou código de erro)							
	 * @throws 	GPPInternalErrorException
	 */
	private short validaCPFAssinante(DadosConsultaRecarga dadosRecarga)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "validaCPFAssinante", "Inicio");

		short retorno = Definicoes.RET_OPERACAO_OK;

		String hashCC = dadosRecarga.getHashCartaoCredito();
		String cpf = dadosRecarga.getCpf();

		try
		{
			// Valida se o cpf e o hash do cartão de crédito foram passados no XML
			if(cpf != null && hashCC != null)
			{
				if(cpf.equals("") || cpf.equals(""))
				{
					retorno = Definicoes.RET_CAMPO_OBRIGATORIO;
				}
			}
			else
			{
				retorno = Definicoes.RET_CAMPO_OBRIGATORIO;
			}

			// Caso cpf/hash sejam válidos
			if(retorno==Definicoes.RET_OPERACAO_OK)
			{
				// Varre o vetor de recargas e determina o valor pago total e extrai a lista dos msisdn's receptores de recargas
				double valorPagoTotal = 0;
				MapValoresRecarga mapValoresRecarga = MapValoresRecarga.getInstancia();
				for(int k=0; k<dadosRecarga.getRecargas().size();k++)
				{
					Assinante assinante = this.executaConsultaCompletaAssinanteTecnomen(dadosRecarga.getRecarga(k).getMsisdn());
					double idValor = dadosRecarga.getRecarga(k).getIdValor();
					Categoria categoria = MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco()).getCategoria(); 
					Date dataRecarga = Calendar.getInstance().getTime();
					valorPagoTotal += mapValoresRecarga.getValoresRecarga(idValor, categoria, dataRecarga).getValorEfetivoPago();
				}

				//Valida valor máximo de recargas compradas pelo CPF no periodo
				retorno = validaValorMaximoCPF (valorPagoTotal, cpf);

				//Se o valor total a recarregar mais o total recarregado pelo CPF no periodo
				// for menor ou igual ao total permintido
				if (retorno == Definicoes.RET_OPERACAO_OK)
				{
					// Valida quantidade de CC utilizados pelo CPF no periodo
					retorno = validaQTDCPF (cpf, hashCC);

					// Se a qtd de CC utilizados nao ultrapassou o limite permitido
					if (retorno == Definicoes.RET_OPERACAO_OK)
					{
						// Categoria 9: Não é nem pré-pago, nem pós-pago nem híbrido (não se aplica)
						if (dadosRecarga.getCategoria() != 9)
						{
							// Valida quantidade de MSISDNs recarregados pelo CPF no periodo
							retorno = validaQTDMsisndnCategoria (dadosRecarga);

							if (retorno != Definicoes.RET_OPERACAO_OK)
							{
								super.log(Definicoes.DEBUG, "validaCPFAssinante", "QTD de MSISDNs recarregados pelo CPF ultrapassada");					
							}
						}
					}
					else
					{
						super.log(Definicoes.DEBUG, "validaCPFAssinante", "QTD de CC utilizados pelo CPF ultrapassada");
					}
				}				
				else
				{
					super.log(Definicoes.DEBUG, "validaCPFAssinante", "Valor Total de Recarga do CPF ultrapassado");				
				}
			}
			else
			{
				super.log(Definicoes.ERRO, "validaCPFAssinante", "Nao foram passados o CPF/CNPJ");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "validaCPFAssinante", "Excecao: "+ e);				
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 
		}
		super.log(Definicoes.DEBUG, "validaCPFAssinante", "Fim");
		return retorno;
	}

	/**
	 * Metodo...: validaValorMaximoCPF
	 * Descricao: Valida se o CPF não extrapola o valor limite de compra de recarga
	 * 				com cartão de crédito permitido no período
	 * @param	aPeriodo			- Período em que se busca as recargas via CC do cpf
	 * @param 	aValorRecarga		- Valor Pago pela Recarga que o CPF deseja comprar
	 * @param	aCPF				- CPF do comprador
	 * @return	short 				- Codigo de retorno 								
	 * @throws 	GPPInternalErrorException
	 */
	private short validaValorMaximoCPF (double aValorRecarga, String aCPF)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "validaValorMaximoCPF", "Inicio");

		short retorno = Definicoes.RET_OPERACAO_OK;

		// Pega Conexão com Banco de Dados
		ConexaoBancoDados DBConexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());;

		try
		{
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			short valorMaximo = Short.parseShort(confGPP.getMapValorConfiguracaoGPP("VALOR_MAXIMO_RECARGA_CC"));
			int periodo = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("PERIODO_RECARGA_CC"));

			//Selecionar o valor total de recarga feito por um CPF nos ultimos 'periodo' dias


			Object paramValor[] = {Definicoes.TIPO_RECARGA, Definicoes.CANAL_CC, aCPF, new Integer(periodo)};

			ResultSet rsValorTotalRecargas = DBConexao.executaPreparedQuery(queryRecargaStatic, paramValor, super.getIdLog());
			long valorTotalRecarga = 0;
			if (rsValorTotalRecargas.next())
			{
				valorTotalRecarga = rsValorTotalRecargas.getLong(1);
			}
			rsValorTotalRecargas.close();

			super.log(Definicoes.DEBUG, "validaValorMaximoCPF", "Valor Total Recarregado pelo CPF no periodo: " + valorTotalRecarga);

			//Se o valor total de recarga do CPF mais o valor da recarga 
			// for maior que o valor maximo permitido por cpf, retorna erro 
			if (valorTotalRecarga + aValorRecarga > valorMaximo )
			{
				retorno = Short.parseShort(Definicoes.RET_S_VALOR_CC_ULTRAPASSADO);
				super.log(Definicoes.WARN, "validaValorMaximoCPF", "CPF extrapolou Limite Máximo de Compra de Recarga via CC");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "validaValorMaximoCPF", "Excecao: "+ e);				
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(DBConexao, super.getIdLog());
		}		
		super.log(Definicoes.DEBUG, "validaValorMaximoCPF", "Fim");
		return retorno;
	}

	/**
	 * Metodo...: validaQTDCPF
	 * Descricao: Valida se a qtd total de CC utilizados pelo CPF não ultrapassa o limite estipulado
	 * 				e menor ou igual a qtd maxima permitida no periodo
	 * @param	aPeriodo			- Numero de dias minimo
	 * @param	aCPF				- CPF do comprador
	 * @param	aHashCartaoCredito	- CC que o CPF deseja usar para comprar a recarga
	 * @return	short 				- Codigo de retorno 								
	 * @throws 	GPPInternalErrorException
	 */
	public short validaQTDCPF (String aCPF, String aHashCartaoCredito)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "validaQTDCPF", "Inicio");

		short retorno = Definicoes.RET_OPERACAO_OK;

		// Pega Conexão com Banco de Dados
		ConexaoBancoDados DBConexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());

		try
		{
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			int periodo = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("PERIODO_RECARGA_CC"));

			//Seleciona CC utilizados por um CPF nos ultimos X dias 



			Object paramQtdCC[] = {Definicoes.TIPO_RECARGA, Definicoes.CANAL_CC, aCPF, new Integer(periodo),aHashCartaoCredito };
			ResultSet rs = DBConexao.executaPreparedQuery(queryCartaoCreditoStatic, paramQtdCC, super.getIdLog());

			int qtdMaximaCC = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("QTD_MAXIMO_CC_CPF"));
			if(rs.next())
			{
				// Se a qtd de cartões já usados diferentes do que está se fazendo a recarga agora 
				// somada a esse cartão extrapolar o limite máximo, retorna-se erro funcional
				if(rs.getInt("QTD_CC") + 1 > qtdMaximaCC)
				{
					retorno = Definicoes.RET_QTD_CC_ULTRAPASSADA;
				}
			}
			rs.close();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "validaQTDCPF", "Excecao: "+ e);				
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "validaQTDCPF", "Fim ");
		return retorno;	
	}

	/**
	 * Metodo...: validaQTDMsisndnCategoria
	 * Descricao: Valida se a qtd total de MSISDNs utilizados pelo CPF 
	 * 				e menor ou igual a qtd maxima permitida no periodo
	 * @param	aPeriodo			- Numero de dias minimo
	 * @param	aMsisdn				- Numero do MSISDN que se deseja recarregar
	 * @param	aCPF				- CPF do comprador
	 * @param	aCategoria			- Categoria do MSISDN
	 * @param	conexaoPrep			- Conexao do BD PREP a ser utilizada
	 * @return	short 				- Codigo de retorno 								
	 * @throws 	GPPInternalErrorException
	 */
	public short validaQTDMsisndnCategoria(DadosConsultaRecarga dadosRecarga)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "validaQTDMsisndnCategoria", "Inicio");

		short retorno = Definicoes.RET_OPERACAO_OK;

		// Pega Conexão com Banco de Dados
		ConexaoBancoDados DBConexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());

		try
		{
			MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
			int periodo = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP("PERIODO_RECARGA_CC"));
			int qtdMaxMSISDNSCategoria = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP(String.valueOf(dadosRecarga.getCategoria())));

			super.log(Definicoes.DEBUG, "validaQTDMsisndnCategoria", "Qtd maxima de MSISDNs que podem ser recarregados: " + qtdMaxMSISDNSCategoria);

			// seleciona MSISDNs recarregados por um CPF nos ultimos X dias 
			String sql = "SELECT COUNT(*) AS QTD_MSISDN FROM (" +
			"SELECT DISTINCT(IDT_MSISDN) FROM TBL_REC_RECARGAS " +
			" WHERE ID_TIPO_RECARGA = ? AND " + 
			" ID_CANAL = ? AND " +
			" IDT_CPF = ? AND " +
			" DAT_ORIGEM BETWEEN SYSDATE - ? AND SYSDATE AND" +
			" IDT_MSISDN NOT IN ("+ this.criaPrepraredQueryList(dadosRecarga.getRecargas().size()) + ") " +		// cria tantas interrogações quantas forem as recargas
			" )";

			// Copia os elementos do vetor para um array de parâmetros que será utilizada para evocar a prepared query
			Object paramQtdMsisdn[] = new Object[dadosRecarga.getRecargas().size() + 4];	// o tamanho é a soma do número de recargas com a qtd de param fixos (tipo recarga, canal, cpr, periodo)
			paramQtdMsisdn[0] = Definicoes.TIPO_RECARGA;
			paramQtdMsisdn[1] = Definicoes.CANAL_CC;
			paramQtdMsisdn[2] = dadosRecarga.getCpf();
			paramQtdMsisdn[3] = new Integer(periodo);
			for(int k=0; k<dadosRecarga.getRecargas().size(); k++)
			{
				paramQtdMsisdn[k+4] = dadosRecarga.getRecarga(k).getMsisdn();
			}

			ResultSet rs = DBConexao.executaPreparedQuery(sql, paramQtdMsisdn, super.getIdLog());

			// Verifica a qtdade de msisdn's que já receberam recarga do cpf 
			// diferentes dos que constam na lista de recargas
			int qtdMsisdnJaRecarregados = 0;
			if(rs.next())
			{
				qtdMsisdnJaRecarregados = rs.getInt("QTD_MSISDN");
			}

			if(qtdMsisdnJaRecarregados + dadosRecarga.getQtdMsisdnDistintos() > qtdMaxMSISDNSCategoria)
			{
				retorno = Definicoes.RET_QTD_MSISDN_ULTRAPASSADA;
			}

			rs.close();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "validaQTDMsisndnCategoria", "Excecao: "+ e);				
			throw new GPPInternalErrorException ("Excecao GPP: " + e);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(DBConexao, super.getIdLog());
		}	
		super.log(Definicoes.DEBUG, "validaQTDMsisndnCategoria", "Fim");
		return retorno;
	}

	/**
	 * Metodo...: getDescricaoErro
	 * Descricao: Retorna Descrição dos códigos de erro
	 * @param	aRetorno			- Retorno das validacoes efetuadas
	 * @return	String				- Descrição do código de erro
	 * 
	 * @throws 	GPPInternalErrorException
	 */
	public String getDescricaoRetorno (short codigoRetorno)throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "getDescricaoRetorno", "Inicio");
		String retorno = null;

		// Pega Conexão com Banco de Dados
		ConexaoBancoDados DBConexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());

		try
		{
			//Seleciona descricao do erro
			String sql = "SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO " +
			" WHERE VLR_RETORNO = ?";

			Object param[] = {new Short(codigoRetorno)};
			ResultSet rs = DBConexao.executaPreparedQuery(sql, param, super.getIdLog());
			if (rs.next())
			{
				retorno = rs.getString(1);
			}
			rs.close();

			super.log(Definicoes.DEBUG, "getDescricaoRetorno", "Retorno: " + codigoRetorno+" Desc Retorno: " + retorno);
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "getDescricaoRetorno", "Excecao Interna GPP: "+ e);				
			throw new GPPInternalErrorException ("Excecao interna GPP: " + e);			 
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "getDescricaoRetorno", "Excecao: "+ e1);				
			throw new GPPInternalErrorException ("Excecao: " + e1);			 
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		super.log(Definicoes.DEBUG, "getDescricaoRetorno", "Fim");	
		return retorno;
	}

	/**
	 * Metodo...: parseXMLRecarga
	 * Descricao: Le um XML de recarga única e seta os campos da estrutura DadosConsultaRecarga 
	 * @param 	 GPPConsultaPreRecarga - XML Recebido
	 * @return 	 DadosConsultaRecarga	- Estrutura DadosConsultaRecarga populada
	 * @throws 	 GPPBadXMLFormatException
	 * @throws 	 GPPInternalErrorException
	 */
	public DadosConsultaRecarga parseXMLConsultaPreRecarga ( String aXML, String tipoRecarga ) throws GPPBadXMLFormatException, GPPInternalErrorException
	{
		DadosConsultaRecarga retorno = new DadosConsultaRecarga();

		super.log(Definicoes.DEBUG, "parseXMLConsultaPreRecarga", "Inicio");

		try
		{
			super.log(Definicoes.DEBUG, "parseXMLConsultaPreRecarga", "XML consulta Pre-Recarga: " + aXML);

			// Busca uma instancia de um DocumentBuilder
			DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();

			// Cria um parse de XML
			DocumentBuilder parse = docBuilder.newDocumentBuilder();

			// Carrega o XML informado dentro e um InputSource
			InputSource is = new InputSource(new StringReader(aXML));

			// Faz o parse do XML
			Document doc = parse.parse(is);

			// Procura a TAG GPPConsultaPreRecarga
			Element consultaPreRecarga = (Element) doc.getElementsByTagName( "GPPConsultaPreRecarga" ).item(0);
			NodeList itemNodes = consultaPreRecarga.getChildNodes();			

			// Busca todos as demais tags do xml
			Element auxElement = (Element)itemNodes;
			retorno.setCpf(auxElement.getElementsByTagName("cpfCnpj").item(0).getChildNodes().item(0)==null?"":auxElement.getElementsByTagName("cpfCnpj").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setHashCartaoCredito(auxElement.getElementsByTagName("hashCc").item(0).getChildNodes().item(0)==null?"":auxElement.getElementsByTagName("hashCc").item(0).getChildNodes().item(0).getNodeValue());
			retorno.setCategoria(auxElement.getElementsByTagName("categoria").item(0).getChildNodes().item(0)==null?9:Short.parseShort(auxElement.getElementsByTagName("categoria").item(0).getChildNodes().item(0).getNodeValue()));
			retorno.setSistemaOrigem(auxElement.getElementsByTagName("sistemaOrigem").item(0).getChildNodes().item(0).getNodeValue());

			if (retorno.getSistemaOrigem().equals(Definicoes.SO_DEALER))
			{
				retorno.setTipoTransacao(auxElement.getElementsByTagName("tipoTransacao").item(0).getChildNodes().item(0)==null?"":auxElement.getElementsByTagName("tipoTransacao").item(0).getChildNodes().item(0).getNodeValue());
			}

			String msisdn;
			long valor;
			if(tipoRecarga.equals("Multipla"))
			{
				// Verifica qual a quantidade de solicitações de recarga
				NodeList recargasNode = auxElement.getElementsByTagName("assinante").item(0).getChildNodes();
				int qtdRecargas = recargasNode.getLength();

				// Pega as informações de cada uma das recargas no XML
				for(int k = 0; k < qtdRecargas; k++)
				{
					// Pega o msisdn do usuário
					msisdn = ((Element)recargasNode.item(k).getChildNodes()).getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue();

					// Pega o identificador do valor da recarga
					valor = Long.valueOf(((Element)recargasNode.item(k).getChildNodes()).getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue()).longValue();

					// Insere o vetor de solicitações de recarga no objeto de retorno
					retorno.setRecarga(valor, msisdn);
				}				
			}
			else
			{
				// Se for uma recarga simples, pega-se único msisdn e valor
				msisdn = auxElement.getElementsByTagName("msisdn").item(0).getChildNodes().item(0)==null?"":auxElement.getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue();
				valor = auxElement.getElementsByTagName("valor").item(0).getChildNodes().item(0)==null?0:Double.valueOf(auxElement.getElementsByTagName("valor").item(0).getChildNodes().item(0).getNodeValue()).longValue();
				retorno.setRecarga(valor,msisdn);
			}
		}
		catch (SAXException e) 
		{
			super.log(Definicoes.WARN, "parseXMLConsultaPreRecarga", "Erro(SAX) formato XML:" + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e.getMessage());
		}
		catch (IOException e) 
		{
			super.log(Definicoes.WARN, "parseXMLConsultaPreRecarga", "Erro(IO) interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e.getMessage());
		}
		catch (ParserConfigurationException e)
		{
			super.log(Definicoes.WARN, "parseXMLConsultaPreRecarga", "Erro(PARSER) interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e.getMessage());
		}
		catch (NumberFormatException e) 
		{
			super.log(Definicoes.WARN, "parseXMLConsultaPreRecarga", "Erro(NUMBERFORMAT) formato XML:" + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e.getMessage());
		}
		catch (NullPointerException e) 
		{
			super.log(Definicoes.WARN, "parseXMLConsultaPreRecarga", "Erro(NULLPOINTER) formato XML:" + e.getMessage());
			throw new GPPBadXMLFormatException ("Erro formato XML:" + e.getMessage());
		}
		catch (Exception e) 
		{
			super.log(Definicoes.ERRO, "parseXMLConsultaPreRecarga", "Erro interno GPP:" + e.getMessage());
			throw new GPPInternalErrorException ("Erro interno GPP:" + e.getMessage());
		}
		finally
		{
			super.log(Definicoes.DEBUG, "parseXMLConsultaPreRecarga", "Fim do parseXMLConsultaPreRecarga");
		}
		return retorno;

	}

	/**
	 * Metodo...: constroiXMLRetornoConsultaPreRecarga
	 * Descricao: Monta o xml de retorno a partir dos retornos de cada uma das consultas
	 * @param 	Vector	vetorRetornos	Vetor contendo todos os retornos das consultas
	 * @param	short	retornoGlobal	Código de retorno geral (da consulta como um todo)
	 * @param	String	tipoRecarga		Unica ou Multipla
	 * @return	String					XML de retorno de consulta pré-recarga
	 */
	public String constroiXMLRetornoConsultaPreRecarga(ArrayList vetorRetornos, short retornoGlobal, String tipoRecarga) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"constroiXMLRetornoConsultaPreRecarga","Inicio");

		GerarXML geradorXML = new GerarXML("GPPRetornoConsultaPreRecarga");

		// Consultas pré-recarga múltiplas exigem um XML mais trabalhado
		if(tipoRecarga.equals("Multipla"))
		{
			// Tags referentes ao retorno global
			geradorXML.adicionaTag("retorno", GPPData.formataNumero(retornoGlobal, 4));
			geradorXML.adicionaTag("descRetorno",this.getDescricaoRetorno(retornoGlobal));	

			// Região do detalhe dos retornos de cada recarga
			geradorXML.abreNo("retornoDetalhado");
			for(int indiceVetorRetornos=0; indiceVetorRetornos<vetorRetornos.size();indiceVetorRetornos++)
			{
				geradorXML.abreNo("retorno");
				geradorXML.adicionaTag("msisdn",((retornoGenerico)vetorRetornos.get(indiceVetorRetornos)).msisdn);
				geradorXML.adicionaTag("retorno",GPPData.formataNumero(((retornoGenerico)vetorRetornos.get(indiceVetorRetornos)).codigoRetorno, 4));
				geradorXML.adicionaTag("descRetorno",((retornoGenerico)vetorRetornos.get(indiceVetorRetornos)).descricao);
				geradorXML.fechaNo();
			}
		}
		else
		{
			// Se houve um erro funcional que inviabilizou a recarga em si, independente das validações de cpf/hashCC,
			// Então, deve-se buscar o código desse erro no primeiro elemento do vetor de retornos.
			// O mesmo deve ser feito para o caso de uma consulta bem sucedida
			String retornoUnico = null;
			String descRetornoUnico = null;
			if(	retornoGlobal == Definicoes.RET_ERRO_CONSULTA_PRE_RECARGA || 
					retornoGlobal == Definicoes.RET_OPERACAO_OK)
			{
				retornoUnico = GPPData.formataNumero(((retornoGenerico)vetorRetornos.get(0)).codigoRetorno, 4);
				descRetornoUnico = ((retornoGenerico)vetorRetornos.get(0)).descricao;
			}
			else
			{
				// Já no caso de um erro funcional nas validações de cpf/hashCC, o código de erro que deverá ser
				// retornado é o código de erro global, passado como parâmetro para esse método
				retornoUnico = new String(GPPData.formataNumero(retornoGlobal, 4));
				descRetornoUnico = this.getDescricaoRetorno(retornoGlobal);
			}

			// Consultas pré-recarga de recarga única retornam um xml bem compacto
			geradorXML.adicionaTag("retorno",retornoUnico);
			geradorXML.adicionaTag("descRetorno",descRetornoUnico);
		}

		super.log(Definicoes.DEBUG,"constroiXMLRetornoConsultaPreRecarga","Fim");
		return geradorXML.getXML();
	}

	/**
	 * Metodo...: criaPreparedQueryList
	 * Descricao: Cria um string de qtdInterrogacoes parametros "?" (ex: "?,?,?,?")
	 * @param 	int		qtdInterrogacoes	Qtd de interrogações que deve ter o string
	 * @return
	 */
	private String criaPrepraredQueryList(int qtdInterrogacoes)
	{
		// Constroi um string com qtdInterrogacoes substrings ",?" 
		StringBuffer sbAuxString = new StringBuffer("?");		
		for(int k=1; k<qtdInterrogacoes; k++)					
		{
			sbAuxString.append(",?");
		}
		return sbAuxString.toString();
	}

	/**
	 * Metodo...: executaConsultaMASC
	 * Descricao: Executa a Consulta via MASC
	 * @param MSISDN               	- numero do msisdn
	 * @return	short 				- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 */
	public Assinante executaConsultaMASC (String MSISDN) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		Assinante retorno = new Assinante();
		retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);

		//short retorno = Definicoes.RET_ERRO_GENERICO_MASC;
		//String sucessoFalha = Definicoes.PROCESSO_SUCESSO;

		super.log(Definicoes.DEBUG,"executaConsultaMASC", "Inicio da Execucao de Consulta via MASC.");
		try
		{
			// Criando uma classe de aplicacao
			ConexaoMASC conexaoMASC = new ConexaoMASC(super.getIdLog());

			// Persiste na consulta, caso esteja ocorrendo erro técnico
			MapConfiguracaoGPP mapConfig = MapConfiguracaoGPP.getInstancia();
			int numTentativas = Integer.parseInt(mapConfig.getMapValorConfiguracaoGPP("NUM_TENTATIVAS_RECARGA_MASC"));
			while(retorno.getRetorno() == Definicoes.RET_ERRO_GENERICO_MASC && numTentativas > 0)
			{
				retorno = conexaoMASC.enviaConsultaMASC(MSISDN);
				numTentativas--;
			}
		}
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO,"executaConsultaMASC", "Erro na Execucao de Consulta via MASC: "+e1);
			throw new GPPInternalErrorException("Erro GPP: "+e1);			
		}
		finally
		{
			super.log(Definicoes.DEBUG,"executaConsultaMASC", "Fim da Execucao de Consulta via MASC.");
		}
		return retorno;
	}
}