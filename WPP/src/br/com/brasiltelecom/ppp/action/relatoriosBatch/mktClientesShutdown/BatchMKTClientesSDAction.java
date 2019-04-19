package br.com.brasiltelecom.ppp.action.relatoriosBatch.mktClientesShutdown;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.SolicitacoesRelatorioHome;
import br.com.brasiltelecom.ppp.portal.entity.SolicitacoesRelatorio;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Cadastra o relatório batch de clientes em shutdown
 * 
 * @author Marcelo Alves Araujo
 * @since 15/09/2005
 */
public class BatchMKTClientesSDAction extends ActionPortal 
{
	private String codOperacao = Constantes.COD_BAT_MKT_CLIENTES_SD;
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping,ActionForm actionForm,HttpServletRequest request,HttpServletResponse response,Database db) throws Exception 
	{		
		String resultado = Constantes.STATUS_SUCESSO;
		
		try
		{
			logger.info("Relatório Batch de Clientes em Shutdown");
	
			// Monta a consulta a ser gravada na tabela
			String consulta = this.consultaShutDown(request.getParameter("dataInicial"), request.getParameter("dataFinal"));
			// Pega dados do usuário conetado
			Usuario login = (Usuario) request.getSession().getAttribute(Constantes.LOGIN);
			
			db.begin();
			
			// Busca o próximo id a ser selecionado
			int idSolicitacao = SolicitacoesRelatorioHome.findNextID(db);
			
			// Cria o objeto com os dados a inserir na tabela de solicitações
			// Parâmetros: 	1 - Id da solicitação gerado pelo SolicitacoesRelatorioHome.findNextID(db);
			//				2 - Nome do usuário, dado por login.getMatricula()
			//				3 - Data para a qual será agendada a consulta
			//				4 - A consulta a ser realizada
			//				5 - Nome do arquivo: departamento_nome_identificador+id
			//				6 - E-mail do usuário
			SolicitacoesRelatorio solicita = new SolicitacoesRelatorio(idSolicitacao,login.getMatricula(),
											 new Date(),consulta,"mkt_clientes_shutdown"+idSolicitacao,
											 request.getParameter("mail"));
			
		 	// Insere na tabela os dados para execução do relatório
		 	db.create(solicita);
		 	db.commit();
		 	
		 	request.setAttribute(Constantes.MENSAGEM, "Consulta Cadastrada com Sucesso!");
		 	request.setAttribute("imagem", "img/tit_rel_mkt_consulta_shutdown.gif");
		 	db.close();
		}
		catch(Exception e)
		{
			resultado = Constantes.STATUS_ERRO;
			request.setAttribute(Constantes.MENSAGEM, "Erro ao Cadastrar Consulta!");
		}
		
		return actionMapping.findForward(resultado);
	}
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao() 
	{
		return this.codOperacao;
	}
	
	/**
	 * <P><b>Metodo....:</b> consultaShutDown
	 * <P><b>Descricao.:</b> Retorna a consulta a ser realizada substituindo
	 * 						 os parâmetros
	 * @param dataInicial 	- Data inicial
	 * @param dataFinal		- Data final
	 * @return Consulta a ser cadastrada na tabela
	 */
	private String consultaShutDown(String dataInicial, String dataFinal)
	{
		return	"SELECT " +
				" IDT_CODIGO_NACIONAL AS CN, " +
				" IDT_MSISDN AS MSISDN, " +
				" NVL(TO_CHAR(DAT_ATIVACAO,'dd/mm/yyyy hh24:mi:ss'),' ') AS data_ativacao, " +
				" QTD_RECARGAS as qtd, " +
				" NVL(TO_CHAR(DAT_DESATIVACAO,'dd/mm/yyyy hh24:mi:ss'),' ') AS data_desativacao " +
				"FROM " +
				" tbl_rel_assinante_shutdown " +
				"WHERE " +
				" DAT_DESATIVACAO >= TO_DATE('" + 
				dataInicial + 
				"','dd/mm/yyyy') " +
				" AND DAT_DESATIVACAO <= TO_DATE('" + 
				dataFinal + 
				"','dd/mm/yyyy') " +
				"ORDER BY " +
				" IDT_CODIGO_NACIONAL, " +
				" DAT_DESATIVACAO";
	}
}
