// Definicao do Pacote
package com.brt.gpp.componentes.aprovisionamento;

//Arquivos de Import Internos
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.aprovisionar.AssinanteXMLParser;
import com.brt.gpp.aplicacoes.aprovisionar.ContingenciaCRM;
import com.brt.gpp.aplicacoes.aprovisionar.DadosSenha;
import com.brt.gpp.aplicacoes.aprovisionar.ativacao.GPPAtivacaoAssinante;
import com.brt.gpp.aplicacoes.consultar.consultaCredito.DesativaAssinante;
//import com.brt.gpp.aplicacoes.enviarMensagemTangram.EnvioMensagemTangram;
import com.brt.gpp.aplicacoes.enviarSMS.dao.EnvioSMSDAO;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.DadosSMS;
import com.brt.gpp.aplicacoes.enviarSMS.entidade.TipoSMS;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.aplicacoes.promocao.estornoExpurgoPulaPula.AprovacaoLote;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.aplicacoes.recarregar.ParametrosRecarga;
import com.brt.gpp.aplicacoes.recarregar.RecargaDAO;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPOA;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;
import com.brt.gpp.comum.gppExceptions.GPPCorbaException;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapPromocao;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;
import com.brt.gpp.gerentesPool.ConsumidorSMS;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
  *
  * Este arquivo contem a definicao da classe componente de negocio de Aprovisionamento. Ela
  * e responsavel pela logica de negocio para o aprovisionamento de assinantes na plataforma
  * pre-pago na tecnomen.
  *
  * @autor:             Daniel Cintra Abib
  * @Data:              28/02/2004
  *
  * Modificado Por: Alberto Magno
  * Data: 18/11/2004
  * Razao: Encerrar ajuste de desativação para usuário em status First_time_user
  */
public class ComponenteNegocioAprovisionamento extends aprovisionamentoPOA
{
    // Variaveis Membro
    protected GerentePoolTecnomen       gerenteTecnomen     = null; // Gerente de conexoes Tecnomen
    protected GerentePoolLog            log                 = null; // Gerente de LOG
    protected ArquivoConfiguracaoGPP    arqConfiguracao     = null; // Referencia ao arquivo de configuracao

    /**
     * Metodo...: ComponenteNegocioAprovisionamento
     * Descricao: Construtor
     * @param
     * @return
     */
    public ComponenteNegocioAprovisionamento ( )
    {
        // Obtem referencia ao gerente de LOG
        this.log = GerentePoolLog.getInstancia(this.getClass());

        log.logComponente (Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, ": Componente de Negocio ativado..." );

    }

