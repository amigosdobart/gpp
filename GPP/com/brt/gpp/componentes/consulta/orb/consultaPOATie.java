
package com.brt.gpp.componentes.consulta.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface consulta {
  ...
};
 * </pre>
 */
public class consultaPOATie extends consultaPOA {
  private com.brt.gpp.componentes.consulta.orb.consultaOperations _delegate;
  private org.omg.PortableServer.POA _poa;

  public consultaPOATie (final com.brt.gpp.componentes.consulta.orb.consultaOperations _delegate) {
    this._delegate = _delegate;
  }

  public consultaPOATie (final com.brt.gpp.componentes.consulta.orb.consultaOperations _delegate, 
                              final org.omg.PortableServer.POA _poa) {
    this._delegate = _delegate;
    this._poa = _poa;
  }

  public com.brt.gpp.componentes.consulta.orb.consultaOperations _delegate () {
    return this._delegate;
  }

  public void _delegate (final com.brt.gpp.componentes.consulta.orb.consultaOperations delegate) {
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
   *   string consultaVoucher (in string voucherId)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaVoucher (java.lang.String voucherId) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaVoucher(voucherId);
  }

  /**
   * <pre>
   *   string consultaAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinante (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAssinante(msisdn);
  }

  /**
   * <pre>
   *   string consultaAssinanteSimples (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteSimples (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAssinanteSimples(msisdn);
  }

  /**
   * <pre>
   *   string consultaAssinanteRecarga (in string msisdn,
                                   in double valorTotalRecarga, in string cpf,
                                   in short categoria,
                                   in string hashCartaoCredito,
                                   in string sistemaOrigem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecarga (java.lang.String msisdn, 
                                                    double valorTotalRecarga, 
                                                    java.lang.String cpf, 
                                                    short categoria, 
                                                    java.lang.String hashCartaoCredito, 
                                                    java.lang.String sistemaOrigem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAssinanteRecarga(msisdn, valorTotalRecarga, cpf, 
                                                   categoria, hashCartaoCredito, 
                                                   sistemaOrigem);
  }

  /**
   * <pre>
   *   string consultaAssinanteRecargaXML (in string GPPConsultaPreRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecargaXML (java.lang.String GPPConsultaPreRecarga) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                       com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                       com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException {
    return this._delegate.consultaAssinanteRecargaXML(GPPConsultaPreRecarga);
  }

  /**
   * <pre>
   *   string consultaAssinanteRecargaMultiplaXML (in string GPPConsultaPreRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecargaMultiplaXML (java.lang.String GPPConsultaPreRecarga) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                               com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                               com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException {
    return this._delegate.consultaAssinanteRecargaMultiplaXML(GPPConsultaPreRecarga);
  }

  /**
   * <pre>
   *   string consultaExtrato (in string msisdn, in string inicioPeriodo,
                          in string finalPeriodo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaExtrato (java.lang.String msisdn, 
                                           java.lang.String inicioPeriodo, 
                                           java.lang.String finalPeriodo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                  com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.consultaExtrato(msisdn, inicioPeriodo, finalPeriodo);
  }

  /**
   * <pre>
   *   string consultaExtratoPulaPula (in string msisdn, in string inicioPeriodo,
                                  in string finalPeriodo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaExtratoPulaPula (java.lang.String msisdn, 
                                                   java.lang.String inicioPeriodo, 
                                                   java.lang.String finalPeriodo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                          com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.consultaExtratoPulaPula(msisdn, inicioPeriodo, finalPeriodo);
  }

  /**
   * <pre>
   *   string consultaExtratoPulaPulaCheio (in string msisdn,
                                       in string inicioPeriodo,
                                       in string finalPeriodo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaExtratoPulaPulaCheio (java.lang.String msisdn, 
                                                        java.lang.String inicioPeriodo, 
                                                        java.lang.String finalPeriodo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                               com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.consultaExtratoPulaPulaCheio(msisdn, inicioPeriodo, finalPeriodo);
  }

  /**
   * <pre>
   *   string consultaSaldoPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPula (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaSaldoPulaPula(msisdn);
  }

  /**
   * <pre>
   *   string consultaPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPula (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaPulaPula(msisdn);
  }

  /**
   * <pre>
   *   string consultaPulaPulaNoMes (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPulaNoMes (java.lang.String msisdn, 
                                                 java.lang.String mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaPulaPulaNoMes(msisdn, mes);
  }

  /**
   * <pre>
   *   string consultaSaldoPulaPulaNoMes (in string msisdn, in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPulaNoMes (java.lang.String msisdn, 
                                                      int mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaSaldoPulaPulaNoMes(msisdn, mes);
  }

  /**
   * <pre>
   *   string consultaEstornoPulaPula (in string msisdn, in string inicio,
                                  in string fim);
   * </pre>
   */
  public java.lang.String consultaEstornoPulaPula (java.lang.String msisdn, 
                                                   java.lang.String inicio, 
                                                   java.lang.String fim) {
    return this._delegate.consultaEstornoPulaPula(msisdn, inicio, fim);
  }

  /**
   * <pre>
   *   string consultaAparelhoAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelhoAssinante (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAparelhoAssinante(msisdn);
  }

  /**
   * <pre>
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (in long numeroJob)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (int numeroJob) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaJobTecnomen(numeroJob);
  }

  /**
   * <pre>
   *   string consultaExtratoBoomerang (in string msisdn, in string inicioPeriodo,
                                   in string finalPeriodo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaExtratoBoomerang (java.lang.String msisdn, 
                                                    java.lang.String inicioPeriodo, 
                                                    java.lang.String finalPeriodo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                           com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.consultaExtratoBoomerang(msisdn, inicioPeriodo, finalPeriodo);
  }

  /**
   * <pre>
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (in string msisdn,
                                                                                              in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (java.lang.String msisdn, 
                                                                                                     int mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaSaldoBoomerang(msisdn, mes);
  }

  /**
   * <pre>
   *   string consultaRecargaAntifraude (in string aXML)
    raises (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException,
            com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaRecargaAntifraude (java.lang.String aXML) throws  com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException, 
                                                                                    com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaRecargaAntifraude(aXML);
  }

  /**
   * <pre>
   *   string consultaAparelho (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelho (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAparelho(msisdn);
  }

  /**
   * <pre>
   *   string consultaAssinanteTFPP (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteTFPP (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultaAssinanteTFPP(msisdn);
  }

  /**
   * <pre>
   *   double consultarCreditoPulaPula (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public double consultarCreditoPulaPula (java.lang.String msisdn, 
                                          java.lang.String mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    return this._delegate.consultarCreditoPulaPula(msisdn, mes);
  }

  /**
   * <pre>
   *   string publicarBS (in string numeroBS, in string numeroIP,
                     in string numeroAssinante, in string matriculaOperador);
   * </pre>
   */
  public java.lang.String publicarBS (java.lang.String numeroBS, 
                                      java.lang.String numeroIP, 
                                      java.lang.String numeroAssinante, 
                                      java.lang.String matriculaOperador) {
    return this._delegate.publicarBS(numeroBS, numeroIP, numeroAssinante, matriculaOperador);
  }

  /**
   * <pre>
   *   string consultarStatusBS (in string xmlConsulta);
   * </pre>
   */
  public java.lang.String consultarStatusBS (java.lang.String xmlConsulta) {
    return this._delegate.consultarStatusBS(xmlConsulta);
  }

}
