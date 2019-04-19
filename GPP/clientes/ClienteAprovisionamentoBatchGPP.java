package clientes;

/*
 * Created on 18/05/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.brt.gpp.aplicacoes.aprovisionar.DadosAprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoHelper;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;
import com.brt.gpp.comum.Definicoes;

public class ClienteAprovisionamentoBatchGPP {

	public static final String RETURN="\n";
	public static final String OPERADOR="AprovisionamentoBatch";
	private aprovisionamento aprovisionamentoGPP;
	private SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String gppPort;
	private String gppHost;
	
	public ClienteAprovisionamentoBatchGPP()
	{
		aprovisionamentoGPP = null;
	}
	
	public void setGPPPort(String gppPort)
	{
		this.gppPort = gppPort;
	}

	public void setGPPHost(String gppHost)
	{
		this.gppHost = gppHost;
	}

	/**
	 * Este metedo retorna o parametro onde define a porta TCP no qual o 
	 * aplicativo GPP esta "escutando" as requisicoes
	 * @return String porta do GPP
	 */
	public String getGPPPort()
	{
		return this.gppPort;
	}

	/**
	 * Este metedo retorna o parametro onde define o servidor (IP/Nome) no qual o 
	 * aplicativo GPP esta "escutando" as requisicoes
	 * @return String servidor GPP
	 */	
	public String getGPPHost()
	{
		return this.gppHost;
	}
	
	/**
	 * Este metodo retorna o componente ORB para a execucao dos aprovisionamentos
	 * no GPP
	 * @return aprovisionamento - Objeto para aprovisionamento
	 * @throws IOException
	 */
	public aprovisionamento getAprovisionamentoGPP()
	{
		if (aprovisionamentoGPP == null)
		{
			String args[] = {getGPPPort(),getGPPHost()};
			java.util.Properties props = System.getProperties();
			props.put("vbroker.agent.port", args[0]);
			props.put("vbroker.agent.addr", args[1]);
			System.setProperties ( props );
			
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			
			byte[] managerId = "ComponenteNegociosAprovisionamento".getBytes();	
			aprovisionamentoGPP = aprovisionamentoHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		}
		return aprovisionamentoGPP;
	}

	/**
	 * Este metodo executa a ativacao de assinantes
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short ativaAssinante(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.ativaAssinante (apr.getMsisdn(), apr.getImsi(), 
		                              apr.getPlanoPreco(), apr.getCreditoInicial(), 
		                              (short)apr.getIdioma(), ClienteAprovisionamentoBatchGPP.OPERADOR );
	}

	/**
	 * Este metodo executa a desativacao de assinantes
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short desativaAssinante(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		retornoDesativacaoAssinante ret = 
		         aprGPP.desativaAssinante (apr.getMsisdn(), 
		                                   apr.getMotivoDesativacao(), 
										   ClienteAprovisionamentoBatchGPP.OPERADOR);
		return ret.codigoRetorno;
	}

	/**
	 * Este metodo executa o bloqueio de assinantes
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short bloqueio(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.bloqueiaAssinante (apr.getMsisdn(), String.valueOf(apr.getIdMotivoBloqueio()),
		                                 apr.getTarifa(), ClienteAprovisionamentoBatchGPP.OPERADOR);
	}

	/**
	 * Este metodo executa o desbloqueio de assinantes
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short desbloqueio(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.desbloqueiaAssinante (apr.getMsisdn(), ClienteAprovisionamentoBatchGPP.OPERADOR);
	}

	/**
	 * Este metodo executa a troca do msisdn do assinante
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short trocaMsisdn(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.trocaMSISDNAssinante (apr.getMsisdn(),
		                                    apr.getNovoMsisdn(),
		                                    String.valueOf(apr.getIdMotivoTroca()),
		                                    apr.getTarifa(),
		                                    ClienteAprovisionamentoBatchGPP.OPERADOR);
	}

	/**
	 * Este metodo executa a troca de plano de preco do assinante
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short trocaPlanoPreco(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.trocaPlanoAssinante (apr.getMsisdn(), 
                                           apr.getNovoPlanoPreco(),
                                           apr.getTarifa(),
		                                   ClienteAprovisionamentoBatchGPP.OPERADOR,
		                                   apr.getFranquia());
	}
	
	/**
	 * Este metodo executa a troca do Simcard do assinante
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short trocaSimcard(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.trocaSimCardAssinante (apr.getMsisdn(),
		                                     apr.getNovoImsi(),
		                                     apr.getTarifa(),
		                                     ClienteAprovisionamentoBatchGPP.OPERADOR);
	}

	/**
	 * Este metodo executa a atualizacao de Family and Friends do assinante
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short atualizaFF(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		return aprGPP.atualizaFriendsFamilyAssinante(apr.getMsisdn(),
		                                             apr.getListaFF(),
		                                             ClienteAprovisionamentoBatchGPP.OPERADOR,
		                                             "NAO_ALTERA_PLANO");
	}

	/**
	 * Este metodo executa a troca de senha do assinante
	 * @param DadosAprovisionamento apr - Dados a serem aprovisionados 
	 * @param aprovisionamento aprGPP - componente GPP para aprovisionamento
	 * @return short - codigo de retorno da execucao do aprovisionamento
	 */
	public short trocaSenha(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
	{
		String aXML = "<?xml version=\"1.0\"?>";
		aXML = aXML + "<GPPTrocaSenha>";
		aXML = aXML + "<msisdn>" + apr.getMsisdn() + "</msisdn>";
		aXML = aXML + "<novaSenha>" + apr.getSenha() + "</novaSenha>";
		aXML = aXML + "</GPPTrocaSenha>";	

		if (aprGPP.trocaSenha(aXML) != null)
			return 0;
		
		return -1; 
	}
	
	/**
     *  Este método executa a atualização do status do assinante.
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      status                  Novo status do assinante.
     *  @param      dataExpiracao           Nova data de expiracao dos saldos, no formato dd/mm/yyyy.
     *  @param      operador                Identificador do operador.
     *  @return     Codigo de retorno da operacao.
     */
    public short alteraStatusAssinante(DadosAprovisionamento apr, aprovisionamento aprGPP) throws Exception
    {
    		return aprGPP.alterarStatusAssinante(apr.getMsisdn(),
            									 apr.getStatus(),
            									 (!apr.getDataExpiracao().equals("")) ? apr.getDataExpiracao() : null,
            									 apr.getOperador());
            
    }	
	
	/**
	 * Este metodo executa o aprovisionamento baseado em cada informacao
	 * do objeto de aprovisionamento
	 * @param apr - Dados do aprovisionamento
	 * @return String - Contendo a linha do aprovisionamento + indicacao de sucesso/erro
	 * @throws IOException
	 */
	public String aprovisiona(DadosAprovisionamento apr) throws Exception
	{
		String aRetorno;
		aRetorno = this.formatoData.format(new Date()) + " " + apr;
		aprovisionamento aprGPP = getAprovisionamentoGPP();

		short retCode=-1;
		try
		{
			if (apr.getAcao().equals(Definicoes.TIPO_APR_ATIVACAO))
				retCode = ativaAssinante(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_DESATIVACAO))
				retCode = desativaAssinante(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_BLOQUEIO))
				retCode = bloqueio(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_DESBLOQUEIO))
				retCode = desbloqueio(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_TROCA_MSISDN))
				retCode = trocaMsisdn(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_TROCA_PLANO))
				retCode = trocaPlanoPreco(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_TROCA_SIMCARD))
				retCode = trocaSimcard(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_ATUALIZA_FF))
				retCode = atualizaFF(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_TROCA_SENHA))
				retCode = trocaSenha(apr,aprGPP);
			else if (apr.getAcao().equals(Definicoes.TIPO_APR_TROCA_STATUS_ASSINANTE))
				retCode = alteraStatusAssinante(apr, aprGPP);
			
			if (retCode==0)
				aRetorno += " Executado com Sucesso.";
			else aRetorno += " Executado com Erro. Codigo:" + retCode;
		}
		catch(Exception e)
		{
			aRetorno += " Executado com Erro. Erro:"+e;
		}

		return aRetorno;
	}
		
	/**
	 * Este metodo tem como objetivo o processamento de um arquivo de 
	 * aprovisionamento. Atraves desse arquivo, serao criados objetos correspondendo
	 * as linhas do mesmo para posterior aprovisionamento em massa.
	 * @param File arq - Arquivo a ser processado.
	 */
	public void processaArquivo(File arq) throws Exception
	{
		int posSeparador = arq.getAbsolutePath().indexOf(".");
		String tipoArquivo = arq.getAbsolutePath().substring(posSeparador+1);
		File arqLog = new File(arq.getAbsolutePath().replaceAll(tipoArquivo, "log"));
		
		FileWriter fWriter = new FileWriter(arqLog);
		fWriter.write(this.formatoData.format(new Date())+ " - Inicio do processo de aprovisionamento Batch");
		fWriter.write(ClienteAprovisionamentoBatchGPP.RETURN);
		
		Collection aprovisionamentos = DadosAprovisionamento.carregaDoArquivo(arq);
		for (Iterator i = aprovisionamentos.iterator(); i.hasNext();)
		{
			DadosAprovisionamento apr = (DadosAprovisionamento)i.next();
			fWriter.write(aprovisiona(apr));
			fWriter.write(ClienteAprovisionamentoBatchGPP.RETURN);
			fWriter.flush();
		}
		
		fWriter.write(this.formatoData.format(new Date())+ " - Fim do processo de aprovisionamento Batch");
		fWriter.write(ClienteAprovisionamentoBatchGPP.RETURN);
		fWriter.flush();		
		fWriter.close();
	}

	/**
	 * Este metodo tem como objetivo processar todos os arquivos de aprovisionamento
	 * de um diretorio. Obs: E importante que o diretorio so contenha arquivos conhecidos
	 * do processamento, caso contrario sera retornado um erro.
	 * 
	 * @param File dir - Diretorio contendo os arquivos de aprovisionamento
	 */
	public void processaDiretorio(File dir) throws Exception
	{
		File arquivos[] = dir.listFiles();
		for (int i=0; i<arquivos.length; i++)
			if (arquivos[i].isFile())
				processaArquivo(arquivos[i]);
	}

	public static void main(String[] args)
	{
		try
		{
			ClienteAprovisionamentoBatchGPP batch = new ClienteAprovisionamentoBatchGPP();
			File file;
			if (args != null && args.length > 0)
			{
				batch.setGPPPort(args[0]);
				batch.setGPPHost(args[1]);
				if ( (file=new File(args[2])).isFile() )
					batch.processaArquivo(file);
				else if ((file=new File(args[2])).isDirectory())
					batch.processaDiretorio(file);
			}
			System.out.println("Aprovisionamento batch executado. Verificar arquivos de logs.");
		}
		catch (Exception e)
		{
			System.out.println("Erro ao executar aprovisionamento batch. " + e);
			// Finaliza o cliente GPP indicando erro no processamento
			System.exit(1);
		}
	}
}
