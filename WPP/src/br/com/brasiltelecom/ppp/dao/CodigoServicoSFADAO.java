package br.com.brasiltelecom.ppp.dao;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.brt.gpp.comum.mapeamentos.entidade.CodigoServicoSFA;

/**
 * Interface de acesso ao cadastro de servicos SFA.
 * 
 * @author Bernardo Dias
 * Criado em: 29/09/2007
 */
public class CodigoServicoSFADAO
{
	
	/**
	 * Consulta um CodigoServicoSFA.
	 * 
	 * @param sessiojn 				Sessão do Hibernate.
	 * @param codigoSFA				Numero do codigo SFA.
	 * @return 						Instancia de <code>CodigoServicoSFA</code>.
	 */
	public static CodigoServicoSFA findById(Session session, int codigoSFA)
	{		
		return (CodigoServicoSFA)session.get(com.brt.gpp.comum.mapeamentos.entidade.
				CodigoServicoSFA.class, new Integer(codigoSFA));
	}
	
	/**
	 * Inclui um CodigoServicoSFA.
	 * 
	 * @param session 				Sessão do Hibernate.
	 * @param codigoServicoSFA 		Entidade <code>CodigoServicoSFA</code>.
	 */
	public static void incluirServico(Session session, CodigoServicoSFA codigoServicoSFA) 
	{	
		session.save(codigoServicoSFA);
	}
	
	/**
	 * Consulta a lista de categorias existentes no banco
	 * (mesmo que distinc idt_categoria)
	 * 
	 * @param session			Session session
	 * @return	ArrayList contendo strings (idt_categoria)
	 */
	public static ArrayList findListaCategorias(Session session)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT distinct a.idt_categoria as categoria " +
				"FROM tbl_con_codigo_servico_sfa a " +
				"ORDER BY categoria ASC ");
		
		query.addScalar("categoria", Hibernate.STRING);
		ArrayList list = new ArrayList();
		
		for (Iterator it = query.list().iterator(); it.hasNext();) 
		{ 
			String item = (String) it.next();
			if (item != null)
				list.add(item);	        
	    }
		
		return list;
	}
	
	/**
	 * Consulta a lista de tipos de registro existentes no banco
	 * (mesmo que distinc tip_registro)
	 * 
	 * @param session			Session session
	 * @return	ArrayList contendo strings (tip_registro)
	 */
	public static ArrayList findListaTiposRegistro(Session session)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT distinct a.tip_registro as tipoRegistro " +
				"FROM tbl_con_codigo_servico_sfa a " +
				"ORDER BY tipoRegistro ASC ");
		
		query.addScalar("tipoRegistro", Hibernate.STRING);
		ArrayList list = new ArrayList();
		
		for (Iterator it = query.list().iterator(); it.hasNext();) 
		{ 
			String item = (String) it.next();
			if (item != null)
				list.add(item);	        
	    }
		
		return list;
	}
	
	/**
	 * Consulta a lista de tipos de servico existentes no banco
	 * (mesmo que distinc idt_tipo_servico)
	 * 
	 * @param session			Session session
	 * @return	ArrayList contendo strings (idt_tipo_servico)
	 */
	public static ArrayList findListaTiposServico(Session session)
	{
		SQLQuery query = session.createSQLQuery(
				"SELECT distinct a.idt_tipo_servico as tipoServico " +
				"FROM tbl_con_codigo_servico_sfa a " +
				"ORDER BY tipoServico ASC ");
		
		query.addScalar("tipoServico", Hibernate.STRING);
		ArrayList list = new ArrayList();
		
		for (Iterator it = query.list().iterator(); it.hasNext();) 
		{ 
			String item = (String) it.next();
			if (item != null)
				list.add(item);	        
	    }
		
		return list;
	}
}