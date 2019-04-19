package br.com.brasiltelecom.ppp.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Interface de acesso ao valor da recarga.
 * 
 * @author Anderson Jefferson Cerqueira
 * Criado em: 15/02/2007
 */
public class ValorRecargaDAO
{
	/**  
	 * Consulta todos os valores para o tipo de saldo Principal e categoria Prepago.
	 *   
	 * @param session 				Sessão do Hibernate.
	 * @return 						Coleção de <code>ValorRecarga</code>.
	 */
	public static List findVigentes(Session session)
	{
		Query query = session.createQuery(
				" FROM com.brt.gpp.comum.mapeamentos.entidade.ValorRecarga" +
				" where categoria=0" + 
				" AND datIniVigencia <= SYSDATE" +
				" AND (datFimVigencia >= SYSDATE OR datFimVigencia IS NULL)"+ 
				" AND tipoSaldo = 0"+
				" AND indValorFace = 1"+
				" ORDER BY idValor");
		return query.list();
	}

}