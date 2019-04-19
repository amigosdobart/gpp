package com.brt.gpp.comum.gppExceptions;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "gppexceptions.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::comum::gppExceptions::GPPBadXMLFormatException
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/comum/gppExceptions/GPPBadXMLFormatException:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * exception GPPBadXMLFormatException {
  ...
};
 * </pre>
 */
public final class GPPBadXMLFormatExceptionHolder implements org.omg.CORBA.portable.Streamable {
  public com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException value;

  public GPPBadXMLFormatExceptionHolder () {
  }

  public GPPBadXMLFormatExceptionHolder (final com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatException _vis_value) {
    this.value = _vis_value;
  }

  public void _read (final org.omg.CORBA.portable.InputStream input) {
    value = com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.read(input);
  }

  public void _write (final org.omg.CORBA.portable.OutputStream output) {
    com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.write(output, value);
  }

  public org.omg.CORBA.TypeCode _type () {
    return com.brt.gpp.comum.gppExceptions.GPPBadXMLFormatExceptionHelper.type();
  }
}
