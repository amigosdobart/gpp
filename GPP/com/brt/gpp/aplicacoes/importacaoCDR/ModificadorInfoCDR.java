package com.brt.gpp.aplicacoes.importacaoCDR;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.AssinanteCache;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorProFaleGratis;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorProPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoModulacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapAssinantesNaoBonificaveis;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapFeriados;
import com.brt.gpp.comum.mapeamentos.MapPromocaoModulacao;

/**
 * Esta classe realiza o trabalho de modificacao de campos no arquivo de CDR
 *
 * @author Joao Carlos
 * Data..: 19/10/2005
 *
 */
public class ModificadorInfoCDR extends Aplicacoes
{
	private long horaInicioPeriodo 	= 86400;
	private long horaFimPeriodo		= 0;
	private long duracaoLimite		= 86400;
	private TotalizadorProFaleGratis totFaleGratis = new TotalizadorProFaleGratis();

	public ModificadorInfoCDR(long idProcesso)
	{
		super(idProcesso,"ModificadorInfoCDR");
		try
		{
			MapConfiguracaoGPP mapConf = MapConfiguracaoGPP.getInstance();
			horaInicioPeriodo = Long.parseLong(mapConf.getMapValorConfiguracaoGPP("INI_PERIODO_TOTALIZACAO"));
			horaFimPeriodo    = Long.parseLong(mapConf.getMapValorConfiguracaoGPP("FIM_PERIODO_TOTALIZACAO"));
			duracaoLimite	  = Long.parseLong(mapConf.getMapValorConfiguracaoGPP("MAX_DURACAO_TOTALIZACAO"));
		}
		catch(Exception e){};
	}

	/**
	 * Metodo....:modificaCDR
	 * Descricao.:Realiza a modificacao nos campos do arquivo de cdr
	 * @param arqCDR		- Linha do arquivo de CDR sendo passado como parametro
	 * @param totalicadores - Lista de totalizadores de CDRs
	 * @param conexaoPrep	- Conexao de banco de dados
	 */
	public void modificaCDR(ArquivoCDR arqCDR, Collection totalizadoresCDR, PREPConexao conexaoPrep)
	{
		// Verifica se na lista de totalizadores existe a totalizacao pula pula
		// Esta classe realiza explicitamente a conversao do totalizador pula pula
		// para entao alterar as informacoes de CDR. Caso esse totalizador nao seja
		// encontrado entao nao considera que o cdr pode ser alterado tambem
		for (Iterator i=totalizadoresCDR.iterator(); i.hasNext();)
		{
			TotalizadorCDR totalizador = (TotalizadorCDR)i.next();
			// Se o totalizador for entao o totalizador do pula pula
			// verifica se deve ser totalizado para promocao pula pula
			if (totalizador instanceof TotalizadorProPulaPula)
				// Verifica se eh necessario alterar informacoes de FF, caso necessario
				// chama o metodo que realiza esta tarefa. Lembrando que as informacoes
				// de FF devem ser alteradas somente para os CDRs contabilizados para a
				// promocao PulaPula.
				if (arqCDR instanceof ArquivoCDRDadosVoz)
				{
					if ( ((TotalizadorProPulaPula)totalizador).totalizaPulaPula((ArquivoCDRDadosVoz)arqCDR) )
						// Se o CDR totalizar para o bonus pula pula entao acerta o campo FFDiscount
						// de acordo com o tipo de desconto que serah aplicado.
						// Normal
						// Friends&Family
						// Nao bonificavel
						// Tarifa Reduzida Noturno ou Diurno
						// Duracao excedida para o periodo
						// TUP
						// Fale Gratis a Noite
						// Numero A igual a B
						// Movel Nao BRT (pos-pago)
						acertaFF(arqCDR,conexaoPrep);

					// Caso o CDR for uma chamada recebida originada por um acesso pre-pago ou hibrido
					// ento este CDR deve ser alterado para nao ser visualizado no extrato pula-pula
					// uma vez que este nao foi totalizado para o pula-pula.
					else if (arqCDR.getTransactionType() == 9 || arqCDR.getTransactionType() == 11)
					{
						arqCDR.setTransactionType(String.valueOf(arqCDR.getTransactionType()*1000));
						arqCDR.setMotivoNaoBonificacao(99);
						// Caso o campo sequence_number for diferente igual a zero entao significa
						// que este CDR vem do Selective By Pass e portanto deve ser alterado inclusive
						// o tipo de chamada para nao caracterizar a RLOCAL. O CDR que possuir o sequence
						// number diferente de zero significa CDR gerado pela Tecnomen e por possuir
						// debito no saldo do assinante entao o RateName nao deve ser alterado por motivos
						// contabeis
						if ( arqCDR.getSequenceNumber() == 0 ||
							( "RLOCAL------".equals(arqCDR.getTipChamada()) || "RLOCAL_HIB--".equals(arqCDR.getTipChamada())) )
						{
							arqCDR.setRateName("14----RBNGB---------");
							arqCDR.setTipChamada("RBNGB-------");
						}
					}
				}
		}
	}

