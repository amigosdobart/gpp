package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenPPServer;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenProvisionServer;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;

/**
 *	Conexao de Aprovisionamento do GPP. Esta classe e um delegate para todas as operacoes de aprovisionamento e 
 *	consulta na plataforma Tecnomen. Possui conexoes para dois servicos: o PP Server e o Provision Server. Para 
 *	manter a compatibilidade com as aplicacoes do GPP, esta classe foi mantida de forma que prove todos os servicos
 *	das duas conexoes, chamando o metodo apropriado de cada conexao de acordo com a operacao.
 *
 *	@author		Daniel Ferreira
 *	@since		26/02/2007
 */
public class TecnomenAprovisionamento extends ConexaoTecnomen
{
	
	/**
	 *	Conexao com o Provision Server.
	 */
	private TecnomenProvisionServer aprovisionamento; 
	
	/**
	 *	Conexao com o PP Server.
	 */
	private TecnomenPPServer operacoes;

	/**
	 *	Construtor da classe.
	 */
	public TecnomenAprovisionamento()
	{
		super(ConexaoTecnomen.APROVISIONAMENTO);
		
		this.aprovisionamento	= new TecnomenProvisionServer();
		this.operacoes			= TecnomenPPServerFactory.getInstance().newTecnomenPPServer();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if(!this.aprovisionamento.isAtivo())
			this.aprovisionamento.conectar();
		if(!this.operacoes.isAtivo())
			this.operacoes.conectar();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#releaseEngine()
	 */
	public void releaseEngine(Autenticador autenticador)
	{
		this.aprovisionamento.fechar();
		this.operacoes.fechar();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#isAtivo()
	 */
	public boolean isAtivo()
	{
		return (this.aprovisionamento.isAtivo() && this.operacoes.isAtivo()); 
	}
	
	/**
	 *	Executa a consulta de um assinante na plataforma Tecnomen. O metodo constroi o objeto Assinante e encaminha a 
	 *	ordem para a conexao com o PP Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Informacoes do assinante na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Assinante consultarAssinante(String msisdn) throws GPPTecnomenException
	{
		return this.operacoes.getAssinante(msisdn);
	}
	
	/**
	 *	Ativa o dado assinante na plataforma Tecnomen. 
	 *	O metodo encaminha a ordem para a conexao com o Provision Server.
	 * @param assinante
	 * @return
	 */
	public short ativarAssinante(Assinante assinante)
	{
		return this.aprovisionamento.ativarAssinante(assinante);
	}

	/**
	 *	Desativa o assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e encaminha a ordem para a 
	 *	conexao com o Provision Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		idProcesso				Identificador do processo requisitante. Nao utilizado porem definido para 
	 *										garantir compatibilidade com versoes antigas.
	 *	@param		ehLigMix				Flag indicando que o acesso e LigMix. Nao utilizado porem definido para  
	 *										garantir compatibilidade com versoes antigas.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short desativarAssinante(String msisdn)
	{
		return this.aprovisionamento.desativarAssinante(msisdn);
	}
	
	/**
	 *	Bloqueia o assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e encaminha a ordem para a 
	 *	conexao com o PP Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short bloquearAssinante(String msisdn)
	{
		//Construindo o objeto Assinante para bloqueio.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setStatusServico((short)Definicoes.SERVICO_BLOQUEADO);
		
		//Bloqueando o assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Desbloqueia o assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e encaminha a ordem para 
	 *	a conexao com o PP Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@return		Codigo de retorno da operacao.
	 */
	public short desbloquearAssinante(String msisdn)
	{
		//Construindo o objeto Assinante para desbloqueio.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setStatusServico((short)Definicoes.SERVICO_DESBLOQUEADO);
		
		//Desbloqueando o assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Troca o plano de preco do assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e encaminha a 
	 *	ordem para a conexao com o PP Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		plano					Novo plano de preco.
	 *	@return 	Codigo de retorno da operacao.
	 */
	public short trocarPlanoPrecoAssinante(String msisdn, short plano)
	{
		//Construindo o objeto Assinante para a troca de plano.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setPlanoPreco(plano);
		
		//Trocando o plano de preco do assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Troca o SimCard (IMSI) de um assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e 
	 *	encaminha a ordem para a conexao com o PP Server.
	 * 
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		imsi					IMSI do assinante.
	 *	@return 	Codigo de retorno da operacao.
	 */
	public short trocarSimCardAssinante(String msisdn, String imsi)
	{
		//Construindo o objeto Assinante para a troca de IMSI.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setIMSI(imsi);
		
		//Trocando o IMSI do assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Atualiza a lista de Family and Friends do assinante. O metodo constroi o objeto Assinante e 
	 *	encaminha a ordem para a conexao com o PP Server.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		listaFF					Lista de Family and Friends do assinante.
	 *	@return 	Codigo de retorno da operacao. 
	 */
	public short atualizarFFAssinante(String msisdn, String listaFF)
	{
		//Construindo o objeto Assinante para a atualizacao da lista de Family and Friends.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setFriendFamily(listaFF);
		
		//Atualizando a lista de Family and Friends do assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Troca a senha (PIN) de um assinante da plataforma Tecnomen. O metodo constroi o objeto Assinante e 
	 *	encaminha a ordem para a conexao com o PP Server.
	 * 
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		senha					Nova senha (PIN) do assinante.
	 *	@return 	Codigo de retorno da operacao.
	 */
	public short trocarSenhaAssinante(String msisdn, String senha)
	{
		//Construindo o objeto Assinante para a troca de senha.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setSenha(senha);
		
		//Atualizando a senha o assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}

	/**
	 *	Atualiza o status do assinante na plataforma Tecnomen. Este metodo assume a operacao como OK caso a 
	 *	atualizacao do status e das datas de expiracao do Saldo Principal e de Bonus tenha resultado OK. Apos esta 
	 *	operacao e necessario atualizar os Saldos de Torpedos e Dados atraves do metodo especifico da API do PP Server. 
	 *	Porem falhas na chamada desta operacao nao serao consideradas para o resultado global da operacao. 
	 *	OBS: Como as operacoes das API's da Tecnomen nao sao atomicas nem transacionais, nao ha como realizar um 
	 *	"rollback" da operacao. 
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		status					Novo status do assinante. Caso seja menor que 1, nao sera atualizado.
	 *	@param 		dataExpPrincipal		Nova data de expiracao do Saldo Principal. Caso NULL, nao sera atualizada.
	 *	@param 		dataExpPeriodico		Nova data de expiracao do Saldo Periodico. Caso NULL, nao sera atualizada.
	 *	@param 		dataExpBonus			Nova data de expiracao do Saldo de Bonus. Caso seja NULL, nao sera atualizada.
	 *	@param		dataExpTorpedos			Nova data de expiracao do Saldo de Torpedos. Caso seja NULL, nao sera atualizada.
	 *	@param		dataExpDados			Nova data de expiracao do Saldo de Dados. Caso seja NULL, nao sera atualizada.
	 *	@return 	Codigo de retorno da operacao. 
	 */
	public short atualizarStatusAssinante(String	msisdn, 
										  short		status, 
										  Date		dataExpPrincipal, 
										  Date		dataExpPeriodico, 
										  Date		dataExpBonus, 
										  Date		dataExpTorpedos, 
										  Date		dataExpDados)
	{
		short result = Definicoes.RET_MSISDN_NAO_ATIVO;
		
		//Construindo o objeto Assinante para a atualizacao de status.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setStatusAssinante(status);
		assinante.setDataExpiracaoPrincipal(dataExpPrincipal);
		assinante.setDataExpiracaoPeriodico(dataExpPeriodico);
		assinante.setDataExpiracaoBonus(dataExpBonus);
		
		//Atualizando o status e as datas de expiracao do Saldo Principal e Saldo de Bonus.
		result = this.operacoes.atualizarAssinante(assinante);
		
		if(result == Definicoes.RET_OPERACAO_OK)
		{
			//Atualizando a data de expiracao do Saldo de Torpedos, caso aplicavel.
			if(dataExpTorpedos != null)
			{
				//Obtendo o tipo de saldo referente ao Saldo de Torpedos.
				TipoSaldo tipo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.TORPEDOS);
				//Atualizando as informacoes referentes ao Saldo de Torpedos.
				SaldoAssinante saldo = new SaldoAssinante(msisdn, tipo, 0.0, dataExpTorpedos);
				this.operacoes.atualizarSaldoAssinante(saldo);
			}
			
			//Atualizando a data de expiracao do Saldo de Dados, caso aplicavel.
			if(dataExpDados != null)
			{
				//Obtendo o tipo de saldo referente ao Saldo de Dados.
				TipoSaldo tipo = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.DADOS);
				//Atualizando as informacoes referentes ao Saldo de Torpedos.
				SaldoAssinante saldo = new SaldoAssinante(msisdn, tipo, 0.0, dataExpDados);
				this.operacoes.atualizarSaldoAssinante(saldo);
			}
		}
		
		return result;
	}
	
	/**
	 *	Atualiza o status e data de expiracao do Saldo Periodico do assinante na plataforma Tecnomen. 
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		status					Novo status periodico do assinante. Caso seja menor que 1, nao sera atualizado.
	 *	@param 		dataExpiracao			Nova data de expiracao do Saldo Periodico. Caso NULL, nao sera atualizada.
	 *	@return 	Codigo de retorno da operacao. 
	 */
	public short atualizarStatusPeriodico(String msisdn, short status, Date dataExpiracao)
	{
		//Construindo o objeto Assinante para a atualizacao de status.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setStatusPeriodico(status);
		assinante.setDataExpiracaoPeriodico(dataExpiracao);
		
		//Atualizando o status e as datas de expiracao do Saldo Principal e Saldo de Bonus.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
	/**
	 *	Atualiza o valor do credito inicial de ativacao de assinantes em status First Time User.
	 *	
	 *	OBS: O metodo tem como premissa a validacao previa do assinante e do seu status. 
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		creditoInicial			Valor do credito inicial de ativacao no Saldo Principal.
	 *	@return 	Codigo de retorno da operacao. 
	 */
	public short atualizarCreditoInicialAtivacao(String msisdn, double creditoInicial)
	{
		//Construindo o objeto Assinante para a atualizacao do credito inicial.
		Assinante assinante = new Assinante();
		
		assinante.setMSISDN(msisdn);
		assinante.setSaldoCreditosPrincipal(creditoInicial);
		
		//Atualizando o credito inicial de ativacao do assinante.
		return this.operacoes.atualizarAssinante(assinante);
	}
	
}