    /**
     * Metodo...: ativaAssinante
     * Descricao: Ativa um assinante na plataforma Tecnomen
     * @param   MSISDN          - Numero do assinante
     * @param   aIMSI           - Numero do IMSI (SimCard) do assinante
     * @param   aPlanoPreco     - Plano de preco do assinante
     * @param   aCreditoInicial - Valor inial de credito
     * @param   aIdioma         - Idioma de acesso a URA
     * @param   aOperador       - Codigo do operador que esta realizando a ativacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short ativaAssinante ( String aMSISDN, String aIMSI, String aPlanoPreco, double aCreditoInicial, short aIdioma, String aOperador ) throws  GPPInternalErrorException, GPPTecnomenException
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        //Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);
        short retorno = Definicoes.RET_ERRO_GENERICO_GPP;

    	GPPAtivacaoAssinante gPPAtivacaoAssinante = new GPPAtivacaoAssinante();
    	Assinante assinante = new Assinante();
    	assinante.setMSISDN(aMSISDN);
    	assinante.setIMSI(aIMSI);
    	assinante.setPlanoPreco(Short.parseShort(aPlanoPreco));
    	assinante.setSaldoCreditosPrincipal(aCreditoInicial);
    	assinante.setIdioma(aIdioma);
    	assinante.setStatusAssinante(Definicoes.FIRST_TIME_USER);
    	
    	gPPAtivacaoAssinante.setAssinante(assinante);
    	gPPAtivacaoAssinante.setOperador(aOperador);

    	retorno = aprovisionar.ativarAssinante(gPPAtivacaoAssinante);
		return retorno;
    }

    /**
     * Metodo...: desativaAssinante
     * Descricao: Desativa um assinante na plataforma Tecnomen
     *
     * @modify	Alteracao no fluxo da Desativacao, setando a conexao com AutoCommit false,
     * 			garantindo que a desativacao completa tera sucesso
     * @author  João Paulo Galvagni
     * @since	27/08/2007
     *
     * @param   MSISDN          - Numero do assinante
     * @param   aIMSI           - Numero do IMSI (SimCard) do assinante
     * @param   aPlanoPreco     - Plano de preco do assinante
     * @param   aCreditoInicial - Valor inial de credito
     * @param   aIdioma         - Idioma de acesso a URA
     * @param   aOperador       - Codigo do operador que esta realizando a operacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public retornoDesativacaoAssinante desativaAssinante ( String aMSISDN, String motivoDesativacao, String aOperador ) throws  GPPInternalErrorException, GPPTecnomenException
    {
        retornoDesativacaoAssinante retorno = new retornoDesativacaoAssinante();
        long 		idProcesso 	 = 0;
        double 		saldo 		 = 0;
        short 		planoPreco   = 0;
        String 		sucessoFalha = Definicoes.PROCESSO_ERRO;
        Timestamp 	data 		 = new Timestamp(Calendar.getInstance().getTimeInMillis());
        PREPConexao conexaoPrep  = null;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"desativaAssinante", "Inicio MSISDN "+aMSISDN+" MOTIVO "+motivoDesativacao+" OPERADOR "+aOperador);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        // Consulta a plataforma para verificar se o assiante existe e armazenar o credito residual
        Assinante retornoAssinante = aprovisionar.consultaAssinante(aMSISDN);

        if ( retornoAssinante == null )
        {
            // Assinante nao existe, e nao pode ser removido
            retorno.codigoRetorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            retorno.somaSaldos = ("");
        }
        else
        {
            // Armazena o saldo residual
            // Se o motivo de desativação for '03' ou '3': Troca de Plano Pré->Pós
            // O Saldo Residual é somente o saldo principal
            if( motivoDesativacao.equals(Definicoes.MOT_DESAT_PRE_POS) ||
                motivoDesativacao.equals(Definicoes.MOT_DESAT_PRE_POS.substring(1)))
                saldo = retornoAssinante.getCreditosPrincipal();
            //Se um assinante Controle Total migrar para fixa, estah perdendo o saldo periodico.
            //Este cenario nao foi mapeado corretamente!
            else
            {
                saldo = retornoAssinante.getCreditosPrincipal()+
                        retornoAssinante.getCreditosBonus()    +
                        retornoAssinante.getCreditosSms()      +
                        retornoAssinante.getCreditosDados()    +
                        retornoAssinante.getCreditosPeriodico();
            }

            // Armazena o plano de preco
            planoPreco = retornoAssinante.getPlanoPreco();

            // Instancia classe para armazenamento dos valores nos quais sera realizado o ajuste
            ValoresRecarga valores = new ValoresRecarga(-Math.abs(retornoAssinante.getCreditosPrincipal()),
            											-Math.abs(retornoAssinante.getCreditosPeriodico()),
            											-Math.abs(retornoAssinante.getCreditosBonus()),
            											-Math.abs(retornoAssinante.getCreditosSms()),
            											-Math.abs(retornoAssinante.getCreditosDados())
                                                        );

            // Zera os saldos do objeto retornoAssinante, para que os valores
            // de saldo apos o ajuste sejam "0.0"
            retornoAssinante.setSaldoCreditosPrincipal(0.0);
            retornoAssinante.setSaldoCreditosPeriodico(0.0);
            retornoAssinante.setSaldoCreditosBonus(0.0);
            retornoAssinante.setSaldoCreditosSMS(0.0);
            retornoAssinante.setSaldoCreditosDados(0.0);

            try
            {
            	conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
                conexaoPrep.setAutoCommit(false);
                ControlePromocao controle = new ControlePromocao(idProcesso);
                controle.retiraPromocoesAssinante(aMSISDN, data, aOperador, Definicoes.CTRL_PROMOCAO_MOTIVO_DESATIVACAO, conexaoPrep);

                // Atualiza Tabela de Comissionamento
                aprovisionar.atualizarTabelaComissionamento(aMSISDN, conexaoPrep);

                // Instancia um novo objeto ParametrosRecarga e seta os valores dos saldos do assinante
                ParametrosRecarga recarga = ParametrosRecarga.getParametrosAjuste(retornoAssinante);
                recarga.setValores(valores);
                recarga.setDatRecarga(Calendar.getInstance().getTime());
                recarga.setTipoTransacao(Definicoes.AJUSTE_DESATIVACAO);
                recarga.setTipoCredito(Definicoes.TIPO_CREDITO_REAIS);
                recarga.setSistemaOrigem(Definicoes.SO_GPP);
                recarga.setOperador(aOperador);

                // Caso o status do assinante não seja first time user, o cdr de ativação
                // já consta no GPP. Assim, há a necessidade de logar o resíduo na desativação
                if (retornoAssinante.getStatusAssinante() > Definicoes.FIRST_TIME_USER)
                	RecargaDAO.inserirRecargaGSM(recarga, conexaoPrep);

                retorno = aprovisionar.removeAssinante(aMSISDN);

                if (retorno.codigoRetorno == Definicoes.RET_OPERACAO_OK)
                {
	                conexaoPrep.commit();
                	retorno.somaSaldos = String.valueOf(saldo);
	                sucessoFalha = Definicoes.PROCESSO_SUCESSO;
                }
                else
                	conexaoPrep.rollback();
            }
            catch (Exception e)
            {
				// Registra na tabela de aprovisionamento a falha SQL ocorrida no processo
        		if (conexaoPrep != null)
        			try
        			{
        				conexaoPrep.rollback();
        			}
        			catch (SQLException se)
        			{
        				log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "desativaAssinante", se.getMessage());
        			}

            	aprovisionar.gravaDadosDesativacao(aMSISDN, motivoDesativacao, saldo, aOperador, sucessoFalha, retorno.codigoRetorno, data);
            	throw new GPPInternalErrorException(e.getMessage());
			}
            finally
            {
	            if(conexaoPrep != null)
	            	try
	            	{
	            		conexaoPrep.setAutoCommit(true);
	            	}
	            	catch (SQLException se)
	            	{
						log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "desativaAssinante", se.getMessage());
					}
	            	GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep, idProcesso);
            }
        }

        // Grava em tabela os dados os dados de eventos de aprovisionamento
        aprovisionar.gravaDadosDesativacao (aMSISDN, motivoDesativacao, saldo, aOperador, sucessoFalha, retorno.codigoRetorno, data);

        // Verifica se o plano e hibrido
        if (aprovisionar.eHibrido(planoPreco))
            // Grava em tabela os dados de msisdn hibrido
            aprovisionar.removeHibrido(aMSISDN);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"desativaAssinante", "Fim");

        // Libera o ID de processo para o LOG
        log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);

        return retorno;
    }

    /**
     * Metodo...: bloqueiaAssinante
     * Descricao: Bloqueia / Suspende um assinante na plataforma Tecnomen
     * @param   MSISDN          - Numero do assinante
     * @param   idBloqueio      - Identificador do bloqueio
     * @param   tarifa          - Valor da tarifa cobrada
     * @param   aOperador       - Codigo do operador que esta realizando a ativacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short bloqueiaAssinante ( String aMSISDN, String idBloqueio, double tarifa, String aOperador ) throws  GPPInternalErrorException, GPPTecnomenException
    {
        long idProcesso = 0;
        Assinante retornoAssinante = null;
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        //short aPlanoPreco = 0;
        String sucessoFalha = Definicoes.PROCESSO_ERRO;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"bloqueiaAssinante", "Inicio MSISDN "+aMSISDN+" IDBLOQUEIO "+idBloqueio+" TARIFA "+tarifa+" OPERADOR "+aOperador);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        // Consulta o assinante para verificar sua existencia
        retornoAssinante = aprovisionar.consultaAssinante(aMSISDN);
        if ( retornoAssinante != null)
        {
            //aPlanoPreco = retornoAssinante.getPlanoPreco();
            retorno = aprovisionar.validaMotivoBloqueio(idBloqueio);
            if (retorno == Definicoes.RET_OPERACAO_OK)
            {
                // Se o valor da tarifa a ser cobrada for maior que zero
                // So cobra a taxa se o status do assinante nao for pre-ativo
                if(tarifa > 0)
                {
                    if(retornoAssinante.getStatusAssinante() != Definicoes.FIRST_TIME_USER)
                        // Ajusta o credito do assinante (cobranca pelo servico)
                        retorno = new Ajustar(idProcesso).executarAjuste(aMSISDN,
                                                                         Definicoes.AJUSTE_BLOQUEIO,
                                                                         Definicoes.TIPO_CREDITO_REAIS,
                                                                         tarifa,
                                                                         Definicoes.TIPO_AJUSTE_DEBITO,
                                                                         Calendar.getInstance().getTime(),
                                                                         Definicoes.SO_GPP,
                                                                         aOperador,
                                                                         null,
                                                                         null,
                                                                         null,
                                                                         true);
                    else
                        retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
                }

                if(retorno == Definicoes.RET_OPERACAO_OK)
                {
                    try
                    {
                        // Bloqueia/Suspende o Assinante
                        retorno = aprovisionar.bloqueiaAssinante(aMSISDN);
                    }
                    catch (GPPTecnomenException tecEx)
                    {
                        // Registra na tabela de aprovisionamento a falha tecnica ocorrida na tecnomen e entao
                        // dispara a excecao novamente para o cliente
                        retorno = Definicoes.RET_ERRO_TECNICO;
                        aprovisionar.gravaDadosBloqueio (aMSISDN, idBloqueio, tarifa, aOperador, sucessoFalha, retorno);
                        throw new GPPTecnomenException(tecEx.getMessage());
                    }
                }

                if (retorno == 0)
                {
                    sucessoFalha = Definicoes.PROCESSO_SUCESSO;
                }
            }
        }

        // Grava em tabela os dados os dados de eventos de aprovisionamento
        aprovisionar.gravaDadosBloqueio (aMSISDN, idBloqueio, tarifa, aOperador, sucessoFalha, retorno);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"bloqueiaAssinante", "Fim RETORNO "+retorno);

        // Libera o ID de processo para o LOG
        log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);

        return retorno;
    }

    /**
     * Metodo...: desbloqueiaAssinante
     * Descricao: Desbloqueia / Suspende um assinante na plataforma Tecnomen
     * @param   MSISDN          - Numero do assinante
     * @param   idBloqueio      - Identificador do bloqueio
     * @param   tarifa          - Valor da tarifa cobrada
     * @param   aOperador       - Codigo do operador que esta realizando a ativacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short desbloqueiaAssinante ( String aMSISDN, String aOperador ) throws GPPInternalErrorException, GPPTecnomenException
    {
        long idProcesso = 0;
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        String sucessoFalha = Definicoes.PROCESSO_ERRO;
        Assinante retornoAssinante = null;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"desbloqueiaAssinante", "Inicio MSISDN "+aMSISDN+" OPERADOR "+aOperador);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            //Consulta o assinante para verificar sua existencia
            retornoAssinante = aprovisionar.consultaAssinante(aMSISDN);
            if ( retornoAssinante != null )
            {
                // Desbloqueia o Assinante
                retorno = aprovisionar.desbloqueiaAssinante(aMSISDN);
            }
            else
            {
            retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            }
        }
        catch (GPPTecnomenException tecEx)
        {
            // Registra na tabela de aprovisionamento a falha tecnica ocorrida na tecnomen e entao
            // dispara a excecao novamente para o cliente
            aprovisionar.gravaDadosDesbloqueio (aMSISDN, aOperador, sucessoFalha, retorno);
            throw new GPPTecnomenException(tecEx.getMessage());
        }


        if (retorno == 0)
            sucessoFalha = Definicoes.PROCESSO_SUCESSO;

        // Grava em tabela os dados os dados de eventos de aprovisionamento
        aprovisionar.gravaDadosDesbloqueio (aMSISDN, aOperador, sucessoFalha, retorno);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"desbloqueiaAssinante", "Fim RETORNO "+retorno);

        // Libera o ID de processo para o LOG
        log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);

        return retorno;
    }

    /**
     * Metodo...: trocaMSISDNAssinante
     * Descricao: Troca um MSISDN de assiante
     * @param   antigoMSISDN    - Antigo (atual) MSISDN
     * @param   novoMSISDN      - Novo MSIDN
     * @param   idTarifa        - Identificacao do tipo de troca de MSISDN
     * @param   valorTarifa     - Valor cobrado pela troca de MSISDN
     * @param   aOperador       - Codigo do operador que esta realizando a operacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short trocaMSISDNAssinante ( String antigoMSISDN, String novoMSISDN, String idTarifa, double valorTarifa, String aOperador ) throws GPPInternalErrorException, GPPTecnomenException
    {
        // Obtendo um ID de processo para o LOG
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        try
        {
            Aprovisionar aprovisionamento = new Aprovisionar(idProcesso);
            return aprovisionamento.trocarMsisdnAssinante(antigoMSISDN, novoMSISDN, idTarifa, valorTarifa, aOperador);
        }
        catch(Exception e)
        {
            this.log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "trocaMSISDNAssinante", "MSISDN: " + antigoMSISDN + " - Excecao: " +e );
            return Definicoes.RET_ERRO_TECNICO;
        }
    }

    /**
     * Metodo...: trocaPlanoAssinante
     * Descricao: Troca o plano de preco do assinante
     * @param   aMSISDN         - Assinante que trocara o plano de precos
     * @param   novoPlano       - Novo plano de precos
     * @param   valorMudanca    - Valor cobrado pela troca de plano de precos
     * @param   aOperador       - Codigo do operador que esta realizando a operacao
     * @param   valorFranquia   - Valor da Franquia na troca de plano de precos
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public  short trocaPlanoAssinante ( String aMSISDN, String aNovoPlano, double valorMudanca, String aOperador, double valorFranquia ) throws GPPTecnomenException
    {
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        long idProcesso = 0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"trocaPlanoAssinante", "Inicio MSISDN "+aMSISDN+" NOVOPLANO "+aNovoPlano+" VLR "+valorMudanca+" OPERADOR "+aOperador+" VLRFRANQUIA "+valorFranquia);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            //Trocando o plano do assinante.
            retorno = aprovisionar.trocarPlanoPreco(aMSISDN, Short.parseShort(aNovoPlano), valorMudanca, valorFranquia, aOperador);
        }
        catch(Exception e)
        {
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"trocaPlanoAssinante", "Excecao: " + e);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"trocaPlanoAssinante", "Fim RETORNO "+retorno);
        }
        return retorno;
    }

    /**
     * Metodo...: trocaSimCardAssinante
     * Descricao: Troca o SimCard do assinante
     * @param   aMSISDN         - Assinante que trocara o SimCard (IMSI)
     * @param   aNovoIMSI       - Novo SimCard (IMSI)
     * @param   aValorMudanca   - Valor cobrado pela troca de IMSI
     * @param   aOperador       - Codigo do operador que esta realizando a operacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short trocaSimCardAssinante ( String aMSISDN, String aNovoIMSI, double aValorMudanca, String aOperador ) throws GPPInternalErrorException, GPPTecnomenException
    {
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        long idProcesso = 0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            retorno = aprovisionar.trocarImsiAssinante(aMSISDN, aNovoIMSI, aValorMudanca, aOperador);
        }
        catch(Exception e)
        {
            // Registra na tabela de aprovisionamento a falha tecnica ocorrida na tecnomen e entao
            // dispara a excecao novamente para o cliente
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"trocaSimCardAssinante", "Excecao:" + e);
        }

        return retorno;
    }

    /**
     * Metodo...: atualizaFriendsFamilyAssinante
     * Descricao: Atualiza a lista de Friends and Falimy do assinante
     * @param   aMSISDN         - Assinante a atualzar o FF
     * @param   aListaFF        - Nova lista de FF
     * @param   aOperador       - Codigo do operador que esta realizando a operacao
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short atualizaFriendsFamilyAssinante ( String aMSISDN, String aListaFF, String aOperador, String codigoServico) throws GPPInternalErrorException
    {
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"atualizaFriendsFamilyAssinante", "Inicio MSISDN "+aMSISDN+" LISTA "+aListaFF+" OPERADOR "+aOperador);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            retorno = aprovisionar.atualizarFF(aMSISDN, aListaFF, aOperador);
        }
        catch(Exception e)
        {
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"atualizaFriendsFamilyAssinante", "Excecao: " + e);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }

        return retorno;
    }

    /**
     * Metodo...: trocaSenha
     * Descricao: Troca a senha do assinante
     * @param   aXML    - XML com os dados para a troca de senha
     * @return  String  - XML de retorno
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     * @throws  GPPBadXMLFormatException
     */
    public String trocaSenha ( String aXMLEntrada ) throws GPPInternalErrorException,
                                                    GPPBadXMLFormatException
    {
        short retorno = Definicoes.RET_OPERACAO_OK;
        String sucessoFalha = Definicoes.PROCESSO_ERRO;
        long idProcesso = 0;

        String MSISDN="";
        String novaSenha="";

        String XMLSaida=null;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"trocaSenha", "Inicio");

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            // Parse do XML de entrada
            DadosSenha dadosSenha = aprovisionar.parseTrocaSenhaXML(aXMLEntrada);

