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
public class _recargaStub extends com.inprise.vbroker.CORBA.portable.ObjectImpl implements com.brt.gpp.componentes.recarga.orb.recarga {
  final public static java.lang.Class _opsClass = com.brt.gpp.componentes.recarga.orb.recargaOperations.class;

  public java.lang.String[] _ids () {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/recarga/orb/recarga:1.0"
  };

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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("executaRecargaXML", true);
        _output.write_string((java.lang.String)GPPRecarga);
        _input = this._invoke(_output);
        _result = _input.read_string();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaRecargaXML", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)GPPRecarga);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaRecargaXML",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_string();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      java.lang.String _ret = _self.executaRecargaXML(GPPRecarga);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_4) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_4);
      }
      throw x_4;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaAjusteXML", true);
        _output.write_string((java.lang.String)GPPRecargaAjuste);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaAjusteXML", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)GPPRecargaAjuste);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaAjusteXML",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaAjusteXML(GPPRecargaAjuste);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_4) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_4);
      }
      throw x_4;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaRecargaBanco", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_string((java.lang.String)identificacaoRecarga);
        _output.write_string((java.lang.String)nsuInstituicao);
        _output.write_string((java.lang.String)codLoja);
        _output.write_string((java.lang.String)tipoCredito);
        _output.write_double((double)valor);
        _output.write_string((java.lang.String)dataHora);
        _output.write_string((java.lang.String)dataHoraBanco);
        _output.write_string((java.lang.String)dataContabil);
        _output.write_string((java.lang.String)terminal);
        _output.write_string((java.lang.String)tipoTerminal);
        _output.write_string((java.lang.String)sistemaOrigem);
        _output.write_string((java.lang.String)operador);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaRecargaBanco", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_string((java.lang.String)identificacaoRecarga);
             _output.write_string((java.lang.String)nsuInstituicao);
             _output.write_string((java.lang.String)codLoja);
             _output.write_string((java.lang.String)tipoCredito);
             _output.write_double((double)valor);
             _output.write_string((java.lang.String)dataHora);
             _output.write_string((java.lang.String)dataHoraBanco);
             _output.write_string((java.lang.String)dataContabil);
             _output.write_string((java.lang.String)terminal);
             _output.write_string((java.lang.String)tipoTerminal);
             _output.write_string((java.lang.String)sistemaOrigem);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaRecargaBanco",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaRecargaBanco(MSISDN, tipoTransacao, identificacaoRecarga, 
                                             nsuInstituicao, codLoja, tipoCredito, 
                                             valor, dataHora, dataHoraBanco, dataContabil, 
                                             terminal, tipoTerminal, sistemaOrigem, 
                                             operador);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaRecarga", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_string((java.lang.String)identificacaoRecarga);
        _output.write_string((java.lang.String)tipoCredito);
        _output.write_double((double)valor);
        _output.write_string((java.lang.String)dataHora);
        _output.write_string((java.lang.String)sistemaOrigem);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)nsuInstituicao);
        _output.write_string((java.lang.String)hash_cc);
        _output.write_string((java.lang.String)cpf);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_string((java.lang.String)identificacaoRecarga);
             _output.write_string((java.lang.String)tipoCredito);
             _output.write_double((double)valor);
             _output.write_string((java.lang.String)dataHora);
             _output.write_string((java.lang.String)sistemaOrigem);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)nsuInstituicao);
             _output.write_string((java.lang.String)hash_cc);
             _output.write_string((java.lang.String)cpf);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaRecarga(MSISDN, tipoTransacao, identificacaoRecarga, 
                                        tipoCredito, valor, dataHora, sistemaOrigem, 
                                        operador, nsuInstituicao, hash_cc, cpf);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaAjuste", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_string((java.lang.String)tipoCredito);
        _output.write_double((double)valor);
        _output.write_string((java.lang.String)tipo);
        _output.write_string((java.lang.String)dataHora);
        _output.write_string((java.lang.String)sistemaOrigem);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)data_expiracao);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaAjuste", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_string((java.lang.String)tipoCredito);
             _output.write_double((double)valor);
             _output.write_string((java.lang.String)tipo);
             _output.write_string((java.lang.String)dataHora);
             _output.write_string((java.lang.String)sistemaOrigem);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)data_expiracao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaAjuste",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaAjuste(MSISDN, tipoTransacao, tipoCredito, valor, 
                                       tipo, dataHora, sistemaOrigem, operador, 
                                       data_expiracao);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaAjusteDescrito", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_string((java.lang.String)tipoCredito);
        _output.write_double((double)valor);
        _output.write_string((java.lang.String)tipo);
        _output.write_string((java.lang.String)dataHora);
        _output.write_string((java.lang.String)sistemaOrigem);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)data_expiracao);
        _output.write_string((java.lang.String)descricao);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaAjusteDescrito", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_string((java.lang.String)tipoCredito);
             _output.write_double((double)valor);
             _output.write_string((java.lang.String)tipo);
             _output.write_string((java.lang.String)dataHora);
             _output.write_string((java.lang.String)sistemaOrigem);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)data_expiracao);
             _output.write_string((java.lang.String)descricao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaAjusteDescrito",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaAjusteDescrito(MSISDN, tipoTransacao, tipoCredito, 
                                               valor, tipo, dataHora, sistemaOrigem, 
                                               operador, data_expiracao, descricao);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaAjusteMultiplosSaldos", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_double((double)ajustePrincipal);
        _output.write_double((double)ajustePeriodico);
        _output.write_double((double)ajusteBonus);
        _output.write_double((double)ajusteSms);
        _output.write_double((double)ajusteGprs);
        _output.write_string((java.lang.String)expPrincipal);
        _output.write_string((java.lang.String)expPeriodico);
        _output.write_string((java.lang.String)expBonus);
        _output.write_string((java.lang.String)expSms);
        _output.write_string((java.lang.String)expGprs);
        _output.write_string((java.lang.String)tipo);
        _output.write_string((java.lang.String)dataHora);
        _output.write_string((java.lang.String)sistemaOrigem);
        _output.write_string((java.lang.String)operador);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaAjusteMultiplosSaldos", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_double((double)ajustePrincipal);
             _output.write_double((double)ajustePeriodico);
             _output.write_double((double)ajusteBonus);
             _output.write_double((double)ajusteSms);
             _output.write_double((double)ajusteGprs);
             _output.write_string((java.lang.String)expPrincipal);
             _output.write_string((java.lang.String)expPeriodico);
             _output.write_string((java.lang.String)expBonus);
             _output.write_string((java.lang.String)expSms);
             _output.write_string((java.lang.String)expGprs);
             _output.write_string((java.lang.String)tipo);
             _output.write_string((java.lang.String)dataHora);
             _output.write_string((java.lang.String)sistemaOrigem);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaAjusteMultiplosSaldos",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.executaAjusteMultiplosSaldos(MSISDN, tipoTransacao, ajustePrincipal, 
                                                      ajustePeriodico, ajusteBonus, 
                                                      ajusteSms, ajusteGprs, expPrincipal, 
                                                      expPeriodico, expBonus, expSms, 
                                                      expGprs, tipo, dataHora, 
                                                      sistemaOrigem, operador);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("podeRecarregar", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_double((double)valorCreditos);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("podeRecarregar", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_double((double)valorCreditos);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("podeRecarregar",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.podeRecarregar(MSISDN, valorCreditos);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("podeRecarregarVarejo", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_double((double)valorCreditos);
        _output.write_string((java.lang.String)tipoTransacao);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("podeRecarregarVarejo", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_double((double)valorCreditos);
             _output.write_string((java.lang.String)tipoTransacao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("podeRecarregarVarejo",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.podeRecarregarVarejo(MSISDN, valorCreditos, tipoTransacao);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("alteraStatusVoucher", true);
        _output.write_string((java.lang.String)numeroVoucher);
        _output.write_double((double)statusVoucher);
        _output.write_string((java.lang.String)comentario);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("alteraStatusVoucher", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)numeroVoucher);
             _output.write_double((double)statusVoucher);
             _output.write_string((java.lang.String)comentario);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("alteraStatusVoucher",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.alteraStatusVoucher(numeroVoucher, statusVoucher, comentario);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
  }

  /**
   * <pre>
   *   void resubmeterPedidoVoucher (in long long numeroPedido)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void resubmeterPedidoVoucher (long numeroPedido) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("resubmeterPedidoVoucher", true);
        _output.write_longlong((long)numeroPedido);
        _input = this._invoke(_output);
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("resubmeterPedidoVoucher", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)numeroPedido);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("resubmeterPedidoVoucher",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return;

    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      _self.resubmeterPedidoVoucher(numeroPedido);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    break;
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("consultaPreRecarga", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_double((double)valorCreditos);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_string((java.lang.String)origem);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
        }
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(_exception.getInputStream());
        }
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaPreRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_double((double)valorCreditos);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_string((java.lang.String)origem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaPreRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.consultaPreRecarga(MSISDN, valorCreditos, tipoTransacao, 
                                            origem);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPTecnomenException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
    } catch (com.brt.gpp.comum.gppExceptions.GPPCorbaException x_3) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_3);
      }
      throw x_3;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
  }

  /**
   * <pre>
   *   short aprovarLoteRecarga (in string lote, in string usuario);
   * </pre>
   */
  public short aprovarLoteRecarga (java.lang.String lote, 
                                   java.lang.String usuario) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("aprovarLoteRecarga", true);
        _output.write_string((java.lang.String)lote);
        _output.write_string((java.lang.String)usuario);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("aprovarLoteRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)lote);
             _output.write_string((java.lang.String)usuario);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("aprovarLoteRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.aprovarLoteRecarga(lote, usuario);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
  }

  /**
   * <pre>
   *   short rejeitarLoteRecarga (in string lote, in string usuario);
   * </pre>
   */
  public short rejeitarLoteRecarga (java.lang.String lote, 
                                    java.lang.String usuario) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("rejeitarLoteRecarga", true);
        _output.write_string((java.lang.String)lote);
        _output.write_string((java.lang.String)usuario);
        _input = this._invoke(_output);
        _result = _input.read_short();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.recarga.orb.recargaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("rejeitarLoteRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.recarga.orb.recargaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)lote);
             _output.write_string((java.lang.String)usuario);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("rejeitarLoteRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_short();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _result;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
    if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
       }finally {
      _servant_postinvoke(_so);
      this._releaseReply(_input);
      }
      }
       try {
      short _ret = _self.rejeitarLoteRecarga(lote, usuario);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (java.lang.RuntimeException re) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)re);
      }
      throw re;
    } catch (java.lang.Error err) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)err);
      }
      throw err;
    } finally {
      _servant_postinvoke(_so);
    }

    }
    }
  }

}
