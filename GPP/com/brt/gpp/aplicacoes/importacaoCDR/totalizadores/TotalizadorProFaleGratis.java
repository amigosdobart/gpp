package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.importacaoCDR.GerenciadorCacheFF;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.AssinanteCache;
import com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoModulacao;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapCodigoNacional;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoModulacao;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe realiza a implementacao da interface de Totalizador para a 
 * totalizacao de informacoes a respeito da utilizacao da promocao Fale Gratis a Noite
 * 
 * Alteracoes
 */
public class TotalizadorProFaleGratis implements TotalizadorCDR 
{
	protected static GerentePoolLog log = null; // Gerente de LOG
	
	private SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
	
	private static final String SQLUPD = "update tbl_pro_totalizacao_falegratis " +
								             "set num_segundos_total = num_segundos_total + ? " +
								           "where idt_msisdn = ? " +
								             "and dat_mes    = ? ";

	private static final String SQLINS = "insert into tbl_pro_totalizacao_falegratis " +
						                 "(dat_mes " +
							              ",idt_msisdn " +
							              ",num_segundos_total " +
							              ") " +
							       "values (?,?,?)";
	
	private long iniPromFaleGratis = 0;
	private long fimPromFaleGratis = 0;
	
	public TotalizadorProFaleGratis()
	{
		try
		{
			log = GerentePoolLog.getInstancia(this.getClass());
			
			MapConfiguracaoGPP mapConf = MapConfiguracaoGPP.getInstance();
			
			iniPromFaleGratis = Long.parseLong(mapConf.getMapValorConfiguracaoGPP("INI_PERIODO_FALEGRATIS"));
			fimPromFaleGratis = Long.parseLong(mapConf.getMapValorConfiguracaoGPP("FIM_PERIODO_FALEGRATIS"));
		}
		catch(Exception e){};
	}
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#deveTotalizar(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR)
	 */
	public boolean deveTotalizar(ArquivoCDR arqCDR) 
	{
		if (!(arqCDR instanceof ArquivoCDRDadosVoz))
			return false;

		ArquivoCDRDadosVoz cdr = (ArquivoCDRDadosVoz)arqCDR;
		return totalizaFaleGratis(cdr);
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#getTotalizado(com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR, com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado)
	 */
	public Totalizado getTotalizado(ArquivoCDR arqCDR, Totalizado totalizado)
	{
		// Realiza o cast do arquivo de CDR para o arquivo de dados voz
		// que deve ser o arquivo sendo processado por esse totalizador
		// e tambem o cast do objeto totalizado para o TotalizacaoFaleGratis
		ArquivoCDRDadosVoz  cdr     = (ArquivoCDRDadosVoz)arqCDR;
		TotalizacaoFaleGratis totFG = (TotalizacaoFaleGratis)totalizado;

		// Caso o objeto totalizado seja passado como nulo no parametro 
		// entao cria a instancia do objeto que armazenarah os valores
		// do CDR atual. Quando esse objeto for diferente de nulo entao
		// nenhuma nova instancia eh criada e sim somente atualizada
		// no objeto atual somando os valores.
		String periodo = getPeriodo(cdr.getSubId(), cdr.getTimestamp());
		if ( totalizado == null || !totFG.getDatMes().equals(periodo) )
		{
			totFG = new TotalizacaoFaleGratis(periodo);
			totFG.setMsisdn(cdr.getSubId());
		}
		// O valor total de segundos eh adicionado caso o CDR
		// seja identificado como uma chamada utilizando a promocao
		// fale gratis a noite. Como o CDR de chamada originada (Realizado pela Tecnomen)
		// possui o fuso horario correto, entao o parametro fuso utilizado no
		// metodo abaixo eh informado zero para ignorar esta informacao
		totFG.addSegundos(getDuracaoFaleGratis(cdr.getStartTime(),cdr.getCallDuration(),cdr.getSubId(),0));
		
		return totFG;
	}
	
	/**
	 * Metodo....:getPeriodo
	 * Descricao.:Retorna o periodo a ser totalizado para o assinante
	 * @param msisdn	- Msisdn do assinante
	 * @param data		- Data a ser considerada
	 * @return String
	 */
	public String getPeriodo(String msisdn, Date data)
	{
		GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
		Calendar atual = Calendar.getInstance();
		atual.setTime(data);
		
		AssinanteCache assinante = gerFF.getAssinante(msisdn);
		if (assinante != null)
		{
			// Define o periodo atual. O mes do periodo eh de principio
			// o mesmo mes do CDR sendo processado e o dia eh o dia da
			// promocao do assinante.
			atual.set(Calendar.DAY_OF_MONTH, assinante.getDiaEntradaFaleGratis());
			
			// Identifica o dia do CDR para comparacao
			Calendar dia = Calendar.getInstance();
			dia.setTime(data);
			
			// Caso o CDR for anterior a data de aniversario do assinante
			// no mesmo mes e ano do CDR entao o periodo serah o mes anterior
			// ao mes do CDR.
			if (dia.before(atual))
				atual.add(Calendar.MONTH, -1);
		}
		return sdf.format(atual.getTime());
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.TotalizadorCDR#persisteTotalilzado(com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado)
	 */
	public void persisteTotalizado(Totalizado totalizado, PREPConexao conexaoDB) throws GPPInternalErrorException
	{
		// Executa o metodo de atualizacao em banco de dados de modo sincronizado
		TotalizadorProFaleGratis.atualizaTabela(totalizado,conexaoDB);
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
		if (totalizado instanceof TotalizacaoFaleGratis)
		{
			TotalizacaoFaleGratis totalFG = (TotalizacaoFaleGratis)totalizado;
			
			Object paramUpd[] = {new Long(totalFG.getNumSegundos())
								,totalFG.getMsisdn()
								,totalFG.getDatMes()
								};
			
			// Executa o comando para atualizacao do registro
			int numLinhas = 0;
			
			try
			{
				numLinhas = conexaoDB.executaPreparedUpdate(TotalizadorProFaleGratis.SQLUPD,paramUpd,0);
			}
			catch (Exception e)
			{
				log.log(-1, Definicoes.ERRO, "TotalizadorProFaleGratis", "atualizaTabela", 
				"Erro ao executar update. Parametros: " +
				"NUM_SEGUNDOS=" + paramUpd[0] + 
				", MSISDN=" + paramUpd[1] + 
				", DATMES=" + paramUpd[2] + 
				". Erro:" + e);
				throw new GPPInternalErrorException(log.traceError(e));
			}
			
			// Caso o numero de linhas atualizadas seja igual a 0 entao significa que o
			// registro para o assinante nao existe entao cria o registro com o comando
			// insert
			if (numLinhas == 0)
			{
				Object paramIns[] = {totalFG.getDatMes()
									,totalFG.getMsisdn()
									,new Long(totalFG.getNumSegundos())
									};
				
				try
				{
					conexaoDB.executaPreparedUpdate(TotalizadorProFaleGratis.SQLINS,paramIns,0);
				}
				catch (Exception e)
				{
					log.log(-1, Definicoes.ERRO, "TotalizadorProFaleGratis", "atualizaTabela", 
					"Erro ao executar insert. Parametros: " +
					"NUM_SEGUNDOS=" + paramIns[2] + 
					", MSISDN=" + paramIns[1] + 
					", DATMES=" + paramIns[0] + 
					". Erro:" + e);
					throw new GPPInternalErrorException(log.traceError(e));
				}
			}
		}
	}
	/**
	 * Metodo....:totalizaFaleGratis
	 * Descricao.:Retorna se o CDR atual pode ser totalizavel na promocao
	 * @return boolean
	 */
	public boolean totalizaFaleGratis(ArquivoCDRDadosVoz cdr)
	{
		try
		{
			// Verifica se o CDR eh relativo a uma chamada originada
			// que pode ter sido utilizada dentro da promocao fale
			// gratis a noite.
			if (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2)
				if (utilizouFaleGratis(cdr.getSubId(), 0, cdr))
					return true;
		}
		catch(Exception e)
		{
			GerentePoolLog log = GerentePoolLog.getInstancia(TotalizadorProFaleGratis.class);
			log.log(0, Definicoes.ERRO, "TotalizadorProFaleGratis", "totalizaFaleGratis", 
					"Erro ao identificar se a chamada totaliza falegratis. SubId:"+cdr.getSubId()+" Ini:"+cdr.getStartTime()+ " Dur:"+cdr.getCallDuration()+" Erro:"+e);
		}
		return false;
	}
	
	/**
	 * Metodo....:utilizouFaleGratis
	 * Descricao.:Verifica se a chamada foi realizada dentro do periodo configurado para a promocao
	 *            fale gratis e originada por um assinante participante da promocao.
	 * @param originador	- Numero do originador da mensagem.
	 * @param fuso			- Fuso horario, caso houver para a regiao do originador
	 * @param cdr			- CDR contendo as outras informacoes a serem analisadas 
	 * @return boolean		- Retorna se a ligacao faz parte do horario da fale gratis
	 */
	public boolean utilizouFaleGratis(String originador, int fuso, ArquivoCDR cdr)
	{
		boolean utilizou = false;
		try
		{
			GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
			// Para identificar um CDR participante da promocao FGN
			// identifica-se o plano de preco utilizado no CDR faz parte 
			// dos planos configurados para a promocao FGN. No CDR de chamada 
			// recebida nao existe informacao do plano de preco, portanto se o 
			// plano for igual a 0 (zero) entao verifica se o assinante faz
			// parte da promocao verificando o plano de preco do assinante e nao
			// da chamada.
			if ( gerFF.isPlanoFaleGratis(cdr.getProfileId()) || ( cdr.getProfileId() == 0 && gerFF.isPromocaoFaleGratis(originador)) )
			{
				// Neste ponto o programa identifica de maneira configuravel por promocao
				// o horario definido para o fale gratis. A configuracao eh realizada por
				// promocao e por codigo nacional, portanto primeiro eh preciso identificar
				// estas informacoes no cache de assinantes.
				AssinanteCache assinante = gerFF.getAssinante(originador);
				if (assinante != null)
				{
					// Verifica se o tipo de chamada do CDR eh permitido para a promocao
					// falegratis. Caso o tipo seja permitido entao a identificacao do
					// periodo eh realizada
					Promocao promocao = MapPromocao.getInstancia().getPromocao(assinante.getIdPromocao());
					if (promocao != null && promocao.permiteTipoChamada(cdr.getTipChamada()))
						if (promocao.permiteDiaSemana(cdr.getTimestamp()))
							// Verifica tambem a utilizacao do saldo principal ou de bonus
							// para a realizacao da chamada. Chamadas relativas a promocao
							// FGN nao sao tarifadas e portanto possuem valores iguais a zero.
							// OBS:No caso de chamadas originadas este valor nao existe, jah
							// vindo entao zerado.
							if (cdr.getAccountBalanceDelta() == 0 && cdr.getBonusBalanceDelta() == 0)
							{
								// Define como horario inicial e final da promocao, o horario default
								// existente configurado para o sistema. O programa agora tente identificar
								// atraves da configuracao de modulacao da promocao o horario a ser utilizado
								// para a promocao do assinante e da regiao deste.
								long iniProm = getHoraInicioPromocao(assinante);
								long fimProm = getHoraFimPromocao(assinante);
					
								// OBS: * 86400 eh a equivalencia de 00hs em segundos
								//      * O tempo inicial (start_time) eh somado ao fuso, pois caso o fuso horario da regiao
								//        seja menor que o de Brasilia (negativo) entao a conta fica correta.
								long startTime = getStartTime(cdr.getStartTime(), fuso);
								long endTime   = getEndTime(startTime, cdr.getCallDuration());
									
								if (startTime >= iniProm || startTime <  fimProm ||
									(startTime < iniProm && (endTime > iniProm || endTime < fimProm))
								   )
									utilizou = true;
							}
				}
			}
		}
		catch(Exception e)
		{
			GerentePoolLog log = GerentePoolLog.getInstancia(TotalizadorProFaleGratis.class);
			log.log(0, Definicoes.ERRO, "TotalizadorProFaleGratis", "utilizouFaleGratis", 
					"Erro ao identificar se a chamada utilizou falegratis. SubId:"+originador+" Ini:"+cdr.getStartTime()+ " Dur:"+cdr.getCallDuration()+" Fuso:"+ fuso +" Erro:"+e);
		};
		return utilizou;
	}
	
	/**
	 * Metodo....:getHoraInicioPromocao
	 * Descricao.:Retorna a hora inicio da promocao
	 * @param assinante - Assinante a ser utilizado para identificacao dos dados de promocao e regiao
	 * @return long		- Hora inicial da promocao
	 * @throws Exception
	 */
	private long getHoraInicioPromocao(AssinanteCache assinante) throws Exception
	{
		long iniProm = iniPromFaleGratis;
		MapPromocaoModulacao map  = MapPromocaoModulacao.getInstancia();
		PromocaoModulacao modProm = map.getModulacao(assinante.getIdPromocao(), Integer.parseInt(assinante.getMsisdn().substring(2,4)));
		if (modProm != null)
			iniProm = modProm.getHoraInicio();
		
		return iniProm;
	}
	
	/**
	 * Metodo....:getHoraFimPromocao
	 * Descricao.:Retorna a hora fim da promocao
	 * @param assinante - Assinante a ser utilizado para identificacao dos dados de promocao e regiao
	 * @return long		- Hora fim da promocao
	 * @throws Exception
	 */
	private long getHoraFimPromocao(AssinanteCache assinante) throws Exception
	{
		long fimProm = fimPromFaleGratis;
		MapPromocaoModulacao map  = MapPromocaoModulacao.getInstancia();
		PromocaoModulacao modProm = map.getModulacao(assinante.getIdPromocao(), Integer.parseInt(assinante.getMsisdn().substring(2,4)));
		if (modProm != null)
			fimProm = modProm.getHoraFim();
		
		return fimProm;
	}
	
	/**
	 * Metodo....:getStartTime
	 * Descricao.:Retorna o horario inicio da chamada, configurando
	 *            o fuso e identificando a virada para a meia-noite
	 *            
	 *            OBS: Como o fuso horario eh cadastrado indicando o numero
	 *                 de horas do fuso horario, entao eh realizado a multiplicacao
	 *                 por 3600 segundos que eh igual a 1 hora.
	 * @param iniChamada - Hora inicial da chamada
	 * @param fuso - fuso horario da regiao
	 * @return long - Horario inicio a ser utilizado no calculo da falegratis
	 */
	private long getStartTime(long iniChamada, int fuso)
	{
		return iniChamada + (fuso*3600) < 0 ? 86400 - (Math.abs(iniChamada + (fuso*3600))) : iniChamada + (fuso*3600);
	}
	
	/**
	 * Metodo....:getEndTime
	 * Descricao.:Retorna o horario final da chamada, incluindo o 
	 *            tratamento de fuso e virada da meia-noite
	 * @param startTime - Horario inicial
	 * @param duracao   - Duracao
	 * @return long		- Horario final da chamada
	 */
	private long getEndTime(long startTime, long duracao)
	{
		return startTime + duracao > 86400 ? (startTime + duracao - 86400) : startTime + duracao;
	}
	
	/**
	 * Metodo....:getDuracaoFaleGratis
	 * Descricao.:Retorna a duracao realizada na chamada para a promocao falegratis
	 * @param iniChamada	- Inicio da chamada
	 * @param duracao		- Duracao da chamada
	 * @param originador	- Msisdn do originador da chamada
	 * @param fuso			- Fuso horario da regiao do originador (se necessario)
	 * @return	long		- Duracao a ser considerada para a promocao fale gratis
	 */
	public long getDuracaoFaleGratis(long iniChamada, long duracao, String originador, int fuso)
	{
		long duracaoFaleGratis = 0;
		try
		{
			// Neste ponto o programa identifica de maneira configuravel por promocao
			// o horario definido para o fale gratis. A configuracao eh realizada por
			// promocao e por codigo nacional, portanto primeiro eh preciso identificar
			// estas informacoes no cache de assinantes.
			GerenciadorCacheFF gerFF = GerenciadorCacheFF.getInstance();
			AssinanteCache assinante = gerFF.getAssinante(originador);
			if (assinante != null)
			{
				// Define como horario inicial e final da promocao, o horario default
				// existente configurado para o sistema. O programa agora tente identificar
				// atraves da configuracao de modulacao da promocao o horario a ser utilizado
				// para a promocao do assinante e da regiao deste.
				long iniProm = getHoraInicioPromocao(assinante);
				long fimProm = getHoraFimPromocao(assinante);
				
				// OBS: O tempo inicial (start_time) eh somado ao fuso, pois caso o fuso horario da regiao
				//      seja menor que o de Brasilia (negativo) entao a conta fica correta.
				long startTime = getStartTime(iniChamada, fuso);
				long endTime   = getEndTime(startTime, duracao);
			
				// Define o numero de segundos antes e pos promocao para ser retirado do
				// total final da ligacao,jah que estes segundos nao participaram do periodo
				// promocional
				long segsAntesProm = 0;
				long segsPosProm   = 0;
			
				if (startTime < iniProm && startTime > fimProm)
					segsAntesProm = iniProm - startTime;
			
				if (endTime > fimProm && endTime < iniProm)
					segsPosProm = endTime - fimProm;
			
				duracaoFaleGratis = duracao - segsAntesProm - segsPosProm;
			}
		}
		catch(Exception e){}
		return duracaoFaleGratis;
	}
	
	/**
	 * Metodo....:getFusoHorario
	 * Descricao.:Retorna o fuso horario definido para uma regiao
	 * @param String - Msisdn do assinante
	 * @return int - Fuso horario
	 */
	public int getFusoHorario(String msisdn)
	{
		int fuso = 0;
		try
		{
			MapCodigoNacional mapCN = MapCodigoNacional.getInstance();
			Integer key[] = {new Integer(msisdn.substring(2,4))};
			CodigoNacional cn = (CodigoNacional)mapCN.get(key);
			if (cn != null)
				fuso = cn.getIdtFuso().intValue();
		}
		catch(Exception e)
		{};
		
		return fuso;
	}
}
