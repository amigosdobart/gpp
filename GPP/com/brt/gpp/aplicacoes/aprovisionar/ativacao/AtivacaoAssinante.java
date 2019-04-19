package com.brt.gpp.aplicacoes.aprovisionar.ativacao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.SaldoAssinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;

/**
 * Classe responsavel pela atividade de aprovisionamento de Ativacao de assinante
 * O metodo mais importante eh o <code>ativar(GPPAtivacaoAssinante gPPAtivacaoAssinante)</code>,
 * pois este eh uma porta de entrada unica para o processo de ativacao.
 * @author Magno Batista Correa
 * @since  20080212
 */
public class AtivacaoAssinante extends Aplicacoes {

	private Aprovisionar aprovisionar;
	private ControlePromocao controle;
	private TecnomenAprovisionamento conexaoTecnomen;
	private AtivacaoAssinanteDAO dao;
	private PREPConexao conexaoPrep;
	public AtivacaoAssinante(long idProcesso, TecnomenAprovisionamento conexaoTecnomen, PREPConexao conexaoPrep) throws GPPInternalErrorException
	{
		super(idProcesso, "AtivacaoAssinante");
		super.logId = idProcesso;
		this.aprovisionar = new Aprovisionar(super.logId);
		this.conexaoTecnomen = conexaoTecnomen;
		this.conexaoPrep  = conexaoPrep;
		this.controle = new ControlePromocao(idProcesso);
		this.dao = new AtivacaoAssinanteDAO(conexaoPrep,super.logId);
	}

	/**
	 * Realiza o calculo do plano de preco de acordo com a necessidade da promocao vigente.
	 * @param planoPreco
	 * @return
	 */
	private short calcularPlanoEfetivo(short planoPreco) {
		ControlePromocao controle = new ControlePromocao(this.logId);
		return controle.calcularPlano(PlanoEspelho.ATIVACAO,planoPreco, (short) -1);
	}

	/**
	 *Inserir o assinante na promocao Novo Pula-Pula 2008 Turbinado
	 */
	private void tratarTurbinado(GPPAtivacaoAssinante gPPAtivacaoAssinante) {
		ControlePulaPula ctrlPulaPula = new ControlePulaPula(super.logId);
		Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());
		
		short retorno = ctrlPulaPula.inserePulaPula(gPPAtivacaoAssinante.getAssinante()
				.getMSISDN(), 7, dataProcessamento, gPPAtivacaoAssinante
				.getOperador(), Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO);
		
