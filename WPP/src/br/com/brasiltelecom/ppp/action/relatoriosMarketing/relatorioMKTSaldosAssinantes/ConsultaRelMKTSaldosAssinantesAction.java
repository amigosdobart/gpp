package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTSaldosAssinantes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * 
 * @author Gustavo Gusmão
 * @since 09/03/2006
 *
 */
public class ConsultaRelMKTSaldosAssinantesAction extends ActionPortal
{
    private String codOperacao;
    private Logger logger = Logger.getLogger(this.getClass());
    
    public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
    {
        logger.info("Consulta relatório de markting de saldos de assinantes");
        
        this.codOperacao = Constantes.COD_CONSULTAR_SALDO_ASSINANTES;

        Object[][] saldos = {{"-1", "-1", "-1", "-1"}, 
                {"Saldo Principal", "Saldo de SMS", "Saldo de Bonus", "Saldo de Dados"}};
        String[][] status = {{"1", "2", "3", "4", "5"}, 
                {"First Time User", "Ativo", "Recarga Expirada", "Desconectado", "Desligado"}};
        
        int indiceStatus = Integer.parseInt(request.getParameter("status"));
        int indiceSaldo = Integer.parseInt(request.getParameter("tipoSaldo"));
        DecimalFormat dFormat = new DecimalFormat("#00,00");
        double saldo = dFormat.parse(request.getParameter("saldoMinimo")).doubleValue();
        saldos[0][indiceSaldo] = new Double(saldo);
        //saldos[0][indiceSaldo]=request.getParameter("saldoMinimo");
        //saldos[0][indiceSaldo] = Long.toString(Math.round(saldo));

        String endereco = "./relatoriosWPP?NOME_RELATORIO=MKTSaldosAssinantesDelimited" +
                          "+DECODE_URL"+
                          "+ARQUIVO_PROPRIEDADES=/relatorio/Marketing.xml"+
                          "+SALDO_PRINCIPAL="+saldos[0][0]+
                          "+SALDO_SMS="+saldos[0][1]+
                          "+SALDO_BONUS="+saldos[0][2]+
                          "+SALDO_GPRS="+saldos[0][3]+
                          "+STATUS="+status[0][indiceStatus]+
                          "+SALDO_UTILIZADO="+saldos[1][indiceSaldo]+
                          "+STATUS_UTILIZADO="+status[1][indiceStatus]+
                          "+VALOR_SALDO="+saldos[0][indiceSaldo]+
                          "+DATA_EXECUCAO="+new SimpleDateFormat(Constantes.DATA_FORMATO).format(Calendar.getInstance().getTime());
            
        
        request.setAttribute("endereco",endereco);
        request.setAttribute(Constantes.MENSAGEM, "Consulta ao relatório de marketing de resumo de credito pula-pula");
        ActionForward result = actionMapping.findForward("redirect");
        
        return result;
        
    }

    public String getOperacao()
    {
        return codOperacao;
    }
}