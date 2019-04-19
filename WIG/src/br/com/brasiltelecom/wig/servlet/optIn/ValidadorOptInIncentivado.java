package br.com.brasiltelecom.wig.servlet.optIn;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.brasiltelecom.wig.entity.MensagemRetorno;
import br.com.brasiltelecom.wig.entity.OptIn;
import br.com.brasiltelecom.wig.util.Definicoes;

public class ValidadorOptInIncentivado implements ValidadorOptIn
{
	/**
	 *@see br.com.brasiltelecom.wig.servlet.optIn.ValidadorOptIn#validarOptIn(br.com.brasiltelecom.wig.entity.OptIn, int)
	 */
	public MensagemRetorno validarOptIn(OptIn optIn, int diasFidelizacao)
	{
		MensagemRetorno retorno = new MensagemRetorno();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Por default, o codigo de retorno eh 1 e a mensagem de
		// retorno eh a de que o assinante ja esta cadastado desde "data OptIn ativo"
		// e o OptIn ativo nao pode ser sobreescrito
		retorno.setCodRetornoNum(Definicoes.WIG_RET_CADASTRADO);
		retorno.setCodRetorno(String.valueOf(Definicoes.WIG_RET_CADASTRADO));
		retorno.setMensagem("Voce ja esta cadastrado desde " + sdf.format(optIn.getDataOptIn()) + ". Clique OK para concluir.");
		
		// Variavel de retorno do metodo
		Calendar novaDataFidelizacao = Calendar.getInstance();
		novaDataFidelizacao.add(Calendar.DAY_OF_MONTH, diasFidelizacao);
		
		// Validacoes realizadas:
		// 1 - Se o OptIn atual NAO for mandatorio E a data de fidelizacao do OptIn ativo for
		//     inferior a data do OptIn novo, o novo OptIn sera ativado, sobreescrevendo o atual
		if ( !optIn.isMandatorio() && optIn.getDataFidelizacao().before(novaDataFidelizacao.getTime()) )
		{
			// Codigo de retorno 0 : sucesso na validacao, o OptIn ativo pode ser sobreescrito
			// Mensagem de retorno : nula (Pois a mensagem sera default pelo ShowMensagemOptIn.jsp)
			retorno.setCodRetornoNum(Definicoes.WIG_RET_OK);
			retorno.setCodRetorno(Definicoes.WIG_RET_OK_STR);
			retorno.setMensagem(null);
		}
		
		return retorno;
	}
}
 
