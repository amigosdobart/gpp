package com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch::BufferDados
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/BufferDados:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltoctet&gt BufferDados;
 * </pre>
 */
public final class BufferDadosHelper {
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static byte[] read (final org.omg.CORBA.portable.InputStream _input) {
    byte[] result;
    final int $length0 = _input.read_long();
    result = new byte[$length0];
    _input.read_octet_array(result, 0, $length0);
    return result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final byte[] _vis_value) {
    _output.write_long(_vis_value.length);
    _output.write_octet_array(_vis_value, 0, _vis_value.length);
  }

  public static void insert (final org.omg.CORBA.Any any, final byte[] _vis_value) {
    any.type(com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.type());
    any.insert_Streamable(new com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHolder(_vis_value));
  }

  public static byte[] extract (final org.omg.CORBA.Any any) {
    byte[] _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHolder _vis_holder = new com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    } else {
      _vis_value = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.BufferDadosHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          org.omg.CORBA.TypeCode originalType = _orb().create_sequence_tc(0, _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_octet));
          _type = _orb().create_alias_tc(id(), "BufferDados", originalType);
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/BufferDados:1.0";
  }
}
