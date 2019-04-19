package br.com.brasiltelecom.ppp.util.LDAP;

import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.LDAPSocketFactory;
import com.novell.ldap.LDAPJSSESecureSocketFactory;

import java.io.UnsupportedEncodingException;
import java.security.Security;
//import javax.servlet.ServletContext;

/**
 * Esta classe realiza a conexao com qualquer servidor LDAP
 * provendo metodos para abstracao de pesquisas e conexoes
 * 
 * @author Joao Carlos
 * @since 01/09/2004
 */
public class ConexaoLDAP
{
	private String				ldapHost;
	private int					ldapPort;
	//private ServletContext		context;
	private LDAPSocketFactory 	socketFactory;
	private LDAPConnection 		ldapConnection;
	private int 				ldapVersion 	= LDAPConnection.LDAP_V3;
	private String				arquivoCertificado;

	/**
	 * Metodo....:ConexaoLDAP
	 * Descricao.:Metodo para inicializar as propriedades para conexao LDAP
	 * @param ldapHost	- LDAPServer a ser conectado
	 */
	public ConexaoLDAP(String ldapHost,String arquivoCertificado)
	{
		this.ldapHost  = ldapHost;
		this.ldapPort  = LDAPConnection.DEFAULT_PORT;
		this.arquivoCertificado = arquivoCertificado;
	}

	/**
	 * Metodo....:ConexaoLDAP
	 * Descricao.:Metodo para inicializar as propriedades para conexao LDAP
	 * @param ldapHost	- LDAPServer a ser conectado
	 * @param ldapPort	- Porta do servidor LDAP a ser conectado
	 */	
	public ConexaoLDAP(String ldapHost, int ldapPort, String arquivoCertificado)
	{
		this.ldapHost  = ldapHost;
		this.ldapPort  = ldapPort;
		this.arquivoCertificado = arquivoCertificado;
	}
	
	/**
	 * Metodo....:connect
	 * Descricao.:Realiza a conexao com o servidor LDAP utilizando as 
	 *            propriedades definidas para conexao
	 * @throws LDAPException
	 */
	public void connect() throws LDAPException
	{
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		System.setProperty("javax.net.ssl.trustStore", arquivoCertificado);
		socketFactory = new LDAPJSSESecureSocketFactory();
		LDAPConnection.setSocketFactory(socketFactory);
		ldapConnection = new LDAPConnection();
		ldapConnection.connect(ldapHost,ldapPort); 
	}
	
	/**
	 * Metodo....:connect
	 * Descricao.:Realiza a conexao com o servidor LDAP utilizando as
	 *            propriedades definidas e apos realiza o login neste
	 *            servidor
	 * @param domainName	- Domain na qual sera executado o login
	 * @param password		- Senha a ser utilizada para login
	 * @throws LDAPException
	 * @throws UnsupportedEncodingException
	 */
	public void connect(String domainName, String password) throws LDAPException,UnsupportedEncodingException
	{
		connect();
		login(domainName,password);
	}
	
	/**
	 * Metodo....:disconnect
	 * Descricao.:Realiza a desconexao com o servidor LDAP
	 * @throws LDAPException
	 */
	public void disconnect() throws LDAPException
	{
		if (ldapConnection != null)
			ldapConnection.disconnect();
	}
	
	/**
	 * Metodo....:login
	 * Descricao.:Realiza o login no servidor LDAP
	 * @param domainName	- Domain name a ser utilizado para login
	 * @param password		- Senha a ser utilizada para login
	 * @throws LDAPException
	 * @throws UnsupportedEncodingException
	 */
	public void login(String domainName, String password) throws LDAPException,UnsupportedEncodingException
	{
		if (ldapConnection != null)
			ldapConnection.bind(ldapVersion, domainName, password.getBytes("UTF8"));
	}
	
	/**
	 * Metodo....:search
	 * Descricao.:Realiza uma pesquisa no servidor LDAP
	 * @param searchBase	- Base onde se inicializara a consulta
	 * @param sarchScope	- Scopo de busca da pesquisa
	 * @param searchFilter	- Filtro a ser utilizado para a consulta
	 * @return LDAPSearchResults - Resultado da consulta
	 * @throws LDAPException
	 */
	public LDAPSearchResults search(String searchBase, int searchScope, String searchFilter) throws LDAPException
	{
		LDAPSearchResults results=null;
		if (ldapConnection != null && ldapConnection.isConnected())
			results = ldapConnection.search(searchBase,searchScope,searchFilter,null,false);
			
		return results;
	}
}
