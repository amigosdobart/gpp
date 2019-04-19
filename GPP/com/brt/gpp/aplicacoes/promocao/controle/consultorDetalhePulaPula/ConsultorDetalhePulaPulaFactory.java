package com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorBonusAgendado;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorBonusAgendadoPeriodo;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorBonusConcedidoPeriodo;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorBonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDataCredito;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorDetalhePulaPula;
import com.brt.gpp.aplicacoes.promocao.controle.consultorDetalhePulaPula.ConsultorTotalizacao;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.SaldoPulaPula;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 *	Factory de objetos ConsultorDetalhePulaPula.
 *
 *	@version	1.0		17/03/2008		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class ConsultorDetalhePulaPulaFactory extends Aplicacoes
{

	/**
	 *	Construtor da classe.
	 *	
	 *	@param		idProcesso				Identificador do processo.
	 */
	public ConsultorDetalhePulaPulaFactory(long idProcesso)
	{
		super(idProcesso, "ConsultorDetalhePulaPulaFactory");
	}
	
    /**
     *	Consulta os detalhes da promocao Pula-Pula do assinante. Estes detalhes sao referentes ao valor de bonus a
     *	ser recebido pelo assinante, valor de bonus parcial recebido, bonus agendado na fila de recargas, 
     *	bonus Pula-Pula para o seu Codigo Nacional e tempo de ligacoes recebidas.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		detalhes				Lista de opcoes de detalhes a serem consultados.
     *	@param		dataReferencia			Data de referencia para a concessao do bonus.
	 *	@param		isConsultaCheia			Indicador de necessidade de consulta cheia, ou seja, sem aplicacao de 
	 *										descontos ou limites.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Informacoes referentes a promocao Pula-Pula do assinante acrescido de detalhes.
     *	@throws		Exception
     */
    public AssinantePulaPula consultarDetalhesPulaPula(PromocaoAssinante	pAssinante, 
    												   int[]				detalhes, 
    												   Date					dataReferencia,
    												   boolean				isConsultaCheia,
    												   PREPConexao			conexaoPrep) throws Exception
    {
        AssinantePulaPula result = null;
        
        if(pAssinante != null)
        {
            result = new AssinantePulaPula();
            pAssinante.fill(result);
            
            if(result.getStatus().isAtivo() || isConsultaCheia)
            {
                Collection consultores = this.newConsultoresDetalhePulaPula(detalhes);
                
                for(Iterator iterator = consultores.iterator(); iterator.hasNext();)
                	((ConsultorDetalhePulaPula)iterator.next()).consultarDetalhePulaPula(result, 
                																		 dataReferencia,
                																		 isConsultaCheia,
                																		 conexaoPrep);
            }
            else
            {
                result = new AssinantePulaPula();
                pAssinante.fill(result);
                result.setSaldo(new SaldoPulaPula());
            }
        }
        
        return result;
    }
        
	/**
	 *	Cria e retorna uma lista de objetos responsaveis pela consulta do detalhe da promocao Pula-Pula do 
	 *	assinante.
	 *	
	 *	@param		detalhes				Array de identificadores dos detalhes a serem consultados, conforme 
	 *										definidos na classe ControlePulaPula.
	 *	@return		Lista de objetos responsaveis pela consulta dos detalhes da promocao Pula-Pula do assinante.
	 *	@throws		IllegalArgumentException Caso o detalhe informado nao tenha sido definido.
	 */
	public Collection newConsultoresDetalhePulaPula(int[] detalhes)
	{
		ArrayList result = new ArrayList();
		
		for(int i = 0; ((detalhes != null) && (i < detalhes.length)); i++)
			result.add(this.newConsultorDetalhePulaPula(detalhes[i]));
		
		return result;
	}
	
	/**
	 *	Cria e retorna um objeto responsavel pela consulta do detalhe da promocao Pula-Pula do assinante.
	 *	
	 *	@param		detalhe					Identificador do detalhe a ser consultado, conforme definido na classe 
	 *										ControlePulaPula.
	 *	@return		Objeto responsavel pela consulta dos detalhes da promocao Pula-Pula do assinante.
	 *	@throws		IllegalArgumentException Caso o detalhe informado nao tenha sido definido.
	 */
	public ConsultorDetalhePulaPula newConsultorDetalhePulaPula(int detalhe)
	{
		switch(detalhe)
		{
			case ControlePulaPula.DATAS_CREDITO: 
				return new ConsultorDataCredito(super.getIdLog());
			case ControlePulaPula.TOTALIZACAO:
				return new ConsultorTotalizacao(super.getIdLog());
			case ControlePulaPula.BONUS_PULA_PULA:
				return new ConsultorBonusPulaPula(super.getIdLog());
			case ControlePulaPula.BONUS_CONCEDIDOS_PERIODO:
				return new ConsultorBonusConcedidoPeriodo(super.getIdLog());
			case ControlePulaPula.BONUS_AGENDADOS_PERIODO:
				return new ConsultorBonusAgendadoPeriodo(super.getIdLog());
			case ControlePulaPula.BONUS_AGENDADOS:
				return new ConsultorBonusAgendado(super.getIdLog());
			case ControlePulaPula.SALDO_PULA_PULA:
				return new ConsultorSaldoPulaPula(super.getIdLog());
			default: throw new IllegalArgumentException("Detalhe nao definido: " + detalhe);
		}
	}
	
}
