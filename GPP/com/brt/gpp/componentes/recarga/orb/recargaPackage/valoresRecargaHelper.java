package com.brt.gpp.componentes.recarga.orb.recargaPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Recarga.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::recarga::orb::recarga::valoresRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/recarga/orb/recarga/valoresRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * struct valoresRecarga {
  ...
};
 * </pre>
 */
public final class valoresRecargaHelper {
  private static boolean _inited = false;
  private static boolean _initing = false;
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga read (final org.omg.CORBA.portable.InputStream _input) {
    final com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga _result = new com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga();
    _result.idValor = _input.read_long();
    _result.saldoPrincipal = _input.read_double();
    _result.saldoBonus = _input.read_double();
    _result.saldoSMS = _input.read_double();
    _result.saldoGPRS = _input.read_double();
    _result.valorBonusSMS = _input.read_double();
    _result.valorBonusGPRS = _input.read_double();
    _result.valorEfetivoPago = _input.read_double();
    _result.numDiasExpiracaoPrincipal = _input.read_long();
    _result.numDiasExpiracaoBonus = _input.read_long();
    _result.numDiasExpiracaoSMS = _input.read_long();
    _result.numDiasExpiracaoGPRS = _input.read_long();
    _result.dataExpPrincipal = _input.read_string();
    _result.dataExpBonus = _input.read_string();
    _result.dataExpSMS = _input.read_string();
    _result.dataExpGPRS = _input.read_string();
    _result.alorFace = _input.read_boolean();
    return _result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga _vis_value) {
    _output.write_long((int)_vis_value.idValor);
    _output.write_double((double)_vis_value.saldoPrincipal);
    _output.write_double((double)_vis_value.saldoBonus);
    _output.write_double((double)_vis_value.saldoSMS);
    _output.write_double((double)_vis_value.saldoGPRS);
    _output.write_double((double)_vis_value.valorBonusSMS);
    _output.write_double((double)_vis_value.valorBonusGPRS);
    _output.write_double((double)_vis_value.valorEfetivoPago);
    _output.write_long((int)_vis_value.numDiasExpiracaoPrincipal);
    _output.write_long((int)_vis_value.numDiasExpiracaoBonus);
    _output.write_long((int)_vis_value.numDiasExpiracaoSMS);
    _output.write_long((int)_vis_value.numDiasExpiracaoGPRS);
    _output.write_string((java.lang.String)_vis_value.dataExpPrincipal);
    _output.write_string((java.lang.String)_vis_value.dataExpBonus);
    _output.write_string((java.lang.String)_vis_value.dataExpSMS);
    _output.write_string((java.lang.String)_vis_value.dataExpGPRS);
    _output.write_boolean((boolean)_vis_value.alorFace);
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga _vis_value) {
    any.insert_Streamable(new com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecarga _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHolder _vis_holder = new com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    }
    else {
      _vis_value = com.brt.gpp.componentes.recarga.orb.recargaPackage.valoresRecargaHelper.read(any.create_input_stream());
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
          final org.omg.CORBA.StructMember[] members = new org.omg.CORBA.StructMember[17];
          members[0] = new org.omg.CORBA.StructMember("idValor", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[1] = new org.omg.CORBA.StructMember("saldoPrincipal", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[2] = new org.omg.CORBA.StructMember("saldoBonus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[3] = new org.omg.CORBA.StructMember("saldoSMS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[4] = new org.omg.CORBA.StructMember("saldoGPRS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[5] = new org.omg.CORBA.StructMember("valorBonusSMS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[6] = new org.omg.CORBA.StructMember("valorBonusGPRS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[7] = new org.omg.CORBA.StructMember("valorEfetivoPago", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_double), null);
          members[8] = new org.omg.CORBA.StructMember("numDiasExpiracaoPrincipal", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[9] = new org.omg.CORBA.StructMember("numDiasExpiracaoBonus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[10] = new org.omg.CORBA.StructMember("numDiasExpiracaoSMS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[11] = new org.omg.CORBA.StructMember("numDiasExpiracaoGPRS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_long), null);
          members[12] = new org.omg.CORBA.StructMember("dataExpPrincipal", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[13] = new org.omg.CORBA.StructMember("dataExpBonus", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[14] = new org.omg.CORBA.StructMember("dataExpSMS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[15] = new org.omg.CORBA.StructMember("dataExpGPRS", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string), null);
          members[16] = new org.omg.CORBA.StructMember("alorFace", _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_boolean), null);
          _type = _orb().create_struct_tc(id(), "valoresRecarga", members);
          _initializing = false;
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/recarga/orb/recarga/valoresRecarga:1.0";
  }
}
