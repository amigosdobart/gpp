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
public final class InfoJobTecnomenHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _result = new com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen();
    _result.numeroJob = _input.read_long();
    _result.workTotal = _input.read_longlong();
    _result.workDone = _input.read_longlong();
    _result.codStatus = _input.read_longlong();
    _result.descStatus = _input.read_string();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _vis_value) {
    _output.write_long((int)_vis_value.numeroJob);
    _output.write_longlong((long)_vis_value.workTotal);
    _output.write_longlong((long)_vis_value.workDone);
    _output.write_longlong((long)_vis_value.codStatus);
    _output.write_string((java.lang.String)_vis_value.descStatus);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomen _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHolder _vis_holder = new com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.consulta.orb.consultaPackage.InfoJobTecnomenHelper.read(any.create_input_stream());
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
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[5];
          members[0] = new org.omg.CORBA.StructMember("numeroJob", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[1] = new org.omg.CORBA.StructMember("workTotal", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong), null);
          members[2] = new org.omg.CORBA.StructMember("workDone", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong), null);
          members[3] = new org.omg.CORBA.StructMember("codStatus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_longlong), null);
          members[4] = new org.omg.CORBA.StructMember("descStatus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          _type = _orb().create_struct_tc(id(), "InfoJobTecnomen", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/consulta/orb/consulta/InfoJobTecnomen:1.0";
  }
}