	/**
	 * Metodo....:acertaFF
	 * Descricao.:
	 *
	 * @param arqCDR		- Arquivo de CDR sendo processado
	 * @param conexaoPrep	- Conexao de banco de dados
	 */
	private void acertaFF(ArquivoCDR arqCDR, PREPConexao conexaoPrep)
	{
		// Define os originadores e destino da chamada dependendo do tipo de transacao.
		// Para os tipos 9 e 11 (chamada recebida), o originador estah no campo CallId
		// com a formatacao do numero sem o 55. No caso de chamada originada, o campo
		// de origem estah bem formatado. O valor default eh considerado uma chamada
		// originada.
		String origem  = arqCDR.getSubId();
		String destino = arqCDR.getCallId();
		if (arqCDR.getTransactionType() == 9 || arqCDR.getTransactionType() == 11)
		{
			// Inverte o originado e o destino na chamada recebida.
			origem  = !"NaoInformado".equals(arqCDR.getCallId()) ? "55"+Long.parseLong(arqCDR.getCallId()) : arqCDR.getCallId();
			destino = arqCDR.getSubId();
		}

		if (isBNGB(origem, destino, arqCDR))
			return;

		if (isAigualB(origem, destino, arqCDR))
			return;

		if (isDuracaoExcedida(arqCDR))
			return;

		if (isBlackList(origem, destino, arqCDR))
			return;

        if (isAmigosDeGraca(origem, destino, arqCDR))
            return;

		if (isFaleGratis(origem, destino, arqCDR))
			return;

		if (isBrasilVantagens(origem, destino, arqCDR))
			return;

		if (isControleTotal(origem, destino, arqCDR))
			return;

		if (isTUP(arqCDR.getOrigSubId(), arqCDR))
			return;

		if (isMovelNaoBrT(origem, arqCDR))
			return;

		// PROMOCAO DIA DAS MAES FASE 2
		
        if (isModulacaoNaoBonificavel(origem, destino, arqCDR))
            return;
	}

    /**
     * Define que o CDR nao deve ser bonificado quando a ligacao
     * se enquadrar em um intervalo nao bonificavel de modulacao de promocao.
     *
     * @param origem    - Msisdn origem
     * @param destino   - Msisdn destino
     * @param cdr       - CDR a ser alterado
     * @return boolean Se o CDR for marcado como BNGB
     */
    private boolean isModulacaoNaoBonificavel(String origem, String destino, ArquivoCDR cdr)
    {
        GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();

        try
        {
            if (!gerFF.pertenceWhiteList(destino, DescontoPulaPula.BONUS))
            {
                AssinanteCache assinante = gerFF.getAssinante(origem);
                if (assinante == null)
                    return false;

                MapPromocaoModulacao mapModulacao = MapPromocaoModulacao.getInstancia();
                PromocaoModulacao modulacao = mapModulacao.getModulacao(
                        assinante.getIdPromocao(), Integer.parseInt(origem.substring(2, 4)));

                if (modulacao != null && modulacao.isNaoBonificavel(cdr.getStartTime()))
                 {
                    cdr.setMotivoNaoBonificacao(DescontoPulaPula.BONUS);
                    return true;
                }


            }
        }
        catch(Exception e){};

        return false;
    }

