package com.brt.gpp.aplicacoes.promocao.controle;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.promocao.controle.GerenciadorDataExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.Promocao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoCategoria;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoDiaExecucao;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoStatusAssinante;
import com.brt.gpp.aplicacoes.promocao.entidade.PromocaoTipoTransacao;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.aplicacoes.promocao.persistencia.Operacoes;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tecnomen.TecnomenAprovisionamento;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoEspelho;
import com.brt.gpp.gerentesPool.GerentePoolTecnomen;

/**
 *	Classe responsavel pela logica de negocio das operacoes relacionadas ao controle de promocoes de assinantes.
 *
 *	@author	Daniel Ferreira
 *	@since	18/08/2005
 */
public class ControlePromocao extends Aplicacoes
{
    
    /**
     *	Objeto utilizado para execucao de ajustes de bonificacao.
     */
    protected Ajustar ajusteGPP;
    
    /**
     *	Gerenciador de datas de execucao das promocoes dos assinantes.
     */
    protected GerenciadorDataExecucao gerenciadorData;
    
    /**
     *	DAO para execucao de consulta de informacoes de promocoes de assinantes.
     */
    protected Consulta consulta;
    
    /**
     *	DAO para execucao de operacoes sobre as informacoes de promocoes de assinantes.
     */
    protected Operacoes operacoes;

    /**
     *	Construtor da classe.
     *
     *	@param		idProcesso				Identificador do processo.
     */
    public ControlePromocao(long idProcesso)
    {
        this(idProcesso, Definicoes.CL_PROMOCAO_CONTROLE);
    }
    
    /**
     *	Construtor da classe.
     *
     *	@param		idProcesso				Identificador do processo.
     *	@param		nomeClasse				Nome da classe.
     */
    public ControlePromocao(long idProcesso, String nomeClasse)
    {
        super(idProcesso, nomeClasse);
        
        this.consulta			= new Consulta(idProcesso);
        this.operacoes			= new Operacoes(idProcesso);
        this.ajusteGPP			= new Ajustar(idProcesso);
        this.gerenciadorData	= new GerenciadorDataExecucao(idProcesso, this.consulta);
    }
    
