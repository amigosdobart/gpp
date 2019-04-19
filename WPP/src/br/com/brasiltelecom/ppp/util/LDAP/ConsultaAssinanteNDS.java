package br.com.brasiltelecom.ppp.util.LDAP;

import br.com.brasiltelecom.ppp.portal.entity.DadosAssinanteNDS;

import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPAttribute;

import java.io.UnsupportedEncodingException;

/**
 * Esta classe realiza a consulta de assinantes no servidor LDAP
 * da BrasilTelecom. Esta classe utiliza para conexao variaveis
 * definidas nos parametros do sistema sendo que se forem alteradas
 * entao deve ser feita uma inicializacao do sistema
 * 
 * @author Joao Carlos
 * @since 01/09/2004
 */

public class ConsultaAssinanteNDS
{
	private String 	nomeServidor;
	private String 	domainName;
	private String 	password;
	private String  baseSearch;
	private String	arquivoCertificado;
	private int		portaServidor;
	
	public ConsultaAssinanteNDS(String servidor,int porta, String domain, String password, String baseSearch, String arqCertificado)
	{
		this.nomeServidor 		= servidor;
		this.portaServidor		= porta;
		this.domainName			= domain;
		this.password			= password;
		this.baseSearch			= baseSearch;
		this.arquivoCertificado	= arqCertificado;
	}

	/**
	 * Metodo....:validaSenhaServicos
	 * Descricao.:Este metodo verifica se a senha de servicos do assinante eh valida no NDS
	 * @param msisdn 	- Msisdn do assinante
	 * @param senha		- Senha de servicos
	 * @return boolean 	- Indica se a senha estah valida ou nao
	 * @throws AssinanteNaoExistenteNDSException
	 */
	public boolean validaSenhaServicos(String msisdn, String senha) throws AssinanteNaoExistenteNDSException,LDAPException,UnsupportedEncodingException
	{
		// Realiza a conexao com o LDAP e utiliza a propria consulta de assinantes dessa
		// classe para identificar o DomainName do assinante necessario para a autenticacao
		// Em caso de excecao (Erro na autenticacao por exe) entao retorna indicando que o
		// assinante nao foi autenticado
		ConexaoLDAP conexaoLDAP = new ConexaoLDAP(nomeServidor,portaServidor,arquivoCertificado);
		DadosAssinanteNDS assinante = consultaAssinante(msisdn);
		if (assinante == null)
			throw new AssinanteNaoExistenteNDSException(msisdn);

		// Apos conectar no servidor e consultar as informacoes do Assinante jah eh possivel
		// identificar se eh possivel realizar o bind com a senha informada. Caso consiga
		// entao foi autenticado, senao a senha estah invalida
		try
		{
			conexaoLDAP.connect(assinante.getDomainName(),senha);
			conexaoLDAP.disconnect();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	/**
	 * Metodo....:consultaAssinante
	 * Descricao.:Este metodo realiza a consulta dos dados do assinante
	 *            no NDS
	 * @param msisdn				- MSISDN do assinante a ser utilizado na pesquisa
	 * @return DadosAssinanteNDS 	- Objeto contendo os dados do assinante
	 * @throws LDAPException
	 * @throws UnsupportedEncodingException
	 */
	public DadosAssinanteNDS consultaAssinante(String msisdn) throws LDAPException,UnsupportedEncodingException
	{
		// Realiza a conexao com o servidor LDAP
		// buscando toda a configuracao no contexto da aplicacao
		// atraves das propriedades de inicializacao 
		ConexaoLDAP conexaoLDAP = new ConexaoLDAP(nomeServidor,portaServidor,arquivoCertificado);
		conexaoLDAP.connect(domainName,password);
		DadosAssinanteNDS assinante = null;
		// Obtem o NRC do assinante
		String dnAssinante  = getDNAssinante(conexaoLDAP,baseSearch,msisdn);
		if (dnAssinante != null)
		{
			String nrcAssinante = dnAssinante.substring (dnAssinante.indexOf(',', 0)+1, dnAssinante.length());
			
			// Busca os dados do assinante utilizando o numero de contrato
			// identificado atraves do MSISDN deste e popula um objeto para
			// armazenar estes dados a serem apresentados no Portal PPP
			assinante = new DadosAssinanteNDS();
			assinante.setDomainName(dnAssinante);
			LDAPSearchResults results = conexaoLDAP.search(nrcAssinante,LDAPConnection.SCOPE_BASE ,"(objectclass=*)");
			while ( results.hasMore() )
			{
				LDAPEntry nextEntry = results.next();
				LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
	
				assinante.setNrcAssinante   ( getValorAtributo(attributeSet,"brtNrc")             );
				assinante.setNomeAssinante  ( getValorAtributo(attributeSet,"brtNomeCliente")     );
				assinante.setTipoDocumento  ( getValorAtributo(attributeSet,"brtTipoDocumento")   );
				assinante.setTipoCliente    ( getValorAtributo(attributeSet,"brtTipoCliente")     );
				assinante.setNumeroDocumento( getValorAtributo(attributeSet,"brtNumeroDocumento") );
				
				if (assinante.getTipoCliente() != null)
				{
					if (assinante.getTipoCliente().equals("Pessoa Juridica"))
					{
						assinante.setNumeroCPFCNPJ( getValorAtributo(attributeSet,"brtCnpj") );
					}
					else
					{
						assinante.setNumeroCPFCNPJ( getValorAtributo(attributeSet,"brtCpf" ) );
					}
				}
			}
		}
		conexaoLDAP.disconnect();
		return assinante;
	}
	
	/**
	 * Metodo....:getDNAssinante
	 * Descricao.:Este metodo realiza a busca do domain name definido para
	 *            o assinante no servidor LDAP
	 * @param conexaoLDAP	- Conexao LDAP a ser utilizada
	 * @param baseSearch	- Base de pesquisa no servidor LDAP
	 * @param msisdn		- MSISDN do assinante a ser pesquisado
	 * @return String		- Numero do domain name do assinante (NRC)
	 * @throws LDAPException
	 */
	public String getDNAssinante(ConexaoLDAP conexaoLDAP, String baseSearch, String msisdn) throws LDAPException
	{
		String dn = null;
		String searchFilter = "(&(objectclass=brtInstanciaServicoSMP)(brtMsisdn=" + msisdn + "))";
		LDAPSearchResults results = conexaoLDAP.search(baseSearch,LDAPConnection.SCOPE_SUB,searchFilter);

		// Procura o MSISDN para obter o NRC do Assinante
		while ( results.hasMore())
		{
			LDAPEntry nextEntry = results.next();
			dn        			= nextEntry.getDN();
		}
		return dn;
	}

	/**
	 * Metodo....:getValorAtributo
	 * Descricao.:Busca o valor do atributo na lista de atributos do assinante
	 * @param attributeSet	- Lista de atributos
	 * @param nomeAtributo	- Nome do atributo
	 * @return String		- Valor do atributo, caso nao encontrado entao retorna nulo
	 */	
	public String getValorAtributo(LDAPAttributeSet attributeSet, String nomeAtributo)
	{
		LDAPAttribute attribute = attributeSet.getAttribute(nomeAtributo);
		if (attribute != null)
		{
			return attribute.getStringValue();
		}
		else
		{
			return null;
		}
	}
}
