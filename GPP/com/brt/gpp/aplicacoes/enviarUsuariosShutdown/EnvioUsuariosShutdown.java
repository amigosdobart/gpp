//Definicao do Pacote
package com.brt.gpp.aplicacoes.enviarUsuariosShutdown;

// Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Arquivos de Import Internos
import java.sql.*;
import java.util.Vector;
import java.io.StringReader;

/**
  *
  * Este arquivo refere-se a classe EnvioUsuariosShutdown, responsavel pela implementacao da
  * logica de envio de usuarios com status de Shutdown
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Vanessa Andrade
  * Data: 				24/03/2004
  *
  * Modificado por:		Denys Oliveira	
  * Data:				27/05/2004
  * Razao:				Correções/Revisões
  *
  */

public final class EnvioUsuariosShutdown extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; // Gerente de conexoes Banco Dados
		     
	// Dados referentes a cada MSISDN
	Vector linhaMsisdn = new Vector();

	/**
	 * Metodo...: EnvioUsuariosShutdown
	 * Descricao: Construtor 
	 * @param	logId	- Identificador do Processo para Log
	 * @return									
	 */
	 public EnvioUsuariosShutdown (long logId)
	 {
		super(logId, Definicoes.CL_ENVIO_USUARIO_SHUTDOWN);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	 }

	/**
	 * Metodo...: enviaUsuariosShutdown
	 * Descricao: Le os dados da tabela que devem ser enviados para o EAI 
	 * 			  caso o usuario esteja no status de Shutdown
	 * @param 	aData		- Data do processamento do batch (formato DD/MM/AAAA)
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha									
	 * @throws 	GPPInternalErrorException
	 * @throws 	GPPTecnomenException													
	 */
	public short enviaUsuariosShutdown (String aData) throws GPPInternalErrorException
	{
        //Inicializa variaveis do metodo
		short retorno = 0;
		long aIdProcesso = super.getIdLog();

		PREPConexao conexaoPrep = null;
		String msisdn = "";
		String dataProcessamento = aData;
		String statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
		String xmlRetorno = "";
		int cont = 0;

		String dataInicial = GPPData.dataCompletaForamtada();
		
		super.log(Definicoes.INFO, "enviaUsuariosShutdown", "Inicio DATA "+aData);
				
		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = gerenteBancoDados.getConexaoPREP(aIdProcesso);
			
			// Seleciona da tabela de eventos de aprovisionamento os usuarios que estão em shutdown
			String sql_shutdown =	"SELECT sub_id													" 
								+ 	"  FROM tbl_apr_assinante_tecnomen a, tbl_ger_plano_preco b     " 
								+	" WHERE a.account_status = ?                                    " 
								+	"   AND a.profile_id = idt_plano_preco                          " 
								+	"   AND b.idt_categoria = ?                                     " 
								+	"   AND a.dat_importacao = TO_DATE (?, 'dd/mm/yyyy')          	" 
								+	"   AND EXISTS (                                                " 
								+	"          SELECT 1                                             " 
								+	"            FROM tbl_apr_assinante_tecnomen                    " 
								+	"           WHERE sub_id = a.sub_id                             " 
								+	"             AND dat_importacao = TO_DATE (?, 'dd/mm/yyyy')-1	" 
								+	"             AND account_status != ?)                          ";
			
			Object param[] =	{new Integer(Definicoes.STATUS_SHUTDOWN)
					 			,new Integer(Definicoes.CATEGORIA_PREPAGO)
					 			,dataProcessamento
					 			,dataProcessamento
					 			,new Integer(Definicoes.STATUS_SHUTDOWN)};

			// verifica qual msisdn trocou o status para shutdown
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql_shutdown, param, super.logId);
			
			if (rs.next())
			{
				do
				{
					msisdn = rs.getString(1);
					
					super.log(Definicoes.DEBUG, "enviaUsuariosShutdown", "Telefone: " + msisdn);
 			 
					String statusAssinante = Definicoes.S_SHUT_DOWN;

					// Cria cada DadosUsusarioShutdown num vetor
					DadosUsusariosShutdown dadosUsusarioShutdown = new DadosUsusariosShutdown(msisdn, statusAssinante, dataProcessamento);
					linhaMsisdn.add(dadosUsusarioShutdown);
						
					// gera o XML
					xmlRetorno = geraUsuariosShutdonwXML();
						
					// grava na tabela um registro para cada xml
					gravaUsuariosShutdown (conexaoPrep, xmlRetorno, aData);

					linhaMsisdn.removeAllElements();
					
					cont = cont + 1;

				} while (rs.next());
				
			}
			rs.close();
			rs = null;
 			
		}		
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "enviaUsuariosShutdown", "Excecao Interna GPP: "+ e1);
			retorno = 1;
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
		}
		catch (SQLException e2)
		{
			super.log(Definicoes.WARN, "enviaUsuariosShutdown", "Excecao SQL:"+ e2);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException ("Excecao GPP:" + e2);				
		}
		finally
		{

			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Foram registrados " + cont + " xmls de usuarios em Shutdown";
			
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			// chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_USUARIO_SHUTDOWN, dataInicial, dataFinal, statusProcesso, descricao, dataProcessamento);

			// Deleta todos os registros processados com sucesso 
			limpaProcessoOk();
			super.log(Definicoes.INFO, "enviaUsuariosShutdown", "Fim");
		}
		return retorno;
	}
	
	
	/**
	 * Metodo...: limpaProcessoOk
	 * Descricao: Deleta os dados da tabela que foram processados com sucesso, 
	 * 			  ou seja, os usuario que foram extraidos para o EAI
	 * @param
	 * @return	void									
	 * @throws 	GPPInternalErrorException
	 */
	
	public void limpaProcessoOk ()
	{

		super.log(Definicoes.DEBUG, "limpaProcessoOk", "Inicio");

		try
		{
			// Busca uma conexao de banco de dados		
			PREPConexao conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
	
			String idt_processamento_ok = Definicoes.IDT_PROCESSAMENTO_OK;
			
			// Pega configuração GPP -> Para prazo de dias sem deleção das tabelas de interface.
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 

			// Deleta da tabela os usuarios processados com sucesso
			String sql_limpa_ppp_out = "DELETE FROM TBL_INT_PPP_OUT " +
								  "WHERE IDT_STATUS_PROCESSAMENTO = ? AND IDT_EVENTO_NEGOCIO = ? " +
								  " and dat_cadastro < (sysdate - ?)";
				
			// Deleta da TBL_INT_PPP_IN dados que o ETI vai colocar, que não serve para o GPP
			// Mas que o GPP deve apagar
			String sql_limpa_ppp_in = "DELETE FROM TBL_INT_PPP_IN "+
									"WHERE IDT_STATUS_PROCESSAMENTO = ? AND IDT_EVENTO_NEGOCIO = ? " +
									" and dat_cadastro < (sysdate - ?)";
	
			Object paramLimpaIn[] = {idt_processamento_ok, Definicoes.IND_EVENTO_SHUTDOWN_PPP_IN,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
			Object paramLimpaOut[] = {idt_processamento_ok, Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT,map.getMapValorConfiguracaoGPP(Definicoes.PRAZO_DELECAO_TABELAS_INTERFACE)};
		 	
		 	int linhasPPPOut = conexaoPREP.executaPreparedUpdate(sql_limpa_ppp_out,paramLimpaOut, super.logId);
		 	super.log(Definicoes.DEBUG,"limpaProcessosOk","Deletados "+linhasPPPOut+" Registros da TBL_INT_PPP_OUT");
		 	
		 	int linhasPPPIn = conexaoPREP.executaPreparedUpdate(sql_limpa_ppp_in,paramLimpaIn, super.logId);
			super.log(Definicoes.DEBUG,"limpaProcessosOk","Deletados "+linhasPPPIn+" Registros da TBL_INT_PPP_OUT");
			
			 	
 			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "limpaProcessoOk", "Erro no limpaProcessoOk");
		}
		finally
		{
			super.log(Definicoes.DEBUG, "limpaProcessoOk", "Fim");
		}
	}
	 
	/**
	 * Metodo...: geraUsuariosShutdonwXML
	 * Descricao: Gera um XML de no maximo 300 msisdn em Sutdown
	 * @param 
	 * @return	String		- Retorna o xml com os msisdn's em Shutdown									
	 * @throws 												
	 */
	private String geraUsuariosShutdonwXML()
	{
		GerarXML geradorXML = new GerarXML("GPPStatus");
		
		super.log(Definicoes.DEBUG, "geraUsuariosShutdonwXML", "Inicio");

		// Criação das Tags Defaults do XML
		for(int j = 0 ; j < linhaMsisdn.size() ; j++ )
		{
			geradorXML.adicionaTag("msisdn",((DadosUsusariosShutdown)linhaMsisdn.get(j)).getMsisdn());
			geradorXML.adicionaTag("status",((DadosUsusariosShutdown)linhaMsisdn.get(j)).getStatus());
			geradorXML.adicionaTag("data",((DadosUsusariosShutdown)linhaMsisdn.get(j)).getData());
		}


		//Retorno do XML completo
		String xml = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML()); 

		super.log(Definicoes.DEBUG, "geraUsuariosShutdonwXML", "Fim");

		return xml; 
	}

	/**
	 * Metodo...: gravaUsuariosShutdown
	 * Descricao: Grava o xml de Saída para o Vitria 
	 * @param	conexaoPrep - Conexao com o Banco de Dados
	 * @param   xmlRetorno  - Xml com os msisdn's em Shutdown
	 * @return	boolean		- True se sucesso ou false em caso de falha									
	 * @throws 												
	 */
	
	public void gravaUsuariosShutdown (PREPConexao conexaoPrep,String xmlRetorno,String aData)
	{
		super.log(Definicoes.DEBUG, "gravaUsuariosShutdown", "Inicio");

		try
		{
			String ind_enviado_vitria = Definicoes.IDT_PROCESSAMENTO_NOT_OK;
			String idt_evento_negocio = Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT;
					
			// gravar os campos na tabela de saída
			String sql_grava = "INSERT INTO TBL_INT_PPP_OUT  " +
							  " (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
							  " XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) " +
							  " VALUES " +
							  " (SEQ_ID_PROCESSAMENTO.NEXTVAL, to_date(?,'dd/mm/yyyy'), ?, ?, ?)";
	
			Object paramInsert[] = {aData, idt_evento_negocio,new StringReader(xmlRetorno), ind_enviado_vitria};
								
			if (conexaoPrep.executaPreparedUpdate(sql_grava, paramInsert, super.logId) > 0)
			{
				super.log(Definicoes.DEBUG, "gravaUsuariosShutdown", "Envio Usuarios em Shutdown ok.");
			}
			else
			{
				super.log(Definicoes.WARN, "gravaUsuariosShutdown", "Erro: Envio Usuarios em Shutdown NAO ok.");
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gravaUsuariosShutdown", "Excecao Interna GPP:"+ e);
		}
		
		super.log(Definicoes.DEBUG, "gravaUsuariosShutdown", "Fim");
	 }
	
}


