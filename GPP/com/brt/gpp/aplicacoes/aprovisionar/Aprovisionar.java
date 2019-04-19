//Definicao do Pacote
package com.brt.gpp.aplicacoes.aprovisionar;

// Arquivo de Imports de Gerentes do GPP 
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.ativacao.AtivacaoAssinante;
import com.brt.gpp.aplicacoes.aprovisionar.ativacao.AtivacaoAssinanteDAO;
import com.brt.gpp.aplicacoes.aprovisionar.ativacao.GPPAtivacaoAssinante;
import com.brt.gpp.aplicacoes.aprovisionar.dao.AprControleTotalDAO;
import com.brt.gpp.aplicacoes.aprovisionar.dao.AprEventosDAO;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.importacaoAssinantes.dao.AssinantePosPagoDAO;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinantePulaPula;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoInfosSms;
import com.brt.gpp.aplicacoes.promocao.gerenteFaleGratis.GerFaleGratisDAO;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.aplicacoes.recarregar.Recarregar;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.gppExceptions.GPPCorbaException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.mapeamentos.MapMotivoBloqueioAssinante;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.MapServicosAssinante;
import com.brt.gpp.comum.mapeamentos.MapStatusAssinante;
import com.brt.gpp.comum.mapeamentos.MapTarifaTrocaMSISDN;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.MapValorServico;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.AssinantePosPago;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoRetorno;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorServico;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 *  Este arquivo refere-se a classe Aprovisionar, responsavel pela implementacao das
 *  logicas de aprovisionamento de assinante.
 *
 *  <P> Versao: 1.0
 *  @author     Daniel Cintra Abib
 *  @since      28/02/2004
 *
 *  <P> Versao: 2.0 Atualizacao para Tecnomen 4.4.5
 *  @author     Daniel Ferreira
 *  @since      08/03/2007
 */
public final class Aprovisionar extends Aplicacoes
{
    GerentePoolTecnomen gerenteTecnomen = null;
    //GerentePoolBancoDados gerenteBancoDados = null; 
    
    /**
     * Metodo...: Aprovisionar
     * Descricao: Contrutor
     * @param   long    aIdProcesso     Id do Processo para efeitos de log
     */
    public Aprovisionar (long aIdProcesso)
    {
        super(aIdProcesso, Definicoes.CL_APROVISIONAR);
        
        // Obtem referencia ao gerente de conexoes a plataforma Tenomen
        this.gerenteTecnomen = GerentePoolTecnomen.getInstancia(aIdProcesso);

        // Referencia ja obtida do construtor da classe Aplicacoes
        // Obtem referencia ao gerente de conexoes ao banco de dados
        //this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(aIdProcesso);
    }
    
