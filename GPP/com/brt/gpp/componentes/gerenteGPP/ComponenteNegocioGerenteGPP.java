// Definicao do Pacote
package com.brt.gpp.componentes.gerenteGPP;

// Classes de POA do Gerente GPP
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPOA;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.*;

import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import com.brt.gpp.aplicacoes.gerenciarGPP.*;
import com.brt.gpp.aplicacoes.importacaoCDR.GerenciadorCacheFF;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;

/**
  *
  * Este arquivo contem a definicao da classe componente de negocio de GerenteGPP. Ela 
  * e responsavel pela logica de negocio para o gerenciamento do GPP. 
  *
  * @autor:				Camile Cardoso Couto
  * @Data:				12/04/2004
  *
  * Modificado Por:
  * Data:
  * Razao:
  */
public class ComponenteNegocioGerenteGPP extends gerenteGPPPOA
{
	// Variaveis Membro
	protected GerentePoolTecnomen 		gerenteTecnomen 	= null; // Gerente de conexoes Tecnomen
	protected GerentePoolLog			Log					= null; // Gerente de LOG
	protected ArquivoConfiguracaoGPP	arqConfiguracao		= null; // Referencia ao arquivo de configuracao
	    
	/**
	 * Metodo...: ComponenteNegocioGerenteGPP
	 * Descricao: Construtor 
	 * @param	
	 * @return									
	 */
	public ComponenteNegocioGerenteGPP ( )
	{
		// Obtem referencia ao gerente de LOG
		this.Log = GerentePoolLog.getInstancia(ComponenteNegocioGerenteGPP.class);
		
		Log.logComponente (Definicoes.INFO, Definicoes.CN_GERENTEGPP, ": Componente de Negocio ativado..." );
	}
	
