package com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "GerenteGPP.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::gerenteGPP::orb::gerenteGPP::IdProcessoConexao
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/IdProcessoConexao:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct IdProcessoConexao {
  ...
};
 * </pre>
 */
public final class IdProcessoConexao implements org.omg.CORBA.portable.IDLEntity {
  
  public short idProcesso;
  
  public java.lang.String dataInicialUso;

  public IdProcessoConexao () {
    dataInicialUso = "";
  }

  public IdProcessoConexao (final short idProcesso, 
                            final java.lang.String dataInicialUso) {
    this.idProcesso = idProcesso;
    this.dataInicialUso = dataInicialUso;
  }

  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao {");
    _ret.append("\n");
    _ret.append("short idProcesso=");
    _ret.append(idProcesso);
    _ret.append(",\n");
    _ret.append("java.lang.String dataInicialUso=");
    _ret.append(dataInicialUso != null?'\"' + dataInicialUso + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    if (o instanceof com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao) {
      final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao obj = (com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao)o;
      boolean res = true;
      do {
        res = this.idProcesso == obj.idProcesso;
        if (!res) break;
        res = this.dataInicialUso == obj.dataInicialUso ||
         (this.dataInicialUso != null && obj.dataInicialUso != null && this.dataInicialUso.equals(obj.dataInicialUso));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
