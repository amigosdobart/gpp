package com.brt.gpp.aplicacoes.recarregar;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.recarregar.RecargaDAO;
import com.brt.gpp.aplicacoes.recarregar.RecargaXMLParser;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapCategoria;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.MapValoresRecargaTFPP;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;

/**
 *	Classe responsavel pela implementacao das logicas de recargas de creditos para um assinante.
 *
 *	@version		1.0		28/02/2004		Primeria versao.
 *	@author 		Denys Oliveira
 *
 *	@version		2.0		01/07/2005		Adaptacao do codigo para o LigMix.
 *	@author			Marcos Castelo Magalhaes
 *
 *	@version		3.0		06/05/2007		Adaptacao para o Controle Total.
 *	@author			Daniel Ferreira.
 */
public class Recarregar extends InsercaoCreditos
{
	/**
	 * Metodo...: Recarregar
	 * Descricao: Construtor
	 * @param 	long	aIdProcesso		Identificador do Processo
	 */
	public Recarregar(long aIdProcesso)
	{
		super(aIdProcesso, Definicoes.CL_RECARREGAR);
	}
	
	/**
	 * Metodo....: consultaPreRecarga
	 * Descricao.: Realiza as validacoes necessarias para a realizacao das recargas
	 * 
	 * @param msisdn		- Numero do assinante
	 * @param idValor		- Valor da recarga/ajuste
	 * @param tipoTransacao	- Tipo da transacao
	 * 
	 */
	public short consultaPreRecarga(String msisdn, double idValor, String tipoTransacao, String sistemaOrigem)
	{
		short result = Definicoes.RET_MSISDN_NAO_ATIVO;
		ParametrosRecarga recarga = new ParametrosRecarga();
		
		super.log(Definicoes.DEBUG, "consultarPreRecarga", "Msisdn: " 		 + msisdn 		 + " - " +
														   "ID Valor: " 	 + idValor 		 + " - " +
														   "TipoTransacao: " + tipoTransacao + " - " + 
														   "Origem: " 		 + sistemaOrigem);
		
		try
		{
			if (Definicoes.SO_BCO.equalsIgnoreCase(sistemaOrigem))
			{
				recarga = this.getParametrosRecarga(msisdn, idValor);
				recarga.setTipoTransacao(tipoTransacao);
				recarga.setTipoCredito(Definicoes.TIPO_CREDITO_REAIS);
				recarga.setSistemaOrigem(sistemaOrigem);
				recarga.setIdentificacaoRecarga(RecargaDAO.newIdRecarga(super.logId));
				
				result = super.validarParametros(recarga);
			}
			else if (Definicoes.SO_VAREJO.equalsIgnoreCase(sistemaOrigem))
				result = this.consultaPreRecargaVarejo(msisdn, idValor, tipoTransacao);
			else
			{
				recarga.setTipoTransacao(tipoTransacao);
				recarga.setSistemaOrigem(sistemaOrigem);
				Assinante assinante = new ConsultaAssinante(super.logId).consultarAssinantePlataforma(msisdn);
				recarga.setAssinante(assinante);
				
				result = super.validarTTSistemaOrigem(recarga);
			}
		}
		catch(Exception e)
		{
			result = Definicoes.RET_ERRO_TECNICO;
			super.log(Definicoes.ERRO, "consultaPreRecarga", recarga.toString() + "Retorno: "+result+" - Excecao: " + e);
		}
		finally
		{
			super.log(Definicoes.INFO, "consultarPreRecarga", recarga.toString() + " - Retorno: " + result);
		}
		
		return result;
	}
	
