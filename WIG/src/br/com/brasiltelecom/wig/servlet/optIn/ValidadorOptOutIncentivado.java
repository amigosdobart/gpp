package br.com.brasiltelecom.wig.servlet.optIn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.util.Definicoes;

public class ValidadorOptOutIncentivado implements ValidadorOptOut
{
	/**
	 *@see br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptIn#validarOptIn(br.com.brasiltelecom.wig.entity.OptIn, int)
	 */
	public MensagemRetorno validarOptOut(OptIn optIn)
	{
		Calendar cal = Calendar.getInstance();
		MensagemRetorno retorno = new MensagemRetorno();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		// Por default, o assinante pode desativar o OptIn
		retorno.setCodRetornoNum(Definicoes.WIG_RET_OK);
		retorno.setCodRetorno(Definicoes.WIG_RET_OK_STR);
		retorno.setMensagem("Voce foi descadastrado. Cadastre-se novamente quando desejar. Clique OK para concluir.");

		// Verifica se a data de fidelizacao do OptIn ativo
		// ja venceu e, caso falhe, o OptIn nao podera ser desativado
		if ( !optIn.getDataFidelizacao().before(cal.getTime()) )
		{
			retorno.setCodRetornoNum(Definicoes.WIG_RET_CADASTRADO_FIDELIZADO);
			retorno.setMensagem("Voce podera se descadastrar a partir de " + sdf.format(optIn.getDataFidelizacao()) + ".");
		}
		
		return retorno;
	}
}
