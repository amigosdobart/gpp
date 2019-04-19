package br.com.brasiltelecom.wig.action;

import br.com.brasiltelecom.wig.action.WIGWmlConstrutor;
import br.com.brasiltelecom.wig.entity.Usuario;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Esta classe realiza o armazenamento em memoria dos assinantes jah autenticados.
 * Uma instancia do objeto HashMap eh utilizado para o controle dos assinantes jah
 * autenticados. A chave de busca para o assinante eh o MSISDN do mesmo, sendo que
 * o objeto Usuario deve ser inserido apos uma autenticacao e verificado se o periodo
 * de expiracao ainda eh valido
 * 
 * @author Joao Carlos
 * Data..: 14/07/2005
 *
 */
public class Autenticador
{
	private static 	Autenticador 	instance;
	private			long			maxSegundos;
	private 		Map		 		usuarios;
	
	private Autenticador()
	{
		usuarios = Collections.synchronizedMap(new HashMap());
	}
	
	public static Autenticador getInstance()
	{
		if (instance == null)
			instance = new Autenticador();
		
		return instance;
	}

	/**
	 * Metodo....:setTempoMaximoExpiracao
	 * Descricao.:Define o tempo maximo em segundos para a expiracao de uma sessao
	 * @param segundos - Tempo maximo de expiracao
	 */
	public synchronized void setTempoMaximoExpiracao(long segundos)
	{
		maxSegundos = segundos;
	}
	
	/**
	 * Metodo....:estaAutenticado
	 * Descricao.:Verifica se o assinante estah no pool em memoria e se a data
	 *            de logon nao foi expirada
	 * @param msisdn	- Msisdn a ser pesquisado
	 * @return boolean	- Indica se o assinante estah autenticado ou nao
	 */
	public boolean estaAutenticado(String msisdn)
	{
		boolean estaAutenticado=false;
		// Tenta encontrar o usuario no pool de assinantes. Se o usuario nao existe
		// ou se o mesmo se encontra com sessao expirada (tempo do logon ateh a data
		// atual) entao retorna falso forcando uma autenticacao senao o usuario eh
		// dito como autenticado e procedimento inicial pode ocorrer
		// Se o usario existe e estah expirado entao remove-o do mapeamento para 
		// tentar otimizar o numero de objetos em memoria
		Usuario usuario = (Usuario)usuarios.get(msisdn);
		if (usuario != null && !estaExpirado(usuario.getDataLogon()))
			estaAutenticado = true;
		else if (usuario != null)
				usuarios.remove(msisdn);

		return estaAutenticado;
	}
	
	/**
	 * Metodo....:estaExpirado
	 * Descricao.:Este metodo verifica se a diferenca entre a data atual e
	 *            a data do logon eh maior que um valor maximo parametrizado
	 *            para indicar expiracao da sessao 
	 * @param dataLogon		- Data de logon
	 * @param maxSegundos	- Tempo maximo para a expiracao em segundos
	 * @return
	 */
	private boolean estaExpirado(Date dataLogon)
	{
		// A diferenca em milisegundos entre as duas datas eh convertida pra segundos
		// e entao comparada com o tempo maximo esperado para a expiracao se o tempo
		// for maior entao a sessao expirou e deve ser feita outra autenticacao
		long segundos = (Calendar.getInstance().getTime().getTime() - dataLogon.getTime())/1000;
		return segundos > maxSegundos;
	}
	
	/**
	 * Metodo....:getWMLAutenticacao
	 * Descricao.:Retorna o WML que sera utilizado para o usuario informar o codigo de acesso
	 * @param msisdn		- Msisdn do assinante
	 * @param servidorWig	- Servidor WIG aonde o autenticador esta sendo executado
	 * @param portaWig		- Porta onde o servidor WIG responde
	 * @param redirect		- URL que sera redirecionada apos autenticacao
	 * @return String 		- WML a ser enviado para o assinante
	 */
	public String getWMLAutenticacao(String msisdn, String containerWig, String redirect)
	{
		StringBuffer wml = new StringBuffer(WIGWmlConstrutor.PROLOGUE_WML);
		String urlCode = redirect;
		// Converte a URL a qual serah redirecionada para o formato UTF-8, Se nao conseguir
		// utiliza a URL no formato no qual foi passado inicialmente
		try
		{
			urlCode = URLEncoder.encode(redirect,"UTF-8");
		} catch (UnsupportedEncodingException e)
		{
			urlCode = redirect;
		}
		wml.append("<wml>\n<card id=\"Aut\">\n");
		wml.append("<p>\n<input title=\"Entre com o codigo de acesso\" type=\"password\" name=\"cod\" maxlength=\"5\"/>\n");
		wml.append("<go href=\""+containerWig+"/autenticador?req="+urlCode+"&amp;cod=$(cod)\"/>\n");
		wml.append("</p>\n</card>\n</wml>");
		return wml.toString();
	}
	
	/**
	 * Metodo....:autenticaUsuario
	 * Descricao.:Realiza a autenticacao do usuario verificando o codigo de acesso
	 *            e retorna se este foi autenticado ou nao
	 * @param msisdn		- Msisdn do Assinante
	 * @param codigoAcesso	- Codigo de acesso
	 * @return boolean		- Indica se 
	 */
	public boolean autenticaUsuario(String msisdn, int codigoAcesso)
	{
		// Cria o objeto que serah utilizado para controle da sessao
		// do assinante (Usuario). Esse objeto sera armazenado durante
		// toda a sessao do usuario
		Usuario usr = new Usuario(msisdn);
		usr.setCodigoServico(codigoAcesso);
		usuarios.put(msisdn,usr);
		return true;
	}
	
	/**
	 * Metodo....:limpaCacheSessao
	 * Descricao.:Verifica todos os objetos na sessao de usuarios
	 *            para identificar quais jah estao expirados. Estes
	 *            serao removidos do objeto afim de liberar memoria
	 *
	 */
	public void limpaCacheSessao()
	{
		// Realiza uma iteracao em todos os objetos contidos
		// no mapeamento de sessao dos usuarios. Para cada
		// objeto (Usuario) eh verificado se este estah expirado
		// verificando atraves da data de logon em comparacao
		// com a data atual e com o tempo maximo definido para esta
		// expiracao. Se estiver expirado entao remove o objeto do
		// mapeamento
		Collection lista = usuarios.values();
		for (Iterator i=lista.iterator(); i.hasNext();)
		{
			Usuario usr = (Usuario)i.next();
			if (estaExpirado(usr.getDataLogon()))
				usuarios.remove(usr.getMsisdn());
		}
	}
}
