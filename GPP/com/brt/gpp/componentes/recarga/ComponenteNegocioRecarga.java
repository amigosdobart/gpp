//Definicao do Pacote  
package com.brt.gpp.componentes.recarga;

import java.text.ParseException;
import java.text.SimpleDateFormat;

//Arquivos de ORB de recarga
import com.brt.gpp.componentes.recarga.orb.*;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Import Internos
import com.brt.gpp.aplicacoes.recargaMassa.ConcessaoRecargaMassa;
import com.brt.gpp.aplicacoes.recarregar.*;
import com.brt.gpp.comum.mapeamentos.*;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.GerenciaPedidosVoucher;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.OperacoesVoucher;

/**
 *
 * Este arquivo contem a definicao da classe componente de negocio de Recarga. Ela 
 * e responsavel pela logica de negocio para as regargas de creditos de assinantes na plataforma
 * pre-pago na tecnomen. 
 *
 * @autor:				Daniel Cintra Abib
 * @Data:				28/02/2004
 *
 * Modificado Por:
 * Data:
 * Razao:
 */
public class ComponenteNegocioRecarga extends recargaPOA
{
	// Variaveis Membro
	protected GerentePoolTecnomen 		gerenteTecnomen 	= null; // Gerente de conexoes Tecnomen
	protected GerentePoolLog			Log					= null; // Gerente de LOG
	protected GerentePoolBancoDados		gerenteBanco		= null; // Gerente Banco de Dados
	//private GeDBConexao	dbConexao	= null; // G
	
	/**
	 * Metodo...: ComponenteNegocioRecarga
	 * Descricao: Construtor 
	 * @param	
	 * @return									
	 */
	public ComponenteNegocioRecarga ( )
	{
		// Obtem referencia ao gerente de LOG
		this.Log = GerentePoolLog.getInstancia(this.getClass());

		Log.logComponente ( Definicoes.INFO, Definicoes.CN_RECARGA, "Componente de Negocio ativado..." );
	}
	
	/**
	 * Metodo...: executaRecargaXML
	 * Descricao: Executa a recarga via XML
	 * @param	XMLGPPRecarga 	- xml de entrada da  recarga
	 * @return	String 			- xml de saida da  recarga
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPBadXMLFormatException
	 * @throws 	GPPTecnomenException
	 */
	public String executaRecargaXML (String XMLGPPRecarga) 	throws 	GPPInternalErrorException, GPPCorbaException, GPPBadXMLFormatException, GPPTecnomenException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		String retornoXML = null;
		long idProcesso = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecargaXML", "Inicio da Execucao de Recarga - XML.");
		
		// Criando uma classe de aplicacao
		Recarregar recarga = new Recarregar(idProcesso);
		
