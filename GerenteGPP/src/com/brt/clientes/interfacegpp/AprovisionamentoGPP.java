
package com.brt.clientes.interfacegpp;

import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;

/**
 * Classe que se conecta ao GPP
 * @author Alex Pitacci Simões
 * @since 02/06/2004
 */
public class AprovisionamentoGPP {

	
	private static aprovisionamento execute(){
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		
		byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();
		
		aprovisionamento pPOA = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
	
		return pPOA;
	}
	
	public static short getAtivacao( 
			String msisdn, 
			String simcard,
			String planoPreco,
			double creditoInicial,
			short idioma,
			String operador) throws Exception{
		
		return execute().ativaAssinante(msisdn,simcard,planoPreco,creditoInicial,idioma,operador);
	}
	
	public static retornoDesativacaoAssinante getDesativacao(
			String msisdn,
			String motivo,
			String operador) throws Exception{
		
		return execute().desativaAssinante(msisdn,motivo,operador); 
	}
	
	public static short getBloqueio(
			String msisdn,
			String motivo,
			double tarifa,
			String operador) throws Exception{
		
		return execute().bloqueiaAssinante(msisdn,motivo,tarifa,operador);
	}
	
	public static short getDesbloqueio(
			String msisdn,
			String operador) throws Exception{
		return execute().desbloqueiaAssinante(msisdn,operador);
	}
	
	public static short getTrocaMsisdn(
			String msisdnAntigo,
			String msisdnNovo,
			String id,
			double tarifa,
			String operador) throws Exception{
		return execute().trocaMSISDNAssinante(msisdnAntigo,msisdnNovo,id,tarifa,operador);
	}
	
	public static short getTrocaPlanoPreco(
			String msisdn,
			String codigoPlano,
			double tarifa,
			double franquia,
			String operador) throws Exception{
		return execute().trocaPlanoAssinante(msisdn,codigoPlano,tarifa,operador,tarifa);
	}
	public static short getTrocaSimcard(
			String msisdn,
			String simcard,
			double tarifa,
			String operador) throws Exception{
		return execute().trocaSimCardAssinante(msisdn,simcard,tarifa,operador);
	}
	public static short getFriendsFamily(
			String msisdn,
			String ff,
			String operador) throws Exception{
		return execute().atualizaFriendsFamilyAssinante(msisdn,ff,operador);
	}
	public static String getTrocaSenha(
			String msisdn,
			String senha) throws Exception{
		String aXML = "<?xml version=\"1.0\"?>";
		aXML += "<GPPTrocaSenha>";
		aXML += "<msisdn>" + msisdn + "</msisdn>";
		aXML += "<novaSenha>" + senha + "</novaSenha>";
		aXML += "</GPPTrocaSenha>";
		return execute().trocaSenha(aXML);
	}
	public static String getAssinante(String msisdn) throws Exception{
		return execute().consultaAssinante(msisdn);
	}
	
}
