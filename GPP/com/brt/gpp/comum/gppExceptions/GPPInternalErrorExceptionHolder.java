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
public final class GPPInternalErrorExceptionHolder implements org.omg.CORBA.portable.Streamable {
  public com.brt.gpp.comum.gppExceptions.GPPInternalErrorException value;

  public GPPInternalErrorExceptionHolder () {
  }

  public GPPInternalErrorExceptionHolder (final com.brt.gpp.comum.gppExceptions.GPPInternalErrorException _vis_value) {
    this.value = _vis_value;
  }

  public void _read (final org.omg.CORBA.portable.InputStream input) {
    value = com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.read(input);
  }

  public void _write (final org.omg.CORBA.portable.OutputStream output) {
    com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.write(output, value);
  }

  public org.omg.CORBA.TypeCode _type () {
    return com.brt.gpp.comum.gppExceptions.GPPInternalErrorExceptionHelper.type();
  }
}
