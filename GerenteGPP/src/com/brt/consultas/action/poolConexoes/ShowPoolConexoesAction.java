/*
 * Created on 18/06/2004
 *
 */
package com.brt.consultas.action.poolConexoes;

import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.velocity.VelocityContext;
import org.omg.CORBA.ORB;

import com.brt.clientes.action.base.ShowAction;
import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP;

/**
 * @author André Gonçalves
 * @since 18/06/2004
 */
public class ShowPoolConexoesAction extends ShowAction {

	private gerenteGPP pPOA;
	
	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
	 */
	public void updateVelocityContext(VelocityContext vctx,
			HttpServletRequest request, ActionForm form) {
		// Solicitando o ORB		
		ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();

		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);

		Map poolConexoes    = new Hashtable();
		Map poolTipos       = new Hashtable();
		Map poolDisponiveis = new Hashtable();

		try
		{		
			poolTipos.put(Definicoes.CO_TECNOMEN_APR,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_APR));
			poolTipos.put(Definicoes.CO_TECNOMEN_REC,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_REC));
			poolTipos.put(Definicoes.CO_TECNOMEN_VOU,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_VOU));
			poolTipos.put(Definicoes.CO_TECNOMEN_ADM,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_ADM));
			poolTipos.put(Definicoes.CO_TECNOMEN_AGE,     new Short((short)Definicoes.CO_TIPO_TECNOMEN_AGE));
			poolTipos.put(Definicoes.CO_BANCO_DADOS_PREP, new Short((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP));
	
			poolConexoes.put(Definicoes.CO_TECNOMEN_APR,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_APR)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_REC,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_REC)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_VOU,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_VOU)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_ADM,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_ADM)));
			poolConexoes.put(Definicoes.CO_TECNOMEN_AGE,     new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_TECNOMEN_AGE)));
			poolConexoes.put(Definicoes.CO_BANCO_DADOS_PREP, new Short(pPOA.getNumerodeConexoes((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP)));

			poolDisponiveis.put(Definicoes.CO_TECNOMEN_APR,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_APR)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_REC,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_REC)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_VOU,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_VOU)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_ADM,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_ADM)));
			poolDisponiveis.put(Definicoes.CO_TECNOMEN_AGE,     new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_TECNOMEN_AGE)));
			poolDisponiveis.put(Definicoes.CO_BANCO_DADOS_PREP, new Short(pPOA.getNumeroConexoesDisponiveis((short)Definicoes.CO_TIPO_BANCO_DADOS_PREP)));

		}
		catch(GPPInternalErrorException ie)
		{
			ie.printStackTrace();		
		}
		
		vctx.put("poolConexoes",poolConexoes);
		vctx.put("poolTipos", poolTipos);
		vctx.put("poolDisponiveis",poolDisponiveis);
	}

	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ShowAction#getTela()
	 */
	public String getTela() {
		return "filtroPoolConexoes.vm";
	}

}