    /**
     *	Insere assinantes em promocoes.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocao				Identificador da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short inserePromocaoAssinante(String msisdn, 
    									int promocao, 
										Date dataProcessamento, 
										String operador, 
										int motivo, 
										PREPConexao conexaoPrep)
    {
        short result = -1;
        
        try
        {
            //Executando consulta de assinante no GPP.
            Assinante assinante = this.consulta.getAssinanteGPPTecnomen(msisdn);
            Integer indRecarga = this.consulta.getIndRecarga(msisdn, conexaoPrep);
            assinante.setNumRecargas((indRecarga != null) ? indRecarga.intValue() : 0);
            //Obtendo objeto PromocaoAssinante para passagem como parametro para metodo de insercao.
            Integer idtPromocao = new Integer(promocao);
            Integer idtCodigoNacional = new Integer(msisdn.substring(2, 4));
            Integer idtPlanoPreco = new Integer(assinante.getPlanoPreco());
            PromocaoAssinante pAssinante = new PromocaoAssinante();
            pAssinante.setIdtMsisdn(msisdn);
            pAssinante.setPromocao(this.consulta.getPromocao(idtPromocao));
            pAssinante.setDatEntradaPromocao(dataProcessamento);
            pAssinante.setAssinante(assinante);
            pAssinante.setCodigoNacional(this.consulta.getPromocaoCodigoNacional(idtPromocao, idtCodigoNacional));
            pAssinante.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
            pAssinante.setDiasExecucao(this.consulta.getPromocaoDiasExecucao(idtPromocao, dataProcessamento));
            //Obtendo as promocoes do assinante.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            
            result = this.inserePromocaoAssinante(pAssinante, promocoesAssinante, dataProcessamento, conexaoPrep);    
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "inserePromocaoAssinante", 
					  "MSISDN: " + msisdn + " - Promocao: " + promocao + " - Excecao: "  + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.operacoes.insereEvento(dataProcessamento,
 			       						msisdn,
 			       						Definicoes.TIPO_APR_PROMOCAO_ENTRADA,
 			       						null,
 			       						String.valueOf(promocao),
 			       						new Double(0.0),
 			       						new Integer(motivo),
 			       						operador,
 			       						((result == Definicoes.RET_OPERACAO_OK) || 
 			       						 (result == Definicoes.RET_PROMOCAO_PENDENTE_RECARGA)) ? 
 			       						 	Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
										new Integer(result),
										conexaoPrep);
        }
        
        return result;
    }
    
    /**
     *	Insere assinantes em promocoes.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		promocoesAssinante		Lista de promocoes do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short inserePromocaoAssinante(PromocaoAssinante pAssinante, 
    									 Collection promocoesAssinante, 
										 Date dataProcessamento, 
										 PREPConexao conexaoPrep)
    {
        short result = -1;
        
        try
        {
            //Validando a entrada do assinante na promocao.
            result = this.validaInsercaoPromocaoAssinante(pAssinante, promocoesAssinante);
            
            switch(result)
            {
                case Definicoes.RET_OPERACAO_OK:
                {
                    //Assinante com status ativo.
                    Integer idtStatus = new Integer(PromocaoStatusAssinante.STATUS_ATIVO);
                    pAssinante.setStatus(this.consulta.getPromocaoStatusAssinante(idtStatus));
                    //O valor default para isencao de limite e que o assinante nao seja isento.
                    pAssinante.setIndIsentoLimite(false);
                	//Data de execucao.
                	this.gerenciadorData.atualizarDataExecucao(pAssinante, dataProcessamento);
                	//Inserindo a promocao do assinante na base de dados.                	
                	if(this.operacoes.inserePromocaoAssinante(pAssinante, conexaoPrep))
                	    promocoesAssinante.add(pAssinante);
                	else
                	    result = Definicoes.RET_ERRO_TECNICO; 
                	
                	break;
                }
                case Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO:
                {
                    super.log(Definicoes.DEBUG, 
                    		  "inserePromocaoAssinante", 
							  pAssinante + " - Plano de Preco invalido.");
                    
                    //Verificando se a promocao exige que o assinante troque o plano para o seu espelho.
                    Assinante assinante = pAssinante.getAssinante();
                    int planoAssinante = assinante.getPlanoPreco();
                    String tipoEspelhamento = pAssinante.getPromocao().getTipEspelhamento();
                    if((this.trocarPlanoEspelho(assinante, tipoEspelhamento, (short)-1, dataProcessamento, conexaoPrep) == Definicoes.RET_OPERACAO_OK) &&
                       (planoAssinante != assinante.getPlanoPreco()))
                    {
                    	//Obtendo as novas informacoes de mapeamento de Promocao / Plano de Preco.
                        Integer idtPromocao     = new Integer(pAssinante.getPromocao().getIdtPromocao());
                        Integer idtPlanoPreco   = new Integer(assinante.getPlanoPreco()); 
                        pAssinante.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
                        //Executando tentativa de insercao de promocao com o novo plano espelho do assinante.
                        result = this.inserePromocaoAssinante(pAssinante, promocoesAssinante, dataProcessamento, conexaoPrep);
                    }
                    
                    break;
                }
                case Definicoes.RET_PROMOCAO_PENDENTE_RECARGA:
                {
                    super.log(Definicoes.DEBUG, 
                    		  "inserePromocaoAssinante", 
							  pAssinante + " - Pendente de primeira recarga.");
                    
                    //Assinante com status Pendente de Primeira Recarga.
                    Integer idtStatus = new Integer(PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA);
                    pAssinante.setStatus(this.consulta.getPromocaoStatusAssinante(idtStatus));
                    //O valor default para isencao de limite e que o assinante nao seja isento.
                    pAssinante.setIndIsentoLimite(false);
                	//Data de execucao.
                	this.gerenciadorData.atualizarDataExecucao(pAssinante, dataProcessamento);
                	if(this.operacoes.inserePromocaoAssinante(pAssinante, conexaoPrep))
                	{
                	    promocoesAssinante.add(pAssinante);
                	    result = Definicoes.RET_OPERACAO_OK;
                	}
                	else
                	    result = Definicoes.RET_ERRO_TECNICO; 
                    
                    break;
                }
                default: break;
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "inserePromocaoAssinante", 
					  pAssinante + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        //OBS: Este metodo nao insere eventos porque e recursivo, e e o metodo invocado por todos os outros
        //metodos de inclusao de assinantes em promocoes.
        return result;
    }

    /**
     *	Insere assinantes nas promocoes disponiveis na data de processamento.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataReferencia			Data de referencia para obtencao das promocoes.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short inserePromocoesAssinante(String msisdn, 
    									  Date dataReferencia, 
										  Date dataProcessamento, 
										  String operador, 
										  int motivo, 
										  PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            //Criando objeto para obter a data/hora dos registros dos varios eventos. Como a chave primaria na
            //TBL_APR_EVENTOS e formada pelo par (MSISDN, Data de Aprovisionamento), segue a regra do calculo da data/hora
            //dos varios eventos do metodo:
            //1 - O metodo que invoca este metodo recebe dataProcessamento;
            //2 - Cada evento PROMOCAO_ENTRADA recebe dataProcessamento do evento anterior + 1 segundo.
            Calendar calProcessamento = Calendar.getInstance();
            calProcessamento.setTime(dataProcessamento);
            
            //Executando consulta de assinante no GPP.
            Assinante assinante = this.consulta.getAssinanteGPPTecnomen(msisdn);
            Integer indRecarga = this.consulta.getIndRecarga(msisdn, conexaoPrep);
            assinante.setNumRecargas((indRecarga != null) ? indRecarga.intValue() : 0);
            //Obtendo as promocoes do assinante.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            //Obtendo a lista de promocoes disponiveis.
            Collection promocoes = this.consulta.getPromocoes(dataReferencia);
            
            for(Iterator iterator = promocoes.iterator(); iterator.hasNext();)
            {
                //Obtendo a data de processamento do evento.
                calProcessamento.add(Calendar.SECOND, 1);
                //Obtendo a promocao da lista.
                Promocao promocao = (Promocao)iterator.next();
                //Obtendo objeto PromocaoAssinante para passagem como parametro para metodo de insercao.
                Integer idtPromocao = new Integer(promocao.getIdtPromocao());
                Integer idtCodigoNacional = new Integer(msisdn.substring(2, 4));
                Integer idtPlanoPreco = new Integer(assinante.getPlanoPreco());
                PromocaoAssinante pAssinante = new PromocaoAssinante();
                pAssinante.setIdtMsisdn(msisdn);
                pAssinante.setPromocao(promocao);
                pAssinante.setDatEntradaPromocao(dataProcessamento);
                pAssinante.setAssinante(assinante);
                pAssinante.setCodigoNacional(this.consulta.getPromocaoCodigoNacional(idtPromocao, idtCodigoNacional));
                pAssinante.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
                pAssinante.setDiasExecucao(this.consulta.getPromocaoDiasExecucao(idtPromocao, dataProcessamento));

                //Invocando metodo de insercao de assinantes em promocoes.
                short retInsercao = this.inserePromocaoAssinante(pAssinante, 
                												 promocoesAssinante, 
																 calProcessamento.getTime(), 
																 conexaoPrep);

                this.operacoes.insereEvento(calProcessamento.getTime(),
     			       						msisdn,
     			       						Definicoes.TIPO_APR_PROMOCAO_ENTRADA,
     			       						null,
     			       						String.valueOf(idtPromocao.intValue()),
     			       						new Double(0.0),
     			       						new Integer(motivo),
     			       						operador,
     			       						((retInsercao == Definicoes.RET_OPERACAO_OK) || 
     			       						 (retInsercao == Definicoes.RET_PROMOCAO_PENDENTE_RECARGA)) ? 
     			       						 	Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
											new Integer(retInsercao),
											conexaoPrep);
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "inserePromocoesAssinante", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        return result;
    }
    
    /**
     *	Insere assinantes nas promocoes disponiveis na data de processamento.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		status					Status do assinante.
     *	@param		plano					Plano de preco do assinante
     *	@param		fezRecarga				Flag indicando se o assinante fez recarga.
     *	@param		dataReferencia			Data de referencia para obtencao das promocoes.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short inserePromocoesAssinante(String msisdn, 
    									  int status, 
										  int plano, 
										  boolean fezRecarga, 
										  Date dataReferencia, 
										  Date dataProcessamento, 
										  String operador, 
										  int motivo, 
										  PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            //Criando objeto para obter a data/hora dos registros dos varios eventos. Como a chave primaria na
            //TBL_APR_EVENTOS e formada pelo par (MSISDN, Data de Aprovisionamento), segue a regra do calculo da data/hora
            //dos varios eventos do metodo:
            //1 - O metodo que invoca este metodo recebe dataProcessamento;
            //2 - Cada evento PROMOCAO_ENTRADA recebe dataProcessamento do evento anterior + 1 segundo.
            Calendar calProcessamento = Calendar.getInstance();
            calProcessamento.setTime(dataProcessamento);
            
            //Criando objeto com as informacoes do assinante na plataforma.
            Assinante assinante = new Assinante();
            assinante.setMSISDN(msisdn);
            assinante.setRetorno(new Integer(Definicoes.RET_OPERACAO_OK).shortValue());
            assinante.setStatusAssinante(new Integer(status).shortValue());
            assinante.setPlanoPreco(new Integer(plano).shortValue());
            assinante.setNumRecargas((fezRecarga) ? 1 : 0);
            //Obtendo as promocoes do assinante.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            //Obtendo a lista de promocoes disponiveis.
            Collection promocoes = this.consulta.getPromocoes(dataReferencia);
            
            for(Iterator iterator = promocoes.iterator(); iterator.hasNext();)
            {
                //Obtendo a data de processamento do evento.
                calProcessamento.add(Calendar.SECOND, 1);
                //Obtendo a promocao da lista.
                Promocao promocao = (Promocao)iterator.next();
                //Obtendo objeto PromocaoAssinante para passagem como parametro para metodo de insercao.
                Integer idtPromocao = new Integer(promocao.getIdtPromocao());
                Integer idtCodigoNacional = new Integer(msisdn.substring(2, 4));
                Integer idtPlanoPreco = new Integer(plano);
                PromocaoAssinante pAssinante = new PromocaoAssinante();
                pAssinante.setIdtMsisdn(msisdn);
                pAssinante.setPromocao(promocao);
                pAssinante.setDatEntradaPromocao(dataProcessamento);
                pAssinante.setAssinante(assinante);
                pAssinante.setCodigoNacional(this.consulta.getPromocaoCodigoNacional(idtPromocao, idtCodigoNacional));
                pAssinante.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
                pAssinante.setDiasExecucao(this.consulta.getPromocaoDiasExecucao(idtPromocao, dataProcessamento));

                //Invocando metodo de insercao de assinantes em promocoes.
                short retInsercao = this.inserePromocaoAssinante(pAssinante, 
                												 promocoesAssinante, 
																 calProcessamento.getTime(), 
																 conexaoPrep);

                this.operacoes.insereEvento(calProcessamento.getTime(),
                							msisdn,
											Definicoes.TIPO_APR_PROMOCAO_ENTRADA,
											null,
											String.valueOf(promocao.getIdtPromocao()),
											new Double(0.0),
											new Integer(motivo),
											operador,
											((retInsercao == Definicoes.RET_OPERACAO_OK) || 
											 (retInsercao == Definicoes.RET_PROMOCAO_PENDENTE_RECARGA)) ? 
											 	Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
											new Integer(retInsercao),
											conexaoPrep);
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "inserePromocoesAssinante", 
					  "MSIDSN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        return result;
    }
    
    /**
     *	Retira uma promocao do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocao				Identificador da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short retiraPromocaoAssinante(String msisdn, 
    									 int promocao, 
										 Date dataProcessamento, 
										 String operador, 
										 int motivo, 
										 PREPConexao conexaoPrep)
    {
        short result = -1;
        Integer idtPromocao = new Integer(promocao);
        
        try
        {
            if(this.operacoes.retiraPromocaoAssinante(idtPromocao, msisdn, conexaoPrep))
                result = Definicoes.RET_OPERACAO_OK;
            else
            	result = Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE; 
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "retiraPromocaoAssinante", 
					  "MSIDSN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.operacoes.insereEvento(dataProcessamento,
            							msisdn,
										Definicoes.TIPO_APR_PROMOCAO_SAIDA,
										String.valueOf(idtPromocao.intValue()),
										null,
										new Double(0.0),
										new Integer(motivo),
										operador,
										(result == Definicoes.RET_OPERACAO_OK) ? 
											Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
										new Integer(result),
										conexaoPrep);
        }
        
        return result;
    }
    
    /**
     *	Retira todas as promocoes do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short retiraPromocoesAssinante(String msisdn, 
    									  Date dataProcessamento, 
										  String operador, 
										  int motivo, 
										  PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        try
        {
            //Criando objeto para obter a data/hora dos registros dos varios eventos. Como a chave primaria na
            //TBL_APR_EVENTOS e formada pelo par (MSISDN, Data de Aprovisionamento), segue a regra do calculo da data/hora
            //dos varios eventos do metodo:
            //1 - O metodo que invoca este metodo recebe dataProcessamento;
            //2 - Cada evento PROMOCAO_SAIDA recebe dataProcessamento do evento anterior + 1 segundo.
            Calendar calProcessamento = Calendar.getInstance();
            calProcessamento.setTime(dataProcessamento);
            
            //Obtendo as promocoes do assinante.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, null, conexaoPrep);
            //Retirando as promocoes do assinante.
            if(this.operacoes.retiraPromocoesAssinante(msisdn, conexaoPrep))
                //Para cada promocao retirada, deve ser inserido um evento de retirada de promocao.
                for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
                {
                    //Adicionando o horario do evento em 1 segundo para evitar violacao de chave primaria.
                    calProcessamento.add(Calendar.SECOND, 1);
                    
                    PromocaoAssinante pAssinante = (PromocaoAssinante)iterator.next();
                    Integer idtPromocao = new Integer(pAssinante.getPromocao().getIdtPromocao());
                    
                    super.log(Definicoes.DEBUG, 
                    		  "retiraPromocoesAssinante", 
							  pAssinante + " - Promocao retirada.");
                    
                    this.operacoes.insereEvento(calProcessamento.getTime(),
                    							msisdn,
												Definicoes.TIPO_APR_PROMOCAO_SAIDA,
												String.valueOf(idtPromocao.intValue()),
												null,
												new Double(0.0),
												new Integer(motivo),
												operador,
												(result == Definicoes.RET_OPERACAO_OK) ? 
													Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
												new Integer(result),
												conexaoPrep);
                }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "retiraPromocoesAssinante", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
            this.operacoes.insereEvento(dataProcessamento,
            							msisdn,
										Definicoes.TIPO_APR_PROMOCAO_SAIDA,
										null,
										null,
										new Double(0.0),
										new Integer(motivo),
										operador,
										Definicoes.PROCESSO_ERRO,
										new Integer(result),
										conexaoPrep);
        }
        
        return result;
    }
    
    /**
     *	Efetua a troca de promocao do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocaoNova			Identificador da nova promocao do assinante.
     *	@param		promocaoAntiga			Identificador da antiga promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPromocaoAssinante(String msisdn, 
    									int promocaoNova, 
										int promocaoAntiga, 
										Date dataProcessamento, 
										String operador, 
										int motivo, 
										PREPConexao conexaoPrep)
    {
        short				result				= -1;
        Assinante			assinante			= null;
        PromocaoAssinante	pAssinanteNova		= null;
        PromocaoAssinante	pAssinanteAntiga	= null;
        Integer				idtPromocaoNova		= new Integer(promocaoNova);
        Integer				idtPromocaoAntiga	= new Integer(promocaoAntiga);
        
        try
        {
            //Executando consulta do assinante na plataforma.
            assinante = this.consulta.getAssinanteGPPTecnomen(msisdn);
            Integer indRecarga = this.consulta.getIndRecarga(msisdn, conexaoPrep);
            assinante.setNumRecargas((indRecarga != null) ? indRecarga.intValue() : 0);

            //Obtendo os objetos necessarios para a consulta de informacoes da promocao do assinante.
            Integer	idtCodigoNacional	= Integer.valueOf(msisdn.substring(2, 4));
            Integer idtPlanoPreco		= new Integer(assinante.getPlanoPreco());
            
            //Obtendo a lista de promocoes do assinante.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            
            //Percorrendo a lista de promocoes do assinante para localizar a promocao antiga. A localizacao da promocao
            //antiga e necessaria para que a validacao da troca de promocao desconsidere a promocoao a ser trocada na
            //lista de promocoes do assinate.
            for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
            {
                PromocaoAssinante pAssinante = (PromocaoAssinante)iterator.next();
                if(pAssinante.getPromocao().getIdtPromocao() == promocaoAntiga)
                {
                    pAssinanteAntiga = pAssinante;
                    break;
                }
            }
            
            //Gerando o objeto PromocaoAssinante para operacoes de validacao e inclusao do assinante na promocao.
            //OBS: O objeto PromocaoDiaExecucao nao e setado aqui porque como o objeto representando a promocao antiga
            //do assinante pode ser NULL, e o metodo de consulta depende da data de entrada na promocao antiga, 
            //pode haver a possibilidade de NullPointerException.
            pAssinanteNova = new PromocaoAssinante();
            pAssinanteNova.setIdtMsisdn(msisdn);
            pAssinanteNova.setPromocao(this.consulta.getPromocao(new Integer(promocaoNova)));
            pAssinanteNova.setAssinante(assinante);
            pAssinanteNova.setCodigoNacional(this.consulta.getPromocaoCodigoNacional(idtPromocaoNova, idtCodigoNacional));
            pAssinanteNova.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocaoNova, idtPlanoPreco));
            
            result = this.trocaPromocaoAssinante(pAssinanteNova, pAssinanteAntiga, promocoesAssinante, dataProcessamento, conexaoPrep);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPromocaoAssinante", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            this.operacoes.insereEvento(dataProcessamento,
            							msisdn,
										Definicoes.TIPO_APR_PROMOCAO_TROCA,
										String.valueOf(idtPromocaoAntiga.intValue()),
										String.valueOf(idtPromocaoNova.intValue()),
										new Double(0.0),
										new Integer(motivo),
										operador,
										(result == Definicoes.RET_OPERACAO_OK) ? 
											Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
										new Integer(result),
										conexaoPrep);
        }
        
        return result;
    }

    /**
     *	Efetua a troca de promocao do assinante.
     *
     *	@param		pAssinanteNova			Informacoes referentes a nova promocao do assinante.
     *	@param		pAssinanteAntiga		Informacoes referentes a antiga promocao do assinante.
     *	@param		promocoesAssinante		Lista de promocoes do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocaPromocaoAssinante(PromocaoAssinante pAssinanteNova, 
    									PromocaoAssinante pAssinanteAntiga, 
										Collection promocoesAssinante, 
										Date dataProcessamento, 
										PREPConexao conexaoPrep)
    {
        short result = -1;
        
        try
        {
            //Validando a entrada do assinante na promocao.
            result = this.validaTrocaPromocaoAssinante(pAssinanteNova, pAssinanteAntiga, promocoesAssinante);
            switch(result)
            {
            	case Definicoes.RET_OPERACAO_OK:
            	{
            	    String	idtMsisdn			= pAssinanteAntiga.getIdtMsisdn();
            	    Integer	idtPromocaoAntiga	= new Integer(pAssinanteAntiga.getPromocao().getIdtPromocao());
            	    Integer idtPromocaoNova		= new Integer(pAssinanteNova.getPromocao().getIdtPromocao());
            	    Date	datEntradaPromocao	= pAssinanteAntiga.getDatEntradaPromocao();
            	    //Data de entrada na promocao.
                    pAssinanteNova.setDatEntradaPromocao(datEntradaPromocao);
                    //Status da promocao do assinante.
                    pAssinanteNova.setStatus(pAssinanteAntiga.getStatus());
                    //Indicador de insencao de limite.
                    pAssinanteNova.setIndIsentoLimite(pAssinanteAntiga.getIndIsentoLimite());
                    //Configuracao de dias de execucao da promocao do assinante.
                    pAssinanteNova.setDiasExecucao(this.consulta.getPromocaoDiasExecucao(idtPromocaoNova, datEntradaPromocao));
                    //Data de execucao.
                    this.gerenciadorData.atualizarDataExecucao(pAssinanteNova, dataProcessamento);
                    //Inserindo a promocao do assinante na base de dados.                	
                    if(this.operacoes.atualizaPromocaoAssinante(idtMsisdn, idtPromocaoAntiga, pAssinanteNova, conexaoPrep))
                    {
                        promocoesAssinante.remove(pAssinanteAntiga);
                        promocoesAssinante.add(pAssinanteNova);
                    }
                    else
                        result = Definicoes.RET_ERRO_TECNICO; 
                    
            	    break;
            	}
                case Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO:
                {
                    super.log(Definicoes.DEBUG, 
                    		  "trocaPromocaoAssinante", 
							  pAssinanteNova + " - Plano de Preco invalido.");
                    
                    //Verificando se a promocao exige que o assinante troque o plano para o seu espelho.
                    Assinante assinante = pAssinanteNova.getAssinante();
                    int planoAssinante = assinante.getPlanoPreco();
                    String tipoEspelhamento = pAssinanteNova.getPromocao().getTipEspelhamento();
                    if((this.trocarPlanoEspelho(assinante, tipoEspelhamento, (short)-1, dataProcessamento, conexaoPrep) == Definicoes.RET_OPERACAO_OK) &&
                       (planoAssinante != assinante.getPlanoPreco()))
                    {
                    	//Obtendo as novas informacoes de mapeamento de Promocao / Plano de Preco.
                        Integer idtPromocao     = new Integer(pAssinanteNova.getPromocao().getIdtPromocao());
                        Integer idtPlanoPreco   = new Integer(assinante.getPlanoPreco()); 
                        pAssinanteNova.setPlanoPreco(this.consulta.getPromocaoPlanoPreco(idtPromocao, idtPlanoPreco));
                        result = this.trocaPromocaoAssinante(pAssinanteNova, pAssinanteAntiga, promocoesAssinante, dataProcessamento, conexaoPrep);
                    }
                    
                    break;
                }
                case Definicoes.RET_PROMOCAO_PENDENTE_RECARGA:
                {
                    super.log(Definicoes.DEBUG, 
                  		  	  "trocaPromocaoAssinante", 
							  pAssinanteNova + " - Pendente de primeira recarga.");
                    
                    String	idtMsisdn			= pAssinanteAntiga.getIdtMsisdn();
                    Integer	idtPromocaoAntiga	= new Integer(pAssinanteAntiga.getPromocao().getIdtPromocao());
                    //Assinante com status Pendente de Primeira Recarga.
                    Integer idtStatus = new Integer(PromocaoStatusAssinante.STATUS_PENDENTE_RECARGA);
                    pAssinanteNova.setStatus(this.consulta.getPromocaoStatusAssinante(idtStatus));
                    //Atribuindo a data de entrada do assinante na nova promocao de acordo com a promocao antiga.
                    pAssinanteNova.setDatEntradaPromocao(pAssinanteAntiga.getDatEntradaPromocao());
                    //Indicador de insencao de limite.
                    pAssinanteNova.setIndIsentoLimite(pAssinanteAntiga.getIndIsentoLimite());
                	//Data de execucao.
                	this.gerenciadorData.atualizarDataExecucao(pAssinanteNova, dataProcessamento);
                    //Atualizando a promocao do assinante.
                	if(this.operacoes.atualizaPromocaoAssinante(idtMsisdn, idtPromocaoAntiga, pAssinanteNova, conexaoPrep))
                	{
                	    promocoesAssinante.remove(pAssinanteAntiga);
                	    promocoesAssinante.add(pAssinanteNova);
                	    result = Definicoes.RET_OPERACAO_OK;
                	}
                	else
                	    result = Definicoes.RET_ERRO_TECNICO; 
                    
                    break;
                }
            	default: break;
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPromocaoAssinante", 
					  "Antiga: "     + pAssinanteAntiga + 
					  "Nova: "       + pAssinanteNova   + 
					  " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        return result;
    }
    
    /**
     *	Gerencia as promocoes do assinante em funcao da troca de plano de preco do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		assinante				Informacoes do assinante na plataforma.
     *	@param		planoNovo				PlanoNovo do assinante.
     *	@param		planoAntigo				Plano antigo do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da validacao.
     */
    public short trocaPlanoPromocoesAssinante(String msisdn, 
    										  Assinante assinante, 
											  short planoNovo, 
											  short planoAntigo, 
											  Date dataProcessamento, 
											  String operador, 
											  int motivo, 
											  PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        boolean inserirEvento = false;
        
        try
        {
            //Criando objeto para obter a data/hora dos registros dos varios eventos. Como a chave primaria na
            //TBL_APR_EVENTOS e formada pelo par (MSISDN, Data de Aprovisionamento), segue a regra do calculo da data/hora
            //dos varios eventos do metodo:
            //1 - O evento TROCA_PLANO (do processo de troca de plano, que invoca este metodo) recebe dataProcessamento;
            //2 - O evento PROMOCAO_TROCA_PLANO recebe dataProcessamento + 1 segundo;
            //3 - Cada evento PROMOCAO_SAIDA (caso a promocoao nao se aplique ao novo plano) recebe dataProcessamento do
            //    evento anterior + 1 segundo.
            Calendar calProcessamento = Calendar.getInstance();
            calProcessamento.setTime(dataProcessamento);
            calProcessamento.add(Calendar.SECOND, 1);
            dataProcessamento = calProcessamento.getTime();
            
            //Executando consulta das promocoes do assinante, caso esta ja nao tenha sido feita.
            Collection promocoesAssinante = this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
            
            //O flag inserirEvento indica se o processo necessita inserir o evento na tabela. Se o assinante nao 
            //possuir promocoes, nao e necessario inserir o evento.
            inserirEvento = (promocoesAssinante.size() > 0) ? true : false;
            
            for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
            {
                PromocaoAssinante pAssinante = (PromocaoAssinante)iterator.next();
                Integer idtPromocao = new Integer(pAssinante.getPromocao().getIdtPromocao());
                
                super.log(Definicoes.DEBUG, 
                		  "trocaPlanoPromocoesAssinante", 
						  pAssinante + " - Gerenciando a promocao do assinante.");
             
                if(validaTrocaPlanoPromocaoAssinante(pAssinante) == Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO)
                {
                    super.log(Definicoes.DEBUG, 
                    		  "trocaPlanoPromocoesAssinante", 
							  pAssinante + " - Promocao nao se aplica ao novo plano de preco.");

                    calProcessamento.add(Calendar.SECOND, 1);
                    
                    if(this.retiraPromocaoAssinante(msisdn, 
                                                    idtPromocao.intValue(), 
                                                    calProcessamento.getTime(), 
                                                    operador, 
                                                    Definicoes.CTRL_PROMOCAO_MOTIVO_PLANO_NOK,
                                                    conexaoPrep) == Definicoes.RET_OPERACAO_OK)
                        iterator.remove();
                    else
                        result = Definicoes.RET_PROMOCAO_NOK_RETIRADA_NOK;
                }
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaPlanoPromocoesAssinante", 
					  "MSISDN: " + msisdn + " - Excecao: " + e);
            //Em caso de erro tecnico e necessario inserir o evento.
            inserirEvento = true;
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            if(inserirEvento)
                this.operacoes.insereEvento(dataProcessamento,
                							msisdn,
											Definicoes.TIPO_APR_PROMOCAO_TROCA_PLANO,
											String.valueOf(planoAntigo),
											String.valueOf(planoNovo),
											new Double(0.0),
											new Integer(motivo),
											operador,
											(result == Definicoes.RET_OPERACAO_OK) ? 
												Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
											new Integer(result),
											conexaoPrep);
        }
        
        return result;
    }
   
