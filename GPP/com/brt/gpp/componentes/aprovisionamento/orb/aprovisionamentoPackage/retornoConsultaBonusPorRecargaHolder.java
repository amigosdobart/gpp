package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::retornoConsultaBonusPorRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/retornoConsultaBonusPorRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct retornoConsultaBonusPorRecarga {
  ...
};
 * </pre>
 */
public final class retornoConsultaBonusPorRecargaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga value;

public retornoConsultaBonusPorRecargaHolder () {
}

public retornoConsultaBonusPorRecargaHolder (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHelper.type();
}
}
