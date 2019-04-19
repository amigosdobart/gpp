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
public abstract class processosBatchPOA extends org.omg.PortableServer.Servant implements 
org.omg.CORBA.portable.InvokeHandler, com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations {

  public com.brt.gpp.componentes.processosBatch.orb.processosBatch _this () {
   return com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper.narrow(super._this_object());
  }

  public com.brt.gpp.componentes.processosBatch.orb.processosBatch _this (org.omg.CORBA.ORB orb) {
    return com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper.narrow(super._this_object(orb));
  }

  public java.lang.String[] _all_interfaces (final org.omg.PortableServer.POA poa, final byte[] objectId) {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch:1.0"
  };

  private static java.util.Dictionary _methods = new java.util.Hashtable();

  static {
    _methods.put("gravaMensagemSMS", new int[] { 0, 0 });
    _methods.put("enviaUsuariosShutdown", new int[] { 0, 1 });
    _methods.put("enviarUsuariosStatusNormal", new int[] { 0, 2 });
    _methods.put("executaRecargaRecorrente", new int[] { 0, 3 });
    _methods.put("executaRecargaMicrosiga", new int[] { 0, 4 });
    _methods.put("enviaInfosRecarga", new int[] { 0, 5 });
    _methods.put("enviaBonusCSP14", new int[] { 0, 6 });
    _methods.put("liberaBumerangue", new int[] { 0, 7 });
    _methods.put("enviaInfosCartaoUnico", new int[] { 0, 8 });
    _methods.put("emiteNFBonusTLDC", new int[] { 0, 9 });
    _methods.put("estornaBonusSobreBonus", new int[] { 0, 10 });
    _methods.put("enviarRecargasConciliacao", new int[] { 0, 11 });
    _methods.put("executaTratamentoVoucher", new int[] { 0, 12 });
    _methods.put("executaContestacao", new int[] { 0, 13 });
    _methods.put("envioComprovanteServico", new int[] { 0, 14 });
    _methods.put("importaArquivosCDR", new int[] { 0, 15 });
    _methods.put("importaAssinantes", new int[] { 0, 16 });
    _methods.put("importaUsuarioPortalNDS", new int[] { 0, 17 });
    _methods.put("importaEstoqueSap", new int[] { 0, 18 });
    _methods.put("atualizaDiasSemRecarga", new int[] { 0, 19 });
    _methods.put("sumarizarProdutoPlano", new int[] { 0, 20 });
    _methods.put("sumarizarAjustes", new int[] { 0, 21 });
    _methods.put("sumarizarContabilidade", new int[] { 0, 22 });
    _methods.put("sumarizarContabilidadeCN", new int[] { 0, 23 });
    _methods.put("consolidarContabilidade", new int[] { 0, 24 });
    _methods.put("exportarTabelasDW", new int[] { 0, 25 });
    _methods.put("importaPedidosCriacaoVoucher", new int[] { 0, 26 });
    _methods.put("gravaDadosArquivoOrdem", new int[] { 0, 27 });
    _methods.put("getUserIDRequisitante", new int[] { 0, 28 });
    _methods.put("enviarConsolidacaoSCR", new int[] { 0, 29 });
    _methods.put("calcularIndiceBonificacao", new int[] { 0, 30 });
    _methods.put("bloqueioAutomaticoServicoPorSaldo", new int[] { 0, 31 });
    _methods.put("bloqueioAutomaticoServicoIncluindoRE", new int[] { 0, 32 });
    _methods.put("enviaPedidoPorEMail", new int[] { 0, 33 });
    _methods.put("enviaContingenciaSolicitada", new int[] { 0, 34 });
    _methods.put("gerenciarPromocao", new int[] { 0, 35 });
    _methods.put("promocaoPrepago", new int[] { 0, 36 });
    _methods.put("contingenciaDaContingencia", new int[] { 0, 37 });
    _methods.put("cadastraAssinantesPromocaoLondrina", new int[] { 0, 38 });
    _methods.put("executarPromocaoLondrina", new int[] { 0, 39 });
    _methods.put("avisaRecargaPromocaoLondrina", new int[] { 0, 40 });
    _methods.put("atualizaNumLotePedido", new int[] { 0, 41 });
    _methods.put("getQtdeCartoes", new int[] { 0, 42 });
    _methods.put("gerenteFeliz", new int[] { 0, 43 });
    _methods.put("atualizaLimiteCreditoVarejo", new int[] { 0, 44 });
    _methods.put("marretaGPP", new int[] { 0, 45 });
    _methods.put("gerarArquivoCobilling", new int[] { 0, 46 });
    _methods.put("enviaDadosPulaPulaDW", new int[] { 0, 47 });
    _methods.put("reiniciaCicloTres", new int[] { 0, 48 });
    _methods.put("processaBumerangue14Dia", new int[] { 0, 49 });
    _methods.put("processaBumerangue14Mes", new int[] { 0, 50 });
    _methods.put("sumarizaAssinantesShutdown", new int[] { 0, 51 });
    _methods.put("gravaNotificacaoSMS", new int[] { 0, 52 });
    _methods.put("aprovisionarAssinantesMMS", new int[] { 0, 53 });
    _methods.put("executaConcessaoPulaPula", new int[] { 0, 54 });
    _methods.put("sumarizaRecargasAssinantes", new int[] { 0, 55 });
    _methods.put("executaProcessoBatch", new int[] { 0, 56 });
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
        return com.brt.gpp.componentes.processosBatch.orb.processosBatchPOA._invoke(this, method[1], _input, handler);
      }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }

  public static org.omg.CORBA.portable.OutputStream _invoke (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self,
                                                             int _method_id,
                                                             org.omg.CORBA.portable.InputStream _input,
                                                             org.omg.CORBA.portable.ResponseHandler _handler) {
  org.omg.CORBA.portable.OutputStream _output = null;
  {
    switch (_method_id) {
    case 0: {
    try {
      java.lang.String aMsisdn;
      aMsisdn = _input.read_string();
      java.lang.String aMensagem;
      aMensagem = _input.read_string();
      short aPrioridade;
      aPrioridade = _input.read_short();
      java.lang.String aTipo;
      aTipo = _input.read_string();
      boolean _result = _self.gravaMensagemSMS(aMsisdn, aMensagem, aPrioridade, 
                                               aTipo);
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 1: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviaUsuariosShutdown(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 2: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviarUsuariosStatusNormal(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 3: {
    try {
      boolean _result = _self.executaRecargaRecorrente();
      _output = _handler.createReply();
      _output.write_boolean((boolean)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 4: {
    try {
      short _result = _self.executaRecargaMicrosiga();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 5: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviaInfosRecarga(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 6: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviaBonusCSP14(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 7: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      _self.liberaBumerangue(aData);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 8: {
    try {
      short _result = _self.enviaInfosCartaoUnico();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 9: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.emiteNFBonusTLDC(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 10: {
    try {
      short _result = _self.estornaBonusSobreBonus();
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
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviarRecargasConciliacao(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 12: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.executaTratamentoVoucher(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 13: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.executaContestacao(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 14: {
    try {
      boolean _result = _self.envioComprovanteServico();
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
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.importaArquivosCDR(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 16: {
    try {
      short _result = _self.importaAssinantes();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 17: {
    try {
      short _result = _self.importaUsuarioPortalNDS();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 18: {
    try {
      short _result = _self.importaEstoqueSap();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 19: {
    try {
      short _result = _self.atualizaDiasSemRecarga();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 20: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.sumarizarProdutoPlano(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 21: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.sumarizarAjustes(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 22: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.sumarizarContabilidade(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 23: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      java.lang.String aCN;
      aCN = _input.read_string();
      short _result = _self.sumarizarContabilidadeCN(aData, aCN);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 24: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.consolidarContabilidade(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 25: {
    try {
      short _result = _self.exportarTabelasDW();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 26: {
    try {
      short _result = _self.importaPedidosCriacaoVoucher();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 27: {
    try {
      java.lang.String nomeArquivo;
      nomeArquivo = _input.read_string();
      byte[] buffer;
      buffer = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.read(_input);
      boolean _result = _self.gravaDadosArquivoOrdem(nomeArquivo, buffer);
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
      long numOrdem;
      numOrdem = _input.read_longlong();
      java.lang.String _result = _self.getUserIDRequisitante(numOrdem);
      _output = _handler.createReply();
      _output.write_string((java.lang.String)_result);
      return _output;
    }
    case 29: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short _result = _self.enviarConsolidacaoSCR(aData);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 30: {
    try {
      java.lang.String data;
      data = _input.read_string();
      short _result = _self.calcularIndiceBonificacao(data);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 31: {
    try {
      java.lang.String dataReferencia;
      dataReferencia = _input.read_string();
      short _result = _self.bloqueioAutomaticoServicoPorSaldo(dataReferencia);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 32: {
    try {
      java.lang.String dataReferencia;
      dataReferencia = _input.read_string();
      short _result = _self.bloqueioAutomaticoServicoIncluindoRE(dataReferencia);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 33: {
    try {
      long numeroOrdem;
      numeroOrdem = _input.read_longlong();
      _self.enviaPedidoPorEMail(numeroOrdem);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 34: {
    try {
      _self.enviaContingenciaSolicitada();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 35: {
    try {
      java.lang.String data;
      data = _input.read_string();
      short _result = _self.gerenciarPromocao(data);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 36: {
    try {
      java.lang.String data;
      data = _input.read_string();
      short _result = _self.promocaoPrepago(data);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 37: {
    try {
      java.lang.String data;
      data = _input.read_string();
      short _result = _self.contingenciaDaContingencia(data);
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
    try {
      double valorBonus;
      valorBonus = _input.read_double();
      _self.cadastraAssinantesPromocaoLondrina(valorBonus);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 39: {
    try {
      java.lang.String data;
      data = _input.read_string();
      _self.executarPromocaoLondrina(data);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 40: {
    try {
      java.lang.String data;
      data = _input.read_string();
      _self.avisaRecargaPromocaoLondrina(data);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 41: {
    try {
      long numOrdem;
      numOrdem = _input.read_longlong();
      int numItem;
      numItem = _input.read_long();
      long numLoteIni;
      numLoteIni = _input.read_longlong();
      long numLoteFim;
      numLoteFim = _input.read_longlong();
      _self.atualizaNumLotePedido(numOrdem, numItem, numLoteIni, numLoteFim);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 42: {
    try {
      long numOrdem;
      numOrdem = _input.read_longlong();
      int numItem;
      numItem = _input.read_long();
      long _result = _self.getQtdeCartoes(numOrdem, numItem);
      _output = _handler.createReply();
      _output.write_longlong((long)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 43: {
    try {
      short _result = _self.gerenteFeliz();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 44: {
    try {
      short _result = _self.atualizaLimiteCreditoVarejo();
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 45: {
    try {
      java.lang.String paramIn;
      paramIn = _input.read_string();
      short _result = _self.marretaGPP(paramIn);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 46: {
    try {
      java.lang.String csp;
      csp = _input.read_string();
      java.lang.String inicio;
      inicio = _input.read_string();
      java.lang.String fim;
      fim = _input.read_string();
      java.lang.String UF;
      UF = _input.read_string();
      _self.gerarArquivoCobilling(csp, inicio, fim, UF);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 47: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      short aPromocao;
      aPromocao = _input.read_short();
      _self.enviaDadosPulaPulaDW(aData, aPromocao);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 48: {
    try {
      _self.reiniciaCicloTres();
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 49: {
    try {
      java.lang.String aData;
      aData = _input.read_string();
      _self.processaBumerangue14Dia(aData);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 50: {
    try {
      short mes;
      mes = _input.read_short();
      _self.processaBumerangue14Mes(mes);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 51: {
    try {
      java.lang.String dataAnalise;
      dataAnalise = _input.read_string();
      short _result = _self.sumarizaAssinantesShutdown(dataAnalise);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 52: {
    try {
      _self.gravaNotificacaoSMS();
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
      java.lang.String data;
      data = _input.read_string();
      _self.aprovisionarAssinantesMMS(data);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 54: {
      java.lang.String tipoExecucao;
      tipoExecucao = _input.read_string();
      java.lang.String referencia;
      referencia = _input.read_string();
      int promocao;
      promocao = _input.read_long();
      short _result = _self.executaConcessaoPulaPula(tipoExecucao, referencia, 
                                                     promocao);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 55: {
      java.lang.String referencia;
      referencia = _input.read_string();
      short _result = _self.sumarizaRecargasAssinantes(referencia);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 56: {
    try {
      int idProcessoBatch;
      idProcessoBatch = _input.read_long();
      java.lang.String[] parametros;
      parametros = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.read(_input);
      _self.executaProcessoBatch(idProcessoBatch, parametros);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }
  }
}
