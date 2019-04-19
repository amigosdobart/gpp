package br.com.brasiltelecom.ppp.action.consultaListaFabricaRelatorios;

import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ParametrosFabricaRelatorios;
import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ShowConsultaFabricaRelatoriosAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Essa Action é executada quando o usuário escolhe um relatorio a ser
 * consultado na página gerada por ShowConsultaListaRelatoriosAction.
 * 
 * O objetivo desse gateway é carregar as configurações (instancia de
 * ParametrosFabricaRelatorios) necessárias para fazer o redirecionamento
 * para a página de gerencia dos arquivos do relatorio (a qual permite
 * download e upload). Vide caso de uso:
 * 
 * UC093_WPP_GerenciaArquivosFábricaRelatórios
 * 
 * @author Bernardo Vergne Dias
 * @since 04/12/2006
 *
 */
public class GatewayConsultaFabricaRelatoriosAction extends 
		ShowConsultaFabricaRelatoriosAction 
{
	private String codOperacao = Constantes.MENU_FABRICA_RELATORIOS;
	
	private ParametrosFabricaRelatorios parametros;
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ShowConsultaFabricaRelatorios#getParametros()
	 */
	public ParametrosFabricaRelatorios getParametros() 
	{
		if (parametros == null) 
		{
			
			parametros = new ParametrosFabricaRelatorios(
					"gatewayConsultaFabricaRelatorios",
					codOperacao,
					codOperacao,
					codOperacao,
					"img/tit_fabrica_relatorios_gpp.gif");
		}
		
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