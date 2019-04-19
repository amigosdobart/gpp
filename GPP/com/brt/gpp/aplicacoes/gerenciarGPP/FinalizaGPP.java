//Definicao do Pacote
package com.brt.gpp.aplicacoes.gerenciarGPP;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;

/**
  *
  * Este arquivo refere-se a classe FinalizaGPP, responsavel pela implementacao da
  * logica de finalizacao das conexoes Tecnomen e Banco de Dados
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Daniel Cintra Abib
  * Data: 				19/10/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public final class FinalizaGPP extends Aplicacoes implements Runnable
{
	public FinalizaGPP ( )
	{
		super(0, Definicoes.CL_FINALIZA_GPP);

		super.log(Definicoes.DEBUG, "FinalizaGPP", "Thread de finalizacao criada");
	}
	
	// Metodo run (da Thread) para a finalizacao das conexoes
	public void run ()
	{
		super.log(Definicoes.DEBUG, "run", "Finalizando conexoes com a plataforma e com o banco de dados");
		
		// Pega uma instancia do gerenteGPP
		GerenteGPP gerenteGPP = new GerenteGPP(0);
		
		// Termina as conexoes
		gerenteGPP.paraGPP ();

		super.log(Definicoes.DEBUG, "run", "Conexoes com a plataforma e com o banco de dados finalizadas");
	}
}