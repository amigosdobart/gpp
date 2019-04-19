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
public final class IdProcessoConexaoHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao _result = new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao();
    _result.idProcesso = _input.read_short();
    _result.dataInicialUso = _input.read_string();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao _vis_value) {
    _output.write_short((short)_vis_value.idProcesso);
    _output.write_string((java.lang.String)_vis_value.dataInicialUso);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHolder _vis_holder = new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.read(any.create_input_stream());
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
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[2];
          members[0] = new org.omg.CORBA.StructMember("idProcesso", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_short), null);
          members[1] = new org.omg.CORBA.StructMember("dataInicialUso", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          _type = _orb().create_struct_tc(id(), "IdProcessoConexao", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/IdProcessoConexao:1.0";
  }
}
