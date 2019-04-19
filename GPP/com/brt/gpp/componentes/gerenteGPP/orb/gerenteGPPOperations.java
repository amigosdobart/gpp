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
public interface gerenteGPPOperations {
  /**
   * <pre>
   *   short getNumerodeConexoes (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumerodeConexoes (short tipoConexao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean criaConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean criaConexao (short tipoConexao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean removeConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeConexao (short tipoConexao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaPlanoPreco ();

  /**
   * <pre>
   *   boolean atualizaListaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPlanoPreco () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaStatusAssinante ();
   * </pre>
   */
  public java.lang.String exibeListaStatusAssinante ();

  /**
   * <pre>
   *   boolean atualizaListaStatusAssinante ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusAssinante () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaStatusServico ();
   * </pre>
   */
  public java.lang.String exibeListaStatusServico ();

  /**
   * <pre>
   *   boolean atualizaListaStatusServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusServico () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaSistemaOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaSistemaOrigem ();

  /**
   * <pre>
   *   boolean atualizaListaSistemaOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaSistemaOrigem () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaTarifaTrocaMSISDN ();
   * </pre>
   */
  public java.lang.String exibeListaTarifaTrocaMSISDN ();

  /**
   * <pre>
   *   boolean atualizaListaTarifaTrocaMSISDN ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaTarifaTrocaMSISDN () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaValoresRecarga ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecarga ();

  /**
   * <pre>
   *   boolean atualizaListaValoresRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecarga () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaValoresRecargaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecargaPlanoPreco ();

  /**
   * <pre>
   *   boolean atualizaListaValoresRecargaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecargaPlanoPreco () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaBonusPulaPula ();
   * </pre>
   */
  public java.lang.String exibeListaBonusPulaPula ();

  /**
   * <pre>
   *   boolean atualizaListaBonusPulaPula ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaBonusPulaPula () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaPromocao ();
   * </pre>
   */
  public java.lang.String exibeListaPromocao ();

  /**
   * <pre>
   *   boolean atualizaListaPromocao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocao () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean atualizaListaPromocaoDiaExecucao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocaoDiaExecucao () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaRecOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaRecOrigem ();

  /**
   * <pre>
   *   boolean atualizaListaRecOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaRecOrigem () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaConfiguracaoGPP ();
   * </pre>
   */
  public java.lang.String exibeListaConfiguracaoGPP ();

  /**
   * <pre>
   *   boolean atualizaListaConfiguracaoGPP ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaConfiguracaoGPP () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibeListaMotivosBloqueio ();
   * </pre>
   */
  public java.lang.String exibeListaMotivosBloqueio ();

  /**
   * <pre>
   *   boolean atualizaListaMotivosBloqueio ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaMotivosBloqueio () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean atualizaListaModulacaoPlano ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaModulacaoPlano () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean atualizaListaAssinantesNaoBonificaveis ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaAssinantesNaoBonificaveis () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean atualizaListaFeriados ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaFeriados () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean processaSMS (in boolean deveProcessar);
   * </pre>
   */
  public boolean processaSMS (boolean deveProcessar);

  /**
   * <pre>
   *   boolean escreveDebug (in boolean escreveDebug);
   * </pre>
   */
  public boolean escreveDebug (boolean escreveDebug);

  /**
   * <pre>
   *   string ping ();
   * </pre>
   */
  public java.lang.String ping ();

  /**
   * <pre>
   *   void finalizaGPP ();
   * </pre>
   */
  public void finalizaGPP ();

  /**
   * <pre>
   *   boolean getStatusProdutorSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusProdutorSMS () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean getStatusEscreveDebug ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusEscreveDebug () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

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
                                                 java.lang.String aIdtStatus) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaProcessosBatch ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaProcessosBatch () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   short getNumeroConexoesDisponiveis (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumeroConexoesDisponiveis (short tipoConexao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string exibirNumeroStatementsPorConexao ();
   * </pre>
   */
  public java.lang.String exibirNumeroStatementsPorConexao ();

  /**
   * <pre>
   *   com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexao getListaProcessosComConexoesEmUso (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] getListaProcessosComConexoesEmUso (short tipoConexao) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean adicionaThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean adicionaThreadEnvioSMS () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   boolean removeThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeThreadEnvioSMS () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void finalizaPoolEnvioSMS ();
   * </pre>
   */
  public void finalizaPoolEnvioSMS ();

  /**
   * <pre>
   *   void inicializaPoolEnvioSMS ();
   * </pre>
   */
  public void inicializaPoolEnvioSMS ();

  /**
   * <pre>
   *   short getNumeroThreadsEnvioSMS ();
   * </pre>
   */
  public short getNumeroThreadsEnvioSMS ();

  /**
   * <pre>
   *   short getNumThreadsImpCDRDadosVoz ();
   * </pre>
   */
  public short getNumThreadsImpCDRDadosVoz ();

  /**
   * <pre>
   *   short getNumThreadsImpCDREvtRec ();
   * </pre>
   */
  public short getNumThreadsImpCDREvtRec ();

  /**
   * <pre>
   *   short getNumArqPendentesDadosVoz ();
   * </pre>
   */
  public short getNumArqPendentesDadosVoz ();

  /**
   * <pre>
   *   short getNumArqPendentesEvtRec ();
   * </pre>
   */
  public short getNumArqPendentesEvtRec ();

  /**
   * <pre>
   *   void removeThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsDadosVoz () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void removeThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsEvtRec () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void inicializaThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsDadosVoz () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void inicializaThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsEvtRec () throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   void reiniciaCacheFF ();
   * </pre>
   */
  public void reiniciaCacheFF ();

  /**
   * <pre>
   *   short atualizaMapeamentos (in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamentos (boolean limpar);

  /**
   * <pre>
   *   short atualizaMapeamento (in string nome, in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamento (java.lang.String nome, 
                                   boolean limpar);

  /**
   * <pre>
   *   string exibeMapeamento (in string nome);
   * </pre>
   */
  public java.lang.String exibeMapeamento (java.lang.String nome);

  /**
   * <pre>
   *   void liberarConexoesEmUso (in long long idProcesso);
   * </pre>
   */
  public void liberarConexoesEmUso (long idProcesso);

}
