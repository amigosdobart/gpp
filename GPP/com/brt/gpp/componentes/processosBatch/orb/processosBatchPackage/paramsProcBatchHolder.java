package com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch::paramsProcBatch
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/paramsProcBatch:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltstring&gt paramsProcBatch;
 * </pre>
 */
public final class paramsProcBatchHolder implements org.omg.CORBA.portable.Streamable {
public java.lang.String[] value;

public paramsProcBatchHolder () {
}

public paramsProcBatchHolder (final java.lang.String[] _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.type();
}
}
