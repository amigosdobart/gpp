package com.brt.gppAlarmes.entity;

import java.util.Date;
import java.util.Calendar;

/**
 * Esta classe representa as informacoes de agendamento de um alarme
 * 
 * @author Joao Carlos
 * Data..: 17-Mar-2005
 *
 */
public class Agendamento
{
	private String anos[];
	private boolean dias[];
	private boolean meses[];
	private boolean diasSemana[];
	private boolean horas[];
	private boolean minutos[];
	
	public Agendamento()
	{
		// Para o construtor vazio, inicia os valores de configuracao com o valor padrao falso
		// para que haja a possibilidade de instanciar um agendamento e configuracao posterior
		this.dias 	 	= new boolean[32];
		this.meses 	 	= new boolean[13];
		this.horas 	 	= new boolean[24];
		this.minutos 	= new boolean[60];
		this.diasSemana	= new boolean[8];
	}

	public Agendamento(String dias, String meses, String anos, String diasSemana,
			           String horas, String minutos)
	{
		// Para o construtor onde os valores sao passados como argumento, entao inicia os valores
		// de configuracao com o valor padrao falso, porem ja em seguida realiza a atualizacao
		// dos valores indicados
		this.dias 	 	= new boolean[32];
		this.meses 	 	= new boolean[13];
		this.horas 	 	= new boolean[24];
		this.minutos 	= new boolean[60];
		this.diasSemana	= new boolean[8];

		setDias			(dias);
		setMeses		(meses);
		setAnos			(anos);
		setDiasSemana	(diasSemana);
		setHoras		(horas);
		setMinutos		(minutos);
	}

	/**
	 * Metodo....:getProximoAgendamento
	 * Descricao.:Retorna da data do proximo agendamento
	 * @param dataUltExecucao 	- Data da ultima execucao para comparacao
	 * @return Date 			- Data do proximo agendamento a partir da data da ultima execucao
	 */
	public Date getProximoAgendamento(Date dataUltExecucao)
	{
		// Define a variavel de retorno igual a data de comparacao
		// O ano sera o proximo ano definido na configuracao
		// Este ponto e importante para que os valores de milisegundos e segundos nao 
		// interfiram na comparacao futura das datas
		Calendar proxData 	= Calendar.getInstance();
		proxData.setTime(dataUltExecucao);
		proxData.set(Calendar.YEAR,getProximoAno(dataUltExecucao));
		// Realiza a pesquisa das datas provaveis de execucao usando como base
		// o mes da data da ultima execucao. Este criterio foi escolhido, pois
		// a maioria das configurações irão ser executadas no mesmo mes
		for (int mes=proxData.get(Calendar.MONTH)+1; mes < meses.length; mes++)
			if (meses[mes])
				for (int dia=1; dia < dias.length; dia++)
					if (dias[dia])
						for (int hora=0; hora < horas.length; hora++)
							if (horas[hora])
								for (int minuto=0; minuto < minutos.length; minuto++)
									if (minutos[minuto])
									{
										// Portanto para cada configuracao acerta a data
										// utilizada como comparacao e verifica se esta
										// ja esta a frente da ultima execucao, caso verdadeiro
										// entao esta data e a data do proximo agendamento
										proxData.set(Calendar.SECOND		,0);
										proxData.set(Calendar.MINUTE		,minuto);
										proxData.set(Calendar.HOUR_OF_DAY	,hora);
										proxData.set(Calendar.DAY_OF_MONTH	,dia);
										proxData.set(Calendar.MONTH			,mes-1);
										
										if (proxData.getTime().after(dataUltExecucao))
											if (diasSemana[proxData.get(Calendar.DAY_OF_WEEK)])
												return proxData.getTime();
									}
		return proxData.getTime();
	}

	/**
	 * Metodo....:getProximoAno
	 * Descricao.:Retorna o proximo ano definido no agendamento desde que seja maior ou igual
	 *            a data utilizada como parametro
	 * @param data	- Data a ser utilizada para comparacao
	 * @return int	- Ano configurado para a execucao
	 */
	private int getProximoAno(Date data)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		int proxAno = cal.get(Calendar.YEAR);
		if (anos != null && !anos[0].equals("*"))
			for (int i=0; i < anos.length; i++)
				if (Integer.parseInt(anos[i]) >= cal.get(Calendar.YEAR))
					proxAno = Integer.parseInt(anos[i]);

		return proxAno;
	}

	/**
	 * Metodo....:setValores
	 * Descricao.:Define o array de valores para configurar o tempo em que o agendamento ira executar
	 * @param lista		- Lista de valores a serem definidas
	 * @param destino	- Array de destino dos valores
	 * @param tamVetor	- Tamanho do array a ser atualizado
	 */
	private void setValores(String lista, boolean destino[], int tamVetor)
	{
		// Preenche o vetor de anos,dias,meses,diassemana,horas e minutos
		// dependendo dos valores passados com parametro na lista. Por default
		// todos os valores estao falso e somente serao aceitos os valores da
		// lista, caso o caracter * esteja no primeiro campo entao todos os 
		// valores sao possiveis
		if (lista != null)
		{
			String aux[] = lista.split(",");
			if (aux[0].equals("*"))
				for (int j=0; j < tamVetor; j++)
					destino[j]=true;
			else
				for (int i=0; i < aux.length; i++)
					destino[Integer.parseInt(aux[i])]=true;
		}
	}

	public void setDias(String lista)
	{
		setValores(lista,this.dias,this.dias.length);
	}
	
	public void setMeses(String lista)
	{
		setValores(lista,this.meses,this.meses.length);
	}
	
	public void setAnos(String lista)
	{
		if (lista != null && !lista.equals(""))
			this.anos = lista.split(",");
	}

	public void setDiasSemana(String lista)
	{
		setValores(lista,this.diasSemana,this.diasSemana.length);
	}

	public void setHoras(String lista)
	{
		setValores(lista,this.horas,this.horas.length);
	}

	public void setMinutos(String lista)
	{
		setValores(lista,this.minutos,this.minutos.length);
	}
}
