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
public class _processosBatchStub extends com.inprise.vbroker.CORBA.portable.ObjectImpl implements com.brt.gpp.componentes.processosBatch.orb.processosBatch {
  final public static java.lang.Class _opsClass = com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations.class;

  public java.lang.String[] _ids () {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch:1.0"
  };

  /**
   * <pre>
   *   boolean gravaMensagemSMS (in string aMsisdn, in string aMensagem,
                            in short aPrioridade, in string aTipo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravaMensagemSMS (java.lang.String aMsisdn, 
                                   java.lang.String aMensagem, 
                                   short aPrioridade, 
                                   java.lang.String aTipo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("gravaMensagemSMS", true);
        _output.write_string((java.lang.String)aMsisdn);
        _output.write_string((java.lang.String)aMensagem);
        _output.write_short((short)aPrioridade);
        _output.write_string((java.lang.String)aTipo);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gravaMensagemSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aMsisdn);
             _output.write_string((java.lang.String)aMensagem);
             _output.write_short((short)aPrioridade);
             _output.write_string((java.lang.String)aTipo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gravaMensagemSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_boolean();
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
      boolean _ret = _self.gravaMensagemSMS(aMsisdn, aMensagem, aPrioridade, aTipo);
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
   *   short enviaUsuariosShutdown (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaUsuariosShutdown (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviaUsuariosShutdown", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaUsuariosShutdown", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaUsuariosShutdown",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviaUsuariosShutdown(aData);
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
   *   short enviarUsuariosStatusNormal (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarUsuariosStatusNormal (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviarUsuariosStatusNormal", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarUsuariosStatusNormal", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarUsuariosStatusNormal",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviarUsuariosStatusNormal(aData);
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
   *   boolean executaRecargaRecorrente ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean executaRecargaRecorrente () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("executaRecargaRecorrente", true);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaRecargaRecorrente", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaRecargaRecorrente",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_boolean();
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
      boolean _ret = _self.executaRecargaRecorrente();
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
   *   short executaRecargaMicrosiga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaRecargaMicrosiga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaRecargaMicrosiga", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaRecargaMicrosiga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaRecargaMicrosiga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.executaRecargaMicrosiga();
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
   *   short enviaInfosRecarga (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosRecarga (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviaInfosRecarga", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaInfosRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaInfosRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviaInfosRecarga(aData);
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
   *   short enviaBonusCSP14 (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaBonusCSP14 (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviaBonusCSP14", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaBonusCSP14", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaBonusCSP14",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviaBonusCSP14(aData);
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
   *   void liberaBumerangue (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void liberaBumerangue (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("liberaBumerangue", true);
        _output.write_string((java.lang.String)aData);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("liberaBumerangue", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("liberaBumerangue",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.liberaBumerangue(aData);
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
   *   short enviaInfosCartaoUnico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviaInfosCartaoUnico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviaInfosCartaoUnico", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaInfosCartaoUnico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaInfosCartaoUnico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviaInfosCartaoUnico();
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
   *   short emiteNFBonusTLDC (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short emiteNFBonusTLDC (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("emiteNFBonusTLDC", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("emiteNFBonusTLDC", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("emiteNFBonusTLDC",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.emiteNFBonusTLDC(aData);
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
   *   short estornaBonusSobreBonus ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short estornaBonusSobreBonus () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("estornaBonusSobreBonus", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("estornaBonusSobreBonus", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("estornaBonusSobreBonus",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.estornaBonusSobreBonus();
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
   *   short enviarRecargasConciliacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarRecargasConciliacao (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviarRecargasConciliacao", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarRecargasConciliacao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarRecargasConciliacao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviarRecargasConciliacao(aData);
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
   *   short executaTratamentoVoucher (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaTratamentoVoucher (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaTratamentoVoucher", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaTratamentoVoucher", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaTratamentoVoucher",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.executaTratamentoVoucher(aData);
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
   *   short executaContestacao (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short executaContestacao (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaContestacao", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaContestacao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaContestacao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.executaContestacao(aData);
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
   *   boolean envioComprovanteServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean envioComprovanteServico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("envioComprovanteServico", true);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("envioComprovanteServico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("envioComprovanteServico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_boolean();
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
      boolean _ret = _self.envioComprovanteServico();
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
   *   short importaArquivosCDR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaArquivosCDR (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("importaArquivosCDR", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("importaArquivosCDR", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("importaArquivosCDR",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.importaArquivosCDR(aData);
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
   *   short importaAssinantes ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaAssinantes () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("importaAssinantes", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("importaAssinantes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("importaAssinantes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.importaAssinantes();
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
   *   short importaUsuarioPortalNDS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaUsuarioPortalNDS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("importaUsuarioPortalNDS", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("importaUsuarioPortalNDS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("importaUsuarioPortalNDS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.importaUsuarioPortalNDS();
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
   *   short importaEstoqueSap ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaEstoqueSap () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("importaEstoqueSap", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("importaEstoqueSap", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("importaEstoqueSap",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.importaEstoqueSap();
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
   *   short atualizaDiasSemRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaDiasSemRecarga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaDiasSemRecarga", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaDiasSemRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaDiasSemRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaDiasSemRecarga();
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
   *   short sumarizarProdutoPlano (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarProdutoPlano (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizarProdutoPlano", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizarProdutoPlano", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizarProdutoPlano",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizarProdutoPlano(aData);
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
   *   short sumarizarAjustes (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarAjustes (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizarAjustes", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizarAjustes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizarAjustes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizarAjustes(aData);
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
   *   short sumarizarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidade (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizarContabilidade", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizarContabilidade", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizarContabilidade",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizarContabilidade(aData);
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
   *   short sumarizarContabilidadeCN (in string aData, in string aCN)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizarContabilidadeCN (java.lang.String aData, 
                                         java.lang.String aCN) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizarContabilidadeCN", true);
        _output.write_string((java.lang.String)aData);
        _output.write_string((java.lang.String)aCN);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizarContabilidadeCN", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
             _output.write_string((java.lang.String)aCN);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizarContabilidadeCN",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizarContabilidadeCN(aData, aCN);
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
   *   short consolidarContabilidade (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short consolidarContabilidade (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("consolidarContabilidade", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consolidarContabilidade", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consolidarContabilidade",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.consolidarContabilidade(aData);
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
   *   short exportarTabelasDW ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short exportarTabelasDW () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("exportarTabelasDW", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exportarTabelasDW", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exportarTabelasDW",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.exportarTabelasDW();
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
   *   short importaPedidosCriacaoVoucher ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short importaPedidosCriacaoVoucher () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("importaPedidosCriacaoVoucher", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("importaPedidosCriacaoVoucher", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("importaPedidosCriacaoVoucher",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.importaPedidosCriacaoVoucher();
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
   *   boolean gravaDadosArquivoOrdem (in string nomeArquivo,
                                  in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDados buffer)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravaDadosArquivoOrdem (java.lang.String nomeArquivo, 
                                         byte[] buffer) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("gravaDadosArquivoOrdem", true);
        _output.write_string((java.lang.String)nomeArquivo);
        com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.write(_output, buffer);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gravaDadosArquivoOrdem", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)nomeArquivo);
             com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.write(_output, buffer);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gravaDadosArquivoOrdem",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_boolean();
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
      boolean _ret = _self.gravaDadosArquivoOrdem(nomeArquivo, buffer);
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
   *   string getUserIDRequisitante (in long long numOrdem);
   * </pre>
   */
  public java.lang.String getUserIDRequisitante (long numOrdem) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("getUserIDRequisitante", true);
        _output.write_longlong((long)numOrdem);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getUserIDRequisitante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)numOrdem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getUserIDRequisitante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.getUserIDRequisitante(numOrdem);
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
   *   short enviarConsolidacaoSCR (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarConsolidacaoSCR (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviarConsolidacaoSCR", true);
        _output.write_string((java.lang.String)aData);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarConsolidacaoSCR", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarConsolidacaoSCR",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviarConsolidacaoSCR(aData);
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
   *   short calcularIndiceBonificacao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short calcularIndiceBonificacao (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("calcularIndiceBonificacao", true);
        _output.write_string((java.lang.String)data);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("calcularIndiceBonificacao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("calcularIndiceBonificacao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.calcularIndiceBonificacao(data);
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
   *   short bloqueioAutomaticoServicoPorSaldo (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoPorSaldo (java.lang.String dataReferencia) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("bloqueioAutomaticoServicoPorSaldo", true);
        _output.write_string((java.lang.String)dataReferencia);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bloqueioAutomaticoServicoPorSaldo", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)dataReferencia);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("bloqueioAutomaticoServicoPorSaldo",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.bloqueioAutomaticoServicoPorSaldo(dataReferencia);
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
   *   short bloqueioAutomaticoServicoIncluindoRE (in string dataReferencia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short bloqueioAutomaticoServicoIncluindoRE (java.lang.String dataReferencia) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("bloqueioAutomaticoServicoIncluindoRE", true);
        _output.write_string((java.lang.String)dataReferencia);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bloqueioAutomaticoServicoIncluindoRE", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)dataReferencia);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("bloqueioAutomaticoServicoIncluindoRE",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.bloqueioAutomaticoServicoIncluindoRE(dataReferencia);
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
   *   void enviaPedidoPorEMail (in long long numeroOrdem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaPedidoPorEMail (long numeroOrdem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("enviaPedidoPorEMail", true);
        _output.write_longlong((long)numeroOrdem);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaPedidoPorEMail", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)numeroOrdem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaPedidoPorEMail",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.enviaPedidoPorEMail(numeroOrdem);
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
   *   void enviaContingenciaSolicitada ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaContingenciaSolicitada () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("enviaContingenciaSolicitada", true);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaContingenciaSolicitada", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaContingenciaSolicitada",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.enviaContingenciaSolicitada();
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
   *   short gerenciarPromocao (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenciarPromocao (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("gerenciarPromocao", true);
        _output.write_string((java.lang.String)data);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gerenciarPromocao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gerenciarPromocao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.gerenciarPromocao(data);
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
   *   short promocaoPrepago (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short promocaoPrepago (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("promocaoPrepago", true);
        _output.write_string((java.lang.String)data);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("promocaoPrepago", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("promocaoPrepago",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.promocaoPrepago(data);
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
   *   short contingenciaDaContingencia (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short contingenciaDaContingencia (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("contingenciaDaContingencia", true);
        _output.write_string((java.lang.String)data);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("contingenciaDaContingencia", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("contingenciaDaContingencia",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.contingenciaDaContingencia(data);
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
   *   void cadastraAssinantesPromocaoLondrina (in double valorBonus)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void cadastraAssinantesPromocaoLondrina (double valorBonus) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("cadastraAssinantesPromocaoLondrina", true);
        _output.write_double((double)valorBonus);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cadastraAssinantesPromocaoLondrina", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_double((double)valorBonus);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("cadastraAssinantesPromocaoLondrina",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.cadastraAssinantesPromocaoLondrina(valorBonus);
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
   *   void executarPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executarPromocaoLondrina (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("executarPromocaoLondrina", true);
        _output.write_string((java.lang.String)data);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executarPromocaoLondrina", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executarPromocaoLondrina",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.executarPromocaoLondrina(data);
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
   *   void avisaRecargaPromocaoLondrina (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void avisaRecargaPromocaoLondrina (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("avisaRecargaPromocaoLondrina", true);
        _output.write_string((java.lang.String)data);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("avisaRecargaPromocaoLondrina", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("avisaRecargaPromocaoLondrina",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.avisaRecargaPromocaoLondrina(data);
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
   *   void atualizaNumLotePedido (in long long numOrdem, in long numItem,
                              in long long numLoteIni, in long long numLoteFim)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void atualizaNumLotePedido (long numOrdem, 
                                     int numItem, 
                                     long numLoteIni, 
                                     long numLoteFim) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("atualizaNumLotePedido", true);
        _output.write_longlong((long)numOrdem);
        _output.write_long((int)numItem);
        _output.write_longlong((long)numLoteIni);
        _output.write_longlong((long)numLoteFim);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaNumLotePedido", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)numOrdem);
             _output.write_long((int)numItem);
             _output.write_longlong((long)numLoteIni);
             _output.write_longlong((long)numLoteFim);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaNumLotePedido",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.atualizaNumLotePedido(numOrdem, numItem, numLoteIni, numLoteFim);
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
   *   long long getQtdeCartoes (in long long numOrdem, in long numItem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long getQtdeCartoes (long numOrdem, 
                              int numItem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
        _output = this._request("getQtdeCartoes", true);
        _output.write_longlong((long)numOrdem);
        _output.write_long((int)numItem);
        _input = this._invoke(_output);
        _result = _input.read_longlong();
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getQtdeCartoes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)numOrdem);
             _output.write_long((int)numItem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getQtdeCartoes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = _input.read_longlong();
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
      long _ret = _self.getQtdeCartoes(numOrdem, numItem);
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
   *   short gerenteFeliz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short gerenteFeliz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("gerenteFeliz", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gerenteFeliz", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gerenteFeliz",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.gerenteFeliz();
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
   *   short atualizaLimiteCreditoVarejo ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaLimiteCreditoVarejo () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaLimiteCreditoVarejo", true);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaLimiteCreditoVarejo", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaLimiteCreditoVarejo",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaLimiteCreditoVarejo();
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
   *   short marretaGPP (in string paramIn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short marretaGPP (java.lang.String paramIn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("marretaGPP", true);
        _output.write_string((java.lang.String)paramIn);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("marretaGPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)paramIn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("marretaGPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.marretaGPP(paramIn);
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
   *   void gerarArquivoCobilling (in string csp, in string inicio, in string fim,
                              in string UF)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void gerarArquivoCobilling (java.lang.String csp, 
                                     java.lang.String inicio, 
                                     java.lang.String fim, 
                                     java.lang.String UF) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("gerarArquivoCobilling", true);
        _output.write_string((java.lang.String)csp);
        _output.write_string((java.lang.String)inicio);
        _output.write_string((java.lang.String)fim);
        _output.write_string((java.lang.String)UF);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gerarArquivoCobilling", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)csp);
             _output.write_string((java.lang.String)inicio);
             _output.write_string((java.lang.String)fim);
             _output.write_string((java.lang.String)UF);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gerarArquivoCobilling",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.gerarArquivoCobilling(csp, inicio, fim, UF);
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
   *   void enviaDadosPulaPulaDW (in string aData, in short aPromocao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviaDadosPulaPulaDW (java.lang.String aData, 
                                    short aPromocao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("enviaDadosPulaPulaDW", true);
        _output.write_string((java.lang.String)aData);
        _output.write_short((short)aPromocao);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviaDadosPulaPulaDW", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
             _output.write_short((short)aPromocao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviaDadosPulaPulaDW",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.enviaDadosPulaPulaDW(aData, aPromocao);
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
   *   void reiniciaCicloTres ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void reiniciaCicloTres () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("reiniciaCicloTres", true);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reiniciaCicloTres", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("reiniciaCicloTres",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.reiniciaCicloTres();
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
   *   void processaBumerangue14Dia (in string aData)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Dia (java.lang.String aData) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("processaBumerangue14Dia", true);
        _output.write_string((java.lang.String)aData);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("processaBumerangue14Dia", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)aData);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("processaBumerangue14Dia",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.processaBumerangue14Dia(aData);
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
   *   void processaBumerangue14Mes (in short mes)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void processaBumerangue14Mes (short mes) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("processaBumerangue14Mes", true);
        _output.write_short((short)mes);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("processaBumerangue14Mes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)mes);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("processaBumerangue14Mes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.processaBumerangue14Mes(mes);
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
   *   short sumarizaAssinantesShutdown (in string dataAnalise)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short sumarizaAssinantesShutdown (java.lang.String dataAnalise) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizaAssinantesShutdown", true);
        _output.write_string((java.lang.String)dataAnalise);
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
        throw new org.omg.CORBA.UNKNOWN("Unexpected User Exception: " + _exception_id);
      }
      catch (org.omg.CORBA.portable.RemarshalException _exception) {
        continue;
      }
      finally {
        this._releaseReply(_input);
      }
    } else {
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizaAssinantesShutdown", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)dataAnalise);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizaAssinantesShutdown",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizaAssinantesShutdown(dataAnalise);
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
   *   void gravaNotificacaoSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void gravaNotificacaoSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("gravaNotificacaoSMS", true);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gravaNotificacaoSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gravaNotificacaoSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.gravaNotificacaoSMS();
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
   *   void aprovisionarAssinantesMMS (in string data)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void aprovisionarAssinantesMMS (java.lang.String data) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("aprovisionarAssinantesMMS", true);
        _output.write_string((java.lang.String)data);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("aprovisionarAssinantesMMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)data);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("aprovisionarAssinantesMMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.aprovisionarAssinantesMMS(data);
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
   *   short executaConcessaoPulaPula (in string tipoExecucao,
                                  in string referencia, in long promocao);
   * </pre>
   */
  public short executaConcessaoPulaPula (java.lang.String tipoExecucao, 
                                         java.lang.String referencia, 
                                         int promocao) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaConcessaoPulaPula", true);
        _output.write_string((java.lang.String)tipoExecucao);
        _output.write_string((java.lang.String)referencia);
        _output.write_long((int)promocao);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaConcessaoPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)tipoExecucao);
             _output.write_string((java.lang.String)referencia);
             _output.write_long((int)promocao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaConcessaoPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.executaConcessaoPulaPula(tipoExecucao, referencia, promocao);
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
   *   short sumarizaRecargasAssinantes (in string referencia);
   * </pre>
   */
  public short sumarizaRecargasAssinantes (java.lang.String referencia) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("sumarizaRecargasAssinantes", true);
        _output.write_string((java.lang.String)referencia);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("sumarizaRecargasAssinantes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)referencia);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("sumarizaRecargasAssinantes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.sumarizaRecargasAssinantes(referencia);
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
   *   void executaProcessoBatch (in long idProcessoBatch,
                             in com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatch parametros)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void executaProcessoBatch (int idProcessoBatch, 
                                    java.lang.String[] parametros) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("executaProcessoBatch", true);
        _output.write_long((int)idProcessoBatch);
        com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.write(_output, parametros);
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
    com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaProcessoBatch", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.processosBatch.orb.processosBatchOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_long((int)idProcessoBatch);
             com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.write(_output, parametros);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaProcessoBatch",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.executaProcessoBatch(idProcessoBatch, parametros);
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

}
