/*
 * Created on 03/06/2004
 *
 */
package com.brt.clientes.interfacegpp;

import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;

/**
 * @author André Gonçalves
 * @since 03/06/2004
 */
public class RecargaGPP {
	
	private static recarga execute(){
		
		// Inicializando o ORB

		ORB orb = GerenteORB.getOrb();

		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		return pPOA;
	}
	
	public static short doRecargaGenericaAssinante(
			String msisdn, 
			String tipoTransacao,
			String identificadorRecarga,
			String tipoCredito, 
			String valor, 
			String dataHora,
			String sistemaOrigem,
			String nsuInstituicao, 
			String hashCartaoCredito, 
			String cpf,
			String operador) throws Exception{
		
		return execute().executaRecarga ( msisdn, tipoTransacao, identificadorRecarga, 
				tipoCredito, Double.parseDouble(valor), dataHora, 
				sistemaOrigem, operador, nsuInstituicao,
				hashCartaoCredito, cpf );
	}
	
	public static short doValidacaoRecargaBanco (
			String msisdn, 
			String valor) throws Exception
	{
		return execute().podeRecarregar(msisdn, Double.parseDouble(valor));
	}
	
	public static short doRecargaBancoAssinante (
			String msisdn, 
			String tipoTransacao,
			String identificadorRecarga,
			String nsuInstituicao,
			String codigoLoja,
			String tipoCredito,
			String valor,
			String dataHora,
			String dataHoraBanco,
			String dataHoraContabil,
			String terminal,
			String tipoTerminal,
			String sistemaOrigem,
			String operador) throws Exception
	{
		return execute().executaRecargaBanco(msisdn, tipoTransacao, identificadorRecarga,
					nsuInstituicao,codigoLoja, tipoCredito, Double.parseDouble(valor), dataHora, dataHoraBanco,
					dataHoraContabil, terminal, tipoTerminal, sistemaOrigem, operador);		
	}
	
	public static short doAjuste(
			String msisdn, 
			String tipoTransacao,
			String tipoCredito, 
			String valor,
			String tipo,
			String dataHora,
			String sistemaOrigem,
			String operador,
			String dataExpiracao) throws Exception{
		
		return execute().executaAjuste( msisdn, tipoTransacao, tipoCredito, Double.parseDouble(valor), 
				tipo, dataHora, sistemaOrigem, operador, dataExpiracao );
	}
	
	public static String insereRecargaXML (
			String xml) throws Exception{
		
		return execute().executaRecargaXML(xml);
	}
	
	public static short doAjusteXML (
			String xml) throws Exception{
		
		return execute().executaAjusteXML(xml);
	}
	


}
