package com.brt.gpp.aplicacoes.marreta;

import com.brt.gpp.aplicacoes.*;
/*
import com.brt.gpp.comum.*;
import com.brt.gpp.aplicacoes.recarregar.*;
import com.brt.gpp.comum.gppExceptions.*;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.conexoes.bancoDados.*;
import com.brt.gpp.componentes.recarga.*;

import java.io.IOException;
import java.sql.*;
*/

public class RecarregarNaMarra extends Aplicacoes
{

	public RecarregarNaMarra(long idProcesso)
	{
		super(idProcesso, "RECARGA_NA_MARRA");
	}
	
	/*
	public void fazRecargaNaMarra(String caminhoNomeArquivo)
	{
		ManipuladorArquivos fileHandler = null;
		ParametrosRecarga dadosRecarga = null;
		String linhaRecarga = null;
		
		try
		{
			fileHandler = new ManipuladorArquivos(caminhoNomeArquivo, false, super.getIdLog());
			ComponenteNegocioRecarga fazRecarga = new ComponenteNegocioRecarga();
		
			linhaRecarga = fileHandler.leLinha();
		
			while(linhaRecarga != null)
			{
				dadosRecarga = this.parseLinhaRecarga(linhaRecarga);

				fazRecarga.executaRecargaBanco(dadosRecarga.getMSISDN(), 
						dadosRecarga.getTipoTransacao(), 
						dadosRecarga.getIdentificacaoRecarga(), 
						dadosRecarga.getNsuInstituicao(), 
						"", 	// codLoja
						dadosRecarga.getTipoCredito(), 
						dadosRecarga.getIdValor(), 
						dadosRecarga.getDatOrigem(), 
						dadosRecarga.getDatInclusao(), 
						dadosRecarga.getDataContabil(),
						dadosRecarga.getIdTerminal(), 
						dadosRecarga.getTipoTerminal(), 
						dadosRecarga.getSistemaOrigem(), 
						dadosRecarga.getOperador());
				
				fazRecarga.executaRecarga(	dadosRecarga.getMSISDN(),
						dadosRecarga.getTipoTransacao(),
						dadosRecarga.getIdentificacaoRecarga(),
						dadosRecarga.getTipoCredito(),
						(long) dadosRecarga.getIdValor(),
						dadosRecarga.getDataHora(),
						dadosRecarga.getSistemaOrigem(),
						dadosRecarga.getOperador(),
						dadosRecarga.getNsuInstituicao(),
						dadosRecarga.getHash_cc(),
						dadosRecarga.getCpf(),
						Definicoes.SMS_PRIORIDADE_ZERO,"","",true);

				linhaRecarga = fileHandler.leLinha();			
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"fazRecargaNaMarra","Erro na Recarga: "+	dadosRecarga.getMSISDN()+":"+
																				dadosRecarga.getIdentificacaoRecarga()+":"+
																				dadosRecarga.getIdValor()+":"+gppE	);
		}
		catch(GPPTecnomenException tecE)
		{
			super.log(Definicoes.ERRO,"fazRecargaNaMarra","Erro na Recarga: "+	dadosRecarga.getMSISDN()+":"+
																				dadosRecarga.getIdentificacaoRecarga()+":"+
																				dadosRecarga.getIdValor()+":"+tecE	);
		}
		catch(GPPCorbaException cE)
		{
			super.log(Definicoes.ERRO,"fazRecargaNaMarra","Erro na Recarga: "+	dadosRecarga.getMSISDN()+":"+
																				dadosRecarga.getIdentificacaoRecarga()+":"+
																				dadosRecarga.getIdValor()+":"+cE	);
		}
		catch(IOException ioE)
		{
			super.log(Definicoes.ERRO,"fazRecargaNaMarra","Erro ao Ler Arquivo (ultima linha foi): "+linhaRecarga+":"+ioE);
		}
	}
	
	public void ajustarNaMarra()
	{
		// Obtem referencia do gerente de conexoes do Banco de Dados
		GerentePoolBancoDados gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			String sql = "select rr.idt_msisdn as msisdn, rv.id_valor as recarga, rr.vlr_credito_sms as bonus "+
				"from tbl_Rec_Recargas rr, tbl_rec_valores rv "+
				"where dat_recarga between '10-jun-2005' and sysdate "+
				"and rv.vlr_saldo_bonus_sms = rr.vlr_credito_sms "+
				"and tip_transacao ='04008' "+
				"and vlr_credito_sms <> 0 "+
				"and id_sistema_origem <> 'TEC' "+
				"group by rr.idt_msisdn, rr.dat_recarga, rr.id_sistema_origem, rr.id_valor, rr.tip_transacao,rv.id_valor,rr.vlr_credito_sms "+
				"having count(*) > 1";
			
			ResultSet rs = conexaoPrep.executaQuery(sql, super.getIdLog());
			
			while (rs.next())
			{
				String msisdn = rs.getString("msisdn");
				double estornoRecarga = rs.getDouble("recarga");
				double estornoBonus = rs.getDouble("bonus");
				
				Ajustar ajustador = new Ajustar(super.getIdLog());

				// Debita o valor dado indevidamente no saldo principal (não deve constar na tbl_rec_recargas)
				ajustador.executaAjuste(msisdn, 
										"05010",	// Estorno de débito 
										"00", 
										estornoRecarga, 
										"D", 
										"20050613144100", 
										"GPP", 
										"ex896518xx", 
										null, 
										null, 
										"Recarga Duplicada Microsiga", 
										true);
				
				// Debita o valor dado no saldo de SMS
				ajustador.executaAjuste(msisdn,
										"05010",
										"02",
										estornoBonus,
										"D",
										"20050613144200",
										"GPP",
										"ex896518",
										null,
										null,
										"Recarga Duplicada Microsiga",
										true);
			}
		}
		catch(GPPInternalErrorException gppe)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro GPP: "+gppe);
		}
		catch(SQLException sqle)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro SQL: "+sqle);
		}
		catch(GPPTecnomenException Tece)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro Tecnomen: "+Tece);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
	}
	
	public void ajustarNaMarraMASC()
	{
		// Obtem referencia do gerente de conexoes do Banco de Dados
		GerentePoolBancoDados gerenteBancoDados = GerentePoolBancoDados.getInstancia(logId);
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = gerenteBancoDados.getConexaoPREP(super.getIdLog());
			
			String sql = "select rr.idt_msisdn as msisdn, "+
						"rr.id_recarga as idRecarga, "+ 
						"to_char(rr.dat_recarga + 1/24/60/60,'yyyymmddhh24miss') as dataHora, "+ 
						"rv.vlr_bonus as vlrBonus "+ 
						"from tbl_rec_recargas_tfpp rr, tbl_rec_valores_tfpp rv "+ 
						"where rr.id_valor = rv.id_valor "+ 
						"and rr.idt_msisdn <> '556133271301' "+ 
						"and rv.vlr_bonus <> 0 "+ 
						"and not exists (select * from tbl_rec_recargas_tfpp where id_recarga = (rr.id_recarga||'B') and id_tipo_recarga = 'A') "+ 
						"and rr.vlr_credito_principal = rr.id_valor";
			
			ResultSet rs = conexaoPrep.executaQuery(sql, super.getIdLog());
			
			while (rs.next())
			{
				String msisdn = rs.getString("msisdn");
				double bonus = rs.getDouble("vlrBonus");
				String idRecarga = rs.getString("idRecarga");
				String dataHora = rs.getString("dataHora");
				
				Ajustar ajustador = new Ajustar(super.getIdLog());

				// Debita o valor dado indevidamente no saldo principal (não deve constar na tbl_rec_recargas)
				ajustador.executaAjusteMASC(msisdn, bonus, idRecarga+"B",dataHora,"04008","00","GPP","GPP");
			}
		}
		catch(GPPInternalErrorException gppe)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro GPP: "+gppe);
		}
		catch(SQLException sqle)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro SQL: "+sqle);
		}
		catch(Exception ex)
		{
			super.log(Definicoes.ERRO,"ajustarNaMarra","Erro SQL: "+ex);
		}
		finally
		{
			gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.getIdLog());
		}
	}
	
	private ParametrosRecarga parseLinhaRecarga(String linhaRecarga)
	{
		ParametrosRecarga retorno = null;
		
		String[] parametros = linhaRecarga.split(";");
		
		retorno = new ParametrosRecarga();
		
		retorno.setMSISDN(parametros[0]);
		retorno.setIdValor(Double.parseDouble(parametros[1]));
		retorno.setTipoTransacao(parametros[2]);
		retorno.setNsuInstituicao(parametros[3]);
		
		retorno.setIdentificacaoRecarga(parametros[4]);
		retorno.setTipoCredito("00");
		
		retorno.setDatOrigem(parametros[5]);
		retorno.setSistemaOrigem("BCO");
				
		retorno.setHash_cc("");
		retorno.setCpf("");
		retorno.setDataContabil(parametros[6]);
		retorno.setDatInclusao(parametros[7]);
		retorno.setOperador(parametros[8]);
		retorno.setIdTerminal(parametros[9]);
		retorno.setTipoTerminal(parametros[10]);
		
		return retorno;
	}
	
	public void ajusteViaArquivo(String caminhoNomeArquivo)
	{
		ManipuladorArquivos fileHandler = null;
		String linhaAjuste = null;
		String[] dadosAjuste = {"",""};
		
		try
		{
			fileHandler = new ManipuladorArquivos(caminhoNomeArquivo, false, super.getIdLog());
			ComponenteNegocioRecarga fazAjuste = new ComponenteNegocioRecarga();
		
			linhaAjuste = fileHandler.leLinha();
		
			while(linhaAjuste != null)
			{
				dadosAjuste = linhaAjuste.split("\t");
				
				fazAjuste.executaAjuste(dadosAjuste[0], 
										"05019", 
										"00", 
										9, 
										"C", 
										"20050719100000", 
										"GPP", 
										"GPP", 
										null);
				
				linhaAjuste = fileHandler.leLinha();			
			}
		}
		catch(GPPInternalErrorException gppE)
		{
			super.log(Definicoes.ERRO,"ajusteViaArquivo","Erro no Ajuste: "+	dadosAjuste[0] + gppE	);
		}
		catch(GPPTecnomenException tecE)
		{
			super.log(Definicoes.ERRO,"ajusteViaArquivo","Erro no Ajuste: "+	dadosAjuste[0] + tecE	);		
		}
		catch(GPPCorbaException cE)
		{
			super.log(Definicoes.ERRO,"ajusteViaArquivo","Erro no Ajuste: "+	dadosAjuste[0] + cE	);		
		}
		catch(IOException ioE)
		{
			super.log(Definicoes.ERRO,"ajusteViaArquivo","Erro ao Ler Arquivo (ultima linha foi): "+linhaAjuste+":"+ioE);
		}		
	}
	
	*/
}
