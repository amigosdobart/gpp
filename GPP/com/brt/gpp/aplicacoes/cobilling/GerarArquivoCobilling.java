// Definicao do Pacote
package com.brt.gpp.aplicacoes.cobilling;

//Arquivos de Java
import java.sql.*;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.ManipuladorArquivos;
import com.brt.gpp.aplicacoes.*;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Exceções GPP
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;


/**
  *
  * Este arquivo refere-se a classe GerarArquivoCobilling, responsavel pela 
  * geração de um arquivo de batimento dos CDRs gerado para o Grupo de Cobilling
  *
  * <P> Versao		:	1.0
  *
  * @Autor				:	Marcelo Alves Araujo
  * <p>Data				:	04/05/2005
  * 
  * 
  * <p>Modificado por 	:
  * <p>Data 			:
  * <p>Razao 			:
  * 
  */

public class GerarArquivoCobilling extends Aplicacoes 
{
    GerentePoolBancoDados gerenteBanco;

	// Dados referentes ao Detalhe
	CobillingDetalhe detalhe = new CobillingDetalhe();
	
	// Variáveis Globais
	private String		csp;
	private String		UF;
	private String		inicioPeriodo;
	private String		finalPeriodo;
	
	// Contador do número de registros no campo detalhe
	private int 		contaRegistros;
	private int 		contaRegistrosLM;
	
	/**
	 * <p><b>Metodo...:</b> GerarArquivoCobilling
	 * <p><b>Descricao:</b> Construtor 
	 * @param aIdProcesso	- Identificador do processo
	 * @param csp			- CSP da operadora de longa distância
	 * @param dataInicio	- Data inicial a ser pesquisada
	 * @param dataFinal		- Data final a ser pesquisada
	 * @param UF			- UF a ser pesquisada, ou "TD" para pesquisar todas as UF cobertas pela BrT
	 * @return									
	 */
	public GerarArquivoCobilling(long aIdProcesso, String csp, String dataInicio, String dataFinal, String UF)
	{
		// Define parâmetros de Log
	    super(aIdProcesso, Definicoes.CL_GERAR_ARQUIVO_COBILLING);
		
		// Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);		
		
		// Inicializa as variáveis globais
		this.csp 				= csp;
		this.UF 				= UF;
		this.inicioPeriodo 		= dataInicio;
		this.finalPeriodo 		= dataFinal;
		this.contaRegistros		= 0;
		this.contaRegistrosLM 	= 0;
	}
	
