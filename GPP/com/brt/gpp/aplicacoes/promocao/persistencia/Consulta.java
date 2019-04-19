package com.brt.gpp.aplicacoes.promocao.persistencia;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.importacaoCDR.entidade.ArquivoCDRDadosVoz;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.HistoricoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.InterfaceEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCodigoNacional;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoEstornoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoLimiteDinamico;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoOrigemEstorno;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoPlanoPreco;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoFaleGratis;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoRecargas;
import com.brt.gpp.aplicacoes.promocao.persistencia.Persistencia;
import com.brt.gpp.aplicacoes.recarregar.FilaRecargas;
import com.brt.gpp.aplicacoes.recarregar.Recarga;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapBonusPulaPula;
import com.brt.gpp.comum.mapeamentos.MapCodigoNacional;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapPlanoEspelho;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoCategoria;
import com.brt.gpp.comum.mapeamentos.MapPromocaoCodigoNacional;
import com.brt.gpp.comum.mapeamentos.MapPromocaoDiaExecucao;
import com.brt.gpp.comum.mapeamentos.MapPromocaoInfosSms;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimite;
import com.brt.gpp.comum.mapeamentos.MapPromocaoLimiteDinamico;
import com.brt.gpp.comum.mapeamentos.MapPromocaoOrigemEstorno;
import com.brt.gpp.comum.mapeamentos.MapPromocaoPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapPromocaoStatusAssinante;
import com.brt.gpp.comum.mapeamentos.MapPromocaoTipoTransacao;
import com.brt.gpp.comum.mapeamentos.MapRecOrigem;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.ConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.entidade.OrigemRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;

/**
 *	Classe responsavel pelas operacoes de persistencia relacionadas a consulta de promocoes e assinantes.
 *
 *	@author	Daniel Ferreira
 *	@since	15/08/2005
 *
 *  Atualizado por: Bernardo Vergne Dias (totalização FGN)
 *  12/07/2007
 */
public class Consulta extends Persistencia
{

    //Construtores.

    /**
     *	Construtor da classe.
     *
     *	@param		idLog					Identificador de LOG.
     */
    public Consulta(long idLog)
    {
        super(idLog, Definicoes.CL_PROMOCAO_PERSISTENCIA_CONSULTA);
    }

    //Metodos de consulta relacionados as Promocoes.

    /**
     *	Retorna a promocao passada por parametro.
     *
     *	@param		idtPromocao				Identificador da promocao.
     *	@return		Informacoes da promocao consultada.
     *	@throws		Exception
     */
    public Promocao getPromocao(Integer idtPromocao) throws Exception
    {
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        return mapPromocao.getPromocao(idtPromocao);
    }

    /**
     *	Retorna uma lista com todas as promocoes cadastradas.
     *
     *	@return		Lista com as promocoes cadastradas.
     *	@throws		Exception
     */
    public Collection getPromocoes() throws Exception
    {
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        return mapPromocao.getListaPromocoes();
    }

    /**
     *	Retorna uma lista com as promocoes pertencentes a categoria passada por parametro.
     *
     *	@param		idtCategoria			Identificador da Categoria de Promocoes.
     *	@return		Lista com as promocoes cadastradas.
     *	@throws		Exception
     */
    public Collection getPromocoes(Integer idtCategoria) throws Exception
    {
        PromocaoCategoria categoria = this.getPromocaoCategoria(idtCategoria);
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        return mapPromocao.getPromocoes(categoria);
    }

    /**
     *	Retorna uma lista com as promocoes com prazo de cadastro de assinantes valido.
     *
     *	@param		datProcessamento		Data de processamento da consulta.
     *	@return		Lista com as promocoes cadastradas.
     *	@throws		Exception
     */
    public Collection getPromocoes(Date datProcessamento) throws Exception
    {
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        return mapPromocao.getPromocoes(datProcessamento);
    }

    /**
     *	Retorna a Categoria de Promocoes passada por parametro.
     *
     *	@param		idtCategoria			Identificador da Categoria de Promocoes.
     *	@return		Informacoes da Categoria de Promocoes consultada.
     *	@throws		Exception
     */
    public PromocaoCategoria getPromocaoCategoria(Integer idtCategoria) throws Exception
    {
        MapPromocaoCategoria mapCategoria = MapPromocaoCategoria.getInstance();
        return mapCategoria.getPromocaoCategoria(idtCategoria);
    }

    /**
     *	Retorna o objeto representando o relacionamento Promocao / Codigo Nacional.
     *
     *	@param		idtPromocao				Identificador da Promocao.
     *	@param		idtCodigoNacional		Codigo Nacional do assinante.
     *	@return		Relacionamento Promocao / Codigo Nacional.
     *	@throws		Exception
     */
    public PromocaoCodigoNacional getPromocaoCodigoNacional(Integer idtPromocao, Integer idtCodigoNacional) throws Exception
    {
        MapPromocaoCodigoNacional mapCodigoNacional = MapPromocaoCodigoNacional.getInstancia();
        return mapCodigoNacional.getPromocaoCodigoNacional(idtPromocao, idtCodigoNacional);
    }

    /**
     *	Retorna o mapeamento de dias de execucao para a promocao e data de entrada passadas por parametro.
     *
     *	@param		idtPromocao				Identificador da Promocao.
	 *	@param		tipExecucao				Tipo de execucao da promocao.
     *	@param		datEntradaPromocao		Data de entrada do assinante na Promocao.
     *	@return		Mapeamento Promocao / Dias de Execucao.
     *	@throws		Exception
     */
    public PromocaoDiaExecucao getPromocaoDiaExecucao(Integer idtPromocao, String tipExecucao, Date datEntradaPromocao) throws Exception
    {
        MapPromocaoDiaExecucao mapDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
        return mapDiaExecucao.getPromocaoDiaExecucao(idtPromocao, tipExecucao, datEntradaPromocao);
    }

    /**
     *	Retorna o mapeamento de dias de execucao para a promocao passada por parametro.
     *
     *	@param		idtPromocao				Identificador da Promocao.
	 *	@param		tipExecucao				Tipo de execucao da promocao.
     *	@return		Lista de mapeamento Promocao / Dias de Execucao.
     *	@throws		Exception
     */
    public Collection getPromocaoDiasExecucao(Integer idtPromocao, String tipExecucao) throws Exception
    {
        MapPromocaoDiaExecucao mapDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
        return mapDiaExecucao.getPromocaoDiasExecucao(idtPromocao, tipExecucao);
    }

    /**
     *	Retorna o mapeamento de dias de execucao para a promocao passada por parametro.
     *
     *	@param		idtPromocao				Identificador da Promocao.
     *	@param		datEntradaPromocao		Data de entrada do assinante na Promocao.
     *	@return		Lista de mapeamento Promocao / Dias de Execucao.
     *	@throws		Exception
     */
    public Collection getPromocaoDiasExecucao(Integer idtPromocao, Date datEntradaPromocao) throws Exception
    {
        MapPromocaoDiaExecucao mapDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
        return mapDiaExecucao.getPromocaoDiasExecucao(idtPromocao, datEntradaPromocao);
    }

    /**
     *	Retorna o mapeamento do ultimo dia de execucao de recarga para a promocao passada por parametro.
     *
     *	@param		idtPromocao				Identificador da Promocao.
	 *	@param		tipExecucao				Tipo de execucao da promocao.
     *	@return		Mapeamento Promocao / Dias de Execucao com o ultimo dia de execucao de recarga.
     *	@throws		Exception
     */
    public PromocaoDiaExecucao getPromocaoMaxDiaExecucaoRecarga(Integer idtPromocao, String tipExecucao) throws Exception
    {
        MapPromocaoDiaExecucao mapDiaExecucao = MapPromocaoDiaExecucao.getInstancia();
        return mapDiaExecucao.getMaxDiaExecucaoRecarga(idtPromocao, tipExecucao);
    }

	/**
	 *	Retorna uma lista de objetos PromocaoInfosSms associados a promocao.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@return		Lista de objetos PromocaoInfosSms associados a promocao.
     *	@throws		Exception
	 */
    public Collection getPromocaoInfosSms(Promocao promocao) throws Exception
    {
        MapPromocaoInfosSms mapInfosSms = MapPromocaoInfosSms.getInstance();
        return mapInfosSms.getPromocaoInfosSms(promocao);
    }

	/**
	 *	Retorna a lista de limites da promocao, um para cada tipo de bonificacao.
	 *
	 *	@param		promocao				Informacoes da promocao.
	 *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return		Objeto PromocaoLimite correspondente.
	 */
    public Collection getPromocaoLimites(Promocao promocao, Date datProcessamento) throws Exception
    {
        MapPromocaoLimite mapLimite = MapPromocaoLimite.getInstance();
        return mapLimite.getPromocaoLimites(promocao, datProcessamento);
    }

    /**
     *	Retorna as informacoes referentes ao limite dinamico da promocao.
     *
     *	@param		idtPromocao				Identificador da Promocao.
     *	@param		datProcessamento		Data de processamento da operacao.
	 *	@return		Informacoes referentes ao limite dinamico da promocao.
     *	@throws		Exception
     */
    public PromocaoLimiteDinamico getPromocaoLimiteDinamico(Integer idtPromocao, Date datProcessamento) throws Exception
    {
        MapPromocaoLimiteDinamico mapLimiteDinamico = MapPromocaoLimiteDinamico.getInstance();
        return mapLimiteDinamico.getPromocaoLimiteDinamico(idtPromocao, datProcessamento);
    }

