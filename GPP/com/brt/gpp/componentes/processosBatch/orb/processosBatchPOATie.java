
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
public class processosBatchPOATie extends processosBatchPOA {
  private com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _delegate;
  private org.omg.PortableServer.POA _poa;

  public processosBatchPOATie (final com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _delegate) {
    this._delegate = _delegate;
  }

  public processosBatchPOATie (final com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _delegate, 
                              final org.omg.PortableServer.POA _poa) {
    this._delegate = _delegate;
    this._poa = _poa;
  }

  public com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _delegate () {
    return this._delegate;
  }

  public void _delegate (final com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations delegate) {
    this._delegate = delegate;
  }

  public org.omg.PortableServer.POA _default_POA () {
    if (_poa != null) {
      return _poa;
    } 
    else {
      return super._default_POA();
    }
  }

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
                                   java.lang.String aTipo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.gravaMensagemSMS(aMsisdn, aMensagem, aPrioridade, aTipo);
  }

  /**
   * <pre>
   *   short enviaUsuariosShutdown (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaUsuariosShutdown (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviaUsuariosShutdown(aData);
  }

  /**
   * <pre>
   *   short enviarUsuariosStatusNormal (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarUsuariosStatusNormal (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviarUsuariosStatusNormal(aData);
  }

  /**
   * <pre>
   *   boolean executaRecargaRecorrente ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean executaRecargaRecorrente () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.executaRecargaRecorrente();
  }

  /**
   * <pre>
   *   short executaRecargaMicrosiga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaRecargaMicrosiga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.executaRecargaMicrosiga();
  }

  /**
   * <pre>
   *   short enviaInfosRecarga (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosRecarga (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviaInfosRecarga(aData);
  }

  /**
   * <pre>
   *   short enviaBonusCSP14 (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaBonusCSP14 (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviaBonusCSP14(aData);
  }

  /**
   * <pre>
   *   void liberaBumerangue (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void liberaBumerangue (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.liberaBumerangue(aData);
  }

  /**
   * <pre>
   *   short enviaInfosCartaoUnico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosCartaoUnico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviaInfosCartaoUnico();
  }

  /**
   * <pre>
   *   short emiteNFBonusTLDC (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short emiteNFBonusTLDC (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.emiteNFBonusTLDC(aData);
  }

  /**
   * <pre>
   *   short estornaBonusSobreBonus ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short estornaBonusSobreBonus () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.estornaBonusSobreBonus();
  }

  /**
   * <pre>
   *   short enviarRecargasConciliacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarRecargasConciliacao (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviarRecargasConciliacao(aData);
  }

  /**
   * <pre>
   *   short executaTratamentoVoucher (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaTratamentoVoucher (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.executaTratamentoVoucher(aData);
  }

  /**
   * <pre>
   *   short executaContestacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaContestacao (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.executaContestacao(aData);
  }

  /**
   * <pre>
   *   boolean envioComprovanteServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean envioComprovanteServico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.envioComprovanteServico();
  }

  /**
   * <pre>
   *   short importaArquivosCDR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaArquivosCDR (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.importaArquivosCDR(aData);
  }

  /**
   * <pre>
   *   short importaAssinantes ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaAssinantes () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.importaAssinantes();
  }

  /**
   * <pre>
   *   short importaUsuarioPortalNDS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaUsuarioPortalNDS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.importaUsuarioPortalNDS();
  }

  /**
   * <pre>
   *   short importaEstoqueSap ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaEstoqueSap () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.importaEstoqueSap();
  }

  /**
   * <pre>
   *   short atualizaDiasSemRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaDiasSemRecarga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaDiasSemRecarga();
  }

  /**
   * <pre>
   *   short sumarizarProdutoPlano (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarProdutoPlano (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.sumarizarProdutoPlano(aData);
  }

  /**
   * <pre>
   *   short sumarizarAjustes (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarAjustes (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.sumarizarAjustes(aData);
  }

  /**
   * <pre>
   *   short sumarizarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidade (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.sumarizarContabilidade(aData);
  }

  /**
   * <pre>
   *   short sumarizarContabilidadeCN (in string aData, in string aCN)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidadeCN (java.lang.String aData, 
                                         java.lang.String aCN) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.sumarizarContabilidadeCN(aData, aCN);
  }

  /**
   * <pre>
   *   short consolidarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short consolidarContabilidade (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consolidarContabilidade(aData);
  }

  /**
   * <pre>
   *   short exportarTabelasDW ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short exportarTabelasDW () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.exportarTabelasDW();
  }

  /**
   * <pre>
   *   short importaPedidosCriacaoVoucher ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaPedidosCriacaoVoucher () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.importaPedidosCriacaoVoucher();
  }

  /**
   * <pre>
   *   boolean gravaDadosArquivoOrdem (in string nomeArquivo,
                                  in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDados buffer)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravaDadosArquivoOrdem (java.lang.String nomeArquivo, 
                                         byte[] buffer) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.gravaDadosArquivoOrdem(nomeArquivo, buffer);
  }

  /**
   * <pre>
   *   string getUserIDRequisitante (in long long numOrdem);
   * </pre>
   */
  public java.lang.String getUserIDRequisitante (long numOrdem) {
    return this._delegate.getUserIDRequisitante(numOrdem);
  }

  /**
   * <pre>
   *   short enviarConsolidacaoSCR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarConsolidacaoSCR (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.enviarConsolidacaoSCR(aData);
  }

  /**
   * <pre>
   *   short calcularIndiceBonificacao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short calcularIndiceBonificacao (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.calcularIndiceBonificacao(data);
  }

  /**
   * <pre>
   *   short bloqueioAutomaticoServicoPorSaldo (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoPorSaldo (java.lang.String dataReferencia) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.bloqueioAutomaticoServicoPorSaldo(dataReferencia);
  }

  /**
   * <pre>
   *   short bloqueioAutomaticoServicoIncluindoRE (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoIncluindoRE (java.lang.String dataReferencia) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.bloqueioAutomaticoServicoIncluindoRE(dataReferencia);
  }

  /**
   * <pre>
   *   void enviaPedidoPorEMail (in long long numeroOrdem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaPedidoPorEMail (long numeroOrdem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.enviaPedidoPorEMail(numeroOrdem);
  }

  /**
   * <pre>
   *   void enviaContingenciaSolicitada ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaContingenciaSolicitada () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.enviaContingenciaSolicitada();
  }

  /**
   * <pre>
   *   short gerenciarPromocao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenciarPromocao (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.gerenciarPromocao(data);
  }

  /**
   * <pre>
   *   short promocaoPrepago (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short promocaoPrepago (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.promocaoPrepago(data);
  }

  /**
   * <pre>
   *   short contingenciaDaContingencia (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short contingenciaDaContingencia (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.contingenciaDaContingencia(data);
  }

  /**
   * <pre>
   *   void cadastraAssinantesPromocaoLondrina (in double valorBonus)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void cadastraAssinantesPromocaoLondrina (double valorBonus) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.cadastraAssinantesPromocaoLondrina(valorBonus);
  }

  /**
   * <pre>
   *   void executarPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executarPromocaoLondrina (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.executarPromocaoLondrina(data);
  }

  /**
   * <pre>
   *   void avisaRecargaPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void avisaRecargaPromocaoLondrina (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.avisaRecargaPromocaoLondrina(data);
  }

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
                                     long numLoteFim) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.atualizaNumLotePedido(numOrdem, numItem, numLoteIni, numLoteFim);
  }

  /**
   * <pre>
   *   long long getQtdeCartoes (in long long numOrdem, in long numItem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long getQtdeCartoes (long numOrdem, 
                              int numItem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getQtdeCartoes(numOrdem, numItem);
  }

  /**
   * <pre>
   *   short gerenteFeliz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenteFeliz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.gerenteFeliz();
  }

  /**
   * <pre>
   *   short atualizaLimiteCreditoVarejo ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaLimiteCreditoVarejo () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaLimiteCreditoVarejo();
  }

  /**
   * <pre>
   *   short marretaGPP (in string paramIn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short marretaGPP (java.lang.String paramIn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.marretaGPP(paramIn);
  }

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
                                     java.lang.String UF) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.gerarArquivoCobilling(csp, inicio, fim, UF);
  }

  /**
   * <pre>
   *   void enviaDadosPulaPulaDW (in string aData, in short aPromocao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaDadosPulaPulaDW (java.lang.String aData, 
                                    short aPromocao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.enviaDadosPulaPulaDW(aData, aPromocao);
  }

  /**
   * <pre>
   *   void reiniciaCicloTres ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void reiniciaCicloTres () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.reiniciaCicloTres();
  }

  /**
   * <pre>
   *   void processaBumerangue14Dia (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Dia (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.processaBumerangue14Dia(aData);
  }

  /**
   * <pre>
   *   void processaBumerangue14Mes (in short mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Mes (short mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.processaBumerangue14Mes(mes);
  }

  /**
   * <pre>
   *   short sumarizaAssinantesShutdown (in string dataAnalise)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizaAssinantesShutdown (java.lang.String dataAnalise) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.sumarizaAssinantesShutdown(dataAnalise);
  }

  /**
   * <pre>
   *   void gravaNotificacaoSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void gravaNotificacaoSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.gravaNotificacaoSMS();
  }

  /**
   * <pre>
   *   void aprovisionarAssinantesMMS (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void aprovisionarAssinantesMMS (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.aprovisionarAssinantesMMS(data);
  }

  /**
   * <pre>
   *   short executaConcessaoPulaPula (in string tipoExecucao,
                                  in string referencia, in long promocao);
   * </pre>
   */
  public short executaConcessaoPulaPula (java.lang.String tipoExecucao, 
                                         java.lang.String referencia, 
                                         int promocao) {
    return this._delegate.executaConcessaoPulaPula(tipoExecucao, referencia, promocao);
  }

  /**
   * <pre>
   *   short sumarizaRecargasAssinantes (in string referencia);
   * </pre>
   */
  public short sumarizaRecargasAssinantes (java.lang.String referencia) {
    return this._delegate.sumarizaRecargasAssinantes(referencia);
  }

  /**
   * <pre>
   *   void executaProcessoBatch (in long idProcessoBatch,
                             in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatch parametros)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executaProcessoBatch (int idProcessoBatch, 
                                    java.lang.String[] parametros) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.executaProcessoBatch(idProcessoBatch, parametros);
  }

}
