package com.brt.gpp.aplicacoes.aprovisionar.mms;

import java.sql.ResultSet;
import java.sql.Timestamp;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

public class ImportacaoAssinantesMMSConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
    private ProcessoBatchProdutor produtor;
    private PREPConexao conexaoPrep;
    /**
     * @param aLogId
     */
    public ImportacaoAssinantesMMSConsumidor()
    {
        super(GerentePoolLog.getInstancia(ImportacaoAssinantesMMSConsumidor.class).getIdProcesso(Definicoes.CL_IMPORTACAO_ASSINANTES_MMS), 
                Definicoes.CL_IMPORTACAO_ASSINANTES_MMS);
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
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#execute(java.lang.Object)
     */
    public void execute(Object obj) throws Exception
    {
        ImportacaoAssinantesMMSVO vo = (ImportacaoAssinantesMMSVO) obj;
        String msisdn = vo.getMsisdn();
		Timestamp novaDataInclusao = vo.getDataInclusao();
		String novoModelo = vo.getModelo();
		String tipOperacao = null;
		String statusRegistro = null;
		super.log(Definicoes.DEBUG, "Consumidor.execute", "Inicio do processamento do assinante: " + msisdn);
		if(Definicoes.MMS_MODELO_DESCONHECIDO.equals(novoModelo)) // aparelho novo desconhecido
		{
			statusRegistro = Definicoes.MMS_MODELO_DESCONHECIDO;
		}
		else //aparelho novo reconhecido
		{
			boolean novoAparelhoMMS = Definicoes.MMS_MODELO_COMPATIVEL.equals(novoModelo);
			String sql = 	"SELECT decode(his.co_modelo, 0, ?, " +
				  	" 		decode((SELECT count(1) " +
				  	"					FROM hsid.hsid_modelo_capacidade mod " +
				  	" 					WHERE mod.co_modelo = his.co_modelo " +
					" 					AND mod.co_capacidade = ? " +
					" 					AND upper(mod.ds_caracteristica) LIKE 'SIM%'), 0, ?,?)) AS ind_mms " +
					" 	FROM hsid.hsid_cliente_historico his " +
					" 	WHERE his.dt_inclusao < ? " +
					" 	AND his.nu_msisdn = ? " + 
					" 	ORDER BY his.dt_inclusao desc";
			Object[] params1 = {Definicoes.MMS_MODELO_DESCONHECIDO, Definicoes.MMS_CODIGO_CAPACIDADE, Definicoes.MMS_MODELO_NAO_COMPATIVEL, Definicoes.MMS_MODELO_COMPATIVEL, novaDataInclusao,  msisdn};
			ResultSet rsPenultimoRegistro = conexaoPrep.executaPreparedQuery(sql, params1, super.getIdLog());
			
			if(rsPenultimoRegistro.next()) //o assinante jah havia mudado de aparelho, ou seja, existe um registro na tabela
			{
				String antigoModelo = rsPenultimoRegistro.getString("ind_mms");
				boolean antigoAparelhoMMS = antigoModelo.equals(Definicoes.MMS_MODELO_COMPATIVEL);
				if(novoAparelhoMMS)
				{
					if(antigoAparelhoMMS) // os 2 aparelhos suportam MMS
					{
						// grava na tabela como concluido
						statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
					}
					else //o aparelho antigo nao suporta MMS
					{
						tipOperacao = Definicoes.XML_OS_DESBLOQUEAR;
						statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
					}
				}
				else // o novo aparelho nao suporta MMS
				{
					if(antigoAparelhoMMS)
					{
						tipOperacao = Definicoes.XML_OS_BLOQUEAR;
						statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
					}
					else // nenhum dos aparelhos suporta MMS
					{
						statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
					}
				}
			}
			else // eh o primeiro registro do assinante na tabela
			{
				if(novoAparelhoMMS)
				{
					tipOperacao = Definicoes.XML_OS_DESBLOQUEAR;
					statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
				}
				else
				{
					statusRegistro = Definicoes.MMS_REGISTRO_CONCLUIDO;
				}
			}
			rsPenultimoRegistro.close();
		}
		// dataProcessamento = GPPData.DateToString(novaDataInclusao); ate agora eu nao entendi isso				
		inserirAssinante(conexaoPrep, msisdn, tipOperacao, statusRegistro, novaDataInclusao);
		super.log(Definicoes.DEBUG, "Consumidor.execute", "Fim do processamento do assinante: " + msisdn);
    }

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
     */
    public void finish()
    {
    }
    
    /**
	 * Metodo...: inserirAssinante
	 * Descricao: insere um registro na tabela TBL_APR_MMS
	 * @param	conexao			objeto do tipo PREPConexao que representa uma conexao com o banco
	 * 			msisdn			MSISDN do assinante
	 * 			operacao		comando que sera enviado a plataforma de MMS (Adicionar/Retirar/null)
	 * 			statusRegistro	status do registro (C = Registro Concluido / D = MSISDN Desconhecido/ E = Registro Enviado)
	 * 			dataInclusao	data na qual o TSD subiu
	 * @return	int				indica o sucesso ou erro da insercao
	 * @throws GPPInternalErrorException
	 */	
	private int inserirAssinante(PREPConexao conexao, String msisdn, String operacao, String statusRegistro, Timestamp dataInclusao) throws GPPInternalErrorException
	{
		int resultado = 0;
		String sql = null;
		try
		{
			sql = "INSERT INTO tbl_apr_mms (idt_msisdn, tip_operacao, idt_status_registro, dat_inclusao_hsid, dat_processamento) " +
					" VALUES (?, ?, ?, ?, SYSDATE) ";
			Object[] params = {msisdn, operacao, statusRegistro, dataInclusao};
			resultado = conexao.executaPreparedUpdate(sql, params, super.getIdLog());					
		}
		catch(Exception sqlE)
		{
			super.log(Definicoes.WARN, "Consumidor.inserirAssinante", "Erro SQL: "+ sqlE);
			throw new GPPInternalErrorException("Erro GPP: "+ sqlE);
		}
		return(resultado);
	}
    
}