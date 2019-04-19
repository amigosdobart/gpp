package com.brt.gpp.aplicacoes.campanha.incentivoRecargas;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Map;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.ParamIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao;
import com.brt.gpp.aplicacoes.campanha.dao.ParamIncentivoRecargasDAO;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

public class ParamIncentivoRecargasImpl implements ParametroInscricao 
{
	private Collection 			paramsIncentivoRecargas;
	private SimpleDateFormat 	sdf = new SimpleDateFormat("dd/MM/yyyy");
	private final long 			MS_POR_DIA = 1000 * 60 * 60 * 24;
	
	public ParamIncentivoRecargasImpl(Campanha campanha)
	{
		// Na inicializacao da classe, realiza a busca de todos os
		// parametros configurados em banco de dados para esta campanha
		// de incentivo de recargas, com isso todas as threads consumidoras
		// do processo de inscricao de assinantes nao precisam buscar no
		// banco essa informacao acelerando o processo de inscricao de 
		// assinantes
		paramsIncentivoRecargas = ParamIncentivoRecargasDAO.getParamsIncentivoRecargas(campanha);
	}

	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#podeSerInscrito(com.brt.gpp.aplicacoes.aprovisionar.Assinante, com.brt.gpp.comum.conexoes.bancoDados.PREPConexao)
	 */
	public boolean podeSerInscrito(Assinante assinante, PREPConexao conexaoPrep) 
	{
		// Realiza uma iteracao entre todos os parametros cadastrados
		// para essa campanha. Para cada parametro eh verificado se o
		// assinante corresponde a todos os criterios e caso seja verdade
		// entao indica que este pode ser inscrito na campanha
		for (Iterator i=paramsIncentivoRecargas.iterator(); i.hasNext();)
		{
			ParamIncentivoRecargas param = (ParamIncentivoRecargas)i.next();
			// Verifica os critorios especificos para a campanha de incentivo de recargas
			// Verifica primeiro se o status do assinante eh o mesmo definido para a campanha
			if (assinante.getStatusAssinante() == param.getStatusAssinante())
			{
				try
				{
					// Verifica se o numero de dias para a expiracao do assinante
					// corresponde ao cadastrado para a campanha. Para isso a data
					// de expiracao eh subtraida da data atual. O numero de dias de
					// expiracao do assinante deve estar contido na faixa dadastrada
					Date dataExpiracao = sdf.parse(assinante.getDataExpiracaoPrincipal());
					Calendar dataAtual = Calendar.getInstance();
					dataAtual.set(Calendar.HOUR_OF_DAY,0);
					dataAtual.set(Calendar.MINUTE,0);
					dataAtual.set(Calendar.SECOND,0);
					dataAtual.set(Calendar.MILLISECOND,0);
					long dias = (dataExpiracao.getTime() - dataAtual.getTimeInMillis()) / MS_POR_DIA;
					if (param.getDiasExpiracaoIni() <= dias && dias <= param.getDiasExpiracaoFim())
						// Verifica agora se o assinate realizou o retorno do ciclo3 e se
						// esse possui mesmo valor do parametro definido para a campanha
						if (assinante.fezRetornoCiclo3() == param.getFezRollOut())
							return true;
				}
				catch(ParseException pe)
				{
					return false;
				}
				catch(NumberFormatException ne)
				{
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#getParametros()
	 */
	public Map getParametros()
	{
		return null;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#enviaSMS()
	 */
	public boolean enviaSMS(Assinante assinante, PREPConexao conexaoPrep) {
		return true;
	}
}
