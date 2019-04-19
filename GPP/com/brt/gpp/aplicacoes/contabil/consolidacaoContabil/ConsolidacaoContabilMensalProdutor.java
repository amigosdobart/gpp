package com.brt.gpp.aplicacoes.contabil.consolidacaoContabil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapPeriodoContabil;
import com.brt.gpp.comum.mapeamentos.entidade.PeriodoContabil;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 *  Classe responsavel pela sumarizacao da tabela TBL_CON_CONTABIL_DIA para
 *  TBL_CON_CONTABIL de acordo com o periodo contabil informado.
 *  
 *  Parametro de entrada 0: dia de processamento (dd/mm/yyyy)            [obrigatorio]
 *  
 *  @author Magno Batista Correa, Bernardo Vergne Dias, Daniel Ferreira
 *  Criado em 23/11/2007
 */
public class ConsolidacaoContabilMensalProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
    private PREPConexao conexaoPrep;        //  Conexão com o banco.
    private String status;                  //  Status de processamento.
    private String mensagem;                //  Mensagem sobre o resultado do processamento.
    
    private PeriodoContabil               periodoContabil; 
    private Date                          dataReferencia;
        
    /**
     *  @param idLog Identificador de LOG.
     */
    public ConsolidacaoContabilMensalProdutor(long logId)
    {
        super(logId, Definicoes.CL_CONSOLIDACAO_CONTABIL_MENSAL_PRODUTOR);      
        this.status         = Definicoes.PROCESSO_SUCESSO;
        this.mensagem       = "";
        this.conexaoPrep    = null;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(String[])
     */
    public void startup(String[] params) throws Exception
    {
        super.log(Definicoes.INFO, "startup", "Inicio da Consolidacao Contabil Mensal");
        
        try
        {
            validaParametro(params);
            
            // seleciona conexão do pool Prep Conexão  
            conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
            
            // cria um VO
            ConsolidacaoContabilDiariaVO vo = new ConsolidacaoContabilDiariaVO(
                    this.dataReferencia, this.periodoContabil.getIdtPeriodoContabil());
            
            // executa as instrucoes SQL
            ConsolidacaoContabilMensalDAO dao = new ConsolidacaoContabilMensalDAO(this.conexaoPrep, this.logId);
            
            int retorno = dao.consolidarPeriodoContabil(vo);
            super.log(Definicoes.INFO, "startup", "Concluido sumarizacao da TBL_CON_CONTABIL. VO: " + vo + ". Retorno: " + retorno);
            
            dao.incluiPerdaCDRs(vo);
            super.log(Definicoes.INFO, "startup", "Concluido inclusao de perda de CDRs. VO: " + vo);
            
            dao.eliminaZeros(vo);
            super.log(Definicoes.INFO, "startup", "Concluido eliminacao de zeros. VO: " + vo);
            
            this.mensagem = "Processo concluido. Retorno: " + retorno;
        }
        catch(Exception e)
        {
            this.status = Definicoes.PROCESSO_ERRO;
            this.mensagem = "Excecao ao iniciar ConsolidacaoContabilMensal: " + e;
            throw e;
        }        
        
        super.log(Definicoes.INFO, "startup", "Fim da Consolidacao Contabil Mensal. Parametro: " + params[0]);
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public synchronized Object next() throws Exception
    {        
        return null;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
    {
        super.log(Definicoes.DEBUG, "finish", "Inicio");
        
        try
        {
            // Libera a conexao com o banco
            gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
        }
        catch(Exception e)
        {
            this.status = Definicoes.PROCESSO_ERRO;
            this.mensagem = "Excecao ao finalizar ConsolidacaoContabilMensal: " + e;
            throw e;
        }
        
        super.log(Definicoes.DEBUG, "finish", "Fim");
    }   
    
    /**
     * Este metodo realiza a verificacao de parametros
     * 
     * @param params Array de valores contendo os parametros para a execucao do processo
     * @throws GPPInternalErrorException, Exception
     */
    private void validaParametro(String params[]) throws GPPInternalErrorException, Exception
    {
        // uma data valida
        if (params == null || params.length == 0 ||params[0] == null)
            throw new GPPInternalErrorException("Parametro de data (dd/mm/aaaa) obrigatorio para o processo.");
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try
        {
            this.dataReferencia    = sdf.parse(params[0]);
        }
        catch(ParseException pe)
        {
            throw new GPPInternalErrorException("Data invalida ou esta em formato invalido. Valor: "+params[0]);
        }
        periodoContabil = MapPeriodoContabil.getInstance().getPeriodoContabil(this.dataReferencia);
        if(periodoContabil.isIndFechado())
            throw new GPPInternalErrorException("Periodo contabil fechado! Nao pode ser reprocessado.");

    }
    
    /**
    *   @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
    */
    public void handleException(){ }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
    public int getIdProcessoBatch()
    {
        return Definicoes.IND_CONSOLIDACAO_CONTABIL_MENSAL;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
    {
        return this.mensagem;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
    public String getStatusProcesso()
    {
        return this.status;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(String)
     */
    public void setStatusProcesso(String status)
    {
        this.status = status;
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
    {
        return new SimpleDateFormat(Definicoes.MASCARA_DATE).format(Calendar.getInstance().getTime());
    }
    
    /**
     *  @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
}
