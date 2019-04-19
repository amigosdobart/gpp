//Definicao do Pacote
package com.brt.gpp.aplicacoes.importacaoCDR;

//Arquivos de Import Internos
import java.io.File;
import java.io.FileFilter;

/**
  *
  * Este arquivo refere-se a classe ImportaAssinantes, responsavel 
  * pela implementacao da logica de importacao de assinantes
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Joao Carlos
  * Data: 				07/04/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */
public class CDRFileFilter implements FileFilter {

	// Nome do arquivo de CDR a ser utilizado como padrão a ser encontrado no diretório
	String pattern;

	/**
	 * Metodo...: CDRFileFilter
	 * Descricao: Construtor 
	 * @param	token	- filtro de arquivo de CDRs 
	 * @return									
	 */
	public CDRFileFilter(String token){
		pattern = token;
	}

   /**
	* Metodo...: accept
	* Descricao: Este metodo valida se o arquivo no diretorio corresponde ao filtro que foi 
	* 			 passado como parametro no construtor dessa classe.
	* @param 	f 		- Arquivo correspondente ao filtro
	* @return	boolean	- True se for com sucesso, false caso contrario
	* @throws 	
	*/
	public boolean accept(File f) {
		if (f.getName().indexOf(pattern) > -1 && f.getName().indexOf(".tmp") == -1)
			return true;
		return false;
	}

}
