package br.com.brasiltelecom.ppp.util.BS;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.brt.gpp.comum.mapeamentos.entidade.Contestacao;
import com.brt.gpp.comum.mapeamentos.entidade.ContestacaoChamadas;
import com.brt.gpp.comum.mapeamentos.entidade.ItemContestacao;

/**
 * Classe para criação do XML para envio da contestação
 * @author Alex Pitacci Simões
 * @since 20/05/2004
 * 
 * Atualizado por Bernardo Dias
 * Data: 13/02/2008
 * Melhoramentos, correções e simplifacações
 */
public class IntPPPOutXml 
{
    private static SimpleDateFormat sdf         = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat sdfDuracao  = new SimpleDateFormat("HHmmss");
    private static DecimalFormat vdf            = new DecimalFormat("0000000000000");
    private static Logger logger                = Logger.getLogger(IntPPPOutXml.class);
        
    /**
     * Cria XML
     */
    public static String getXml(Contestacao contestacao, ItemContestacao item, String planoPreco, long idEvento) 
    {
        Date data = new Date();     
        StringBuffer xmlDocument = new StringBuffer();
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.SECOND, (item.getCallDuration() != null) ? item.getCallDuration().intValue() : 0);
       
        String duracao              = sdfDuracao.format(cal.getTime());
        String callId               = item.getCallId();
        String idMotivoContestacao  = String.valueOf(contestacao.getMotivoContestacao().getIdMotivoContestacao());
        String msisdnDestino        = (callId != null) ? formataMsidnDestino(callId) : lPad("0",17,'0');
  
        xmlDocument.append("<root>");
        xmlDocument.append(    "<header>");
        xmlDocument.append(         "<evento>"           + rPad("CRITEMBS",20,' ')                                              + "</evento>");
        xmlDocument.append(         "<idEvento>"         + lPad(String.valueOf(idEvento),20,'0')                         + " </idEvento>");
        xmlDocument.append(    "</header>");
        xmlDocument.append(    "<sistemas><sistema>WPPP</sistema></sistemas>");
        xmlDocument.append(    "<USER>");
        xmlDocument.append(       "<numeroBS>"           + lPad(contestacao.getNumeroBS(),11,'0')                               + "</numeroBS>");
        xmlDocument.append(       "<numeroContrato>"     + lPad(formatMSISDN(contestacao.getIdtMsisdn()),10,'0')                + "</numeroContrato>");
        xmlDocument.append(       "<statusBS>A</statusBS>");
        xmlDocument.append(       "<datahoraAbertura>"   + sdf.format(data)                                                     + "</datahoraAbertura>");
        xmlDocument.append(       "<motivoReclamacao>"   + lPad(idMotivoContestacao,2,'0')                                      + "</motivoReclamacao>");
        xmlDocument.append(       "<descricao><![CDATA[" + rPad(contestacao.getDesContestacao(),1050,' ')                       + "]]></descricao>");
        xmlDocument.append(       "<operador>"           + rPad(contestacao.getIdUsuario(),10,' ').substring(0,9)               + "</operador>");
        xmlDocument.append(       "<canal>"              + contestacao.getCanalOrigemBS().getIdCanalOrigemBS()                  + "</canal>");
        xmlDocument.append(       "<plano>"              + planoPreco                                                           + "</plano>");
        xmlDocument.append(       "<item>"); 
        xmlDocument.append(          "<msisdnOrigem>"    + formatMSISDN(item.getSubId())                             + "</msisdnOrigem>");
        xmlDocument.append(          "<msisdnDestino>"   + msisdnDestino                                                        + "</msisdnDestino>");
        xmlDocument.append(          "<datahoraServico>" + sdf.format(item.getTimestamp())                           + "</datahoraServico>");   
        xmlDocument.append(          "<tipoTarifacao>"   + formataTipoTarifacao(item)                                + "</tipoTarifacao>"); 
        xmlDocument.append(          "<codigoServico>"   + lPad(String.valueOf(item.getIdtCodigoServicoSFA()),5,'0') + "</codigoServico>");
        xmlDocument.append(          "<duracao>"         + duracao                                                              + "</duracao>");
        xmlDocument.append(          "<valor>"           + vdf.format(item.getVlrTotal())                            + "</valor>");
        xmlDocument.append(       "</item>");   
        xmlDocument.append(    "</USER>");
        xmlDocument.append("</root>");
        
        logger.debug(xmlDocument.toString());       
        return xmlDocument.toString();
    }
    
    /* ******************************
     *       METODOS PRIVADOS
     * *******************************/
    
    private static String formatMSISDN(String msisdn)
    {
        if (msisdn != null && msisdn.length() == 12)    
            return msisdn.substring(2);
        else                
            return msisdn;
    }

    private static String lPad(String origem, int repeticao, char caracter)
    {
        String appendedStr = "";
        for (int i = 0; i < repeticao - origem.length(); i++)
            appendedStr += Character.toString(caracter);

        return appendedStr + origem;
    }

    private static String rPad(String origem, int repeticao, char caracter)
    {
        String appendedStr = "";
        for (int i = 0; i < repeticao - origem.length(); i++)
            appendedStr += Character.toString(caracter);

        return origem + appendedStr;
    }

    private static String formataMsidnDestino(String msisdn)
    {
        String result = "0";
        if (msisdn != null)
        {
            result = msisdn.replaceAll("\\*", "").replaceAll("\\#", "");
            try
            {
                result = Long.toString(Long.parseLong(result));
            } 
            catch (NumberFormatException ignorado)
            {
                result = "0";
            }
        }
        return lPad(result, 17, '0');

    }

    /**
     * Transforma do tipo de tarifacao de Normal, Reduzida, Flat para 1, 2, 3
     * respectivamente. Se tipo for null retorna 0.
     * 
     * @param tipo
     * @return String
     */
    private static String formataTipoTarifacao(ItemContestacao item)
    {
        String result = "0";
        String tipo = (item instanceof ContestacaoChamadas) ? ((ContestacaoChamadas)item).getIdtModulacao() : null; //TODO isso mesmo???
        
        if (tipo == null)                       result = "0";
        else if (tipo.equalsIgnoreCase("N"))    result = "1";
        else if (tipo.equalsIgnoreCase("R"))    result = "2";
        else if (tipo.equalsIgnoreCase("F"))    result = "3";
        
        return result;
    }       
}