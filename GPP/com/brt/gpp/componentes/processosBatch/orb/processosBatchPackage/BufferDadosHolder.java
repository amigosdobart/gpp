package com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch::BufferDados
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/BufferDados:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltoctet&gt BufferDados;
 * </pre>
 */
public final class BufferDadosHolder implements org.omg.CORBA.portable.Streamable {
public byte[] value;

public BufferDadosHolder () {
}

public BufferDadosHolder (final byte[] _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.type();
}
}
