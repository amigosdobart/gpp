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
public final class dadosBonusPorRecarga implements org.omg.CORBA.portable.IDLEntity {
  
  public short numeroRecargas;
  
  public short percentualBonus;

  public dadosBonusPorRecarga () {
  }

  public dadosBonusPorRecarga (final short numeroRecargas, 
                               final short percentualBonus) {
    this.numeroRecargas = numeroRecargas;
    this.percentualBonus = percentualBonus;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga {");
    _ret.append("\n");
    _ret.append("short numeroRecargas=");
    _ret.append(numeroRecargas);
    _ret.append(",\n");
    _ret.append("short percentualBonus=");
    _ret.append(percentualBonus);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga) {
      final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga obj = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga)o;
      boolean res = true;
      do {
        res = this.numeroRecargas == obj.numeroRecargas;
        if (!res) break;
        res = this.percentualBonus == obj.percentualBonus;
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
