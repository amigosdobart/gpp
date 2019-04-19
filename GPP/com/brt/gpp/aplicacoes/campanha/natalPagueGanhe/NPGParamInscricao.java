package com.brt.gpp.aplicacoes.campanha.natalPagueGanhe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.campanha.dao.AssinanteCampanhaDAO;
import com.brt.gpp.aplicacoes.campanha.dao.NatalPagueGanheDAO;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteTSD;
import com.brt.gpp.aplicacoes.campanha.entidade.Campanha;
import com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao;
import com.brt.gpp.aplicacoes.campanha.entidade.NPGInfosBonificacao;
import com.brt.gpp.aplicacoes.campanha.entidade.NPGStatusBonificacao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;

/**
 *	Implementacao das regras de negocio para inscricao de assinantes na campanha da promocao Natal Pague e Ganhe.
 *
 *	@author		Daniel Ferreira
 *	@since		03/11/2006
 */
public class NPGParamInscricao implements ParametroInscricao
{
	
	//Atributos.
	
	/**
	 *	Campanha promocional cujos parametros de inscricao sao implementados por esta classe.
	 */
	private Campanha campanha;
	
	/**
	 *	Informacoes referentes a subida de TSD do assinante e de sua bonificacao.
	 */
	private NPGInfosBonificacao infosBonificacao;
	
	//Construtores.
	
	/**
	 *	Construtor da classe.
	 */
	public NPGParamInscricao()
	{
		this.campanha			= null;
		this.infosBonificacao 	= null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		campanha				Campanha a qual o parametro de inscricao pertence.
	 */
	public NPGParamInscricao(Campanha campanha)
	{
		super();
		
		this.campanha = campanha;
	}
	
	//Implementacao de ParametroInscricao.
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#podeSerInscrito(Assinante, PREPConexao)
	 */
	public boolean podeSerInscrito(Assinante assinante, PREPConexao conexaoPrep)
	{
		boolean	result		= true;
		int		codStatus	= NPGStatusBonificacao.INSCRITO;
		int		codRetorno	= Definicoes.RET_OPERACAO_OK;
		
		try
		{
			//Verificando se o assinante ainda nao esta cadastrado. Se ja estiver cadastrado, nao deve ser inscrito novamente.
			if(AssinanteCampanhaDAO.existeAssinanteCampanha(assinante.getMSISDN(), this.campanha.getId(), conexaoPrep))
			{
				result = false;
				return result;
			}
			//Verificando se o plano do assinante corresponde a um Pre-Pago.
			if(MapPlanoPreco.getInstancia().consultaCategoria(assinante.getPlanoPreco()) != Definicoes.CATEGORIA_PREPAGO)
			{
				codStatus	= NPGStatusBonificacao.NAO_VALIDADO;
				codRetorno	= Definicoes.RET_PLANO_PRECO_INVALIDO;
				return result;
			}
			//Verificando se o MSISDN do aparelho ja participou de alguma subida de TSD antes da subida em analise.
			//Como o MSISDN pode ser re-utilizado, ou seja, o assinante que realizar churn tera seu MSISDN desativado
			//e disponibilizado novamente apos periodo de quarentena, o processo deve verificar somente as subidas
			//apos o inicio da promocao.
			SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
			Date dataInicioPromocao = conversorDate.parse(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("CAMPANHA_NPG_DATA_INICIO"));
			if(NatalPagueGanheDAO.msisdnSubiuTSD(assinante.getMSISDN(), dataInicioPromocao, ((AssinanteTSD)assinante).getDataSubidaTSD(), conexaoPrep))
			{
				codStatus	= NPGStatusBonificacao.NAO_VALIDADO;
				codRetorno	= Definicoes.RET_MSISDN_JA_UTILIZADO;
				return result;
			}
			//Verificando se o ICCID do SIMCard ja participou de alguma subida de TSD antes da subida em analise.
			if(NatalPagueGanheDAO.simcardSubiuTSD (((AssinanteTSD)assinante).getICCID(), ((AssinanteTSD)assinante).getDataSubidaTSD(), conexaoPrep))
			{
				codStatus	= NPGStatusBonificacao.NAO_VALIDADO;
				codRetorno	= Definicoes.RET_SIMCARD_JA_UTILIZADO;
				return result;
			}
			//Verificando se o IMEI do aparelho ja participou de alguma subida de TSD antes da subida em analise.
			if(NatalPagueGanheDAO.aparelhoSubiuTSD(((AssinanteTSD)assinante).getIMEI(), ((AssinanteTSD)assinante).getDataSubidaTSD(), conexaoPrep))
			{
				codStatus	= NPGStatusBonificacao.NAO_VALIDADO;
				codRetorno	= Definicoes.RET_APARELHO_JA_UTILIZADO;
				return result;
			}
		}
		catch(Exception e)
		{
			codStatus	= NPGStatusBonificacao.NAO_VALIDADO;
			codRetorno	= Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			//Se o assinante pode ser inscrito, entao obter as informacoes referentes a bonificacao. O fato de o 
			//assinante poder ser inscrito nao necessariamente significa que pode receber o bonus. A inscricao do
			//assinante possibilita a posterior consulta no WPP, disponibilizando o valor do aparelho caso seja 
			//validado e o motivo de nao se-lo.
			if(result)
			{
				try
				{
					//Obtendo as descricoes do status e do codigo de retorno.
					String descStatus	= NPGStatusBonificacao.getDescricao(codStatus);
					String descRetorno	= MapCodigosRetorno.getInstance().getRetorno(codRetorno).getDescRetorno();
					
					//Obtendo as informacoes referentes a bonificacao do assinante elegivel.
					this.infosBonificacao = NatalPagueGanheDAO.getNPGInfosBonificacao((AssinanteTSD)assinante, conexaoPrep);
					this.infosBonificacao.setCodStatus  (codStatus  );
					this.infosBonificacao.setDescStatus (descStatus );
					this.infosBonificacao.setCodRetorno (codRetorno );
					this.infosBonificacao.setDescRetorno(descRetorno);
				}
				catch(Exception e)
				{
					result = false;
				}
			}
		}
		
		return result;
	}
	
	/**
	 *	@see		com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#getParametros()
	 */
	public Map getParametros()
	{
		if(this.infosBonificacao != null)
		{
			return this.infosBonificacao.toMap();
		}
		
		return null;
	}
	
	/**
	 * @see com.brt.gpp.aplicacoes.campanha.entidade.ParametroInscricao#enviaSMS()
	 */
	public boolean enviaSMS(Assinante assinante, PREPConexao conexaoPrep) {
		return true;
	}
}
