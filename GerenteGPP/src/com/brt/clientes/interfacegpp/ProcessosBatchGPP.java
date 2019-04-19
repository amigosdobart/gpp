/*
 * Created on 08/06/2004
 */
package com.brt.clientes.interfacegpp;

import org.omg.CORBA.ORB;
import com.brt.gpp.componentes.processosBatch.orb.processosBatch;
import com.brt.gpp.componentes.processosBatch.orb.processosBatchHelper;

//import java.text.SimpleDateFormat;
//import java.util.Calendar;
/**
 * @author Daniel Ferreira
 * @since  08/06/2004
 */
public class ProcessosBatchGPP 
{
	
	private static processosBatch execute()
	{
		
	  // Inicializando o ORB

	  ORB orb = GerenteORB.getOrb();

	  byte[] managerId = "ComponenteNegociosProcessosBatch".getBytes();
		
	  processosBatch pPOA = processosBatchHelper.bind(orb, "/AtivaComponentesPOA", managerId);		
	  return pPOA;
	}
	
	public static short doProcBatchEnvioInfosRecargas(String dataExecucao) throws Exception
	{
	  return execute().enviaInfosRecarga(dataExecucao);
	}
	
	public static short doProcBatchEnvioBonusCSP14(String dataExecucao) throws Exception
	{
	  return execute().enviaBonusCSP14(dataExecucao);
	}
  
	public static short doProcBatchEnvioRecargasConciliacao(String dataExecucao) throws Exception
	{
	  return execute().enviarRecargasConciliacao(dataExecucao);
	}
	
	public static short doProcBatchContestacao(String dataExecucao) throws Exception
	{
	  return execute().executaContestacao(dataExecucao);
	}

	public static short doProcBatchEnvioUsuariosStatusNormal(String dataExecucao) throws Exception
	{
	  return execute().enviarUsuariosStatusNormal(dataExecucao);
	}

	public static short doProcBatchEnvioUsuariosStatusShutdown(String dataExecucao) throws Exception
	{
	  return execute().enviaUsuariosShutdown(dataExecucao);
	}
	
	public static short doProcBatchImportacaoAssinantes() throws Exception
	{
	  return execute().importaAssinantes();
	}
	
	public static short doProcBatchImportacaoCDR(String dataExecucao) throws Exception
	{
	  return execute().importaArquivosCDR(dataExecucao);
	}
	
	public static short doProcBatchImportacaoUsuariosNDSPortal() throws Exception
	{
	  return execute().importaUsuarioPortalNDS();
	}
	
	public static boolean doProcBatchRecargaRecorrente() throws Exception
	{
	  return execute().executaRecargaRecorrente();
	}
	
	public static short doProcBatchSumarizacaoAjustes(String dataExecucao) throws Exception
	{
	  return execute().sumarizarAjustes(dataExecucao);
	}
	
	public static short doProcBatchDiasSemRecargas() throws Exception
	{
	  return execute().atualizaDiasSemRecarga();
	}
	
	public static short doProcBatchSumarizacaoProdutoPlano(String dataExecucao) throws Exception
	{
	  return execute().sumarizarProdutoPlano(dataExecucao);
	}
	
	public static short doProcBatchTratarVoucher(String dataExecucao) throws Exception
	{
	  return execute().executaTratamentoVoucher(dataExecucao);
	}

	public static short doProcBatchEnvioConsolidacaoSCRGPP(String dataExecucao) throws Exception
	{
	  return execute().enviarConsolidacaoSCR(dataExecucao);
	}

	public static short doProcBatchSumarizacaoContabil(String dataExecucao) throws Exception
	{
	  return execute().sumarizarContabilidade(dataExecucao);
	}
	
	public static short doProcBatchIndiceBonificacao(String dataExecucao) throws Exception
	{
	  
		/* Calendar c = Calendar.getInstance();
		int dia = Integer.parseInt(dataExecucao.substring(0,2));
		int mes = Integer.parseInt(dataExecucao.substring(3,5));
		int ano = Integer.parseInt(dataExecucao.substring(6,10));
		c.set(ano,mes-1,28);
		c.add(Calendar.DATE,1);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataInicial = sdf.format(c.getTime());
		c.set(ano,mes,28); */
		
		return execute().calcularIndiceBonificacao(dataExecucao);
	}
	
	public static short doProcBatchConsolidacaoContabil(String dataExecucao) throws Exception
	{
	  return execute().consolidarContabilidade(dataExecucao);
	}
	
}
