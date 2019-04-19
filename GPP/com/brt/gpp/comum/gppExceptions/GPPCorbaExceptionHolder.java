package com.brt.gpp.comum.gppExceptions;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "gppexceptions.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::comum::gppExceptions::GPPCorbaException
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/comum/gppExceptions/GPPCorbaException:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * exception GPPCorbaException {
  ...
};
 * </pre>
 */
public final class GPPCorbaExceptionHolder implements org.omg.CORBA.portable.Streamable {
  public com.brt.gpp.comum.gppExceptions.GPPCorbaException value;

  public GPPCorbaExceptionHolder () {
  }

  public GPPCorbaExceptionHolder (final com.brt.gpp.comum.gppExceptions.GPPCorbaException _vis_value) {
    this.value = _vis_value;
  }

  public void _read (final org.omg.CORBA.portable.InputStream input) {
    value = com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.read(input);
  }

  public void _write (final org.omg.CORBA.portable.OutputStream output) {
    com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.write(output, value);
  }

  public org.omg.CORBA.TypeCode _type () {
    return com.brt.gpp.comum.gppExceptions.GPPCorbaExceptionHelper.type();
  }
}
