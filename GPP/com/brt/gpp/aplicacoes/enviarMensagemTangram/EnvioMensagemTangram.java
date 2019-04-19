package com.brt.gpp.aplicacoes.enviarMensagemTangram;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.enviarMensagemTangram.dao.RequisicaoDAO;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.tangram.ConexaoTangram;
import com.brt.gpp.comum.conexoes.tangram.entidade.DestinoMensagem;
import com.brt.gpp.comum.conexoes.tangram.entidade.ParametrosNotificacao;
import com.brt.gpp.comum.conexoes.tangram.entidade.Requisicao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 *  Envia uma requisição ao Tangram (faz persistencia e oferece Proxy de notificacao). <br>
 *  
 *  Recursos importantes dessa aplicação:<br>
 *  
 *  <blockquote>
 *      a) UTILIZA A BIBLIOTECA 'ConexaoTangram'
 *      
 *  	b) PERSISTENCIA DE REQUISICOES:
 *         Mantém persistencia, em banco, dos principais dados da requisicao (TBL_TAN_REQUISICAO), 
 *         incluindo os conteudos de mensagem (TBL_TAN_CONTEUDO_MENSAGEM) e dados de retorno
 *         (síncrono) para cada destinatario (TBL_TAN_DESTINO_MENSAGEM).<br>
 *         
 *      c) PUBLICACAO VIA CORBA:
 *      	Possui um método, publicado via CORBA, que oferece um mecanismo fácil para uso do Tangram.
 *         Esse método recebe um xml 'GPPRequisicaoTangram' o qual contém, de forma simplificada,
 *         os paramentros de requisicao Tangram.<br>
 *  
 * 		d) PROXY DE NOTIFICACAO:
 * 		   Qualquer requisição enviada por essa API terá como url de notificação uma servlet
 *         específica do WPP (Proxy de Notificação). Esse proxy fará a persistência
 *         dos dados enviados pelo Tangram (TBL_TAN_NOTIFICACAO) e encaminhará a requisição
 *         de notificação para a url definida inicialmente na requisição.<br>
 *         
 *      e) REGISTRA LOG
 *  </blockquote>
 *  
 *  Ao utilizar essa API algumas restrições são aplicadas aos parâmetros de requisição,
 *  conforme segue abaixo. A violação de alguma dessas regras implicará IllegalArgumentException.<br>
 *  
 *  <blockquote>
 *  	CAMPO: 		requisicao.idtOrigem<br>
 *  	OBSERVACAO: Obrigatório<br>
 *  
 *  	CAMPO: 		requisicao.destinosMensagem<br>
 *  	OBSERVACAO: Deve existir pelo menos um destino. Todas as instancias devem possuir 
 *  			    idtMsisdnDestino preenchido.<br>
 *  
 *  	CAMPO: 		requisicao.parametrosNotificacao.tipoRetorno<br>
 *  	OBSERVACAO: Esse campo é desprezado. Sera substituido por 1 (HTTP_POST) caso o Proxy esteja ativo.<br>
 *  
 *  	CAMPO: 		requisicao.parametrosNotificacao.tiposEvento<br>
 *  	OBSERVACAO: Esse campo é desprezado. Sera habilitada notificacao para todos os eventos.<br>
 *  
 *  	CAMPO:		requisicao.dataEnvioRequisicao<br>
 *  	OBSERVACAO: Sera substituida pela data corrente.<br>
 *  </blockquote>
 *  
 *  NÃO será feita a persistencia de:<br>
 *  
 *  <blockquote>
 *  	- Entidades 'ConteudoMensagem' sem o atributo 'textoConteudo'<br>
 *  	- Entidades 'DestinoMensagem' sem os atributos 'idtMsisdnDestino' e
 *        'idMensagem' (Se por algum motivo esse dado não for informado
 *        pelo Tangram no momento do envio da requisicao, a entidade é
 *        descartada) <br>
 *  </blockquote>
 *  
 *  Configurações do GPP aplicáveis:
 *  
 *  <blockquote>
 *  	- TAN_ENDERECO_HOST<br>
 *  	- TAN_PORTA_HTTP<br>
 *  	- TAN_PORTA_FTP<br>
 *  	- TAN_URL_PROXY<br>
 *  	- TAN_PROXY_ATIVO<br>
 *  	- TAN_USUARIO<br>
 *  	- TAN_SENHA<br>
 *      - TAN_ID_EMPRESA<br>
 *  </blockquote>
 *  
 *  @author Bernardo Vergne Dias
 *  Criado em: 20/09/2007
 */
