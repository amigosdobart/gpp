package com.brt.gpp.aplicacoes.gerenciamentoVouchers;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.dao.PedidoDAO;
import com.brt.gpp.aplicacoes.gerenciamentoVouchers.entity.VoucherPedido;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.conexoes.email.GPPMail;

import java.io.File;
import java.util.Date;
import java.util.Calendar;
import java.sql.ResultSet;

/**
  * Esta classe e a responsavel pela transmissao do pedido de criacao
  * de voucher para o seu respectivo requisitante. Esta classe assume
  * que o arquivo de dados do voucher ja existe e esteja disponivel
  * para envio no diretorio configurado no GPP
  * 
  * <P> Versao:        	1.0
  *
  * @Autor:            	Joao Carlos
  * Data:               02/08/2004
  *
  * Modificado por:
  * Data:
  * Razao:
  *
  */

public class TransmitePedidoVoucher extends Aplicacoes 
{
	private GerentePoolBancoDados 	gerenteBancoDados 	= null; // Gerente de conexoes Banco Dados
	private MapConfiguracaoGPP		mapConfig			= null; //Mapeamento da TBL_GER_CONFIG_GPP
	
	/**
	 * Metodo....:TransmitePedidoVoucher
	 * Descricao.:Construtor da classe
	 * @param idProcesso - Id do processo que esta iniciando a classe
	 */
	public TransmitePedidoVoucher(long idProcesso)
	{
		super(idProcesso, Definicoes.CL_TRANSMITE_PEDIDO_VOUCHER);
		gerenteBancoDados 	= GerentePoolBancoDados.getInstancia(idProcesso);
		// Instanciando mapeamento da TBL_GER_CONFIG_GPP
		try
		{
			//Instacia do mapeamento da ConfigGPP
			mapConfig = MapConfiguracaoGPP.getInstancia();
			if (mapConfig == null)
				super.log(Definicoes.WARN, "TransmitePedidoVoucher", "Problemas com Mapeamento das Configurações GPP");		
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "TransmitePedidoVoucher", "Problemas com Mapeamento das Configurações GPP");
		}
	}

	/**
	 * Metodo....:acertaMensagemEMail
	 * Descricao.:Este metodo acerta a mensagem que sera gravada no e-mail a ser enviado
	 *            para o requisitante de pedido de voucher. Os parametros passados por
	 *            parametro sao pesquisados na mensagem sua ocorrencia e entao trocados
	 *            pelo valor correspondente.
	 * @param mensagem		- Mensagem a ser anexada ao e-mail
	 * @param parametros	- Parametros para troca do conteudo da mensagem
	 * @return String		- Nova mensagem com conteudo alterado
	 */	
