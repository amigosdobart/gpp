package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::dadosBonusPorRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/dadosBonusPorRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct dadosBonusPorRecarga {
  ...
};
 * </pre>
 */
public final class dadosBonusPorRecargaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga value;

public dadosBonusPorRecargaHolder () {
}

public dadosBonusPorRecargaHolder (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.type();
}
}
