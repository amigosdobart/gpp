/*
 * Created on 08/09/2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package br.com.brasiltelecom.ppp.action.contingenciaCrm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;

import com.brt.gpp.comum.Definicoes;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.ContingenciaCrmHome;
import br.com.brasiltelecom.ppp.home.PlanoPrecoHome;
import br.com.brasiltelecom.ppp.interfacegpp.ConsultaStatusAssinanteGPP;
import br.com.brasiltelecom.ppp.interfacegpp.DesbloqueioContingenciaGPP;
import br.com.brasiltelecom.ppp.model.Assinante;
import br.com.brasiltelecom.ppp.portal.entity.ContingenciaCrm;
import br.com.brasiltelecom.ppp.portal.entity.DadosCadastraisCrm;
import br.com.brasiltelecom.ppp.portal.entity.Usuario;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;
import java.text.SimpleDateFormat;

/**
 * @author Henrique Canto
 * 
 */
public class EfetuaDesbloqueioAction extends ActionPortal {
	
	
	/** Atributos da Classe **/
	Logger logger = Logger.getLogger(this.getClass());
	private String codOperacao = Constantes.COD_DESBLOQUEIO_CONTINGENCIA;
	private static SimpleDateFormat dat = new SimpleDateFormat("dd/MM/yyyy");
	 /** Fim dos Atributos **/
	
	
	public ActionForward performPortal(ActionMapping actionMapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response, Database db) 
	
