
package com.brt.clientes.interfacegpp;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;

/**
 * Conecta com o GPP para realizar as consultas
 * @author Alex Pitacci Simões
 * @since 08/06/2004
 */
public class ConsultaGPP {

	private static consulta execute(){
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		
		byte[] managerId = "ComponenteNegociosConsulta".getBytes();
		
		consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		return pPOA;
	}
	
	public static String getVoucher(String voucher) throws Exception{
		
		return execute().consultaVoucher(voucher);
	}
	
	public static String getAssinante(String msisdn) throws Exception{
		return execute().consultaAssinante(msisdn);
	}
	
	public static String getRecargaAssinanteXML(
			String msisdn,
			String valor,
			String cpf,
			String categoria,
			String hashCartao,
			String sistemaOrigem) throws Exception{
		
		String aXML = "<?xml version=\"1.0\"?>";
		aXML += "<GPPConsultaPreRecarga>";
		aXML += "<msisdn>" + msisdn + "</msisdn>";
		aXML += "<valor>" + valor + "</valor>";
		aXML += "<cpfCnpj>" + cpf + "</cpfCnpj>";
		aXML += "<categoria>" + categoria + "</categoria>";
		aXML += "<hashCc>" + hashCartao + "</hashCc>";
		aXML += "<sistemaOrigem>" + sistemaOrigem + "</sistemaOrigem>";
		aXML += "</GPPConsultaPreRecarga>";
		
		return execute().consultaAssinanteRecargaXML(aXML);
	}
	
	public static String getRecargaAssinante(
			String msisdn,
			double valor,
			String cpf,
			short categoria,
			String hashCartao,
			String sistemaOrigem) throws Exception{
		
		return execute().consultaAssinanteRecarga(msisdn,valor,cpf,categoria,hashCartao,sistemaOrigem);
	}
	
	public static String getComprovanteServico(
			String msisdn,
			String dataInicial,
			String dataFinal) throws Exception{
		return execute().consultaExtrato(msisdn,dataInicial,dataFinal);
	}
}
