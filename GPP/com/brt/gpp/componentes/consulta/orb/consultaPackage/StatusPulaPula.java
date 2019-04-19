package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::StatusPulaPula
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/StatusPulaPula:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct StatusPulaPula {
  ...
};
 * </pre>
 */
public final class StatusPulaPula implements org.omg.CORBA.portable.IDLEntity {
  
  public boolean isNull;
  
  public java.lang.String msisdn;
  
  public short idPromocao;
  
  public java.lang.String nomePromocao;
  
  public java.lang.String dataExecucao;
  
  public java.lang.String dataCreditoBonus;
  
  public java.lang.String dataEntradaPromocao;
  
  public java.lang.String dataInicioAnalise;
  
  public java.lang.String dataFimAnalise;
  
  public boolean suspenso;
  
  public double minutos;
  
  public double minutosFF;
  
  public double valorBonus;
  
  public double limitePromocao;
  
  public boolean isentoLimite;
  
  public java.lang.String observacao;

  public StatusPulaPula () {
    msisdn = "";
    nomePromocao = "";
    dataExecucao = "";
    dataCreditoBonus = "";
    dataEntradaPromocao = "";
    dataInicioAnalise = "";
    dataFimAnalise = "";
    observacao = "";
  }

  public StatusPulaPula (final boolean isNull, 
                         final java.lang.String msisdn, 
                         final short idPromocao, 
                         final java.lang.String nomePromocao, 
                         final java.lang.String dataExecucao, 
                         final java.lang.String dataCreditoBonus, 
                         final java.lang.String dataEntradaPromocao, 
                         final java.lang.String dataInicioAnalise, 
                         final java.lang.String dataFimAnalise, 
                         final boolean suspenso, 
                         final double minutos, 
                         final double minutosFF, 
                         final double valorBonus, 
                         final double limitePromocao, 
                         final boolean isentoLimite, 
                         final java.lang.String observacao) {
    this.isNull = isNull;
    this.msisdn = msisdn;
    this.idPromocao = idPromocao;
    this.nomePromocao = nomePromocao;
    this.dataExecucao = dataExecucao;
    this.dataCreditoBonus = dataCreditoBonus;
    this.dataEntradaPromocao = dataEntradaPromocao;
    this.dataInicioAnalise = dataInicioAnalise;
    this.dataFimAnalise = dataFimAnalise;
    this.suspenso = suspenso;
    this.minutos = minutos;
    this.minutosFF = minutosFF;
    this.valorBonus = valorBonus;
    this.limitePromocao = limitePromocao;
    this.isentoLimite = isentoLimite;
    this.observacao = observacao;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula {");
    _ret.append("\n");
    _ret.append("boolean isNull=");
    _ret.append(isNull);
    _ret.append(",\n");
    _ret.append("java.lang.String msisdn=");
    _ret.append(msisdn != null?'\"' + msisdn + '\"':null);
    _ret.append(",\n");
    _ret.append("short idPromocao=");
    _ret.append(idPromocao);
    _ret.append(",\n");
    _ret.append("java.lang.String nomePromocao=");
    _ret.append(nomePromocao != null?'\"' + nomePromocao + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataExecucao=");
    _ret.append(dataExecucao != null?'\"' + dataExecucao + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataCreditoBonus=");
    _ret.append(dataCreditoBonus != null?'\"' + dataCreditoBonus + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataEntradaPromocao=");
    _ret.append(dataEntradaPromocao != null?'\"' + dataEntradaPromocao + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataInicioAnalise=");
    _ret.append(dataInicioAnalise != null?'\"' + dataInicioAnalise + '\"':null);
    _ret.append(",\n");
    _ret.append("java.lang.String dataFimAnalise=");
    _ret.append(dataFimAnalise != null?'\"' + dataFimAnalise + '\"':null);
    _ret.append(",\n");
    _ret.append("boolean suspenso=");
    _ret.append(suspenso);
    _ret.append(",\n");
    _ret.append("double minutos=");
    _ret.append(minutos);
    _ret.append(",\n");
    _ret.append("double minutosFF=");
    _ret.append(minutosFF);
    _ret.append(",\n");
    _ret.append("double valorBonus=");
    _ret.append(valorBonus);
    _ret.append(",\n");
    _ret.append("double limitePromocao=");
    _ret.append(limitePromocao);
    _ret.append(",\n");
    _ret.append("boolean isentoLimite=");
    _ret.append(isentoLimite);
    _ret.append(",\n");
    _ret.append("java.lang.String observacao=");
    _ret.append(observacao != null?'\"' + observacao + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula) {
      final com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula obj = (com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula)o;
      boolean res = true;
      do {
        res = this.isNull == obj.isNull;
        if (!res) break;
        res = this.msisdn == obj.msisdn ||
         (this.msisdn != null && obj.msisdn != null && this.msisdn.equals(obj.msisdn));
        if (!res) break;
        res = this.idPromocao == obj.idPromocao;
        if (!res) break;
        res = this.nomePromocao == obj.nomePromocao ||
         (this.nomePromocao != null && obj.nomePromocao != null && this.nomePromocao.equals(obj.nomePromocao));
        if (!res) break;
        res = this.dataExecucao == obj.dataExecucao ||
         (this.dataExecucao != null && obj.dataExecucao != null && this.dataExecucao.equals(obj.dataExecucao));
        if (!res) break;
        res = this.dataCreditoBonus == obj.dataCreditoBonus ||
         (this.dataCreditoBonus != null && obj.dataCreditoBonus != null && this.dataCreditoBonus.equals(obj.dataCreditoBonus));
        if (!res) break;
        res = this.dataEntradaPromocao == obj.dataEntradaPromocao ||
         (this.dataEntradaPromocao != null && obj.dataEntradaPromocao != null && this.dataEntradaPromocao.equals(obj.dataEntradaPromocao));
        if (!res) break;
        res = this.dataInicioAnalise == obj.dataInicioAnalise ||
         (this.dataInicioAnalise != null && obj.dataInicioAnalise != null && this.dataInicioAnalise.equals(obj.dataInicioAnalise));
        if (!res) break;
        res = this.dataFimAnalise == obj.dataFimAnalise ||
         (this.dataFimAnalise != null && obj.dataFimAnalise != null && this.dataFimAnalise.equals(obj.dataFimAnalise));
        if (!res) break;
        res = this.suspenso == obj.suspenso;
        if (!res) break;
        res = this.minutos == obj.minutos;
        if (!res) break;
        res = this.minutosFF == obj.minutosFF;
        if (!res) break;
        res = this.valorBonus == obj.valorBonus;
        if (!res) break;
        res = this.limitePromocao == obj.limitePromocao;
        if (!res) break;
        res = this.isentoLimite == obj.isentoLimite;
        if (!res) break;
        res = this.observacao == obj.observacao ||
         (this.observacao != null && obj.observacao != null && this.observacao.equals(obj.observacao));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
