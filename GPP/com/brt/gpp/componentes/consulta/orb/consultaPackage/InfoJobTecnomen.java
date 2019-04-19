package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::InfoJobTecnomen
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/InfoJobTecnomen:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct InfoJobTecnomen {
  ...
};
 * </pre>
 */
public final class InfoJobTecnomen implements org.omg.CORBA.portable.IDLEntity {
  
  public int numeroJob;
  
  public long workTotal;
  
  public long workDone;
  
  public long codStatus;
  
  public java.lang.String descStatus;

  public InfoJobTecnomen () {
    descStatus = "";
  }

  public InfoJobTecnomen (final int numeroJob, 
                          final long workTotal, 
                          final long workDone, 
                          final long codStatus, 
                          final java.lang.String descStatus) {
    this.numeroJob = numeroJob;
    this.workTotal = workTotal;
    this.workDone = workDone;
    this.codStatus = codStatus;
    this.descStatus = descStatus;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen {");
    _ret.append("\n");
    _ret.append("int numeroJob=");
    _ret.append(numeroJob);
    _ret.append(",\n");
    _ret.append("long workTotal=");
    _ret.append(workTotal);
    _ret.append(",\n");
    _ret.append("long workDone=");
    _ret.append(workDone);
    _ret.append(",\n");
    _ret.append("long codStatus=");
    _ret.append(codStatus);
    _ret.append(",\n");
    _ret.append("java.lang.String descStatus=");
    _ret.append(descStatus != null?'\"' + descStatus + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen) {
      final com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen obj = (com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen)o;
      boolean res = true;
      do {
        res = this.numeroJob == obj.numeroJob;
        if (!res) break;
        res = this.workTotal == obj.workTotal;
        if (!res) break;
        res = this.workDone == obj.workDone;
        if (!res) break;
        res = this.codStatus == obj.codStatus;
        if (!res) break;
        res = this.descStatus == obj.descStatus ||
         (this.descStatus != null && obj.descStatus != null && this.descStatus.equals(obj.descStatus));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
