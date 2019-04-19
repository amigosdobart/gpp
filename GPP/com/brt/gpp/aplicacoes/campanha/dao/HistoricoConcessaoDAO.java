package com.brt.gpp.aplicacoes.campanha.dao;

import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.gerentesPool.GerentePoolLog;

import java.util.Calendar;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Esta classe realiza o tratamento de acesso ao banco de dados para processamento 
 * de informacoes relativos ao Historico de Concessao de creditos das Campanhas.
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class HistoricoConcessaoDAO 
{

	private static final String SQL_INS = "INSERT INTO TBL_CAM_HISTORICO_CONCESSAO " +
	                                      "(ID_CAMPANHA,IDT_MSISDN,DAT_SATISFACAO_CONDICAO" +
	                                      ",VLR_BONUS_SM,VLR_BONUS_DADOS,VLR_BONUS,NOM_CONDICAO) " +
	                                      "VALUES (?,?,?,?,?,?,?)";
	/**
	 * Este metodo realiza o registro da concessao efetuada em uma campanha para um 
	 * determinado assinante
	 * 
	 * @param assinante e campanha na qual efetuou a concessao de credito. - Assinante a ser registrado a concessao do credito
	 * @param valorSM concedido no saldo de SM  - Valor de bonus concedido no saldo de SMS
	 * @param valorDados que foi concedido no saldo de Dados - Valor de bonus concedido no saldo de dados
	 * @param dataSatisfacao - Data que foi executada a satisfacao de condicao
	 * @param condicao que satisfez a condicao da campanha
	 */
	public static void registraConcessao(AssinanteCampanha assinante
			                            ,double valorSM
			                            ,double valorDados
			                            ,double valorBonus
			                            ,String condicao
			                            ,Date dataSatisfacao) 
	{
		long idProcesso = GerentePoolLog.getInstancia(CampanhaDAO.class).getIdProcesso("HistoricoConcessaoDAO");
		PREPConexao conexaoPrep = null;
		try
		{
			conexaoPrep = GerentePoolBancoDados.getInstancia(idProcesso).getConexaoPREP(idProcesso);
			// Caso a data de satisfacao da condicao seja passado nulo entao utiliza
			// a data atual
			if (dataSatisfacao == null);
				dataSatisfacao = Calendar.getInstance().getTime();
				
			// Executa a insercao do historico de concessao de credito
			// indicando os valores recebidos pelo assinante e qual a
			// condicao de concessao que foi satisfeita pelo assinante
			Object param[] = {new Long(assinante.getCampanha().getId())
					         ,assinante.getMsisdn()
					         ,new Timestamp(dataSatisfacao.getTime())
					         ,new Double(valorSM)
					         ,new Double(valorDados)
					         ,new Double(valorBonus)
					         ,condicao
					         };
			conexaoPrep.executaPreparedUpdate(SQL_INS,param,idProcesso);
		}
		catch(Exception e)
		{
			GerentePoolLog.getInstancia(HistoricoConcessaoDAO.class).log(idProcesso,Definicoes.ERRO
                    ,"HistoricoConcessaoDAO"
                    ,"registraConcessao"
                    ,"Erro ao registrar concessao de credito para o assinante:"+assinante.getMsisdn()+". Erro:"+e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(idProcesso).liberaConexaoPREP(conexaoPrep,idProcesso);
		}
	}
}
