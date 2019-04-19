package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Collection;

import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorVoucher;

/**
 *	Conexao com o Admin Server da Tecnomen. Este servico e responsavel pelas operacoes de administrador na plataforma. 
 *
 *	<P> Versao:	1.0
 *	@author		Camile Cardoso Couto
 *	@since 		06/04/2004
 *                       
 *	<P> Versao:	2.0	Migracao para Tecnomen 4.4.5
 *	@author		Daniel Ferreira
 *	@since 		07/03/2007
 */
public class TecnomenAdmin extends ConexaoTecnomen
{
	
	/**
	 *	Conexao com a interface de Profile Feature.
	 */
	private TecnomenGeneralMaintainance manutencao;
	
	/**
	 *	Conexao com a interface de Profile Feature.
	 */
	private TecnomenProfileFeature gerenciadorPlanos;
	
	/**
	 *	Conexao com a interface de System Config.
	 */
	private TecnomenSystemConfig configurador;
	
	/**
	 *	Conexao com a interface de Credit Amount Map.
	 */
	private TecnomenCreditAmountMap valoresVoucher;
	
	/**
	 *	Construtor da classe.
	 */
	public TecnomenAdmin()
	{
		super(ConexaoTecnomen.ADMIN);
		
		this.manutencao			= new TecnomenGeneralMaintainance();
		this.gerenciadorPlanos	= new TecnomenProfileFeature();
		this.configurador		= new TecnomenSystemConfig();
		this.valoresVoucher		= new TecnomenCreditAmountMap();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#setEngine(Autenticador)
	 */
	protected void setEngine(Autenticador autenticador) throws GPPTecnomenException
	{
		if(!this.manutencao.isAtivo())
			this.manutencao.conectar();
		if(!this.gerenciadorPlanos.isAtivo())
			this.gerenciadorPlanos.conectar();
		if(!this.configurador.isAtivo())
			this.configurador.conectar();
		if(!this.valoresVoucher.isAtivo())
			this.valoresVoucher.conectar();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#releaseEngine()
	 */
	public void releaseEngine(Autenticador autenticador)
	{
		this.manutencao.fechar();
		this.gerenciadorPlanos.fechar();
		this.configurador.fechar();
		this.valoresVoucher.fechar();
	}

	/**
	 *	@see		com.brt.gpp.comum.conexoes.tecnomen.ConexaoTecnomen#isAtivo()
	 */
	public boolean isAtivo()
	{
		return (this.manutencao.isAtivo() && this.gerenciadorPlanos.isAtivo() 
				&& this.configurador.isAtivo() && this.valoresVoucher.isAtivo() );
	}
	
	/**
	 *	Consulta uma coleção de <code>ValorVoucher</code> para um determinado tipo de saldo.
	 *
	 *  Essa consulta retorna um mapeamento DE-PARA no qual podemos obter o valor atual (e data de 
	 *  expiração) a partir do tipo de saldo e da categoria de serviço do voucher.
	 *
	 *	@param		tipoSaldo		Instancia de <code>TipoSaldo</code>.
	 *	@return		Lista de valores de voucher disponibilizados na plataforma Tecnomen.
	 *	@throws		GPPTecnomenException 
	 */
	public Collection getValoresVoucher(TipoSaldo tipoSaldo) throws GPPTecnomenException
	{
		// Criando uma entide ValorVoucher fake, cujo objetivo é gerar os dados de entrada
		// para o método de consulta getCreditAmountMap. No nosso caso, apenas precisamos do
		// atributo tipoSaldo. Para os demais atributos gravamos -1 de forma que o método
		// toEntidadeTEC do Holder não gere os pares de ANYs.
		
		ValorVoucher valorVoucher = new ValorVoucher();
		valorVoucher.setDiasExpiracao(-1);
		valorVoucher.setTipoVoucher(-1);
		valorVoucher.setValorAtual(-1);
		valorVoucher.setValorFace(-1);
		valorVoucher.setTipoSaldo(tipoSaldo);
		
		return valoresVoucher.getValoresVoucher(valorVoucher);
	}
	
}