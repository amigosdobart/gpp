package com.brt.gpp.aplicacoes.consultar.consultaCredito;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.aprovisionar.Aprovisionar;
import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.componentes.aprovisionamento.ComponenteNegocioAprovisionamento;
import com.brt.gpp.componentes.aprovisionamento.orb.aprovisionamentoPackage.retornoDesativacaoAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GerarXML;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.gppExceptions.GPPTecnomenException;
import com.brt.gpp.comum.mapeamentos.MapCodigosRetorno;
import com.brt.gpp.comum.mapeamentos.MapPlanoPreco;

/**
*
* Classe destinada a realizar a consulta dos creditos de um assinante.
*
* @author 	Bernardo Pina
* 
*/

public class DesativaAssinante extends Aplicacoes 
{
	private long idProcesso;
	private PREPConexao conexaoPrep;
	private CreditoAssinante creditoAssinante = new CreditoAssinante();
	private retornoDesativacaoAssinante retornoDesativacao = new retornoDesativacaoAssinante();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DecimalFormat df = new DecimalFormat("######0.00",new DecimalFormatSymbols(new Locale("PT","br")));
	
	/**
	 * Metodo....: DesativaAssinante
	 * Descricao.: Metodo construtor da classe
	 * @param idProcesso
	 */
	public DesativaAssinante(long idProcesso)
	{
		super(idProcesso, Definicoes.CL_DESATIVA_ASSINANTE);
		this.idProcesso = idProcesso;
	}
	
	/**
	 * Metodo....: desativarAssinante
	 * Descricao.: Metodo responsavel pela desativacao do assinante
	 * 
	 * @param MSISDN
	 * @param motivoDesativacao
	 * @param aOperador
	 * @param idProcesso
	 * @return String - XML da desativacao do assinante
	 * @throws GPPInternalErrorException
	 * @throws GPPTecnomenException
	 */
	public String desativarAssinante(String MSISDN, String motivoDesativacao, String aOperador, long idProcesso)
	{
		try {
			ComponenteNegocioAprovisionamento componenteAprovisionamento = new ComponenteNegocioAprovisionamento();
			Aprovisionar aprovisionamento = new Aprovisionar(super.getIdLog());
			
			consultaAssinante(MSISDN);
			retornoDesativacao.codigoRetorno = creditoAssinante.getAssinante().getRetorno();
			
			// Esta verificação está sendo feita hardcode porque na data de
			// hoje (04/07/2007) o motivo de desativação está em hardcode 
			// na classe Definicoes.java
			if (Integer.parseInt(motivoDesativacao) >= 05)
			{
				retornoDesativacao.codigoRetorno = Definicoes.RET_MOTIVO_DESATIVACAO_INVALIDO;

				aprovisionamento.gravaDadosDesativacao(MSISDN, 
													   motivoDesativacao, 
													   0, 
													   aOperador, 
													   Definicoes.PROCESSO_ERRO, 
													   (short)Definicoes.RET_MOTIVO_DESATIVACAO_INVALIDO,
													   new Timestamp(Calendar.getInstance().getTimeInMillis()));
			}
			else if (creditoAssinante.getAssinante().getRetorno() == Definicoes.RET_OPERACAO_OK)
			{
				consultaCategoria();
				consultaRecarga();
				
				// Realiza a desativação do usuário na plataforma Tecnomen
				retornoDesativacao = componenteAprovisionamento.desativaAssinante(MSISDN, motivoDesativacao, aOperador);
			}
			else
				aprovisionamento.gravaDadosDesativacao(MSISDN, 
													   motivoDesativacao, 
													   0, 
													   aOperador, 
													   Definicoes.PROCESSO_ERRO, 
													   creditoAssinante.getAssinante().getRetorno(),
													   new Timestamp(Calendar.getInstance().getTimeInMillis()));
		} catch (Exception e) {
			retornoDesativacao.codigoRetorno = Definicoes.RET_ERRO_TECNICO;
		}
		return gerarXML();
	}
	
	/**
	 * Metodo....: consultaAssinante
	 * Descricao.: Metodo responsavel por fazer a consulta dos dados do assinante
	 * 
	 * @param MSISDN
	 * @throws GPPInternalErrorException
	 */
	private void consultaAssinante(String MSISDN) throws GPPInternalErrorException
	{
		ConsultaAssinante consultaAssinante = new ConsultaAssinante(idProcesso);
		Assinante assinante = consultaAssinante.executaConsultaCompletaAssinanteTecnomen(MSISDN);
		creditoAssinante.setAssinante(assinante);
	}
	
	/**
	 * Metodo....: consultaCategoria
	 * Descricao.: Método responsável por fazer a consulta da categoria do terminal
	 * 
	 * @throws GPPInternalErrorException
	 */
	private void consultaCategoria() throws GPPInternalErrorException
	{
		MapPlanoPreco mapPlano = MapPlanoPreco.getInstancia();
		creditoAssinante.setCategoria(mapPlano.consultaCategoria(creditoAssinante.getAssinante().getPlanoPreco()));
	}
	
	/**
	 * Metodo....: consultaRecarga
	 * Descricao.: Metodo responsavel por fazer a consulta dos dados da ultima recarga
	 * 			   do terminal.
	 * 
	 * @throws GPPInternalErrorException
	 */
	private void consultaRecarga() throws GPPInternalErrorException
	{
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());

			String sql ="select dat_recarga,id_sistema_origem,dat_origem,tip_transacao,vlr_pago " + 
						  "from tbl_rec_recargas a " +
						 "where idt_msisdn = ? " +
						   "and id_tipo_recarga = ? " +
						   "and ROWNUM = 1 " +
						"order by dat_origem desc";

