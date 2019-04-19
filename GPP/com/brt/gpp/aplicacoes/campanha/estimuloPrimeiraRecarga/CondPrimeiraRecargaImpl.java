package com.brt.gpp.aplicacoes.campanha.estimuloPrimeiraRecarga;

import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.dao.CondIncentivoRecargasDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.CondIncentivoRecargas;
import com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.persistencia.Operacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CondPrimeiraRecargaImpl implements CondicaoConcessao
{
	private DecimalFormat df = new DecimalFormat("###0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private CondIncentivoRecargas condicaoConcessao;
	private GerentePoolLog log;
	
	public CondPrimeiraRecargaImpl(Campanha campanha)
	{
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#deveSerBonificado(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha)
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		boolean deveBonificar = false;
		try
		{
			// Utiliza os parametros do assinante para definir o saldo
			// inicial do assinante no ato da primeira recarga que o 
			// elegeu para a campanha assim como tambem a data e o valor
			// desta primeira recarga
			double 	saldoRecarga 	= (df.parse((String)assinante.getParametros().get("saldoFinal"))).doubleValue();
			double	valorRecarga 	= (df.parse((String)assinante.getParametros().get("valorRecarga"))).doubleValue();
			Date   	dataPrimRecarga = sdf.parse((String)assinante.getParametros().get("dataRecarga"));
			double  saldoInicial    = saldoRecarga - valorRecarga;
			
			// Como todo o processo de bonificacao vale para os assinantes que 
			// consumiram o valor da primeira recarga em ateh 15 dias entao caso
			// a diferenca da data atual de processamento da data inicial de
			// ativacao for maior que 15 dias entao o assinante nao mais precisa
			// ser bonificado e pode ser retirado da campanha promocional
			Calendar dataMaxima = Calendar.getInstance();
			dataMaxima.setTime(dataPrimRecarga);
			dataMaxima.add(Calendar.DAY_OF_MONTH,15);
			
			if (Calendar.getInstance().getTime().after(dataMaxima.getTime()))
			{
				// Retira o assinante da campanha e termina o processamento
				// indicando que este assinante nao deve ser bonificado
				AssinanteCampanhaDAO.retiraAssinante(assinante);
				return false;
			}
			
			// Pesquisa o saldo final do assinante na data de processamento
			double saldoFinal = getSaldoFinalAtual(assinante.getMsisdn(), conexaoPrep);
			
			// Identifica todos os valores de recargas realizados no saldo principal
			// do assinante a partir da data de ativacao da primeira recarga
			// que o elegeu em ateh 15 dias desta data.
			double somaRecargas = getValorRecargas(assinante.getMsisdn(), dataPrimRecarga, conexaoPrep);
			
			// Realiza o calculo afim de identificar se o assinante consumiu todo
			// o valor da primeira recarga. Se sim entao este deve receber o valor
			// da recarga em bonus pela campanha senao nao faz nada. Como a campanha
			// especifica em ateh 15 dias entao este assinante continuara sendo
			// processado ateh o termino destes 15 dias.
			if ( (saldoInicial + somaRecargas) >= saldoFinal)
			{
				// Busa no mapeamento de condicoes de incentivo de recargas
				// qual o valor do bonus associado ao valor efetivo da recarga
				// feita pelo assinante. Este valor tambem a condicao de concessao
				Map condicoesRecarga = CondIncentivoRecargasDAO.getCondsIncentivoRecargasMap(assinante.getCampanha());
				condicaoConcessao = (CondIncentivoRecargas)condicoesRecarga.get(new Double(valorRecarga));

				// Indica que o assinante pode ser bonificado e define tambem
				// os valores das condicoes estabelecidas e dos bonus a serem
				// configurados para o assinante
				deveBonificar = true;
			}
		}
		catch(Exception e)
		{
			log.log(0,Definicoes.ERRO,"CondPrimeiraRecargaImpl","deveSerBonificado"
					 ,"Erro ao verificar se o assinante " + assinante.getMsisdn() + " deve ser bonificado pela campanha de primeira recarga. Erro:"+e);
		}
		return deveBonificar;
	}
	
	/**
	 * Metodo....:getSaldoFinalAtual
	 * Descricao.:Retorna o valor do saldo atual principal do assinante
	 *            registrado no dia do processamento na tabela de dados
	 *            de assinantes da plataforma tecnomen
	 * @param msisdn 		- Msisdn a ser pesquisado
	 * @param conexaoPrep	- Conexao de banco de dados a ser utilizada
	 * @return	double		- Valor do saldo final do assinante
	 * @throws Exception
	 */
	private double getSaldoFinalAtual(String msisdn, PREPConexao conexaoPrep) throws Exception
	{
		double saldoFinal = 0;
		String sql = "select account_balance " +
		               "from tbl_apr_assinante_tecnomen " +
		              "where dat_importacao = ? " +
		                "and sub_id = ?";
		ResultSet rs = null;
		try
		{
			Object param[] = {new java.sql.Date (Calendar.getInstance().getTimeInMillis()),msisdn};
			rs = conexaoPrep.executaPreparedQuery(sql,param,0);
			if (rs.next())
				saldoFinal = rs.getDouble("account_balance")/Definicoes.TECNOMEN_MULTIPLICADOR;
			else
				throw new Exception("Assinante nao existe na tabela de dados da Tecnomen para a data do processamento.");
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
		return saldoFinal;
	}
	
	/**
	 * Metodo....:getValorRecargas
	 * Descricao.:Retorna o valor das recargas feitas apos a data da primeira recarga
	 * @param msisdn		 	- Msisdn a ser pesquisado
	 * @param dataPrimRecarga	- Data da primeira recarga
	 * @param conexaoPrep		- Conexao de BD a ser utilizado
	 * @return double			- Soma das recargas efetivadas apos a data da prim recarga
	 * @throws Exception
	 */
	private double getValorRecargas(String msisdn, Date dataPrimRecarga, PREPConexao conexaoPrep) throws Exception
	{
		double somaRecargas = 0;
		String sql = "select sum(r.vlr_credito_principal) as valor_recargas " +
		               "from tbl_rec_recargas r " +
		              "where r.dat_origem > ? " +
		                "and r.idt_msisdn = ? " +
		                "and r.vlr_credito_principal != 0";
		
		Object param[] = {new Timestamp(dataPrimRecarga.getTime()), msisdn};
		ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,0);
		if (rs.next())
			somaRecargas = rs.getDouble("valor_recargas");
		
		return somaRecargas;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getNomeCondicao()
	 */
	public String getNomeCondicao()
	{
		if (condicaoConcessao != null)
			return df.format(condicaoConcessao.getValorRecarga());
		
		return null;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederBonus()
	 */
	public double getValorConcederBonus()
	{
		if (condicaoConcessao != null)
			return condicaoConcessao.getValorBonus();
		
		return 0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederSM()
	 */
	public double getValorConcederSM()
	{
		if (condicaoConcessao != null)
			return condicaoConcessao.getValorBonusSM();
		
		return 0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getValorConcederDados()
	 */
	public double getValorConcederDados()
	{
		if (condicaoConcessao != null)
			return condicaoConcessao.getValorBonusDados();
		
		return 0;
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#getDataSatisfacaoCondicao()
	 */
	public Date getDataSatisfacaoCondicao()
	{
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#executarPosBonificacao(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha, com.brt.gpp.comum.conexoes.bancoDados.PREPConexao)
	 */
	public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		// Para a campanha de primeira recarga, o assinante se bonficado
		// deve indicar ao processo de concessao do pula pula para nao
		// "zerar" o saldo de bonus nesta primeira concessao. Portanto
		// somente na segunda concessao de bonus eh que o saldo de bonus
		// deste assinante eh zerado.
		try
		{
			Operacoes op = new Operacoes(0);
			op.atualizaStatus(assinante.getMsisdn(), PromocaoStatusAssinante.STATUS_ATIVO_RECARGA, conexaoPrep);
		}
		catch(Exception e)
		{
			log.log(0,Definicoes.ERRO,"CondPrimeiraRecargaImpl","executarPosBonificacao"
					 ,"Erro ao executar pos bonificacao de campanha para o assinante: " + assinante.getMsisdn() + ". Erro:"+e);
		}
	}
}

