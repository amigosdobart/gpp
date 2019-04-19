package com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "GerenteGPP.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::gerenteGPP::orb::gerenteGPP::ListaProcessosConexao
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/ListaProcessosConexao:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltcom.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao&gt ListaProcessosConexao;
 * </pre>
 */
public final class ListaProcessosConexaoHelper {
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] read (final org.omg.CORBA.portable.InputStream _input) {
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] result;
    final int $length0 = _input.read_long();
    result = new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[$length0];
    for (int $counter1 = 0; $counter1 < $length0; $counter1++) {
      result[$counter1] = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.read(_input);
    }
    return result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _vis_value) {
    _output.write_long(_vis_value.length);
    for (int $counter2 = 0;  $counter2 < _vis_value.length; $counter2++) {
      if (_vis_value[$counter2] == null) {
        throw new org.omg.CORBA.BAD_PARAM("Invalid array length");
      }
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.write(_output, _vis_value[$counter2]);
    }
  }

  public static void insert (final org.omg.CORBA.Any any, final com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _vis_value) {
    any.type(com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.type());
    any.insert_Streamable(new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHolder(_vis_value));
  }

  public static com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] extract (final org.omg.CORBA.Any any) {
    com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexao[] _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHolder _vis_holder = new com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    } else {
      _vis_value = com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.ListaProcessosConexaoHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          org.omg.CORBA.TypeCode originalType = _orb().create_sequence_tc(0, com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPPackage.IdProcessoConexaoHelper.type());
          _type = _orb().create_alias_tc(id(), "ListaProcessosConexao", originalType);
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/gerenteGPP/orb/gerenteGPP/ListaProcessosConexao:1.0";
  }
}
