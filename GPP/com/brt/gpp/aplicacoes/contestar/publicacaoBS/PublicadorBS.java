package com.brt.gpp.aplicacoes.contestar.publicacaoBS;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.contestar.dao.PublicacaoBSDAO;
import com.brt.gpp.aplicacoes.contestar.publicacaoBS.PublicadorBSXMLParser;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.toolbar.ConexaoToolbar;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
 *	Classe responsavel pelo processo de publicacao de Boletins de Sindicancia no UNIPRO.
 * 
 *	@author		Daniel Ferreira
 *	@since		21/12/2006
 */
public class PublicadorBS extends Aplicacoes 
{
	protected GerentePoolBancoDados	gerenteBancoDados = null;
	
	/**
	 *	Construtor da classe.
	 * 
	 *	@param		idProcesso				Identificador do processo.
	 */
	public PublicadorBS(long idProcesso)
	{
		super(idProcesso, "PublicadorBS");
		
		// Obtem referencia do gerente de conexoes do Banco de Dados
		this.gerenteBancoDados = GerentePoolBancoDados.getInstancia(idProcesso);
	}
	
	/**
	 *	Publica o numero do Boletim de Sindicancia no UNIPRO.
	 * 
	 *	@param		numeroBS				Numero do BS gerado pelo SFA.
	 *	@param		numeroIP				Endereco IP do solicitante da abertura da contestacao.
	 *	@param		XML de retorno da operacao.
	 */
	public String publicarBS(String numeroBS, String numeroIP, String numeroAssinante, String matriculaOperador)
	{
		String	result			= null;
		long	numeroProtocolo	= -1;
		String	mensagem		= null;
		short	codigoRetorno	= -1;
		PREPConexao prepConexao = null;
		// Publicando o numero do Protocolo Unico no UNIPRO.
		PublicacaoBSDAO dao = new PublicacaoBSDAO(super.getIdLog());
		
		try
		{
			super.log(Definicoes.INFO, "publicarBS", "Inicio da publicacao do BS: " + numeroBS);
			// Busca uma conexao de banco de dados
			prepConexao = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			//Obtendo o numero do Protocolo Unico.
			numeroProtocolo = this.consultarProtocoloUnico(numeroIP);
			
			// Mensagem informativa de operacao OK e codigo de retorno OK
			mensagem = "BS " + numeroBS + " publicado sob o Protocolo Unico " + String.valueOf(numeroProtocolo);
			codigoRetorno = Definicoes.RET_OPERACAO_OK;
			
			// Monta o XML que sera publicado
			String xmlPublicacao = PublicadorBSXMLParser.getXMLPublicacao(Definicoes.PROC_PUBLICACAO_PROT_UNICO, numeroProtocolo,
																		  numeroAssinante, numeroBS, matriculaOperador);
			
			// Insere as informacoes do Protocolo Unico na tabela de Interface do Vitria
			dao.inserePublicacao(prepConexao, Definicoes.PROC_PUBLICACAO_PROT_UNICO, xmlPublicacao);
			
			super.log(Definicoes.INFO, "publicarBS", mensagem);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "publicarBS", "Excecao na publicacao do BS " + numeroBS + ": " + e.getMessage());
			codigoRetorno	= Definicoes.RET_ERRO_TECNICO;
			mensagem		= "Excecao na publicacao do BS " + numeroBS + ". Codigo: " + codigoRetorno + " - Descricao: " + e.getMessage(); 
		}
		finally
		{
			// Monta o XML a ser retornado ao solicitante
			result = PublicadorBSXMLParser.getXMLRetorno(numeroBS, numeroIP, numeroProtocolo, mensagem, codigoRetorno, Definicoes.PROC_PUBLICACAO_PROT_UNICO);
			
			// Devolve a conexao
			gerenteBancoDados.liberaConexaoPREP(prepConexao, super.getIdLog());
		}
		
		// Retorno do XML de publicacao
		return result;
	}
	
	/**
	 *	Obtem do Toolbar o numero do protocolo unico.
	 *
	 *	@throws		Exception
	 */
	private long consultarProtocoloUnico(String numeroIP) throws Exception
	{
		long result = -1;
		
		//Obtendo a conexao com o Toolbar. 
		ConexaoToolbar toolbar = new ConexaoToolbar(numeroIP);
		
		//Obtendo o numero de tentativas e o tempo de espera para obtencao do numero do protocolo.
		MapConfiguracaoGPP mapConfiguracao = MapConfiguracaoGPP.getInstance();
		short	numTentativas	= Short.parseShort(mapConfiguracao.getMapValorConfiguracaoGPP("PUBLICADOR_BS_NUM_TENTATIVAS"));
		int		tempoEspera		= Integer.parseInt(mapConfiguracao.getMapValorConfiguracaoGPP("PUBLICADOR_BS_TEMPO_ESPERA"));
		
		//Obtendo o numero do protocolo unico.
		short tentativa = 0;
		while((result == -1) && (tentativa < numTentativas))
		{
			try
			{
				toolbar.conectar();
				result = toolbar.consultarProtocoloUnico();
			}
			catch(Exception e)
			{
				tentativa++;
				if(tentativa >= numTentativas)
					throw e;
				Thread.sleep(tempoEspera);
			}
			finally
			{
				this.fecharConexao(toolbar);
			}
		}
		
		return result;
	}
	
	/**
	 *	Fechar a conexao com o Toolbar.
	 * 
	 *	@param		toolbar					Conexao com o Toolbar.
	 */
	private void fecharConexao(ConexaoToolbar toolbar)
	{
		try
		{
			toolbar.fechar();
		}
		catch(Exception e)
		{
			super.log(Definicoes.WARN, "fecharConexoes", "Excecao ao fechar conexao com o Toolbar: " + e.getMessage());
		}
	}
}