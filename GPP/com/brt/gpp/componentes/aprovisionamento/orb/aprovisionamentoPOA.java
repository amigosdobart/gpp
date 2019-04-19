package com.brt.gpp.componentes.aprovisionamento.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface aprovisionamento {
  ...
};
 * </pre>
 */
public abstract class aprovisionamentoPOA extends org.omg.PortableServer.Servant implements 
org.omg.CORBA.portable.InvokeHandler, com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations {

  public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _this () {
   return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.narrow(super._this_object());
  }

  public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _this (org.omg.CORBA.ORB orb) {
    return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.narrow(super._this_object(orb));
  }

  public java.lang.String[] _all_interfaces (final org.omg.PortableServer.POA poa, final byte[] objectId) {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0"
  };

  private static java.util.Dictionary _methods = new java.util.Hashtable();

  static {
    _methods.put("ativaAssinante", new int[] { 0, 0 });
    _methods.put("desativaAssinante", new int[] { 0, 1 });
    _methods.put("desativarAssinanteXML", new int[] { 0, 2 });
    _methods.put("alterarStatusAssinante", new int[] { 0, 3 });
    _methods.put("alterarStatusPeriodico", new int[] { 0, 4 });
    _methods.put("bloqueiaAssinante", new int[] { 0, 5 });
    _methods.put("desbloqueiaAssinante", new int[] { 0, 6 });
    _methods.put("trocaMSISDNAssinante", new int[] { 0, 7 });
    _methods.put("trocaPlanoAssinante", new int[] { 0, 8 });
    _methods.put("trocaSimCardAssinante", new int[] { 0, 9 });
    _methods.put("atualizaFriendsFamilyAssinante", new int[] { 0, 10 });
    _methods.put("trocaSenha", new int[] { 0, 11 });
    _methods.put("resetSenha", new int[] { 0, 12 });
    _methods.put("consultaAssinante", new int[] { 0, 13 });
    _methods.put("ativacaoCancelamentoServico", new int[] { 0, 14 });
    _methods.put("bloquearServicos", new int[] { 0, 15 });
    _methods.put("desativarHotLine", new int[] { 0, 16 });
    _methods.put("desativarHotLineURA", new int[] { 0, 17 });
    _methods.put("confirmaBloqueioDesbloqueioServicos", new int[] { 0, 18 });
    _methods.put("enviarSMS", new int[] { 0, 19 });
    _methods.put("enviarSMSMulti", new int[] { 0, 20 });
    _methods.put("gravarMensagemSMS", new int[] { 0, 21 });
    _methods.put("inserePulaPula", new int[] { 0, 22 });
    _methods.put("retiraPulaPula", new int[] { 0, 23 });
    _methods.put("trocaPulaPula", new int[] { 0, 24 });
    _methods.put("trocaPulaPulaPPP", new int[] { 0, 25 });
    _methods.put("executaPulaPula", new int[] { 0, 26 });
    _methods.put("zerarSaldos", new int[] { 0, 27 });
    _methods.put("ativaAssinanteComStatus", new int[] { 0, 28 });
    _methods.put("cobrarServico", new int[] { 0, 29 });
    _methods.put("cadastrarBumerangue", new int[] { 0, 30 });
    _methods.put("atualizarAmigosTodaHora", new int[] { 0, 31 });
    _methods.put("reativarAssinante", new int[] { 0, 32 });
    _methods.put("aprovarLote", new int[] { 0, 33 });
    _methods.put("rejeitarLote", new int[] { 0, 34 });
    _methods.put("inserirPromocaoFaleGratisANoite", new int[] { 0, 35 });
    _methods.put("retirarPromocaoFaleGratisANoite", new int[] { 0, 36 });
    _methods.put("enviarRequisicaoTangram", new int[] { 0, 37 });
    _methods.put("atualizaAmigosDeGraca", new int[] { 0, 38 });
    _methods.put("ativarAssinante", new int[] { 0, 39 });
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
        return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPOA._invoke(this, method[1], _input, handler);
      }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }

  public static org.omg.CORBA.portable.OutputStream _invoke (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self,
                                                             int _method_id,
                                                             org.omg.CORBA.portable.InputStream _input,
                                                             org.omg.CORBA.portable.ResponseHandler _handler) {
  org.omg.CORBA.portable.OutputStream _output = null;
  {
    switch (_method_id) {
    case 0: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String IMSI;
      IMSI = _input.read_string();
      java.lang.String planoPreco;
      planoPreco = _input.read_string();
      double creditoInicial;
      creditoInicial = _input.read_double();
      short idioma;
      idioma = _input.read_short();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.ativaAssinante(MSISDN, IMSI, planoPreco, creditoInicial, 
                                           idioma, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 1: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String motivoDesativacao;
      motivoDesativacao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante _result = _self.desativaAssinante(MSISDN, 
                                                                                                                                         motivoDesativacao, 
                                                                                                                                         operador);
      _output = _handler.createReply();
      if (_result == null) {
        throw new org.omg.CORBA.BAD_PARAM("Invalid array length");
      }
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.write(_output, _result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 2: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String motivoDesativacao;
      motivoDesativacao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String _result = _self.desativarAssinanteXML(MSISDN, motivoDesativacao, 
                                                             operador);
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
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 3: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      short status;
      status = _input.read_short();
      java.lang.String dataExpiracao;
      dataExpiracao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.alterarStatusAssinante(msisdn, status, dataExpiracao, 
                                                   operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 4: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      short status;
      status = _input.read_short();
      java.lang.String dataExpiracao;
      dataExpiracao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.alterarStatusPeriodico(msisdn, status, dataExpiracao, 
                                                   operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 5: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String idBloqueio;
      idBloqueio = _input.read_string();
      double tarifa;
      tarifa = _input.read_double();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.bloqueiaAssinante(MSISDN, idBloqueio, tarifa, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 6: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.desbloqueiaAssinante(MSISDN, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 7: {
    try {
      java.lang.String antigoMSISDN;
      antigoMSISDN = _input.read_string();
      java.lang.String novoMSISDN;
      novoMSISDN = _input.read_string();
      java.lang.String idTarifa;
      idTarifa = _input.read_string();
      double valorTarifa;
      valorTarifa = _input.read_double();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.trocaMSISDNAssinante(antigoMSISDN, novoMSISDN, idTarifa, 
                                                 valorTarifa, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 8: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String novoPlano;
      novoPlano = _input.read_string();
      double valorMudanca;
      valorMudanca = _input.read_double();
      java.lang.String operador;
      operador = _input.read_string();
      double valorFranquia;
      valorFranquia = _input.read_double();
      short _result = _self.trocaPlanoAssinante(MSISDN, novoPlano, valorMudanca, 
                                                operador, valorFranquia);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 9: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String novoIMSI;
      novoIMSI = _input.read_string();
      double valorMudanca;
      valorMudanca = _input.read_double();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.trocaSimCardAssinante(MSISDN, novoIMSI, valorMudanca, 
                                                  operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 10: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String listaMSISDN;
      listaMSISDN = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String codigoServico;
      codigoServico = _input.read_string();
      short _result = _self.atualizaFriendsFamilyAssinante(MSISDN, listaMSISDN, 
                                                           operador, codigoServico);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 11: {
    try {
      java.lang.String GPPTrocaSenha;
      GPPTrocaSenha = _input.read_string();
      java.lang.String _result = _self.trocaSenha(GPPTrocaSenha);
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
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 12: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String novaSenha;
      novaSenha = _input.read_string();
      short _result = _self.resetSenha(MSISDN, novaSenha);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 13: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String _result = _self.consultaAssinante(MSISDN);
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
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 14: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String idServico;
      idServico = _input.read_string();
      short acao;
      acao = _input.read_short();
      short _result = _self.ativacaoCancelamentoServico(MSISDN, idServico, acao);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 15: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      long _result = _self.bloquearServicos(msisdn);
      _output = _handler.createReply();
      _output.write_longlong((long)_result);
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
      java.lang.String categoria;
      categoria = _input.read_string();
      long _result = _self.desativarHotLine(msisdn, categoria);
      _output = _handler.createReply();
      _output.write_longlong((long)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 17: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String categoria;
      categoria = _input.read_string();
      long _result = _self.desativarHotLineURA(msisdn, categoria);
      _output = _handler.createReply();
      _output.write_longlong((long)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 18: {
    try {
      java.lang.String xmlAprovisionamento;
      xmlAprovisionamento = _input.read_string();
      _self.confirmaBloqueioDesbloqueioServicos(xmlAprovisionamento);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 19: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String mensagem;
      mensagem = _input.read_string();
      _self.enviarSMS(MSISDN, mensagem);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 20: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String mensagem;
      mensagem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      _self.enviarSMSMulti(MSISDN, mensagem, operador);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 21: {
    try {
      java.lang.String msisdnOrigem;
      msisdnOrigem = _input.read_string();
      java.lang.String msisdnDestino;
      msisdnDestino = _input.read_string();
      java.lang.String mensagem;
      mensagem = _input.read_string();
      int prioridade;
      prioridade = _input.read_long();
      java.lang.String tipo;
      tipo = _input.read_string();
      boolean _result = _self.gravarMensagemSMS(msisdnOrigem, msisdnDestino, mensagem, 
                                                prioridade, tipo);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 22: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String promocao;
      promocao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.inserePulaPula(msisdn, promocao, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 23: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.retiraPulaPula(msisdn, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 24: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String promocaoNova;
      promocaoNova = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.trocaPulaPula(msisdn, promocaoNova, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 25: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      int promocaoNova;
      promocaoNova = _input.read_long();
      java.lang.String operador;
      operador = _input.read_string();
      int motivo;
      motivo = _input.read_long();
      int tipoDocumento;
      tipoDocumento = _input.read_long();
      java.lang.String numDocumento;
      numDocumento = _input.read_string();
      short _result = _self.trocaPulaPulaPPP(msisdn, promocaoNova, operador, motivo, 
                                             tipoDocumento, numDocumento);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 26: {
      java.lang.String tipoExecucao;
      tipoExecucao = _input.read_string();
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String dataReferencia;
      dataReferencia = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      int motivo;
      motivo = _input.read_long();
      short _result = _self.executaPulaPula(tipoExecucao, msisdn, dataReferencia, 
                                            operador, motivo);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 27: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      int codSaldosZerados;
      codSaldosZerados = _input.read_long();
      short _result = _self.zerarSaldos(msisdn, operador, tipoTransacao, codSaldosZerados);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 28: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String imsi;
      imsi = _input.read_string();
      java.lang.String planoPreco;
      planoPreco = _input.read_string();
      double creditoInicial;
      creditoInicial = _input.read_double();
      short idioma;
      idioma = _input.read_short();
      java.lang.String operador;
      operador = _input.read_string();
      short status;
      status = _input.read_short();
      short _result = _self.ativaAssinanteComStatus(msisdn, imsi, planoPreco, creditoInicial, 
                                                    idioma, operador, status);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 29: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String codigoServico;
      codigoServico = _input.read_string();
      java.lang.String operacao;
      operacao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String _result = _self.cobrarServico(msisdn, codigoServico, operacao, 
                                                     operador);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 30: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String codigoServico;
      codigoServico = _input.read_string();
      java.lang.String operacao;
      operacao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String _result = _self.cadastrarBumerangue(msisdn, codigoServico, 
                                                           operacao, operador);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 31: {
    try {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String listaMsisdn;
      listaMsisdn = _input.read_string();
      java.lang.String codigoServico;
      codigoServico = _input.read_string();
      java.lang.String operacao;
      operacao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String _result = _self.atualizarAmigosTodaHora(msisdn, listaMsisdn, 
                                                               codigoServico, operacao, 
                                                               operador);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 32: {
      java.lang.String msisdnNovo;
      msisdnNovo = _input.read_string();
      java.lang.String msisdnAntigo;
      msisdnAntigo = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.reativarAssinante(msisdnNovo, msisdnAntigo, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 33: {
    try {
      java.lang.String lote;
      lote = _input.read_string();
      short _result = _self.aprovarLote(lote);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 34: {
    try {
      java.lang.String lote;
      lote = _input.read_string();
      short _result = _self.rejeitarLote(lote);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 35: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String promocao;
      promocao = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.inserirPromocaoFaleGratisANoite(msisdn, promocao, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 36: {
      java.lang.String msisdn;
      msisdn = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.retirarPromocaoFaleGratisANoite(msisdn, operador);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 37: {
    try {
      java.lang.String xml;
      xml = _input.read_string();
      short _result = _self.enviarRequisicaoTangram(xml);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 38: {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String listaMSISDN;
      listaMSISDN = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String codigoServico;
      codigoServico = _input.read_string();
      short _result = _self.atualizaAmigosDeGraca(MSISDN, listaMSISDN, operador, 
                                                  codigoServico);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 39: {
      java.lang.String xml;
      xml = _input.read_string();
      short _result = _self.ativarAssinante(xml);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }
  }
}
