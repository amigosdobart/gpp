package br.com.brasiltelecom.ppp.session.acesso;

import java.util.Map;

//Framework castor
import org.exolab.castor.jdo.JDO;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import javax.servlet.ServletContext;

import br.com.brasiltelecom.ppp.portal.entity.*;
import br.com.brasiltelecom.ppp.home.*;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author ex471453
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LogFacade {
	public static void log(ServletContext context, LogOperacao log){
		JDO jdo = (JDO) context.getAttribute(Constantes.JDO);
		try{
			Database db = jdo.getDatabase();
			db.begin();
			db.create(log);
			db.commit();
			db.close();
		}
		catch(PersistenceException e){
			context.log("Erro Castor", e);
		}
		
	}
	public static void log(ServletContext context, String usuario, String operacao,
	String mensagem, String ip){
		JDO jdo = (JDO) context.getAttribute(Constantes.JDO);
		Map operacoes = (Map) context.getAttribute(Constantes.OPERACOES);
		try{
			Database db = jdo.getDatabase();
			db.begin();
			Operacao op = (Operacao) operacoes.get(operacao);
			LogOperacao lOp = new LogOperacao(usuario, op, mensagem, ip);
			db.commit();
			db.close();
			log(context, lOp);
		}
		catch(PersistenceException e){
			context.log("Erro Castor", e);
		}
		
	}
	
	public static void log( Database db, LogOperacao log){
		try{
			db.create(log);
		}
		catch(PersistenceException e){
			e.printStackTrace();
		}
		
	}

	public static void log( Database db, String usuario, String operacao,
	String mensagem, String ip){
		try{
			Operacao op = OperacaoHome.findByNome(db,operacao);
			LogOperacao lOp = new LogOperacao(usuario, op, mensagem, ip);
			log( db, lOp);
		}
		catch(PersistenceException e){
			e.printStackTrace();
		}
		
	}
}
