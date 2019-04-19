//Definicao do Pacote
package com.brt.gpp.aplicacoes.gerenteFeliz;

//Arquivo de Imports de Gerentes do GPP 
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;




//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

//Arquivos de imports do java
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;


/**
*
* Este arquivo refere-se a classe GerenteFeliz, responsavel pela manutenção de 
* saldo principal positivo para certos msisdn da tbl_ext_happy_manager 
* que necessitam ter créditos ilimitados
*
* <P> Versao:			1.0
*
* @Autor: 			Lawrence Josuá
* Data: 				03/02/2005
*
* Modificado por:
* Data:
* Razao:
*
*/


public final class GerenteFeliz extends Aplicacoes
{
	// Variaveis Membro
	protected GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	protected GerentePoolTecnomen gerenteTecnomen = null; 		// Gerente de conexoes Tecnomen
	protected long idLog; 										// Armazena o ID do log
		     
	/**
	 * Metodo...: MantemCreditos
	 * Descricao: Construtor 
	 * @param	logId - Identificador do Processo para Log
	 * @return									
	 */
	 public GerenteFeliz (long logId)
	 {
		super(logId, Definicoes.CL_GERENTE_FELIZ);
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);

		// Obtem referencia ao gerente de conexoes a plataforma Tenomen
		this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(logId);
		
