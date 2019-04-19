package com.brt.gpp.aplicacoes.promocao.controle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;

/**
 *	Classe responsavel pelo gerenciamento das datas de execucao e credito de bonus das promocoes. 
 *
 *	@version	1.0		22/11/2007		Primeira versao.
 *	@author		Daniel Ferreira
 */
public class GerenciadorDataExecucao extends Aplicacoes
{

	/**
	 *	DAO para consulta de configuracoes de promocoes. 
	 */
	private Consulta consulta;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		idProcesso				Identificador do processo.
	 *	@param		consulta				DAO para consulta de configuracoes de promocoes.
	 */
	public GerenciadorDataExecucao(long idProcesso, Consulta consulta)
	{
		super(idProcesso, "GerenciadorDataExecucao");
		
		this.consulta = consulta;
	}
	
    /**
     *	Calcula e atualiza a data de execucao da promocao do assinante de acordo com o tipo de execucao default. 
     *	Utilizado por processos que atualizam a data do assinante e que nao representam concessoes.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@throws		Exception
     */
	public void atualizarDataExecucao(PromocaoAssinante pAssinante, Date dataProcessamento) throws Exception
	{
		//Regra: A data de execucao deve ser definida somente quando o status da promocao do assinante e ativo.
		if(pAssinante.getStatus().isAtivo())
		{
			//Obtendo o mapeamento Promocao / Dia de Execucao da promocao do assinante, com o tipo de execucao default.
			PromocaoDiaExecucao diaExecucao = pAssinante.getDiaExecucao(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT); 
			
			//Regra: Caso a data de execucao antiga esteja definida, sera utilizada como base para o calculo da nova 
			//data de execucao e nao sera feito o ajuste no novo mes de execucao. Isto devido a possibilidade de haver 
			//uma troca de promocao onde o novo periodo de bonificacao e anterior ao antigo. Caso isto aconteca o 
			//assinante deve manter seu mes atual para garantir o recebimento de seu bonus.
			if(pAssinante.getDatExecucao() != null)
				pAssinante.setDatExecucao(this.calcularDataExecucao(diaExecucao, pAssinante.getDatExecucao()));
			else
			{
				//Calculando a data de execucao do assinante utilizando como referencia a data de processamento. 
				pAssinante.setDatExecucao(this.calcularDataExecucao(diaExecucao, dataProcessamento));

				if(pAssinante.getDatExecucao() != null)
					//Aplicando as regras de calculo de mes de execucao.
					this.atualizarMesExecucao(pAssinante, dataProcessamento); 
			}
		}
		else
			pAssinante.setDatExecucao(null);
		
		//Atualizando as datas de analise.
		this.atualizarDatasAnalise(pAssinante);
	}
	
