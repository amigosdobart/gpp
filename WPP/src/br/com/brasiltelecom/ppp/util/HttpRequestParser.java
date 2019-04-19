package br.com.brasiltelecom.ppp.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * Interpreta um formulario HTML do tipo Multipart
 * 
 * @author Bernardo Vergne Dias
 * @since 19/12/2006
 */
public class HttpRequestParser 
{
    /**
     * Converte os parametros do request para um Properties (mapa da Strings)
     * excluindo aqueles que valem "" (string vazia).
     * 
     * @param request Instancia de <code>HttpServletRequest</code>
     */ 
    public static Properties getParametros(HttpServletRequest request)
    {
        Properties properties = new Properties();
        Map parametros = request.getParameterMap();
        
        for (Iterator it = parametros.keySet().iterator(); it.hasNext(); )
        {
            Object key = it.next();
            String[] value = (String[])parametros.get(key);
            if (value != null && value.length == 1 && !value[0].equals(""))
                properties.put((String)key, value[0]);
        }
        
        return properties;
    }
}