		this.idLog = logId;
	 }

	/**
	 * Metodo...: mantemCreditos
	 * Descricao: Realiza a manutenção de créditos dos gerentes felizes
	 * 
	 * @return	short		- RET_OPERACAO_OK se sucesso ou diferente em caso de falha
	 */
	public short mantemCreditos ( ) throws GPPInternalErrorException 
	{
		long nroManutencoesSucesso = 0;
		long nroManutencoesFalha = 0;
		short retorno = -1 ;
		String status = Definicoes.PROCESSO_ERRO;
		String dataInicial = GPPData.dataCompletaForamtada();
		
		super.log(Definicoes.DEBUG, "mantemCreditos", "Inicio do processo de manutencao de creditos");
		PREPConexao conexaoPrep = null;
		MapConfiguracaoGPP mapConfiguracaoGPP = MapConfiguracaoGPP.getInstancia();

		try
		{			
			// Busca uma conexao de banco de dados		
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(this.idLog);
		
			// Faz a pesquisa no banco para checar quem são os gerentes felizes a receber crédito
			String sql = "SELECT MSISDN, TIPO, CREDITO, DATA_INCLUSAO " + 
						 "  FROM TBL_EXT_HAPPY_MANAGER" + 
						 "  WHERE CREDITO = 'S'";
						
			ResultSet rs = conexaoPrep.executaQuery(sql, this.idLog);
		
			// Para cada registro retornado, realizar o ajuste de saldo especificado: 50 - saldo
			while (rs.next())
			{
				String msisdn = rs.getString("msisdn");
				Aprovisionar meiodeConsulta = new Aprovisionar(this.idLog);
				Assinante assinante = meiodeConsulta.consultaAssinante(msisdn);
				
				//checa a existência do assinante e se o saldo dele é inferior a 5
				super.log(Definicoes.DEBUG, "mantemCreditos", "Assinante: "+ msisdn);
				if(assinante != null)
				{
					// Inicialização de Variáveis para realizar o Ajuste
					Ajustar ajustar = new Ajustar(this.idLog);
					short numDiasExp = Short.parseShort(mapConfiguracaoGPP.getMapValorConfiguracaoGPP("NUM_DIAS_EXP_GERENTE_FELIZ"));
					String tipoTransacao = null;
					double valorAjuste = 0;
					
					// Verifica se o assinante vai expirar nos próximos três dias
					// Ou se já expirou
					if(	GPPData.d1GreaterThanD2(GPPData.mudaFormato(GPPData.getDataAcrescidaDias("3")),GPPData.mudaFormato(assinante.getDataExpiracaoPrincipal())) ||
						assinante.getStatusAssinante()==3 || assinante.getStatusAssinante()==4 )
					{
						// Se ele estiver para expirar, realiza um ajuste para prorrogar data de expiração
						tipoTransacao = "05011";		// tipo de Transação para Prorrogação Data de Expiração
						valorAjuste = 0;
					}
					else
					{
						// Verifica se os créditos do acesso estão se esgotando
						if(assinante.getCreditosPrincipal() < Double.parseDouble(mapConfiguracaoGPP.getMapValorConfiguracaoGPP("SALDO_MINIMO_GERENTE_FELIZ")))
						{
							// Se os créditos estiverem se esgotando, ajustar seu saldo para que fique no máximo de novo
							tipoTransacao = "05008";	// tipo de Transação para Ajuste de Crédito Amigos Toda Hora
							valorAjuste = Double.parseDouble(mapConfiguracaoGPP.getMapValorConfiguracaoGPP("SALDO_MANUTENCAO_GERENTE_FELIZ")) - assinante.getCreditosPrincipal();
						}
					}
					
					// Se o tipoTransacao continuar null, não há necessidade de ajuste para esse acesso  
					if(tipoTransacao != null)
					{
						short retAjuste = ajustar.executarAjuste(msisdn, 
																 tipoTransacao, 
																 Definicoes.TIPO_CREDITO_REAIS, 
																 valorAjuste, 
																 Definicoes.TIPO_AJUSTE_CREDITO, 
																 Calendar.getInstance().getTime(), 
																 Definicoes.SO_GPP, 
																 Definicoes.GPP_OPERADOR, 
																 assinante.newDataExpiracao(TipoSaldo.PRINCIPAL, numDiasExp),
																 assinante, 
																 "",
																 Definicoes.AJUSTE_NORMAL);						
										     
						if (retAjuste == 0 )
						{
							super.log(Definicoes.DEBUG, "mantemCreditos", "- OK - Manutencao de Creditos para o MSISDN: "+ msisdn +" realizada com sucesso");
							nroManutencoesSucesso++;
						}
						else
						{
							super.log(Definicoes.WARN, "mantemCreditos", "+ NOK + Manutencao de Creditos para o MSISDN: "+ msisdn +" realizada com erro: "+retAjuste);
							nroManutencoesFalha++;
						}
				    }
				    else
				    {
				    	super.log(Definicoes.DEBUG, "mantemCreditos", "Saldo do MSISDN " + msisdn + " nao precisa ser ajustado");
				    	nroManutencoesSucesso++;
				    }
				}
				else
				{
					super.log(Definicoes.WARN, "mantemCreditos", "+ NOK + Assinante "+msisdn+" Inexistente");
					nroManutencoesFalha++;
				}
			}
			
			// Se não houve exceção, o processo rodou bem
			status = Definicoes.PROCESSO_SUCESSO;
			retorno = 0;
		}
		catch (GPPInternalErrorException e1)
		{
			super.log(Definicoes.ERRO, "mantemCreditos", "Excecao Interna GPP ocorrida: "+ e1);
			status = Definicoes.PROCESSO_ERRO;
		}
		catch (SQLException e3)
		{
			super.log(Definicoes.ERRO, "mantemCreditos", "Erro durante execucao de operacao no Banco de Dados: "+ e3);
			status = Definicoes.PROCESSO_ERRO;
		}
		finally
		{
			// Libera conexao do banco de dados
			this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());

			String dataFinal = GPPData.dataCompletaForamtada();
			String descricao = "Manutencoes com sucesso:" + nroManutencoesSucesso + 
			                   ";Manutencoes com erro:"    + nroManutencoesFalha;
			
			//chama a funcao para gravar no historico o Processo em questao
			super.gravaHistoricoProcessos(Definicoes.IND_GERENTE_FELIZ, dataInicial, dataFinal, status, descricao, dataInicial);
			super.log(Definicoes.INFO, "mantemCreditos", "Fim do processo batch de manutenção de créditos");
		}
		
		return retorno;
	}
}