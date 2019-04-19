package br.com.brasiltelecom;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.lang.Math;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.brasiltelecom.wig.entity.cadpre.Endereco;
import br.com.brasiltelecom.wig.entity.cadpre.Pessoa;
import br.com.brasiltelecom.wig.entity.cadpre.PessoaClarify;

// Referenced classes of package br.com.brasiltelecom:
//            ValidaCPFCNPJ, DesbloqueioHL

public class CadPre extends HttpServlet
{
	/**
	 * 
	 * @author Luciano Vilela
	 * @date 11/11/2005
	 */
	
	static Logger log = Logger.getLogger(CadPre.class);
	private InitialContext ictx = null;
	ServletContext sctx =  null;
	
	public CadPre()
	{
		/*
		ictx = null;
		log = Logger.getLogger(this.getClass());
		*/
	}
	
	public void init(ServletConfig arg) throws ServletException
	{
		try 
		{
			ictx = new InitialContext();
			//clarifyCadastro = (String)arg.getServletContext().getAttribute("ClarifyCadastro");
		}
		catch (NamingException e)
		{
			log.error("cadpre", e);
		}
		sctx = arg.getServletContext();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		String clarifyCadastro = (String)sctx.getAttribute("ClarifyCadastro");
		
		String querystring 		= request.getQueryString();
		String msisdn 			= request.getParameter("MSISDN");
		String reqtiposervico 	= request.getParameter("t");
		String pdv 				= (request.getParameter("pdv") != null ? request.getParameter("pdv") : "");
		String reqresp 			= request.getParameter("resp");
		String reqtipo 			= request.getParameter("tipo");
		String reqid 			= request.getParameter("id");
		String reqendereco 		= request.getParameter("endereco");
		String reqcep 			= request.getParameter("cep");
		String complemento 		= (request.getParameter("comp") == null ? "" : request.getParameter("comp") );
		String configDM 		= request.getParameter("cdm");
		int trys 				= Integer.parseInt(request.getParameter("try") == null ? "0" : request.getParameter("try"));
		String inRG 			= "";
		String inCPF 			= "";
		
		log.debug("msisdn: " + msisdn + ", reqtiposervico: " +
				  reqtiposervico + ", reqresp: " + reqresp + ", reqtipo: " +
				  reqtipo + ", reqid: " + reqid + ", reqendereco: " +
				  reqendereco + ", reqcep: " + reqcep + ", configDM: " +
				  configDM + ", trys: " + trys + ", pdv: " + pdv);
		
		try
		{
			// Executa o cadastro do Pre-Pago
			cadastro(msisdn, reqtiposervico, pdv, reqresp, reqtipo, reqid, reqendereco, reqcep, 
					 complemento, inRG, inCPF, configDM, trys, clarifyCadastro, ictx, request);
		}
		catch (Exception e)
		{
			log.error("Erro no cadastro: ", e);
		}
		
		// Encaminha a requisicao com a devida mensagem setada para o assinante
		request.getRequestDispatcher("/cadastro_pre.jsp").forward(request, response);
	}
	
	
	/**
	 * Metodo....: cadastro
	 * Descricao.: Realiza todo o fluxo para o cadastro de assinantes
	 * 			   do plano Pre-Pago, como validacao de CPF e CEP junto 
	 * 			   ao DataQuality, como o envio da requisicao para abertura
	 * 			   de OS no Clarify
	 * 
	 * @param msisdn		  - MSISDN do assinante
	 * @param reqtiposervico  - Tipo de servico
	 * @param pdv			  - Numero carimbado na Nota Fiscal
	 * @param reqresp		  - Resposta
	 * @param reqtipo		  - Tipo de validacao ("RG" ou "CPF")
	 * @param reqid			  - Identificador da requisicao (CPF ou RG no formato numerico)
	 * @param reqendereco	  - Endereco
	 * @param reqcep		  - CEP do assinante
	 * @param complemento	  - Complemento do logradouro
	 * @param inRG			  - RG do assinante
	 * @param inCPF			  - CPF do assinante
	 * @param configDM		  - Parametro para configuracao
	 * @param trys			  - Quantidade de Tentativas
	 * @param clarifyCadastro - Nome do DataSource a ser utilizado no Banco
	 * @param ictx			  - InitialContext
	 * @param request		  - HttpServletRequest
	 * @throws NamingException
	 */
	protected void cadastro(String msisdn, String reqtiposervico, String pdv, String reqresp, 
							  String reqtipo, String reqid, String reqendereco, String reqcep, 
							  String complemento, String inRG, String inCPF, String configDM, int trys, 
							  String clarifyCadastro, InitialContext ictx, HttpServletRequest request) throws NamingException 
	{
		request.setAttribute("CDM", configDM);
		request.setAttribute("try", Integer.toString(trys));
		
		int ver_pre;
		int ver_hot;
		int ver_erro;
		int ver_fila;
		// retornocadastro = "";
		if ("CPF".equals(reqtipo))
			inCPF = reqid;
		else 
			if ("RG".equals(reqtipo))
				inRG = reqid;
		if (reqendereco == null)
			reqendereco = "";
		if (reqid == null)
			reqid = "";
		
		/*
		 * Parametro que indica se o cadastramento foi chamado pelo tsd ou pelo
		 * menu brt.
		 * 
		 */
		boolean itemMenu = request.getParameter("itemMenu") != null ? true : false;
		request.setAttribute("itemMenu", request.getParameter("itemMenu"));
		// Inicio de requisição - (reqtiposervico=0)
		/*
		 * Apresenta tela inicial do cadastro caso o cliente ja esteja
		 * cadastrado no clarify e apresentado uma mensagem de cliente ja
		 * cadastrado
		 * 
		 */

		if (reqtiposervico.equals("0"))
		{
			ver_pre = 0;
			ver_hot = 0;
			ver_erro = 0;
			ver_fila = 0;
			String desc_erro = "";

			DAOGPP daoGPP = new DAOGPP(ictx);
			// verifica se eh um cliente pre-pago
			if (!daoGPP.ehPrepago(msisdn))
			{
				// nao eh prepago
				log.debug(msisdn + ": nao e um prepago");
				ver_pre = 1;
			}
			else
			{
				/*
				 * verifica se o cliente ja esta cadastrado no clarify
				 */
				
				DAOClarify verificaMSISDN = new DAOClarify(ictx);
				
				try
				{
					//clarifyCadastro = (String)ServletConfig.getServletContext().getAttribute("ClarifyCadastro");
					DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/"+clarifyCadastro);
					Connection con = ds.getConnection();
					
					PessoaClarify resposta = verificaMSISDN.ConsultaMSISDN(msisdn.substring(2), con);
					log.debug(msisdn + " : codigo retorno clarify " + resposta.getCod_retorno());
					if ("101".equals(resposta.getCod_retorno()))
					{
						/*
						 * Cliente nao esta cadastrado Apresenta tela inicial para
						 * cadastro das informacoes
						 */
						request.setAttribute("telaInicial", "S");
						request.setAttribute("telaCadastro", "S");
						ver_erro = 0;
					}
					else if (itemMenu) 
					{
						/*
						 * cliente ja esta cadastro Se a requisicao foi realizada
						 * pelo menu brt Apresenta mensagem de cliente ja cadastrado
						 * 
						 */
						
						request.setAttribute("mensagemSucesso","Numero ja cadastrado");
					}
					else 
					{
						/*
						 * Cliente pos-pago ou cliente pre-pago ja cadastrado na
						 * subida do tsd
						 */
						request.setAttribute("sucesso", "S");
						ver_erro = 1;
					}
				}
				catch (NamingException e)
				{
					log.error("Erro NamingException: ", e);
				}
				catch (SQLException e)
				{
					log.error("Erro de SQL: ", e);
				}
			}
		}

		// Recebimento de resposta de WML de cadastro - (reqtiposervico=1)
		if (reqtiposervico.equals("1"))
		{
			// Verifica se pediu cadastro ou nao
			if (reqresp.equals("S"))
			{
				/*
				 * controla o numero de tentativa de cadastramento
				 */
				trys++;
				request.setAttribute("try", Integer.toString(trys));
				StringBuffer mensagem = new StringBuffer();
				boolean verificaCPF = true;
				Pessoa cliente = null;
				
				// valida digito verificador
				if (ValidaCPFCNPJ.valida_CpfCnpj(reqid))
				{
					// valida cliente no DataQuality
					cliente = APIConsultaCPF.consultacpf(reqid);
					log.debug("msisdn : " + msisdn + "cpf: " + reqid
							+ " situacao_cadastral : "
							+ cliente.getSit_cadastral());
					if (!("1".equals(cliente.getSit_cadastral()) || "0".equals(cliente.getSit_cadastral())) )
					{
						// Situacao do CPF nao eh valida
						verificaCPF = false;
						request.setAttribute("telaCadastro", "S");
						mensagem.append("CPF invalido, tente novamente<br/>");
					}
				}
				else 
				{
					// CPF Invalido, mostrara mensagem de erro
					log.debug("msisdn: " + msisdn + " cpf: " + reqid + "digito verificados invalido");
					verificaCPF = false;
					request.setAttribute("telaCadastro", "S");
					mensagem.append("CPF invalido, tente novamente<br/>");
				}
				
				boolean verificaCEP = true;
				Endereco endereco = null;
				
				/*
				 * solicita validacao do CEP no DataQuality
				 */
				endereco = APIConsultaCEP.consultacep(reqcep);
				
				//
				// Validacao do CEP
				//
				if (endereco.getCod_retorno() == 0)
				{
					// Logradouro Unico
					log.debug("msisdn :" + msisdn + " cep : " + reqcep + " logradouro unico");
					
					// Endereco do assinante no caso de logradouro Unico
					reqendereco = getEnderecoConcatenado(endereco, complemento);
				}
				else
					if (endereco.getCod_retorno() == 2 || endereco.getCod_retorno() == 1 )
					{
						// CEP Generico, Mais de um Logradouro
						log.debug("msisdn :" + msisdn + " cep : " + reqcep + " mais de um logradouro");
						reqendereco = getEnderecoConcatenado(endereco, complemento);
					} 
				else 
					if (endereco.getCod_retorno() == 3)
					{
						// CEP Invalido, mensagem de erro sera mostrada
						log.debug("msisdn :" + msisdn + " cep : " + reqcep + " cep invalido");
						request.setAttribute("telaCadastro", "S");
						mensagem.append("CEP invalido, tente novamente<br/>");
						verificaCEP = false;
					}
				
				/*
				 * caso exista uma mensagem, eh apresentada na tela
				 */
				if (mensagem.length() > 0)
					request.setAttribute("mensagem", mensagem.toString());
				
//------------- Inicio da verificacao do PDV
				// Validacao do PDV sera feita no metodo
				String pdvFinal = validaPdv(pdv);
				
//------------- Final da verificacao do PDV
				
				if (verificaCPF && verificaCEP)
				{
					// cria OS no Clarify
					log.debug("msisdn :" + msisdn + " cep : " + reqcep + " mais de um logradouro e PDV - " + pdv);
					int retorno = 0;
					//ServletContext sctx = null;
					if(!"N".equals((String)sctx .getAttribute("CriaOSClarify")))
					{
						Connection con = null;
						try
						{
							DAOClarify daoClarify = new DAOClarify(ictx);
							DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/"+clarifyCadastro);
							con = ds.getConnection();
							
							retorno = daoClarify.criaOS(msisdn.substring(2), reqid, cliente.getNome(), reqendereco, reqcep, pdv, con);
						}
						catch (SQLException e)
						{
							log.debug("Erro na criacao da OS no Clarify. ERRO: ", e);
						}
						finally
						{
							try
							{
								if (con != null && !con.isClosed())
									con.close();
							}
							catch(Exception e)
							{
								log.error("Erro ao fechar conexao com o banco de dados. Erro:", e);
							}
						}
					}
					else
						log.warn("Em modo de debug, nao esta gerando OS no Clarify");
					
					log.debug("msisdn :" + msisdn + " retorno OS clarify : " + retorno);
					String s = "";
					
					if (retorno == 0)
					{
						// Desbloqueia HotLine do cliente no ASAP
						DAOGPP daoGpp = new DAOGPP(ictx);
						long idInterface = daoGpp.getInterfaceId();
						DecimalFormat df = new DecimalFormat("SB00000000000000");
						String servidorVitria = (String)sctx.getAttribute("ServidorVitria");
						int portaVitria = Integer.parseInt((String)sctx.getAttribute("PortaServidorVitria"));
						DesbloqueioHL desbloqHL = new DesbloqueioHL(ictx, servidorVitria, portaVitria);
						String desbloqHotline = desbloqHL.DesbloqueioHotline(msisdn, "Retirar", df.format(idInterface));
						log.debug("msisdn :" + msisdn + " retorno desbloqueio Hotline: " + desbloqHotline);
						request.setAttribute("sucesso", "S");
						
						if (desbloqHotline.equals("OK"))
							request.setAttribute("mensagemSucesso", "Cadastramento efetuado com sucesso.");
						else if (desbloqHotline.equals("CONTINGENCIA"))
						{
							request.setAttribute("sucesso", "S");
							request.setAttribute("mensagemSucesso", "Brt GSM:Aguarde. O desbloqueio do seu celular esta sendo processado. Voce recebera uma mensagem de confirmacao");
						}
					}
					else
						request.setAttribute("sucesso", "S");
				}
				
				// Numero de tentativas
				// Alteracao para retirada de tentativas
				// Luciano Vilela - 24/07/2006 conforme solicitacao da
				// area usuario - Libania
				else if (trys > 0)
				{
					/*
					 * caso o numero de tentativas seja excedido, limpa as
					 * mensagens e apresenta uma mensagem de cadastro nao
					 * efetuado
					 */
					log.debug("msisdn :" + msisdn + " numero de tentativas excedido : " + trys);
					request.removeAttribute("telaCadastro");
					mensagem.append("<br/>Cadastro nao efetuado. Utilize o MenuBrTGSM > PrePago > OutrasOpcoes > IncluiCadastro. " +
									"Caso nao possua esta opcao, acesse Personalizar > Incluir > PrePago");
					request.setAttribute("mensagemSucesso", mensagem.toString());
				}
			}
		}
	}
	
