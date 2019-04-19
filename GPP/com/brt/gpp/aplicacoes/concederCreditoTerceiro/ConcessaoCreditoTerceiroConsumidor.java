package com.brt.gpp.aplicacoes.concederCreditoTerceiro;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
*
* Este arquivo refere-se a classe ConcessaoCreditoTerceiroConsumidor, 
* responsável pela concessão de créditos aos promotores e supervisores
* da BrasilTelecom  
*
* @version				1.0
* @author				Marcelo Alves Araujo
* @since	 			10/07/2006
* 
* @version				1.1
* @author				Magno Batista Corrêa
* @since	 			15/03/2007
* Acrescentado a flexibilidade para saldo de SMS, DADOS e PRINCIPAL além da criação de um novo Plano.
*/
public class ConcessaoCreditoTerceiroConsumidor extends Aplicacoes implements ProcessoBatchConsumidor 
{
	private ConsultaAssinante 	consulta;
	private Ajustar 			ajuste;
	private ControlePromocao 	controle;
	private MapConfiguracaoGPP 	configuracaoGPP;
	private PREPConexao			conexaoBanco;
	private short               diasExpiracao; 
	
	/**
	 * Construtor da classe
	 */
	public ConcessaoCreditoTerceiroConsumidor() 
	{
		super(GerentePoolLog.getInstancia(ConcessaoCreditoTerceiroConsumidor.class).getIdProcesso(Definicoes.CL_CONCESSAO_CREDITO_TERCEIRO), Definicoes.CL_CONCESSAO_CREDITO_TERCEIRO);
	}

	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor) throws GPPInternalErrorException
	{
		conexaoBanco = produtor.getConexao();
		startup();
	}

	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws GPPInternalErrorException
	{
		startup((ProcessoBatchProdutor) produtor);		
	}

	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup()
	 */
	public void startup() throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Consumidor.startup", "Inicio dos consumidores");
		ajuste = new Ajustar(super.getIdLog());
		consulta = new ConsultaAssinante(super.getIdLog());	
		configuracaoGPP = MapConfiguracaoGPP.getInstancia();
		controle = new ControlePromocao(super.getIdLog());
		// Número de dias de expiração do saldo
		diasExpiracao = Short.parseShort(configuracaoGPP.getMapValorConfiguracaoGPP("NUM_DIAS_EXPIRACAO_PROMOTOR_SUPERVISOR"));
		
	}

	/**
	 * Concede os créditos aos assinantes
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#execute(Object)
	 */
	public void execute(Object obj) 
	{
		try
		{
			// Objeto contendo o msisdn e o plano do terceiro
			ConcessaoCreditoTerceiroVO concessao = (ConcessaoCreditoTerceiroVO) obj;
			// Necessário para saber o saldo e verificar a existência do assinante
			Assinante assinante = consulta.executaConsultaSimplesAssinanteTecnomen(concessao.getMsisdn());
						
			if(assinante != null && assinante.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO)
			{
				// Retira a promoção do assinante para impedir a concessão do pula-pula
				controle.retiraPromocoesAssinante(concessao.getMsisdn(), 
												  new Timestamp(new Date().getTime()), 
												  Definicoes.GPP_OPERADOR, 
												  Definicoes.CTRL_PROMOCAO_MOTIVO_PULA_PULA_NOK, 
												  conexaoBanco);
				
				// Pegar o valor e o saldo a ser alterado
				double limite[]        = adquireLimites(concessao.getPlano());
				double limitePrincipal = limite[0];
				double limiteBonus     = limite[1];
				double limiteSMS       = limite[2];
				double limiteDados     = limite[3];
				
				ValoresRecarga creditos = new ValoresRecarga();
								
				// Calcular quanto falta a conceder em todos os saldos e ajusta o numero de dias para Expiração
				// Caso um dado limite nao exista, este valor serah zero, e considerando que um assinante nao
				// pode ter nehum saldo negativo, nao processarah um dado if caso nao esteja configurado alteracao
				// para o dado tipo de saldo.
				if(assinante.getCreditosPrincipal() < limitePrincipal)
				{
					creditos.setSaldoPrincipal(limitePrincipal - assinante.getCreditosPrincipal());
				}
				if(assinante.getCreditosBonus() < limiteBonus)
				{
					creditos.setSaldoBonus(limiteBonus - assinante.getCreditosBonus());
					creditos.setDataExpBonus(assinante.newDataExpiracao(TipoSaldo.BONUS, diasExpiracao));
				}
				if(assinante.getCreditosSms() < limiteSMS)
				{
					creditos.setSaldoSMS(limiteSMS - assinante.getCreditosSms());
					creditos.setDataExpSMS(assinante.newDataExpiracao(TipoSaldo.TORPEDOS, diasExpiracao));
				}
				if(assinante.getCreditosDados() < limiteDados)
				{
					creditos.setSaldoGPRS(limiteDados - assinante.getCreditosDados());
					creditos.setDataExpGPRS(assinante.newDataExpiracao(TipoSaldo.DADOS, diasExpiracao));
				}
				// Concede o crédito apenas se alguns dos saldos 
				if( creditos.getSaldoPrincipal() != 0.0 || creditos.getSaldoBonus() != 0.0 ||
						creditos.getSaldoSMS() != 0.0 || creditos.getSaldoGPRS() != 0.0)
				{
//					Atualiza a data de expiracao do saldo princiapal
					creditos.setDataExpPrincipal(assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, diasExpiracao));
					ajuste.executarAjuste(concessao.getMsisdn(), 
										  Definicoes.AJUSTE_CONCESSAO_TERCEIROS,
										  Definicoes.TIPO_CREDITO_REAIS,
										  creditos,
										  Definicoes.TIPO_AJUSTE_CREDITO,
										  Calendar.getInstance().getTime(),
										  Definicoes.SO_GPP,
										  Definicoes.GPP_OPERADOR,
										  assinante,
										  "ConcessaoTerceiros",
										  true,
                                          null);
				}
			}
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Erro no GPP: " + e);
		}
	}
	/**
	 * 
	 * @param plano
	 * @return double[4] - 0 Principal, 1 Bonus, 2 SMS, 3 Dados
	 */
	private double[] adquireLimites(String plano)
	{
		double limite[] = new double[4];

		if (plano.equals(Definicoes.ID_PROMOTOR))
		{
			limite[0] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_PROMOTOR_PRINCIPAL")));
			limite[1] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_PROMOTOR_BONUS")));
			limite[2] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_PROMOTOR_SMS")));
			limite[3] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_PROMOTOR_DADOS")));
		}
		else if (plano.equals(Definicoes.ID_SUPERVISOR))
		{
			limite[0] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_SUPERVISOR_PRINCIPAL")));
			limite[1] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_SUPERVISOR_BONUS")));
			limite[2] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_SUPERVISOR_SMS")));
			limite[3] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_SUPERVISOR_DADOS")));
		}
		else if (plano.equals(Definicoes.ID_MULTIPLICADOR))
		{
			limite[0] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_MULTIPLICADOR_PRINCIPAL")));
			limite[1] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_MULTIPLICADOR_BONUS")));
			limite[2] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_MULTIPLICADOR_SMS")));
			limite[3] = (Double.parseDouble(configuracaoGPP.getMapValorConfiguracaoGPP("LIMITE_CONCESSAO_MULTIPLICADOR_DADOS")));
		}
		
		return limite;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
		super.log(Definicoes.DEBUG, "Consumidor.finish", "Fim dos consumidores");		
	}
}