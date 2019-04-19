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
public final class retornoConsultaBonusPorRecargaHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga _result = new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga();
    _result.listaBonus = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.read(_input);
    _result.codRetorno = _input.read_short();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga _vis_value) {
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.write(_output, _vis_value.listaBonus);
    _output.write_short((short)_vis_value.codRetorno);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecarga _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHolder _vis_holder = new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoConsultaBonusPorRecargaHelper.read(any.create_input_stream());
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
          members[0] = new org.omg.CORBA.StructMember("listaBonus", com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.type(), null);
          members[1] = new org.omg.CORBA.StructMember("codRetorno", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_short), null);
          _type = _orb().create_struct_tc(id(), "retornoConsultaBonusPorRecarga", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/retornoConsultaBonusPorRecarga:1.0";
  }
}
