package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::retornoGenerico
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/retornoGenerico:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct retornoGenerico {
  ...
};
 * </pre>
 */
public final class retornoGenericoHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico value;

public retornoGenericoHolder () {
}

public retornoGenericoHolder (final com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHelper.type();
}
}