	throws Exception 
			{
			
			logger.info("Bloqueio de assinante em modo de contingência solicitado");
			HttpSession session = request.getSession();
			db.begin();
			String telContato=null;
			
			String msisdn =  "55" + (String)request.getParameter("msisdn");
			
			String msisdnFormatado = "("+(String)request.getParameter("msisdn").substring(0,2)+")"+(String)request.getParameter("msisdn").substring(2);
			String cpfCnpj = null;

			Usuario usuario = (Usuario) session.getAttribute(Constantes.USUARIO);
		
			// Validação do Telefone de Contato
			if (request.getParameter("telContato")!=null && !request.getParameter("telContato").toString().equals(""))
				telContato = "55" + (String)request.getParameter("telContato");
			// Fim da Validação
			
			// Validação do CPF
			if ((request.getParameter("cpf")!=null)  &&  (!"".equals((String)request.getParameter("cpf"))))
			{
				cpfCnpj = (String)request.getParameter("cpf");
				// Verifica se o cpf esta com ou sem separadores (11 ou 14 posicoes)
				if (cpfCnpj.length() == 14)
				{
					// numero ja formatado
					cpfCnpj = cpfCnpj.toString().substring(0,3) + cpfCnpj.toString().substring(4,7) + cpfCnpj.toString().substring(8,11)+cpfCnpj.toString().substring(12,14);
				}
				
			}
				else
				{
					cpfCnpj = (String)request.getParameter("cnpj");
					cpfCnpj = cpfCnpj.toString().substring(0,2) + cpfCnpj.toString().substring(3,6) + cpfCnpj.toString().substring(7,10) + cpfCnpj.toString().substring(11,15) + cpfCnpj.toString().substring(16,18);
				}
			// Fim da validação do CPF
			
			try {
				
				int codHotline = jaHouveDesbloqueioHotline(db,msisdn);
				
				if(codHotline>0)
				{
					//String motivo = request.getParameter("motivoBloqueio");
					// Início do Código para bloqueio de servicos e consulta de idAtividade ao GPP
					String servidor  = (String) servlet.getServletContext().getAttribute(Constantes.GPP_NOME_SERVIDOR);
					String porta     = (String) servlet.getServletContext().getAttribute(Constantes.GPP_PORTA_SERVIDOR);
					//String categoria = (String) request.getParameter("produtos");
					
					long idAtividade = 0;
					Assinante assinante = ConsultaStatusAssinanteGPP.getAssinante(msisdn, servidor, porta);
					/*
					 * Se o assinante existir na base da Tecnomen desativar Hotline como pré-pago F2,
					 * caso contrário como pós-pago
					 */
					int retorno = Integer.parseInt(assinante.getRetorno());
					if(retorno != 2)
					{
					//if(retorno == 2){
						//Usuario nao existe na tecnomen
						//idAtividade = DesbloqueioContingenciaGPP.desastivarHotline(servidor, porta, msisdn, "F1");
					//}
					//else{
						//Usuario existe na tecnomen
						idAtividade = DesbloqueioContingenciaGPP.desastivarHotline(servidor, porta, msisdn, "F2");
					//}

						// Fim do código de acesso ao GPP
						if(idAtividade>0)
						{
							ContingenciaCrm contingenciaCrm = ContingenciaCrmHome.findByAtividade(db,idAtividade);
							if (contingenciaCrm == null) 
							{
								contingenciaCrm = new ContingenciaCrm();
								contingenciaCrm.setIdAtividade(idAtividade);
								contingenciaCrm.setIdOperacao(request.getParameter("motivoBloqueio"));
								contingenciaCrm.setMsisdn(msisdn);
								contingenciaCrm.setDatAtividade(new java.util.Date());
								contingenciaCrm.setIdAtendente(usuario.getMatricula());
								contingenciaCrm.setIdStatus(ContingenciaCrmHome.findByBloqueioStatusId(db, Definicoes.STATUS_BLOQUEIO_SOLICITADO));
								if(request.getParameter("planos")!=null && !request.getParameter("planos").equals(""))
									contingenciaCrm.setIdPlanoPreco(PlanoPrecoHome.findByID(db,Integer.parseInt(request.getParameter("planos"))));
								db.create(contingenciaCrm);	
								// inicio do codigo para insercao de dados cadastrais do requisitante de bloqueio.
								DadosCadastraisCrm dadosCadastrais = new DadosCadastraisCrm();
								dadosCadastrais.setIdAtividade(idAtividade);
								dadosCadastrais.setNomeUsuario(request.getParameter("nome"));
								dadosCadastrais.setNumCpf(cpfCnpj);
								dadosCadastrais.setNumRg(request.getParameter("rg"));
								dadosCadastrais.setNumTelefContato(telContato);
								dadosCadastrais.setNomLogradouro(request.getParameter("logradouro"));
								dadosCadastrais.setNomCidade(request.getParameter("cidade"));
								dadosCadastrais.setNomBairro(request.getParameter("bairro"));
								dadosCadastrais.setNumCep(Long.parseLong((request.getParameter("cep"))));
								dadosCadastrais.setIdUf(request.getParameter("uf"));
	
								try 
								{
									dadosCadastrais.setDatNascimento(dat.parse(request.getParameter("datAniversario")));
								} catch (java.text.ParseException e1) 
								{
								}
								db.create(dadosCadastrais);
								
								request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " desbloqueado com sucesso");	
							}
							
							else request.setAttribute(Constantes.MENSAGEM, ""+request.getParameter("msisdn")+"já se encontra no Banco de Dados");
						}
						else
						{
							request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " não está no status pré-ativo");
						}
					}
					else{
						request.setAttribute(Constantes.MENSAGEM, "Assinante "+msisdnFormatado+ " não é um assinante pré-pago");
					}
				}
				else 
				{
					if (codHotline==-1)
						request.setAttribute(Constantes.MENSAGEM, "Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (CONFIRMADO).");
					else
						if (codHotline==-2)
							request.setAttribute(Constantes.MENSAGEM, "Solicitação de desbloqueio do assinante "+msisdnFormatado+ " já efetuada (EM ANDAMENTO).");
				}
				
		}
		catch (Exception e)
		{
				logger.error("Não foi possível desbloquear o assinante em modo de contingência, problemas na conexão com o banco de dados (" +
								e.getMessage() + ")");
				throw e;
		}
			
		ActionForward result = actionMapping.findForward("success");
		return result;
			
	}
	
	private int jaHouveDesbloqueioHotline(Database db, String msisdn) throws Exception
	{ 
		int ret = 1; // Retorno ok
		ContingenciaCrm contingenciaCrm = ContingenciaCrmHome.findAssinanteOperacao(db,msisdn,Constantes.CONTINGENCIA_DESATIVACAO_HOTLINE);
		if (contingenciaCrm!=null)
		{
			if (contingenciaCrm.getIdStatus().getIdStatusBloqueio().equals(Constantes.CONTINGENCIA_BLOQUEIO_CONFIRMADO) )
					//&& contingenciaCrm.getIdStatusProc().equals(Constantes.CONTINGENCIA_BLOQUEIO_OK))
				ret = -1;
			else 
				if (contingenciaCrm.getIdStatus().getIdStatusBloqueio().equals(Constantes.CONTINGENCIA_BLOQUEIO_SOLICITADO))
					ret = -2;
		}
		return ret;
	}

	public String getOperacao() {
		return this.codOperacao;
	}

}