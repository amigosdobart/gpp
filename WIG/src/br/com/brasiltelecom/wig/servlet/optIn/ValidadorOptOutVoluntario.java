package br.com.brasiltelecom.wig.servlet.optIn;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.util.Definicoes;

public class ValidadorOptOutVoluntario implements ValidadorOptOut
{
	/**
	 *@see br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptIn#validarOptIn(br.com.brasiltelecom.wig.entity.OptIn, int)
	 */
	public MensagemRetorno validarOptOut(OptIn optIn)
	{
		MensagemRetorno retorno = new MensagemRetorno();
		
		// Para o OptIn voluntario, nao eh necessaria nenhuma validacao e,
		// portanto, o OptOut sempre sera possivel, uma vez que nao existe
		// um periodo de fidelizacao
		// Codigo de retorno : 0 (sucesso na validacao)
		// Mensagem de retorno : Sucesso na desativacao do OptIn
		retorno.setCodRetornoNum(Definicoes.WIG_RET_OK);
		retorno.setCodRetorno(Definicoes.WIG_RET_OK_STR);
		retorno.setMensagem("Voce foi descadastrado. Cadastre-se novamente quando desejar. Clique OK para concluir.");
		
		return retorno;
	}
}
