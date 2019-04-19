// Definicao do Pacote
package com.brt.gpp.aplicacoes.consultar.gerarExtratoBoomerang;

//Arquivos de java
import java.sql.*;
import java.util.Locale;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.text.*;

//Arquivos de Gerentes do GPP
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

//Imports Internos
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.aplicacoes.*;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.Cabecalho;
import com.brt.gpp.aplicacoes.consultar.gerarExtrato.Detalhe;
//import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;

//Arquivos de Conexão com Banco de Dados
import com.brt.gpp.comum.conexoes.bancoDados.*;

// Exceções GPP
import com.brt.gpp.comum.gppExceptions.*;

// Arquivos TECNOMEN
/*import com.brt.gpp.comum.conexoes.tecnomen.TecnomenRecarga;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;
import TINC.Pi_exception;
*/
/**
  *
  * Este arquivo refere-se a classe GerarExtratoPulaPula, responsavel pela implementacao do
  * extrato contendo os bônus dados pela promoçao PulaPula
  *
  * <P> Versao:			1.0
  *
  * @Autor: 			Denys Oliveira
  * Data: 				19/11/2004
  * 
  * Modificado por : Luciano Vilela
  * Data : 13/04/2005
  * Razao : Todas as strings de sql foram declaradas como final static 
  * com o objetivo de melhorar a utilizacao de memoria

  *
  */

public class GerarExtratoBoomerang extends Aplicacoes 
{

	// 
	
	private final static String queryDadosComprovanteStatic = "SELECT NOM_CLIENTE, DES_ENDERECO, DES_COMPLEMENTO, DES_BAIRRO, " 
					 + " DES_CIDADE, DES_UF, DES_CEP FROM TBL_GER_DADOS_COMPROVANTE "
					 + "WHERE IDT_MSISDN = ? "
					 + "AND DAT_REQUISICAO = ?"			
					 + "AND IDT_STATUS_PROCESSAMENTO = ?";
					 
	private final static String queryEventosStatic =  "SELECT MAX(DAT_APROVISIONAMENTO) AS DATA " 
				+ "FROM TBL_APR_EVENTOS "
				+ "WHERE TIP_OPERACAO = ? AND IDT_MSISDN = ? ";

	private final static String queryBomerangueStatic = "SELECT	A.CALL_ID AS NRO_DESTINO,"+
					"		TO_CHAR(A.TIMESTAMP,'DD/MM/YYYY') AS DATA,"+
					"		TO_CHAR(A.TIMESTAMP,'HH24:MI:SS') AS HORA,"+
					"		TGR.DES_OPERACAO AS DESCRICAO,"+
					"		A.CELL_NAME AS REG_ORIGEM,"+
					"		A.DESTINATION_NAME AS REG_DESTINO,"+
					"		A.CALL_DURATION AS SEGUNDOS,"+
					"		ROUND(DECODE(P.IDT_CATEGORIA,0,(A.CALL_DURATION/60/3),(A.CALL_DURATION/60/2))*B.VLR_BONUS,2) AS BONUS_RECEBIDO, "+
					"		A.TIP_DESLOCAMENTO AS ROAMING "+ 
					"FROM TBL_GER_CDR A, TBL_GER_TIP_TRANSACAO_TECNOMEN T, TBL_GER_BONUS_CSP14 B, TBL_GER_RATING TGR, TBL_GER_PLANO_PRECO P "+
					"WHERE A.NUM_CSP = ? "+		// Parametro 1
					"AND (A.CALL_DURATION) BETWEEN B.NUM_MINUTOS_MINIMO AND B.NUM_MINUTOS_MAXIMO "+ 
					"AND A.SUB_ID = ? "+		// Parametro 2
					"AND (A.TIP_DESLOCAMENTO = ? OR A.TIP_DESLOCAMENTO = ?) "+		// Parametro 3,4
					"AND nvl(A.ACCOUNT_BALANCE_DELTA,0) <> 0 "+
					"AND T.IDT_SENTIDO <> ? "+ 		// Parametro 5
					"AND T.TRANSACTION_TYPE = A.TRANSACTION_TYPE "+ 
					"AND A.TIMESTAMP BETWEEN ? and ? "+		// Parametro 6,7
					"AND B.IDT_PLANO = A.PROFILE_ID "+ 
					"AND B.IDT_PLANO = P.IDT_PLANO_PRECO "+ 
					"AND B.IDT_CODIGO_NACIONAL = SUBSTR(?,3,2) "+
					"AND TGR.RATE_NAME = A.TIP_CHAMADA " +
					"ORDER BY TIMESTAMP";

