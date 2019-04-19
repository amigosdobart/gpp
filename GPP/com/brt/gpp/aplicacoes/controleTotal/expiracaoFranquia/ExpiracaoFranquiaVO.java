package com.brt.gpp.aplicacoes.controleTotal.expiracaoFranquia;

import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;

//Imports Java.

/**
 *	Classe que representa o Value Object do Produtor-Consumidor da 
 *  Expiracao Franquia Controle Total.
 * 
 *	@author	Magno Batista Corrêa
 *	@since	2007/05/21 (yyyy/mm/dd)
 */
public class ExpiracaoFranquiaVO
{
    // Informacoes de tabela
	private String              msisdn;                 // MSISDN do Assinante a ser processado
	private int                 idtStatusExpiracao;     // Estado do processamento da maquina de estados
    private Date                datInclusao;            // Data de inclusao do registro de recarga Expirada
    private Date                datProcessamento;       // Data do ultimo processamento para o registro
    private Date                datExpiracao;           // Data de expiracao da franquia
    private int                 qtdTentativas;          // Quantidade de tentativas com falha
    private double              vlrFranquia;            // Valor da franquia expirada
    
    // Outras informacoes importantes
    private Assinante           assinante;              // Objeto Assinante a ser processado
    private double              vlrFranquiaNaoExpirada; // Valor das franquias nao expiradas

    //Construtores.
	/**
     * @return o vlrFranquiaNaoExpirada
     */
    public double getVlrFranquiaNaoExpirada()
    {
        return vlrFranquiaNaoExpirada;
    }

    /**
     * @param vlrFranquiaNaoExpirada Ajusta o valor de vlrFranquiaNaoExpirada
     */
    public void setVlrFranquiaNaoExpirada(double vlrFranquiaNaoExpirada)
    {
        this.vlrFranquiaNaoExpirada = vlrFranquiaNaoExpirada;
    }

    /**
	 * Construtor Carregado
	 */
	public ExpiracaoFranquiaVO(String msisdn,
            int idtStatusExpiracao, Date datInclusao, Date datProcessamento,
            Date datExpiracao, int qtdTentativas, double vlrFranquia,
            Assinante assinante, double vlrFranquiaNaoExpirada)
    {
        this.msisdn                 = msisdn;
        this.idtStatusExpiracao     = idtStatusExpiracao;
        this.datInclusao            = datInclusao;
        this.datProcessamento       = datProcessamento;
        this.datExpiracao           = datExpiracao;
        this.qtdTentativas          = qtdTentativas;
        this.vlrFranquia            = vlrFranquia;
        this.assinante              = assinante;
    }
    
    /**
     * Construtor Para ser construido pelo Produtor.
     * Sem a informacao do assinante
     * @param msisdn
     * @param idtStatusExpiracao
     * @param datInclusao
     * @param datProcessamento
     * @param datExpiracao
     * @param qtdTentativas
     * @param vlrFranquia
     * @param vlrFranquiaNaoExpirada
     */
    public ExpiracaoFranquiaVO(String msisdn, int idtStatusExpiracao,
            Date datInclusao, Date datProcessamento, Date datExpiracao,
            int qtdTentativas, double vlrFranquia, double vlrFranquiaNaoExpirada)
    {
        this.msisdn                 = msisdn;
        this.idtStatusExpiracao     = idtStatusExpiracao;
        this.datInclusao            = datInclusao;
        this.datProcessamento       = datProcessamento;
        this.datExpiracao           = datExpiracao;
        this.qtdTentativas          = qtdTentativas;
        this.vlrFranquia            = vlrFranquia;
        this.vlrFranquiaNaoExpirada = vlrFranquiaNaoExpirada;
        this.assinante              = null;
    }

    /**
	 * Construtor da classe
	 */
	public ExpiracaoFranquiaVO()
	{
	    this.reset();
	}

	/**
	 *	Inicializa o objeto com valores vazios.
	 */
	public void reset()
	{
        this.msisdn                 = null;
        this.idtStatusExpiracao     = ExpiracaoFranquiaMaquinaDeEstados.AINDA_NAO_PENDENTE;
        this.datInclusao            = null;
        this.datProcessamento       = null;
        this.datExpiracao           = null;
        this.qtdTentativas          = 0;
        this.vlrFranquia            = 0.0;
        this.assinante              = null;
	}

    /**
     * @return o msisdn
     */
    public String getMsisdn()
    {
        return msisdn;
    }

    /**
     * @param msisdn Ajusta o valor de msisdn
     */
    public void setMsisdn(String msisdn)
    {
        this.msisdn = msisdn;
    }

    /**
     * @return o vlrFranquiaAtual
     */
    public double getVlrFranquiaAtual()
    {
        return assinante.getCreditosPeriodico();
    }

    /**
     * @return o datExpiracao
     */
    public Date getDatExpiracao()
    {
        return datExpiracao;
    }

    /**
     * @param datExpiracao Ajusta o valor de datExpiracao
     */
    public void setDatExpiracao(Date datExpiracao)
    {
        this.datExpiracao = datExpiracao;
    }

    /**
     * @return o datInclusao
     */
    public Date getDatInclusao()
    {
        return datInclusao;
    }

    /**
     * @param datInclusao Ajusta o valor de datInclusao
     */
    public void setDatInclusao(Date datInclusao)
    {
        this.datInclusao = datInclusao;
    }

    /**
     * @return o datProcessamento
     */
    public Date getDatProcessamento()
    {
        return datProcessamento;
    }

    /**
     * @param datProcessamento Ajusta o valor de datProcessamento
     */
    public void setDatProcessamento(Date datProcessamento)
    {
        this.datProcessamento = datProcessamento;
    }

    /**
     * @return o idtStatusExpiracao
     */
    public int getIdtStatusExpiracao()
    {
        return idtStatusExpiracao;
    }

    /**
     * @param idtStatusExpiracao Ajusta o valor de idtStatusExpiracao
     */
    public void setIdtStatusExpiracao(int idtStatusExpiracao)
    {
        this.idtStatusExpiracao = idtStatusExpiracao;
    }

    /**
     * @return o qtdTentativas
     */
    public int getQtdTentativas()
    {
        return qtdTentativas;
    }

    /**
     * @param qtdTentativas Ajusta o valor de qtdTentativas
     */
    public void setQtdTentativas(int qtdTentativas)
    {
        this.qtdTentativas = qtdTentativas;
    }

    /**
     * @return o vlrFranquia
     */
    public double getVlrFranquia()
    {
        return vlrFranquia;
    }

    /**
     * @param vlrFranquia Ajusta o valor de vlrFranquia
     */
    public void setVlrFranquia(double vlrFranquia)
    {
        this.vlrFranquia = vlrFranquia;
    }
    /**
     * @return o assinante
     */
    public Assinante getAssinante()
    {
        return assinante;
    }
    /**
     * @param assinante Ajusta o valor de assinante
     */
    public void setAssinante(Assinante assinante)
    {
        this.assinante = assinante;
    }
}