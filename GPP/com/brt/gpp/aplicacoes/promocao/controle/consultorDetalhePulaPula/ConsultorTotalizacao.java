package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Classe responsavel pela consulta de totalizacoes relacionadas a promocao Pula-Pula do assinante. Estas 
 *	totalizacoes referem-se a sumarizacoes mensais de informacoes pertinentes a promocao, tais como ligacoes 
 *	recebidas, ligacoes originadas e recargas efetuadas.
 *
 *	@version	1.0		14/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorTotalizacao extends ConsultorDetalhePulaPula
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorTotalizacao(long idProcesso)
	{
		super("ConsultorTotalizacao", idProcesso);
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula#consultarDetalhePulaPula(AssinantePulaPula, java.util.Date, PREPConexao)
	 */
	public void consultarDetalhePulaPula(AssinantePulaPula	pAssinante, 
										 Date				dataReferencia,
										 boolean			isConsultaCheia,
										 PREPConexao		conexaoPrep) throws Exception
	{
		//Formatador de datas em mes para comparacao entre mes de referencia e data de entrada na promocao.
		SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
		
        //Obtendo as informacoes referentes ao tempo de ligacoes recebidas pelo assinante. A totalizacao deve ser 
		//posterior a data de entrada do assinante no Pula-Pula. Como a totalizacao e mensal, a visibilidade 
		//comeca no mes de entrada do assinante na promocao.
	    Calendar calTotalizacao = Calendar.getInstance();
	    calTotalizacao.setTime(dataReferencia);
        calTotalizacao.add(Calendar.MONTH, -1);
        
        String	mesTotalizacao	= conversorDatMes.format(calTotalizacao.getTime());
        String	mesEntrada		= conversorDatMes.format(pAssinante.getDatEntradaPromocao());
	    
        if((mesTotalizacao.compareTo(mesEntrada) >= 0) || isConsultaCheia)
        {
        	pAssinante.setTotalizacao(super.getConsulta().getTotalizacaoPulaPula(pAssinante.getIdtMsisdn(), 
            																	 conversorDatMes.format(calTotalizacao.getTime()), 
            																	 conexaoPrep));
            pAssinante.setTotalRecargas(super.getConsulta().getTotalizacaoRecargas(pAssinante.getIdtMsisdn(), 
            																	   conversorDatMes.format(calTotalizacao.getTime()), 
            																	   conexaoPrep));
        }
        
        /*
         * Verifica se o assinante possui Fale de Graça. Caso sim, preenche os dados de tolalização.
         */
        if (pAssinante.getPromocao() != null && pAssinante.getDatEntradaPromocao() != null
        	&& pAssinante.getDiasExecucao() != null)
        {
            // Cria uma entidade fake com os dados de chave primaria. 
            PromocaoDiaExecucao diaExecucao = new PromocaoDiaExecucao();
            diaExecucao.setIdtPromocao(new Integer(pAssinante.getPromocao().getIdtPromocao()));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(pAssinante.getDatEntradaPromocao().getTime());
            diaExecucao.setNumDiaEntrada(new Integer(calendar.get(Calendar.DAY_OF_MONTH)));
            diaExecucao.setTipExecucao(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_FALEGRATIS);
            
            // Verifica se alguma entidade diaExecucao com os dados acima está associada
            // ao assinante em consulta. Caso sim, entao este possui a promocao FGN.
            TotalizacaoFaleGratis totalizacaoFGN = null;
            
            for (Iterator it = pAssinante.getDiasExecucao().iterator(); it.hasNext();)
            {
            	PromocaoDiaExecucao temp = (PromocaoDiaExecucao)it.next();
            	if (temp.equals(diaExecucao))
            	{
            		diaExecucao = temp;
            		totalizacaoFGN = super.getConsulta().getTotalizacaoFGN(pAssinante.getIdtMsisdn(), 
            															   diaExecucao, 
            															   conexaoPrep);
            		break;
            	}
            } 
            
            // Se totalizacaoFGN for null, entao criamos uma entidade Fake vazia.
    		// Isso é necessário para que AssinantePulaPula.getTotalizacaoFGN() não seja nulo.
    		// Se essa propriedade for nula, nenhum xml será gerado no método getXMLPromocaoPulaPula().
    		// O XML deve ser gerao (mesmo que vazio) pois ControlePulaPula.TOTALIZACAO foi definida.
    		//if (totalizacaoFGN == null) totalizacaoFGN = new TotalizacaoFaleGratis();
    		
    		pAssinante.setTotalizacaoFGN(totalizacaoFGN);
        }
	}
	
}