	/**
	 * <p><b>Metodo...:</b> comporBatimentoGeral
	 * <p><b>Descricao:</b> Cria os arquivos com os dados do batimento com um registro por linha
	 * @param
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public void comporBatimentoGeral() throws GPPInternalErrorException
	{
	    // Data de início da execução de comporBatimentoGeral
	    String dataInicial 	= GPPData.dataCompletaForamtada();
	    String status 		= Definicoes.TIPO_OPER_SUCESSO;
    	
	    PREPConexao conexaoPrep = null;
	    
	    super.log(Definicoes.DEBUG, "comporBatimentoGeral", "Inicio. CSP: " + this.csp);
	    
	    try
	    {
	        // Testa se o CSP passado existe 
		    this.isCSP(this.csp);
	        		     
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    String sql = 	"SELECT "+
		    				"AREA.IDT_UF AS ESTADO, " + 
		        			"RPAD(SUBSTR(CDR.SUB_ID, 3),21,'-') AS ASSINANTE_A, "+
		    				"TO_CHAR(CDR.TIMESTAMP,'DDMMYYYY')  AS DATA_CHAMADA, "+
		    				"TO_CHAR(CDR.TIMESTAMP,'HH24MISS')  AS HORA_CHAMADA, "+
		    				"DECODE(SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2), "+ 
							"	    '05', '0'||RPAD(LD.NUM_CSP||LTRIM(LTRIM(RTRIM(CDR.CALL_ID,'*'),'0'), '55'),19,'-'), "+
							"	    '04', RPAD(LD.NUM_CSP||LTRIM(RTRIM(CDR.CALL_ID,'*'), '55'),20,'-'), "+
							"	    '03', RPAD(LD.NUM_CSP||LTRIM(RTRIM(CDR.CALL_ID,'*'), '55'),20,'-'), "+
							"	    RPAD('21'||RTRIM(LTRIM(LTRIM(CDR.CALL_ID,'0'), '55'), '*'),20,'-')) AS ASSINANTE_B, "+
							"'          ' AS PONTO_INTERCONEXAO, "+
		    				"TRIM(TO_CHAR(REPLACE(PROC_HORA_TECNOMEN(CDR.CALL_DURATION), ': '),'0999999')) AS DURACAO_REAL_CHAMADA, "+
		    				"TRIM(TO_CHAR(REPLACE(PROC_HORA_TECNOMEN(DECODE (SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2), "+ 
		    				"      	'05', 60+GREATEST((CEIL((CDR.CALL_DURATION - 60)/6)*6),0), "+
		    				"             30+GREATEST((CEIL((CDR.CALL_DURATION - 30)/6)*6),0))), ': '),'0999999')) AS DURACAO_TARIFADA_CHAMADA, "+
		    				"SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2) AS TIPO_SERVICO, "+
		    				"LPAD(ROUND((NVL(CDR.AIRTIME_COST,0)+NVL(CDR.INTERCONNECTION_COST,0))/1000),11,'0') AS VALOR_LIQUIDO, "+
    						"LPAD(ROUND((NVL(CDR.AIRTIME_COST,0)+NVL(CDR.INTERCONNECTION_COST,0)+NVL(CDR.TAX,0))/1000),11,'0') AS VALOR_BRUTO, "+
    						"'                              ' AS RESERVA, " +
    						"CDR.PROFILE_ID AS PLANO "+
		    				"FROM TBL_GER_CDR CDR, TBL_GER_CODIGO_NACIONAL AREA, TBL_GER_OPERADORAS_LD LD "+
		    				"WHERE "+
		    				"(CDR.TIP_CHAMADA LIKE 'E0300%' OR CDR.TIP_CHAMADA LIKE 'VC2%' "+
		    				"OR CDR.TIP_CHAMADA LIKE 'VC3%' OR CDR.TIP_CHAMADA LIKE 'INTERNAC%') "+
		    				"AND (AREA.IDT_CODIGO_NACIONAL = SUBSTR(CDR.SUB_ID,3,2)) "+
		    				"AND (CDR.NUM_CSP = LD.NUM_CSP OR CDR.TIP_CHAMADA = LD.RATE_NAME_0300) "+
		    				"AND LD.NUM_CSP = ? "+
		    				"AND CDR.TIMESTAMP BETWEEN to_date(?,'dd/mm/yyyy hh24:mi:ss') AND to_date(?,'dd/mm/yyyy hh24:mi:ss') "+
							"ORDER BY AREA.IDT_UF,AREA.IDT_CODIGO_NACIONAL";
		    
		    // Parâmetros para a consulta
		    Object parametros[] = {this.csp,this.inicioPeriodo,this.finalPeriodo};
		    // Execução da query
		    rs = conexaoPrep.executaPreparedQuery1(sql, parametros, super.getIdLog());
		    
		    String 	sequencia 	= null;
		    String	estado 		= null;
					    
		    // Arquivo em que será armazenado o batimento
	    	ManipuladorArquivos arquivoBatimento = null;
	    	// Arquivo em que será armazenado o batimento de assinates LigMix
	    	ManipuladorArquivos arquivoBatimentoLM = null;
	    	
	    	// Mapeamento da tabela configuração GPP
	    	MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
		    		    		    
		    while(rs.next())
		    {
		        estado = rs.getString("ESTADO");
		        
		        // Se o estado for o primeiro 
		        if(this.UF.equals(Definicoes.TODAS_UF))
		        {
		            // Atualização do estado
		            this.UF	= estado;
		            
		            // Abertura do novo arquivo em que será escrito o batimento no diretório
				    arquivoBatimento = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),false);
				    arquivoBatimentoLM = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),true);
				    
				    
			        // Construir cabeçalhos
			        this.gerarCabecalho(arquivoBatimento);
			        this.gerarCabecalho(arquivoBatimentoLM);
		        }
		        // Ao mudar o estado
		        else if(!estado.equals(this.UF))
		        {
		            // Constroi o Trailler do arquivo que será fechado
		            this.gerarTrailler(arquivoBatimento,false);
		            this.gerarTrailler(arquivoBatimentoLM,true);
		            
		            // Atualização do estado
		            this.UF		 		= estado;
		            // Reinício do contador de registros
		            this.contaRegistros = 0;
		            this.contaRegistrosLM = 0;		          
		            
		            // Fecha o arquivo antigo aberto
		            arquivoBatimento.fechaArquivo();
		            arquivoBatimentoLM.fechaArquivo();
			        		               
				    // Abertura do novo arquivo em que será escrito o batimento no diretório
				    arquivoBatimento = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),false);
				    arquivoBatimentoLM = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),true);
				    
			        // Construir cabeçalhos
			        this.gerarCabecalho(arquivoBatimento);	
			        this.gerarCabecalho(arquivoBatimentoLM);	
		        }
		        
		        // Preenche os campos de detalhe
		        detalhe.setAssinanteA(rs.getString("ASSINANTE_A"));
		        detalhe.setDataChamada(rs.getString("DATA_CHAMADA"));
		        detalhe.setHoraChamada(rs.getString("HORA_CHAMADA"));
		        detalhe.setAssinanteB(rs.getString("ASSINANTE_B"));
		        detalhe.setPontoInterconexao(rs.getString("PONTO_INTERCONEXAO"));
				detalhe.setDuracaoRealChamada(rs.getString("DURACAO_REAL_CHAMADA"));
				detalhe.setDuracaoTarifadaChamada(rs.getString("DURACAO_TARIFADA_CHAMADA"));
				detalhe.setTipoServico(rs.getString("TIPO_SERVICO"));
				detalhe.setValorLiquidoChamada(rs.getString("VALOR_LIQUIDO"));
				detalhe.setValorBrutoChamada(rs.getString("VALOR_BRUTO"));
				detalhe.setReserva(rs.getString("RESERVA"));
				
				//Criando classe de aplicacao
				Aprovisionar aprovisionar = new Aprovisionar(this.getIdLog());
				if(aprovisionar.ehLigMix(rs.getInt("PLANO")))
				{
					// Incrementa a contagem do número de registros
					this.contaRegistrosLM++;
					// Converte o número para o formato especificado
			        sequencia = this.alinhaString(""+this.contaRegistrosLM,false,'0',10);
			        
			        // Coloca a linha de detalhes no arquivo
		        	arquivoBatimentoLM.escreveLinha(Definicoes.TIPO_REGISTRO_DETALHE+sequencia+detalhe.getDetalhe());
		        }
		        else
		        {
					// Incrementa a contagem do número de registros
					this.contaRegistros++;
					// Converte o número para o formato especificado
			        sequencia = this.alinhaString(""+this.contaRegistros,false,'0',10);
			        
		        	// Coloca a linha de detalhes no arquivo
		        	arquivoBatimento.escreveLinha(Definicoes.TIPO_REGISTRO_DETALHE+sequencia+detalhe.getDetalhe());
		        }		        
			}
		    
		    if( arquivoBatimento != null && arquivoBatimentoLM != null )
		    {
			    // Constroi o Trailler do arquivo que será fechado
	            this.gerarTrailler(arquivoBatimento,false);
	            this.gerarTrailler(arquivoBatimentoLM,true);
	
	            // Fecha o arquivo antigo aberto
	            arquivoBatimento.fechaArquivo();
	            arquivoBatimentoLM.fechaArquivo();
		    }
		    rs.close();
		    rs = null;
		    
	    }
	    catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "comporBatimentoGeral", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
	    catch (GPPInternalErrorException e)
	    {
	        status = Definicoes.TIPO_OPER_ERRO;
	        
	        super.log(Definicoes.ERRO, "comporBatimentoGeral", "Erro GPP: " + e);
	        throw new GPPInternalErrorException("Erro: " + e);
	    }
	    finally
	    {
	        String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Arquivos de Cobilling de Todos os Estados Gerados.";
			
			// Chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_ARQUIVO_COBILLING, dataInicial, dataFinal, status, descricao, dataInicial);
			
			super.log(Definicoes.DEBUG, "comporBatimentoGeral", "Fim");
	    }	    
	}
	
	/**
	 * <p><b>Metodo...:</b> comporBatimento
	 * <p><b>Descricao:</b> Cria o arquivo com os dados do batimento com um registro por linha
	 * @param
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public void comporBatimento() throws GPPInternalErrorException
	{
	    // Arquivo em que será armazenado o batimento
    	ManipuladorArquivos arquivoBatimento = null;
    	ManipuladorArquivos arquivoBatimentoLM = null;

    	// Data de início do processo
    	String dataInicial = GPPData.dataCompletaForamtada();
    	String status = Definicoes.TIPO_OPER_SUCESSO;
	    
    	try
	    {	
	        // Testa se a UF é coberta pela Brasil Telecom e o CSP passados existem ou não 
	        isUF(this.UF);
	        isCSP(this.csp);
	        
	        // Mapeamento da tabela configuração GPP
	        MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
	        
	        // Abertura do arquivo em que será escrito o batimento no diretório
	    	arquivoBatimento = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),false);
	    	arquivoBatimentoLM = this.abrirArquivo(map.getMapValorConfiguracaoGPP("DIRETORIO_ARQUIVO_COBILLING"),true);
	    	
	        super.log(Definicoes.DEBUG, "comporBatimento", "Inicio. CSP: " + this.csp + ". Periodo: " + this.inicioPeriodo + "-" + this.finalPeriodo);
	        
	        // Construir cabeçalho
	        this.gerarCabecalho(arquivoBatimento);
	        this.gerarCabecalho(arquivoBatimentoLM);
	        
	        // Construir detalhes
	        this.gerarDetalhe(arquivoBatimento,arquivoBatimentoLM);
	        
	        // Construir trailler
	        this.gerarTrailler(arquivoBatimento,false);
	        this.gerarTrailler(arquivoBatimentoLM,true);	        
	    }
	    catch (GPPInternalErrorException e)
	    {
	        status = Definicoes.TIPO_OPER_ERRO;
	        
	        super.log(Definicoes.ERRO, "comporBatimento", "Erro GPP: " + e);
	        throw new GPPInternalErrorException("Erro: " + e);		       
	    }
	    finally
	    {
	        arquivoBatimento.fechaArquivo();
	        arquivoBatimentoLM.fechaArquivo();
	        
	        String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Arquivo de Cobilling: " + this.UF + "Gerado.";
			
			// Chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_ARQUIVO_COBILLING, dataInicial, dataFinal, status, descricao, dataInicial);
	        
	        super.log(Definicoes.DEBUG, "comporBatimento", "Fim");
	    }
	}

    /**
	 * <p><b>Metodo...:</b> gerarCabecalho
	 * <p><b>Descricao:</b> Gera a linha correspondente ao cabeçalho do arquivo de batimento
	 * @param arquivoBatimento	- Arquivo em que será escrito o cabeçalho
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private void gerarCabecalho(ManipuladorArquivos arquivoBatimento) throws GPPInternalErrorException
	{	     
	    PREPConexao conexaoPrep = null;
	    
	    // Prestadora dona do terminal
	    final String prestadora = "Brasil Telecom      ";
	    	 
		super.log(Definicoes.DEBUG, "gerarCabecalho", "Inicio CSP: " + this.csp + "UF: " + this.UF);
		
		try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para pegar o nome da operadora de acordo com o CSP
		    String sqlCabecalho = 	"SELECT  RPAD(NOM_OPERADORA,20) AS OPERADORA "+
		        					"FROM    TBL_GER_OPERADORAS_LD "+
		        					"WHERE   NUM_CSP = ?";
		    
		    // Prestadora de Longa Distância
		    String prestadoraLD = null;
		    
		    Object parametro[]  = {this.csp};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlCabecalho, parametro, super.getIdLog());
		    
		    if(rs.next())
		        prestadoraLD = rs.getString("OPERADORA");
		    
		    rs.close();
		    rs = null;
		    
		    // Formata a data 
		    String dataInicio = this.inicioPeriodo.toString().substring(0,2)+ this.inicioPeriodo.toString().substring(3,5)+ this.inicioPeriodo.toString().substring(6);
	        String dataFim	  = this.finalPeriodo.toString().substring(0,2) + this.finalPeriodo.toString().substring(3,5) + this.finalPeriodo.toString().substring(6);
	        
	        // Escreve a linha de cabeçalho no arquivo
		    arquivoBatimento.escreveLinha(Definicoes.TIPO_REGISTRO_CABECALHO+prestadora+this.UF+prestadoraLD+dataInicio+dataFim);
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "gerarCabecalho", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "gerarCabecalho", "Erro GPP: " + e);
		}
		finally
		{
		    // Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "gerarCabecalho", "Fim");
		}
	}
	
	/**
	 * <p><b>Metodo...:</b> gerarDetalhe
	 * <p><b>Descricao:</b> Gera a linha correspondente ao detalhe do arquivo de batimento
	 * @param arquivoBatimento		- Arquivo em que será escrito os detalhes dos assinantes GSM
	 * @param arquivoBatimentoLM	- Arquivo em que será escrito os detalhes dos assinantes LigMix
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private void gerarDetalhe(ManipuladorArquivos arquivoBatimento, ManipuladorArquivos arquivoBatimentoLM) throws GPPInternalErrorException
	{    
		PREPConexao conexaoPrep = null;
		
		super.log(Definicoes.DEBUG, "gerarDetalhe", "Inicio. CSP: " + this.csp + ". UF: " + this.UF);
		
		try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    String sqlDetalhe = 	"SELECT /*+ index(cdr XIE1TBL_GER_CDR)*/"+
									"RPAD(SUBSTR(CDR.SUB_ID, 3),21,'-') AS ASSINANTE_A, "+
									"TO_CHAR(CDR.TIMESTAMP,'DDMMYYYY')  AS DATA_CHAMADA, "+
									"TO_CHAR(CDR.TIMESTAMP,'HH24MISS')  AS HORA_CHAMADA, "+
									"DECODE(SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2), "+
									"'05', '0'||RPAD('21'||LTRIM(LTRIM(CDR.CALL_ID,'0'), '55'),19,'-'), "+
									"'04', RPAD('21'||LTRIM(CDR.CALL_ID, '55'),20,'-'), "+
									"'03', RPAD('21'||LTRIM(CDR.CALL_ID, '55'),20,'-'), "+
									"RPAD('21'||RTRIM(LTRIM(LTRIM(CDR.CALL_ID,'0'), '55'), '*'),20,'-')) AS ASSINANTE_B, "+
									"'          ' AS PONTO_INTERCONEXAO, "+
									"TRIM(TO_CHAR(REPLACE(PROC_HORA_TECNOMEN(CDR.CALL_DURATION), ': '),'0999999')) AS DURACAO_REAL_CHAMADA, "+
									"TRIM(TO_CHAR(REPLACE(PROC_HORA_TECNOMEN(DECODE (SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2), "+
									"'05', 60+GREATEST((CEIL((CDR.CALL_DURATION - 60)/6)*6),0), "+
									"30+GREATEST((CEIL((CDR.CALL_DURATION - 30)/6)*6),0))), ': '),'0999999')) AS DURACAO_TARIFADA_CHAMADA, "+
									"SUBSTR(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(CDR.TIP_CHAMADA,'VC2','01'),'VC3','02'),'E0300','03'),'E0500','04'),'INTERNAC','05'),1,2) AS TIPO_SERVICO, "+
									"LPAD(ROUND((NVL(CDR.AIRTIME_COST,0)+NVL(CDR.INTERCONNECTION_COST,0))/1000),11,'0') AS VALOR_LIQUIDO,  "+
									"LPAD(ROUND((NVL(CDR.AIRTIME_COST,0)+NVL(CDR.INTERCONNECTION_COST,0)+NVL(CDR.TAX,0))/1000),11,'0') AS VALOR_BRUTO,  "+
									"'                    'AS RESERVA," +
									"CDR.PROFILE_ID AS PLANO "+
									"FROM TBL_GER_CDR CDR, TBL_GER_CODIGO_NACIONAL AREA, TBL_GER_OPERADORAS_LD LD "+
				    				"WHERE "+
									"(CDR.TIP_CHAMADA LIKE 'E0300%' OR CDR.TIP_CHAMADA LIKE 'VC2%' "+
				    				"OR CDR.TIP_CHAMADA LIKE 'VC3%' OR CDR.TIP_CHAMADA LIKE 'INTERNAC%') "+
				    				"AND (AREA.IDT_CODIGO_NACIONAL = SUBSTR(CDR.SUB_ID,3,2)) "+
				    				"AND (CDR.NUM_CSP = LD.NUM_CSP OR CDR.TIP_CHAMADA = LD.RATE_NAME_0300) "+
				    				"AND AREA.IDT_UF = ? "+
									"AND LD.NUM_CSP = ? "+
				    				"AND CDR.TIMESTAMP BETWEEN to_date(?,'dd/mm/yyyy') AND to_date(?,'dd/mm/yyyy') " +
				    				"ORDER BY AREA.IDT_UF,AREA.IDT_CODIGO_NACIONAL";
		        					
		    
		    // Parâmetros da consulta
		    Object parametros[] = {this.UF,this.csp,this.inicioPeriodo,this.finalPeriodo};
		    // Executa a query
		    rs = conexaoPrep.executaPreparedQuery(sqlDetalhe, parametros, super.getIdLog());
		    		    		    
