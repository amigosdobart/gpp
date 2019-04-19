package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::ListaDadosBonusPorRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/ListaDadosBonusPorRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltcom.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga&gt ListaDadosBonusPorRecarga;
 * </pre>
 */
public final class ListaDadosBonusPorRecargaHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] value;

public ListaDadosBonusPorRecargaHolder () {
}

public ListaDadosBonusPorRecargaHolder (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.type();
}
}
