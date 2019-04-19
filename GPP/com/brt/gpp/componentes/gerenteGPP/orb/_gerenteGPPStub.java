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
public class _gerenteGPPStub extends com.inprise.vbroker.CORBA.portable.ObjectImpl implements com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP {
  final public static java.lang.Class _opsClass = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations.class;

  public java.lang.String[] _ids () {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP:1.0"
  };

  /**
   * <pre>
   *   short getNumerodeConexoes (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumerodeConexoes (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumerodeConexoes", true);
        _output.write_short((short)tipoConexao);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumerodeConexoes", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)tipoConexao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumerodeConexoes",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumerodeConexoes(tipoConexao);
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
   *   boolean criaConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean criaConexao (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("criaConexao", true);
        _output.write_short((short)tipoConexao);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("criaConexao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)tipoConexao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("criaConexao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.criaConexao(tipoConexao);
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
   *   boolean removeConexao (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeConexao (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("removeConexao", true);
        _output.write_short((short)tipoConexao);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("removeConexao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)tipoConexao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("removeConexao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.removeConexao(tipoConexao);
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
   *   string exibeListaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaPlanoPreco () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaPlanoPreco", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaPlanoPreco", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaPlanoPreco",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaPlanoPreco();
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
   *   boolean atualizaListaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPlanoPreco () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaPlanoPreco", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaPlanoPreco", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaPlanoPreco",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaPlanoPreco();
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
   *   string exibeListaStatusAssinante ();
   * </pre>
   */
  public java.lang.String exibeListaStatusAssinante () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaStatusAssinante", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaStatusAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaStatusAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaStatusAssinante();
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
   *   boolean atualizaListaStatusAssinante ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusAssinante () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaStatusAssinante", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaStatusAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaStatusAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaStatusAssinante();
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
   *   string exibeListaStatusServico ();
   * </pre>
   */
  public java.lang.String exibeListaStatusServico () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaStatusServico", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaStatusServico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaStatusServico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaStatusServico();
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
   *   boolean atualizaListaStatusServico ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaStatusServico () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaStatusServico", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaStatusServico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaStatusServico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaStatusServico();
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
   *   string exibeListaSistemaOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaSistemaOrigem () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaSistemaOrigem", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaSistemaOrigem", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaSistemaOrigem",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaSistemaOrigem();
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
   *   boolean atualizaListaSistemaOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaSistemaOrigem () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaSistemaOrigem", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaSistemaOrigem", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaSistemaOrigem",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaSistemaOrigem();
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
   *   string exibeListaTarifaTrocaMSISDN ();
   * </pre>
   */
  public java.lang.String exibeListaTarifaTrocaMSISDN () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaTarifaTrocaMSISDN", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaTarifaTrocaMSISDN", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaTarifaTrocaMSISDN",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaTarifaTrocaMSISDN();
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
   *   boolean atualizaListaTarifaTrocaMSISDN ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaTarifaTrocaMSISDN () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaTarifaTrocaMSISDN", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaTarifaTrocaMSISDN", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaTarifaTrocaMSISDN",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaTarifaTrocaMSISDN();
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
   *   string exibeListaValoresRecarga ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecarga () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaValoresRecarga", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaValoresRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaValoresRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaValoresRecarga();
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
   *   boolean atualizaListaValoresRecarga ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecarga () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaValoresRecarga", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaValoresRecarga", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaValoresRecarga",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaValoresRecarga();
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
   *   string exibeListaValoresRecargaPlanoPreco ();
   * </pre>
   */
  public java.lang.String exibeListaValoresRecargaPlanoPreco () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaValoresRecargaPlanoPreco", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaValoresRecargaPlanoPreco", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaValoresRecargaPlanoPreco",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaValoresRecargaPlanoPreco();
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
   *   boolean atualizaListaValoresRecargaPlanoPreco ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaValoresRecargaPlanoPreco () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaValoresRecargaPlanoPreco", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaValoresRecargaPlanoPreco", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaValoresRecargaPlanoPreco",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaValoresRecargaPlanoPreco();
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
   *   string exibeListaBonusPulaPula ();
   * </pre>
   */
  public java.lang.String exibeListaBonusPulaPula () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaBonusPulaPula", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaBonusPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaBonusPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaBonusPulaPula();
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
   *   boolean atualizaListaBonusPulaPula ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaBonusPulaPula () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaBonusPulaPula", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaBonusPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaBonusPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaBonusPulaPula();
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
   *   string exibeListaPromocao ();
   * </pre>
   */
  public java.lang.String exibeListaPromocao () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaPromocao", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaPromocao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaPromocao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaPromocao();
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
   *   boolean atualizaListaPromocao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocao () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaPromocao", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaPromocao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaPromocao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaPromocao();
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
   *   boolean atualizaListaPromocaoDiaExecucao ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaPromocaoDiaExecucao () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaPromocaoDiaExecucao", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaPromocaoDiaExecucao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaPromocaoDiaExecucao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaPromocaoDiaExecucao();
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
   *   string exibeListaRecOrigem ();
   * </pre>
   */
  public java.lang.String exibeListaRecOrigem () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaRecOrigem", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaRecOrigem", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaRecOrigem",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaRecOrigem();
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
   *   boolean atualizaListaRecOrigem ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaRecOrigem () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaRecOrigem", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaRecOrigem", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaRecOrigem",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaRecOrigem();
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
   *   string exibeListaConfiguracaoGPP ();
   * </pre>
   */
  public java.lang.String exibeListaConfiguracaoGPP () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaConfiguracaoGPP", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaConfiguracaoGPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaConfiguracaoGPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaConfiguracaoGPP();
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
   *   boolean atualizaListaConfiguracaoGPP ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaConfiguracaoGPP () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaConfiguracaoGPP", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaConfiguracaoGPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaConfiguracaoGPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaConfiguracaoGPP();
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
   *   string exibeListaMotivosBloqueio ();
   * </pre>
   */
  public java.lang.String exibeListaMotivosBloqueio () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeListaMotivosBloqueio", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeListaMotivosBloqueio", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeListaMotivosBloqueio",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeListaMotivosBloqueio();
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
   *   boolean atualizaListaMotivosBloqueio ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaMotivosBloqueio () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaMotivosBloqueio", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaMotivosBloqueio", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaMotivosBloqueio",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaMotivosBloqueio();
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
   *   boolean atualizaListaModulacaoPlano ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaModulacaoPlano () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaModulacaoPlano", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaModulacaoPlano", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaModulacaoPlano",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaModulacaoPlano();
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
   *   boolean atualizaListaAssinantesNaoBonificaveis ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaAssinantesNaoBonificaveis () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaAssinantesNaoBonificaveis", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaAssinantesNaoBonificaveis", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaAssinantesNaoBonificaveis",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaAssinantesNaoBonificaveis();
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
   *   boolean atualizaListaFeriados ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean atualizaListaFeriados () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("atualizaListaFeriados", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaListaFeriados", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaListaFeriados",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.atualizaListaFeriados();
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
   *   boolean processaSMS (in boolean deveProcessar);
   * </pre>
   */
  public boolean processaSMS (boolean deveProcessar) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("processaSMS", true);
        _output.write_boolean((boolean)deveProcessar);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("processaSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_boolean((boolean)deveProcessar);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("processaSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.processaSMS(deveProcessar);
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
   *   boolean escreveDebug (in boolean escreveDebug);
   * </pre>
   */
  public boolean escreveDebug (boolean escreveDebug) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("escreveDebug", true);
        _output.write_boolean((boolean)escreveDebug);
        _input = this._invoke(_output);
        _result = _input.read_boolean();
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("escreveDebug", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_boolean((boolean)escreveDebug);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("escreveDebug",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.escreveDebug(escreveDebug);
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
   *   string ping ();
   * </pre>
   */
  public java.lang.String ping () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("ping", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("ping", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("ping",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.ping();
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
   *   void finalizaGPP ();
   * </pre>
   */
  public void finalizaGPP () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("finalizaGPP", true);
        _input = this._invoke(_output);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("finalizaGPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("finalizaGPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.finalizaGPP();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
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
   *   boolean getStatusProdutorSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusProdutorSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("getStatusProdutorSMS", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getStatusProdutorSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getStatusProdutorSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.getStatusProdutorSMS();
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
   *   boolean getStatusEscreveDebug ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean getStatusEscreveDebug () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("getStatusEscreveDebug", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getStatusEscreveDebug", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getStatusEscreveDebug",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.getStatusEscreveDebug();
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
   *   string getHistProcessosBatch (in short aIdProcBatch, in string aDatIni,
                                in string aDatFim, in string aIdtStatus)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String getHistProcessosBatch (short aIdProcBatch, 
                                                 java.lang.String aDatIni, 
                                                 java.lang.String aDatFim, 
                                                 java.lang.String aIdtStatus) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("getHistProcessosBatch", true);
        _output.write_short((short)aIdProcBatch);
        _output.write_string((java.lang.String)aDatIni);
        _output.write_string((java.lang.String)aDatFim);
        _output.write_string((java.lang.String)aIdtStatus);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getHistProcessosBatch", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)aIdProcBatch);
             _output.write_string((java.lang.String)aDatIni);
             _output.write_string((java.lang.String)aDatFim);
             _output.write_string((java.lang.String)aIdtStatus);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getHistProcessosBatch",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.getHistProcessosBatch(aIdProcBatch, aDatIni, 
                                                          aDatFim, aIdtStatus);
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
   *   string consultaProcessosBatch ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String consultaProcessosBatch () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaProcessosBatch", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaProcessosBatch", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("consultaProcessosBatch",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.consultaProcessosBatch();
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
   *   short getNumeroConexoesDisponiveis (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short getNumeroConexoesDisponiveis (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumeroConexoesDisponiveis", true);
        _output.write_short((short)tipoConexao);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumeroConexoesDisponiveis", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)tipoConexao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumeroConexoesDisponiveis",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumeroConexoesDisponiveis(tipoConexao);
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
   *   string exibirNumeroStatementsPorConexao ();
   * </pre>
   */
  public java.lang.String exibirNumeroStatementsPorConexao () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibirNumeroStatementsPorConexao", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibirNumeroStatementsPorConexao", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibirNumeroStatementsPorConexao",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibirNumeroStatementsPorConexao();
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
   *   com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexao getListaProcessosComConexoesEmUso (in short tipoConexao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] getListaProcessosComConexoesEmUso (short tipoConexao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _result;
      try {
        _output = this._request("getListaProcessosComConexoesEmUso", true);
        _output.write_short((short)tipoConexao);
        _input = this._invoke(_output);
        _result = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.read(_input);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getListaProcessosComConexoesEmUso", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_short((short)tipoConexao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getListaProcessosComConexoesEmUso",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.read(_input);
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
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _ret = _self.getListaProcessosComConexoesEmUso(tipoConexao);
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
   *   boolean adicionaThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean adicionaThreadEnvioSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("adicionaThreadEnvioSMS", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("adicionaThreadEnvioSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("adicionaThreadEnvioSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.adicionaThreadEnvioSMS();
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
   *   boolean removeThreadEnvioSMS ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean removeThreadEnvioSMS () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("removeThreadEnvioSMS", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("removeThreadEnvioSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("removeThreadEnvioSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.removeThreadEnvioSMS();
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
   *   void finalizaPoolEnvioSMS ();
   * </pre>
   */
  public void finalizaPoolEnvioSMS () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("finalizaPoolEnvioSMS", true);
        _input = this._invoke(_output);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("finalizaPoolEnvioSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("finalizaPoolEnvioSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.finalizaPoolEnvioSMS();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
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
   *   void inicializaPoolEnvioSMS ();
   * </pre>
   */
  public void inicializaPoolEnvioSMS () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("inicializaPoolEnvioSMS", true);
        _input = this._invoke(_output);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("inicializaPoolEnvioSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("inicializaPoolEnvioSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.inicializaPoolEnvioSMS();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
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
   *   short getNumeroThreadsEnvioSMS ();
   * </pre>
   */
  public short getNumeroThreadsEnvioSMS () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumeroThreadsEnvioSMS", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumeroThreadsEnvioSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumeroThreadsEnvioSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumeroThreadsEnvioSMS();
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
   *   short getNumThreadsImpCDRDadosVoz ();
   * </pre>
   */
  public short getNumThreadsImpCDRDadosVoz () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumThreadsImpCDRDadosVoz", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumThreadsImpCDRDadosVoz", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumThreadsImpCDRDadosVoz",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumThreadsImpCDRDadosVoz();
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
   *   short getNumThreadsImpCDREvtRec ();
   * </pre>
   */
  public short getNumThreadsImpCDREvtRec () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumThreadsImpCDREvtRec", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumThreadsImpCDREvtRec", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumThreadsImpCDREvtRec",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumThreadsImpCDREvtRec();
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
   *   short getNumArqPendentesDadosVoz ();
   * </pre>
   */
  public short getNumArqPendentesDadosVoz () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumArqPendentesDadosVoz", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumArqPendentesDadosVoz", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumArqPendentesDadosVoz",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumArqPendentesDadosVoz();
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
   *   short getNumArqPendentesEvtRec ();
   * </pre>
   */
  public short getNumArqPendentesEvtRec () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("getNumArqPendentesEvtRec", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("getNumArqPendentesEvtRec", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("getNumArqPendentesEvtRec",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.getNumArqPendentesEvtRec();
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
   *   void removeThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsDadosVoz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("removeThreadsDadosVoz", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("removeThreadsDadosVoz", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("removeThreadsDadosVoz",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.removeThreadsDadosVoz();
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
   *   void removeThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void removeThreadsEvtRec () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("removeThreadsEvtRec", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("removeThreadsEvtRec", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("removeThreadsEvtRec",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.removeThreadsEvtRec();
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
   *   void inicializaThreadsDadosVoz ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsDadosVoz () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("inicializaThreadsDadosVoz", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("inicializaThreadsDadosVoz", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("inicializaThreadsDadosVoz",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.inicializaThreadsDadosVoz();
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
   *   void inicializaThreadsEvtRec ()
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void inicializaThreadsEvtRec () throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("inicializaThreadsEvtRec", true);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("inicializaThreadsEvtRec", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("inicializaThreadsEvtRec",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.inicializaThreadsEvtRec();
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
   *   void reiniciaCacheFF ();
   * </pre>
   */
  public void reiniciaCacheFF () {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("reiniciaCacheFF", true);
        _input = this._invoke(_output);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reiniciaCacheFF", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("reiniciaCacheFF",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.reiniciaCacheFF();
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
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
   *   short atualizaMapeamentos (in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamentos (boolean limpar) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaMapeamentos", true);
        _output.write_boolean((boolean)limpar);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaMapeamentos", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_boolean((boolean)limpar);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaMapeamentos",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaMapeamentos(limpar);
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
   *   short atualizaMapeamento (in string nome, in boolean limpar);
   * </pre>
   */
  public short atualizaMapeamento (java.lang.String nome, 
                                   boolean limpar) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaMapeamento", true);
        _output.write_string((java.lang.String)nome);
        _output.write_boolean((boolean)limpar);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaMapeamento", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)nome);
             _output.write_boolean((boolean)limpar);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaMapeamento",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaMapeamento(nome, limpar);
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
   *   string exibeMapeamento (in string nome);
   * </pre>
   */
  public java.lang.String exibeMapeamento (java.lang.String nome) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("exibeMapeamento", true);
        _output.write_string((java.lang.String)nome);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("exibeMapeamento", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)nome);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("exibeMapeamento",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.exibeMapeamento(nome);
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
   *   void liberarConexoesEmUso (in long long idProcesso);
   * </pre>
   */
  public void liberarConexoesEmUso (long idProcesso) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("liberarConexoesEmUso", true);
        _output.write_longlong((long)idProcesso);
        _input = this._invoke(_output);
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
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("liberarConexoesEmUso", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_longlong((long)idProcesso);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("liberarConexoesEmUso",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.liberarConexoesEmUso(idProcesso);
      if (_so instanceof org.omg.CORBA.portable.ServantObjectExt) {
        ((org.omg.CORBA.portable.ServantObjectExt)_so).normalCompletion();
      }
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