            MSISDN = dadosSenha.getMSISDN();
            novaSenha = dadosSenha.getSenha();

            // Consulta a plataforma para verificar se o assiante existe
            Assinante retornoAssinante = aprovisionar.consultaDetalhadaAssinante(MSISDN);
            if ( retornoAssinante != null )
            {
                // Consulta o status do assinante para verificar se pode realizar a troca de senha
                retorno = aprovisionar.consultaStatusDisconnectedShutdown(retornoAssinante.getStatusAssinante(), retornoAssinante.getStatusServico() );
                if (retorno == Definicoes.RET_STATUS_MSISDN_VALIDO)
                {
                    // Troca a senha do assinante
                    retorno = aprovisionar.trocaSenha(MSISDN, novaSenha);
                    if ( retorno == Definicoes.RET_OPERACAO_OK )
                        sucessoFalha = Definicoes.PROCESSO_SUCESSO;
                }
            }
            //Assinante nao existe
            else
            {
                retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            }

            // Monta XML Saida
            aprovisionar.setaRetornoTrocaSenha(dadosSenha, retorno);
            XMLSaida = dadosSenha.getXMLSaida();

            // Grava em tabela os dados os dados de eventos de aprovisionamento
            aprovisionar.gravaTrocaSenha (MSISDN, sucessoFalha, retorno);
        }

        catch (GPPBadXMLFormatException e)
        {
            log.log(idProcesso, Definicoes.WARN,Definicoes.CN_APROVISIONAMENTO,"trocaSenha", "BAD_XML_FORMAT:Erro na Troca de Senha do Assinante.");

            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO,Definicoes.PROCESSO_ERRO);
            throw new GPPBadXMLFormatException ("Excecao XMLFormat: " +e);
        }
        finally
        {
            log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"trocaSenha", "Fim");

            // Libera o ID de processo para o LOG
            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);
        }
        return XMLSaida;
    }


    /**
     * Metodo...: resetSenha
     * Descricao: Reseta a senha do assinante
     * @param   aMSISDN - Assinante que requisitou reset de senha
     * @param   aNovaSenha - Nova senha
     * @return  short - retorno com o resultado da operacao ( 0 = sucesso )
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short resetSenha ( String aMSISDN, String aNovaSenha ) throws GPPInternalErrorException
    {
        short retorno = Definicoes.RET_ERRO_GENERICO_TECNOMEN;
        short retornoConsulta = Definicoes.RET_OPERACAO_OK;
        String sucessoFalha = Definicoes.PROCESSO_ERRO;
        long idProcesso = 0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"resetSenha", "Inicio");

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        try
        {
            // Consulta a plataforma para verificar se o assiante existe
            Assinante retornoAssinante = aprovisionar.consultaDetalhadaAssinante(aMSISDN);
            if ( retornoAssinante != null )
            {
                // Consulta o status do assinante para verificar se pode realizar a troca de senha
                retornoConsulta = aprovisionar.consultaStatusDisconnectedShutdown(retornoAssinante.getStatusAssinante(), retornoAssinante.getStatusServico() );
                if (retornoConsulta == Definicoes.RET_STATUS_MSISDN_VALIDO)
                {
                	// Comentado por nao ser utilizada a variavel (27/08/2007 - Galvagni)
                    //boolean ehLigMix = aprovisionar.ehLigMix(retornoAssinante.getPlanoPreco());

                    // Troca a senha do assinante
                    retorno = aprovisionar.resetSenha(aMSISDN, aNovaSenha);
                    if ( retorno == Definicoes.RET_OPERACAO_OK )
                        sucessoFalha = Definicoes.PROCESSO_SUCESSO;
                }
                else
                {
                    retorno = retornoConsulta;
                }
            }
            //Assinante nao existe
            else
            {
                retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
            }

            // Grava em tabela os dados os dados de eventos de aprovisionamento
            aprovisionar.gravaTrocaSenha (aMSISDN, sucessoFalha, retorno);

        }
        finally
        {
            log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"resetSenha", "Fim");

            // Libera o ID de processo para o LOG
            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);
        }
        return retorno;
    }


    /**
     * Metodo: consultaAssiante
     * Descricao: Consulta Assiante na plataforma Tecnomen
     * @param aMSISDN   Identificador do Assinante a consultar
     * @return          XML de dados do Assinante
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public  String consultaAssinante ( String aMSISDN ) throws  GPPInternalErrorException
    {
        String retorno = null;
        long idProcesso=0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Inicio MSISDN "+aMSISDN);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        // Consulta o assinante
        Assinante retornoAssinante = aprovisionar.consultaAssinante(aMSISDN);
        if (retornoAssinante != null)
        {
            retorno = AssinanteXMLParser.getXML(retornoAssinante);
        }
        else
        {
            Assinante assinante = new Assinante();

            //Seta codigo de retorno na estrutura do assinante
            assinante.setRetorno(Short.parseShort(Definicoes.RET_S_MSISDN_NAO_ATIVO));

            //seta o atributo msisdn na estrutura Assinante
            assinante.setMSISDN(aMSISDN);

            retorno = AssinanteXMLParser.getXML(assinante);
        }

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"consultaAssinante", "Fim RETORNO:"+retorno);

        return retorno;
    }

    /**
     * Metodo...: ativacaoCancelamentoServico
     * Descricao: Recebe a notificacao de um cadastro ou cancelamento de um servico
     * @param   MSISDN          - Numero do assinante
     * @param   idServico       - Identificador do servico
     * @param   acao            - Acao a ser efetuada (ativacao 0 - cancelamento 1)
     * @return  short           - Sucesso(0) ou erro (diferente de 0)
     * @throws  GPPInternalErrorException
     * @throws  GPPTecnomenException
     * @throws  GPPCorbaException
     */
    public short ativacaoCancelamentoServico ( String aMSISDN, String idServico, short acao ) throws  GPPInternalErrorException
    {
        long idProcesso = 0;
        short retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        String sucessoFalha = Definicoes.PROCESSO_ERRO;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"ativacaoCancelamentoServico", "Inicio MSISDN "+aMSISDN+" IDSERVICO "+idServico+" ACAO "+acao);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        // Consulta a plataforma para verificar se o assiante existe
        Assinante retornoAssinante = aprovisionar.consultaAssinante(aMSISDN);
        if ( retornoAssinante != null )
        {
            // Consulta se o servico esta cadastrado no banco de dados
            if (aprovisionar.consultaServico(idServico))
            {
                if (acao == Definicoes.EXCLUIR_SERVICO_ASSINANTE)
                {
                    // Verifica se o assinante NAO possui o servico a ser incluido
                    if (!aprovisionar.consultaServicoParaAssinante (aMSISDN, idServico))
                    {
                        // Inclui servico para o assinante
                        retorno = aprovisionar.ativacaoCancelamentoServico(aMSISDN, idServico, acao);
                    }
                    else
                    {
                        // Se o assinante ja tem o servico, respode apenas OK
                        retorno = Definicoes.RET_OPERACAO_OK;
                    }
                }
                else
                {
                    // Verifica se o assinante JA possui o servico a ser incluido
                    if (aprovisionar.consultaServicoParaAssinante (aMSISDN, idServico))
                    {
                        // Exclui servico para o assinante
                        retorno = aprovisionar.ativacaoCancelamentoServico(aMSISDN, idServico, acao);
                    }
                    else
                    {
                        // Se o assinante nao tem o servico, respode apenas OK
                        retorno = Definicoes.RET_OPERACAO_OK;
                    }
                }
                // Atualizacao com sucesso
                sucessoFalha = Definicoes.PROCESSO_SUCESSO;
            }
            else
            {
                retorno = Definicoes.RET_SERVICO_INEXISTENTE;
            }
        }
        else
        {
            // Assinante nao ativo
            retorno = Definicoes.RET_MSISDN_NAO_ATIVO;
        }

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"ativacaoCancelamentoServico", "Fim RETORNO "+retorno);

        // Libera o ID de processo para o LOG
        log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, sucessoFalha);

        return retorno;
    }

    /**
     * Metodo....:bloquearServicos
     * Descricao.:Este metodo realiza o bloqueio de servicos do assinante
     *            como uma contingencia para o CRM (sistema que realiza esse processo)
     * @param msisdn    - MSISDN do assinante
     * @return long     - Id do bloqueio criado
     * @throws GPPInternalErrorException
     */
    public long bloquearServicos (String msisdn) throws GPPInternalErrorException
    {
        // Busca um id para o processo e realiza o log inicial
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "bloquearServicos", "Inicio MSISDN "+msisdn);

        // Inicia referencia para a classe que realizara o bloqueio de servicos do assinante
        // e retorna o id do bloqueio utilizado para envio ao sistema de aprovisionamento
        // (ASAP) para que o portal web PPP faca o registro deste bloqueio no sistema GPP
        ContingenciaCRM bloqCRM = new ContingenciaCRM(idProcesso);
        long idBloqueio = bloqCRM.bloquearServicos(msisdn);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "bloquearServicos", "Fim IDBLOQUEIO "+idBloqueio);
        return idBloqueio;
    }

    /**
     * Metodo....:desativarHotLine
     * Descricao.:Este metodo realiza a retirada de hot line do assinante
     *            como uma contingencia para o CRM (sistema que realiza esse processo)
     * @param msisdn    - MSISDN do assinante
     * @param categoria - Categoria do assinante
     * @return long     - Id da retirada criado
     * @throws GPPInternalErrorException
     */
    public long desativarHotLine (String msisdn, String categoria) throws GPPInternalErrorException
    {
        // Busca um id para o processo e realiza o log inicial
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "desativarHotLine", "Inicio NSISDN "+msisdn+" CATEGORIA "+categoria);

        // Inicia referencia para a classe que realizara o bloqueio de servicos do assinante
        // e retorna o id do bloqueio utilizado para envio ao sistema de aprovisionamento
        // (ASAP) para que o portal web PPP faca o registro deste bloqueio no sistema GPP
        ContingenciaCRM bloqCRM = new ContingenciaCRM(idProcesso);
        long idBloqueio = bloqCRM.desativarHotLine(msisdn,categoria);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "desativarHotLine", "Fim IDBLOQUEIO "+idBloqueio);
        return idBloqueio;
    }

    /**
     * Metodo....:desativarHotLineURA
     * Descricao.:Este metodo realiza a retirada de hot line do assinante
     *            como uma contingencia para o CRM (sistema que realiza esse processo) para a URA
     * @param msisdn    - MSISDN do assinante
     * @param categoria - Categoria do assinante
     * @return long     - Id da retirada criado
     * @throws GPPInternalErrorException
     */
    public long desativarHotLineURA (String msisdn, String categoria) throws GPPInternalErrorException
    {
        // Busca um id para o processo e realiza o log inicial
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "desativarHotLine", "Inicio MSISDN "+msisdn+" CATEGORIA "+categoria);

        // Inicia referencia para a classe que realizara o bloqueio de servicos do assinante
        // e retorna o id do bloqueio utilizado para envio ao sistema de aprovisionamento
        // (ASAP) para que o portal web PPP faca o registro deste bloqueio no sistema GPP
        ContingenciaCRM bloqCRM = new ContingenciaCRM(idProcesso);
        long idBloqueio = bloqCRM.desativarHotLine(msisdn,categoria);

        // Apos realizar o envio do desbloqueio Hot-Line este metodo realiza o insert dos dados
        // da atividade em banco de dados para registro
        bloqCRM.insereOSDesativaHotLine(idBloqueio,msisdn,bloqCRM.getPlanoPrecoAssinante(msisdn));

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "desativarHotLine", "Fim IDBLOQUEIO "+idBloqueio);
        return idBloqueio;
    }

    /**
     * Metodo....:confirmaBloqueioDesbloqueioServicos
     * Descricao.:Este metodo recebe do servidor de socket o xml de aprovisionamento
     *            contendo a confirmacao do bloqueio/desbloqueio de servicos de aprovisionamento
     * @param xmlAprovisionamento   - XML de aprovisionamento
     * @throws GPPInternalErrorException
     */
    public void confirmaBloqueioDesbloqueioServicos(String xmlAprovisionamento) throws GPPInternalErrorException
    {
        // Busca um id para o processo e realiza o log inicial
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "confirmaBloqueioDesbloqueioServicos", "Inicio");

        ContingenciaCRM bloqCRM = new ContingenciaCRM(idProcesso);
        bloqCRM.confirmaBloqueioDesbloqueioServicos(xmlAprovisionamento);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "confirmaBloqueioDesbloqueioServicos", "Fim");
    }

    /**
     * Metodo...: enviaSMS
     * Descricao: Envia um SMS para o assinante para iniciar o teste do serviço
     * @param   MSISDN      - Numero do assinante
     * @param   mensagem    - Mensagem a ser enviada
     * @throws  GPPInternalErrorException
     */
    public void enviarSMS ( String aMSISDN, String mensagem ) throws  GPPInternalErrorException
    {
    	DadosSMS sms = new DadosSMS();
    	sms.setIdtMsisdn(aMSISDN);
    	sms.setDesMensagem(mensagem);

    	long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        GerentePoolBancoDados pool = GerentePoolBancoDados.getInstancia(idProcesso);
        PREPConexao con = pool.getConexaoPREP(idProcesso);

        EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, con);

        try
        {
        	dao.inserirSMS(sms);
        }
        finally
        {
        	if(con != null)
        		pool.liberaConexaoPREP(con, idProcesso);
        }
    }

    /**
     * Metodo...: enviaSMSMulti
     * Descricao: Envia um SMS para o assinante para iniciar o teste do serviço
     * @param   listaMsisdn - Lista de números de assinantes
     * @param   mensagem    - Mensagem a ser enviada
     * @param   operador    - Operador responsável pelo broadcast de sms
     * @throws  GPPInternalErrorException
     */
    public void enviarSMSMulti ( String listaMsisdn, String mensagem, String operador ) throws  GPPInternalErrorException
    {
    	long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

    	// Define o tipo de SMS que serah utilizado para o envio deste Broadcast de mensagens.
    	TipoSMS tipoSMS = new TipoSMS();
    	tipoSMS.setIdtTipoSMS(Definicoes.SMS_BROADCAST);

    	GerentePoolBancoDados pool = GerentePoolBancoDados.getInstancia(idProcesso);
    	PREPConexao con = pool.getConexaoPREP(idProcesso);

    	EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, con);
    	// Realiza a iteracao na lista de assinantes (separados por ';') para a inclusao
    	// de cada linha na tabela de envio de SMS.
		String [] assinantes = listaMsisdn.split(";");
		for(int i=0; i< assinantes.length; i++)
		{
			try
			{
				if(assinantes[i].matches(Definicoes.MASCARA_GSM_BRT))
				{
					DadosSMS sms = new DadosSMS();
					sms.setIdtMsisdn(assinantes[i]);
					sms.setDesMensagem(mensagem);
					// A prioridade de mensagens a ser enviadas pela ferramenta de BroadCast nao estah
					// configurado para ser parametrizado, portanto serah utilizado um valor fixo de -1
					// entendendo que as outras prioridades sao 0 e 1 ganhando entao prioridade em todas
					// as mensagens existentes a serem enviadas.
					sms.setNumPrioridade(-1);
					sms.setTipoSMS(tipoSMS);

					// OBS: O metodo antigo desta funcionalidade registrava as mensagens de broadcast em uma
					// tabela chamada TBL_GER_BROADCAST_SMS. Esta tabela funcionava apenas como um log do
					// aceite ou nao da mensagem pela plataforma SMS. A partir desta versao a funcionalidade
					// de envio de SMS realiza um controle de entrega da mensagem configurada a partir do TIPO do
					// SMS registrado na tabela. Portanto, basta realizar o insert na tabela normal de envio de
					// mensagens TBL_GER_ENVIO_SMS
					dao.inserirSMS(sms);

					log.log(idProcesso, Definicoes.INFO, "ComponenteNegocioAprovisionamento", "enviarSMSMulti",
							"Registrada a mensagem de BROADCAST para o assinante: "+ assinantes[i]);
				}
			}
			catch(Exception e)
			{
				log.log(idProcesso, Definicoes.INFO, "ComponenteNegocioAprovisionamento", "enviarSMSMulti",
						"Erro ao registrar mensagem de BROADCAST para o assinante "+ assinantes[i]+". Erro:"+e);
			}
		}

		pool.liberaConexaoPREP(con, idProcesso);
    }

    /**
     * Grava registro de mensagem de SMS a ser enviada pelo sistema.
     *
     * @param msisdnOrigem		MSISDN de Origem.
     * @param msisdnDestino		MSISDN de Destino.
     * @param mensagem			Mensagem a ser enviada.
     * @param prioridade		Prioridade de envio.
     * @param tipo				Tipo de SMS a ser enviado.
     *
     * @return <code>true</code> caso a mensagem tenha sido gravada com sucesso;<br>
     *         <code>false</code> caso contrario
     *
     * @throws GPPInternalErrorException
     */
    public boolean gravarMensagemSMS (String msisdnOrigem, String msisdnDestino, String mensagem, int prioridade, String tipo) throws GPPInternalErrorException
    {
    	DadosSMS sms = new DadosSMS();
    	sms.setIdtMsisdnOrigem(msisdnOrigem);
    	sms.setIdtMsisdn(msisdnDestino);
    	sms.setDesMensagem(mensagem);
    	sms.setNumPrioridade(prioridade);
    	sms.getTipoSMS().setIdtTipoSMS(tipo);

        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        GerentePoolBancoDados pool = GerentePoolBancoDados.getInstancia(idProcesso);
        PREPConexao con = pool.getConexaoPREP(idProcesso);

        EnvioSMSDAO dao = new EnvioSMSDAO(idProcesso, con);

        boolean retorno = false;

        try
        {
        	retorno = (dao.inserirSMS(sms) > 0);
        }
        finally
        {
        	if(con != null)
        		pool.liberaConexaoPREP(con, idProcesso);
        }

        return retorno;
    }

    //Metodos relacionados a promocoes.

    /**
     * Metodo...: inserePulaPula
     * Descricao: Insere um assinante em uma promocao Pula-Pula
     * @param   String  msisdn      MSISDN do assinante
     * @param   int     promocao    Identificador da Promocao
     * @param   String  operador    Matricula do operador.
     * @param   int     motivo      Identificador do motivo da operacao.
     * @return  short               Codigo de retorno da operacao
     */
    public short inserePulaPula(String msisdn, String promocao, String operador)
    {
        short retorno = -1;
        long idProcesso=0;
        Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_APROVISIONAMENTO, "inserePulaPula", "Inicio MSISDN: " + msisdn + " Promocao: " + promocao);

        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(idProcesso);
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        retorno = controle.inserePulaPula(msisdn, mapPromocao.getIdPromocao(promocao), dataProcessamento, operador, Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO);

        return retorno;
    }

    /**
     * Metodo...: retiraPulaPula
     * Descricao: Retira uma promocao Pula-Pula do assinante
     * @param   String  msisdn      MSISDN do assinante
     * @param   String  operador    Matricula do operador.
     * @param   int     motivo      Identificador do motivo da operacao.
     * @return  short               Codigo de retorno da operacao
     */
    public short retiraPulaPula(String msisdn, String operador)
    {
        short retorno = -1;
        long idProcesso=0;
        Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_APROVISIONAMENTO, "retiraPulaPula", "Inicio MSISDN: " + msisdn);

        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(idProcesso);

        retorno = controle.retiraPulaPula(msisdn, dataProcessamento, operador, Definicoes.CTRL_PROMOCAO_MOTIVO_DESATIVACAO);

        return retorno;
    }

    /**
     * Metodo...: trocaPulaPula
     * Descricao: Troca a Promocao Pula-Pula do assinante
     * @param   String  msisdn          MSISDN do assinante
     * @param   int     promocaoNova    Promocao nova do assinante
     * @param   String  operador    Matricula do operador.
     * @param   int     motivo      Identificador do motivo da operacao.
     * @return  short                   Codigo de retorno da operacao
     */
    public short trocaPulaPula(String msisdn, String promocaoNova, String operador)
    {
        short retorno = -1;
        long idProcesso=0;
        Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_APROVISIONAMENTO, "trocaPulaPula", "Inicio MSISDN: " + msisdn + " Promocao Nova: " + promocaoNova);

        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(idProcesso);
        MapPromocao mapPromocao = MapPromocao.getInstancia();
        retorno = controle.trocaPulaPula(msisdn, mapPromocao.getIdPromocao(promocaoNova), dataProcessamento, operador, Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PROMOCAO);

        return retorno;
    }

    /**
     * Metodo...: trocaPulaPulaPPP
     * Descricao: Troca a Promocao Pula-Pula do assinante a partir do portal pré - pago
     * @param   String  msisdn          MSISDN do assinante
     * @param   int     promocaoNova    Promocao nova do assinante
     * @param   String  operador        Matricula do operador.
     * @param   int     motivo          Identificador do motivo da operacao.
     * @param   int     tipoDocumento   Tipo de documento.
     * @return  String  numDocumento    Numero do documento.
     * @return  short                   Codigo de retorno da operacao
     */
    public short trocaPulaPulaPPP(String msisdn, int promocaoNova, String operador, int motivo, int tipoDocumento, String numDocumento)
    {
        short retorno = -1;
        long idProcesso=0;
        Timestamp dataProcessamento = new Timestamp(Calendar.getInstance().getTimeInMillis());

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "trocaPulaPulaPPP", "Inicio MSISDN: " + msisdn + " Promocao Nova: " + promocaoNova);

        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(idProcesso);

        retorno = controle.trocaPulaPula(msisdn, promocaoNova, dataProcessamento, operador, motivo, tipoDocumento, numDocumento);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO, "trocaPulaPulaPPP", "Fim RETORNO " + retorno);

        return retorno;
    }

    /**
     * Metodo...: executaPulaPula
     * Descricao: Executa o processo de concessao de bonus Pula-Pula para o assinante.
     * @param   String  tipoExecucao    Tipo de execucao da concessao.
     * @param   String  msisdn          MSISDN do assinante.
     * @param   String  mes             Mes de concessao.
     * @param   String  operador        Matricula do operador.
     * @param   int     motivo          Identificador do motivo da operacao.
     * @return  short                   Codigo de retorno da operacao
     */
    public short executaPulaPula(String tipoExecucao, String msisdn, String dataReferencia, String operador, int motivo)
    {
        short retorno = -1;
        long idProcesso=0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "executaPulaPula", "Inicio MSISDN: " + msisdn);

        // Criando uma classe de aplicacao
        ControlePulaPula controle = new ControlePulaPula(idProcesso);

        try
        {
            // Insere uma configuracao de bonus por recarga na tecnonen
            retorno = controle.executaConcessao(tipoExecucao, msisdn, new SimpleDateFormat(Definicoes.MASCARA_DATE).parse(dataReferencia), operador, motivo);
        }
        catch(Exception e)
        {
            log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "executaPulaPula", "Excecao: " + e);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO, "executaPulaPula", "Fim RETORNO " + retorno);

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
     */
    public short zerarSaldos(String msisdn, String operador, String tipoTransacao, int codSaldosZerados)
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        short retorno = Definicoes.RET_OPERACAO_OK;
        try
        {
            Aprovisionar aprovisionar = new Aprovisionar(idProcesso);
            retorno = aprovisionar.zerarSaldos(msisdn, operador, tipoTransacao, codSaldosZerados);
        }
        catch(Exception exception)
        {
            log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "zerarSaldos", "Excecao: " + exception);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }
        return retorno;
    }


    /**
     * Metodo....: ativaAssinanteComStatus
     * Descricao.: Ativa um assinante na plataforma Tecnomen com um determinado status
     * @param   msisdn Numero do assinante
     * @param   imsi Numero do IMSI (SimCard) do assinante
     * @param   planoPreco Plano de preco do assinante
     * @param   creditoInicial Valor inial de credito
     * @param   idioma Idioma de acesso a URA
     * @param   operador Codigo do operador que esta realizando a ativacao
     * @param   status Status do assinante
     * @return  short Sucesso(0) ou erro (diferente de 0)
     */
    public short ativaAssinanteComStatus(String msisdn, String imsi, String planoPreco, double creditoInicial, short idioma, String operador, short status)
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        short retorno = Definicoes.RET_OPERACAO_OK;
        if((status >= Definicoes.FIRST_TIME_USER) && (status <= Definicoes.SHUT_DOWN)) // Se o status tem um valor válido
        {
            try
            {
                Aprovisionar aprovisionar = new Aprovisionar(idProcesso);
                Assinante assinante = aprovisionar.consultaAssinante(msisdn);
                if(assinante == null) // Se o assinante nao existe na plataforma
                {
                    retorno = ativaAssinante(msisdn, imsi, planoPreco, creditoInicial, idioma, operador);
                    if(retorno == Definicoes.RET_OPERACAO_OK) // Se o assinante foi ativado corretamente
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
                log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "ativaAssinanteComStatus", "Excecao: " + exception);
                retorno = Definicoes.RET_ERRO_TECNICO;
            }
        }
        else
        {
            retorno = Definicoes.RET_STATUS_MSISDN_INVALIDO;
        }
        return retorno;
    }

    // Métodos do Novo Brasil Vantegens

    /**
     * Metodo....: cobrarServico
     * Descricao.: Executa a cobrança do serviço
     * @param   msisdn          - Numero do assinante
     * @param   codigoServico   - Código do serviço a ser realizado
     * @param   operacao        - C: Consulta, D: Débito, E: Estorno
     * @param   operador        - Solicitante da cobrança
     * @return  String          - XML o retorno da cobrança
     * @throws GPPInternalErrorException
     */
    public String cobrarServico(String msisdn, String codigoServico, String operacao, String operador)
    {
        String xml = null;

        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        Aprovisionar aprovisiona = new Aprovisionar(idProcesso);
        short retorno = Definicoes.RET_OPERACAO_OK;

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "cobrarServico", "MSISDN: " + msisdn + "Servico: " + codigoServico + "Operacao: " + operacao);

        try
        {
            retorno = aprovisiona.cobrarServico(aprovisiona.consultaAssinante(msisdn),codigoServico,operacao,operador);
        }
        catch (Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"cobrarServico", "Erro ao executar ajuste: " + e);
        }
        finally
        {
            xml = aprovisiona.gerarXMLServico(msisdn,codigoServico,retorno,operacao,Definicoes.COBRANCA_SERVICO);
            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, Definicoes.PROCESSO_SUCESSO);
        }

        return xml;
    }

    /**
     * Metodo....: cadastrarBumerangue
     * Descricao.: Executa a cobrança do serviço
     * @param   msisdn          - Numero do assinante
     * @param   codigoServico   - Código do serviço a ser realizado
     * @param   operacao        - C: Consulta, D: Débito, E: Estorno
     * @param   operador        - Solicitante da cobrança
     * @return  String          - XML o retorno da cobrança
     * @throws GPPInternalErrorException
     */
    public String cadastrarBumerangue(String msisdn, String codigoServico, String operacao, String operador)
    {
        String xml = null;

        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        Aprovisionar aprovisiona = new Aprovisionar(idProcesso);

        short retorno = Definicoes.RET_OPERACAO_OK;

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "cadastrarBumerangue", "MSISDN: " + msisdn + "Servico: " + codigoServico + "Operacao: " + operacao);

        try
        {
            // Valida os parâmetros de entrada
            retorno = aprovisiona.validarParametros(aprovisiona.consultaAssinante(msisdn), codigoServico, operacao);

            if(retorno == Definicoes.RET_OPERACAO_OK && !operacao.equalsIgnoreCase(Definicoes.OPERACAO_CONSULTA))
            {
                retorno = aprovisiona.aprovisionarBumerangue(msisdn, Definicoes.SERVICO_BUMERANGUE, operacao, operador);

                // Caso o assinante já tenha o bumerangue não deve ser retornado erro
                if(retorno == Definicoes.RET_PROMOCAO_ASSINANTE_JA_EXISTE)
                    retorno = Definicoes.RET_OPERACAO_OK;
                else if(!(codigoServico == null || codigoServico.length() == 0) && retorno == Definicoes.RET_OPERACAO_OK)
                    retorno = aprovisiona.cobrarServico(aprovisiona.consultaAssinante(msisdn),codigoServico,operacao,operador);
            }
        }
        catch (Exception e)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"cadastrarBumerangue", "Erro ao executar ajuste: " + e);
        }
        finally
        {
            xml = aprovisiona.gerarXMLServico(msisdn,codigoServico,retorno,operacao,Definicoes.PROCESSO_BRASIL_VANTAGENS);
            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, Definicoes.PROCESSO_SUCESSO);
        }

        return xml;
    }

    /**
     * Metodo...: atualizarAmigosTodaHora
     * Descricao: Atualiza a lista de Amigos toda hora
     * @param   msisdn          - Assinante a atualzar o FF
     * @param   listaMsisdn     - Nova lista de FF
     * @param   codigoServico   - Código do servico a ser cobrado
     * @param   operacao        - C: Consulta, D: Débito, E: Estorno
     * @param   operador        - Codigo do operador que esta realizando a operacao
     * @return  String          - XML com os dados da atualização de amigos toda hora
     * @throws  GPPInternalErrorException
     */
    public String atualizarAmigosTodaHora(String msisdn, String listaMsisdn, String codigoServico, String operacao, String operador)
    {
        String xml = null;
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        short retorno = Definicoes.RET_OPERACAO_OK;

        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);

        log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "atualizarAmigosTodaHora", "MSISDN: " + msisdn + "Servico: " + codigoServico + "Operacao: " + operacao + "Lista: " + listaMsisdn);

        try
        {
            Assinante assinante = aprovisionar.consultaAssinante(msisdn);

            // Valida os parâmetros de entrada
            retorno = aprovisionar.validarParametros(assinante, codigoServico, operacao);

            if(retorno == Definicoes.RET_OPERACAO_OK && !operacao.equalsIgnoreCase(Definicoes.OPERACAO_CONSULTA))
            {
                // Troca o Plano do Cliente para permitir a tarifação
                //diferenciada de acordo com o tipo de Amigos Toda Hora
                if(!(codigoServico == null || codigoServico.length() == 0))
                    aprovisionar.trocarPlanoEspelho(assinante,operador,codigoServico);

                retorno = aprovisionar.atualizarFF(assinante, listaMsisdn);

                // Cobra o serviço
                if(!(codigoServico == null || codigoServico.length() == 0) && retorno == Definicoes.RET_OPERACAO_OK)
                    retorno = aprovisionar.cobrarServico(assinante,codigoServico,operacao,operador);
            }
        }
        catch (Exception tecEx)
        {
            retorno = Definicoes.RET_ERRO_TECNICO;
            log.log(idProcesso, Definicoes.ERRO,Definicoes.CN_APROVISIONAMENTO,"atualizarAmigosTodaHora", "Erro ao executar ajuste");
        }
        finally
        {
            if(!operacao.equalsIgnoreCase(Definicoes.OPERACAO_CONSULTA))
                // Grava em tabela os dados os dados de eventos de aprovisionamento
                aprovisionar.gravaAtualizaFF (msisdn, listaMsisdn, operador, retorno);

            xml = aprovisionar.gerarXMLServico(msisdn,codigoServico,retorno,operacao,Definicoes.PROCESSO_BRASIL_VANTAGENS);
            log.liberaIdProcesso(idProcesso, Definicoes.CN_APROVISIONAMENTO, Definicoes.PROCESSO_SUCESSO);
        }

        return xml;
    }


    /**
     * Reativa um assinante na plataforma com o status prévio à desativação
     * @param   msisdnNovo      Número com o qual o cliente deve ser ativado
     * @param   msisdnAntigo    Número com o qual o cliente foi desativado
     * @return  short Sucesso(0) ou erro (diferente de 0)
     */
    public short reativarAssinante(String msisdnNovo, String msisdnAntigo, String operador)
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        short retorno = Definicoes.RET_OPERACAO_OK;

        try
        {
            Aprovisionar aprovisionar = new Aprovisionar(idProcesso);

            retorno = aprovisionar.reativarAssinante(msisdnNovo, msisdnAntigo, operador);
        }
        catch(Exception exception)
        {
            log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "reativarAssinante", "Excecao: " + exception);
            retorno = Definicoes.RET_ERRO_TECNICO;
        }

        return retorno;
    }



    /**
     * Por: Bernardo Vergne Dias
     * Metodo...: aprovarLote
     * Descricao: Aprova lote de estorno/expurgo de bonus pula-pula
     * @param   lote            - Identificação do lote
     */
    public short aprovarLote ( String lote )
    {
        try
        {
            long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
            AprovacaoLote aprovacaoLote = new AprovacaoLote (idProcesso);
            return aprovacaoLote.aprovarLote(lote);
        }
        catch (Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
    }


    /**
     * Por: Bernardo Vergne Dias
     * Metodo...: aprovarLote
     * Descricao: Aprova lote de estorno/expurgo de bonus pula-pula
     * @param   lote            - Identificação do lote
     */
    public short rejeitarLote ( String lote )
    {
        try
        {
            long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
            AprovacaoLote aprovacaoLote = new AprovacaoLote (idProcesso);
            return aprovacaoLote.rejeitarLote(lote);
        }
        catch (Exception e)
        {
            return Definicoes.RET_ERRO_TECNICO;
        }
    }

    public String desativarAssinanteXML ( String MSISDN, String motivoDesativacao, String aOperador ) throws  GPPInternalErrorException, GPPTecnomenException
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        DesativaAssinante desativaAssinante = new DesativaAssinante(idProcesso);

        return desativaAssinante.desativarAssinante(MSISDN, motivoDesativacao, aOperador, idProcesso);
    }

    /**
     *  Atualiza o status do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      status                  Novo status do assinante.
     *  @param      dataExpiracao           Nova data de expiracao dos saldos, no formato dd/mm/yyyy.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short alterarStatusAssinante(String msisdn, short status, String dataExpiracao, String operador)
    {
        long idProcesso = this.log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        Aprovisionar aprovisionamento = new Aprovisionar(idProcesso);

        try
        {
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
            return aprovisionamento.atualizarStatusAssinante(msisdn,
                                                             status,
                                                             (!dataExpiracao.equals("")) ?
                                                                    conversorDate.parse(dataExpiracao) : null,
                                                             operador);
        }
        catch(ParseException e)
        {
            this.log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_APROVISIONAMENTO, "alterarStatusAssinante", "MSISDN: " + msisdn + " - Formato de data invalida.");
            this.log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_APROVISIONAMENTO, "alterarStatusAssinante", "MSISDN: " + msisdn + " - Data de expiracao: " + dataExpiracao);

            return Definicoes.RET_FORMATO_DATA_INVALIDA;
        }
    }

    /**
     *  Atualiza o status periodico do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      status                  Novo status periodico do assinante.
     *  @param      dataExpiracao           Nova data de expiracao do Saldo Periodico, no formato dd/mm/yyyy.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short alterarStatusPeriodico(String msisdn, short status, String dataExpiracao, String operador)
    {
        long idProcesso = this.log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        Aprovisionar aprovisionamento = new Aprovisionar(idProcesso);

        try
        {
            SimpleDateFormat conversorDate = new SimpleDateFormat(Definicoes.MASCARA_DATE);
            return aprovisionamento.atualizarStatusPeriodico(msisdn,
                                                             status,
                                                             (!dataExpiracao.equals("")) ?
                                                                    conversorDate.parse(dataExpiracao) : null,
                                                             operador);
        }
        catch(ParseException e)
        {
            this.log.log(idProcesso, Definicoes.ERRO , Definicoes.CN_APROVISIONAMENTO, "alterarStatusPeriodico", "MSISDN: " + msisdn + " - Formato de data invalida.");
            this.log.log(idProcesso, Definicoes.DEBUG, Definicoes.CN_APROVISIONAMENTO, "alterarStatusPeriodico", "MSISDN: " + msisdn + " - Data de expiracao: " + dataExpiracao);

            return Definicoes.RET_FORMATO_DATA_INVALIDA;
        }
    }

    /**
     * Insere a Promocao "Fale Gratis a Noite" para o assinante informado
     * @param msisdn    Assinante a ser cadastrado na promocao Fale Gratis a Noite
     * @param promocao  Promocao FGN do assinante
     * @param operador  Operador que requesita o cadastro na promocao Fale Gratis a Noite
     * @return
     */
    public short inserirPromocaoFaleGratisANoite(String msisdn, String promocao, String operador)
    {
        short retorno   = -1;
        long idProcesso =  0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar(idProcesso);
        retorno = aprovisionar.inserirPromocaoFaleGratisANoite(msisdn, promocao, operador);

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
        short retorno   = -1;
        long idProcesso =  0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        // Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar(idProcesso);
        retorno = aprovisionar.retirarPromocaoFaleGratisANoite(msisdn, operador);

        return retorno;
    }

    /**
     * Metodo...: enviarRequisicaoTangram
     * Descricao: Envia uma requisicao de envio de SMS para o Tangram
     * @param   xml - XML contendo informacoes da requisica tais como msisdn dos destinatarios,
     *          conteudo da mensagem, data de envio...
     * @throws  GPPInternalErrorException
     */
    public short enviarRequisicaoTangram(String xml) throws GPPInternalErrorException
    {
        long idProcesso = 0;

        // Obtendo um ID de processo para o LOG
        idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);

        log.log(idProcesso, Definicoes.INFO,Definicoes.CN_APROVISIONAMENTO,"enviarRequisicaoTangram", "XML da Requisicao: " + xml);

        //EnvioMensagemTangram envioMensagemTangram = new EnvioMensagemTangram(idProcesso);
        //return envioMensagemTangram.enviarRequisicaoTangram(xml);
        return 0;

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
    public short atualizaAmigosDeGraca (String MSISDN, String listaMSISDN, String operador, String codigoServico)
    {
    	long idProcesso = this.log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
    	short retorno = Definicoes.RET_ERRO_GENERICO_GPP;

    	try
    	{
    		Aprovisionar aprovisionar = new Aprovisionar(idProcesso);

    		retorno = aprovisionar.atualizaAmigosDeGraca(MSISDN, listaMSISDN, operador, codigoServico);

    		log.log(idProcesso, Definicoes.INFO, Definicoes.CN_APROVISIONAMENTO, "atualizaAmigosDeGraca", "Termino da atualizacao dos amigos do assinante "+  MSISDN  + ". Parametros de entrada: Lista de Amigos: "+listaMSISDN+" Codigo de Servico: "+codigoServico+" Nome do Operador: "+operador+" Codigo de Retorno: "+ retorno);

    	}
    	catch(Exception e)
    	{
    		log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "atualizaAmigosDeGraca", "Erro ao atualizar os amigos do assinante "+ MSISDN + " : Erro " + e);
    	}

    		return retorno;
    }
    /**
     * Ativa um assiannte com um XML
     * <mensagem>                                                                
     *     <cabecalho>                                                           
     *         <empresa>BRG</empresa>                                            
     *         <sistema>GPP</sistema>                                            
     *         <processo>GPPAtivacaoAssinante</processo>                                          
     *         <data>2007/01/17 12:00:00</data>                                  
     *         <identificador_requisicao>556185000000</identificador_requisicao> 
     *         <codigo_erro>0000</codigo_erro>                                   
     *         <descricao_erro>Sucesso</descricao_erro>                          
     *     </cabecalho>                                                          
     *     <conteudo>                                                            
     *         <![CDATA[                                                         
     *             <root>                                                        
     * <GPPAtivacaoAssinante>                                                  
     *                 <assinante>                                               
     *                     <msisdn>556185777701</msisdn>                         
     *                     <imsi>556185777701</imsi>                             
     *                     <planoPreco>1</planoPreco>                            
     *                     <idioma>4</idioma>                                    
     *                     <creditoInicial>                                      
     *                         <saldo id=\"0\">50.00</saldo>                     
     *                         <saldo id=\"1\">10.00</saldo>                     
     *                         <saldo id=\"2\">0.00</saldo>                      
     *                         <saldo id=\"3\">0.00</saldo>                      
     *                     </creditoInicial>                                     
     *                 </assinante>                                              
     *                 <operador>TR00000</operador>                              
     * </GPPAtivacaoAssinante>                                                 
     * <GPPAtivacaoAssinante>                                                  
     *                 <assinante>                                               
     *                     <msisdn>556185777702</msisdn>                         
     *                     <imsi>556185777702</imsi>                             
     *                     <planoPreco>1</planoPreco>                            
     *                     <idioma>4</idioma>                                    
     *                     <status>2</status>                                    
     *                     <creditoInicial>                                      
     *                         <saldo id=\"0\">50.00</saldo>                     
     *                         <saldo id=\"1\">10.00</saldo>                     
     *                         <saldo id=\"2\">0.00</saldo>                      
     *                         <saldo id=\"3\">0.00</saldo>                      
     *                     </creditoInicial>                                     
     *                 </assinante>                                              
     *                 <operador>TR00000</operador>                              
     * </GPPAtivacaoAssinante>                                                 
     *             </root>                                                       
     *         ]]>                                                               
     *     </conteudo>                                                           
     * </mensagem>                                                               
     */
    public short ativarAssinante(String xml)
    {
        long idProcesso = log.getIdProcesso(Definicoes.CN_APROVISIONAMENTO);
        //Criando uma classe de aplicacao
        Aprovisionar aprovisionar = new Aprovisionar (idProcesso);
        short retorno = Definicoes.RET_XML_MAL_FORMADO;
    	try
    	{
			retorno = aprovisionar.ativarAssinante(xml);
    	}
	    catch(Exception e)
	    {
	    	log.log(idProcesso, Definicoes.ERRO, Definicoes.CN_APROVISIONAMENTO, "ativarAssinante", "Erro ao aprovisionar assinante com o "+ "Erro: " + e + ";XML: "+ xml );
	    }
		return retorno;
    }
}