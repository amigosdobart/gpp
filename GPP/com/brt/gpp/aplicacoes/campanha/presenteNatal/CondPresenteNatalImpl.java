package com.brt.gpp.aplicacoes.campanha.presenteNatal;

import com.brt.gpp.aplicacoes.campanha.dao.CondIncentivoRecargasDAO;
import com.brt.gpp.aplicacoes.campanha.dao.RecargaDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.campanha.entidade.InfoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.persistencia.Operacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

public class CondPresenteNatalImpl implements CondicaoConcessao
{
	private CondIncentivoRecargas condSatisfeita;
	private InfoRecarga somatorioRecargas;
	private DecimalFormat df = new DecimalFormat("#,##0.00");

	public CondPresenteNatalImpl(Campanha campanha)
	{
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#deveSerBonificado(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha, com.brt.gpp.comum.conexoes.bancoDados.PREPConexao)
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		boolean deveBonificar = false;
		// Para o assinante receber o bonus da campanha. Este deve ter acumulado um valor X
		// em recargas em um determinado periodo. Portanto para a concessao eh preciso
		// validar as recargas efetuadas pelo assinante e entao buscar o valor de bonus
		// associado a este valor.
		
		// Portanto realiza neste ponto a pesquisa do somatorio de recargas
		InfoRecarga infoRecarga = RecargaDAO.getSomatorioRecargas(assinante.getMsisdn()
																 ,assinante.getCampanha().getValidadeIniConcessao()
																 ,assinante.getCampanha().getValidadeFimConcessao()
																 ,conexaoPrep);
		
		// Realiza a pesquisa do valor de bonificacao correspondente ao somatorio
		// de recargas efetuadas pelo assinante. O valor serah o mais aproximado
		// utilizando o SELECT para retornar este valor.
		condSatisfeita = CondIncentivoRecargasDAO.getCondIncentivoRecargasAprox(assinante.getCampanha()
				                                                               ,infoRecarga.getValorRecargas()
				                                                               ,conexaoPrep);
		// Caso a condicao seja nula entao nao foi encontrado
		// o registro correspondente. Caso entao seja diferente
		// este valor serah utilizado como referencia para a
		// bonificacao
		if (condSatisfeita != null)
			deveBonificar = true;
		
		return deveBonificar;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#executarPosBonificacao(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha, com.brt.gpp.comum.conexoes.bancoDados.PREPConexao)
	 */
	public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		// Para garantir que o assinante utilize o bonus por 30 dias
		// o sistema realiza uma modificacao na configuracao pula pula
		// do assinante para identificar que o valor nao deve ser zerado
		// Apesar deste plug-in poder ser utilizado por qualquer campanha
		// somente as campanhas definidas neste metodo realizam esta
		// modificao. Isto devido campanhas que nao podem permitir este
		// adiamento do "zerar" saldo de bonus
		//if (assinante.getCampanha() == ?)
		//{
			try
			{
				Operacoes op = new Operacoes(0);
				op.atualizaStatus(assinante.getMsisdn(), PromocaoStatusAssinante.STATUS_ATIVO_RECARGA, conexaoPrep);
			}
			catch(Exception e)
			{
				GerentePoolLog.getInstancia(CondPresenteNatalImpl.class).log(0
						 ,Definicoes.ERRO,"CondPresenteNatalImpl","executarPosBonificacao"
						 ,"Erro ao executar pos bonificacao de campanha para o assinante: " + assinante.getMsisdn() + ". Erro:"+e);
			}
		//}
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getNomeCondicao()
	 */
	public String getNomeCondicao()
	{
		// Identifica o valor da recarga da condicao que foi
		// satisfeita para a concessao de credito, se essa
		// condicao for nula entao retorna uma string nula
		// tambem
		if (condSatisfeita != null)
			return df.format(somatorioRecargas.getValorRecargas());
		
		return null;
	}

	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederBonus()
	 */
	public double getValorConcederBonus()
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonus();
		
		return 0;
	}

	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederSM()
	 */
	public double getValorConcederSM()
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonusSM();
		return 0;
	}

	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederDados()
	 */
	public double getValorConcederDados()
	{
		if (condSatisfeita != null)
			return condSatisfeita.getValorBonusDados();
		return 0;
	}

	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getDataSatisfacaoCondicao()
	 */
	public Date getDataSatisfacaoCondicao()
	{
		return Calendar.getInstance().getTime();
	}
}

