package br.com.brasiltelecom.ppp.action.consultaComparativoPlano;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Osvaldeir
 * 
 */
public class GrupoSumarioServicoDAO
{
    private Session session;
    private String msisdn;
    private Date datInicio; // Inclusivo
    private Date datFim;    // Exclusivo
    private Collection meses = new ArrayList(0);

    private String SQL_SERVICOS_ML = 
              " SELECT TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                  "      
            + "        ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                  "
            + "  FROM tbl_ger_cdr                                                 "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND (TIP_CHAMADA LIKE 'VC1MF-%' OR TIP_CHAMADA LIKE 'VC1MFI%')  "
            + "   GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                           ";
    
    private String SQL_SERVICOS_LL =
              " SELECT TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                  "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "  FROM tbl_ger_cdr                                                 "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'VCMMI%'                                   "
            + "   GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                           ";
    
    private String SQL_SERVICOS_MM =
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 " 
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "  FROM tbl_ger_cdr                                                 "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'VCMM-%'                                   "
            + "   GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                           ";

    private String SQL_CHAMADAS_II = 
              " SELECT TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                  "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                " 
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'RVC_MMI%'                                 " 
            + "   AND SUBSTR(TIP_CHAMADA,4,1) != '1'                              " 
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";
    
    private String SQL_CHAMADAS_I_III = 
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                " 
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'RVC_MM-%'                                 "
            + "   AND SUBSTR(TIP_CHAMADA,4,1) != '1'                              " 
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";

    private String SQL_DADOS_SMS = 
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND (TIP_CHAMADA LIKE 'SMOOUTRAS%'                              "
            + "     OR TIP_CHAMADA LIKE 'SMOBRT%'                                 "
            + "     OR TIP_CHAMADA LIKE 'SMOEXTERIOR')                            "
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";
    
    private String SQL_DADOS_MMS = 
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'MM%'                                      "
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";
    
    private String SQL_DADOS_GPRS = 
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'GPRS%'                                    "
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";
    
    private String SQL_DADOS_WAP = 
              " SELECT  TO_CHAR(TIMESTAMP,'MM/YYYY')     AS DATA,                 "
            + "       ROUND((SUM(CALL_DURATION/60)),2) AS VALOR                   "
            + "   FROM tbl_ger_cdr                                                "
            + " WHERE SUB_ID = :msisdn                                            "
            + "   AND TIMESTAMP >= :datInicio                                     "
            + "   AND TIMESTAMP < :datFim                                         "
            + "   AND TIP_CHAMADA LIKE 'WAP%'                                     "
            + "    GROUP BY TO_CHAR(TIMESTAMP,'MM/YYYY')                          ";
    

    public GrupoSumarioServicoDAO(String msisdn, Session session) throws Exception
    {
        this.msisdn = msisdn;
        this.session = session;
        
        // Gera a lista dos meses de consulta (3 meses, com atual incluso)
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(sdf.format(cal.getTime())));
        
        datFim = cal.getTime(); // Primeiro dia do mes corrente
        
        cal.add(Calendar.MONTH, -1);
        meses.add(sdf.format(cal.getTime())); // mes passado 
        
        cal.add(Calendar.MONTH, -1);
        meses.add(sdf.format(cal.getTime())); 
        
        cal.add(Calendar.MONTH, -1);
        meses.add(sdf.format(cal.getTime()));
               
        datInicio = cal.getTime();
    }

    public GrupoSumarioServico findSumarioServicoBrt()
    {
        GrupoSumarioServico grupo = new GrupoSumarioServico();
        grupo.setTitulo("Servicos Brasil Telecom GSM (Minutos)");
        
        // Faz as consultas de ServicoBrt
        
        addSumario(grupo, SQL_SERVICOS_ML, "Local Movel-Fixo");
        addSumario(grupo, SQL_SERVICOS_LL, "Local Movel-Movel Brt");
        addSumario(grupo, SQL_SERVICOS_MM, "Local Movel-Movel Nao-Brt");

        return insereTotal(grupo);
    }

    public GrupoSumarioServico findSumarioChamadasRoaming()
    {
        GrupoSumarioServico grupo = new GrupoSumarioServico();
        grupo.setTitulo("Chamadas Recebidas em Romaing (Minutos)");
        
        // Faz as consultas de chamadas
        
        addSumario(grupo, SQL_CHAMADAS_II, "Recebidas Roaming Região II");
        addSumario(grupo, SQL_CHAMADAS_I_III, "Recebidas Roaming Regiões I e III");

        return insereTotal(grupo);
    }

    public GrupoSumarioServico findSumarioDadosEvento()
    {
        GrupoSumarioServico grupo = new GrupoSumarioServico();
        grupo.setTitulo("Serviços de Dados por Evento");
        
        // Faz as consultas de Dados
        
        addSumario(grupo, SQL_DADOS_SMS, "Torpedo (Quantidade)");
        addSumario(grupo, SQL_DADOS_MMS, "Torpedo Multimidia (Quantidade)");
        addSumario(grupo, SQL_DADOS_GPRS, "GPRS (KB)");
        addSumario(grupo, SQL_DADOS_WAP, "WAP (KB)");

        return insereTotal(grupo);
    }
    
    private GrupoSumarioServico insereTotal(GrupoSumarioServico grupo)
    {
        SumarioServico sumarioTotal = new SumarioServico();
        sumarioTotal.setDescricaoServico("Total:");
        
        // Inicializa o totalizador
        
        for (Iterator it = meses.iterator(); it.hasNext();)
            sumarioTotal.getValores().put(it.next(), new Double(0));
 
        // Normaliza (gravando 0,00 nos valores nulos)
        for (Iterator it = grupo.getSumarios().iterator(); it.hasNext();)
        {
            SumarioServico sumario = (SumarioServico)it.next();
            for (Iterator it2 = meses.iterator(); it2.hasNext();)
            {
                String mes = (String)it2.next();
                if (!sumario.getValores().containsKey(mes))
                    sumario.getValores().put(mes, new Double(0));
                
                double total = ((Double) sumario.getValores().get(mes)).doubleValue();
                total += ((Double) sumarioTotal.getValores().get(mes)).doubleValue();
                sumarioTotal.getValores().put(mes, new Double(total));
            }
        }
        
        grupo.getSumarios().add(sumarioTotal);
        return grupo;
    }
    
    private void addSumario(GrupoSumarioServico grupo, String sqlQuery, String descricao)
    {
        SumarioServico sumario = new SumarioServico();
        sumario.setDescricaoServico(descricao);
        
        // Consulta os valores
        
        Query query = session.createSQLQuery(sqlQuery).
                         addScalar("DATA", Hibernate.STRING).
                         addScalar("VALOR", Hibernate.DOUBLE);

        query.setString("msisdn", msisdn);
        query.setDate("datInicio", datInicio);
        query.setDate("datFim", datFim);

        Iterator linha = query.list().iterator();

        for (; linha.hasNext(); )
        {
            Object[] dados = (Object[])linha.next();        
            sumario.getValores().put((String)dados[0], (Double)dados[1]);           
        }
        
        grupo.getSumarios().add(sumario);   
    }

    public Collection getMeses()
    {
        return meses;
    }
    
    
 }