	private final static String queryPulaPulaStatic = 	"SELECT IDT_MSISDN  FROM TBL_REC_RECARGAS "+ 
							"WHERE IDT_MSISDN = ? "+
							"AND ID_TIPO_RECARGA = ? "+ 
							"AND DAT_ORIGEM BETWEEN ? and ? ";

	GerentePoolBancoDados gerenteBanco;

	DecimalFormat dFormat = new DecimalFormat("###0.00");
	SimpleDateFormat extraiData = new SimpleDateFormat("dd/MM/yyyy");
	//SimpleDateFormat extraiHora = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	// Dados referentes ao Cabeçalho
	Cabecalho cabecalho = new Cabecalho();
	
	// Dados referentes ao Detalhe
	Vector linhaDetalhe = new Vector();
	
	// Variáveis globais
	String mensagemInformativa = "Espaco Reservado para Mensagem Informativa";
	
	/**
	 * Metodo...: GerarExtrato
	 * Descricao: Construtor 
	 * @param	aIdProcesso - Identificador do processo
	 * @return									
	 */
	public GerarExtratoBoomerang(long aIdProcesso)
	{
		//Define parâmetros de Log
		super(aIdProcesso, Definicoes.CL_GERAR_EXTRATO_BOOMERANG);
		
		//Cria Referência para Banco de Dados
		this.gerenteBanco = GerentePoolBancoDados.getInstancia(aIdProcesso);	
	}
	
