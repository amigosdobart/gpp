package com.brt.gpp.componentes.recarga.orb.recargaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Recarga.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::recarga::orb::recarga::valoresRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/recarga/orb/recarga/valoresRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct valoresRecarga {
  ...
};
 * </pre>
 */
public final class valoresRecarga implements org.omg.CORBA.portable.IDLEntity {
  
  public int idValor;
  
  public double saldoPrincipal;
  
  public double saldoBonus;
  
  public double saldoSMS;
  
  public double saldoGPRS;
  
  public double valorBonusSMS;
  
  public double valorBonusGPRS;
  
  public double valorEfetivoPago;
  
  public int numDiasExpiracaoPrincipal;
  
  public int numDiasExpiracaoBonus;
  
  public int numDiasExpiracaoSMS;
  
  public int numDiasExpiracaoGPRS;
  
  public java.lang.String dataExpPrincipal;
  
  public java.lang.String dataExpBonus;
  
  public java.lang.String dataExpSMS;
  
  public java.lang.String dataExpGPRS;
  
  public boolean alorFace;

  public valoresRecarga () {
    dataExpPrincipal = "";
    dataExpBonus = "";
    dataExpSMS = "";
    dataExpGPRS = "";
  }

  public valoresRecarga (final int idValor, 
                         final double saldoPrincipal, 
                         final double saldoBonus, 
                         final double saldoSMS, 
                         final double saldoGPRS, 
                         final double valorBonusSMS, 
                         final double valorBonusGPRS, 
                         final double valorEfetivoPago, 
                         final int numDiasExpiracaoPrincipal, 
                         final int numDiasExpiracaoBonus, 
                         final int numDiasExpiracaoSMS, 
                         final int numDiasExpiracaoGPRS, 
                         final java.lang.String dataExpPrincipal, 
                         final java.lang.String dataExpBonus, 
                         final java.lang.String dataExpSMS, 
                         final java.lang.String dataExpGPRS, 
                         final boolean alorFace) {
    this.idValor = idValor;
    this.saldoPrincipal = saldoPrincipal;
    this.saldoBonus = saldoBonus;
    this.saldoSMS = saldoSMS;
    this.saldoGPRS = saldoGPRS;
    this.valorBonusSMS = valorBonusSMS;
    this.valorBonusGPRS = valorBonusGPRS;
    this.valorEfetivoPago = valorEfetivoPago;
    this.numDiasExpiracaoPrincipal = numDiasExpiracaoPrincipal;
    this.numDiasExpiracaoBonus = numDiasExpiracaoBonus;
    this.numDiasExpiracaoSMS = numDiasExpiracaoSMS;
    this.numDiasExpiracaoGPRS = numDiasExpiracaoGPRS;
    this.dataExpPrincipal = dataExpPrincipal;
    this.dataExpBonus = dataExpBonus;
    this.dataExpSMS = dataExpSMS;
    this.dataExpGPRS = dataExpGPRS;
    this.alorFace = alorFace;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga {");
    _ret.append("\n");
    _ret.append("int idValor=");
    _ret.append(idValor);
    _ret.append(",\n");
    _ret.append("double saldoPrincipal=");
    _ret.append(saldoPrincipal);
    _ret.append(",\n");
    _ret.append("double saldoBonus=");
    _ret.append(saldoBonus);
    _ret.append(",\n");
    _ret.append("double saldoSMS=");
    _ret.append(saldoSMS);
    _ret.append(",\n");
    _ret.append("double saldoGPRS=");
    _ret.append(saldoGPRS);
    _ret.append(",\n");
    _ret.append("double valorBonusSMS=");
    _ret.append(valorBonusSMS);
    _ret.append(",\n");
    _ret.append("double valorBonusGPRS=");
    _ret.append(valorBonusGPRS);
    _ret.append(",\n");
    _ret.append("double valorEfetivoPago=");
    _ret.append(valorEfetivoPago);
    _ret.append(",\n");
    _ret.append("int numDiasExpiracaoPrincipal=");
    _ret.append(numDiasExpiracaoPrincipal);
    _ret.append(",\n");
    _ret.append("int numDiasExpiracaoBonus=");
    _ret.append(numDiasExpiracaoBonus);
    _ret.append(",\n");
    _ret.append("int numDiasExpiracaoSMS=");
    _ret.append(numDiasExpiracaoSMS);
    _ret.append(",\n");
    _ret.append("int numDiasExpiracaoGPRS=");
    _ret.append(numDiasExpiracaoGPRS);
    _ret.append(",\n");
    _ret.append("java.lang.String dataExpPrincipal=");
    _ret.append(dataExpPrincipal != null?'\"' + dataExpPrincipal + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataExpBonus=");
    _ret.append(dataExpBonus != null?'\"' + dataExpBonus + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataExpSMS=");
    _ret.append(dataExpSMS != null?'\"' + dataExpSMS + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataExpGPRS=");
    _ret.append(dataExpGPRS != null?'\"' + dataExpGPRS + '\"':null);
    _ret.append(",\n");
    _ret.append("boolean alorFace=");
    _ret.append(alorFace);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga) {
      final com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga obj = (com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga)o;
      boolean res = true;
      do {
        res = this.idValor == obj.idValor;
        if (!res) break;
        res = this.saldoPrincipal == obj.saldoPrincipal;
        if (!res) break;
        res = this.saldoBonus == obj.saldoBonus;
        if (!res) break;
        res = this.saldoSMS == obj.saldoSMS;
        if (!res) break;
        res = this.saldoGPRS == obj.saldoGPRS;
        if (!res) break;
        res = this.valorBonusSMS == obj.valorBonusSMS;
        if (!res) break;
        res = this.valorBonusGPRS == obj.valorBonusGPRS;
        if (!res) break;
        res = this.valorEfetivoPago == obj.valorEfetivoPago;
        if (!res) break;
        res = this.numDiasExpiracaoPrincipal == obj.numDiasExpiracaoPrincipal;
        if (!res) break;
        res = this.numDiasExpiracaoBonus == obj.numDiasExpiracaoBonus;
        if (!res) break;
        res = this.numDiasExpiracaoSMS == obj.numDiasExpiracaoSMS;
        if (!res) break;
        res = this.numDiasExpiracaoGPRS == obj.numDiasExpiracaoGPRS;
        if (!res) break;
        res = this.dataExpPrincipal == obj.dataExpPrincipal ||
         (this.dataExpPrincipal != null && obj.dataExpPrincipal != null && this.dataExpPrincipal.equals(obj.dataExpPrincipal));
        if (!res) break;
        res = this.dataExpBonus == obj.dataExpBonus ||
         (this.dataExpBonus != null && obj.dataExpBonus != null && this.dataExpBonus.equals(obj.dataExpBonus));
        if (!res) break;
        res = this.dataExpSMS == obj.dataExpSMS ||
         (this.dataExpSMS != null && obj.dataExpSMS != null && this.dataExpSMS.equals(obj.dataExpSMS));
        if (!res) break;
        res = this.dataExpGPRS == obj.dataExpGPRS ||
         (this.dataExpGPRS != null && obj.dataExpGPRS != null && this.dataExpGPRS.equals(obj.dataExpGPRS));
        if (!res) break;
        res = this.alorFace == obj.alorFace;
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
