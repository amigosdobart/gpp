package com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "GerenteGPP.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::gerenteGPP::orb::gerenteGPP::ListaProcessosConexao
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/ListaProcessosConexao:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltcom.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao&gt ListaProcessosConexao;
 * </pre>
 */
public final class ListaProcessosConexaoHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] value;

public ListaProcessosConexaoHolder () {
}

public ListaProcessosConexaoHolder (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.type();
}
}
