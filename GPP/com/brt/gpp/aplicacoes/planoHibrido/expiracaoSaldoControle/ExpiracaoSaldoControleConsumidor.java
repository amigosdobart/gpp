package com.brt.gpp.aplicacoes.planoHibrido.expiracaoSaldoControle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.recarregar.Ajustar;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
*
* Expira os saldos dos clientes do plano controle 
* 
* @version	1.0
* @author	Marcelo Alves Araujo
* @since	08/06/2006
*
*/
public class ExpiracaoSaldoControleConsumidor extends Aplicacoes implements ProcessoBatchConsumidor 
{
	private PREPConexao			conexaoBanco;
	private Ajustar				ajustar;
	
	/**
	 * Construtor da classe
	 */
	public ExpiracaoSaldoControleConsumidor() 
	{
		super(GerentePoolLog.getInstancia(ExpiracaoSaldoControleConsumidor.class).getIdProcesso(Definicoes.CL_EXPIRACAO_SALDO_CONTROLE), Definicoes.CL_EXPIRACAO_SALDO_CONTROLE);
	}
	
	/**
	 * @throws GPPInternalErrorException 
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup()
	 */
	public void startup() throws GPPInternalErrorException
	{
		ajustar = new Ajustar(super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor#startup(com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor)
	 */
	public void startup(ProcessoBatchProdutor produtor) throws GPPInternalErrorException
	{
		conexaoBanco = produtor.getConexao();
		startup();
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
	 */
	public void startup(Produtor produtor) throws GPPInternalErrorException
	{
		startup((ProcessoBatchProdutor) produtor);
	}
	
	/**
	 * Realiza o calculo do valor a ser expirado 
	 * 
	 * @param Object - Objeto contendo os dados gerados pelo produtor
	 */
	public void execute(Object obj)
	{
		ExpiracaoSaldoControleVO vo = (ExpiracaoSaldoControleVO) obj;
		double saldoExpiravel = 0.0;
		
		try
		{
			// Diferença entre o saldo atual do assinante e o saldo de recargas não expiráveis
			if(vo.getTipoSaldoExpirado().equals(Definicoes.EXPIRA_SALDO_PRINCIPAL))
				saldoExpiravel = calcularSaldoPrincipalExpiravel(vo);
			else if(vo.getTipoSaldoExpirado().equals(Definicoes.EXPIRA_SALDO_BONUS))
				// Diferença entre o saldo de bônus e o saldo ativo de bônus
				saldoExpiravel = calcularSaldoBonusExpiravel(vo);
			else if(vo.getTipoSaldoExpirado().equals(Definicoes.EXPIRA_SALDO_PERIODICO))
				// Diferenca entre o saldo periodico (Off-Net) e o saldo ativo de bonus
				saldoExpiravel = calcularSaldoPeriodicoExpiravel(vo);
			
			// Expira o saldo do assinante
			debitarSaldoExpirado(vo, saldoExpiravel);
		}
		catch(SQLException e)
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Erro ao consultar saldo a expirar do assinante ("+vo.getMsisdn()+"): "+e);
		}
		catch (GPPTecnomenException e) 
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Erro ao debitar saldo do assinante ("+vo.getMsisdn()+"). Erro: "+e);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, super.nomeClasse, "Erro generico: "+e);
		}
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish() 
	{
		super.log(Definicoes.INFO, "Consumidor.finish", "Fim dos consumidores");		
	}
	
	/**
	 * Calcula o saldo que deve ser retirado do assinante
	 * 
	 * @param  vo			- Instancia de <code>ExpiracaoSaldoControleVO</code>
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 * @throws GPPInternalErrorException 
	 */
	private double calcularSaldoPrincipalExpiravel(ExpiracaoSaldoControleVO vo) throws SQLException, GPPInternalErrorException
	{
		double saldoExpiravel = 0.0;
		ResultSet rs = null;
		
		try
		{
			String sql 	= "SELECT SUM (ABS(VLR_CREDITO_PRINCIPAL)) AS RECARGAS      " 
						+ "  FROM TBL_REC_RECARGAS                                  " 
						+ " WHERE DAT_ORIGEM  > TO_DATE(?, 'dd/mm/yyyy hh24:mi:ss') " 
						+ "   AND DAT_ORIGEM <= SYSDATE                             " 
						+ "   AND IDT_MSISDN  = ?                                   " ;
			
			Object[] param = {vo.getDataRecarga(),vo.getMsisdn()};
			
			rs = conexaoBanco.executaPreparedQuery(sql, param, super.getIdLog());
			
			if(rs.next())
				if(!fezRecarga(vo.getMsisdn()) && !vo.getTipoTransacao().equals(Definicoes.RECARGA_FRANQUIA_BONUS))
					saldoExpiravel = vo.getSaldoFinal() - rs.getDouble("RECARGAS");
				else
					saldoExpiravel = vo.getSaldoFinal() - (vo.getSaldoInicial() + rs.getDouble("RECARGAS"));
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
		
		return saldoExpiravel;
	}
	
	/**
	 * Calcula o saldo que deve ser retirado do assinante
	 * 
	 * @param  vo			- Instancia de <code>ExpiracaoSaldoControleVO</code>
	 * @return double		- Saldo total a retirar do assinante
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	private double calcularSaldoBonusExpiravel(ExpiracaoSaldoControleVO vo) throws GPPInternalErrorException, SQLException
	{
		double saldoExpiravel = 0.0;
		ResultSet rs = null;
		
		try
		{
			String sql 	= "SELECT SUM(ABS(VLR_CREDITO_BONUS)) AS BONUS              "
						+ "  FROM TBL_REC_RECARGAS                                  "
						+ " WHERE DAT_ORIGEM >  TO_DATE(?, 'dd/mm/yyyy hh24:mi:ss') " 
						+ "   AND DAT_ORIGEM <= SYSDATE                             "
						+ "   AND IDT_MSISDN  = ?                                   " ;
			
			Object[] param = {vo.getDataRecarga(),vo.getMsisdn()};
			
			rs = conexaoBanco.executaPreparedQuery(sql, param, super.getIdLog());
			
			if(rs.next())
				saldoExpiravel = vo.getSaldoFinal() - rs.getDouble("BONUS");			
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
		
		return saldoExpiravel;
	}
	
	/**
	 * Calcula o saldo que deve ser retirado do assinante
	 * 
	 * @param  vo			- Instancia de <code>ExpiracaoSaldoControleVO</code>
	 * @return double		- Saldo total a retirar do assinante
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	private double calcularSaldoPeriodicoExpiravel(ExpiracaoSaldoControleVO vo) throws GPPInternalErrorException, SQLException
	{
		double saldoExpiravel = 0.0;
		ResultSet rs = null;
		
		try
		{
			String sql 	= "SELECT SUM(ABS(VLR_CREDITO_PERIODICO)) AS PERIODICO		"
						+ "  FROM TBL_REC_RECARGAS                                  "
						+ " WHERE DAT_ORIGEM >  TO_DATE(?, 'dd/mm/yyyy hh24:mi:ss') " 
						+ "   AND DAT_ORIGEM <= SYSDATE                             "
						+ "   AND IDT_MSISDN  = ?                                   " ;
			
			Object[] param = {vo.getDataRecarga(),vo.getMsisdn()};
			
			rs = conexaoBanco.executaPreparedQuery(sql, param, super.getIdLog());
			
			if(rs.next())
				saldoExpiravel = vo.getSaldoFinal() - rs.getDouble("PERIODICO");			
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
		
		return saldoExpiravel;
	}
	
	/**
	 * Efetua o débito do saldo do assinante
	 * @param msisdn			- msisdn do assinante
	 * @param valor				- valor a ser debitado
	 * @return short			- Código de erro ou sucesso
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	private void debitarSaldoExpirado(ExpiracaoSaldoControleVO vo, double valor) throws GPPInternalErrorException, GPPTecnomenException
	{
		// Eh realizada a construcao do ValoresRecarga para
		// realizacao do ajuste necessario, levando em
		// consideradao o Tipo de Saldo a ser expirado do VO
		ValoresRecarga valoresRecarga = vo.getValoresRecarga(valor);
		// Monta-se o Tipo de Transacao para a realizacao do 
		// ajuste de expiracao de saldo utilizando o Tipo de Saldo
		// a ser expirado ou o tipo de transacao no caso do Controle
		// da safra nova
		String tipoTransacao = vo.getTipoTransacaoAjuste();
		
		// Realiza o ajuste apenas se a entidade nao for nula
		if (valoresRecarga != null)
		{
			short retorno = ajustar.executarAjuste(vo.getMsisdn(),
								   				   tipoTransacao,
								   				   Definicoes.TIPO_CREDITO_REAIS,
								   				   valoresRecarga,
								   				   Definicoes.TIPO_AJUSTE_DEBITO,
								   				   Calendar.getInstance().getTime(),
								   				   Definicoes.SO_GPP,
								   				   Definicoes.GPP_OPERADOR,
								   				   null,
								   				   "ExpiracaoSaldo",
								   				   true,
								   				   null);
			
			if (retorno == Definicoes.RET_OPERACAO_OK)
				super.log(Definicoes.INFO, super.nomeClasse, "Foi retirado R$ "+valor+" do assinante "+vo.getMsisdn());
			else
				super.log(Definicoes.WARN, super.nomeClasse, "Falha na expiracao de R$ "+valor+" para o assinante " + vo.getMsisdn());
		}
	}
	
	/**
	 * Verifica se o assinante fez recargas
	 * 
	 * @param msisdn	- msisdn do assinante
	 * @return boolean
	 * @throws GPPInternalErrorException
	 * @throws SQLException
	 */
	private boolean fezRecarga(String msisdn) throws GPPInternalErrorException, SQLException
	{
		boolean fezRecargas = false;
		ResultSet rs = null;
		
		try
		{
			String sql 	= "SELECT 1                   " 
						+ "  FROM tbl_rec_recargas 	  "
						+ " WHERE id_tipo_recarga = ? "
						+ "   AND tip_transacao <> ?  "
						+ "   AND idt_msisdn = ?      ";
			
			Object[] param = {Definicoes.TIPO_RECARGA
							 ,Definicoes.RECARGA_FRANQUIA
							 ,msisdn};
			
			rs = conexaoBanco.executaPreparedQuery(sql, param, super.getIdLog());
			
			if(rs.next())
				fezRecargas = true;
		}
		finally
		{
			if (rs != null)
				rs.close();
		}	
		
		return fezRecargas;
	}
}