package br.com.brasiltelecom.ppp.action.relatorioTeste;

import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ParametrosFabricaRelatorios;
import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ShowConsultaFabricaRelatoriosAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/*
 * Apenas define as configurações do formulário Teste
 * A implementação da página de consulta/upload está no pacote:
 * br.com.brasiltelecom.ppp.action.base.fabricaRelatorios
 * 
 */

public class ShowRelatorioTesteAction extends
		ShowConsultaFabricaRelatoriosAction 
{
	private String codOperacao = Constantes.MENU_RELATORIO_TESTE;
	private ParametrosFabricaRelatorios parametros = new ParametrosFabricaRelatorios(
			"showRelatorioTeste",
			Constantes.COD_DOWNLOAD_ENTRADA_RELATORIO_TESTE,
			Constantes.COD_DOWNLOAD_SAIDA_RELATORIO_TESTE,
			Constantes.COD_UPLOAD_ENTRADA_RELATORIO_TESTE,
			"img/tit_consulta_promocao_pula_pula.gif");

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ShowConsultaFabricaRelatorios#getParametros()
	 */
	public ParametrosFabricaRelatorios getParametros() 
	{
		return parametros;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return codOperacao;
	}
}
