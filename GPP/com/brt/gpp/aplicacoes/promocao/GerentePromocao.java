package com.brt.gpp.aplicacoes.promocao;

import java.sql.Timestamp;
//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.persistencia.SelecaoAssinantes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *	Classe responsavel pelo processo batch de inclusao de assinantes em promocoes. Este processo e dividido em
 *	3 sub-processos:
 *		1. Inclusao de novos assinantes (recem-ativados) em promocoes.
 *		2. Gerenciamento de assinantes pendentes de primeira recarga para inclusao em promocoes.
 *		3. Gerenciamento de ex-assinantes hibridos que mudaram de plano para pre-pago.
 *
 *	@author	Daniel Ferreira
 *	@since	18/08/2005
 */
public class GerentePromocao extends Aplicacoes
{

    private ControlePromocao	controle;
    private SelecaoAssinantes	selecao;
    
    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
	public GerentePromocao(long logId)
	{
		super(logId, Definicoes.CL_PROMOCAO_GERENTE);
		
		this.controle	= new ControlePromocao(logId);
	}

    /**
     *	Gerencia assinantes para inclusao em promocoes.
     *
     *	@param		processamento			Data de processamento da operacao, no formato dd/MM/yyyy.
     *	@return								Codigo de retorno da operacao.
     */
	public short gerenciaPromocao(String processamento)
	{
	    short result = Definicoes.RET_OPERACAO_OK;
	    //Date dataProcessamento = null;
	    StringBuffer mensagem = new StringBuffer();
	    
		super.log(Definicoes.INFO, "gerenciaPromocao", "Inicio Data " + processamento);
		
		//Obtendo a data de inicio da execucao do processo.
		Calendar calExecucao = Calendar.getInstance();
		Timestamp inicioExecucao = new Timestamp(calExecucao.getTimeInMillis());
		
		//Gerenciamento de assinantes pendentes de primeira recarga.
		String msgPendente = this.gerenciaPendenciaRecarga();
		mensagem.append("PENDENTES DE RECARGA: ");
		mensagem.append(msgPendente);
		//mensagem.append(" - ");
		
		/*	23/02/2006: Retirada de gerenciamento de ativacoes e troca de plano hibrido/pre porque o Clarify
		 	tornou-se o master do Pula-Pula.
		try
		{
		    SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
			dataProcessamento = conversorDate.parse(processamento);
		    
			//Gerenciamento de ativacao de novos assinantes.
			String msgAtivacao = this.gerenciaAtivacao(dataProcessamento);
			mensagem.append("ATIVACAO DE ASSINANTES: ");
			mensagem.append(msgAtivacao);
			mensagem.append(" - ");
			
			//Gerenciamento de troca de plano hibrido / pre-pago.
			String msgTrocaPlano = this.gerenciaTrocaPlanoHibPre(dataProcessamento);
			mensagem.append("TROCA DE PLANO HIBRIDO / PRE-PAGO: ");
			mensagem.append(msgTrocaPlano);
			
		}
		catch(ParseException e)
		{
			super.log(Definicoes.ERRO, "gerenciaPromocao", "Excecao de parse: " + e);
			dataProcessamento = Calendar.getInstance().getTime();
			mensagem = new StringBuffer("Excecao de parse: " + e);
			result = Definicoes.RET_FORMATO_DATA_INVALIDA;
		}
		*/
		
		//Obtendo a data de fim da execucao do processo.
		calExecucao = Calendar.getInstance();
		Timestamp fimExecucao = new Timestamp(calExecucao.getTimeInMillis());

		try
		{
			//Inserindo registro no historico de processos batch.
		    SimpleDateFormat conversorTimestamp = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
			super.gravaHistoricoProcessos(Definicoes.IND_GERENTE_PROMOCAO, 
			        			 		  conversorTimestamp.format(inicioExecucao), 
			        			 		  conversorTimestamp.format(fimExecucao), 
			        			 		  Definicoes.PROCESSO_SUCESSO, 
			        			 		  mensagem.toString(),
			        			 		  processamento);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "gerenciaPromocao", "Excecao: " + e);
		    super.log(Definicoes.WARN, "gerenciaPromocao", "Nao foi possivel inserir o registro no historico de execucao do Gerente Promocao");
		}
		
