//Definicao do Pacote
package com.brt.gpp.comum.conexoes.masc;

//Arquivos de Imports do GPP
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.ConexaoBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.recarregar.*;


// Arquivos de Imports de Java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Este arquivo inclui a definicao da classe de conexao com a plataforma MASC.
 * Contem a definicao dos metodos de conexao, autenticacao e troca de senha. 
 *
 * <P> Versao:			1.0
 * 
 * @Autor: 			Lawrence Josuá Fernandes Costa
 * Date: 				15/02/2005
 *                       
 * Modificado Por:
 * Data:
 * Razao:
 */

public class ConexaoMASC 
{
	GerentePoolBancoDados gerenteBanco = null;
	
	// Atributos da classe
	private long idProcesso;
	private GerentePoolLog 		Log 			= null; // Gerente de LOG

	// Dados de conexao
	private String 	ipServidor = null;
	private String 	portaServico = null;
	private String 	nomeUsuario = null;
	private String 	senhaUsuario = null;
	private int		numTentativasReadln = 0;
	private int		tempoTimeoutReadln = 0;


	// Atributos de conexao		
	private Socket sk = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	
	/**
	 * Metodo...: ConexaoMASC
	 * Descricao: Construtor 
	 * @param	aIdProcesso	- Numero indicador do processo 
	 * @return	
	 * @throws 	GPPInternalErrorException								
	 */
	public ConexaoMASC ( long aIdProcesso ) throws GPPInternalErrorException											
	{
		// Obtem instancias de servantes
		ArquivoConfiguracaoGPP arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);
		
		// Armazena o ID do log 
		this.idProcesso = aIdProcesso;
		this.Log = GerentePoolLog.getInstancia(this.getClass());
		
