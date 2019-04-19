package com.brt.gpp.aplicacoes.campanha.entidade;

import java.util.Map;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 * Esta interface define como serah a classe que deverah implementar um Parametro 
 * de Inscricao. O parametro de inscricao eh a definicao de como um determinado 
 * assinante se inscreve em uma campanha. Portanto cada classe define sua forma de 
 * inscricao.
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public interface ParametroInscricao 
{
	/**
	 * Este metodo retorna se um determinado assinante, recebido como parametro, 
	 * deverah ou nao ser inscrito na campanha
	 * 
	 * @param assinante a ser verificado se poderah ser inscrito na campanha
	 * @param conexaoPrep de banco de dados a ser utilizada
	 * @return boolean - Indica se o assinante deve ser ou nao inscrito na campanha
	 */
	public boolean podeSerInscrito(Assinante assinante, PREPConexao conexaoPrep);
	
	/**
	 * Este metodo retorna uma lista de valores (chave-valor) para ser utilizado
	 * pela campanha da forma que cada uma exigir. O processo irah configurar estes
	 * parametros em formato XML no banco de dados
	 * 
	 * @return Map - Lista dos valores utilizos para a inscricao do assinante
	 */
	public Map getParametros();
	
	/**
	 * Este método diz ao produtor da campanha se ele deverá ou não enviar
	 * o SMS para o assinante.
	 * 
	 * @return boolean - Indica se o sms deverá ser enviado ao assinante
	 */
	public boolean enviaSMS(Assinante assinante, PREPConexao conexaoPrep);
}
