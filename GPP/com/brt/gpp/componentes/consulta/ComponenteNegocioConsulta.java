// Definicao do Pacote
package com.brt.gpp.componentes.consulta;

//Classes de POA da Consulta
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

// Classes de POA da Consulta
import com.brt.gpp.componentes.consulta.orb.consultaPOA;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.*;
import com.brt.gpp.aplicacoes.consultar.consultaAparelho.ConsultaAparelho;
import com.brt.gpp.aplicacoes.consultar.consultaRecargasPeriodo.ConsultaRecargasPeriodo;
import com.brt.gpp.aplicacoes.consultar.consultaEstornoPulaPula.ConsultaEstornoPulaPula;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.*;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula.*;
import com.brt.gpp.aplicacoes.consultar.gerarExtratoBoomerang.*;
import com.brt.gpp.aplicacoes.contestar.consultaStatusBS.ConsultaStatusBS;
import com.brt.gpp.aplicacoes.contestar.publicacaoBS.PublicadorBS;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.*;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.recarregar.Recarregar;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.masc.ConexaoMASC;
import com.brt.gpp.comum.gppExceptions.*;

/**
  *
  * Este arquivo contem a definicao da classe componente de negocio de Consulta. Ela 
  * e responsavel pela logica de negocio para as consultas na base de dados do GPP 
  * e na plataforma Tecnomen
  *
  * @autor:				Camile Cardoso Couto
  * @Data:				15/03/2004
  *
  * Modificado Por:
  * Data:
  * Razao:
  */
public class ComponenteNegocioConsulta extends consultaPOA
{  
	//	Variaveis Membro
	protected GerentePoolLog Log = null; // Gerente de LOG
	
	/**
	 * Metodo...: ComponenteNegocioConsulta
	 * Descricao: Construtor 
	 * @param	
	 * @return									
	 */
	public ComponenteNegocioConsulta ( )
	{
		//Obtem referencia ao gerente de LOG
		this.Log = GerentePoolLog.getInstancia(this.getClass());
		Log.logComponente (Definicoes.INFO, Definicoes.CN_CONSULTA, ": Componente de Negocio ativado..." );
	}
	
	/**
	 * Metodo...: consultaVoucher
	 * Descricao: Consulta dados de voucher na plataforma Tecnomen
	 * @param	voucherId 	- Identificador do voucher
	 * @return	String 		- Codigo de retorno + voucherId + codStatus + descStatus + valorFace + MSISDN + dtUltimaAtualizacao
	 * @throws GPPInternalErrorException
	 */
	public String consultaVoucher ( String voucherId) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno = null;		
		long idProcesso=0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
			
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaVoucher", "Inicio VOUCHERID "+voucherId);

