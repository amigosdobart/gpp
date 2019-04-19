package com.brt.gpp.aplicacoes.bloquear;

import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.Definicoes;

/**
* Esta classe é responsável pelo consumo das solicitações de bloqueio geradas pela
* classe BloquearServico
*    
* <P> Versao:        	1.0
*
* @Autor:            	Denys Oliveira
* Data:                 15/03/2005
*
* Modificado por:
* Data:
* Razao:
*
*/

public class ConsumoBloqueio implements Runnable
{
	private GerentePoolLog gerPoolLog;
	private long		   idProcesso;
	private java.sql.Timestamp dataHora;

	/**
	 * Metodo....:ConsumoBloqueio
	 * Descricao.:Inicializacao (construtor) da classe
	 *
	 */
	public ConsumoBloqueio(java.sql.Timestamp dataHora)
	{
		gerPoolLog = GerentePoolLog.getInstancia(this.getClass());
		idProcesso = gerPoolLog.getIdProcesso(Definicoes.CN_RECARGA);
		this.dataHora = dataHora;
	}
	
	/**
	 * Metodo...:consomeBloqueios
	 * Descricao:Este metodo e responsavel por consumir as recargas disponiveis
	 *            no pool do produtor de recargas
	 * @param ajustar - Referencia para a classe que realiza o ajuste
	 */
	private void consomeBloqueios()
	{
		SolicitacaoBloqueio sB = null;
		
		// Busca a referencia para o Hash com as solicitações de Bloqueio
		AcumularSolicitacoesBloqueio acumuladorSolicitacoes = AcumularSolicitacoesBloqueio.getInstancia();
		
		try
		{
			// Incrementa contador de Threads ativas
			acumuladorSolicitacoes.notificaInicioProcessamento();

			// Enquanto ainda existir bloqueio a ser processado, a thread é executada
			sB = acumuladorSolicitacoes.getProximaSolicitacaoBloqueio();
			while ( sB != null )
			{
				// Solicita bloqueio/desbloqueio de serviço
				ProcessaBloqueioDesbloqueio processador = new ProcessaBloqueioDesbloqueio(sB, this.dataHora, idProcesso);
				short retBloqueio = processador.processarBloqueioDesbloqueio();
				
				// Verificar retorno do bloqueio/desbloqueio
				switch(retBloqueio)
				{
					// Bloqueio/Desbloqueio concluído com sucesso
					case 0:
					{
						if(sB.getAcao().equals("Bloquear"))
						{
							acumuladorSolicitacoes.incrementaBloqueios();						
						}
						else
						{
							acumuladorSolicitacoes.incrementaDesbloqueios();						
						}
						break;							
					}
					// Bloqueio/Desbloqueio Inviável
					case 1:
					{
						acumuladorSolicitacoes.incrementaImpossiveis();
						break;
					}
					// Bloqueio/Desbloqueio Inviável
					case 2:
					{
						acumuladorSolicitacoes.incrementaImpossiveis();
						break;
					}
				}
				sB = acumuladorSolicitacoes.getProximaSolicitacaoBloqueio();
			}
		}
		catch(Exception ge)
		{
			gerPoolLog.log(idProcesso,Definicoes.ERRO,Definicoes.CL_CONSUMO_RECARGA,"run","Erro ao realizar o processamento do bloqueio: "+sB.getMsisdn()+":"+sB.getAcao()+":"+sB.getServico()+":"+ge);
		}
		finally
		{
			// Avisa o singleton que essa Thread concluiu seu trabalho
			acumuladorSolicitacoes.notificaFimProcessamento();
		}
	}

	/**
	 * Metodo....:run
	 * Descricao.:Inicia o consumo das solicitações de bloqueio/desbloqueio
	 * @see run
	 */
	public void run()
	{
		gerPoolLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_CONSUMO_RECARGA,"run","Inicio");

		// Consome os bloqueios disponiveis
		consomeBloqueios();

		gerPoolLog.log(idProcesso,Definicoes.DEBUG,Definicoes.CL_CONSUMO_RECARGA,"run","Fim");
	}
}