	/**
	 * Metodo....:isBNGB
	 * Descricao.:Define se o CDR deve nao ser bonificado pois a ligacao
	 *            foi originada utilizando o saldo de bonus ou saldo periodico
	 * @param origem	- Msisdn origem
	 * @param destino	- Msisdn destino
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isBNGB(String origem, String destino, ArquivoCDR cdr)
	{
		GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
		try
		{
			if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
			{
				String msisdn = "55" + Long.parseLong(destino);
				if ((cdr.getBonusBalanceDelta() != 0 || cdr.getPeriodicBalanceDelta() != 0) && !gerFF.pertenceWhiteList(msisdn, DescontoPulaPula.BONUS))
				{
					cdr.setMotivoNaoBonificacao(DescontoPulaPula.BONUS);
					return true;
				}
			}
		}
		catch(Exception e){};

		return false;
	}

    /**
     * Metodo....:isAmigosDeGraca
     * Descricao.:Define se o CDR deve nao ser bonificado pois a ligacao
     *            foi originada por FGN4 (Pospago GSM) para um terminal Pre/Controle
     *            que pertence 'a lista de amigos do originador
     * @param origem    - Msisdn origem
     * @param destino   - Msisdn destino
     * @param cdr       - CDR a ser alterado
     * @return boolean
     */
    private boolean isAmigosDeGraca(String origem, String destino, ArquivoCDR cdr)
    {
        GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();

        if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
            return false;

        try
        {
            if (gerFF.isPromocaoAmigosDeGraca(origem) && !gerFF.pertenceWhiteList(destino, DescontoPulaPula.AMIGOS_GRACA))
            {
                String amigosGratis = gerFF.getAssinante(origem).getAmigosGratis();
                if (amigosGratis != null && amigosGratis.indexOf(destino) >= 0)

                {
                    cdr.setMotivoNaoBonificacao(DescontoPulaPula.AMIGOS_GRACA);
                    return true;
                }
            }
        }
        catch(Exception e){};

        return false;
    }

