package com.brt.gpp.aplicacoes.promocao.sumarizacaoPulaPula;

//Imports Java.

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

//Imports GPP.

import com.brt.gpp.aplicacoes.promocao.entidade.RelatorioPulaPula;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que sumariza os dados do Pula-Pula
 *  O Principal método é o sumariza.
 *  A principal saída de dados é o toCollection.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	18/04/2005 (dd/mm/yyyy)
 *	@deprecated
 */
public class SumarizacaoPulaPula
{
    
	protected	Map			values;
    
    //Formatadores.
    
//    private static final SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
    

    //Construtores.
	
	/**
	 * Construtor da classe
	 */
	public SumarizacaoPulaPula()
	{
	    this.reset();
		this.values     		= Collections.synchronizedMap(new HashMap());
	}

	/**
	 *	Inicializa o objeto.
	 */
	public void reset()
	{
	    this.values					= null;
	}

	/**
	 * 'Igual' ao do Mapeamento
	 */
	public Collection toColleection(){
	    ArrayList result = new ArrayList();
	    if((this.values != null) && (this.values.size() > 0))
	    {
		    Iterator iterator = this.values.values().iterator();
	        Stack stack = new Stack();
	        
		    while(true)
		    {
		        try
		        {
			        Object value = iterator.next();
			        
			        if(value instanceof Map)
			        {
			            stack.push(iterator);
			            iterator = ((Map)value).values().iterator();
			        }
			        
			        if(value instanceof Collection)
			        {
			            stack.push(iterator);
			            iterator = ((Collection)value).iterator();
			        }
			        
			        if(value instanceof Entidade)
			        {
				        result.add(((Entidade)value).clone());
			        }
		        }
		        catch(NoSuchElementException e)
		        {
			        if(stack.isEmpty())
			        {
			            break;
			        }
			        
		            iterator = (Iterator)stack.pop();
		        }
		    }
	    }
	    
	    return result;
	}
	/**
	 * Sumariza os dados em conjunto com a nova informação que está vindo
	 * pelo registro
	 * 
	 * @param registro		Registro a completar a sumarização
	 */
	public synchronized void sumariza(RelatorioPulaPula registro) {
		Object[] key = new Object[1];
		key[0] = new Integer(registro.getIdtCodigoNacional());
		RelatorioPulaPula acumulado = this.get(key);
		if(acumulado ==null){
			acumulado =(RelatorioPulaPula)registro.clone();
			acumulado.setDatInicialExecucao(new Date());
			//Inserindo o registro no Map.
			this.values.put(key[0], acumulado);
			
		}
		else{
			acumulado.add(RelatorioPulaPula.QTD_ASSINANTES,registro.getQtdAssinantes());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_0,registro.getQtdStatus0());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_1,registro.getQtdStatus1());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_2,registro.getQtdStatus2());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_3,registro.getQtdStatus3());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_4,registro.getQtdStatus4());
			acumulado.add(RelatorioPulaPula.QTD_STATUS_5,registro.getQtdStatus5());

			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_TOTAL,registro.getNumSegundosTotal());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_NORMAL,registro.getNumSegundosNormal());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_FF,registro.getNumSegundosFF());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_PLANO_NOTURNO,registro.getNumSegundosPlanoNoturno());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_PLANO_DIURNO,registro.getNumSegundosPlanoDiurno());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_NAO_BONIFICADO,registro.getNumSegundosNaoBonificado());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_DURAC_EXCEDIDA,registro.getNumSegundosDuracExcedida());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_EXPURGO_FRAUDE,registro.getNumSegundosExpurgoFraude());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_ESTORNO_FRAUDE,registro.getNumSegundosEstornoFraude());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_TUP,registro.getNumSegundosTup());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_AIGUALB,registro.getNumSegundosAIgualB());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_ATH,registro.getNumSegundosATH());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_MOVEL_NAO_BRT,registro.getNumSegundosMovelNaoBrt());
			acumulado.add(RelatorioPulaPula.NUM_SEGUNDOS_FALE_GRATIS,registro.getNumSegundosFaleGratis());
			
			acumulado.add(RelatorioPulaPula.VLR_RECARGAS,registro.getVlrRecargas());
			acumulado.add(RelatorioPulaPula.VLR_BONUS_TOTAL,registro.getVlrBonusTotal());
			acumulado.add(RelatorioPulaPula.VLR_BONUS_ADIANTAMENTO,registro.getVlrBonusAdiantamento());
			acumulado.add(RelatorioPulaPula.VLR_SALDO_PRINCIPAL,registro.getVlrSaldoPrincipal());
			acumulado.add(RelatorioPulaPula.VLR_SALDO_BONUS,registro.getVlrSaldoBonus());
			acumulado.add(RelatorioPulaPula.VLR_SALDO_SMS,registro.getVlrSaldoSms());
			acumulado.add(RelatorioPulaPula.VLR_SALDO_GPRS,registro.getVlrSaldoGprs());
			acumulado.add(RelatorioPulaPula.VLR_SALDO_CONCESSAO_FRACIONADA,registro.getVlrSaldoConcessaoFracionada());

		}
		acumulado.setDatFinalExecucao			(new Date());

	}

	/***/
	/**
	 *	Retorna objeto representando registro de tabela de banco de dados mapeado em memoria.
	 *
	 *	@param		Object[]				key							Identificador unico do registro na tabela.
	 *	@return		Entidade											Objeto representando registro mapeado em memoria.
	 */
	public RelatorioPulaPula getNEW(Object[] key)
	{
	    Map values = (Map) this.values.get(key[0]);
		RelatorioPulaPula entidade = (RelatorioPulaPula) ((values != null) ? values.get(key[0])	: this.values.get(key[0]));

		if (entidade != null) {
			return entidade;
		}
	    return null;
	}
	
	public RelatorioPulaPula get(Object[] key)
	{
	    Map values = null;
	    
	    if((key != null) && (key.length > 0))
	    {
	        for(int i = 0; i < key.length; i++)
	        {
	            if(i == key.length - 1)
	            {
	            	RelatorioPulaPula entidade = (RelatorioPulaPula)((values != null) ? values.get(key[i]) : this.values.get(key[i])); 
	                
	                if(entidade != null)
	                {
	                    return entidade;//Foi Retirado o Clone, pois deve necessariamente ocorrer alteração de dados
	                }
	            }
	            
		        values = (Map)this.values.get(key[i]);
	        }
	    }
	    
	    return null;
	}
}