		try
		{
			retorno = recarga.executarRecarga(XMLGPPRecarga);
			
			if(retorno !=0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
			
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecargaXML", "XML retornado:" + retornoXML);
			
			// Monta o XML para enviar como retorno
			retornoXML = recarga.constroiXMLRetorno(retorno);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecargaXML", "Fim da Execucao de Recarga - XML.");
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		
		return retornoXML;
	}
	
	/**
	 * Metodo...: executaAjusteXML
	 * Descricao: Executa o ajuste via XML
	 * @param	XMLGPPRecarga 	- xml de entrada do ajuste
	 * @return	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPBadXMLFormatException
	 * @throws 	GPPTecnomenException
	 */
	public short executaAjusteXML (String XMLGPPRecargaAjuste) throws GPPInternalErrorException, GPPCorbaException, GPPBadXMLFormatException, GPPTecnomenException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjusteXML", "Inicio da Execucao de Ajuste - XML.");

		// Criando uma classe de aplicacao
		//Ajustar ajuste = new Ajustar(idProcesso);
		
		try
		{
			// parse do xml de entrada
			//ParametrosRecarga dadosAjuste = ajuste.parseXML(XMLGPPRecargaAjuste,"A");
			
			// executa o ajuste
			//retorno = this.executaAjuste(dadosAjuste.getMSISDN(),dadosAjuste.getTipoTransacao(),
			//				dadosAjuste.getTipoCredito(),dadosAjuste.getSaldo(),
			//			dadosAjuste.getIndCreditoDebito(),dadosAjuste.getDataHora(),
			//		dadosAjuste.getSistemaOrigem(),dadosAjuste.getOperador());
			
			if(retorno!=0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
		}
		/*catch (GPPBadXMLFormatException e) 
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_RECARGA,"executaAjusteXML", "Erro na Execucao de Ajuste - XML.");
			
			throw new GPPBadXMLFormatException(e.codigoErro);
		}*/
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjusteXML", "Fim da Execucao de Ajuste - XML.");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: executaEstornoXML
	 * Descricao: Executa o estorno via XML
	 * @param	XMLGPPRecarga 	- xml de entrada do estorno
	 * @return	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPBadXMLFormatException
	 * @throws 	GPPTecnomenException
	 */
	public short executaEstornoXML (String XMLGPPRecargaEstorno) throws GPPInternalErrorException, GPPCorbaException, GPPBadXMLFormatException, GPPTecnomenException
	{
		return 0;
	}
	
	/**
	 * Metodo...: executaRecargaBanco
	 * Descricao: Executa a Recarga via Banco
	 * @param MSISDN               	- numero do msisdn
	 * @param tipoTransacao        	- tipo de trnsacao
	 * @param identificacaoRecarga 	- identificacao da Recarga 
	 * @param nsuInstituicao       	- NSU da instituicao
	 * @param codLoja              	- codigo da loja
	 * @param tipoCredito          	- tipo de credito
	 * @param valor                	- valor da recarga
	 * @param dataHora             	- data e hora da recarga
	 * @param dataHoraBanco        	- data e hora do banco
	 * @param dataContabil         	- data contabil
	 * @param terminal              - terminal       
	 * @param tipoTerminal          - tipo do terminal       
	 * @param sistemaOrigem         - sistema de Origem        
	 * @param operador              - operador que realizou a recarga 	 
	 * @return	short 			- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPTecnomenException
	 */
	public short executaRecargaBanco (String MSISDN, String tipoTransacao, String identificacaoRecarga, String nsuInstituicao, String codLoja, String tipoCredito, double valor, String dataHora, String dataHoraBanco, String dataContabil, String terminal, String tipoTerminal, String sistemaOrigem, String operador) throws GPPInternalErrorException, GPPCorbaException, GPPTecnomenException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecargaBanco", "Inicio da Execucao de Recarga via Banco.");
		
		try
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_RECARGA,"executaRecargaBanco", "Recarga Banco: "+MSISDN);
			
			//Criando os parsers das datas informadas.
			SimpleDateFormat	conversorDataHoraGPP	= new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
			SimpleDateFormat	conversorDatDia			= new SimpleDateFormat(Definicoes.MASCARA_MES_DIA);
			
			// Criando uma classe de aplicacao
			Recarregar recarga = new Recarregar(idProcesso);
			retorno = recarga.executarRecarga(MSISDN, 
											  tipoTransacao, 
											  identificacaoRecarga, 
											  tipoCredito, 
											  valor,
											  conversorDataHoraGPP.parse(dataHoraBanco),
											  sistemaOrigem,
											  operador,
											  nsuInstituicao,
											  null,
											  null,
											  terminal,
											  tipoTerminal,
											  conversorDatDia.parse(dataContabil),
                                              null);
		}
		catch(ParseException e)
		{
			this.Log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_RECARGA, "executaRecargaBanco", "MSISDN: " + MSISDN + " - Formato de data invalido.");
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecargaBanco", "MSISDN: " + MSISDN + " - Data/hora: " + dataHoraBanco);
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecargaBanco", "MSISDN: " + MSISDN + " - Data contabil: " + dataContabil);
			retorno = Definicoes.RET_FORMATO_DATA_INVALIDA;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecargaBanco", "Fim da Execucao de Recarga via Banco.");
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_RECARGA,"executaRecargaBanco", "Retorno: "+retorno);

			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: executaRecarga
	 * Descricao: Executa a Recarga generica
	 * @param MSISDN               	- numero do msisdn
	 * @param tipoTransacao        	- tipo de trnsacao
	 * @param identificacaoRecarga 	- identificacao da Recarga 
	 * @param tipoCredito          	- tipo de credito
	 * @param valor                	- valor da recarga
	 * @param dataHora             	- data e hora da recarga
	 * @param sistemaOrigem         - sistema de Origem        
	 * @param operador              - operador que realizou a recarga 	 
	 * @param nsuInstituicao       	- NSU da instituicao
	 * @param hash_cc              	- Hash do CC
	 * @param cpf              		- CPF
	 * @return	short 				- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPTecnomenException
	 */
	public short executaRecarga (String MSISDN, String tipoTransacao, String identificacaoRecarga, String tipoCredito, double valor, String dataHora, String sistemaOrigem, String operador, String nsuInstituicao, String hash_cc, String cpf) throws GPPInternalErrorException, GPPCorbaException, GPPTecnomenException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecarga", "Inicio da Execucao de Recarga.");
		
		// Criando uma classe de aplicacao
		Recarregar recarga = new Recarregar(idProcesso);
		
		try
		{
			//Criando o parser da data informada.
			SimpleDateFormat conversorDataHoraGPP = new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
			
			// executa a recarga
			retorno = recarga.executarRecarga(MSISDN, 
											  tipoTransacao, 
											  identificacaoRecarga, 
											  tipoCredito, 
											  valor, 
											  conversorDataHoraGPP.parse(dataHora), 
											  sistemaOrigem, 
											  operador, 
											  nsuInstituicao, 
											  hash_cc, 
											  cpf, 
											  null,
											  null,
											  null,
                                              null);
			
			if(retorno != 0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
		}
		catch(ParseException e)
		{
			this.Log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_RECARGA, "executaRecarga", "MSISDN: " + MSISDN + " - Formato de data invalido.");
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecarga", "MSISDN: " + MSISDN + " - Data/hora: " + dataHora);
			retorno = Definicoes.RET_FORMATO_DATA_INVALIDA;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaRecarga", "Fim da Execucao de Recarga.");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: executaAjuste (sem descrição)
	 * Descricao: Executa um ajuste somente em um tipo de saldo
	 * @param MSISDN               	- numero do msisdn
	 * @param tipoTransacao        	- tipo de trnsacao
	 * @param tipoCredito          	- tipo de credito (00,01,02,03 para principal,bonus,sms e dados respectivamente)
	 * @param valor                	- valor da recarga
	 * @param tipo              	- tipo de ajuste
	 * @param dataHora             	- data e hora da recarga
	 * @param sistemaOrigem         - sistema de Origem        
	 * @param operador              - operador que realizou a recarga 	 
	 * @param data_expiracao   		- data de expiracao
	 * @return	short 				- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPTecnomenException
	 */
	public short executaAjuste (String MSISDN, String tipoTransacao, String tipoCredito, double valor, String tipo, String dataHora, String sistemaOrigem, String operador, String data_expiracao) throws 	GPPInternalErrorException, GPPCorbaException, GPPTecnomenException
	{
		return this.executaAjusteDescrito(MSISDN, 
										  tipoTransacao, 
										  tipoCredito,
										  valor,
										  tipo,
										  dataHora,
										  sistemaOrigem,
										  operador,
										  data_expiracao,
										  null);
	}
	
	/**
	 * Metodo...: executaAjusteDescrito (com descricao)
	 * Descricao: Executa um ajuste somente em um tipo de saldo
	 * @param MSISDN               	- numero do msisdn
	 * @param tipoTransacao        	- tipo de trnsacao
	 * @param tipoCredito          	- tipo de credito (00,01,02,03 para principal,bonus,sms e dados respectivamente)
	 * @param valor                	- valor da recarga
	 * @param tipo              	- tipo de ajuste
	 * @param dataHora             	- data e hora da recarga
	 * @param sistemaOrigem         - sistema de Origem        
	 * @param operador              - operador que realizou a recarga 	 
	 * @param data_expiracao   		- data de expiracao
	 * @param descricao				- Descricao do Ajuste
	 * @return	short 				- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPTecnomenException
	 */
	public short executaAjusteDescrito(String MSISDN, String tipoTransacao, String tipoCredito, double valor, String tipo, String dataHora, String sistemaOrigem, String operador, String data_expiracao, String descricao) throws 	GPPInternalErrorException, GPPCorbaException, GPPTecnomenException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjuste", "Inicio da Execucao de Ajuste.");
		
		// Criando uma classe de aplicacao
		Ajustar ajuste = new Ajustar(idProcesso);
		
		try
		{
			//Criando os parsers das datas informadas.
			SimpleDateFormat	conversorDataHoraGPP	= new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
			SimpleDateFormat	conversorDatDia			= new SimpleDateFormat(Definicoes.MASCARA_DAT_DIA);
			
			// executa o ajuste
			retorno = ajuste.executarAjuste(MSISDN,
											tipoTransacao,
											tipoCredito,
											valor,
											tipo,
											conversorDataHoraGPP.parse(dataHora),
											sistemaOrigem,
											operador,
											(!data_expiracao.equals("")) ? conversorDatDia.parse(data_expiracao) : null,
											null,
											descricao,
											true);
			
			if(retorno != 0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
		}
		catch(ParseException e)
		{
			this.Log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Formato de data invalido.");
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecargaBanco", "MSISDN: " + MSISDN + " - Data/hora: " + dataHora);
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaRecargaBanco", "MSISDN: " + MSISDN + " - Data de expiracao: " + data_expiracao);
			retorno = Definicoes.RET_FORMATO_DATA_INVALIDA;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjuste", "Fim da Execucao de Ajuste.");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: executaAjusteMultiplosSaldos
	 * Descricao: Executa ajuste de saldos múltiplos
	 * @param MSISDN               	- numero do msisdn
	 * @param tipoTransacao        	- tipo de trnsacao
	 * @param tipoCredito          	- tipo de credito
	 * @param valor                	- valor da recarga
	 * @param tipo              	- tipo de ajuste
	 * @param dataHora             	- data e hora da recarga
	 * @param sistemaOrigem         - sistema de Origem        
	 * @param operador              - operador que realizou a recarga 	 
	 * @param data_expiracao   		- data de expiracao
	 * @return	short 				- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPCorbaException
	 * @throws 	GPPTecnomenException
	 */
	public short executaAjusteMultiplosSaldos(String MSISDN, 
											  String tipoTransacao, 
											  double ajustePrincipal, 
											  double ajustePeriodico, 
											  double ajusteBonus, 
											  double ajusteSms, 
											  double ajusteGprs,
											  String expPrincipal, 
											  String expPeriodico, 
											  String expBonus, 
											  String expSms, 
											  String expGprs, 
											  String tipo, 
											  String dataHora, 
											  String sistemaOrigem, 
											  String operador)
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso = 0;
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		// Obtém um ID do processo para escrita no LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjuste", "Inicio da Execucao de Ajuste.");
		
		// Criando uma classe de aplicacao
		Ajustar ajuste = new Ajustar(idProcesso);
		
		try
		{
			//Criando os parsers das datas informadas.
			SimpleDateFormat	conversorDataHoraGPP	= new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
			SimpleDateFormat	conversorDate			= new SimpleDateFormat(Definicoes.MASCARA_DATE);
			
			ValoresRecarga valores = new ValoresRecarga(ajustePrincipal,
														ajustePeriodico,
														ajusteBonus, 
														ajusteSms, 
														ajusteGprs,
														(!expPrincipal.equals("")) ? conversorDate.parse(expPrincipal) : null, 
														(!expPeriodico.equals("")) ? conversorDate.parse(expPeriodico) : null, 
														(!expBonus.equals    ("")) ? conversorDate.parse(expBonus    ) : null, 
														(!expSms.equals      ("")) ? conversorDate.parse(expSms      ) : null, 
														(!expGprs.equals     ("")) ? conversorDate.parse(expGprs     ) : null);
			
			// executa o ajuste.
			retorno = ajuste.executarAjuste(MSISDN,
											tipoTransacao,
											Definicoes.TIPO_CREDITO_REAIS,
											valores,
											tipo,
											conversorDataHoraGPP.parse(dataHora),
											sistemaOrigem,
											operador,
											null,
											null,
											true,
                                            null);
			
			if(retorno != 0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
		}
		catch(ParseException e)
		{
			this.Log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Formato de data invalido.");
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data/hora: " + dataHora);
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data de expiracao do Saldo Principal: " + expPrincipal);
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data de expiracao do Saldo Periodico: " + expPeriodico);
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data de expiracao do Saldo Principal: " + expBonus    );
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data de expiracao do Saldo Principal: " + expSms      );
			this.Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_RECARGA, "executaAjuste", "MSISDN: " + MSISDN + " - Data de expiracao do Saldo Principal: " + expGprs     );
			retorno = Definicoes.RET_FORMATO_DATA_INVALIDA;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"executaAjuste", "Fim da Execucao de Ajuste.");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo....: consultaPreRecarga
	 * Descricao.: Realiza as validacoes antes da realizacao das recargas
	 * 			   atraves dos Bancos, prevendo possiveis falhas na 
	 * 			   execucao das mesmas.
	 * 
	 * @param  MSISDN		 - Numero do assinante
	 * @param  valorCreditos - Valor do credito a ser concedido
	 * @param  tipoTransacao - Tipo da transacao
	 * @param  origem		 - Origem (SistemaOrigem)
	 * @return retorno		 - Resultado da validacao
	 */
	public short consultaPreRecarga(String MSISDN, double valorCreditos, String tipoTransacao, String origem)
	{
		// Inicializa variaveis do metodo
		short retorno = 99;
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"podeRecarregar", "Inicio da Consulta pré-recarga efetuada pelos Bancos.");
		
		// Criando classes de aplicacao
		Recarregar recarga = new Recarregar(idProcesso);
		
		try
		{
			retorno = recarga.consultaPreRecarga(MSISDN, valorCreditos, tipoTransacao, origem);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_RECARGA,"podeRecarregar","Retorno Pre-Recarga Banco: "+retorno);
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: podeRecarregar
	 * Descricao: Consulta pré-recarga efetuada pelos Bancos
	 * 
	 * @param MSISDN        - numero do msisdn
	 * @param valorCreditos	- valor dos creditos
	 * @return	short 		- Sucesso(0) ou erro (diferente de 0)
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPTecnomenException
	 */
	public short podeRecarregar (String MSISDN, double valorCreditos)
	{
		// Inicializa variaveis do metodo
		short retorno = 99;
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"podeRecarregar", "Inicio da Consulta pré-recarga efetuada pelos Bancos.");
		
		// Criando classes de aplicacao
		Recarregar recarga = new Recarregar(idProcesso);
		
		try
		{
			retorno = recarga.consultaPreRecarga(MSISDN, valorCreditos);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_RECARGA,"podeRecarregar","Retorno Pre-Recarga Banco: "+retorno);
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		
		return retorno;
	}
	
	/**
	 * Metodo...: podeRecarregarVarejo
	 * Descricao: Valida Recarga para Varejo
	 * @param String	MSISDN			Msisdn do assinante
	 * @param double	valorCreditos	Valor da Recarga
	 * @param String	tipoTransacao	Transaction Type da Recarga
	 * @return	short	0, se ok ou erro funcional
	 * @throws GPPTecnomenException
	 * @throws GPPInternalErrorException
	 */
	public short podeRecarregarVarejo (String MSISDN, double valorCreditos, String tipoTransacao )	throws  GPPTecnomenException, GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		// Obtém um ID do processo para escrita no LOG
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"podeRecarregarVarejo", "Inicio da Consulta pré-recarga de Varejo");
		
		try
		{
			// Foi alterada a chamada para o metodo local, o qual fara
			// o trabalho das validacoes do Varejo
			retorno = this.consultaPreRecarga(MSISDN, valorCreditos, tipoTransacao, Definicoes.SO_VAREJO);
			
			if(retorno != 0)
				sucessoFalha = Definicoes.PROCESSO_ERRO;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"podeRecarregarVarejo", "Fim da Consulta pre-recarga de Varejo");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo...: alteraStatusVoucher
	 * Descricao: Altera o status do voucher
	 * @param String	numeroVoucher	Numero do voucher
	 * @param int    	statusVoucher	Novo status do voucher
	 * @param String	comentario	    Comentario
	 * @return	short	0, se ok ou erro funcional
	 * @throws GPPTecnomenException
	 * @throws GPPInternalErrorException
	 */
	public short alteraStatusVoucher (String numeroVoucher, double statusVoucher, String comentario )	throws  GPPTecnomenException, GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		short retorno = 0;
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"alteraStatusVoucher", "Inicio da alteracao de status voucher");
		
		// Criando uma classe de aplicacao
		OperacoesVoucher alteracao = new OperacoesVoucher(idProcesso);
		
		try
		{
			// Obtém um ID do processo para escrita no LOG
			idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
			
			// consulta a pre recarga de bancos
			retorno = (short)alteracao.atualizaStatusVoucher(numeroVoucher, (int)statusVoucher, comentario);
			
			if(retorno == 0)
			{
				alteracao.insereCancelamentoVoucher(numeroVoucher, comentario);
			}
			else
			{
				sucessoFalha = Definicoes.PROCESSO_ERRO;
			}
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_RECARGA,"alteraStatusVoucher", "Erro na alteracao de status do voucher: "+e);
			
			throw new GPPInternalErrorException("Erro GPP: "+e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_RECARGA,"alteraStatusVoucher", "Fim da alteracao de status do voucher");
			
			// libera o ID do processo
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
		return retorno;
	}
	
	/**
	 * Metodo....:resubmeterPedidoVoucher
	 * Descricao.:Resubmete ao processo de criacao de vouchers um determinado pedido.
	 *            OBS: Somente pedidos com erro poderao ser resubmetido
	 * @param numeroPedido	- Numero do pedido de voucher a ser resubmetido
	 * @throws GPPInternalErrorException
	 */
	public void resubmeterPedidoVoucher(long numeroPedido) throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
		String sucessoFalha = Definicoes.PROCESSO_SUCESSO;
		try
		{
			GerenciaPedidosVoucher.resubmeterPedidoVoucher(numeroPedido);
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Erro ao resubmeter pedido de voucher.Erro:"+e);
		}
		finally
		{
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_RECARGA, sucessoFalha);
		}
	}
	
	/**
	 * Por: Bernardo Vergne Dias
	 * Metodo...: aprovarLoteRecarga
	 * Descricao: Aprova lote de recarga em massa
	 * @param	lote 			- Identificador do lote
	 * @param 	usuario 		- Usuario responsavel pela aprovação
	 */
	public short aprovarLoteRecarga ( String lote, String usuario )
	{
		try
		{
			long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
			ConcessaoRecargaMassa concessao = new ConcessaoRecargaMassa (idProcesso);
			return concessao.aprovarLoteRecarga(lote,usuario);
		}
		catch (Exception e)
		{
			return Definicoes.RET_ERRO_TECNICO;
		}
	}
	
	/**
	 * Por: Bernardo Vergne Dias
	 * Metodo...: rejeitarLoteRecarga
	 * Descricao: Rejeita lote de recarga em massa
	 * @param	lote 			- Identificador do lote
	 * @param 	usuario 		- Usuario responsavel pela aprovação
	 */
	public short rejeitarLoteRecarga ( String lote, String usuario )
	{
		try
		{
			long idProcesso = Log.getIdProcesso(Definicoes.CN_RECARGA);
			ConcessaoRecargaMassa concessao = new ConcessaoRecargaMassa (idProcesso);
			return concessao.rejeitarLoteRecarga(lote,usuario);
		}
		catch (Exception e)
		{
			return Definicoes.RET_ERRO_TECNICO;
		}
	}
}