		try
		{
			// Criando uma classe de aplicacao
			ConsultaVoucher consultaVoucher = new ConsultaVoucher (idProcesso);

			retorno= consultaVoucher.run(voucherId);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaVoucher", "Erro Consulta Voucher");

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaVoucher", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	/**
	 * Metodo...: consultaAssinante
	 * Descricao: Consulta dados de assinante na plataforma Tecnomen
	 * @param	aMsisdn 	- Numero do telefone a ser consultado
	 * @return	String 		- Codigo de retorno + MSISDN + Codigo do Plano de Preco + 
	 * 						Descricao do Plano de Preco + Saldo de Creditos + 
	 * 						Codigo do Status MSISDN + Descricao do Status do MSISDN +
	 * 						Codigo do Status do Servico + Descricao do Status do Servico +
	 * 						Data de Expiracao									
	 * @throws GPPInternalErrorException
	 */
	public String consultaAssinante (String aMsisdn) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno = null;		
		long idProcesso=0;
	
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Inicio MSISDN "+aMsisdn);

		try
		{
			// Criando uma classe de aplicacao
			ConsultaAssinante consultaAssinante = new ConsultaAssinante (idProcesso);

			retorno= consultaAssinante.executaConsultaAssinante(aMsisdn, Definicoes.CONSULTA_ASSINANTE_COMPLETA);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Erro Consulta Assinante.");

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}

	/**
	 * Metodo...: consultaAssinanteSimples
	 * Descricao: Consulta os dados simplificados de assinante na plataforma Tecnomen
	 * @param	aMsisdn 	- Numero do telefone a ser consultado
	 * @return	String 		- Codigo de retorno + MSISDN + Codigo do Plano de Preco + 
	 * 						+ Saldo de Creditos + Codigo do Status MSISDN 
	 * 						+ Codigo do Status do Servico + Data de Expiracao									
	 * @throws GPPInternalErrorException
	 */
	public String consultaAssinanteSimples (String aMsisdn) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno = null;		
		long idProcesso=0;
	
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Inicio MSISDN "+aMsisdn);

		try
		{
			// Criando uma classe de aplicacao
			ConsultaAssinante consultaAssinante = new ConsultaAssinante (idProcesso);

			retorno = consultaAssinante.executaConsultaAssinante(aMsisdn, Definicoes.CONSULTA_ASSINANTE_SIMPLES);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Erro Consulta Assinante.");

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}

	/**
	 * Metodo...: consultaAssinanteRecarga (=>>> Método não utilizado)
	 * Descricao: Consulta dados de assinante na plataforma Tecnomen e no Banco de Dados do GPP
	 * @param	aMsisdn 			- Numero do telefone a ser consultado
	 * @param	aValorRecarga 		- Valor Total da Recarga a ser creditada
	 * @param	aCPF				- Numero do CPF/CNPJ possuidor do CC
	 * @param	aCategoria			- Codigo que identifica a categoria do cliente comprador (Pos, Pre ou Hibrido)
	 * @param	aHashCartaoCredito 	- Numero do Cartão de Credito encriptado
	 * @param	aSistemaOrigem		- Identificador do sistema de origem da consulta
	 * @return	String 				- Codigo de retorno (OK ou NOK) + Descricao do retorno 								
	 * @throws GPPInternalErrorException
	 */
	public String consultaAssinanteRecarga (String aMsisdn, double aValorRecarga, String aCPF, short aCategoria, 
										String aHashCartaoCredito, String aSistemaOrigem) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno = null;		
/*		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecarga", "Inicio da Consulta de Recarga.");
	
		try
		{
			// Criando uma classe de aplicacao
			ConsultaAssinante consultaAssinante = new ConsultaAssinante (idProcesso);

			retorno= consultaAssinante.executaConsultaAssinanteRecarga(aMsisdn, aValorRecarga, aCPF, aCategoria, aHashCartaoCredito, aSistemaOrigem);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecarga", "Erro na Consulta de Recarga.");

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		*/
		return retorno;
	}
	
	/**
	 * Metodo...: consultaAssinanteRecargaXML
	 * Descricao: Consulta pré-recarga usando o TOTAL das recargas a serem efetuadas
	 * @param	aXMLConsultaAssinantePreRecarga - Numero do telefone a ser consultado
	 * @return	String 							- Codigo de retorno (OK ou NOK) + descricao retorno								
	 * @throws GPPInternalErrorException
	 * @throws GPPCorbaException
	 * @throws GPPBadXMLFormatException
	 */
	public String consultaAssinanteRecargaXML (String aXMLConsultaAssinantePreRecarga) 
											throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaXML", "Inicio");
		
