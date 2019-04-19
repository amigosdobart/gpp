package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::retornoGenerico
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/retornoGenerico:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct retornoGenerico {
  ...
};
 * </pre>
 */
public final class retornoGenerico implements org.omg.CORBA.portable.IDLEntity {
  
  public java.lang.String msisdn;
  
  public short codigoRetorno;
  
  public java.lang.String descricao;

  public retornoGenerico () {
    msisdn = "";
    descricao = "";
  }

  public retornoGenerico (final java.lang.String msisdn, 
                          final short codigoRetorno, 
                          final java.lang.String descricao) {
    this.msisdn = msisdn;
    this.codigoRetorno = codigoRetorno;
    this.descricao = descricao;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico {");
    _ret.append("\n");
    _ret.append("java.lang.String msisdn=");
    _ret.append(msisdn != null?'\"' + msisdn + '\"':null);
    _ret.append(",\n");
    _ret.append("short codigoRetorno=");
    _ret.append(codigoRetorno);
    _ret.append(",\n");
    _ret.append("java.lang.String descricao=");
    _ret.append(descricao != null?'\"' + descricao + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico) {
      final com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico obj = (com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico)o;
      boolean res = true;
      do {
        res = this.msisdn == obj.msisdn ||
         (this.msisdn != null && obj.msisdn != null && this.msisdn.equals(obj.msisdn));
        if (!res) break;
        res = this.codigoRetorno == obj.codigoRetorno;
        if (!res) break;
        res = this.descricao == obj.descricao ||
         (this.descricao != null && obj.descricao != null && this.descricao.equals(obj.descricao));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
