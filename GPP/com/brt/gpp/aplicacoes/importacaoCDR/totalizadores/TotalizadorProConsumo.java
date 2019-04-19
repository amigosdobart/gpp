package com.brt.gpp.aplicacoes.importacaoCDR.totalizadores;

import java.util.Arrays;

import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDR;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoConsumo;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * Esta classe realiza a implementacao da interface de totalizador para totalizar
 * informacoes de consumo de saldo
 */
public class TotalizadorProConsumo implements TotalizadorCDR
{
    private static String[] EOTbrt = null;
    
    static
    {
        EOTbrt = "61;60;594;593;561;562;567;565;540;551;547;555;582;592;596;571;585;527;598;583;586;584;595;579;511;511;521;535;542;548;549;513;516;525;591;581".split(";");
        Arrays.sort(EOTbrt);
    }
    
	private static final String SQLUPD = "update tbl_pro_totalizacao_consumo " +
	                                 "set num_segundos_bonus_offnet = num_segundos_bonus_offnet + ? " +
	                               "where idt_msisdn = ? " +
	                                 "and dat_mes = ?";
	
	private static final String SQLINS = "insert into tbl_pro_totalizacao_consumo " +
	                              "(dat_mes,idt_msisdn,num_segundos_bonus_offnet) values (?,?,?)";
	
	/**
	 * Verifica se o CDR deve ser processado para totalizacao 
     * de consumo de bonus offnet.
     *  
	 * @param cdr - CDR de dados/voz a ser verificado
	 * @return boolean - Indica se deve totalizar consumo de bonus offnet
	 */
	private boolean totalizaBonusOffnet(ArquivoCDR cdr)
	{
		// Verifica se o CDR possui os parametros necessarios para ser totalizado
		// consumo de bonus offnet. Condições:
        //   - Destinatário é off-net (nao BrT)
        //   - A ligação consumiu saldo de bonus
        
		if (cdr.getBonusBalanceDelta() != 0 && 
            Arrays.binarySearch(EOTbrt, cdr.getIdtOperadoraDestino()) < 0)
			return true;
		
		return false;
	}
	
	/**
     * Retorna verdadeiro caso seja um CDR de chamada sainte 
     * (Pre-Pago/Controle/Ligmix/CT).
     * 
     * Nesse caso aplica-se a totalização de consumo dessa ligação/CDR.
     * 
	 * @param arqCDR
	 * @return boolean
	 */
	public boolean deveTotalizar(ArquivoCDR arqCDR) 
	{
		if (!(arqCDR instanceof ArquivoCDRDadosVoz))
			return false;

		ArquivoCDRDadosVoz cdr = (ArquivoCDRDadosVoz)arqCDR;
        
		// Verifica se eh uma chamada originada. 
        // Somente chamadas de arquivos de VOZ sao verificadas. 
        if (Definicoes.IND_CHAMADAS_VOZ.equals(cdr.getTipCdr()) &&
            (cdr.getTransactionType() == 0 || cdr.getTransactionType() == 2))
        {
            if (totalizaBonusOffnet(cdr))
                return true;
        }
            
		return false;
	}
	
	/**
	 * @param arqCDR
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public Totalizado getTotalizado(ArquivoCDR arqCDR, Totalizado totalizado) 
	{        
        /*
	    // Define a origem e o fuso baseando na informacao de chamada recebida ou originada.
        String origem  = arqCDR.getSubId();
        String destino = "55" + Long.parseLong(arqCDR.getCallId());
        if (arqCDR.getTransactionType() == 9 || arqCDR.getTransactionType() == 11)
        {
            // Inverte o originado e o destino na chamada recebida.
            origem  = !"NaoInformado".equals(arqCDR.getCallId()) ? "55"+Long.parseLong(arqCDR.getCallId()) : arqCDR.getCallId();
            destino = arqCDR.getSubId();
        }
        */

		// Realiza o cast do arquivo de CDR para o arquivo de dados voz
		// que deve ser o arquivo sendo processado por esse totalizador
		// e tambem o cast do objeto totalizado para o TotalizacaoBumerangue
		ArquivoCDRDadosVoz cdr    = (ArquivoCDRDadosVoz)arqCDR;
		TotalizacaoConsumo totConsumo = (TotalizacaoConsumo)totalizado;
		
		// Caso o objeto totalizado seja passado como nulo no parametro 
		// entao cria a instancia do objeto que armazenarah os valores
		// do CDR atual. Quando esse objeto for diferente de nulo entao
		// nenhuma nova instancia eh criada e sim somente atualizada
		// no objeto atual somando os valores. Veja que se a data mes
		// do CDR (Chave para a Totalizacao) for diferente entre o objeto
		// totalizado e o CDR entao cria-se tambem uma nova instancia do
		// objeto totalizado afim de evitar somatoria em periodos diferentes
		if (totalizado == null)
		{
			totConsumo = new TotalizacaoConsumo(cdr.getTimestamp());
			totConsumo.setIdtMsisdn(cdr.getSubId());
		}
        
        // NAO EH NECESSARIO VERIFICAR SE TOTALIZA BONUS OFFNET (pois isso já eh feito
        // em deveTotalizar(). Esse totalizar so faz, no momento, totalizacao de bonus off net)
        // if (totalizaBonusOffnet(cdr))
            totConsumo.add(TotalizacaoConsumo.NUM_SEGUNDOS_BONUS_OFFNET,cdr.getCallDuration());
		
		return totConsumo;
	}
	
	/**
	 * @param totalizado
	 * @return com.brt.gpp.aplicacoes.importacaoCDR.totalizadores.Totalizado
	 */
	public void persisteTotalizado(Totalizado totalizado, PREPConexao conexaoPrep) 
        throws GPPInternalErrorException
	{
		// Utiliza o metodo sincronizado para realizar a totalizacao da promocao
		TotalizadorProConsumo.atualizaTabela(totalizado,conexaoPrep);
	}
	
	/**
	 * Atutaliza a tabela contendo informacoes de totalizacao
     * 
	 * @param totalizado
	 * @param conexaoPrep
	 * @throws GPPInternalErrorException
	 */
	public static synchronized void atualizaTabela(Totalizado totalizado, PREPConexao conexaoPrep) 
        throws GPPInternalErrorException
	{
		if (totalizado instanceof TotalizacaoConsumo)
		{
            TotalizacaoConsumo totConsumo = (TotalizacaoConsumo)totalizado;
            
			// Tenta realizar a atualizacao da linha na tabela
			Object paramUpd[] = {new Long(totConsumo.getNumSegundosBonusOffnet()), totConsumo.getIdtMsisdn(), totConsumo.getDatMes()};
			int numLinhas = conexaoPrep.executaPreparedUpdate(TotalizadorProConsumo.SQLUPD,paramUpd,0);
			
            // Caso o numero de linhas atualizadas seja igual a 0 entao significa que o assinante
			// ainda nao foi totalizado pelo periodo entao realiza um insert na tabela para inicializar
			// essa totalizacao
			if (numLinhas == 0)
			{
				Object paramIns[] = {totConsumo.getDatMes(), totConsumo.getIdtMsisdn(), new Long(totConsumo.getNumSegundosBonusOffnet())};
				conexaoPrep.executaPreparedUpdate(TotalizadorProConsumo.SQLINS,paramIns,0);
			}
		}
	}
}
