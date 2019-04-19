package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import com.brt.gpp.aplicacoes.enviarBonusCSP14.TotalizacaoBumerangue;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Esta classe realiza a implementacao da interface de totalizador para totalizar
 * informacoes a respeito da promocao bumerangue14
 */
public class TotalizadorProBumerangue implements TotalizadorCDR
{
	protected static GerentePoolLog log = null; // Gerente de LOG
	
	private static final String SQLUPD = "update tbl_pro_totalizacao_bumerangue " +
	                                 "set num_segundos = num_segundos + ? " +
	                               "where idt_msisdn = ? " +
	                                 "and dat_mes = ?";
	
	private static final String SQLINS = "insert into tbl_pro_totalizacao_bumerangue " +
	                              "(dat_mes,idt_msisdn,num_segundos) values (?,?,?)";
	
	public TotalizadorProBumerangue() 
	{
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 * Metodo....:totalizaBumerangue
	 * Descricao.:Verifica se o CDR deve ser processado para a promocao bumerangue
	 * @param cdr - CDR de dados/voz a ser verificado
	 * @return boolean - Indica se deve totalizar para a promocao bumerangue
	 */
	private boolean totalizaBumerangue(ArquivoCDR cdr)
	{
		// Verifica se o CDR possui os parametros necessarios para ser totalizado
		// na promocao bumerangue14. Os parametros verificados sao:
		// - O CDR deve indicar uso do CSP 14
		// - A ligacao deve ter sido tarifado pelo saldo principal
		// - Deve existir o Deslocamento AD1 ou - indicando que nao ha deslocamento
		if (cdr.getAccountBalanceDelta() != 0 											&& 
			String.valueOf(Definicoes.BONUS_NUM_CSP14).equals(cdr.getNumCsp())			&&
			(Definicoes.BONUS_TIP_DESLOCAMENTO_CSP14.equals	 (cdr.getTipDeslocamento()) ||
			 Definicoes.CAMPO_VAZIO.equals					 (cdr.getTipDeslocamento())
			))
			return true;
		
		return false;
	}
	
	/**
	 * @param arqCDR
	 * @return boolean
	 */
	public boolean deveTotalizar(ArquivoCDR arqCDR) 
	{
		if (!(arqCDR instanceof ArquivoCDRDadosVoz))
			return false;

		ArquivoCDRDadosVoz cdr = (ArquivoCDRDadosVoz)arqCDR;
		return totalizaBumerangue(cdr);
	}
	
	/**
	 * @param arqCDR
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public Totalizado getTotalizado(ArquivoCDR arqCDR, Totalizado totalizado) 
	{
		// Realiza o cast do arquivo de CDR para o arquivo de dados voz
		// que deve ser o arquivo sendo processado por esse totalizador
		// e tambem o cast do objeto totalizado para o TotalizacaoBumerangue
		ArquivoCDRDadosVoz    cdr    = (ArquivoCDRDadosVoz)arqCDR;
		TotalizacaoBumerangue totBum = (TotalizacaoBumerangue)totalizado;
		
		// Caso o objeto totalizado seja passado como nulo no parametro 
		// entao cria a instancia do objeto que armazenarah os valores
		// do CDR atual. Quando esse objeto for diferente de nulo entao
		// nenhuma nova instancia eh criada e sim somente atualizada
		// no objeto atual somando os valores. Veja que se a data mes
		// do CDR (Chave para a Totalizacao) for diferente entre o objeto
		// totalizado e o CDR entao cria-se tambem uma nova instancia do
		// objeto totalizado afim de evitar somatoria em periodos diferentes
		// para a promocao
		if (totalizado == null)
		{
			totBum = new TotalizacaoBumerangue(cdr.getTimestamp());
			totBum.setMsisdn(cdr.getSubId());
		}
		totBum.addSegundos(cdr.getCallDuration());
		
		return totBum;
	}
	
	/**
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public void persisteTotalizado(Totalizado totalizado, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		// Utiliza o metodo sincronizado para realizar a totalizacao da promocao
		TotalizadorProBumerangue.atualizaTabela(totalizado,conexaoPrep);
	}
	
	/**
	 * Metodo....:atualizaTabela
	 * Descricao.:Atutaliza a tabela contendo informacoes de totalizacao da promocao de modo sincronizado
	 * @param totalizado
	 * @param conexaoPrep
	 * @throws GPPInternalErrorException
	 */
	public static synchronized void atualizaTabela(Totalizado totalizado, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		if (totalizado instanceof TotalizacaoBumerangue)
		{
			TotalizacaoBumerangue totBum = (TotalizacaoBumerangue)totalizado;
			// Tenta realizar a atualizacao da linha na tabela
			
			Object paramUpd[] = {new Long(totBum.getNumSegundos()), totBum.getMsisdn(), totBum.getDatMes()};
			int numLinhas = 0;
			
			try
			{
				numLinhas = conexaoPrep.executaPreparedUpdate(TotalizadorProBumerangue.SQLUPD,paramUpd,0);
			}
			catch (Exception e)
			{
				log.log(-1, Definicoes.ERRO, "TotalizadorProBumerangue", "atualizaTabela", 
				"Erro ao executar update. Parametros: " +
				"NUM_SEGUNDOS=" + paramUpd[0] + 
				", MSISDN=" + paramUpd[1] + 
				", DATMES=" + paramUpd[2] + 
				". Erro:" + e);
				throw new GPPInternalErrorException(log.traceError(e));
			}
				
				
			// Caso o numero de linhas atualizadas seja igual a 0 entao significa que o assinante
			// ainda nao foi totalizado pelo periodo entao realiza um insert na tabela para inicializar
			// essa totalizacao
			if (numLinhas == 0)
			{
				Object paramIns[] = {totBum.getDatMes(), totBum.getMsisdn(), new Long(totBum.getNumSegundos())};
				
				try
				{
					conexaoPrep.executaPreparedUpdate(TotalizadorProBumerangue.SQLINS,paramIns,0);
				}
				catch (Exception e)
				{
					log.log(-1, Definicoes.ERRO, "TotalizadorProBumerangue", "atualizaTabela", 
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
}
