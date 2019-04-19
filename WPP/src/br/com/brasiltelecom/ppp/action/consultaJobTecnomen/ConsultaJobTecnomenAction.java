package br.com.brasiltelecom.ppp.action.consultaJobTecnomen;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.omg.CORBA.ORB;

import com.brt.gpp.componentes.consulta.orb.consulta;
import com.brt.gpp.componentes.consulta.orb.consultaHelper;
import com.brt.gpp.componentes.consulta.orb.consultaPackage.*;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.GerenteORB;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * @author Joao Carlos
 */
public class ConsultaJobTecnomenAction extends ActionPortal {
	
	//private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) throws Exception {
		
		logger.info("Consulta às informações do job na tecnomen");
		//codOperacao = "CONSULTAR_JOB_TECNOMEN";

		// Pega o numero do job passado como parametro para a consulta
		int numeroJob = Integer.parseInt(request.getParameter("numeroJob"));
		
		// Busca informações de porta e servidor do GPP
		String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		try 
		{
			// Executa a consulta do job na Tecnomen atraves do GPP
			ORB orb = GerenteORB.getORB(servidor, porta);
			byte[] managerId = "ComponenteNegociosConsulta".getBytes();
			consulta pPOA = consultaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
			InfoJobTecnomen jobTec = pPOA.consultaJobTecnomen(numeroJob);
			// Devido ao template Velocity nao conseguir utilizar o objeto InfoJobTecnomen 
			// pois este nao possui getter e setter metodos entao o um hashMap e utilizado
			// para conter os valores da propriedade de forma que o nome e a chave
			HashMap map = new HashMap();
			map.put("numeroJob"	,new Integer(jobTec.numeroJob));
			map.put("codStatus"	,new Long(jobTec.codStatus));
			map.put("existeJob" ,new Boolean(jobTec.codStatus==3002 ? false : true));
			map.put("descStatus",jobTec.descStatus);
			map.put("workTotal"	,new Long(jobTec.workTotal));
			map.put("workDone"	,new Long(jobTec.workDone));
			map.put("percPronto",jobTec.workTotal==jobTec.workDone ? new Long(100) : new Long((jobTec.workTotal/jobTec.workDone)*100));

			request.setAttribute("infoJobTecnomen",map);
		}
		catch (Exception e)
		{
			request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
		}
		return actionMapping.findForward("success");
	}

	/* (non-Javadoc)
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() {
		return null;
	}
}
