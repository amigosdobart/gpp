package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::retornoDesativacaoAssinante
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/retornoDesativacaoAssinante:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct retornoDesativacaoAssinante {
  ...
};
 * </pre>
 */
public final class retornoDesativacaoAssinanteHolder implements org.omg.CORBA.portable.Streamable {
public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante value;

public retornoDesativacaoAssinanteHolder () {
}

public retornoDesativacaoAssinanteHolder (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante _vis_value) {
  this.value = _vis_value;
}

public void _read (final org.omg.CORBA.portable.InputStream input) {
  value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.read(input);
}

public void _write (final org.omg.CORBA.portable.OutputStream output) {
  com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.write(output, value);
}

public org.omg.CORBA.TypeCode _type () {
  return com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinanteHelper.type();
}
}
