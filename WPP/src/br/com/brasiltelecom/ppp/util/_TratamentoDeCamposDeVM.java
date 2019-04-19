package br.com.brasiltelecom.ppp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Contém todos os tratamentos para os campos possíveis para VMs de relatório.
 * @author Magno Batista Corrêa
 *
 */
public class _TratamentoDeCamposDeVM {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATA_FORMATO);
	private static SimpleDateFormat sdfMesAno = new SimpleDateFormat(Constantes.MES_FORMATO_CURTO);

	/**
	 * Construtor
	 *
	 */
	public _TratamentoDeCamposDeVM() {
	}
	
	/**
	 * Coloca nos parâmetros a informação de Data inicial e Data final 
	 * @param request
	 * @param parametros 
	 * @param valores 
	 * @throws ParseException
	 */
	public synchronized void getDataOuPeriodo(HttpServletRequest request, ArrayList parametros, ArrayList valores) throws ParseException
	{
		String tipoPeriodo=request.getParameter("tipoPeriodo");
		String dataInicial = null;
		String dataFinal = sdf.format(new Date());
		if (tipoPeriodo != null)
		{
			if (tipoPeriodo.equals("P") && !"0".equals(request.getParameter("periodo")))
			{
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_YEAR,-1*Integer.parseInt(request.getParameter("periodo")));
				dataInicial = sdf.format(c.getTime());
			}
			else if (tipoPeriodo.equals("D") && !"".equals(request.getParameter("dataInicial")) && !"".equals(request.getParameter("dataFinal")))
			{
				dataInicial = sdf.format(sdf.parse(request.getParameter("dataInicial")));
				dataFinal = sdf.format(sdf.parse(request.getParameter("dataFinal")));
			}
		}
		parametros.add("DATA_INICIAL");
		valores.add(dataInicial);
		parametros.add("DATA_FINAL");
		valores.add(dataFinal);
	}

	/**
	 * Coloca nos parâmetros a informação de Granulosidade 
	 * @param request
	 * @param parametros
	 * @param valores
	 */
	public synchronized void getGranulosidadeData(HttpServletRequest request, ArrayList parametros, ArrayList valores)
	{
		String granulosidadeData = request.getParameter("granulosidadeData");
		//TODO TIRAR OS HARD-CODE E TORNÁ-LOS TODOS IGUAIS.
		if (!"0".equals(granulosidadeData))
		{
			parametros.add("MASCARA_TEMPO");
			valores.add(granulosidadeData);
		}
	}
	
	/**
	 * Coloca nos parâmetros a informação de Mês inicial e Mês final 
	 * @param request
	 * @param parametros
	 * @param valores
	 */
	public synchronized void getCampoIntervaloDataPorMes(HttpServletRequest request, ArrayList parametros, ArrayList valores)
	{
		String mesInicial = request.getParameter("mesInicial");
		String mesFinal   = request.getParameter("mesFinal");
		if (!"".equals(mesInicial) && !"".equals(mesFinal))
		{
			try {
				mesInicial = sdfMesAno.format(sdfMesAno.parse(request.getParameter("mesInicial")));
				mesFinal   = sdfMesAno.format(sdfMesAno.parse(request.getParameter("mesFinal")));

				parametros.add("MES_INICIAL");
				valores.add(mesInicial);
				parametros.add("MES_FINAL");
				valores.add(mesFinal);
			} catch (ParseException e)
			{
				//TODO <MAGNO>Pode ser interessante logar, mas se não respeita a máscara, não é dado
			}
		}
	}
	
	/**
	 * Coloca nos parâmetros a informação de Códigos Nacionais
	 * @param request
	 * @param parametros
	 * @param valores
	 */
	public synchronized void getCampoComboBotoesCN(HttpServletRequest request, ArrayList parametros, ArrayList valores)
	{
		String CNs = request.getParameter("CODIGOS_NACIONAIS");
		parametros.add("CODIGOS_NACIONAIS");
		valores.add(CNs);
	}

	/**
	 * Coloca nos parâmetros a informação de Controle ou Pré-pago
	 * @param request
	 * @param parametros
	 * @param valores
	 */
	public synchronized void getComboCategoriaControleOuPP(HttpServletRequest request, ArrayList parametros, ArrayList valores)
	{
		String comboCategoriaOuPP = request.getParameter("comboCategoriaControleOuPP");
		if (!"".equals(comboCategoriaOuPP))
		{
			parametros.add("CONTROLE_OU_PP");
			valores.add(comboCategoriaOuPP);
		}
	}

}
