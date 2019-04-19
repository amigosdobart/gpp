/**
 * 
 */
package br.com.brasiltelecom.ppp.action.consultaComparativoPlano;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Osvaldeir
 *
 */
public class GrupoSumarioServico
{
	private String titulo;	                       
	private Collection sumarios = new ArrayList(); // linha da tabela
    
    public Collection getSumarios()
    {
        return sumarios;
    }
    public void setSumarios(Collection sumarios)
    {
        this.sumarios = sumarios;
    }
    public String getTitulo()
    {
        return titulo;
    }
    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }
	
}
