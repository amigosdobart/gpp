/*
 * Created on 25/03/2004
 *
 */
package br.com.brasiltelecom.ppp.action.consultaRecarga;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.omg.CORBA.ORB;

import org.apache.log4j.Logger;

import com.brt.gpp.componentes.recarga.orb.recarga;
import com.brt.gpp.componentes.recarga.orb.recargaHelper;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.RecargaHome;
import br.com.brasiltelecom.ppp.interfacegpp.GerenteORB;
import br.com.brasiltelecom.ppp.portal.entity.UsedVoucher;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Consulta a recarga/ajustes 
 * 
 * @author André Gonçalves
 * @since 21/05/2004
 */
public class ExecutaRecargaAction extends ActionPortal {
	
	private String codOperacao;
	Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(
				ActionMapping actionMapping,
				ActionForm actionForm,
				HttpServletRequest request,
				HttpServletResponse response,
				Database db)
				throws Exception
	{
		String servidor = (String)servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
		String porta    = (String)servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
		String usuario  = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO)).getMatricula();
		String mensagem="";
		db.begin();
		logger.info("Executa recarga de cartao temporario ligmix");
		try
		{
			String voucherNo = request.getParameter("voucherno");
			String msisdn    = "55"+request.getParameter("msisdn");
			
			UsedVoucher used = RecargaHome.findVoucherByNo(voucherNo,db);
			if (used == null)
				mensagem = "Cartao numero:"+voucherNo+" nao foi encontrado";
			else
			{
				if (used.getSubId() != null || (used.getSubId() != null && !("".equals(used.getSubId()))) )
						mensagem = "Cartao numero:"+voucherNo+" ja foi utilizado por:"+msisdn;
				else
				{
					// Caso o cartao nao tenha sido utilizado, entao realiza a recarga
					// atraves do GPP para o assinante. Em caso de retorno ok da recarga
					// entao atualiza a informacao do assinante na tabela temporaria
					// de usedvoucher
					short ret = executaRecarga(servidor,porta,msisdn,voucherNo,used.getFaceValue(),usuario);
					if (ret == Constantes.GPP_RET_OPERACAO_OK)
					{
						RecargaHome.atualizaCartao(used,msisdn,db);
						db.commit();
						mensagem = "Recarga de R$"+used.getFaceValue()+" do cartao:"+voucherNo+" efetuada com sucesso para o assinante:"+msisdn;
					}
					else
						mensagem = "Recarga nao efetuada devido ao erro:"+ret;
				}
			}
		}
		catch (Exception pe)
		{
			mensagem = "Erro ao executar recarga. Erro:"+pe.getMessage();
		}
		finally
		{
			db.rollback();
			db.close();
		}
		request.setAttribute(Constantes.MENSAGEM,mensagem);
		return actionMapping.findForward("success");
	}
		
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */

	public String getOperacao() {
		return this.codOperacao;
	}
	
	public short executaRecarga(String servidor, String porta, String msisdn, String voucherNo, double valor, String usuario)
	{
		short ret = -1;
		ORB orb = GerenteORB.getORB(servidor, porta);
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			ret = pPOA.executaRecarga(msisdn
					                 ,"02000"
					                 ,voucherNo
					                 ,"00"
					                 ,valor
					                 ,sdf.format(Calendar.getInstance().getTime())
					                 ,"PPP"
					                 ,usuario
					                 ,""
					                 ,""
					                 ,""
					                 );
		}
		catch (Exception e) 
		{
			logger.error("Erro ao executar a recarga no gpp. Erro:"+e.getMessage());
		}
		return ret;
	}
}