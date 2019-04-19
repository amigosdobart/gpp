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
public final class retornoDesativacaoAssinante implements org.omg.CORBA.portable.IDLEntity {
  
  public short codigoRetorno;
  
  public java.lang.String somaSaldos;

  public retornoDesativacaoAssinante () {
    somaSaldos = "";
  }

  public retornoDesativacaoAssinante (final short codigoRetorno, 
                                      final java.lang.String somaSaldos) {
    this.codigoRetorno = codigoRetorno;
    this.somaSaldos = somaSaldos;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante {");
    _ret.append("\n");
    _ret.append("short codigoRetorno=");
    _ret.append(codigoRetorno);
    _ret.append(",\n");
    _ret.append("java.lang.String somaSaldos=");
    _ret.append(somaSaldos != null?'\"' + somaSaldos + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante) {
      final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante obj = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante)o;
      boolean res = true;
      do {
        res = this.codigoRetorno == obj.codigoRetorno;
        if (!res) break;
        res = this.somaSaldos == obj.somaSaldos ||
         (this.somaSaldos != null && obj.somaSaldos != null && this.somaSaldos.equals(obj.somaSaldos));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
