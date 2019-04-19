/*
 * Created on 23/06/2004
 *
 */
package com.brt.consultas.action.poolConexoes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.brt.clientes.action.base.ActionPortal;
import com.brt.clientes.interfacegpp.GerenteORB;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPP;
import com.brt.gpp.componentes.gerenteGPP.orb.gerenteGPPHelper;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 * @author André Gonçalves
 * @since 23/06/2004
 */
public class ConsultaPoolConexoesAction extends ActionPortal {
	
	private gerenteGPP pPOA;
	private static final int CRIAR = 0;
	private static final int REMOVER = 1;

	/* (non-Javadoc)
	 * @see com.brt.clientes.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward performPortal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		//Solicitando o ORB
		org.omg.CORBA.ORB orb = GerenteORB.getOrb();
		byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
		
		pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		
		String nomePool = null;
		short tipoPool;
		int acao;		
		
		try
		{
			tipoPool = Short.parseShort( request.getParameter("tipoPool") );
			nomePool = request.getParameter("nomePool");
			acao = Integer.parseInt( request.getParameter("acao") );
			
			if (acao == CRIAR)
			{
				if ( pPOA.criaConexao( tipoPool ) )
				{
					request.setAttribute("mensagem", "Conexão no pool de conexões " + nomePool +
								" foi criada com sucesso.");
				}
				else
				{
					request.setAttribute("mensagem", "Não foi possível criar conexão no pool de conexões " +
							nomePool + ".");
				}
			}
			else if (acao == REMOVER)
			{
				if ( pPOA.removeConexao( tipoPool ) )
				{
					request.setAttribute("mensagem", "Conexão no pool de conexões " + nomePool +
								" removida com sucesso.");
				}
				else
				{
					request.setAttribute("mensagem", "Não foi possível remover conexão no pool de conexões " +
							nomePool + ".");
				}
			}
		}
		catch(GPPInternalErrorException ie)
		{
			request.setAttribute("mensagem","Não foi possível efetuar alteração de conexão no pool de conexões " +
					nomePool + ". \n Erro: " + ie.getMessage() );
		}
		
		return mapping.findForward("success");
	}

}
