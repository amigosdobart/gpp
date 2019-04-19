package com.brt.gpp.comum.gppExceptions;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "gppexceptions.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::comum::gppExceptions::GPPInternalErrorException
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/comum/gppExceptions/GPPInternalErrorException:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * exception GPPInternalErrorException {
  ...
};
 * </pre>
 */
public final class GPPInternalErrorExceptionHelper {
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.comum.gppExceptions.GPPInternalErrorException read (final org.omg.CORBA.portable.InputStream _input) {
    if (!_input.read_string().equals(id())) {
      throw new org.omg.CORBA.MARSHAL("Mismatched repository id");
    }
    final com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _result = new com.brt.gpp.comum.gppExceptions.GPPInternalErrorException();
    _result.codigoErro = _input.read_string();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _vis_value) {
    _output.write_string(id());
    _output.write_string((java.lang.String)_vis_value.codigoErro);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _vis_value) {
    any.insert_Streamable(new com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHolder(_vis_value));
  }

  public static com.brt.gpp.comum.gppExceptions.GPPInternalErrorException extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHolder _vis_holder = new com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[1];
          members[0] = new org.omg.CORBA.StructMember("codigoErro", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          _type = _orb().create_exception_tc(id(), "GPPInternalErrorException", members);
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/comum/gppExceptions/GPPInternalErrorException:1.0";
  }
}
