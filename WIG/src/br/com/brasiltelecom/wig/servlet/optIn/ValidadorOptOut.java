package br.com.brasiltelecom.wig.servlet.optIn;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;

public interface ValidadorOptOut
{
	public MensagemRetorno validarOptOut(OptIn optIn);
}
