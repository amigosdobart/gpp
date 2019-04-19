/*
 * Created on 20/04/2004
 *
 */
package br.com.brasiltelecom.ppp.home;

import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.portal.entity.DadosComprovante;

/**
 * Classe responsável pelas atualizações no banco de dados relativas a dados de comprovantes
 * 
 * @author André Gonçalves
 * @since 24/05/2004
 */
public class DadosComprovanteHome {

	/**
	 * Cria dados de comprovantes de acordo com o parâmetro informado
	 * 
	 * @param dc dados de comprovantes a ser criado
	 * @param db Conexão com o banco de dados
	 * @throws Exception
	 */
	public static void criarDadosComprovante(DadosComprovante dc, Database db)throws Exception{
		db.create(dc);
	}
	
}