    /**
     * Metodo...: retornaDescPlanoPrec
     * Descricao: Retorna a descricao do plano de preço
     * @param   String  aPlanoPreco     - Codigo do Plano de Preco
     * @param   long    aIdProcesso     - Identificacao do Processo
     * @return  String                  - Descricao do plano de preço
     * @throws  GPPInternalErrorException 
     */
    public String retornaDescPlanoPreco (String aPlanoPreco) throws GPPInternalErrorException
    {
        String retorno = "";
        
        try
        {
            super.log(Definicoes.DEBUG,"retornaDescPlanoPreco","Inicio plano "+aPlanoPreco);
            
            // Seleciona a instancia do objeto de plano de preco em memoria
            MapPlanoPreco mapPlano = MapPlanoPreco.getInstancia();
            
            if (mapPlano != null)
            {
                // Seleciona descricao do plano
                retorno =  mapPlano.getMapDescPlanoPreco(aPlanoPreco);
            }
        }
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO,"retornaDescPlanoPreco","Excecao Interna GPP: " + e1);
            throw new GPPInternalErrorException ("Excecao Interna GPP:" + e1);           
        }
        finally
        {
            super.log(Definicoes.DEBUG,"retornaDescPlanoPreco","Fim");
        }
        return retorno;
    }

    /**
     * Metodo...: consultaStatus
     * Descricao: Valida o status do Assinante e do Servico
     * @param   short   aStatusAssinante    - Status do Assinante
     * @param   short   aStatusServico      - Status do Servico do Assinante
     * @return  short                       - Status valido (0) ou invalido (!0)  
     */
    public short consultaStatus (short aStatusAssinante, short aStatusServico)
    {
        short retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
        
        super.log(Definicoes.DEBUG,"consultaStatus","Inicio StatusAssinan "+aStatusAssinante);
        // Verifica se o assinante nao esta no status disconected
        //      if ( aStatusAssinante != Definicoes.DISCONNECTED)
        if ( aStatusAssinante != Definicoes.SHUT_DOWN)
        {
            // Verifica se o servico nao esta bloqueado
            if (aStatusServico != Definicoes.SERVICO_BLOQUEADO)
            {
                retorno = Definicoes.RET_STATUS_MSISDN_VALIDO;
            }
        }
        super.log(Definicoes.DEBUG,"consultaStatus","Fim");
        return retorno;
    }
    
    /**
     * Metodo...: consultaStatusFisrtTimeDisconnectedShutdown
     * Descricao: Valida o status do Assinante e do Servico
     * @param   short   aStatusAssinante    - Status do Assinante
     * @param   short   aStatusServico      - Status do Servico do Assinante
     * @return  short                       - Status valido ou invalido  
     */
    public short consultaStatusFisrtTimeDisconnectedShutdown (short aStatusAssinante, short aStatusServico)
    {
        short retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
        
        super.log(Definicoes.DEBUG,"consultaStatusFisrtTimeDisconnectedShutdown","Inicio STAT_ASSINANTE "+aStatusAssinante+" STAT_SERVICO "+aStatusServico);
        // Verifica se o assinante nao esta no status disconected
        if ( aStatusAssinante != Definicoes.DISCONNECTED)
        {
            // Verifica se o servico nao esta bloqueado
            if (aStatusServico != Definicoes.SERVICO_BLOQUEADO)
            {
                // Verifica se o assinante nao esta em shutdown
                if (aStatusAssinante != Definicoes.SHUT_DOWN)
                {
                    retorno = Definicoes.RET_STATUS_MSISDN_VALIDO;
                }
            }
        }
        super.log(Definicoes.DEBUG,"consultaStatusFisrtTimeDisconnectedShutdown","Fim");
        return retorno;
    }

    /**
     * Metodo...: consultaStatusDisconnectedShutdown
     * Descricao: Valida o status do Assinante e do Servico para Troca de Senha
     * @param   short   aStatusAssinante    - Status do Assinante
     * @param   short   aStatusServico      - Status do Servico do Assinante
     * @return short                        - Status valido ou invalido  
     */
    public short consultaStatusDisconnectedShutdown (short aStatusAssinante, short aStatusServico)
    {
        short retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
        
        super.log(Definicoes.DEBUG,"consultaStatusDisconnectedShutdown","Inicio STAT_ASSINANTE "+aStatusAssinante+" STAT_SERVICO "+aStatusServico);
        // Verifica se o assinante nao esta no status disconected
        if ( (aStatusAssinante != Definicoes.DISCONNECTED) && (aStatusAssinante != Definicoes.SHUT_DOWN) )
        {
            // Verifica se o servico nao esta bloqueado
            if (aStatusServico != Definicoes.SERVICO_BLOQUEADO)
            {
                retorno = Definicoes.RET_STATUS_MSISDN_VALIDO;
            }
        }
        super.log(Definicoes.DEBUG,"consultaStatusDisconnectedShutdown","Fim RET:"+retorno);
        return retorno;
    }
    
    /** 
     * Metodo...: creditoSuficiente
     * Descricao: Valida o credito suficiente para realizar uma troca (MSISDN, SimCard, etc.) ou bloqueio
     * @param   double  aSaldo  - Saldo atual do cliente
     * @param   double  aValor  - Valor que sera cobrado pelo servico
     * @return  short           - Sucesso(0) ou erro (diferente de 0)  
     */
    public short creditoSuficiente (double aSaldo, double aValor, short statusAssinante)
    {
        short retorno = Definicoes.RET_CREDITO_INSUFICIENTE;
        
        if ( (aSaldo >= aValor && statusAssinante != Definicoes.STATUS_RECHARGE_EXPIRED) ||
             aValor == 0)
            retorno = Definicoes.RET_OPERACAO_OK;
        
        return retorno;
    }

    /** 
     *  Verifica se um plano de preco e hibrido.
     *  @param      plano                   Plano de preco do assinante.
     *  @return     True se for hibrido e false caso contrario.
     */
    public boolean eHibrido(String plano)
    {
        return this.eHibrido(Short.parseShort(plano));
    }
    
    /** 
     * Metodo...: eHibrido
     * Descricao: Verifica se um plano de preco é hibrido
     * @param   String  aPlanoPreco     - Plano de Preco a ser consultado
     * @return  boolean                 - True se for hibrido e false caso contrario
     * @throws  GPPInternalErrorException  
     */
    public boolean eHibrido(short aPlanoPreco)
    {
        boolean retorno = false;
        
        try
        {       
            super.log(Definicoes.DEBUG,"eHibrido","Inicio Plano "+aPlanoPreco);
            //Seleciona a instancia do objeto de plano de preco em memoria
            MapPlanoPreco mapPlano = MapPlanoPreco.getInstancia();
        
            if (mapPlano != null)
            {
                // verifica se o plano e hibrido
                if ( (mapPlano.getMapHibrido(String.valueOf(aPlanoPreco))) != null)
                {
                    if ( (mapPlano.getMapHibrido(String.valueOf(aPlanoPreco))).equals("1") )                
                    {
                        retorno =  true;
                    }
                }
            }
        
            if (retorno)
            {
                super.log(Definicoes.DEBUG, "eHibrido", "Plano eh Hibrido.");
            }   
        }
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO, "eHibrido", "Excecao Interna GPP: " + e1);
        }
        finally
        {
            super.log(Definicoes.DEBUG,"eHibrido","Fim RET:"+retorno);
        }
        
        return retorno;
    }
    
    /** 
     * Metodo...: ativaAssinante
     * Descricao: Ativa Assinante na plataforma Tecnomen
     * @param   String  aMSISDN         - Numero do MSISDN que sera ativado
     * @param   String  aIMSI           - Numero do SimCard (IMSI)
     * @param   String  aPlanoPreco     - Plano do preco a ser ativado
     * @param   double  aCreditoInicial - Credito Inicial do assinante
     * @param   short   aIdioma         - Idioma do assinante
     * @return  short                   - RET_OPERACAO_OK se sucesso ou diferente em caso de falha
     * @throws  GPPTecnomenException    
     */
    public short ativaAssinante(String aMSISDN, String aIMSI, short aPlanoPreco, double aCreditoInicial, short aIdioma) throws GPPTecnomenException
    {   
        short retorno = Definicoes.RET_OPERACAO_OK;
    	GPPAtivacaoAssinante gPPAtivacaoAssinante = new GPPAtivacaoAssinante();
    	Assinante assinante = new Assinante();
    	assinante.setMSISDN(aMSISDN);
    	assinante.setIMSI(aIMSI);
    	assinante.setPlanoPreco(aPlanoPreco);
    	assinante.setSaldoCreditosPrincipal(aCreditoInicial);
    	assinante.setIdioma(aIdioma);
    	gPPAtivacaoAssinante.setAssinante(assinante);
    	retorno = this.ativarAssinante(gPPAtivacaoAssinante);       
        return retorno;
    }

    /** 
     *  Valida a ativacao do assinante.
     * 
     *  @param      msisdn                  MSISDN do assinante a ser ativado.
     *  @param      plano                   Plano de preco do assinante.
     *  @param      creditoInicial          Credito inicial de ativacao.
     *  @return     Codigo de retorno da operacao.
     */
    public short validarAtivacao(String msisdn, short plano, double creditoInicial)
    {
        PlanoPreco infoPlano = MapPlanoPreco.getInstancia().getPlanoPreco(plano);
        
        //Verificando se o plano do assinante existe.
        if(infoPlano == null)
            return Definicoes.RET_PLANO_PRECO_INVALIDO;
        //Verificando se o MSISDN do assinante e aceito pela categoria informada.
        if(!infoPlano.aceitaMsisdn(msisdn))
            return Definicoes.RET_INCOERENCIA_CATEGORIA_NUMERACAO;
        //Verificando se o credito inicial de ativacao e valido.
        if(creditoInicial < 0)
            return Definicoes.RET_VALOR_CREDITO_INVALIDO;
        try
        {
            MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
            double saldoMaximo = Double.parseDouble(mapConfiguracao.getMapValorConfiguracaoGPP("SALDO_MAXIMO"));
            if(creditoInicial > saldoMaximo)
                return Definicoes.RET_VALOR_CREDITO_INVALIDO;
        }
        catch(Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
            
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /**
     * Metodo...: removeAssinante
     * Descricao: Desativa / Remove Assinante na plataforma Tecnomen
     * @param   String  aMSISDN                         - Numero do MSISDN que sera removido
     * @param   short   aPlanoPreco                     - Plano de preco do assinante
     * @return  retornoDesativacaoAssinante             - Classe retornoDesaticacaoAssinante com campo retorno preenchido
     * @throws  GPPTecnomenException
     */
    public retornoDesativacaoAssinante removeAssinante(String aMSISDN)
    {   
        retornoDesativacaoAssinante retorno = new retornoDesativacaoAssinante();
        TecnomenAprovisionamento conexaoTecnomen = null;

        super.log(Definicoes.DEBUG, "removeAssinante", "Inicio - MSISDN: " + aMSISDN);
        
        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            retorno.codigoRetorno = conexaoTecnomen.desativarAssinante(aMSISDN);
        }
        catch(Exception e)
        {
            retorno.codigoRetorno = Definicoes.RET_ERRO_TECNICO;
            super.log(Definicoes.ERRO, "removeAssinante", "MSISDN: " + aMSISDN + " - Erro Interno: " + e);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.INFO, "removeAssinante", "MSISDN: " + aMSISDN + " - Codigo de retorno da operacao: " + retorno.codigoRetorno);
        }
        
        return retorno;
    }

    /** 
     * Metodo...: bloqueiaAssinante
     * Descricao: Bloqueia / Suspende um assinante na plataforma Tecnomen
     * @param   String  aMSISDN     - Numero do MSISDN que sera bloqueado
     * @return  short               - RET_OPERACAO_OK em caso de sucesso, diferente caso contrario
     * @throws  GPPTecnomenException
     */
    public short bloqueiaAssinante(String aMSISDN) throws GPPTecnomenException
    {   
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento conexaoTecnomen = null;
        super.log(Definicoes.DEBUG, "bloqueiaAssinante", "Inicio - MSISDN: " + aMSISDN);

        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            retorno = conexaoTecnomen.bloquearAssinante(aMSISDN);
        }
        catch(Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            super.log(Definicoes.ERRO, "bloqueiaAssinante", "MSISDN: " + aMSISDN + " - Erro Interno: " + e);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.INFO, "bloqueiaAssinante", "MSISDN: " + aMSISDN + " - Codigo de retorno da operacao: " + retorno);
        }
        
        return retorno;
    }

    /** 
     * Metodo...: desbloqueiaAssinante
     * Descricao: Desbloqueia um assinante na plataforma Tecnomen
     * @param   String  aMSISDN     - Numero do MSISDN que sera desbloqueado
     * @return  short               - RET_OPERACAO_OK em caso de sucesso, diferente caso contrario
     * @throws  GPPTecnomenException
     * @throws  GPPInternalErrorException
     */
    public short desbloqueiaAssinante(String aMSISDN) throws GPPTecnomenException, GPPInternalErrorException
    {   
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento conexaoTecnomen = null;
        
        super.log(Definicoes.DEBUG, "desbloqueiaAssinante", "Inicio - MSISDN: " + aMSISDN);
        
        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            retorno = conexaoTecnomen.desbloquearAssinante(aMSISDN);
        }
        catch(Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            super.log(Definicoes.ERRO, "desbloqueiaAssinante", "MSISDN: " + aMSISDN + " - Erro Interno: " + e);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.INFO, "desbloqueiaAssinante", "MSISDN: " + aMSISDN + " - Codigo de retorno da operacao: " + retorno);
        }
        
        return retorno;
    }

    /** 
     * Metodo...: consultaAssinante
     * Descricao: Consulta Assinante na plataforma Tecnomen
     * @param   String  aMSISDN     - Numero do MSISDN a consultar
     * @return  Assinante           - Retorna os dados do Assinante
     * @throws  GPPInternalErrorException
     */
    public Assinante consultaAssinante(String aMSISDN) throws GPPInternalErrorException
    {   
        TecnomenAprovisionamento conexaoTecnomen = null;
        Assinante retorno = null;

        super.log(Definicoes.DEBUG, "consultaAssinante", "Inicio - MSISDN: " + aMSISDN);
        
        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            retorno = conexaoTecnomen.consultarAssinante(aMSISDN);
            super.log(Definicoes.DEBUG, "consultaAssinante", "MSISDN: " + aMSISDN + " - Consulta realizada com sucesso");
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "consultaAssinante", "MSISDN: " + aMSISDN + " - Excecao: " + e);
            throw new GPPInternalErrorException("Excecao: " + e);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.DEBUG, "consultaAssinante", "Fim");
        }
        
        return retorno;
    }

    private void atualizaSaldoTrocaMSISDN(Assinante assinanteAntigo, String msisdnNovo,
    		TecnomenAprovisionamento conexaoTecnomen, Ajustar ajuste, ValoresRecarga valores, Calendar calTrocaMsisdn, String operador)
    {
        assinanteAntigo.setMSISDN(msisdnNovo);
        //Atualizando o status e as datas de expiracao do assinante.
        conexaoTecnomen.atualizarStatusAssinante(msisdnNovo,
                                                 assinanteAntigo.getStatusAssinante (),
                                                 assinanteAntigo.getDataExpPrincipal(),
                                                 assinanteAntigo.getDataExpPeriodico(),
                                                 assinanteAntigo.getDataExpBonus    (),
                                                 assinanteAntigo.getDataExpTorpedos (),
                                                 assinanteAntigo.getDataExpDados    ());
        
        //Realizando o ajuste de credito com os saldos antigos do assinante. Para garantir a coerencia 
        //das datas das operacoes de ajuste, e adicionado um segundo a data de execucao.
        calTrocaMsisdn.add(Calendar.SECOND, 1);
        ajuste.executarAjuste(msisdnNovo, 
                              Definicoes.AJUSTE_TROCA_MSISDN_SALDOS,
                              Definicoes.TIPO_CREDITO_REAIS,
                              valores,
                              Definicoes.TIPO_AJUSTE_CREDITO,
                              calTrocaMsisdn.getTime(),
                              Definicoes.SO_GPP,
                              operador,
                              assinanteAntigo,
                              null,
                              true,
                              null);
        

    }
    /** 
     *  Executa o processo de troca de MSISDN do assinante.
     *
     *  @param      msisdnAntigo            MSISDN do assinante.
     *  @param      msisdnNovo              MSISDN novo do assinante.
     *  @param      idTarifa                Identificador da tarifa de cobranca para a troca.
     *  @param      valorTarifa             Valor da tarifa de cobranca para a troca.
     *  @param      operador                Identificador do operador do processo.
     *  @return     Codigo de retorno da operacao.
     */
    public short trocarMsisdnAssinante(String msisdnAntigo, String msisdnNovo, String idTarifa, double valorTarifa, String operador)
    {
        short                       result          = Definicoes.RET_OPERACAO_OK;
        TecnomenAprovisionamento    conexaoTecnomen = null;
        PREPConexao                 conexaoPrep     = null;
        Ajustar                     ajuste          = new Ajustar(super.logId);
        Calendar                    calTrocaMsisdn  = Calendar.getInstance();
        
        super.log(Definicoes.DEBUG, "trocarMsisdnAssinante", "MSISDN antigo: " + msisdnAntigo + "MSISDN novo: " + msisdnNovo + " - Tarifa: " + valorTarifa);
        
        try
        {
            //Executando a consulta de assinante.
            ConsultaAssinante   consulta        = new ConsultaAssinante(super.logId);
            Assinante           assinanteAntigo = consulta.executaConsultaCompletaAssinanteTecnomen(msisdnAntigo);
            Assinante           assinanteNovo   = consulta.executaConsultaCompletaAssinanteTecnomen(msisdnNovo  );
            
            //Obtendo a conexao de aprovisionamento.
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.logId);
            //Obtendo a conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            result = this.validarTrocaMsisdnAssinante(assinanteAntigo, assinanteNovo, msisdnNovo);
            
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Verificando a necessidade de cobranca de taxa. Neste caso nao e necessario chamar a Tecnomen, uma 
                //vez que o assinante sera desativado e ativado novamente com o Saldo Principal atualizado.
                if(valorTarifa > 0)
                    result = ajuste.executarAjuste(msisdnAntigo, 
                                                   this.getTipoTransacao(idTarifa), 
                                                   Definicoes.TIPO_CREDITO_REAIS, 
                                                   valorTarifa, 
                                                   Definicoes.TIPO_AJUSTE_DEBITO, 
                                                   calTrocaMsisdn.getTime(), 
                                                   Definicoes.SO_GPP, 
                                                   operador, 
                                                   null, 
                                                   assinanteAntigo, 
                                                   null,
                                                   false);
                
                if(result != Definicoes.RET_OPERACAO_OK)
                    return result;
                
                //Executando o ajuste de desativacao para troca de MSISDN. Tambem nao necessita de chamar a tecnomen,
                //uma vez que o assinante sera desativado. Para garantir a coerencia das datas das operacoes de ajuste, 
                //e adicionado um segundo a data de execucao.
                calTrocaMsisdn.add(Calendar.SECOND, 1);
                ValoresRecarga valores = new ValoresRecarga(assinanteAntigo.getCreditosPrincipal(),
                                                            assinanteAntigo.getCreditosPeriodico(),
                                                            assinanteAntigo.getCreditosBonus    (),
                                                            assinanteAntigo.getCreditosSms      (),
                                                            assinanteAntigo.getCreditosDados    ());
                ajuste.executarAjuste(msisdnAntigo,
                                      Definicoes.AJUSTE_DESATIVACAO,
                                      Definicoes.TIPO_CREDITO_REAIS,
                                      valores,
                                      Definicoes.TIPO_AJUSTE_DEBITO,
                                      calTrocaMsisdn.getTime(),
                                      Definicoes.SO_GPP,
                                      operador,
                                      assinanteAntigo,
                                      null,
                                      false,
                                      null);
                //  Caso tenha conseguido criar o novo assinante na Plataforma,
                // Desativa o antigo e inicia a migracao dentro do Banco de Dados do GPP
                result = conexaoTecnomen.desativarAssinante(msisdnAntigo);
                if(result != Definicoes.RET_OPERACAO_OK)
                    return result;
                
                //Ativando o assinante com o novo MSISDN.
                assinanteAntigo.setMSISDN(msisdnNovo);
                result = conexaoTecnomen.ativarAssinante(assinanteAntigo);
/*                result = conexaoTecnomen.ativarAssinante(msisdnNovo, 
                                                         assinanteAntigo.getIMSI(), 
                                                         assinanteAntigo.getPlanoPreco(),
                                                         0.0,
                                                         assinanteAntigo.getIdioma(),
														 assinanteAntigo.getStatusPeriodico(),
														 assinanteAntigo.getDataExpPeriodico());
*/                if(result != Definicoes.RET_OPERACAO_OK)
                {
                    //Caso ocorreu problema com o aprovisionamento do novo assinante
					assinanteAntigo.setMSISDN(msisdnAntigo);
                	result = conexaoTecnomen.ativarAssinante(assinanteAntigo);
/*                	result = conexaoTecnomen.ativarAssinante(msisdnAntigo, 
                                                             assinanteAntigo.getIMSI(), 
                                                             assinanteAntigo.getPlanoPreco(),
                                                             0.0,
                                                             assinanteAntigo.getIdioma(),
    														 assinanteAntigo.getStatusPeriodico(),
    														 assinanteAntigo.getDataExpPeriodico());
*/                    if(result != Definicoes.RET_OPERACAO_OK)
                    	return result;  // Problema com a Tecnomen Grave
                    else //Se nao deu problema na reativacao, volta os saldos e status
                    	this.atualizaSaldoTrocaMSISDN(assinanteAntigo, msisdnAntigo, conexaoTecnomen, ajuste, valores, calTrocaMsisdn, operador);
                }
                else
                {
                	this.atualizaSaldoTrocaMSISDN(assinanteAntigo, msisdnNovo, conexaoTecnomen, ajuste, valores, calTrocaMsisdn, operador);
                    //Atualizando a tabela de comissionamento.
                    this.atualizarTabelaComissionamento(msisdnAntigo);
                    
                    // Duplicando o registro do periodo corrente da totalizacao FaleGratis
                    // para manter a correta utilizacao da referida Promocao. Isso evita que
                    // o assinante "zere" os minutos de FGN utilizados
                    // Data....: 19/10/2007
                    GerFaleGratisDAO faleGratis = new GerFaleGratisDAO(super.logId);
                    try
                    {
                    	faleGratis.duplicaTotalizacaoFaleGratis(msisdnNovo, msisdnAntigo, conexaoPrep);
                    }
                    catch (Exception e)
                    {
						super.log(Definicoes.WARN, "trocarMsisdnAssinante", 
								  "Erro na duplicacao da totalizacaoFaleGratis na troca de MSISDN antigo-" + msisdnAntigo + 
								  " para o novo-" + msisdnNovo + " (Ja existe totalizacao FGN para o novo MSISDN)");
					}
                    
                    //Atualizando as promocoes do assinante.
                    ControlePromocao controle = new ControlePromocao(super.logId); 
                    controle.trocaMsisdnPromocoesAssinante(msisdnNovo, 
                                                           msisdnAntigo, 
                                                           new Timestamp(calTrocaMsisdn.getTimeInMillis()),
                                                           operador,
                                                           Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_MSISDN,
                                                           conexaoPrep);
                    
                    //Atualizando as informacoes de plano hibrido.
                    if(this.eHibrido(assinanteAntigo.getPlanoPreco()))
                        this.atualizaHibrido(msisdnAntigo, msisdnNovo);
                    
                    if (MapPlanoPreco.getInstance().consultaCategoria(assinanteAntigo.getPlanoPreco()) == Definicoes.CATEGORIA_CT);
                    {
                    	// Apaga o registro do MSISDN antigo da tabela TBL_APR_CONTROLE_TOTAL
                    	AprControleTotalDAO dao = new AprControleTotalDAO(this.logId);
                    	dao.atualizarMSISDN(conexaoPrep, msisdnAntigo, msisdnNovo, this.logId);
                    }
                  
                }//Fim
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "trocarMsisdnAssinante", "MSISDN antigo: " + msisdnAntigo + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO; 
        }
        finally
        {
            this.gravaTrocaMSISDN(msisdnAntigo, msisdnNovo, idTarifa, valorTarifa, operador, result, calTrocaMsisdn.getTime(), conexaoPrep);
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            this.gerenteTecnomen.liberarConexao(conexaoTecnomen);
            super.log(Definicoes.INFO, "trocarMsisdnAssinante", "MSISDN: " + msisdnAntigo + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /** 
     *  Executa a validacao da troca de MSISDN de assinantes.
     *
     *  @param      assinanteAntigo         Informacoes do assinante antigo na plataforma.
     *  @param      assinanteNovo           Informacoes do assinante novo na plataforma. E necessario que nao haja 
     *                                      nenhum assinante ativo com o MSISDN novo.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarTrocaMsisdnAssinante(Assinante assinanteAntigo, Assinante assinanteNovo, String msisdnNovo)
    {
        //Validacao de MSISDN antigo ativo.
        if((assinanteAntigo == null) || (assinanteAntigo.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        //Validacao de MSISDN em status valido.
        if(assinanteAntigo.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        //Validacao de status periodico valido.
        if((assinanteAntigo.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) &&
           (assinanteAntigo.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NORMAL_USER))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        //Validacao de assinante bloqueado.
        if(assinanteAntigo.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        //Validacao de novo assinante inexistente.
        if((assinanteNovo != null) && (assinanteNovo.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_NOVO_MSISDN_JA_ATIVO;
        //Validacao da mascara do novo MSISDN.
        PlanoPreco infoPlano = MapPlanoPreco.getInstance().getPlanoPreco(assinanteAntigo.getPlanoPreco());
        if(!infoPlano.aceitaMsisdn(msisdnNovo))
            return Definicoes.RET_INCOERENCIA_CATEGORIA_NUMERACAO;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /** 
     * Metodo...: gravaTrocaMSISDN
     * Descricao: Grava dados em tabela referente à troca de MSISDN
     * @param   String      antigoMSISDN    - Antigo (atual) MSISDN 
     * @param   String      novoMSISDN      - Novo MSIDN
     * @param   String      idTarifa        - Identificacao do tipo de troca de MSISDN
     * @param   double      valorTarifa     - Valor cobrado pela troca de MSISDN
     * @param   String      aOperador       - Codigo do operador que esta realizando a ativacao
     * @param   String      aStatus         - Status da Operacao
     * @param   short       aRetOperacao    - Retorno da operacao
     * @param   Timestamp   dataTroca       - Data de troca do MSISDN
     * @return  boolean                     - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public void gravaTrocaMSISDN (String antigoMSISDN, String novoMSISDN, String idTarifa, double valorTarifa, String aOperador, short aRetOperacao, Date dataTroca, PREPConexao conexaoPrep)
    {
        if(conexaoPrep == null)
        {
            super.log(Definicoes.WARN, "gravaTrocaMSISDN", "MSISDN: " + antigoMSISDN + " - Conexao com o banco de dados nao disponivel.");
            return;
        }
        
        String comandoSQL =     
            "INSERT INTO TBL_APR_EVENTOS " +
            "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_ANTIGO_CAMPO, IDT_NOVO_CAMPO, VLR_CREDITO_INICIAL, IDT_MOTIVO, NOM_OPERADOR, DES_STATUS, COD_RETORNO) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";

        Object params[] = {new java.sql.Timestamp(dataTroca.getTime()), antigoMSISDN, Definicoes.TIPO_APR_TROCA_MSISDN, 
                   antigoMSISDN, novoMSISDN, new Double(valorTarifa), idTarifa, aOperador, 
                   (aRetOperacao == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO, 
                   new Integer(aRetOperacao)};

        try
        {
            conexaoPrep.executaPreparedUpdate(comandoSQL, params, super.logId);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaMSISDN", "MSISDN: " + antigoMSISDN + " - Excecao: " + e);
        }
        
        params[1] = novoMSISDN;
        
        try
        {
            conexaoPrep.executaPreparedUpdate(comandoSQL, params, super.logId);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaMSISDN", "MSISDN: " + antigoMSISDN + " - Excecao: " + e);
        }
    }
    
    /** 
     *  Executa a troca de plano do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      planoNovo               Novo plano de preco do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    public short trocarPlanoPreco(String msisdn, short planoNovo, double valorTarifa, double valorFranquia, String operador)
    {
        short                       result          = Definicoes.RET_OPERACAO_OK;
        short                       planoAntigo     = -1;
        short                       planoEfetivo    = -1;
        PREPConexao                 conexaoPrep     = null;
        Ajustar                     ajuste          = new Ajustar(super.logId);
        Calendar                    calTrocaPlano   = Calendar.getInstance();
        TecnomenAprovisionamento    conexaoTecnomen = null;
        ControlePromocao            controle        = new ControlePromocao(super.logId);

        super.log(Definicoes.DEBUG, "trocarPlanoPreco", "MSISDN: " + msisdn + " - Novo Plano: " + planoNovo);
        
        try
        {
            //Obtendo a conexao de aprovisionamento.
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.logId);
            //Obtendo a conexao com o banco de dados.
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
        
            //Executando a consulta de assinante.
            Assinante assinante = new ConsultaAssinante(super.logId).executaConsultaCompletaAssinanteTecnomen(msisdn);

            //Guardando o plano antigo do assinante.
            planoAntigo = (assinante != null) ? assinante.getPlanoPreco() : -1;
            
            //Calculado o plano efetivo do assinante.
            planoEfetivo = controle.calcularPlano(PlanoEspelho.TROCA_PLANO, 
                                                  planoNovo, 
                                                  planoAntigo);
            
            //Validando o assinante.
            result = this.validarTrocaPlanoAssinante(assinante, planoAntigo, planoEfetivo);
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Verificando a necessidade de cobranca de taxa.
                if(valorTarifa > 0)
                    result = ajuste.executarAjuste(msisdn, 
                                                   Definicoes.AJUSTE_TROCA_PLANO, 
                                                   Definicoes.TIPO_CREDITO_REAIS, 
                                                   valorTarifa, 
                                                   Definicoes.TIPO_AJUSTE_DEBITO, 
                                                   calTrocaPlano.getTime(), 
                                                   Definicoes.SO_GPP, 
                                                   operador, 
                                                   null, 
                                                   assinante, 
                                                   null,
                                                   true);

                if(result != Definicoes.RET_OPERACAO_OK)
                    return result;
                
                //Executando a troca de plano do assinante na plataforma.
                result = conexaoTecnomen.trocarPlanoPrecoAssinante(msisdn, planoEfetivo);
                
                //Executando o pos-processamento da troca de plano.
                if(result == Definicoes.RET_OPERACAO_OK)
                {
                    //Atribuindo o novo plano ao objeto.
                    assinante.setPlanoPreco(planoEfetivo);
                    
                    //Gerenciando as promocoes do assinante em funcao da troca de plano de preco.
                    controle.trocaPlanoPromocoesAssinante(msisdn, 
                                                          assinante, 
                                                          planoEfetivo,
                                                          planoAntigo,
                                                          calTrocaPlano.getTime(),
                                                          operador,
                                                          Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PLANO,
                                                          conexaoPrep);
                    
                    //Executando as acoes relacionadas a planos hibridos. A data do calendario e adiantada em um 
                    //segundo para garantir a coerencia da sucessao de eventos e recargas/ajustes que possam vir a 
                    //ocorrer durante o processo.
                    calTrocaPlano.add(Calendar.SECOND, 1);
                    this.gerenciarTrocaPlanoHibrido(assinante, 
                                                    planoAntigo, 
                                                    planoEfetivo, 
                                                    valorFranquia, 
                                                    calTrocaPlano.getTime(), 
                                                    operador, 
                                                    conexaoPrep, 
                                                    conexaoTecnomen);
                }
            }
        }
        catch (Exception e)
        {
            super.log(Definicoes.ERRO, "trocarPlanoPreco", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.gravaTrocaPlanoPreco(msisdn, planoAntigo, planoEfetivo, valorTarifa, operador, result, calTrocaPlano.getTime());
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
            super.log(Definicoes.INFO, "trocarPlanoPreco", "MSISDN: " + msisdn + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /** 
     *  Gerencia as operacoes de troca de assinantes para planos hibridos. O processo faz o pos-processamento da 
     *  troca de plano de assinantes onde ha planos hibrido envolvidos.
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      planoAntigo             Plano de preco antigodo assinante.
     *  @param      planoNovo               Novo plano de preco do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    private void gerenciarTrocaPlanoHibrido(Assinante assinante, 
                                            short planoAntigo, 
                                            short planoNovo, 
                                            double valorFranquia,
                                            Date dataExecucao,
                                            String operador,
                                            PREPConexao conexaoPrep, 
                                            TecnomenAprovisionamento conexaoTecnomen)
    {
        if(this.eHibrido(planoNovo))
        {
            ControlePromocao controle = new ControlePromocao(super.logId);
            
            //Inserindo as promocoes vigentes dos assinantes controle.
            controle.inserePromocoesAssinante(assinante.getMSISDN(), 
                                              dataExecucao, 
                                              dataExecucao, 
                                              operador, 
                                              Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PLANO,
                                              conexaoPrep);
            
            if(valorFranquia > 0.0)
                if(assinante.getStatusAssinante() == Definicoes.STATUS_FIRST_TIME_USER)
                    //Assinante ainda esta no status First Time User. Desta forma sera atualizado o seu Saldo 
                    //Principal com o novo valor de franquia. Isto nao e caracterizado como recarga nem ajuste, uma 
                    //vez que o registro do credito inicial de ativacao ainda nao foi inserido na tabela de recargas.
                    //Alem disso, o status do assinante nao pode ser alterado.
                    conexaoTecnomen.atualizarCreditoInicialAtivacao(assinante.getMSISDN(), valorFranquia);
                else
                {
                    //O assinante ja esta ativo. Desta forma, o valor de franquia e caracterizado como uma recarga.
                    //A recarga de franquia deve ser registrada na tabela e o Saldo Principal do assinante nao pode 
                    //ser substituido, uma vez que pode haver recargas de cartao. O identificador da recarga e o NSU 
                    //da instituicao correspondem a data no formato yyyyMMddHHmmss. Alem disso, o cadastro de plano 
                    //hibrido do assinante deve ser registrado.
                    SimpleDateFormat conversorDataHoraGPP = new SimpleDateFormat(Definicoes.MASCARA_DATA_HORA_GPP);
                    Recarregar recarga = new Recarregar(super.logId);
                    recarga.executarRecarga(assinante.getMSISDN(),
                                            Definicoes.RECARGA_FRANQUIA,
                                            conversorDataHoraGPP.format(dataExecucao),
                                            Definicoes.TIPO_CREDITO_REAIS,
                                            valorFranquia,
                                            dataExecucao,
                                            Definicoes.SO_GPP,
                                            Definicoes.GPP_OPERADOR,
                                            conversorDataHoraGPP.format(dataExecucao),
                                            null,
                                            null,
                                            null,
                                            null,
                                            null,
                                            null);
                    
                    //Caso o plano antigo do assinante nao seja hibrido, e necessario inserir seu registro na tabela 
                    //de plano hibrido uma vez que o assinante ja se encontra ativo e o processo de insercao nao ira 
                    //detecta-lo.
                    if(!this.eHibrido(planoAntigo))
                    {
                    	AtivacaoAssinanteDAO dao = new AtivacaoAssinanteDAO(conexaoPrep,super.logId);
                        try {
							dao.gravaHibrido(assinante.getMSISDN(), 
							        valorFranquia, 
							        assinante.getCreditosPrincipal(), 
							        dataExecucao);
						} catch (GPPInternalErrorException e) {
				            super.log(Definicoes.ERRO, "gravaHibrido", "Erro gravacao dos dados ativacao hibrido.");
						}
                    }                }
        }
        //O plano novo nao e hibrido, mas o plano antigo sim. Caso o assinante nao esteja no status First Time User,
        //e necessario retirar o registro de plano hibrido do assinante.
        else if(this.eHibrido(planoAntigo))
        {
            //18/10/2006 - Daniel Ferreira: No caso de Troca de Categoria Hibrido para Pre-Pago,
            //e necessario nivelar as datas de expiracao do saldo de Bonus, SMS e Dados de acordo
            //com a expiracao do saldo Principal. Isto porque existe a regra para assinantes
            //hibridos com Pula-Pula antigo de prorrogacao do saldo Principal em 60 dias contra 
            //30 dias para os outros saldos. Esta divergencia de expiracoes em planos Pre-Pago 
            //pode resultar em expiracao indevida de bonus Pula-Pula.
            if(conexaoTecnomen.atualizarStatusAssinante(assinante.getMSISDN(), 
                                                        assinante.getStatusAssinante(),
                                                        assinante.getDataExpPrincipal(),
                                                        assinante.getDataExpPrincipal(),
                                                        assinante.getDataExpPrincipal(),
                                                        assinante.getDataExpPrincipal(),
                                                        assinante.getDataExpPrincipal()) != Definicoes.RET_OPERACAO_OK)
                super.log(Definicoes.WARN, "gerenciarTrocaPlanoHibrido", "MSISDN: " + assinante.getMSISDN() + " - Nao foi possivel atualizar as datas de expiracao.");
            
            this.removeHibrido(assinante.getMSISDN());
        }
    }

    /** 
     *  Executa a validacao da troca de plano de preco de assinantes.
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      planoAntigo             Plano antigo do assinante.
     *  @param      planoNovo               Plano novo do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarTrocaPlanoAssinante(Assinante assinante, short planoAntigo, short planoNovo)
    {
        //Validacao de MSISDN antigo ativo.
        if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        //Validacao de MSISDN em status valido.
        if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        //Validacao de status periodico valido.
        if((assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) &&
           (assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NORMAL_USER))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        //Validacao de assinante bloqueado.
        if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        //Validacao do novo plano. Deve estar definido e deve permitir a troca para o novo plano.
        PlanoPreco  infoPlanoAntigo = MapPlanoPreco.getInstance().getPlanoPreco(planoAntigo);
        PlanoPreco  infoPlanoNovo   = MapPlanoPreco.getInstance().getPlanoPreco(planoNovo);
        if(infoPlanoNovo == null)
            return Definicoes.RET_PLANO_PRECO_INVALIDO;
        if(infoPlanoAntigo.equals(infoPlanoNovo))
            return Definicoes.RET_ALTERACAO_SEM_EFEITO;
        if(!infoPlanoAntigo.isTrocaPermitida(infoPlanoNovo))
            return Definicoes.RET_PLANO_PRECO_INVALIDO;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /** 
     * Metodo...: gravaTrocaPlanoPreco
     * Descricao: Grava dados em tabela referente troca de plano de precos
     * @param   String      aMSISDN         - Codigo do Assinante (MSISDN) 
     * @param   String      aAntigoPlano    - Antigo plano de precos
     * @param   String      aNovoPlano      - Novo plano de precos
     * @param   double      aValorMudanca   - Valor cobrado pela troca de plano de precos
     * @param   String      aOperador       - Codigo do operador que esta realizando a ativacao
     * @param   String      aStatus         - Status da Operacao
     * @param   short       aRetOperacao    - Retorno da operacao
     * @param   Timestamp   dataTroca       - Data de troca de plano preço
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
     public boolean gravaTrocaPlanoPreco(String aMSISDN, short aAntigoPlano, short aNovoPlano, double aValorMudanca, String aOperador, short aRetOperacao, Date dataTroca)
     {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "gravaTrocaPlanoPreco", "Inicio MSISDN "+aMSISDN);

        try
        {
             // Busca uma conexao de banco de dados     
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_ANTIGO_CAMPO, IDT_NOVO_CAMPO, VLR_CREDITO_INICIAL, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?,?,?,?)";
            
            Object params[] = {new Timestamp(dataTroca.getTime()), aMSISDN, Definicoes.TIPO_APR_TROCA_PLANO, 
                               String.valueOf(aAntigoPlano), String.valueOf(aNovoPlano), new Double(aValorMudanca), 
                               aOperador, (aRetOperacao == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO, 
                               new Integer(aRetOperacao)};
            
             if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
             {
                 super.log(Definicoes.DEBUG, "gravaTrocaPlanoPreco", "Dados troca de plano de precos gravados.");
                 retorno = true;
             }
             else
             {
                 super.log(Definicoes.INFO, "gravaTrocaPlanoPreco", "Erro gravacao dados da troca plano de precos.");
             }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaPlanoPreco", "Erro gravacao dados de troca de plano de precos.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaTrocaPlanoPreco", "Fim");
        }
        return retorno;
     }
     
    /**
     *  Troca o IMSI do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      imsi                    Novo IMSI do assinante.
     *  @param      valorTarifa             Valor da cobranca da operacao.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short trocarImsiAssinante(String msisdn, String imsi, double valorTarifa, String operador)
    {   
        short                       result          =   Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento    conexaoTecnomen = null;
        Ajustar                     ajuste          = new Ajustar(super.logId);
        Calendar                    calTrocaImsi    = Calendar.getInstance();
        String                      imsiAssinante   = null;
        
        super.log(Definicoes.DEBUG, "trocaSimCard", "MSISDN: " + msisdn + " - IMSI: " + imsi + " - Tarifa: " + valorTarifa);
        
        try
        {
            //Executando a consulta do assinante.
            Assinante assinante = new ConsultaAssinante(super.logId).executaConsultaCompletaAssinanteTecnomen(msisdn);
            
            //Obtendo o IMSI atual do assinante.
            imsiAssinante = (assinante != null) ? assinante.getIMSI() : null;
            
            //Validando o assinante.
            result = this.validarTrocaImsiAssinante(assinante);
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Executando a cobranca da troca, caso aplicavel.
                if(valorTarifa > 0.0)
                    result = ajuste.executarAjuste(msisdn, 
                                                   Definicoes.AJUSTE_TROCA_SIMCARD, 
                                                   Definicoes.TIPO_CREDITO_REAIS, 
                                                   valorTarifa, 
                                                   Definicoes.TIPO_AJUSTE_DEBITO, 
                                                   calTrocaImsi.getTime(), 
                                                   Definicoes.SO_GPP, 
                                                   operador, 
                                                   null, 
                                                   assinante, 
                                                   null, 
                                                   true);
                
                if(result != Definicoes.RET_OPERACAO_OK)
                    return result;
                
                //Assinante validado e taxa cobrada, se aplicavel. Executando a troca na plataforma.
                conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
                result = conexaoTecnomen.trocarSimCardAssinante(msisdn, imsi);
            }
        }
        catch (Exception e)
        {
            result = Definicoes.RET_ERRO_TECNICO;
            super.log(Definicoes.ERRO, "trocarImsiAssinante", "MSISDN: " + msisdn + " - Excecao: " + e);
        }
        finally
        {
            this.gerenteTecnomen.liberarConexao(conexaoTecnomen);
            this.gravaTrocaSimCard(msisdn, imsiAssinante, imsi, valorTarifa, operador, result);
            super.log(Definicoes.INFO, "trocarImsiAssinante", "MSISDN: " + msisdn + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }

    /** 
     *  Executa a validacao da troca de plano de preco de assinantes.
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarTrocaImsiAssinante(Assinante assinante)
    {
        //Validacao de MSISDN antigo ativo.
        if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        //Validacao de MSISDN em status valido.
        if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        //Validacao de status periodico valido.
        if((assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) &&
           (assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NORMAL_USER))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        //Validacao de assinante bloqueado.
        if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        //Validacao do plano. Deve verificar se o plano permite a troca de IMSI.
        PlanoPreco infoPlano = MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco());
        if(!infoPlano.possuiImsi())
            return Definicoes.RET_FUNC_INDISP_CATEGORIA;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /** 
     * Metodo...: gravaTrocaSimCard
     * Descricao: Grava dados em tabela referente troca de SimCard
     * @param   String  aMSISDN         - Codigo do Assinante (MSISDN) 
     * @param   String  aAntigoIMSI     - Antigo SimCard (IMSI)
     * @param   String  aNovoIMSI       - Novo SimCard (IMSI)
     * @param   double  aValorMudanca   - Valor cobrado pela troca de SimCard
     * @param   String  aOperador       - Codigo do operador que esta realizando a ativacao
     * @param   String  aStatus         - Status da Operacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaTrocaSimCard (String aMSISDN, String aAntigoIMSI, String aNovoIMSI, double aValorMudanca, String aOperador, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;

        super.log(Definicoes.INFO, "gravaTrocaSimCard", "Inicio MSISDN "+aMSISDN);

        try
        {
             // Busca uma conexao de banco de dados     
             conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
             // Obtem a data do sistema formatada
            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_ANTIGO_CAMPO, IDT_NOVO_CAMPO, VLR_CREDITO_INICIAL, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?,?,?,?)";
            
            Object params[] = {new Timestamp(Calendar.getInstance().getTimeInMillis()), 
                                aMSISDN, Definicoes.TIPO_APR_TROCA_SIMCARD, 
                               aAntigoIMSI, aNovoIMSI, new Double(aValorMudanca), aOperador, 
                               (aRetOperacao == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
                               new Integer(aRetOperacao)};
            
             if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
             {
                 super.log(Definicoes.DEBUG, "gravaTrocaSimCard", "Dados troca de MSISDN gravados.");
                 retorno = true;
             }
             else
             {
                 super.log(Definicoes.INFO, "gravaTrocaSimCard", "Erro gravacao dados da troca de SimCard.");
             }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaSimCard", "Erro gravacao dados de troca de SimCard.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaTrocaSimCard", "Fim");
        }
        return retorno;
     }
     
    /** 
     *  Atualiza a lista de Amigos Toda Hora (Family And Friends) do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      listaFF                 Lista de ATH do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    public short atualizarFF(String msisdn, String listaFF, String operador)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            //Obtendo as informacoes do assinante na plataforma.
            Assinante assinante = new ConsultaAssinante(super.logId).executaConsultaCompletaAssinanteTecnomen(msisdn);
            //Executando a atualizacao da lista.
            result = this.atualizarFF(assinante, listaFF);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "atualizarFF", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.gravaAtualizaFF(msisdn, listaFF, operador, result);
            super.log(Definicoes.INFO, "atualizarFF", "MSISDN: " + msisdn + " - Codigo de retorno da operacao: " + result);
        }
        
        return result;
    }
    
    /** 
     * Metodo...: atualizaFF
     * Descricao: Atualiza lista de Friends and Family do assinante
     * @param   Assinante   assinante   - Numero do MSISDN que atualizara a lista de FF
     * @param   String      aListaFF    - Lista de FF (MSISDN separados por ';')
     * @return  short               - RET_OPERACAO_OK em caso de sucesso, diferente caso contrario
     */
    public short atualizarFF(Assinante assinante, String listaFF)
    {   
        short                       result          = Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento    conexaoTecnomen = null;
        
        super.log(Definicoes.DEBUG, "atualizarFF", "MSISDN: " + ((assinante != null) ? assinante.getMSISDN() : null) + " - Lista ATH: " + listaFF);
        
        try
        {
            //Validando a atualizacao da lista efetiva.
            result = this.validarAtualizacaoFFAssinante(assinante, listaFF);
            
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
                result = conexaoTecnomen.atualizarFFAssinante(assinante.getMSISDN(), listaFF);
            }
        }
        catch (Exception e)
        {
            super.log(Definicoes.ERRO, "atualizarFF", "Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
        }
        
        return result;
    }

    /** 
     *  Executa a validacao da atualizacao da lista de Amigos Toda Hora (Family And Friends) do assinante.
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarAtualizacaoFFAssinante(Assinante assinante, String listaFF)
    {
        //Validacao de MSISDN antigo ativo.
        if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        //Validacao de MSISDN em status valido.
        if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        //Validacao de status periodico valido.
        if((assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NAO_APLICAVEL) &&
           (assinante.getStatusPeriodico() != Definicoes.STATUS_PERIODICO_NORMAL_USER))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        //Validacao de assinante bloqueado.
        if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        //Validacao do plano. Deve verificar se o plano permite a troca de IMSI.
        PlanoPreco infoPlano = MapPlanoPreco.getInstance().getPlanoPreco(assinante.getPlanoPreco());
        if(!infoPlano.possuiAth())
            return Definicoes.RET_FUNC_INDISP_CATEGORIA;
        //Validacao da lista de ATH.
        if((listaFF == null) || (listaFF.equals("")))
            return Definicoes.RET_ALTERACAO_SEM_EFEITO;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /** 
     * Metodo...: trocaSenha
     * Descricao: Troca Senha (password) do assinante
     * @param   String  aMSISDN         - Numero do assinante (MSISDN) 
     * @param   String  aNovaSenha      - Nova senha (password - 8 digitos separados por ifem)
     * @return  short                   - RET_OPERACAO_OK em caso de sucesso, diferente caso contrario
     * @throws  GPPTecnomenException
     * @throws  GPPInternalErrorException
     */
    public short trocaSenha(String aMSISDN, String aNovaSenha) throws GPPInternalErrorException
    {   
        short retorno = Definicoes.RET_SENHA_ASSINANTE_INVALIDA;

        super.log(Definicoes.DEBUG, "trocaSenha", "Inicio - MSISDN: " + aMSISDN);
        
        try
        {
            //Decodificando e atualizando a senha.
            retorno = this.resetSenha(aMSISDN, this.decodificaSenha(aNovaSenha));
            
            super.log(Definicoes.INFO, "trocaSenha", "MSISDN: " + aMSISDN + " - Codigo de retorno da operacao: " + retorno);
        }
        catch(NumberFormatException e)
        {
            retorno = Definicoes.RET_SENHA_ASSINANTE_INVALIDA;
            super.log(Definicoes.ERRO, "trocaSenha", "MSISDN: " + aMSISDN + " - Formato de senha invalido.");
        }
        catch(Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            super.log(Definicoes.ERRO, "trocaSenha",  "MSISDN: " + aMSISDN + " - Erro Interno: " + e);
        }
        finally
        {
            super.log(Definicoes.DEBUG, "trocaSenha", "Fim");
        }
        
        return retorno;
    }
    
    /** 
     * Metodo...: resetSenha
     * Descricao: Troca Senha (password) do assinante
     * @param   String  aMSISDN         - Numero do assinante (MSISDN) 
     * @param   String  aNovaSenha      - Nova senha (password)
     * @return  short                   - RET_OPERACAO_OK em caso de sucesso, diferente caso contrario
     * @throws  GPPTecnomenException
     * @throws  GPPInternalErrorException
     */
    public short resetSenha(String aMSISDN, String aNovaSenha) throws GPPInternalErrorException
    {   
        TecnomenAprovisionamento conexaoTecnomen = null;
        short retorno = Definicoes.RET_SENHA_ASSINANTE_INVALIDA;
        
        super.log(Definicoes.DEBUG, "resetSenha", "Incio - MSISDN: " + aMSISDN);
        
        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());

            if (aNovaSenha != null) 
                retorno = conexaoTecnomen.trocarSenhaAssinante(aMSISDN, aNovaSenha);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.DEBUG, "resetSenha", "Fim");
        }
        
        return retorno;
    }

    /** 
     * Metodo...: consultaDetalhadaAssinante
     * Descricao: Consulta detalhada do assinante
     * @param   String  aMSISDN     - Numero do MSISDN a consultar
     * @return  Assinante           - Retorna os dados do Assinante
     * @throws  GPPTecnomenException
     * @throws  GPPInternalErrorException
     */
    public Assinante consultaDetalhadaAssinante (String aMSISDN) throws GPPInternalErrorException
    {   
        TecnomenAprovisionamento conexaoTecnomen = null;
        Assinante retorno = null;

        super.log(Definicoes.DEBUG, "consultaDetalhadaAssinante", "Inicio - MSISDN: " + aMSISDN);
        
        try
        {
            conexaoTecnomen = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
            retorno = conexaoTecnomen.consultarAssinante(aMSISDN);
        }
        catch(GPPTecnomenException e)
        {
            throw new GPPInternalErrorException("Excecao Tecnomen:" + e);
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
            super.log(Definicoes.DEBUG, "consultaDetalhadaAssinante", "Fim");
        }
        
        return retorno;
    }

    /**
     * Metodo...: ativacaoCancelamentoServico
     * Descricao: Recebe a notificacao de um cadastro ou cancelamento de um servico e armazena em banco de dados
     * @param   MSISDN          - Numero do assinante
     * @param   idServico       - Identificador do servico
     * @param   acao            - Acao a ser efetuada (ativacao 0 - cancelamento 1)
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * 
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */ 
    public short ativacaoCancelamentoServico (String aMSISDN, String aIdServico, short acao)
    {   
        short retorno = Definicoes.RET_ERRO_TECNICO;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "ativacaoCancelamentoServico", "Inicio da ativacao ou cancelamento de servico para o MSISDN " + aMSISDN );

        try
        {
            String comandoSQL = null;
            Object[] params = null;
            
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            // Verifica se acao de incluir ou cancelar o servico
            if (acao == Definicoes.EXCLUIR_SERVICO_ASSINANTE)
            {
                comandoSQL =    "INSERT INTO tbl_apr_bloqueio_servico " +
                                "(idt_msisdn,id_servico,idt_usuario,id_motivo,id_status,dat_atualizacao) " +
                                "VALUES (?,?,?,?,?,sysdate)";

                // Monta o vetor de parametros para o preparedUpdate
                params = new Object[5];
                params[0] = aMSISDN;
                params[1] = aIdServico;
                params[2] = Definicoes.GPP_OPERADOR;
                params[3] = Definicoes.MOT_DESAT_PEDIDO;
                params[4] = Definicoes.STATUS_BLOQUEIO_CONCLUIDO;               
            }
            else if (acao == Definicoes.INCLUIR_SERVICO_ASSINANTE)
            {
                comandoSQL =    "DELETE tbl_apr_bloqueio_servico " +
                                "WHERE idt_msisdn = ? AND id_servico = ?";

                params = new Object[2];
                // Monta o vetor de parametros para o preparedUpdate
                params[0] = aMSISDN;
                params[1] = aIdServico;
            }
            
            /* Comentado para inutilização da inserção na tabela TBL_APR_SERVICOS_ASSINANTE e utilização da tbl_apr_bloqueio_servico
            if (acao == Definicoes.INCLUIR_SERVICO_ASSINANTE)
            {
                // Obtem a data do sistema formatada
                String data = GPPData.dataCompletaForamtada();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            
                String comandoSQL = "INSERT INTO TBL_APR_SERVICOS_ASSINANTE ";
                comandoSQL = comandoSQL + "(IDT_MSISDN, ID_SERVICO, DAT_ATUALIZACAO) ";
                comandoSQL = comandoSQL + "VALUES (?,?,?)";

                params = new Object[3];
                // Monta o vetor de parametros para o preparedUpdate
                params[0] = aMSISDN;
                params[1] = aIdServico;
                params[2] = new Timestamp(sdf.parse(data).getTime());
                
            }
            if (acao == Definicoes.EXCLUIR_SERVICO_ASSINANTE)
            {
                comandoSQL              = "DELETE TBL_APR_SERVICOS_ASSINANTE ";
                comandoSQL = comandoSQL + "WHERE IDT_MSISDN = ? AND ID_SERVICO = ?";

                params = new Object[2];
                // Monta o vetor de parametros para o preparedUpdate
                params[0] = aMSISDN;
                params[1] = aIdServico;
            }*/ 
            // Executa a inclusao ou exclusao do registo no banco de dados                  
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                retorno = Definicoes.RET_OPERACAO_OK;
                super.log(Definicoes.DEBUG, "ativacaoCancelamentoServico", "Servico"+new String(acao==0?" Incluido":" Cancelado"));
            }
            else
            {
                retorno = Definicoes.RET_OPERACAO_OK;
                super.log(Definicoes.DEBUG, "ativacaoCancelamentoServico", "Servico ja existente ou nao foi removido por nao existir.");
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "ativacaoCancelamentoServico", "Erro atualizacao dados de servico: " + e);
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "ativacaoCancelamentoServico", "Fim");
        }

        return retorno;
    }

    /** 
     * Metodo...: consultaServico
     * Descricao: Consulta a existencia de um servico no banco de dados
     * @param   String  aIdServico      - Identificador do servico a ser consultado
     * @return  boolean                 - True se o servico existe, falso caso contrario
     */
    public boolean consultaServico (String aIdServico)
    {
        boolean retorno = false;
        
        super.log(Definicoes.INFO, "consultaServico", "Inicio SERVICO id "+aIdServico);

        try
        {
            MapServicosAssinante mapServicosAssinante = MapServicosAssinante.getInstancia();
            
            if ( mapServicosAssinante.getMapDescServico(aIdServico) != null )
            {
                retorno = true;
                super.log(Definicoes.DEBUG, "consultaServico", "Servico: " + aIdServico + " existente.");
            }
            else
            {
                super.log(Definicoes.DEBUG, "consultaServico", "Nao existe registro do servico: " + aIdServico + " no banco: ");
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "consultaServico", "Erro interno GPP: " + e);
        }
        super.log(Definicoes.DEBUG, "consultaServico", "Fim");

        return retorno;     
    }

    /** 
     * Metodo...: consultaServicoParaAssinante
     * Descricao: Consulta a existencia de um servico associado a um assinante
     * @param   String  aMsisdn         - Assinante a ser consultado
     * @param   String  aIdServico      - Identificador do servico a ser consultado
     * @return  boolean                 - True se o assinante possuir o servico, falso caso contrario
     */
    public boolean consultaServicoParaAssinante ( String aMSISDN, String aIdServico )
    {
        boolean retorno = false;
        
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "consultaServico", "Inicio");

        try
        {
            String comandoSQL = null;

            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            comandoSQL =    "SELECT DAT_ATUALIZACAO FROM tbl_apr_bloqueio_servico " + 
                            "WHERE IDT_MSISDN = ? AND ID_SERVICO = ?";
            
            /*Comentado para inutilização da inserção na tabela TBL_APR_SERVICOS_ASSINANTE e utilização da tbl_apr_bloqueio_servico 
             * comandoSQL              = "SELECT DAT_ATUALIZACAO FROM TBL_APR_SERVICOS_ASSINANTE ";
             * comandoSQL = comandoSQL + "WHERE IDT_MSISDN = ? AND ID_SERVICO = ?";*/

            // Monta o vetor de parametros para o preparedUpdate
            Object[] params = { aMSISDN, aIdServico };
                
            // Executa a inclusao ou exclusao do registo no banco de dados                  
            ResultSet rs = conexaoPREP.executaPreparedQuery(comandoSQL, params, super.logId);
            if (rs.next())
            {
                retorno = true;
                super.log(Definicoes.DEBUG, "consultaServicoParaAssinante", "Usuario:" + aMSISDN + " possui o servico: " + aIdServico + " cadastrado.");
            }
        }
        catch (SQLException e)
        {
            super.log(Definicoes.ERRO, "consultaServicoParaAssinante", "Usuario:" + aMSISDN + " NAO possui o servico: " + aIdServico + " cadastrado.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "consultaServicoParaAssinante", "Erro consulta do assinante/servico: " + e);
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "consultaServicoParaAsinante", "Fim");
        }

        return retorno;
    }
    
    /** 
     * Metodo...: gravaDadosAtivacao
     * Descricao: Grava dados em tabela referente a ativacao
     * @param   String  aMSISDN         - Numero do MSISDN ativado
     * @param   String  aIMSI           - Numero do SimCard (IMSI)
     * @param   String  aPlanoPreco     - Plano do preco a ser ativado
     * @param   double  aCreditoInicial - Credito Inicial do assinante
     * @param   short   aIdioma         - Idioma do assinante
     * @param   String  aOperador       - Operador que realizou a ativacao
     * @param   String  aStatus         - Status da Operacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaDadosAtivacao (String aMSISDN, String aIMSI, String aPlanoPreco, double aCreditoInicial, short aIdioma, String aOperador, String aStatus, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "gravaDadosAtivacao", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            // Obtem a data do sistema formatada
            String data = GPPData.dataCompletaForamtada();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            
            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_IMSI, IDT_PLANO_PRECO, VLR_CREDITO_INICIAL, IDT_IDIOMA, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, Definicoes.TIPO_APR_ATIVACAO, aIMSI,
                               aPlanoPreco, new Double(aCreditoInicial), new Integer(aIdioma), aOperador, aStatus, 
                               new Integer(aRetOperacao)};
                                
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaDadosAtivacao", "Dados ativacao gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaDadosAtivacao", "Erro gravacao dos dados de ativacao.");
            }
        }
        catch (ParseException pe)
        {
            super.log(Definicoes.ERRO, "gravaDadosAtivacao", "Erro gravacao dados de ativacao: Data de aprovisionamento esta no formato invalido: ");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaDadosAtivacao", "Erro gravacao dados de ativacao: Nao conseguiu pegar conexao com banco de dados.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaDadosAtivacao", "Fim");
        }
        return retorno;
    }
    
    /** 
     * Metodo...: gravaHibrido
     * Descricao: Grava dados em tabela referente a ativacao de hibrido
     * @param       String  aMSISDN         - Numero do MSISDN ativado
     * @param       double  aCreditoInicial - Credito Inicial do assinante
     * @param       double  saldoAtual      - Saldo atual do cliente
     * @param       Date    dataAtivacao    - Data de ativação
     * @return      boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaHibrido (String aMSISDN, double aCreditoInicial, double saldoAtual, Date dataAtivacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());

            String comandoSQL   = "INSERT INTO tbl_apr_plano_hibrido                                   "
                                + "(idt_msisdn, vlr_cred_fatura, vlr_saldo_inicial, dat_ciclo,         "
                                + " num_mes_execucao, dat_ultima_recarga_processada, dat_ativacao_gpp, "
                                + " dat_ativacao_geneva, num_contrato, ind_novo_controle)              "
                                + "values                                                              "
                                + "(?,?,?,null,0,sysdate,trunc(?),null,null,1)                         ";
            
            Object params[] = { aMSISDN, new Double(aCreditoInicial),
                                new Double(saldoAtual), new Timestamp(dataAtivacao.getTime())};

            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaHibrido", "Dados ativacao hibrido gravados.");
                retorno = true;
            }
            else
                super.log(Definicoes.INFO, "gravaHibrido", "Erro gravacao dados ativacao hibrido.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaHibrido", "Erro gravacao dos dados ativacao hibrido.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
        }
        
        return retorno;
    }

    /** 
     * Metodo...: gravaDadosDesativacao
     * Descricao: Grava dados em tabela referente a desativacao
     * @param   String      aMSISDN             - Numero do MSISDN ativado
     * @param   String      aMotivoDesativacao  - Motivo da desativacao
     * @param   double      aSaldoFinal         - Saldo final antes da desativacao
     * @param   String      aOperador           - Operador que realizou a ativacao
     * @param   String      aStatus             - Status da Operacao
     * @param   short       aRetOperacao        - Retorno da operacao
     * @param   Timestamp   dataDesativacao     - Data de desativação do assinante
     * @return  boolean                         - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaDadosDesativacao (String aMSISDN, String aMotivoDesativacao, double aSaldoFinal, String aOperador, String aStatus, short aRetOperacao, Timestamp dataDesativacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "gravaDadosDesativacao", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_MOTIVO, VLR_CREDITO_INICIAL, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?,?,?)";
            
            Object params[] = {dataDesativacao, aMSISDN, Definicoes.TIPO_APR_DESATIVACAO, 
                               aMotivoDesativacao, new Double(aSaldoFinal), aOperador, aStatus, 
                               new Integer(aRetOperacao)};
                
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaDadosDesativacao", "Dados desativacao gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaDadosDesativacao", "Erro gravacao dados desativacao.");
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaDadosDesativacao", "Erro gravacao dados desativacao: Nao conseguiu pegar conexao com banco de dados.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaDadosDesativacao", "Fim");
        }
        return retorno;
    }
    
    /** 
     * Metodo...: removeHibrido
     * Descricao: Remove dados de tabela referente a desativacao de hibrido
     * @param   String  aMSISDN     - Numero do MSISDN desativado
     * @return  boolean             - True se conseguiu apagar os dados no banco, false caso contrario
     */
    public boolean removeHibrido (String aMSISDN)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "removeHibrido", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());

            String comandoSQL       = "DELETE FROM TBL_APR_PLANO_HIBRIDO ";
            comandoSQL = comandoSQL + "WHERE IDT_MSISDN = ?";

            Object param[] = {aMSISDN};

            if (conexaoPREP.executaPreparedUpdate(comandoSQL, param, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "removeHibrido", "Dados hibrido deletados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "removeHibrido", "Erro delecao dados de hibrido.");
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "removeHibrido", "Erro delecao dados de hibrido.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "removeHibrido", "Fim");
        }
        return retorno;
    }
    
    /** 
     * Metodo...: gravaDadosBloqueio
     * Descricao: Grava dados em tabela referente ao bloqueio de assinante
     * @param   String  aMSISDN         - Numero do MSISDN ativado
     * @param   String  aIdBloqueio     - Identificacao do motivo do bloqueio
     * @param   double  aTarifa         - Tarifa cobrada pelo bloqueio
     * @param   String  aOperador       - Operador que realizou a ativacao
     * @param   String  aStatus         - Status da Operacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaDadosBloqueio (String aMSISDN, String aIdBloqueio, double aTarifa, String aOperador, String aStatus, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP =null;
        
        super.log(Definicoes.INFO, "gravaDadosBloqueio", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            // Obtem a data do sistema formatada
            String data = GPPData.dataCompletaForamtada();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_MOTIVO, VLR_CREDITO_INICIAL, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?,?,?)";
            
            Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, Definicoes.TIPO_APR_BLOQUEIO, 
                               aIdBloqueio, new Double(aTarifa), aOperador, aStatus, 
                               new Integer(aRetOperacao)};
            
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaDadosBloqueio", "Dados bloqueio gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaDadosBloqueio", "Erro gravacao dados de bloqueio.");
            }
        }
        catch(ParseException pe)
        {
            super.log(Definicoes.ERRO, "gravaDadosBloqueio", "Erro gravacao dados de bloqueio: Data de aprovisionamento esta no formato invalido.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaDadosBloqueio", "Erro gravacao dados de bloqueio.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaDadosBloqueio", "Fim");
        }
        return retorno;
    }

   /** 
    * Metodo...: gravaDadosBloqueio
    * Descricao: Grava dados em tabela referente ao desbloqueio de assinante
    * @param    String  aMSISDN         - Numero do MSISDN ativado
    * @param    String  aOperador       - Operador que realizou a ativacao
    * @param    String  aStatus         - Status da Operacao
    * @param    short   aRetOperacao    - Retorno da operacao
    * @return   boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
    */
   public boolean gravaDadosDesbloqueio (String aMSISDN, String aOperador, String aStatus, short aRetOperacao)
   {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
         
        super.log(Definicoes.INFO, "gravaDadosDesbloqueio", "Inicio MSISDN "+aMSISDN);
        
        try
        {
           // Busca uma conexao de banco de dados       
           conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
           // Obtem a data do sistema formatada
           String data = GPPData.dataCompletaForamtada();
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

           String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
           comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
           comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?)";
            
           Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, Definicoes.TIPO_APR_DESBLOQUEIO, 
                              aOperador, aStatus, new Integer(aRetOperacao)};
            
           if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
           {
               super.log(Definicoes.DEBUG, "gravaDadosDesbloqueio", "Dados desbloqueio gravados.");
               retorno = true;
           }
           else
           {
               super.log(Definicoes.INFO, "gravaDadosDesbloqueio", "Erro gravacao dados de desbloqueio.");
           }
        }
        catch (ParseException pe)
        {
            super.log(Definicoes.ERRO, "gravaDadosDesbloqueio", "Erro gravacao dados de desbloqueio: Data de aprovisionamento esta no formato invalido.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaDadosDesbloqueio", "Erro gravacao dados de desbloqueio.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaDadosDesbloqueio", "Fim");
        }
        return retorno;
   }

    /** 
     * Metodo...: gravaAtualizaFF
     * Descricao: Grava dados em tabela referente atualizacao de FF
     * @param   String  aMSISDN         - Codigo do Assinante (MSISDN) 
     * @param   String  aListaFF        - Lista de Friends and Family do Assinante
     * @param   String  aOperador       - Codigo do operador que esta realizando a ativacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaAtualizaFF (String aMSISDN, String aListaFF, String aOperador, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;

        super.log(Definicoes.INFO, "gravaAtualizaFF", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            // Obtem a data do sistema formatada
            String data = GPPData.dataCompletaForamtada();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String status = aRetOperacao == Definicoes.RET_OPERACAO_OK ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO;
            
            String comandoSQL   = "INSERT INTO TBL_APR_EVENTOS "
                                + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, NOM_OPERADOR, DES_STATUS, COD_RETORNO, DES_LISTA_FF) "
                                + "VALUES (?,?,?,?,?,?,substr(?,1,216))";

            Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, Definicoes.TIPO_APR_ATUALIZA_FF, 
                               aOperador, status, new Integer(aRetOperacao), aListaFF};
            
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaAtualizaFF", "Dados atualizacao lista Friends and Family gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaAtualizaFF", "Erro gravacao dados atualizacao lista Friends and Family.");
            }
        }
        catch (ParseException pe)
        {   
            super.log(Definicoes.ERRO, "gravaAtualizaFF", "Erro gravacao dados troca de atualizacao lista Friends and Family: Data de aprovisionamento formato invalido."); 
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaAtualizaFF", "Erro gravacao dados troca atualizacao lista Friends and Family.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaAtualizaFF", "Fim");
        }
        return retorno;
     }

    /** 
     * Metodo...: gravaTrocaSenha
     * Descricao: Grava dados em tabela referente troca de senha
     * @param   String  aMSISDN         - Codigo do Assinante (MSISDN) 
     * @param   String  aSistemaOrigem  - Codigo do Sistema de Origem de onde vem a troca de senha
     * @param   String  aStatus         - Status da Operacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaModificaDados (String aMSISDN, short aTipoDado, String aValorDado, String aDadoAntigo, String aOperador, String aSucessoFalha, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;

        super.log(Definicoes.INFO, "gravaModificaDados", "Inicio");

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
            // Obtem a data do sistema formatada
            String data = GPPData.dataCompletaForamtada();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            
            String tipoEvento = "";
            
            switch(aTipoDado)
            {
                case(Definicoes.TIPO_BLOQUEIO):
                {
                    tipoEvento = Definicoes.TIPO_APR_BLOQUEIO;
                    break;
                }
                case(Definicoes.TIPO_TROCA_PLANO):
                {
                    tipoEvento = Definicoes.TIPO_APR_TROCA_PLANO;
                    break;
                }
                case(Definicoes.TIPO_ATUALIZA_FF):
                {
                    tipoEvento = Definicoes.TIPO_APR_ATUALIZA_FF;
                    break;
                }
                case(Definicoes.TIPO_TROCA_SINCARD):
                {
                    tipoEvento = Definicoes.TIPO_APR_TROCA_SIMCARD;
                    break;
                }
                case(Definicoes.TIPO_STATUS_SHUTDOWN):
                {
                    tipoEvento = Definicoes.TIPO_APR_SHUTDOWN;
                    break;
                }
            }

            String comandoSQL   = "INSERT INTO TBL_APR_EVENTOS "+
                                  "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_ANTIGO_CAMPO, IDT_NOVO_CAMPO, NOM_OPERADOR, DES_STATUS, COD_RETORNO) "+
                                  "VALUES (?,?,?,?,?,?,?,?)";

            Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, tipoEvento, 
                                aDadoAntigo, aValorDado, aOperador, aSucessoFalha, new Integer(aRetOperacao)};

            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaTrocaSenha", "Dados troca de Senha gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaTrocaSenha", "Erro gravacao dados da troca de Senha.");
            }
        }
        catch (ParseException pe)
        {
            super.log(Definicoes.ERRO, "gravaTrocaSenha", "Erro gravacao dados troca de Senha: Data de aprovisionamento esta no formato invalido.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaSenha", "Erro gravacao dados troca de Senha.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaTrocaSenha", "Fim");
        }
        return retorno;
     }
     
    /** 
     * Metodo...: gravaTrocaSenha
     * Descricao: Grava dados em tabela referente troca de senha
     * @param   String  aMSISDN         - Codigo do Assinante (MSISDN) 
     * @param   String  aSistemaOrigem  - Codigo do Sistema de Origem de onde vem a troca de senha
     * @param   String  aStatus         - Status da Operacao
     * @param   short   aRetOperacao    - Retorno da operacao
     * @return  boolean                 - True se conseguiu gravar os dados no banco, false caso contrario
     */
    public boolean gravaTrocaSenha (String aMSISDN, String aStatus, short aRetOperacao)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;

        super.log(Definicoes.INFO, "gravaTrocaSenha", "Inicio");

        try
        {
             // Busca uma conexao de banco de dados     
             conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
    
             // Obtem a data do sistema formatada
             String data = GPPData.dataCompletaForamtada();
             SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            String comandoSQL       = "INSERT INTO TBL_APR_EVENTOS ";
            comandoSQL = comandoSQL + "(DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, NOM_OPERADOR, DES_STATUS, COD_RETORNO) ";
            comandoSQL = comandoSQL + "VALUES (?,?,?,?,?,?)";

            Object params[] = {new Timestamp(sdf.parse(data).getTime()), aMSISDN, Definicoes.TIPO_APR_TROCA_SENHA, 
                                Definicoes.GPP_OPERADOR, aStatus, new Integer(aRetOperacao)};

            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "gravaTrocaSenha", "Dados troca de Senha gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "gravaTrocaSenha", "Erro gravacao dados da troca de Senha.");
            }
        }
        catch (ParseException pe)
        {
            super.log(Definicoes.ERRO, "gravaTrocaSenha", "Erro gravacao dados troca de Senha: Data de aprovisionamento esta no formato invalido.");
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gravaTrocaSenha", "Erro gravacao dados troca de Senha.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "gravaTrocaSenha", "Fim");
        }
        return retorno;
     }
    
    /** 
     * Metodo...:   getTipoTransacao
     * Descricao:   Verifica o tipo de transacao de acordo com a tarifa
     * @param   String  aIdTarifa   - Identificacao da Tarifa que esta sendo cobrada
     * @return  String              - Retorna o tipo de transacao   
     * @throws  GPPInternalErrorException                   
     */
    public String getTipoTransacao (String aIdTarifa) throws GPPInternalErrorException
    {
        String retorno = null;
        
        super.log(Definicoes.INFO, "getTipoTransacao", "Inicio TARIFA "+aIdTarifa);

        try
        {       
            //Seleciona a instancia do objeto de tarifa de troca de msisdn em memoria
            MapTarifaTrocaMSISDN mapTarifa = MapTarifaTrocaMSISDN.getInstancia();
        
            if (mapTarifa != null)
            {
                // verifica o tipo de transacao associado a tarifa
                retorno = mapTarifa.getMapTipoTransacao(aIdTarifa);
            }   
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "getTipoTransacao", "Excecao Interna GPP: " + e);
            throw new GPPInternalErrorException ("Excecao Interna GPP: " + e);
        }                   
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO, "getTipoTransacao", "Excecao Interna GPP: " + e1);
            throw new GPPInternalErrorException ("Excecao Interna do GPP ocorrida: " + e1);          
        }
        finally
        {
            super.log(Definicoes.DEBUG, "getTipoTransacao", "Fim");
        }
        return retorno;
    }
    
    /** 
     * Metodo...:   atualizaHibrido
     * Descricao:   Atualiza MSISDN de hibrido na tabela referente a hibridos
     * @param   String  aMSISDN     - Numero do MSISDN antigo
     * @param   String  aNovoMSISDN - Numero do Novo MSISDN
     * @return  boolean             - True se conseguiu atualizar os dados no banco, false caso contrario
     */
    public boolean atualizaHibrido (String aMSISDN, String aNovoMSISDN)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;

        super.log(Definicoes.INFO, "atualizaHibrido", "Inicio MSISDN " + aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());

            String comandoSQL       = "UPDATE TBL_APR_PLANO_HIBRIDO ";
            comandoSQL = comandoSQL + "SET IDT_MSISDN = ? ";
            comandoSQL = comandoSQL + "WHERE IDT_MSISDN = ?";
            
            Object params[] = {aNovoMSISDN, aMSISDN};
            
            if (conexaoPREP.executaPreparedUpdate(comandoSQL, params, super.logId) > 0)
            {
                super.log(Definicoes.DEBUG, "atualizaHibrido", "Dados atualizacao de hibrido gravados.");
                retorno = true;
            }
            else
            {
                super.log(Definicoes.INFO, "atualizaHibrido", "Erro gravacao dados atualizacao de hibrido.");
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "atualizaHibrido", "Erro gravacao dados atualizacao de hibrido.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "atualizaHibrido", "Fim");
        }
        return retorno;
    }
    
    /**
     * Metodo...: parseTrocaSenhaXML
     * Descricao: Le um XML e seta os campos da estrutura DadosSenha 
     * @param   String  GPPTrocaSenha   - XML Recebido
     * @return  DadosSenha              - Estrutura DadosSenha populada
     * @throws  GPPBadXMLFormatException
     * @throws  GPPInternalErrorException
     */
    public DadosSenha parseTrocaSenhaXML ( String GPPTrocaSenha ) throws GPPBadXMLFormatException, GPPInternalErrorException
    {
        DadosSenha dadosSenha = null;
        
        super.log(Definicoes.INFO, "parseTrocaSenhaXML", "Inicio");

        try
        {
            super.log(Definicoes.DEBUG, "parseTrocaSenhaXML", "XML: " + GPPTrocaSenha);

            // Busca uma instancia de um DocumentBuilder
            DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
            
            // Cria um parse de XML
            DocumentBuilder parse = docBuilder.newDocumentBuilder();
            
            // Carrega o XML informado dentro e um InputSource
            InputSource is = new InputSource(new StringReader(GPPTrocaSenha));
            
            // Faz o parse do XML
            Document doc = parse.parse(is);
    
            // Procura a TAG GPPRecarga
            Element serviceElement = (Element) doc.getElementsByTagName( "GPPTrocaSenha" ).item(0);
            NodeList itemNodes = serviceElement.getChildNodes();            

            for (int i = 0; i < itemNodes.getLength(); i++) 
            {
                if (itemNodes.item(i).getChildNodes().item(0).getNodeValue() == null)
                {
                    super.log(Definicoes.DEBUG, "parseTrocaSenhaXML", "Erro formato XML(falta parametro)");
                    throw new GPPBadXMLFormatException ("Erro formato XML(falta parametro)");
                }
            }

            dadosSenha = new DadosSenha();
            
            // Armazena os dados do XML em uma classe DadosSenha
            dadosSenha.setMSISDN(((Element)itemNodes).getElementsByTagName("msisdn").item(0).getChildNodes().item(0).getNodeValue());
            dadosSenha.setSenha(((Element)itemNodes).getElementsByTagName("novaSenha").item(0).getChildNodes().item(0).getNodeValue());
        }
        catch (SAXException e) 
        {
            super.log(Definicoes.ERRO, "parseTrocaSenhaXML", "Erro formato XML: " + e);
            throw new GPPBadXMLFormatException ("Erro formato de XML:" + e);
        }
        catch (NullPointerException e)
        {
            super.log(Definicoes.ERRO, "parseTrocaSenhaXML", "Erro formato XML: " + e);
            throw new GPPBadXMLFormatException ("Erro formato de XML:" + e);
        }
        catch (IOException e) 
        {
            super.log(Definicoes.ERRO, "parseTrocaSenhaXML", "Erro IO: " + e);
            throw new GPPInternalErrorException ("Erro interno GPP:" + e);
        }
        catch (ParserConfigurationException e)
        {
            super.log(Definicoes.ERRO, "parseTrocaSenhaXML", "Erro Configuracao Parser: " + e);
            throw new GPPInternalErrorException ("Erro interno GPP:" + e);
        }
        finally
        {
            super.log(Definicoes.DEBUG, "parseTrocaSenhaXML", "Fim");
        }
        return dadosSenha;
    }
    
    /**
     * Metodo...: setaRetornoTrocaSenha
     * Descricao: Seta os campos de retorno da estrutura DadosSenha 
     * @param   String  aDadosSenha  - Estrutura DadosSenha populada
     * @param   short   aCodRetorno  - codigo do retorno da troca de senha
     * @return  
     */
    public void setaRetornoTrocaSenha (DadosSenha aDadosSenha, short aCodRetorno)
    {
        String sql;
        ResultSet rs;
        
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "setaRetornoTrocaSenha", "Inicio MSISDN "+aDadosSenha.getMSISDN());

        // Seta valor do retorno
        aDadosSenha.setRetorno(aCodRetorno);
        
        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
            
            // Seleciona descricao do retorno
            sql = "SELECT DES_RETORNO FROM TBL_GER_CODIGOS_RETORNO ";
            sql = sql + "WHERE VLR_RETORNO = ?";            
            Object param[] = {aCodRetorno + ""};            
            rs = conexaoPREP.executaPreparedQuery(sql, param, super.getIdLog());
            
            if (rs.next())
            {
                // Seta descricao do retorno
                aDadosSenha.setDescRetorno( rs.getString(1));
            }
            rs.close();
            rs = null;
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "setaRetornoTrocaSenha", "Erro Interno GPP: " + e);
        }
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO, "setaRetornoTrocaSenha", "Erro: " + e1);
        }
        finally
        {
            super.log(Definicoes.DEBUG, "setaRetornoTrocaSenha", "Fim");

            // Libera conexao com o banco de dados
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
        }
    }
    
    /**
     * Metodo...: decodificaSenha
     * Descricao: Decodifica a Senha da estrutura DadosSenha 
     * @param       String  aSenhaCodificada    - Senha codificada
     * @return      String                      - Retorna a senha decodificada
     */
    public String decodificaSenha (String aSenhaCodificada)
    {
        String retorno = null;
        
        PREPConexao conexaoPrep = null;
        String sql;
        ResultSet rs;
        
        Key chave= null; 
        
        super.log(Definicoes.DEBUG, "decodificaSenha", "Inicio");

        // Retira os caracteres '-' da senha informada
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        java.util.StringTokenizer st = new java.util.StringTokenizer( aSenhaCodificada, "-", false );
        while( st.hasMoreTokens() )
        {
          int i = Integer.parseInt( st.nextToken() );
          bos.write( ( byte )i );
        }
        
        // Inicia o processo de decriptografia
        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
            
            // Seleciona chave de criptografia na tabela
            sql = "SELECT ID_CHAVE FROM TBL_GER_CHAVE_CRIPTOGRAFADA ";
            sql = sql + "WHERE DAT_FIM IS NULL";
            rs = conexaoPrep.executaPreparedQuery( sql, null, super.logId);
            if (rs.next())
            {
                ChaveGPP chaveGPP = new ChaveGPP();
                chaveGPP.setEncoded( (rs.getString(1)).getBytes());
                chaveGPP.setFormat("RAW");
                chaveGPP.setAlgorithm ("DESEDE");   
                
                chave = chaveGPP; 
            }
            rs.close();
            rs = null;          
            
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            
            Cipher cipher=Cipher.getInstance("DESEDE/ECB/PKCS5Padding"); 
            cipher.init(Cipher.DECRYPT_MODE,chave); 
            
            //BASE64Decoder decoder = new BASE64Decoder(); 
            //byte[] raw = decoder.decodeBuffer(aSenhaCodificada); 
            byte[] stringBytes = cipher.doFinal(bos.toByteArray()); 
            retorno = new String(stringBytes,"UTF8"); 
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "decodificaSenha", "Erro Interno GPP: " + e);
        }
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO, "decodificaSenha", "Erro: " + e1);
        }
        finally
        {
            // Libera conexao com o banco de dados
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
            
            super.log(Definicoes.DEBUG, "decodificaSenha", "Fim");
        }
        return retorno;
    }
    
    /** 
     * Metodo...: validaMotivoBloqueio
     * Descricao: Verifica se um codigo de motivo de bloqueio e valido 
     * @param   String  aIdBloqueio     - Codigo do Motivo do Bloqueio a ser consultado
     * @return  short                   - Sucesso(0) ou erro (diferente de 0)  
     */
    public short validaMotivoBloqueio (String aIdBloqueio) throws GPPInternalErrorException
    {
        short retorno = Definicoes.RET_CODIGO_BLOQUEIO_INVALIDO;
        
        super.log(Definicoes.DEBUG, "validaMotivoBloqueio", "Inicio");

        try
        {       
            //Seleciona a instancia do objeto de motivos de bloqueio em memoria
            MapMotivoBloqueioAssinante mapMotivo = MapMotivoBloqueioAssinante.getInstancia();
        
            if (mapMotivo != null)
            {
                // verifica se o motivo do bloqueio
                if ( (mapMotivo.getMapDescMotivoBloqueioAssinante(aIdBloqueio)) != null)
                {
                    if ( (mapMotivo.getMapDescMotivoBloqueioAssinante(aIdBloqueio)) != null )               
                    {
                        retorno =  Definicoes.RET_OPERACAO_OK;
                    }
                }
            }   
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "validaMotivoBloqueio", "Erro Interno GPP: " + e);
            throw new GPPInternalErrorException ("Excecao Interna GPP: " + e);
        }                   
        catch (Exception e1)
        {
            super.log(Definicoes.ERRO, "validaMotivoBloqueio", "Erro: " + e1);
            throw new GPPInternalErrorException ("Excecao Interna GPP: " + e1);          
        }
        finally
        {
            super.log(Definicoes.DEBUG, "validaMotivoBloqueio", "Fim");
        }
        return retorno;
    }   

    public boolean atualizarTabelaComissionamento(String msisdn, PREPConexao conexaoPrep) throws Exception
    {
        String sqlUpdate = 
        	"DELETE " +
			"  FROM tbl_int_status_out " + 
			" WHERE idt_msisdn = ? " +
			"   AND idt_status_processamento in (?,?) ";           

        Object params[]= {msisdn, Definicoes.IND_LINHA_DISPONIBILIZADA, Definicoes.IDT_PROC_OK};
        return (conexaoPrep.executaPreparedUpdate(sqlUpdate, params, super.getIdLog()) > 0);
	}
    
    public boolean atualizarTabelaComissionamento(String msisdnAntigo)
    {
        PREPConexao conexaoPrep = null;
        boolean result = false;
        
        try
        {
            conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
            return this.atualizarTabelaComissionamento(msisdnAntigo, conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "atualizaTabelaComissionamento", "MSISDN: " + msisdnAntigo + " - Nao foi possivel atualizar a tabela de comissionamento");
        }
        finally
        {
            GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
        }
        
        return result; 
    }
    
    // Metodo que retorna true para ligmix e false caso contrario
    public boolean ehLigMix(int aPlanoPreco) throws GPPInternalErrorException
    {
        boolean retorno = false;
        
        MapPlanoPreco mapPlano = MapPlanoPreco.getInstancia();
        
        if (mapPlano != null)
        {
            // Verifica se o plano e ligMix
            if ((mapPlano.consultaCategoria(aPlanoPreco)) == Definicoes.COD_CAT_LIGMIX )
            {
                retorno = true;
            }
        }
        
        return retorno;
    }
    
    /**
     * Metodo...: zerarSaldos
     * Descricao: Metodo responsavel por retirar saldos para um determinado assinante
     * @param msisdn MSISDN do assinante
     * @param operador Operador que solicitou a operacao
     * @param tipoTransacao Tipo de Transacao
     * @param codSaldosZerados Codigo que indica os saldos que serao zerados
     * @return Resultado da operacao (0 em caso de sucesso)
     * @throws Exception
     */
    public short zerarSaldos(String msisdn, String operador, String tipoTransacao, int codSaldosZerados) throws Exception 
    {
        short retorno = Definicoes.RET_OPERACAO_OK;
        double creditoRetirado = 0;
        PREPConexao conexao = null;
        Assinante assinante = null;
        
        super.log(Definicoes.INFO, "zerarSaldos", "Inicio do processo de zerar saldos para o msisdn " + msisdn);
        
        try
        {
            conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
            conexao.setAutoCommit(false);
            
            // Consulta os saldos do assinante
            ConsultaAssinante consulta = new ConsultaAssinante(super.getIdLog());
            assinante = consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
            
            if(assinante.getRetorno() != Definicoes.RET_OPERACAO_OK)
            {
                retorno = assinante.getRetorno();
                return retorno;
            }
            
            ValoresRecarga valoresRecarga = new ValoresRecarga();
            // Seleciona os saldos a serem zerados
            switch(codSaldosZerados)
            {
                case(Definicoes.IND_ZERAR_SALDO_PRINCIPAL): // Deve zerar apenas o saldo principal
                {
                    creditoRetirado = assinante.getCreditosPrincipal();
                    valoresRecarga.setSaldoPrincipal(assinante.getCreditosPrincipal());
                    break;
                }
                case(Definicoes.IND_ZERAR_SALDO_BONUS): // Deve zerar apenas o saldo de bonus
                {
                    creditoRetirado = assinante.getCreditosBonus();
                    valoresRecarga.setSaldoBonus(assinante.getCreditosBonus());
                    break;
                }
                case(Definicoes.IND_ZERAR_SALDO_SMS): // Deve zerar apenas o saldo de sms
                {
                    creditoRetirado = assinante.getCreditosSms();
                    valoresRecarga.setValorBonusSMS(assinante.getCreditosSms());
                    break;
                }
                case(Definicoes.IND_ZERAR_SALDO_DADOS): //Deve zerar apenas o saldo de gprs
                {
                    creditoRetirado = assinante.getCreditosDados();
                    valoresRecarga.setValorBonusGPRS(assinante.getCreditosDados());
                    break;
                }
                case(Definicoes.IND_ZERAR_SALDOS_EXCETO_PRINCIPAL): // Deve zerar todos os saldos exceto o principal
                {
                    creditoRetirado = assinante.getCreditosBonus() 
                        + assinante.getCreditosSms() + assinante.getCreditosDados();
                    valoresRecarga.setSaldoBonus(assinante.getCreditosBonus());
                    valoresRecarga.setValorBonusSMS(assinante.getCreditosSms());
                    valoresRecarga.setValorBonusGPRS(assinante.getCreditosDados());
                    break;
                }
                case(Definicoes.IND_ZERAR_SALDOS): // Deve zerar todos os saldos
                {
                    creditoRetirado = assinante.getCreditosPrincipal() + assinante.getCreditosBonus() 
                        + assinante.getCreditosSms() + assinante.getCreditosDados();
                    valoresRecarga.setSaldoPrincipal(assinante.getCreditosPrincipal());
                    valoresRecarga.setSaldoBonus(assinante.getCreditosBonus());
                    valoresRecarga.setValorBonusSMS(assinante.getCreditosSms());
                    valoresRecarga.setValorBonusGPRS(assinante.getCreditosDados());
                    break;
                }
                default:
                {
                    retorno = Definicoes.RET_CODIGO_ZERAR_SALDOS_INVALIDO;
                    return retorno;
                }
            }
            
            // Retira o bonus pula-pula que seria concedido ao assinante
            ControlePulaPula controle = new ControlePulaPula(super.getIdLog());
            retorno = controle.trataTransferencia(assinante, Calendar.getInstance().getTime(), conexao);
            
            if(retorno == Definicoes.RET_OPERACAO_OK)
            {
                // Realiza um ajuste para zerar os saldos escolhidos
                super.log(Definicoes.DEBUG, "zerarSaldos", "Credito total a ser zerado " + creditoRetirado);
                Ajustar ajustar = new Ajustar(super.getIdLog());
                retorno = ajustar.executarAjuste(assinante.getMSISDN(), 
                                                 tipoTransacao, 
                                                 Definicoes.TIPO_CREDITO_REAIS, 
                                                 valoresRecarga, 
                                                 Definicoes.TIPO_AJUSTE_DEBITO, 
                                                 Calendar.getInstance().getTime(), 
                                                 Definicoes.SO_GPP, 
                                                 operador, 
                                                 assinante, 
                                                 null, 
                                                 true,
                                                 null);
            }
        }
        catch(Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            if(conexao != null)
            {
                try
                {
                    try
                    {
                        //Verifica o codigo de retorno para executar commit ou rollback na transacao.
                        if(retorno == Definicoes.RET_OPERACAO_OK)
                        {
                            conexao.commit();
                        }
                        else
                        {
                            conexao.rollback();
                        }
                    }
                    finally
                    {
                        conexao.setAutoCommit(true);
                        // Insere a operacao na TBL_APR_EVENTOS
                        String sql = "INSERT INTO TBL_APR_EVENTOS " +
                                " (DAT_APROVISIONAMENTO, IDT_MSISDN, TIP_OPERACAO, IDT_IMSI, IDT_PLANO_PRECO, VLR_CREDITO_INICIAL, " +
                                " IDT_IDIOMA, IDT_ANTIGO_CAMPO, IDT_NOVO_CAMPO, IDT_TARIFA, DES_LISTA_FF, IDT_MOTIVO, NOM_OPERADOR, " +
                                " DES_STATUS, COD_RETORNO) " +
                                " VALUES (SYSDATE, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        Object[] parametros = {msisdn, Definicoes.TIPO_APR_ZERAR_SALDOS, null, null, new Double(creditoRetirado), 
                                null, null, null, new Integer(0), null, null, operador, 
                                (retorno == Definicoes.RET_OPERACAO_OK) ? Definicoes.TIPO_OPER_SUCESSO : Definicoes.TIPO_OPER_ERRO,
                                new Integer(retorno)};
                        conexao.executaPreparedUpdate(sql, parametros, super.getIdLog());
                    }
                }
                finally
                {
                    super.gerenteBancoDados.liberaConexaoPREP(conexao, super.getIdLog());
                    super.log(Definicoes.INFO, "zerarSaldos", "Fim do processo de zerar saldos");
                }
            }
        }
        
        return retorno;
    }
    
    /** 
     *  Atualiza o status do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.  
     *  @param      status                  Novo status do assinante.
     *  @param      dataExpiracao           Nova data de expiracao dos saldos do assinante.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short atualizarStatusAssinante(String msisdn, short status, Date dataExpiracao, String operador)
    {
        try
        {
            //Obtendo as informacoes do assinante na plataforma.
            ConsultaAssinante consulta = new ConsultaAssinante(super.logId);
            Assinante assinante = consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
            //Atualizando o status do assinante.
            return this.atualizarStatusAssinante(assinante, status, dataExpiracao, operador);
        }
        catch(Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
    }
    
    /**
     *  Altera o status e as datas de expiracao do assinante. Este metodo recebe somente uma data de expiracao por 
     *  parametro devido a regra de que todas as datas de expiracao (com excecao da periodica) devem se manter iguais.
     *  E importante observar que o metodo NAO respeita a regra da data de expiracao mais longa. Isto porque ha 
     *  processos de migracao de planos em que as datas de expiracao devem ser ajustadas nos assinantes da nova 
     *  categoria de planos. Por exemplo, se um assinante for migrado de LigMix para Controle Total, sera desativado e 
     *  ativado novamente em um plano desta ultima categoria. A data de expiracao do saldo principal nao deve avancar 
     *  para os cinco anos e sim manter-se como a data configurada para o acesso antigo (LigMix).
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      status                  Novo status do assinante.
     *  @param      dataExpiracao           Nova data de expiracao do assinante. Se a data for NULL, as datas de 
     *                                      expiracao atuais do assinante nao serao atualizadas.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short atualizarStatusAssinante(Assinante assinante, short status, Date dataExpiracao, String operador)
    {
        short                       result              = Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento    conexao             = null;
        SimpleDateFormat            conversorTimestamp  = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
        String                      msisdn              = null;
        short                       statusAntigo        = -1;
        short                       plano               = -1;
        Date                        dataExecucao        = Calendar.getInstance().getTime();
        
        try
        {
            //Obtendo informacoes do assinante.
            msisdn          = (assinante != null) ? assinante.getMSISDN()           : null;
            statusAntigo    = (assinante != null) ? assinante.getStatusAssinante()  : -1;
            plano           = (assinante != null) ? assinante.getPlanoPreco()       : -1;
            
            super.log(Definicoes.DEBUG, "atualizarStatusAssinante", "MSISDN: " + msisdn + 
                                                                    " - Status: " + status + 
                                                                    " - Data de Expiracao: " +
                                                                    ((dataExpiracao != null) ? 
                                                                            conversorTimestamp.format(dataExpiracao) : 
                                                                            "Nao modificada"));

            //Validando o status passado por parametro.
            result = this.validarAtualizacaoStatusAssinante(assinante, status);
            
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Determinando as datas de expiracao a partir do mapeamento de saldos que o plano do assinante 
            	//permite. O Saldo Periodico deve ser atualizado se o assinante o possuir e nao possuir status 
            	//periodico. Se possuisse, a data de expiracao do Saldo Periodico seria independente.
                PlanoPreco  infoPlano   = MapPlanoPreco.getInstancia().getPlanoPreco(plano);
                TipoSaldo   principal   = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.PRINCIPAL);
                TipoSaldo   periodico   = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.PERIODICO);
                TipoSaldo   bonus       = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.BONUS    );
                TipoSaldo   torpedos    = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.TORPEDOS );
                TipoSaldo   dados       = MapTipoSaldo.getInstance().getTipoSaldo(TipoSaldo.DADOS    );
                Date dataExpPrincipal = (infoPlano.possuiSaldo(principal)) ? dataExpiracao : null;
                Date dataExpPeriodico = (infoPlano.possuiSaldo(periodico) && !infoPlano.possuiStatusPeriodico()) ? 
                							dataExpiracao : null;
                Date dataExpBonus     = (infoPlano.possuiSaldo(bonus    )) ? dataExpiracao : null;
                Date dataExpTorpedos  = (infoPlano.possuiSaldo(torpedos )) ? dataExpiracao : null;
                Date dataExpDados     = (infoPlano.possuiSaldo(dados    )) ? dataExpiracao : null;
                
                //Obtendo conexao de aprovisionamento.
                conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
                //Executando a atualizacao.
                result = conexao.atualizarStatusAssinante(assinante.getMSISDN(), 
                                                          status, 
                                                          dataExpPrincipal, 
                                                          dataExpPeriodico, 
                                                          dataExpBonus, 
                                                          dataExpTorpedos, 
                                                          dataExpDados);
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "atualizarStatusAssinante", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, super.getIdLog());
            this.gravaAtualizaStatusAssinante(msisdn, 
                                              MapStatusAssinante.getStatusAssinanteAsString(status), 
                                              MapStatusAssinante.getStatusAssinanteAsString(statusAntigo),
                                              dataExecucao,
                                              result,
                                              operador);
            super.log(Definicoes.INFO, "atualizarStatusAssinante", "MSISDN: " + msisdn + " - Codigo de retorno da operacao: " + result);
        }

        return result;
    }

    /**
     *  Executa a validacao do processo de atualizacao de status de assinante. 
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      status                  Novo status do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarAtualizacaoStatusAssinante(Assinante assinante, short status)
    {
        if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        if((status != Definicoes.STATUS_NORMAL_USER) && 
           (status != Definicoes.STATUS_RECHARGE_EXPIRED) && 
           (status != Definicoes.STATUS_DISCONNECTED))
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        
        return Definicoes.RET_OPERACAO_OK;
    }

    /** 
     *  Insere registro na tabela de eventos referente a atualizacao do status do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.  
     *  @param      statusNovo              Status novo.
     *  @param      statusAntigo            Status antigo.
     *  @param      dataProcessamento       Data de processamento da operacao.
     *  @param      retorno                 Codigo de retorno da operacao.
     *  @param      operador                Nome do operador.
     *  @return     True se o evento foi inserido e false caso contrario.
     */
    public boolean gravaAtualizaStatusAssinante(String msisdn, String statusNovo, String statusAntigo, Date dataAprovisionamento, short codigoRetorno, String operador)
    {
        boolean     result      = false;
        PREPConexao conexaoPrep = null;
        
        super.log(Definicoes.DEBUG, "gravaAtualizaStatusAssinante", "MSISDN: " + msisdn);
        
        try
        {
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            String sqlInsert =  "INSERT INTO TBL_APR_EVENTOS " +
                                "(DAT_APROVISIONAMENTO , " + 
                                " IDT_MSISDN           , " + 
                                " TIP_OPERACAO         , " + 
                                " IDT_ANTIGO_CAMPO     , " + 
                                " IDT_NOVO_CAMPO       , " + 
                                " NOM_OPERADOR         , " + 
                                " DES_STATUS           , " + 
                                " COD_RETORNO)           " + 
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
            
            Object[] parametros =
            {
                new Timestamp(dataAprovisionamento.getTime()),
                msisdn,
                statusNovo,
                statusAntigo,
                statusNovo,
                operador,
                (codigoRetorno == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
                new Short(codigoRetorno)
            };
            
            result = (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "gravaAtualizaStatusAssinante", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = false;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
        }
        
        return result;
    }

    /** 
     *  Atualiza o status periodico do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.  
     *  @param      status                  Novo status periodico do assinante.
     *  @param      dataExpiracao           Nova data de expiracao do Saldo Periodico do assinante.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short atualizarStatusPeriodico(String msisdn, short status, Date dataExpiracao, String operador)
    {
        try
        {
            //Obtendo as informacoes do assinante na plataforma.
            ConsultaAssinante consulta = new ConsultaAssinante(super.logId);
            Assinante assinante = consulta.executaConsultaCompletaAssinanteTecnomen(msisdn);
            //Atualizando o status do assinante.
            return this.atualizarStatusPeriodico(assinante, status, dataExpiracao, operador);
        }
        catch(Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
    }
    
    /**
     *  Altera o status periodico do assinante e a sua respectiva data de expiracao. E importante observar que o 
     *  metodo atribui exatamente a data de expiracao passada por parametro, mesmo que ela seja menor que a data de 
     *  expiracao atual ou menor que a data atual. 
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      status                  Novo status periodico do assinante.
     *  @param      dataExpiracao           Nova data de expiracao do saldo periodico do assinante. Se a data for NULL, 
     *                                      a data de expiracao atual do assinante nao sera atualizada.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short atualizarStatusPeriodico(Assinante assinante, short status, Date dataExpiracao, String operador)
    {
        short                       result              = Definicoes.RET_MSISDN_NAO_ATIVO;
        TecnomenAprovisionamento    conexao             = null;
        SimpleDateFormat            conversorTimestamp  = new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP);
        String                      msisdn              = null;
        short                       statusAntigo        = -1;
        Date                        dataExecucao        = Calendar.getInstance().getTime();
        
        try
        {
            //Obtendo informacoes do assinante.
            msisdn          = (assinante != null) ? assinante.getMSISDN()           : null;
            statusAntigo    = (assinante != null) ? assinante.getStatusPeriodico()  : -1;
            
            super.log(Definicoes.DEBUG, "atualizarStatusPeriodico", "MSISDN: " + msisdn + 
                                                                    " - Status: " + status + 
                                                                    " - Data de Expiracao: " +
                                                                    ((dataExpiracao != null) ? 
                                                                            conversorTimestamp.format(dataExpiracao) : 
                                                                            "Nao modificada"));

            //Validando o status passado por parametro.
            result = this.validarAtualizacaoStatusPeriodico(assinante, status);
            
            if(result == Definicoes.RET_OPERACAO_OK)
            {
                //Obtendo conexao de aprovisionamento.
                conexao = this.gerenteTecnomen.getTecnomenAprovisionamento(super.getIdLog());
                //Executando a atualizacao.
                result = conexao.atualizarStatusPeriodico(assinante.getMSISDN(), status, dataExpiracao);
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "atualizarStatusPeriodico", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.gerenteTecnomen.liberaConexaoAprovisionamento(conexao, super.getIdLog());
            this.gravarAtualizacaoStatusPeriodico(msisdn,
                                                  status,
                                                  statusAntigo,
                                                  dataExecucao,
                                                  result,
                                                  operador);
            super.log(Definicoes.INFO, "atualizarStatusPeriodico", "MSISDN: " + msisdn + " - Codigo de retorno da operacao: " + result);
        }

        return result;
    }
    
    /**
     *  Executa a validacao do processo de atualizacao de status periodico do assinante. 
     *
     *  @param      assinante               Informacoes do assinante na plataforma.
     *  @param      status                  Novo status periodico do assinante.
     *  @return     Codigo de retorno da operacao.
     */
    private short validarAtualizacaoStatusPeriodico(Assinante assinante, short status)
    {
        if((assinante == null) || (assinante.getRetorno() == Definicoes.RET_MSISDN_NAO_ATIVO))
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        if(assinante.getStatusServico() != Definicoes.SERVICO_DESBLOQUEADO)
            return Definicoes.RET_MSISDN_BLOQUEADO;
        if(assinante.getStatusAssinante() == Definicoes.STATUS_SHUTDOWN)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        if((status != Definicoes.STATUS_PERIODICO_NORMAL_USER) && 
           (status != Definicoes.STATUS_PERIODICO_PARTIALLY_BLOCKED) && 
           (status != Definicoes.STATUS_PERIODICO_BLOCKED))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        
        //Verificando se o plano do assinante possui saldo periodico.
        PlanoPreco  infoPlano   = MapPlanoPreco.getInstancia().getPlanoPreco(assinante.getPlanoPreco());
        if((infoPlano == null) || (!infoPlano.possuiStatusPeriodico()))
            return Definicoes.RET_STATUS_PERIODICO_INVALIDO;
        
        return Definicoes.RET_OPERACAO_OK;
    }

    /** 
     *  Insere registro na tabela de eventos referente a atualizacao do status periodicodo assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.  
     *  @param      statusNovo              Status novo.
     *  @param      statusAntigo            Status antigo.
     *  @param      dataExecucao            Data de processamento da operacao.
     *  @param      retorno                 Codigo de retorno da operacao.
     *  @param      operador                Nome do operador.
     *  @return     True se o evento foi inserido e false caso contrario.
     */
    public boolean gravarAtualizacaoStatusPeriodico(String msisdn, short statusNovo, short statusAntigo, Date dataExecucao, short codigoRetorno, String operador)
    {
        boolean     result      = false;
        PREPConexao conexaoPrep = null;
        
        super.log(Definicoes.DEBUG, "gravaAtualizaStatusPeriodico", "MSISDN: " + msisdn);
        
        try
        {
            conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            String sqlInsert =  "INSERT INTO tbl_apr_eventos(dat_aprovisionamento, " + 
                                "                            idt_msisdn, " + 
                                "                            tip_operacao, " + 
                                "                            idt_antigo_campo, " + 
                                "                            idt_novo_campo, " + 
                                "                            nom_operador, " + 
                                "                            des_status, " + 
                                "                            cod_retorno) " + 
                                "VALUES(?,?,?,?,?,?,?,?) ";
            
            Object[] parametros =
            {
                new Timestamp(dataExecucao.getTime()),
                msisdn,
                Definicoes.TIPO_APR_TROCA_STATUS_PERIODICO,
                String.valueOf(statusAntigo),
                String.valueOf(statusNovo),
                operador,
                (codigoRetorno == Definicoes.RET_OPERACAO_OK) ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
                new Short(codigoRetorno)
            };
            
            result = (conexaoPrep.executaPreparedUpdate(sqlInsert, parametros, super.logId) > 0);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "gravaAtualizaStatusPeriodico", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = false;
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
        }
        
        return result;
    }

    /**
     * Valida os parâmetros do atualização de amigos toda hora
     * @param assinante     -   Objeto assinante
     * @param codigoServico -   Código do servico
     * @param operacao      -   C: Consulta, D: Débito, E: Estorno 
     * @return short        -   Código de Erro ou Sucesso   
     * @throws GPPInternalErrorException 
     */
    public short validarParametros(Assinante assinante, String codigoServico, String operacao) throws GPPInternalErrorException
    {       
        // Valida a existência do assinante 
        if (assinante == null)
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        
        // Valida se não é LigMix
        if(ehLigMix(assinante.getPlanoPreco()))
            return Definicoes.RET_ASSINANTE_LIGMIX;
        
        // Valida o status do cliente
        if(consultaStatusFisrtTimeDisconnectedShutdown(assinante.getStatusAssinante(), assinante.getStatusServico()) == Definicoes.RET_STATUS_MSISDN_INVALIDO)
            return Definicoes.RET_STATUS_MSISDN_INVALIDO;
        
        // Caso não haja cobrança
        if(codigoServico == null || codigoServico.length() == 0)
            return Definicoes.RET_OPERACAO_OK;      
        
        // Busca os dados do serviço
        MapValorServico mapServico = MapValorServico.getInstance();
        ValorServico valor = mapServico.getServico  ( codigoServico, Integer.parseInt(assinante.getMSISDN().substring(2, 4))
                                                    , assinante.getPlanoPreco(), new Date(), operacao);

        // Valida a existência do código 
        if(valor == null)
            return Definicoes.RET_SERVICO_INEXISTENTE;
        
        // Valida se o saldo é suficiente para executar a cobrança
        if(!operacao.equals(Definicoes.OPERACAO_ESTORNO) && assinante.getCreditosPrincipal() < valor.getValorServico().doubleValue())
            return Definicoes.RET_CREDITO_INSUFICIENTE;
                        
        return Definicoes.RET_OPERACAO_OK;
    }   
    
    /**
     * Valida os parâmetros do atualização de amigos toda hora
     * @param msisdn        -   msisdn do assinante
     * @param codigoServico -   Código do servico
     * @param operacao      -   C: Consulta, D: Débito, E: Estorno 
     * @param operador      -   ID do operador que realizou a operação 
     * @return short        -   Código de Erro ou Sucesso   
     * @throws GPPInternalErrorException 
     * @throws GPPTecnomenException 
     */
    public short cobrarServico(Assinante assinante, String codigoServico, String operacao, String operador) throws GPPInternalErrorException, GPPTecnomenException
    {       
        short retorno = validarParametros(assinante, codigoServico, operacao);      
        
        if(retorno == Definicoes.RET_OPERACAO_OK 
           && (Definicoes.OPERACAO_ESTORNO.equalsIgnoreCase(operacao)
           || Definicoes.OPERACAO_DEBITO.equalsIgnoreCase(operacao)))
        {
            MapValorServico mapServico = MapValorServico.getInstance();
            ValorServico valor = mapServico.getServico  ( codigoServico, Integer.parseInt(assinante.getMSISDN().substring(2,4))
                                                        , assinante.getPlanoPreco(), new Date(), operacao);
    
            // Instancia objeto de ajuste
            Ajustar ajustar = new Ajustar(super.logId);
            String tipoTransacao = valor.getIdCanal()+valor.getIdOrigem();
            double valorTaxa = valor.getValorServico().doubleValue();
            
            String tipoAjuste = operacao.equalsIgnoreCase(Definicoes.OPERACAO_DEBITO) ? Definicoes.TIPO_AJUSTE_DEBITO : Definicoes.TIPO_AJUSTE_CREDITO;
            
            // Executa o ajuste
            if(valorTaxa != 0)
                retorno = ajustar.executarAjuste(assinante.getMSISDN(),
                                                 tipoTransacao,
                                                 Definicoes.TIPO_CREDITO_REAIS,
                                                 valorTaxa,
                                                 tipoAjuste,
                                                 Calendar.getInstance().getTime(),
                                                 Definicoes.
                                                 SO_GPP,
                                                 operador,
                                                 null,
                                                 assinante,
                                                 null,
                                                 Definicoes.AJUSTE_NORMAL);
        }
        
        return retorno;
    }
    
    /**
     * Consulta o servico
     * @param msisdn        -   msisdn do assinante
     * @param codigoServico -   Código do servico
     * @param retorno       -   Código de retorno da operacao
     * @param operacao      -   C: Consulta, D: Débito, E: Estorno
     * @return String       -   XML com o retorno da consulta   
     * @throws GPPInternalErrorException 
     */
    public String consultarServico(String msisdn, String codigoServico, CodigoRetorno retorno, String operacao) throws GPPInternalErrorException
    {   
        // Não consulta o assinante se houver consulta anterior identificando a inexistência do cliente
        Assinante assinante = ((Integer.parseInt(retorno.getValorRetorno()) == Definicoes.RET_MSISDN_NAO_ATIVO) ? null : consultaAssinante(msisdn));
                
        DecimalFormat conversorDouble = new DecimalFormat(Definicoes.MASCARA_DOUBLE_S_PONTO, new DecimalFormatSymbols(new Locale("pt", "BR", "")));
        DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
        
        GerarXML geradorXML     = new GerarXML("root");
        
        geradorXML.abreNo("GPPServico");
        
        geradorXML.adicionaTag("operacao"   ,operacao);
        geradorXML.adicionaTag("codRetorno" ,conversorRetorno.format(Integer.parseInt(retorno.getValorRetorno())));
        geradorXML.adicionaTag("descRetorno",retorno.getDescRetorno());
        
        int planoPreco = assinante == null ? 0 : assinante.getPlanoPreco(); 
        
        MapValorServico mapValorServico = MapValorServico.getInstance();
        ValorServico valor = mapValorServico.getServico ( codigoServico, Integer.parseInt(msisdn.substring(2,4))
                                                        , planoPreco, new Date(), operacao);
        
        MapServicosAssinante mapServico = MapServicosAssinante.getInstancia();
        
        // Insere os códigos apenas se o serviço estiver cadastrado na tabela
        geradorXML.adicionaTag("codServico" ,codigoServico);
        geradorXML.adicionaTag("descServico",valor == null ? null : mapServico.getMapDescServico(codigoServico));
        geradorXML.adicionaTag("valor"      ,valor == null ? null : conversorDouble.format(valor.getValorServico()));       
        
        // Insere os dados do assinante apenas se ele existir
        geradorXML.abreNo("assinante");
        geradorXML.adicionaTag("msisdn"         ,msisdn);
        geradorXML.adicionaTag("saldoPrincipal" ,assinante == null ? null : conversorDouble.format(assinante.getCreditosPrincipal()));
        geradorXML.adicionaTag("saldoBonus"     ,assinante == null ? null : conversorDouble.format(assinante.getCreditosBonus()));
        geradorXML.adicionaTag("saldoSMS"       ,assinante == null ? null : conversorDouble.format(assinante.getCreditosSms()));
        geradorXML.adicionaTag("saldoDados"     ,assinante == null ? null : conversorDouble.format(assinante.getCreditosDados()));
        geradorXML.fechaNo();
        
        geradorXML.fechaNo();
                        
        return geradorXML.getXML(); 
    }   
    
    /**
     * Monta o xml de retorno a partir dos dados da operação
     * @param msisdn        -   msisdn do assinante
     * @param codigoServico -   Código do servico
     * @param retorno       -   Código de retorno da operacao
     * @param operacao      -   C: Consulta, D: Débito, E: Estorno
     * @return String       -   XML com o retorno da consulta   
     * Formato do XML:
     *  <mensagem>
     *      <cabecalho>
     *          <empresa>BRG</empresa>
     *          <sistema>GPP</sistema>
     *          <processo>RELBRVALTTARATH</processo>
     *          <data>2006-08-16 12:23:00</data> 
     *          <identificador_requisicao>6184111111</identificador_requisicao>
     *          <codigo_erro>0000</codigo_erro>
     *          <descricao_erro>Sucesso</descricao_erro>
     *      </cabecalho>
     *      <conteudo>
     *          <![CDATA[
     *              <root>
     *                  <GPPServico>
     *                      <operacao>RqConsultaRecargaPeriodoRsp</operacao>
     *                      <codRetorno>0000</codRetorno>
     *                      <descRetorno>Sucesso</descRetorno>
     *                      <codServico>ELM_AMGS_TDHORA_V02_ID1885</codServico>
     *                      <descServico>Novo Amigos Toda Hora</descServico>
     *                      <valor>3,00</valor>
     *                  <assinante>
     *                      <msisdn>556184111111</msisdn>
     *                      <saldoPrincipal>50,00</saldoPrincipal>
     *                      <saldoBonus>100,00</saldoBonus>
     *                      <saldoSMS>0,00</saldoSMS>
     *                      <saldoDados>0,00</saldoDados>
     *                  </assinante>
     *              </root>
     *          ]]> 
     *      </conteudo>
     *  </mensagem>
     */
    public String gerarXMLServico(String msisdn, String codigoServico, int retorno, String operacao, String processo)
    {
        GerarXML geradorXML     = new GerarXML("mensagem");
        
        SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DATA_VITRIA);
        DecimalFormat conversorRetorno = new DecimalFormat(Definicoes.MASCARA_CODIGO_RETORNO);
        try
        {       
            MapCodigosRetorno mapRetorno = MapCodigosRetorno.getInstance(); 
            CodigoRetorno codRetorno = mapRetorno.getRetorno(retorno);
            
            // Compõe o conteúdo da Tag cabecalho
            geradorXML.abreNo("cabecalho");
            geradorXML.adicionaTag("empresa"                 ,Definicoes.EMPRESA_GSM);
            geradorXML.adicionaTag("sistema"                 ,Definicoes.SO_GPP);
            geradorXML.adicionaTag("processo"                ,processo);
            geradorXML.adicionaTag("data"                    ,sdf.format(new Date()));
            geradorXML.adicionaTag("identificador_requisicao",msisdn.substring(2));
            geradorXML.adicionaTag("codigo_erro"             ,conversorRetorno.format(retorno));
            geradorXML.adicionaTag("descricao_erro"          ,codRetorno.getDescRetorno());
            geradorXML.fechaNo();
            
            // Cria a Tag conteudo e inclui o campo CDATA com o XML     
            geradorXML.adicionaTag("conteudo", "<![CDATA[" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" 
                                   + consultarServico(msisdn,codigoServico, codRetorno, operacao) 
                                   + "]]>");
        }
        catch(GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "gerarXMLServico", "Erro ao consultar servico");
        }
                
        return ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML());
    }
    
    /**
     * Cadastrar Bumerangue 14
     * @param msisdn        -   msisdn do assinante
     * @param codigoServico -   Código do servico
     * @param retorno       -   Código de retorno da operacao
     * @param operacao      -   C: Consulta, D: Débito, E: Estorno
     * @param operador      -   ID do operador que realizou a operação
     * @return String       -   XML com o retorno da consulta   
     * @throws GPPInternalErrorException 
     */
    public short aprovisionarBumerangue(String msisdn, String codigoServico, String operacao, String operador) throws GPPInternalErrorException
    {   
        PREPConexao conexaoBanco = null;
        short retorno = Definicoes.RET_OPERACAO_OK;
                
        try
        {
            conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
            
            Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());
            MapPromocao mapPromocao = MapPromocao.getInstancia();
            
            ControlePromocao controle = new ControlePromocao(super.getIdLog());
            
            // Retira ou insere a promoção de bumerangue 14
            if(Definicoes.OPERACAO_DEBITO.equalsIgnoreCase(operacao))
                retorno = controle.inserePromocaoAssinante(msisdn,mapPromocao.getIdPromocao(codigoServico),dataProcessamento
                                                          ,operador,Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO,conexaoBanco);
            else if(Definicoes.OPERACAO_ESTORNO.equalsIgnoreCase(operacao))
                retorno = controle.retiraPromocaoAssinante(msisdn,mapPromocao.getIdPromocao(codigoServico),dataProcessamento
                                                          ,operador,Definicoes.CTRL_PROMOCAO_MOTIVO_DESATIVACAO,conexaoBanco);
        }
        finally
        {
            super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());
        }
        
                        
        return retorno; 
    }
            
    /** 
     * Metodo...: removerServicos
     * Descricao: Remove bloqueios de serviço do assinante
     * @param   String  aMSISDN     - Numero do MSISDN desativado
     * @return  boolean             - True se conseguiu apagar os dados no banco, false caso contrario
     */
    public boolean removerServicos (String aMSISDN)
    {
        boolean retorno = false;
        PREPConexao conexaoPREP = null;
        
        super.log(Definicoes.INFO, "removerServicos", "Inicio MSISDN "+aMSISDN);

        try
        {
            // Busca uma conexao de banco de dados      
            conexaoPREP = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());

            String comandoSQL       = "DELETE FROM tbl_apr_bloqueio_servico "
                                    + "WHERE IDT_MSISDN = ?";

            Object param[] = {aMSISDN};

            conexaoPREP.executaPreparedUpdate(comandoSQL, param, super.logId);
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "removerServicos", "Erro ao apagar servicos.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoPREP, super.getIdLog());
            super.log(Definicoes.DEBUG, "removerServicos", "Fim");
        }
        return retorno;
    }   
    
    /** 
     * Efetua a troca de plano preço para o plano espelho
     * @param   assinante       Dados do assinante
     * @param   operador        Operador responsável pela troca
     * @param   codigoServico   Serviço associado à troca
     * @return  retorno         Sucesso(0) ou erro (diferente de 0)
     */
    public short trocarPlanoEspelho(Assinante assinante, String operador, String codigoServico)
    {
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        PREPConexao conexaoBanco = null;
        
        ControlePromocao controle = new ControlePromocao(super.getIdLog());
        
        try
        {
            if(assinante != null)
            {
                conexaoBanco = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
                Date dataAtual = new Date();
                
                retorno = controle.trocarPlanoEspelho(assinante, 
                                                      codigoServico, 
                                                      assinante.getPlanoPreco(),
                                                      dataAtual,
                                                      conexaoBanco); 
            }           
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "trocarPlanoEspelho", "Erro ao trocar plano.");
        }
        finally
        {
            this.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());
        }
        
        return retorno;
    }
    
    /** 
     * Formata a lista de amigos toda hora, incluindo 0 no início
     * de cada número caso não tenha
     * @param   listaFF     Lista de amigos toda hora
     * @return  novaListaFF Lista formatada
     */
    /*private String formatarListaFF(String listaFF)
    {
        String novaListaFF = listaFF;
        
        if(listaFF != null && listaFF.length() >= 10)
        {
            novaListaFF = "";
            String [] lista = listaFF.split(";");
            
            for(int i = 0; i < lista.length; i++)
            {
                if(lista[i].length() == 10)
                    lista[i] = '0'+lista[i];
                novaListaFF += lista[i]+";";
            }
        }
        
        return novaListaFF;
    }*/
    
    /** 
     * Reativação de assinante que foi destivado indevidamente
     * @param   msisdnNovo      Numero com o qual o cliente deve ser reativado
     * @param   msisdnAntigo    Numero com o qual o cliente foi desativado
     * @return  retorno         Sucesso(0) ou erro (diferente de 0)
     */
    public short reativarAssinante(String msisdnNovo, String msisdnAntigo, String operador)
    {
        Assinante assinante = consultarUltimoStatus(msisdnAntigo);
        
        short retorno = Definicoes.RET_REATIVACAO_INVIAVEL;
        
        try
        {
            if(assinante.getMSISDN() != null)
            {
            	// TODO MAGNO Corrigir a lingua no caso de CT, pegar o do objeto assinante
                // Ativa o assinante com o novo MSISDN
                assinante.setMSISDN(msisdnNovo);
                retorno = ativaAssinante(msisdnNovo, assinante.getIMSI(), assinante.getPlanoPreco(), 
                                         0.0, Definicoes.TECNOMEN_LING_PORTUGUES);
                
                String statusProcesso = (retorno == Definicoes.RET_OPERACAO_OK ? Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO);
                PREPConexao conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.getIdLog());
                AtivacaoAssinanteDAO dao = new AtivacaoAssinanteDAO(conexaoPrep,super.logId);
                dao.gravaDadosAtivacao( assinante.getMSISDN(), assinante.getIMSI(), Short.toString(assinante.getPlanoPreco()), 
                                    assinante.getCreditosPrincipal(), Definicoes.TECNOMEN_LING_PORTUGUES, operador, statusProcesso, retorno);
                
                // Atualiza o status e concede créditos pré-desativação
                if(retorno == Definicoes.RET_OPERACAO_OK)
                {
                    atualizarStatusAssinante(assinante.getMSISDN(), assinante.getStatusAssinante(), (Date)null, operador);
                    
                    Ajustar ajuste = new Ajustar(super.getIdLog());
                    retorno = ajuste.executarAjuste(msisdnNovo, 
                                                    Definicoes.AJUSTE_REATIVACAO,
                                                    Definicoes.TIPO_CREDITO_REAIS,
                                                    assinante.getValoresRecarga(),
                                                    Definicoes.TIPO_AJUSTE_CREDITO,
                                                    Calendar.getInstance().getTime(), 
                                                    Definicoes.SO_GPP, 
                                                    operador,
                                                    assinante, 
                                                    null,
                                                    true,
                                                    null);
                }
            }               
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "reativarAssinante", "Erro ao reativar cliente: " + e);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
        
        return retorno;
    }    
    /**
     * Consulta o status do cliente antes de sua desativação
     * @param msisdnAntigo  Msisdn com o qual o cliente foi desativado
     * @return assinante    Objeto com dados do assinante antes da desativação
     */
    private Assinante consultarUltimoStatus(String msisdnAntigo)
    {
        PREPConexao conexaoBanco = null;
        ResultSet resultado = null;
        Assinante assinante = new Assinante();
        
        try
        {           
            conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.logId);
            
            String dataDesativacao = getDataDesativacao(msisdnAntigo, conexaoBanco);
            
            if(dataDesativacao != null)
            {
            
                String consultaStatus   = " SELECT recarga.idt_msisdn, recarga.vlr_credito_principal,                    "
                                        + "        recarga.vlr_credito_bonus, recarga.vlr_credito_sms,                   "
                                        + "        recarga.vlr_credito_gprs, assinante.sub_id, assinante.imsi,           "
                                        + "        assinante.status, assinante.plano, assinante.saldo_principal,         "
                                        + "        assinante.expiracao_principal, assinante.saldo_bonus,                 "
                                        + "        assinante.expiracao_bonus, assinante.saldo_sms,                       "
                                        + "        assinante.expiracao_sms, assinante.saldo_dados,                       "
                                        + "        assinante.expiracao_dados, assinante.service_status                   "
                                        + "   FROM (SELECT recarga.idt_msisdn, recarga.vlr_credito_principal,            "
                                        + "                recarga.vlr_credito_bonus, recarga.vlr_credito_sms,           "
                                        + "                recarga.vlr_credito_gprs                                      "
                                        + "           FROM tbl_rec_recargas recarga                                      "
                                        + "          WHERE recarga.idt_msisdn = ?                                        "
                                        + "            AND recarga.dat_origem >= to_date(?,'dd/mm/yyyy')                 "
                                        + "            AND recarga.tip_transacao = ?) recarga,                           "
                                        + "        (SELECT sub_id, imsi, account_status status, profile_id plano,        "
                                        + "                account_balance/100000 saldo_principal,                       "
                                        + "                to_char(expiry,'dd/mm/yyyy') expiracao_principal,             "
                                        + "                bonus_balance/100000 saldo_bonus,                             "
                                        + "                to_char(bonus_expiry,'dd/mm/yyyy') expiracao_bonus,           " 
                                        + "                sm_balance/100000 saldo_sms,                                  "
                                        + "                to_char(sm_expiry,'dd/mm/yyyy') expiracao_sms,                " 
                                        + "                data_balance/100000 saldo_dados,                              "
                                        + "                to_char(data_expiry,'dd/mm/yyyy') expiracao_dados,            " 
                                        + "                service_status                                                "
                                        + "           FROM tbl_apr_assinante_tecnomen assinante                          "
                                        + "          WHERE assinante.sub_id = ?                                          "
                                        + "            AND assinante.dat_importacao = to_date(?,'dd/mm/yyyy')) assinante "
                                        + "  WHERE assinante.sub_id = recarga.idt_msisdn(+)                              ";
                
                Object parametros [] = {msisdnAntigo,dataDesativacao,Definicoes.AJUSTE_DESATIVACAO,msisdnAntigo,dataDesativacao};
                            
                resultado = conexaoBanco.executaPreparedQuery(consultaStatus, parametros, super.logId);
                
                if(resultado.next())
                {
                    String msisdn = resultado.getString("idt_msisdn");
                    
                    // Saldo que foi retirada por motivo da desativação
                    if(msisdn != null)
                    {
                        assinante.setSaldoCreditosPrincipal(resultado.getDouble("vlr_credito_principal"));
                        assinante.setSaldoCreditosBonus(resultado.getDouble("vlr_credito_bonus"));
                        assinante.getValoresRecarga().setValorBonusSMS(resultado.getDouble("vlr_credito_sms"));
                        assinante.getValoresRecarga().setValorBonusGPRS(resultado.getDouble("vlr_credito_gprs"));                   
                    }
                    // Pega o saldo mais recente da importação de assinante
                    else
                    {
                        msisdn = resultado.getString("sub_id");
                        assinante.setSaldoCreditosPrincipal(resultado.getDouble("saldo_principal"));
                        assinante.setSaldoCreditosBonus(resultado.getDouble("saldo_bonus"));
                        assinante.getValoresRecarga().setValorBonusSMS(resultado.getDouble("saldo_sms"));
                        assinante.getValoresRecarga().setValorBonusGPRS(resultado.getDouble("saldo_dados"));
                    }
                    
                    assinante.setMSISDN(msisdn);
                    assinante.setIMSI(resultado.getString("imsi"));
                    assinante.setPlanoPreco(resultado.getShort("plano"));
                    assinante.setStatusAssinante(resultado.getShort("status"));
                    assinante.setStatusServico(resultado.getShort("service_status"));
                    assinante.setDataExpiracaoPrincipal(resultado.getString("expiracao_principal"));
                    assinante.setDataExpiracaoBonus(resultado.getString("expiracao_bonus"));
                    assinante.setDataExpiracaoSMS(resultado.getString("expiracao_sms"));
                    assinante.setDataExpiracaoDados(resultado.getString("expiracao_dados"));
                }
                
                resultado.close();  
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "consultarUltimoStatus", "Erro ao efetuar consulta: " + e);
        }
        finally
        {           
            super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.logId);
        }       
        
        return assinante;       
    }
    
    /**
     * Consulta a data mais recente de desativação do cliente
     * @param msisdn            Msisdn a consultar
     * @param conexaoBanco      Conexão com o Banco de Dados
     * @return dataDesativacao  Data de desativação do cliente
     * @throws GPPInternalErrorException 
     * @throws SQLException 
     * @throws Exception
     */
    private String getDataDesativacao(String msisdn, PREPConexao conexaoBanco) throws GPPInternalErrorException, SQLException
    {
        String dataDesativacao = null;
        
        String consultaDesativacao  = "SELECT to_char(MAX(dat_aprovisionamento),'dd/mm/yyyy') data_desativacao "
                                    + "  FROM tbl_apr_eventos                                                  "
                                    + " WHERE idt_msisdn = ?                                                   "
                                    + "   AND tip_operacao = ?                                                 ";
        
        Object parametros [] = {msisdn,Definicoes.TIPO_APR_DESATIVACAO};
        
        ResultSet desativacao = conexaoBanco.executaPreparedQuery(consultaDesativacao, parametros, super.getIdLog());
        
        if(desativacao.next())
            dataDesativacao = desativacao.getString("data_desativacao");
        
        desativacao.close();    
        
        return dataDesativacao;
    }
    
    /**
     * Opera um assinante colocando-o na promoção configurada na tabela de configuracoes do GPP.
     * Esta operacao se dah com o conhecimento previo do idtPromocao a qual o assinante deverah
     * ser inserido
     * 
     * @param msisdn
     * @param idtPromocao
     * @param operador
     * @return
     */
    private short operarPromocaoFaleGratisANoite(String msisdn, int idtPromocao, String operador)
    {
        short retorno = 0;
        Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());
        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(this.logId);
        // Trocando o pula-pula do assinante para o FGN
        retorno = controle.trocaPulaPula(msisdn, idtPromocao, dataProcessamento, operador,
                Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PROMOCAO);             
        return retorno;     

    }
    
    /**
     * Opera um assinante colocando-o na promoção configurada na tabela de configuracoes do GPP.
     * Esta operacao se dah com o conhecimento previo do ELM da promocao
     * 
     * @param msisdn
     * @param promocao
     * @param operador
     * @return
     */
    private short operarPromocaoFaleGratisANoite(String msisdn, String promocao, String operador)
    {
        short retorno = 0;
        int idtPromocao = 0;

        MapPromocao mapPromocao = MapPromocao.getInstancia();
        idtPromocao = mapPromocao.getIdPromocao(promocao);
        retorno = operarPromocaoFaleGratisANoite(msisdn, idtPromocao, operador);             

        return retorno;     
    }

    /**
     * Insere a Promocao "Fale Gratis a Noite" para o assinante informado
     * @param msisdn     Assinante a ser cadastrado na promocao Fale Gratis a Noite
     * @param promocao   Promocao CRM do assinante
     * @param operador   Operador que requesita o cadastro na promocao Fale Gratis a Noite
     * @return
     * @see     com.brt.gpp.componentes.aprovisionamento.ComponenteNegocioAprovisionamento.inserePulaPula
     */
    public short inserirPromocaoFaleGratisANoite(String msisdn,String promocao, String operador)
    {
        super.log(Definicoes.INFO, "inserirPromocaoFaleGratisANoite", "Inicio MSISDN: " + msisdn + " Promocao: " + promocao);
        short retorno = -1;
        
        // Analize de status
        Assinante assinante;
        try
        {
            // Consulta a plataforma para verificar se o assiante existe
            assinante = this.consultaAssinante(msisdn);
            if ((assinante != null ) && (assinante.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO))
            {
                /* Confere se o status do assinante nao o impede de entrar nas promocoes FGN
                short status = assinante.getStatusAssinante();
                if((status == Definicoes.STATUS_RECHARGE_EXPIRED ||
                    status == Definicoes.STATUS_DISCONNECTED ))
                    return Definicoes.RET_MSISDN_BLOQUEADO; */
            }
            else
            {
                retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            }
        }
        catch (GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "inserirPromocaoFaleGratisANoite", "Falha ao inserir o MSISDN:"+ msisdn +
                      " no FGN " + promocao + " com o Erro: " + e.toString());
            return Definicoes.RET_ERRO_GENERICO_TECNOMEN;
        }
        
        // INICIO: Adequacao para o fato de ser recebido o mesmo ELM do CRM
        try
        {
            MapPlanoPreco mapPlanoPreco = MapPlanoPreco.getInstancia();
            int idtCategoria = mapPlanoPreco.consultaCategoria(assinante.getPlanoPreco());
            promocao = promocao + idtCategoria;
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, "inserirPromocaoFaleGratisANoite", "Erro ao preencher mapeamento: " + e.toString());
            return Definicoes.RET_ERRO_GENERICO_GPP;
        }
        // FIM: Adequacao para o fato de ser recebido o mesmo ELM do CRM

        retorno = this.operarPromocaoFaleGratisANoite(msisdn, promocao, operador);
        super.log(Definicoes.INFO, "inserirPromocaoFaleGratisANoite", "Fim RETORNO " + retorno +
                " MSISDN: " + msisdn + " promocao: " + promocao);
        
        if(retorno == Definicoes.RET_OPERACAO_OK)
        {
    		GerentePoolBancoDados gerDB = GerentePoolBancoDados.getInstancia(super.logId);
    		PREPConexao conexaoPrep = null;
    		try
    		{
    			conexaoPrep = gerDB.getConexaoPREP(super.logId);
    			this.enviarSMS(msisdn, Definicoes.SMS_FGN_ATIVACAO, conexaoPrep);
    		}
    		catch(Exception e)
    		{
    			super.log(Definicoes.ERRO,Definicoes.CL_APROVISIONAR,"Erro interno. Excecao:"+e);
    		}
    		finally
    		{
    			gerDB.liberaConexaoPREP(conexaoPrep,super.logId);
    		}

        }
        return retorno;     
    }
	/**
	 * Envia um SMS para um dado assinante informando a entrada em NormalUser
	 * @param assinantePulaPula
	 * @throws Exception
	 */
	private boolean enviarSMS(String msisdn, String tipoExecucao, PREPConexao conexaoPrep) throws Exception
	{
		AssinantePulaPula assinantePulaPula;
		ControlePulaPula controlePulaPula = new ControlePulaPula(super.logId);
        assinantePulaPula = controlePulaPula.consultaPromocaoPulaPula(msisdn, 
        															  null, 
        															  false, 
        															  false, 
        															  conexaoPrep);

    	ConsumidorSMS consumidorSMS = ConsumidorSMS.getInstance(super.logId);
		boolean retorno = false;
        PromocaoInfosSms infosSms = 
        	assinantePulaPula.getInfosSms(tipoExecucao).iterator().hasNext() ? 
        		(PromocaoInfosSms)assinantePulaPula.getInfosSms(tipoExecucao).iterator().next() : null;
		String mensagem = null;
		int prioridade  = 0;
		String tipo     = null;
        if((infosSms != null) && (infosSms.enviaSms()))
        {
            tipo        = infosSms.getTipSms();
            prioridade  = infosSms.getNumPrioridade();
            mensagem    = infosSms.getDesMensagem();

        	retorno = consumidorSMS.gravarMensagemSMS(msisdn, mensagem, prioridade, tipo,conexaoPrep);
        }

        return retorno;
	}
	

    /**
     * Retira a Promocao "Fale Gratis a Noite" para o assinante informado
     * @param msisdn   Assinante a ser retirado da promocao Fale Gratis a Noite
     * @param operador Operador que requesita a retirada na promocao Fale Gratis a Noite
     * @return
     */
    public short retirarPromocaoFaleGratisANoite(String msisdn, String operador)
    {
        super.log(Definicoes.INFO, "retirarPromocaoFaleGratisANoite", "Inicio MSISDN: " + msisdn);
        short retorno   = -1;
        int idtPromocao =  0;
        
        try
        {
            // Consulta a plataforma para verificar se o assiante existe
            Assinante assinante = this.consultaAssinante(msisdn);
            if ((assinante != null ) && (assinante.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO))
            {
                MapPlanoPreco mapPlanoPreco = MapPlanoPreco.getInstancia();
                MapConfiguracaoGPP mapConfiguracaoGPP = MapConfiguracaoGPP.getInstance();
                int idtCategoria = mapPlanoPreco.consultaCategoria(assinante.getPlanoPreco());
                switch (idtCategoria)
                {
                    case Definicoes.CATEGORIA_HIBRIDO:
                    	idtPromocao = Integer.parseInt(mapConfiguracaoGPP.getMapValorConfiguracaoGPP(
                    			"PROMOCAO_PULA_PULA_CONTROLE_VIGENTE"));
                    	break;
                    case Definicoes.CATEGORIA_PREPAGO:
                    	idtPromocao = Integer.parseInt(mapConfiguracaoGPP.getMapValorConfiguracaoGPP(
            					"PROMOCAO_PULA_PULA_PRE_PAGO_VIGENTE"));
                        break;
                    default:
                        retorno = Definicoes.RET_ACAO_INEXISTENTE ;
                        break;
                }
                if (retorno != Definicoes.RET_ACAO_INEXISTENTE)
                {
                    retorno = this.operarPromocaoFaleGratisANoite(msisdn, idtPromocao, operador);               
                }
            }
            else
            {
                retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            }
        }
        catch(GPPInternalErrorException e)
        {
            super.log(Definicoes.ERRO, "operarPromocaoFaleGratisANoite", "Falha ao operar o MSISDN:"+ msisdn +
                      " com o Erro: " + e.toString());
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
        super.log(Definicoes.INFO, "retirarPromocaoFaleGratisANoite", "Fim RETORNO " + retorno +
                " MSISDN: " + msisdn);
        return retorno;     
    }
    
    /**
     * Metodo...: atualizaAmigosDeGraca
     * Descricao: Atualiza os amigos da promocao fale de graca para os assinantes pos-pago 
     * @param   MSISDN - Assinante pós pago que terá seus 2 amigos atualizados
     *          listaMSISDN - Dois Msisdn separados por ; com os quais o assinante pós-pago irá falar de graça
     *          operador - Operador que realizou a atualizacao dos amigos
     *          codigoServico - Código de serviço que esta relacionado a promocao   
     * @return  short - codigo de retorno (0 - Sucesso, 9998 - Erro Generico GPP, 94 - Erro de fomato do MSISDN, 1 - Assinante Pre-Pago nao pode participar da promocao, 38 - Parametro nao informado )
     */
    public short atualizaAmigosDeGraca (String MSISDN, String listaMSISDN, String operador, String codigoServico) throws GPPInternalErrorException
    {
    	short retorno = Definicoes.RET_ERRO_GENERICO_GPP;
    	GerentePoolBancoDados gerDB = GerentePoolBancoDados.getInstancia(super.logId);
		PREPConexao conexaoPrep = null;
		AssinantePosPago assinantePosPagoAntigo = null;
		AssinantePosPago assinantePosPagoNovo = new AssinantePosPago();
    	
    	try
    	{
    		MapPromocao mapa = MapPromocao.getInstancia();
    		//Valida os parametros de entrada
    		assinantePosPagoNovo.setMsisdn(MSISDN);
    		assinantePosPagoNovo.setListaAmigos(listaMSISDN);
    		assinantePosPagoNovo.setAtivo(true);
    		assinantePosPagoNovo.setDataInclusao(Calendar.getInstance().getTime());
    		int promAssinante = mapa.getIdPromocao(codigoServico);
    		assinantePosPagoNovo.setPromocao(mapa.getPromocao(promAssinante == -1 ? Definicoes.PROMOCAO_FALE_GRATIS_POSPAGO : promAssinante));
    		
    		if((retorno = validarAmigosDeGraca(assinantePosPagoNovo)) == Definicoes.RET_OPERACAO_OK)
    		{
    			//Instancia classe responsavel pela persistencia dos dados na tabela tbl_apr_assinante_pospago
    			AssinantePosPagoDAO assinantePosPagoDAO = new AssinantePosPagoDAO(super.logId);
    			conexaoPrep = gerDB.getConexaoPREP(super.logId);
    			
    			//Consulta os dados do assinante caso ele ja exista na tabela
    			assinantePosPagoAntigo = assinantePosPagoDAO.consultarAssinante(conexaoPrep, assinantePosPagoNovo.getMsisdn());
    			
    			//Se o assinante ja existir, entao realiza uma atualizacao dos dados
    			if(assinantePosPagoAntigo != null)
    				assinantePosPagoDAO.atualizarAmigosDeGraca(conexaoPrep, assinantePosPagoNovo);
    			else //caso contrario, o assinante nao existir, realiza-se uma insercao de seus dados na tabela
    				assinantePosPagoDAO.inserirAmigosDeGraca(conexaoPrep, assinantePosPagoNovo);	
    		}
    	}
    	catch(Exception e)
    	{
    		retorno = Definicoes.RET_ERRO_TECNICO; 
    		super.log(Definicoes.ERRO, "atualizaAmigosDeGraca", "Erro ao atualizar os dados do assinante "+MSISDN+": "+e);
    	}
    	finally
    	{
    		try
    		{
    			if(conexaoPrep == null)
    				conexaoPrep = gerDB.getConexaoPREP(super.logId);
    			
    			//Chama o metodo responsavel pela persistencia dos dados de registro da atualizacao na tabela tbl_apr_eventos
    			AprEventosDAO aprEventos = new AprEventosDAO(super.logId);
    			aprEventos.registraAtualizacaoAmigosDeGraca(conexaoPrep, assinantePosPagoNovo, assinantePosPagoAntigo, operador , retorno);
    		}
    		catch(Exception e)
    		{
    			super.log(Definicoes.WARN, "atualizaAmigosDeGraca", "Nao foi possivel registrar o evento de atualizacao dos amigos do assinante "+MSISDN+" na TBL_APR_EVENTOS. "+e);
    		}
    		finally
    		{
    			gerDB.liberaConexaoPREP(conexaoPrep, super.logId);
    		}
    	}
    	
    	return retorno;
    }
    
    /**
     * Metodo...: validaAmigosDeGraca
     * Descricao: Valida os parametros do metodo atualizaAmigosDeGraca da promocao fale de graca para os assinantes pos-pago. 
     * @param   MSISDN - Assinante pós pago que terá seus 2 amigos atualizados
     *          listaMSISDN - Dois Msisdn separados por ; com os quais o assinante pós-pago irá falar de graça
     *          operador - Operador que realizou a atualizacao dos amigos
     *          codigoServico - Código de serviço que esta relacionado a promocao   
     * @return  short - codigo de retorno (0 - Sucesso, 9998 - Erro Generico GPP, 94 - Erro de fomato do MSISDN, 1 - Assinante Pre-Pago nao pode participar da promocao, 38 - Parametro nao informado )
     */
    public short validarAmigosDeGraca (AssinantePosPago assinantePosPago) throws Exception
    {
    	Collection novaLista = new ArrayList();
        //Validando a lista de MSISDN de amigos
		for(Iterator i=assinantePosPago.getAmigosGratis().iterator(); i.hasNext();)
		{
			String amigo = (String)i.next();
			
			// Verifica se o amigo possui a mascara correspondente a um numero Fixo
			// ou a um numero GSM BrT e se nao possui o 55. Caso seja valido
			// entao eh necessario inserir o 55 no numero ao inserir como amigo.
			if(amigo.matches("^0?\\d{2}(([23456]\\d)|(8[45]))\\d{6}$"))
				amigo = amigo.replaceFirst("^0?", "55");
			else
				// Caso o amigo possua o formato correto GSM e fixo BrT inclusive
				// com o 55, entao o amigo estah validado, caso contrario termina
				// a validacao do metodo
				if (!amigo.matches("^55\\d{2}(([23456]\\d)|(8[45]))\\d{6}$"))
				{
					super.log(Definicoes.DEBUG, "validaAmigosDeGraca", "Formato invalido de MSISDN do amigo "+amigo+" do assinante "+ assinantePosPago.getMsisdn());
					return Definicoes.RET_FORMATO_MSISDN_INVALIDO;
				}
			
			novaLista.add(amigo);
		}
		
		assinantePosPago.setAmigosGratis(novaLista);
		
		//Validando o MSISDN do assinante		
		if(!assinantePosPago.getMsisdn().matches("^55\\d{2}8[45]\\d{6}$"))
		{	
			super.log(Definicoes.DEBUG, "validaAmigosDeGraca", "Formato invalido de MSISDN do assinante: "+assinantePosPago.getMsisdn());
			return Definicoes.RET_FORMATO_MSISDN_INVALIDO;
		}
		
		//Validando se o MSISDN do assinante não é pré-pago
		Assinante retornoAssinante = this.consultaAssinante(assinantePosPago.getMsisdn());
		if(retornoAssinante != null)
			if(retornoAssinante.getRetorno() != Definicoes.RET_MSISDN_NAO_ATIVO)
		    { 	
				super.log(Definicoes.DEBUG, "validaAmigosDeGraca", "Assinante "+assinantePosPago.getMsisdn()+" eh prepago e nao pode adicionar amigos.");
				return Definicoes.RET_MSISDN_JA_ATIVO;
		    }
		
    	return Definicoes.RET_OPERACAO_OK;
    }

    /**
     * 
     * @param xml Para multiplos assinantes
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public short ativarAssinante(String xml) throws ParserConfigurationException, SAXException, IOException{
    	short retorno = Definicoes.RET_OPERACAO_OK;
 		if (xml == null)
		{
 			retorno = ((short)Definicoes.RET_XML_MAL_FORMADO);
			return retorno;
		}
		// Cria um parser de XML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));

		// Executa o parser 
		Document doc = parse.parse(is);
		// Interpreta os elementos
		Element rootMensagem = (Element) doc.getElementsByTagName( "mensagem" ).item(0);
		Element root = getRootCDATA(rootMensagem,"conteudo","root");

		NodeList ativacoes = root.getElementsByTagName("GPPAtivacaoAssinante");
		int size = ativacoes.getLength();
		for (int i = 0; i<size;i++)
		{
			Element ativacao = (Element)ativacoes.item(i);
	    	GPPAtivacaoAssinante gPPAtivacaoAssinante = GPPAtivacaoAssinante.parseXML(ativacao); 
		    retorno = this.ativarAssinante(gPPAtivacaoAssinante);
			if(retorno != Definicoes.RET_OPERACAO_OK)
	    		return retorno;
		}
		return retorno;
    }

    /**
     * Ativa um unico assinante
     * @param gPPAtivacaoAssinante
     * @return
     */
    public short ativarAssinante(GPPAtivacaoAssinante gPPAtivacaoAssinante)
    {
    	short retorno = Definicoes.RET_OPERACAO_OK;
    	TecnomenAprovisionamento conexaoTecnomen = null;
    	PREPConexao conexaoPrep = null;
    	try 
    	{
    		conexaoTecnomen = this.gerenteTecnomen
			.getTecnomenAprovisionamento(super.getIdLog());
    		conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
	    	AtivacaoAssinante ativacaoAssinante = new AtivacaoAssinante(this.logId,conexaoTecnomen, conexaoPrep);
	    	retorno = ativacaoAssinante.ativar(gPPAtivacaoAssinante);
		}
    	catch (GPPInternalErrorException e)
    	{
			retorno = Definicoes.RET_ERRO_GENERICO_GPP;
		}
    	finally
    	{
    		if (conexaoTecnomen!= null)
    			this.gerenteTecnomen.liberaConexaoAprovisionamento(conexaoTecnomen, super.getIdLog());
    		if (conexaoPrep != null)
    			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
    	}
		return retorno;
    }
    
    /**
     * Extrai o Eletento indicado por tagRoot que estah dentro da tagConteudo que possui um bloco CDATA 
     * @param rootGeral
     * @param tagConteudo
     * @param tagRoot
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
	private Element getRootCDATA(Element rootGeral,String tagConteudo, String tagRoot) throws ParserConfigurationException, SAXException, IOException
	{
		String entrada = null;
		Element conteudo = (Element)rootGeral.getElementsByTagName(tagConteudo).item(0);
		
		if(conteudo == null)
			throw new IllegalArgumentException("Tag \n" + tagConteudo + " nao encontrada.");
		
		NodeList list = conteudo.getChildNodes();
		for (int i = 0; i < list.getLength(); i++)
		{
			String strNode = list.item(i).getNodeValue();
			if (strNode != null && strNode.trim().length() > 0)
			{
				entrada = strNode;
				break;
			}		
		}
		if(entrada == null)
			throw new IllegalArgumentException("conteudo invalido");

		// Cria um parser de XML
		DocumentBuilderFactory docBuilder = DocumentBuilderFactory.newInstance();
		DocumentBuilder parse = docBuilder.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(entrada));
		
		// Executa o parser 
		Document doc = parse.parse(is);

		// Interpreta os elementos
		Element root = (Element) doc.getElementsByTagName( tagRoot ).item(0);
		return root;
	}
}