	/**
	 * Metodo....: getEnderecoConcatenado
	 * Descricao.: Monta o endereco no formato padrao do Clarify para posterior envio
	 * 
	 * @param  endereco	  		- Objeto contendo os parametros do Endereco
	 * @param  complemento 		- Complemento do endereco para Logradouror Genericos
	 * @return enderecoCompleto	- Endereco completo contatenado
	 */
	private String getEnderecoConcatenado(Endereco endereco, String complemento)
	{
		String separador = ";";
		// Montagem do endereco completo para geracao de OS no Clarify
		StringBuffer enderecoCompleto = new StringBuffer(endereco.getCod_retorno()).append(separador);
		enderecoCompleto.append(endereco.getCod_logradouro()).append(separador);
		enderecoCompleto.append(endereco.getNome_logradouro()).append(separador);
		enderecoCompleto.append(endereco.getTipo_logradouro()).append(separador);
		enderecoCompleto.append(endereco.getDescricao_tipo_logradouro()).append(separador);
		enderecoCompleto.append(endereco.getNome_bairro()).append(separador);
		enderecoCompleto.append(endereco.getSigla_cnl()).append(separador);
		enderecoCompleto.append(endereco.getSigla_uf()).append(separador);
		enderecoCompleto.append(endereco.getSigla_municipio()).append(separador);
		enderecoCompleto.append(endereco.getCod_num_localidade()).append(separador);
		enderecoCompleto.append(endereco.getNome_localidade()).append(separador);
		// Caso o logradouro seja unico, o complemento sera VAZIO ("")
		enderecoCompleto.append(complemento);
		
		// Retorno do endereco devidamente concatenado
		return enderecoCompleto.toString();
	}
	
