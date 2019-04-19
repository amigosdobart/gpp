package br.com.brasiltelecom.wig.servlet.optIn;

import java.text.SimpleDateFormat;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.util.Definicoes;

public class ValidadorOptInVoluntario implements ValidadorOptIn
{
	/**
	 *@see br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptIn#validarOptIn(br.com.brasiltelecom.wig.entity.OptIn, int)
	 */
	public MensagemRetorno validarOptIn(OptIn optIn, int diasFidelizacao)
	{
		// Variavel de retorno do Metodo
		MensagemRetorno retorno = new MensagemRetorno();
		
		// Validacoes realizadas:
		// 1 - Se os dias de fidelizacao do novo OptIn for maior que zero
		//     significa que o OptIn ativo (voluntario) sera sobreescrito
		//   - Caso contrario, o novo OptIn tambem sera voluntario, nao podendo
		//     sobreescrever o atual
		if (diasFidelizacao > 0 || optIn.getDataOptOut() != null)
		{
			// Codigo de retorno 0 : sucesso na validacao
			// Mensagem de retorno : nula (Pois a mensagem sera default pelo ShowMensagemOptIn.jsp)
			retorno.setCodRetornoNum(Definicoes.WIG_RET_OK);
			retorno.setCodRetorno(Definicoes.WIG_RET_OK_STR);
			retorno.setMensagem(null);
		}
		else
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			// Codigo de retorno 1 : falha na validacao
			// Mensagem de retorno : Assinante ja esta cadastrado desde "data OptIn ativo"
			retorno.setCodRetornoNum(Definicoes.WIG_RET_CADASTRADO);
			retorno.setCodRetorno(String.valueOf(Definicoes.WIG_RET_CADASTRADO));
			retorno.setMensagem("Voce ja esta cadastrado desde " + sdf.format(optIn.getDataOptIn()) + ". Clique OK para concluir.");
		}
		return retorno;
	}
}
 
