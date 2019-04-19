package com.brt.gpp.aplicacoes.enviarUsuariosDisconnected;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.aplicacoes.promocao.controle.ControlePulaPula;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;

/**
 *
 * Este arquivo contem a definicao da classe de EnvioUsuariosDisconnectedConsumidor.
 * Responsável pela envio dos usuários que entraram ou sairam do status Disconnected 
 * para que eles sejam processados pelo Clarify e ASAP e tenham suas promoções retiradas 
 * ou colocadas, assim como o selective bypass.
 * 
 * <P> Versao:	1.0
 *
 * @Autor:		Marcelo Alves Araujo
 * Data:		13/10/2005
 *
 */
public class EnvioUsuariosDisconnectedConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao		conexaoPrep;
	private ConsultaAssinante consulta;
	private Assinante assinante;
	
	/**
	 * Metodo....:EnvioUsuariosDisconnectedProdutor
	 * Descricao.:Construtor da classe do processo batch
	 */
	public EnvioUsuariosDisconnectedConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioUsuariosDisconnectedConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_DISCONNECTED),Definicoes.CL_ENVIO_USUARIO_DISCONNECTED);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		conexaoPrep = produtor.getConexao();
		consulta = new ConsultaAssinante(super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor)produtor);
	}	

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
     */
    public void startup(ProcessoBatchProdutor produtor) throws Exception
    {
        this.produtor = produtor;
        startup();
    }

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object obj) throws Exception
	{
		// Faz o cast do objeto para o ValueObject desejado
		// apos isso realiza o insert na tabela de interface
		// armazenando o xml construido a partir dessas informacoes
		UsuariosDisconnectedVO usrDisconnected = (UsuariosDisconnectedVO)obj;
		
		// Número da OS
		String os = getNumeroOS();
		
		// Insere na tabela a requisição para mudança de status do usuário no clarify
		String sqlClarify = "INSERT INTO TBL_INT_PPP_OUT  " +
						   	" (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
						   	" XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) " +
						   	" VALUES " +
						   	" (SEQ_ID_PROCESSAMENTO.NEXTVAL, SYSDATE, ?, ?, ?)";
		
		// Insere na tabela de interface a solicitação de retirada/inserção do selective-bypass
		String sqlAsap = 	"INSERT INTO TBL_INT_PPP_OUT " +
							" (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) " +
							" VALUES " +
							" (?, SYSDATE, ?, ?, ?)";
		
		// Tipo de operação: Bloquear ou Desbloquear assinante no ASAP
		String operacao = (usrDisconnected.getStatus() == Definicoes.DISCONNECTED) ? Definicoes.XML_OS_BLOQUEAR : Definicoes.XML_OS_DESBLOQUEAR;
		
		Object[] paramAsap = {os
							 ,Definicoes.IDT_EVT_SELECTIVE_BYPASS
							 ,montaXMLAprovisionamento(Definicoes.SO_GPP + alinhaString(os,false,'0',13), usrDisconnected.getMsisdn(), Definicoes.XML_OS_CATEGORIA_PREPAGO, operacao)
							 ,Definicoes.IDT_PROCESSAMENTO_NOT_OK};
		
		Object[] paramlarify = {Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT
				               ,montaXMLClarify(usrDisconnected)
							   ,Definicoes.IDT_PROCESSAMENTO_NOT_OK};
		
		// Consulta o status do assinante na tecnomen
		assinante = consulta.executaConsultaSimplesAssinanteTecnomen(usrDisconnected.getMsisdn());
		
		// Troca a promoção para a mais atual
		if( usrDisconnected.getStatus()== Definicoes.DISCONNECTED )
		{
			/* Comentado pela ativação da interface com o Clarify
			Date dataExecucao = new Date();
			
			// Muda a promocao do assinante em estado DISCONNECTED para a promocao atual
			ControlePulaPula controle = new ControlePulaPula(GerentePoolLog.getInstancia(this.getClass()).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_DISCONNECTED));
			// Pega a promocao vigente
			MapPromocao promocoes = MapPromocao.getInstancia();
			Integer promocaoAtual = ((Promocao)promocoes.getListaPromocoes(new Date()).iterator().next()).getIdtPromocao();
			*/
			
			if(assinante != null && assinante.getStatusAssinante() == Definicoes.DISCONNECTED)
			{
				// Insere um xml na interface para retirada do selective bypass pelo ASAP
				int numLinhas = conexaoPrep.executaPreparedUpdate(sqlAsap, paramAsap, super.getIdLog());
				super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface ASAP" : " nao incluido na interface ASAP" ));
				
				// Insere um xml na interface para mudança do status do assinante no clarify
				numLinhas = conexaoPrep.executaPreparedUpdate(sqlClarify,paramlarify,super.getIdLog());
				super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface Clarify" : " nao incluido na interface Clarify"));
			
				/* Comentado pela ativação da interface com o Clarify
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.MONTH, 1);
				
				SimpleDateFormat dat = new SimpleDateFormat("yyyyMM");
				
				AssinantePulaPula assPP = controle.consultaPromocaoPulaPula(usrDisconnected.getMsisdn(), null, dat.format(cal.getTime()), assinante);
				
				// Só altera a promoção vigente se for diferente da promoção do assinante 
				if( assPP != null && !promocaoAtual.equals(assPP.getPromocao().getIdtPromocao()) )
				{
					// Insere um xml na interface para retirada do selective bypass pelo ASAP
					int numLinhas = conexaoPrep.executaPreparedUpdate(sqlAsap, paramAsap, super.getIdLog());
					
					super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface ASAP" : " nao incluido na interface ASAP" ));
										
					short retorno = controle.trocaPulaPula(usrDisconnected.getMsisdn(), dataExecucao, new Timestamp(dataExecucao.getTime()), Definicoes.GPP_OPERADOR, Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_DISCONNECTED);
					super.log(Definicoes.DEBUG,"execute","Troca de promocao realizada " + ((retorno == 0)? "com sucesso" : ("com erro: "+ retorno)));
				}
				//Caso o assinante não tenha promoção apenas desaprovisiona o cliente
				else
				{
					// Insere um xml na interface para retirada do selective bypass pelo ASAP
					int numLinhas = conexaoPrep.executaPreparedUpdate(sqlAsap, paramAsap, super.getIdLog());
					
					super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface ASAP" : " nao incluido na interface ASAP" ));
				}*/
			}
			else
				super.log(Definicoes.DEBUG,"execute","Assinante nao existe");
		}
		else
		{
			if(assinante != null && assinante.getStatusAssinante() == Definicoes.NORMAL)
			{
				ControlePulaPula controle = new ControlePulaPula(super.getIdLog());
				
				// Efetua a troca de promoção do assinante que retorna 
				short retorno = controle.trocaPulaPula(usrDisconnected.getMsisdn(), new Date(), new Timestamp(new Date().getTime()), Definicoes.GPP_OPERADOR, Definicoes.CTRL_PROMOCAO_MOTIVO_TROCA_PROMOCAO);
				super.log(Definicoes.DEBUG,"execute","Usuario: " + usrDisconnected.getMsisdn() + "Retorno: " + retorno);
				
				// Insere um xml na interface para retirada do selective bypass pelo ASAP
				int numLinhas = conexaoPrep.executaPreparedUpdate(sqlAsap, paramAsap, super.getIdLog());
				super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface ASAP" : " nao incluido na interface ASAP" ));
				
				// Insere um xml na interface para mudança do status do assinante no clarify
				numLinhas = conexaoPrep.executaPreparedUpdate(sqlClarify,paramlarify,super.getIdLog());
				super.log(Definicoes.DEBUG,"execute","Usuario " + usrDisconnected.getMsisdn() + (numLinhas > 0 ? " incluido na interface Clarify" : " nao incluido na interface Clarify"));
			}
			else
				super.log(Definicoes.DEBUG,"execute","Assinante nao existe");
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
		super.log(Definicoes.DEBUG,"Consumidor.finish","Alteracao do status do usuario" );
	}
	
	/**
	 * Metodo....:montaXMLClarify
	 * Descricao.:Monta o XML que sera armazenado na tabela de interface
	 *            resultante do processamento consumidor
	 * @param  usrDisconnected	- ValueObject contendo informacoes de Usuarios que entraram no status Disconnected
	 * @return String			- XML a ser gravado na tabela de interface
	 */
	private String montaXMLClarify(UsuariosDisconnectedVO usrDisconnected)
	{
		GerarXML geradorXML = new GerarXML("GPPStatus");
		// Criação das Tags Defaults do XML
		geradorXML.adicionaTag("msisdn",usrDisconnected.getMsisdn());
		geradorXML.adicionaTag("status",""+usrDisconnected.getStatus());
		geradorXML.adicionaTag("data"  ,usrDisconnected.getData());
		
		//Retorno do XML completo
		String xml = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML()); 
		
		return xml; 
	}
	
	/**
	 * Metodo...: getNumeroOS
	 * Descricao: Retorna um número de OS para o XML do provision (pega numa sequence do banco de dados)
	 * @return	String			- identificador da OS do provision
	 * @throws 	GPPInternalErrorException
	 */
	private String getNumeroOS() throws Exception
	{
		String retorno = null;
		ResultSet rsSeq = null;
		
		String sqlSequence = "SELECT SEQ_OS_PROVISION.NEXTVAL AS ID_OS FROM DUAL";
		
		try
		{
			// Retorna o próximo elemento da sequence do Banco de Dados
			rsSeq = conexaoPrep.executaQuery(sqlSequence,super.getIdLog());
			rsSeq.next();
			retorno = rsSeq.getString("ID_OS");			
		}
		catch(SQLException sqlE)
		{
			super.log(Definicoes.WARN, "getNumeroOS", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		finally
		{
			if(rsSeq != null)
				rsSeq.close();
		}
		return retorno;
	}
	
	/**
	 * Metodo...: montaXMLAprovisionamento
	 * Descricao: Retorna o XML de aprovisionamento/desaprovisionamento do selective-bypass
	 * @param 	os			- numero da OS
	 * 			msisdn		- MSISDN do assinante
	 * 			categoria	- categoria do assinante (F2 = pre-pago)
	 * 			operacao	- operacao a ser realizada
	 * @return	String		- XML de retorno
	 */
	private String montaXMLAprovisionamento(String os, String msisdn, String categoria, String operacao)
	{
		GerarXML gerador = new GerarXML("root");
		gerador.adicionaTag("id_os", os);
		gerador.adicionaTag("case_id", os);
		gerador.adicionaTag("order_priority", Definicoes.XML_OS_ORDER_LOW_PRIORITY);
		gerador.adicionaTag("categoria", categoria);
		gerador.adicionaTag("categoria_anterior", categoria);
		gerador.adicionaTag("case_type", Definicoes.XML_OS_CASE_TYPE_SEL_BYPASS);
		gerador.adicionaTag("case_sub_type", operacao);
		gerador.abreNo("provision");
		gerador.abreNo("ELM_INFO_SIMCARD");
		gerador.adicionaTag("macro_servico", "ELM_INFO_SIMCARD");
		gerador.adicionaTag("operacao", "nao_alterado");
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.adicionaTag("x_tipo", "SIMCARD");
		gerador.abreNo("parametros");
		gerador.adicionaTag("simcard_msisdn", msisdn);
		gerador.fechaNo();
		gerador.fechaNo();
		gerador.abreNo("ELM_SEL_BYPASS");
		gerador.adicionaTag("macro_servico", "ELM_SEL_BYPASS");
		gerador.adicionaTag("operacao", operacao);
		gerador.adicionaTag("status", "NAO_FEITO");
		gerador.fechaNo();
		gerador.fechaNo();
		return(gerador.getXML());
	}
	
	/**
	 * <p><b>Metodo...:</b> alinhaString</p>
	 * <p><b>Descricao:</b> Alinha uma String de acordo com os parâmetros</p>
	 * @param  original					- String original
	 * @param  alinhamento			<p>	- true : Alinhamento à esquerda
	 * 								<p>	- false: Alinhamento à direita
	 * @param  caracterPreenchimento	- Caracter com que será preenchida a String resultante
	 * @param  tamanhoString			- Número de caracteres da String resultante
	 * @return resultante				- Resultado do alinhamento
	 * @throws GPPInternalErrorException
	 */
	public String alinhaString(String original, boolean alinhamento, char caracterPreenchimento, int tamanhoString)
	{
	    String resultante = original;
	    // Se alinhamento for true, alinha a string à esquerda
	    if(alinhamento)
	        while(resultante.length() < tamanhoString)
	            resultante = resultante + caracterPreenchimento;
	    // Se alinhamento for false, alinha a string à direita
	    else
	        while(resultante.length() < tamanhoString)
	            resultante = caracterPreenchimento + resultante;
	    return resultante;
	}
}
