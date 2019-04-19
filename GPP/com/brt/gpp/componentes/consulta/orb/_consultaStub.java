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
public class _consultaStub extends com.inprise.vbroker.CORBA.portable.ObjectImpl implements com.brt.gpp.componentes.consulta.orb.consulta {
  final public static java.lang.Class _opsClass = com.brt.gpp.componentes.consulta.orb.consultaOperations.class;

  public java.lang.String[] _ids () {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/consulta/orb/consulta:1.0"
  };

  /**
   * <pre>
   *   string consultaVoucher (in string voucherId)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaVoucher (java.lang.String voucherId) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaVoucher", true);
        _output.write_string((java.lang.String)voucherId);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaVoucher", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)voucherId);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaVoucher",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaVoucher(voucherId);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinante (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinante", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinante(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaAssinanteSimples (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteSimples (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinanteSimples", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinanteSimples", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinanteSimples",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinanteSimples(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinanteRecarga", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_double((double)valorTotalRecarga);
        _output.write_string((java.lang.String)cpf);
        _output.write_short((short)categoria);
        _output.write_string((java.lang.String)hashCartaoCredito);
        _output.write_string((java.lang.String)sistemaOrigem);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinanteRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_double((double)valorTotalRecarga);
             _output.write_string((java.lang.String)cpf);
             _output.write_short((short)categoria);
             _output.write_string((java.lang.String)hashCartaoCredito);
             _output.write_string((java.lang.String)sistemaOrigem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinanteRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinanteRecarga(msisdn, valorTotalRecarga, 
                                                             cpf, categoria, hashCartaoCredito, 
                                                             sistemaOrigem);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinanteRecargaXML", true);
        _output.write_string((java.lang.String)GPPConsultaPreRecarga);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinanteRecargaXML", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)GPPConsultaPreRecarga);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinanteRecargaXML",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinanteRecargaXML(GPPConsultaPreRecarga);
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
   *   string consultaAssinanteRecargaMultiplaXML (in string GPPConsultaPreRecarga)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String consultaAssinanteRecargaMultiplaXML (java.lang.String GPPConsultaPreRecarga) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                               com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                               com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinanteRecargaMultiplaXML", true);
        _output.write_string((java.lang.String)GPPConsultaPreRecarga);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinanteRecargaMultiplaXML", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)GPPConsultaPreRecarga);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinanteRecargaMultiplaXML",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinanteRecargaMultiplaXML(GPPConsultaPreRecarga);
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaExtrato", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)inicioPeriodo);
        _output.write_string((java.lang.String)finalPeriodo);
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaExtrato", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)inicioPeriodo);
             _output.write_string((java.lang.String)finalPeriodo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaExtrato",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaExtrato(msisdn, inicioPeriodo, finalPeriodo);
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaExtratoPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)inicioPeriodo);
        _output.write_string((java.lang.String)finalPeriodo);
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaExtratoPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)inicioPeriodo);
             _output.write_string((java.lang.String)finalPeriodo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaExtratoPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaExtratoPulaPula(msisdn, inicioPeriodo, 
                                                            finalPeriodo);
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaExtratoPulaPulaCheio", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)inicioPeriodo);
        _output.write_string((java.lang.String)finalPeriodo);
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaExtratoPulaPulaCheio", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)inicioPeriodo);
             _output.write_string((java.lang.String)finalPeriodo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaExtratoPulaPulaCheio",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaExtratoPulaPulaCheio(msisdn, inicioPeriodo, 
                                                                 finalPeriodo);
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
   *   string consultaSaldoPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPula (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaSaldoPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaSaldoPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaSaldoPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaSaldoPulaPula(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaPulaPula (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPula (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaPulaPula(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaPulaPulaNoMes (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaPulaPulaNoMes (java.lang.String msisdn, 
                                                 java.lang.String mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaPulaPulaNoMes", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)mes);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaPulaPulaNoMes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)mes);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaPulaPulaNoMes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaPulaPulaNoMes(msisdn, mes);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaSaldoPulaPulaNoMes (in string msisdn, in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaSaldoPulaPulaNoMes (java.lang.String msisdn, 
                                                      int mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaSaldoPulaPulaNoMes", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_long((int)mes);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaSaldoPulaPulaNoMes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_long((int)mes);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaSaldoPulaPulaNoMes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaSaldoPulaPulaNoMes(msisdn, mes);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaEstornoPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)inicio);
        _output.write_string((java.lang.String)fim);
        _input = this._invoke(_output);
        _result = _input.read_string();
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaEstornoPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)inicio);
             _output.write_string((java.lang.String)fim);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaEstornoPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaEstornoPulaPula(msisdn, inicio, fim);
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
   *   string consultaAparelhoAssinante (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelhoAssinante (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAparelhoAssinante", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAparelhoAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAparelhoAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAparelhoAssinante(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (in long numeroJob)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen consultaJobTecnomen (int numeroJob) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _result;
      try {
        _output = this._request("consultaJobTecnomen", true);
        _output.write_long((int)numeroJob);
        _input = this._invoke(_output);
        _result = com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHelper.read(_input);
        return _result;
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaJobTecnomen", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_long((int)numeroJob);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaJobTecnomen",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHelper.read(_input);
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
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _ret = _self.consultaJobTecnomen(numeroJob);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaExtratoBoomerang", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)inicioPeriodo);
        _output.write_string((java.lang.String)finalPeriodo);
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaExtratoBoomerang", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)inicioPeriodo);
             _output.write_string((java.lang.String)finalPeriodo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaExtratoBoomerang",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaExtratoBoomerang(msisdn, inicioPeriodo, 
                                                             finalPeriodo);
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
   *   com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (in string msisdn,
                                                                                              in long mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang consultaSaldoBoomerang (java.lang.String msisdn, 
                                                                                                     int mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang _result;
      try {
        _output = this._request("consultaSaldoBoomerang", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_long((int)mes);
        _input = this._invoke(_output);
        _result = com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerangHelper.read(_input);
        return _result;
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaSaldoBoomerang", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_long((int)mes);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaSaldoBoomerang",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerangHelper.read(_input);
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
      com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang _ret = _self.consultaSaldoBoomerang(msisdn, 
                                                                                                              mes);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaRecargaAntifraude", true);
        _output.write_string((java.lang.String)aXML);
        _input = this._invoke(_output);
        _result = _input.read_string();
        return _result;
      }
      catch (org.omg.CORBA.portable.ApplicationException _exception) {
        final org.omg.CORBA.portable.InputStream in = _exception.getInputStream();
        java.lang.String _exception_id = _exception.getId();
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.read(_exception.getInputStream());
        }
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaRecargaAntifraude", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aXML);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaRecargaAntifraude",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaRecargaAntifraude(aXML);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
    } catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException x_1) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_1);
      }
      throw x_1;
    } catch (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException x_2) {
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).exceptionalCompletion((Throwable)x_2);
      }
      throw x_2;
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
   *   string consultaAparelho (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAparelho (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAparelho", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAparelho", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAparelho",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAparelho(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   string consultaAssinanteTFPP (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaAssinanteTFPP (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinanteTFPP", true);
        _output.write_string((java.lang.String)msisdn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinanteTFPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaAssinanteTFPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaAssinanteTFPP(msisdn);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
  }

  /**
   * <pre>
   *   double consultarCreditoPulaPula (in string msisdn, in string mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public double consultarCreditoPulaPula (java.lang.String msisdn, 
                                          java.lang.String mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      double _result;
      try {
        _output = this._request("consultarCreditoPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)mes);
        _input = this._invoke(_output);
        _result = _input.read_double();
        return _result;
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultarCreditoPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      double _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)mes);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultarCreditoPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_double();
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
      double _ret = _self.consultarCreditoPulaPula(msisdn, mes);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
      return _ret;
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
    }
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

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("publicarBS", true);
        _output.write_string((java.lang.String)numeroBS);
        _output.write_string((java.lang.String)numeroIP);
        _output.write_string((java.lang.String)numeroAssinante);
        _output.write_string((java.lang.String)matriculaOperador);
        _input = this._invoke(_output);
        _result = _input.read_string();
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("publicarBS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)numeroBS);
             _output.write_string((java.lang.String)numeroIP);
             _output.write_string((java.lang.String)numeroAssinante);
             _output.write_string((java.lang.String)matriculaOperador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("publicarBS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.publicarBS(numeroBS, numeroIP, numeroAssinante, 
                                               matriculaOperador);
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
   *   string consultarStatusBS (in string xmlConsulta);
   * </pre>
   */
  public java.lang.String consultarStatusBS (java.lang.String xmlConsulta) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultarStatusBS", true);
        _output.write_string((java.lang.String)xmlConsulta);
        _input = this._invoke(_output);
        _result = _input.read_string();
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
    com.brt.gpp.componentes.consulta.orb.consultaOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultarStatusBS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.consulta.orb.consultaOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)xmlConsulta);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultarStatusBS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultarStatusBS(xmlConsulta);
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
