package com.brt.gpp.aplicacoes.exportacaoDW;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * Esta classe é responsável pela exportação de dados de contábeis para o DW
 *
 * @version:		1.0
 * @author: 		Marcelo Alves Araujo
 * @since:			02/03/2006
 */
public class EnvioDadosContabeisDWProdutor extends Aplicacoes implements	ProcessoBatchProdutor
{
	private int 		numRegistros = 0;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private PREPConexao	conexaoBanco = null;
	private String		dataMes = null;
	
	/**
	 * Contrutor
	 * @param long - Id de log
	 */
	public EnvioDadosContabeisDWProdutor(long logId)
	{
		super(logId, Definicoes.CL_ENVIO_DADOS_CONTABEIS_DW);
	}

	/**
	 * Retorna o id do processo batch
	 * @return int - Id do processo batch
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_ENVIO_DW_CONTABIL;
	}

	/**
	 * Retorna a descrição do status do processo
	 * @return String - Descrição do processo
	 */
	public String getDescricaoProcesso()
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return this.numRegistros + " Registros de Inserido na Tabela tbl_int_dw_contabil.";
		return "Erro: " + this.getStatusProcesso() + " na exportacao de dados para o DW.";
	}

	/**
	 * Retorna o status do processo batch
	 * @return String - Status do processo batch
	 */
	public String getStatusProcesso()
	{
		return this.statusProcesso;
	}

	/**
	 * Atribui status ao processo batch
	 * @param String - Status do processo batch
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}

	/**
	 * Retorna a data de processamento
	 * @return String - Data de processamento
	 */
	public String getDataProcessamento()
	{
		return null;
	}

	/**
	 * Retorna a conexão com o banco de dados
	 * @return PREPConexao - Conexão com o banco de dados
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoBanco;
	}
	
	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	private void parseParametros(String params[]) throws GPPInternalErrorException
	{
		// Verifica se foi passado algum parâmetro
		if (params == null || params.length == 0 || params[0] == null)
		{
		    super.log(Definicoes.ERRO, "parseParametros", "Parametro de data (MM/AAAA) obrigatorio para o processo.");
		    setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		
		// Verifica se a data está em um formato válido
		try
		{
			sdf.parse(params[0]);
			dataMes = params[0];
		}
		catch(ParseException pe)
		{
		    super.log(Definicoes.ERRO, "parseParametros", "Data invalida ou fora do formato (MM/AAAA).");
		    this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException("Data invalida ou fora do formato (MM/AAAA). Data: " + params[0]);
		}
	}

	/**
	 * Executa a inserção de dados na tabela de interface
	 * @param String[] - Parâmetro de data 
	 * @throws GPPInternalErrorException
	 */
	public void startup(String[] params) throws GPPInternalErrorException
	{
		super.log(Definicoes.INFO, "startup", "Inicio da exportacao de dados contabeis para o DW.");
		
		this.parseParametros(params);
		
		try
		{
			this.conexaoBanco = this.gerenteBancoDados.getConexaoPREP(logId);
			
			// Limpa a tabela de interface antes de inserir um novo período contábil
			conexaoBanco.executaPreparedUpdate("delete tbl_int_dw_contabil where idt_status_processamento <> 'N'", null, getIdLog());
							
			// Insere dados sobre índice de bonifica e código serviço
			String insert = "  INSERT INTO tbl_int_dw_contabil  " +
							"  SELECT   " +
							"   crs.id_canal AS IDT_CSP_CANAL,  " +
							"   crs.id_origem AS IDT_MODULACAO_ORIGEM,  " +
							"   crs.id_sistema_origem AS IDT_TIP_CHAMADA_SIS_ORIGEM,  " +
							"   crs.idt_tipo_servico AS idt_tipo_servico,  " +
							"   crs.idt_plano_preco AS idt_plano_preco,  " +
							"   cib.idt_codigo_nacional AS idt_codigo_nacional,  " +
							"   cib.idt_periodo_contabil AS idt_periodo_contabil,  " +
							"   cib.idt_tipo_saldo AS idt_tipo_saldo,  " +
							"   crs.idt_codigo_servico_sfa AS idt_codigo_servico_sfa,  " +
							"   'E' AS idt_unidade_medida,  " +
							"   decode(instr(crs.idt_tipo_servico,'CB'),  " +
							"           0, 1-cib.vlr_indice_bonificacao,  " +
							"           cib.vlr_indice_bonificacao) AS idt_indice_bonificacao,  " +
							"   'N' AS IDT_STATUS_PROCESSAMENTO,  " +
							"   null AS DAT_PROCESSAMENTO_ETI  " +
							"  FROM   " +
							"   tbl_con_recarga_servico crs,  " +
							"   tbl_con_indice_bonificacao cib,  " +
							"	tbl_ger_plano_preco gpp " +
							"  WHERE   " +
							"   decode(gpp.idt_categoria,?,?,?) = cib.idt_pre_hibrido  " + 	// Parâmetros 1, 2, 3
							"	AND gpp.idt_plano_preco = crs.idt_plano_preco " +
							"   AND crs.idt_tipo_servico <> ?  " +							// Parâmetro 4
							"   AND cib.idt_periodo_contabil = ?  " +						// Parâmetro 5
							"  UNION all   " +
							"  SELECT   " +
							"   ccs.num_csp AS IDT_CSP_CANAL,  " +
							"   ccs.idt_modulacao AS IDT_MODULACAO_ORIGEM,  " +
							"   ccs.tip_chamada AS IDT_TIP_CHAMADA_SIS_ORIGEM,  " +
							"   ccs.idt_tipo_servico AS idt_tipo_servico,  " +
							"   ccs.idt_plano_preco AS idt_plano_preco,  " +
							"   cib.idt_codigo_nacional AS idt_codigo_nacional,  " +
							"   cib.idt_periodo_contabil AS idt_periodo_contabil,  " +
							"   cib.idt_tipo_saldo AS idt_tipo_saldo,  " +
							"   ccs.idt_codigo_servico_sfa AS idt_codigo_servico_sfa,  " +
							"   decode(ccs.idt_consumo_dados,   " +
							"           'S','E',  " +
							"           'N',DECODE(INSTR(CCS.tip_chamada,'GPRS'),  " +
							"                       0,'S',  " +
							"                       'K'  " +
							"                     )    " +
							"         ) AS idt_unidade_medida,  " +
							"   decode(instr(ccs.idt_tipo_servico,'CB'),  " +
							"           0, 1-cib.vlr_indice_bonificacao,  " +
							"           cib.vlr_indice_bonificacao) AS idt_indice_bonificacao,  " +
							"   'N' AS IDT_STATUS_PROCESSAMENTO,  " +
							"   null AS DAT_PROCESSAMENTO_ETI  " +
							"  FROM   " +
							"   tbl_con_cdr_servico ccs,  " +
							"   tbl_con_indice_bonificacao cib," +
							"	tbl_ger_plano_preco gpp  " +
							"  WHERE   " +
							"   decode(gpp.idt_categoria,?,?,?) = cib.idt_pre_hibrido  " +	// Parâmetros 6, 7, 8
							"	AND gpp.idt_plano_preco = ccs.idt_plano_preco " +
							"   AND ccs.idt_tipo_servico <> ?  " +							// Parâmetro 9
							"   AND cib.idt_periodo_contabil = ?  ";						// Parâmetro 10
			
			Object[] parametros = {	new Integer(Definicoes.CATEGORIA_HIBRIDO),Definicoes.COD_ASSINANTE_HIBRIDO, Definicoes.COD_ASSINANTE_PREPAGO,
									Definicoes.TIPO_SERVICO_CONSUMO_BONUS_GSM, this.dataMes,
									new Integer(Definicoes.CATEGORIA_HIBRIDO),Definicoes.COD_ASSINANTE_HIBRIDO, Definicoes.COD_ASSINANTE_PREPAGO,
									Definicoes.TIPO_SERVICO_CONSUMO_BONUS_GSM, this.dataMes};
			
			this.numRegistros = this.conexaoBanco.executaPreparedUpdate(insert, parametros, this.getIdLog());
			
			super.log(Definicoes.INFO, "startup", "Exportacao de dados contabeis para o DW. Qtd registros: " + this.numRegistros);
		}
		catch(GPPInternalErrorException e)
		{
			this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			super.log(Definicoes.ERRO, "startup", "Erro ao executar operacoes no banco.");
			throw new GPPInternalErrorException("Erro ao executar operacoes no banco.");
		}		
	}

	/**
	 * Método não implementado devido à ausência de consumidor
	 * @return Object - null
	 */
	public Object next()
	{
		return null;
	}

	/**
	 * Finaliza a conexão com o banco de dados.
	 */	
	public void finish()
	{
		// Libera conexão com o banco de dados
		this.gerenteBancoDados.liberaConexaoPREP(conexaoBanco,super.getIdLog());
	}
	
	/**
	 * Não há implementação de tratamento de exceção.
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException(){}
}
