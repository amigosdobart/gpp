package com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "Aprovisionamento.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::aprovisionamento::orb::aprovisionamento::ListaDadosBonusPorRecarga
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/ListaDadosBonusPorRecarga:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltcom.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga&gt ListaDadosBonusPorRecarga;
 * </pre>
 */
public final class ListaDadosBonusPorRecargaHelper {
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] read (final org.omg.CORBA.portable.InputStream _input) {
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] result;
    final int $length0 = _input.read_long();
    result = new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[$length0];
    for (int $counter1 = 0; $counter1 < $length0; $counter1++) {
      result[$counter1] = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.read(_input);
    }
    return result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] _vis_value) {
    _output.write_long(_vis_value.length);
    for (int $counter2 = 0;  $counter2 < _vis_value.length; $counter2++) {
      if (_vis_value[$counter2] == null) {
        throw new org.omg.CORBA.BAD_PARAM("Invalid array length");
      }
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.write(_output, _vis_value[$counter2]);
    }
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] _vis_value) {
    any.type(com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.type());
    any.insert_Streamable(new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecarga[] _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHolder _vis_holder = new com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    } else {
      _vis_value = com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.ListaDadosBonusPorRecargaHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          org.omg.CORBA.TypeCode originalType = _orb().create_sequence_tc(0, com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.dadosBonusPorRecargaHelper.type());
          _type = _orb().create_alias_tc(id(), "ListaDadosBonusPorRecarga", originalType);
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/aprovisionamento/orb/aprovisionamento/ListaDadosBonusPorRecarga:1.0";
  }
}
