package br.com.brasiltelecom.ppp.home;

import br.com.brasiltelecom.ppp.portal.entity.HistoricoBroadcastSMS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.exolab.castor.jdo.*;

/**
 * Classe responsável pelas consultas ao banco de dados relativas a consultas de histórico de broadcast de sms
 * 
 * @author Marcelo Alves Araujo
 * @since 03/08/2005
 */
public class ConsultaHistoricoBroadcastHome
{
	/**
	 * Obtém o histórico completo de broadcst
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @return Coleção de objetos HistoricoBroadcastSMS
	 * @throws PersistenceException
	 */
    public static Collection findAll(Database db)throws PersistenceException
    {
        org.exolab.castor.jdo.OQLQuery query = null;
        Collection result = new ArrayList();
		QueryResults rs = null;
        try
        {
            query = db.getOQLQuery("select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoBroadcastSMS a ");
            for(rs = query.execute(); rs.hasMore(); result.add((HistoricoBroadcastSMS)rs.next()));
        }
        finally
        {
			if(rs != null)
				rs.close();
			
            if(query != null)
                query.close();
        }
        return result;
    }
    
	/**
	 * Obtém os históricos de extrato de acordo com o msisdn, periodo, sucesso e erro
	 * 
	 * @param db Conexão com o Banco de Dados
	 * @param msisdn		- Msisdn a ser buscado
	 * @param dataInicio	- Inicio do periodo da consulta
	 * @param dataFim		- Fim do periodo da consulta
	 * @param sucesso		- Buscar mensagens enviadas com sucesso
	 * @param erro			- Buscar mensagens não enviadas
	 * @return Coleção de objetos HistoricoExtrato
	 * @throws PersistenceException
	 * @throws ParseException 
	 * @throws IllegalArgumentException 
	 */
    public static Collection findByParam(Database db, String msisdn, String dataInicio, String dataFim, String sucesso, String erro )throws PersistenceException, IllegalArgumentException, ParseException
    {
        org.exolab.castor.jdo.OQLQuery query = null;
        Collection result = new ArrayList();
		QueryResults rs = null;
		
        try
        {
        	String consulta = "select a from br.com.brasiltelecom.ppp.portal.entity.HistoricoBroadcastSMS a " +
			" where a.dataEnvio >= $1 " +
			" and a.dataEnvio <= $2 ";
			
        	
        	if( msisdn != null )
        		consulta = consulta + " and a.msisdn = \""+msisdn+"\"";
        	
        	if(sucesso == null)
        		sucesso = "false";
        	if(erro == null)
        		erro = "false";
        	
        	if( sucesso.equalsIgnoreCase("true") || erro.equalsIgnoreCase("true"))
        	{
        		consulta += " and (";
        		consulta += sucesso.equalsIgnoreCase("true") ? "a.statusEnvio = 'S'" : "a.statusEnvio = 'N'";
        		if( sucesso.equalsIgnoreCase("true") && erro.equalsIgnoreCase("true") )
        			consulta += "or a.statusEnvio = 'N'";

        		consulta += ")";
        	}
        	
        	consulta += " order by a.dataEnvio";
        	query = db.getOQLQuery(consulta);
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	
        	query.bind(sdf.parse(dataInicio));
        	query.bind(sdf.parse(dataFim));
        	
            for(rs = query.execute(); rs.hasMore(); result.add((HistoricoBroadcastSMS)rs.next()));
        }
        finally
        {
			if(rs != null)
				rs.close();
			
            if(query != null)
                query.close();
        }
        return result;
    }
}