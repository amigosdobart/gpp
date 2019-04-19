package com.brt.gpp.aplicacoes.recargaMassa;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;

/**
 * Converte o VO (ImportacaoRecargaMassaVO) em uma entidade, fazendo
 * todas as validacoes necessarias
 * 
 * @author Bernardo Dias
 * Data: 09/08/2007
 */
public class RegistroLoteParser 
{
	public static RegistroLote parse(ImportacaoRecargaMassaVO vo, RecargaMassaDAO dao, PREPConexao conexaoPrep)
	{
		RegistroLote registro = new RegistroLote();
		String[] campo = vo.getRegistro().split(";");
		
		try
		{
			// valida quantidade minima de campos
			
			if (campo.length < 4)
				throw new Exception("Especifique pelo menos 4 campos");
			
			// campo LOTE
			
			registro.setLote(vo.getNomeArquivo().substring(0, vo.getNomeArquivo().lastIndexOf(".")));
			
			// campo MSISDN
			
			if (!campo[0].matches("^55[0-9]{10}$"))
				throw new Exception("MSISDN nao eh GSM Pre-Pago ou Controle");
			registro.setMsisdn(campo[0]);
			
			// campo BONUS
			
			campo[1] = campo[1].replace(',','.');
			try
			{
				registro.setVlrBonus(new Double(campo[1]));
			}
			catch(Exception e)
			{
				throw new Exception("Valor Invalido");
			}
			
			// campo SM
			
			campo[2] = campo[2].replace(',','.');
			try
			{
				registro.setVlrSm(new Double(campo[2]));
			}
			catch(Exception e)
			{
				throw new Exception("Valor Invalido");
			}
			
			// campo DADOS
			
			campo[3] = campo[3].replace(',','.');
			try
			{
				registro.setVlrDados(new Double(campo[3]));
			}
			catch(Exception e)
			{
				throw new Exception("Valor Invalido");
			}
			
			/*
			 * valida as demais regras de negócio
			 */
			
			// comprimento da mensagem SMS
			
			if (campo.length >= 5)
			{
				registro.setMensagemSMS(campo[4]);
				if (campo[4].length() > 120)
					throw new Exception("Mensagem Extrapola o Limite de Caracteres");
			}
			
			// status do assinante
			 
			int status = dao.getStatusAssinante(registro.getMsisdn(), conexaoPrep);
			
			if (status == -1)
			{
				throw new Exception("MSISDN Inexistente");
			}
			
			if (!(status == Definicoes.NORMAL	||
				status == Definicoes.RECHARGE_EXPIRED ||
				status == Definicoes.DISCONNECTED))
			{
				throw new Exception("Status de Assinante Invalido");
			}

			// limite de ajuste
			
			double limite = Double.parseDouble(MapConfiguracaoGPP.getInstance().
					getMapValorConfiguracaoGPP("RECARGA_MASSA_LIMITE_AJUSTE"));
			
			if (registro.getVlrBonus().doubleValue() > limite || 
				registro.getVlrDados().doubleValue() > limite || 
				registro.getVlrSm().doubleValue() > limite)
			{
				throw new Exception("Valor Acima do Permitido");
			}
		}
		catch (Exception e) 
		{
			vo.setMensagemErro(e.getMessage());
			return null;
		}

		return registro;
	}
}