	/**
	 * Metodo....: validaPdv
	 * Descricao.: Valida o PDV atraves do calculo do digito verificador
	 * 
	 * @param pdv	- Numero a ser validados
	 * @return pdv  - Caso seja valido, eh retornado, caso contrario retorna vazio ("")
	 */
	public String validaPdv(String pdv)
	{
		if (pdv.length() != 6)
		{
			log.debug("Quantidade de caracteres do PDV invalido: " + pdv.length() + " digitos - " + pdv);
			pdv = "";
		}
		else
		{
			try
			{
				long longPdv = Long.parseLong(pdv);
				
				if (longPdv < 100001)
				{
					log.debug("PDV inferior ao piso 100001: " + pdv);
					pdv = "";
				}
				else
				{
					// Array contendo cada numero do PDV
					int numPdv[] = new int[6];
					int digitoVerificador = 0;
					int soma = 0;
					for (int i = 0; i < 5; i++)
					{
						// Populando o array com os digitos do PDV
						// exceto o digito verificador
						numPdv[i] = Integer.parseInt(pdv.substring(i, i + 1));
						// Realiza a soma dos digitos, conforme os mesmo sao populados
						soma += numPdv[i];
					}
					// Seleciona o digito verificador
					digitoVerificador = Integer.parseInt(pdv.substring(5));
					if (soma >= 10)
					{
						// Caso a soma seja igual ou superior a 10, o digito verificador
						// sera o modulo da diferenca entre os 2 digitos
						// Selecao do digito um e dois, respectivamente
						int digitoUm   = (int) soma / 10;
						int digitoDois = soma % 10;
						
						// Tira a diferenca entre os dois valores e pega
						// o valor absoluto (modulo)
						soma = Math.abs(digitoUm - digitoDois);
					}
					if (digitoVerificador != soma)
					{
						// Validacao do digito verificador falhou
						log.debug("Digito verificador invalido: " + digitoVerificador + 
								  "Digito verificador correto: " + soma);
						pdv = "";
					}
					else
						// Validacao do digito verificador efetuada com sucesso
						log.debug("Digito verificador correto: '" + digitoVerificador + "'. PDV valido:" + pdv);
				}
			}
			catch (NumberFormatException e)
			{
				log.error("Erro na validacao do PDV. ERRO: ", e);
				pdv = "";
			}
		}
		
		return pdv;
	}
}