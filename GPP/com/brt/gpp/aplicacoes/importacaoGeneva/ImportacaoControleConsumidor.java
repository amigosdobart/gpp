package com.brt.gpp.aplicacoes.importacaoGeneva;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerenteArquivosCDR;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Classe respons�vel pelo tratamento das ativa��es e
 * desativa��es enviadas pelo Geneva. E inser��o desses
 * dados na tabela de assinantes h�bridos.
 * Caso o assinante j� exista na tabela seus dados s�o 
 * atualizados.
 * Caso o assinante tenha sido desativado ele s� ser�
 * removido se o assinante tamb�m estiver inativo no GPP.
 * 
 * @author 	Marcelo Alves Araujo
 * @since	05/04/2006
 *
 */
public class ImportacaoControleConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private ProcessoBatchProdutor	produtor;
	private PREPConexao				conexaoBanco;
	private Aprovisionar			aprovisiona;
	private SimpleDateFormat 		formato;
	
	/**
	 * Construtor
	 */
	public ImportacaoControleConsumidor()
	{
		super(GerentePoolLog.getInstancia(ImportacaoControleConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_GENEVA),Definicoes.CL_IMPORTACAO_GENEVA);
		formato = new SimpleDateFormat("yyyyMMdd");
	}

	/**
	 * Pega os dados do produtor, inicia a conx�o de banco e o objeto aprovisionar
	 * @param produtor - Produtor
	 */
	public void startup(ProcessoBatchProdutor produtor)
	{
		this.produtor = produtor;
		startup();
	}

	/**
	 * Inicia a conx�o de banco e o objeto aprovisionar
	 */
	public void startup()
	{
		super.log(Definicoes.DEBUG, "Consumidor.startup", "Inicio");
		// Pega a conex�o de banco do produtor
		this.conexaoBanco = this.produtor.getConexao();
		// Instancia um objeto de aprovisionamento
		this.aprovisiona = new Aprovisionar(super.getIdLog());
	}

	/**
	 * Pega os dados do produtor, inicia a conx�o de banco e o objeto aprovisionar
	 * @param produtor - Produtor
	 */
	public void startup(Produtor produtor)
	{
		startup((ProcessoBatchProdutor)produtor);
	}

	/**
	 * Processa as linhas do arquivos e faz as atualiza��es necess�rias na tabela de h�bridos
	 * @param obj - Arquivo com a lista de ativa��es e desativa��es no Geneva
	 */
	public void execute(Object obj)
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");
		ArquivoGeneva	parser		= new ArquivoGeneva();
		
		try
		{
			// Inicializa instancias para tratamento da leitura do arquivo
			FileReader fReader;
			
			// Abre o arquivo de registros de ativa��es e desativa��es
			fReader = new FileReader((File)obj);
			BufferedReader	buffReader	= new BufferedReader(fReader);
			
			
			String linha=null;
			
			while ( (linha=buffReader.readLine()) != null )
			{
				// Utiliza o parser para realizar o parse da linha em processamento
				parser.parse(linha);
				
				// Realiza o processamento da linha atual do arquivo contendo os campos ja definidos
				processaRegistro(parser);
			}
			
			// Busca a referencia para o arquivo de configuracao do sistema GPP
			ArquivoConfiguracaoGPP config = ArquivoConfiguracaoGPP.getInstance();
			
			GerenteArquivosCDR.moveArquivo((File)obj, config.getDirGenevaDestino(),super.getIdLog());
		} 
		catch (FileNotFoundException e)
		{
			super.log(Definicoes.ERRO,"Consumidor.execute","O arquivo nao foi encontrado. Arquivo: " + ((File)obj).getName());
		} 
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO,"Consumidor.execute","Erro ao processar registro do arquivo. Msisdn: " + parser.getCodigoNacional() + parser.getPrefixo() + parser.getMcdu());
		} 
		catch (GPPTecnomenException e)
		{
			super.log(Definicoes.ERRO,"Consumidor.execute","Erro ao consultar assinante: " + parser.getCodigoNacional() + parser.getPrefixo() + parser.getMcdu());
		} 
		catch (IOException e)
		{
			super.log(Definicoes.ERRO,"Consumidor.execute","Erro ao ler arquivo: " + ((File)obj).getName());
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO,"Consumidor.execute","Erro generico: " + e);
		}
	}

	/**
	 * Finaliza o consumidor
	 */
	public void finish()
	{
		super.log(Definicoes.DEBUG, "Consumidor.finish", "Fim do Consumidor");
	}

	/**
	 * Insere, remove e atualiza assinantes h�bridos de acordo com certos par�metros
	 * @param parser - Objeto com os dados do arquivo
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	private void processaRegistro(ArquivoGeneva parser) throws GPPInternalErrorException, GPPTecnomenException
	{		
		super.log(Definicoes.DEBUG, "processaRegistro", "Processamento do registro. Msisdn: " + parser.getMsisdn());
		// Consulta o assinante 
		Assinante assinante = aprovisiona.consultaDetalhadaAssinante(parser.getMsisdn());
		
		// Testa se o registro � de ativa��o ou desativa��o de assinantes
		if(!parser.getRemoverAssinante())
		{
			// Testa se o assinante � h�brido na plataforma
			if(eHibrido(assinante))
			{			
				// Verifica se o assinante est� na tabela de h�bridos
				if(!verificaAprHibrido(parser.getMsisdn()))
					insereHibrido(parser.getMsisdn(),parser.getVlrCredito(),formato.format(assinante.getDataAtivacao()),parser.getDataAtivacao(), parser.getNumContrato());
				else
					atualizaHibrido(parser.getDataAtivacao(),parser.getNumContrato(),parser.getMsisdn());
			}
			else
			{
				if(!verificaAprHibrido(parser.getMsisdn()))
					insereHibrido(parser.getMsisdn(),parser.getVlrCredito(),null,parser.getDataAtivacao(), parser.getNumContrato());
				else
					atualizaHibrido(parser.getDataAtivacao(),parser.getNumContrato(),parser.getMsisdn());			
			}
			
		}
		else
		{
			// Testa se o assinante � h�brido na plataforma
			if(eHibrido(assinante))
				atualizaHibrido(null,null,parser.getMsisdn());
			else
				removeHibrido(parser.getMsisdn());
		}
	}
	
	/**
	 * Verifica se o assinante � h�brido
	 * @param assinante - Objeto com os dados do assinante
	 * @return boolean - true se o assinante for h�brido e false se n�o for
	 * @throws GPPInternalErrorException
	 */
	private boolean eHibrido(Assinante assinante) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "eHibrido", "Testa se o assinante e hibrido na plataforma.");
		if(assinante != null && aprovisiona.eHibrido(""+assinante.getPlanoPreco()))
			return true;
		return false;
	}
	
	/**
	 * Verifica se o assinante est� na tabela de assinantes h�bridos
	 * @param msisdn - N�mero do assinante
	 * @return boolean - true se existir na tabela, false se n�o estiver
	 * @throws GPPInternalErrorException
	 */
	private boolean verificaAprHibrido(String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "verificaAprHibrido", "Testa se o est� na tabela de hibridos.");
		// Verifica se o assinante est� registrado na tabela de assinantes h�bridos
		String consultaHibrido = "SELECT 1 FROM tbl_apr_plano_hibrido WHERE idt_msisdn = ?";
		Object[] hibrido = {msisdn};
		if(conexaoBanco.executaPreparedUpdate(consultaHibrido, hibrido, getIdLog())>0)
			return true;
		return false;
	}
	
	/**
	 * Insere o registro de um novo assinante na tabela de h�bridos
	 * @param msisdn			- N�mero do assinante
	 * @param vlrCredito		- Cr�dito da franquia
	 * @param dataAtivacaoGPP	- Data de ativa��o no GPP (aaaammdd)
	 * @param dataAtivacaoGNV	- Data de ativa��o no Geneva (aaaammdd)
	 * @param numContrato		- N�mero do contrato SFA
	 * @throws GPPInternalErrorException
	 */
	private void insereHibrido(String msisdn,Double vlrCredito,String dataAtivacaoGPP,String dataAtivacaoGNV,String numContrato) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "insereHibrido", "Insercao de assinante na tabela de hibridos.");
		String insereHibrido = 	"INSERT INTO tbl_apr_plano_hibrido " +
								"(idt_msisdn, vlr_cred_fatura, vlr_cred_carry_over, vlr_saldo_inicial, " +
								"dat_ciclo, num_mes_execucao, dat_ultima_recarga_processada, ind_drop, " +
								"dat_ativacao_gpp, dat_ativacao_geneva, num_contrato) " +
								"VALUES " +
								"(?,?,0,0,NULL,NULL,NULL,0,to_date(?,'yyyymmdd'),to_date(?,'yyyymmdd'),?)";
		Object[] novoHibrido = {msisdn,vlrCredito,dataAtivacaoGPP,dataAtivacaoGNV,numContrato};
		conexaoBanco.executaPreparedUpdate(insereHibrido, novoHibrido, getIdLog());
	}
	
	/**
	 * Atualiza os dados do assinante na tabela de h�bridos
	 * @param dataAtivacaoGNV	- Data de ativa��o no Geneva (aaaammdd)
	 * @param numContrato		- N�mero do contrato SFA
	 * @param msisdn			- N�mero do assinante
	 * @throws GPPInternalErrorException
	 */
	private void atualizaHibrido(String dataAtivacaoGNV, String numContrato, String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "atualizaHibrido", "Atualizacao de assinante na tabela de hibridos.");
		String atualizaHibrido =	"update tbl_apr_plano_hibrido " +
									"set dat_ativacao_geneva = to_date(?,'yyyymmdd'), " +
									"    num_contrato = ? " +
									"where idt_msisdn = ?";

		Object[] paramHibrido = {dataAtivacaoGNV,numContrato,msisdn};
		conexaoBanco.executaPreparedUpdate(atualizaHibrido, paramHibrido, getIdLog());	
	}
	
	/**
	 * Remove o registro de um assinante na tabela de h�bridos
	 * @param msisdn			- N�mero do assinante
	 * @throws GPPInternalErrorException
	 */
	private void removeHibrido(String msisdn) throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "removeHibrido", "Remocao de assinante na tabela de hibridos.");
		String removeHibrido = 	"delete from tbl_apr_plano_hibrido " +
								"where idt_msisdn = ?";
		
		Object[] paramHibrido = {msisdn};
		conexaoBanco.executaPreparedUpdate(removeHibrido, paramHibrido, getIdLog());
	}
}