		    String 	sequencia = null;
		    
		    while(rs.next())
		    {
		        detalhe.setAssinanteA(rs.getString("ASSINANTE_A"));
		        detalhe.setDataChamada(rs.getString("DATA_CHAMADA"));
		        detalhe.setHoraChamada(rs.getString("HORA_CHAMADA"));
		        detalhe.setAssinanteB(rs.getString("ASSINANTE_B"));
		        detalhe.setPontoInterconexao(rs.getString("PONTO_INTERCONEXAO"));
				detalhe.setDuracaoRealChamada(rs.getString("DURACAO_REAL_CHAMADA"));
				detalhe.setDuracaoTarifadaChamada(rs.getString("DURACAO_TARIFADA_CHAMADA"));
				detalhe.setTipoServico(rs.getString("TIPO_SERVICO"));
				detalhe.setValorLiquidoChamada(rs.getString("VALOR_LIQUIDO"));
				detalhe.setValorBrutoChamada(rs.getString("VALOR_BRUTO"));
				detalhe.setReserva(rs.getString("RESERVA"));
				
				//Criando classe de aplicacao
				Aprovisionar aprovisionar = new Aprovisionar(this.getIdLog());
				// Escreve a linha de detalhes no arquivo
				if(aprovisionar.ehLigMix(rs.getInt("PLANO")))
				{
					// Incrementa o contador de registros
					this.contaRegistrosLM++;
					sequencia = this.alinhaString(""+this.contaRegistrosLM,false,'0',10);
					
					arquivoBatimentoLM.escreveLinha(Definicoes.TIPO_REGISTRO_DETALHE+sequencia+detalhe.getDetalhe());
				}
				else
				{
					// Incrementa o contador de registros
					this.contaRegistros++;
					sequencia = this.alinhaString(""+this.contaRegistros,false,'0',10);
					
					arquivoBatimento.escreveLinha(Definicoes.TIPO_REGISTRO_DETALHE+sequencia+detalhe.getDetalhe());
				}
		    }
		    
