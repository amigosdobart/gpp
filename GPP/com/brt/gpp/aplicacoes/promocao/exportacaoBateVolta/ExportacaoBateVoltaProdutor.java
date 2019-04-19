package com.brt.gpp.aplicacoes.promocao.exportacaoBateVolta;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.entidade.ArquivoBateVolta;
import com.brt.gpp.aplicacoes.promocao.entidade.TotalizacaoPulaPula;
import com.brt.gpp.aplicacoes.promocao.persistencia.Consulta;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * Classe respons�vel pela sele��o de todos os registros
 * da totaliza��o pula-pula dos planos conta.
 * A partir dessez registros ser�o gerados arquivos contendo
 * a quantidade de minutos acumulados pelo cliente no per�odo.
 *
 * @author	Marcelo Alves Araujo
 * @since	19/09/2006
 *
 */
public class ExportacaoBateVoltaProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Conex�o com o banco de dados
	private PREPConexao		conexaoBanco;

	// M�s de refer�ncia para consulta dos registros bate-volta
	private String			mesReferencia;

	// Status do processo batch
	private String			statusProcesso;

	// Conjunto de dados resultante da consulta
	private ResultSet		resultTotalizacao;

	// Diret�rio em que ser� gravado o arquivo
	private String			diretorioArquivo;

	// Nome do arquivo
	private String			nomeArquivo;

	// Arquivo em que ser�o gravados os registros
	private ArquivoEscrita	arquivoBateVolta;

	// Contador do n�mero de linhas inseridas
	private long			contador;

    // Consulta
    private Consulta        consulta;

	/**
	 * Construtor
	 * @param aLogId
	 */
	public ExportacaoBateVoltaProdutor(long aLogId)
    {
	    super(aLogId, Definicoes.CL_EXPORTACAO_BATE_VOLTA);
	}


    /**
	 * Este metodo realiza a verificacao da data
	 * @param String[] - Data de reger�ncia
	 * @throws GPPInternalErrorException
	 */
	private void validarData(String[] params) throws GPPInternalErrorException
	{
		if (params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");

		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(Definicoes.MASCARA_DAT_MES);
			sdf.parse(params[0]);
		}
		catch(ParseException pe)
		{
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			super.log(Definicoes.ERRO, super.nomeClasse, "Data invalida ou nao esta no formato (aaaamm). Erro: "+pe);
			throw new GPPInternalErrorException("Data invalida ou nao esta no formato (aaaamm). Valor:"+params[0]);
		}
	}

	/**
	 * Efetua a consulta das totaliza��es de clientes do plano conta
	 * @param String[] - M�s de refer�ncia para consulta
     * @throws Exception
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception
	{
		contador = 0;
		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

		// Valida se a data est� no formato yyyymm
		validarData(params);
		mesReferencia = params[0];

		// Abertura do arquivo para inser��o dos registros
		ArquivoConfiguracaoGPP arqConfiguracao = ArquivoConfiguracaoGPP.getInstance();
	    diretorioArquivo = arqConfiguracao.getDirBateVolta();

	    MapConfiguracaoGPP configuracaoGPP = MapConfiguracaoGPP.getInstancia();
	    nomeArquivo = configuracaoGPP.getMapValorConfiguracaoGPP("PREFIXO_ARQUIVO_BATE_VOLTA") + mesReferencia;

	    arquivoBateVolta = new ArquivoEscrita(diretorioArquivo + nomeArquivo);

	    // Consulta da totaliza��o de registros para o Bate Volta
		consulta = new Consulta(super.getIdLog());
		conexaoBanco = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());

		resultTotalizacao = consulta.getTotalizacoesBateVolta(mesReferencia, conexaoBanco);
	}

	/**
     * @throws SQLException
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
     */
    public Object next() throws Exception
	{
    	TotalizacaoPulaPula result = null;

    	if(resultTotalizacao.next())
            result = consulta.getTotalizacao(resultTotalizacao);

    	return result;
	}

    /**
     * @throws SQLException
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
    public void finish() throws Exception
	{
    	if(resultTotalizacao != null)
    		resultTotalizacao.close();

		super.gerenteBancoDados.liberaConexaoPREP(conexaoBanco, super.getIdLog());

		if(arquivoBateVolta != null)
		{
			arquivoBateVolta.fechar();

	        // Pega o arquivo rec�m-criado para identificar seu tamanho
	        File arquivoVerificacao = new File(diretorioArquivo + nomeArquivo);
	        long tamanho = arquivoVerificacao.length();

	        // Cria o arquivo de controle colocar dados e fechar
	        ArquivoBateVolta bateVolta = new ArquivoBateVolta();
	        ArquivoEscrita arquivoControle = new ArquivoEscrita(diretorioArquivo + nomeArquivo + Definicoes.EXTENSAO_CTRL);
	        arquivoControle.escrever(bateVolta.toString(nomeArquivo, tamanho));
	        arquivoControle.escrever("\nEOF\n");
	        arquivoControle.fechar();
		}
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
	{
		return conexaoBanco;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
     */
    public String getDataProcessamento()
	{
    	return null;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
     */
    public String getDescricaoProcesso()
	{
    	if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
            return "Arquivo gerado com sucesso. Mes: " + mesReferencia;

        return "Erro composicao do arquivo";
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
     */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_EXPORTACAO_BATE_VOLTA;
	}

	/**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
     */
    public String getStatusProcesso()
	{
		return statusProcesso;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
     */
    public void setStatusProcesso(String status)
	{
		statusProcesso = status;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
	{
    	// Tratamento de Exce��es n�o Implementado
	}

    /**
     * Pega o m�s de refer�ncia da consulta
     * @return mesReferencia
     */
	public ArquivoEscrita getArquivoEscrita()
    {
    	return arquivoBateVolta;
    }

	/**
     * Escreve a linha do registro no arquivo
     * @return bateVolta	Entidade com dados a inserir no registro
     */
	public synchronized void escreveLinha(ArquivoBateVolta bateVolta) throws IOException
	{
		bateVolta.setContador(++contador);

		// Atribui valores a todos as vari�veis cujo valor depende de
		//msisdn, data atual, data de refer�ncia e dura��o das chamadas
		bateVolta.setParametros();

		arquivoBateVolta.escrever(bateVolta.toString() + "\n");
	}
}
