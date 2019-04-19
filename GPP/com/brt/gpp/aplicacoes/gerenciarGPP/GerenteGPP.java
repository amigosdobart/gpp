// Definicao do Pacote
package com.brt.gpp.aplicacoes.gerenciarGPP;

// Arquivo de Imports de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolSMPP;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerenteProcessamentoCDR;
import com.brt.gpp.gerentesPool.GerenteArquivosCDR;
import com.brt.gpp.aplicacoes.consultar.*;
import com.brt.gpp.aplicacoes.enviarSMS.EnvioSMSProdutor;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.*;
import com.brt.gpp.comum.mapeamentos.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.*;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Import Internos
import java.text.*;
import java.util.Collection;
import java.util.Iterator;

/**
  *
  * Este arquivo refere-se a classe GerenteGPP, responsavel pela implementacao da
  * logica de gerenciamento do GPP
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Camile Cardoso Couto
  * Data: 				13/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class GerenteGPP extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolTecnomen 		gerenteTecnomen 	= null; // Gerente de conexoes Tecnomen
	protected GerentePoolBancoDados		gerenteBancoDados 	= null; // Gerente de conexoes Banco Dados
	protected GerentePoolSMPP			gerenteSMPP		 	= null; // Gerente de conexoes SMPP
	protected ArquivoConfiguracaoGPP	arqConfiguracao		= null; // Referencia ao arquivo de configuraca
	protected EnvioSMSProdutor			produtorSMS			= null;
	/**
	 * Metodo...: GerenteGPP
	 * Descricao: Construtor
	 * @param	logId	- Identificador do Processo para Log
	 * @return
	 */
	 public GerenteGPP (long logId)
	 {
		super(logId, Definicoes.CL_GERENTE_GPP);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
	    this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);

		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		this.gerenteSMPP = GerentePoolSMPP.getInstancia(logId);

		// Carrega a instancia do arquivo de configuracao
		arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
	 }

	 /**
	  * Metodo...: consultaNumeroConexoes
	  * Descricao: Consulta numero de conexoes ativas com a plataforma Tecnomen e com o banco de dados do GPP
	  * @param	aTipoConexao		- Tipo de Conexao
	  * @return	int 				- Numero de Conexoes
	  * @throws GPPInternalErrorException
	  */
	 public int consultaNumeroConexoes (int aTipoConexao) throws GPPInternalErrorException
	 {
		super.log(Definicoes.DEBUG, "consultaNumeroConexoes", "Inicio da Consulta do Numero de Conexoes Ativas.");

	 	// Inicializa variaveis do metodo
		int retorno = 0;
//		long aIdProcesso = super.getIdLog();

		super.log(Definicoes.DEBUG, "consultaNumeroConexoes", "aTipoConexao: " + aTipoConexao);

		try
		{
			switch ( aTipoConexao )
			{
				case Definicoes.CO_TIPO_TECNOMEN_APR:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes Tecnomen Aprovisionamento...");
					retorno = this.gerenteTecnomen.getNumeroConexoesAprovisionamento();
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_REC:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes Tecnomen Recarga...");
					retorno = this.gerenteTecnomen.getNumeroConexoesRecarga();
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_VOU:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes Tecnomen Voucher...");
					retorno = this.gerenteTecnomen.getNumeroConexoesVoucher();
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_ADM:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes Tecnomen Admin...");
					retorno = this.gerenteTecnomen.getNumeroConexoesAdmin();
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_AGE:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes Tecnomen Agent...");
					retorno = this.gerenteTecnomen.getNumeroConexoesAgent();
					break;
				}
				case Definicoes.CO_TIPO_BANCO_DADOS_PREP:
				{
					super.log(Definicoes.INFO, "consultaNumeroConexoes", "Retornando numero de conexoes PREPConexao...");
					retorno = this.gerenteBancoDados.getNumeroConexoesBancoDados();
					break;
				}
			 }
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "consultaNumeroConexoes", "Excecao ocorrida: "+ e1);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);
		}
		finally
		{
		 	super.log(Definicoes.DEBUG, "consultaNumeroConexoes", "retorno = " + retorno);

			super.log(Definicoes.INFO, "consultaNumeroConexoes", "Fim da Consulta de Numero de Conexoes Ativas.");
		 }
		return retorno;
	 }

	/**
	 * Metodo...: criaConexoes
	 * Descricao: Cria conexao com a plataforma Tecnomen e com o banco de dados do GPP
	 * @param	aTipoConexao		- Tipo de Conexao
	 * @return	boolean 			- True se criou e False se nao criou
	 * @throws GPPInternalErrorException
	 */
	public boolean criaConexoes (int aTipoConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "criaConexoes", "Inicio da Criacao de Conexoes.");

	   	// Inicializa variaveis do metodo
	   	boolean retorno = false;
	   	long aIdProcesso = super.getIdLog();

	   	super.log(Definicoes.DEBUG, "criaConexoes", "aTipoConexao: " + aTipoConexao);

	   	try
	   	{
	   		switch ( aTipoConexao )
		   	{
				case Definicoes.CO_TIPO_TECNOMEN_APR:
			   	{
					super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao Tecnomen Aprovisionamento...");
				   	if (this.gerenteTecnomen.criaTecnomenAprovisionamento())
				   		retorno = true;
				   	break;
			   	}
			   	case Definicoes.CO_TIPO_TECNOMEN_REC:
			   	{
			   		super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao Tecnomen Recarga...");
				   	if (this.gerenteTecnomen.criaTecnomenRecarga())
				   		retorno = true;
				   	break;
			   	}
			   	case Definicoes.CO_TIPO_TECNOMEN_VOU:
			   	{
					super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao Tecnomen Voucher...");
					if (this.gerenteTecnomen.criaTecnomenVoucher())
						retorno = true;
					break;
				}
			   	case Definicoes.CO_TIPO_TECNOMEN_ADM:
			   	{
					super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao Tecnomen Admin...");
					if (this.gerenteTecnomen.criaTecnomenAdmin())
						retorno = true;
					break;
			   	}
			   	case Definicoes.CO_TIPO_TECNOMEN_AGE:
			   	{
					super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao Tecnomen Agent...");
					if (this.gerenteTecnomen.criaTecnomenAgent())
						retorno = true;
					break;
			   	}
			   	case Definicoes.CO_TIPO_BANCO_DADOS_PREP:
			   	{
					super.log(Definicoes.INFO, "criaConexoes", "Retornando criacao de conexao PREP...");
					if (this.gerenteBancoDados.criaConexaoBancoDados(aIdProcesso))
						retorno = true;
					break;
			   	}
			}
	   	}
	   	catch (Exception e1)
	   	{
			super.log(Definicoes.ERRO, "criaConexoes", "Excecao ocorrida: "+ e1);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);
	   	}
	   	finally
	   	{
			super.log(Definicoes.DEBUG, "criaConexoes", "retorno = " + retorno);

		   	super.log(Definicoes.INFO, "criaConexoes", "Fim da Criacao de Conexoes.");
		}
		return retorno;
	}
	/**
	 * Metodo...: removeConexoes
	 * Descricao: Deleta conexao com a plataforma Tecnomen e com o banco de dados do GPP
	 * @param	aTipoConexao		- Tipo de Conexao
	 * @return	boolean 			- True se deletou e False se nao deletou
	 * @throws GPPInternalErrorException
	 */
	public boolean removeConexoes (int aTipoConexao) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "removeConexoes", "Inicio da Remocao de Conexoes.");

		// Inicializa variaveis do metodo
		boolean retorno = false;
		long aIdProcesso = super.getIdLog();

		super.log(Definicoes.DEBUG, "removeConexoes", "aTipoConexao: " + aTipoConexao);

		try
		{
			switch ( aTipoConexao )
			{
				case Definicoes.CO_TIPO_TECNOMEN_APR:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao Tecnomen Aprovisionamento...");
					if (this.gerenteTecnomen.destroiConexaoTecnomenAprovisionamento(super.logId))
						retorno = true;
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_REC:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao Tecnomen Recarga...");
					if (this.gerenteTecnomen.destroiConexaoTecnomenRecarga(super.logId))
						retorno = true;
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_VOU:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao Tecnomen Voucher...");
					if (this.gerenteTecnomen.destroiConexaoTecnomenVoucher(super.logId))
						retorno = true;
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_ADM:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao Tecnomen Admin...");
					if (this.gerenteTecnomen.destroiConexaoTecnomenAdmin(super.logId))
						retorno = true;
					break;
				}
				case Definicoes.CO_TIPO_TECNOMEN_AGE:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao Tecnomen Agent...");
					if (this.gerenteTecnomen.destroiConexaoTecnomenAgent(super.logId))
						retorno = true;
					break;
				}
				case Definicoes.CO_TIPO_BANCO_DADOS_PREP:
				{
					super.log(Definicoes.INFO, "removeConexoes", "Retornando remocao de conexao PREP...");
					if (this.gerenteBancoDados.destroiConexaoPREP(aIdProcesso))
						retorno = true;
					break;
				}
			}
		}
		catch (Exception e1)
		{
			super.log(Definicoes.ERRO, "removeConexoes", "Excecao ocorrida: "+ e1);
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);
		}
		finally
		{
			super.log(Definicoes.DEBUG, "removeConexoes", "retorno = " + retorno);

			super.log(Definicoes.INFO, "removeConexoes", "Fim da Remocao de Conexoes.");
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapPlanoPreco
	 * Descricao: Exibe os planos de preco mapeados em memoria
	 * @param
	 * @return	String	- A lista de planos de preco
	 * @throws
	 */
	public String exibeMapPlanoPreco()
	{
		MapPlanoPreco mapPlanoPreco = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapPlanoPreco = MapPlanoPreco.getInstancia();
			if (mapPlanoPreco == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapPlanoPreco", "Problemas com Mapeamento dos Planos de Preço");
			else
				retorno = mapPlanoPreco.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapPlanoPreco","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaMapeamentoPlanoPreco
	 * Descricao: Atualiza os planos de preco mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaPlanoPreco() throws GPPInternalErrorException
	{
		MapPlanoPreco mapPlanoPreco = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapPlanoPreco = MapPlanoPreco.getInstancia();
			if (mapPlanoPreco == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaPlanoPreco", "Problemas com Mapeamento dos Planos de Preço");
			else
			{
			    mapPlanoPreco.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaPlanoPreco","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapStatusAssinante
	 * Descricao: Exibe os status de assinante mapeados em memoria
	 * @param
	 * @return	String	- A lista de status de assinante
	 * @throws
	 */
	public String exibeMapStatusAssinante()
	{
		MapStatusAssinante mapStatusAssinante = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapStatusAssinante = MapStatusAssinante.getInstancia();
			if (mapStatusAssinante == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapStatusAssinante", "Problemas com Mapeamento dos Status de Assinante");
			else
				retorno = mapStatusAssinante.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapStatusAssinante","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaStatusAssinante
	 * Descricao: Atualiza os status de assinante mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaStatusAssinante() throws GPPInternalErrorException
	{
		MapStatusAssinante mapStatusAssinante = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapStatusAssinante = MapStatusAssinante.getInstancia();
			if (mapStatusAssinante == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaStatusAssinante", "Problemas com Mapeamento dos Status de Assinante");
			else
			{
				mapStatusAssinante.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaStatusAssinante","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapStatusServico
	 * Descricao: Exibe os status de servicos mapeados em memoria
	 * @param
	 * @return	String	- A lista de status de servico
	 * @throws
	 */
	public String exibeMapStatusServico()
	{
		MapStatusServico mapStatusServico = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapStatusServico = MapStatusServico.getInstancia();
			if (mapStatusServico == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapStatusServico", "Problemas com Mapeamento dos Status de Servico");
			else
				retorno = mapStatusServico.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapStatusServico","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaStatusServico
	 * Descricao: Atualiza os status de servico mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaStatusServico() throws GPPInternalErrorException
	{
		MapStatusServico mapStatusServico = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Planos de Preço em Memoria
			mapStatusServico = MapStatusServico.getInstancia();
			if (mapStatusServico == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaStatusServico", "Problemas com Mapeamento dos Status de Servico");
			else
			{
				mapStatusServico.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaStatusServico","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapSistemaOrigem
	 * Descricao: Exibe os sistemas de origem mapeados em memoria
	 * @param
	 * @return	String	- A lista de sistemas de origem
	 * @throws
	 */
	public String exibeMapSistemaOrigem()
	{
		MapSistemaOrigem mapSistemaOrigem = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Sistemas de Origem
			mapSistemaOrigem = MapSistemaOrigem.getInstancia();
			if (mapSistemaOrigem == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapSistemaOrigem", "Problemas com Mapeamento dos Sistemas de Origem");
			else
				retorno = mapSistemaOrigem.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapSistemaOrigem","Erro GPP: "+e);
		}
		return retorno;

	}

	/**
	 * Metodo...: atualizaListaSistemaOrigem
	 * Descricao: Atualiza os sistemas de origem mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaSistemaOrigem() throws GPPInternalErrorException
	{
		MapSistemaOrigem mapSistemaOrigem = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Sistemas de Origem
			mapSistemaOrigem = MapSistemaOrigem.getInstancia();
			if (mapSistemaOrigem == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaSistemaOrigem", "Problemas com Mapeamento dos Sistemas de Origem");
			else
			{
				mapSistemaOrigem.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaSistemaOrigem","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapTarifaTrocaMSISDN
	 * Descricao: Exibe as tarifas de troca de MSISDN mapeados em memoria
	 * @param
	 * @return	String	- A lista de tarifas de troca de MSISDN
	 * @throws
	 */
	public String exibeMapTarifaTrocaMSISDN()
	{
		MapTarifaTrocaMSISDN mapTarifaTrocaMSISDN = null;
		String retorno = null;
		try
		{
			//Mapeamento das Tarifas de Troca de MSISDN
			mapTarifaTrocaMSISDN = MapTarifaTrocaMSISDN.getInstancia();
			if (mapTarifaTrocaMSISDN == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapTarifaTrocaMSISDN", "Problemas com Mapeamento das Tarifas de Troca de MSISDN");
			else
				retorno = mapTarifaTrocaMSISDN.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapTarifaTrocaMSISDN","Erro GPP: "+e);
		}
		return retorno;

	}

	/**
	 * Metodo...: atualizaListaTarifaTrocaMSISDN
	 * Descricao: Atualiza as tarifas de troca de MSISDN mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaTarifaTrocaMSISDN() throws GPPInternalErrorException
	{
		MapTarifaTrocaMSISDN mapTarifaTrocaMSISDN = null;
		boolean retorno = false;
		try
		{
			//Mapeamento das Tarifas de Troca de MSISDN
			mapTarifaTrocaMSISDN = MapTarifaTrocaMSISDN.getInstancia();
			if (mapTarifaTrocaMSISDN == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaTarifaTrocaMSISDN", "Problemas com Mapeamento das Tarifas de Troca de MSISDN");
			else
			{
				mapTarifaTrocaMSISDN.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaTarifaTrocaMSISDN","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapConfiguracaoGPP
	 * Descricao: Exibe as configuracoes GPP mapeadas em memoria
	 * @param
	 * @return	String	- A lista de configuracoes GPP
	 * @throws
	 */
	public String exibeMapConfiguracaoGPP()
	{
		MapConfiguracaoGPP mapConfigGPP = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapConfigGPP = MapConfiguracaoGPP.getInstancia();
			if (mapConfigGPP == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapConfiguracaoGPP", "Problemas com Mapeamento das Configurações GPP");
			else
				retorno = mapConfigGPP.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapConfiguracaoGPP","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaConfiguracaoGPP
	 * Descricao: Atualiza as configuracoes GPP mapeadas em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaConfiguracaoGPP() throws GPPInternalErrorException
	{
		MapConfiguracaoGPP mapConfigGPP = null;
		boolean retorno = true;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapConfigGPP = MapConfiguracaoGPP.getInstancia();
			if (mapConfigGPP == null)
			{
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaConfiguracaoGPP", "Problemas com Mapeamento das Configurações GPP");
				retorno = false;
			}
			else
			{
				mapConfigGPP.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaConfiguracaoGPP","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapValoresRecarga
	 * Descricao: Exibe Valores de Recarga mapeadas em memoria
	 * @param
	 * @return	String		A lista de Valores de Recarga
	 * @throws
	 */
	public String exibeMapValoresRecarga()
	{
		MapValoresRecarga mapValoresRecarga = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapValoresRecarga = MapValoresRecarga.getInstancia();
			if (mapValoresRecarga == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapValoresRecarga", "Problemas com Mapeamento dos Valores de Recarga");
			else
				retorno = "Nao utilizado";
				//retorno = mapValoresRecarga.exibeMapeamentoValoresRecarga();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapValoresRecarga","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaValoresRecarga
	 * Descricao: Atualiza os Valores de Recarga mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaValoresRecarga() throws GPPInternalErrorException
	{
		MapValoresRecarga mapValoresRecarga = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapValoresRecarga = MapValoresRecarga.getInstancia();
			if (mapValoresRecarga == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaValoresRecarga", "Problemas com Mapeamento dos Valores de Recarga");
			else
				retorno = true;
				//mapValoresRecarga.atualizaMapeamentoValoresRecarga();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaValoresRecarga","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapValoresRecargaPlanoPreco
	 * Descricao: Exibe Valores de Recarga em funcao dos Planos de Preco mapeados em memoria
	 * @param
	 * @return	String		A lista de relacionamento entre valores de recarga e planos de preco
	 * @throws
	 */
	public String exibeMapValoresRecargaPlanoPreco()
	{
		MapValoresRecargaPlanoPreco mapVlrRecargaPlano = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapVlrRecargaPlano = MapValoresRecargaPlanoPreco.getInstancia();
			if (mapVlrRecargaPlano == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapValoresRecargaPlanoPreco",
						"Problemas com Mapeamento dos Valores de Recarga em funcao dos Planos de Preco");
			else
				retorno = mapVlrRecargaPlano.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapValoresRecargaPlanoPreco","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaValoresRecargaPlanoPreco
	 * Descricao: Atualiza os Valores de Recarga em funcao dos Planos de Preco mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaValoresRecargaPlanoPreco() throws GPPInternalErrorException
	{
		MapValoresRecargaPlanoPreco mapVlrRecargaPlano = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Valores de Recarga em Memoria
			mapVlrRecargaPlano = MapValoresRecargaPlanoPreco.getInstancia();
			if (mapVlrRecargaPlano == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaValoresRecargaPlanoPreco",
						"Problemas com Mapeamento dos Valores de Recarga em funcao dos Planos de Preco");
			else
			{
				mapVlrRecargaPlano.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaValoresRecargaPlanoPreco","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapBonusPulaPula
	 * Descricao: Exibe os valores de Bonus Pula-Pula mapeados em memoria
	 * @param
	 * @return	String		A lista de valores de Bonus Pula-Pula
	 * @throws
	 */
	public String exibeMapBonusPulaPula()
	{
		MapBonusPulaPula mapBonusPulaPula = null;
		String retorno = null;
		try
		{
			//Mapeamento dos valores de Bonus Pula-Pula em Memoria
			mapBonusPulaPula = MapBonusPulaPula.getInstancia();
			if (mapBonusPulaPula == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapBonusPulaPula",
						"Problemas com Mapeamento dos valores de Bonus Pula-Pula");
			else
				retorno = mapBonusPulaPula.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapBonusPulaPula","Erro GPP: " + e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaBonusPulaPula
	 * Descricao: Atualiza os valores de Bonus Pula-Pula mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaBonusPulaPula() throws GPPInternalErrorException
	{
		MapBonusPulaPula mapBonusPulaPula = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos valores de Bonus Pula-Pula em Memoria
			mapBonusPulaPula = MapBonusPulaPula.getInstancia();
			if (mapBonusPulaPula == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaBonusPulaPula",
						"Problemas com Mapeamento dos valores de Bonus Pula-Pula");
			else
			{
				mapBonusPulaPula.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaBonusPulaPula","Erro GPP: "+e);
		}

		return retorno;
	}

	/**
	 * Metodo...: exibeMapPromocao
	 * Descricao: Exibe as Promocoes mapeadas em memoria
	 * @param
	 * @return	String		A lista de Promocoes
	 * @throws
	 */
	public String exibeMapPromocao()
	{
		MapPromocao mapPromocao = null;
		String retorno = null;
		try
		{
			//Mapeamento das Promocoes em Memoria
			mapPromocao = MapPromocao.getInstancia();
			if (mapPromocao == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapPromocao",
						"Problemas com Mapeamento das Promocoes");
			else
				retorno = mapPromocao.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapPromocao","Erro GPP: " + e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaPromocao
	 * Descricao: Atualiza as Promocoes mapeadas em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaPromocao() throws GPPInternalErrorException
	{
		MapPromocao mapPromocao = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos valores de Bonus Pula-Pula em Memoria
			mapPromocao = MapPromocao.getInstancia();
			if (mapPromocao == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaPromocao",
						"Problemas com Mapeamento das Promocoes");
			else
			{
				mapPromocao.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaPromocao","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapPromocaoDiaExecucao
	 * Descricao: Exibe os dias de execucao das Promocoes mapeados em memoria
	 * @param
	 * @return	String		A lista de dias de execucao das Promocoes
	 * @throws
	 */
	public String exibeMapPromocaoDiaExecucao()
	{
		MapPromocaoDiaExecucao mapPromocaoDiaExecucao = null;
		String retorno = null;
		try
		{
			//Mapeamento das Promocoes em Memoria
			mapPromocaoDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
			if (mapPromocaoDiaExecucao == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapPromocaoDiaExecucao",
						"Problemas com Mapeamento dos dias de execucao das Promocoes");
			else
				retorno = mapPromocaoDiaExecucao.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapPromocaoDiaExecucao","Erro GPP: " + e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaPromocaoDiaExecucao
	 * Descricao: Atualiza dos dias de execucao das Promocoes mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaPromocaoDiaExecucao() throws GPPInternalErrorException
	{
		MapPromocaoDiaExecucao mapPromocaoDiaExecucao = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos valores de Bonus Pula-Pula em Memoria
			mapPromocaoDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
			if (mapPromocaoDiaExecucao == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaPromocaoDiaExecucao",
						"Problemas com Mapeamento dos dias de execucao das Promocoes");
			else
			{
				mapPromocaoDiaExecucao.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaPromocaoDiaExecucao","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMapRecOrigem
	 * Descricao: Exibe lista de TTs mapeados em memoria
	 * @param
	 * @return	String		A lista de TTs
	 * @throws
	 */
	public String exibeMapRecOrigem()
	{
		MapRecOrigem mapRecOrigem = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Transaction Types Memoria
			mapRecOrigem = MapRecOrigem.getInstancia();
			if (mapRecOrigem == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMapRecOrigem", "Problemas com Mapeamento dos TTs");
			else
				retorno = mapRecOrigem.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMapRecOrigem","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaRecOrigem
	 * Descricao: Atualiza os TTs mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaListaRecOrigem() throws GPPInternalErrorException
	{
		MapRecOrigem mapRecOrigem = null;
		boolean retorno = true;
		try
		{
			//Mapeamento dos Transaction Types Memoria
			mapRecOrigem = MapRecOrigem.getInstancia();
			if (mapRecOrigem == null)
			{
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaListaRecOrigem", "Problemas com Mapeamento dos TTs");
				retorno = false;
			}
			else
			{
				mapRecOrigem.refresh();
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaListaRecOrigem","Erro GPP: "+e);
			retorno = false;
		}
		return retorno;
	}

	/**
	 * Metodo...: exibeMotivosBloqueio
	 * Descricao: Exibe lista de Motivos de Bloqueio mapeados em memoria
	 * @param
	 * @return	String		A lista de Motivos de Bloqueio
	 * @throws
	 */
	public String exibeMotivosBloqueio()
	{
		MapMotivoBloqueioAssinante mapMotivoBloqueio = null;
		String retorno = null;
		try
		{
			//Mapeamento dos Transaction Types Memoria
			mapMotivoBloqueio = MapMotivoBloqueioAssinante.getInstancia();
			if (mapMotivoBloqueio == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "exibeMotivosBloqueio", "Problemas com Mapeamento dos Motivos de Bloqueio");
			else
				retorno = mapMotivoBloqueio.toString();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"exibeMotivosBloqueio","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaMotivosBloqueio
	 * Descricao: Atualiza os Motivos de Bloqueio mapeados em memoria
	 * @param
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaMotivosBloqueio() throws GPPInternalErrorException
	{
		MapMotivoBloqueioAssinante mapMotivoBloqueio = null;
		boolean retorno = false;
		try
		{
			//Mapeamento dos Motivos de Bloqueio em Memoria
			mapMotivoBloqueio = MapMotivoBloqueioAssinante.getInstancia();
			if (mapMotivoBloqueio == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaMotivosBloqueio", "Problemas com Mapeamento dos Motivos de Bloqueio");
			else
			{
				mapMotivoBloqueio.refresh();
				retorno = true;
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaMotivosBloqueio","Erro GPP: "+e);
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaModulacaoPlano
	 * Descricao: Atualiza as faixas de horario para modulacao x planos mapeados em memoria
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaModulacaoPlano() throws GPPInternalErrorException
	{
		boolean retorno = true;
		try
		{
			MapModulacaoPlano map = MapModulacaoPlano.getInstance();
			if (map == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaModulacaoPlano", "Problemas com Mapeamento de Modulacao de planos");
			else
				map.refresh();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaModulacaoPlano","Erro GPP: "+e);
			retorno = false;
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaAssinantesNaoBonificaveis
	 * Descricao: Atualiza os assinantes nao bonificaveis em memoria
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaAssinantesNaoBonificaveis() throws GPPInternalErrorException
	{
		boolean retorno = true;
		try
		{
			MapAssinantesNaoBonificaveis map = MapAssinantesNaoBonificaveis.getInstance(0);
			if (map == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaAssinantesNaoBonificaveis", "Problemas com Mapeamento de assinantes nao bonificaveis");
			else
				map.refresh();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaAssinantesNaoBonificaveis","Erro GPP: "+e);
			retorno = false;
		}
		return retorno;
	}

	/**
	 * Metodo...: atualizaFeriados
	 * Descricao: Atualiza os assinantes nao bonificaveis em memoria
	 * @return	boolean	- TRUE se atualizou a lista ou FALSE se nao atualizou
	 * @throws 	GPPInternalErrorException
	 */
	public boolean atualizaFeriados() throws GPPInternalErrorException
	{
		boolean retorno = true;
		try
		{
			MapFeriados map = MapFeriados.getInstance();
			if (map == null)
				log.log(logId, Definicoes.WARN, nomeClasse, "atualizaFeriados", "Problemas com Mapeamento de feriados");
			else
				map.refresh();
		}
		catch (Exception e)
		{
			super.log(Definicoes.DEBUG,"atualizaFeriados","Erro GPP: "+e);
			retorno = false;
		}
		return retorno;
	}

	/**
	 * Metodo...: ativaDesativaEnvioSMS
	 * Descricao: Ativa ou Desativa o Processo de Envio de SMS
	 * @param 	boolean 			- TRUE para ativar e FALSE para desativar
	 * @param 	long aIdProcesso 	- Identificacao do processo no Log
	 * @return	boolean				- TRUE se ativou/desativou ou FALSE se nao conseguiu atualizar
	 * @throws
	 */
	public boolean ativaDesativaEnvioSMS(boolean aDeveProcessar, long aIdProcesso)
	{
		return true;
	}

	/**
	 * Metodo...: ativaDesativaDebug
	 * Descricao: Ativa ou Desativa as mensagens de DEBUG no Log
	 * @param 	boolean 			- TRUE para ativar e FALSE para desativar
	 * @return	boolean				- TRUE se ativou/desativou ou FALSE se nao conseguiu atualizar
	 * @throws
	 */
	public boolean ativaDesativaDebug(boolean aFlagDebug)
	{
		ArquivoConfiguracaoGPP arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
		arqConfiguracao.setAtivaDebug(aFlagDebug);
		arqConfiguracao.setSaidaDebug(aFlagDebug);
		return true;
	}

	/**
	 * Metodo...: pingGPP
	 * Descricao: Verifica se o GPP esta no ar
	 * @param
	 * @return	String	- Parametros de Execucao do GPP
	 * @throws
	 */
	public String pingGPP ( )
	{
		String retorno = null;

		retorno = "GPP rodando na porta: " + arqConfiguracao.getPortaOrbGPP() + " e host: " + arqConfiguracao.getEnderecoOrbGPP() + " esta vivo... \n";
		//retorno += " \n";
		//retorno += "Tecnomen Aprovisionamento: " + this.gerenteTecnomen.getNumeroConexoesAprovisionamento() + " conexao(oes) \n";
		//retorno += "Tecnomen Recarga: " + this.gerenteTecnomen.getNumeroConexoesRecarga() + " conexao(oes) \n";
		//retorno += "Tecnomen Voucher: " + this.gerenteTecnomen.getNumeroConexoesVoucher() + " conexao(oes) \n";
		//retorno += "Tecnomen Admin: " + this.gerenteTecnomen.getNumeroConexoesAdmin() + " conexao(oes) \n";
		//retorno += "Tecnomen Agent: " + this.gerenteTecnomen.getNumeroConexoesAgent() + " conexao(oes) \n";
		//retorno += "PREP: " + this.gerenteBancoDados.getNumeroConexoesBancoDados() + " conexao(oes) \n";

		return retorno;
	}

	/**
	 * Metodo...: paraGPP
	 * Descricao: Para o processo do GPP
	 * @param
	 * @return
	 * @throws
	 */
	public void paraGPP ( )
	{
		this.gerenteTecnomen.destroiPool(super.getIdLog());
		this.gerenteBancoDados.destroiPool(super.logId);
		this.gerenteSMPP.destruirPool();
	}

	/**
	 * Metodo...: getStatusProdutorSMS
	 * Descricao: Busca o status do processo de envio de SMS
	 * @param
	 * @return	boolean	- TRUE se status com sucesso ou FALSE se nao conseguiu
	 * @throws
	 */
	public boolean getStatusProdutorSMS ( )
	{
		if(produtorSMS != null)
			return produtorSMS.isDeveConsumirSMS();

		return false;
	}

	/**
	 * Metodo...: getStatusEscreveDebug
	 * Descricao: Busca o status do processo para escrita do debug no arquivo de log
	 * @param
	 * @return	 boolean - TRUE se status com sucesso ou FALSE se nao conseguiu
	 * @throws
	 */
	public boolean getStatusEscreveDebug ( )
	{
		ArquivoConfiguracaoGPP arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
		return arqConfiguracao.getAtivaDebug();
	}

	/**
	 * Metodo...: getHistProcessosBatch
	 * Descricao: Este metodo tem como objetivo retornar uma lista de valores contendo
	 * 			  informacoes sobre o historico de processo batch
	 * @param aIdProcBatch	- Id do processo batch
	 * @param aDatIni 		- Data Inicial de execucao do processo batch
	 * @param aDatFim 		- Data Final de execucao do processo batch
	 * @param aIdtStatus	- Status da execucao do processo batch
	 * @return String 		- Lista do historico de execucao
	 * @throws GPPInternalErrorException
	 */
	public String getHistProcessosBatch(short aIdProcBatch, String aDatIni, String aDatFim, String aIdtStatus) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "getHistProcessosBatch", "Inicio da busca do historico de processos bacth.");

		ConsultaHistoricoProcessoBatch consHis = new ConsultaHistoricoProcessoBatch(super.getIdLog());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try
		{
			consHis.setIdProcessoBatch(aIdProcBatch);
			if (aDatIni != null && !aDatIni.equals(""))
				consHis.setDataInicial(dateFormat.parse(aDatIni));

			if (aDatFim != null && !aDatFim.equals(""))
				consHis.setDataFinal(dateFormat.parse(aDatFim));

			if (aIdtStatus != null && !aIdtStatus.equals(""))
				consHis.setStatus(aIdtStatus);
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Erro ao executar o parse da string de Data");
		}
		super.log(Definicoes.DEBUG, "getHistProcessosBatch", "Fim da busca do historico de processos bacth.");
		return consHis.executaConsulta();
	}

	/**
	 * Metodo...: consultaProcessosBatch
	 * Descricao: Este metodo retorna uma lista com o nome dos processos batches existentes no GPP
	 * @param
	 * @return 	String 		- Lista dos processos batch
	 * @throws 	GPPInternalErrorException
	 */
	public String consultaProcessosBatch() throws GPPInternalErrorException
	{
		return ConsultaHistoricoProcessoBatch.consultaProcessosBatch(super.getIdLog());
	}

	/**
	 * Metodo...: consultaNumeroConexoesDisponiveis
	 * Descricao: Consulta numero de conexoes disponiveis com a plataforma Tecnomen e com o banco de dados do GPP
	 * @param	aTipoConexao		- Tipo de Conexao
	 * @return	int 				- Numero de Conexoes Disponiveis
	 * @throws GPPInternalErrorException
	 */
	public int consultaNumeroConexoesDisponiveis (int aTipoConexao) throws GPPInternalErrorException
	{
	   super.log(Definicoes.DEBUG, "consultaNumeroConexoesDisponiveis", "Inicio da Consulta do Numero de Conexoes Disponiveis.");

	   // Inicializa variaveis do metodo
	   int retorno = 0;
//	   long aIdProcesso = super.getIdLog();

	   super.log(Definicoes.DEBUG, "consultaNumeroConexoesDisponiveis", "aTipoConexao: " + aTipoConexao);

	   try
	   {
		   switch ( aTipoConexao )
		   {
			   case Definicoes.CO_TIPO_TECNOMEN_APR:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na Tecnomen Aprovisionamento...");
				   retorno = this.gerenteTecnomen.getNumeroConexoesAprovisionamentoDisponiveis();
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_REC:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na Tecnomen Recarga...");
				   retorno = this.gerenteTecnomen.getNumeroConexoesRecargaDisponiveis();
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_VOU:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na Tecnomen Voucher...");
				   retorno = this.gerenteTecnomen.getNumeroConexoesVoucherDisponiveis();
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_ADM:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na Tecnomen Admin...");
				   retorno = this.gerenteTecnomen.getNumeroConexoesAdminDisponiveis();
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_AGE:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na Tecnomen Agent...");
				   retorno = this.gerenteTecnomen.getNumeroConexoesAgentDisponiveis();
				   break;
			   }
			   case Definicoes.CO_TIPO_BANCO_DADOS_PREP:
			   {
				   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Retornando numero de conexoes disponiveis na PREPConexao...");
				   retorno = this.gerenteBancoDados.getNumeroConexoesBancoDadosDisponiveis();
				   break;
			   }
			}
	   }
	   catch (Exception e1)
	   {
		   super.log(Definicoes.ERRO, "consultaNumeroConexoesDisponiveis", "Excecao ocorrida: "+ e1);
		   throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);
	   }
	   finally
	   {
		   super.log(Definicoes.DEBUG, "consultaNumeroConexoesDisponiveis", "retorno = " + retorno);
		   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Fim da Consulta de Numero de Conexoes Disponiveis.");
		}
		return retorno;
	}

	/**
	 * Metodo...: getListaProcessosComConexoesEmUso
	 * Descricao: Lista os processos (id e data de uso) que esta utilizando conexoes
	 * @param	aTipoConexao		- Tipo de Conexao
	 * @return	IdProcessoconexao[]	- Array com os valores de ids de processos que estao utilizando conexoes
	 * @throws 	GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosComConexoesEmUso (int aTipoConexao) throws GPPInternalErrorException
	{
	   super.log(Definicoes.DEBUG, "getListaProcessosComConexoesEmUso", "Inicio da Listagem de processos que possuem conexoes em uso.");

	   // Inicializa variaveis do metodo
	   IdProcessoConexao[] retorno = null;
	   long aIdProcesso = super.getIdLog();

	   super.log(Definicoes.DEBUG, "getListaProcessosComConexoesEmUso", "aTipoConexao: " + aTipoConexao);
	   try
	   {
		   switch ( aTipoConexao )
		   {
			   case Definicoes.CO_TIPO_TECNOMEN_APR:
			   {
				   retorno = this.gerenteTecnomen.getListaProcessosAprovisionamento(aIdProcesso);
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_REC:
			   {
					retorno = this.gerenteTecnomen.getListaProcessosRecarga(aIdProcesso);
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_VOU:
			   {
				   retorno = this.gerenteTecnomen.getListaProcessosVoucher(aIdProcesso);
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_ADM:
			   {
				   retorno = this.gerenteTecnomen.getListaProcessosAdmin(aIdProcesso);
				   break;
			   }
			   case Definicoes.CO_TIPO_TECNOMEN_AGE:
			   {
				   retorno = this.gerenteTecnomen.getListaProcessosAgent(aIdProcesso);
				   break;
			   }
			   case Definicoes.CO_TIPO_BANCO_DADOS_PREP:
			   {
				   retorno = this.gerenteBancoDados.getListaProcessos(aIdProcesso);
				   break;
			   }
			}
	   }
	   catch (Exception e1)
	   {
		   super.log(Definicoes.ERRO, "consultaNumeroConexoesDisponiveis", "Excecao ocorrida: "+ e1);
		   throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e1);
	   }
	   finally
	   {
		   super.log(Definicoes.DEBUG, "consultaNumeroConexoesDisponiveis", "retorno = " + retorno);
		   super.log(Definicoes.INFO, "consultaNumeroConexoesDisponiveis", "Fim da Consulta de Numero de Conexoes Disponiveis.");
		}
		return retorno;
	}

	/**
	 *	Retorna um lista de numero de Statements abertos por conexao com o banco de dados.
	 *
	 *	@return		Lista de numero de Statements abertos por conexao com o banco de dados.
	 */
	public String getListaNumStatementsPorConexao()
	{
		StringBuffer result = new StringBuffer();
		Collection dados = super.gerenteBancoDados.getDadosConexoes();

		for(Iterator iterator = dados.iterator(); iterator.hasNext();)
			result.append(iterator.next().toString() + "\n");

		return result.toString();
	}

	/**
	 * Metodo....:adicionaThreadEnvioSMS
	 * Descricao.:Adiciona threads de envio de SMS dentro do pool controlado pelo ConsumidorSMS
	 * @return boolean - Indica se conseguiu adicionar ou nao a nova thread de envio de SMS
	 * @throws GPPInternalErrorException
	 */
	public boolean adicionaThreadEnvioSMS() throws GPPInternalErrorException
	{
		return false;
	}

	/**
	 * Metodo...:removeThreadEnvioSMS
	 * Descricao:Remove thread de envio de SMS no pool controlado pelo ConsumidorSMS
	 * @return boolean - Indica se conseguiu remover ou nao a thread de envio de SMS
	 * @throws GPPInternalErrorException
	 */
	public boolean removeThreadEnvioSMS() throws GPPInternalErrorException
	{
		return false;
	}

	/**
	 * Metodo...:finalizaPoolEnvioSMS
	 * Descricao:Destroi todas as threads de envio de SMS
	 *
	 */
	public void finalizaPoolEnvioSMS()
	{
		produtorSMS = EnvioSMSProdutor.getInstancia(log.getIdProcesso("EnvioSMSProdutor"));

		produtorSMS.pararConsumoSMS();
	}

	/**
	 * Metodo...:inicializaPoolEnvioSMS
	 * Descricao:Inicializa o pool de threads de envio de SMS
	 *
	 */
	public void inicializaPoolEnvioSMS()
	{
		produtorSMS = EnvioSMSProdutor.getInstancia(log.getIdProcesso("EnvioSMSProdutor"));

		produtorSMS.iniciarConsumoSMS();
	}

	/**
	 * Metodo....:getNumeroThreadsEnvioSMS
	 * Descricao.:Metodo que retorna o numero de threads atualmente ativas
	 *            no pool do ConsumidorSMS para envio de SMS
	 * @return in - Numero de threads ativas de envio de SMS
	 */
	public short getNumeroThreadsEnvioSMS()
	{
		if(produtorSMS != null)
			return (short)produtorSMS.getNumThreadsEnvioSMS();

		return 0;
	}

	/**
	 * Metodo....:getNumThreadsImpCDRDadosVoz
	 * Descricao.:Retorna o numero de threads ativas na importacao de CDRs
	 * @return int - Numero de threads ativas na importacao de CDRs
	 */
	public int getNumThreadsImpCDRDadosVoz()
	{
		long aIdProcesso = super.getIdLog();
		int numThreads = 0;
		try
		{
			GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
			numThreads = ger.getNumThreadsAtivasDadosVoz();
		}
		catch(Exception e)
		{
			numThreads = 0;
		}
		return numThreads;
	}

	/**
	 * Metodo....:getNumThreadsImpCDREvtRec
	 * Descricao.:Retorna o numero de threads ativas na importacao de CDRs
	 * @return int - Numero de threads ativas na importacao de CDRs
	 */
	public int getNumThreadsImpCDREvtRec()
	{
		long aIdProcesso = super.getIdLog();
		int numThreads = 0;
		try
		{
			GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
			numThreads = ger.getNumThreadsAtivasEvtRec();
		}
		catch(Exception e)
		{
			numThreads = 0;
		}
		return numThreads;
	}

	/**
	 * Metodo....:getNumArqPendentesDadosVoz
	 * Descricao.:Retorna o numero de arquivos de CDR ainda a serem importados
	 * @return int - Numero de arquivos pendentes
	 */
	public int getNumArqPendentesDadosVoz()
	{
		long aIdProcesso = super.getIdLog();
		int numArquivos = 0;
		try
		{
			GerenteArquivosCDR ger = GerenteArquivosCDR.getInstance(aIdProcesso);
			numArquivos = ger.getNumArquivosPendentesDadosVoz();
		}
		catch(Exception e)
		{
			numArquivos = 0;
		}
		return numArquivos;
	}

	/**
	 * Metodo....:getNumArqPendentesEvtRec
	 * Descricao.:Retorna o numero de arquivos de CDR ainda a serem importados
	 * @return int - Numero de arquivos pendentes
	 */
	public int getNumArqPendentesEvtRec()
	{
		long aIdProcesso = super.getIdLog();
		int numArquivos = 0;
		try
		{
			GerenteArquivosCDR ger = GerenteArquivosCDR.getInstance(aIdProcesso);
			numArquivos = ger.getNumArquivosPendentesEvtRec();
		}
		catch(Exception e)
		{
			numArquivos = 0;
		}
		return numArquivos;
	}

	/**
	 * Metodo....:removeThreadsDadosVoz
	 * Descricao.:Remove as threads de importacao de arquivos de dados e voz
	 * @throws GPPInternalErrorException
	 */
	public void removeThreadsDadosVoz() throws GPPInternalErrorException
	{
		long aIdProcesso = super.getIdLog();
		GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
		ger.removeThreadsDadosVoz();
	}

	/**
	 * Metodo....:removeThreadsEvtRec
	 * Descricao.:Remove as threads de importacao de arquivos de eventos e recargas
	 * @throws GPPInternalErrorException
	 */
	public void removeThreadsEvtRec() throws GPPInternalErrorException
	{
		long aIdProcesso = super.getIdLog();
		GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
		ger.removeThreadsEvtRec();
	}

	/**
	 * Metodo....:inicializaThreadsDadosVoz
	 * Descricao.:Inicializa o pool de processamento de arquivos de CDR para dados e voz
	 * @throws GPPInternalErrorException
	 */
	public void inicializaThreadsDadosVoz() throws GPPInternalErrorException
	{
		long aIdProcesso = super.getIdLog();
		MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
		int numThreadsDadosVoz 	= Integer.parseInt(confGPP.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NUM_THREADS_CDR_DADOSVOZ));
		// Inicializa novamente as threads
		GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
		ger.inicializaPoolDadosVoz(numThreadsDadosVoz);
	}

	/**
	 * Metodo....:inicializaThreadsEvtRec
	 * Descricao.:Inicializa o pool de processamento de arquivos de CDR para dados e voz
	 * @throws GPPInternalErrorException
	 */
	public void inicializaThreadsEvtRec() throws GPPInternalErrorException
	{
		long aIdProcesso = super.getIdLog();
		MapConfiguracaoGPP confGPP = MapConfiguracaoGPP.getInstancia();
		int numThreadsAprRec = Integer.parseInt(confGPP.getMapValorConfiguracaoGPP(Definicoes.IMPCDR_NUM_THREADS_CDR_APRREC));
		// Inicializa novamente as threads
		GerenteProcessamentoCDR ger = GerenteProcessamentoCDR.getInstance(aIdProcesso);
		ger.inicializaPoolAprRec(numThreadsAprRec);
	}

	/**
	 * Metodo....:atualizaMapeamentos
	 * Descricao.:Atualiza todos os mapeamentos em memoria
	 * @param	boolean		limpar		Flag indicando se o mapeamento deve ser limpo da memoria antes da atualizacao.
	 * @return	short					Codigo de retorno da operacao.
	 */
	public short atualizaMapeamentos(boolean limpar)
	{
	    return Mapeamentos.getInstance().atualizarMapeamentos(limpar);
	}

	/**
	 * Metodo....:atualizaMapeamento
	 * Descricao.:Atualiza o mapeamento em memoria informado pelo nome da classe
	 * @param	String		nome		Nome da classe
	 * @param	boolean		limpar		Flag indicando se o mapeamento deve ser limpo da memoria antes da atualizacao.
	 * @return	short					Codigo de retorno da operacao.
	 */
	public short atualizaMapeamento(String nome, boolean limpar)
	{
	    return Mapeamentos.getInstance().atualizarMapeamento(nome, limpar);
	}

	/**
	 * Metodo....:exibeMapeamento
	 * Descricao.:Exibe o mapeamento em memoria informado pelo nome da classe
	 * @param	String	nome	Nome da classe
	 */
	public String exibeMapeamento(String nome)
	{
	    return Mapeamentos.getInstance().exibirMapeamento(nome);
	}

	/**
	 * Metodo....:liberarConexoesEmUso
	 * Descricao.:Metodo que força a liberação de conexoes de BD e da Tecnomen usadas por um processo
	 * @param idProcesso Identificador do processo que terá suas conexoes liberadas
	 */
	public void liberarConexoesEmUso(long idProcesso)
	{
		gerenteBancoDados.liberarConexaoEmUso(idProcesso);
		gerenteTecnomen.liberarConexoesEmUso(idProcesso);
	}
}