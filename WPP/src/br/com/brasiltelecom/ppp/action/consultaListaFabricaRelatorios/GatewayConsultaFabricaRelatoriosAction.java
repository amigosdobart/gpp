package br.com.brasiltelecom.ppp.action.consultaListaFabricaRelatorios;

import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ParametrosFabricaRelatorios;
import br.com.brasiltelecom.ppp.action.base.fabricaRelatorios.ShowConsultaFabricaRelatoriosAction;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Essa Action � executada quando o usu�rio escolhe um relatorio a ser
 * consultado na p�gina gerada por ShowConsultaListaRelatoriosAction.
 * 
 * O objetivo desse gateway � carregar as configura��es (instancia de
 * ParametrosFabricaRelatorios) necess�rias para fazer o redirecionamento
 * para a p�gina de gerencia dos arquivos do relatorio (a qual permite
 * download e upload). Vide caso de uso:
 * 
 * UC093_WPP_GerenciaArquivosF�bricaRelat�rios
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