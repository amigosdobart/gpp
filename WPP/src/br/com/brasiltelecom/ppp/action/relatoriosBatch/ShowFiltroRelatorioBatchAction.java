package br.com.brasiltelecom.ppp.action.relatoriosBatch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.home.SolicitacoesRelatorioHome;
import br.com.brasiltelecom.ppp.model.RelatoriosBatch;
import br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de consulta dos relat�rios batch executados
 * 
 * @author Marcelo Alves Araujo
 * @since 19/09/2005
 */
public class ShowFiltroRelatorioBatchAction extends ShowAction 
{
	private String codOperacao = Constantes.MENU_RELATORIO_CONCLUIDO;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroRelatorioConcluido.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(VelocityContext context,HttpServletRequest request,Database db) throws Exception 
	{
		// Pega dados do usu�rio conectado
		Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
		db.begin();
		// Pega todos os relat�rios solicitados pelo usu�rio
		Collection relatorio = SolicitacoesRelatorioHome.findByOperador(db,login.getMatricula());
		// Cria um cole��o temporaria dos dados
		Collection altRel = new ArrayList();
		
		// Seta o diret�rio em que est�o os arquivos
		for(Iterator it = relatorio.iterator();it.hasNext();)
		{
			RelatoriosBatch rel = new RelatoriosBatch((SolicitacoesRelatorio)it.next());
			rel.setEnderecoArquivo(	servlet.getServletContext().getAttribute(Constantes.ENDERECO_PORTAL)+
									"carregaArquivo.do?operador="+ rel.getOperador()+
									"&id="+rel.getSolicitacao());
			altRel.add(rel);			
		}
		
		// Cole��o com os dados do arquivo
		context.put("arquivos", altRel);
		
		// Conclui a opera��o com o banco
		db.commit();
		db.close();
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