    /**
     *	Calcula e atualiza a nova data de execucao da promocao do assinante apos o processo de concessao de bonus. 
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		tipoExecucao			Tipo de execucao do processo de concessao de bonus.
     *	@param		retorno					Codigo de retorno da operacao.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@throws		Exception
     */
	public void atualizarDataExecucao(PromocaoAssinante pAssinante,
									  String tipoExecucao,
									  short retorno,
									  Date dataReferencia,
									  Date dataProcessamento) throws Exception
	{
		//Regra: A data de execucao somente deve ser atualizada para os tipos de execucao DEFAULT e REBARBA.
		if((tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT)) ||
		   (tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_REBARBA)))
		{
			if(pAssinante.getStatus().isAtivo())
			{
				//Obtendo o mapeamento Promocao / Dia de Execucao da promocao do assinante.
				PromocaoDiaExecucao diaExecucao = pAssinante.getDiaExecucao(tipoExecucao); 
				
				//Atualizando o proximo dia de execucao da promocao do assinante. Utiliza como referencia a data de 
				//execucao atual.
				pAssinante.setDatExecucao(this.calcularDataExecucao(diaExecucao, pAssinante.getDatExecucao()));

				if(pAssinante.getDatExecucao() != null)
					//Aplicando as regras de calculo de mes de execucao.
					this.atualizarMesExecucao(pAssinante, retorno, dataReferencia, dataProcessamento); 
			}
			else
				pAssinante.setDatExecucao(null);
			
			//Atualizando as datas de analise.
			this.atualizarDatasAnalise(pAssinante);
		}
	}
	
    /**
     *	Calcula as datas de execucao dos bonus do assinante na fila de recargas para cada tipo de execucao. Compara a 
     *	data calculada com a ultima concessao de bonus ao assinante, de forma a garantir que o ultimo bonus do 
     *	assinante tenha expiracao de 1 mes. A data final e finalmente antecipada caso ultrapasse o ultimo dia de 
     *	concessao da promocao do assinante.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@return		Datas de execucao na fila de recargas, para cada tipo de execucao.
     *	@throws		Exception
     */
    public Map calcularDatasCredito(PromocaoAssinante pAssinante, Date dataReferencia) throws Exception 
    {
        Map result = new TreeMap();
        
        if((pAssinante != null) && (pAssinante.getPromocao() != null) && (pAssinante.getAssinante() != null))
        {
            //Para cada objeto na lista de mapeamentos de dias de execucao do assinante na promocao, calcular a data
            //de consumo do bonus pela fila de recargas.
            for(Iterator iterator = pAssinante.getDiasExecucao().iterator(); iterator.hasNext();)
            {
                PromocaoDiaExecucao diaExecucao = (PromocaoDiaExecucao)iterator.next();
                
                //Caso o tipo de transacao seja PARCIAL, retroceder 1 mes. Isto porque por definicao o mes de
                //referencia corresponde ao mes em que o assinante recebera a concessao DEFAULT. Como para o tipo de
                //execucao PARCIAL o assinante recebe no mes correspondente ao de analise das ligacoes recebidas, a
                //data de credito devem tambem corresponder a este mes.
                Calendar calReferencia = Calendar.getInstance();
                calReferencia.setTime(dataReferencia);
                if((diaExecucao.getTipExecucao() != null) &&
                   (diaExecucao.getTipExecucao().equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_PARCIAL)))
                {
                    calReferencia.add(Calendar.MONTH, -1);
                    calReferencia.set(Calendar.DAY_OF_MONTH, calReferencia.getActualMinimum(Calendar.DAY_OF_MONTH));
                }
                
                //Calculando a data de credito de acordo com o mapeamento de dias de execucao da promocao do assinante.
                Date dataCredito = this.calcularDataCredito(diaExecucao, calReferencia.getTime());

                //Caso a promocao nao possua dia de execucao de concessao de bonus para o tipo de execucao atual, 
                //passar para o proximo tipo de execucao.
                if(dataCredito == null)
                	continue;
                
                Calendar calCredito = Calendar.getInstance();
                calCredito.setTime(dataCredito);
                
                //Caso a data credito seja anterior a data de referencia, e necessario ajusta-la. Isto porque e 
                //necessario armazena-la, garantindo a validade de 1 mes do bonus.
                if(dataCredito.before(calReferencia.getTime()))
                {
                    calCredito.set(Calendar.DAY_OF_MONTH, calReferencia.get(Calendar.DAY_OF_MONTH));
                    dataCredito = calCredito.getTime();
                }
                
                //Caso o tipo de execucao seja DEFAULT, a promocao tenha expiracao de saldo de bonus e o assinante
                //ainda tenha bonus, deve ser assegurado que a expiracao do ultimo bonus tenha ao menos 1 mes.
                Promocao	promocao		= pAssinante.getPromocao();
                Assinante	assinante		= pAssinante.getAssinante();
                String		tipoExecucao	= diaExecucao.getTipExecucao();
                if(tipoExecucao.equals(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT) && 
                   pAssinante.zerarSaldo(Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT) &&
                  (assinante.getCreditosBonus() > 0.0) && (pAssinante.getDatUltimoBonus() != null))
                {
                    //Obtendo a data do ultimo bonus recebido pelo assinante.
                    Date dataUltimoBonus = pAssinante.getDatUltimoBonus();
                    Calendar calUltimoBonus = Calendar.getInstance();
                    calUltimoBonus.setTime(dataUltimoBonus);
                    //Obtendo o ultimo dia do mes da concessao do ultimo bonus.
                    int ultimoDia = calUltimoBonus.getActualMaximum(Calendar.DAY_OF_MONTH);
                    
                    //Obtendo o numero de dias a serem adicionados a data de credito para assegurar o periodo de 
                    //validade do bonus de 1 mes. OBS: Uma vez que o metodo getTimeInMillis() retorna as datas em 
                    //milissegundos, a diferenca deve ser dividida por 24*60*60*1000 para ser convertida em dias.
                    int diferenca = (int)((calCredito.getTimeInMillis() - calUltimoBonus.getTimeInMillis())/86400000);
                    int numDias = ((ultimoDia - diferenca) >= 0) ? (ultimoDia - diferenca) : 0;

                    //Ajustando a data de credito do bonus, sendo feita a verificacao para nao ultrapasse o ultimo dia 
                    //da concessao DEFAULT da promocao do assinante.
                    Integer idtPromocao = new Integer(promocao.getIdtPromocao());
                    PromocaoDiaExecucao maxDiaRecarga = 
                    	this.consulta.getPromocaoMaxDiaExecucaoRecarga(idtPromocao, tipoExecucao);
                    if((calCredito.get(Calendar.DAY_OF_MONTH) + numDias) <= maxDiaRecarga.getNumDiaExecucaoRecarga().intValue())
                        calCredito.add(Calendar.DAY_OF_MONTH, numDias);
                    else
                        calCredito.set(Calendar.DAY_OF_MONTH, maxDiaRecarga.getNumDiaExecucaoRecarga().intValue());
                    
                    //Extraindo a data de credito do bonus.
                    dataCredito = calCredito.getTime();
                }
                
                //Inserindo a data no resultado final, com o tipo de execucao sendo a chave do mapeamento.
                result.put(diaExecucao.getTipExecucao(), dataCredito);
            }
        }
        
        return result;
    }
    
    /**
     *	Atualiza as datas de analise da promocao do assinante. As datas de analise correspondem ao inicio e o fim do 
     *	mes de analise, que corresponde ao mes anterior ao da data de execucao.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     */
	private void atualizarDatasAnalise(PromocaoAssinante pAssinante)
	{
		Date	dataExecucao	= pAssinante.getDatExecucao();
		Date	dataIniAnalise	= null;
		Date	dataFimAnalise	= null;
		
		if(dataExecucao != null)
		{
			Calendar calAnalise = Calendar.getInstance();
			calAnalise.setTime(dataExecucao);
			calAnalise.add(Calendar.MONTH, -1);
			
			calAnalise.set(Calendar.DAY_OF_MONTH, calAnalise.getActualMinimum(Calendar.DAY_OF_MONTH));
			dataIniAnalise = calAnalise.getTime();
			
			calAnalise.set(Calendar.DAY_OF_MONTH, calAnalise.getActualMaximum(Calendar.DAY_OF_MONTH));
			dataFimAnalise = calAnalise.getTime();
		}
		
		pAssinante.setDatInicioAnalise(dataIniAnalise);
		pAssinante.setDatFimAnalise   (dataFimAnalise);
	}
	
    /**
     *	Calcula a data de execucao da promocao do assinante.
     *
     *	@param		diaExecucao				Mapeamento Promocao / Dia de Execucao.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@return		Data de execucao da promocao do assinante.
     */
    private Date calcularDataExecucao(PromocaoDiaExecucao diaExecucao, Date dataProcessamento) 
    {
        if((diaExecucao != null) && (diaExecucao.getNumDiaExecucao() != null) && (dataProcessamento != null))
        {
            Calendar calResult = Calendar.getInstance();
            calResult.setTime(dataProcessamento);
            calResult.set(Calendar.DAY_OF_MONTH, diaExecucao.getNumDiaExecucao().intValue());
            calResult.clear(Calendar.HOUR_OF_DAY);
            calResult.clear(Calendar.MINUTE);
            calResult.clear(Calendar.SECOND);
            calResult.clear(Calendar.MILLISECOND);
            
            return calResult.getTime();
        }
        
        return null;
    }
    
    /**
     *	Calcula a data de execucao do bonus do assinante na Fila de Recargas.
     *
     *	@param		diaExecucao				Mapeamento Promocao / Dia de Execucao.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@return		Data de execucao na Fila de Recargas.
     */
    private Date calcularDataCredito(PromocaoDiaExecucao diaExecucao, Date dataReferencia) 
    {
        if((diaExecucao != null) && (diaExecucao.getNumDiaExecucaoRecarga() != null) && (dataReferencia != null))
        {
            Calendar calResult = Calendar.getInstance();
            calResult.setTime(dataReferencia);
            calResult.clear(Calendar.HOUR_OF_DAY);
            calResult.clear(Calendar.MINUTE);
            calResult.clear(Calendar.SECOND);
            calResult.clear(Calendar.MILLISECOND);
            
            calResult.set(Calendar.DAY_OF_MONTH, diaExecucao.getNumDiaExecucaoRecarga().intValue());
            if(diaExecucao.getNumHoraExecucaoRecarga() != null)
            	calResult.set(Calendar.HOUR_OF_DAY, diaExecucao.getNumHoraExecucaoRecarga().intValue());
            
            return calResult.getTime();
        }
        
        return null;
    }
	
    /**
     *	Calcula e atualiza o mes da data de execucao da promocao do assinante.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@throws		Exception
     */
	private void atualizarMesExecucao(PromocaoAssinante pAssinante, Date dataProcessamento) throws Exception
	{
		//Data de processamento da operacao.
		Calendar calProcessamento = Calendar.getInstance();
		calProcessamento.setTime(dataProcessamento);
		
		//Data de execucao.
		Calendar calExecucao = Calendar.getInstance();
		calExecucao.setTime(pAssinante.getDatExecucao());
		
		//Data de entrada do assinante na promocao.
		Calendar calEntrada = Calendar.getInstance();
		calEntrada.setTime(pAssinante.getDatEntradaPromocao());
		
		//Regra: Se o mes da data de execucao for igual ao mes de entrada do assinante na promocao, o mes de 
		//execucao deve ser igual ao mes de entrada do assinante + 1.
		if((calProcessamento.get(Calendar.MONTH) == calEntrada.get(Calendar.MONTH)) &&
		   (calProcessamento.get(Calendar.YEAR ) == calEntrada.get(Calendar.YEAR )))
			calExecucao.add(Calendar.MONTH, 1);
		else
		{
			//Regra: Se a data de referencia for maior que o ultimo dia de execucao da promocao do assinante, o mes de 
			//execucao deve ser igual ao mes de referencia + 1.
			PromocaoDiaExecucao maxDiaRecarga = 
				this.consulta.getPromocaoMaxDiaExecucaoRecarga(new Integer(pAssinante.getPromocao().getIdtPromocao()), 
															   Definicoes.CTRL_PROMOCAO_TIPO_EXECUCAO_DEFAULT);
			Date maxDataRecarga = this.calcularDataExecucao(maxDiaRecarga, calExecucao.getTime());
			
			if(calProcessamento.getTime().after(maxDataRecarga))
				calExecucao.add(Calendar.MONTH, 1);
		}
		
		pAssinante.setDatExecucao(calExecucao.getTime());
	}
	
	/**
     *	Calcula e retorna o mes da data de execucao da promocao do assinante. Utilizado por processos que representam 
     *	concessao de bonus.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		retorno					Codigo de retorno da operacao.
     *	@param		dataReferencia			Data de referencia para recebimento do bonus.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@throws		Exception
     */
	private void atualizarMesExecucao(PromocaoAssinante pAssinante, 
									  short retorno, 
									  Date dataReferencia, 
									  Date dataProcessamento) throws Exception
    {
    	//Objeto utilizado para converter as datas em meses. Desta forma os meses podem ser comparados.
    	SimpleDateFormat conversorMes = new SimpleDateFormat("yyyyMM");
    	
		//Data de execucao.
		Calendar calExecucao = Calendar.getInstance();
		calExecucao.setTime(pAssinante.getDatExecucao());
		
		//Regra: O mes de execucao deve ser atualizado somente se o processamento atual nao representar uma concessao 
		//retroativa. A concessao e considerada retroativa se o mes da data de referencia for menor que o da data de 
		//execucao da promocao do assinante.
		if(conversorMes.format(dataReferencia).compareTo(conversorMes.format(pAssinante.getDatExecucao())) >= 0)
		{
	    	//Regra: Os codigos de retorno em que o mes de execucao deve ser avancado em uma unidade sao: Operacao OK, 
			//assinante nao recebeu ligacoes e assinante ja recebeu bonus. Outros codigos de retorno devem passar pelo 
			//calculo do mes de execucao referentes a processos de nao bonificacao.
			switch(retorno)
			{
				case Definicoes.RET_OPERACAO_OK:
				case Definicoes.RET_PROMOCAO_LIGACOES_NOK:
				case Definicoes.RET_PROMOCAO_BONUS_CONCEDIDO:
					calExecucao.add(Calendar.MONTH, 1);
					pAssinante.setDatExecucao(calExecucao.getTime());
					break;
				default: this.atualizarMesExecucao(pAssinante, dataProcessamento);
			}
		}
    }

}
