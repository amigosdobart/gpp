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
public final class aprovisionamentoHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento value;

public aprovisionamentoHolder () {
}

public aprovisionamentoHolder (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.type();
}
}
