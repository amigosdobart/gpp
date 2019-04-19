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
public abstract class recargaPOA extends org.omg.PortableServer.Servant implements 
org.omg.CORBA.portable.InvokeHandler, com.brt.gpp.componentes.recarga.orb.recargaOperations {

  public com.brt.gpp.componentes.recarga.orb.recarga _this () {
   return com.brt.gpp.componentes.recarga.orb.recargaHelper.narrow(super._this_object());
  }

  public com.brt.gpp.componentes.recarga.orb.recarga _this (org.omg.CORBA.ORB orb) {
    return com.brt.gpp.componentes.recarga.orb.recargaHelper.narrow(super._this_object(orb));
  }

  public java.lang.String[] _all_interfaces (final org.omg.PortableServer.POA poa, final byte[] objectId) {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/recarga/orb/recarga:1.0"
  };

  private static java.util.Dictionary _methods = new java.util.Hashtable();

  static {
    _methods.put("executaRecargaXML", new int[] { 0, 0 });
    _methods.put("executaAjusteXML", new int[] { 0, 1 });
    _methods.put("executaRecargaBanco", new int[] { 0, 2 });
    _methods.put("executaRecarga", new int[] { 0, 3 });
    _methods.put("executaAjuste", new int[] { 0, 4 });
    _methods.put("executaAjusteDescrito", new int[] { 0, 5 });
    _methods.put("executaAjusteMultiplosSaldos", new int[] { 0, 6 });
    _methods.put("podeRecarregar", new int[] { 0, 7 });
    _methods.put("podeRecarregarVarejo", new int[] { 0, 8 });
    _methods.put("alteraStatusVoucher", new int[] { 0, 9 });
    _methods.put("resubmeterPedidoVoucher", new int[] { 0, 10 });
    _methods.put("consultaPreRecarga", new int[] { 0, 11 });
    _methods.put("aprovarLoteRecarga", new int[] { 0, 12 });
    _methods.put("rejeitarLoteRecarga", new int[] { 0, 13 });
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
        return com.brt.gpp.componentes.recarga.orb.recargaPOA._invoke(this, method[1], _input, handler);
      }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }

  public static org.omg.CORBA.portable.OutputStream _invoke (com.brt.gpp.componentes.recarga.orb.recargaOperations _self,
                                                             int _method_id,
                                                             org.omg.CORBA.portable.InputStream _input,
                                                             org.omg.CORBA.portable.ResponseHandler _handler) {
  org.omg.CORBA.portable.OutputStream _output = null;
  {
    switch (_method_id) {
    case 0: {
    try {
      java.lang.String GPPRecarga;
      GPPRecarga = _input.read_string();
      java.lang.String _result = _self.executaRecargaXML(GPPRecarga);
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
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 1: {
    try {
      java.lang.String GPPRecargaAjuste;
      GPPRecargaAjuste = _input.read_string();
      short _result = _self.executaAjusteXML(GPPRecargaAjuste);
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
    catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(_output, _exception);
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
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      java.lang.String identificacaoRecarga;
      identificacaoRecarga = _input.read_string();
      java.lang.String nsuInstituicao;
      nsuInstituicao = _input.read_string();
      java.lang.String codLoja;
      codLoja = _input.read_string();
      java.lang.String tipoCredito;
      tipoCredito = _input.read_string();
      double valor;
      valor = _input.read_double();
      java.lang.String dataHora;
      dataHora = _input.read_string();
      java.lang.String dataHoraBanco;
      dataHoraBanco = _input.read_string();
      java.lang.String dataContabil;
      dataContabil = _input.read_string();
      java.lang.String terminal;
      terminal = _input.read_string();
      java.lang.String tipoTerminal;
      tipoTerminal = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.executaRecargaBanco(MSISDN, tipoTransacao, identificacaoRecarga, 
                                                nsuInstituicao, codLoja, tipoCredito, 
                                                valor, dataHora, dataHoraBanco, 
                                                dataContabil, terminal, tipoTerminal, 
                                                sistemaOrigem, operador);
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
    case 3: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      java.lang.String identificacaoRecarga;
      identificacaoRecarga = _input.read_string();
      java.lang.String tipoCredito;
      tipoCredito = _input.read_string();
      double valor;
      valor = _input.read_double();
      java.lang.String dataHora;
      dataHora = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String nsuInstituicao;
      nsuInstituicao = _input.read_string();
      java.lang.String hash_cc;
      hash_cc = _input.read_string();
      java.lang.String cpf;
      cpf = _input.read_string();
      short _result = _self.executaRecarga(MSISDN, tipoTransacao, identificacaoRecarga, 
                                           tipoCredito, valor, dataHora, sistemaOrigem, 
                                           operador, nsuInstituicao, hash_cc, cpf);
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
    case 4: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      java.lang.String tipoCredito;
      tipoCredito = _input.read_string();
      double valor;
      valor = _input.read_double();
      java.lang.String tipo;
      tipo = _input.read_string();
      java.lang.String dataHora;
      dataHora = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String data_expiracao;
      data_expiracao = _input.read_string();
      short _result = _self.executaAjuste(MSISDN, tipoTransacao, tipoCredito, valor, 
                                          tipo, dataHora, sistemaOrigem, operador, 
                                          data_expiracao);
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
    case 5: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      java.lang.String tipoCredito;
      tipoCredito = _input.read_string();
      double valor;
      valor = _input.read_double();
      java.lang.String tipo;
      tipo = _input.read_string();
      java.lang.String dataHora;
      dataHora = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      java.lang.String data_expiracao;
      data_expiracao = _input.read_string();
      java.lang.String descricao;
      descricao = _input.read_string();
      short _result = _self.executaAjusteDescrito(MSISDN, tipoTransacao, tipoCredito, 
                                                  valor, tipo, dataHora, sistemaOrigem, 
                                                  operador, data_expiracao, descricao);
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
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      double ajustePrincipal;
      ajustePrincipal = _input.read_double();
      double ajustePeriodico;
      ajustePeriodico = _input.read_double();
      double ajusteBonus;
      ajusteBonus = _input.read_double();
      double ajusteSms;
      ajusteSms = _input.read_double();
      double ajusteGprs;
      ajusteGprs = _input.read_double();
      java.lang.String expPrincipal;
      expPrincipal = _input.read_string();
      java.lang.String expPeriodico;
      expPeriodico = _input.read_string();
      java.lang.String expBonus;
      expBonus = _input.read_string();
      java.lang.String expSms;
      expSms = _input.read_string();
      java.lang.String expGprs;
      expGprs = _input.read_string();
      java.lang.String tipo;
      tipo = _input.read_string();
      java.lang.String dataHora;
      dataHora = _input.read_string();
      java.lang.String sistemaOrigem;
      sistemaOrigem = _input.read_string();
      java.lang.String operador;
      operador = _input.read_string();
      short _result = _self.executaAjusteMultiplosSaldos(MSISDN, tipoTransacao, 
                                                         ajustePrincipal, ajustePeriodico, 
                                                         ajusteBonus, ajusteSms, 
                                                         ajusteGprs, expPrincipal, 
                                                         expPeriodico, expBonus, 
                                                         expSms, expGprs, tipo, 
                                                         dataHora, sistemaOrigem, 
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
    case 7: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      double valorCreditos;
      valorCreditos = _input.read_double();
      short _result = _self.podeRecarregar(MSISDN, valorCreditos);
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
      double valorCreditos;
      valorCreditos = _input.read_double();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      short _result = _self.podeRecarregarVarejo(MSISDN, valorCreditos, tipoTransacao);
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
      java.lang.String numeroVoucher;
      numeroVoucher = _input.read_string();
      double statusVoucher;
      statusVoucher = _input.read_double();
      java.lang.String comentario;
      comentario = _input.read_string();
      short _result = _self.alteraStatusVoucher(numeroVoucher, statusVoucher, comentario);
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
      long numeroPedido;
      numeroPedido = _input.read_longlong();
      _self.resubmeterPedidoVoucher(numeroPedido);
      _output = _handler.createReply();
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 11: {
    try {
      java.lang.String MSISDN;
      MSISDN = _input.read_string();
      double valorCreditos;
      valorCreditos = _input.read_double();
      java.lang.String tipoTransacao;
      tipoTransacao = _input.read_string();
      java.lang.String origem;
      origem = _input.read_string();
      short _result = _self.consultaPreRecarga(MSISDN, valorCreditos, tipoTransacao, 
                                               origem);
      _output = _handler.createReply();
      _output.write_short((short)_result);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.write(_output, _exception);
    }
    catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException _exception) {
      _output = _handler.createExceptionReply();
      com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(_output, _exception);
    }
      return _output;
    }
    case 12: {
      java.lang.String lote;
      lote = _input.read_string();
      java.lang.String usuario;
      usuario = _input.read_string();
      short _result = _self.aprovarLoteRecarga(lote, usuario);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    case 13: {
      java.lang.String lote;
      lote = _input.read_string();
      java.lang.String usuario;
      usuario = _input.read_string();
      short _result = _self.rejeitarLoteRecarga(lote, usuario);
      _output = _handler.createReply();
      _output.write_short((short)_result);
      return _output;
    }
    }
    throw new org.omg.CORBA.BAD_OPERATION();
  }
  }
}
