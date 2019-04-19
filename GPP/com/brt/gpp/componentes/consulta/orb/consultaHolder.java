package com.brt.gpp.componentes.consulta.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface consulta {
  ...
};
 * </pre>
 */
public final class consultaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.consulta.orb.consulta value;

public consultaHolder () {
}

public consultaHolder (final com.brt.gpp.componentes.consulta.orb.consulta _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.consulta.orb.consultaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.consulta.orb.consultaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.consulta.orb.consultaHelper.type();
}
}