//	private String acertaMensagemEMail(String mensagem,String parametros[])
//	{
//		for (int i=1; i <= parametros.length; i++)
//		{
//			// Pesquisa na mensagem o conjunto de caracters identificando
//			// o parametro. Ex: Pedido numero %1. O conjunto %1 devera entao
//			// ser trocado pelo valor do 1 parametro e assim sucessivamente.
//			mensagem.replaceAll("\\%"+i,parametros[i-1]);
//		}
//		return mensagem;
//	}

	/**
	 * Metodo....:getNumeroPedido
	 * Descricao.:Pesquisa qual o numero do pedido de uma certa ordem criada
	 * @param numeroOrdem	- Numero da Ordem
	 * @return long			- Numero do pedido correspondente
	 * @throws GPPInternalErrorException
	 */
	private long getNumeroPedido(long numeroOrdem) throws GPPInternalErrorException
	{
		PREPConexao conexaoPrep = null;
		long numeroPedido = 0;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sql = "SELECT NUM_PEDIDO FROM TBL_REC_VOUCHER_PEDIDO WHERE NUM_ORDEM = ?";
			Object param[] = {new Long(numeroOrdem)};
			ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
			if (rs.next())
				numeroPedido = rs.getLong("NUM_PEDIDO");
		}
		catch(Exception ge)
		{
			throw new GPPInternalErrorException(ge.toString());
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		return numeroPedido;
	}

	/**
	 * Metodo....:enviaPedidoPorEMail
	 * Descricao.:Esta classe realiza o envio da mensagem por e-mail ao requisitante
	 *            do pedido de cartoes (vouchers) 
	 * @param numeroOrdem	- Numero da ordem a ser enviada
	 * @throws GPPInternalErrorException
	 */
	public void enviaPedidoPorEMail(long numeroOrdem) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO,"enviaPedidoPorEMail","Inicio NRO-ORDEM "+numeroOrdem);
		// Inicia referencia com a classe de batimento para buscar o objeto
		// contendo informacoes do pedido de voucher
		VoucherPedido pedidoVoucher = PedidoDAO.buscaPedido(getNumeroPedido(numeroOrdem));

		// Como o arquivo de voucher contempla todos os vouchers do pedido, entao
		// somente podera ser enviado se o status do pedido for PROCESSADO, indicando
		// que os cartoes virtuais foram ativados e os cartoes fisicos pelo menos foram
		// enviados ao SAP para a ativacao
		if (pedidoVoucher.getStatusPedido().equals(Definicoes.STATUS_PEDIDO_VOUCHER_PROCESSADO) ||
			pedidoVoucher.getStatusPedido().equals(Definicoes.STATUS_PEDIDO_VOUCHER_TRANSMITIDO) )
		{
			String assunto;
			// Busca os valores de configuracao para as informacoes do e-Mail
			if (Definicoes.TIPO_CARTAO_LIGMIX.equals(pedidoVoucher.getTipCartao()))
			{
				assunto = mapConfig.getMapValorConfiguracaoGPP("ASSUNTO_EMAIL_VOUCHER_LIGMIX");
			}
			else
			{
				assunto  = mapConfig.getMapValorConfiguracaoGPP("ASSUNTO_EMAIL_PEDIDO_VOUCHER");
			}

			// Cria o objeto de e-mail definindo o e-mail origem, assunto e a mensagem
			// observando que a mensagem deve ser acertada com os parametros
			GPPMail eMail = new GPPMail(mapConfig.getMapValorConfiguracaoGPP("EMAIL_VOUCHER_ADMIN"),super.getIdLog());
			eMail.setTipoMensagem   (GPPMail.TIPO_TEXTO_HTML);
			eMail.setAssunto        (assunto);
			eMail.setMensagem       (pedidoVoucher.toHTML());
			eMail.addEnderecoDestino(pedidoVoucher.getEMailRequisitante());
			eMail.addArquivoAnexo   (new File(mapConfig.getMapValorConfiguracaoGPP(Definicoes.DIR_IMPORTACAO_ORDENS_VOUCHER)+
			                                  System.getProperty("file.separator")+
			                                  pedidoVoucher.getNomeArquivo()));		
			// Envia o e-Mail
			eMail.enviaMail();
			
			// Caso o e-mail tenha sido enviado entao atualiza a data
			// do envio deste e-mail na tabela de pedidos de voucher
			atualizaDataEnvioOrdem(numeroOrdem,Calendar.getInstance().getTime());
		}
		else
			super.log(Definicoes.WARN,"enviaPedidoPorEMail","Ordem de criacao de voucher numero "+numeroOrdem+" nao pode ser enviada por e-mail. Status:"+pedidoVoucher.getStatusPedido());
		super.log(Definicoes.INFO,"enviaPedidoPorEMail","Fim");
	}
	
	/**
	 * Metodo....:atualizaDataEnvioOrdem
	 * Descricao.:Atualiza a data de envio da ordem por e-mail
	 * @param numOrdem 	- Numero da ordem a ser atualizada
	 * @param dataEnvio	- Data de envio do e-mail
	 * @throws GPPInternalErrorException
	 */
	public void atualizaDataEnvioOrdem(long numOrdem, Date dataEnvio) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG,"atualizaDataEnvioOrdem","Inicio NRO-ORDEM "+numOrdem+" DATAENVIO e-mail "+dataEnvio);
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			String sqlUpdate = "UPDATE TBL_REC_VOUCHER_PEDIDO " +
			                      "SET DAT_ENVIO_EMAIL = ? " +
								"WHERE NUM_ORDEM       = ? ";
			Object param[] = {new java.sql.Timestamp(dataEnvio.getTime()), new Long(numOrdem)};
			conexaoPrep.executaPreparedUpdate(sqlUpdate,param,super.getIdLog());
		}
		catch(GPPInternalErrorException ge)
		{
			throw new GPPInternalErrorException(ge.toString());
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
		super.log(Definicoes.DEBUG,"atualizaDataEnvioOrdem","Fim");
	}
}
