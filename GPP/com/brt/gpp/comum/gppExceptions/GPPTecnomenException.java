package com.brt.gpp.comum.gppExceptions;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "gppexceptions.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::comum::gppExceptions::GPPTecnomenException
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/comum/gppExceptions/GPPTecnomenException:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * exception GPPTecnomenException {
  ...
};
 * </pre>
 */
public final class GPPTecnomenException extends org.omg.CORBA.UserException {
  
  public java.lang.String codigoErro;

  public GPPTecnomenException () {
    super(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id());
  }

  public GPPTecnomenException (java.lang.String codigoErro) {
    this();
    this.codigoErro = codigoErro;
  }

  public GPPTecnomenException (java.lang.String _reason, java.lang.String codigoErro) {
    super(com.brt.gpp.comum.gppExceptions.GPPTecnomenExceptionHelper.id() + ' ' + _reason);
    this.codigoErro = codigoErro;
  }

  public java.lang.String toString () {
    final java.lang.StringBuffer _ret = new java.lang.StringBuffer("exception com.brt.gpp.comum.gppExceptions.GPPTecnomenException {");
    _ret.append("\n");
    _ret.append("java.lang.String codigoErro=");
    _ret.append(codigoErro != null?'\"' + codigoErro + '\"':null);
    _ret.append("\n");
    _ret.append("}");
    return _ret.toString();
  }

  public boolean equals (java.lang.Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (o instanceof com.brt.gpp.comum.gppExceptions.GPPTecnomenException) {
      final com.brt.gpp.comum.gppExceptions.GPPTecnomenException obj = (com.brt.gpp.comum.gppExceptions.GPPTecnomenException)o;
      boolean res = true;
      do {
        res = this.codigoErro == obj.codigoErro ||
         (this.codigoErro != null && obj.codigoErro != null && this.codigoErro.equals(obj.codigoErro));
      } while (false);
      return res;
    }
    else {
      return false;
    }
  }
}