	/**
	 * Metodo....:defineAigualB
	 * Descricao.:Define se o CDR deve nao ser bonificado, pois o originador
	 *            eh igual ao destino da chamada.
	 * @param origem	- Msisdn origem
	 * @param destino	- Msisdn destino
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isAigualB(String origem, String destino, ArquivoCDR cdr)
	{
		if ( !"NaoInformado".equals(origem) && origem.equals(destino) )
		{
			cdr.setMotivoNaoBonificacao(DescontoPulaPula.AIGUALB);
			return true;
		}
		return false;
	}

	/**
	 * Metodo....:isDuracaoExcedida
	 * Descricao.:Identifica se a chamada teve duracao excedida ao limite configurado
	 * @param inicioChamada	- Inicio da chamada
	 * @param duracao		- Duracao da chamada
	 * @param cdr			- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isDuracaoExcedida(ArquivoCDR cdr)
	{
		if ((cdr.getStartTime() > horaInicioPeriodo || cdr.getStartTime() < horaFimPeriodo) && cdr.getCallDuration() > duracaoLimite)
		{
			cdr.setMotivoNaoBonificacao(DescontoPulaPula.DURACAO_EXCEDIDA);
			return true;
		}
		return false;
	}

	/**
	 * Metodo....:isTUP
	 * Descricao.:Identifica se a chamada recebida foi proveniente de um TUP.
	 * @param origem	- Origem (este campo eh o origSubId do CDR)
	 * @param duracao	- Duracao da chamada
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isTUP(String origem, ArquivoCDR cdr)
	{
		if (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11)
			if ("TUP".equals(origem) && cdr.getCallDuration() > getDuracaoMaximaTup(cdr.getTimestamp(),cdr.getStartTime()))
			{
				cdr.setMotivoNaoBonificacao(DescontoPulaPula.TUP);
				return true;
			}
		return false;
	}

	/**
	 * Metodo....:isBlackList
	 * Descricao.:Identifica se o originador da chamada estah retido em black list
	 * @param origem	- Msisdn do originador da chamada
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isBlackList(String origem, String destino, ArquivoCDR cdr)
	{
        GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();

        try
        {
            if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
                destino =  "55" + Long.parseLong(destino);

            if (!"NaoInformado".equals(origem) && MapAssinantesNaoBonificaveis.getInstance(0).descartaAssinante(origem) &&
                    !(gerFF.pertenceWhiteList(destino, DescontoPulaPula.BLACK_LIST)))
            {
                cdr.setMotivoNaoBonificacao(DescontoPulaPula.BLACK_LIST);
                return true;
            }
        }
        catch (Exception ignored){}

		return false;
	}

	/**
	 * Metodo....:isFaleGratis
	 * Descricao.:Identifica se a ligacao foi originada por um assinante participante da promocao
	 *            fale gratis a noite utilizando as regras da mesma definida na classe TotalizadorProFaleGratis
	 * @param origem - Msisdn do originador da chamada
	 * @param cdr	 - CDR a ser alterado
	 * @return boolean
	 */
	private boolean isFaleGratis(String origem, String destino, ArquivoCDR cdr)
	{
		// Define o fuso horario a ser aplicado para verificacao
		// da promocao Fale Gratis a Noite. Para o CDR de chamada
		// recebida, o horario vem com a hora de Brasilia mesmo se
		// a regiao aplicar fuso horario. No CDR de chamada originada
		// este problema eh corrigido pela plataforma Tecnomen e a informacao
		// jah vem com o horario correto.
		int fuso = (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11) ? totFaleGratis.getFusoHorario(origem) : 0;
        GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();

        try
        {
            if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
                destino =  "55" + Long.parseLong(destino);

            if (totFaleGratis.utilizouFaleGratis(origem, fuso, cdr) &&
                    !(gerFF.pertenceWhiteList(destino, DescontoPulaPula.FALE_GRACA)))
            {
                cdr.setMotivoNaoBonificacao(DescontoPulaPula.FALE_GRACA);
                return true;
            }
        }
        catch (Exception ignored){}

		return false;
	}

	/**
	 * Metodo....:isBrasilVantagens
	 * Descricao.:Identifica se a ligacao originada foi utilizada
	 *            para um numero utilizando amigos toda hora.
	 * @param origem	- Numero de origem da chamada
	 * @param destino	- Numero de destino da chamada
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	public boolean isBrasilVantagens(String origem, String destino, ArquivoCDR cdr)
	{
		GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
		// Verifica se a chamada eh uma chamada originada e se estah marcada
		// indicando o desconto aplicado pela Tecnomen de Friends & Family
		// Para marcar o uso do BrasilVantagens eh necessario verificar se o
		// numero que estah recebendo a chamada pertence a white list para
		// este tipo de desconto.
		if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
			try
			{
				String msisdn = "55" + Long.parseLong(destino);
				if (cdr.getFfDiscount() != 0 &&
						!(gerFF.pertenceWhiteList(msisdn, DescontoPulaPula.ATH2)
						  || gerFF.pertenceWhiteList(msisdn, DescontoPulaPula.ATH) ))
				{
					// Verifica se o plano de preco do originador da chamada identifica
					// a nova configuracao do amigos toda hora para o novo brasilVantagens
					if (cdr.getProfileId() > 9)
						cdr.setMotivoNaoBonificacao(DescontoPulaPula.ATH2);
					else
						cdr.setMotivoNaoBonificacao(DescontoPulaPula.ATH);

					return true;
				}
			}
			catch(Exception e){};

		return false;
	}

	/**
	 * Metodo....:isMovelNaoBrT
	 * Descricao.:Identifica se o originador da chamada eh um movel nao BrT.
	 * @param origem	- Msisdn do originador da chamada
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	public boolean isMovelNaoBrT(String origem, ArquivoCDR cdr)
	{
		// Verifica se a chamada foi originada por um terminal Movel Nao BrT. Isso
		// eh para a bonificacao de POS-PAGO. Por enquanto essa totalizacao nao
		// deve ser verificada para o PRE-PAGO, por isso fica somente apos todas as
		// validacoes anteriores serem executadas.
		if (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11)
			if (isMovelNaoBrT(origem))
			{
				cdr.setMotivoNaoBonificacao(DescontoPulaPula.MOVEL_OFFNET);
				return true;
			}
		return false;
	}

	/**
	 * Metodo....:isControleTotal
	 * Descricao.:Define se o CDR deve nao ser bonificado, pois a origem eh de terminal ControleTotal.
	 * @param origem	- Msisdn origem
	 * @param destino	- Msisdn destino
	 * @param cdr		- CDR a ser alterado
	 * @return boolean
	 */
	private boolean isControleTotal(String origem, String destino, ArquivoCDR cdr)
	{
		GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();

		if (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11)
			try
			{
				if (gerFF.isControleTotal(origem) && !gerFF.pertenceWhiteList(destino, DescontoPulaPula.CT))
				{
					cdr.setMotivoNaoBonificacao(DescontoPulaPula.CT);
					return true;
				}
			}
			catch(Exception ignored){};

		return false;
	}