	/**
	 * Metodo...: getNumerodeConexoes
	 * Descricao: Verifica o numero de conexoes ativas
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	short 			- Numero de conexoes ativas
	 * @throws  GPPInternalErrorException
	 */
	public short getNumerodeConexoes ( short tipoConexao ) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		int retNumerodeConexoes = 0;
		long idProcesso = 0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "Inicio TIPOCONEXAO "+tipoConexao);
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retNumerodeConexoes = gerenteGPP.consultaNumeroConexoes(tipoConexao);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "Excecao ocorrida: "+ e);				

			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "Fim retNumerodeConexoes = " + retNumerodeConexoes);
		}	
		return (short)retNumerodeConexoes;
	}
	
	/**
	 * Metodo...: criaConexao
	 * Descricao: Cria uma conexao do tipo passado como parametro
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public boolean criaConexao ( short tipoConexao ) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		boolean retorno = false;
		long idProcesso = 0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "criaConexao", "Inicio TIPOCONEXAO "+tipoConexao);
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.criaConexoes(tipoConexao);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_GERENTEGPP, "criaConexao", "Excecao ocorrida: "+ e);				

			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "criaConexao", "Fim RETORNO "+retorno);
		return retorno;
	}
	
	/**
	 * Metodo...: removeConexao
	 * Descricao: Remove uma conexao do tipo passado como parametro
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public boolean removeConexao ( short tipoConexao ) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		boolean retorno = false;
		long idProcesso = 0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "removeConexao", "Inicio TIPOCONEXAO "+tipoConexao);
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.removeConexoes(tipoConexao);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_GERENTEGPP, "removeConexao", "Excecao ocorrida: "+ e);				

			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "removeConexao", "Fim RETORNO "+retorno);
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaPlanoPreco
	 * Descricao: Exibe a lista de planos de precos mapeados em memoria no GPP
	 * @param 	
	 * @return 	string	- lista de planos de precos mapeados em memoria
	 * @throws  
	 */
	public String exibeListaPlanoPreco() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPlanoPreco", "Inicio");

		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

		lista = gerenteGPP.exibeMapPlanoPreco();

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPlanoPreco", "Fim");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaPlanoPreco
	 * Descricao: Atualiza a lista de planos de preco em memoria no GPP
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaPlanoPreco() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPlanoPreco", "Inicio");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaPlanoPreco();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaPlanoPreco", "Nao foi possivel atualizar o mapeamento de plano de preco em memoria. Excecao Interna do GPP ocorrida: " + e);
			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de plano de preco em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPlanoPreco", "Fim");
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaStatusAssinante
	 * Descricao: Exibe a lista de status de assinantes mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de status de assinantes
	 * @throws  
	 */
	public String exibeListaStatusAssinante() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaStatusAssinante", "Inicio");

		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

		lista = gerenteGPP.exibeMapStatusAssinante();

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaStatusAssinante", "Fim");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaStatusAssinante
	 * Descricao: Atualiza a lista de status de assinantes em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaStatusAssinante() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusAssinante", "Inicio");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaStatusAssinante();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusAssinante", "Nao foi possivel atualizar o mapeamento de status de assinantes em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de status de assinantes em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusAssinante", "Fim RETORNO "+retorno);
		}		
		return retorno;
	}

	/**
	 * Metodo...: exibeListaStatusServico
	 * Descricao: Exibe a lista de status de servicos mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de status de servicos mapeados em memoria
	 * @throws  
	 */
	public String exibeListaStatusServico() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaStatusServico", "Inicio do metodo que exibe lista de status de servicos em memoria.");

		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

		lista = gerenteGPP.exibeMapStatusServico();

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaStatusServico", "Fim do metodo que exibe lista de status de servicos em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaStatusServico
	 * Descricao: Atualiza a lista de status de servicos em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaStatusServico() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusServico", "Inicio da atualizacao da lista de status de servicos em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaStatusServico();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusServico", "Nao foi possivel atualizar o mapeamento de status de servicos em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de status de servicos em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaStatusServico", "Fim da atualizacao da lista de status de servicos em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaSistemaOrigem
	 * Descricao: Exibe a lista de sistemas de origem mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de sistemas de origem mapeados em memoria
	 * @throws  
	 */
	public String exibeListaSistemaOrigem() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
				
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaSistemaOrigem", "Inicio do metodo que exibe lista de sistemas de origem em memoria.");

		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

		lista = gerenteGPP.exibeMapSistemaOrigem();

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaSistemaOrigem", "Fim do metodo que exibe lista de sistemas de origem em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaSistemaOrigem
	 * Descricao: Atualiza a lista de sistemas de origem em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaSistemaOrigem() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaSistemaOrigem", "Inicio da atualizacao da lista de sistemas de origem em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaSistemaOrigem();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaSistemaOrigem", "Nao foi possivel atualizar o mapeamento de sistemas de origem em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de sistemas de origem em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaSistemaOrigem", "Fim da atualizacao da lista de sistemas de origem em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaTarifaTrocaMSISDN
	 * Descricao: Exibe a lista de tarifas de troca de MSISDN mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de tarifas de troca de MSISDN mapeadas em memoria
	 * @throws  
	 */
	public String exibeListaTarifaTrocaMSISDN() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		String lista = "";
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaTarifaTrocaMSISDN", "Inicio do metodo que exibe lista de tarifas de troca de MSISDN em memoria.");

		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

		lista = gerenteGPP.exibeMapTarifaTrocaMSISDN();

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaTarifaTrocaMSISDN", "Fim do metodo que exibe lista de tarifas de troca de MSISDN em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaTarifaTrocaMSISDN
	 * Descricao: Atualiza a lista de tarifas de troca de MSISDN em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaTarifaTrocaMSISDN() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaTarifaTrocaMSISDN", "Inicio da atualizacao da lista de tarifas de troca de MSISDN em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaTarifaTrocaMSISDN();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaTarifaTrocaMSISDN", "Nao foi possivel atualizar o mapeamento de tarifas de troca de MSISDN em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de tarifas de troca de MSISDN em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaTarifaTrocaMSISDN", "Fim da atualizacao da lista de tarifas de troca de MSISDN em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaValoresRecarga
	 * Descricao: Exibe a lista de Valores de Recarga mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de Valores de Recarga mapeadas em memoria
	 * @throws  
	 */
	public String exibeListaValoresRecarga() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaValoresRecarga", "Inicio do metodo que exibe lista de Valores de Recarga em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapValoresRecarga();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaValoresRecarga", "Fim do metodo que exibe lista de Valores de Recarga em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaValoresRecarga
	 * Descricao: Atualiza a lista de Valores de Recarga em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws	GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaValoresRecarga() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecarga", "Inicio da atualizacao da lista de Valores de Recarga em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaValoresRecarga();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecarga", "Nao foi possivel atualizar a lista de Valores de Recarga em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de Valores de Recarga em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecarga", "Fim da atualizacao da lista de Valores de Recarga em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaValoresRecargaPlanoPreco
	 * Descricao: Exibe a lista de Valores de Recarga em funcao dos Planos de Preco mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de relacionamento entre valores de recarga e planos de preco mapeados em memoria
	 * @throws  
	 */
	public String exibeListaValoresRecargaPlanoPreco() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaValoresRecargaPlanoPreco", 
				"Inicio do metodo que exibe lista de Valores de Recarga em funcao dos Planos de Preco em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapValoresRecargaPlanoPreco();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaValoresRecargaPlanoPreco", 
				"Fim do metodo que exibe lista de Valores de Recarga em funcao dos Planos de Preco em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaValoresRecargaPlanoPreco
	 * Descricao: Atualiza a lista de Valores de Recarga em funcao dos Planos de Preco em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws	GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaValoresRecargaPlanoPreco() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecargaPlanoPreco", 
				"Inicio da atualizacao da lista de Valores de Recarga em funcao dos Planos de Preco em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaValoresRecargaPlanoPreco();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecargaPlanoPreco", 
					"Nao foi possivel atualizar a lista de Valores de Recarga em funcao dos Planos de Preco em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de Valores de Recarga em funcao dos Planos de Preco em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaValoresRecargaPlanoPreco", 
					"Fim da atualizacao da lista de Valores de Recarga em funcao dos Planos de Preco em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaBonusPulaPula
	 * Descricao: Exibe a lista de valores de Bonus Pula-Pula mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de valores de Bonus Pula-Pula mapeados em memoria
	 * @throws  
	 */
	public String exibeListaBonusPulaPula() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaBonusPulaPula", 
				"Inicio do metodo que exibe lista de valores de Bonus Pula-Pula em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapBonusPulaPula();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaBonusPulaPula", 
				"Fim do metodo que exibe lista de valores de Bonus Pula-Pula em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaBonusPulaPula
	 * Descricao: Atualiza a lista de valores de Bonus Pula-Pula em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws	GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaBonusPulaPula() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaBonusPulaPula", 
				"Inicio da atualizacao da lista de valores de Bonus Pula-Pula em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaBonusPulaPula();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaBonusPulaPula", 
					"Nao foi possivel atualizar a lista de valores de Bonus Pula-Pula em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de valores de Bonus Pula-Pula em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaBonusPulaPula", 
					"Fim da atualizacao da lista de valores de Bonus Pula-Pula em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaPromocao
	 * Descricao: Exibe a lista de Promocoes mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de valores de Promocoes mapeadas em memoria
	 * @throws  
	 */
	public String exibeListaPromocao() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPromocao", 
				"Inicio do metodo que exibe lista de Promocoes em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapPromocao();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPromocao", 
				"Fim do metodo que exibe lista de Promocoes em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaPromocao
	 * Descricao: Atualiza a lista de Promocoes em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws	GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaPromocao() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocao", 
				"Inicio da atualizacao da lista de Promocoes em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaPromocao();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocao", 
					"Nao foi possivel atualizar a lista de Promocoes em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de Promocoes em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocao", 
					"Fim da atualizacao da lista de Promocoes em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaPromocaoDiaExecucao
	 * Descricao: Exibe a lista de dias de execucao das Promocoes mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de dias de execucao das Promocoes mapeados em memoria
	 * @throws  
	 */
	public String exibeListaPromocaoDiaExecucao() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPromocaoDiaExecucao", 
				"Inicio do metodo que exibe lista de dias de execucao das Promocoes em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapPromocaoDiaExecucao();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaPromocaoDiaExecucao", 
				"Fim do metodo que exibe lista de dias de execucao das Promocoes em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaPromocaoDiaExecucao
	 * Descricao: Atualiza a lista de dias de execucao das Promocoes em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws	GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaPromocaoDiaExecucao() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocaoDiaExecucao", 
				"Inicio da atualizacao da lista de dias de execucao das Promocoes em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaPromocaoDiaExecucao();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocaoDiaExecucao", 
					"Nao foi possivel atualizar a lista de dias de execucao das Promocoes em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de dias de execucao das Promocoes em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaPromocaoDiaExecucao", 
					"Fim da atualizacao da lista de dias de execucao das Promocoes em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeListaConfiguracaoGPP
	 * Descricao: Exibe a lista de configuracoes GPP mapeadas em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de configuracoes GPP mapeadas em memoria
	 * @throws  
	 */
	public String exibeListaConfiguracaoGPP() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaConfiguracaoGPP", "Inicio do metodo que exibe lista de configuracoes GPP em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapConfiguracaoGPP();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaConfiguracaoGPP", "Fim do metodo que exibe lista de configuracoes GPP em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaConfiguracaoGPP
	 * Descricao: Atualiza a lista de configuracoes GPP em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaConfiguracaoGPP() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaConfiguracaoGPP", "Inicio da atualizacao da lista de configuracoes GPP em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaConfiguracaoGPP();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaConfiguracaoGPP", "Nao foi possivel atualizar a lista de configuracoes GPP em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de configuracoes GPP em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaConfiguracaoGPP", "Fim da atualizacao da lista de configuracoes GPP em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: exibeListaRecOrigem
	 * Descricao: Exibe a lista de TTs mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de TTs mapeados em memoria
	 */
	public String exibeListaRecOrigem() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaRecOrigem", "Inicio do metodo que exibe lista de TTs em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMapRecOrigem();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeListaRecOrigem", "Fim do metodo que exibe lista de TTs em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaListaRecOrigem
	 * Descricao: Atualiza a lista de TTs em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaRecOrigem() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaRecOrigem", "Inicio da atualizacao da lista de TTs em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaListaRecOrigem();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaRecOrigem", "Nao foi possivel atualizar a lista de TTs em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de TTs em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaRecOrigem", "Fim da atualizacao da lista de TTs em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: exibeMotivosBloqueio
	 * Descricao: Exibe a lista de Motivos de Bloqueio mapeados em memoria no GPP
	 * @param 	
	 * @return 	string 		- lista de Motivos de Bloqueio mapeados em memoria
	 */
	public String exibeListaMotivosBloqueio() 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String lista = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeMotivosBloqueio", "Inicio do metodo que exibe lista de Motivos de Bloqueio em memoria.");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		
		lista = gerenteGPP.exibeMotivosBloqueio();
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeMotivosBloqueio", "Fim do metodo que exibe lista de Motivos de Bloqueio em memoria.");

		return lista;
	}
	
	/**
	 * Metodo...: atualizaMotivosBloqueio
	 * Descricao: Atualiza a lista de TTs em memoria no GPP
	 * @param 	
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaMotivosBloqueio() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaMotivosBloqueio", "Inicio da atualizacao da lista de Motivos de Bloqueio em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.atualizaMotivosBloqueio();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaListaMotivosBloqueio", "Nao foi possivel atualizar a lista de Motivos de Bloqueio em memoria. Excecao Interna do GPP ocorrida: " + e);

			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de TTs em memoria. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaMotivosBloqueio", "Fim da atualizacao da lista de Motivos de Bloqueio em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaModulacaoPlano
	 * Descricao: Atualiza a lista de modulacao de planos em memoria
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaModulacaoPlano() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		boolean retorno = false;
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaModulacaoPlano", "Inicio da atualizacao da lista de Modulacao de planos em memoria.");
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.atualizaModulacaoPlano();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de modulacao de planos. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaModulacaoPlano", "Fim da atualizacao da lista de Modulacao de planos em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaAssinantesNaoBonificaveis
	 * Descricao: Atualiza a lista de assinantes nao bonificaveis em memoria
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaAssinantesNaoBonificaveis() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		boolean retorno = false;
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaAssinantesNaoBonificaveis", "Inicio da atualizacao da lista de assinantes nao bonificaveis em memoria.");
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.atualizaAssinantesNaoBonificaveis();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de modulacao de planos. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaAssinantesNaoBonificaveis", "Fim da atualizacao da lista de assinantes nao bonificaveis em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: atualizaListaFeriados
	 * Descricao: Atualiza a lista de feriados
	 * @return 	boolean 		- True se criou e false caso contrario
	 * @throws  GPPInternalErrorException
	 */
	public synchronized boolean atualizaListaFeriados() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		boolean retorno = false;
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaFeriados", "Inicio da atualizacao da lista de feriados em memoria.");
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.atualizaFeriados();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException ("Nao foi possivel atualizar o mapeamento de feriados. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaListaFeriados", "Fim da atualizacao da lista de feriados em memoria.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: processaSMS
	 * Descricao: Ativa/Desativa processo de envio de SMS
	 * @param 	boolean deveProcessar 	- verifica se o metodo deve ser ativado ou desativado
	 * @return 	boolean 				- True se criou e false caso contrario
	 * @throws  
	 */
	public synchronized boolean processaSMS(boolean deveProcessar)
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "processaSMS", "Inicio do processo de ativacao/desativacao de envio de SMS.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.ativaDesativaEnvioSMS(deveProcessar, idProcesso);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "processaSMS", "Nao foi possivel ativar/desativar o envio de SMS. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "processaSMS", "Fim do processo de ativacao/desativacao de envio de SMS.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: escreveDebug
	 * Descricao: Ativa/Desativa mensagens de DEBUG no Log
	 * @param 	boolean escreveDebug 	- verifica se o metodo deve ser ativado ou desativado
	 * @return 	boolean 				- True se criou e false caso contrario
	 * @throws  
	 */
	public synchronized boolean escreveDebug(boolean escreveDebug)
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "escreveDebug", "Inicio do processo de ativacao/desativacao de DEBUG no Log.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.ativaDesativaDebug(escreveDebug);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "escreveDebug", "Nao foi possivel ativar/desativar o Debug no Log. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "escreveDebug", "Fim do processo de ativacao/desativacao de DEBUG no Log.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: ping
	 * Descricao: Verifica se o GPP esta no ar
	 * @param 	
	 * @return  string - Parametros de Execucao do GPP
	 * @throws  
	 */
	public String ping ( )
	{
		// Inicializa variaveis do metodo
		String retPing = null;
		long idProcesso = 0; // Utiliza o id 0 para o processo de ping
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "ping", "Retornando Ping do GPP ...");
		
		// Criando uma classe de aplicacao
		GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
		retPing = gerenteGPP.pingGPP ();
		return retPing;
	}
	
	/**
	 * Metodo...: finalizaGPP
	 * Descricao: Finaliza o processo GPP
	 * @param 	
	 * @return  
	 * @throws  
	 */
	public void finalizaGPP ()
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		
		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "finalizaGPP", "Finalizando GPP ...");
		
		try
		{	
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			gerenteGPP.paraGPP ();
		}
		catch (Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "finalizaGPP", "Erro tentando destruir os pools de conexao: " + e);
		}		
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "finalizaGPP", "GPP Finalizado.");
		}
		System.exit ( 0 );
	}
	
	/**
	 * Metodo...: getStatusProdutorSMS
	 * Descricao: Busca status atual do serviço de envio de SMS
	 * @param 	
	 * @return  boolean - True se criou e false caso contrario
	 * @throws  
	 */
	public boolean getStatusProdutorSMS()
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusProdutorSMS", "Buscando o status atual do processo de envio de SMS.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.getStatusProdutorSMS();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "getStatusProdutorSMS", "Nao foi possivel buscar o status atual do processo de envio de SMS. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusProdutorSMS", "Fim do processo de busca do status atual do processo de envio de SMS.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: getStatusEscreveDebug
	 * Descricao: Busca status atual processo de escrita de debug no LOG
	 * @param 	
	 * @return  boolean - True se criou e false caso contrario
	 * @throws  
	 */
	public boolean getStatusEscreveDebug()
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		boolean retorno = false;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusEscreveDebug", "Buscando o status atual do processo de escrita de Debug no LOG.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.getStatusEscreveDebug();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "getStatusEscreveDebug", "Erro ao identificar o status atual do processo de escrita de Debug no LOG. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusEscreveDebug", "Fim do processo de busca do status atual do processo de escrita de Debug no LOG.");
		}		
		return retorno;
	}

	/**
	 * Metodo...: getHistProcessosBatch
	 * Descricao: Busca a lista de historico de execucao de processos batch
	 * @param 	short aIdProcBatch 	- Identificador do processo batch
	 * @param 	string aDatIni 		- Data de inicio do processo
	 * @param 	string aDatFim 		- Data de fim do processo
	 * @param 	string aIdtStatus 	- Identificador do status
	 * @return  string 				- Lista de historico de execucao de processos batch
	 * @throws  GPPInternalErrorException
	 */
	public String getHistProcessosBatch(short aIdProcBatch, String aDatIni, String aDatFim, String aIdtStatus) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String retorno = "";
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getHistProcessosBatch", "Buscando a lista de execucao dos processos batch.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.getHistProcessosBatch(aIdProcBatch,aDatIni,aDatFim,aIdtStatus);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "getHistProcessosBatch", "Erro ao buscar a lista de execucao dos processos batch. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusEscreveDebug", "Fim do processo de busca da lista de execucao dos processos batch.");
		}
		return retorno;
	}
	
	/**
	 * Metodo...: consultaProcessosBatch
	 * Descricao: Consulta a lista de execucao de processos batch
	 * @param 	
	 * @return  string 	- lista de execucao de processos batch
	 * @throws  GPPInternalErrorException
	 */
	public String consultaProcessosBatch() throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String retorno = "";
	
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getHistProcessosBatch", "Buscando a lista de execucao dos processos batch.");
	
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retorno = gerenteGPP.consultaProcessosBatch();
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "getHistProcessosBatch", "Erro ao buscar a lista de execucao dos processos batch. Excecao Interna do GPP ocorrida: " + e);
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "getStatusEscreveDebug", "Fim do processo de busca da lista de execucao dos processos batch.");
		}
		return retorno;
	}
	
	/**
	 * Metodo...: getNumerodeConexoes
	 * Descricao: Verifica o numero de conexoes disponiveis
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	short 			- Numero de conexoes disponiveis
	 * @throws  GPPInternalErrorException
	 */
	public short getNumeroConexoesDisponiveis ( short tipoConexao ) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		int retNumerodeConexoesDisponiveis = 0;
		long idProcesso = 0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumeroConexoesDisponiveis", "Inicio da Consulta de Numero de Conexoes Disponiveis.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);

			retNumerodeConexoesDisponiveis = gerenteGPP.consultaNumeroConexoesDisponiveis(tipoConexao);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_GERENTEGPP, "getNumeroConexoesDisponiveis", "Excecao ocorrida: "+ e);				
			
			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "retNumerodeConexoes = " + retNumerodeConexoesDisponiveis);
	
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "Fim da Consulta de Numero de Conexoes Ativas.");
		 }		
		return (short)retNumerodeConexoesDisponiveis;
	}
	
	/**
	 * Metodo...: getListaProcessosComConexoesEmUso
	 * Descricao: Lista os ids de processo que possuem conexoes em uso
	 * @param 	int tipoConexao - Tipo de Conexao (TecnomenAprovisionamento, TecnomenRecarga, PREPConexao...)
	 * @return 	IdProcessoConexao[] - Lista dos ids e datas de processos que estao utilizando conexao
	 * @throws  GPPInternalErrorException
	 */
	public IdProcessoConexao[] getListaProcessosComConexoesEmUso( short tipoConexao ) throws GPPInternalErrorException
	{
		// Inicializa variaveis do metodo
		IdProcessoConexao[] listaProcessos = null;
		long idProcesso = 0;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		
		Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getListaProcessosComConexoesEmUso", "Inicio da Consulta de Ids que possuem conexoes em uso.");	
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			listaProcessos = gerenteGPP.getListaProcessosComConexoesEmUso(tipoConexao);
		}
		catch (GPPInternalErrorException e)
		{
			Log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_GERENTEGPP, "getListaProcessosComConexoesEmUso", "Excecao ocorrida: "+ e);				

			throw new GPPInternalErrorException ("Excecao GPP ocorrida: " + e);			 
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO,Definicoes.CN_GERENTEGPP, "getNumerodeConexoes", "Fim da Consulta de Ids que possuem conexoes em uso.");
		 }		
		return listaProcessos;
	}
	
	/**
	 *	Exibe uma lista com o numero de statements abertos por conexao com o banco de dados.
	 *
	 *	@return		Lista com o numero de statements abertos por conexao com o banco de dados.
	 */
	public String exibirNumeroStatementsPorConexao() 
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return gerenteGPP.getListaNumStatementsPorConexao();
	}
	
	/**
	 * Metodo....:adicionaThreadEnvioSMS
	 * Descricao.:Adiciona threads de envio de SMS dentro do pool controlado pelo ConsumidorSMS
	 * @return boolean - Indica se conseguiu adicionar ou nao a nova thread de envio de SMS
	 * @throws GPPInternalErrorException
	 */
	public boolean adicionaThreadEnvioSMS() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return gerenteGPP.adicionaThreadEnvioSMS(); 
	}

	/**
	 * Metodo...:removeThreadEnvioSMS
	 * Descricao:Remove thread de envio de SMS no pool controlado pelo ConsumidorSMS
	 * @return boolean - Indica se conseguiu remover ou nao a thread de envio de SMS
	 * @throws GPPInternalErrorException
	 */
	public boolean removeThreadEnvioSMS() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return gerenteGPP.removeThreadEnvioSMS();
	}
	
	/**
	 * Metodo...:finalizaPoolEnvioSMS
	 * Descricao:Destroi todas as threads de envio de SMS
	 *
	 */
	public void finalizaPoolEnvioSMS()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.finalizaPoolEnvioSMS();
	}
	
	/**
	 * Metodo...:inicializaPoolEnvioSMS
	 * Descricao:Inicializa o pool de threads de envio de SMS
	 *
	 */
	public void inicializaPoolEnvioSMS()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.inicializaPoolEnvioSMS();
	}
	
	/**
	 * Metodo....:getNumeroThreadsEnvioSMS
	 * Descricao.:Metodo que retorna o numero de threads atualmente ativas
	 *            no pool do ConsumidorSMS para envio de SMS
	 * @return in - Numero de threads ativas de envio de SMS
	 */
	public short getNumeroThreadsEnvioSMS()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return gerenteGPP.getNumeroThreadsEnvioSMS();
	}

	/**
	 * Metodo....:getNumThreadsImpCDRDadosVoz
	 * Descricao.:Retorna o numero de threads de importacao de CDRs
	 * @return short - Numero de threads
	 */
	public short getNumThreadsImpCDRDadosVoz()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return (short)gerenteGPP.getNumThreadsImpCDRDadosVoz();
	}
	
	/**
	 * Metodo....:getNumThreadsImpCDREvtRec
	 * Descricao.:Retorna o numero de threads de importacao de CDRs
	 * @return short - Numero de threads
	 */
	public short getNumThreadsImpCDREvtRec()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return (short)gerenteGPP.getNumThreadsImpCDREvtRec();
	}
	
	/**
	 * Metodo....:getNumArqPendentesDadosVoz
	 * Descricao.:Retorna o numero de arquivos de CDR pendentes de importacao 
	 * @return short - Numero de arquivos
	 */
	public short getNumArqPendentesDadosVoz()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return (short)gerenteGPP.getNumArqPendentesDadosVoz();
	}
	
	/**
	 * Metodo....:getNumArqPendentesEvtRec
	 * Descricao.:Retorna o numero de arquivos de CDR pendentes de importacao 
	 * @return short - Numero de arquivos
	 */
	public short getNumArqPendentesEvtRec()
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		return (short)gerenteGPP.getNumThreadsImpCDREvtRec();
	}
	
	/**
	 * Metodo....:removeThreadsDadosVoz
	 * Descricao.:Remove threads de importacao de cdrs de dados e voz
	 * @throws GPPInternalErrorException
	 */
	public void removeThreadsDadosVoz() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.removeThreadsDadosVoz();
	}
	
	/**
	 * Metodo....:removeThreadsEvtRec
	 * Descricao.:Remove threads de importacao de cdrs de eventos e recargas
	 * @throws GPPInternalErrorException
	 */
	public void removeThreadsEvtRec() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.removeThreadsEvtRec();
	}
	
	/**
	 * Metodo....:inicializaThreadsDadosVoz
	 * Descricao.:Inicializa o pool de threads de importacao de dados e voz
	 * @throws GPPInternalErrorException
	 */
	public void inicializaThreadsDadosVoz() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.inicializaThreadsDadosVoz();
	}

	/**
	 * Metodo....:inicializaThreadsEvtRec
	 * Descricao.:Inicializa o pool de threads de importacao de eventos e recargas
	 * @throws GPPInternalErrorException
	 */
	public void inicializaThreadsEvtRec() throws GPPInternalErrorException
	{
		long idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(idProcesso);
		gerenteGPP.inicializaThreadsEvtRec();
	}
	
	/**
	 * Metodo....:limpaCacheFF
	 * Descricao.:Realiza a limpeza do cache de FF na importacao de CDRs
	 */
	public void reiniciaCacheFF()
	{
		GerenciadorCacheFF.getInstance().iniciaCacheDados();
	}
	
	/**
	 * Metodo...: atualizaMapeamentos
	 * Descricao: Atualiza todos os mapeamentos em memoria
	 * @param	boolean	limpar	- Flag indicando que o mapeamento deve ser limpo antes da atualizacao. 
	 * @return 	short 			- Codigo de retorno da operacao
	 */
	public short atualizaMapeamentos(boolean limpar) 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaMapeamentos", "Inicio da atualizacao dos mapeamentos em memoria.");
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.atualizaMapeamentos(limpar);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaMapeamentos", "Excecao: " + e);
			retorno = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaMapeamentos", "Fim da atualizacao dos mapeamentos em memoria.");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: atualizaMapeamento
	 * Descricao: Atualiza o mapeamento em memoria informado pelo nome da classe
	 * @param	String	nome	- Nome da classe
	 * @param	boolean	limpar	- Flag indicando que o mapeamento deve ser limpo antes da atualizacao. 
	 * @return 	short 			- Codigo de retorno da operacao
	 */
	public short atualizaMapeamento(String nome, boolean limpar) 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaMapeamento", "Inicio da atualizacao do mapeamento em memoria: " + nome);
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.atualizaMapeamento(nome, limpar);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "atualizaMapeamento", "Excecao: " + e);
			retorno = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "atualizaMapeamento", "Fim da atualizacao do mapeamento em memoria");
		}		
		return retorno;
	}
	
	/**
	 * Metodo...: exibeMapeamento
	 * Descricao: Exibe o mapeamento em memoria informado pelo nome da classe
	 * @param	String	nome	- Nome da classe
	 * @return 	short 			- Codigo de retorno da operacao
	 */
	public String exibeMapeamento(String nome) 
	{
		// Inicializa variaveis do metodo
		long idProcesso = 0;
		String retorno = null;
		
		// Obtendo um ID de processo para o LOG
		idProcesso = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);

		Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeMapeamento", "Inicio da exibicao do mapeamento em memoria: " + nome);
		
		try
		{
			// Criando uma classe de aplicacao
			GerenteGPP gerenteGPP =  new GerenteGPP(idProcesso);
			retorno = gerenteGPP.exibeMapeamento(nome);
		}
		catch(Exception e)
		{
			Log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_GERENTEGPP, "exibeMapeamento", "Excecao: " + e);
			retorno = "Excecao:" + e;
		}
		finally
		{
			Log.log(idProcesso, Definicoes.INFO, Definicoes.CN_GERENTEGPP, "exibeMapeamento", "Fim da exibicao do mapeamento em memoria");
		}		
		return retorno;
	}
	
	/**
	 * Metodo....:liberarConexoesEmUso
	 * Descricao.:Metodo que força a liberação de conexoes de BD e da Tecnomen usadas por um processo
	 * @param idProcesso Identificador do processo que terá suas conexoes liberadas
	 */
	public void liberarConexoesEmUso(long idProcesso)
	{
		long id = Log.getIdProcesso(Definicoes.CN_GERENTEGPP);
		GerenteGPP gerenteGPP = new GerenteGPP(id);
		gerenteGPP.liberarConexoesEmUso(idProcesso);
	}
}