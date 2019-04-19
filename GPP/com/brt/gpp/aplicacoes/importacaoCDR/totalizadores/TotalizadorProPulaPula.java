package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.importacaoCDR.GerenciadorCacheFF;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.aplicacoes.promocao.entidade.DescontoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe realiza a implementacao da interface de Totalizador para a 
 * totalizacao de informacoes a respeito do bonus pula pula.
 * 
 *  Atualizado por: Bernardo Vergne Dias (Adicionado ControleTotal)
 *  Data: 25/10/2007
 */
public class TotalizadorProPulaPula implements TotalizadorCDR 
{	
	protected static GerentePoolLog log = null; // Gerente de LOG
	
	private boolean totalizaPulaPula = false;
	private TotalizadorProFaleGratis totFGN = new TotalizadorProFaleGratis();
	private SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	private static final String SQLUPD = "update tbl_pro_totalizacao_pula_pula " +
								    "set num_segundos_total           = num_segundos_total + ? " +
									    ",num_segundos_ff              = num_segundos_ff    + ? " +
									    ",num_segundos_plano_noturno   = num_segundos_plano_noturno  + ? " +
									    ",num_segundos_plano_diurno    = num_segundos_plano_diurno   + ? " +
									    ",num_segundos_nao_bonificado  = num_segundos_nao_bonificado + ? " +
									    ",num_segundos_durac_excedida  = num_segundos_durac_excedida + ? " +
									    ",num_segundos_tup             = num_segundos_tup            + ? " +
									    ",num_segundos_aigualb		   = num_segundos_aigualb		 + ? " +
									    ",num_segundos_ath		   	   = num_segundos_ath		 	 + ? " +
									    ",num_segundos_movel_nao_brt   = num_segundos_movel_nao_brt  + ? " +
									    ",num_segundos_falegratis      = num_segundos_falegratis     + ? " +
									    ",num_segundos_bonus 		   = num_segundos_bonus          + ? " +
									    ",num_segundos_ct 		   	   = num_segundos_ct             + ? " +
								  "where idt_msisdn = ? " +
								    "and dat_mes    = ? ";

