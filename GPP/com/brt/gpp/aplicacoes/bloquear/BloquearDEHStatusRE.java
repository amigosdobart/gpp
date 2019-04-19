package com.brt.gpp.aplicacoes.bloquear;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.gppExceptions.*;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

//Arquivos de Import Internos
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.Definicoes;

//Arquivos Java
import java.sql.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
* Este arquivo refere-se à classe BloquearDEHStatusRE, responsavel pela implementacao 
* do processo de Bloqueio do serviço de Free Call dos acessos que entraram no Status
* Recharge Expired
*
* <P> Versao:			1.0
*
* @Autor: 			Denys Oliveira
* Data: 				08/04/2005
*
*/
public class BloquearDEHStatusRE extends Aplicacoes 
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
	private MapConfiguracaoGPP mapConfiguracaoGPP;
		     
	/**
	 * Metodo...: BloquearDEHStatusRE
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public BloquearDEHStatusRE (long logId)
	 {
		super(logId, Definicoes.CL_ENVIO_BONUS_CSP14);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: gerarArquivosBloqueioRE
	 * Descricao: Procura por assinantes que tiveram status mudado de 2 para 3
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha									
	 */
	public short gerarArquivosBloqueioRE (String aData) throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		long nroLinhasProcessadas = 0;							// Controla o número de bloqueios por RE efetuados
		String dataInicial = GPPData.dataCompletaForamtada();	// Data de início do processo batch
		String status = Definicoes.TIPO_OPER_SUCESSO;			// Indicador de status de execução do processo
		//Inicializa variaveis do metodo
		short retorno = 0;
		
		try
		{
			// Converte data de referencia para timestamp
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			new java.sql.Timestamp(sdf.parse(dataInicial).getTime());

			// Busca Limite Mínimo de Saldo
			this.mapConfiguracaoGPP = MapConfiguracaoGPP.getInstancia();

			super.log(Definicoes.DEBUG, "gerarArquivosBloqueio", "Inicio do processo de Bloqueio DEH Status RE");

			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			//Selecionar os que passaram para Recharge Expired "+ 
			String sqlBloqueiosRE = 
				"select sub_id from "+
				"( "+
				"select sub_id as from tbl_apr_assinante_tecnomen "+
				"where dat_importacao = trunc(sysdate) "+
				"and account_status = 3 "+
				") ass "+
				"where not exists "+
				"( "+
				"    select idt_msisdn from tbl_apr_bloqueio_servico "+
				"    where idt_msisdn = ass.sub_id "+
				"    and id_servico = 'ELM_FREE_CALL' "+
				") "+
				"and not exists "+
				"(	"+
				"	select idt_msisdn msisdn from tbl_Rec_recargas rec "+
				"	where rec.id_tipo_recarga = 'R' "+
				"	and dat_recarga between trunc(sysdate) and sysdate "+
				"	and idt_msisdn = ass.sub_id "+
				")";
						
			ResultSet rsBloqueios = conexaoPrep.executaQuery(sqlBloqueiosRE, super.getIdLog());
			
			// Gera arquivo de Bloqueio RE
			nroLinhasProcessadas = this.gerarArquivo(rsBloqueios);	
			super.log(Definicoes.INFO, "gerarArquivosBloqueioRE","Bloqueios por RE: "+nroLinhasProcessadas);
		}
		catch(GPPInternalErrorException gppE)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO,"gerarArquivosBloqueioRE","Erro Interno GPP: "+gppE);
			throw new GPPInternalErrorException("Erro Interno GPP: "+gppE);
		}
		catch(ParseException pE)
		{
			status = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO,"gerarArquivosBloqueioRE","Erro Formato Parametro Data (dd/mm/yyyy): "+pE);
			throw new GPPInternalErrorException("EErro Formato Parametro Data (dd/mm/yyyy): "+pE);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = nroLinhasProcessadas + " acessos Bloqueados por Status RE";
			
			//chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_BLOQUEIO_DEH_RE, dataInicial, dataFinal, status, descricao, aData);
			super.log(Definicoes.INFO, "gerarArquivosBloqueioRE", "Fim do processo de Bonus por chamada sainte de CSP14");
		}
			
		return retorno;
	}
	
	/**
	 * Metodo...: gerarArquivo
	 * Descricao: Gera Arquivo com MSISDNs a serem bloqueados por estarem em status RE
	 * @param 	ResultSet	rsBloqueiosRE	ResultSet contendo os msisdns
	 * @return	long		Quantidade de bloqueios
	 * @throws GPPInternalErrorException
	 */
	private long gerarArquivo(ResultSet rsBloqueiosRE) throws GPPInternalErrorException
	{
		long nroBloqueios = 0;
		try
		{
			// Determina o nome do Arquivo de Bloqueio
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String nomeArquivo = "RE_"+sdf.format(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()))+".txt";
			
			// Cria o arquivo
			ManipuladorArquivos manipularArquivo = 
				new ManipuladorArquivos( 	this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("DIR_ARQUIVOS_BLOQUEIO")+
											System.getProperty("file.separator")+
											nomeArquivo,
											true, super.getIdLog());
			
			while (rsBloqueiosRE.next() && nroBloqueios < Long.parseLong(this.mapConfiguracaoGPP.getMapValorConfiguracaoGPP("QUANTUM_BLOQUEIO_RE")))
			{
				manipularArquivo.escreveLinha(rsBloqueiosRE.getString("sub_id"));
				nroBloqueios++;
			}
			
			// Fecha o Arquivo de Bloqueios RE
			manipularArquivo.fechaArquivo();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"gerarArquivoBloqueioRE","Erro ao gerar o arquivo de Bloqueios RE: "+e);
			throw new GPPInternalErrorException("Erro ao Gerar Arquivo de Bloqueio RE: "+e);
		}
		
		return nroBloqueios;
	}
}