	/**
	 * Metodo...: consultaPreRecargaBanco
	 * Descricao: Realiza Consulta Pré-Recarga Via Banco
	 * 
	 * @param 	double	valor		Valor da Recarga
	 * @return	short				0=sucesso; !0=erro
	 */
	public short consultaPreRecarga(String msisdn, double idValor)
	{
		short result = Definicoes.RET_MSISDN_NAO_ATIVO;
		
		super.log(Definicoes.DEBUG, "consultaPreRecarga", "MSISDN: " + msisdn + " - ID Valor: " + idValor);
		
		try
		{
			// A montagem dos parametros de recarga sao montados agora no metodo abaixo
			ParametrosRecarga recarga = this.getParametrosRecarga(msisdn, idValor);
			
			result = super.validarPreRecarga(recarga);
			//result = super.validarParametros(recarga);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "consultaPreRecarga", "MSISDN: " + msisdn + " - ID Valor: " + idValor + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		
		super.log(Definicoes.DEBUG, "consultaPreRecarga", "MSISDN: " + msisdn + " - ID Valor: " + idValor + " - Retorno: " + result);
		
		return result;
	}
	
	/**
	 * Metodo....: getParametrosRecarga
	 * Descricao.: Retorna um objeto ParametrosRecarga com os valores
	 * 			   do assinante
	 * 
	 * @param  msisdn	- Numero do assinante
	 * @param  idValor	- Valor da recarga
	 * @return recarga	- Objeto com os valores do assinante/recarga
	 * @throws Exception
	 */
	private ParametrosRecarga getParametrosRecarga(String msisdn, double idValor) throws Exception
	{
		ParametrosRecarga recarga = null;
		
		try
		{
			Assinante assinante = new ConsultaAssinante(super.logId).consultarAssinantePlataforma(msisdn);
			
			recarga = ParametrosRecarga.getParametrosRecarga(assinante, idValor);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "getParametrosRecarga", "MSISDN: " + msisdn + " - ID Valor: " + idValor + " - Excecao: " + e);
			throw e;
		}
		
		// Objeto contendo os dados do Assinante/Recarga
		return recarga;
	}
	
