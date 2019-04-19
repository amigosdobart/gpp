package com.brt.gpp.aplicacoes.promocao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.promocao.EstornoPulaPulaVO;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.OperacoesEstornoPulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pela execucao do processo de Estorno de Bonus Pula-Pula por Fraude.
 *
 *	@author	Daniel Ferreira
 *	@since	16/12/2005
 *	@review_author Magno Batista Corrêa
 *	@review_date 16/05/2006 dd/mm/yyyy
 */
public final class EstornoPulaPulaConsumidor extends ControlePulaPula implements ProcessoBatchConsumidor
{

    //Construtores.
    
    /**
     *	Construtor da classe.
     */
	public EstornoPulaPulaConsumidor()
	{
		super(GerentePoolLog.getInstancia(EstornoPulaPulaConsumidor.class).getIdProcesso(Definicoes.CL_PROMOCAO_ESTORNO_PULA_PULA_PROD), 
		      Definicoes.CL_PROMOCAO_ESTORNO_PULA_PULA_CONS);
	}

	//Implementacao de Consumidor.
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
     */
	public void startup() throws Exception
	{
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(Produtor)
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(Object)
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");
		OperacoesEstornoPulaPula operacoes = null;
		
		try
		{
            //Obtendo a lista de registros e a interface de operacoes para execucao do estorno do assinante.
			Collection listaEstorno = ((EstornoPulaPulaVO)obj).getListaEstorno();
			operacoes = ((EstornoPulaPulaVO)obj).getOperacoes();
			
            for(Iterator iterator = listaEstorno.iterator(); iterator.hasNext();)
            {
                InterfaceEstornoPulaPula estorno = (InterfaceEstornoPulaPula)iterator.next();
                
                //Atualizando a data de processamento do registro.
                estorno.setDatProcessamento(new Timestamp(Calendar.getInstance().getTimeInMillis()));
                
                //Como a natureza do processo e transacional por assinante, e necessario desligar o auto-commit.
                operacoes.setAutoCommit(false);
                
                try
                {
                    //Executando o estorno e atualizando o registro.
                    short codigoRetorno = super.executaEstornoPulaPula(estorno, operacoes);
                    String statusProcessamento = (codigoRetorno == Definicoes.RET_OPERACAO_OK) ? 
                        Definicoes.IDT_PROCESSAMENTO_OK : Definicoes.IDT_PROCESSAMENTO_ERRO;
                    estorno.setIdtCodigoRetorno(codigoRetorno);
                    estorno.setIdtStatusProcessamento(statusProcessamento);
                }
                catch(Exception e)
                {
        		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
        		    estorno.setIdtCodigoRetorno(Definicoes.RET_ERRO_TECNICO);
                    estorno.setIdtStatusProcessamento(Definicoes.IDT_PROCESSAMENTO_ERRO);
                }
                finally
                {
                    //Finalizando a transacao.
                    try
                    {
                        if(estorno.getIdtCodigoRetorno() == Definicoes.RET_OPERACAO_OK)
                            operacoes.commit();
                        else
                            operacoes.rollback();
                    }
                    catch(Exception e)
                    {
                        super.log(Definicoes.ERRO, "executaEstornoPulaPula", "Excecao: " + e);
                        estorno.setIdtCodigoRetorno(Definicoes.RET_ERRO_TECNICO);
                        estorno.setIdtStatusProcessamento(Definicoes.IDT_PROCESSAMENTO_ERRO);
                    }
                    //Atualizando o status de execucao do registro de estorno.
                    try
                    {
                        operacoes.setAutoCommit(true);
                        Date	dataReferencia	= estorno.getDatReferencia();
                        String	msisdn			= estorno.getIdtMsisdn();
                        String	idtNumeroOrigem	= estorno.getIdtNumeroOrigem();
                        operacoes.atualizarInterfaceEstornoPulaPula(dataReferencia, msisdn, idtNumeroOrigem, estorno);
                    }
                    catch(Exception e)
                    {
                        super.log(Definicoes.ERRO, "executaEstornoPulaPula", "Excecao: " + e);
                    }
                }
            }
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		    throw e;
		}
		finally
		{
		    //Fechando a interface de operacoes.
			if(operacoes != null)
				operacoes.close();
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
	public void finish()
	{
	}

	//Implementacao de ProcessoBatchConsumidor.
	
    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(ProcessoBatchProdutor)
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	}
	
}