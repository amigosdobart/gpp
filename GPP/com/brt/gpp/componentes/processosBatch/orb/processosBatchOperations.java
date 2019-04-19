package com.brt.gpp.componentes.processosBatch.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface processosBatch {
  ...
};
 * </pre>
 */
public interface processosBatchOperations {
  /**
   * <pre>
   *   boolean gravaMensagemSMS (in string aMsisdn, in string aMensagem,
                            in short aPrioridade, in string aTipo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravaMensagemSMS (java.lang.String aMsisdn, 
                                   java.lang.String aMensagem, 
                                   short aPrioridade, 
                                   java.lang.String aTipo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviaUsuariosShutdown (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaUsuariosShutdown (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviarUsuariosStatusNormal (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarUsuariosStatusNormal (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean executaRecargaRecorrente ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean executaRecargaRecorrente () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short executaRecargaMicrosiga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaRecargaMicrosiga () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviaInfosRecarga (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosRecarga (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviaBonusCSP14 (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaBonusCSP14 (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void liberaBumerangue (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void liberaBumerangue (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviaInfosCartaoUnico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosCartaoUnico () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short emiteNFBonusTLDC (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short emiteNFBonusTLDC (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short estornaBonusSobreBonus ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short estornaBonusSobreBonus () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short enviarRecargasConciliacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarRecargasConciliacao (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short executaTratamentoVoucher (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaTratamentoVoucher (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short executaContestacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaContestacao (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean envioComprovanteServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean envioComprovanteServico () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short importaArquivosCDR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaArquivosCDR (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short importaAssinantes ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaAssinantes () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short importaUsuarioPortalNDS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaUsuarioPortalNDS () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short importaEstoqueSap ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaEstoqueSap () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short atualizaDiasSemRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaDiasSemRecarga () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short sumarizarProdutoPlano (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarProdutoPlano (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short sumarizarAjustes (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarAjustes (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short sumarizarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidade (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short sumarizarContabilidadeCN (in string aData, in string aCN)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidadeCN (java.lang.String aData, 
                                         java.lang.String aCN) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short consolidarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short consolidarContabilidade (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short exportarTabelasDW ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short exportarTabelasDW () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short importaPedidosCriacaoVoucher ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaPedidosCriacaoVoucher () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean gravaDadosArquivoOrdem (in string nomeArquivo,
                                  in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDados buffer)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravaDadosArquivoOrdem (java.lang.String nomeArquivo, 
                                         byte[] buffer) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string getUserIDRequisitante (in long long numOrdem);
   * </pre>
   */
  public java.lang.String getUserIDRequisitante (long numOrdem);

  /**
   * <pre>
   *   short enviarConsolidacaoSCR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarConsolidacaoSCR (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short calcularIndiceBonificacao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short calcularIndiceBonificacao (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short bloqueioAutomaticoServicoPorSaldo (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoPorSaldo (java.lang.String dataReferencia) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short bloqueioAutomaticoServicoIncluindoRE (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoIncluindoRE (java.lang.String dataReferencia) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void enviaPedidoPorEMail (in long long numeroOrdem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaPedidoPorEMail (long numeroOrdem) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void enviaContingenciaSolicitada ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaContingenciaSolicitada () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short gerenciarPromocao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenciarPromocao (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short promocaoPrepago (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short promocaoPrepago (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short contingenciaDaContingencia (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short contingenciaDaContingencia (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void cadastraAssinantesPromocaoLondrina (in double valorBonus)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void cadastraAssinantesPromocaoLondrina (double valorBonus) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void executarPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executarPromocaoLondrina (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void avisaRecargaPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void avisaRecargaPromocaoLondrina (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void atualizaNumLotePedido (in long long numOrdem, in long numItem,
                              in long long numLoteIni, in long long numLoteFim)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void atualizaNumLotePedido (long numOrdem, 
                                     int numItem, 
                                     long numLoteIni, 
                                     long numLoteFim) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   long long getQtdeCartoes (in long long numOrdem, in long numItem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long getQtdeCartoes (long numOrdem, 
                              int numItem) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short gerenteFeliz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenteFeliz () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short atualizaLimiteCreditoVarejo ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaLimiteCreditoVarejo () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short marretaGPP (in string paramIn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short marretaGPP (java.lang.String paramIn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void gerarArquivoCobilling (in string csp, in string inicio, in string fim,
                              in string UF)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void gerarArquivoCobilling (java.lang.String csp, 
                                     java.lang.String inicio, 
                                     java.lang.String fim, 
                                     java.lang.String UF) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void enviaDadosPulaPulaDW (in string aData, in short aPromocao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaDadosPulaPulaDW (java.lang.String aData, 
                                    short aPromocao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void reiniciaCicloTres ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void reiniciaCicloTres () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void processaBumerangue14Dia (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Dia (java.lang.String aData) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void processaBumerangue14Mes (in short mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Mes (short mes) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short sumarizaAssinantesShutdown (in string dataAnalise)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizaAssinantesShutdown (java.lang.String dataAnalise) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void gravaNotificacaoSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void gravaNotificacaoSMS () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void aprovisionarAssinantesMMS (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void aprovisionarAssinantesMMS (java.lang.String data) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short executaConcessaoPulaPula (in string tipoExecucao,
                                  in string referencia, in long promocao);
   * </pre>
   */
  public short executaConcessaoPulaPula (java.lang.String tipoExecucao, 
                                         java.lang.String referencia, 
                                         int promocao);

  /**
   * <pre>
   *   short sumarizaRecargasAssinantes (in string referencia);
   * </pre>
   */
  public short sumarizaRecargasAssinantes (java.lang.String referencia);

  /**
   * <pre>
   *   void executaProcessoBatch (in long idProcessoBatch,
                             in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatch parametros)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executaProcessoBatch (int idProcessoBatch, 
                                    java.lang.String[] parametros) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

}
