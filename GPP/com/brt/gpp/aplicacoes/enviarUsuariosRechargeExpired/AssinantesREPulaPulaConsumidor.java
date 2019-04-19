package com.brt.gpp.aplicacoes.enviarUsuariosRechargeExpired;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.enviarUsuariosRechargeExpired.AssinantesREPulaPulaProdutor;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe responsavel pelo consumo e execucao do processo de Gerenciamento de assinantes em Recharge Expired 
 *	cadastrados em promocoes Pula-Pula. Recebe lista de MSISDN's do produtor e, caso o assinante possua as 
 *	caracteristicas necessarias, deve mudar de status para Disconnected. 
 *
 *	@author	Daniel Ferreira
 *	@since	17/02/2006
 */
public final class AssinantesREPulaPulaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{

    /**
     *	Objeto utilizado para consulta de assinantes.
     */
    private ConsultaAssinante consultaGPP;
    
    /**
     *	Objeto utilizado para operacoes de troca de status.
     */
    private Aprovisionar aprovisionamentoGPP;
    
    /**
     *	Produtor para notificacao de sucesso.
     */
    private ProcessoBatchProdutor produtor;
    
    /**
     *	Construtor da classe.
     */
	public AssinantesREPulaPulaConsumidor()
	{
		super(GerentePoolLog.getInstancia(AssinantesREPulaPulaConsumidor.class).getIdProcesso(Definicoes.CL_ASSINANTES_RE_PULA_PULA_PROD), 
		      Definicoes.CL_ASSINANTES_RE_PULA_PULA_CONS);
		
		this.consultaGPP = new ConsultaAssinante(super.logId);
		this.aprovisionamentoGPP = new Aprovisionar(super.logId);
	}

    /**
     *	Inicializa o objeto. Nao utilizado. 
     *
     *	@throws		Exception
     */
	public void startup() throws Exception
	{
	}

    /**
     *	Inicializa o objeto. Nao utilizado.
     *
     *	@param		produtor				Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
    /**
     *	Executa o processo de Gerenciamento de assinantes com status Recharge Expired em promocao Pula-Pula.
     *
     *	@return		MSISDN do assinante a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
        short result = Definicoes.RET_MSISDN_NAO_ATIVO;
        
		super.log(Definicoes.DEBUG, "execute", "Inicio");
		
		try
		{
            //Executando a consulta pelo assinante na Plataforma Tecnomen. Esta consulta e necessaria para confirmar
		    //que o assinante ainda encontra-se em status Recharge Expired, uma vez que pode ter feito uma recarga
		    //durante o dia, voltando para o status Normal User, ou entao ter sido desativado.
            Assinante assinante = this.consultaGPP.executaConsultaCompletaAssinanteTecnomen((String)obj);
            //Validando o assinante.
            result = this.validaAssinante(assinante);
            
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Obtendo o numero de dias de expiracao do status Disconnected para mudanca para o status Shutdown.
                MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstancia(); 
                int diasExpiracao = Integer.parseInt(mapConfiguracao.getMapValorConfiguracaoGPP("NUM_DIAS_EXPIRACAO_DISCONNECTED"));
                //Calculando a nova data de expiracao do assinante.
                Calendar calExpiracao = Calendar.getInstance();
                calExpiracao.add(Calendar.DAY_OF_MONTH, diasExpiracao);
                Date dataExpiracao = calExpiracao.getTime();
                //Modificando o status do assinante para Disconnected.
                this.aprovisionamentoGPP.atualizarStatusAssinante(assinante, Definicoes.DISCONNECTED, dataExpiracao, super.nomeClasse);
                
                ((AssinantesREPulaPulaProdutor)this.produtor).notificaSucesso();
                result = Definicoes.RET_OPERACAO_OK;
            }
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		    result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}

    /**
     *	Finaliza a execucao do processo batch. Nao utilizado.
     */
	public void finish()
	{
	}

    /**
     *	Inicializa o objeto. 
     *
     *	@param		produtor				Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	    this.produtor = produtor;
	}
	
    /**
     *	Valida se o assinante deve mudar para o status Disconnected. 
     *
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@return		Codigo de retorno da validacao.
     */
	public short validaAssinante(Assinante assinante)
	{
	    if(assinante == null)
	        return Definicoes.RET_MSISDN_NAO_ATIVO;
	    if(assinante.getRetorno() != Definicoes.RET_OPERACAO_OK)
	        return assinante.getRetorno();
	    if(assinante.getStatusAssinante() != Definicoes.RECHARGE_EXPIRED)
	        return Definicoes.RET_STATUS_MSISDN_INVALIDO;
	    
	    return Definicoes.RET_OPERACAO_OK;
	}
	
}