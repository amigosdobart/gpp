package com.brt.gpp.componentes.consulta.orb.consultaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Consulta.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::consulta::orb::consulta::StatusPulaPula
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/consulta/orb/consulta/StatusPulaPula:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct StatusPulaPula {
  ...
};
 * </pre>
 */
public final class StatusPulaPulaHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula _result = new com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula();
    _result.isNull = _input.read_boolean();
    _result.msisdn = _input.read_string();
    _result.idPromocao = _input.read_short();
    _result.nomePromocao = _input.read_string();
    _result.dataExecucao = _input.read_string();
    _result.dataCreditoBonus = _input.read_string();
    _result.dataEntradaPromocao = _input.read_string();
    _result.dataInicioAnalise = _input.read_string();
    _result.dataFimAnalise = _input.read_string();
    _result.suspenso = _input.read_boolean();
    _result.minutos = _input.read_double();
    _result.minutosFF = _input.read_double();
    _result.valorBonus = _input.read_double();
    _result.limitePromocao = _input.read_double();
    _result.isentoLimite = _input.read_boolean();
    _result.observacao = _input.read_string();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula _vis_value) {
    _output.write_boolean((boolean)_vis_value.isNull);
    _output.write_string((java.lang.String)_vis_value.msisdn);
    _output.write_short((short)_vis_value.idPromocao);
    _output.write_string((java.lang.String)_vis_value.nomePromocao);
    _output.write_string((java.lang.String)_vis_value.dataExecucao);
    _output.write_string((java.lang.String)_vis_value.dataCreditoBonus);
    _output.write_string((java.lang.String)_vis_value.dataEntradaPromocao);
    _output.write_string((java.lang.String)_vis_value.dataInicioAnalise);
    _output.write_string((java.lang.String)_vis_value.dataFimAnalise);
    _output.write_boolean((boolean)_vis_value.suspenso);
    _output.write_double((double)_vis_value.minutos);
    _output.write_double((double)_vis_value.minutosFF);
    _output.write_double((double)_vis_value.valorBonus);
    _output.write_double((double)_vis_value.limitePromocao);
    _output.write_boolean((boolean)_vis_value.isentoLimite);
    _output.write_string((java.lang.String)_vis_value.observacao);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPulaHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPula _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPulaHolder _vis_holder = new com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPulaHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.consulta.orb.consultaPackage.StatusPulaPulaHelper.read(any.create_input_stream());
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
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[16];
          members[0] = new org.omg.CORBA.StructMember("isNull", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_boolean), null);
          members[1] = new org.omg.CORBA.StructMember("msisdn", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[2] = new org.omg.CORBA.StructMember("idPromocao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_short), null);
          members[3] = new org.omg.CORBA.StructMember("nomePromocao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[4] = new org.omg.CORBA.StructMember("dataExecucao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[5] = new org.omg.CORBA.StructMember("dataCreditoBonus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[6] = new org.omg.CORBA.StructMember("dataEntradaPromocao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[7] = new org.omg.CORBA.StructMember("dataInicioAnalise", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[8] = new org.omg.CORBA.StructMember("dataFimAnalise", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[9] = new org.omg.CORBA.StructMember("suspenso", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_boolean), null);
          members[10] = new org.omg.CORBA.StructMember("minutos", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[11] = new org.omg.CORBA.StructMember("minutosFF", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[12] = new org.omg.CORBA.StructMember("valorBonus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[13] = new org.omg.CORBA.StructMember("limitePromocao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[14] = new org.omg.CORBA.StructMember("isentoLimite", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_boolean), null);
          members[15] = new org.omg.CORBA.StructMember("observacao", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          _type = _orb().create_struct_tc(id(), "StatusPulaPula", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/consulta/orb/consulta/StatusPulaPula:1.0";
  }
}
