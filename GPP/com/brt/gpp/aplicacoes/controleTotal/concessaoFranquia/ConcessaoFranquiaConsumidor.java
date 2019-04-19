package com.brt.gpp.aplicacoes.controleTotal.concessaoFranquia;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.recarregar.Recarregar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapValoresRecarga;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 *	Classe responsavel pela execucao do processo de Concessao de Franquia Controle Total
 *
 *	@author	Magno Batista Corrêa
 *	@since	2007/05/17 (yyyy/mm/dd)
 *	
 *	@modify Correcao da data de expiracao do Saldo Periodico, ignorando
 *			os 20 anos concedidos pela Tecnomen na mudanca de status normal
 *			para parcialmente bloqueado
 *	@author João Paulo Galvagni
 *	@since  2007/12/17
 */
public final class ConcessaoFranquiaConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    private MapValoresRecarga mapValoresRecarga;
    private ConcessaoFranquiaDAO dao;
    private Aprovisionar aprovisionar;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     */
	public ConcessaoFranquiaConsumidor()
	{
		super(GerentePoolLog.getInstancia(ConcessaoFranquiaConsumidor.class).getIdProcesso(
				Definicoes.CL_CONCESSAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR), 
		        Definicoes.CL_CONCESSAO_FRANQUIA_CONTROLE_TOTAL_CONSUMIDOR);
		
		this.conexaoPrep = null;
	}
	
	//Implementacao de ProcessoBatchConsumidor.
	
	/**
     *	@param		Produtor				produtor					Produtor de registros para execucao.
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		this.startup((ProcessoBatchProdutor) produtor);
	}
	
    /**
     *	Inicializa o objeto.
     *
     *	@param		ProcessoBatchProdutor	produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.conexaoPrep = produtor.getConexao();
		this.startup();
	}
	
    /**
     *	Inicializa o objeto. Uma vez que a unica operacao necessaria para o startup e a atribuicao do produtor, neste
     *	caso o metodo nao faz nada. 
     *
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		this.mapValoresRecarga 	= MapValoresRecarga.getInstancia();
		this.dao 				= new ConcessaoFranquiaDAO(this.conexaoPrep,super.logId);
		this.aprovisionar 		= new Aprovisionar(super.logId);
	}
	
	//Implementacao de Consumidor.
    
	/**
     *	Executa o processo de Concessao de Franquia Controle Total para o Assinante Informado.
     *	<BR> Para cada cada VO passado, procssa a concessao.</BR>
     *
     *	@return		obj						VO a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio do processamento com o vo:"+obj);
		ConcessaoFranquiaVO vo = (ConcessaoFranquiaVO) obj;
		// Caso ocorra algum erro dentro do GPP e nao efetive a recarga.
		short retorno = Definicoes.RET_ERRO_GENERICO_GPP ;
		
		try
		{
			if( vo.getRetorno() != Definicoes.RET_OPERACAO_OK )
				retorno = vo.getRetorno();
			else
				retorno = this.processarConcessao(vo);
		}
		catch(Exception e)
		{
			retorno = Definicoes.RET_ERRO_GENERICO_GPP; //Caso ocorra algum erro imprevisto
		    super.log(Definicoes.ERRO, "execute", "vo:"+obj+"Excecao: " + e);
		}
		finally
		{
			vo.setRetorno(retorno);
			
			try
			{
				this.finalizar(vo);
			}
			catch(Exception e)
			{
			    super.log(Definicoes.ERRO, "execute",
			    		"Falha ao Registrar Concessao Franquia Controle Total no Banco de dados.vo:"+obj);
			}
			
		    super.log(Definicoes.DEBUG, "execute", "Fim.vo:"+obj);
		}
	}
	
	/**
	 * Processa a concessao da franquia
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	private short processarConcessao(ConcessaoFranquiaVO vo) throws Exception
	{
		short retorno;
		
		// testando se o numero da assinatura paga informada pelo SAG 
		// eh igual a proxima franquia a ser concedida armazenada pelo GPP
		int numUltimaFranquiaConcedida = dao.getNumeroUltimaFranquiaConcedida(vo.getMsisdn());
		if( numUltimaFranquiaConcedida + 1 == vo.getNumeroRecarga() )
		{
			// Faz a consulta do assinante na Tecnomen.
			ConsultaAssinante consultaAssinante = new ConsultaAssinante(super.logId);
			//Assinante assinante = consultaAssinante.executaConsultaCompletaAssinanteTecnomen(vo.getMsisdn());
			Assinante assinante = consultaAssinante.consultarAssinantePlataforma(vo.getMsisdn());
			
			// Validar os dados enviados pelo SAG atraves do ETI 
			retorno = this.validaDados(assinante, vo);
			// Faz todas as acoes do caso que deve conceder a franquia
			if( retorno == Definicoes.RET_OPERACAO_OK )
			{
				retorno = ajustarDataExpiracao(assinante, vo);
				// Caso consiga atualizar o estatus periodico e a data de expiracao processa a recarga
				// Conferir se o Plano do assinante consultado na Tecnomen é o mesmo informado pelo SAG através do ETI
				if( retorno == Definicoes.RET_OPERACAO_OK )
				{
					retorno = ajustarPlanoPreco(assinante, vo);
					if( retorno == Definicoes.RET_OPERACAO_OK )
						
						// Cerne do processo
						retorno = this.efetuarConcessao(vo);
					else
						super.log(Definicoes.INFO, "execute", "Plano de preco nao atualizado para o assinante:" + vo.toString());
				}
				else
				    super.log(Definicoes.INFO, "execute", "Status periodico nao atualizado para o assinante:" + vo.toString());
			}
			else
			    super.log(Definicoes.INFO, "execute", "Dados nao validados para o assinante:" + vo.toString());
		}
		else // Trata o caso da concessão nao ser a correta a ser processada
			retorno = Definicoes.RET_CICLO_PLANO_HIBRIDO_JA_RODADO;
		return retorno;
	}
	
	/**
	 * Ajusta a data de Expiracao do assinante
	 * @param assinante
	 * @param vo
	 * @return
	 * @throws GPPInternalErrorException 
	 */
	private short ajustarDataExpiracao(Assinante assinante, ConcessaoFranquiaVO vo)
	{
		// Conferir se o Status Periódico é diferente de Normal User
		short retorno = Definicoes.RET_ERRO_TECNICO;
		// Nao precisa ser tomada nenhuma acao pois o ajuste de expiracao jah altera o status
		
		// Calcular nova data de expiracao do saldo de franquia
		Date novaDataExpiracaoFranquia = novaDataExpiracaoFranquiaPorValorFanquia(assinante,vo.getValorFranquia());
		if (novaDataExpiracaoFranquia == null) // Caso nao tenha conseguido montar a nova data de expiracao devido a nao ocorrencia do mapeamento de valores
			retorno = Definicoes.RET_VALOR_CREDITO_INVALIDO;
		else
		{
			TecnomenRecarga conexaoRecarga = null;
			
			try
			{
				// Caso consiga calcular uma nova data de expiracao
				vo.setNovaDataExpiracao(novaDataExpiracaoFranquia);
				
				conexaoRecarga = GerentePoolTecnomen.getInstance().getTecnomenRecarga(super.getIdLog());
				
				// Alterar o stausPeriodico para NormalUser neste momento, com a nova data de Expiracao
				retorno= conexaoRecarga.atualizarStatusAssinante(assinante.getMSISDN(),
																 assinante.getStatusAssinante(),
																 assinante.getDataExpPrincipal(),
																 Definicoes.STATUS_PERIODICO_NORMAL_USER,
																 vo.getNovaDataExpiracao(),
																 assinante.getDataExpBonus(),
																 assinante.getDataExpTorpedos(),
																 assinante.getDataExpDados());
			}
			catch (GPPInternalErrorException e)
			{
				log(Definicoes.ERRO, "ajustarDataExpiracao", "Erro ao atualizar o status do assinante " + vo.getMsisdn() + ". retorno:" + retorno);
			}
			finally
			{
				GerentePoolTecnomen.getInstance().liberaConexaoRecarga(conexaoRecarga, super.getIdLog());
			}
		}
		
		return retorno;
	}
	
	/**
	 * Ajusta o plano de preco do assinante, caso necessario
	 * @param assinante
	 * @param vo
	 * @return
	 */
	private short ajustarPlanoPreco(Assinante assinante, ConcessaoFranquiaVO vo)
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		short planoPreco = assinante.getPlanoPreco();
		
		// Atualizar PlanoPreco
		if(planoPreco != vo.getPlanoPrecoGPP())
			retorno = this.aprovisionar.trocarPlanoPreco(vo.getMsisdn(),vo.getPlanoPrecoGPP(),
														 0.0,0.0,Definicoes.GPP_OPERADOR);
		
		return retorno;
	}
	
	/**
	 * Finalizacoes para cada VO
	 * @param vo
	 * @throws GPPInternalErrorException
	 */
	private void finalizar(ConcessaoFranquiaVO vo) throws GPPInternalErrorException
	{
		dao.atualizarConcessaoFranquia(vo);
	}
	
	/**
	 * Valida os daos informados pelo SAG
	 * @param assinante
	 * @param vo
	 * @return
	 */
	private short validaDados (Assinante assinante, ConcessaoFranquiaVO vo)
	{
		// Por algum motivo desconhecido o objeto assinante nao foi criado
		if (assinante == null)
			return Definicoes.RET_ERRO_GENERICO_GPP;
		
		// Validar se o assinate estah aprovisionado na Plataforma
		short retorno = assinante.getRetorno();
		if (retorno != Definicoes.RET_OPERACAO_OK)
			return retorno;
		
		// Validar se o assinante estah com algum bloqueio de servico
		short statusServico = assinante.getStatusServico();
		if (statusServico != Definicoes.SERVICO_DESBLOQUEADO)
			return Definicoes.RET_MSISDN_BLOQUEADO;
		
		return retorno;
	}
	
	/**
	 * Calcula a nova data de Expiracao da Franquia com base nas informacoes do assinante e do valor pago
	 * @param assinante
	 * @param idValor
	 * @return Data ou <code>null</code> no caso do valores recarga nao estar configurado
	 * @throws GPPInternalErrorException
	 */
	private Date novaDataExpiracaoFranquiaPorValorFanquia(Assinante assinante, double idValor)
	{
		Date dataExpPeriodico = assinante.getDataExpPeriodico();
		Date dataAtual 		  = Calendar.getInstance().getTime();
		Date dataBase		  = dataAtual;
		
		// Caso alguma falha na Tecnomen
		if (dataExpPeriodico == null)
			dataExpPeriodico = dataAtual;
		
		// Aqui valida o pagamento do assinante em relacao a data de expiracao
		// do saldo periodico (Tecnomen). Caso essa data de expiracao seja maior
		// ou igual ao dia do processamento, significa que o pagamento foi em dia
		// Verifica-se entao o status periodico do assinante, e se esse for diferente 
		// de Normal User, a data de expiracao devera ser setada como a data atual, 
		// para evitar 20 anos de expiracao + 30 dias.
		
		if ( assinante.getStatusPeriodico() == Definicoes.STATUS_PERIODICO_NORMAL_USER && 
		     dataExpPeriodico.after(dataAtual) )
			dataBase = dataExpPeriodico;
		
		Categoria categoria = MapPlanoPreco.getInstancia().getPlanoPreco(assinante.getPlanoPreco()).getCategoria();
		ValoresRecarga valor = this.mapValoresRecarga.getValoresRecarga(idValor, categoria, dataAtual);
		
		if (valor == null)
		{
			super.log(Definicoes.ERRO, "novaDataExpiracaoFranquia", "Valor de credito invalido ("+idValor+") para o msisdn "+assinante.getMSISDN());
			return null; // Caso o valor de recarga nao esteja configurado, retorna nulo
		}
		
		int numDiasExpiracao = valor.getNumDiasExpiracaoPeriodico();
		Calendar novaDataExpiracaoFranquia = Calendar.getInstance();
		novaDataExpiracaoFranquia.setTime(dataBase);
		novaDataExpiracaoFranquia.add(Calendar.DATE, numDiasExpiracao);
        
        //Truncando a nova data de expiracao
        novaDataExpiracaoFranquia.set(Calendar.HOUR_OF_DAY, 0);
        novaDataExpiracaoFranquia.set(Calendar.MINUTE,		0);
        novaDataExpiracaoFranquia.set(Calendar.SECOND, 		0);
        novaDataExpiracaoFranquia.set(Calendar.MILLISECOND, 0);
        
		return novaDataExpiracaoFranquia.getTime();
	}
	
	/**
	 * Chama a funcao de recarga para creditar o valor de recarga no MSISDN
	 * 
	 * @param ConcessaoFranquiaVO vo - Dados para efetuar a concessao
	 * @return short - 0 se recarga efetuada, outro valor se recarga não efetuada
	 * @throws Exception
	 */
	public short efetuarConcessao(ConcessaoFranquiaVO vo) throws Exception
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		Date dataAtual = Calendar.getInstance().getTime();
		
		String tipoCredito   = Definicoes.TIPO_CREDITO_FRANQUIA;
		String sistemaOrigem = Definicoes.SO_GNV;
		String operador      = Definicoes.GPP_OPERADOR;
		String tipoTransacao = Definicoes.RECARGA_FRANQUIA_CONTROLE_TOTAL;
		
		// Seleciona um identificador para p registro de recarga
		String identificacaoRecarga = dao.getIdentificacaoRecarga();
		
		super.log(Definicoes.DEBUG, "Consumidor.efetuarConcessao", "Identificao Recarga: "	+ identificacaoRecarga);
		
		// Chama a funcao de recarga
		Recarregar recarregar = new Recarregar(super.logId);
		retorno = recarregar.executarRecarga(vo.getMsisdn(), 
										 	 tipoTransacao,
										 	 identificacaoRecarga, 
										 	 tipoCredito, 
										 	 vo.getValorFranquia(),
										 	 dataAtual,
										 	 sistemaOrigem,
										 	 operador,
										 	 null,
										 	 null,
										 	 null,
										 	 null,
										 	 null,
										 	 null,
										 	 null);
		
		super.log(Definicoes.DEBUG, "Consumidor.efetuarConcessao", "Resposta Recarga: " + retorno);
		
		return retorno;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	}
}