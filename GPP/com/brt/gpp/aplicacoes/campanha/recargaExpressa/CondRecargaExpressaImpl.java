package com.brt.gpp.aplicacoes.campanha.recargaExpressa;

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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CondRecargaExpressaImpl implements CondicaoConcessao
{
	private DecimalFormat df = new DecimalFormat("###0.00");
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private CondIncentivoRecargas condicaoConcessao;
	private GerentePoolLog log;
	private RecargaExpressaDAO recargaExpressaDAO	= new RecargaExpressaDAO();
	
	public CondRecargaExpressaImpl(Campanha campanha)
	{
		log = GerentePoolLog.getInstancia(this.getClass());
	}
	
	/**
	 *@see com.brt.gpp.aplicacoes.campanha.entidade.CondicaoConcessao#deveSerBonificado(com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha)
	 */
	public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep)
	{
		boolean deveBonificar 	= false;
		Map condicoesRecarga 	= CondIncentivoRecargasDAO.getCondsIncentivoRecargasMap(assinante.getCampanha());
		try
		{
			// Utiliza os parametros do assinante para definir o saldo
			// inicial do assinante no ato da primeira recarga que o 
			// elegeu para a campanha assim como tambem a data e o valor
			// desta primeira recarga
			Date 		dtEntradaStatus2	= sdf.parse(assinante.getParametros().get("dataEntradaStatus2").toString());
			boolean		fezRecarga			= Boolean.getBoolean((String)assinante.getParametros().get("fezRecarga"));
			int 		valorRecarga		= Integer.parseInt((String)assinante.getParametros().get("valorRecarga"));
			ResultSet	rs					= null;	

			// Busca no mapeamento de condicoes de incentivo de recargas
			// qual o valor do bonus associado ao valor efetivo da recarga
			// feita pelo assinante. Este valor tambem a condicao de concessao
			condicaoConcessao = (CondIncentivoRecargas)condicoesRecarga.get(new Double(valorRecarga));
			
			// Verifica se uma SMS ja foi enviada ao assinante. Se sim,
			// significa que ele ja recebeu o bônus.
			if (!fezRecarga) 
			{
				// Como todo o processo de bonificacao vale para os assinantes que 
				// consumiram o valor da primeira recarga em ateh 48 horas, contando
				// um tempo extra de limite, se a data atual for 3 dias maior que a
				// data de entrada no status 2, o assinante não está mais apto a
				// participar da promoção e deve ser retirado da base
				Calendar dataMaxima = Calendar.getInstance();
				dataMaxima.setTime(dtEntradaStatus2);
				dataMaxima.add(Calendar.DAY_OF_MONTH,3);
				
				if (Calendar.getInstance().getTime().after(dataMaxima.getTime()))
				{
					// Retira o assinante da campanha e termina o processamento
					// indicando que este assinante nao deve ser bonificado
					AssinanteCampanhaDAO.retiraAssinante(assinante);
					return false;
				}
				
				// Verifica se o assinante fez a recarga
				rs = recargaExpressaDAO.fezRecargaUltimosDias(assinante, conexaoPrep, 3);
				// Verifica se deve bonificar ou nao
				if (rs.next())
				{
					deveBonificar = true;
					// Atribui novamente o valor de concessão de bônus para que
					// não haja problemas caso o assinante tenha efetuado a recarga
					// somente após a sua inscrição (o XML estará preenchido
					// com valorRecarga zero)
					condicaoConcessao = (CondIncentivoRecargas)condicoesRecarga.get(new Double(rs.getInt("valor")));
				}
				else
				{
					deveBonificar = false;
				}
				rs.close();
			}
			else
			{
				return true;
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

