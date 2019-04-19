package com.brt.gpp.componentes.recarga.orb.recargaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Recarga.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::recarga::orb::recarga::valoresRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/recarga/orb/recarga/valoresRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct valoresRecarga {
  ...
};
 * </pre>
 */
public final class valoresRecargaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga value;

public valoresRecargaHolder () {
}

public valoresRecargaHolder (final com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHelper.type();
}
}
