package br.com.brasiltelecom.ppp.action.consultaStatusRecargaRecorrente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.AssinanteControleHome;
import br.com.brasiltelecom.ppp.home.InterfaceRecargaRecorrenteHome;
import br.com.brasiltelecom.ppp.home.RecargaHome;
import br.com.brasiltelecom.ppp.model.StatusRecargasControle;
import br.com.brasiltelecom.ppp.portal.entity.AssinanteControle;
import br.com.brasiltelecom.ppp.portal.entity.InterfaceRecargaRecorrente;
import br.com.brasiltelecom.ppp.portal.entity.Recargas;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua a consulta de recargas recorrentes na tabela
 * @author  Marcelo Alves Araujo
 * @since   13/04/2006
 */
public class ConsultaStatusRecargaRecorrente extends ActionPortal
{
    Logger logger = Logger.getLogger(this.getClass());
        
    /**
     * Gera os dados necessários para apresentação
     * na consulta de status de recarga recorrente
     * @param ActionMapping
     * @param ActionForm
     * @param HttpServletRequest
     * @param HttpServletResponse
     * @param Database
     * @return ActionForward
     */
    public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db)
    {
        logger.info("Consulta às recargas recorrentes do assinante");
                    
        try
        {           
            Collection recargas = null;
            String filial = null;
            long contrato = 0;
            
            // Formato esperado da data
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // Consulta das recargas recorrentes de acordo com os parâmetros passados
            db.begin();
                        
            // Pega os parâmetros passados pela tela do portal
            boolean mostraExpiracao = new Boolean(request.getParameter("expiracao")).booleanValue();
            String msisdn = request.getParameter("msisdn");
            String dataInicial = request.getParameter("dataInicial");
            String dataFinal = request.getParameter("dataFinal");
            
            // Consultar pela tabela de recargas
            if(mostraExpiracao)
                recargas = RecargaHome.findRecargasControle(msisdn, sdf.parse(dataInicial), sdf.parse(dataFinal), db);
            // Consulta pela tabela de interface
            else
                recargas = InterfaceRecargaRecorrenteHome.findByMsisdnPeriodo(db, msisdn, sdf.parse(dataInicial), sdf.parse(dataFinal));
            
            // Formata as recargas para apresentação na tela
            recargas = formatarRecargas(recargas, mostraExpiracao);
            filial = InterfaceRecargaRecorrenteHome.findFilial(db, msisdn);
            contrato = InterfaceRecargaRecorrenteHome.findContrato(db, msisdn);
            
            AssinanteControle assinante = AssinanteControleHome.findByMsisdn(db, msisdn);            
            if(assinante != null && assinante.getIndNovoControle().equals(Constantes.SAFRA_NOVA))
                request.setAttribute("safra", "Safra Nova (Paga todo mês)");
            else if(assinante != null && assinante.getIndNovoControle().equals(Constantes.SAFRA_ANTIGA))
                request.setAttribute("safra", "Safra Antiga (Paga num mês e não paga no outro)");
            
            request.setAttribute("tamanho", recargas != null ? String.valueOf(recargas.size()): new String("0"));
            request.setAttribute("listaRecargas", recargas);
            request.setAttribute("filial", filial);
            request.setAttribute("contrato", String.valueOf(contrato));
        } 
        catch (PersistenceException e)
        {
            logger.error("Erro ao consultar recargas recorrentes do assinante");
        } 
        catch (ParseException e)
        {
            logger.error("Erro ao converte data");
        }
        
        return actionMapping.findForward(Constantes.STATUS_SUCESSO);
    }

    /**
     * Retorna o código da operação
     * @return String
     */
    public String getOperacao()
    {
        return Constantes.COD_CONSULTA_REC_RECORRENTE;
    }
    
    /**
     * Formata os dados gerados pelas consultas às tabelas de
     * recarga e de recarga recorrente em uma Collection 
     * @param Collection    -   Dados gerados na consulta à tbl_rec_recarga ou à tbl_int_recarga_recorrente
     * @param boolean       -   Indica a Collection é de recarga ou de recarga recorrente
     */
    private Collection formatarRecargas(Collection recargas, boolean conversaoRecargas) throws ParseException
    {       
        if(recargas == null)
            return null;
                
        Iterator it = recargas.iterator();        
        Collection dados = new ArrayList();
                
        if(conversaoRecargas)
        {
            Recargas rec = null;           
            while(it.hasNext())
            {
                StatusRecargasControle status = new StatusRecargasControle();
                rec    = (Recargas)it.next();
                status.setDataRecarga(rec.getDatOrigem());
                status.setDecricaoRetorno("Operação OK");
                status.setValorPrincipal(rec.getValorPrincipal());
                status.setValorBonusOnnet(rec.getValorBonus());
                status.setValorBonusOffnet(rec.getValorPeriodico());
                status.setValorBonusRecarga(0);
                
                if(rec.getTipoTransacao().equals(Constantes.FRANQUIA_CONTROLE))
                    status.setStatusRecarga("Franquia Concedida");
                else if(rec.getTipoTransacao().equals(Constantes.EXPIRACAO_CREDITOS) 
                      ||rec.getTipoTransacao().equals(Constantes.EXPIRACAO_FRANQUIA_CONTROLE))
                    status.setStatusRecarga("Franquia Expirada");
                else if(rec.getTipoTransacao().equals(Constantes.EXPIRACAO_BONUS) 
                      ||rec.getTipoTransacao().equals(Constantes.EXPIRACAO_BONUS_CONTROLE))
                    status.setStatusRecarga("Bônus Expirado");
                else
                    status.setStatusRecarga("Bônus Concedido");
                
                dados.add(status);
            }
        }
        else
        {
            InterfaceRecargaRecorrente rec = null;
            while(it.hasNext())
            {
                StatusRecargasControle status = new StatusRecargasControle();
                rec    = (InterfaceRecargaRecorrente)it.next();
                status.setDataRecarga(rec.getDataRecarga());
                status.setDecricaoRetorno(rec.getDesRetorno());
                status.setValorPrincipal(rec.getValorRecarga().doubleValue());
                status.setValorBonusOnnet(rec.getValorBonusOnnet() != null ? rec.getValorBonusOnnet().doubleValue() : 0.0);
                status.setValorBonusOffnet(rec.getValorBonusOffnet() != null ? rec.getValorBonusOffnet().doubleValue() : 0.0);
                status.setValorBonusRecarga(rec.getValorBonusRecarga() != null ? rec.getValorBonusRecarga().doubleValue() : 0.0);
                
                // Valor do bônus é pego no registro do código de faturamento, preferencialmente.
                // Obs: esse IF pode ser removido a partir de 01/08/2008
                if(rec.getCodigoFaturamento().length() > 200)
                {
                    status.setSValorBonusOnnet(rec.getCodigoFaturamento().substring(200,202)+","
                                         +rec.getCodigoFaturamento().substring(202,204));
                }

                if(rec.getCodigoRetorno().equals("0"))
                {
                    if(rec.getTipoEnvio() == Constantes.CONCESSAO_FRANQUIA)
                        status.setStatusRecarga("Franquia Concedida");
                    else if(rec.getTipoEnvio() == Constantes.CONCESSAO_BONUS)
                        status.setStatusRecarga("Bônus Concedido");
                    else
                        status.setStatusRecarga("Franquia e Bônus Concedidos"); 
                }
                else
                {
                    if(rec.getTipoEnvio() == Constantes.CONCESSAO_FRANQUIA)
                        status.setStatusRecarga("Franquia Não Concedida");
                    else if(rec.getTipoEnvio() == Constantes.CONCESSAO_BONUS)
                        status.setStatusRecarga("Bônus Não Concedido");
                    else
                        status.setStatusRecarga("Franquia e Bônus Não Concedidos");
                }
                
                dados.add(status);
            }
        }
        
        return dados;
    }
}