	/**
	 * Metodo...: consultaPreRecargaVarejo
	 * Descricao: Verifica se um canal de varejo ainda pode vender recargas
	 * @param 	double	_valorCreditos	ID valor
	 * @param 	String	_tipoTransacao	Canal/Origem
	 * @return	0 se ok ou erro funcional
	 */
	public short consultaPreRecargaVarejo(String msisdn, double idValor, String tipoTransacao)
	{
		short result = Definicoes.RET_MSISDN_NAO_ATIVO;
		
		super.log(Definicoes.DEBUG, "consultaPreRecargaVarejo", "MSISDN: " + msisdn + " - ID Valor: " + idValor + " - Tipo de Transacao: " + tipoTransacao);
		
		try
		{
			ParametrosRecarga recarga = new ParametrosRecarga();
			
			recarga.setMSISDN(msisdn);
			recarga.setIdOperacao(Definicoes.TIPO_RECARGA);
			recarga.setIdValor(idValor);
			recarga.setTipoTransacao(tipoTransacao);
			recarga.setSistemaOrigem(Definicoes.SO_VAREJO);
			if (recarga.getDatOrigem() == null)
				recarga.setDatOrigem(Calendar.getInstance().getTime());
			
			Assinante assinante = new ConsultaAssinante(super.logId).consultarAssinantePlataforma(msisdn);
			recarga.setAssinante(assinante);
			
			if(assinante.getNaturezaAcesso().equalsIgnoreCase("GSM"))
			{
				int idCategoria = MapPlanoPreco.getInstance().consultaCategoria(assinante.getPlanoPreco());
				Categoria categoria = MapCategoria.getInstance().getCategoria(idCategoria);
				recarga.setValores(MapValoresRecarga.getInstance().getValoresRecarga(recarga.getIdValor(), categoria, recarga.getDatOrigem()));
			}
			else
				recarga.setValores(MapValoresRecargaTFPP.getInstancia().getValorRecarga(recarga.getIdValor()));
			
			result = super.validarPreRecargaVarejo(recarga);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "consultaPreRecargaBanco", "MSISDN: " + msisdn + " - ID Valor: " + idValor + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		
		return result;
	}
	
	/**
	 *	Executa a recarga de um assinante.
	 *
	 *	@param		xmlRecarga				XML com os parametros de recarga.
	 *	@return 	Codigo de retorno da operacao.
	 *	@throws		GPPInternalErrorException, GPPBadXMLFormatException 
	 */
	public short executarRecarga(String xmlRecarga) throws GPPBadXMLFormatException
	{
		ParametrosRecarga	recarga	= null;
		short				result	= Definicoes.RET_OPERACAO_OK;
		super.log(Definicoes.DEBUG, "executarRecarga", "XML para execucao da recarga:" + xmlRecarga);
		
		//Obtendo os parametros de recarga a partir do XML.
		recarga = RecargaXMLParser.parseXMLRecarga(xmlRecarga);
		
		try
		{
			//Caso trate-se de uma recarga via banco ou canal varejo, e necessario atualizar os atributos idTerminal e
			//tipoTerminal com os campos hash_cc e cpf, respectivamente. Isto e feito para garantir compatibilidade 
			//com versoes antigas.
			if((recarga.getTipoTransacao() != null) && (recarga.getTipoTransacao().length() >= 2) &&
			   ((recarga.getTipoTransacao().substring(0, 2).equals(Definicoes.CANAL_BANCO)) || 
			   	(recarga.getTipoTransacao().substring(0, 2).equals(Definicoes.CANAL_VRJ))))
			{
				recarga.setIdTerminal  (recarga.getHash_cc());
				recarga.setTipoTerminal(recarga.getCpf    ());
				recarga.setHash_cc     (null);
				recarga.setCpf         (null);
			}
			
			//Executando a consulta do assinante nas plataformas.
			Assinante assinante = new ConsultaAssinante(super.logId).consultarAssinantePlataforma(recarga.getMSISDN());
			recarga.setAssinante(assinante);
			
			//Decidindo qual valor de recarga e qual metodo de recarga deve ser executado baseado na natureza do 
			//assinante.
			if((assinante.getNaturezaAcesso().equalsIgnoreCase("GSM")) || 
			   (assinante.getNaturezaAcesso().equalsIgnoreCase("INEXISTENTE")))
			{
				int idCategoria = MapPlanoPreco.getInstance().consultaCategoria(assinante.getPlanoPreco());
				Categoria categoria = MapCategoria.getInstance().getCategoria(idCategoria);
				recarga.setValores(MapValoresRecarga.getInstance().getValoresRecarga(recarga.getIdValor(), categoria, recarga.getDatOrigem()));
				result = this.executarRecargaMultiplosSaldos(recarga);
			}
			else
			{
				recarga.setValores(MapValoresRecargaTFPP.getInstancia().getValorRecarga(recarga.getIdValor()));
				result = this.executarRecargaMASC(recarga);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO , "executarRecarga", "XML de recarga: " + xmlRecarga + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		
		return result;
	}
	
	/**
	 *	Executa a recarga de um assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		tipoTransacao			Tipo da transacao de recarga.
	 *	@param		identificacaoRecarga	Codigo de identificacao de uma determinada recarga.
	 *	@param		tipoCredito				Tipo de credito.
	 *	@param		idValor					Identificador do valor da recarga.
	 *	@param		dataHora				Data e hora da recarga (yyyymmddhhmmss).
	 *	@param		sistemaOrigem			Sistema de Origem que esta realizando a recarga.
	 *	@param		operador				Identificador do operador que esta realizando a recarga.
	 *	@param		nsuInstituicao			Indetificacao da recarga da instituicao.
	 *	@param		idTerminal				Identificador do terminal de execucao da recarga.
	 *	@param		tipoTerminal			Tipo de terminal de execucao da recarga.
	 *	@param		dataContabil			Data Contabil da recarga.
     *  @param      assinante               Se o assinante for null, eh feita consulta na plataforma.
	 *	@return 	Codigo de retorno da operacao. 
	 */
	public short executarRecarga(String	msisdn, 
								 String	tipoTransacao, 
								 String	identificacaoRecarga, 
								 String	tipoCredito, 
								 double	idValor, 
								 Date	dataHora, 
								 String	sistemaOrigem, 
								 String	operador, 
								 String	nsuInstituicao,
								 String	hashCc,
								 String	cpf,
								 String	idTerminal,
								 String	tipoTerminal,
								 Date	dataContabil,
                                 Assinante assinante)
	{
		short				result	= Definicoes.RET_OPERACAO_OK;
		ParametrosRecarga	recarga	= new ParametrosRecarga();
		
		super.log(Definicoes.DEBUG, "executarRecarga", "MSISDN: " + msisdn + " - Tipo de Transacao: " + tipoTransacao + " - ID Valor: " + idValor);
		
		try
		{
			//Preenchendo os parametros da recarga.
			recarga.setIdOperacao(Definicoes.TIPO_RECARGA);
			recarga.setIdentificacaoRecarga(identificacaoRecarga);
			recarga.setMSISDN(msisdn);
			recarga.setDatOrigem(dataHora);
			recarga.setDatRecarga(Calendar.getInstance().getTime());
			recarga.setDataContabil(dataContabil);
			recarga.setTipoTransacao(tipoTransacao);
			recarga.setTipoCredito(tipoCredito);
			recarga.setIdValor(idValor);
			recarga.setSistemaOrigem(sistemaOrigem);
			recarga.setOperador(operador);
			recarga.setNsuInstituicao(nsuInstituicao);
			recarga.setHash_cc(hashCc);
			recarga.setCpf(cpf);
			recarga.setIdTerminal(idTerminal);
			recarga.setTipoTerminal(tipoTerminal);
			
            //Inserindo os dados do assinante.
            if(assinante == null)
                assinante = (new ConsultaAssinante(super.logId)).consultarAssinantePlataforma(msisdn);
                
            recarga.setAssinante(assinante);
            
			//Decidindo qual valor de recarga e qual metodo de recarga deve ser executado baseado na natureza do 
			//assinante.
			if((assinante.getNaturezaAcesso().equalsIgnoreCase("GSM")) || 
			   (assinante.getNaturezaAcesso().equalsIgnoreCase("INEXISTENTE")))
			{
				int idCategoria = MapPlanoPreco.getInstance().consultaCategoria(assinante.getPlanoPreco());
				Categoria categoria = MapCategoria.getInstance().getCategoria(idCategoria);
				recarga.setValores(MapValoresRecarga.getInstance().getValoresRecarga(idValor, categoria, recarga.getDatOrigem()));
				result = this.executarRecargaMultiplosSaldos(recarga);
			}
			else
			{
				recarga.setValores(MapValoresRecargaTFPP.getInstancia().getValorRecarga(idValor));
				result = this.executarRecargaMASC(recarga);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO , "executarRecarga", "MSISDN: " + msisdn + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		
		return result;
	}	
	
	/**
	 *	Executa o processo de recarga para acessos da plataforma Tecnomen.
	 *
	 *	@param		recarga					Parametros de recarga.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short executarRecargaMultiplosSaldos(ParametrosRecarga recarga)
	{
		short result = Definicoes.RET_OPERACAO_OK;
		
		try
		{
			super.log(Definicoes.DEBUG, "executarRecargaMultiplosSaldos", recarga.toString());
			
			//Validando os parametros da recarga.
			result = super.validarParametros(recarga);
			
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				//Recarregando o assinante.
				result = super.executarRecarga(recarga);
				
				//Concedendo o bonus ao assinante, caso exista.
				if((result == Definicoes.RET_OPERACAO_OK) && (recarga.getValores().getBonus() != null))
					new Ajustar(super.logId).executarAjuste(recarga.getMSISDN(),
															Definicoes.AJUSTE_BONUS_NA_RECARGA,
															Definicoes.TIPO_CREDITO_REAIS,
															recarga.getValores().getBonus(),
															Definicoes.TIPO_AJUSTE_CREDITO,
															Calendar.getInstance().getTime(),
															recarga.getSistemaOrigem(),
															recarga.getOperador(),
															recarga.getAssinante(),
															"Bonus da Recarga",
															true,
                                                            null);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "executaRecargaMultiplosSaldos", recarga.toString() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			if(result != Definicoes.RET_OPERACAO_OK) 
				if(!RecargaDAO.incrementarRecargaNok(recarga, result, super.logId))
					super.log(Definicoes.WARN, "executarRecargaMultiplosSaldos", recarga.toString() + " - Nao foi possivel inserir registro de erro de recarga.");
			
			super.log(Definicoes.INFO, "executarRecargaMultiplosSaldos", recarga.toString() + " - Codigo de retorno da operacao: " + result);
		}
		
		return result;
	}
	
	/**
	 *	Executa a recarga via MASC.
	 *
	 *	@param		recarga					Parametros da recarga.
	 *	@return		Codigo de retorno da operacao. 
	 */
	private short executarRecargaMASC(ParametrosRecarga recarga)
	{
		short		result		= Definicoes.RET_OPERACAO_OK;
		PREPConexao	conexaoPrep	= null;
		
		super.log(Definicoes.DEBUG, "executaRecargaMASC", recarga.toString());
		
		try
		{
			//Obtendo conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			
			//Executando a validacao da recarga.
			result = super.validarRecargaMASC(recarga);
			
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				//No caso da recarga para o MASC sera efetuada somente uma operacao. Caso tenha sucesso, o registros  
				//de recarga sera inserido na tabela. Caso aplicavel, o registro de bonus tambem sera inserido.
				Assinante		assinante		= recarga.getAssinante();
				ValoresRecarga	valores			= recarga.getValores();
				String			msisdn			= recarga.getMSISDN();
				double			valor			= valores.getSaldoPrincipal() + valores.getValorBonusPrincipal();
				double			valorPago		= recarga.getValores().getValorEfetivoPago();
				int				numDiasExp		= recarga.getValores().getNumDiasExpiracaoPrincipal();
				String			idRecarga		= recarga.getIdentificacaoRecarga();
				String			tipoTransacao	= recarga.getTipoTransacao();
				
				DadosRecarga retornoMasc = super.insereCreditosTFPP(msisdn, valor, numDiasExp, idRecarga); 
				result = (short)retornoMasc.getCodigoErro();
				
				//Executando o pos-processamento da recarga.
				if(result == Definicoes.RET_OPERACAO_OK)
				{
					//Atualizando os saldos do assinante de acordo com os valores da recarga.
					assinante.atualizarSaldos(valores);
					//Inserindo o registro da recarga.
					RecargaDAO.inserirRecargaTFPP(recarga, conexaoPrep);
					//Caso a recarga tenha bonus, e necessario inserir o registro. Nao e necessario chamar o MASC
					//novamente uma vez que o bonus ja foi concedido na chamada anterior.
					if(valores.getBonus() != null)
					{
						//Atualizando os saldos do assinante de acordo com os valores do bonus.
						assinante.atualizarSaldos(valores.getBonus());
						
						//Criando objeto ParametrosRecarga para insercao do registro de ajuste.
						ParametrosRecarga ajuste = new ParametrosRecarga();
						ajuste.setIdOperacao(Definicoes.TIPO_AJUSTE);
						ajuste.setIdentificacaoRecarga(recarga.getIdentificacaoRecarga() + "B");
						ajuste.setMSISDN(msisdn);
						ajuste.setAssinante(assinante);
						ajuste.setDatOrigem(Calendar.getInstance().getTime());
						ajuste.setDatRecarga(Calendar.getInstance().getTime());
						ajuste.setTipoTransacao(Definicoes.AJUSTE_BONUS_NA_RECARGA);
						ajuste.setTipoCredito(Definicoes.TIPO_CREDITO_REAIS);
						ajuste.setIdValor(recarga.getIdValor());
						ajuste.setValores(valores.getBonus());
						ajuste.setSistemaOrigem(recarga.getSistemaOrigem());
						ajuste.setOperador(recarga.getOperador());
						ajuste.setNsuInstituicao(recarga.getNsuInstituicao());
						
						//Inserindo o registro do bonus na tabela.
						RecargaDAO.inserirRecargaTFPP(ajuste, conexaoPrep);
					}
					
					//Se o sistema de origem for revenda varejo, e necessario atualizar o valor utilizado pelo agente.
					if((recarga.getSistemaOrigem().equals(Definicoes.SO_VAREJO)) ||
					   (recarga.getSistemaOrigem().equals(Definicoes.SO_DEALER)))
						RecargaDAO.atualizarCreditoCanalVarejo(tipoTransacao, valorPago, conexaoPrep);
				}
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "executaRecargaMASC", recarga.toString() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			if(result != Definicoes.RET_OPERACAO_OK) 
				if(!RecargaDAO.incrementarRecargaNok(recarga, result, conexaoPrep))
					super.log(Definicoes.WARN, "executaRecargaMASC", recarga.toString() + " - Nao foi possivel inserir registro de erro de recarga.");
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
			super.log(Definicoes.INFO,"executaRecargaMASC", recarga.toString() + " - Codigo de retorno da operacao: " + result);
		}
		
		return result;
	}
}