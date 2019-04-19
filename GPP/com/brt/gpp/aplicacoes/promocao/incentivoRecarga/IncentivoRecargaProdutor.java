package com.brt.gpp.aplicacoes.promocao.incentivoRecarga;

import java.sql.Date;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.promocao.entidade.AssinanteIncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.BonusIncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.IncentivoRecarga;
import com.brt.gpp.aplicacoes.promocao.entidade.MensagemBonusIncentivoRecarga;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
 * 
 * @author João Paulo Galvagni
 * @since  19/11/2007
 */
public class IncentivoRecargaProdutor extends Aplicacoes
			 implements ProcessoBatchProdutor
{
	private String			status 	 			 = null;
	private PREPConexao 	conexaoPrep 		 = null;
	private ResultSet		rsElegiveis			 = null;
	private String 			dataInicio	 		 = null;
	private int				numRegistros		 = 0;
	Map						mapIncentivoRecarga  = null;
	private String			dataReferenciaHoje	 = null;
	private String			dataReferenciaAmanha = null;
	private String			dataReferenciaOntem	 = null;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	private final String SQL_CONSULTA_ELEGIVEIS_CAMPANHA = "SELECT idt_msisdn                              AS idt_msisdn, 			" +
														   "       dat_origem							   AS dat_origem, 			" +
														   "       vlr_pago								   AS vlr_recarga, 			" +
														   "       idt_incentivo_recarga                   AS idt_incentivo_recarga," +
														   "       dia_execucao                   		   AS dia_execucao			" +
														   "  FROM (SELECT rec.idt_msisdn                  AS idt_msisdn 			" +
														   "              ,rec.dat_origem                  AS dat_origem 			" +
														   "              ,rec.vlr_pago                    AS vlr_pago   			" +
														   "              ,RANK() OVER(partition by rec.idt_msisdn ORDER BY rec.dat_origem) AS ordem " +
														   "              ,ir.idt_incentivo_recarga        AS idt_incentivo_recarga " +
														   "              ,air.dia_execucao        		   AS dia_execucao			" +
														   "         FROM tbl_rec_recargas rec 										" +
														   "             ,tbl_ger_plano_preco pl 									" +
														   "             ,tbl_cam_ass_incentivo_recarga air 						" +
														   "             ,tbl_cam_incentivo_recarga ir 								" +
														   "        WHERE rec.IDT_PLANO_PRECO  = pl.IDT_PLANO_PRECO 				" +
														   "          AND rec.ID_TIPO_RECARGA  = ? 									" +
														   "          AND rec.DAT_ORIGEM      >= to_date(?,'dd/mm/yyyy')			" + // ONTEM
														   "          AND rec.DAT_ORIGEM       < to_date(?,'dd/mm/yyyy')			" + // AMANHA
														   "          AND pl.IDT_CATEGORIA     = 0 									" +
														   "          AND rec.IDT_MSISDN       = air.IDT_MSISDN 					" +
														   "		  AND rec.VLR_PAGO >= (SELECT MIN(b.vlr_recarga)				" +
														   "                                 FROM tbl_cam_bon_incentivo_recarga b	" +
														   "                                WHERE b.IDT_INCENTIVO_RECARGA = ir.idt_incentivo_recarga ) " +
														   "		  AND air.DAT_RETIRADA IS NULL									" +
														   "          AND air.IDT_INCENTIVO_RECARGA = ir.IDT_INCENTIVO_RECARGA 		" +
														   "          AND ir.DAT_INICIO_VIGENCIA < to_date(?,'dd/mm/yyyy') 			" + // HOJE
														   "          AND (ir.DAT_FIM_VIGENCIA   >= to_date(?,'dd/mm/yyyy')			" + // AMANHA
														   "			  OR 														" +
														   "				ir.DAT_FIM_VIGENCIA IS NULL) 							" +
														   "          ) 															" +
														   " WHERE ordem = 1														" ;
	
	private final String SQL_CONSULTA_INCENTIVOS = "SELECT A.IDT_INCENTIVO_RECARGA 								" +
												   "      ,A.NOM_INCENTIVO_RECARGA  							" +
												   "      ,A.DAT_INICIO_VIGENCIA  								" +
												   "      ,A.DAT_FIM_VIGENCIA  									" +
												   "      ,A.ID_CANAL 	 										" +
												   "      ,A.ID_ORIGEM  										" +
												   "      ,B.IDT_BONUS_INCENTIVO  								" +
												   "      ,B.VLR_RECARGA 	 									" +
												   "      ,B.VLR_BONUS  										" +
												   "      ,B.IDT_TIPO_SALDO  									" +
												   "      ,B.DAT_INICIO_BONIFICACAO  							" +
												   "      ,C.DES_MENSAGEM  										" +
												   "      ,C.NUM_DIAS_ANTECEDE_BONUS  							" +
												   "  FROM TBL_CAM_INCENTIVO_RECARGA A 							" +
												   "      ,TBL_CAM_BON_INCENTIVO_RECARGA B 						" +
												   "      ,TBL_CAM_SMS_INCENTIVO_RECARGA C 						" +
												   " WHERE A.IDT_INCENTIVO_RECARGA = B.IDT_INCENTIVO_RECARGA 	" +
												   "   AND B.IDT_BONUS_INCENTIVO   = C.IDT_BONUS_INCENTIVO (+)  " +
												   "   AND A.DAT_INICIO_VIGENCIA <= to_date(?,'dd/mm/yyyy')		" + // HOJE
												   "   AND (A.DAT_FIM_VIGENCIA >= to_date(?,'dd/mm/yyyy') 		" + // AMANHA
												   "		OR A.DAT_FIM_VIGENCIA IS NULL)						" ;
	
	/**
	 * Construtor da Classe
	 * 
	 * @param aLogId - Id para controle de log
	 */
	public IncentivoRecargaProdutor(long aLogId)
	{
		super(aLogId, Definicoes.CL_INCENTIVO_RECARGA_PRODUTOR);
	}
	
	/**
	 * Descricao.: Inicia o processamento das acoes referentes
	 * 			   incentivo de recargas
	 * 
	 * @param params[] - Nao ha parametros
	 */
	public void startup(String[] params) throws GPPInternalErrorException, Exception
	{
		dataInicio 	 = getDataProcessamento();
		numRegistros = 0;
		setStatusProcesso(Definicoes.PROCESSO_SUCESSO);
		
		loadDatasReferencia(params[0] != null ? params[0] : null);
		
		try
		{
			// Seleciona a conexao com o Banco de Dados
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			loadIncentivosRecargas();
			
			if (mapIncentivoRecarga.size() > 0)
			{
				Object[] param = {Definicoes.TIPO_RECARGA
								 ,dataReferenciaOntem
								 ,dataReferenciaAmanha
								 ,dataReferenciaHoje
								 ,dataReferenciaAmanha};
				
				rsElegiveis = conexaoPrep.executaPreparedQuery(SQL_CONSULTA_ELEGIVEIS_CAMPANHA, param, getIdLog());
			}
		}
		catch (GPPInternalErrorException e)
		{
			super.log(Definicoes.ERRO, "Produtor.startup", "Excecao na consulta das recargas, erro:" + e);
			setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw e;
		}
		
		super.log(Definicoes.DEBUG, "startup", "Fim da execucao do processo.");
	}
	
	/**
	 * Retorna o dia anterior a hoje, caso a data seja nula, ou 
	 * a data que o usuario deseja executar
	 * 
	 * @param  dataInformada - Data informada pelo usuario ou nao
	 * @return data			 - 
	 */
	private void loadDatasReferencia(String dataInformada) throws ParseException
	{
		Calendar cal = Calendar.getInstance();
		
		if (dataInformada != null && !"".equals(dataInformada))
			cal.setTime(sdf.parse(dataInformada));
		
		this.dataReferenciaHoje = sdf.format(cal.getTime()); 
		cal.add(Calendar.DAY_OF_MONTH, -1);
		this.dataReferenciaOntem = sdf.format(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, 2);
		this.dataReferenciaAmanha = sdf.format(cal.getTime());
	}
	
	/**
	 * Descricao.: Retorna o proximo registro a ser processado
	 * 			   pelo Consumidor
	 * 
	 * @return Object - Objeto populado com as informacoes de Banco
	 */
	public Object next() throws Exception
	{
		AssinanteIncentivoRecarga assinanteIncentivo = null;
		
		if (mapIncentivoRecarga.size() > 0 && rsElegiveis.next())
		{
			assinanteIncentivo = new AssinanteIncentivoRecarga();
			assinanteIncentivo.setMsisdn(rsElegiveis.getString("idt_msisdn"));
			assinanteIncentivo.setDataRecarga(rsElegiveis.getDate("dat_origem"));
			assinanteIncentivo.setValorRecarga(rsElegiveis.getDouble("vlr_recarga"));
			assinanteIncentivo.setDiaExecucao(rsElegiveis.getInt("dia_execucao"));
			int idIncentivoRecarga = rsElegiveis.getInt("idt_incentivo_recarga");
			assinanteIncentivo.setIncentivoRecarga(getIncentivoRecarga(idIncentivoRecarga, assinanteIncentivo.getValorRecarga()));
			
			numRegistros++;
		}
		
		return assinanteIncentivo;
	}
	
	/**
	 * Descricao.: Retorna a conexao do Banco de Dados do Produtor
	 * 
	 * @return conexaoPrep - Conexao com o Banco de Dados
	 */
	public PREPConexao getConexao()
	{
		return this.conexaoPrep;
	}
	
	/**
	 * Descricao.: Retorna a data de processamento atual
	 * 
	 * @return String - Data atual de processamento
	 */
	public String getDataProcessamento()
	{
		return new SimpleDateFormat(Definicoes.MASCARA_TIMESTAMP).format(Calendar.getInstance().getTime());
	}
	
	/**
	 * Descricao.: Retorna a descricao do Processo
	 * 
	 * @return String - Detalhe do processo
	 */
	public String getDescricaoProcesso()
	{
		return "Processo de bonificacao de Incentivo de Recargas. " +
		   	   "Qtde registros: " + numRegistros;
	}
	
	/**
	 * Descricao.: Retorna o ID do processo batch
	 * 
	 * @return idProcessoBatch - ID do processo batch
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_INCENTIVO_RECARGA;
	}
	
	/**
	 * Descricao.: Retorna o status do processo
	 * 
	 * @return status - Status atual do processo
	 */
	public String getStatusProcesso()
	{
		return this.status;
	}
	
	/**
	 * Descricao.: Seta o status do processo
	 * 
	 * @param status - Novo status do processo
	 */
	public void setStatusProcesso(String status)
	{
		this.status = status;
	}
	
	/**
	 * Descricao.: Realiza as acoes apos o termino do 
	 * 			   processamento do produtor
	 * 
	 */
	public void finish() throws Exception
	{
		// Chama a funcao para gravar no historico o Processo em questao
		super.gravaHistoricoProcessos(getIdProcessoBatch(), this.dataInicio,
									  this.getDataProcessamento(), this.getStatusProcesso(),
									  this.getDescricaoProcesso(), dataInicio);
		
		this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		
		super.log(Definicoes.DEBUG, "finish", "Status de processamento: " + this.getStatusProcesso());
		super.log(Definicoes.DEBUG, "finish", "Descricao do processamento: " + this.getDescricaoProcesso());
	}
	
	/**
	 * Descricao.: Trata as excecoes geradas pelo Produtor
	 * 
	 */
	public void handleException()
	{
	}
	
	/**
	 * Carrega todos os Incentivos de Recarga vigentes
	 * para a memoria do produtor
	 * 
	 * @throws Exception
	 */
	private void loadIncentivosRecargas() throws Exception
	{
		mapIncentivoRecarga 	= new HashMap();
		ResultSet rsIncentivos 	= null;
		
		try
		{
			Object[] param = {dataReferenciaHoje
							 ,dataReferenciaAmanha};
			
			rsIncentivos = conexaoPrep.executaPreparedQuery(SQL_CONSULTA_INCENTIVOS, param, getIdLog());
			
			while (rsIncentivos.next())
			{
				int idIncentivoRecarga = rsIncentivos.getInt("IDT_INCENTIVO_RECARGA");
				IncentivoRecarga incentivoRecarga 	   = (IncentivoRecarga)mapIncentivoRecarga.get(new Integer(idIncentivoRecarga));
				BonusIncentivoRecarga bonusIncentivo   = new BonusIncentivoRecarga();
				MensagemBonusIncentivoRecarga mensagem = new MensagemBonusIncentivoRecarga();
				
				// Caso o incentivo nao exista, uma nova entidade eh criada 
				// para conter as informacoes do bonus
				if (incentivoRecarga == null)
				{
					incentivoRecarga = new IncentivoRecarga(idIncentivoRecarga);
					incentivoRecarga.setNomeIncentivoRecarga(rsIncentivos.getString("NOM_INCENTIVO_RECARGA"));
					incentivoRecarga.setDataInicioVigencia(rsIncentivos.getDate("DAT_INICIO_VIGENCIA"));
					incentivoRecarga.setIdCanal(rsIncentivos.getString("ID_CANAL"));
					incentivoRecarga.setIdOrigem(rsIncentivos.getString("ID_ORIGEM"));
				}
				
				// Preenchimento da entidade MensagemBonusIncentivoRecarga
				mensagem.setMensagem(rsIncentivos.getString("DES_MENSAGEM"));
				mensagem.setNumDiasAntecedeBonus(rsIncentivos.getInt("NUM_DIAS_ANTECEDE_BONUS"));
				
				// Seta os atributos da bonificacao do incentivo
				bonusIncentivo.setMensagemBonusIncentivo(mensagem);
				bonusIncentivo.setValorRecarga(rsIncentivos.getDouble("VLR_RECARGA"));
				bonusIncentivo.setValorConcessaoBonus(rsIncentivos.getDouble("VLR_BONUS"));
				bonusIncentivo.setTipoSaldo(MapTipoSaldo.getInstance().getTipoSaldo(rsIncentivos.getShort("IDT_TIPO_SALDO")));
				bonusIncentivo.setDataInicioConcessaoBonus(rsIncentivos.getString("DAT_INICIO_BONIFICACAO"));
				
				// Adiciona no incentivo o bonus preenchido anteriormente
				incentivoRecarga.addListaBonusIncentivo(bonusIncentivo);
				
				// Adiciona no mapeamento o incentivo
				mapIncentivoRecarga.put(new Integer(idIncentivoRecarga), incentivoRecarga);
			}
		}
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "loadIncentivosRecarga", "Erro na consulta dos incentivos: "+ e);
			throw e;
		}
	}
	
	/**
	 * Retorna a entidade referente ao Identificador da mesma
	 * 
	 * @param  idtIncentivoRecarga	- Identificador do Incentivo
	 * @param  valorRecarga			- Valor da recarga do assinante
	 * @return novoIncentivo		- Entidade de <code>IncentivoRecarga</code>
	 */
	private IncentivoRecarga getIncentivoRecarga(int idtIncentivoRecarga, double valorRecarga)
	{
		IncentivoRecarga novoIncentivo 	     = new IncentivoRecarga(idtIncentivoRecarga);
		IncentivoRecarga incentivoReferencia = (IncentivoRecarga)mapIncentivoRecarga.get(new Integer(idtIncentivoRecarga));
		
		novoIncentivo.setNomeIncentivoRecarga(incentivoReferencia.getNomeIncentivoRecarga());
		novoIncentivo.setDataInicioVigencia(incentivoReferencia.getDataInicioVigencia());
		novoIncentivo.setDataFinalVigencia(incentivoReferencia.getDataFinalVigencia());
		novoIncentivo.setIdCanal(incentivoReferencia.getIdCanal());
		novoIncentivo.setIdOrigem(incentivoReferencia.getIdOrigem());
		
		for (Iterator i = incentivoReferencia.getListaBonusIncentivo().iterator(); i.hasNext(); )
		{
			BonusIncentivoRecarga bonus = (BonusIncentivoRecarga)i.next();
			
			if (bonus.getValorRecarga() == valorRecarga)
				novoIncentivo.addListaBonusIncentivo(bonus);
		}
		
		return novoIncentivo;
	}
}