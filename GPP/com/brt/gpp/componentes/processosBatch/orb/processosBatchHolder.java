package com.brt.gpp.componentes.processosBatch.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface processosBatch {
  ...
};
 * </pre>
 */
public final class processosBatchHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.processosBatch.orb.processosBatch value;

public processosBatchHolder () {
}

public processosBatchHolder (final com.brt.gpp.componentes.processosBatch.orb.processosBatch _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper.type();
}
}