		// Obtem dados da plataforma de SMS no arquivo de configuracao do GPP
		this.ipServidor 			= arqConfiguracao.getEnderecoMaquinaMASC();
		this.portaServico 			= arqConfiguracao.getPortaMaquinaMASC();
		this.nomeUsuario 			= arqConfiguracao.getNomeUsuarioMASC();
		this.senhaUsuario 			= arqConfiguracao.getSenhaUsuarioMASC();
		this.numTentativasReadln	= arqConfiguracao.getNumTentativasReadln();
		this.tempoTimeoutReadln		= arqConfiguracao.getTempoTimeoutReadln();
	}    
	  
   /**
	* Metodo...: enviaRecargaMASC
	* Descricao: Envia a Recarga a ser realizada pelo MASC 
	* @param 	aNumeroAssinante	- Numero do assinante que deve receber a Recarga
	* @param 	aCredito			- Valor a ser creditado
	* @param	aNUmDias			- Data de expiração dos créditos
	* @return	boolean 			- TRUE  e FALSE  
	* @throws 	GPPInternalErrorException
	*/
	public DadosRecarga enviaRecargaMASC(String aNumeroAssinante, double aCredito, int aNumDias, String idRecarga) throws GPPInternalErrorException
	{	
		Log.log (this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Inicio MSISDN "+aNumeroAssinante);

		short retorno = Definicoes.RET_ERRO_GENERICO_MASC;
		DecimalFormat numFormatado = new DecimalFormat("##,#00.00", new DecimalFormatSymbols(Locale.FRENCH));
		String linha = null;
		double max_cred = 0d;
		double cur_cred = 0d;
//		double recarga;
		DadosRecarga retornoRecarga = new DadosRecarga();

		ConexaoBancoDados DBConexao = null;
	
		try
		{
			// Abre conexao MASC
			sk = new Socket(this.ipServidor, Integer.parseInt(this.portaServico));
			sk.setSoTimeout(this.tempoTimeoutReadln * 1000);
			out = new PrintWriter(sk.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			
			// Checa se recebeu a linha de confirmação de conexão: token "200"
			linha = recebeString();
			if(linha.startsWith("200"))
			{
				// Envia o pedido de login, primeiro o user
				enviaString("user " + this.nomeUsuario);
				linha = recebeString();
				if(linha.startsWith("300"))
				{
					// Envia o pedido de login, a senha
					enviaString("pass " + this.senhaUsuario);	
					linha = recebeString();
					// Checa se conseguiu logar: token "202"
					if (linha.startsWith("202"))
					{
	
						Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Usuário " + this.nomeUsuario +" logado"); 
						
						// numero sequencial de consulta
						DBConexao = gerenteBanco.getConexaoPREP(this.idProcesso);
						retornoRecarga.setIdentificacaoRecarga(idRecarga);
						
						// envia o pedido de serviço de consulta de assinante
						//String servico = "SEND " + this.nomeUsuario + " " + GPPData.formataNumero(num,10) + " " + aNumeroAssinante.substring(2, 4) + " " + aNumeroAssinante.substring(4, aNumeroAssinante.length()) + " " + "IOJ" + " " + numFormatado.format(aCredito) + " " + Integer.toString(aNumDias); 
						String servico = "SEND " + this.nomeUsuario + " " + GPPData.formataNumero(idRecarga,10) + " " + aNumeroAssinante.substring(2, 4) + " " + aNumeroAssinante.substring(4, aNumeroAssinante.length()) + " " + "IOJ" + " " + numFormatado.format(aCredito) + " " + Integer.toString(aNumDias);
						Log.log (this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Comando MASC: "+servico);
						enviaString(servico);

						linha = recebeString();
						// Confirma a inclusão do serviço
						if (linha.startsWith("203"))
						{
							Log.log (this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Servico de Recarga Adicionado");
							linha = recebeString();
							java.util.StringTokenizer st = new java.util.StringTokenizer( linha, " ", false );
	
							// Confirma a realização do serviço "211"
							if (st.nextToken().equals("211"))
							{
//								int numlinhas = 
								Integer.parseInt(st.nextToken());
								linha = recebeString();
								st = new java.util.StringTokenizer( linha, " ", false );
								
								// Retorna o último token da linha
								for (int c = 0; c<4; c++)
								{
									st.nextToken();
								}
								int codretorno = Integer.parseInt(st.nextToken());
								
								// Assinante inexistente
								switch (codretorno)
								{
									case Definicoes.RET_OPERACAO_OK:
									{
									retorno = Definicoes.RET_OPERACAO_OK;
									Log.log (this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Assinante " + aNumeroAssinante + " teve recarga efetuada ");
									break;
									}
									case -15:
									{
										retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Assinante " + aNumeroAssinante + " Inexistente");
										break;
									}									
									case -1:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: Problemas de comunicação interna");
										break;
									}
									case -2:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: Demora exagerada na resposta");
										break;
									}
									case -4:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: Resposta da central problemática");
										break;
									}
									case -8:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: Valor de Crédito Inválido");
										break;
									}
									case -9:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: O processador da central estava ocupado na hora do serviço");
										break;
									}
									case -22:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "MASC: Central em falha");
										break;
									}
									case -99:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro Plataforma RI");
										break;
									}
									default:
									{
										retorno = Definicoes.RET_ERRO_GENERICO_MASC;
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro generico");
										break;
									}
								}
								
								StringBuffer sbuffer = new StringBuffer();
								// Usa o 1o pattern pra retirar o 210 e concatenar as linhas
								Pattern pattern = Pattern.compile("^210 (.*)$");
								while((linha = recebeString()) != null)
								{
								    Matcher matcher = pattern.matcher(linha);
							        if(matcher.matches())
							        {
							            sbuffer.append(matcher.group(1));
							        }
								}
								Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Recarga Terminada");
						        // Usa o 2o pattern pra popular a Hashtable
						        Hashtable htable = new Hashtable();
						        // Retorna todos os tokens alternados entre NOME_CAMPO (group(1)) e CONTEUDO_CAMPO (group(2))
						        // definidos nos []
						        pattern = Pattern.compile("([\\w|\\s|:]*)=([\\w|\\s|:|\\/]*),");
						        Matcher matcher = pattern.matcher(sbuffer);
						        
						        while(matcher.find())
						        {
						            String chave = matcher.group(1).trim();
						            String valor = matcher.group(2).trim();
						            htable.put(chave, valor);         
						        }
							    max_cred = Double.parseDouble((String)htable.get("MAX_CRED"));
							    cur_cred = Double.parseDouble((String)htable.get("CURCRED"));
							}
						}
						else // Problemas de conexão
						{
							retorno = Definicoes.RET_ERRO_GENERICO_MASC;
							if (linha.startsWith("529"))
							{
								Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Não existe conexão OK disponível com MASC");
							}
						}
					}
					else //Senha incorreta
					{
						retorno = Definicoes.RET_ERRO_GENERICO_MASC;
						Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Login não realizado. Senha Incorreta");
					}
				}
				else //User não cadastrado
				{
					retorno = Definicoes.RET_ERRO_GENERICO_MASC;
					Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Login não realizado. User não cadastrado");
				}
				
			}
		    // Se nao encontrou o token "200", aconteceu um erro na conexão com o MASC
			else
			{
					retorno = Definicoes.RET_ERRO_GENERICO_MASC;
					Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "ERRO na conexão com o MASC");
			}
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "ERRO ao abrir socket com o MASC: " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (NumberFormatException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "ERRO ao abrir socket: NumberFormatException - " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (IOException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "ERRO ao abrir socket: IOException - " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (NullPointerException npE)
		{
			retorno = Definicoes.RET_ERRO_TIMEOUT_MASC;
			Log.log(this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC,"enviaRecargaMASC", "ERRO ao receber dados do MASC - " + npE);
			//retornoRecarga.setCodigoErro(retorno);
			return retornoRecarga;
			//throw new GPPInternalErrorException("Erro interno do GPP: " + npE);
		}
		catch (Exception ex)
		{
			Log.log(this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC,"enviaRecargaMASC", "ERRO ao receber dados do MASC - " + ex);
			throw new GPPInternalErrorException("Erro interno do GPP: " + ex);
		}
				
		finally
		{
			retornoRecarga.setMSISDN(aNumeroAssinante);
			retornoRecarga.setValorPrincipal((max_cred - cur_cred)/100000 + aCredito);
			retornoRecarga.setCodigoErro(retorno);
			
			// Libera conexao do banco de dados
			this.gerenteBanco.liberaConexaoPREP(DBConexao, this.idProcesso);
			try
			{
				// Verifica se o socket foi aberto
				if (sk != null)
				{
					// Libera Conexao SMSC
					sk.close();
					out.close();
					in.close();
				}
			}
			catch (IOException e)
			{
				Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro ao fechar socket com o MASC: " + e);
				throw new GPPInternalErrorException ("Erro: " + e);
			}
			catch(Exception e)
			{
				Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro ao fechar socket com o MASC: " + e);
				throw new GPPInternalErrorException ("Erro: " + e);
			}
			Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Fim RETORNO "+retorno);
		}
		return retornoRecarga;
	
	}

  /**
	* Metodo...: enviaConsultaMASC
	* Descricao: Envia a Consulta a ser realizada pelo MASC 
	* @param 	aNumeroAssinante	- Numero do assinante a ser consultado
	* @return	Assinante 			- objeto do tipo assinante contendo o saldo e a data de expiracao 
	* @throws 	GPPInternalErrorException
	*/
	public Assinante enviaConsultaMASC(String aMSISDN) throws GPPInternalErrorException
	{
		Log.log(this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Inicio MSISDN " + aMSISDN);
		Assinante retorno = new Assinante();
		double max_cred = 0.0;
		double cur_cred = 0.0;
		String date_fin = null;
		String linha = null;
//		ArquivoConfiguracaoGPP arqConfig = 
		ArquivoConfiguracaoGPP.getInstance();
		ConexaoBancoDados DBConexao = null;
		try
		{
			// Abre conexao MASC
			sk = new Socket(this.ipServidor, Integer.parseInt(this.portaServico));
			sk.setSoTimeout(60000);
			
			out = new PrintWriter(sk.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sk.getInputStream()));
			// Checa se recebeu a linha de confirmação de conexão: token "200"
			linha = recebeString();
			if(linha.startsWith("200"))
			{
				// Envia o pedido de login, primeiro o user
				enviaString("user " + this.nomeUsuario);
				linha = recebeString();
				if(linha.startsWith("300"))
				{
					// Envia o pedido de login, a senha
					enviaString("pass " + this.senhaUsuario);	
					linha = recebeString();
					// Checa se conseguiu logar: token "202"
					if (linha.startsWith("202"))
					{
	
						Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Usuário " + this.nomeUsuario +" logado"); 
						// numero sequencial de consulta
						DBConexao = gerenteBanco.getConexaoPREP(this.idProcesso);
						ResultSet rsSequenceNSU = DBConexao.executaQuery("SELECT SEQ_RECARGA_MASC.NEXTVAL SEQ FROM DUAL", this.idProcesso);
						String num = null;
						if(rsSequenceNSU.next())
						{
							num = new Long(rsSequenceNSU.getLong("SEQ")).toString();
						}
						rsSequenceNSU.close();
						
						// envia o pedido de serviço de consulta de assinante
						enviaString("SEND " + this.nomeUsuario + " " + GPPData.formataNumero(num,10) + " " + aMSISDN.substring(2, 4) + " " + aMSISDN.substring(4, aMSISDN.length()) + " " + "CRI");
						
						linha = recebeString();
						// Confirma a inclusão do serviço
						if (linha.startsWith("203"))
						{
							Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Serviço de Consulta Adicionado");
							linha = recebeString();
	
							// Confirma a realização do serviço "211"
							if (linha.startsWith("211"))
							{
								linha = recebeString();
								java.util.StringTokenizer st = new java.util.StringTokenizer( linha, " ", false );
								
								// Retorna o último token da linha
								for (int c = 0; c<4; c++)
								{
									st.nextToken();
								}
								int codretorno = Integer.parseInt(st.nextToken());
								
								// Assinante inexistente
								switch (codretorno)
								{
									case Definicoes.RET_OPERACAO_OK:
									{
										retorno.setRetorno((short)Definicoes.RET_OPERACAO_OK);
										Log.log (this.idProcesso, Definicoes.INFO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Assinante " + aMSISDN + " Existente");
										break;
									}	
									case -15:
									{
										retorno.setRetorno((short)Definicoes.RET_MSISDN_NAO_ATIVO);
										Log.log (this.idProcesso, Definicoes.WARN, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Assinante " + aMSISDN + " Inexistente");
										break;
									}									
									case -1:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "MASC: Problemas de comunicação interna");
										break;
									}
									case -2:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "MASC: Demora exagerada na resposta");
										break;
									}
									case -4:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "MASC: Resposta da central problemática");
										break;
									}
									case -9:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "MASC: O processador da central estava ocupado na hora do serviço");
										break;
									}
									case -22:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "MASC: Central em falha");
										break;
									}
									case -99:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro Plataforma RI");
										break;
									}
									default:
									{
										retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
										Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaRecargaMASC", "Erro generico");
										break;
									}
								}
								
								StringBuffer sbuffer = new StringBuffer();
								// Usa o 1o pattern pra retirar o 210 e concatenar as linhas
								Pattern pattern = Pattern.compile("^210 (.*)$");
								while((linha = recebeString()) != null)
								{
								    Matcher matcher = pattern.matcher(linha);
							        if(matcher.matches())
							        {
							            sbuffer.append(matcher.group(1));
							        }
								}
						        // Usa o 2o pattern pra popular a Hashtable
						        Hashtable htable = new Hashtable();
						        // Retorna todos os tokens alternados entre NOME_CAMPO (group(1)) e CONTEUDO_CAMPO (group(2))
						        // definidos nos []
						        pattern = Pattern.compile("([\\w|\\s|:]*)=([\\w|\\s|:|\\/]*),");
						        Matcher matcher = pattern.matcher(sbuffer);
						        
						        while(matcher.find())
						        {
						            String chave = matcher.group(1).trim();
						            String valor = matcher.group(2).trim();
						            htable.put(chave, valor);         
						        }
						        
								if(codretorno == Definicoes.RET_OPERACAO_OK)
								{
									if (htable.containsKey("MAX_CRED") && htable.containsKey("CURCRED"))
									{
										max_cred = Double.parseDouble((String)htable.get("MAX_CRED"));
										cur_cred = Double.parseDouble((String)htable.get("CURCRED"));
										retorno.setSaldoCreditosPrincipal((max_cred - cur_cred)/100000L);
									}
									else
										retorno.setSaldoCreditosPrincipal(0.0);
								    
									if(htable.containsKey("DATEFIN"))
								        date_fin = (String)htable.get("DATEFIN");
								    
									retorno.setMSISDN(aMSISDN);
									retorno.setStatusServico((short)Definicoes.SERVICO_DESBLOQUEADO);
									retorno.setDataExpiracaoPrincipal(date_fin);
									retorno.setRetorno((short)Definicoes.RET_OPERACAO_OK);
								}
								Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Consulta Terminada");
															
							}
						}
						else // Problemas de conexão
						{
							retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
							if (linha.startsWith("529"))
							{
								Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Não existe conexão OK disponível com MASC");
							}
						}
					}
					else //Senha incorreta
					{
						retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
						Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Login não realizado. Senha Incorreta");
					}
				}
				else //User não cadastrado
				{
					retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
					Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Login não realizado. User não cadastrado");
				}
			}
		    // Se nao encontrou o token "200", aconteceu um erro na conexão com o MASC
			else
			{
				retorno.setRetorno((short)Definicoes.RET_ERRO_GENERICO_MASC);
					Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "ERRO na conexão com o MASC");
			}
		}
		catch (GPPInternalErrorException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "ERRO ao abrir socket com o MASC: " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (NumberFormatException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "ERRO ao abrir socket: NumberFormatException - " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (IOException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "ERRO ao abrir socket: IOException - " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (SQLException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "ERRO ao abrir socket: SQLException - " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBanco.liberaConexaoPREP(DBConexao, this.idProcesso);
			try
			{
				// Verifica se o socket foi aberto
				if (sk != null)
				{
					// Libera Conexao SMSC
					sk.close();
					out.close();
					in.close();
				}
			}
			catch (IOException e)
			{
				Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Erro ao fechar socket com o MASC: " + e);
				throw new GPPInternalErrorException ("Erro: " + e);
			}
			
			Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaConsultaMASC", "Fim RETORNO "+retorno);
		}
		return(retorno);
	
	}
	
  /**
	* Metodo...: enviaString
	* Descricao: Envia uma string pro servidor MASC 
	* @param 	parametro			- parametro a ser enviado ao MASC
	*/	
	public void enviaString(String parametro)
	{
		
		// Envia Mensagem
		Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "enviaString", parametro);
		out.println(parametro);
	
	}
 
  /**
	* Metodo...: recebeString
	* Descricao: recebe uma string pro servidor MASC 
	* @param 	inBuffer			- parametro do tipo BufferedReader
	* @return	parametroin			- String recebida do MASC 
	* @throws 	GPPInternalErrorException
	*/		
	public String recebeString() throws GPPInternalErrorException
	{
		String parametroin=null;	
		try
		{
			int numtentativas = 0;
			
			parametroin = in.readLine();
			while((parametroin == null)&&(numtentativas++ < this.numTentativasReadln))
			{	
				Thread.sleep(500);
				parametroin = in.readLine();
				//Espera até receber alguma linha
			}
		}
		catch (IOException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "recebeString", "ERRO ao abrir socket com o MASC: " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		catch (InterruptedException e)
		{
			Log.log (this.idProcesso, Definicoes.ERRO, Definicoes.CL_CONEXAO_MASC, "recebeString", "ERRO ao abrir socket com o MASC: " + e);
			throw new GPPInternalErrorException ("Erro: " + e);
		}
		Log.log (this.idProcesso, Definicoes.DEBUG, Definicoes.CL_CONEXAO_MASC, "recebeString", parametroin);
		return parametroin;
	}
	
}