    /**
     *	Retorna o objeto representando o relacionamento Promocao / Plano de Preco.
     *
     *	@param		idtPromocao				Identificador da Promocao.
     *	@param		idtPlanoPreco			Plano de preco do assinante.
     *	@return		Relacionamento Promocao / Plano de preco.
     *	@throws		Exception
     */
    public PromocaoPlanoPreco getPromocaoPlanoPreco(Integer idtPromocao, Integer idtPlanoPreco) throws Exception
    {
        MapPromocaoPlanoPreco mapPlanoPreco = MapPromocaoPlanoPreco.getInstancia();
        return mapPlanoPreco.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco);
    }

    /**
     *	Retorna uma lista de objetos PromocaoTipoTransacao associados a promocao. Cada um destes objetos
     *	representa um tipo de transacao de bonus concedido ao assinante.
     *
     *	@param		promocao				Informacoes da promocao.
	 *	@return		Lista de objetos PromocaoTipoTransacao associados promocao.
     *	@throws		Exception
     */
    public Collection getPromocaoTiposTransacao(Promocao promocao) throws Exception
    {
        MapPromocaoTipoTransacao mapTipoTransacao = MapPromocaoTipoTransacao.getInstance();
        return mapTipoTransacao.getPromocaoTiposTransacao(promocao);
    }

    //Metodos de consulta relacionados a Assinantes.

    /**
     *	Retorna objeto representando a promocao do assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		idtPromocao				Identificador da Promocao.
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes da promocao do assinante.
     *	@throws		Exception
     */
    public PromocaoAssinante getPromocaoAssinante(String idtMsisdn, Integer idtPromocao, Assinante assinante, PREPConexao conexaoPrep) throws Exception
    {
        PromocaoAssinante result = null;
        ResultSet registro = null;

        try
        {
            String sqlQuery =	"SELECT " +
            					"  IDT_PROMOCAO, " +
            					"  IDT_MSISDN, " +
            					"  DAT_EXECUCAO, " +
            					"  DAT_ENTRADA_PROMOCAO, " +
            					"  DAT_INICIO_ANALISE, " +
            					"  DAT_FIM_ANALISE, " +
            					"  IND_ISENTO_LIMITE, " +
            					"  IDT_STATUS, " +
            					"  DAT_ULTIMO_BONUS " +
            					"FROM " +
            					"  TBL_PRO_ASSINANTE " +
            					"WHERE " +
            					"  IDT_PROMOCAO = ? AND " +
            					"  IDT_MSISDN = ? ";
            Object[] parametros =
            {
            	idtPromocao,
            	idtMsisdn
            };
            registro = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(registro.next())
            {
                result = new PromocaoAssinante();
                result.setPromocao(this.getPromocao(idtPromocao));
                result.setIdtMsisdn(idtMsisdn);
                result.setDatExecucao(registro.getDate("DAT_EXECUCAO"));
                result.setDatEntradaPromocao(registro.getTimestamp("DAT_ENTRADA_PROMOCAO"));
                result.setDatInicioAnalise(registro.getDate("DAT_INICIO_ANALISE"));
                result.setDatFimAnalise(registro.getDate("DAT_FIM_ANALISE"));
                result.setIndIsentoLimite(registro.getInt("IND_ISENTO_LIMITE") != 0);
                result.setStatus(this.getPromocaoStatusAssinante(new Integer(registro.getInt("IDT_STATUS"))));
                result.setDatUltimoBonus(registro.getTimestamp("DAT_ULTIMO_BONUS"));
                result.setAssinante(assinante);
                result.setCodigoNacional(this.getPromocaoCodigoNacional(idtPromocao, new Integer(idtMsisdn.substring(2, 4))));
                result.setPlanoPreco((assinante != null) ? this.getPromocaoPlanoPreco(idtPromocao, new Integer(result.getAssinante().getPlanoPreco())) : null);
                result.setDiasExecucao(this.getPromocaoDiasExecucao(idtPromocao, result.getDatEntradaPromocao()));
                result.setInfosSms(this.getPromocaoInfosSms(this.getPromocao(idtPromocao)));
                result.setTiposTransacao(this.getPromocaoTiposTransacao(this.getPromocao(idtPromocao)));
            }
        }
        finally
        {
            if(registro != null)
                registro.close();
        }

        return result;
    }

    /**
     *	Retorna uma lista de promocoes do assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection getPromocoesAssinante(String idtMsisdn, Assinante assinante, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet registros = null;

        try
        {
            String sqlQuery =	"SELECT " +
            					"  IDT_PROMOCAO, " +
            					"  IDT_MSISDN, " +
            					"  DAT_EXECUCAO, " +
            					"  DAT_ENTRADA_PROMOCAO, " +
            					"  DAT_INICIO_ANALISE, " +
            					"  DAT_FIM_ANALISE, " +
            					"  IND_ISENTO_LIMITE, " +
            					"  IDT_STATUS," +
            					"  DAT_ULTIMO_BONUS " +
            					"FROM " +
            					"  TBL_PRO_ASSINANTE " +
            					"WHERE " +
            					"  IDT_MSISDN = ? ";

            Object[] parametros =
            {
            	idtMsisdn
            };

            registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(registros.next())
            {
                PromocaoAssinante pAssinante = new PromocaoAssinante();
                Integer idtPromocao = new Integer(registros.getInt("IDT_PROMOCAO"));
                pAssinante.setPromocao(this.getPromocao(idtPromocao));
                pAssinante.setIdtMsisdn(idtMsisdn);
                pAssinante.setDatExecucao(registros.getDate("DAT_EXECUCAO"));
                pAssinante.setDatEntradaPromocao(registros.getTimestamp("DAT_ENTRADA_PROMOCAO"));
                pAssinante.setDatInicioAnalise(registros.getDate("DAT_INICIO_ANALISE"));
                pAssinante.setDatFimAnalise(registros.getDate("DAT_FIM_ANALISE"));
                pAssinante.setIndIsentoLimite(registros.getInt("IND_ISENTO_LIMITE") != 0);
                pAssinante.setStatus(this.getPromocaoStatusAssinante(new Integer(registros.getInt("IDT_STATUS"))));
                pAssinante.setDatUltimoBonus(registros.getTimestamp("DAT_ULTIMO_BONUS"));
                pAssinante.setAssinante(assinante);
                pAssinante.setCodigoNacional(this.getPromocaoCodigoNacional(idtPromocao, new Integer(idtMsisdn.substring(2, 4))));
                pAssinante.setPlanoPreco((assinante != null) ? this.getPromocaoPlanoPreco(idtPromocao, new Integer(assinante.getPlanoPreco())) : null);
                pAssinante.setDiasExecucao(this.getPromocaoDiasExecucao(idtPromocao, pAssinante.getDatEntradaPromocao()));
                pAssinante.setInfosSms(this.getPromocaoInfosSms(this.getPromocao(idtPromocao)));
                pAssinante.setTiposTransacao(this.getPromocaoTiposTransacao(this.getPromocao(idtPromocao)));

                result.add(pAssinante);
            }
        }
        finally
        {
            if(registros != null)
                registros.close();
        }

        return result;
    }

    /**
     *	Retorna uma lista de promocoes do assinante da categoria passada por parametro.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		idtCategoria			Identificador da Categoria de Promocoes.
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection getPromocoesAssinante(String idtMsisdn, Integer idtCategoria, Assinante assinante, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet registros = null;

        try
        {
            String sqlQuery =	"SELECT " +
            					"  ASSINANTE.IDT_PROMOCAO, " +
            					"  ASSINANTE.IDT_MSISDN, " +
            					"  ASSINANTE.DAT_EXECUCAO, " +
            					"  ASSINANTE.DAT_ENTRADA_PROMOCAO, " +
            					"  ASSINANTE.DAT_INICIO_ANALISE, " +
            					"  ASSINANTE.DAT_FIM_ANALISE, " +
            					"  ASSINANTE.IND_ISENTO_LIMITE, " +
            					"  ASSINANTE.IDT_STATUS, " +
            					"  ASSINANTE.DAT_ULTIMO_BONUS " +
            					"FROM " +
            					"  TBL_PRO_ASSINANTE ASSINANTE, " +
            					"  TBL_PRO_PROMOCAO  PROMOCAO " +
            					"WHERE " +
            					"  ASSINANTE.IDT_PROMOCAO = PROMOCAO.IDT_PROMOCAO AND " +
            					"  PROMOCAO.IDT_CATEGORIA = ? AND " +
            					"  ASSINANTE.IDT_MSISDN = ? ";
            Object[] parametros =
            {
                idtCategoria,
            	idtMsisdn
            };
            registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(registros.next())
            {
                PromocaoAssinante pAssinante = new PromocaoAssinante();
                Integer idtPromocao = new Integer(registros.getInt("IDT_PROMOCAO"));
                pAssinante.setPromocao(this.getPromocao(idtPromocao));
                pAssinante.setIdtMsisdn(idtMsisdn);
                pAssinante.setDatExecucao(registros.getDate("DAT_EXECUCAO"));
                pAssinante.setDatEntradaPromocao(registros.getTimestamp("DAT_ENTRADA_PROMOCAO"));
                pAssinante.setDatInicioAnalise(registros.getDate("DAT_INICIO_ANALISE"));
                pAssinante.setDatFimAnalise(registros.getDate("DAT_FIM_ANALISE"));
                pAssinante.setIndIsentoLimite(registros.getInt("IND_ISENTO_LIMITE") != 0);
                pAssinante.setStatus(this.getPromocaoStatusAssinante(new Integer(registros.getInt("IDT_STATUS"))));
                pAssinante.setDatUltimoBonus(registros.getTimestamp("DAT_ULTIMO_BONUS"));
                pAssinante.setAssinante(assinante);
                pAssinante.setCodigoNacional(this.getPromocaoCodigoNacional(idtPromocao, new Integer(idtMsisdn.substring(2, 4))));
                pAssinante.setPlanoPreco((assinante != null) ? this.getPromocaoPlanoPreco(idtPromocao, new Integer(assinante.getPlanoPreco())) : null);
                pAssinante.setDiasExecucao(this.getPromocaoDiasExecucao(idtPromocao, pAssinante.getDatEntradaPromocao()));
                pAssinante.setInfosSms(this.getPromocaoInfosSms(this.getPromocao(idtPromocao)));
                pAssinante.setTiposTransacao(this.getPromocaoTiposTransacao(this.getPromocao(idtPromocao)));

                result.add(pAssinante);
            }
        }
        finally
        {
            if(registros != null)
                registros.close();
        }

        return result;
    }

    /**
     *	Retorna o objeto representando as informacoes do status da promocao do assinante.
     *
     *	@param		idtStatus				Identificador do status da promocao do assinante.
     *	@return		Informacoes do status da promocao do assinante.
     *	@throws		Exception
     */
    public PromocaoStatusAssinante getPromocaoStatusAssinante(Integer idtStatus) throws Exception
    {
        MapPromocaoStatusAssinante mapStatus = MapPromocaoStatusAssinante.getInstance();
        return mapStatus.getPromocaoStatusAssinante(idtStatus);
    }

    /**
     *	Efetua consulta por informacoes de assinante do GPP. A consulta e executada no banco de dados (importacao
     *	diaria) ou na plataforma Tecnomen (online) dependendo do parametro informado.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta. Obrigatorio no caso de consulta no banco.
     *	@return		Informacoes do assinante no Banco de Dados.
     *	@throws		Exception
     */
    public Assinante getAssinanteGPP(String msisdn, boolean consultarTec, PREPConexao conexaoPrep) throws Exception
    {
        if(consultarTec)
        {
       	    return this.getAssinanteGPPTecnomen(msisdn);
        }

        return this.getAssinanteGPPBanco(msisdn, conexaoPrep);
    }

    /**
     *	Efetua consulta na plataforma Tecnomen e retorna as informacoes do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@return		Informacoes do assinante na plataforma.
     *	@throws		Exception
     */
    public Assinante getAssinanteGPPTecnomen(String msisdn) throws Exception
    {
        ConsultaAssinante consulta = new ConsultaAssinante(super.logId);
        return consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
    }

    /**
     *	Efetua consulta no Banco de Dados e retorna as informacoes do assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes do assinante no Banco de Dados.
     *	@throws		Exception
     */
    public Assinante getAssinanteGPPBanco(String idtMsisdn, PREPConexao conexaoPrep) throws Exception
    {
        Assinante result = null;
        ResultSet resultAssinante = null;

        try
        {
            String sqlQuery =	"SELECT							" +
					            "	IDT_MSISDN             ,	" +
					            "	IDT_IMSI               ,	" +
					            "	DAT_ATIVACAO           ,	" +
					            "	VLR_SALDO_PRINCIPAL    ,	" +
					            "	VLR_SALDO_BONUS        ,	" +
					            "	VLR_SALDO_SM           ,	" +
					            "	VLR_SALDO_DADOS        ,	" +
					            "	DAT_EXPIRACAO_PRINCIPAL,	" +
					            "	DAT_EXPIRACAO_BONUS    ,	" +
					            "	DAT_EXPIRACAO_SM       ,	" +
					            "	DAT_EXPIRACAO_DADOS    ,	" +
					            "	IDT_STATUS             ,	" +
					            "	IDT_PLANO_PRECO        ,	" +
					            "	DES_FAMILY_FRIENDS     ,	" +
					            "	IDT_SUB_OPTIONS        ,	" +
					            "	DAT_ULTIMA_RECARGA     ,	" +
					            "	DAT_CONGELAMENTO       ,	" +
					            "	IDT_STATUS_SERVICO     ,	" +
					            "	IND_RETORNO_CICLO3      	" +
					            "FROM                       	" +
					            "	TBL_APR_ASSINANTE       	" +
					            "WHERE                      	" +
					            "	IDT_MSISDN = ?          	";
            Object[] parametros =
            {
            	idtMsisdn
            };
            resultAssinante = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(resultAssinante.next())
            {
                result = new Assinante();
                SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
                result.setMSISDN(idtMsisdn);
                result.setIMSI(resultAssinante.getString("IDT_IMSI"));
                result.setDataAtivacao(resultAssinante.getDate("DAT_ATIVACAO"));
                result.setSaldoCreditosPrincipal(resultAssinante.getDouble("VLR_SALDO_PRINCIPAL"));
                result.setSaldoCreditosBonus(resultAssinante.getDouble("VLR_SALDO_BONUS"));
                result.setSaldoCreditosSMS(resultAssinante.getDouble("VLR_SALDO_SM"));
                result.setSaldoCreditosDados(resultAssinante.getDouble("VLR_SALDO_DADOS"));
                Date datExpiracaoPrincipal = resultAssinante.getDate("DAT_EXPIRACAO_PRINCIPAL");
                result.setDataExpiracaoPrincipal((datExpiracaoPrincipal != null) ? conversorDate.format(datExpiracaoPrincipal) : null);
                Date datExpiracaoBonus = resultAssinante.getDate("DAT_EXPIRACAO_BONUS");
                result.setDataExpiracaoBonus((datExpiracaoBonus != null) ? conversorDate.format(datExpiracaoBonus) : null);
                Date datExpiracaoSm = resultAssinante.getDate("DAT_EXPIRACAO_SM");
                result.setDataExpiracaoSMS((datExpiracaoSm != null) ? conversorDate.format(datExpiracaoSm) : null);
                Date datExpiracaoDados = resultAssinante.getDate("DAT_EXPIRACAO_DADOS");
                result.setDataExpiracaoDados((datExpiracaoDados != null) ? conversorDate.format(datExpiracaoDados) : null);
                result.setStatusAssinante(resultAssinante.getShort("IDT_STATUS"));
                result.setPlanoPreco(resultAssinante.getShort("IDT_PLANO_PRECO"));
                result.setFriendFamily(resultAssinante.getString("DES_FAMILY_FRIENDS"));
                result.setStatusServico(resultAssinante.getShort("IDT_STATUS_SERVICO"));
                Object[] key = {new Integer(idtMsisdn.substring(2,4))};
                result.setCodigoNacional((CodigoNacional)(MapCodigoNacional.getInstance().get(key)));
            }
        }
        finally
        {
            if(resultAssinante != null)
                resultAssinante.close();
        }

        return result;
    }

    //Metodos de consulta relacionados ao Pula-Pula.

    /**
     *	Retorna o bonus Pula-Pula por minuto dos assinantes.
     *
     *	@param		idtCodigoNacional		Codigo Nacional do assinante.
     *	@param		idtPlanoPreco			Plano de preco do assinante.
     *	@param		datProcessamento		Data de processamento da operacao.
     *	@return		Bonus Pula-Pula por minuto do Codigo Nacional.
     *	@throws		Exception
     */
    public BonusPulaPula getBonusPulaPula(Integer idtCodigoNacional, Integer idtPlanoPreco, Date datProcessamento) throws Exception
    {
        MapBonusPulaPula mapBonus = MapBonusPulaPula.getInstancia();
        return mapBonus.getBonusPulaPula(idtCodigoNacional, idtPlanoPreco, datProcessamento);
    }

    /**
     *	Retorna as informacoes de totalização da promoção Fale de Graça à Noite.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		diaExecucao				Instancia de PromocaoDiaExecucao.
     *	@return		Informacoes de tempo de ligacoes recebidas.
     *	@throws		Exception
     */
    public TotalizacaoFaleGratis getTotalizacaoFGN(String idtMsisdn,
    		PromocaoDiaExecucao diaExecucao, PREPConexao conexaoPrep) throws Exception
    {
        TotalizacaoFaleGratis result = null;
        ResultSet resultTotalizacao = null;

        // Calcula o período corrente do assinante, de acordo com o dia de referencia na promocao FGN
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();

        // Verifica se o periodo do assinante é o mes passado
    	int numDiaExecucao = diaExecucao.getNumDiaExecucao().intValue();
    	int diaHoje = cal.get(Calendar.DAY_OF_MONTH);
    	if (numDiaExecucao > diaHoje) cal.add(Calendar.MONTH, -1);
    	cal.set(Calendar.DAY_OF_MONTH, numDiaExecucao);

    	String datMes = sdf1.format(cal.getTime());

        try
        {
            String sqlQuery =	"SELECT " +
								"  DAT_MES, " +
            					"  IDT_MSISDN, " +
            					"  NUM_SEGUNDOS_TOTAL, " +
            					"  DAT_RETIRADA_FGN " +
            					"FROM " +
            					"  TBL_PRO_TOTALIZACAO_FALEGRATIS " +
            					"WHERE " +
            					"  DAT_MES = ? AND " +
            					"  IDT_MSISDN = ? ";


            Object[] parametros = {datMes, idtMsisdn};

            resultTotalizacao = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(resultTotalizacao.next())
            {
                result = new TotalizacaoFaleGratis();
                result.setDatMes(datMes);
                Date datRetirada = resultTotalizacao.getTimestamp("DAT_RETIRADA_FGN");
                result.setDatRetiradaFGN(datRetirada);
                result.setMsisdn(idtMsisdn);
                long numSegundosTotal = resultTotalizacao.getLong("NUM_SEGUNDOS_TOTAL");
                result.setNumSegundos(numSegundosTotal);
            }
        }
        finally
        {
            if(resultTotalizacao != null)
                resultTotalizacao.close();
        }

        return result;
    }

    /**
     *	Retorna as informacoes de tempo de ligacoes bonificaveis pelo Pula-Pula recebidas pelo assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes de tempo de ligacoes recebidas.
     *	@throws		Exception
     */
    public TotalizacaoPulaPula getTotalizacaoPulaPula(String idtMsisdn, String datMes, PREPConexao conexaoPrep) throws Exception
    {
        TotalizacaoPulaPula result = null;
        ResultSet resultTotalizacao = null;

        try
        {
            String sqlQuery =	"SELECT " +
								"  DAT_MES, " +
            					"  IDT_MSISDN, " +
            					"  NUM_SEGUNDOS_TOTAL, " +
            					"  NUM_SEGUNDOS_FF, " +
            					"  NUM_SEGUNDOS_NAO_BONIFICADO, " +
            					"  NUM_SEGUNDOS_PLANO_NOTURNO, " +
            					"  NUM_SEGUNDOS_PLANO_DIURNO, " +
            					"  NUM_SEGUNDOS_DURAC_EXCEDIDA, " +
            					"  NUM_SEGUNDOS_EXPURGO_FRAUDE, " +
            					"  NUM_SEGUNDOS_ESTORNO_FRAUDE, " +
            					"  NUM_SEGUNDOS_TUP, " +
            					"  NUM_SEGUNDOS_AIGUALB, " +
            					"  NUM_SEGUNDOS_ATH, " +
            					"  NUM_SEGUNDOS_MOVEL_NAO_BRT, " +
            					"  NUM_SEGUNDOS_FALEGRATIS, " +
            					"  NUM_SEGUNDOS_BONUS, " +
            					"  NUM_SEGUNDOS_CT " +
            					"FROM " +
            					"  TBL_PRO_TOTALIZACAO_PULA_PULA " +
            					"WHERE " +
            					"  DAT_MES = ? AND " +
            					"  IDT_MSISDN = ? ";
            Object[] parametros =
            {
            	datMes,
            	idtMsisdn
            };
            resultTotalizacao = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(resultTotalizacao.next())
                result = getTotalizacao(resultTotalizacao);
        }
        finally
        {
            if(resultTotalizacao != null)
                resultTotalizacao.close();
        }

        return result;
    }

    /**
     *	Retorna as informacoes de tempo de ligacoes bonificaveis de todos os clientes do plano conta.
     *
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes de tempo de ligacoes recebidas.
     *	@throws		Exception
     */
    public ResultSet getTotalizacoesBateVolta(String datMes, PREPConexao conexaoPrep) throws Exception
    {
    	ResultSet resultTotalizacao = null;

        String sqlQuery =	"SELECT /*+ INDEX(TOTALIZACAO XPKTBL_PRO_TOTAL_PULA_PULA)*/ " +
							"  DAT_MES, " +
        					"  IDT_MSISDN, " +
        					"  NUM_SEGUNDOS_TOTAL, " +
        					"  NUM_SEGUNDOS_FF, " +
        					"  NUM_SEGUNDOS_NAO_BONIFICADO, " +
        					"  NUM_SEGUNDOS_PLANO_NOTURNO, " +
        					"  NUM_SEGUNDOS_PLANO_DIURNO, " +
        					"  NUM_SEGUNDOS_DURAC_EXCEDIDA, " +
        					"  NUM_SEGUNDOS_EXPURGO_FRAUDE, " +
        					"  NUM_SEGUNDOS_ESTORNO_FRAUDE, " +
        					"  NUM_SEGUNDOS_TUP, " +
        					"  NUM_SEGUNDOS_AIGUALB, " +
        					"  NUM_SEGUNDOS_ATH, " +
        					"  NUM_SEGUNDOS_MOVEL_NAO_BRT, " +
                            "  NUM_SEGUNDOS_FALEGRATIS, " +
                            "  NUM_SEGUNDOS_BONUS, " +
                            "  NUM_SEGUNDOS_CT, " +
        					"  VLR_RECARGAS " +
        					"FROM " +
        					"  TBL_PRO_TOTALIZACAO_PULA_PULA TOTALIZACAO " +
        					"WHERE " +
        					"  (IDT_MSISDN like '55__7%' OR IDT_MSISDN like '55__8%' OR IDT_MSISDN like '55__9%') AND " +
        					"  DAT_MES = ? AND " +
        					"  NOT EXISTS (SELECT 1 " +
        					"			   FROM " +
        					"				 TBL_APR_ASSINANTE ASSINANTE " +
        					"			   WHERE " +
        					"				 ASSINANTE.IDT_MSISDN = TOTALIZACAO.IDT_MSISDN) ";

        Object[] parametros = {datMes};
        resultTotalizacao = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

        return resultTotalizacao;
    }

    /**
     *  Retorna um objeto TotalizacaoPulaPula para um dado resultSet
     *
     * @param       resultTotalizacao ResultSet
     * @throws      Exception
     */
    public TotalizacaoPulaPula getTotalizacao(ResultSet resultTotalizacao) throws Exception
    {
        TotalizacaoPulaPula result = new TotalizacaoPulaPula();

        result.setDatMes(resultTotalizacao.getString("DAT_MES"));
        result.setIdtMsisdn(resultTotalizacao.getString("IDT_MSISDN"));
        long numSegundosTotal = resultTotalizacao.getLong("NUM_SEGUNDOS_TOTAL");
        result.setNumSegundosTotal(resultTotalizacao.wasNull() ? null : new Long(numSegundosTotal));
        long numSegundosFF = resultTotalizacao.getLong("NUM_SEGUNDOS_FF");
        result.setNumSegundosFF(resultTotalizacao.wasNull() ? null : new Long(numSegundosFF));
        long numSegundosNaoBonificado = resultTotalizacao.getLong("NUM_SEGUNDOS_NAO_BONIFICADO");
        result.setNumSegundosNaoBonificado(resultTotalizacao.wasNull() ? null : new Long(numSegundosNaoBonificado));
        long numSegundosNoturno = resultTotalizacao.getLong("NUM_SEGUNDOS_PLANO_NOTURNO");
        result.setNumSegundosNoturno(resultTotalizacao.wasNull() ? null : new Long(numSegundosNoturno));
        long numSegundosDiurno = resultTotalizacao.getLong("NUM_SEGUNDOS_PLANO_DIURNO");
        result.setNumSegundosDiurno(resultTotalizacao.wasNull() ? null : new Long(numSegundosDiurno));
        long numSegundosDuracExcedida = resultTotalizacao.getLong("NUM_SEGUNDOS_DURAC_EXCEDIDA");
        result.setNumSegundosDuracaoExcedida(resultTotalizacao.wasNull() ? null : new Long(numSegundosDuracExcedida));
        long numSegundosExpurgoFraude = resultTotalizacao.getLong("NUM_SEGUNDOS_EXPURGO_FRAUDE");
        result.setNumSegundosExpurgoFraude(resultTotalizacao.wasNull() ? null : new Long(numSegundosExpurgoFraude));
        long numSegundosEstornoFraude = resultTotalizacao.getLong("NUM_SEGUNDOS_ESTORNO_FRAUDE");
        result.setNumSegundosEstornoFraude(resultTotalizacao.wasNull() ? null : new Long(numSegundosEstornoFraude));
        long numSegundosTup = resultTotalizacao.getLong("NUM_SEGUNDOS_TUP");
        result.setNumSegundosTup(resultTotalizacao.wasNull() ? null : new Long(numSegundosTup));
        long numSegundosAIgualB = resultTotalizacao.getLong("NUM_SEGUNDOS_AIGUALB");
        result.setNumSegundosAIgualB(resultTotalizacao.wasNull() ? null : new Long(numSegundosAIgualB));
        long numSegundosATH = resultTotalizacao.getLong("NUM_SEGUNDOS_ATH");
        result.setNumSegundosATH(resultTotalizacao.wasNull() ? null : new Long(numSegundosATH));
        long numSegundosMovelNaoBrt = resultTotalizacao.getLong("NUM_SEGUNDOS_MOVEL_NAO_BRT");
        result.setNumSegundosMovelNaoBrt(resultTotalizacao.wasNull() ? null : new Long(numSegundosMovelNaoBrt));
        long numSegundosFaleGratis = resultTotalizacao.getLong("NUM_SEGUNDOS_FALEGRATIS");
        result.setNumSegundosFaleGratis(resultTotalizacao.wasNull() ? null : new Long(numSegundosFaleGratis));
        long numSegundosBonus = resultTotalizacao.getLong("NUM_SEGUNDOS_BONUS");
        result.setNumSegundosBonus(resultTotalizacao.wasNull() ? null : new Long(numSegundosBonus));
        long numSegundosCT = resultTotalizacao.getLong("NUM_SEGUNDOS_CT");
        result.setNumSegundosCT(resultTotalizacao.wasNull() ? null : new Long(numSegundosCT));

        return result;
    }

    /**
     *	Retorna as informacoes de recargas efetuadas pelo assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datMes					Mes de analise de ligacoes recebidas.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes referentes as recargas efetuadas pelo assinante.
     *	@throws		Exception
     */
    public TotalizacaoRecargas getTotalizacaoRecargas(String idtMsisdn, String datMes, PREPConexao conexaoPrep) throws Exception
    {
        TotalizacaoRecargas result = null;
        ResultSet registros = null;

        try
        {
            String sqlQuery =	"SELECT dat_mes, " +
            					"       idt_msisdn, " +
            					"       qtd_recargas, " +
            					"       vlr_pago " +
            					"  FROM tbl_pro_totalizacao_recargas " +
            					" WHERE dat_mes = ? " +
            					"   AND idt_msisdn = ? ";

            Object[] parametros =
            {
            	datMes,
            	idtMsisdn
            };

            registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(registros.next())
            {
                result = new TotalizacaoRecargas();
                result.setIdtMsisdn(idtMsisdn);
                result.setDatMes(datMes);
                result.setQtdRecargas(registros.getInt("qtd_recargas"));
                result.setVlrPago(registros.getDouble("vlr_pago"));
            }
        }
        finally
        {
            if(registros != null)
                registros.close();
        }

        return result;
    }

    /**
     *	Retorna o historico da execucao da promocao Pula-Pula para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datInicio				Data de inicio de analise.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista com o historico do Pula-Pula para o assinante.
     *	@throws		Exception
     */
    public Collection getHistoricoPulaPula(String idtMsisdn, Date datInicio, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet resultHistorico = null;

        try
        {
            String sqlQuery =
	            "SELECT                         " +
	            "	IDT_MSISDN         ,        " +
	            "	IDT_PROMOCAO       ,        " +
	            "	DAT_EXECUCAO       ,        " +
	            "	NOM_OPERADOR       ,        " +
	            "	IDT_MOTIVO         ,        " +
	            "	DES_STATUS_EXECUCAO,        " +
	            "	IDT_CODIGO_RETORNO ,        " +
	            "	VLR_CREDITO_BONUS           " +
	            "FROM                           " +
	            "	TBL_PRO_HISTORICO_PULA_PULA	" +
	            "WHERE                          " +
	            "	IDT_MSISDN		=	?   AND " +
	            "	DAT_EXECUCAO	>=	?       " +
	            "ORDER BY                       " +
	            "	DAT_EXECUCAO				";
            Object[] parametros =
            {
            	idtMsisdn,
            	new java.sql.Date(datInicio.getTime())
            };
            resultHistorico = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(resultHistorico.next())
            {
                HistoricoPulaPula historico = new HistoricoPulaPula();
                historico.setIdtMsisdn(idtMsisdn);
                int idtPromocao = resultHistorico.getInt("IDT_PROMOCAO");
                historico.setIdtPromocao(resultHistorico.wasNull() ? null : new Integer(idtPromocao));
                historico.setDatExecucao(resultHistorico.getTimestamp("DAT_EXECUCAO"));
                historico.setNomOperador(resultHistorico.getString("NOM_OPERADOR"));
                int idtMotivo = resultHistorico.getInt("IDT_MOTIVO");
                historico.setIdtMotivo(resultHistorico.wasNull() ? null : new Integer(idtMotivo));
                historico.setDesStatusExecucao(resultHistorico.getString("DES_STATUS_EXECUCAO"));
                int idtCodigoRetorno = resultHistorico.getInt("IDT_CODIGO_RETORNO");
                historico.setIdtCodigoRetorno(resultHistorico.wasNull() ? null : new Integer(idtCodigoRetorno));
                double vlrCreditoBonus = resultHistorico.getDouble("VLR_CREDITO_BONUS");
                historico.setVlrCreditoBonus(resultHistorico.wasNull() ? null : new Double(vlrCreditoBonus));

                result.add(historico);
            }
        }
        finally
        {
            if(resultHistorico != null)
                resultHistorico.close();
        }

        return result;
    }

    /**
     *	Retorna as informacoes referentes a origem do estorno de bonus Pula-Pula por fraude.
     *
     *	@param		idtOrigem				Identificador da origem do estorno.
     *	@return		Objeto com as informacoes referentes a origem do estorno.
     *	@throws		Exception
     */
    public PromocaoOrigemEstorno getPromocaoOrigemEstorno(String idtOrigem) throws Exception
    {
        MapPromocaoOrigemEstorno mapOrigem = MapPromocaoOrigemEstorno.getInstance();
        return (PromocaoOrigemEstorno)mapOrigem.get(new Object[]{idtOrigem});
    }

    /**
     *	Retorna os registros de ligacoes fraudulentas recebidas pelo assinante para processamento pelo Estorno de
     *	Bonus Pula-Pula por Fraude.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista com os registros do assinante para processamento do estorno.
     *	@throws		Exception
     */
    public Collection getInterfaceEstornoPulaPula(String idtMsisdn, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList	result		= new ArrayList();
        ResultSet	registros	= null;

        try
        {
            String sqlQuery =
        		"SELECT idt_lote " +
        		"       dat_referencia, " +
        		"       idt_msisdn, " +
        		"       idt_numero_origem, " +
        		"       idt_origem, " +
        		"       dat_cadastro, " +
        		"       dat_processamento, " +
        		"       idt_status_processamento, " +
        		"       idt_codigo_retorno " +
        		"  FROM tbl_int_estorno_pula_pula " +
        		" WHERE idt_msisdn = ? " +
        		"   AND (idt_status_processamento = ? " +
        		"    OR  (idt_status_processamento = ? " +
        		"   AND	  idt_codigo_retorno IN (?,?,?,?))) ";
            Object[] parametros =
            {
            	idtMsisdn,
		        Definicoes.IDT_PROCESSAMENTO_NOT_OK,
		        Definicoes.IDT_PROCESSAMENTO_ERRO,
		        new Integer(Definicoes.RET_ERRO_TECNICO),
		        new Integer(Definicoes.RET_CREDITO_INSUFICIENTE),
		        new Integer(Definicoes.RET_LIMITE_CREDITO_ULTRAPASSADO),
				new Integer(Definicoes.RET_PROMOCAO_ASSINANTE_NAO_ATIVO)
            };
            registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(registros.next())
            {
                InterfaceEstornoPulaPula estorno = new InterfaceEstornoPulaPula();
                estorno.setIdtLote(registros.getString("idt_lote"));
	        	estorno.setDatReferencia(registros.getDate("dat_referencia"));
	        	estorno.setIdtMsisdn(idtMsisdn);
	        	estorno.setIdtNumeroOrigem(registros.getString("idt_numero_origem"));
	        	estorno.setIdtOrigem(registros.getString("idt_origem"));
	        	estorno.setDatCadastro(registros.getTimestamp("dat_cadastro"));
	        	estorno.setDatProcessamento(registros.getTimestamp("dat_processamento"));
	        	estorno.setIdtStatusProcessamento(registros.getString("idt_status_processamento"));
	        	estorno.setIdtCodigoRetorno(registros.getInt("idt_codigo_retorno"));

                result.add(estorno);
            }
        }
        finally
        {
            if(registros != null)
                registros.close();
        }

        return result;
    }

    /**
     *	Retorna os registros de sumarizacao da execucao do processo de Estorno de Bonus Pula-Pula por Fraude para o
     *	assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		datInicio				Primeiro dia do intervalo de consulta, incluso.
     *	@param		datFim					Ultimo dia do intervalo de consulta, NAO incluso.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista com os registros de sumarizacao do estorno para o assinante.
     *	@throws		Exception
     */
    public Collection getPromocaoEstornoPulaPula(String idtMsisdn, Date datInicio, Date datFim, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList	result		= new ArrayList();
        ResultSet	registros	= null;

        try
        {
            if(datFim.compareTo(datInicio) <= 0)
            {
                return result;
            }

            String sqlQuery =
                "SELECT idt_lote, " +
                "       dat_referencia, " +
                "       idt_msisdn, " +
                "       idt_promocao, " +
                "       idt_numero_origem, " +
                "       idt_origem, " +
                "       dat_processamento, " +
                "       vlr_expurgo, " +
                "       vlr_expurgo_saturado, " +
                "       vlr_estorno, " +
                "       vlr_estorno_efetivo " +
                "  FROM tbl_pro_estorno_pula_pula " +
                " WHERE idt_msisdn = ? " +
                "   AND	dat_processamento >= to_date(?, 'DD/MM/YYYY HH24:MI:SS') " +
                "   AND dat_processamento <  to_date(?, 'DD/MM/YYYY HH24:MI:SS') " +
                " ORDER BY dat_processamento ";
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
            Object[] parametros =
            {
            	idtMsisdn,
            	conversorDate.format(datInicio),
            	conversorDate.format(datFim)
            };
            registros = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(registros.next())
            {
                PromocaoEstornoPulaPula estorno = new PromocaoEstornoPulaPula();
                estorno.setIdtLote(registros.getString("idt_lote"));
	        	estorno.setDatReferencia(registros.getDate("dat_referencia"));
	        	estorno.setIdtMsisdn(idtMsisdn);
	        	estorno.setIdtPromocao(registros.getInt("idt_promocao"));
	        	estorno.setIdtNumeroOrigem(registros.getString("idt_numero_origem"));
	        	estorno.setIdtOrigem(registros.getString("idt_origem"));
	        	estorno.setDatProcessamento(registros.getTimestamp("dat_processamento"));
	        	estorno.setVlrExpurgo(registros.getDouble("vlr_expurgo"));
	        	estorno.setVlrExpurgoSaturado(registros.getDouble("vlr_expurgo_saturado"));
	        	estorno.setVlrEstorno(registros.getDouble("vlr_estorno"));
	        	estorno.setVlrEstornoEfetivo(registros.getDouble("vlr_estorno_efetivo"));

                result.add(estorno);
            }
        }
        finally
        {
            if(registros != null)
                registros.close();
        }

        return result;
    }

    /**
     * Metodo....: getAssinantesContratadosPacoteDados
     * Descricao.: Consulta os assinantes que estao utilizando algum
     * 			   pacote de servicos
     *
     * @param  conexaoPrep	- Conexao com o Banco de Dados
     * @return resultado	- Resultado da consulta
     * @throws Exception
     */
    public ResultSet getAssinantesContratadosPacoteDados(PREPConexao conexaoPrep) throws Exception
    {
	    	String sql = "SELECT a.IDT_OFERTA, a.IDT_MSISDN 			 " +
	    				 "		,a.VLR_SALDO_TORPEDOS					 " +
	    				 "		,a.VLR_SALDO_DADOS 						 " +
	    				 "		,a.IND_SUSPENSO							 " +
	    				 "      ,a.DAT_CONTRATACAO						 " +
	    				 "		,b.IDT_TIPO_SALDO						 " +
	    				 "		,b.DAT_INICIO_OFERTA 					 " +
	    				 "		,b.DAT_FIM_OFERTA						 " +
	    				 "      ,c.IDT_PACOTE_DADOS						 " +
	    				 " 		,c.NUM_DIAS								 " +
	    				 "		,c.DES_VLR_PACOTE						 " +
	    				 "		,c.DES_PACOTE							 " +
	    				 "		,c.IND_HABILITADO						 " +
	    				 "  FROM TBL_PRO_ASS_OFERTA_PCT_DADOS a 		 " +
	    				 "		,TBL_PRO_OFERTA_PACOTE_DADOS b 			 " +
	    				 "      ,TBL_PRO_PACOTE_DADOS c         		 " +
	    				 " WHERE a.IDT_OFERTA = b.IDT_OFERTA 			 " +
	    				 "   AND a.DAT_CONTRATACAO IS NOT NULL 			 " +
	    				 "   AND a.DAT_RETIRADA_OFERTA IS NULL			 " +
	    				 "   AND b.IDT_PACOTE_DADOS = c.IDT_PACOTE_DADOS " ;

	    	return conexaoPrep.executaPreparedQuery(sql, null, this.getIdLog());
    }

    //Metodos de consulta de recargas para o assinante.

    /**
     *	Retorna o indicador de que o assinante ja fez recarga durante seu ciclo de vida.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Indicador de que o assinante ja fez recarga.
     *	@throws		Exception
     */
    public Integer getIndRecarga(String idtMsisdn, PREPConexao conexaoPrep) throws Exception
    {
        Integer result = null;
        ResultSet resultRecarga = null;

        try
        {
            String sqlQuery =	"SELECT " +
            					"  FNC_FEZ_RECARGA(?) AS IND_RECARGA " +
            					"FROM " +
            					"  DUAL ";
            Object[] parametros =
            {
            	idtMsisdn
            };
            resultRecarga = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            if(resultRecarga.next())
            {
                result = new Integer(resultRecarga.getInt("IND_RECARGA"));
            }
        }
        finally
        {
            if(resultRecarga != null)
                resultRecarga.close();
        }

        return result;
    }

    /**
     *	Retorna uma lista de recargas na Fila de Recargas para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		tipTransacao			Tipo de Transacao da recarga.
     *	@param		idtStatusProcessamento	Status de processamento das recargas na fila.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de recargas na fila de recargas.
     *	@throws		Exception
     */
    public Collection getFilaRecargas(String idtMsisdn, String tipTransacao, Integer idtStatusProcessamento, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet resultRecargas = null;

        try
        {
            String sqlQuery =	"SELECT 									" +
								"	F.IDT_MSISDN,							" +
								"	F.TIP_TRANSACAO, 						" +
								"	F.DAT_CADASTRO, 						" +
								"	F.DAT_EXECUCAO, 						" +
								"	F.DAT_PROCESSAMENTO, 					" +
								"	F.VLR_CREDITO_PRINCIPAL, 				" +
								"	F.VLR_CREDITO_PERIODICO, 				" +
								"	F.VLR_CREDITO_BONUS, 					" +
								"	F.VLR_CREDITO_SMS, 						" +
								"	F.VLR_CREDITO_GPRS, 					" +
								"	F.NUM_DIAS_EXP_PRINCIPAL, 				" +
								"	F.NUM_DIAS_EXP_PERIODICO, 				" +
								"	F.NUM_DIAS_EXP_BONUS, 					" +
								"	F.NUM_DIAS_EXP_SMS, 					" +
								"	F.NUM_DIAS_EXP_GPRS, 					" +
								"	F.DES_MENSAGEM, 						" +
								"	F.TIP_SMS, 								" +
								"	F.IND_ENVIA_SMS,						" +
								"	F.IDT_STATUS_PROCESSAMENTO, 			" +
								"	F.IDT_CODIGO_RETORNO, 					" +
								"	F.IND_ZERAR_SALDO_PERIODICO,			" +
								"	F.IND_ZERAR_SALDO_BONUS, 				" +
								"	F.IND_ZERAR_SALDO_SMS, 					" +
								"	F.IND_ZERAR_SALDO_GPRS 					" +
								"FROM 										" +
								"	TBL_REC_FILA_RECARGAS	F 				" +
								"WHERE 										" +
								"	F.IDT_MSISDN				=	?	AND	" +
								"	F.TIP_TRANSACAO				=	?	AND	" +
								"	F.IDT_STATUS_PROCESSAMENTO	=	?		";

            Object[] parametros =
            {
            	idtMsisdn,
            	tipTransacao,
            	idtStatusProcessamento
            };

            resultRecargas = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(resultRecargas.next())
            {
                result.add(getFilaRecarga(resultRecargas));
            }
        }
        finally
        {
            if(resultRecargas != null)
                resultRecargas.close();
        }

        return result;
    }

    /**
     *	Retorna uma lista de recargas na Fila de Recargas para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		tipTransacao			Tipo de Transacao da recarga.
     *	@param		idtStatusProcessamento	Status de processamento das recargas na fila.
     *	@param		datInicio				Primeiro dia do intervalo de consulta, incluso.
     *	@param		datFim					Ultimo dia do intervalo de consulta, NAO incluso.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de recargas na fila de recargas.
     *	@throws		Exception
     */
    public Collection getFilaRecargas(String idtMsisdn, String tipTransacao, Integer idtStatusProcessamento, Date datInicio, Date datFim, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet resultRecargas = null;

        try
        {
            if(datFim != null && datFim.compareTo(datInicio) <= 0)
            {
                return result;
            }

            String sqlQuery =	"SELECT 															" +
								"	F.IDT_MSISDN,													" +
								"	F.TIP_TRANSACAO, 												" +
								"	F.DAT_CADASTRO, 												" +
								"	F.DAT_EXECUCAO, 												" +
								"	F.DAT_PROCESSAMENTO, 											" +
								"	F.VLR_CREDITO_PRINCIPAL, 										" +
								"	F.VLR_CREDITO_PERIODICO, 										" +
								"	F.VLR_CREDITO_BONUS, 											" +
								"	F.VLR_CREDITO_SMS, 												" +
								"	F.VLR_CREDITO_GPRS, 											" +
								"	F.NUM_DIAS_EXP_PRINCIPAL, 										" +
								"	F.NUM_DIAS_EXP_PERIODICO, 										" +
								"	F.NUM_DIAS_EXP_BONUS, 											" +
								"	F.NUM_DIAS_EXP_SMS, 											" +
								"	F.NUM_DIAS_EXP_GPRS, 											" +
								"	F.DES_MENSAGEM, 												" +
								"	F.TIP_SMS, 														" +
								"	F.IND_ENVIA_SMS,												" +
								"	F.IDT_STATUS_PROCESSAMENTO, 									" +
								"	F.IDT_CODIGO_RETORNO, 											" +
								"	F.IND_ZERAR_SALDO_PERIODICO,									" +
								"	F.IND_ZERAR_SALDO_BONUS, 										" +
								"	F.IND_ZERAR_SALDO_SMS, 											" +
								"	F.IND_ZERAR_SALDO_GPRS 											" +
								"FROM 																" +
								"	TBL_REC_FILA_RECARGAS	F 										" +
								"WHERE 																" +
								"	F.IDT_MSISDN				=	?							AND	" +
								"	F.TIP_TRANSACAO				=	?							AND	" +
								"	F.IDT_STATUS_PROCESSAMENTO	=	?							AND	" +
								"	F.DAT_EXECUCAO				>=	TO_DATE(?, 'DD/MM/YYYY')	AND	" +
								"	F.DAT_EXECUCAO				<	TO_DATE(?, 'DD/MM/YYYY')		";
            Object[] parametros =
            {
            	idtMsisdn,
            	tipTransacao,
            	idtStatusProcessamento,
            	(datInicio	!= null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datInicio)	: null,
            	(datFim		!= null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datFim) 	: null
            };
            resultRecargas = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(resultRecargas.next())
            {
                result.add(getFilaRecarga(resultRecargas));
            }
        }
        finally
        {
            if(resultRecargas != null)
                resultRecargas.close();
        }

        return result;
    }

    /**
     *	Retorna o objeto contendo as informacoes.
     *	@param		rs						Result set contendo o registro.
     *	@return		recarga					Objeto representando o registro na tabela.
     *	@throws Exception
     */
    public FilaRecargas getFilaRecarga(ResultSet rs) throws Exception
    {
        FilaRecargas recarga = new FilaRecargas();
        recarga.setIdtMsisdn(rs.getString("IDT_MSISDN"));
        recarga.setTipTransacao(rs.getString("TIP_TRANSACAO"));
        recarga.setDatCadastro(rs.getTimestamp("DAT_CADASTRO"));
        recarga.setDatExecucao(rs.getTimestamp("DAT_EXECUCAO"));
        recarga.setDatProcessamento(rs.getTimestamp("DAT_PROCESSAMENTO"));
        double vlrCreditoPrincipal = rs.getDouble("VLR_CREDITO_PRINCIPAL");
        recarga.setVlrCreditoPrincipal(rs.wasNull() ? null : new Double(vlrCreditoPrincipal));
        double vlrCreditoPeriodico = rs.getDouble("VLR_CREDITO_PERIODICO");
        recarga.setVlrCreditoPeriodico(rs.wasNull() ? null : new Double(vlrCreditoPeriodico));
        double vlrCreditoBonus = rs.getDouble("VLR_CREDITO_BONUS");
        recarga.setVlrCreditoBonus(rs.wasNull() ? null : new Double(vlrCreditoBonus));
        double vlrCreditoSms = rs.getDouble("VLR_CREDITO_SMS");
        recarga.setVlrCreditoSms(rs.wasNull() ? null : new Double(vlrCreditoSms));
        double vlrCreditoGprs = rs.getDouble("VLR_CREDITO_GPRS");
        recarga.setVlrCreditoGprs(rs.wasNull() ? null : new Double(vlrCreditoGprs));
        int numDiasExpPrincipal = rs.getInt("NUM_DIAS_EXP_PRINCIPAL");
        recarga.setNumDiasExpPrincipal(rs.wasNull() ? null : new Integer(numDiasExpPrincipal));
        int numDiasExpPeriodico = rs.getInt("NUM_DIAS_EXP_PERIODICO");
        recarga.setNumDiasExpPeriodico(rs.wasNull() ? null : new Integer(numDiasExpPeriodico));
        int numDiasExpBonus = rs.getInt("NUM_DIAS_EXP_BONUS");
        recarga.setNumDiasExpBonus(rs.wasNull() ? null : new Integer(numDiasExpBonus));
        int numDiasExpSms = rs.getInt("NUM_DIAS_EXP_SMS");
        recarga.setNumDiasExpSms(rs.wasNull() ? null : new Integer(numDiasExpSms));
        int numDiasExpGprs = rs.getInt("NUM_DIAS_EXP_GPRS");
        recarga.setNumDiasExpGprs(rs.wasNull() ? null : new Integer(numDiasExpGprs));
        recarga.setDesMensagem(rs.getString("DES_MENSAGEM"));
        recarga.setTipSms(rs.getString("TIP_SMS"));
        int indEnviaSms = rs.getInt("IND_ENVIA_SMS");
        recarga.setIndEnviaSms(rs.wasNull() ? null : new Integer(indEnviaSms));
        recarga.setIdtStatusProcessamento(new Integer(rs.getInt("IDT_STATUS_PROCESSAMENTO")));
        int idtCodigoRetorno = rs.getInt("IDT_CODIGO_RETORNO");
        recarga.setIdtCodigoRetorno(rs.wasNull() ? null : new Integer(idtCodigoRetorno));
        int indZerarSaldoPeriodico = rs.getInt("IND_ZERAR_SALDO_PERIODICO");
        recarga.setIndZerarSaldoPeriodico(rs.wasNull() ? null : new Integer(indZerarSaldoPeriodico));
        int indZerarSaldoBonus = rs.getInt("IND_ZERAR_SALDO_BONUS");
        recarga.setIndZerarSaldoBonus(rs.wasNull() ? null : new Integer(indZerarSaldoBonus));
        int indZerarSaldoSms = rs.getInt("IND_ZERAR_SALDO_SMS");
        recarga.setIndZerarSaldoSms(rs.wasNull() ? null : new Integer(indZerarSaldoSms));
        int indZerarSaldoGprs = rs.getInt("IND_ZERAR_SALDO_GPRS");
        recarga.setIndZerarSaldoGprs(rs.wasNull() ? null : new Integer(indZerarSaldoGprs));

        return recarga;
    }

    /**
     *	Retorna uma lista de recargas executadas para o assinante.
     *
     *	@param		idtMsisdn				MSISDN do assinante.
     *	@param		tipTransacao			Tipo de Transacao da recarga.
     *	@param		datInicio				Primeiro dia do intervalo de consulta, incluso.
     *	@param		datFim					Ultimo dia do intervalo de consulta, NAO incluso.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de recargas.
     *	@throws		Exception
     */
    public Collection getRecargas(String idtMsisdn, String tipTransacao, Date datInicio, Date datFim, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet resultRecargas = null;

        try
        {
            if(datFim != null && datFim.compareTo(datInicio) <= 0)
            {
                return result;
            }

            String sqlQuery =	"SELECT                         						" +
								"	ID_RECARGA,                							" +
								"	IDT_MSISDN,                							" +
								"	IDT_PLANO_PRECO,           							" +
								"	TIP_TRANSACAO,             							" +
								"	ID_TIPO_CREDITO,           							" +
								"	ID_VALOR,                  							" +
					            "	DAT_RECARGA,               							" +
					            "	DAT_ORIGEM,              							" +
					            "	NOM_OPERADOR,              							" +
					            "	ID_TIPO_RECARGA,           							" +
					            "	IDT_CPF,                   							" +
					            "	NUM_HASH_CC,               							" +
					            "	IDT_LOJA,                  							" +
					            "	DAT_INCLUSAO,              							" +
					            "	DAT_CONTABIL,              							" +
					            "	IDT_TERMINAL,              							" +
					            "	IDT_NSU_INSTITUICAO,       							" +
					            "	ID_CANAL,                  							" +
					            "	ID_ORIGEM,                 							" +
					            "	ID_SISTEMA_ORIGEM,         							" +
					            "	VLR_PAGO,                  							" +
					            "	VLR_CREDITO_PRINCIPAL,     							" +
					            "	VLR_CREDITO_PERIODICO,     							" +
					            "	VLR_CREDITO_BONUS,         							" +
					            "	VLR_CREDITO_SMS,           							" +
					            "	VLR_CREDITO_GPRS,          							" +
					            "	VLR_SALDO_FINAL_PRINCIPAL,							" +
					            "	VLR_SALDO_FINAL_PERIODICO,							" +
					            "	VLR_SALDO_FINAL_BONUS,     							" +
					            "	VLR_SALDO_FINAL_SMS,       							" +
					            "	VLR_SALDO_FINAL_GPRS,      							" +
					            "	NUM_DIAS_EXP_PRINCIPAL,    							" +
					            "	NUM_DIAS_EXP_PERIODICO,    							" +
					            "	NUM_DIAS_EXP_BONUS,        							" +
					            "	NUM_DIAS_EXP_SMS,          							" +
					            "	NUM_DIAS_EXP_GPRS,         							" +
					            "	DES_OBSERVACAO             							" +
					            "FROM                           						" +
					            "	TBL_REC_RECARGAS  									" +
					            "WHERE                          						" +
					            "	IDT_MSISDN		=	?							AND	" +
					            "	TIP_TRANSACAO	=	?							AND	" +
					            "	DAT_ORIGEM		>=	TO_DATE(?, 'DD/MM/YYYY')	AND	" +
					            "	DAT_ORIGEM		<	TO_DATE(?, 'DD/MM/YYYY')		";
            Object[] parametros =
            {
            	idtMsisdn,
            	tipTransacao,
            	(datInicio	!= null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datInicio)	: null,
            	(datFim		!= null) ? new SimpleDateFormat(Definicoes.MASCARA_DATE).format(datFim)		: null
            };
            resultRecargas = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(resultRecargas.next())
            {
                Recarga recarga = new Recarga();
                recarga.setIdRecarga(new Long(resultRecargas.getLong("ID_RECARGA")));
                recarga.setIdtMsisdn(idtMsisdn);
                int idtPlanoPreco = resultRecargas.getInt("IDT_PLANO_PRECO");
                recarga.setIdtPlanoPreco((resultRecargas.wasNull()) ? null : new Integer(idtPlanoPreco));
                recarga.setTipTransacao(resultRecargas.getString("TIP_TRANSACAO"));
                recarga.setIdTipoCredito(resultRecargas.getString("ID_TIPO_CREDITO"));
                double idValor = resultRecargas.getDouble("ID_VALOR");
                recarga.setIdValor((resultRecargas.wasNull()) ? null : new Double(idValor));
                recarga.setDatRecarga(resultRecargas.getTimestamp("DAT_RECARGA"));
                recarga.setDatOrigem(resultRecargas.getTimestamp("DAT_ORIGEM"));
                recarga.setNomOperador(resultRecargas.getString("NOM_OPERADOR"));
                recarga.setIdTipoRecarga(resultRecargas.getString("ID_TIPO_RECARGA"));
                recarga.setIdtCpf(resultRecargas.getString("IDT_CPF"));
                recarga.setNumHashCc(resultRecargas.getString("NUM_HASH_CC"));
                recarga.setIdtLoja(resultRecargas.getString("IDT_LOJA"));
                recarga.setDatInclusao(resultRecargas.getTimestamp("DAT_INCLUSAO"));
                recarga.setDatContabil(resultRecargas.getString("DAT_CONTABIL"));
                recarga.setIdtTerminal(resultRecargas.getString("IDT_TERMINAL"));
                recarga.setIdtNsuInstituicao(resultRecargas.getString("IDT_NSU_INSTITUICAO"));
                recarga.setIdCanal(resultRecargas.getString("ID_CANAL"));
                recarga.setIdOrigem(resultRecargas.getString("ID_ORIGEM"));
                recarga.setIdSistemaOrigem(resultRecargas.getString("ID_SISTEMA_ORIGEM"));
                double vlrPago = resultRecargas.getDouble("VLR_PAGO");
                recarga.setVlrPago((resultRecargas.wasNull()) ? null : new Double(vlrPago));
                double vlrCreditoPrincipal = resultRecargas.getDouble("VLR_CREDITO_PRINCIPAL");
                recarga.setVlrCreditoPrincipal(resultRecargas.wasNull() ? null : new Double(vlrCreditoPrincipal));
                double vlrCreditoPeriodico = resultRecargas.getDouble("VLR_CREDITO_PERIODICO");
                recarga.setVlrCreditoPeriodico(resultRecargas.wasNull() ? null : new Double(vlrCreditoPeriodico));
                double vlrCreditoBonus = resultRecargas.getDouble("VLR_CREDITO_BONUS");
                recarga.setVlrCreditoBonus(resultRecargas.wasNull() ? null : new Double(vlrCreditoBonus));
                double vlrCreditoSms = resultRecargas.getDouble("VLR_CREDITO_SMS");
                recarga.setVlrCreditoSms(resultRecargas.wasNull() ? null : new Double(vlrCreditoSms));
                double vlrCreditoGprs = resultRecargas.getDouble("VLR_CREDITO_GPRS");
                recarga.setVlrCreditoGprs(resultRecargas.wasNull() ? null : new Double(vlrCreditoGprs));
                double vlrSaldoFinalPrincipal = resultRecargas.getDouble("VLR_SALDO_FINAL_PRINCIPAL");
                recarga.setVlrSaldoFinalPrincipal(resultRecargas.wasNull() ? null : new Double(vlrSaldoFinalPrincipal));
                double vlrSaldoFinalPeriodico = resultRecargas.getDouble("VLR_SALDO_FINAL_PERIODICO");
                recarga.setVlrSaldoFinalPeriodico(resultRecargas.wasNull() ? null : new Double(vlrSaldoFinalPeriodico));
                double vlrSaldoFinalBonus = resultRecargas.getDouble("VLR_SALDO_FINAL_BONUS");
                recarga.setVlrSaldoFinalBonus(resultRecargas.wasNull() ? null : new Double(vlrSaldoFinalBonus));
                double vlrSaldoFinalSms = resultRecargas.getDouble("VLR_SALDO_FINAL_SMS");
                recarga.setVlrSaldoFinalSms(resultRecargas.wasNull() ? null : new Double(vlrSaldoFinalSms));
                double vlrSaldoFinalGprs = resultRecargas.getDouble("VLR_SALDO_FINAL_GPRS");
                recarga.setVlrSaldoFinalGprs(resultRecargas.wasNull() ? null : new Double(vlrSaldoFinalGprs));
                int numDiasExpPrincipal = resultRecargas.getInt("NUM_DIAS_EXP_PRINCIPAL");
                recarga.setNumDiasExpPrincipal(resultRecargas.wasNull() ? null : new Integer(numDiasExpPrincipal));
                int numDiasExpPeriodico = resultRecargas.getInt("NUM_DIAS_EXP_PERIODICO");
                recarga.setNumDiasExpPeriodico(resultRecargas.wasNull() ? null : new Integer(numDiasExpPeriodico));
                int numDiasExpBonus = resultRecargas.getInt("NUM_DIAS_EXP_BONUS");
                recarga.setNumDiasExpBonus(resultRecargas.wasNull() ? null : new Integer(numDiasExpBonus));
                int numDiasExpSms = resultRecargas.getInt("NUM_DIAS_EXP_SMS");
                recarga.setNumDiasExpSms(resultRecargas.wasNull() ? null : new Integer(numDiasExpSms));
                int numDiasExpGprs = resultRecargas.getInt("NUM_DIAS_EXP_GPRS");
                recarga.setNumDiasExpGprs(resultRecargas.wasNull() ? null : new Integer(numDiasExpGprs));
                recarga.setDesObservacao(resultRecargas.getString("DES_OBSERVACAO"));

                result.add(recarga);
            }
        }
        finally
        {
            if(resultRecargas != null)
                resultRecargas.close();
        }

        return result;
    }

    /**
     *	Retorna a descricao do Tipo de Transacao.
     *
     *	@param		tipTransacao			Tipo de Transacao da recarga.
     *	@return		Descricao do tipo de transacao.
     *	@throws		Exception
     */
    public String getDescTipTransacao(String tipTransacao) throws Exception
    {
        MapRecOrigem mapOrigem = MapRecOrigem.getInstancia();
        return mapOrigem.getMapDescRecOrigem(tipTransacao);
    }

    /**
     *	Retorna a origem da recarga de acordo com o tipo de transacao passado por parametro.
     *
     *	@param		tipTransacao			Tipo de Transacao da recarga.
     *	@return		Origem da recarga.
     *	@throws		Exception
     */
    public OrigemRecarga getOrigemRecarga(String tipTransacao) throws Exception
    {
        MapRecOrigem mapOrigem = MapRecOrigem.getInstancia();
        return mapOrigem.getOrigemRecarga(tipTransacao);
    }

    //Metodos de consulta de planos de preco.

    /**
     *	Retorna as informacoes de espelhamento de acordo com o tipo de espelhamento, plano informado e plano do assinante.
     *
     *	@param		tipEspelhamento			Tipo de espelhamento do plano.
     *	@param		idtPlanoPreco			Plano de preco informado.
     *  @param      idtPlanoAssinante       Plano do assinante.
     *	@return		Informacoes de espelhamento de plano.
     *	@throws		Exception
     */
    public PlanoEspelho getPlanoEspelho(String tipEspelhamento, String idtPlanoPreco, String idtPlanoAssinante) throws Exception
    {
        MapPlanoEspelho mapPlano = MapPlanoEspelho.getInstancia();
        return mapPlano.getPlanoEspelho(tipEspelhamento, idtPlanoPreco, idtPlanoAssinante);
    }

    //Metodos de consulta de CDR's.

    /**
     *	Retorna os CDR's de ligacoes recebidas para execucao de estorno de bonus Pula-Pula por fraude.
     *
     *	@param		subId					MSISDN do assinante.
     *	@param		idtPromocao				Identificador da promocao do assinante.
     *	@param		callId					Identificador da ligacao.
     *	@param		datInicio				Primeiro dia do intervalo de consulta, incluso.
     *	@param		datFim					Ultimo dia do intervalo de consulta, NAO incluso.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista com os CDR's.
     *	@throws		Exception
     */
    public Collection getCdrsEstorno(String subId, Integer idtPromocao, String callId, Date datInicio, Date datFim, PREPConexao conexaoPrep) throws Exception
    {
        ArrayList result = new ArrayList();
        ResultSet resultCdrs = null;

        try
        {
            if(datFim != null && datFim.compareTo(datInicio) <= 0)
            {
                return result;
            }

            String sqlQuery =
            "SELECT /*+ index(CDR xpktbl_ger_cdr)*/ " +
            "       CDR.SEQUENCE_NUMBER             , " +
            "	    CDR.SUB_ID                      , " +
            "	    CDR.TIMESTAMP                   , " +
            "	    CDR.START_TIME                  , " +
            "	    CDR.CALL_DURATION               , " +
            "	    CDR.TRANSACTION_TYPE            , " +
            "	    CDR.CALL_ID                     , " +
            "	    CDR.FINAL_ACCOUNT_BALANCE       , " +
            "	    CDR.PROFILE_ID                  , " +
            "	    CDR.ORIG_CALLED_NUMBER          , " +
            "	    CDR.RATE_NAME                   , " +
            "	    CDR.DISCOUNT_TYPE               , " +
            "	    CDR.PERCENT_DISCOUNT_APPLIED    , " +
            "	    CDR.EXTERNAL_TRANSACTION_TYPE   , " +
            "	    CDR.OPERATOR_ID                 , " +
            "	    CDR.LOCATION_ID                 , " +
            "	    CDR.CELL_NAME                   , " +
            "	    CDR.CARRIER_PREFIX              , " +
            "	    CDR.AIRTIME_COST                , " +
            "	    CDR.INTERCONNECTION_COST        , " +
            "	    CDR.TAX                         , " +
            "	    CDR.DESTINATION_NAME            , " +
            "	    CDR.FF_DISCOUNT                 , " +
            "	    CDR.REDIRECTING_NUMBER          , " +
            "	    CDR.HOME_MSC_ADDRESS            , " +
            "	    CDR.VISITED_MSC_ADDRESS         , " +
            "	    CDR.ORIG_SUB_ID                 , " +
            "	    CDR.BONUS_TYPE                  , " +
            "	    CDR.BONUS_PERCENTAGE            , " +
            "	    CDR.BONUS_AMOUNT                , " +
            "	    CDR.SERVICE_KEY                 , " +
            "	    CDR.PEAK_TIME                   , " +
            "	    CDR.APPLICATION_TYPE            , " +
            "	    CDR.BILLING_EVENT_ID            , " +
            "	    CDR.SERVICE_PROVIDER_ID         , " +
            "	    CDR.MESSAGE_SIZE                , " +
            "	    CDR.PRIMARY_MESSAGE_CONTENT_TYPE, " +
            "	    CDR.MESSAGE_CLASS               , " +
            "	    CDR.RECIPIENT_ADDRESS           , " +
            "	    CDR.RECIPIENT_TYPE              , " +
            "	    CDR.NUM_CSP                     , " +
            "	    CDR.IDT_PLANO                   , " +
            "	    CDR.TIP_CHAMADA                 , " +
            "	    CDR.TIP_DESLOCAMENTO            , " +
            "	    CDR.IDT_MODULACAO               , " +
            "	    CDR.TIP_CDR                     , " +
            "	    CDR.IDT_OPERADORA_ORIGEM        , " +
            "	    CDR.IDT_LOCALIDADE_ORIGEM       , " +
            "	    CDR.IDT_OPERADORA_DESTINO       , " +
            "	    CDR.IDT_LOCALIDADE_DESTINO      , " +
            "	    CDR.IDT_HORA_TRANSACAO_CGW      , " +
            "	    CDR.IDT_DATA_TRANSACAO_CGW      , " +
            "	    CDR.ACCOUNT_STATUS              , " +
            "	    CDR.IDT_AREA                    , " +
            "	    CDR.DAT_IMPORTACAO_CDR          , " +
            "	    CDR.ACCOUNT_BALANCE_DELTA       , " +
            "	    CDR.BONUS_BALANCE               , " +
            "	    CDR.BONUS_BALANCE_DELTA         , " +
            "	    CDR.SM_BALANCE                  , " +
            "	    CDR.SM_BALANCE_DELTA            , " +
            "	    CDR.DATA_BALANCE                , " +
            "	    CDR.DATA_BALANCE_DELTA          , " +
            "	    CDR.CALL_TYPE_ID                , " +
            "	    CDR.COST " +
            "  FROM TBL_GER_CDR PARTITION(PC:DAT_MES) CDR, " +
            "       TBL_PRO_ASSINANTE                 ASSINANTE, " +
            "       TBL_PRO_DESCONTO_PULA_PULA        DESCONTO " +
            " WHERE CDR.SUB_ID = ASSINANTE.IDT_MSISDN " +
            "   AND CDR.FF_DISCOUNT = DESCONTO.ID_DESCONTO " +
            "   AND CDR.SUB_ID = ? " +
            "   AND CDR.CALL_ID IN (?, '0' || ?) " +
            "   AND CDR.TIMESTAMP >= TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') " +
            "   AND CDR.TIMESTAMP <  TO_DATE(?, 'DD/MM/YYYY HH24:MI:SS') " +
            "   AND ASSINANTE.IDT_PROMOCAO = ? " +
            "   AND DESCONTO.IND_ESTORNO <> ? " +
            "   AND CDR.TRANSACTION_TYPE IN " +
            "       (SELECT TRANSACTION_TYPE " +
            "          FROM TBL_PRO_TRANSACTION TRANSACTION " +
            "         WHERE TRANSACTION.IDT_PROMOCAO = ASSINANTE.IDT_PROMOCAO) " +
            "   AND CDR.TIP_CHAMADA IN " +
            "       (SELECT RATE_NAME " +
            "          FROM TBL_PRO_RATE_NAME RATE " +
            "         WHERE RATE.IDT_PROMOCAO = ASSINANTE.IDT_PROMOCAO) ";

            //Substituindo o padrao pelo mes de referencia para consulta em particao da TBL_GER_CDR.
            SimpleDateFormat conversorDatMes = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
            sqlQuery = sqlQuery.replaceAll(Definicoes.PATTERN_DAT_MES, conversorDatMes.format(datInicio));

            Object[] parametros =
            {
            	subId,
            	callId,
            	callId,
            	(datInicio	!= null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datInicio)	: null,
                (datFim		!= null) ? new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(datFim)	: null,
                idtPromocao,
                new Integer(1)
            };

           resultCdrs = conexaoPrep.executaPreparedQuery(sqlQuery, parametros, super.logId);

            while(resultCdrs.next())
            {
                ArquivoCDRDadosVoz cdr = new ArquivoCDRDadosVoz();
                cdr.setSequenceNumber(resultCdrs.getString("SEQUENCE_NUMBER"));
                cdr.setSubId(subId);
                cdr.setTimestamp(resultCdrs.getTimestamp("TIMESTAMP"));
                cdr.setStartTime(resultCdrs.getString("START_TIME"));
                cdr.setCallDuration(resultCdrs.getString("CALL_DURATION"));
                cdr.setTransactionType(resultCdrs.getString("TRANSACTION_TYPE"));
                cdr.setCallId(resultCdrs.getString("CALL_ID"));
                cdr.setFinalAccountBalance(resultCdrs.getString("FINAL_ACCOUNT_BALANCE"));
                cdr.setProfileId(resultCdrs.getString("PROFILE_ID"));
                cdr.setOrigCalledNumber(resultCdrs.getString("ORIG_CALLED_NUMBER"));
                cdr.setRateName(resultCdrs.getString("RATE_NAME"));
                cdr.setDiscountType(resultCdrs.getString("DISCOUNT_TYPE"));
                cdr.setPercenteDiscountApplied(resultCdrs.getString("PERCENT_DISCOUNT_APPLIED"));
                cdr.setExternalTransactionType(resultCdrs.getString("EXTERNAL_TRANSACTION_TYPE"));
                cdr.setOperatorId(resultCdrs.getString("OPERATOR_ID"));
                cdr.setLocationId(resultCdrs.getString("LOCATION_ID"));
                cdr.setCellName(resultCdrs.getString("CELL_NAME"));
                cdr.setCarrierPrefix(resultCdrs.getString("CARRIER_PREFIX"));
                cdr.setAirtimeCost(resultCdrs.getString("AIRTIME_COST"));
                cdr.setInterconnectionCost(resultCdrs.getString("INTERCONNECTION_COST"));
                cdr.setTax(resultCdrs.getString("TAX"));
                cdr.setDestinationName(resultCdrs.getString("DESTINATION_NAME"));
                cdr.setFfDiscount(resultCdrs.getString("FF_DISCOUNT"));
                cdr.setRedirectingNumber(resultCdrs.getString("REDIRECTING_NUMBER"));
                cdr.setHomeMscAddress(resultCdrs.getString("HOME_MSC_ADDRESS"));
                cdr.setVisitedMscAddress(resultCdrs.getString("VISITED_MSC_ADDRESS"));
                cdr.setOrigSubId(resultCdrs.getString("ORIG_SUB_ID"));
                cdr.setBonusType(resultCdrs.getString("BONUS_TYPE"));
                cdr.setBonusPercentage(resultCdrs.getString("BONUS_PERCENTAGE"));
                cdr.setBonusAmount(resultCdrs.getString("BONUS_AMOUNT"));
                cdr.setServiceKey(resultCdrs.getString("SERVICE_KEY"));
                cdr.setPeakTime(resultCdrs.getString("PEAK_TIME"));
                cdr.setApplicationType(resultCdrs.getString("APPLICATION_TYPE"));
                cdr.setBillingEventId(resultCdrs.getString("BILLING_EVENT_ID"));
                cdr.setServiceProviderId(resultCdrs.getString("SERVICE_PROVIDER_ID"));
                cdr.setMessageSize(resultCdrs.getString("MESSAGE_SIZE"));
                cdr.setPrimaryMessageContentType(resultCdrs.getString("PRIMARY_MESSAGE_CONTENT_TYPE"));
                cdr.setMessageClass(resultCdrs.getString("MESSAGE_CLASS"));
                cdr.setRecipientAddress(resultCdrs.getString("RECIPIENT_ADDRESS"));
                cdr.setRecipientType(resultCdrs.getString("RECIPIENT_TYPE"));
                cdr.setNumCsp(resultCdrs.getString("NUM_CSP"));
                cdr.setIdtPlano(resultCdrs.getString("IDT_PLANO"));
                cdr.setTipChamada(resultCdrs.getString("TIP_CHAMADA"));
                cdr.setTipDeslocamento(resultCdrs.getString("TIP_DESLOCAMENTO"));
                cdr.setIdtModulacao(resultCdrs.getString("IDT_MODULACAO"));
                cdr.setTipCdr(resultCdrs.getString("TIP_CDR"));
                cdr.setIdtOperadoraOrigem(resultCdrs.getString("IDT_OPERADORA_ORIGEM"));
                cdr.setIdtLocalidadeOrigem(resultCdrs.getString("IDT_LOCALIDADE_ORIGEM"));
                cdr.setIdtOperadoraDestino(resultCdrs.getString("IDT_OPERADORA_DESTINO"));
                cdr.setIdtLocalidadeDestino(resultCdrs.getString("IDT_LOCALIDADE_DESTINO"));
                cdr.setIdtHoraTransacaoCGW(resultCdrs.getString("IDT_HORA_TRANSACAO_CGW"));
                cdr.setIdtDataTransacaoCGW(resultCdrs.getString("IDT_DATA_TRANSACAO_CGW"));
                cdr.setAccountStatus(resultCdrs.getString("ACCOUNT_STATUS"));
                cdr.setIdtArea(resultCdrs.getString("IDT_AREA"));
                cdr.setDatImportacaoCdr(resultCdrs.getTimestamp("DAT_IMPORTACAO_CDR"));
                cdr.setAccountBalanceDelta(resultCdrs.getString("ACCOUNT_BALANCE_DELTA"));
                cdr.setBonusBalance(resultCdrs.getString("BONUS_BALANCE"));
                cdr.setBonusBalanceDelta(resultCdrs.getString("BONUS_BALANCE_DELTA"));
                cdr.setSmBalance(resultCdrs.getString("SM_BALANCE"));
                cdr.setSmBalanceDelta(resultCdrs.getString("SM_BALANCE_DELTA"));
                cdr.setDataBalance(resultCdrs.getString("DATA_BALANCE"));
                cdr.setDataBalanceDelta(resultCdrs.getString("DATA_BALANCE_DELTA"));
                cdr.setCallTypeId(resultCdrs.getString("CALL_TYPE_ID"));
                cdr.setCost(resultCdrs.getString("COST"));

                result.add(cdr);
            }
        }
        finally
        {
            if(resultCdrs != null)
                resultCdrs.close();
        }

        return result;
    }

    //Metodos de consulta relacionados as configuracoes do GPP.

    /**
     *	Retorna o registro de configuracao do GPP de acordo com o valor de chave.
     *
     *	@param		String					idConfiguracao				Identificador da configuracao do GPP.
     *	@return		ConfiguracaoGPP			result						Registro de configuracao do GPP correspondente.
     *	@throws		Exception
     */
    public ConfiguracaoGPP getConfiguracaoGPP(String idConfiguracao) throws Exception
    {
        MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
        return (ConfiguracaoGPP)mapConfiguracao.get(new Object[]{idConfiguracao});
    }
}