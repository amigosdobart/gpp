package com.brt.gpp.componentes.aprovisionamento.orb;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * interface aprovisionamento {
  ...
};
 * </pre>
 */
public final class aprovisionamentoHelper {
  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento narrow (final org.omg.CORBA.Object obj) {
    return narrow(obj, false);
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento unchecked_narrow (org.omg.CORBA.Object obj) {
    return narrow(obj, true);
  }

  private static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento narrow (final org.omg.CORBA.Object obj, final boolean is_a) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento) {
      return (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento)obj;
    }
    if (is_a || obj._is_a(id())) {
      final org.omg.CORBA.portable.ObjectImpl _obj = (org.omg.CORBA.portable.ObjectImpl)obj;
      com.brt.gpp.componentes.aprovisionamento.orb._aprovisionamentoStub result = new com.brt.gpp.componentes.aprovisionamento.orb._aprovisionamentoStub();
      final org.omg.CORBA.portable.Delegate _delegate = _obj._get_delegate();
      result._set_delegate(_delegate);
      return result;
    }
    throw new org.omg.CORBA.BAD_PARAM();
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento bind (org.omg.CORBA.ORB orb) {
    return bind(orb, null, null, null);
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento bind (org.omg.CORBA.ORB orb, java.lang.String name) {
    return bind(orb, name, null, null);
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento bind (org.omg.CORBA.ORB orb,
                                                                                    java.lang.String name,
                                                                                    java.lang.String host,
                                                                                    com.inprise.vbroker.CORBA.BindOptions _options) {
    if (!(orb instanceof com.inprise.vbroker.CORBA.ORB)) {
      throw new org.omg.CORBA.BAD_PARAM();
    }
    return narrow(((com.inprise.vbroker.CORBA.ORB)orb).bind(id(), name, host, _options), true);
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento bind (org.omg.CORBA.ORB orb, java.lang.String fullPoaName, byte[] oid) {
    return bind(orb, fullPoaName, oid, null, null);
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento bind (org.omg.CORBA.ORB orb,
                                                                                    java.lang.String fullPoaName,
                                                                                    byte[] oid,
                                                                                    java.lang.String host,
                                                                                    com.inprise.vbroker.CORBA.BindOptions _options) {
    if (!(orb instanceof com.inprise.vbroker.CORBA.ORB)) {
      throw new org.omg.CORBA.BAD_PARAM();
    }
    return narrow(((com.inprise.vbroker.CORBA.ORB)orb).bind(fullPoaName, oid, host, _options), true);
  }

  public java.lang.Object read_Object (final org.omg.CORBA.portable.InputStream istream) {
    return read(istream);
  }

  public void write_Object (final org.omg.CORBA.portable.OutputStream ostream, final java.lang.Object obj) {
    if (!(obj instanceof com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento)) {
      throw new org.omg.CORBA.BAD_PARAM();
    }
    write(ostream, (com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento)obj);
  }

  public java.lang.String get_id () {
    return id();
  }

  public org.omg.CORBA.TypeCode get_type () {
    return type();
  }

  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento read (final org.omg.CORBA.portable.InputStream _input) {
    return narrow(_input.read_Object(com.brt.gpp.componentes.aprovisionamento.orb._aprovisionamentoStub.class), true);
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _vis_value) {
    if (_vis_value != null && !(_vis_value instanceof org.omg.CORBA.portable.ObjectImpl)) {
      throw new org.omg.CORBA.BAD_PARAM();
    }
    _output.write_Object((org.omg.CORBA.Object)_vis_value);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _vis_value) {
    any.insert_Object((org.omg.CORBA.Object)_vis_value, com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.type());
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento _vis_value;
    final org.omg.CORBA.Object _obj = any.extract_Object();
    _vis_value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper.narrow(_obj);
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          _type = _orb().create_interface_tc(id(), "aprovisionamento");
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento:1.0";
  }
}
