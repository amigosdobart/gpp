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
public final class gerenteGPPHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP value;

public gerenteGPPHolder () {
}

public gerenteGPPHolder (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper.type();
}
}
