//Definicao do Pacote
package com.brt.gpp.aplicacoes.retomadaCicloTres;

//Imports de acesso ao banco e padr�o do java
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

//Imports das defini��es, extens�es e excess�es
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
*
* Este arquivo refere-se a classe ProcessaRetomada, responsavel pela implementacao
* da Thread de atualiza��o do IND_RETORNO_CICLO3 na tabela TBL_APR_ASSINANTE, do
* cliente que recebeu mais que X minutos de liga��es locais ou 0800 ou � cobrar 
* dentro do ciclo atual
*
* <P> Versao:			1.0
*
* <P>@Autor: 			Marcelo Alves Araujo
* <P>Data: 				10/06/2005
*
* Modificado por:
* Data:
* Razao:
*
*/
public class ProcessaRetomada extends Aplicacoes implements Runnable 
{
    private String pMSISDN = null;
    private Date dataExpiracao = null;
    private int	tempoMinimo = 0;
    
    public ProcessaRetomada(long aIdProcesso,String msisdn, Date expira, int tempoMin)
    {
        super(aIdProcesso, Definicoes.CL_NOVO_CICLO);
        this.pMSISDN = msisdn;
        this.dataExpiracao = expira;
        this.tempoMinimo = tempoMin;
    }

	/**
	 * Metodo...: setRetornoCiclo
	 * Descricao: Valida o credito inicial do assinante
	 * @param  msisdn		- MSISDN do cliente que atualizado
	 * @param  retornoCiclo	- Valor a ser colocado no campo IND_RETORNO_CICLO3 da tabela TBL_APR_ASSINANTE
	 * @param  conexaoPrep	- Conex�o com o banco de dados
	 * @throws GPPInternalErrorException
	 */
	public void setRetornoCiclo(String msisdn, int retornoCiclo, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
	    super.log(Definicoes.DEBUG,"setRetornoCiclo","Inicio");
		
	    // Query que para alterar o valor do campo IND_RETORNO_CICLO3
	    String sqlGrava = 	"UPDATE " +
	    					"TBL_APR_ASSINANTE " +
							"SET IND_RETORNO_CICLO3 = ? " +
							"WHERE " +
							"IDT_MSISDN = ?";
		    
	    Object parametros[]  = {new Integer(retornoCiclo),msisdn};
		    
	    // Executa a query, atualizando o Banco
	    if (conexaoPrep.executaPreparedUpdate(sqlGrava, parametros, this.logId) > 0)
			this.log(Definicoes.DEBUG, "setRetornoCiclo", "Atualiza��o efetuada com sucesso.");
		else
			this.log(Definicoes.WARN, "setRetornoCiclo",  "MSISDN : "+ msisdn + " - Erro: Nao foi possivel atualizar.");
	}

	/**
	 * Metodo...: run
	 * Descricao: Valida se o cliente atende � condi��o de total de chamadas locais, 0800
	 * e � cobrar recebidas, maior que um limite inferior de chamadas durante o ciclo atual
	 * e seta na TBL_APR_ASSINANTE o IND_RETORNO_CICLO3
	 */
    public void run()
    {
	    super.log(Definicoes.DEBUG,"run","Inicio");
	    
	    PREPConexao conexaoPrep = null;
	    try
	    {
	        // Seleciona conex�o do pool Prep Conex�o
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
	        int diasCiclo;
	        
	        // Seleciona o n�mero de dias da consulta de acordo com o ciclo
	        diasCiclo = Definicoes.DIAS_CICLO_3-1;
	        
	        ResultSet rs;
		    
		    // Query que retorna o total de liga��es locais, 0800 e � cobrar recebidas pelo cliente durante o ciclo
		    String sqlCDR = "select " +
		    				"sum(cdr.call_duration) as soma " +
		    				"from " +
		    				"tbl_ger_cdr cdr " +
		    				"where " +
		    				"(((cdr.tip_chamada = 'RLOCAL------' " +
		    				"or cdr.tip_chamada = 'RLOCAL_HIB--') " +
		    				"and cdr.call_id <> '5132909998') " +
		    				"or cdr.tip_chamada = 'E0800-------' " +
		    				"or cdr.tip_chamada = 'ORIG_COBRAR-') " +
		    				"and cdr.timestamp >= trunc(?-?) " +
		    				"and cdr.sub_id = ? ";
		    
		    Object parametros[]  = {this.dataExpiracao,new Integer(diasCiclo),this.pMSISDN};
		    
		    // Executa a query  de consulta na CDR
		    rs = conexaoPrep.executaPreparedQuery(sqlCDR, parametros, super.getIdLog());
		    
		    if(rs.next())
		        // Se o total de chamadas exceder o m�mimo, a atualiza��o fica habilitada
			    if((rs.getLong("soma")) > this.tempoMinimo)
			        // Seta IND_RETORNO_CICLO3 na tabela TBL_APR_ASSINANTE
			        this.setRetornoCiclo(this.pMSISDN,Definicoes.RETORNAR_CICLO_3,conexaoPrep);
		    
		    rs.close();
		    rs = null;	        
	    }
	    catch (SQLException e)
		{
	        super.log(Definicoes.ERRO, "run",  "MSISDN : "+ this.pMSISDN + " - Erro(SQL): " + super.log.traceError(e));
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "run", "MSISDN : "+ this.pMSISDN + " - Erro GPP: " + super.log.traceError(e));
		}
		finally
		{
		    // Libera conexao com o pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
		    super.log(Definicoes.DEBUG, "run", "Fim");
		}
	}
}
