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
public abstract class gerenteGPPPOA extends org.omg.PortableServer.Servant implements 
org.omg.CORBA.portable.InvokeHandler, com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations {

  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP _this () {
   return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper.narrow(super._this_object());
  }

  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP _this (org.omg.CORBA.ORB orb) {
    return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper.narrow(super._this_object(orb));
  }

  public java.lang.String[] _all_interfaces (final org.omg.PortableServer.POA poa, final byte[] objectId) {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP:1.0"
  };

  private static java.util.Dictionary _methods = new java.util.Hashtable();

  static {
    _methods.put("getNumerodeConexoes", new int[] { 0, 0 });
    _methods.put("criaConexao", new int[] { 0, 1 });
    _methods.put("removeConexao", new int[] { 0, 2 });
    _methods.put("exibeListaPlanoPreco", new int[] { 0, 3 });
    _methods.put("atualizaListaPlanoPreco", new int[] { 0, 4 });
    _methods.put("exibeListaStatusAssinante", new int[] { 0, 5 });
    _methods.put("atualizaListaStatusAssinante", new int[] { 0, 6 });
    _methods.put("exibeListaStatusServico", new int[] { 0, 7 });
    _methods.put("atualizaListaStatusServico", new int[] { 0, 8 });
    _methods.put("exibeListaSistemaOrigem", new int[] { 0, 9 });
    _methods.put("atualizaListaSistemaOrigem", new int[] { 0, 10 });
    _methods.put("exibeListaTarifaTrocaMSISDN", new int[] { 0, 11 });
    _methods.put("atualizaListaTarifaTrocaMSISDN", new int[] { 0, 12 });
    _methods.put("exibeListaValoresRecarga", new int[] { 0, 13 });
    _methods.put("atualizaListaValoresRecarga", new int[] { 0, 14 });
    _methods.put("exibeListaValoresRecargaPlanoPreco", new int[] { 0, 15 });
    _methods.put("atualizaListaValoresRecargaPlanoPreco", new int[] { 0, 16 });
    _methods.put("exibeListaBonusPulaPula", new int[] { 0, 17 });
    _methods.put("atualizaListaBonusPulaPula", new int[] { 0, 18 });
    _methods.put("exibeListaPromocao", new int[] { 0, 19 });
    _methods.put("atualizaListaPromocao", new int[] { 0, 20 });
    _methods.put("atualizaListaPromocaoDiaExecucao", new int[] { 0, 21 });
    _methods.put("exibeListaRecOrigem", new int[] { 0, 22 });
    _methods.put("atualizaListaRecOrigem", new int[] { 0, 23 });
    _methods.put("exibeListaConfiguracaoGPP", new int[] { 0, 24 });
    _methods.put("atualizaListaConfiguracaoGPP", new int[] { 0, 25 });
    _methods.put("exibeListaMotivosBloqueio", new int[] { 0, 26 });
    _methods.put("atualizaListaMotivosBloqueio", new int[] { 0, 27 });
    _methods.put("atualizaListaModulacaoPlano", new int[] { 0, 28 });
    _methods.put("atualizaListaAssinantesNaoBonificaveis", new int[] { 0, 29 });
    _methods.put("atualizaListaFeriados", new int[] { 0, 30 });
    _methods.put("processaSMS", new int[] { 0, 31 });
    _methods.put("escreveDebug", new int[] { 0, 32 });
    _methods.put("ping", new int[] { 0, 33 });
    _methods.put("finalizaGPP", new int[] { 0, 34 });
    _methods.put("getStatusProdutorSMS", new int[] { 0, 35 });
    _methods.put("getStatusEscreveDebug", new int[] { 0, 36 });
    _methods.put("getHistProcessosBatch", new int[] { 0, 37 });
    _methods.put("consultaProcessosBatch", new int[] { 0, 38 });
    _methods.put("getNumeroConexoesDisponiveis", new int[] { 0, 39 });
    _methods.put("exibirNumeroStatementsPorConexao", new int[] { 0, 40 });
    _methods.put("getListaProcessosComConexoesEmUso", new int[] { 0, 41 });
    _methods.put("adicionaThreadEnvioSMS", new int[] { 0, 42 });
    _methods.put("removeThreadEnvioSMS", new int[] { 0, 43 });
    _methods.put("finalizaPoolEnvioSMS", new int[] { 0, 44 });
    _methods.put("inicializaPoolEnvioSMS", new int[] { 0, 45 });
    _methods.put("getNumeroThreadsEnvioSMS", new int[] { 0, 46 });
    _methods.put("getNumThreadsImpCDRDadosVoz", new int[] { 0, 47 });
    _methods.put("getNumThreadsImpCDREvtRec", new int[] { 0, 48 });
    _methods.put("getNumArqPendentesDadosVoz", new int[] { 0, 49 });
    _methods.put("getNumArqPendentesEvtRec", new int[] { 0, 50 });
    _methods.put("removeThreadsDadosVoz", new int[] { 0, 51 });
    _methods.put("removeThreadsEvtRec", new int[] { 0, 52 });
    _methods.put("inicializaThreadsDadosVoz", new int[] { 0, 53 });
    _methods.put("inicializaThreadsEvtRec", new int[] { 0, 54 });
    _methods.put("reiniciaCacheFF", new int[] { 0, 55 });
    _methods.put("atualizaMapeamentos", new int[] { 0, 56 });
    _methods.put("atualizaMapeamento", new int[] { 0, 57 });
    _methods.put("exibeMapeamento", new int[] { 0, 58 });
    _methods.put("liberarConexoesEmUso", new int[] { 0, 59 });
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
        return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPOA._invoke(this, method[1], _input, handler);
      }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }

  public static org.omg.CORBA.portable.OutputStream _invoke (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self,
                                                             int _method_id,
                                                             org.omg.CORBA.portable.InputStream _input,
                                                             org.omg.CORBA.portable.ResponseHandler _handler) {
  org.omg.CORBA.portable.OutputStream _output = null;
  {
    switch (_method_id) {
    case 0: {
    try {
      short tipoConexao;
      tipoConexao = _input.read_short();
      short _result = _self.getNumerodeConexoes(tipoConexao);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 1: {
    try {
      short tipoConexao;
      tipoConexao = _input.read_short();
      boolean _result = _self.criaConexao(tipoConexao);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 2: {
    try {
      short tipoConexao;
      tipoConexao = _input.read_short();
      boolean _result = _self.removeConexao(tipoConexao);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 3: {
      java.lang.String _result = _self.exibeListaPlanoPreco();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 4: {
    try {
      boolean _result = _self.atualizaListaPlanoPreco();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 5: {
      java.lang.String _result = _self.exibeListaStatusAssinante();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 6: {
    try {
      boolean _result = _self.atualizaListaStatusAssinante();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 7: {
      java.lang.String _result = _self.exibeListaStatusServico();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 8: {
    try {
      boolean _result = _self.atualizaListaStatusServico();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 9: {
      java.lang.String _result = _self.exibeListaSistemaOrigem();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 10: {
    try {
      boolean _result = _self.atualizaListaSistemaOrigem();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 11: {
      java.lang.String _result = _self.exibeListaTarifaTrocaMSISDN();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 12: {
    try {
      boolean _result = _self.atualizaListaTarifaTrocaMSISDN();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 13: {
      java.lang.String _result = _self.exibeListaValoresRecarga();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 14: {
    try {
      boolean _result = _self.atualizaListaValoresRecarga();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 15: {
      java.lang.String _result = _self.exibeListaValoresRecargaPlanoPreco();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 16: {
    try {
      boolean _result = _self.atualizaListaValoresRecargaPlanoPreco();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 17: {
      java.lang.String _result = _self.exibeListaBonusPulaPula();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 18: {
    try {
      boolean _result = _self.atualizaListaBonusPulaPula();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 19: {
      java.lang.String _result = _self.exibeListaPromocao();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 20: {
    try {
      boolean _result = _self.atualizaListaPromocao();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 21: {
    try {
      boolean _result = _self.atualizaListaPromocaoDiaExecucao();
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
      java.lang.String _result = _self.exibeListaRecOrigem();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 23: {
    try {
      boolean _result = _self.atualizaListaRecOrigem();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 24: {
      java.lang.String _result = _self.exibeListaConfiguracaoGPP();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 25: {
    try {
      boolean _result = _self.atualizaListaConfiguracaoGPP();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 26: {
      java.lang.String _result = _self.exibeListaMotivosBloqueio();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 27: {
    try {
      boolean _result = _self.atualizaListaMotivosBloqueio();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 28: {
    try {
      boolean _result = _self.atualizaListaModulacaoPlano();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 29: {
    try {
      boolean _result = _self.atualizaListaAssinantesNaoBonificaveis();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 30: {
    try {
      boolean _result = _self.atualizaListaFeriados();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 31: {
      boolean deveProcessar;
      deveProcessar = _input.read_boolean();
      boolean _result = _self.processaSMS(deveProcessar);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
      return _output;
    }
    case 32: {
      boolean escreveDebug;
      escreveDebug = _input.read_boolean();
      boolean _result = _self.escreveDebug(escreveDebug);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
      return _output;
    }
    case 33: {
      java.lang.String _result = _self.ping();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 34: {
      _self.finalizaGPP();
      _output = _handler.createReply();
      return _output;
    }
    case 35: {
    try {
      boolean _result = _self.getStatusProdutorSMS();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 36: {
    try {
      boolean _result = _self.getStatusEscreveDebug();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 37: {
    try {
      short aIdProcBatch;
      aIdProcBatch = _input.read_short();
      java.lang.String aDatIni;
      aDatIni = _input.read_string();
      java.lang.String aDatFim;
      aDatFim = _input.read_string();
      java.lang.String aIdtStatus;
      aIdtStatus = _input.read_string();
      java.lang.String _result = _self.getHistProcessosBatch(aIdProcBatch, aDatIni, 
                                                             aDatFim, aIdtStatus);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 38: {
    try {
      java.lang.String _result = _self.consultaProcessosBatch();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 39: {
    try {
      short tipoConexao;
      tipoConexao = _input.read_short();
      short _result = _self.getNumeroConexoesDisponiveis(tipoConexao);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 40: {
      java.lang.String _result = _self.exibirNumeroStatementsPorConexao();
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 41: {
    try {
      short tipoConexao;
      tipoConexao = _input.read_short();
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _result = _self.getListaProcessosComConexoesEmUso(tipoConexao);
      _output = _handler.createReply();
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.write(_output, _result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 42: {
    try {
      boolean _result = _self.adicionaThreadEnvioSMS();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 43: {
    try {
      boolean _result = _self.removeThreadEnvioSMS();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 44: {
      _self.finalizaPoolEnvioSMS();
      _output = _handler.createReply();
      return _output;
    }
    case 45: {
      _self.inicializaPoolEnvioSMS();
      _output = _handler.createReply();
      return _output;
    }
    case 46: {
      short _result = _self.getNumeroThreadsEnvioSMS();
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 47: {
      short _result = _self.getNumThreadsImpCDRDadosVoz();
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 48: {
      short _result = _self.getNumThreadsImpCDREvtRec();
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 49: {
      short _result = _self.getNumArqPendentesDadosVoz();
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 50: {
      short _result = _self.getNumArqPendentesEvtRec();
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 51: {
    try {
      _self.removeThreadsDadosVoz();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 52: {
    try {
      _self.removeThreadsEvtRec();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 53: {
    try {
      _self.inicializaThreadsDadosVoz();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 54: {
    try {
      _self.inicializaThreadsEvtRec();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 55: {
      _self.reiniciaCacheFF();
      _output = _handler.createReply();
      return _output;
    }
    case 56: {
      boolean limpar;
      limpar = _input.read_boolean();
      short _result = _self.atualizaMapeamentos(limpar);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 57: {
      java.lang.String nome;
      nome = _input.read_string();
      boolean limpar;
      limpar = _input.read_boolean();
      short _result = _self.atualizaMapeamento(nome, limpar);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 58: {
      java.lang.String nome;
      nome = _input.read_string();
      java.lang.String _result = _self.exibeMapeamento(nome);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 59: {
      long idProcesso;
      idProcesso = _input.read_longlong();
      _self.liberarConexoesEmUso(idProcesso);
      _output = _handler.createReply();
      return _output;
    }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }
  }
}
