package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioBaseAparelhosGSM;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.brasiltelecom.ppp.action.base.ShowActionHibernate;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import br.com.brasiltelecom.ppp.util.treemenu.GrupoItem;
import br.com.brasiltelecom.ppp.util.treemenu.OpcaoItem;
import br.com.brasiltelecom.ppp.util.treemenu.TreeMenu;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.CodigoNacional;
import com.brt.gpp.comum.mapeamentos.entidade.OperadoraLD;
import br.com.brasiltelecom.ppp.dao.CategoriaDAO;
import br.com.brasiltelecom.ppp.dao.CodigoNacionalDAO;
import br.com.brasiltelecom.ppp.dao.OperadoraLDDAO;

/**
 * Mostra a tela de Filtro de Relatório de Base de Aparelhos GSM
 * 
 * @author Anderson Jefferson Cerqueira
 * @since 29/02/2008
 */
public class ShowFiltroConsultaBaseAparelhosGSMAction extends ShowActionHibernate 
{

	private String codOperacao = Constantes.MENU_REL_BASE_APARELHOS;
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela() 
	{
		return "filtroConsultaRelBaseAparelhosGSM.vm";
	}


	/**
	 * Implementa a lógica de negócio dessa ação.
	 * 
	 * @param context 			Contexto do Velocity.
	 * @param request 			Requisição HTTP que originou a execução dessa ação.
	 * @param sessionFactory	Factory de sessões para acesso ao banco de dados (Hibernate).
	 */
	public void updateVelocityContext(VelocityContext context,
		   HttpServletRequest request, SessionFactory sessionFactory) throws Exception
	{
		logger.info("Redireciona para a tela de filtro da consulta relacao de base de aparelhos GSM");
	}

	/**
	 * @return Nome da operação (permissão) associada a essa ação.
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}