		super.log(Definicoes.INFO, "gerenciaPromocao", "Fim retorno " + result);

		return result;
	}	

    /**
     *	Gerencia assinantes recem-ativados para inclusao em promocoes.
     *
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@return								Codigo de retorno da operacao.
     */
	/*
	private String gerenciaAtivacao(Date dataProcessamento)
	{
	    String result = "Numero de assinantes processados: ";
	    int subCounter = 0;
	    
		super.log(Definicoes.INFO, "gerenciaAtivacao", "Inicio");
		
		try
		{
		    //Obtendo a lista de assinantes recem-ativados.
		    this.selecao = new SelecaoAssinantes(SelecaoAssinantes.ATIVACAO, super.logId);
		    selecao.execute(dataProcessamento);
		    
		    //Executando processo de insercao do assinante nas promocoes vigentes.
		    Assinante assinante = null;
		    while((assinante = this.selecao.next()) != null)
		    {
		        //Obtendo objetos para passagem de parametro para o metodo de inclusao em promocoes. O objeto
		        //dataReferencia sera utilizado para determinar em quais promocoes o assinante deve entrar.
		        String	msisdn			= assinante.getMSISDN();
		        Date	dataReferencia	= assinante.getDataAtivacao();
		        int		status			= assinante.getStatusAssinante();
		        int		plano			= assinante.getPlanoPreco();
		        boolean	fezRecarga		= assinante.fezRecarga();
		        int retorno = this.controle.inserePromocoesAssinante(msisdn,
		                											 status,
		                											 plano,
		                											 fezRecarga,
						  											 dataReferencia,
						  											 new Timestamp(Calendar.getInstance().getTimeInMillis()),
						  											 super.nomeClasse, 
						  											 Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO);
		        subCounter += (retorno == Definicoes.RET_OPERACAO_OK) ? 1 : 0;
		    }
		    
		    result += String.valueOf(subCounter);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "gerenciaAtivacao", "Excecao: " + e);
		    result = e.getMessage();
		}
		finally
		{
	        try
	        {
	            this.selecao.close();
	        }
	        catch(GPPInternalErrorException e)
	        {
	            super.log(Definicoes.WARN, "gerenciaAtivacao", "Excecao interna do GPP: " + e);
	        }
		    
		    super.log(Definicoes.INFO, "gerenciaAtivacao", "Fim");
		}
		
		return result;
	}
	*/
	
    /**
     *	Gerencia assinantes pendentes de primeira recarga para inclusao em promocoes.
     *
     *	@return								Codigo de retorno da operacao.
     */
	private String gerenciaPendenciaRecarga()
	{
	    String		result		= "Numero de assinantes processados: ";
	    int			subCounter	= 0;
	    PREPConexao	conexaoPrep	= null;
	    
		super.log(Definicoes.INFO, "gerenciaPendenciaRecarga", "Inicio");
		
		try
		{
		    //Obtendo conexao com o banco de dados.
		    conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
		    
		    //Obtendo a lista de assinantes recem-ativados.
		    this.selecao = new SelecaoAssinantes(SelecaoAssinantes.PENDENTE_RECARGA, super.logId);
		    selecao.execute(null);
		    
		    //Executando processo de insercao do assinante nas promocoes vigentes.
		    Assinante assinante = null;
		    while((assinante = this.selecao.next()) != null)
		    {
		        //Obtendo objetos para passagem de parametro para o metodo de inclusao em promocoes. O objeto
		        //dataReferencia sera utilizado para determinar em quais promocoes o assinante deve entrar.
		        String	msisdn			= assinante.getMSISDN();
		        Date	dataReferencia	= assinante.getDataAtivacao();
		        int		status			= assinante.getStatusAssinante();
		        int		plano			= assinante.getPlanoPreco();
		        boolean	fezRecarga		= assinante.fezRecarga();
		        
		        //Se o assinante executou recarga, invocar o metodo de inclusao do assinante em promocoes.
		        if(fezRecarga)
		        {
			        int retorno = this.controle.inserePromocoesAssinante(msisdn,
							 											 status,
							 											 plano,
							 											 fezRecarga,
							 											 dataReferencia,
							 											 new Timestamp(Calendar.getInstance().getTimeInMillis()),
							 											 super.nomeClasse, 
							 											 Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO,
							 											 conexaoPrep);
			        
			        if(retorno == Definicoes.RET_OPERACAO_OK)
			        {
				        subCounter++;
				        
				        //Finalmente, se a operacao foi OK, retirar a promocao de pendencia de recarga da 				        
				        //lista de promocoes do assinante.
			            this.controle.retiraPromocaoAssinante(msisdn, 
  							  								  Definicoes.CTRL_PROMOCAO_PENDENTE_RECARGA,
  							  								  new Timestamp(Calendar.getInstance().getTimeInMillis()),
  							  								  super.nomeClasse,
  							  								  Definicoes.CTRL_PROMOCAO_MOTIVO_RECARGA_EFETUADA,
  							  								  conexaoPrep);
			        }
		        }
		    }
		    
		    result += String.valueOf(subCounter);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "gerenciaPendenciaRecarga", "Excecao: " + e);
		    result = e.getMessage();
		}
		finally
		{
	        try
	        {
	            this.selecao.close();
	        }
	        catch(GPPInternalErrorException e)
	        {
	            super.log(Definicoes.WARN, "gerenciaPendenciaRecarga", "Excecao interna do GPP: " + e);
	        }
		    
		    //Liberando a conexao com o banco de dados.
		    super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
		    
		    super.log(Definicoes.INFO, "gerenciaPendenciaRecarga", "Fim");
		}
		
		return result;
	}
	
    /**
     *	Gerencia assinantes que trocaram de plano hibrido para pre-pago na data passada por parametro 
     *	para inclusao em promocoes.
     *
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@return								Codigo de retorno da operacao.
     */
	/*
	private String gerenciaTrocaPlanoHibPre(Date dataProcessamento)
	{
	    String result = "Numero de assinantes processados: ";
	    int subCounter = 0;
	    
		super.log(Definicoes.INFO, "gerenciaTrocaPlanoHibPre", "Inicio");
		
		try
		{
		    //Obtendo a lista de assinantes recem-ativados.
		    this.selecao = new SelecaoAssinantes(SelecaoAssinantes.TROCA_PLANO_HIB_PRE, super.logId);
		    selecao.execute(dataProcessamento);
		    
		    //Executando processo de insercao do assinante nas promocoes vigentes.
		    Assinante assinante = null;
		    while((assinante = this.selecao.next()) != null)
		    {
		        //Obtendo objetos para passagem de parametro para o metodo de inclusao em promocoes. O objeto
		        //dataReferencia sera utilizado para determinar em quais promocoes o assinante deve entrar.
		        String	msisdn			= assinante.getMSISDN();
		        Date	dataReferencia	= assinante.getDataAtivacao();
		        int		status			= assinante.getStatusAssinante();
		        int		plano			= assinante.getPlanoPreco();
		        boolean	fezRecarga		= assinante.fezRecarga();
		        int retorno = this.controle.inserePromocoesAssinante(msisdn,
		                											 status,
		                											 plano,
		                											 fezRecarga,
						  											 dataReferencia,
						  											 new Timestamp(Calendar.getInstance().getTimeInMillis()),
						  											 super.nomeClasse, 
						  											 Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PLANO);
		        subCounter += (retorno == Definicoes.RET_OPERACAO_OK) ? 1 : 0;
		    }
		    
		    result += String.valueOf(subCounter);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "gerenciaTrocaPlanoHibPre", "Excecao: " + e);
		    result = e.getMessage();
		}
		finally
		{
	        try
	        {
	            this.selecao.close();
	        }
	        catch(GPPInternalErrorException e)
	        {
	            super.log(Definicoes.WARN, "gerenciaTrocaPlanoHibPre", "Excecao interna do GPP: " + e);
	        }
		    
		    super.log(Definicoes.INFO, "gerenciaTrocaPlanoHibPre", "Fim");
		}
		
		return result;
	}
	*/

}