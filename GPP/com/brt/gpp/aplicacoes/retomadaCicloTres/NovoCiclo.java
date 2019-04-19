//Definicao do Pacote
package com.brt.gpp.aplicacoes.retomadaCicloTres;

// Arquivo de Imports de Gerentes do GPP 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;


/**
*
* Este arquivo refere-se a classe NovoCiclo, responsavel pela implementacao da
* lógica de retorno do stautus de assinante de 3 ou 4 para o status 3 para os
* casos em que o cliente está no último ou penúltimo dia do ciclo e o número de
* ligações locais, 0800 e a cobrar excede um limite mínimo de ligações durante
* o ciclo
*
* <P> Versao:			1.0
*
* <P>@Autor: 			Marcelo Alves Araujo
* <P>Data: 				01/06/2005
*
* Modificado por:
* Data:
* Razao:
*
*/
public final class NovoCiclo extends Aplicacoes
{
	private GerentePoolBancoDados gerenteBancoDados = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Metodo...: NovoCiclo
	 * Descricao: Contrutor
	 * @param 	long	aIdProcesso		Id do Processo para efeitos de log
	 */
	public NovoCiclo(long aIdProcesso)
	{
	    super(aIdProcesso, Definicoes.CL_NOVO_CICLO);
		
		// Obtem referencia ao gerente de conexoes ao banco de dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(aIdProcesso);
	}
	
