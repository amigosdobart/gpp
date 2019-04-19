package com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage;

/**
 * <ul>
 * <li> <b>IDL Source</b>    "ProcessosBatch.idl"
 * <li> <b>IDL Name</b>      ::com::brt::gpp::componentes::processosBatch::orb::processosBatch::paramsProcBatch
 * <li> <b>Repository Id</b> IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/paramsProcBatch:1.0
 * </ul>
 * <b>IDL definition:</b>
 * <pre>
 * typedef sequence&ltstring&gt paramsProcBatch;
 * </pre>
 */
public final class paramsProcBatchHelper {
  private static org.omg.CORBA.TypeCode _type;
  private static boolean _initializing;

  private static org.omg.CORBA.ORB _orb () {
    return org.omg.CORBA.ORB.init();
  }

  public static java.lang.String[] read (final org.omg.CORBA.portable.InputStream _input) {
    java.lang.String[] result;
    final int $length1 = _input.read_long();
    result = new java.lang.String[$length1];
    for (int $counter2 = 0; $counter2 < $length1; $counter2++) {
      result[$counter2] = _input.read_string();
    }
    return result;
  }

  public static void write (final org.omg.CORBA.portable.OutputStream _output, final java.lang.String[] _vis_value) {
    _output.write_long(_vis_value.length);
    for (int $counter3 = 0;  $counter3 < _vis_value.length; $counter3++) {
      _output.write_string((java.lang.String)_vis_value[$counter3]);
    }
  }

  public static void insert (final org.omg.CORBA.Any any, final java.lang.String[] _vis_value) {
    any.type(com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.type());
    any.insert_Streamable(new com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHolder(_vis_value));
  }

  public static java.lang.String[] extract (final org.omg.CORBA.Any any) {
    java.lang.String[] _vis_value;
    if (any instanceof com.inprise.vbroker.CORBA.Any) {
      com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHolder _vis_holder = new com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHolder();
      ((com.inprise.vbroker.CORBA.Any)any).extract_Streamable(_vis_holder);
      _vis_value = _vis_holder.value;
    } else {
      _vis_value = com.brt.gpp.componentes.processosBatch.orb.processosBatchPackage.paramsProcBatchHelper.read(any.create_input_stream());
    }
    return _vis_value;
  }

  public static org.omg.CORBA.TypeCode type () {
    if (_type == null) {
      synchronized (org.omg.CORBA.TypeCode.class) {
        if (_type == null) {
          org.omg.CORBA.TypeCode originalType = _orb().create_sequence_tc(0, _orb().get_primitive_tc(org.omg.CORBA.TCKind.tk_string));
          _type = _orb().create_alias_tc(id(), "paramsProcBatch", originalType);
        }
      }
    }
    return _type;
  }

  public static java.lang.String id () {
    return "IDL:com/brt/gpp/componentes/processosBatch/orb/processosBatch/paramsProcBatch:1.0";
  }
}
