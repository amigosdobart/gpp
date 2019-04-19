package com.brt.gpp.aplicacoes.importacaoAssinantes.importacaoPospago;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
*
* Inclui ou retira os registros da tabela de assinantes
* pós-pagos
* 
* @version	1.0
* @author	Marcelo Alves Araujo
* @since	22/11/2006
*
*/
public class ImportaPospagoConsumidor extends Aplicacoes implements ProcessoBatchConsumidor 
{
	private PREPConexao			conexaoBanco;
	private ProcessoBatchProdutor produtor;
	private static final String SQLINSERE    = "INSERT INTO tbl_apr_assinante_pospago               "
										    + "(idt_msisdn, dat_inclusao, idt_promocao, ind_ativo)  "
										    + "VALUES                                               "
										    + "(?,?,?,?)                                            ";
	
	private static final String SQLATUALIZA	= "UPDATE tbl_apr_assinante_pospago      "
			                                + "SET ind_ativo = ?, idt_promocao = ?   " 
										    + "WHERE idt_msisdn = ?                  "; 
	
	private static final String SQLUPD	= "UPDATE tbl_int_assinante_pospago " 
										+ "SET idt_status_processamento = ? " 
										+ "WHERE idt_msisdn = ?             " 
										+ "AND idt_status_processamento = ? " 
										+ "AND idt_processamento = ?";
	
	/**
	 * Construtor da classe
	 */
	public ImportaPospagoConsumidor() 
	{
		super(GerentePoolLog.getInstancia(ImportaPospagoConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_POSPAGO), Definicoes.CL_IMPORTACAO_POSPAGO);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup()
	 */
	public void startup()
	{
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor)
	{
		this.produtor = produtor;
		this.conexaoBanco = produtor.getConexao();
		startup();
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor)
	{
		startup((ProcessoBatchProdutor) produtor);
	}

	/**
	 * Insere ou retira o pós-pago da tabela de assinantes
	 * @param Object - Objeto contendo os dados gerados pelo produtor
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#execute(Object)
	 */
	public void execute(Object obj) throws GPPInternalErrorException
	{
		ImportaPospagoVO importa = (ImportaPospagoVO) obj;
		
		try
		{
			if(Definicoes.INCLUSAO_POSPAGO.equals(importa.getAcao()))
			{
				Object[] paramAtualiza =	{new Integer(1), 
											 Integer.toString(importa.getPromocao()),
											 importa.getMsisdn()};
				if(conexaoBanco.executaPreparedUpdate(SQLATUALIZA, paramAtualiza, super.getIdLog()) == 0)
				{
					Object[] paramInsert =	{importa.getMsisdn(), 
											 importa.getDataInclusao(), 
											 Integer.toString(importa.getPromocao()), 
											 new Integer(1)};
					conexaoBanco.executaPreparedUpdate(SQLINSERE, paramInsert, super.getIdLog());
				}
			}
			else
			{
				if(Definicoes.EXCLUSAO_POSPAGO.equals(importa.getAcao()))
				{
					Object[] paramDelete =	{new Integer(0), 
											 Integer.toString(importa.getPromocao()),
										     importa.getMsisdn()};
					conexaoBanco.executaPreparedUpdate(SQLATUALIZA, paramDelete, super.getIdLog());
				}
			}	
			
			Object[] paramUpdate =	{ Definicoes.IDT_PROC_OK, importa.getMsisdn()
					, Definicoes.IND_LINHA_NAO_PROCESSADA, new Long(importa.getIdtProcessamento())};
			conexaoBanco.executaPreparedUpdate(SQLUPD, paramUpdate, super.getIdLog());
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Erro em operação no banco para o assinante "+importa.getMsisdn()+": "+e);
			produtor.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			
			Object[] paramUpdate =	{ Definicoes.IND_LINHA_PROCESSADA_ERRO, importa.getMsisdn()
					, Definicoes.IND_LINHA_NAO_PROCESSADA, new Long(importa.getIdtProcessamento())};
			conexaoBanco.executaPreparedUpdate(SQLUPD, paramUpdate, super.getIdLog());
		} 
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
		super.log(Definicoes.INFO, "Consumidor.finish", "Fim dos consumidores");		
	}	
}