	private static final String SQLINS = "insert into tbl_pro_totalizacao_pula_pula " +
						                 "(dat_mes " +
							              ",idt_msisdn " +
							              ",num_segundos_total " +
							              ",num_segundos_ff " +
							              ",num_segundos_plano_noturno  " +
							              ",num_segundos_plano_diurno   " +
							              ",num_segundos_nao_bonificado " +
							              ",num_segundos_durac_excedida " +
							              ",num_segundos_tup " +
							              ",num_segundos_aigualb " +
							              ",num_segundos_ath " +
							              ",num_segundos_movel_nao_brt " +
							              ",num_segundos_falegratis " +
							              ",num_segundos_bonus " +
							              ",num_segundos_ct " +
							              ") " +
							       "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public TotalizadorProPulaPula() 
	{
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#deveTotalizar(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR)
	 */
	public boolean deveTotalizar(ArquivoCDR arqCDR) 
	{
		if (!(arqCDR instanceof ArquivoCDRDadosVoz))
			return false;

		ArquivoCDRDadosVoz cdr = (ArquivoCDRDadosVoz)arqCDR;
		this.totalizaPulaPula = totalizaPulaPula(cdr);
		return this.totalizaPulaPula;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#getTotalizado(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR, com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado)
	 */
	public Totalizado getTotalizado(ArquivoCDR arqCDR, Totalizado totalizado)
	{
		// Define a origem e o fuso baseando na informacao de chamada recebida ou originada.
		String origem  = arqCDR.getSubId();
		
		String destino = arqCDR.getCallIdFormatado();
		Long.parseLong(destino);
		
		if (arqCDR.getTransactionType() == 9 || arqCDR.getTransactionType() == 11)
		{
			// Inverte o originado e o destino na chamada recebida.
			origem  = "NaoInformado".equals(arqCDR.getCallId()) ? "NaoInformado" : destino;
			destino = arqCDR.getSubId();
		}

		// Realiza o cast do arquivo de CDR para o arquivo de dados voz
		// que deve ser o arquivo sendo processado por esse totalizador
		// e tambem o cast do objeto totalizado para o TotalizacaoPulaPula
		ArquivoCDRDadosVoz  cdr     = (ArquivoCDRDadosVoz)arqCDR;
		TotalizacaoPulaPula totPula = (TotalizacaoPulaPula)totalizado;

		// Caso o objeto totalizado seja passado como nulo no parametro 
		// entao cria a instancia do objeto que armazenarah os valores
		// do CDR atual. Quando esse objeto for diferente de nulo entao
		// nenhuma nova instancia eh criada e sim somente atualizada
		// no objeto atual somando os valores. Veja que se a data mes
		// do CDR (Chave para a Totalizacao) for diferente entre o objeto
		// totalizado e o CDR entao cria-se tambem uma nova instancia do
		// objeto totalizado afim de evitar somatoria em periodos diferentes
		// para a promocao pula pula
		if ( totalizado == null || !sdf.format(cdr.getTimestamp()).equals(totPula.getDatMes()) )
		{
			totPula = new TotalizacaoPulaPula();
			totPula.setDatMes	(cdr.getTimestamp());
			totPula.setIdtMsisdn(destino);
		}
		// O valor total de segundos sempre eh adicionado
		totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_TOTAL,cdr.getCallDuration());
		// Verifica os tipos de nao bonificacao definidos para o CDR
		// Para cada tipo realiza ou nao o somatorio na coluna correspondente
		if (totalizaNaoBonificado(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_NAOBONIFICADO,cdr.getCallDuration());
		if (totalizaFriendsFamily(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_FF,cdr.getCallDuration());
		if (totalizaTarifaReduzidaDiurno(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_DIURNO,cdr.getCallDuration());
		if (totalizaTarifaReduzidaNoturno(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_NOTURNO,cdr.getCallDuration());
		if (totalizaDuracaoExcedida(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_DURACAOEXCEDIDA,cdr.getCallDuration());
		if (totalizaTupExcedido(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_TUP,cdr.getCallDuration());
		if (totalizaAigualB(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_AIGUALB,cdr.getCallDuration());
		if (totalizaAmigosTodaHora(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_AMIGOSTODAHORA,cdr.getCallDuration());
		if (totalizaMovelNaoBrT(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_MOVELNAOBRT,cdr.getCallDuration());
		if (totalizaBonus(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_BONUS, getDuracaoUtilizacaoBonus(cdr));
		if (totalizaCT(cdr))
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_CT,cdr.getCallDuration());
		
		// Para a totalizacao do fale gratis eh necessario utilizar um metodo
		// na classe TotalizadorProFaleGratis que irah identificar a quantidade
		// correta de segundos nao bonificados devido ao fale gratis. Essa duracao
		// eh dependente da informacao do fuso horario que nao vem correto no CDR
		// de chamadas recebidas, entao eh necessario a pesquisa do fuso da regiao
		// do assinante que originou a chamada.
		if (totalizaFaleGratis(cdr))
		{
			int fuso = (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11) ? totFGN.getFusoHorario(origem) : 0;
			totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_FALEGRATIS
					   ,totFGN.getDuracaoFaleGratis(cdr.getStartTime(),cdr.getCallDuration(),origem,fuso));
		}
        
        // A ligacoes de FGN4 para um amigo de graça sao acumuladas no totalizador de FGN.
        // Toda a ligação nao deve ser bonificada.
        if (totalizaAmigosDeGraca(cdr))
            totPula.add(TotalizacaoPulaPula.NUM_SEGUNDOS_FALEGRATIS, cdr.getCallDuration());
		
		return totPula;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#persisteTotalilzado(com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado)
	 */
	public void persisteTotalizado(Totalizado totalizado, PREPConexao conexaoDB) throws GPPInternalErrorException
	{
		// Executa o metodo de atualizacao em banco de dados de modo sincronizado
		TotalizadorProPulaPula.atualizaTabela(totalizado,conexaoDB);
	}
	
	/**
	 * Metodo....:atualizaTabela
	 * Descricao.:Realiza a atualizacao em tabela da totalizacao feita em memoria pela carga de cdrs
	 *            Este metodo esta a parte para que esse seja sincronizado evitando que duas threads
	 *            tentem inserir o mesmo assinante na tabela e sim que somente um insira e a outra jah
	 *            faca a atualizacao
	 * @param totalizado	- Objeto totalizado a ser persistido
	 * @param conexaoDB		- Conexao com o banco de dados
	 * @throws GPPInternalErrorException
	 */
	public static synchronized void atualizaTabela(Totalizado totalizado, PREPConexao conexaoDB) throws GPPInternalErrorException
	{
		if (totalizado instanceof TotalizacaoPulaPula)
		{
			TotalizacaoPulaPula totalPulaPula = (TotalizacaoPulaPula)totalizado;
			
			Object paramUpd[] = {totalPulaPula.getNumSegundosTotal()
								,totalPulaPula.getNumSegundosFF()
								,totalPulaPula.getNumSegundosNoturno()
								,totalPulaPula.getNumSegundosDiurno()
								,totalPulaPula.getNumSegundosNaoBonificado()
								,totalPulaPula.getNumSegundosDuracaoExcedida()
								,totalPulaPula.getNumSegundosTup()
								,totalPulaPula.getNumSegundosAIgualB()
								,totalPulaPula.getNumSegundosATH()
								,totalPulaPula.getNumSegundosMovelNaoBrt()
								,totalPulaPula.getNumSegundosFaleGratis()
								,totalPulaPula.getNumSegundosBonus()
								,totalPulaPula.getNumSegundosCT()
								,totalPulaPula.getIdtMsisdn()
								,totalPulaPula.getDatMes()
								};
			
			// Executa o comando para atualizacao do registro
			int numLinhas = 0;
			
			try
			{
				numLinhas = conexaoDB.executaPreparedUpdate(TotalizadorProPulaPula.SQLUPD,paramUpd,0);
			}
			catch (Exception e)
			{
				log.log(-1, Definicoes.ERRO, "TotalizadorProPulaPula", "atualizaTabela", 
				"Erro ao executar update. Parametros: " +
				"NUM_SEGUNDOS_TOTAL=" + paramUpd[0] + 
				", NUM_SEGUNDOS_FF=" + paramUpd[1] + 
				", NUM_SEGUNDOS_NOTURNO=" + paramUpd[2] + 
				", NUM_SEGUNDOS_DIURNO=" + paramUpd[3] + 
				", NUM_SEGUNDOS_NAO_BONIF=" + paramUpd[4] + 
				", NUM_SEGUNDOS_DURACAO_EXCEDIDA=" + paramUpd[5] + 
				", NUM_SEGUNDOS_TUP=" + paramUpd[6] + 
				", NUM_SEGUNDOS_A_IGUAL_B=" + paramUpd[7] + 
				", NUM_SEGUNDOS_ATH=" + paramUpd[8] + 
				", NUM_SEGUNDOS_MOVEL_NAO_BRT=" + paramUpd[9] + 
				", NUM_SEGUNDOS_FALE_GRATIS=" + paramUpd[10] + 
				", NUM_SEGUNDOS_BONUS=" + paramUpd[11] + 
				", NUM_SEGUNDOS_CT=" + paramUpd[12] + 
				", MSISDN=" + paramUpd[13] + 
				", DATMES=" + paramUpd[14] + 
				". Erro:" + e);
				throw new GPPInternalErrorException(log.traceError(e));
			}			
			
			// Caso o numero de linhas atualizadas seja igual a 0 entao significa que o
			// registro para o assinante nao existe entao cria o registro com o comando
			// insert
			if (numLinhas == 0)
			{
				Object paramIns[] = {totalPulaPula.getDatMes()
									,totalPulaPula.getIdtMsisdn()
									,totalPulaPula.getNumSegundosTotal()
									,totalPulaPula.getNumSegundosFF()
									,totalPulaPula.getNumSegundosNoturno()
									,totalPulaPula.getNumSegundosDiurno()
									,totalPulaPula.getNumSegundosNaoBonificado()
									,totalPulaPula.getNumSegundosDuracaoExcedida()
									,totalPulaPula.getNumSegundosTup()
									,totalPulaPula.getNumSegundosAIgualB()
									,totalPulaPula.getNumSegundosATH()
									,totalPulaPula.getNumSegundosMovelNaoBrt()
									,totalPulaPula.getNumSegundosFaleGratis()
									,totalPulaPula.getNumSegundosBonus()
									,totalPulaPula.getNumSegundosCT()
									};
				
				try
				{
					conexaoDB.executaPreparedUpdate(TotalizadorProPulaPula.SQLINS,paramIns,0);
				}
				catch (Exception e)
				{
					log.log(-1, Definicoes.ERRO, "TotalizadorProPulaPula", "atualizaTabela", 
					"Erro ao executar insert. Parametros: " +
					"NUM_SEGUNDOS_TOTAL=" + paramIns[2] + 
					", NUM_SEGUNDOS_FF=" + paramIns[3] + 
					", NUM_SEGUNDOS_NOTURNO=" + paramIns[4] + 
					", NUM_SEGUNDOS_DIURNO=" + paramIns[5] + 
					", NUM_SEGUNDOS_NAO_BONIF=" + paramIns[6] + 
					", NUM_SEGUNDOS_DURACAO_EXCEDIDA=" + paramIns[7] + 
					", NUM_SEGUNDOS_TUP=" + paramIns[8] + 
					", NUM_SEGUNDOS_A_IGUAL_B=" + paramIns[9] + 
					", NUM_SEGUNDOS_ATH=" + paramIns[10] + 
					", NUM_SEGUNDOS_MOVEL_NAO_BRT=" + paramIns[11] + 
					", NUM_SEGUNDOS_FALE_GRATIS=" + paramIns[12] + 
					", NUM_SEGUNDOS_BONUS=" + paramIns[13] + 
					", NUM_SEGUNDOS_CT=" + paramIns[14] + 
					", MSISDN=" + paramIns[1] + 
					", DATMES=" + paramIns[0] + 
					". Erro:" + e);
					throw new GPPInternalErrorException(log.traceError(e));
				}
			}
		}
	}
	/**
	 * Metodo....:totalizaPulaPula
	 * Descricao.:Retorna se o CDR atual pode ser totalizavel na promocao
	 * @return boolean
	 */
	public boolean totalizaPulaPula(ArquivoCDRDadosVoz cdr)
	{
		GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
		// Verifica se for uma chamada entrante. Caso seja entao verifica
		// se o tipChamada corresponde aos tipos permitidos na promocao
		if (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11)
			if (cdr.getTipChamada() != null)
				// Para a verificacao dos tipos de chamadas as chamadas locais devem obrigatoriamente vir com o campo
				// sequence number igual a 0 (devido ser cdrs provindos do SelectiveByPass). Os outros tipos de chamada
				// como as chamadas recebidas em roaming entao tais chamadas deverao ser obrigatoriamente vindos da 
				// plataforma Tecnomen (devido a tarifacao) portanto o sequence number obrigatoriamente deve vir diferente
				// de zero.
				if ( ((cdr.getTipChamada().equals("RLOCAL------")||cdr.getTipChamada().equals("RLOCAL_HIB--"))&&cdr.getSequenceNumber() == 0) ||
						((cdr.getTipChamada().equals("RVC2MMI-----")||cdr.getTipChamada().equals("RVC3MMI-----"))&&cdr.getSequenceNumber() != 0)
				)
				{
					if (cdr.getCallId().equals("NaoInformado"))
						throw new IllegalArgumentException("Originador da chamada nao informado. CDR entrante descartado.");
					
					String origem = "55"+Long.parseLong(cdr.getCallId());
					
					// Verifica se o numero originado eh um numero da Brasil Telecom GSM. Para
					// numeros nao GSM ou nao BrT o CDR eh importado normalmente.
					if (origem.matches(Definicoes.MASCARA_GSM_BRT_REGEX) )
					{
						// Para numeros da BrT GSM somente o plano pos-pago deve ser totalizado para 
						// a promocao pula-pula atraves da chamada recebida.
						if ( !gerFF.isPrepagoOuControle(origem) )
							return true;
					}
					else
						// Essa condicao acontece os casos: 
						// - BrT Fixo
						// - Fixo/GSM (outras operadoras)
						return true;
				}
		
		// Verifica agora se eh uma chamada originada. Esta implementacao eh para
		// a realizacao da implementacao do Bonus Nao Gera Bonus devido as chamadas
		// recebidas de pre-pago e controle nao mais serem utilizadas para a totalizacao 
		// pula-pula.
		// Somente chamadas de arquivos de VOZ sao verificadas. Os outros tipos
		// MT, MO, MMS e GPRS sao importados normalmente sem nenhuma interferencia
		// de pula-pula
		if (Definicoes.IND_CHAMADAS_VOZ.equals(cdr.getTipCdr()))
			if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
				// Verifica se o originador eh um terminal movel BrT. Isso eh realizado
				// pois os CDRs podem conter originadores Fixo como LigMix e ControleTotal
				if (cdr.getSubId().matches(Definicoes.MASCARA_GSM_BRT_REGEX))
					if (cdr.getTipChamada() != null)
						// Verifica se o numero de destino da chamada originada eh um
						// movel brt (pre/controle/pos). 
						try
						{
                            String destino = cdr.getCallIdFormatado();                           
                            if (destino.matches(Definicoes.MASCARA_GSM_BRT_REGEX))
                            	return true;
						}
						catch(Exception e){};
		
		return false;
	}
	
	/**
	 * Metodo....:getDuracaoUtilizacaoBonus
	 * Descricao.:Este metodo retorna o valor da duracao na chamada relativo
	 *            a utilizacao do saldo de bonus.
	 * @param cdr - CDR a ser processado
	 * @return long
	 */
	private long getDuracaoUtilizacaoBonus(ArquivoCDRDadosVoz cdr)
	{
		if (cdr.getTransactionType() == 9 || cdr.getTransactionType() == 11)
			return cdr.getCallDuration();
		
		// O código abaixo acontece apenas para CDR saintes (pre/controle). ControleTotal eh tratado no CDR entrante (9/11).
		
		double accountBalanceDelta  = cdr.getAccountBalanceDelta() * 1.;
		double bonusBalanceDelta	= cdr.getBonusBalanceDelta()   * 1.;
		double periodicBalanceDelta	= cdr.getPeriodicBalanceDelta()* 1.;
		
		double totalBalanceDelta = bonusBalanceDelta + periodicBalanceDelta + accountBalanceDelta;
		
		if (totalBalanceDelta == 0)
			return cdr.getCallDuration();

		// Encontra o percentual consumido do saldo de bonus e periodico relativos ao total tarifado.
		double perc = (bonusBalanceDelta + periodicBalanceDelta) * 100. / totalBalanceDelta;

		// Realiza entao o rateio da duracao da chamada 
		return (long)(perc * cdr.getCallDuration() / 100.);
	}
	
	/**
	 * Metodo....:totalizaFriendsFamily
	 * Descricao.:Retorna se o CDR atual pode ser totalizavel no que diz respeito ao friends and family
	 * @return boolean
	 */
	public boolean totalizaFriendsFamily(ArquivoCDRDadosVoz cdr)
	{
		// As mesmas condicoes de CDR para a totalizacao pula pula serao validas aqui com a adicao
		// de que serah verificado se o numero que originou a chamada possui o destino na lista de
		// amigos toda hora, se for encontrado entao o total de minutos serah utilizado depois para
		// diminuir o valor de bonus que o assinante terah direito.
		// Verifica se for uma chamada sainte. Caso seja entao verifica
		// se o tipChamada corresponde aos tipos permitidos na promocao
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.ATH);
	}
	
	/**
	 * Metodo....:totalizaTarifaReduzidaNoturno
	 * Descricao.:Retorna se o CDR atual pode ser totalizavel no que diz respeito a uma chamada utilizando
	 *            tarifa reduzida no plano noturno
	 * @return boolean
	 */
	public boolean totalizaTarifaReduzidaNoturno(ArquivoCDRDadosVoz cdr)
	{
		// As mesmas condicoes de CDR para a totalizacao pula pula serao validadas aqui com a adicao
		// de que a totalizacao Friends and Family nao pode estar ligado, caso for FF entao utiliza 
		// somente esta totalizacao, senao ai realiza a verificacao se a ligacao recebida foi realizada
		// utilizando tarifacao reduzida. Na modificacao do CDR o mesmo foi alterado para indicar esse
		// comportamento no campo FFDiscount
		return (this.totalizaPulaPula && !totalizaFriendsFamily(cdr) && (cdr.getMotivoNaoBonificacao()==DescontoPulaPula.NOTURNO));
	}
	
	/**
	 * Metodo....:totalizaTarifaReduzidaDiurno
	 * Descricao.:Retorna se o CDR atual pode ser totalizavel no que diz respeito a uma chamada utilizando
	 *            tarifa reduzida no plano diurno
	 * @return boolean
	 */
	public boolean totalizaTarifaReduzidaDiurno(ArquivoCDRDadosVoz cdr)
	{
		// As mesmas condicoes de CDR para a totalizacao pula pula serao validadas aqui com a adicao
		// de que a totalizacao Friends and Family nao pode estar ligado, caso for FF entao utiliza 
		// somente esta totalizacao, senao ai realiza a verificacao se a ligacao recebida foi realizada
		// utilizando tarifacao reduzida. Na modificacao do CDR o mesmo foi alterado para indicar esse
		// comportamento no campo FFDiscount
		return (this.totalizaPulaPula && !totalizaFriendsFamily(cdr) && (cdr.getMotivoNaoBonificacao()==DescontoPulaPula.DIURNO));
	}
	
	/**
	 * Metodo....:totalizaNaoBonificado
	 * Descricao.:Retorna se esse cdr deve ser totalizado pela informacao da chamada nao bonificada
	 * @return boolean
	 */
	public boolean totalizaNaoBonificado(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.BLACK_LIST);
	}
	
	/**
	 * Metodo....:totalizaDuracaoExcedida
	 * Descricao.:Retorna se esse cdr deve ser totalizado pela informacao da chamada com duracao excedida
	 * @return boolean
	 */
	public boolean totalizaDuracaoExcedida(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.DURACAO_EXCEDIDA);
	}
	
	/**
	 * Metodo....:totalizaTupExcedido
	 * Descricao.:Retorna se esse cdr deve ser totalizado pela informacao de chamada recebida por TUP com
	 *            duracao excedida a 15 minutos
	 * @return boolean
	 */
	public boolean totalizaTupExcedido(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.TUP);
	}
	
	/**
	 * Metodo....:totalizaAigualB
	 * Descricao.:Retorna true se esse CDR deve ser totalizado pela informacao de chamada onde o chamador
	 *            eh igual ao receptor
	 * @return boolean
	 */
	public boolean totalizaAigualB(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.AIGUALB);
	}
	
	/**
	 * Metodo....:totalizaAmigosTodaHora
	 * Descricao.:Retorna true se esse CDR deve ser totalizado pela informacao do novo amigos toda hora
	 * @return boolean
	 */
	public boolean totalizaAmigosTodaHora(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.ATH2);
	}
	
	/**
	 * Metodo....:totalizaMovelNaoBrT
	 * Descricao.:Retorna true se esse CDR deve ser totalizado por chamada proveniente de um acesso movel nao BrT
	 * @param cdr
	 * @return boolean
	 */
	public boolean totalizaMovelNaoBrT(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.MOVEL_OFFNET);
	}
	
	/**
	 * Metodo....:totalizaFaleGratis
	 * Descricao.:Retorna true se esse CDR deve ser totalizado por chamada proveniente de promocao fale gratis
	 * @param cdr
	 * @return boolean
	 */
	public boolean totalizaFaleGratis(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.FALE_GRACA);
	}
	
	/**
	 * Metodo....:totalizaBonus
	 * Descricao.:Retorna true se esse CDR deve ser totalizado por chamada proveniente de bonus
	 * @param cdr
	 * @return boolean
	 */
	public boolean totalizaBonus(ArquivoCDRDadosVoz cdr)
	{
		// Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
		// portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
		// a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.BONUS);
	}
	
	/**
	 * Metodo....:totalizaCT
	 * Descricao.:Retorna true se esse CDR deve ser totalizado por chamada proveniente de terminal Controle Total
	 * @param cdr
	 * @return boolean
	 */
	public boolean totalizaCT(ArquivoCDRDadosVoz cdr)
	{
	    // Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
        // portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
        // a parte para indicar o desconto
		return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.CT);
	}
    
    /**
     * Metodo....:totalizaAmigosDeGraca
     * Descricao.:Retorna true se esse CDR deve ser totalizado por chamada realizada por FGN4 para um amigo de graca
     * @param cdr
     * @return boolean
     */
    public boolean totalizaAmigosDeGraca(ArquivoCDRDadosVoz cdr)
    {
        // Se o CDR estiver marcado com essa informacao entao indica que este nao deve ser bonificado
        // portanto o total de segundos apesar de continuar sendo totalizado esse valor deve ser totalizado
        // a parte para indicar o desconto
        return (this.totalizaPulaPula && cdr.getMotivoNaoBonificacao()==DescontoPulaPula.AMIGOS_GRACA);
    }
}
