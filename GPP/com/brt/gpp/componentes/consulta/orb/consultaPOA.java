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
public abstract class consultaPOA extends org.omg.PortableServer.Servant implements 
org.omg.CORBA.portable.InvokeHandler, com.brt.gpp.componentes.consulta.orb.consultaOperations {

  public com.brt.gpp.componentes.consulta.orb.consulta _this () {
   return com.brt.gpp.componentes.consulta.orb.consultaHelper.narrow(super._this_object());
  }

  public com.brt.gpp.componentes.consulta.orb.consulta _this (org.omg.CORBA.ORB orb) {
    return com.brt.gpp.componentes.consulta.orb.consultaHelper.narrow(super._this_object(orb));
  }

  public java.lang.String[] _all_interfaces (final org.omg.PortableServer.POA poa, final byte[] objectId) {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/consulta/orb/consulta:1.0"
  };

  private static java.util.Dictionary _methods = new java.util.Hashtable();

  static {
    _methods.put("consultaVoucher", new int[] { 0, 0 });
    _methods.put("consultaAssinante", new int[] { 0, 1 });
    _methods.put("consultaAssinanteSimples", new int[] { 0, 2 });
    _methods.put("consultaAssinanteRecarga", new int[] { 0, 3 });
    _methods.put("consultaAssinanteRecargaXML", new int[] { 0, 4 });
    _methods.put("consultaAssinanteRecargaMultiplaXML", new int[] { 0, 5 });
    _methods.put("consultaExtrato", new int[] { 0, 6 });
    _methods.put("consultaExtratoPulaPula", new int[] { 0, 7 });
    _methods.put("consultaExtratoPulaPulaCheio", new int[] { 0, 8 });
    _methods.put("consultaSaldoPulaPula", new int[] { 0, 9 });
    _methods.put("consultaPulaPula", new int[] { 0, 10 });
    _methods.put("consultaPulaPulaNoMes", new int[] { 0, 11 });
    _methods.put("consultaSaldoPulaPulaNoMes", new int[] { 0, 12 });
    _methods.put("consultaEstornoPulaPula", new int[] { 0, 13 });
    _methods.put("consultaAparelhoAssinante", new int[] { 0, 14 });
    _methods.put("consultaJobTecnomen", new int[] { 0, 15 });
    _methods.put("consultaExtratoBoomerang", new int[] { 0, 16 });
    _methods.put("consultaSaldoBoomerang", new int[] { 0, 17 });
    _methods.put("consultaRecargaAntifraude", new int[] { 0, 18 });
    _methods.put("consultaAparelho", new int[] { 0, 19 });
    _methods.put("consultaAssinanteTFPP", new int[] { 0, 20 });
    _methods.put("consultarCreditoPulaPula", new int[] { 0, 21 });
    _methods.put("publicarBS", new int[] { 0, 22 });
    _methods.put("consultarStatusBS", new int[] { 0, 23 });
  }

  public org.omg.CORBA.portable.OutputStream _invoke (java.lang.String opName,
                                                      org.omg.CORBA.portable.InputStream _input,
                                                      org.omg.CORBA.portable.ResponseHandler handler) {
    int[] method = (int[]) _methods.get(opName);
    if (method == null) {
      throw new org.omg.CORBA.BAD_OPERATION();
    }
    switch (method[0]) {
      case 0: {
        return com.brt.gpp.componentes.consulta.orb.consultaPOA._invoke(this, method[1], _input, handler);
      }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }

  public static org.omg.CORBA.portable.OutputStream _invoke (com.brt.gpp.componentes.consulta.orb.consultaOperations _self,
                                                             int _method_id,
                                                             org.omg.CORBA.portable.InputStream _input,
                                                             org.omg.CORBA.portable.ResponseHandler _handler) {
  org.omg.CORBA.portable.OutputStream _output = null;
  {
    switch (_method_id) {
    case 0: {
    try {
      java.lang.String voucherId;
      voucherId = _input.read_string();
      java.lang.String _result = _self.consultaVoucher(voucherId);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 1: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaAssinante(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 2: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaAssinanteSimples(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 3: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      double valorTotalRecarga;
      valorTotalRecarga = _input.read_double();
      java.lang.String cpf;
      cpf = _input.read_string();
      short categoria;
      categoria = _input.read_short();
      java.lang.String hashCartaoCredito;
      hashCartaoCredito = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String _result = _self.consultaAssinanteRecarga(msisdn, valorTotalRecarga, 
                                                                cpf, categoria, 
                                                                hashCartaoCredito, 
                                                                sistemaOrigem);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 4: {
    try {
      java.lang.String GPPConsultaPreRecarga;
      GPPConsultaPreRecarga = _input.read_string();
      java.lang.String _result = _self.consultaAssinanteRecargaXML(GPPConsultaPreRecarga);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 5: {
    try {
      java.lang.String GPPConsultaPreRecarga;
      GPPConsultaPreRecarga = _input.read_string();
      java.lang.String _result = _self.consultaAssinanteRecargaMultiplaXML(GPPConsultaPreRecarga);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 6: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String inicioPeriodo;
      inicioPeriodo = _input.read_string();
      java.lang.String finalPeriodo;
      finalPeriodo = _input.read_string();
      java.lang.String _result = _self.consultaExtrato(msisdn, inicioPeriodo, finalPeriodo);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 7: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String inicioPeriodo;
      inicioPeriodo = _input.read_string();
      java.lang.String finalPeriodo;
      finalPeriodo = _input.read_string();
      java.lang.String _result = _self.consultaExtratoPulaPula(msisdn, inicioPeriodo, 
                                                               finalPeriodo);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 8: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String inicioPeriodo;
      inicioPeriodo = _input.read_string();
      java.lang.String finalPeriodo;
      finalPeriodo = _input.read_string();
      java.lang.String _result = _self.consultaExtratoPulaPulaCheio(msisdn, inicioPeriodo, 
                                                                    finalPeriodo);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 9: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaSaldoPulaPula(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 10: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaPulaPula(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 11: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String mes;
      mes = _input.read_string();
      java.lang.String _result = _self.consultaPulaPulaNoMes(msisdn, mes);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 12: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      int mes;
      mes = _input.read_long();
      java.lang.String _result = _self.consultaSaldoPulaPulaNoMes(msisdn, mes);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 13: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String inicio;
      inicio = _input.read_string();
      java.lang.String fim;
      fim = _input.read_string();
      java.lang.String _result = _self.consultaEstornoPulaPula(msisdn, inicio, 
                                                               fim);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 14: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaAparelhoAssinante(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 15: {
    try {
      int numeroJob;
      numeroJob = _input.read_long();
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _result = _self.consultaJobTecnomen(numeroJob);
      _output = _handler.createReply();
      if (_result == null) {
        throw new org.omg.CORBA.BAD_PARAM("Invalid array length");
      }
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHelper.write(_output, _result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 16: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String inicioPeriodo;
      inicioPeriodo = _input.read_string();
      java.lang.String finalPeriodo;
      finalPeriodo = _input.read_string();
      java.lang.String _result = _self.consultaExtratoBoomerang(msisdn, inicioPeriodo, 
                                                                finalPeriodo);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 17: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      int mes;
      mes = _input.read_long();
      com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang _result = _self.consultaSaldoBoomerang(msisdn, 
                                                                                                                 mes);
      _output = _handler.createReply();
      if (_result == null) {
        throw new org.omg.CORBA.BAD_PARAM("Invalid array length");
      }
      com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerangHelper.write(_output, _result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 18: {
    try {
      java.lang.String aXML;
      aXML = _input.read_string();
      java.lang.String _result = _self.consultaRecargaAntifraude(aXML);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 19: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaAparelho(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 20: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String _result = _self.consultaAssinanteTFPP(msisdn);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 21: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String mes;
      mes = _input.read_string();
      double _result = _self.consultarCreditoPulaPula(msisdn, mes);
      _output = _handler.createReply();
      _output.write_double((double)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 22: {
      java.lang.String numeroBS;
      numeroBS = _input.read_string();
      java.lang.String numeroIP;
      numeroIP = _input.read_string();
      java.lang.String numeroAssinante;
      numeroAssinante = _input.read_string();
      java.lang.String matriculaOperador;
      matriculaOperador = _input.read_string();
      java.lang.String _result = _self.publicarBS(numeroBS, numeroIP, numeroAssinante, 
                                                  matriculaOperador);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 23: {
      java.lang.String xmlConsulta;
      xmlConsulta = _input.read_string();
      java.lang.String _result = _self.consultarStatusBS(xmlConsulta);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }
  }
}
