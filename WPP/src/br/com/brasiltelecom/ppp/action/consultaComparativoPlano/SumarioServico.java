/**
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaComparativoPlano;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Osvaldeir
 *
 */
public class SumarioServico
{
	private String descricaoServico = "";
	
    // Map<"MM/YYYY", "valor">
	private Map valores = new HashMap();

    public String getDescricaoServico()
    {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico)
    {
        this.descricaoServico = descricaoServico;
    }

    public Map getValores()
    {
        return valores;
    }

    public void setValores(Map valores)
    {
        this.valores = valores;
    }
}