public class EnvioMensagemTangram extends Aplicacoes
{
	// IDs de Configuracoes GPP
	
	public static final String TAN_ENDERECO_HOST			= "TAN_ENDERECO_HOST";
	public static final String TAN_PORTA_HTTP				= "TAN_PORTA_HTTP";
	public static final String TAN_PORTA_FTP				= "TAN_PORTA_FTP";
	public static final String TAN_URL_PROXY				= "TAN_URL_PROXY";
	public static final String TAN_PROXY_ATIVO				= "TAN_PROXY_ATIVO";
	public static final String TAN_USUARIO					= "TAN_USUARIO";
	public static final String TAN_SENHA					= "TAN_SENHA";
	public static final String TAN_ID_EMPRESA				= "TAN_ID_EMPRESA";
	
	private GerentePoolBancoDados	gerenteBancoDados = null; 	// Gerente de conexoes Banco Dados
	
	public EnvioMensagemTangram (long logId)
	{		
		//######################################
		super(logId, "Definicoes.CL_ENVIO_MENSAGEM_TANGRAM");
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
	}
	
	/**
	 * Envia uma requisição ao Tangram de acordo com o XML informado.
	 * 
	 * Veja sintaxe do XML na classe ParserGPPRequisicaoTangram.
	 * 
	 * @param xmlGPPRequisicaoTangram XML
	 * @return Código de retorno (0 = sucesso, 9998 = RET_ERRO_GENERICO_GPP, outros = codigo de retorno Tangram)
	 * @throws Exception 
	 */
	public short enviarRequisicaoTangram(String xmlGPPRequisicaoTangram) 
	{	
		short codRetorno = 0;
		PREPConexao conexaoPrep = null;
		
		try
		{
			// Seleciona conexão do pool Prep Conexão
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
		    conexaoPrep.setAutoCommit(false);
		    
			// Carrega as configuracoes
			MapConfiguracaoGPP configuracaoGPP = MapConfiguracaoGPP.getInstancia();
			int idEmpresa = Integer.parseInt(configuracaoGPP.getMapValorConfiguracaoGPP(TAN_ID_EMPRESA));
			
			Requisicao requisicao = ParserGPPRequisicaoTangram.parse(xmlGPPRequisicaoTangram);
			requisicao.setIdEmpresa(idEmpresa);
			enviarRequisicaoTangram(conexaoPrep, requisicao);
			
			if (requisicao.getCodRetorno() == null)
				codRetorno = Definicoes.RET_ERRO_GENERICO_GPP;
			else
				codRetorno = requisicao.getCodRetorno().shortValue();
			
			conexaoPrep.commit();
		}
		catch (Exception e)
		{
			codRetorno = Definicoes.RET_ERRO_GENERICO_GPP;
			super.log(Definicoes.ERRO, "enviarRequisicaoTangram", "Excecao: " + e);
			
			try
			{
				conexaoPrep.rollback();
			}
			catch (Exception ignored){}
		}
		finally
		{
			// Libera a conexao com o banco
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
		
		return codRetorno;
	}
	
	/**
	 * Envia uma requisição ao Tangram de acordo com a entidade informada.
	 * 
	 * @param conexaoPrep Conexao (para persistencia da requisicao)
	 * @param requisicao Instancia de <code>Requisicao</code>
	 * @return Requisicao com os parametros de retorno preenchidos.
	 * @throws Exception 
	 */
	public Requisicao enviarRequisicaoTangram(PREPConexao conexaoPrep, Requisicao requisicao) throws Exception
	{		
		// Carrega as configuracoes
		MapConfiguracaoGPP configuracaoGPP = MapConfiguracaoGPP.getInstancia();
		String host 		= configuracaoGPP.getMapValorConfiguracaoGPP(TAN_ENDERECO_HOST);
		int portaHttp 		= Integer.parseInt(configuracaoGPP.getMapValorConfiguracaoGPP(TAN_PORTA_HTTP));
		String usuario 		= configuracaoGPP.getMapValorConfiguracaoGPP(TAN_USUARIO);
		String senha 		= configuracaoGPP.getMapValorConfiguracaoGPP(TAN_SENHA);
		boolean proxyAtivo 	= (configuracaoGPP.getMapValorConfiguracaoGPP(TAN_PROXY_ATIVO).equals("0") ?
							  false : true);
		String urlProxy 	= configuracaoGPP.getMapValorConfiguracaoGPP(TAN_URL_PROXY);

		// Faz backup das configuracoes de notificacao
		ParametrosNotificacao bkpParametrosNotificacao = requisicao.getParametrosNotificacao();

		// Valida os atributos da requisicao
		validaRequisicao(requisicao, proxyAtivo);
		
		try
		{
		    // Configura o conector para o Tangram
			ConexaoTangram conexao = new ConexaoTangram();
			conexao.setHost(host);
			conexao.setPortaHttp(portaHttp);
			conexao.setProtocolo(ConexaoTangram.HTTP_POST);
			conexao.setUsuario(usuario);
			conexao.setSenha(senha);
			
			// Configura notificacao do tipo URL (retorno HTTP/POST) 
			// para todos os tipos de eventos do Tangram.
			if (proxyAtivo)
				requisicao.setNotificacaoHttp(urlProxy);				
			
			// Insere a data de envio da requisicao
			requisicao.setDataEnvioRequisicao(Calendar.getInstance().getTime());
			
			// Faz o envio ao Tangram
			conexao.enviaRequisicao(requisicao);
						
			// Faz a persistencia da requisicao e entidades relacionadas
			(new RequisicaoDAO(super.logId)).incluiRequisicao(conexaoPrep, requisicao, proxyAtivo);
		}
		finally
		{
			// Restaura as configuracoes de notificacao (com as adaptações desse método)
			if (bkpParametrosNotificacao != null && proxyAtivo)
			{
				bkpParametrosNotificacao.setTipoRetorno(
						requisicao.getParametrosNotificacao().getTipoRetorno());
				bkpParametrosNotificacao.setTiposEvento(
						requisicao.getParametrosNotificacao().getTiposEvento());
			}
			requisicao.setParametrosNotificacao(bkpParametrosNotificacao);
		}
		
		return requisicao;
	}
	
	private void validaRequisicao(Requisicao requisicao, boolean proxyAtivo) throws Exception
	{
		if (requisicao.getIdtOrigem() == null)
			throw new IllegalArgumentException("Campo 'idtOrigem' nao pode ser nulo");
		
		if (requisicao.getDestinosMensagem() == null)
			throw new IllegalArgumentException("Campo 'destinosMensagem' nao pode ser nulo");
		
		Collection destinos = requisicao.getDestinosMensagem();

		if (destinos.size() == 0)
			throw new IllegalArgumentException("Campo 'destinosMensagem' deve possuir " +
					"pelo menos uma instancia de DestinoMensagem");
		
		Iterator it = destinos.iterator();
		while (it.hasNext())
		{
			DestinoMensagem destino = (DestinoMensagem)it.next();
			
			if (destino.getIdtMsisdnDestino() == null)
				throw new IllegalArgumentException("Campo 'idtMsisdnDestino' de DestinoMensagem " +
						"nao pode ser nulo");
		}		
		
		ParametrosNotificacao params = requisicao.getParametrosNotificacao();
		
		if (params != null && proxyAtivo)
		{
			if (!params.getTipoRetorno().equals(ParametrosNotificacao.RETORNO_HTTP_GET) ||
				!params.getTipoRetorno().equals(ParametrosNotificacao.RETORNO_HTTP_POST))
				throw new IllegalArgumentException("Campo 'tipoRetorno' de ParametrosNotificacao " +
				"deve valer RETORNO_HTTP_GET ou RETORNO_HTTP_POST");
		}
	}
	
}
