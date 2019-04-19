package com.brt.gpp.gerentesPool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.ORB;

import TINC.AuthFactory;
import TINC.AuthFactoryHelper;
import TINC.LOGIN_IN_USE;
import TINC.Pi_exception;
import TINC.UserInfo;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.tecnomen.conversor.ConversorExcecaoTecnomen;
import com.brt.gpp.comum.conexoes.tecnomen.entidade.Autenticador;
import com.brt.gpp.comum.conexoes.tecnomen.holder.AutenticadorHolder;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Gerente de autenticacao do usuario GPP com a Tecnomen. Possui uma lista de autenticadores, um para cada servico, 
 *	contendo os IOR's e chaves necessarias no processo de estabelecimento de conexoes com os servidores. As conexoes
 *	ao se conectarem devem obter seus devidos autenticadores atraves deste gerente no intuito de continuar o processo.
 *
 *	@author		Daniel Ferreira
 *	@since		22/02/2007
 */
public class GerenteAutenticadorTecnomen 
{

	/**
	 *	Instancia do singleton. 
	 */
	private static GerenteAutenticadorTecnomen instance;

	/**
	 *	Container de autenticadores. Cada autenticador tem como chave o ID do servico provido pela Tecnomen. Exemplos
	 *	de servico sao o Provision Server, PP Server, Payment Engine e Voucher Management.
	 */
	private Map autenticadores;
	
	/**
	 *	Gerente de Log. Utilizado para geracao de mensagens de log.
	 */
	private GerentePoolLog logger;
	
	/**
	 *	Construtor da classe. 
	 */
	private GerenteAutenticadorTecnomen()
	{
		this.autenticadores	= Collections.synchronizedMap(new HashMap());
		this.logger			= GerentePoolLog.getInstancia(this.getClass());
		
		this.autenticar();
	}

	/**
	 *	Retorna a instancia do singleton.
	 *
	 * 	@return		Instancia do singleton. 
	 */
	public static GerenteAutenticadorTecnomen getInstance()
	{
		if(GerenteAutenticadorTecnomen.instance == null)
			GerenteAutenticadorTecnomen.instance = new GerenteAutenticadorTecnomen();
		
		return GerenteAutenticadorTecnomen.instance;
	}
	
	/**
	 *	Retorna o autenticador do servico.
	 *
	 *	@param		idServico				Identificador do servico provido pela Tecnomen.
	 * 	@return		Autenticador do servico. 
	 */
	public Autenticador getAutenticador(int idServico)
	{
		return (Autenticador)this.autenticadores.get(new Integer(idServico));
	}
	
	/**
	 *	Autentica o usuario GPP no Servidor de Autenticacao da Tecnomen.
	 */
	public synchronized void autenticar()
	{
		//Obtendo o ORB da aplicacao.
		ORB orb = GerenteORB.getInstance().getOrb();
		
		//Obtendo o IOR do Servidor de Autenticacao.
		ArquivoConfiguracaoGPP configuracao = ArquivoConfiguracaoGPP.getInstance();
		String referencia = configuracao.getAuthServerReferencia();

		//Obtendo a referencia ao Servidor de Autenticacao da Tecnomen.
		org.omg.CORBA.Object objAuthServer = orb.string_to_object(referencia);
		AuthFactory fabrica = AuthFactoryHelper.narrow(objAuthServer);
		
		//Obtendo o nome e senha do usuario operador do GPP. Este usuario tem acesso as conexoes com o Provision 
		//Server, PP Server e Payment Engine.
		String operadorGPP = configuracao.getNomeUsuarioTecnomen();
		String senhaOperadorGPP = configuracao.getSenhaUsuarioTecnomen();
		
		//Efetuando o login do usuario.
		this.autenticar(fabrica, operadorGPP, senhaOperadorGPP);
		
		//Obtendo o nome e senha do usuario de voucher do GPP. Este usuario tem acesso as conexoes com o Voucher 
		//Management Server. 
		String voucherGPP = configuracao.getNomeUsuarioTecnomenVoucher();
		String senhaVoucherGPP = configuracao.getSenhaUsuarioTecnomenVoucher();
		
		//Efetuando o login do usuario.
		this.autenticar(fabrica, voucherGPP, senhaVoucherGPP);
	}
	
	/**
	 *	Autentica o usuario GPP no Servidor de Autenticacao da Tecnomen.
	 *
	 *	@param		fabrica					Referencia ao Servidor de Autenticacao da Tecnomen.
	 *	@param		usuario					Nome do usuario.
	 *	@param		senha					Senha do usuario.
	 *	@return		Lista de autenticadores relacionados ao acesso do usuario.
	 */
	private void autenticar(AuthFactory fabrica, String login, String senha)
	{
		try
		{
			//Efetuando o login e extraindo as informacoes referentes aos servicos disponibilizados ao assinante.
			AutenticadorHolder autenticadores = new AutenticadorHolder();
			this.logger.log(0, 
							Definicoes.DEBUG, 
							Definicoes.CL_GERENTE_TECNOMEN, 
							"autenticar", 
							"Autenticando Usuario: " + login + " - Senha: " + senha);
			
			// Modificacao realizada em 20/09/07 por Joao Carlos devido aos problemas 
			// de conexao na Tecnomen com o PPInterface. Existe a possibilidade de distribuicao
			// entre o PPServer e o PPInterface para consulta de assinante. Para isso deve-se
			// utilizar a funcao "validate" no AuthServer ao inves da funcao "login". Esta funcao
			// validate nao estah disponivel em documentos pois eh de uso interno da Tecnomen
			//UserInfo info = fabrica.login(login, senha, autenticadores.toServiceKeySeqHolder());
			UserInfo info = fabrica.validate(login, senha, autenticadores.toServiceKeySeqHolder());
			
			this.autenticadores.putAll(autenticadores.toMap(login, info)); 
			
			this.logger.log(0, 
							Definicoes.DEBUG, 
							Definicoes.CL_GERENTE_TECNOMEN, 
							"autenticar", 
							"Usuario GPP: " + login + " - Autenticado no Servidor de Autenticacao da Tecnomen. " + autenticadores);
		}
		catch(Pi_exception e)
		{
			//No caso de excecao lancada pelo Servidor de Autenticacao, e necessario verificar qual o codigo de 
			//retorno. Caso o codigo seja de usuario ja logado, nao representa erro. Caso contrario, a autenticacao
			//nao pode ser feita e os autenticadores nao podem ser criados.
			if(e.error_code != LOGIN_IN_USE.value)
				this.logger.log(0, Definicoes.WARN, Definicoes.CL_GERENTE_TECNOMEN, "autenticar", 
								"Usuario GPP: " + login + " - NAO autenticado: " + ConversorExcecaoTecnomen.toGPPTecnomenException(e));
		}
	}
	
}
