package com.brt.gpp.componentes.recarga.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Recarga.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::recarga::orb::recarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/recarga/orb/recarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface recarga {
  ...
};
 * </pre>
 */
public final class recargaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.recarga.orb.recarga value;

public recargaHolder () {
}

public recargaHolder (final com.brt.gpp.componentes.recarga.orb.recarga _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.recarga.orb.recargaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.recarga.orb.recargaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.recarga.orb.recargaHelper.type();
}
}
