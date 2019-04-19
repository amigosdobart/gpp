package com.brt.gpp.aplicacoes.enviarUsuariosShutdown;

import java.io.StringReader;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 * @author Vanessa Andrade
 * Adaptacao Joao Carlos
 *
 */
public class EnvioUsuariosShutdownConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao		conexaoPrep;

	/**
	 * Metodo....:EnvioUsuariosShutdownProdutor
	 * Descricao.:Construtor da classe do processo batch
	 */
	public EnvioUsuariosShutdownConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioUsuariosShutdownConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_SHUTDOWN)
		     ,Definicoes.CL_ENVIO_USUARIO_SHUTDOWN);
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		conexaoPrep = produtor.getConexao();
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup();
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
		UsuariosShutdownVO usrShutdown = (UsuariosShutdownVO)obj;
		
		String sqlInsert = "INSERT INTO TBL_INT_PPP_OUT  " +
						   " (ID_PROCESSAMENTO, DAT_CADASTRO, IDT_EVENTO_NEGOCIO, " +
						   " XML_DOCUMENT, IDT_STATUS_PROCESSAMENTO) " +
						   " VALUES " +
						   " (SEQ_ID_PROCESSAMENTO.NEXTVAL, SYSDATE, ?, ?, ?)";
		Object paramInsert[] = {Definicoes.IND_EVENTO_SHUTDOWN_PPP_OUT
				               ,new StringReader(geraUsuariosShutdonwXML(usrShutdown))
							   ,Definicoes.IDT_PROCESSAMENTO_NOT_OK
							   };
		int numLinhas = conexaoPrep.executaPreparedUpdate(sqlInsert,paramInsert,super.getIdLog());
		super.log(Definicoes.DEBUG,"execute","Usuario "+usrShutdown.getMsisdn() + (numLinhas > 0 ? " incluido na interface" : " nao incluido na interface"));
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	    
	}
	
	/**
	 * Metodo....:geraUsuariosShutdonwXML
	 * Descricao.:Monta o XML que sera armazenado na tabela de interface
	 *            resultante do processamento consumidor
	 * @param usrShutdown 	- ValueObject contendo informacoes de Usuarios que entraram no status Shutdown
	 * @return String		- XML a ser gravado na tabela de interface
	 */
	private String geraUsuariosShutdonwXML(UsuariosShutdownVO usrShutdown)
	{
		GerarXML geradorXML = new GerarXML("GPPStatus");
		// Criação das Tags Defaults do XML
		geradorXML.adicionaTag("msisdn",usrShutdown.getMsisdn());
		geradorXML.adicionaTag("status",usrShutdown.getStatus());
		geradorXML.adicionaTag("data"  ,usrShutdown.getData());

		//Retorno do XML completo
		String xml = new String("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + geradorXML.getXML()); 
		return xml; 
	}
}
