package br.com.brasiltelecom.ppp.action.relatoriosMarketing.relatorioMKTGerencialOptIn;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ShowAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Mostra a tela de relatorio gerencial Opt-in
 * Se encontra no novo modelo para testes da simplificação dos templates do portal
 * 
 * @author Magno Batista Corrêa
 * @since 2006/07/26
 */
public class ShowFiltroConsultaRelMKTGerencialOptInAction extends ShowAction
{
	private String codOperacao;

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#getTela()
	 */
	public String getTela()
	{
		return "_filtroConsultaRelUnico.vm";
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ShowAction#updateVelocityContext(org.apache.velocity.VelocityContext, javax.servlet.http.HttpServletRequest, org.exolab.castor.jdo.Database)
	 */
	public void updateVelocityContext(
		VelocityContext context,
		HttpServletRequest request,
		Database db)
		throws Exception 
	{
		/** <Magno>Abrir uma entrada em Constantes.java</Magno>*/
			this.codOperacao =  Constantes.MENU_MKT_GERENCIAL_OPT_IN;
			String imagemSrc = "img/tit_rel_mkt_gerencial_opt_in.gif";
			String imagemAlt = "Relatório Gerencial Opt-in";
			String frmAction = "ConsultaRelMKTGerencialOptInAction.do";
			String[] camposEntrada = {"DataOuPeriodo", "GranulosidadeData"};
			String campoFormatosSaida = "ComboDiversosFormatos";

			context.put("imagemSrc", imagemSrc);
			context.put("imagemAlt", imagemAlt);
			context.put("frmAction", frmAction);
			context.put("camposEntrada", camposEntrada);
			context.put("campoFormatosSaida", campoFormatosSaida);
	}

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return codOperacao;
	}
}
