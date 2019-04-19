/* Created on 29/11/2005
 */
package br.com.brasiltelecom.ppp.action.consultaCartoesPrePago;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.interfacegpp.RecargaGPP;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.mapeamentos.MapTVMCodRetorno;

/**
 * @author Marcos C. Magalhães
 */
public class CancelaVoucherAction extends ActionPortal {
    
    private String codOperacao = Constantes.COD_CANCELAR_VOUCHER;
    Logger logger = Logger.getLogger(this.getClass());

    /* (non-Javadoc)
     * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
     */
    public ActionForward performPortal(ActionMapping actionMapping,
            ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, Database db) throws Exception {
        
        logger.info("Cancela o número de voucher solicitado");
        //codOperacao = Constantes.COD_CANCELAR_VOUCHER;
        
        Usuario usuario = ((Usuario)request.getSession().getAttribute(Constantes.USUARIO));
        String operador = usuario.getMatricula();
        
        // Busca informações de porta e servidor do GPP
        
        String servidor = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
        String porta = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
        
        String numeroVoucher = request.getParameter("numeroCartao");
        
        String comentario = operador;
        short ret = 0;
        
        try 
        {
            ret = RecargaGPP.alteraStatusVoucher(numeroVoucher, Definicoes.VOUCHER_INVALIDADO, comentario, operador, servidor, porta);
        }
        
        catch (Exception e)
        {
            request.setAttribute(Constantes.MENSAGEM,"Erro ao conectar ao GPP");
        }

        if (ret == 0)
        {
            return actionMapping.findForward("success");
        }
        else
        {
            logger.error("Erro ao tentar cancelar voucher.");
            
            MapTVMCodRetorno codErro = MapTVMCodRetorno.getInstancia();
            
            request.setAttribute(Constantes.MENSAGEM, "Erro: " + codErro.getDescricao(ret));
            
            return actionMapping.findForward("error");
        }
            
    }

    /* (non-Javadoc)
     * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
     */
    public String getOperacao() {
        return codOperacao;
    }

}
