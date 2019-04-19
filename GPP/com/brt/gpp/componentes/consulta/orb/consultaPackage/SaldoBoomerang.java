package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::SaldoBoomerang
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/SaldoBoomerang:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct SaldoBoomerang {
  ...
};
 * </pre>
 */
public final class SaldoBoomerang implements org.omg.CORBA.portable.IDLEntity {
  
  public java.lang.String msisdn;
  
  public double valorRecebido;
  
  public boolean fezRecarga;

  public SaldoBoomerang () {
    msisdn = "";
  }

  public SaldoBoomerang (final java.lang.String msisdn, 
                         final double valorRecebido, 
                         final boolean fezRecarga) {
    this.msisdn = msisdn;
    this.valorRecebido = valorRecebido;
    this.fezRecarga = fezRecarga;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang {");
    _ret.append("\n");
    _ret.append("java.lang.String msisdn=");
    _ret.append(msisdn != null?'\"' + msisdn + '\"':null);
    _ret.append(",\n");
    _ret.append("double valorRecebido=");
    _ret.append(valorRecebido);
    _ret.append(",\n");
    _ret.append("boolean fezRecarga=");
    _ret.append(fezRecarga);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang) {
      final com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang obj = (com.brt.gpp.componentes.consulta.orb.consultaPackage.SaldoBoomerang)o;
      boolean res = true;
      do {
        res = this.msisdn == obj.msisdn ||
         (this.msisdn != null && obj.msisdn != null && this.msisdn.equals(obj.msisdn));
        if (!res) break;
        res = this.valorRecebido == obj.valorRecebido;
        if (!res) break;
        res = this.fezRecarga == obj.fezRecarga;
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