	/**
	 * Metodo....:getDuracaoMaximaTup
	 * Descricao.:Retorna a maxima duracao de uma chamada TUP permitida
	 * @param dataCDR - Data do CDR
	 * @return long - Duracao maxima permitida para TUP
	 */
	public long getDuracaoMaximaTup(Date dataCDR, long startTime)
	{
		// Define como duracao maxima para TUP default uma chamada de 15 minutos
		long duracaoMaxima = 990;
		// As chamadas realizadas entre 7:00 e 21:00 serah consideradas para uma
		// duracao maxima de 15 minutos exceto se for um feriado ou domingo. Para
		// as outras situacoes eh considerado a duracao de 16 minutos e 30 segundos
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataCDR);
		// 25200 segundos desde a meia noite representa 7:00 hs da manha
		// 75600 segundos desde a meia noite representa 21:00hs da noite
		if (!MapFeriados.getInstance().isFeriado(dataCDR))
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
				if (startTime > 25200 && startTime < 75600)
					duracaoMaxima = 900;

		return duracaoMaxima;
	}

	/**
	 * Metodo....:isMovelNaoBrT
	 * Descricao.:Identifica se o numero do assinante passado como parametro
	 *            eh um terminal movel nao BrasilTelecom
	 * @param msisdn - Msisdn
	 * @return boolean - Indica se o terminal eh um acesso movel nao Brt
	 */
	private boolean isMovelNaoBrT(String msisdn)
	{
		// Para verificar se eh um acesso movel nao Brt,
		// verifica-se se nao eh um acesso movel Brt e
		// se nao eh um terminal fixo (BrT ou nao-BrT).
		if (!isMovelBrT(msisdn) && !isTerminalFixo(msisdn))
			return true;

		return false;
	}

	/**
	 * Metodo....:isMovelBrT
	 * Descricao.:Identifica se o numero de acesso eh um movel BrasilTelecom. Essa verificacao
	 *            eh feita verificando se o numero possui a mascara de um telefone BrasilTelecom
	 *            (55XX84*)
	 * @param msisdn - Msisdn a ser verificado
	 * @return boolean - se o numero eh um movel BrT
	 */
	private boolean isMovelBrT(String msisdn)
	{
		return msisdn.matches(Definicoes.MASCARA_GSM_BRT_REGEX);
	}

	/**
	 * Metodo....:isTerminalFixo
	 * Descricao.:Identifica se o numero de terminal passado como parametro pertence a uma mascara
	 *            especifica (55XX[2 ou 3 ou 4 ou 5 ou6]*
	 * @param terminal - Numero do terminal a ser verificado
	 * @return boolean - se o numero eh um terminal fixo
	 */
	private boolean isTerminalFixo(String terminal)
	{
		return terminal.matches(Definicoes.MASCARA_TERMINAL_FIXO_REGEX);
	}
}