    /**
     *	Gerencia as promocoes do assinante em funcao da troca de MSISDN do assinante.
     *
     *	@param		msisdnNovo				MSISDN novo do assinante.
     *	@param		msisdnAntigo			MSISDN antigo do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		operador				Nome do operador.
     *	@param		motivo					Motivo de execucao da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da validacao.
     */
    public short trocaMsisdnPromocoesAssinante(String msisdnNovo, 
    										   String msisdnAntigo, 
											   Date dataProcessamento, 
											   String operador, 
											   int motivo, 
											   PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        boolean inserirEvento = false;
        
        try
        {
            if(this.operacoes.trocaMsisdnPromocoesAssinante(msisdnNovo, msisdnAntigo, conexaoPrep))
                inserirEvento = true;
            else
                inserirEvento = false;
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocaMsisdnPromocoesAssinante", 
					  "MSISDN antigo: " + msisdnAntigo + " - MSISDN novo: " + msisdnNovo + " - Excecao: " + e);
            //Em caso de erro tecnico e necessario inserir o evento.
            inserirEvento = true;
            result = Definicoes.RET_ERRO_TECNICO;
        }
        finally
        {
            //Se o assinante nao possui promocoes, nao e necessario inserir eventos.
            if(inserirEvento)
            {
                //Ajustando a data de processamento. Para evitar violacao de chave primaria durante a insercao dos 
                //eventos, considera-se que a data de processamento pertence ao metodo que invocou este metodo. A data  
                //de processamento deste metodo sera 1 segundo depois.
                Calendar calProcessamento = Calendar.getInstance();
                calProcessamento.setTime(dataProcessamento);
                calProcessamento.add(Calendar.SECOND, 1);
                
                this.operacoes.insereEvento(calProcessamento.getTime(),
                							msisdnNovo,
											Definicoes.TIPO_APR_PROMOCAO_TROCA_MSISDN,
											msisdnAntigo,
											msisdnNovo,
											new Double(0.0),
											new Integer(motivo),
											operador,
											(result == Definicoes.RET_OPERACAO_OK) ? 
												Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
											new Integer(result),
											conexaoPrep);
                this.operacoes.insereEvento(calProcessamento.getTime(),
                							msisdnAntigo,
											Definicoes.TIPO_APR_PROMOCAO_TROCA_MSISDN,
											msisdnAntigo,
											msisdnNovo,
											new Double(0.0),
											new Integer(motivo),
											operador,
											(result == Definicoes.RET_OPERACAO_OK) ? 
												Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
											new Integer(result),
											conexaoPrep);
            }
        }
        