		try
		{
			// Criando uma classe de aplicacao
			ConsultaAssinante consultaAssinante = new ConsultaAssinante(idProcesso);
			
			DadosConsultaRecarga dadosRecarga = consultaAssinante.parseXMLConsultaPreRecarga(aXMLConsultaAssinantePreRecarga, "Unica");

			// Nesse caso, o parametro recargas do objeto dadosConsulta contém apenas seu primeiro elemento com valores válidos
			retorno = consultaAssinante.executaConsultaAssinantePreRecarga(aXMLConsultaAssinantePreRecarga, "Unica", dadosRecarga);
		}
		catch(GPPBadXMLFormatException e)
		{
			Log.log(idProcesso,Definicoes.WARN, Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaXML", "Excecao GPPBadXMLFormatException: " + e);
	 		retorno = Definicoes.RET_S_CAMPO_OBRIGATORIO;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaXML", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	/**
	 * Metodo...: consultaAssinanteRecargaMultiplaXML
	 * Descricao: Consulta pré-recarga usando o TOTAL das recargas a serem efetuadas
	 * @param	aXMLConsultaAssinantePreRecarga - Numero do telefone a ser consultado
	 * @return	String 							- Codigo de retorno (OK ou NOK) + descricao retorno								
	 * @throws GPPInternalErrorException
	 * @throws GPPCorbaException
	 * @throws GPPBadXMLFormatException
	 */
	public String consultaAssinanteRecargaMultiplaXML (String aXMLConsultaAssinantePreRecarga) 
											throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaMultiplaXML", "Inicio");


		// Verifica qual o parser deverá ser usado
		DadosConsultaRecarga dadosRecarga = new DadosConsultaRecarga();
		
		// Criando uma classe de aplicacao
		ConsultaAssinante consultaAssinante = new ConsultaAssinante(idProcesso);
	
		try
		{
			dadosRecarga = consultaAssinante.parseXMLConsultaPreRecarga(aXMLConsultaAssinantePreRecarga, "Multipla");
			
			if (dadosRecarga.getSistemaOrigem().equals(Definicoes.SO_DEALER))
			{
				Recarregar recarga = new Recarregar(idProcesso);
				short retornoVarejo = recarga.consultaPreRecargaVarejo(dadosRecarga.getMSISDN(0), dadosRecarga.getValorRecarga(0), dadosRecarga.getTipoTransacao());
				ArrayList dadosRetorno = new ArrayList();
				retornoGenerico retornoRecarga = new retornoGenerico();
				retornoRecarga.msisdn = dadosRecarga.getMSISDN(0);
				retornoRecarga.codigoRetorno = retornoVarejo;
				retornoRecarga.descricao = consultaAssinante.getDescricaoRetorno(retornoVarejo);
				dadosRetorno.add(retornoRecarga);
				retorno = consultaAssinante.constroiXMLRetornoConsultaPreRecarga(dadosRetorno, retornoVarejo, "Multipla");
			}
			else
			{
				retorno = consultaAssinante.executaConsultaAssinantePreRecarga(aXMLConsultaAssinantePreRecarga, "Multipla", dadosRecarga);
			}
		}
		catch(GPPBadXMLFormatException e)
		{
			Log.log(idProcesso,Definicoes.WARN, Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaMultiplaXML", "Excecao GPPBadXMLFormatException: " + e);
	 		retorno = Definicoes.RET_S_CAMPO_OBRIGATORIO;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaAssinanteRecargaMultiplaXML", "Fim");
	
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	
	/**
	 * Metodo...: consultaAparelhoAssinante
	 * Descricao: Consulta dados do aparelho do assinante
	 * @param	aMsisdn 			- Numero do telefone a ser consultado
	 * @return	String 				- XML de retorno da pesquisa 								
	 * @throws GPPInternalErrorException
	 */
	public String consultaAparelhoAssinante (String aMsisdn) throws GPPInternalErrorException
	{
		String retorno = null;		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaAparelhoAssinante", "Inicio MSISDN "+aMsisdn);
	
		try
		{
			// Criando uma classe de aplicacao
			ConsultaWIG consultaAparelho = new ConsultaWIG (idProcesso);

			retorno= consultaAparelho.consultaWIGInformacoesAparelho(aMsisdn);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_CONSULTA,"consultaAparelhoAssinante", "Erro Consulta Aparelho."+e);
			throw new GPPInternalErrorException ("Excecao Interna GPP: " +e);
		}
		finally
		{
			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	/**
	 * Metodo...: consultaExtrato
	 * Descricao: Consulta Extrato de acesso pré-pago de um determinado período
	 * @param	msisdn			- Numero do msisdn
	 * @param	inicioPeriodo 	- Data  do extrato no formato dd/mm/aaaa
	 * @param	finalPeriodo 	- Data  do extrato no formato dd/mm/aaaa
	 * @return	String 			- XML contendo as informações do extrato
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public String consultaExtrato (String msisdn, String inicioPeriodo, String finalPeriodo) 
											throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaExtrato", "Inicio MSISDN "+msisdn);

		try
		{
			// Criando uma classe de aplicacao
			GerarExtrato geradorExtrato = new GerarExtrato(idProcesso);

			retorno = geradorExtrato.comporExtrato(msisdn, inicioPeriodo, finalPeriodo, false, null);
		}
		catch (GPPInternalErrorException e) 
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaExtrato", "Erro Consulta Extrato.");

			throw new GPPInternalErrorException ("Excecao Erro GPP: " +e);
		}
		catch (GPPTecnomenException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaExtrato", "Erro Consulta Extrato.");

			throw new GPPInternalErrorException ("Excecao Tecnomen: " +e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_APROVISIONAMENTO,"consultaExtrato", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	/**
	 * Metodo...: consultaExtratoPulaPula
	 * Descricao: Gera um extrato dos bônus acumulados pelo cliente por chamadas recebidas
	 * @param 	String		aMSISDN			Msisdn do assinante
	 * @param 	String		aInicioPeriodo	Data Inicial (dd/mm/yyyy) de busca por ligações recebidas
	 * @param 	String		aFinalPeriodo	Data Final (dd/mm/yyyy) de busca por ligações recebidas
	 * @return	String		XML com o extrato
	*/
	public String consultaExtratoPulaPula(String msisdn, String inicioPeriodo, String finalPeriodo)
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		// Criando uma classe de aplicacao
		GerarExtratoPulaPula geradorExtrato = new GerarExtratoPulaPula(idProcesso);

		retorno = geradorExtrato.comporExtrato(msisdn, inicioPeriodo, finalPeriodo, false);
		
		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);
			
		return retorno;		
	}

	/**
	 * Metodo...: consultaExtratoPulaPulaCheio
	 * Descricao: Gera um extrato cheio dos bônus acumulados pelo cliente por chamadas recebidas
	 * @param 	String		aMSISDN			Msisdn do assinante
	 * @param 	String		aInicioPeriodo	Data Inicial (dd/mm/yyyy) de busca por ligações recebidas
	 * @param 	String		aFinalPeriodo	Data Final (dd/mm/yyyy) de busca por ligações recebidas
	 * @return	String		XML com o extrato
	*/
	public String consultaExtratoPulaPulaCheio(String msisdn, String inicioPeriodo, String finalPeriodo)
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		// Criando uma classe de aplicacao
		GerarExtratoPulaPula geradorExtrato = new GerarExtratoPulaPula(idProcesso);

		retorno = geradorExtrato.comporExtrato(msisdn, inicioPeriodo, finalPeriodo, true);
		
		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);
			
		return retorno;		
	}
	
	/**
	 * Metodo...: consultaSaldoPulaPula
	 * Descricao: Consulta Saldo Pula-Pula acumulado no mês corrente
	 * @param 	String	_msisdn		Msisdn do assinante
	 * @return	double		Saldo Pula-Pula acumulado no mês corrente
	 */
	public String consultaSaldoPulaPula(String _msisdn)throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="000";
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaSaldoPulaPula", "Inicio MSISDN: "+_msisdn);

		// Criando uma classe de aplicacao
		ConsultaSaldoPulaPula informanteSaldo = new ConsultaSaldoPulaPula(idProcesso);

		retorno = informanteSaldo.getSaldoPulaPula(_msisdn);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaSaldoPulaPula", "Fim");

		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			

		return retorno;
	}

