package com.brt.gpp.comum.conexoes.tecnomen;

import java.util.Calendar;

import org.omg.CORBA.ORB;

import TINC.eAdminInterface;
import TINC.eIdlGateway;
import TINC.eInitalPaymentInterface;
import TINC.eNone;
import TINC.ePPInterface;
import TINC.eProvisioningInterface;
import TINC.eVoucher;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.Conexao;
import com.brt.gpp.comum.conexoes.DadosConexao;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.DadosConexaoTecnomen;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.gerentesPool.GerenteAutenticadorTecnomen;
import com.brt.gpp.gerentesPool.GerenteORB;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe abstrata para implementacao de conexoes com a Tecnomen.
 * 
 *	@author		Daniel Ferreira
 *	@since		23/02/2007
 */
public abstract class ConexaoTecnomen extends Conexao 
{

	/**
	 *	Constante de conexoes de Admin.
	 */
	public static final int ADMIN = eAdminInterface.value;

	/**
	 *	Constante de conexoes de Agent. Nao utilizado.
	 */
	public static final int AGENT = eNone.value;

	/**
	 *	Constante de conexoes de Aprovisionamento.
	 */
	public static final int APROVISIONAMENTO = eIdlGateway.value;

	/**
	 *	Constante de conexoes com o Provision Server da Tecnomen.
	 */
	public static final int PROVISION = eProvisioningInterface.value;

	/**
	 *	Constante de conexoes com o PP Server da Tecnomen.
	 */
	public static final int PP_SERVER = ePPInterface.value;

	/**
	 *	Constante de conexoes de Recarga.
	 */
	public static final int RECARGA = eInitalPaymentInterface.value;

	/**
	 *	Constante de conexoes de Voucher.
	 */
	public static final int VOUCHER = eVoucher.value;

	/**
	 *	Identificador do servico da Tecnomen. Exemplos de servico sao o Provision Server, PP Server, Payment Engine,
	 *	Voucher Management. Cada um destes servicos possui um identificador unico.
	 */
	protected int idServico;
	
	/**
	 *	Dados da conexao com o servico da Tecnomen. Contem informacoes referentes ao status da conexao.
	 */
	private DadosConexaoTecnomen dadosConexao;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idServico				Identificador do servico da Tecnomen
	 */
	protected ConexaoTecnomen(int idServico)
	{
		this.idServico = idServico;
		
		this.desalocarConexao();
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#alocarConexao(long)
	 */
	public void alocarConexao(long idProcesso)
	{
		this.dadosConexao = new DadosConexaoTecnomen(idProcesso, Calendar.getInstance().getTime(), this.idServico);
	}
	
	/**
	 *	Estabelece a conexao com o servidor da Tecnomen.
	 *
	 *	@throws		GPPTecnomenException
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#conectar()
	 */
	public void conectar() throws GPPTecnomenException
	{
		this.setEngine(GerenteAutenticadorTecnomen.getInstance().getAutenticador(this.idServico));
		this.log(Definicoes.DEBUG, "conectar", "Conexao ID Servico Tecnomen: " + this.idServico + " - Estabelecida com sucesso");
	}
	
	/**
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#desalocarConexao()
	 */
	public void desalocarConexao()
	{
		this.dadosConexao = new DadosConexaoTecnomen(this.idServico);
	}
	
	/**
	 *	Fecha a conexao com o servidor da Tecnomen.
	 *	
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#fechar()
	 */
	public void fechar()
	{
		this.releaseEngine(GerenteAutenticadorTecnomen.getInstance().getAutenticador(this.idServico));
	}
	
	/**
	 *	Retorna os dados da conexao com o servico da Tecnomen.
	 *
	 *	@return		Dados da conexao com o servico da Tecnomen.
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#getDadosConexao()
	 */
	public DadosConexao getDadosConexao()
	{
		return this.dadosConexao;
	}
	
	/**
	 *	Retorna a referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 *
	 *	@return		Referencia ao servico da Tecnomen para estabelecimento de conexoes (Object Request Broker).
	 */
	protected ORB getOrb()
	{
		return GerenteORB.getInstance().getOrb();
	}
	
	/**
	 *	Indica se a conexao encontra-se ativa.
	 *
	 *	@return		True se a conexao encontra-se ativa e false caso contrario.
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#isAtivo()
	 */
	public abstract boolean isAtivo();
	
	/**
	 *	Reestabelece a conexao com o servidor da Tecnomen.
	 *
	 *	@throws		GPPTecnomenException
	 */
	public void reconectar() throws GPPTecnomenException
	{
		this.fechar();
		this.conectar();
	}
	
	/**
	 *	Libera o engine apropriado do servico da Tecnomen ao qual a conexao se refere.
	 *
	 *	@param		autenticador			Informacoes obtidas no processo de autenticacao utilizadas para 
	 *										estabelecimento da conexao com o servico apropriado.
	 */
	protected abstract void releaseEngine(Autenticador autenticador);
	
	/**
	 *	Atribui o engine apropriado do servico da Tecnomen ao qual a conexao se refere.
	 *
	 *	@param		autenticador			Informacoes obtidas no processo de autenticacao utilizadas para 
	 *										estabelecimento da conexao com o servico apropriado.
	 *	@throws		GPPTecnomenException
	 */
	protected abstract void setEngine(Autenticador autenticador) throws GPPTecnomenException;
	
	/**
	 *	Efetua o teste da conexao. Verifica se a conexao esta ativa. Se nao estiver, tenta reconectar ao servidor.
	 *	Lanca excecao caso o servidor nao esteja disponivel, mesmo apos reautenticacao no Servidor de Autenticacao.
	 *
	 *	@throws		GPPTecnomenException
	 *	@see		com.brt.gpp.comum.conexoes.Conexao#testarConexao()
	 */
	public void testarConexao() throws GPPTecnomenException
	{
		if(!this.isAtivo())
		{
			this.log(Definicoes.WARN, "testarConexao", "Conexao ID Servico Tecnomen: " + this.idServico + " - Nao esta ativa. Reconectando.");
			this.reconectar();
			this.log(Definicoes.INFO, "testarConexao", "Conexao ID Servico Tecnomen: " + this.idServico + " - Reestabelecida com sucesso.");
		}
		
		this.log(Definicoes.DEBUG, "testarConexao", "Conexao ID Servico Tecnomen: " + this.idServico + " - Ativa.");
	}
	
	/**
	 *	Loga uma mensagem.
	 *
	 *	@param		tipo					Grau de severidade do log.
	 *	@param		metodo					Nome do metodo que loga a mensagem.
	 *	@param		mensagem				Mensagem logada. 
	 */
	protected void log(int tipo, String metodo, String mensagem)
	{
		GerentePoolLog.getInstancia(ConexaoTecnomen.class).log(0, tipo, Definicoes.CL_GERENTE_TECNOMEN, metodo, mensagem);
	}
	
}
