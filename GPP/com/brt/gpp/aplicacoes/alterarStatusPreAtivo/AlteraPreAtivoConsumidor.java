package com.brt.gpp.aplicacoes.alterarStatusPreAtivo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;


/**
 *
 * Este arquivo refere-se a classe AlteraPreAtivoConsumidor, 
 * ele altera o status dos assinantes sem tráfego para 5 
 * (shutdown), e para 2 (normal) os assinantes com tráfego
 * 
 * @version		1.0
 * @autor		Marcelo Alves Araujo
 * @since		11/11/2005
 *
 */
public class AlteraPreAtivoConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private PREPConexao					conexaoPrep;
	private AlteraPreAtivoProdutor		produtor;
	private Aprovisionar 				aprovisiona;
	private Assinante					assinante;
	private MapConfiguracaoGPP 			map;
	private GerentePoolTecnomen 		gerenteTecnomen;
	private Ajustar 					ajuste; 
	
	/**
	 * Construtor da classe do processo batch
	 */
	public AlteraPreAtivoConsumidor()
	{
		super(GerentePoolLog.getInstancia(AlteraPreAtivoConsumidor.class).getIdProcesso(Definicoes.CL_ALTERA_PREATIVO),Definicoes.CL_ALTERA_PREATIVO);
		aprovisiona = new Aprovisionar(super.getIdLog());
		assinante = new Assinante();
		gerenteTecnomen = GerentePoolTecnomen.getInstancia(super.getIdLog());		
	}
	
	/**
	 * Pega uma conexão com o banco de dados
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws GPPInternalErrorException
	{
		map = MapConfiguracaoGPP.getInstancia();
		ajuste = new Ajustar(super.getIdLog());
		super.log(Definicoes.DEBUG, "Consumidor.startup", "Inicio");
	}
	
	/**
	 * Pega uma conexão com o banco de dados
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws GPPInternalErrorException
	{
		startup((ProcessoBatchProdutor)produtor);		
	}	
	
	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor) throws GPPInternalErrorException
	{
		conexaoPrep = produtor.getConexao();
		this.produtor = (AlteraPreAtivoProdutor)produtor;
		startup();
	}	
	
	/**
	 * Executa as alterações de status dos assinantes
	 * @param Objeto com os dados gerados pelo Produtor
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object objeto) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");
		
		// Faz o cast do objeto para o ValueObject desejado
		AlteraPreAtivoVO altera = (AlteraPreAtivoVO)objeto;
		
		try
		{
			// Pega a data limite que o assinante pode ficar 
	        //no status 1 após o desbloqueio do hotline
	        Date dataLimite = GPPData.getDataAcrescidaDias(-(new Integer(map.getMapValorConfiguracaoGPP("LIMITE_DIAS_PREATIVO")).intValue()));
	        	        
	        short retorno = Definicoes.RET_OPERACAO_OK;        
	        
	        if(assinante != null)
	        {
		        // Assinantes que geraram tráfego não tarifado nos últimos dois dias
		        if(gerouTrafego(altera.getMsisdn(),new Integer(map.getMapValorConfiguracaoGPP("NUM_DIAS_CONSULTA_CDR_PREATIVO"))))
		        {
		        	assinante = aprovisiona.consultaAssinante(altera.getMsisdn());
		        	
		        	if(assinante != null)
		        	{
			        	retorno = trocaStatus(assinante, Definicoes.NORMAL);
			        	
			        	aprovisiona.gravaAtualizaStatusAssinante(altera.getMsisdn(), 
			        											 Definicoes.TIPO_APR_STATUS_NORMAL,
			        											 Definicoes.TIPO_APR_STATUS_FIRSTIME,
																 new Date(),
																 retorno, 
																 super.nomeClasse);
			        	
			        	if(retorno == Definicoes.RET_OPERACAO_OK)
			        	{
							registrarCreditoInicial(assinante);
							produtor.incrementarTotalNormal();
			        	}
		        	}
		        	else
		        		retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
		        }
		        else if(altera.getData().getTime() < dataLimite.getTime())
		        {
		        	// Assinantes que não geraram tráfego em 90 dias
		        	if(!gerouTrafego(altera.getMsisdn(),new Integer(map.getMapValorConfiguracaoGPP("LIMITE_DIAS_PREATIVO"))))
		        	{
		        		assinante = aprovisiona.consultaAssinante(altera.getMsisdn());
		        		
		        		if(assinante != null)
			        	{
				        	retorno = trocaStatus(assinante, Definicoes.SHUT_DOWN);
							
							aprovisiona.gravaAtualizaStatusAssinante(altera.getMsisdn(), 
																	 Definicoes.TIPO_APR_SHUTDOWN,
																	 Definicoes.TIPO_APR_STATUS_FIRSTIME,
																	 new Date(),
																	 retorno, 
																	 super.nomeClasse);
							
							if(retorno == Definicoes.RET_OPERACAO_OK)
								produtor.incrementarTotalDesligado();
			        	}
			        	else
			        		retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
					}		        	
		        }
	        }
	    }
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO,"execute","Consulta " + altera.getMsisdn() + " com problemas. Erro: " + e);
		}
		
		super.log(Definicoes.DEBUG,"execute","Consulta " + altera.getMsisdn());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		super.log(Definicoes.DEBUG, "Consumidor.finish", "Inicio");
		// Libera conexao de banco de dados ao pool
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}
	
	/**
	 * Troca o status do assinante
	 * @param msisdn		- Msisdn do assinante
	 * @param novoStatus	- Status para o qual deve ser mudada a consulta
	 * @param dataExpiracao	- Data de Expiracao dos créditos do assinante
	 * @throws Exception
	 */
	private short trocaStatus(Assinante assinante, short novoStatus) throws Exception 
	{
		super.log(Definicoes.DEBUG, "trocaStatus", "Inicio");
		
		short retorno = Definicoes.RET_OPERACAO_OK;		
		
		// Testa se o assinante continua no status 1
		if(assinante.getStatusAssinante()==Definicoes.FIRST_TIME_USER)
		{
			// Muda o status do assinante na tecnomen
			retorno = alterarStatusAssinante(assinante, novoStatus);
			super.log(Definicoes.INFO, "trocaStatus", "O assinante: " + assinante.getMSISDN() + " foi migrado para status " + novoStatus);
		}
		else
		{
			super.log(Definicoes.WARN, "trocaStatus", "O assinante: " + assinante.getMSISDN() + " nao esta no status 1.");
			retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
		}
		
		return retorno;
	}
	
	/**
	 * Retorna se o assinante gerou tráfego ou não dentro do período especificado
	 * @param msisdn	- Msisdn do assinante
	 * @param numDias	- Número de dias a analisar 	
	 * @throws GPPInternalErrorException 
	 * @throws SQLException 
	 */
	private boolean gerouTrafego(String msisdn, Integer numDias) throws GPPInternalErrorException, SQLException
	{
		super.log(Definicoes.DEBUG, "gerouTrafego", "Inicio");
		
		boolean trafego = false;
		
		String remover = 	"SELECT " +
							" 1 " +
							"FROM " +
							" tbl_ger_cdr cdr, tbl_ger_dia_teleaviso teleaviso " +
							"WHERE " +
							" cdr.sub_id = ? AND " +
							" cdr.timestamp > SYSDATE - ? AND " +
							" cdr.rate_name NOT LIKE '%MCN%' AND " +
							" (cdr.tip_chamada LIKE '%SMO%' OR " +
							"  cdr.tip_chamada LIKE '%VC%' OR" +
							"  cdr.tip_chamada = 'RLOCAL------') and " +
							" TRUNC(cdr.TIMESTAMP) <> teleaviso.dat_teleaviso ";
		
		Object parametros[] = {msisdn,numDias};
		
		ResultSet consulta = conexaoPrep.executaPreparedQuery(remover, parametros, getIdLog());
		
		if(consulta.next())
			trafego = true;
		
		consulta.close();
		
		return trafego;
	}	
	
	/**
	 * Metodo que altera o status de um assinante, e avança em 30 dia a data de expiração
	 * @param assinante Objeto com dados do assinante
	 * @param status 	Novo status do assinante
	 * @return short - Código de Retorno
	 */
	public short alterarStatusAssinante(Assinante assinante, short status)
	{
		int retorno = Definicoes.RET_ERRO_TECNICO;
		
		//Obtendo a nova data de expiracao.
		Calendar calExpiracao = Calendar.getInstance();
		if(status == Definicoes.STATUS_NORMAL_USER)
			calExpiracao.add(Calendar.DAY_OF_MONTH, 30);
		Date dataExpiracao = calExpiracao.getTime();
		
		TecnomenAprovisionamento conexao = null;
		try
		{
            //Obtendo conexao de aprovisionamento.
            conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            
            // Atualizacao da data de saldo principal.
            retorno = conexao.atualizarStatusAssinante(assinante.getMSISDN(), 
            										   status, 
            										   dataExpiracao,
            										   dataExpiracao,
            										   dataExpiracao, 
            										   dataExpiracao, 
            										   dataExpiracao);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"","Erro ao alterar status do assinante "+assinante.getMSISDN()+" para o status "+ status+". Erro:"+super.log.traceError(e));
		}
		finally
		{
			this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, super.getIdLog());
		}
		return (short)retorno;
	}
	
	/**
	 * Metodo que registra um ajust inicial de ativação para os clientes
	 * @param assinante Objeto com dados do assinante
	 * @return short - Código de Retorno
	 * @throws GPPTecnomenException 
	 */
	public short registrarCreditoInicial(Assinante assinante)
	{		
		return this.ajuste.executarAjuste(assinante.getMSISDN(), 
										  Definicoes.AJUSTE_STATUS_FIRST_TIME_NORMAL,
										  Definicoes.TIPO_CREDITO_REAIS, 
										  assinante.getCreditosPrincipal(),
										  Definicoes.TIPO_AJUSTE_CREDITO, 
										  Calendar.getInstance().getTime(),
										  Definicoes.SO_GPP, 
										  super.nomeClasse, 
										  Calendar.getInstance().getTime(), 
										  assinante, 
										  "", 
										  false);
	}
	
}