	/**
	 * Metodo...: consultaSaldoPulaPulaNoMes
	 * Descricao: Consulta Saldo Pula-Pula acumulado no mês desejado
	 * @param 	String	_msisdn		Msisdn do assinante
	 * @param   int		mes			mes a ser utilizado para processamento
	 * @return	String				Saldo Pula-Pula acumulado no mês desejado
	 */
	public String consultaSaldoPulaPulaNoMes(String _msisdn, int mes)throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="000";
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaSaldoPulaPulaNoMes", "Inicio MSISDN: "+_msisdn+" mes:"+mes);

		// Criando uma classe de aplicacao
		ConsultaSaldoPulaPula informanteSaldo = new ConsultaSaldoPulaPula(idProcesso);
		// Formata o resultado como String para manter compatibilidade com a IDL atual
		DecimalFormat df = new DecimalFormat("##########000");
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(0);

		retorno = df.format(informanteSaldo.getSaldoPulaPula(_msisdn,mes)*1000);
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaSaldoPulaPulaNoMes", "Fim");

		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		return retorno;
	}

	/**
	 * Metodo...: consultaPulaPula
	 * Descricao: Consulta as informacoes da promocao Pula-Pula do assinante
	 * @param 	String			msisdn		Msisdn do assinante
	 * @return	String						XML com as informacoes da promocao Pula-Pula.
	 */
	public String consultaPulaPula(String msisdn)
	{
		// Inicializa variaveis do metodo
		String result = null;
		long idProcesso = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaPromocaoPulaPula", "Inicio MSISDN " + msisdn);

		// Criando uma classe de aplicacao
		ControlePulaPula controle = new ControlePulaPula(idProcesso);

		result = controle.consultaPromocaoPulaPulaXML(msisdn);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaPromocaoPulaPula", "Fim");

		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			

		return result;
	}
	
	/**
	 * Metodo...:	consultaPulaPulaNoMes
	 * Descricao:	Consulta as informacoes da promocao Pula-Pula do assinante no mes informado.
	 * 				OBS: O mes passado sera o de referencia para concessao do bonus para o assinante, ou seja,
	 * 					 o mes em que o assinante recebe o bonus referente as ligacoes do mes anterior.
	 * @param 	String			msisdn		Msisdn do assinante
	 * @return	String						XML com as informacoes da promocao Pula-Pula.
	 */
	public String consultaPulaPulaNoMes(String msisdn, String mes)
	{
		// Inicializa variaveis do metodo
		String result = null;
		long idProcesso = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaPromocaoPulaPula", "Inicio MSISDN " + msisdn);

		// Criando uma classe de aplicacao
		ControlePulaPula controle = new ControlePulaPula(idProcesso);

		result = controle.consultaPromocaoPulaPulaXML(msisdn, mes);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaPromocaoPulaPula", "Fim");

		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			

		return result;
	}
	
	/**
	 * Metodo...:	consultaEstornoPulaPula
	 * Descricao:	Consulta as informacoes de estorno de bonus Pula-Pula por fraude para o assinante.
	 * @param 	String			msisdn		MSISDN do assinante.
	 * @param	String			dataInicio	Data inicial da consulta, no formato dd/MM/yyyy.
	 * @param	String			dataFim		Data final da consulta, no formato dd/MM/yyyy.
	 * @return	String						XML com as informacoes do estorno.
	 */
	public String consultaEstornoPulaPula(String msisdn, String dataInicio, String dataFim)
	{
		// Inicializa variaveis do metodo
		String result = null;
		long idProcesso = 0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaEstornoPulaPula", "Inicio MSISDN " + msisdn + " Inicio " + dataInicio + " Fim " + dataFim);

		// Criando uma classe de aplicacao
		ConsultaEstornoPulaPula consulta = new ConsultaEstornoPulaPula(idProcesso);

		result = consulta.consultaEstornoPulaPula(msisdn, dataInicio, dataFim);

		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_CONSULTA,"consultaEstornoPulaPula", "Fim");

		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			

		return result;
	}
	
	/**
	 * Metodo....:consultaJobTecnomen
	 * Descricao.:Realiza a consulta de job na plataforma tecnomen
	 * @param numeroJob	- Numero do Job
	 * @throws GPPInternalErrorException
	 */
	public InfoJobTecnomen consultaJobTecnomen(int numeroJob)
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		// Pega a referencia para a classe que ira realizar a consulta do job
		OperacoesVoucher opVoucher = new OperacoesVoucher(idProcesso);
		JobTecnomen job = opVoucher.getJobTecnomen(numeroJob);
		// Apos consultar as informacoes do job caso este exista entao altera as
		// propriedades do objeto que sera retornado para o cliente
		// OBS: As informacoes quando o job for nulo significam informacoes de um
		// job nao existente, porem como nao pode ser nulo a resposta entao o objeto
		// vai preenchido com as informacoes indicando este estado
		InfoJobTecnomen infoJob = new InfoJobTecnomen();
		infoJob.numeroJob 	= numeroJob;
		infoJob.codStatus	= job != null ? job.getOpState() 	: 3002;
		infoJob.descStatus	= job != null ? job.getStatusJob()	: "Job nao existente";
		infoJob.workTotal	= job != null ? job.getWorkTotal()	: 1;
		infoJob.workDone	= job != null ? job.getWorkDone()	: 1;
		
		return infoJob;
	}
	
	/**
	 * Metodo...: consultaExtratoBoomerang
	 * Descricao: Gera um extrato dos bônus boomerang
	 * @param 	String		aMSISDN					Msisdn do assinante
	 * @param 	String		inicioPeriodo			Data do inicio do período (dd/mm/yyyy(
	 * @param   String		finalPeriodo			Data do final do periódo (dd/mm/yyyy)
	 * @return	String		XML com o extrato
	*/
	public String consultaExtratoBoomerang(String msisdn, String inicioPeriodo, String finalPeriodo) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		String retorno="";		
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO,"consultaExtratoBoomerang", "Inicio do consultaExtratoBoomerang");

		try
		{
			// Criando uma classe de aplicacao
			GerarExtratoBoomerang geradorExtrato = new GerarExtratoBoomerang(idProcesso);

			retorno = geradorExtrato.comporExtrato(msisdn, inicioPeriodo, finalPeriodo);
		}
		catch (GPPInternalErrorException e) 
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaExtratoBoomerang", "Erro Consulta Extrato Boomerang");

			throw new GPPInternalErrorException ("Excecao Erro GPP: " +e);
		}
		catch (GPPTecnomenException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"consultaExtratoBoomerang", "Erro(TECNOMEN) Consulta Extrato Boomerang");

			throw new GPPInternalErrorException ("Excecao Tecnomen: " +e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaExtratoBoomerang", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;		
	}
	
	/**
	 * Metodo...: consultaSaldoPulaPula
	 * Descricao: Consulta Saldo Pula-Pula acumulado no mês corrente
	 * @param 	String	msisdn		Msisdn do assinante
	 * @param   int		mes			Mes de consulta do credito concedido
	 * @return	SaldoBoomerang		Saldo Boomerang acumulado no desejado
	 */
	public SaldoBoomerang consultaSaldoBoomerang(String msisdn, int mes)throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		SaldoBoomerang saldoBoomerang = new SaldoBoomerang();
		saldoBoomerang.msisdn = msisdn;
		long idProcesso=0;

		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaSaldoBoomerang", "Inicio MSISDN: "+msisdn);
		// Criando uma classe de aplicacao
		ConsultaSaldoBoomerang informanteSaldo = new ConsultaSaldoBoomerang(idProcesso);
		saldoBoomerang = informanteSaldo.getSaldoBoomerang(msisdn,mes);

		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaSaldoBoomerang", "Fim");
		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		return saldoBoomerang;
	}
	
	/**
	 * Metodo...: consultaRecargaAntifraude
	 * Descricao: Consulta as recargas realizadas por um cliente pré-pago em um período
	 * @param 	String	aXML		XML contendo os dados para a consulta
	 * @return	String	retornoXML	XML contendo os dados retornados pela consulta
	 * @throws GPPInternalErrorException
	 * @throws GPPBadXMLFormatException
	 */
	public String consultaRecargaAntifraude(String aXML)throws GPPInternalErrorException, GPPBadXMLFormatException
	{
	    // Obtendo um ID de processo para o LOG
	    long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaRecargaAntifraude", "Inicio");
		
		// Cria o objeto
		
		// Criando uma classe de aplicacao
		ConsultaRecargasPeriodo recargas = new ConsultaRecargasPeriodo(idProcesso);
		
		// Execução de consulta das recargas 
		String retornoXML = recargas.executaConsultaRecargas(aXML);

		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaRecargaAntifraude", "Resposta: " + retornoXML);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaRecargaAntifraude", "Fim");
		
		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		
		return retornoXML;
	}
	
	/**
	 * Metodo...: consultaAparelho
	 * Descricao: Consulta de características do aparelho do assinante
	 * @param 	String	msisdn		Numero do telefone a ser consultado
	 * @return	String	retornoXML	XML contendo os dados retornados pela consulta
	 * @throws GPPInternalErrorException
	 */
	public String consultaAparelho(String msisdn)throws GPPInternalErrorException
	{
	    // Obtendo um ID de processo para o LOG
	    long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaAparelho", "Inicio");
		
		// Criação do objeto
		ConsultaAparelho aparelho = new ConsultaAparelho(idProcesso);
		
		// Execução de consulta das recargas 
		String retornoXML = aparelho.executaConsultaAparelho(msisdn);
		
		ConsultaAparelho.parseXMLConsultaAparelho(retornoXML);

		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultaAparelho", "Fim");
		
		// Libera o ID de processo para o LOG
		Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		
		return retornoXML;
	}
	
	/**
	 * Metodo...: consultaAssinanteTFPP
	 * Descricao: Consulta a Assinantes TFPP
	 * 				Atenção: Essa consulta não tem implementada a persistência. Isso é,
	 * 				se ocorrer erro técnico, a consulta não será refeita.
	 * 				Essa API foi desenvolvida EXCLUSIVAMENTE para uso em testes
	 * @param 	String 	aMSISDN		Numero do telefone a ser consultado
	 * @return 	String 	retorno 	XML contando os dados retornados pela consulta
	 * @throws GPPInternalErrorException
	 */
	public String consultaAssinanteTFPP(String aMSISDN) throws GPPInternalErrorException
	{
		Assinante assinante = null;
		String retorno = null;
		long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		Log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_CONSULTA, "consultaAssinanteTFPP", "Inicio");
		try
		{
			ConexaoMASC conexaoMASC = new ConexaoMASC(idProcesso);
			assinante = conexaoMASC.enviaConsultaMASC(aMSISDN);
			if(assinante.getRetorno() == Definicoes.RET_OPERACAO_OK)
			{
				GerarXML gerador = new GerarXML("ConsultaTFPP");
				gerador.adicionaTag("MSISDN", assinante.getMSISDN());
				DecimalFormat df = new DecimalFormat("#,##0.00",new DecimalFormatSymbols(Locale.ENGLISH));
				gerador.adicionaTag("Saldo", df.format(assinante.getCreditosPrincipal()));
				gerador.adicionaTag("DataExpiracao", assinante.getDataExpiracaoPrincipal());
				retorno = gerador.getXML();
			}
			else
			{
				String erro = (new Short(assinante.getRetorno())).toString();
				retorno = "Erro " + erro;
			}
			
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_CONSULTA,"consultaAssinanteTFPP", "Erro Consulta Assinante TFPP."+e);
			throw new GPPInternalErrorException ("Excecao Interna GPP: " +e);
		}
		finally
		{
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);
		}
		return(retorno);
	}
	
	
	
	/**
	 * Metodo...: consultarCreditoPulaPula
	 * Descricao: Consulta dados de assinante na plataforma Tecnomen
	 * @param	msisdn 	- Numero do telefone a ser consultado
	 * @return	mes 	- mes atual								
	 * @throws Exception
	 */
	public double consultarCreditoPulaPula(String msisdn, String mes) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		double retorno;		
		long idProcesso=0;
	
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		
		Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultarCreditoPulaPula", "Inicio MSISDN "+msisdn);

		try
		{
			// Criando uma classe de aplicacao
			ControlePulaPula consultaCreditoPP = new ControlePulaPula(idProcesso);

			retorno= consultaCreditoPP.consultarCreditoPulaPula(msisdn,mes);
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_CONSULTA,"consultarCreditoPulaPula", "Erro Consulta Credito Pula Pula.");

			throw new GPPInternalErrorException ("Excecao Interna do GPP: " +e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.DEBUG,Definicoes.CN_CONSULTA,"consultarCreditoPulaPula", "Fim");

			// Libera o ID de processo para o LOG
			Log.liberaIdProcesso(idProcesso, Definicoes.CN_CONSULTA,Definicoes.PROCESSO_SUCESSO);			
		}
		return retorno;
	}
	
	/**
	 * Metodo....: publicarBS
	 * Descricao.: Realiza a publicacao do BS
	 * 
	 * @param  numeroBS	- Numero do BS a ser publicado
	 * @param  numeroIP - IP da maquina em que o BS foi aberto
	 * @return String	- XML contendo o resultado da publicacao
	 * 
	 */
	public String publicarBS(String numeroBS, String numeroIP, String numeroAssinante, String matriculaOperador)
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		PublicadorBS publicador = new PublicadorBS(idProcesso);
		
		return publicador.publicarBS(numeroBS, numeroIP, numeroAssinante, matriculaOperador);
	}
	
	/**
	 * Metodo....: consultarStatusBS
	 * Descricao.: Realiza a consulta de status de BS
	 * 
	 * @param  xmlConsulta	- XML contendo os dados da consulta
	 * @return String		- XML de retorno
	 * 
	 */
	public String consultarStatusBS(String xmlConsulta)
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_CONSULTA);
		ConsultaStatusBS consultor = new ConsultaStatusBS(idProcesso);
		
		return consultor.consultarStatusBS(xmlConsulta);
	}
}