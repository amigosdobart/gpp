package com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "GerenteGPP.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::gerenteGPP::orb::gerenteGPP::IdProcessoConexao
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/IdProcessoConexao:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct IdProcessoConexao {
  ...
};
 * </pre>
 */
public final class IdProcessoConexaoHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao value;

public IdProcessoConexaoHolder () {
}

public IdProcessoConexaoHolder (final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.type();
}
}