	/**
	 * Metodo...: comporExtrato
	 * Descricao: Cria o XML com os dados do Assinante gravados em tabela
	 * @param nomeCliente			- Nome do assinante
	 * @param endereco				- Endereco do assinante
	 * @param msisdn				- Numero do assinante	
	 * @param plano					- Plano de precos
	 * @param dataAtivacao 			- Data de ativacao do assinante (formato dd/mm/aaaa)	
	 * @param inicioPeriodo 		- Periodo inicial do comprovante de servico - extrato (formato dd/mm/aaaa)
	 * @param finalPeriodo 			- Periodo final do comprovante de servico - extrato (formato dd/mm/aaaa)
	 * @param eComprovanteServico	- Flag se indica se eh para ser impresso ou nao
	 * @param dataRequisicao		- Data de requisicao do comprovante
	 * @return String 				- XML com informações do extrato
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public String comporExtrato(String aMsisdn, String inicioPeriodo, String finalPeriodo) throws GPPInternalErrorException, GPPTecnomenException
	{
		try		
		{
			super.log(Definicoes.INFO, "comporExtrato", "Inicio MSISDN "+aMsisdn);

			java.sql.Timestamp tInicioPeriodo = new java.sql.Timestamp(extraiData.parse(inicioPeriodo).getTime());
			java.sql.Timestamp tFinalPeriodo = new java.sql.Timestamp(sdf.parse(finalPeriodo+" "+Definicoes.HORA_FINAL_DIA).getTime());
				
			// Construir o Cabeçalho 
			this.gerarCabecalho(aMsisdn, inicioPeriodo, finalPeriodo, false, null);
			
			// Construir o Detalhe 
			this.gerarDetalhe(aMsisdn, tInicioPeriodo, tFinalPeriodo);
		
			// Gerar arquivo no formato a ser impresso
			return this.gerarXML(aMsisdn, tInicioPeriodo, tFinalPeriodo);										
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO,"comporExtrato","Erro GPP:"+e);
			throw new GPPInternalErrorException("Erro GPP:"+e);
		}
		catch (GPPTecnomenException e)
		{
			super.log(Definicoes.ERRO,"comporExtrato","Erro TECNOMEN:"+e);
			throw new GPPTecnomenException("Erro Tecnomen:"+e);
		}
		catch(ParseException pE)
		{
			super.log(Definicoes.ERRO,"getInfoExtratoPulaPula","Erro no formato de datas");
			throw new GPPInternalErrorException("Erro no formato de datas: "+pE);
		}
		finally
		{
			super.log(Definicoes.INFO, "comporExtrato", "Fim");
		}
	}
	
	/**
	 * Metodo...: gerarDetalhe
	 * Descricao: Busca e valida dados do detalhe de chamadas e recargas para o comprovante de servico
	 * @param  msisdn		- Numero do assinante a buscar os detalhes 
	 * @param  dataInicial 	- Data inicial da busca de detalhes (formato DD/MM/YYYY)
	 * @param  dataFinal 	- Data final da busca de detalhes (formato DD/MM/YYYY)
	 * @return
	 * @throws GPPInternalErrorException
	 */
	private void gerarDetalhe(String msisdn, java.sql.Timestamp tInicioPeriodo, java.sql.Timestamp tFinalPeriodo) throws GPPInternalErrorException
	{
		// Inicialização de Variáveis
		ConexaoBancoDados DBConexao = null;
		ResultSet rsChamadas = null;
		dFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
		double saldoAcumulado = 0;

		try
		{
			// Pega conexão com banco de dados
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());
			
			// Traz as linhas de extrato do assinante no período em questão
			rsChamadas = this.getInfoExtratoBoomerang(msisdn, tInicioPeriodo, tFinalPeriodo, DBConexao);
			
			double bonus=0;
			while(rsChamadas.next())
			{
				// Valida e formata a região de origem da chamada
				String regOrigem = rsChamadas.getString("REG_ORIGEM");
				if (regOrigem == null)
				{
					regOrigem = "-";
				}
				else
				{
					// Pega os 5 primeiros caracteres e substitui o underscore por hífen
					regOrigem = (regOrigem.substring(0,5)).replace('_', '-');
				}
				
				// Valida e formata a região de destino da chamada
				String regDestino = rsChamadas.getString("REG_DESTINO");
				if (regDestino == null)
				{
					regDestino = "-";
				}
				else
				{
					// Pega os 5 primeiros caracteres e substitui o underscore por hífen
					regDestino = (regDestino.substring(0,5)).replace('_', '-');
				}

				bonus = rsChamadas.getDouble("BONUS_RECEBIDO");
				saldoAcumulado += bonus;
				
				// Cria linhas de detalhe de chamadas
				linhaDetalhe.add(new Detalhe(	msisdn, 		//numeroOrig
												rsChamadas.getString("DATA"), 		//data
												rsChamadas.getString("HORA"),			//hora
												rsChamadas.getString("ROAMING").equals("-") ? rsChamadas.getString("DESCRICAO") : rsChamadas.getString("DESCRICAO") + Definicoes.EM_ROAMING,	//Operação
												regOrigem,
												regDestino,													//regiaoOrigem
												rsChamadas.getString("NRO_DESTINO"),														//numeroDestino
												String.valueOf(rsChamadas.getLong("SEGUNDOS")),							//duracao
												new Double(dFormat.format(bonus)).doubleValue()));	// bonus recebido
			}
			
			rsChamadas.close();
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "gerarDetalhe", "Erro: "+e);
			throw new GPPInternalErrorException("Erro GPP: "+e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
			super.log(Definicoes.DEBUG, "gerarDetalhe", "Fim");
		}		
	}

	/**
	 * Metodo...: gerarXML
	 * Descricao: Gera o XML com os dados do comprovante de servico
	 * @param 	String	_msisdn		Msisdn do assinante
	 * @return  String - XML do extrato
	 * @throws
	 */
	private String gerarXML(String msisdn, java.sql.Timestamp tInicioPeriodo, java.sql.Timestamp tFinalPeriodo) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "gerarXML", "Inicio");
		double totalBonus = 0;
		
		DecimalFormat dFormat = new DecimalFormat("###0.00");

		// Variáveis totalizadoras
		long totalSegundos = 0;

		GerarXML geradorXML = new GerarXML("GPPExtrato");
	
		// Mensagem do Comprovante
		geradorXML.adicionaTag("mensagemComprovante","Relatorio de Bonus Pula-Pula");
	
		// Geração da Sessão de Cabecalho - Dados Cadastrais - do XML
		geradorXML.abreNo("dadosCadastrais");
		geradorXML.adicionaTag("nome", cabecalho.getNomeCliente());
		geradorXML.adicionaTag("msisdn", cabecalho.getMsisdn());
		geradorXML.adicionaTag("dataAtivacao",cabecalho.getDataAtivacao());
		geradorXML.adicionaTag("plano",cabecalho.getPlano());
		geradorXML.adicionaTag("periodoInicial",cabecalho.getInicioPeriodo());
		geradorXML.adicionaTag("periodoFinal",cabecalho.getFinalPeriodo());
		geradorXML.adicionaTag("dataHoraImpressao", cabecalho.getDataHora());
		geradorXML.fechaNo();

		// Geração da Sessão de Cabecalho - Dados Correspondência - do XML
		geradorXML.abreNo("dadosCorrespondencia");
		geradorXML.adicionaTag("endereco", cabecalho.getEndereco());
		geradorXML.adicionaTag("complemento", cabecalho.getComplemento());
		geradorXML.adicionaTag("cidade", cabecalho.getCidade());
		geradorXML.adicionaTag("bairro", cabecalho.getBairro());
		geradorXML.adicionaTag("uf", cabecalho.getUf());
		geradorXML.adicionaTag("cep", cabecalho.getCep());
		geradorXML.fechaNo();
		
		// Geração da Sessão de Controle (Mensagens para o Portal)
		geradorXML.abreNo("dadosControle");
		geradorXML.adicionaTag("indRecarga",usuarioFezRecarga(msisdn, tInicioPeriodo, tFinalPeriodo)?"SIM":"NAO");
		geradorXML.fechaNo();

		//Mensagem de Serviços
		geradorXML.adicionaTag("mensagemServicos", "Chamadas Recebidas e Bonus Acumulados no Periodo");

		// Geração da sessão de Detalhes do XML
		if(linhaDetalhe.isEmpty())
		{
			geradorXML.abreNo("detalhe");
			geradorXML.fechaNo();
		}
		else
		{
			//Criação das Tags de Detalhe do XML
			double valor = 0;

			for(int j=0;j<linhaDetalhe.size();j++)
			{
				long duracao;
				
				Detalhe itemDetalhe = (Detalhe) linhaDetalhe.get(j);
			
				geradorXML.abreNo("detalhe");
				geradorXML.adicionaTag("numeroDestino", itemDetalhe.getNumeroDestino());
				geradorXML.adicionaTag("data", itemDetalhe.getData());
				geradorXML.adicionaTag("hora", itemDetalhe.getHora());
				geradorXML.adicionaTag("operacao", itemDetalhe.getOperacao());
				geradorXML.adicionaTag("regiaoOrigem", itemDetalhe.getRegiaoOrigem());
				geradorXML.adicionaTag("regiaoDestino", itemDetalhe.getRegiaoDestino());
				
				duracao = Long.valueOf(itemDetalhe.getDuracao()).longValue();				// extrai duracao para totalização
				totalSegundos += duracao;	// acumula totalizador de segundos
				geradorXML.adicionaTag("duracao", GPPData.segundosParaHoras(duracao));

				valor = itemDetalhe.getValor();				// extrai valor para totalização
				geradorXML.adicionaTag("valor", new Double(valor).toString());
				totalBonus += valor;
				
				geradorXML.fechaNo();
			}
		}

		//Mensagem de Totais
		geradorXML.adicionaTag("mensagemTotais",null);

		// Criação das tags de Total do XML
		geradorXML.abreNo("total");
	
		geradorXML.adicionaTag("numeroChamadas",new Integer(linhaDetalhe.size()).toString());
		geradorXML.adicionaTag("tempoTotal",GPPData.segundosParaHoras(new Long(totalSegundos).longValue()));
		geradorXML.adicionaTag("totalBonus", dFormat.format(totalBonus));

		// Fecha o tag de totais
		geradorXML.fechaNo();

		//Mensagem Informativa
		geradorXML.adicionaTag("mensagemInformativo", mensagemInformativa);

		super.log(Definicoes.DEBUG, "gerarXML", "Fim");
	
		//Retorno do XML completo
		return geradorXML.getXML();			
	}
	
	/**
	 * Metodo...: gerarCabecalho
	 * Descricao: Busca os dados de cabecalho para o comprovante de servico / extrato
	 * @param msisdn				- Numero do assinante a buscar os detalhes 
	 * @param dataInicial 			- Data inicial da busca de detalhes (formato DD/MM/YYYY)
	 * @param dataFinal 			- Data final da busca de detalhes (formato DD/MM/YYYY)
	 * @param eComprovanteServico	- Flag que verifica se eh para ser impresso ou nao
	 * @param dataRequisicao		- Data da requisicao do comprovante
	 * @return
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	private void gerarCabecalho(String msisdn, String inicioPeriodo, String finalPeriodo, boolean eComprovanteServico, Timestamp dataRequisicao) throws GPPInternalErrorException, GPPTecnomenException
	{
		// Inicializa variáveis para conexão com Tecnomen
		//GerentePoolTecnomen gerenteRecarga = GerentePoolTecnomen.getInstancia(super.getIdLog());;
		//TecnomenRecarga tr = null;
		//Assinante infoAssinante = null;
		//String sql;
		ResultSet rs;
		PREPConexao conexaoPrep = null;
		MapPlanoPreco mapPlanoPreco = MapPlanoPreco.getInstancia();
		
		super.log(Definicoes.DEBUG, "gerarCabecalho", "Inicio MSISDN "+msisdn+" Periodo "+inicioPeriodo+"-"+finalPeriodo);

		try
		{
			// Faz a consulta ao Assinante e armazena seus dados num objeto da classe Assinante
			//tr = gerenteRecarga.getTecnomenRecarga(super.getIdLog());
			//infoAssinante = tr.consultaAssinante (msisdn, super.getIdLog());

			//	Seleciona conexao do pool Prep Conexao	
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			if (eComprovanteServico)
			{			

				//	Seleciona dados para gerar comprovante
				
				
				Object param[] = {msisdn, dataRequisicao, "N"};
				rs = conexaoPrep.executaPreparedQuery(queryDadosComprovanteStatic, param, super.getIdLog());
			
				if (rs.next())
				{
					cabecalho.setNomeCliente(rs.getString(1));
					cabecalho.setEndereco(rs.getString(2));
					cabecalho.setComplemento(rs.getString(3));
					cabecalho.setBairro(rs.getString(4));
					cabecalho.setCidade(rs.getString(5));
					cabecalho.setUf(rs.getString(6));
					cabecalho.setCep(rs.getString(7));
					cabecalho.setDataHora(GPPData.dataCompletaForamtada());
				}
				rs.close();
				rs = null;
			}
			
			// Preenche informações do cabeçalho
			cabecalho.setMsisdn(msisdn.substring(2));
			cabecalho.setDataAtivacao(this.getDataAtivacao(msisdn));
			cabecalho.setInicioPeriodo(inicioPeriodo);
			cabecalho.setFinalPeriodo(finalPeriodo);
			
			//Verificando se o assinante existe
			int planoAssinante = -1;
			rs = conexaoPrep.executaPreparedQuery("select fnc_plano_assinante(?,?) from dual",new Object[]{new Timestamp(System.currentTimeMillis()),msisdn}, super.getIdLog());
			if(rs.next())
				planoAssinante = rs.getInt(1); 

			if (planoAssinante !=-1) //infoAssinante != null)
			{
				cabecalho.setPlano(mapPlanoPreco.getMapDescPlanoPreco(new Integer(planoAssinante).toString()));
						//Short.toString(infoAssinante.getPlanoPreco())));									
			}
			else
			{
				cabecalho.setPlano ("");
				super.log(Definicoes.WARN, "comporExtrato", "Gerando extrato de conta para usuario nao cadastrado no GPP.");
			}
		}
		/*catch(Pi_exception e)
		{
			super.log(Definicoes.ERRO, "gerarCabecalho", "Erro tecnomen:" + e);
			throw new GPPTecnomenException("Erro Tecnomen:" + e);
		}*/
		catch(SQLException e)
		{
			super.log(Definicoes.WARN, "gerarCabecalho", "Erro(SQL): " + e);
			throw new GPPInternalErrorException("Erro SQL: " + e);
		}
		finally
		{
			// Libera conexao com do pool PREP
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
			
			// Libera conexao com do pool Recargas
			//gerenteRecarga.liberaConexaoRecarga(tr, super.getIdLog());
			
			super.log(Definicoes.DEBUG, "gerarCabecalho", "Fim");
		}
	}

	/**
	 * Metodo...: getDataAtivacao
	 * Descricao: Obtem a data de ativacao do assinante
	 * @param 	msisdn	- Numero do acesso do assinante a ser pesquisado
	 * @return 	String 	- Data da ultima ativacao
	 * @throws  GPPInternalErrorException
	 */
	private String getDataAtivacao(String msisdn) throws GPPInternalErrorException
	{
		String dataAtivacao = ""; // Melhor alocacao de memoria do que String dataAtivacao = new String();
		ConexaoBancoDados DBConexao = null;
		String retorno = null;
			
		super.log(Definicoes.DEBUG, "getDadosAtivacao", "Inicio MSISDN "+msisdn);

		try
		{
			DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

			// Procura pela data de ativação entre os eventos de aprovisionamento
			
			
			Object params1[] = {Definicoes.TIPO_APR_ATIVACAO, msisdn};
			ResultSet rsAtivacao = DBConexao.executaPreparedQuery1(queryEventosStatic, params1, super.getIdLog());
	
			if(rsAtivacao.next())
			{
				Date auxData = rsAtivacao.getDate("DATA");
				if (auxData != null)
				{
					super.log(Definicoes.DEBUG, "getDadosAtivacao", "Data de ativacao de assinante encontrada");
					dataAtivacao = extraiData.format(auxData);
				}
				rsAtivacao.close();
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "getDadosAtivacao", "Erro:" + e);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao, super.getIdLog());
		}
		
		if ( (dataAtivacao==null) || (dataAtivacao.equals("")) )
		{
			retorno = dataAtivacao;
			super.log(Definicoes.WARN, "getDadosAtivacao", "Nao ha registro de ativacao desto assinante: " + msisdn);
		}
		else
		{
			retorno = dataAtivacao.substring(0,10);
		}
		
		super.log(Definicoes.DEBUG, "getDadosAtivacao", "Fim");

		return retorno;
	}
	
	/**
	 * Metodo...: getInfoExtratoPulaPula
	 * @param double		_bonusPorMinuto		Valor ganho por minuto na promoção pula pula
	 * @param String		_msisdn				Msisdn do assinante
	 * @param int			_numeroMes			Mes em que ele receberá o bonus boomerang
	 * @param ConxaoBanco	DBConexao			Conexão com Banco de Dados
	 * @return		ResultSet					ResultSet contendo as informações dos assinantes
	 * @throws GPPInternalErrorException
	 */
	public ResultSet getInfoExtratoBoomerang( String _msisdn, java.sql.Timestamp tInicioPeriodo, java.sql.Timestamp tFinalPeriodo, ConexaoBancoDados DBConexao) throws GPPInternalErrorException
	{
		ResultSet retorno = null;
		try
		{
			// Busca chamadas para o número no período em questão

			
			Object params1[] = {	String.valueOf(Definicoes.BONUS_NUM_CSP14),
									_msisdn,
									Definicoes.BONUS_TIP_DESLOCAMENTO_CSP14,
									Definicoes.CAMPO_VAZIO,
									Definicoes.BONUS_TT_CHAMADA_ENTRANTE,
									tInicioPeriodo,
									tFinalPeriodo,
									_msisdn
								};
							
			retorno = DBConexao.executaPreparedQuery1(queryBomerangueStatic, params1, super.getIdLog());
		}
		catch (GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"getInfoExtratoBoomerang","Erro Banco de Dados: "+gppE);
			throw new GPPInternalErrorException("Erro Banco de Dados: "+gppE);
		}

		return retorno;
	}

	/**
	 * Metodo...: usuarioFezRecarga
	 * Descricao: Indica se consta uma recarga no período
	 * @param 	String	_msisdn		Número do Assinante
	 * @return	boolean	- Indica se o assinante fez ou nao recarga no periodo
	 */
	public boolean usuarioFezRecarga(String _msisdn, java.sql.Timestamp tInicioPeriodo, java.sql.Timestamp tFinalPeriodo) throws GPPInternalErrorException
	{
		boolean retorno = false;
		ConexaoBancoDados DBConexao = null;
		
		DBConexao = gerenteBanco.getConexaoPREP(super.getIdLog());

		Object[] pulaPulaParams = {	_msisdn,
									Definicoes.TIPO_RECARGA,
									tInicioPeriodo,
									tFinalPeriodo
									};
		
		ResultSet rsPulaPula = DBConexao.executaPreparedQuery(queryPulaPulaStatic, pulaPulaParams, super.getIdLog());
		
		try
		{
			retorno = rsPulaPula.next();
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.ERRO,"usuarioFezRecarga","Erro Banco de Dados: "+sqlE);
			throw new GPPInternalErrorException("Erro Banco de Dados: "+sqlE);
		}
		finally
		{
			gerenteBanco.liberaConexaoPREP(DBConexao,super.getIdLog());
		}
		return retorno;
	}
}