        return result;
    }
    
    /**
     *	Altera o status da promocao do assinante.
     *
     *	@param		pAssinante				Informacoes da promocao do assinante.
     *	@param		novoStatus				Informacoes do novo status da promocao do assinante.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocarStatusPromocaoAssinante(PromocaoAssinante pAssinante, 
    										   PromocaoStatusAssinante novoStatus,
											   Date dataProcessamento,
											   PREPConexao conexaoPrep)
	{
    	short result = Definicoes.RET_OPERACAO_OK;
    	
    	try
		{
    		if(pAssinante != null)
    		{
    			String	idtMsisdn	= pAssinante.getIdtMsisdn();
    			Integer idtPromocao	= new Integer(pAssinante.getPromocao().getIdtPromocao());
    			//Atribuindo o novo status da promocao do assinante.
    			pAssinante.setStatus(novoStatus);
    			//Gerenciando a data de execucao da promocao do assinante em funcao do novo status.
    			this.gerenciadorData.atualizarDataExecucao(pAssinante, dataProcessamento);
    			//Atualizando as informacoes do assinante no banco de dados.
    			this.operacoes.atualizaPromocaoAssinante(idtMsisdn, idtPromocao, pAssinante, conexaoPrep);
    		}
		}
    	catch(Exception e)
		{
    		super.log(Definicoes.ERRO, 
    				  "trocarStatusPromocaoAssinante", 
					  pAssinante + " - " + novoStatus + " - Excecao: " + e);
    		result = Definicoes.RET_ERRO_TECNICO;
		}
    	
    	return result;
	}
    
    /**
     *	Consulta uma promocao do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocao				Identificador da promocao.
     *	@param		assinante				Informacoes do assinante. 
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes da promocao do assinante.
     *	@throws		Exception
     */
    public PromocaoAssinante consultaPromocaoAssinante(String msisdn, 
    												   int promocao, 
													   Assinante assinante, 
													   PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta pela promocao do assinante.
        return this.consulta.getPromocaoAssinante(msisdn, new Integer(promocao), assinante, conexaoPrep);
    }

    /**
     *	Consulta uma promocao do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		promocao				Identificador da promocao.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Informacoes da promocao do assinante.
     *	@throws		Exception
     */
    public PromocaoAssinante consultaPromocaoAssinante(String msisdn, 
    												   int promocao, 
													   boolean consultarTec, 
													   PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta de assinante no GPP.
        Assinante assinante = this.consulta.getAssinanteGPP(msisdn, consultarTec, conexaoPrep);
        //Executando consulta pela promocao do assinante.
        return this.consultaPromocaoAssinante(msisdn, promocao, assinante, conexaoPrep);
    }

    /**
     *	Consulta todas as promocoes do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		assinante				Informacoes do assinante. 
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection consultaPromocoesAssinante(String msisdn, Assinante assinante, PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta pelas promocoes do assinante.
        return this.consulta.getPromocoesAssinante(msisdn, assinante, conexaoPrep);
    }

    /**
     *	Consulta todas as promocoes do assinante.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection consultaPromocoesAssinante(String msisdn, boolean consultarTec, PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta de assinante no GPP.
        Assinante assinante = this.consulta.getAssinanteGPP(msisdn, consultarTec, conexaoPrep);
        //Executando consulta pelas promocoes do assinante.
        return this.consultaPromocoesAssinante(msisdn, assinante, conexaoPrep);
    }

    /**
     *	Consulta as promocoes do assinante da categoria passada por parametro.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		categoria				Categoria das promocoes.
     *	@param		assinante				Informacoes do assinante. 
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection consultaPromocoesAssinante(String msisdn, int categoria, Assinante assinante, PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta pelas promocoes do assinante.
        return this.consulta.getPromocoesAssinante(msisdn, new Integer(categoria), assinante, conexaoPrep);
    }
    
    /**
     *	Consulta as promocoes do assinante que pertencem a uma determinada categoria.
     *
     *	@param		msisdn					MSISDN do assinante.
     *	@param		categoria				Categoria das promocoes.
     *	@param		consultarTec			Flag indicando se a consulta de assinante deve ser feita na plataforma Tecnomen.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Lista de promocoes do assinante.
     *	@throws		Exception
     */
    public Collection consultaPromocoesAssinante(String msisdn, int categoria, boolean consultarTec, PREPConexao conexaoPrep) throws Exception
    {
        //Executando consulta de assinante no GPP.
        Assinante assinante = this.consulta.getAssinanteGPP(msisdn, consultarTec, conexaoPrep);
        //Executando consulta pelas promocoes do assinante.
        return this.consultaPromocoesAssinante(msisdn, categoria, assinante, conexaoPrep);
    }
    
    /**
     *	Executa concessao ou estorno de bonus da promocao para o assinante.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@param		tipoExecucao			Tipo de execucao da promocao.
     *	@param		valorBonus				Valor de bonus a ser concedido.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *  @param      nsuInstituicao          NSU da instituicao (se for null, sera a identificacao da recarga)
     *	@return		Codigo de retorno da operacao.
     */
    public short executaBonus(PromocaoAssinante			pAssinante, 
    						  String					tipoExecucao,
    						  double					valorBonus, 
    						  Date						dataProcessamento,
    						  String					nsuInstituicao)
    {
        short					result			= Definicoes.RET_ERRO_TECNICO;
        PromocaoTipoTransacao	tipoTransacao	= null;
        
        try
        {
        	tipoTransacao = (PromocaoTipoTransacao)pAssinante.getTiposTransacao(tipoExecucao).iterator().next();
        	
        	ValoresRecarga bonus = new ValoresRecarga();
        	bonus.setSaldoPrincipal(0.0);
        	bonus.setSaldoBonus(valorBonus);
        	bonus.setSaldoSMS(0.0);
        	bonus.setSaldoGPRS(0.0);
            
        	result = this.executaBonus(pAssinante.getAssinante(),
        							   tipoTransacao.getOrigem().getTipoTransacao(),
									   bonus,
									   dataProcessamento,
                                       nsuInstituicao);
        }
        catch(Exception e)
        {
        	super.log(Definicoes.ERRO, 
        			  "executaBonus", 
					  pAssinante + " - TT: " + tipoTransacao + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        return result;
    }
    
    /**
     *	Executa concessao ou estorno de bonus da promocao para o assinante.
     *
     *	@param		assinante				Informacoes referentes a promocao do assinante.
     *	@param		tipoTransacao			Tipo de execucao da promocao.
     *	@param		valoresRecarga			Valor de bonus a ser concedido.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *  @param      nsuInstituicao          NSU da instituicao (se for null, sera a identificacao da recarga)
     *	@return		Codigo de retorno da operacao.
     */
    public short executaBonus(Assinante assinante, String tipoTransacao, ValoresRecarga valoresRecarga, 
            Date dataProcessamento, String nsuInstituicao)
    {
        short result = Definicoes.RET_ERRO_TECNICO;
        
        try
        {
            String		msisdn			= assinante.getMSISDN();
            String		tipoLancamento	= this.consulta.getOrigemRecarga(tipoTransacao).getTipLancamento();
            
            result = this.ajusteGPP.executarAjuste(msisdn, 
                    							   tipoTransacao, 
                    							   Definicoes.TIPO_CREDITO_REAIS, 
                    							   valoresRecarga, 
                    							   tipoLancamento,
                    							   dataProcessamento,
                    							   Definicoes.SO_GPP,
                    							   this.nomeClasse,
                    							   assinante, 
												   null, 
												   true,
                                                   nsuInstituicao);
        }
        catch(Exception e)
        {
        	super.log(Definicoes.ERRO, 
        			  "executaBonus", 
					  assinante + " - TT: " + tipoTransacao + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
        
        return result;
    }
    
    /**
     *	Validacoes comuns a todas as operacoes relacionadas a promocoes.
     *
     *	@param		pAssinante				Informacoes referentes a nova promocao do assinante.
     *	@return		Codigo de retorno da validacao.
     */
    private short validacoesComuns(PromocaoAssinante pAssinante)
    {
        //Validacao: As informacoes da promocao do assinante sao validas.
        if(pAssinante == null) 
            return Definicoes.RET_PROMOCAO_ASSINANTE_NAO_EXISTE;
        //Validacao: A promocao e valida.
        if(pAssinante.getPromocao() == null)
            return Definicoes.RET_PROMOCAO_NAO_EXISTE;
        //Validacao: O assinante esta ativo na plataforma.
        Assinante assinante = pAssinante.getAssinante();
        if(assinante == null)
            return Definicoes.RET_MSISDN_NAO_ATIVO;
        if(assinante.getRetorno() != Definicoes.RET_OPERACAO_OK)
            return assinante.getRetorno();
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /**
     *	Validacoes de entrada de assinantes em promocoes.
     *
     *	@param		pAssinante				Informacoes referentes a nova promocao do assinante.
     *	@param		promocoesAssinante		Lista de promocoes do assinante.
     *	@return		Codigo de retorno da validacao.
     */
    private short validaInsercaoPromocaoAssinante(PromocaoAssinante pAssinante, Collection promocoesAssinante) 
    {
        //Validacoes comuns: A nova promocao do assinante deve ser valida.
        short validacao = this.validacoesComuns(pAssinante);
        if(validacao != Definicoes.RET_OPERACAO_OK)
            return validacao;

        Promocao promocaoNova = pAssinante.getPromocao();
        //Validacao: O assinante nao possui a promocao ou nao possui uma promocao da mesma categoria com 
        //cadastro exclusivo (neste caso o assinante nao pode ter mais de uma promocao da mesma categoria).
        for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
        {
            Promocao promocao = ((PromocaoAssinante)iterator.next()).getPromocao();
            if(promocao.equals(promocaoNova))
                return Definicoes.RET_PROMOCAO_ASSINANTE_JA_EXISTE;

            PromocaoCategoria categoria = promocao.getCategoria();
            if((categoria.equals(promocaoNova.getCategoria())) && (categoria.cadastroExclusivo()))
                return Definicoes.RET_PROMOCAO_CATEGORIA_EXCLUSIVA;
        }
        //Validacao: O relacionamento Promocao / Codigo Nacional do assinante e valido.
        if(pAssinante.getCodigoNacional() == null)
            return Definicoes.RET_PROMOCAO_CN_INVALIDO;
        //Validacao: O relacionamento Promocao / Plano de Preco do assinante e valido.
        //IMPORTANTE: A validacao do plano de preco deve ser anterior as validacoes acima porque o metodo de insercao
        //de assinantes em promocoes podera realizar uma troca de plano do assinante pelo plano espelho, efetuando uma
        //nova tentativa de insercao de promocao. Caso isto ocorra, se a promocao nao for validada por algum outro 
        //motivo, seria necessario fazer uma troca de plano de "rollback".
        if(pAssinante.getPlanoPreco() == null)
            return Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO;
        //Validacao: O assinante efetuou primeira recarga, caso a promocao tenha este requisito.
        Assinante assinante = pAssinante.getAssinante();
        if((promocaoNova.exigePrimeiraRecarga()) && (!assinante.fezRecarga()))
        	return Definicoes.RET_PROMOCAO_PENDENTE_RECARGA;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /**
     *	Validacoes de troca de promocoes de assinantes.
     *
     *	@param		pAssinanteNova			Informacoes referentes a nova promocao do assinante.
     *	@param		pAssinanteAntiga		Informacoes referentes a antiga promocao do assinante.
     *	@param		promocoesAssinante		Lista de promocoes do assinante.
     *	@return		Codigo de retorno da validacao.
     */
    private short validaTrocaPromocaoAssinante(PromocaoAssinante pAssinanteNova, PromocaoAssinante pAssinanteAntiga, Collection promocoesAssinante)
    {
        //Validacoes comuns: As promocoes nova e antiga devem ser validas.
        short validacao = this.validacoesComuns(pAssinanteNova);
        if((validacao != Definicoes.RET_OPERACAO_OK))
            return validacao;

        validacao = this.validacoesComuns(pAssinanteAntiga);
        if((validacao != Definicoes.RET_OPERACAO_OK))
            return validacao;

        Promocao promocaoNova	= pAssinanteNova.getPromocao();
        Promocao promocaoAntiga	= pAssinanteAntiga.getPromocao();
        //Validacao: O assinante nao possui a promocao ou nao possui uma promocao da mesma categoria com 
        //cadastro exclusivo (neste caso o assinante nao pode ter mais de uma promocao da mesma categoria).
        for(Iterator iterator = promocoesAssinante.iterator(); iterator.hasNext();)
        {
            Promocao promocao = ((PromocaoAssinante)iterator.next()).getPromocao();
            if(promocao.equals(promocaoNova))
                return Definicoes.RET_PROMOCAO_ASSINANTE_JA_EXISTE;

            PromocaoCategoria categoria = promocao.getCategoria();
            //OBS: A primeira linha da validacao abaixo descarta da lista de promocoes do assinante a informacao da 
            //promocao antiga, que esta sendo trocada pela nova.
            if((!promocaoAntiga.equals(promocao)) && (categoria.equals(promocaoNova.getCategoria())) && (categoria.cadastroExclusivo()))
                return Definicoes.RET_PROMOCAO_CATEGORIA_EXCLUSIVA;
        }
        //Validacao: O relacionamento Promocao / Plano de Preco do assinante e valido.
        //IMPORTANTE: A validacao do plano de preco deve ser anterior as validacoes acima porque o metodo de insercao
        //de assinantes em promocoes podera realizar uma troca de plano do assinante pelo plano espelho, efetuando uma
        //nova tentativa de insercao de promocao. Caso isto ocorra, se a promocao nao for validada por algum outro 
        //motivo, seria necessario fazer uma troca de plano de "rollback".
        if(pAssinanteNova.getPlanoPreco() == null)
            return Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO;
        //Validacao: O assinante efetuou primeira recarga, caso a promocao tenha este requisito.
        Assinante assinante = pAssinanteNova.getAssinante();
        if((promocaoNova.exigePrimeiraRecarga()) && (!assinante.fezRecarga()))
        	return Definicoes.RET_PROMOCAO_PENDENTE_RECARGA;
        
        return Definicoes.RET_OPERACAO_OK;
    }
    
    /**
     *	Validacoes de promocoes diante da troca de plano de preco dos assinantes.
     *
     *	@param		pAssinante				Informacoes referentes a promocao do assinante.
     *	@return		Codigo de retorno da validacao.
     */
    private short validaTrocaPlanoPromocaoAssinante(PromocaoAssinante pAssinante)
    {
        //Validacoes comuns: A promocao do assinante deve ser valida.
        short validacao = this.validacoesComuns(pAssinante);
        if(validacao != Definicoes.RET_OPERACAO_OK)
            return validacao;
        //Validacao: O relacionamento Promocao / Plano de Preco do assinante e valido.
        if(pAssinante.getPlanoPreco() == null)
            return Definicoes.RET_PROMOCAO_PLANO_PRECO_INVALIDO;
        
        return Definicoes.RET_OPERACAO_OK;
    }

    /**
     *	Calcula a data de execucao do bonus do assinante na fila de recargas.
     *
     *	@param		diaExecucao				Mapeamento Promocao / Dia de Execucao.
     *	@param		dataReferencia			Data de referencia para concessao do bonus.
     *	@return		Data de execucao na fila de recargas.
     */
    public Date calculaDataCredito(PromocaoDiaExecucao diaExecucao, Date dataReferencia) 
    {
        Date result = null;
        
        if((diaExecucao != null) && (diaExecucao.getNumDiaExecucaoRecarga() != null) && (dataReferencia != null))
        {
            Calendar calResult = Calendar.getInstance();
            calResult.setTime(dataReferencia);
            int diaRecarga = diaExecucao.getNumDiaExecucaoRecarga().intValue();
            int horaRecarga = (diaExecucao.getNumHoraExecucaoRecarga() != null) ? 
                diaExecucao.getNumHoraExecucaoRecarga().intValue() : 0;
            calResult.set(Calendar.DAY_OF_MONTH, diaRecarga);
            calResult.set(Calendar.HOUR_OF_DAY, horaRecarga);
            calResult.set(Calendar.MINUTE, 0);
            calResult.set(Calendar.SECOND, 0);
            calResult.set(Calendar.MILLISECOND, 0);
            //Obtendo o resultado.
            result = calResult.getTime();
        }
        
        return result;
    }
    
    /**
     *	Calcula e executa a troca de plano do assinante pelo seu espelho, caso seja necessario.
     *
     *	@param		assinante				Informacoes do assinante na plataforma.
     *  @param      tipoEspelhamento        Tipo de espelhamento que define o calculo dos planos.
     *  @param      planoPreco              Plano de preco informado pelo processo que necessita do calculo do
     *                                      espelhamento, se necessario.
     *	@param		dataProcessamento		Data de processamento da operacao.
     *	@param		conexaoPrep				Conexao com o banco de dados para execucao da consulta.
     *	@return		Codigo de retorno da operacao.
     */
    public short trocarPlanoEspelho(Assinante assinante, String tipoEspelhamento, short planoPreco, Date dataProcessamento, PREPConexao conexaoPrep)
    {
        short result = Definicoes.RET_OPERACAO_OK;
        
        TecnomenAprovisionamento conexaoTecnomen = null;
        
        try
        {
        	//Obtendo as informacoes do assinante.
        	String msisdn = assinante.getMSISDN();
        	short planoAssinante = assinante.getPlanoPreco();
            //Calculando o novo plano baseado na promocao informada.
            short planoEspelho = this.calcularPlano(tipoEspelhamento, planoPreco, planoAssinante);
            
            if((planoEspelho != -1) && (planoEspelho != planoAssinante))
            {
                //Efetuando o processo de troca de plano para adequar o plano do assinante de acordo com a 
                //nova promocao.
                super.log(Definicoes.DEBUG, 
                		  "trocarPlanoEspelho", 
						  assinante + " - Deve trocar para o plano espelho: " + planoEspelho);
                
                //Ajustando a data de processamento. Para evitar violacao de chave primaria durante a insercao dos 
                //eventos, considera-se que a data de processamento pertence ao metodo que invocou este metodo. A data  
                //de processamento deste metodo sera 1 segundo depois.
                Calendar calProcessamento = Calendar.getInstance();
                calProcessamento.setTime(dataProcessamento);
                calProcessamento.add(Calendar.SECOND, 1);
                
                try
                {
                	conexaoTecnomen = GerentePoolTecnomen.getInstance().getTecnomenAprovisionamento(super.logId);
                    result = conexaoTecnomen.trocarPlanoPrecoAssinante(assinante.getMSISDN(), planoEspelho);

                    if(result == Definicoes.RET_OPERACAO_OK)
                        assinante.setPlanoPreco(planoEspelho);
                }
                catch(Exception e)
                {
                    super.log(Definicoes.ERRO, 
                    		  "trocarPlanoEspelho", 
							  assinante + " - Excecao: " + e);
                    result = Definicoes.RET_ERRO_TECNICO;
                }
                finally
                {
                	GerentePoolTecnomen.getInstance().liberarConexao(conexaoTecnomen);
                    this.operacoes.insereEvento(calProcessamento.getTime(),
                    							msisdn,
												Definicoes.TIPO_APR_TROCA_PLANO,
												String.valueOf(planoAssinante),
												String.valueOf(planoEspelho),
												new Double(0.0),
												new Integer(Definicoes.CTRL_PROMOCAO_MOTIVO_DIVERG_PLANO_PROMOCAO),
												super.nomeClasse,
												(result == Definicoes.RET_OPERACAO_OK) ? 
													Definicoes.PROCESSO_SUCESSO : Definicoes.PROCESSO_ERRO,
												new Integer(result),
												conexaoPrep);
                }
                
                if(result != Definicoes.RET_OPERACAO_OK)
                    super.log(Definicoes.WARN, 
                    		  "trocarPlanoEspelho", 
							  assinante + " - Nao foi possivel trocar o plano do assinante de acordo com a nova promocao.");
            }
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO, 
            		  "trocarPlanoEspelho", 
					  assinante + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }
    
        return result;
    }

    /**
     *  Calcula o plano para o assinante. Este calculo baseia-se nas regras de espelhamento em funcao do tipo de 
     *  espelhamento (que e definido pelo processo que necessita do calculo do plano) e no plano original informado.
     *	Ao receber estes parametros, o metodo busca no mapeamento de espelhamento de planos qual o plano efetivo 
     *	(espelho) que deve ser retornado. Se nao houver espelhamento (ex: LigMix), e retornado o plano passado por 
     *	parametro. 
     *
     *  @param      tipoEspelhamento        Tipo de espelhamento que define o calculo dos planos.
     *  @param      planoPreco              Plano de preco informado pelo processo que necessita do calculo do
     *                                      espelhamento, se necessario.
     *  @param      planoAssinante          Plano do assinante, se existir.
     *  @return     Codigo do plano efetivo do assinante.
     */
    public short calcularPlano(String tipoEspelhamento, short planoPreco, short planoAssinante)
    {
        try
        {
            //Obtendo as informacoes de espelhamento de plano baseado no tipo de espelhamento, no plano informado
            //e no plano atual do assinante.
            PlanoEspelho espelho = this.consulta.getPlanoEspelho(tipoEspelhamento, 
            													 String.valueOf(planoPreco), 
																 String.valueOf(planoAssinante));
            
            if(espelho != null)
                return Short.parseShort(espelho.getIdtPlanoEspelho());
            	
            //Caso nao haja nenhuma informacao de espelhamento para o tipo informado, o plano efetivo deve ser o informado.
            return planoPreco;
        }
        catch(Exception e)
        {
            //Este metodo nao pode gerar excecao uma vez que pode parar os processos de ativacao e troca de plano.
            //Se algum erro ocorrer o processo deve logar e retornar o plano original.
            super.log(Definicoes.ERRO, 
            		  "calcularPlano", 
					  "Tipo de espelhamento: "  + tipoEspelhamento + 
					  " - Plano de Preco: "     + planoPreco       + 
					  " - Plano do Assinante: " + planoAssinante   + 
					  " - Excecao: " + e);
            
            return planoPreco;
        }
    }
    
}
