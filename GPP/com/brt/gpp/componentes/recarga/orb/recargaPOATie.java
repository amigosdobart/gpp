
package com.brt.gpp.componentes.recarga.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Recarga.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::recarga::orb::recarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/recarga/orb/recarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface recarga {
  ...
};
 * </pre>
 */
public class recargaPOATie extends recargaPOA {
  private com.brt.gpp.componentes.recarga.orb.recargaOperations _delegate;
  private org.omg.PortableServer.POA _poa;

  public recargaPOATie (final com.brt.gpp.componentes.recarga.orb.recargaOperations _delegate) {
    this._delegate = _delegate;
  }

  public recargaPOATie (final com.brt.gpp.componentes.recarga.orb.recargaOperations _delegate, 
                              final org.omg.PortableServer.POA _poa) {
    this._delegate = _delegate;
    this._poa = _poa;
  }

  public com.brt.gpp.componentes.recarga.orb.recargaOperations _delegate () {
    return this._delegate;
  }

  public void _delegate (final com.brt.gpp.componentes.recarga.orb.recargaOperations delegate) {
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
   *   string executaRecargaXML (in string GPPRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String executaRecargaXML (java.lang.String GPPRecarga) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                  com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                  com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException, 
                                                                                  com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaRecargaXML(GPPRecarga);
  }

  /**
   * <pre>
   *   short executaAjusteXML (in string GPPRecargaAjuste)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaAjusteXML (java.lang.String GPPRecargaAjuste) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                            com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException, 
                                                                            com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaAjusteXML(GPPRecargaAjuste);
  }

  /**
   * <pre>
   *   short executaRecargaBanco (in string MSISDN, in string tipoTransacao,
                             in string identificacaoRecarga,
                             in string nsuInstituicao, in string codLoja,
                             in string tipoCredito, in double valor,
                             in string dataHora, in string dataHoraBanco,
                             in string dataContabil, in string terminal,
                             in string tipoTerminal, in string sistemaOrigem,
                             in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaRecargaBanco (java.lang.String MSISDN, 
                                    java.lang.String tipoTransacao, 
                                    java.lang.String identificacaoRecarga, 
                                    java.lang.String nsuInstituicao, 
                                    java.lang.String codLoja, 
                                    java.lang.String tipoCredito, 
                                    double valor, 
                                    java.lang.String dataHora, 
                                    java.lang.String dataHoraBanco, 
                                    java.lang.String dataContabil, 
                                    java.lang.String terminal, 
                                    java.lang.String tipoTerminal, 
                                    java.lang.String sistemaOrigem, 
                                    java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaRecargaBanco(MSISDN, tipoTransacao, identificacaoRecarga, 
                                              nsuInstituicao, codLoja, tipoCredito, 
                                              valor, dataHora, dataHoraBanco, dataContabil, 
                                              terminal, tipoTerminal, sistemaOrigem, 
                                              operador);
  }

  /**
   * <pre>
   *   short executaRecarga (in string MSISDN, in string tipoTransacao,
                        in string identificacaoRecarga, in string tipoCredito,
                        in double valor, in string dataHora,
                        in string sistemaOrigem, in string operador,
                        in string nsuInstituicao, in string hash_cc,
                        in string cpf)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaRecarga (java.lang.String MSISDN, 
                               java.lang.String tipoTransacao, 
                               java.lang.String identificacaoRecarga, 
                               java.lang.String tipoCredito, 
                               double valor, 
                               java.lang.String dataHora, 
                               java.lang.String sistemaOrigem, 
                               java.lang.String operador, 
                               java.lang.String nsuInstituicao, 
                               java.lang.String hash_cc, 
                               java.lang.String cpf) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                             com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                             com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaRecarga(MSISDN, tipoTransacao, identificacaoRecarga, 
                                         tipoCredito, valor, dataHora, sistemaOrigem, 
                                         operador, nsuInstituicao, hash_cc, cpf);
  }

  /**
   * <pre>
   *   short executaAjuste (in string MSISDN, in string tipoTransacao,
                       in string tipoCredito, in double valor, in string tipo,
                       in string dataHora, in string sistemaOrigem,
                       in string operador, in string data_expiracao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaAjuste (java.lang.String MSISDN, 
                              java.lang.String tipoTransacao, 
                              java.lang.String tipoCredito, 
                              double valor, 
                              java.lang.String tipo, 
                              java.lang.String dataHora, 
                              java.lang.String sistemaOrigem, 
                              java.lang.String operador, 
                              java.lang.String data_expiracao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                       com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaAjuste(MSISDN, tipoTransacao, tipoCredito, valor, 
                                        tipo, dataHora, sistemaOrigem, operador, 
                                        data_expiracao);
  }

  /**
   * <pre>
   *   short executaAjusteDescrito (in string MSISDN, in string tipoTransacao,
                               in string tipoCredito, in double valor,
                               in string tipo, in string dataHora,
                               in string sistemaOrigem, in string operador,
                               in string data_expiracao, in string descricao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaAjusteDescrito (java.lang.String MSISDN, 
                                      java.lang.String tipoTransacao, 
                                      java.lang.String tipoCredito, 
                                      double valor, 
                                      java.lang.String tipo, 
                                      java.lang.String dataHora, 
                                      java.lang.String sistemaOrigem, 
                                      java.lang.String operador, 
                                      java.lang.String data_expiracao, 
                                      java.lang.String descricao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                          com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                          com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaAjusteDescrito(MSISDN, tipoTransacao, tipoCredito, 
                                                valor, tipo, dataHora, sistemaOrigem, 
                                                operador, data_expiracao, descricao);
  }

  /**
   * <pre>
   *   short executaAjusteMultiplosSaldos (in string MSISDN,
                                      in string tipoTransacao,
                                      in double ajustePrincipal,
                                      in double ajustePeriodico,
                                      in double ajusteBonus,
                                      in double ajusteSms,
                                      in double ajusteGprs,
                                      in string expPrincipal,
                                      in string expPeriodico,
                                      in string expBonus, in string expSms,
                                      in string expGprs, in string tipo,
                                      in string dataHora,
                                      in string sistemaOrigem,
                                      in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short executaAjusteMultiplosSaldos (java.lang.String MSISDN, 
                                             java.lang.String tipoTransacao, 
                                             double ajustePrincipal, 
                                             double ajustePeriodico, 
                                             double ajusteBonus, 
                                             double ajusteSms, 
                                             double ajusteGprs, 
                                             java.lang.String expPrincipal, 
                                             java.lang.String expPeriodico, 
                                             java.lang.String expBonus, 
                                             java.lang.String expSms, 
                                             java.lang.String expGprs, 
                                             java.lang.String tipo, 
                                             java.lang.String dataHora, 
                                             java.lang.String sistemaOrigem, 
                                             java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.executaAjusteMultiplosSaldos(MSISDN, tipoTransacao, ajustePrincipal, 
                                                       ajustePeriodico, ajusteBonus, 
                                                       ajusteSms, ajusteGprs, expPrincipal, 
                                                       expPeriodico, expBonus, 
                                                       expSms, expGprs, tipo, dataHora, 
                                                       sistemaOrigem, operador);
  }

  /**
   * <pre>
   *   short podeRecarregar (in string MSISDN, in double valorCreditos)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short podeRecarregar (java.lang.String MSISDN, 
                               double valorCreditos) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                             com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                             com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.podeRecarregar(MSISDN, valorCreditos);
  }

  /**
   * <pre>
   *   short podeRecarregarVarejo (in string MSISDN, in double valorCreditos,
                              in string tipoTransacao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short podeRecarregarVarejo (java.lang.String MSISDN, 
                                     double valorCreditos, 
                                     java.lang.String tipoTransacao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                             com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.podeRecarregarVarejo(MSISDN, valorCreditos, tipoTransacao);
  }

  /**
   * <pre>
   *   short alteraStatusVoucher (in string numeroVoucher, in double statusVoucher,
                             in string comentario)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short alteraStatusVoucher (java.lang.String numeroVoucher, 
                                    double statusVoucher, 
                                    java.lang.String comentario) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                         com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                         com.brt.gpp.comum.gppExceptions.GPPTecnomenException {
    return this._delegate.alteraStatusVoucher(numeroVoucher, statusVoucher, comentario);
  }

  /**
   * <pre>
   *   void resubmeterPedidoVoucher (in long long numeroPedido)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void resubmeterPedidoVoucher (long numeroPedido) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {
    this._delegate.resubmeterPedidoVoucher(numeroPedido);
  }

  /**
   * <pre>
   *   short consultaPreRecarga (in string MSISDN, in double valorCreditos,
                            in string tipoTransacao, in string origem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException);
   * </pre>
   */
  public short consultaPreRecarga (java.lang.String MSISDN, 
                                   double valorCreditos, 
                                   java.lang.String tipoTransacao, 
                                   java.lang.String origem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                    com.brt.gpp.comum.gppExceptions.GPPTecnomenException, 
                                                                    com.brt.gpp.comum.gppExceptions.GPPCorbaException {
    return this._delegate.consultaPreRecarga(MSISDN, valorCreditos, tipoTransacao, 
                                             origem);
  }

  /**
   * <pre>
   *   short aprovarLoteRecarga (in string lote, in string usuario);
   * </pre>
   */
  public short aprovarLoteRecarga (java.lang.String lote, 
                                   java.lang.String usuario) {
    return this._delegate.aprovarLoteRecarga(lote, usuario);
  }

  /**
   * <pre>
   *   short rejeitarLoteRecarga (in string lote, in string usuario);
   * </pre>
   */
  public short rejeitarLoteRecarga (java.lang.String lote, 
                                    java.lang.String usuario) {
    return this._delegate.rejeitarLoteRecarga(lote, usuario);
  }

}
