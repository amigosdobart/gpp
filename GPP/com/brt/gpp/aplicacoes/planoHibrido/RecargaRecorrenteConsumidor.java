package com.brt.gpp.aplicacoes.planoHibrido;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonificacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoPacoteBonus;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoTransacao;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.aplicacoes.recarregar.RecargaDAO;
import com.brt.gpp.aplicacoes.recarregar.Recarregar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapBonusPulaPula;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 * 
 * Este arquivo refere-se a classe RecargaGeneva, responsavel pela implementacao
 * da logica de leitura e execucao de recargas de assinantes provenientes do
 * Genevade
 * 
 * <P>
 * Versao: 1.0
 * 
 * @Autor: Camile Cardoso Couto Data: 26/03/2004
 * 
 * Modificado por: Gustavo Gusmao Data: 11/11/2005 Razao: Remodelagem
 * produtor-consumidor
 * 
 * @author	Joao Paulo Galvagni Junior
 * @since	30/03/2008
 * @modify	Adequacao do codigo de recarga recorrente para contemplar a Promocao
 * 			do Dia das Maes 2008, onde o bonus Pula-Pula eh dividido em:
 * 			On-Net, Off-Net e Recarga, nao mais havendo o lancamento unico
 * 
 */
public class RecargaRecorrenteConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private PREPConexao 		conexaoPrep;
	private MapConfiguracaoGPP 	mapConfig;
	private ControlePulaPula 	controle;
	
	public RecargaRecorrenteConsumidor()
	{
		super(GerentePoolLog.getInstancia(RecargaRecorrenteConsumidor.class).getIdProcesso(Definicoes.CL_RECARGA_RECORRENTE), Definicoes.CL_RECARGA_RECORRENTE);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		try
		{
			controle = new ControlePulaPula(super.getIdLog());
			
			// Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "startup", "Problemas Mapeamento das Configuracoes GPP.");
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "Startup", "Problemas nos Mapeamentos.");
			throw new GPPInternalErrorException(e.toString());
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		conexaoPrep = produtor.getConexao();
		startup();
	}
	
	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#execute(Object)
	 */
	public void execute(Object obj) throws GPPInternalErrorException
	{
		short codRetorno = Definicoes.RET_OPERACAO_OK;
		RecargaRecorrenteVO vo = (RecargaRecorrenteVO) obj;
		
		super.log(Definicoes.INFO, "execute", "MSISDN: " + vo.getMsisdn() +
				  " Codigo Recarga: " + vo.getCodRecarga() + " Tipo Recarga: " +
				  vo.getTipoRecarga() + " Valor da Recarga: " + vo.getValorRecarga());
		
		try
		{
			ConsultaAssinante consultaAssinante = new ConsultaAssinante(super.getIdLog());
			
			// Efetua a consulta dos dados do assinante recebido como parametro
			Assinante dadosAssinante = consultaAssinante.consultarAssinantePlataforma(vo.getMsisdn());
			
			// Valida Assinante e Valor Recarga
			codRetorno = validaAssinante(dadosAssinante, vo.getValorRecarga(), vo.getCodRecarga(), vo.getTipoRecarga());
			
			if (codRetorno == Definicoes.RET_OPERACAO_OK)
				codRetorno = avaliaEfetuaRecargaRecorrente(vo, dadosAssinante);
		}
		catch (Exception e)
		{
			codRetorno = Definicoes.RET_ERRO_TECNICO;
			super.log(Definicoes.ERRO, "execute", "Erro tecnico na recarga recorrente: " + e);
		}
		finally
		{
			// atualiza tabela do vitria com status da Recarga
			String sql =  "UPDATE TBL_INT_RECARGA_RECORRENTE 	" +
						  "   SET IDT_STATUS_PROCESSAMENTO = ?  " +
						  "      ,COD_RETORNO 			   = ?  " +
						  "      ,DES_RETORNO 			   = ?  " +
						  "      ,COD_FATURAMENTO 		   = ?  " +
						  "		 ,VLR_BONUS_ONNET		   = ?  " +
						  "		 ,VLR_BONUS_OFFNET		   = ?  " +
						  "		 ,VLR_BONUS_RECARGA		   = ?  " +
						  " WHERE IDT_MSISDN_PRE 		   = ? 	" +
						  "   AND TIP_ENVIO 			   = ?  " +
						  "   AND DAT_RECARGA 			   = ?  " +
						  "   AND DAT_PROCESSAMENTO 	   = ?	" ;
			
			// Se o processo rodar direito, devemos colocar 'R' na tabela.
			// O ETI coloca 'P' quando ler esse registro novamente
			Object param[] = {	codRetorno == 	Definicoes.RET_OPERACAO_OK ?
												Definicoes.IDT_REC_RECORRENTE_OK :
												Definicoes.IDT_REC_RECORRENTE_ERRO,
								new Short(codRetorno),
								this.getDescMotivo(codRetorno),
								vo.getCodFaturamento().toString(),
								new Double(vo.getValorBonusOnNet()),
								new Double(vo.getValorBonusOffNet()),
								new Double(vo.getValorBonusRecarga()),
								vo.getMsisdn(),
								vo.getTipoRecarga(),
								vo.getDataRecarga(),
								vo.getDataSolicitacao()};
			
			conexaoPrep.executaPreparedUpdate(sql, param, super.getIdLog());
		}
	}
	
	/**
	 * Avalia o plano do assinante com o tipo de recarga solicitada para realização de recarga ou ajuste.
	 * 
	 * @param  vo 	 - Instancia de <code>RecargaRecorrenteVO</code>
	 * @return short - 0 se recarga efetuada, outro valor se recarga não efetuada
	 * @throws Exception 
	 * @throws ParseException
	 */
	private short avaliaEfetuaRecargaRecorrente(RecargaRecorrenteVO vo, Assinante dadosAssinante) throws Exception
	{
		short result = Definicoes.RET_OPERACAO_OK;
		
		// se a recarga for programada (P), a mesma deve ser executada
		// independente do plano do assinante
		if (vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_PROGRAMADA))
			result = processaRecargaProgramada(vo, dadosAssinante);
        else
		{
			// Caso o assinante seja híbrido, se a recarga for do tipo
			// FRANQUIA (F);
			// Realizar uma recarga para o assinante.
			if (vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_FRANQUIA))
				result = efetuaRecarga(vo, dadosAssinante, Definicoes.RECARGA_FRANQUIA);
			
			// Caso o assinante seja híbrido, se a recarga for do tipo BONUS (B)
			// Deve ser realizado um AJUSTE para o assinante.
			else if (vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_BONUS))
				result = efetuaBonusSafraAntiga(vo, dadosAssinante);
			
			// Caso o assinante seja híbrido, se a recarga for do tipo FRANQUIA-BONUS (D)
			// Deve ser realizado uma RECARGA e um BÔNUS para o assinante.
			else if (vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_FRANQUIA_BONUS) ||
				     vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_BONUS_SMS) ||
				     vo.getTipoRecarga().equals(Definicoes.TIPO_REC_RECORRENTE_MAES))
			{
				result = efetuaBonus(vo, dadosAssinante);
				if(result == Definicoes.RET_OPERACAO_OK)
					result = efetuaRecarga(vo, dadosAssinante, Definicoes.RECARGA_FRANQUIA);
			}
			
			// Verifica se o assinante eh elegivel ao Bonus Fale Mais para
			// realizacao da respectiva bonificacao
			if (vo.getCodFaturamento().isBonusFaleMais())
				efetuaAjusteBonusFaleMais(vo, dadosAssinante);
			
			// Atualiza o status do assinante híbrido
			if (result == Definicoes.RET_OPERACAO_OK)
				atualizaCicloControle(vo, dadosAssinante);
		}
		
		return result;
	}
	
	/**
	 * Metodo para atualizar o Ciclo da concessao da franquia
	 * para os assinantes controle, uma vez que todas as acoes
	 * retornaram sucesso
	 * 
	 * @param vo - Instancia de <code>RecargaRecorrenteVO</code>
	 * @param dadosAssinante - Instancia de <code>Assinante</code>
	 */
	private void atualizaCicloControle(RecargaRecorrenteVO vo, Assinante dadosAssinante)
	{
		CicloPlanoHibrido ciclo = new CicloPlanoHibrido(super.getIdLog());
		
		try
		{
			ciclo.atualizaCicloPlanoHibrido(dadosAssinante,
											vo.getValorRecarga(),
											vo.getCodRecarga(),
											conexaoPrep,
											vo.getDataRecarga());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.WARN, "atualizaCicloControle", "Falha na atualizacao do Ciclo do assinante controle:" + e);
		}
	}
	
	/**
	 * Este metodo foi alterado de tal forma que nao impacte na classe EnvioSMSAtrasoRecargasConsumidor
	 * que chama esse metodo
	 * 
	 * @param  dadosAssinante	- Instancia de <code>Assinante</code>
	 * @param  valorRecarga		- Valor da recarga
	 * @param  codRecarga		- Codigo da recarga
	 * @return
	 */
	public short validaAssinante(Assinante dadosAssinante, double valorRecarga, int codRecarga)
	{
		short retorno = validaAssinante(dadosAssinante, valorRecarga, codRecarga, null);
		
		return retorno;
	}
	
	/**
	 * Esta classe foi criada com o intuito de atender as chamadas a este metodo
	 * que necessitam passar o tipoRecarga como parametro
	 * 
	 * Chama a funcao de consulta pre-recarga
	 * 
	 * @param Assinante	dadosAssinante	- Dados do Assinante
	 * @param double	idRecarga 		- Valor da Recarga
	 * @param int		codRecarga 		- Código da recarga (Número do mês de recarga)
	 * @return short 					- identificacao do codigo sucesso ou erro
	 * @throws GPPInternalErrorException
	 */
	public short validaAssinante(Assinante dadosAssinante, double idRecarga, int codRecarga, String tipoRecarga)
	{
		super.log(Definicoes.DEBUG, "validaAssinante", "Inicio da validacao");
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		try
		{
			// Se o assinante existe
			if((dadosAssinante != null) && (dadosAssinante.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO))
			{
				// valida status do assinante
				if (dadosAssinante.getStatusAssinante() == Definicoes.SHUT_DOWN)
					retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
				// valida status do servico
				else if (dadosAssinante.getStatusServico() == Definicoes.SERVICO_BLOQUEADO)
					retorno = Definicoes.RET_MSISDN_BLOQUEADO;
				// valida se o assinante é híbrido
				else if (!dadosAssinante.isHibrido() && tipoRecarga != null && !(tipoRecarga.equals(Definicoes.TIPO_REC_RECORRENTE_PROGRAMADA)))
						retorno = Definicoes.RET_FRANQUIA_PARA_NAO_HIBRIDO;
				else
				{
					//Obtendo os valores de recarga a partir do identificador da recarga, da categoria do plano do 
					//assinante e da data de execucao.
					Date dataExecucao = Calendar.getInstance().getTime();
					Categoria categoria = MapPlanoPreco.getInstance().getPlanoPreco(dadosAssinante.getPlanoPreco()).getCategoria();
					ValoresRecarga valores = MapValoresRecarga.getInstance().getValoresRecarga(idRecarga, categoria, dataExecucao);
					
					if(valores != null)
					{
						CicloPlanoHibrido ciclo = new CicloPlanoHibrido(super.getIdLog());
						// Seleciona valor do saldo principal e maximo de saldo
						double recargaPrincipal = valores.getSaldoPrincipal();
						short saldoMaximo = Short.parseShort(mapConfig.getMapValorConfiguracaoGPP("SALDO_MAXIMO"));
						
						// valida valor do saldo maximo
						if ((recargaPrincipal + dadosAssinante.getValoresRecarga().getSaldoPrincipal()) > saldoMaximo)
							retorno = Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO;
						// valida se a recarga já foi efetuada nesse mês
						else if (ciclo.cicloExecutado(dadosAssinante.getMSISDN(), codRecarga) && tipoRecarga != null && !(tipoRecarga.equals(Definicoes.TIPO_REC_RECORRENTE_PROGRAMADA)))
							retorno = Definicoes.RET_CICLO_PLANO_HIBRIDO_JA_RODADO;
					}
					else
						retorno = Definicoes.RET_VALOR_CREDITO_INVALIDO;
				}
			}
			else
			{
				super.log(Definicoes.WARN, "validaAssinante", "Assinante NAO existe na plataforma da Tecnomen.");
				retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
			}
		}
		catch (GPPInternalErrorException e)
		{
			retorno = Definicoes.RET_ERRO_TECNICO;
			super.log(Definicoes.ERRO, "validaAssinante", "Excecao:"	+ e);
		}
		
		return retorno;
	}
	
	/**
	 * Chama a funcao de recarga para creditar o valor de recarga no MSISDN
	 * 
	 * @param  vo 	 - Instanciao de <code>RecargaRecorrenteVO</code>
	 * @return short - 0 se recarga efetuada, outro valor se recarga não efetuada
	 * @throws GPPTecnomenException
	 * @throws GPPInternalErrorException
	 */
	private short efetuaRecarga(RecargaRecorrenteVO vo, Assinante assinante, String tipoTransacao) throws Exception
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		String identificacaoRecarga = "0";
		String tipoCredito 			= Definicoes.TIPO_CREDITO_REAIS;
		String sistemaOrigem 		= Definicoes.SO_GNV;
		String operador 			= Definicoes.GPP_OPERADOR;
		
		// Seleciona um identificador para p registro de recarga
		identificacaoRecarga = RecargaDAO.newIdRecarga(super.logId);
		super.log(Definicoes.DEBUG, "efetuaRecarga", "Identificao Recarga: "	+ identificacaoRecarga);
		
		// Chama a funcao de recarga
		Recarregar recarregar = new Recarregar(super.logId);
		retorno = recarregar.executarRecarga(vo.getMsisdn(), 
											 tipoTransacao,
											 identificacaoRecarga, 
											 tipoCredito, 
											 vo.getValorRecarga(),
											 vo.getDataSolicitacao(),
											 sistemaOrigem,
											 operador,
											 vo.getNsuInstituicao(),
											 null,
											 null,
											 null,
											 null,
											 null,
                                             null);
		
		super.log(Definicoes.DEBUG, "efetuaRecarga", "Resposta Recarga: " + retorno);
		
		// Para os clientes do controle antigo o saldo é expirado em 60 dias e não em 30. Para isto e necessario 
		// utilizar diretamente a conexao de aprovisionamento, informando somente a data de expiracao do saldo principal.
		if((retorno == Definicoes.RET_OPERACAO_OK) && (vo.getTipoControle() == Definicoes.IND_CONTROLE_ANTIGO))
			this.atualizarDataExpiracao(assinante);
		
		return retorno;
	}
	
	/**
	 * Chama a API de concessão do bônus pula-pula
	 * 
	 * @param  vo	 		  - Instancia de <code>RecargaRecorrenteVO</code>
	 * @param  dadosAssinante - Instancia de <code>Assinante</code>
	 * @return retorno		  - 0 se o bônus foi efetuado com sucesso
	 * @throws Exception 
	 */
	private short efetuaBonus(RecargaRecorrenteVO vo, Assinante dadosAssinante) throws Exception
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		// Parâmetros da consulta
		int[] detalhes = {ControlePulaPula.TOTALIZACAO,
						  ControlePulaPula.BONUS_PULA_PULA,
						  ControlePulaPula.BONUS_CONCEDIDOS_PERIODO,
						  ControlePulaPula.SALDO_PULA_PULA};
		
		// Consulta os dados para concessão do bônus pula-pula
		AssinantePulaPula assinante = controle.consultaPromocaoPulaPula(dadosAssinante.getMSISDN(),
																		detalhes,
																		vo.getDataRecarga(),
																		dadosAssinante,
																		false,
																		conexaoPrep);
		
		if(assinante == null)
		{
			// Metodo para insercao do assinante na promocao (GPP) de acordo
			// com a promocao informada no SAG (D ou E)
			controle.inserePromocaoAssinante(dadosAssinante.getMSISDN(),
											 MapPromocao.getInstancia().getIdPromocaoByPromocaoSag(vo.getTipoRecarga()), 
											 new Date(), Definicoes.GPP_OPERADOR, 
											 Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO, conexaoPrep);
			
			assinante = controle.consultaPromocaoPulaPula(dadosAssinante.getMSISDN(),
														  detalhes,
														  dadosAssinante,
														  false,
														  conexaoPrep);
		}
		
		retorno = efetuaPacoteBonus(assinante, vo.getNsuInstituicao());
		
		if (retorno == Definicoes.RET_OPERACAO_OK)
		{
			// Alteracao para contemplar a promocao do dia das maes
			for (Iterator i = assinante.getTiposTransacao(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT).iterator(); i.hasNext(); )
			{
				// Obtendo o tipo de transacao
				PromocaoTipoTransacao tipoTransacao = (PromocaoTipoTransacao)i.next();
				BonificacaoPulaPula bonificacao = assinante.getSaldo().getBonificacao(tipoTransacao.getTipoBonificacao());
				
				Collection listaBonusConcedido = assinante.getBonusConcedidos(tipoTransacao.getOrigem().getTipoTransacao());
				if (bonificacao != null)
				{
					if (listaBonusConcedido != null && listaBonusConcedido.size() > 0)
						for (Iterator j = listaBonusConcedido.iterator(); j.hasNext(); )
						{
							Recarga recarga = (Recarga)j.next();
							vo.somaValorBonusByTipoBonificacao(bonificacao, recarga.getVlrPago().doubleValue());
						}
					else
					{
						// Obtendo a data da bonificacao
						Date dataBonificacao = Calendar.getInstance().getTime();
						
						if (bonificacao != null && bonificacao.getValorAReceber() > 0.0)
						{
							ValoresRecarga valoresRecarga = new ValoresRecarga();
							valoresRecarga.setValoresRecarga(bonificacao);
							
							// POG para fazer com que a expiracao do Saldo Periodico seja alterado
							// pois a Tecnomen nao realiza essa atualizacao da data de expiracao
							// para o Saldo periodico
							if (bonificacao.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.PERIODICO)
								valoresRecarga.setNumDiasExpiracaoPeriodico(Integer.parseInt(
										mapConfig.getMapValorConfiguracaoGPP("PULA_PULA_NUM_DIAS_EXP_PERIODICO")));
							
							retorno = controle.executaBonus(assinante.getAssinante(),
														    tipoTransacao.getOrigem().getTipoTransacao(),
														    valoresRecarga,
														    dataBonificacao,
														    vo.getNsuInstituicao());
							
							// Apos verificado que a bonificacao retornou sucesso,
							// a bonificacao eh adicionada ao VO para retorno dos
							// valores ao SAG
							if (retorno == Definicoes.RET_OPERACAO_OK)
								vo.somaValorBonusByTipoBonificacao(bonificacao, bonificacao.getValorAReceber());
							else
								break;
						}
					}
				}
			}
		}
		
		return retorno;
	}
	
	/**
	 * Chama a funcao de ajuste do GPP
	 * 
	 * @param  vo
	 * @param  dadosAssinante
	 * @return retorno
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	private short efetuaBonusSafraAntiga(RecargaRecorrenteVO vo, Assinante dadosAssinante) throws GPPInternalErrorException, GPPTecnomenException
	{
		// Mapeia valores de recarga e seta as datas de expiração
		Categoria categoria    = MapPlanoPreco.getInstance().getPlanoPreco(dadosAssinante.getPlanoPreco()).getCategoria();
		Date dataExecucao 	   = Calendar.getInstance().getTime();
		ValoresRecarga valores = MapValoresRecarga.getInstance().getValoresRecarga(vo.getValorRecarga(), categoria, dataExecucao);
		
		valores.setDataExpPrincipal(dadosAssinante.newDataExpiracao(TipoSaldo.PRINCIPAL, valores.getNumDiasExpPrincipal()));
		valores.setDataExpBonus    (dadosAssinante.newDataExpiracao(TipoSaldo.BONUS    , valores.getNumDiasExpBonus    ()));
		valores.setDataExpSMS      (dadosAssinante.newDataExpiracao(TipoSaldo.TORPEDOS , valores.getNumDiasExpSMS      ()));
		valores.setDataExpGPRS     (dadosAssinante.newDataExpiracao(TipoSaldo.DADOS    , valores.getNumDiasExpGPRS     ()));
		
		//Cria valoresAjuste para realizar ajuste bonus
		ValoresRecarga valoresAjuste = new ValoresRecarga(valores.getSaldoPrincipal(),
														  valores.getSaldoPeriodico(),
														  valores.getSaldoBonus(), 
														  valores.getSaldoSMS(), 
														  valores.getSaldoGPRS(),
														  valores.getDataExpPrincipal(),
														  valores.getDataExpPeriodico(),
														  valores.getDataExpBonus(),
														  valores.getDataExpSMS(),
														  valores.getDataExpGPRS());
		
		// Executa o ajuste da franquia como um bonus (pula-pula) nao sendo trocada a data de expiracao
		Ajustar ajustar = new Ajustar(super.getIdLog());
		return ajustar.executarAjuste(vo.getMsisdn(),
						              Definicoes.RECARGA_FRANQUIA_BONUS,
									  Definicoes.TIPO_CREDITO_REAIS,
									  valoresAjuste,
									  Definicoes.TIPO_AJUSTE_CREDITO,
									  dataExecucao,
									  Definicoes.SO_GNV,
									  Definicoes.GPP_OPERADOR,
									  dadosAssinante,
									  "",
									  true,
                                      vo.getNsuInstituicao());
	}
	
	/**
	 * Realiza a iteracao nos Pacotes de Bonus para a bonificacao
	 * dos bonus referentes ao Assinante
	 * 
	 * @param  pAssinante	- Instancia de <code>AssinantePulaPula</code>
	 * @return retorno		- Retorno das operacoes
	 */
	private short efetuaPacoteBonus(AssinantePulaPula pAssinante, String nsuInstituicao)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		
		Collection listaPromoPctBonus = pAssinante.getSaldo().getListaPromocoesPacoteBonus();
		
		// Realiza uma iteracao nos Pacotes de Bonus inerentes a promocao do assinante
		// e efetua o bonus para cada Tipo de Saldo existente
		for (Iterator i = getValoresRecarga(listaPromoPctBonus).entrySet().iterator(); i.hasNext(); )
		{
			Map.Entry entry = (Map.Entry)i.next();
			String tipoTransacao = (String)entry.getKey();
			ValoresRecarga valorRecarga = (ValoresRecarga)entry.getValue();
			
			retorno = controle.executaBonus(pAssinante.getAssinante(),
											tipoTransacao,
											valorRecarga,
											Calendar.getInstance().getTime(),
                                            nsuInstituicao);
			
			if (retorno != Definicoes.RET_OPERACAO_OK)
				break;
		}
		
		return retorno;
	}
	
	/**
	 * Realiza uma iteracao na lista das PromocoesPacoteBonus para montar
	 * entidades ValoresRecarga para bonificacao extra
	 * 
	 * @param 	listaPromocoesPacoteBonus	- lista das promocoesPacoteBonus
	 * @return	mapValoresTipoTransacao		- mapeamento contendo as entidades ValoresRecarga
	 */
	private Map getValoresRecarga(Collection listaPromocoesPacoteBonus)
	{
		Map mapValoresTipoTransacao = new HashMap();
		
		for (Iterator i = listaPromocoesPacoteBonus.iterator(); i.hasNext(); )
		{
			PromocaoPacoteBonus promo = (PromocaoPacoteBonus)i.next();
			
			ValoresRecarga valorRecarga = (ValoresRecarga)mapValoresTipoTransacao.get(promo.getTipoTransacao());
			
			if (valorRecarga == null)
				valorRecarga = new ValoresRecarga();
			
			valorRecarga.setSaldoPrincipal(0.0);
        	valorRecarga.setSaldoBonus(promo.getTipoSaldo().getIdtTipoSaldo() == TipoSaldo.BONUS 	? promo.getValorBonus() : 0.0);
        	valorRecarga.setSaldoSMS(promo.getTipoSaldo().getIdtTipoSaldo()   == TipoSaldo.TORPEDOS ? promo.getValorBonus() : 0.0);
        	valorRecarga.setSaldoGPRS(promo.getTipoSaldo().getIdtTipoSaldo()  == TipoSaldo.DADOS 	? promo.getValorBonus() : 0.0);
        	
        	mapValoresTipoTransacao.put(promo.getTipoTransacao(), valorRecarga);
		}
		
		return mapValoresTipoTransacao;
	}
	
	/**
	 * Verifica qual a msg relacionada ao codigo do motivo
	 * 
	 * @param short CodMotivo Código do Motivo
	 * @return String msg do motivo
	 * @throws GPPInternalErrorException
	 */
	private String getDescMotivo(short CodMotivo) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "getCodigoMotivo", "Inicio CODMOTIVO "
				+ CodMotivo);
		String retorno = "";
		ResultSet rs_msg;
		try
		{
			// Faz a pesquisa no banco para saber o valor da msg de retorno
			String sql_msg 	= "SELECT DES_RETORNO " 
							+ "FROM TBL_GER_CODIGOS_RETORNO "
							+ " WHERE VLR_RETORNO = ? ";
			Object paramMsg[] =
			{ new Short(CodMotivo) };
			rs_msg = conexaoPrep.executaPreparedQuery(sql_msg, paramMsg, super.getIdLog());
			
			if (rs_msg.next())
			{
				// variavel da mensagem de retorno
				String msgRetorno = rs_msg.getString(1);
				retorno = msgRetorno;
			}
			rs_msg.close();
		}
		catch (SQLException e)
		{
			super.log(Definicoes.WARN, "getCodMotivo", "Excecao de SQL:"	+ e);
			throw new GPPInternalErrorException(e.toString());
		}
		
		super.log(Definicoes.DEBUG, "getCodigoMotivo", "Fim RETORNO:" + retorno);
		return retorno;
	}
	
	/**
	 * Processa a recarga programada para o assinante Pre-Pago
	 * 
	 * @param  vo				  - Instancia de <code>RecargaRecorrenteVO</code>
	 * @param  dadosAssinante     - Instancia de <code>Assinante</code>
	 * @return short			  - 0 se a recarga foi processada
	 * @throws Exception
	 */
	private short processaRecargaProgramada(RecargaRecorrenteVO vo, Assinante dadosAssinante) throws Exception
	{
		short result = Definicoes.RET_CICLO_PLANO_HIBRIDO_JA_RODADO;
		int codRecargaAtual = findRecargaRecorrente(vo.getMsisdn())+1;
		
		if (codRecargaAtual == vo.getCodRecarga())
		{
			result = efetuaRecarga(vo, dadosAssinante, Definicoes.RECARGA_PROGRAMADA);
			
			if (result == Definicoes.RET_OPERACAO_OK)
				updateRecargaRecorrente(vo);
		}
		
		return result;
	}
	
	/**
	 * Metodo utilizado para atualizar o identificador da recarga
	 * para o assinante Pre-Pago da Recarga Programada
	 * 
	 * @param  msisdn	  - Assinante que recebeu a recarga
	 * @param  codRecarga - Codigo da recarga atual
	 */
	private void updateRecargaRecorrente(RecargaRecorrenteVO vo)
	{

		String sql =  "UPDATE tbl_apr_recarga_programada " +
					  "   SET tarp_cod_recarga = ? 		 " +
					  " WHERE tarp_idt_msisdn  = ? 		 " ;
		
		Object param[] = {	new Integer(vo.getCodRecarga()),
							vo.getMsisdn()};
		
		try
		{
			if (conexaoPrep.executaPreparedUpdate(sql, param, super.getIdLog()) == 0)
				gravaRecargaRecorrente(vo);
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "updateRecargaRecorrente", "Problemas ao realizar o update na tabela tbl_int_recarga_programada.");
		}
	}
	
	/**
	 * Metodo utilizado para consultar o codigo da ultima
	 * recarga efetuada para o assinante
	 * 
	 * @param  msisdn - Numero do assinante Pre-Pago
	 * @return int	  - Codigo da ultima recarga processada
	 */
	private int findRecargaRecorrente(String msisdn)
	{
		// @author Joao Paulo Galvagni
		// @since  14/04/2008
		// @modify Alteracao do result para 1, pois a primeira recarga do assinante
		// 		   eh realizada pelo Clarify e nao ira inserir o assinante na tabela
		int result = 1;
		ResultSet resultSet;

		String sql =  " SELECT tarp_cod_recarga				" +
					  "   FROM tbl_apr_recarga_programada 	" +
					  "  WHERE tarp_idt_msisdn = ? 			" ; 
		
		Object param[] = {msisdn};
		
		try
		{
			resultSet = conexaoPrep.executaPreparedQuery(sql,
														 param,
														 super.getIdLog());
			
			if (resultSet.next())
				result = resultSet.getInt("TARP_COD_RECARGA");
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "findRecargaRecorrente", "Problemas ao realizar o select na tabela tbl_int_recarga_programada.");
		}
		
		return result;
	}
	
	/**
	 * Metodo para gravar a primeira recarga programada para o
	 * assinante Pre-Pago
	 * 
	 * @param vo	- Instancia de <code>RecargaRecorrenteVO</code>
	 */
	private void gravaRecargaRecorrente(RecargaRecorrenteVO vo)
	{
		String sql =  "INSERT INTO tbl_apr_recarga_programada 					 " +
					  "		  (tarp_idt_msisdn, tarp_cod_recarga,				 " +
					  "		   tarp_dat_ultima_recarga, tarp_vlr_ultima_recarga) " +
					  "VALUES (?, ?, ?, ?) 										 " ;
		
		Object param[] = {	vo.getMsisdn(),
							new Integer(vo.getCodRecarga()),
							new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()),
							new Double(vo.getValorRecarga())};
		
		try
		{
			conexaoPrep.executaPreparedUpdate(sql, param, super.getIdLog());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "gravaRecargaRecorrente", "Problemas ao realizar o insert na tabela tbl_int_recarga_programada.");
		}
	}
	
	/**
	 * Metodo responsavel por efetuar a bonificacao do Bonus Fale Mais
	 * 
	 * @param vo			 - Instancia de <code>RecargaRecorrenteVO</code>
	 * @param dadosAssinante - Instancia de <code>Assinante</code>
	 * @param nsuInstituicao - NsuInstiuicao
	 */
	private void efetuaAjusteBonusFaleMais(RecargaRecorrenteVO vo, Assinante dadosAssinante) 
	{
		try
		{
			MapBonusPulaPula mapBonus = MapBonusPulaPula.getInstancia();
			BonusPulaPula bonusPulaPula = new BonusPulaPula();
			String codigoNacional = dadosAssinante.getMSISDN().substring(2, 4);
			bonusPulaPula = mapBonus.getBonusPulaPula(Integer.valueOf((codigoNacional)).intValue(), dadosAssinante.getPlanoPreco(), Calendar.getInstance().getTime());		
			
			short resultBonus;
			
			int minutos = Integer.parseInt(mapConfig.getMapValorConfiguracaoGPP("MINUTOS_BONUS_FALE_MAIS"));
			double valorPorMinuto = bonusPulaPula.getVlrBonusMinuto().doubleValue();
			double valorBonus = minutos * valorPorMinuto;
			
			ValoresRecarga valoresRecarga = new ValoresRecarga(0.0, 0.0, valorBonus, 0.0, 0.0);
			
			Ajustar ajustar = new Ajustar(super.getIdLog());
			resultBonus = ajustar.executarAjuste(vo.getMsisdn(),
												 Definicoes.RECARGA_FRANQUIA_BONUS,
												 Definicoes.TIPO_CREDITO_REAIS,
												 valoresRecarga,
												 Definicoes.TIPO_AJUSTE_CREDITO,
												 Calendar.getInstance().getTime(),
												 Definicoes.SO_GNV,
												 Definicoes.GPP_OPERADOR,
												 dadosAssinante,
												 "",
												 true,
												 vo.getNsuInstituicao());
			
			// O vlrBonusFaleMais recebera o valor do bonus apenas se o ajuste for efetuado com sucesso
			if (resultBonus == 0)
				vo.setVlrBonusFaleMais(valorBonus);
		}
		catch (Exception e)
		{
			// Se ocorrer algum erro ao efetuar o ajuste do bonus fale
			// mais, nao devera impactar nos demais ajustes efetuados
			super.log(Definicoes.ERRO, "efetuaAjusteBonusFaleMais", "Erro ao efetuar o ajuste do Bonus Fale Mais");
		}
	}
	
	/**
	 *	Atualiza a data de expiracao do Saldo Principal de assinantes do plano Controle antigo.
	 * 
	 *	@param  recarga	VO contendo dados da recarga a conceder
	 *	@throws GPPInternalErrorException 
	 */
	private void atualizarDataExpiracao(Assinante assinante)
	{
		Date	dataExpiracao	= assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, (short)Definicoes.NUM_DIAS_EXPIRACAO_FRANQUIA);
		short	retExpiracao	= Definicoes.RET_OPERACAO_OK;
		
		TecnomenAprovisionamento conexao = null;
		
		try
		{
			conexao = GerentePoolTecnomen.getInstance().getTecnomenAprovisionamento(super.logId);
			retExpiracao = conexao.atualizarStatusAssinante(assinante.getMSISDN(), 
															(short)Definicoes.STATUS_NORMAL_USER, 
															dataExpiracao,
															null,
															null,
															null,
															null);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "efetuaRecarga", "MSISDN: " + assinante.getMSISDN() + " - Excecao: " + e);
			retExpiracao = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			if(retExpiracao != Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.WARN, "efetuaRecarga", "MSISDN: " + assinante.getMSISDN() + " - Nao foi possivel atualizar a data de expiracao principal do assinante Controle antigo.");
			GerentePoolTecnomen.getInstance().liberarConexao(conexao);
		}
	}
	/*
	*//**
	 * Grava na tabela TBL_PRO_BONIFICACAO_CONTROLE as informacoes
	 * referentes a concessao efetuada do Plano Controle para que, 
	 * apos um novo envio do evento pelo SAG o GPP nao realize 
	 * uma mesma bonificacao
	 * 
	 * @param vo - Instancia de <code>BonificacaoControle</code>
	 *//*
	private void gravaBonificacaoControle(BonificacaoControle bonificacaoControle)
	{
		String sql = "INSERT INTO TBL_PRO_BONIFICACAO_CONTROLE 				 " +
					 "		(TPBC_IDT_MSISDN, TPBC_NUM_MES_EXECUCAO, 		 " +
					 "	     TPBC_ID_TIPO_BONIFICACAO, TPBC_VLR_BONIFICACAO, " +
					 "       TPBC_DAT_BONIFICACAO) 							 " +
					 "VALUES (?, ?, ?, ?, ?)								 " ;
		
		Object[] param = {bonificacaoControle.getIdtMsisdn(),
						  new Integer(bonificacaoControle.getNumMesExecucao()),
						  new Integer(bonificacaoControle.getTipoBonificacao().getIdTipoBonificacao()),
						  new Double(bonificacaoControle.getValorBonificacao()),
						  new java.sql.Timestamp(bonificacaoControle.getDataBonificacao().getTime())};
		
		try
		{
			// Atualiza o Valor recebido e a Data da bonificacao
			// para o assinante que obteve erro em alguma bonificacao
			// e/ou concesao da Franquia
			conexaoPrep.executaPreparedUpdate(sql, param, super.getIdLog());
		}
		catch (Exception e)
		{
			super.log(Definicoes.WARN, "gravaBonificacoesEfetuadas", "Erro na gravacao das bonificacoes para o assinante " + 
					  bonificacaoControle.toString());
		}
	}
	
	*//**
	 * Realiza a consulta da bonificacao ja concedida para o assinante
	 * 
	 * @param  vo					- Instancia de <code>RecaraRecorrenteVO</code>
	 * @param  bonificacao			- Instancia de <code>BonificacaoPulaPula</code>
	 * @return bonificacaoControle	- Instancia de <code>BonificacaoControle</code>
	 *//*
	private BonificacaoControle getBonificacaoControle(RecargaRecorrenteVO vo, BonificacaoPulaPula bonificacao)
	{
		BonificacaoControle bonificacaoControle = null;
		
		try
		{
			String SQL = "SELECT (TPBC_IDT_MSISDN, TPBC_NUM_MES_EXECUCAO,  	 " +
						 "		  TPBC_VLR_BONIFICACAO, TPBC_DAT_BONIFICACAO)" +
						 "  FROM TBL_PRO_BONIFICACAO_CONTROLE				 " +
						 "	WHERE TPBC_IDT_MSISDN = ? 						 " +
						 "    AND TPBC_NUM_MES_EXECUCAO = ? 				 " +
						 "			   AND TPBC_ID_TIPO_BONIFICACAO = ? 	 " ;
			
			Object[] param = {vo.getMsisdn(),
							  new Integer(vo.getCodRecarga()),
							  new Integer(bonificacao.getTipoBonificacao().getIdTipoBonificacao())};
			
			ResultSet rs = conexaoPrep.executaPreparedQuery(SQL, param, getIdLog());
			
			if (rs.next())
			{
				bonificacaoControle = new BonificacaoControle();
				
				bonificacaoControle.setIdtMsisdn(rs.getString("TPBC_IDT_MSISDN"));
				bonificacaoControle.setNumMesExecucao(rs.getInt("TPBC_NUM_MES_EXECUCAO"));
				bonificacaoControle.setTipoBonificacao(bonificacao.getTipoBonificacao());
				bonificacaoControle.setValorBonificacao(rs.getDouble("TPBC_VLR_BONIFICACAO"));
				bonificacaoControle.setDataBonificacao(rs.getDate("TPBC_DAT_BONIFICACAO"));
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.WARN, "getBonificacaoControle", "Erro na selecao da bonificacao ja concedida para o Controle " + vo.toString());;
		}
		
		return bonificacaoControle;
	}*/
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		super.log(Definicoes.INFO, "finish", "Fim dos consumidores");
	}
}