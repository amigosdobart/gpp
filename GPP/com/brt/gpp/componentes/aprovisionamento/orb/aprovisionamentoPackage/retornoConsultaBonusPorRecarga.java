package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::retornoConsultaBonusPorRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/retornoConsultaBonusPorRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct retornoConsultaBonusPorRecarga {
  ...
};
 * </pre>
 */
public final class retornoConsultaBonusPorRecarga implements org.omg.CORBA.portable.IDLEntity {
  
  public com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] listaBonus;
  
  public short codRetorno;

  public retornoConsultaBonusPorRecarga () {
  }

  public retornoConsultaBonusPorRecarga (final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] listaBonus, 
                                         final short codRetorno) {
    this.listaBonus = listaBonus;
    this.codRetorno = codRetorno;
  }

  private transient java.util.Hashtable _printMap = null;
  public java.lang.String toString() {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("struct com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga {");
    final java.lang.Thread _currentThread = java.lang.Thread.currentThread();
    boolean justCreated = false;
    if (_printMap == null) {
      synchronized (this) {
        if (_printMap == null) {
          justCreated = true;
          _printMap = new java.util.Hashtable();
        }
      }
    }
    if (!justCreated) {
      if (_printMap.get(_currentThread) != null) {
        _ret.append("...}");
        return _ret.toString();
      }
    }
    _printMap.put(_currentThread, this);
    _ret.append("\n");
    _ret.append("com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] listaBonus=");
    _ret.append("{");
    if (listaBonus == null) {
      _ret.append(listaBonus);
    } else {
      for (int $counter3 = 0; $counter3 < listaBonus.length; $counter3++) {
        _ret.append(listaBonus[$counter3]);
        if ($counter3 < listaBonus.length - 1) {
          _ret.append(",");
        }
      }
    }
    _ret.append("}");
    _ret.append(",\n");
    _ret.append("short codRetorno=");
    _ret.append(codRetorno);
    _ret.append("\n");
    _printMap.remove(_currentThread);
    _ret.append("}");
    return _ret.toString();
  }

  private transient java.util.Hashtable _cmpMap = null;
  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;

    final java.lang.Thread _currentThread = java.lang.Thread.currentThread();
    boolean justCreated = false;
    if (_cmpMap == null) {
      synchronized (this) {
        if (_cmpMap == null) {
          justCreated = true;
          _cmpMap = new java.util.Hashtable();
        }
      }
    }
    if (!justCreated) {
      final java.lang.Object _cmpObj;
      _cmpObj= _cmpMap.get(_currentThread);
      if (_cmpObj != null) return o == _cmpObj;
    }
    if (o instanceof com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga) {
      _cmpMap.put(_currentThread, o);
      final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga obj = (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga)o;
      boolean res = true;
      do {
          if (res = (this.listaBonus.length == obj.listaBonus.length)) {
            for (int $counter4 = 0; res && $counter4 < this.listaBonus.length; $counter4++) {
              res = this.listaBonus[$counter4] == obj.listaBonus[$counter4] ||
               (this.listaBonus[$counter4] != null && obj.listaBonus[$counter4] != null && this.listaBonus[$counter4].equals(obj.listaBonus[$counter4]));
            }
          }
        if (!res) break;
        res = this.codRetorno == obj.codRetorno;
      } while (false);
      _cmpMap.remove(_currentThread);
      return res;
    }
    else {
      return false;
    }
  }
}