	/**
	 * Metodo...: recarregaNocoCiclo
	 * Descricao: Executa a atualização do assinante e 
	 * @param
	 * @return short	- Sucesso(0) ou erro (diferente de 0) 
	 * @throws GPPInternalErrorException
	 */
	public void recarregaNovoCiclo () throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"recarregaNocoCiclo","Inicio");
	    
		// Data de início e status do processo
    	String dataInicial = GPPData.dataCompletaForamtada();
    	String status = Definicoes.TIPO_OPER_SUCESSO;
    	
    	try
		{
    	    // Habilita os assinantes ao retorno de ciclo
		    this.atualizaRetornoCiclo();
		    
		    // Retoma as réguas dos clientes que estão habilitados e estão na data de expiração
		    this.retomaRegua();		    		    
		}
		catch (GPPInternalErrorException e)
		{
		    status  = Definicoes.TIPO_OPER_ERRO;
		    super.log(Definicoes.ERRO, "recarregaNocoCiclo", "Erro GPP: " + super.log.traceError(e));
		    throw new GPPInternalErrorException("Erro GPP: " + super.log.traceError(e));
		}
		finally
		{
		    String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Atualizacao de Status";
				
			// Chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_NOVO_CICLO, dataInicial, dataFinal, status, descricao, dataInicial);
		       
		    super.log(Definicoes.DEBUG, "recarregaNocoCiclo", "Fim");
		}    
	}
	
	/**
	 * Metodo...: atualizaRetornoCiclo
	 * Descricao: Seta o campo IND_RETORNO_CICLO3 da tabela TBL_APR_ASSINANTE se o cliente estiver
	 * no ciclo 3 ou 4 e tiver feito mais de 10 min de ligações recebidas durante o ciclo
	 * @param
	 * @return 
	 * @throws GPPInternalErrorException
	 */
	public void atualizaRetornoCiclo () throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"atualizaRetornoCiclo","Inicio");
	    
		PREPConexao conexaoPrep = null;
	        	
	    try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para selecionar os cliente que estão no 
		    // ciclo 3, não estão habilitados ao retorno e 
		    // não são das promoções 1, 2, 3 e 4 
		    // [Luciano Vilela] os clientes sem promocao tambem serao retornados
		    // pela consulta 29/02/2008
		    String sqlStatus =	"select " +
		    					"tec.idt_msisdn as msisdn, " +
		    					"tec.dat_expiracao_principal as expira " +
		    					"from " +
		    					"tbl_apr_assinante tec, " +
		    					"tbl_pro_assinante pro " +
		    					"where " +
		    					"tec.ind_retorno_ciclo3 = 0 " +
		    					"and tec.idt_status = 3 " +
		    					"and tec.idt_msisdn = pro.idt_msisdn (+)" +
		    					"and (pro.idt_promocao > 4 or pro.idt_promocao is null)";
		    
		    Object parametro[]  = {};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlStatus, parametro, super.getIdLog());
		    ThreadGroup thGroup = new ThreadGroup("retomadaCiclo");	
		    
		    // Mapeamento da tabela configuração GPP
	        MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
	        
	        // Número limite de threads que poderão ser utilizadas
	    	Integer numThreads = new Integer(map.getMapValorConfiguracaoGPP("NUM_THREADS_RETOMADA_CICLO"));
	    	
	    	// Tempo de espera até uma nova para testar se existe thread livre
	    	Integer tempoEspera = new Integer(map.getMapValorConfiguracaoGPP("RETOMADA_CICLO_TEMPO_ESPERA"));

	    	// Tempo de espera até uma nova para testar se existe thread livre
	    	Integer tempoMinimo = new Integer(map.getMapValorConfiguracaoGPP("TEMPO_MINIMO_CHAMADAS"));
	    		    		    	
		    while(rs.next())
		    {
		        // Espera até que uma das n threads estaja livre
		        while (thGroup.activeCount() >= numThreads.intValue())
		            synchronized(this)
		            {
		                super.log(Definicoes.DEBUG, "atualizaRetornoCiclo", "Dormindo por 10s");
		                Thread.sleep(tempoEspera.intValue());
		            }

		        ProcessaRetomada proc = new ProcessaRetomada(super.getIdLog(),rs.getString("msisdn"),rs.getDate("expira"),tempoMinimo.intValue());
		        Thread t = new Thread(thGroup,proc);
		        
		        // Executa uma Thread de habilitação do retorno de ciclo
		        t.start();
		    }
		    
		    // Espera o fim de todas as threads
		    while (thGroup.activeCount() > 0)
	            synchronized(this)
	            {
	                super.log(Definicoes.DEBUG, "atualizaRetornoCiclo", "Esperando Termino das Threads");
	                Thread.sleep(10*1000);	                
	            }

		    // Libera o ResultSet
		    rs.close();
		    rs = null;		    
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "atualizaRetornoCiclo", "Erro(SQL): " + super.log.traceError(e));
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "atualizaRetornoCiclo", "Erro GPP: " + super.log.traceError(e));
		} 
		catch (InterruptedException e) 
		{
		    super.log(Definicoes.ERRO, "atualizaRetornoCiclo", "Erro GPP: " + super.log.traceError(e));
        } 
		finally
		{
		    // Libera conexao com o pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "atualizaRetornoCiclo", "Fim");
		}
	}
	
	/**
	 * Metodo...: retomaRegua
	 * Descricao: Retorna o cliente com ao inicio do ciclo 3 se o campo IND_RETORNO_CICLO3 da tabela 
	 * TBL_APR_ASSINANTE estiver setado, se estiver no fim do ciclo 3 ou 4 e se o cliente estiver desbloqueado 
	 * @param
	 * @return 
	 * @throws GPPInternalErrorException
	 */
	public void retomaRegua () throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"retomaRegua","Inicio");
	    
		PREPConexao conexaoPrep = null;
	        	
	    try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para selecionar os cliente que estão no fim do ciclo 3 ou 4
		    String sqlStatus =	"select " +
		    					"tec.idt_msisdn as msisdn, " +
		    					"tec.dat_expiracao_principal as expira, " +
		    					"tec.idt_status as status " +
		    					"from " +
		    					"tbl_apr_assinante tec " +
		    					"where " +
		    					"(trunc(tec.dat_expiracao_principal) = trunc(sysdate) " +
		    					"or trunc(tec.dat_expiracao_principal) = trunc(sysdate) + 1) " +
		    					"and tec.ind_retorno_ciclo3 = 1 ";
		    
		    Object parametro[]  = {};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlStatus, parametro, super.getIdLog());
		    
		    while(rs.next())
		    {
		        String 		msisdn = rs.getString("msisdn");
		        Date		dataExpiracao = rs.getDate("expira");
		        	               
		        // Retorna o status do assinante ao início deo ciclo 3 se estiver desbloqueado e seu status for confirmado
	            String atualizado = this.atualizaDadosAssinante(msisdn,dataExpiracao);
	            
	            // Data da atualização
	        	String dataInicial = GPPData.dataCompletaForamtada();
	            
	        	// Se o assinante foi atualizado na Tecnomen
	        	if(atualizado.equals(Definicoes.TIPO_OPER_SUCESSO))
	            {
	                super.log(Definicoes.DEBUG,"retomaRegua","O status do assinante: " + msisdn + " foi atualizado com sucesso" );
	                // Grava na tabela TBL_GER_HIST_RETOMADA_CICLO log da execução
	                this.gravaAtualizacao(msisdn,dataInicial,atualizado);
	                // Desabilita o retorno do assinante ao início do ciclo 3
	                this.setRetornoCiclo(msisdn,Definicoes.NAO_RETORNAR_CICLO_3);
	            }
	        	// Se o assinante já havia sido atualizado
	        	else if(atualizado.equals(Definicoes.TIPO_OPER_PARCIAL))
	        	{
	        	    super.log(Definicoes.DEBUG,"retomaRegua","O status do assinante: " + msisdn + " foi atualizado anteriormente" );
	                // Desabilita o retorno do assinante ao início do ciclo 3
	                this.setRetornoCiclo(msisdn,Definicoes.NAO_RETORNAR_CICLO_3);
	        	}
	        	// Se ocorreu erro na atualização da Tecnomen
	            else if(atualizado.equals(Definicoes.TIPO_OPER_ERRO))
	            {
	                super.log(Definicoes.WARN,"retomaRegua","O status do assinante: " + msisdn + " nao foi atualizado" );
	                this.gravaAtualizacao(msisdn,dataInicial,atualizado);
	            }
		    }
		    
		    rs.close();
		    rs = null;		    
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "retomaRegua", " Erro(SQL): " + super.log.traceError(e));
		    throw new GPPInternalErrorException("Erro SQL: " + super.log.traceError(e));
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "retomaRegua", "Erro GPP: " + super.log.traceError(e));
		}
		finally
		{
		    // Libera conexao com o pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "retomaRegua", "Fim");
		}
	}
		
	/**
	 * Metodo...: atualizaDadosAssinante
	 * Descricao: Atualiza o status e a data de expiração do cliente caso esses 
	 * parâmetros sejam confirmados na tecnomen e o cliente não esteja bloqueado 
	 * @param  msisdn			- MSISDN do cliente
	 * @param  dataExpiracao	- Data de expiração do ciclo no formato dd/mm/aaaa
	 * @return String 			- "SUCESSO" ou "ERRO" 
	 */
	private String atualizaDadosAssinante (String msisdn, Date dataExpiracao)
	{
	    super.log(Definicoes.DEBUG, "atualizaDadosAssinante", "Inicio MSISDN "+msisdn);
		
		String retorno = Definicoes.TIPO_OPER_PARCIAL;
		
		try
		{
		    // Cria objeto aprovisionar para consultar e alterar situação do cliente
		    Aprovisionar apr = new Aprovisionar(super.getIdLog());
		    // Objeto que contém informações retiradas da tecnomen sobre o assinante
		    Assinante assinante = apr.consultaAssinante(msisdn);
		    
		    if(assinante != null)
		    {
			    // Se o status do assinante não for confirmado na tecnomen a atualização não é feita
				if(assinante.getStatusAssinante() != Definicoes.RECHARGE_EXPIRED)
				    super.log(Definicoes.DEBUG, "atualizaDadosAssinante", "Status do assinante: "+msisdn+" diferente de 3");
				
				// Se a data de expiração do assinante não for confirmada na tecnomen a atualização não é feita 
				// Inclusao feita em 14/09/07 por Joao Carlos.
				// A comparacao realizada entre a data de expiracao nao pode ser realizada entre um objeto 
				// Date e String, portanto realiza a formatacao da data de expiracao para String no formato
				// dd/MM/yyyy para comparar com a data vinda no objeto Assinante que eh String
				else if(!assinante.getDataExpiracaoPrincipal().equals(sdf.format(dataExpiracao)))
				    super.log(Definicoes.DEBUG, "atualizaDadosAssinante", "Data de expiracao do assinante: "+msisdn+" diferente do banco");
				
				// Se o cliente não está bloqueado
				else if(assinante.getStatusServico() == Definicoes.SERVICO_DESBLOQUEADO)
				{
				    // Incrementa a data de expiração para voltar o assinante ao início do ciclo 3
			        dataExpiracao = assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, (short)Definicoes.DIAS_CICLO_3);
					
			        // Atualiza o novo status na tecnomen
			        apr.atualizarStatusAssinante(assinante, Definicoes.RECHARGE_EXPIRED, dataExpiracao, Definicoes.GPP_OPERADOR);
				    
			 	    // Operação realizada com sucesso
			 	    retorno = Definicoes.TIPO_OPER_SUCESSO;
					
					super.log(Definicoes.INFO, "atualizaDadosAssinante", "SUCESSO atualizaStatusAssinante: " + msisdn);
			    }	
		    }
		}
		catch (GPPInternalErrorException e)
		{
		    retorno = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO, "atualizaDadosAssinante", "MSISDN : "+ msisdn + " - Erro interno GPP:" + super.log.traceError(e));
		}
		catch (Exception e)
		{
		    retorno = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO, "atualizaDadosAssinante", "MSISDN : "+ msisdn + " - Erro interno GPP:" + super.log.traceError(e));
		}
		
		super.log(Definicoes.DEBUG, "atualizaDadosAssinante", "Fim");
		
		return retorno;
	}
			
	/**
	 * Metodo...: gravaAtualizacao
	 * Descricao: Grava na tabela TBL_GER_HIST_RETOMADA_CICLO o MSISDN, 
	 * a data e o status da execução da atualização status do cliente
	 * @param  msisdn			- MSISDN do cliente que atualizado
	 * @param  dataAtualizacao	- Data da atualização
	 * @param  status			- Status do processo de atualização
	 * @return 
	 * @throws GPPInternalErrorException
	 */
	public void gravaAtualizacao (String msisdn, String dataAtualizacao, String status) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"gravaAtualizacao","Inicio");
		
	    PREPConexao conexaoPrep = null;
	    
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    // Transforma a string da data em um objeto Date
		    SimpleDateFormat tempo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		    Date inicio = tempo.parse(dataAtualizacao);
		    
		    // Query que retorna o total de ligações locais, 0800 e à cobrar recebidas pelo cliente durante o ciclo
		    String sqlGrava = 	"INSERT INTO TBL_GER_HIST_RETOMADA_CICLO " +
								" (IDT_MSISDN, DAT_EXECUCAO, IDT_STATUS_EXECUCAO) " +
								"VALUES " +
								" (?, " +
								" ?, " +
								" ?) ";
		    
		    Object parametros[]  = {msisdn,new Timestamp(inicio.getTime()),status};
		    
		    // Executa a query, incluindo o registro da atualização no Banco
		    if (conexaoPrep.executaPreparedUpdate(sqlGrava, parametros, this.logId) > 0)
				this.log(Definicoes.DEBUG, "gravaAtualizacao", "Reinicio do Ciclo 3 efetuado com sucesso.");
			else
				this.log(Definicoes.WARN, "gravaAtualizacao", "MSISDN : "+ msisdn + " - Erro: Nao foi possivel inserir o registro da atualizacao.");
		}
	    catch (ParseException e)
	    {
	        super.log(Definicoes.ERRO, "gravaAtualizacao", "MSISDN : "+ msisdn + " Data : "+ dataAtualizacao + 
	        		                                                 " - Erro de Parse: " + super.log.traceError(e));
		    throw new GPPInternalErrorException("Erro de Parse: " + super.log.traceError(e));
	    }
	    catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "gravaAtualizacao", "Erro no processo de gravaAtualizacao: Nao conseguiu pegar conexao com banco de dados.");
		}
		finally
		{
		    // Libera conexao com o pool PREP
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
		    super.log(Definicoes.DEBUG, "gravaAtualizacao", "Fim");
		}
	}

	/**
	 * Metodo...: setRetornoCiclo
	 * Descricao: Valida o credito inicial do assinante
	 * @param  msisdn		- MSISDN do cliente que atualizado
	 * @param  retornoCiclo	- Valor a ser colocado no campo IND_RETORNO_CICLO3 da tabela TBL_APR_ASSINANTE
	 * @return 
	 * @throws GPPInternalErrorException
	 */
	public void setRetornoCiclo(String msisdn, int retornoCiclo)
	{
	    super.log(Definicoes.DEBUG,"setRetornoCiclo","Inicio");
		
	    PREPConexao conexaoPrep = null;
	    
	    try
	    {
	        // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    // Query que para alterar o valor do campo IND_RETORNO_CICLO3
		    String sqlGrava = 	"UPDATE " +
		    					"TBL_APR_ASSINANTE " +
								"SET IND_RETORNO_CICLO3 = ? " +
								"WHERE " +
								"IDT_MSISDN = ?";
		    
		    Object parametros[]  = {new Integer(retornoCiclo),msisdn};
		    
		    // Executa a query, atualizando o Banco
		    if (conexaoPrep.executaPreparedUpdate(sqlGrava, parametros, this.logId) > 0)
				this.log(Definicoes.DEBUG, "setRetornoCiclo", "Atualização efetuada com sucesso.");
			else
				this.log(Definicoes.WARN, "setRetornoCiclo", "Erro: Nao foi possivel atualizar.");
		}
	    catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "setRetornoCiclo", "MSISDN : "+ msisdn + " - Erro no processo: Nao conseguiu pegar conexao com banco de dados.");
		}
	    catch(Exception e){
	    	super.log(Definicoes.ERRO, "setRetornoCiclo", "MSISDN : "+ msisdn + " Erro : " + super.log.traceError(e));
	    }
		finally
		{
		    // Libera conexao com o pool PREP
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
		    super.log(Definicoes.DEBUG, "setRetornoCiclo", "Fim");
		}
	}
}

