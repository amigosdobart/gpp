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
public interface consultaOperations {
  /**
   * <pre>
   *   string consultaVoucher (in string voucherId)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaVoucher (java.lang.String voucherId) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinante (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaAssinanteSimples (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteSimples (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

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
                                                    java.lang.String sistemaOrigem) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaAssinanteRecargaXML (in string GPPConsultaPreRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecargaXML (java.lang.String GPPConsultaPreRecarga) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                      com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;

  /**
   * <pre>
   *   string consultaAssinanteRecargaMultiplaXML (in string GPPConsultaPreRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecargaMultiplaXML (java.lang.String GPPConsultaPreRecarga) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                              com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                              com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException;

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
                                           java.lang.String finalPeriodo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                 com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

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
                                                   java.lang.String finalPeriodo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                         com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

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
                                                        java.lang.String finalPeriodo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                              com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   string consultaSaldoPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPula (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPula (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaPulaPulaNoMes (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPulaNoMes (java.lang.String msisdn, 
                                                 java.lang.String mes) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaSaldoPulaPulaNoMes (in string msisdn, in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPulaNoMes (java.lang.String msisdn, 
                                                      int mes) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaEstornoPulaPula (in string msisdn, in string inicio,
                                  in string fim);
   * </pre>
   */
  public java.lang.String consultaEstornoPulaPula (java.lang.String msisdn, 
                                                   java.lang.String inicio, 
                                                   java.lang.String fim);

  /**
   * <pre>
   *   string consultaAparelhoAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelhoAssinante (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (in long numeroJob)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (int numeroJob) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

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
                                                    java.lang.String finalPeriodo) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                          com.brt.gpp.comum.gppExceptions.GPPTecnomenException;

  /**
   * <pre>
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (in string msisdn,
                                                                                              in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (java.lang.String msisdn, 
                                                                                                     int mes) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaRecargaAntifraude (in string aXML)
    raises (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException,
            com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaRecargaAntifraude (java.lang.String aXML) throws com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException, 
                                                                                   com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaAparelho (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelho (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string consultaAssinanteTFPP (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteTFPP (java.lang.String msisdn) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   double consultarCreditoPulaPula (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public double consultarCreditoPulaPula (java.lang.String msisdn, 
                                          java.lang.String mes) throws com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

  /**
   * <pre>
   *   string publicarBS (in string numeroBS, in string numeroIP,
                     in string numeroAssinante, in string matriculaOperador);
   * </pre>
   */
  public java.lang.String publicarBS (java.lang.String numeroBS, 
                                      java.lang.String numeroIP, 
                                      java.lang.String numeroAssinante, 
                                      java.lang.String matriculaOperador);

  /**
   * <pre>
   *   string consultarStatusBS (in string xmlConsulta);
   * </pre>
   */
  public java.lang.String consultarStatusBS (java.lang.String xmlConsulta);

}
