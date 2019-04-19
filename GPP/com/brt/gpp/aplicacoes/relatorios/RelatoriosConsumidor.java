package com.brt.gpp.aplicacoes.relatorios;

import java.io.File;
import java.sql.ResultSet;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
import com.brt.gppEnviaMail.GPPEnviaMailDefinicoes;
import com.brt.gppEnviaMail.aplicacao.CompactadorArquivos;
import com.brt.gppEnviaMail.aplicacao.ProcessadorResultadoCSV;
import com.brt.gppEnviaMail.conexoes.EnviaMail;


/**
 *
 * Este arquivo refere-se a classe RelatoriosConsumidor, execução das consultas
 * da tabela de solicitações de relatórios, gera um arquivo delimitado compactado
 * muda o status das solicitações na tabela e envia e-mail para o usuário avisando
 * da conclusão da consulta.
 * 
 * <P><b>Versao:</b>		1.0
 * @Autor: 					Marcelo Alves Araujo
 * <P><b>Data:</b> 			30/08/2005
 *
 */
public class RelatoriosConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
	private PREPConexao		conexaoPrep;
	
	/**
	 * <P><b>Metodo....:</b>RelatoriosConsumidor
	 * <P><b>Descricao.:</b>Construtor da classe do processo batch
	 */
	public RelatoriosConsumidor()
	{
		super(GerentePoolLog.getInstancia(RelatoriosConsumidor.class).getIdProcesso(Definicoes.CL_RELATORIOS_CONSUMIDOR),Definicoes.CL_RELATORIOS_CONSUMIDOR);
	}
	
	/**
	 * <P><b>Metodo....:</b> startup
	 * <P><b>Descricao.:</b> Pega uma conexão com o banco de dados
	 * 
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
		conexaoPrep = produtor.getConexao();
	}
	
	/**
	 * <P><b>Metodo....:</b> startup
	 * <P><b>Descricao.:</b> Pega uma conexão com o banco de dados
	 * 
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws Exception
	{
		startup((ProcessoBatchProdutor) produtor);
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
	 * <P><b>Metodo....:</b> execute
	 * <P><b>Descricao.:</b> Executa, altera o status e envia e-mail avisando 
	 *                       que a consulta foi realizada com sucesso 
	 * @param objeto - Objeto com os dados gerados pelo Produtor
	 * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
	 */
	public void execute(Object objeto) throws Exception
	{
		// Faz o cast do objeto para o ValueObject desejado
		RelatoriosVO relatorios = (RelatoriosVO)objeto;
		
		try
		{
			// Muda o estado da consulta para 'P' (Processando)
			this.trocaStatus(relatorios.getId(),Definicoes.IDT_PROCESSANDO);
			
			// Executa a consulta
			ResultSet resultado = conexaoPrep.executaPreparedQuery(relatorios.getConsulta(), null, super.getIdLog());
			
			// Coloca a data de execução como a do fim da consulta
			this.setDataExecucao(relatorios.getId());
			
			// Gera o arquivo
			this.geraArquivo(relatorios.getArquivo(),resultado);
			
			//Muda o estado da consulta para (Processado)
			if(relatorios.getTipoSolicitacao().equals(Definicoes.TIPO_SOLICITACAO_TEMPORARIA))
				this.trocaStatus(relatorios.getId(),Definicoes.IDT_PROCESSAMENTO_OK);
			else
				this.trocaStatus(relatorios.getId(), Definicoes.IDT_PROCESSO_PERMANENTE);
			
			// Envia e-mail avisando da conslusão da consulta
			if(relatorios.getEMail() != null)
				this.enviaMail(relatorios.getOperador(),relatorios.getEMail(),relatorios.getArquivo(),relatorios.getId());
		}
		catch(GPPInternalErrorException e)
		{
			this.trocaStatus(relatorios.getId(),Definicoes.IDT_PROCESSAMENTO_ERRO);
			super.log(Definicoes.ERRO,"Consumidor.execute","Consulta " + relatorios.getId() + " com problemas. Erro: " + e);
		}
		
		super.log(Definicoes.DEBUG,"Consumidor.execute","Consulta " + relatorios.getId());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{
	    
	}
	
	/**
	 * <P><b>Metodo....:</b> trocaStatus
	 * <P><b>Descricao.:</b> Troca o status da consulta
	 * 
	 * @param id 			- Identificação da consulta
	 * @param novoStatus	- Status para o qual deve ser mudada a consulta
	 * @throws Exception
	 */
	private void trocaStatus(int id, String novoStatus) throws Exception 
	{
		String sqlAtualiza = 	"UPDATE tbl_rel_solicitacoes_relatorio " +
								"SET idt_status = ? " +
								"WHERE id_solicitacao = ? ";
		
		Object parametros[] = {novoStatus,new Integer(id)};
		
		// Muda o status da consulta na tabela
		conexaoPrep.executaPreparedUpdate(sqlAtualiza, parametros, super.getIdLog());
	}
	
	/**
	 * <P><b>Metodo....:</b> geraArquivo
	 * <P><b>Descricao.:</b> Gera um arquivo em um diretório da máquina de processos batch
	 * 
	 * @param arquivo			- Identificação da consulta
	 * @param resultadoConsulta	- Status para o qual deve ser mudada a consulta
	 */
	private void geraArquivo(String nomeArquivo, ResultSet resultadoConsulta)
	{
		try
		{
			MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia();
			
			ProcessadorResultadoCSV  processador = new ProcessadorResultadoCSV();
			// Gera o arquivo delimitado por ;
			File arquivoTemporario = 	processador.getResultadoComoArquivo(resultadoConsulta, 
										map.getMapValorConfiguracaoGPP("DIRETORIO_RELATORIOS") + 
										nomeArquivo + 
										Definicoes.EXTENSAO_CSV, ";");
			// Array com a lista de arquivos a compactar
			File saida [] = {arquivoTemporario};
			// Gera o arquivo zipado
			CompactadorArquivos.getArquivoCompactado(map.getMapValorConfiguracaoGPP("DIRETORIO_RELATORIOS") +
													 nomeArquivo +
													 Definicoes.EXTENSAO_ZIP, saida);
			// Apaga o arquivo do diretório
			arquivoTemporario.delete();
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"geraArquivo","Arquivo ja existe ou sem acesso para gravacao: " + e);
		}
	}
	
	/**
	 * <P><b>Metodo....:</b> enviaMail
	 * <P><b>Descricao.:</b> Envia e-mail ao operador avisando da conclusão da consulta
	 * 
	 * @param operador		- Operador que realizou a solicitação
	 * @param endereco		- E-mail alternativo para envio da mensagem
	 * @param arquivo		- Nome do arquivo com o resultado da consulta
	 * @param idArquivo		- Identificação do arquivo com o resultado da consulta
	 * @throws Exception 
	 */
	private void enviaMail(String operador, String endereco, String arquivo, int idArquivo) throws Exception
	{
		MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
		
		EnviaMail enviar = new EnviaMail(map.getMapValorConfiguracaoGPP("EMAIL_VOUCHER_ADMIN"),map.getMapValorConfiguracaoGPP("GPP_SERVIDOR_SMTP"));
		enviar.setAssunto(map.getMapValorConfiguracaoGPP("ASSUNTO_RELATORIO_PRONTO"));
		enviar.setMensagem(	"<p>"+map.getMapValorConfiguracaoGPP("MENSAGEM_RELATORIO_PRONTO") +
							"</p>" +
							"<p>"+
							"<a href=\"" + 
							map.getMapValorConfiguracaoGPP("ENDERECO_RELATORIOS") + 
							"carregaArquivo.do?operador="+
							operador+
							"&id="+
							idArquivo + 
							"\">" + 
							arquivo +
							"</a>" + 
							"</p>"+
							"<p>"+
							"__________________"+
							"</p>"+
							"<p>"+
							"Relatorios Batch"+
							"</p>");
		
		enviar.setTipoMensagem(GPPEnviaMailDefinicoes.TIPO_TEXTO_HTML);
		
		if(endereco != null)
			enviar.addEnderecoDestino(endereco);
		
		enviar.enviaMail();
	}
	
	/**
	 * <P><b>Metodo....:</b> setDataExecucao
	 * <P><b>Descricao.:</b> Seta a data de execução
	 * 
	 * @param id	- Identificacao da solicitação
	 */
	private void setDataExecucao(int id)
	{
		try
		{
			String destino = 	"UPDATE tbl_rel_solicitacoes_relatorio " +
								"SET dat_execucao = SYSDATE " + 
								"WHERE id_solicitacao = ? ";
			
			Object parametro[] = {new Integer(id)};
			
			conexaoPrep.executaPreparedUpdate(destino, parametro, super.getIdLog());
		}
		catch(GPPInternalErrorException e)
		{
			super.log(Definicoes.WARN,"setDataExecucao","Erro ao atualizar: " + e);
		}
	}
}
