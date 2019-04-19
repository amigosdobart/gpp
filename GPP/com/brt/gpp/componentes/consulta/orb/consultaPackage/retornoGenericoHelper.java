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
public final class retornoGenericoHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico _result = new com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico();
    _result.msisdn = _input.read_string();
    _result.codigoRetorno = _input.read_short();
    _result.descricao = _input.read_string();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico _vis_value) {
    _output.write_string((java.lang.String)_vis_value.msisdn);
    _output.write_short((short)_vis_value.codigoRetorno);
    _output.write_string((java.lang.String)_vis_value.descricao);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenerico _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHolder _vis_holder = new com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.consulta.orb.consultaPackage.retornoGenericoHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          if (_initializing) {
            return _orb().create_recursive_tc(id());
          }
          _initializing = true;
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[3];
          members[0] = new org.omg.CORBA.StructMember("msisdn", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[1] = new org.omg.CORBA.StructMember("codigoRetorno", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_short), null);
          members[2] = new org.omg.CORBA.StructMember("descricao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          _type = _orb().create_struct_tc(id(), "retornoGenerico", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/consulta/orb/consulta/retornoGenerico:1.0";
  }
}
