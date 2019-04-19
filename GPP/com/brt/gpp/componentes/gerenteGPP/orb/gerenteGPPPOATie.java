
package com.brt.gpp.componentes.gerenteGPP.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "GerenteGPP.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::gerenteGPP::orb::gerenteGPP
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface gerenteGPP {
  ...
};
 * </pre>
 */
public class gerenteGPPPOATie extends gerenteGPPPOA {
  private com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _delegate;
  private org.omg.PortableServer.POA _poa;

  public gerenteGPPPOATie (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _delegate) {
    this._delegate = _delegate;
  }

  public gerenteGPPPOATie (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _delegate, 
                              final org.omg.PortableServer.POA _poa) {
    this._delegate = _delegate;
    this._poa = _poa;
  }

  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _delegate () {
    return this._delegate;
  }

  public void _delegate (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations delegate) {
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
   *   short getNumerodeConexoes (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumerodeConexoes (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getNumerodeConexoes(tipoConexao);
  }

  /**
   * <pre>
   *   boolean criaConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean criaConexao (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.criaConexao(tipoConexao);
  }

  /**
   * <pre>
   *   boolean removeConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeConexao (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.removeConexao(tipoConexao);
  }

  /**
   * <pre>
   *   string exibeListaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaPlanoPreco () {
    return this._delegate.exibeListaPlanoPreco();
  }

  /**
   * <pre>
   *   boolean atualizaListaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPlanoPreco () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaPlanoPreco();
  }

  /**
   * <pre>
   *   string exibeListaStatusAssinante ();
   * </pre>
   */
  public java.lang.String exibeListaStatusAssinante () {
    return this._delegate.exibeListaStatusAssinante();
  }

  /**
   * <pre>
   *   boolean atualizaListaStatusAssinante ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusAssinante () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaStatusAssinante();
  }

  /**
   * <pre>
   *   string exibeListaStatusServico ();
   * </pre>
   */
  public java.lang.String exibeListaStatusServico () {
    return this._delegate.exibeListaStatusServico();
  }

  /**
   * <pre>
   *   boolean atualizaListaStatusServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusServico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaStatusServico();
  }

  /**
   * <pre>
   *   string exibeListaSistemaOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaSistemaOrigem () {
    return this._delegate.exibeListaSistemaOrigem();
  }

  /**
   * <pre>
   *   boolean atualizaListaSistemaOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaSistemaOrigem () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaSistemaOrigem();
  }

  /**
   * <pre>
   *   string exibeListaTarifaTrocaMSISDN ();
   * </pre>
   */
  public java.lang.String exibeListaTarifaTrocaMSISDN () {
    return this._delegate.exibeListaTarifaTrocaMSISDN();
  }

  /**
   * <pre>
   *   boolean atualizaListaTarifaTrocaMSISDN ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaTarifaTrocaMSISDN () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaTarifaTrocaMSISDN();
  }

  /**
   * <pre>
   *   string exibeListaValoresRecarga ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecarga () {
    return this._delegate.exibeListaValoresRecarga();
  }

  /**
   * <pre>
   *   boolean atualizaListaValoresRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecarga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaValoresRecarga();
  }

  /**
   * <pre>
   *   string exibeListaValoresRecargaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecargaPlanoPreco () {
    return this._delegate.exibeListaValoresRecargaPlanoPreco();
  }

  /**
   * <pre>
   *   boolean atualizaListaValoresRecargaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecargaPlanoPreco () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaValoresRecargaPlanoPreco();
  }

  /**
   * <pre>
   *   string exibeListaBonusPulaPula ();
   * </pre>
   */
  public java.lang.String exibeListaBonusPulaPula () {
    return this._delegate.exibeListaBonusPulaPula();
  }

  /**
   * <pre>
   *   boolean atualizaListaBonusPulaPula ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaBonusPulaPula () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaBonusPulaPula();
  }

  /**
   * <pre>
   *   string exibeListaPromocao ();
   * </pre>
   */
  public java.lang.String exibeListaPromocao () {
    return this._delegate.exibeListaPromocao();
  }

  /**
   * <pre>
   *   boolean atualizaListaPromocao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocao () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaPromocao();
  }

  /**
   * <pre>
   *   boolean atualizaListaPromocaoDiaExecucao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocaoDiaExecucao () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaPromocaoDiaExecucao();
  }

  /**
   * <pre>
   *   string exibeListaRecOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaRecOrigem () {
    return this._delegate.exibeListaRecOrigem();
  }

  /**
   * <pre>
   *   boolean atualizaListaRecOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaRecOrigem () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaRecOrigem();
  }

  /**
   * <pre>
   *   string exibeListaConfiguracaoGPP ();
   * </pre>
   */
  public java.lang.String exibeListaConfiguracaoGPP () {
    return this._delegate.exibeListaConfiguracaoGPP();
  }

  /**
   * <pre>
   *   boolean atualizaListaConfiguracaoGPP ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaConfiguracaoGPP () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaConfiguracaoGPP();
  }

  /**
   * <pre>
   *   string exibeListaMotivosBloqueio ();
   * </pre>
   */
  public java.lang.String exibeListaMotivosBloqueio () {
    return this._delegate.exibeListaMotivosBloqueio();
  }

  /**
   * <pre>
   *   boolean atualizaListaMotivosBloqueio ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaMotivosBloqueio () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaMotivosBloqueio();
  }

  /**
   * <pre>
   *   boolean atualizaListaModulacaoPlano ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaModulacaoPlano () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaModulacaoPlano();
  }

  /**
   * <pre>
   *   boolean atualizaListaAssinantesNaoBonificaveis ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaAssinantesNaoBonificaveis () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaAssinantesNaoBonificaveis();
  }

  /**
   * <pre>
   *   boolean atualizaListaFeriados ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaFeriados () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.atualizaListaFeriados();
  }

  /**
   * <pre>
   *   boolean processaSMS (in boolean deveProcessar);
   * </pre>
   */
  public boolean processaSMS (boolean deveProcessar) {
    return this._delegate.processaSMS(deveProcessar);
  }

  /**
   * <pre>
   *   boolean escreveDebug (in boolean escreveDebug);
   * </pre>
   */
  public boolean escreveDebug (boolean escreveDebug) {
    return this._delegate.escreveDebug(escreveDebug);
  }

  /**
   * <pre>
   *   string ping ();
   * </pre>
   */
  public java.lang.String ping () {
    return this._delegate.ping();
  }

  /**
   * <pre>
   *   void finalizaGPP ();
   * </pre>
   */
  public void finalizaGPP () {
    this._delegate.finalizaGPP();
  }

  /**
   * <pre>
   *   boolean getStatusProdutorSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusProdutorSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getStatusProdutorSMS();
  }

  /**
   * <pre>
   *   boolean getStatusEscreveDebug ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusEscreveDebug () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getStatusEscreveDebug();
  }

  /**
   * <pre>
   *   string getHistProcessosBatch (in short aIdProcBatch, in string aDatIni,
                                in string aDatFim, in string aIdtStatus)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String getHistProcessosBatch (short aIdProcBatch, 
                                                 java.lang.String aDatIni, 
                                                 java.lang.String aDatFim, 
                                                 java.lang.String aIdtStatus) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getHistProcessosBatch(aIdProcBatch, aDatIni, aDatFim, 
                                                aIdtStatus);
  }

  /**
   * <pre>
   *   string consultaProcessosBatch ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaProcessosBatch () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaProcessosBatch();
  }

  /**
   * <pre>
   *   short getNumeroConexoesDisponiveis (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumeroConexoesDisponiveis (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getNumeroConexoesDisponiveis(tipoConexao);
  }

  /**
   * <pre>
   *   string exibirNumeroStatementsPorConexao ();
   * </pre>
   */
  public java.lang.String exibirNumeroStatementsPorConexao () {
    return this._delegate.exibirNumeroStatementsPorConexao();
  }

  /**
   * <pre>
   *   com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexao getListaProcessosComConexoesEmUso (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] getListaProcessosComConexoesEmUso (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.getListaProcessosComConexoesEmUso(tipoConexao);
  }

  /**
   * <pre>
   *   boolean adicionaThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean adicionaThreadEnvioSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.adicionaThreadEnvioSMS();
  }

  /**
   * <pre>
   *   boolean removeThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeThreadEnvioSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.removeThreadEnvioSMS();
  }

  /**
   * <pre>
   *   void finalizaPoolEnvioSMS ();
   * </pre>
   */
  public void finalizaPoolEnvioSMS () {
    this._delegate.finalizaPoolEnvioSMS();
  }

  /**
   * <pre>
   *   void inicializaPoolEnvioSMS ();
   * </pre>
   */
  public void inicializaPoolEnvioSMS () {
    this._delegate.inicializaPoolEnvioSMS();
  }

  /**
   * <pre>
   *   short getNumeroThreadsEnvioSMS ();
   * </pre>
   */
  public short getNumeroThreadsEnvioSMS () {
    return this._delegate.getNumeroThreadsEnvioSMS();
  }

  /**
   * <pre>
   *   short getNumThreadsImpCDRDadosVoz ();
   * </pre>
   */
  public short getNumThreadsImpCDRDadosVoz () {
    return this._delegate.getNumThreadsImpCDRDadosVoz();
  }

  /**
   * <pre>
   *   short getNumThreadsImpCDREvtRec ();
   * </pre>
   */
  public short getNumThreadsImpCDREvtRec () {
    return this._delegate.getNumThreadsImpCDREvtRec();
  }

  /**
   * <pre>
   *   short getNumArqPendentesDadosVoz ();
   * </pre>
   */
  public short getNumArqPendentesDadosVoz () {
    return this._delegate.getNumArqPendentesDadosVoz();
  }

  /**
   * <pre>
   *   short getNumArqPendentesEvtRec ();
   * </pre>
   */
  public short getNumArqPendentesEvtRec () {
    return this._delegate.getNumArqPendentesEvtRec();
  }

  /**
   * <pre>
   *   void removeThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsDadosVoz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.removeThreadsDadosVoz();
  }

  /**
   * <pre>
   *   void removeThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsEvtRec () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.removeThreadsEvtRec();
  }

  /**
   * <pre>
   *   void inicializaThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsDadosVoz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.inicializaThreadsDadosVoz();
  }

  /**
   * <pre>
   *   void inicializaThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsEvtRec () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.inicializaThreadsEvtRec();
  }

  /**
   * <pre>
   *   void reiniciaCacheFF ();
   * </pre>
   */
  public void reiniciaCacheFF () {
    this._delegate.reiniciaCacheFF();
  }

  /**
   * <pre>
   *   short atualizaMapeamentos (in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamentos (boolean limpar) {
    return this._delegate.atualizaMapeamentos(limpar);
  }

  /**
   * <pre>
   *   short atualizaMapeamento (in string nome, in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamento (java.lang.String nome, 
                                   boolean limpar) {
    return this._delegate.atualizaMapeamento(nome, limpar);
  }

  /**
   * <pre>
   *   string exibeMapeamento (in string nome);
   * </pre>
   */
  public java.lang.String exibeMapeamento (java.lang.String nome) {
    return this._delegate.exibeMapeamento(nome);
  }

  /**
   * <pre>
   *   void liberarConexoesEmUso (in long long idProcesso);
   * </pre>
   */
  public void liberarConexoesEmUso (long idProcesso) {
    this._delegate.liberarConexoesEmUso(idProcesso);
  }

}