			Object params[] = {creditoAssinante.getAssinante().getMSISDN()
					          ,Definicoes.TIPO_RECARGA
					          };
	
			ResultSet res = conexaoPrep.executaPreparedQuery(sql, params ,idProcesso);
			if (res.next()) 
			{
				creditoAssinante.setDataUltimaRecarga(res.getDate("DAT_RECARGA"));
				creditoAssinante.setValorUltimaRecarga(res.getDouble("VLR_PAGO"));
				creditoAssinante.setTipoTransacao(res.getString("TIP_TRANSACAO"));
				creditoAssinante.setSistemaDeOrigem(res.getString("ID_SISTEMA_ORIGEM"));
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO, "consultaRecarga", "Erro ao consultar ultima recarga do assinante:"+creditoAssinante.getAssinante().getMSISDN()+". Erro:"+se);
			throw new GPPInternalErrorException(se.getMessage());
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
	}
	
	/**
	 * Metodo....: gerarXML
	 * Descricao.: Metodo responsavel por gerar o XML de retorno da desativacao
	 * 
	 * @return String - XML com as informacoes de desativacao
	 */
	private String gerarXML() {
		GerarXML gerarXML 				= new GerarXML("mensagem");
		MapCodigosRetorno mapCodRetorno	= MapCodigosRetorno.getInstance();
		
		gerarXML.abreNo("cabecalho");
		gerarXML.adicionaTag("empresa", "BRG");
		gerarXML.adicionaTag("sistema", "GPP");
		gerarXML.adicionaTag("processo", "DESATIVARASSINANTEXML");
		gerarXML.adicionaTag("data", sdf.format(Calendar.getInstance().getTime()));
		gerarXML.adicionaTag("identificador_requisicao", creditoAssinante.getAssinante().getMSISDN());
		gerarXML.adicionaTag("codigo_erro", String.valueOf(retornoDesativacao.codigoRetorno));
		gerarXML.adicionaTag("descricao_erro", mapCodRetorno.getRetorno(retornoDesativacao.codigoRetorno).getDescRetorno());
		gerarXML.fechaNo();
		gerarXML.abreNo("conteudo");
		gerarXML.abreTagCDATA();
		gerarXML.abreNo("root");
		gerarXML.abreNo("GPPDesativacaoAssinante");
			gerarXML.abreNo("assinante");
				gerarXML.adicionaTag("MSISDN", creditoAssinante.getAssinante().getMSISDN());
				gerarXML.adicionaTag("PlanoPreco", String.valueOf(creditoAssinante.getAssinante().getPlanoPreco()));
				gerarXML.adicionaTag("categoria", String.valueOf(creditoAssinante.getCategoria()));
				gerarXML.abreNo("saldos");
					gerarXML.abreNo("principal");
						gerarXML.adicionaTag("valor", df.format(creditoAssinante.getCreditoPrincipal()));
						gerarXML.adicionaTag("dataExpiracao", creditoAssinante.getDataExpiracaoCreditoPrincipal());
					gerarXML.fechaNo();
					gerarXML.abreNo("bonus");
						gerarXML.adicionaTag("valor", df.format(creditoAssinante.getCreditoBonus()));
						gerarXML.adicionaTag("dataExpiracao", creditoAssinante.getDataExpiracaoCreditoBonus());
					gerarXML.fechaNo();
					gerarXML.abreNo("sms");
						gerarXML.adicionaTag("valor", df.format(creditoAssinante.getCreditoSMS()));
						gerarXML.adicionaTag("dataExpiracao", creditoAssinante.getDataExpiracaoCreditoSMS());
					gerarXML.fechaNo();
					gerarXML.abreNo("dados");
						gerarXML.adicionaTag("valor", df.format(creditoAssinante.getCreditoDados()));
						gerarXML.adicionaTag("dataExpiracao", creditoAssinante.getDataExpiracaoCreditoDados());
					gerarXML.fechaNo();
					gerarXML.abreNo("periodico");
						gerarXML.adicionaTag("valor", df.format(creditoAssinante.getCreditoPeriodico()));
						gerarXML.adicionaTag("dataExpiracao", creditoAssinante.getDataExpiracaoCreditoPeriodico());
					gerarXML.fechaNo();
				gerarXML.fechaNo();
				gerarXML.abreNo("ultimaRecarga");
					gerarXML.adicionaTag("dataHora", creditoAssinante.getDataUltimaRecarga() != null ? sdf.format(creditoAssinante.getDataUltimaRecarga()) : "");
					gerarXML.adicionaTag("valor", df.format(creditoAssinante.getValorUltimaRecarga()));
					gerarXML.adicionaTag("tipoTransacao", creditoAssinante.getTipoTransacao() != null ? creditoAssinante.getTipoTransacao() : "");
					gerarXML.adicionaTag("sistemaOrigem", creditoAssinante.getSistemaDeOrigem() != null ? creditoAssinante.getSistemaDeOrigem() : "");
				gerarXML.fechaNo();
			gerarXML.fechaNo();		// Fecha o Tag "assinante"
		gerarXML.fechaNo();			// Fecha o Tag "GPPCreditoRemanescente"
		gerarXML.fechaNo();			// Fecha o Tag "root"
		gerarXML.fechaTagCDATA();
		gerarXML.fechaNo();			// Fecha o Tag "conteudo"
		
		return gerarXML.getXML();
	}	
}
