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
public class _aprovisionamentoStub extends com.inprise.vbroker.CORBA.portable.ObjectImpl implements com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento {
  final public static java.lang.Class _opsClass = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations.class;

  public java.lang.String[] _ids () {
    return __ids;
  }

  private static java.lang.String[] __ids = {
    "IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0"
  };

  /**
   * <pre>
   *   short ativaAssinante (in string MSISDN, in string IMSI,
                        in string planoPreco, in double creditoInicial,
                        in short idioma, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short ativaAssinante (java.lang.String MSISDN, 
                               java.lang.String IMSI, 
                               java.lang.String planoPreco, 
                               double creditoInicial, 
                               short idioma, 
                               java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                  com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                  com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("ativaAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)IMSI);
        _output.write_string((java.lang.String)planoPreco);
        _output.write_double((double)creditoInicial);
        _output.write_short((short)idioma);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("ativaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)IMSI);
             _output.write_string((java.lang.String)planoPreco);
             _output.write_double((double)creditoInicial);
             _output.write_short((short)idioma);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("ativaAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.ativaAssinante(MSISDN, IMSI, planoPreco, creditoInicial, 
                                        idioma, operador);
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
   *   com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante desativaAssinante (in string MSISDN,
                                                                                                                      in string motivoDesativacao,
                                                                                                                      in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante desativaAssinante (java.lang.String MSISDN, 
                                                                                                                             java.lang.String motivoDesativacao, 
                                                                                                                             java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                                                                                                com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                                                                                                com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante _result;
      try {
        _output = this._request("desativaAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)motivoDesativacao);
        _output.write_string((java.lang.String)operador);
        _input = this._invoke(_output);
        _result = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.read(_input);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("desativaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)motivoDesativacao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("desativaAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
      _input = _result_output.create_input_stream();
      _result = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.read(_input);
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
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante _ret = _self.desativaAssinante(MSISDN, 
                                                                                                                                      motivoDesativacao, 
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
   *   string desativarAssinanteXML (in string MSISDN, in string motivoDesativacao,
                                in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String desativarAssinanteXML (java.lang.String MSISDN, 
                                                 java.lang.String motivoDesativacao, 
                                                 java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                                    com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                                    com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("desativarAssinanteXML", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)motivoDesativacao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("desativarAssinanteXML", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)motivoDesativacao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("desativarAssinanteXML",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.desativarAssinanteXML(MSISDN, motivoDesativacao, 
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
   *   short alterarStatusAssinante (in string msisdn, in short status,
                                in string dataExpiracao, in string operador);
   * </pre>
   */
  public short alterarStatusAssinante (java.lang.String msisdn, 
                                       short status, 
                                       java.lang.String dataExpiracao, 
                                       java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("alterarStatusAssinante", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_short((short)status);
        _output.write_string((java.lang.String)dataExpiracao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("alterarStatusAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_short((short)status);
             _output.write_string((java.lang.String)dataExpiracao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("alterarStatusAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.alterarStatusAssinante(msisdn, status, dataExpiracao, 
                                                operador);
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
   *   short alterarStatusPeriodico (in string msisdn, in short status,
                                in string dataExpiracao, in string operador);
   * </pre>
   */
  public short alterarStatusPeriodico (java.lang.String msisdn, 
                                       short status, 
                                       java.lang.String dataExpiracao, 
                                       java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("alterarStatusPeriodico", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_short((short)status);
        _output.write_string((java.lang.String)dataExpiracao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("alterarStatusPeriodico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_short((short)status);
             _output.write_string((java.lang.String)dataExpiracao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("alterarStatusPeriodico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.alterarStatusPeriodico(msisdn, status, dataExpiracao, 
                                                operador);
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
   *   short bloqueiaAssinante (in string MSISDN, in string idBloqueio,
                           in double tarifa, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short bloqueiaAssinante (java.lang.String MSISDN, 
                                  java.lang.String idBloqueio, 
                                  double tarifa, 
                                  java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                     com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                     com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("bloqueiaAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)idBloqueio);
        _output.write_double((double)tarifa);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bloqueiaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)idBloqueio);
             _output.write_double((double)tarifa);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("bloqueiaAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.bloqueiaAssinante(MSISDN, idBloqueio, tarifa, operador);
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
   *   short desbloqueiaAssinante (in string MSISDN, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short desbloqueiaAssinante (java.lang.String MSISDN, 
                                     java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("desbloqueiaAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("desbloqueiaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("desbloqueiaAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.desbloqueiaAssinante(MSISDN, operador);
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
   *   short trocaMSISDNAssinante (in string antigoMSISDN, in string novoMSISDN,
                              in string idTarifa, in double valorTarifa,
                              in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaMSISDNAssinante (java.lang.String antigoMSISDN, 
                                     java.lang.String novoMSISDN, 
                                     java.lang.String idTarifa, 
                                     double valorTarifa, 
                                     java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                        com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("trocaMSISDNAssinante", true);
        _output.write_string((java.lang.String)antigoMSISDN);
        _output.write_string((java.lang.String)novoMSISDN);
        _output.write_string((java.lang.String)idTarifa);
        _output.write_double((double)valorTarifa);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaMSISDNAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)antigoMSISDN);
             _output.write_string((java.lang.String)novoMSISDN);
             _output.write_string((java.lang.String)idTarifa);
             _output.write_double((double)valorTarifa);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaMSISDNAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.trocaMSISDNAssinante(antigoMSISDN, novoMSISDN, idTarifa, 
                                              valorTarifa, operador);
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
   *   short trocaPlanoAssinante (in string MSISDN, in string novoPlano,
                             in double valorMudanca, in string operador,
                             in double valorFranquia)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaPlanoAssinante (java.lang.String MSISDN, 
                                    java.lang.String novoPlano, 
                                    double valorMudanca, 
                                    java.lang.String operador, 
                                    double valorFranquia) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                  com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                  com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("trocaPlanoAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)novoPlano);
        _output.write_double((double)valorMudanca);
        _output.write_string((java.lang.String)operador);
        _output.write_double((double)valorFranquia);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaPlanoAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)novoPlano);
             _output.write_double((double)valorMudanca);
             _output.write_string((java.lang.String)operador);
             _output.write_double((double)valorFranquia);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaPlanoAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.trocaPlanoAssinante(MSISDN, novoPlano, valorMudanca, operador, 
                                             valorFranquia);
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
   *   short trocaSimCardAssinante (in string MSISDN, in string novoIMSI,
                               in double valorMudanca, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short trocaSimCardAssinante (java.lang.String MSISDN, 
                                      java.lang.String novoIMSI, 
                                      double valorMudanca, 
                                      java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                         com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                         com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("trocaSimCardAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)novoIMSI);
        _output.write_double((double)valorMudanca);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaSimCardAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)novoIMSI);
             _output.write_double((double)valorMudanca);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaSimCardAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.trocaSimCardAssinante(MSISDN, novoIMSI, valorMudanca, 
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
   *   short atualizaFriendsFamilyAssinante (in string MSISDN,
                                        in string listaMSISDN,
                                        in string operador,
                                        in string codigoServico)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short atualizaFriendsFamilyAssinante (java.lang.String MSISDN, 
                                               java.lang.String listaMSISDN, 
                                               java.lang.String operador, 
                                               java.lang.String codigoServico) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaFriendsFamilyAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)listaMSISDN);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)codigoServico);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaFriendsFamilyAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)listaMSISDN);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)codigoServico);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaFriendsFamilyAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaFriendsFamilyAssinante(MSISDN, listaMSISDN, operador, 
                                                        codigoServico);
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
   *   string trocaSenha (in string GPPTrocaSenha)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException,
            com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException);
   * </pre>
   */
  public java.lang.String trocaSenha (java.lang.String GPPTrocaSenha) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                              com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                              com.brt.gpp.comum.gppExceptions.GPPTecnomenException, 
                                                                              com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("trocaSenha", true);
        _output.write_string((java.lang.String)GPPTrocaSenha);
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
        if (_exception_id.equals(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id())) {
          throw           com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.read(_exception.getInputStream());
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaSenha", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)GPPTrocaSenha);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaSenha",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.trocaSenha(GPPTrocaSenha);
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
    } catch (com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException x_4) {
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
   *   short resetSenha (in string MSISDN, in string novaSenha)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short resetSenha (java.lang.String MSISDN, 
                           java.lang.String novaSenha) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                               com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                               com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("resetSenha", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)novaSenha);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("resetSenha", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)novaSenha);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("resetSenha",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.resetSenha(MSISDN, novaSenha);
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
   *   string consultaAssinante (in string MSISDN)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public java.lang.String consultaAssinante (java.lang.String MSISDN) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                              com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                              com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("consultaAssinante", true);
        _output.write_string((java.lang.String)MSISDN);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("consultaAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
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
      java.lang.String _ret = _self.consultaAssinante(MSISDN);
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
   *   short ativacaoCancelamentoServico (in string MSISDN, in string idServico,
                                     in short acao)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException,
            com.brt.gpp.comum.gppExceptions.GPPCorbaException,
            com.brt.gpp.comum.gppExceptions.GPPTecnomenException);
   * </pre>
   */
  public short ativacaoCancelamentoServico (java.lang.String MSISDN, 
                                            java.lang.String idServico, 
                                            short acao) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException, 
                                                                com.brt.gpp.comum.gppExceptions.GPPCorbaException, 
                                                                com.brt.gpp.comum.gppExceptions.GPPTecnomenException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("ativacaoCancelamentoServico", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)idServico);
        _output.write_short((short)acao);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("ativacaoCancelamentoServico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)idServico);
             _output.write_short((short)acao);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("ativacaoCancelamentoServico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.ativacaoCancelamentoServico(MSISDN, idServico, acao);
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
   *   long long bloquearServicos (in string msisdn)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long bloquearServicos (java.lang.String msisdn) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
        _output = this._request("bloquearServicos", true);
        _output.write_string((java.lang.String)msisdn);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("bloquearServicos", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("bloquearServicos",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      long _ret = _self.bloquearServicos(msisdn);
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
   *   long long desativarHotLine (in string msisdn, in string categoria)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long desativarHotLine (java.lang.String msisdn, 
                                java.lang.String categoria) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
        _output = this._request("desativarHotLine", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)categoria);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("desativarHotLine", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)categoria);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("desativarHotLine",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      long _ret = _self.desativarHotLine(msisdn, categoria);
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
   *   long long desativarHotLineURA (in string msisdn, in string categoria)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public long desativarHotLineURA (java.lang.String msisdn, 
                                   java.lang.String categoria) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
        _output = this._request("desativarHotLineURA", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)categoria);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("desativarHotLineURA", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      long _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)categoria);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("desativarHotLineURA",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      long _ret = _self.desativarHotLineURA(msisdn, categoria);
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
   *   void confirmaBloqueioDesbloqueioServicos (in string xmlAprovisionamento)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void confirmaBloqueioDesbloqueioServicos (java.lang.String xmlAprovisionamento) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("confirmaBloqueioDesbloqueioServicos", true);
        _output.write_string((java.lang.String)xmlAprovisionamento);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("confirmaBloqueioDesbloqueioServicos", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)xmlAprovisionamento);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("confirmaBloqueioDesbloqueioServicos",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.confirmaBloqueioDesbloqueioServicos(xmlAprovisionamento);
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
   *   void enviarSMS (in string MSISDN, in string mensagem)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviarSMS (java.lang.String MSISDN, 
                         java.lang.String mensagem) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("enviarSMS", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)mensagem);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)mensagem);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.enviarSMS(MSISDN, mensagem);
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
   *   void enviarSMSMulti (in string MSISDN, in string mensagem,
                       in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public void enviarSMSMulti (java.lang.String MSISDN, 
                              java.lang.String mensagem, 
                              java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      try {
        _output = this._request("enviarSMSMulti", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)mensagem);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarSMSMulti", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)mensagem);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarSMSMulti",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      _self.enviarSMSMulti(MSISDN, mensagem, operador);
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
   *   boolean gravarMensagemSMS (in string msisdnOrigem, in string msisdnDestino,
                             in string mensagem, in long prioridade,
                             in string tipo)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public boolean gravarMensagemSMS (java.lang.String msisdnOrigem, 
                                    java.lang.String msisdnDestino, 
                                    java.lang.String mensagem, 
                                    int prioridade, 
                                    java.lang.String tipo) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
        _output = this._request("gravarMensagemSMS", true);
        _output.write_string((java.lang.String)msisdnOrigem);
        _output.write_string((java.lang.String)msisdnDestino);
        _output.write_string((java.lang.String)mensagem);
        _output.write_long((int)prioridade);
        _output.write_string((java.lang.String)tipo);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("gravarMensagemSMS", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      boolean _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdnOrigem);
             _output.write_string((java.lang.String)msisdnDestino);
             _output.write_string((java.lang.String)mensagem);
             _output.write_long((int)prioridade);
             _output.write_string((java.lang.String)tipo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("gravarMensagemSMS",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      boolean _ret = _self.gravarMensagemSMS(msisdnOrigem, msisdnDestino, mensagem, 
                                             prioridade, tipo);
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
   *   short inserePulaPula (in string msisdn, in string promocao,
                        in string operador);
   * </pre>
   */
  public short inserePulaPula (java.lang.String msisdn, 
                               java.lang.String promocao, 
                               java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("inserePulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)promocao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("inserePulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)promocao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("inserePulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.inserePulaPula(msisdn, promocao, operador);
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
   *   short retiraPulaPula (in string msisdn, in string operador);
   * </pre>
   */
  public short retiraPulaPula (java.lang.String msisdn, 
                               java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("retiraPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("retiraPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("retiraPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.retiraPulaPula(msisdn, operador);
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
   *   short trocaPulaPula (in string msisdn, in string promocaoNova,
                       in string operador);
   * </pre>
   */
  public short trocaPulaPula (java.lang.String msisdn, 
                              java.lang.String promocaoNova, 
                              java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("trocaPulaPula", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)promocaoNova);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)promocaoNova);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.trocaPulaPula(msisdn, promocaoNova, operador);
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
   *   short trocaPulaPulaPPP (in string msisdn, in long promocaoNova,
                          in string operador, in long motivo,
                          in long tipoDocumento, in string numDocumento);
   * </pre>
   */
  public short trocaPulaPulaPPP (java.lang.String msisdn, 
                                 int promocaoNova, 
                                 java.lang.String operador, 
                                 int motivo, 
                                 int tipoDocumento, 
                                 java.lang.String numDocumento) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("trocaPulaPulaPPP", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_long((int)promocaoNova);
        _output.write_string((java.lang.String)operador);
        _output.write_long((int)motivo);
        _output.write_long((int)tipoDocumento);
        _output.write_string((java.lang.String)numDocumento);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("trocaPulaPulaPPP", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_long((int)promocaoNova);
             _output.write_string((java.lang.String)operador);
             _output.write_long((int)motivo);
             _output.write_long((int)tipoDocumento);
             _output.write_string((java.lang.String)numDocumento);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("trocaPulaPulaPPP",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.trocaPulaPulaPPP(msisdn, promocaoNova, operador, motivo, 
                                          tipoDocumento, numDocumento);
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
   *   short executaPulaPula (in string tipoExecucao, in string msisdn,
                         in string dataReferencia, in string operador,
                         in long motivo);
   * </pre>
   */
  public short executaPulaPula (java.lang.String tipoExecucao, 
                                java.lang.String msisdn, 
                                java.lang.String dataReferencia, 
                                java.lang.String operador, 
                                int motivo) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("executaPulaPula", true);
        _output.write_string((java.lang.String)tipoExecucao);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)dataReferencia);
        _output.write_string((java.lang.String)operador);
        _output.write_long((int)motivo);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("executaPulaPula", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)tipoExecucao);
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)dataReferencia);
             _output.write_string((java.lang.String)operador);
             _output.write_long((int)motivo);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("executaPulaPula",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.executaPulaPula(tipoExecucao, msisdn, dataReferencia, 
                                         operador, motivo);
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
   *   short zerarSaldos (in string msisdn, in string operador,
                     in string tipoTransacao, in long codSaldosZerados);
   * </pre>
   */
  public short zerarSaldos (java.lang.String msisdn, 
                            java.lang.String operador, 
                            java.lang.String tipoTransacao, 
                            int codSaldosZerados) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("zerarSaldos", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)tipoTransacao);
        _output.write_long((int)codSaldosZerados);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("zerarSaldos", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)tipoTransacao);
             _output.write_long((int)codSaldosZerados);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("zerarSaldos",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.zerarSaldos(msisdn, operador, tipoTransacao, codSaldosZerados);
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
   *   short ativaAssinanteComStatus (in string msisdn, in string imsi,
                                 in string planoPreco,
                                 in double creditoInicial, in short idioma,
                                 in string operador, in short status);
   * </pre>
   */
  public short ativaAssinanteComStatus (java.lang.String msisdn, 
                                        java.lang.String imsi, 
                                        java.lang.String planoPreco, 
                                        double creditoInicial, 
                                        short idioma, 
                                        java.lang.String operador, 
                                        short status) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("ativaAssinanteComStatus", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)imsi);
        _output.write_string((java.lang.String)planoPreco);
        _output.write_double((double)creditoInicial);
        _output.write_short((short)idioma);
        _output.write_string((java.lang.String)operador);
        _output.write_short((short)status);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("ativaAssinanteComStatus", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)imsi);
             _output.write_string((java.lang.String)planoPreco);
             _output.write_double((double)creditoInicial);
             _output.write_short((short)idioma);
             _output.write_string((java.lang.String)operador);
             _output.write_short((short)status);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("ativaAssinanteComStatus",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.ativaAssinanteComStatus(msisdn, imsi, planoPreco, creditoInicial, 
                                                 idioma, operador, status);
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
   *   string cobrarServico (in string msisdn, in string codigoServico,
                        in string operacao, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String cobrarServico (java.lang.String msisdn, 
                                         java.lang.String codigoServico, 
                                         java.lang.String operacao, 
                                         java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("cobrarServico", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)codigoServico);
        _output.write_string((java.lang.String)operacao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cobrarServico", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)codigoServico);
             _output.write_string((java.lang.String)operacao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("cobrarServico",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.cobrarServico(msisdn, codigoServico, operacao, 
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
    } catch (java.lang.RuntimeException re) {
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
   *   string cadastrarBumerangue (in string msisdn, in string codigoServico,
                              in string operacao, in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String cadastrarBumerangue (java.lang.String msisdn, 
                                               java.lang.String codigoServico, 
                                               java.lang.String operacao, 
                                               java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("cadastrarBumerangue", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)codigoServico);
        _output.write_string((java.lang.String)operacao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("cadastrarBumerangue", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)codigoServico);
             _output.write_string((java.lang.String)operacao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("cadastrarBumerangue",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.cadastrarBumerangue(msisdn, codigoServico, 
                                                        operacao, operador);
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
   *   string atualizarAmigosTodaHora (in string msisdn, in string listaMsisdn,
                                  in string codigoServico, in string operacao,
                                  in string operador)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public java.lang.String atualizarAmigosTodaHora (java.lang.String msisdn, 
                                                   java.lang.String listaMsisdn, 
                                                   java.lang.String codigoServico, 
                                                   java.lang.String operacao, 
                                                   java.lang.String operador) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
        _output = this._request("atualizarAmigosTodaHora", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)listaMsisdn);
        _output.write_string((java.lang.String)codigoServico);
        _output.write_string((java.lang.String)operacao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizarAmigosTodaHora", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      java.lang.String _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)listaMsisdn);
             _output.write_string((java.lang.String)codigoServico);
             _output.write_string((java.lang.String)operacao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizarAmigosTodaHora",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      java.lang.String _ret = _self.atualizarAmigosTodaHora(msisdn, listaMsisdn, 
                                                            codigoServico, operacao, 
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
    } catch (java.lang.RuntimeException re) {
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
   *   short reativarAssinante (in string msisdnNovo, in string msisdnAntigo,
                           in string operador);
   * </pre>
   */
  public short reativarAssinante (java.lang.String msisdnNovo, 
                                  java.lang.String msisdnAntigo, 
                                  java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("reativarAssinante", true);
        _output.write_string((java.lang.String)msisdnNovo);
        _output.write_string((java.lang.String)msisdnAntigo);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("reativarAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdnNovo);
             _output.write_string((java.lang.String)msisdnAntigo);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("reativarAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.reativarAssinante(msisdnNovo, msisdnAntigo, operador);
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
   *   short aprovarLote (in string lote)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short aprovarLote (java.lang.String lote) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("aprovarLote", true);
        _output.write_string((java.lang.String)lote);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("aprovarLote", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)lote);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("aprovarLote",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.aprovarLote(lote);
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
   *   short rejeitarLote (in string lote)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short rejeitarLote (java.lang.String lote) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("rejeitarLote", true);
        _output.write_string((java.lang.String)lote);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("rejeitarLote", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)lote);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("rejeitarLote",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.rejeitarLote(lote);
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
   *   short inserirPromocaoFaleGratisANoite (in string msisdn, in string promocao,
                                         in string operador);
   * </pre>
   */
  public short inserirPromocaoFaleGratisANoite (java.lang.String msisdn, 
                                                java.lang.String promocao, 
                                                java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("inserirPromocaoFaleGratisANoite", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)promocao);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("inserirPromocaoFaleGratisANoite", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)promocao);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("inserirPromocaoFaleGratisANoite",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.inserirPromocaoFaleGratisANoite(msisdn, promocao, operador);
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
   *   short retirarPromocaoFaleGratisANoite (in string msisdn, in string operador);
   * </pre>
   */
  public short retirarPromocaoFaleGratisANoite (java.lang.String msisdn, 
                                                java.lang.String operador) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("retirarPromocaoFaleGratisANoite", true);
        _output.write_string((java.lang.String)msisdn);
        _output.write_string((java.lang.String)operador);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("retirarPromocaoFaleGratisANoite", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)msisdn);
             _output.write_string((java.lang.String)operador);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("retirarPromocaoFaleGratisANoite",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.retirarPromocaoFaleGratisANoite(msisdn, operador);
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
   *   short enviarRequisicaoTangram (in string xml)
    raises (com.brt.gpp.comum.gppExceptions.GPPInternalErrorException);
   * </pre>
   */
  public short enviarRequisicaoTangram (java.lang.String xml) throws  com.brt.gpp.comum.gppExceptions.GPPInternalErrorException {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("enviarRequisicaoTangram", true);
        _output.write_string((java.lang.String)xml);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("enviarRequisicaoTangram", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)xml);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("enviarRequisicaoTangram",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.enviarRequisicaoTangram(xml);
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
   *   short atualizaAmigosDeGraca (in string MSISDN, in string listaMSISDN,
                               in string operador, in string codigoServico);
   * </pre>
   */
  public short atualizaAmigosDeGraca (java.lang.String MSISDN, 
                                      java.lang.String listaMSISDN, 
                                      java.lang.String operador, 
                                      java.lang.String codigoServico) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("atualizaAmigosDeGraca", true);
        _output.write_string((java.lang.String)MSISDN);
        _output.write_string((java.lang.String)listaMSISDN);
        _output.write_string((java.lang.String)operador);
        _output.write_string((java.lang.String)codigoServico);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("atualizaAmigosDeGraca", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)MSISDN);
             _output.write_string((java.lang.String)listaMSISDN);
             _output.write_string((java.lang.String)operador);
             _output.write_string((java.lang.String)codigoServico);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("atualizaAmigosDeGraca",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.atualizaAmigosDeGraca(MSISDN, listaMSISDN, operador, codigoServico);
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
   *   short ativarAssinante (in string xml);
   * </pre>
   */
  public short ativarAssinante (java.lang.String xml) {

    while (true) {
    if (!_is_local()) {
      org.omg.CORBA.portable.OutputStream _output = null;
      org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
        _output = this._request("ativarAssinante", true);
        _output.write_string((java.lang.String)xml);
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
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations _self = null;
    final org.omg.CORBA.portable.ServantObject _so = _servant_preinvoke("ativarAssinante", _opsClass);
    if (_so == null) {
      continue;
    }
    try {
          _self = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoOperations)_so.servant;
        }catch (java.lang.ClassCastException ce) {
        org.omg.CORBA.portable.OutputStream _output = null;
        org.omg.CORBA.portable.InputStream  _input  = null;
      short _result;
      try {
      _output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_output = _orb().create_output_stream();
      final org.omg.CORBA.portable.OutputStream _response_ex_output = _orb().create_output_stream();
             _output.write_string((java.lang.String)xml);
      org.omg.CORBA.portable.OutputStream _result_output = ((org.omg.CORBA.portable.InvokeHandler)_so.servant)._invoke("ativarAssinante",_output.create_input_stream(),com.inprise.vbroker.orb.VBResponseHandler.getResponseHandler(_response_output,_response_ex_output));
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
      short _ret = _self.ativarAssinante(xml);
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