		    rs.close();
		    rs = null;
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "gerarDetalhe", "Erro(SQL): " + e);
		    throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "gerarDetalhe", "Erro GPP: " + e);
		}
		finally
		{
		    // Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "gerarDetalhe", "Fim");
		}
	}


	/**
	 * <p><b>Metodo...:</b> gerarTrailler
	 * <p><b>Descricao:</b> Compõe o final do arquivo de batimento, adicionando apenas o número de registros em detalhe
	 * @param arquivoBatimento	- Arquivo em que será escrito o trailler
	 * @param ligMix			- Identifica se o plano do assinante é LigMix(true) ou GSM(false)
	 * @return
	 */
	private void gerarTrailler(ManipuladorArquivos arquivoBatimento, boolean ligMix)
	{
	    String numeroRegistros = null;
	    
	    if(ligMix)
	    	numeroRegistros = ""+this.contaRegistrosLM;
	    else
	    	numeroRegistros = ""+this.contaRegistros;
	    
	    super.log(Definicoes.DEBUG, "gerarTrailler", "Inicio CSP: " + this.csp + "UF: " + this.UF);
	    
	    numeroRegistros = this.alinhaString(numeroRegistros,false,'0',10);
	    	    
	    // Escreve o tipo de registro e o número de registros em Detalhe
	    arquivoBatimento.escreveLinha(Definicoes.TIPO_REGISTRO_TRAILLER+ numeroRegistros);			
	    
	    super.log(Definicoes.DEBUG, "gerarTrailler", "Fim");
	}    

	
	/**
	 * <p><b>Metodo...:</b> abrirArquivo</p>
	 * <p><b>Descricao:</b> Abre o arquivo para escrita dos campos Cabeçalho, Detalhe e Trailler</p>
	 * @param diretorio			- Diretório em que será gravado o arquivo 
	 * @param ligMix			- Identifica se o plano do assinante é LigMix(true) ou GSM(false)
	 * @return arquivoBatimento	- Arquivo em que será escrito o batimento
	 * @throws GPPInternalErrorException
	 */
	private ManipuladorArquivos abrirArquivo(String diretorio, boolean ligMix) throws GPPInternalErrorException
	{	 
	    // Objeto do arquivo gerado
	    ManipuladorArquivos arquivoBatimento = null;
	    
	    super.log(Definicoes.DEBUG, "abrirArquivo", "Criacao do arquivo...");
	    
	    try
	    {
	        String nomeArquivo = null;
	        
		    // Compõe o nome completo do arquivo
		    nomeArquivo = diretorio + this.gerarNome(ligMix);
		    
		    //Abre o arquivo para escrita
		    arquivoBatimento = new ManipuladorArquivos(nomeArquivo, true, super.getIdLog());
	    }
	    catch( GPPInternalErrorException e)
	    {
	        super.log(Definicoes.ERRO, "abrirArquivo", "Erro GPP: " + e);
	        throw new GPPInternalErrorException("Erro: " + e);
		}
	    return arquivoBatimento;
	}
	
	/**
	 * <p><b>Metodo...:</b> gerarNome</p>
	 * <p><b>Descricao:</b> Cria o nome dos arquivos no formato:</p>
	 ***************************************************************************************** 		
	 * 		<p>TCOB.Txxyyy.Dddmmaa.Hhhmmss</p>
	 * 		<p>	- xx : Código EOT da Prestadora de Cobilling (Estado de Origem da Chamada)</p>
	 * 		<p>	- yyy: Código EOT da Prestadora de Longa Distância (0+Código da Operadora)</p>
	 * 		<p>	- dd : dia de geração do arquivo</p>
	 * 		<p>	- mm : mês de geração do arquivo</p>
	 * 		<p>	- aa : ano de geração do arquivo</p>
	 * 		<p>	- hh : hora de geração do arquivo</p>
	 * 		<p>	- mm : minuto de geração do arquivo</p>
	 * 		<p>	- ss : segundo de geração do arquivo</p>
	 * ***************************************************************************************
	 * @param ligMix	- Identifica se o plano do assinante é LigMix(true) ou GSM(false)
	 * @return
	 */
	public String gerarNome(boolean ligMix)
	{
	    String		eotCobilling	= null;
	    String	 	dia 			= null;
	    String		mes 			= null;
	    String		ano 			= null;
	    String		hh 				= null;
	    String		mm 				= null;
	    String		ss 				= null;
	    
	    super.log(Definicoes.DEBUG, "gerarNome", "Criacao do nome do arquivo");
	    // Pega a UF
	    eotCobilling = this.UF;
	    // Pega da data formatada
	    dia = GPPData.dataFormatada().substring(0,2);
	    mes = GPPData.dataFormatada().substring(3,5);
	    ano = GPPData.dataFormatada().substring(6,10);
	    // Pega o tempo formatado
	    hh  = GPPData.horaFormatada().substring(0,2);
	    mm  = GPPData.horaFormatada().substring(3,5);
	    ss  = GPPData.horaFormatada().substring(6,8);
	    
	    
	    // Compõe o nome do arquivo
	    if(ligMix)
	    	return ("TCOB.T"+eotCobilling+"0"+this.csp+".D"+dia+mes+ano+".H"+hh+mm+ss+".LIGMIX");
	    return ("TCOB.T"+eotCobilling+"0"+this.csp+".D"+dia+mes+ano+".H"+hh+mm+ss+".GSM");
	}
	
	/**
	 * <p><b>Metodo...:</b> isUF</p>
	 * <p><b>Descricao:</b> Testa se um estado é válido e é coberto pela Brasil Telecom</p>
	 * @param  UF				- UF a ser pesquisada 
	 * @return boolena			- Retorna true se a UF existir
	 * @throws GPPInternalErrorException
	 */
	public boolean isUF( String UF ) throws GPPInternalErrorException
	{
	    PREPConexao conexaoPrep = null;
	    boolean estadoValido = false;
	    
	    super.log(Definicoes.DEBUG, "isUF", "Validando UF");
		
		try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para testar se o estado é coberto pela BrT
		    String sqlCabecalho = 	"SELECT  distinct 1  "+
		        					"FROM    TBL_GER_CODIGO_NACIONAL "+
		        					"WHERE IDT_UF = ? "+
		        					"AND IND_REGIAO_BRT = 1";
		    
		    // Parâmetro da consulta
		    Object parametro[]  = {this.UF};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlCabecalho, parametro, super.getIdLog());
		    
		    // Se o estado for coberto pela BrT estado recebe true
		    if(rs.next())
		        estadoValido = rs.getBoolean(1);
		    
		    rs.close();
		    rs = null;
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "isUF", "Erro(SQL): " + e);
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "isUF", "Erro GPP: " + e);
		}
		finally
		{
		    // Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "isUF", "Fim");	
			
			// Gera uma exceção caso o estado não exista
			if(!estadoValido)
			    throw new GPPInternalErrorException ("Excecao Interna do GPP: " + "A UF " + this.UF + " nao existe ou nao e coberta pela BrT");
		}	    
		return estadoValido;
	}
	
	/**
	 * <p><b>Metodo...:</b> isCSP</p>
	 * <p><b>Descricao:</b> Testa se um CSP é válido</p>
	 * @param  csp				- CSP a ser pesquisado 
	 * @return boolena			- Retorna true se o CSP existir
	 * @throws GPPInternalErrorException
	 */
	public boolean isCSP( String csp ) throws GPPInternalErrorException
	{
	    PREPConexao conexaoPrep = null;
	    boolean cspValido = false;
	    
	    super.log(Definicoes.DEBUG, "isCSP", "Validando CSP");
		
		try
		{
		    // Seleciona conexão do pool Prep Conexão
		    conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    
		    ResultSet rs;
		    
		    // Query para pvalidar o CSP
		    String sqlCabecalho = 	"SELECT  1 "+
		        					"FROM    TBL_GER_OPERADORAS_LD "+
		        					"WHERE 	 NUM_CSP = ? ";
		    
		    // Parâmetro da consulta
		    Object parametro[]  = {this.csp};
		    
		    rs = conexaoPrep.executaPreparedQuery(sqlCabecalho, parametro, super.getIdLog());
		    
		    // Se o CSP for válido cspValido = true
		    if(rs.next())
		        cspValido = rs.getBoolean(1);
		    
		    rs.close();
		    rs = null;
		}
		catch (SQLException e)
		{
		    super.log(Definicoes.ERRO, "isCSP", "Erro(SQL): " + e);
		}
		catch (GPPInternalErrorException e)
		{
		    super.log(Definicoes.ERRO, "isCSP", "Erro GPP: " + e);
		}
		finally
		{
		    // Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "isCSP", "Fim");
			
			// Caso o CSP não seja válido, gera uma exceção
			if(!cspValido)
			    throw new GPPInternalErrorException ("Excecao Interna do GPP: " + "O CSP " + this.csp + " nao existe");
		}
	    return cspValido;
	}
	
	/**
	 * <p><b>Metodo...:</b> alinhaString</p>
	 * <p><b>Descricao:</b> Alinha uma String de acordo com os parâmetros</p>
	 * @param  original					- String original
	 * @param  alinhamento			<p>	- true : Alinhamento à esquerda
	 * 								<p>	- false: Alinhamento à direita
	 * @param  caracterPreenchimento	- Caracter com que será preenchida a String resultante
	 * @param  tamanhoString			- Número de caracteres da String resultante
	 * @return resultante				- Resultado do alinhamento
	 * @throws GPPInternalErrorException
	 */
	public String alinhaString(String original, boolean alinhamento, char caracterPreenchimento, int tamanhoString)
	{
	    String resultante = original;
	    // Se alinhamento for true, alinha a string à esquerda
	    if(alinhamento)
	        while(resultante.length() < tamanhoString)
	            resultante = resultante + caracterPreenchimento;
	    // Se alinhamento for false, alinha a string à direita
	    else
	        while(resultante.length() < tamanhoString)
	            resultante = caracterPreenchimento + resultante;
	    return resultante;
	}	
}