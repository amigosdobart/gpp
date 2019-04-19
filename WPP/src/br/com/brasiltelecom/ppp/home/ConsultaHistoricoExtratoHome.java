package br.com.brasiltelecom.ppp.home;

import br.com.brasiltelecom.ppp.portal.entity.EventoAprovisionamento;
import br.com.brasiltelecom.ppp.portal.entity.HistoricoExtrato;
import java.util.*;
import org.exolab.castor.jdo.*;

/**
 * Classe respons�vel pelas consultas/atualiza��es no banco de dados relativas a consultas de hist�rico do extrato
 * 
 * @author Andr� Gon�alves
 * @since 24/05/2004
 */
public class ConsultaHistoricoExtratoHome
{
	/**
	 * Obt�m os hist�ricos de extrato existentes
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @return Cole��o de objetos HistoricoExtrato
	 * @throws PersistenceException
	 */
    public static Collection findAll(Database db)
        throws PersistenceException
    {
        org.exolab.castor.jdo.OQLQuery query = null;
        Collection result = new ArrayList();
		QueryResults rs = null;
        try
        {
            query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoExtrato a ");
            for(rs = query.execute(); rs.hasMore(); result.add((HistoricoExtrato)rs.next()));
        }
        finally
        {
			if(rs != null){
				rs.close();
			}
 
            if(query != null)
                query.close();
        }
        return result;
    }
    
	/**
	 * Obt�m os hist�ricos de extrato de acordo com o par�metro passado
	 * 
	 * @param db Conex�o com o Banco de Dados
	 * @param param objeto Map contendo as informa��es de busca
	 * @return Cole��o de objetos HistoricoExtrato
	 * @throws PersistenceException
	 */
    public static Collection findByFilter(Database db, Map param)
        throws PersistenceException
    {
        org.exolab.castor.jdo.OQLQuery query = null;
        Collection result = new ArrayList();
		QueryResults rs = null;
        try
        {
            StringBuffer filtro = new StringBuffer();
            if(param.get("idTipoExtrato") != null && !"0".equals(param.get("idTipoExtrato")))
            	if(!"TODOS".equals(param.get("idTipoExtrato")))
                filtro.append(" a.tipoExtrato = ").append(param.get("idTipoExtrato"));
            if(param.get("dataInicial") != null && !"".equals(param.get("dataInicial")))
            {
                if(filtro.length() > 0)
                    filtro.append(" and ");
                filtro.append(" a.data between to_date(\"").append(param.get("dataInicial")).append("\",\"DD/MM/YYYY\") and to_date(\"").append(param.get("dataFinal")).append("\",\"DD/MM/YYYY\")");
            }
            if(param.get("msisdn") != null && !"".equals(param.get("msisdn")))
            {
                if(filtro.length() > 0)
                    filtro.append(" and ");
                filtro.append(" a.msisdn = ").append(param.get("msisdn"));
            }
            if(param.get("filtro_sql") != null){
            	filtro= new StringBuffer(param.get("filtro_sql").toString());
            }
            StringBuffer consulta = new StringBuffer("select a from ");
            consulta.append("br.com.brasiltelecom.ppp.portal.entity.HistoricoExtrato a ");
            if(filtro.length() > 0)
                consulta.append(" where ").append(filtro.toString());
            query = db.getOQLQuery(consulta.toString() + "order by a.data");
            for(rs = query.execute(); rs.hasMore(); result.add((HistoricoExtrato)rs.next()));
            result.add(filtro.toString());
        }
        finally
        {
			if(rs != null){
				rs.close();
			}
 
            if(query != null)
                query.close();
        }
        return result;
    }
    /**
     * Cria um hist�rico de extrato
     * 
     * @param he objeto HistoricoExtrato a ser criado
     * @param db Conex�o com o Banco de Dados
     * @throws Exception
     */
	public static void criarOrigem(HistoricoExtrato he, Database db)throws Exception{

		db.create(he);
	}
	
	/**
     * Cria um evento de aprovisionamento
     * 
     * @param ea objeto EventoAprovisionamento a ser criado
     * @param db Conex�o com o Banco de Dados
     * @throws Exception
     */
	public static void criarEventoAprovisionamento(EventoAprovisionamento ea, Database db)throws Exception{

		db.create(ea);
	}
}