		if  (retorno != Definicoes.RET_OPERACAO_OK) //Caso ocorreu algum problema estranho
			super.log(Definicoes.WARN, "tratarTurbinado", "Falha ao inserir pula-pula para o assinante:"+gPPAtivacaoAssinante);

	}

	/**
	 * Realiza todas as operacoes necessarias e especificas para um assinante hibrido
	 * @param gPPAtivacaoAssinante
	 * @throws GPPInternalErrorException
	 */
	private void tratarHibrido(GPPAtivacaoAssinante gPPAtivacaoAssinante) throws GPPInternalErrorException {
		Calendar dataAtual = Calendar.getInstance();
		dataAtual.add(Calendar.SECOND, 10);
		Timestamp dataTAtual = new Timestamp(dataAtual.getTimeInMillis());

		String msisdn = gPPAtivacaoAssinante.getAssinante().getMSISDN();
		// Grava em tabela os dados de msisdn hibrido
		if(!dao.gravarHibrido(gPPAtivacaoAssinante, dataAtual.getTime()))
			super.log(Definicoes.WARN, "tratarHibrido", "Falha ao gravar dados de hibrido para o assinante:"+gPPAtivacaoAssinante);

		// Insere a promoção vigente
		short retorno = this.controle.inserePromocoesAssinante(msisdn, dataAtual.getTime(),
				dataTAtual, gPPAtivacaoAssinante.getOperador(),
				Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO, this.conexaoPrep);

		if  (retorno != Definicoes.RET_OPERACAO_OK) //Caso ocorreu algum problema estranho
			super.log(Definicoes.WARN, "tratarHibrido", "Falha ao inserir promocoes para o assinante:"+gPPAtivacaoAssinante);
	}

	
	private short executarAtivar(GPPAtivacaoAssinante gPPAtivacaoAssinante) throws GPPInternalErrorException
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		// Configura a flag de turbinado e de hibrido
		// O turbinado depende do plano informado,
		// enquanto o hibrido depende do plano efetivo
		boolean isTurbinado = gPPAtivacaoAssinante.getAssinante().getPlanoPreco() == 49;
		gPPAtivacaoAssinante.getAssinante().setPlanoPreco(
				this.calcularPlanoEfetivo(gPPAtivacaoAssinante.getAssinante().getPlanoPreco()));
		boolean isHibrido = aprovisionar.eHibrido(gPPAtivacaoAssinante.getAssinante().getPlanoPreco());
		
		try
		{
			retorno = this.conexaoTecnomen.ativarAssinante(gPPAtivacaoAssinante.getAssinante());
			if (retorno == Definicoes.RET_OPERACAO_OK) 
			{
				if (isHibrido)
					this.tratarHibrido(gPPAtivacaoAssinante);
				if (isTurbinado)
					this.tratarTurbinado(gPPAtivacaoAssinante);
			}
		}
		catch (Exception e)
		{
			retorno = Definicoes.RET_ERRO_GENERICO_GPP;
			super.log(Definicoes.ERRO,"ativacaoAssinante", "Erro ao Ativar Assinante:"+gPPAtivacaoAssinante);
			throw new GPPInternalErrorException("Erro GPP: " + e);
		}
		finally
		{
			dao.gravarDadosAtivacao(gPPAtivacaoAssinante,retorno);
			super.log(Definicoes.INFO, "ativacaoAssinante","Fim aprovisionamento: "+gPPAtivacaoAssinante+";RETORNO " + retorno);
		}
		return retorno;
		
	}
	/**
	 * 
	 * @param gPPAtivacaoAssinante
	 * @return
	 * @throws GPPInternalErrorException
	 */
	public short ativar(GPPAtivacaoAssinante gPPAtivacaoAssinante) throws GPPInternalErrorException
	{
		short retorno = Definicoes.RET_OPERACAO_OK;
		super.log(Definicoes.INFO, "ativacaoAssinante","Inicio aprovisionamento: " + gPPAtivacaoAssinante);
		
		retorno = this.validarAtivacao(gPPAtivacaoAssinante);
		if (retorno != Definicoes.RET_OPERACAO_OK)
			return retorno;
		
		String msisdn = gPPAtivacaoAssinante.getAssinante().getMSISDN();
		short status = gPPAtivacaoAssinante.getAssinante().getStatusAssinante();
		String operador =gPPAtivacaoAssinante.getOperador();
		
		if(status !=Definicoes.FIRST_TIME_USER)
		{
			try
	        {
	            Aprovisionar aprovisionar = new Aprovisionar(this.logId);
	            Assinante assinante = aprovisionar.consultaAssinante(msisdn);
	            if(assinante == null) // Se o assinante nao existe na plataforma
	            {   
	            	retorno = this.executarAtivar(gPPAtivacaoAssinante);
	            	if(retorno == Definicoes.RET_OPERACAO_OK)
	            		retorno = aprovisionar.atualizarStatusAssinante(msisdn, status, (Date)null, operador);
	            }
	            else
	            {
	                if(assinante.getStatusAssinante() == Definicoes.FIRST_TIME_USER) // Se o assinante existe mas seu status ainda nao foi alterado
	                	retorno = aprovisionar.atualizarStatusAssinante(msisdn, status, (Date)null, operador);
	                else
	                    retorno = Definicoes.RET_MSISDN_JA_ATIVO;
	            }
	        }
	        catch(Exception exception)
	        {
	            super.log(Definicoes.ERRO, "ativaAssinanteComStatus", "Excecao: " + exception);
	            retorno = Definicoes.RET_ERRO_TECNICO;
	        }
		}
		else
			retorno = this.executarAtivar(gPPAtivacaoAssinante);
		return retorno;
		
	}

	/**
	 * Ativar com o formato antigo
	 * @param msisdn
	 * @param imsi
	 * @param planoPreco
	 * @param creditoInicialPrincipal
	 * @param idioma
	 * @param operador
	 * @return
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public short ativar(String msisdn, String imsi, short planoPreco,
			double creditoInicialPrincipal, short idioma, String operador)
			throws GPPInternalErrorException, GPPTecnomenException
			{
		Assinante assinante = new Assinante();
		assinante.setMSISDN(msisdn);
		assinante.setIMSI(imsi);
		assinante.setPlanoPreco(planoPreco);
		assinante.setSaldoCreditosPrincipal(creditoInicialPrincipal);
		assinante.setIdioma(idioma);

		GPPAtivacaoAssinante gPPAtivacaoAssinante = new GPPAtivacaoAssinante(assinante,operador);		
		return this.ativar(gPPAtivacaoAssinante);
	}
	
	/**
	 * Valida se os dados da ativacao estao coerentes
	 * @param gPPAtivacaoAssinante
	 * @return
	 */
    private short validarAtivacao(GPPAtivacaoAssinante gPPAtivacaoAssinante)
    {
    	Assinante assinante = gPPAtivacaoAssinante.getAssinante();
    	short planoEfetivo = this.calcularPlanoEfetivo(gPPAtivacaoAssinante.getAssinante().getPlanoPreco());
    	PlanoPreco infoPlano = MapPlanoPreco.getInstancia().getPlanoPreco(planoEfetivo);
        
    	//Verificando se o plano do assinante existe.
        if(infoPlano == null)
            return Definicoes.RET_PLANO_PRECO_INVALIDO;
        //Verificando se o MSISDN do assinante e aceito pela categoria informada.
        if(!infoPlano.aceitaMsisdn(assinante.getMSISDN()))
            return Definicoes.RET_INCOERENCIA_CATEGORIA_NUMERACAO;
        
		//Verificando se o credito inicial de ativacao e valido.
        try
        {
            MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
            double saldoMaximo = Double.parseDouble(mapConfiguracao.getMapValorConfiguracaoGPP("SALDO_MAXIMO"));

            double creditoInicial = 0.0;
            Collection saldo = assinante.getSaldo();
    		for (Iterator iterator = saldo.iterator(); iterator.hasNext();)
    		{
    			 creditoInicial = ((SaldoAssinante) iterator.next()).getCreditos();
    	         if(creditoInicial  < 0 || creditoInicial > saldoMaximo )
    	              return Definicoes.RET_VALOR_CREDITO_INVALIDO;
    		}
        }
        catch(Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
		// valida o status apresentado
        short status = assinante.getStatusAssinante();
		if(!((status >= Definicoes.FIRST_TIME_USER) && (status <= Definicoes.SHUT_DOWN))) // Se o status NAO tem um valor valido
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;

		return Definicoes.RET_OPERACAO_OK;
    }
}
