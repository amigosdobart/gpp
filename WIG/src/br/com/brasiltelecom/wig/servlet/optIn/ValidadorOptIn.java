package br.com.brasiltelecom.wig.servlet.optIn;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;

public interface ValidadorOptIn
{
	public MensagemRetorno validarOptIn(OptIn optIn, int diasFidelizacao);
}
 
