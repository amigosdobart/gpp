package br.com.brasiltelecom.ppp.action.contestacaoConsulta;

import javax.servlet.http.HttpServletRequest;

import org.exolab.castor.jdo.Database;
import org.apache.velocity.VelocityContext;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta de histórico de boletim de sindicância
 * 
 * @author Bernardo Dias
 * @since 25/02/2008
 */
public class FiltroBSContestacaoAction extends ShowAction 
{        
    private String codOperacao = Constantes.MENU_CONTESTACAO_HIS;

    public String getTela()
    {
        return "filtroBSContestacao.vm";
    }

    public void updateVelocityContext(VelocityContext context,
            HttpServletRequest request, Database db) throws Exception
    {
        // Apenas mostra o filtro
    }

    public String getOperacao()
    {
        return codOperacao;
    }
}