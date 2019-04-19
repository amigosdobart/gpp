package com.brt.gpp.aplicacoes.recarregar;

import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.aprovisionar.Assinante;
import com.brt.gpp.aplicacoes.consultar.ConsultaAssinante;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;

/**
 *	Classe responsavel por fazer ajustes em saldos de assinantes.
 *
 *	@version		1.0		25/03/2004		Primeira versao.
 *	@author			Denys Oliveira
 *
 *	@version		2.0		06/05/2007		Adaptacao para o Controle Total.
 *	@author			Daniel Ferreira
 */
public class Ajustar extends InsercaoCreditos
{

	/**
	 * Metodo...: Ajustar
	 * Descricao: Construtor 
	 * @param	long	logId (Identificador do Processo para Log)
	 */
	public Ajustar(long aIdProcesso)
	{
		super(aIdProcesso, Definicoes.CL_AJUSTAR);
	}	

	/**
	 *	Executa ajuste de um unico saldo do assinante.
	 *
	 *	@param		msisdn					MSISDN do assinante.
	 *	@param		tipoTransacao			Tipo de transacao.
	 *	@param		tipoCredito				Tipo de credito.
	 *	@param		valor					Valor do ajuste.
	 *	@param		tipoAjuste				Tipo de ajuste (credito ou debito)
	 *	@param		dataHora				Data/hora do ajuste.
	 *	@param		sistemaOrigem			Identificador do sistema de origem.
	 *	@param		operador				Identificador do operador que solicitou o ajuste.
	 *	@param		dataExpiracao			Data de expiracao do ajuste.
	 *	@param		assinante				Informacoes do assinante na plataforma. Utilizado quando uma consulta 
	 *										previa de assinante ja foi realizada.
	 *	@param		descricao				Descricao do ajuste.
	 *	@param		isAjusteNormal 			Indicador de ajuste normal (true) ou sem credito (false).
	 *	@return		Codigo de retorno da operacao.
	 */	
	public short executarAjuste(String msisdn, 
								String tipoTransacao, 
								String tipoCredito, 
								double valor, 
								String tipoAjuste, 
								Date dataHora, 
								String sistemaOrigem, 
								String operador, 
								Date dataExpiracao, 
								Assinante assinante, 
								String descricao, 
								boolean isAjusteNormal)
	{
		short result = Definicoes.RET_OPERACAO_OK;
		
		try
		{
			ValoresRecarga valores = null;
			
			//Criando o objeto de valores de recarga.
			if(tipoCredito != null)
			{
				//Ajuste no Saldo Principal.
				if(tipoCredito.equals(Definicoes.TIPO_CREDITO_REAIS))
					valores = new ValoresRecarga(valor, 0, 0, 0, 0);
				//Ajuste no Saldo de Bonus.
				else if(tipoCredito.equals(Definicoes.TIPO_CREDITO_MINUTOS))
					valores = new ValoresRecarga(0, 0, valor, 0, 0);
				//Ajuste no Saldo de Torpedos
				else if(tipoCredito.equals(Definicoes.TIPO_CREDITO_SMS))
					valores = new ValoresRecarga(0, 0, 0, valor, 0);
				//Ajuste no Saldo de Dados.
				else if(tipoCredito.equals(Definicoes.TIPO_CREDITO_VOLUME_DADOS))
					valores = new ValoresRecarga(0, 0, 0, 0, valor);
				//Ajuste no Saldo Periodico.
				else if(tipoCredito.equals(Definicoes.TIPO_CREDITO_FRANQUIA))
					valores = new ValoresRecarga(0, valor, 0, 0, 0);
				
				//Atribuindo a data de expiracao. As datas de expiracao devem sempre ser atribuidas em funcao da data 
				//do Saldo Principal, com excecao do Saldo Periodico, que e independente.
				if(valores != null)
				{
					if(tipoCredito.equals(Definicoes.TIPO_CREDITO_FRANQUIA))
						valores.setDataExpPeriodico(dataExpiracao);
					// A inicializacao das outras datas foi transferida para a classe InsercaoDatasExpiracao
					else
						valores.setDataExpPrincipal(dataExpiracao);
				}
			}
			
			//Executando o ajuste.
			result = this.executarAjuste(msisdn, 
										 tipoTransacao,
										 tipoCredito,
										 valores, 
										 tipoAjuste, 
										 dataHora, 
										 sistemaOrigem, 
										 operador, 
										 assinante, 
										 descricao, 
										 isAjusteNormal,
                                         null);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "executaAjuste", "MSISDN: " + msisdn + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		
		return result;
	}
    
    /**
     *  Executa ajuste nos saldos do assinante.
     *
     *  @param      msisdn                  MSISDN do assinante.
     *  @param      tipoTransacao           Tipo de transacao.
     *  @param      valores                 Informacoes dos valores do ajuste.
     *  @param      tipoAjuste              Tipo de ajuste (credito ou debito)
     *  @param      dataHora                Data/hora do ajuste.
     *  @param      sistemaOrigem           Identificador do sistema de origem.
     *  @param      operador                Identificador do operador que solicitou o ajuste.
     *  @param      dataExpiracao           Data de expiracao do ajuste.
     *  @param      assinante               Informacoes do assinante na plataforma. Utilizado quando uma consulta 
     *                                      previa de assinante ja foi realizada.
     *  @param      descricao               Descricao do ajuste.
     *  @param      isAjusteNormal          Indicador de ajuste normal (true) ou sem credito (false).
     *  @param      nsuInstituicao          NSU da instituicao (caso null, esse metodo usa a identificacao da recarga)
     *  @return     Codigo de retorno da operacao.
     */ 
    public short executarAjuste(String msisdn, 
                                String tipoTransacao, 
                                String tipoCredito, 
                                ValoresRecarga valores, 
                                String tipoAjuste, 
                                Date dataHora, 
                                String sistemaOrigem, 
                                String operador, 
                                Assinante assinante, 
                                String descricao, 
                                boolean isAjusteNormal,
                                String nsuInstituicao)
    {
        short               result  = Definicoes.RET_OPERACAO_OK;
        ParametrosRecarga   ajuste  = new ParametrosRecarga();
        
        super.log(Definicoes.DEBUG, "executarRecarga", "MSISDN: " + msisdn + " - Tipo de Transacao: " + tipoTransacao);
        
        try
        {
            //Preenchendo os parametros do ajuste.
            ajuste.setIdOperacao(Definicoes.TIPO_AJUSTE);
            ajuste.setIdentificacaoRecarga(RecargaDAO.newIdRecarga(super.logId));
            ajuste.setMSISDN(msisdn);
            ajuste.setDatOrigem(dataHora);
            ajuste.setDatRecarga(Calendar.getInstance().getTime());
            ajuste.setTipoTransacao(tipoTransacao);
            ajuste.setTipoCredito(tipoCredito);
            ajuste.setIndCreditoDebito(tipoAjuste);
            ajuste.setSistemaOrigem(sistemaOrigem);
            ajuste.setOperador(operador);
            ajuste.setNsuInstituicao((nsuInstituicao == null) ? ajuste.getIdentificacaoRecarga() : nsuInstituicao);
            ajuste.setValores(valores);
            ajuste.setDescricao(descricao);
            
            //Ajustando os valores de recarga.
            if(valores != null)
            {
                //Correcao de seguranca. O processo deve certificar que os valores para o ajuste nao estao invertidos, para
                //evitar fraude. Desta forma e calculado o valor absoluto antes da aplicacao do tipo de ajuste.
                valores.setSaldoPrincipal(Math.abs(valores.getSaldoPrincipal()));
                valores.setSaldoPeriodico(Math.abs(valores.getSaldoPeriodico()));
                valores.setSaldoBonus    (Math.abs(valores.getSaldoBonus    ()));
                valores.setSaldoSMS      (Math.abs(valores.getSaldoSMS      ()));
                valores.setSaldoGPRS     (Math.abs(valores.getSaldoGPRS     ()));
                
                //Se for uma transacao de debito, inverter valor informando.
                if(tipoAjuste.equals(Definicoes.TIPO_AJUSTE_DEBITO))
                {
                    valores.setSaldoPrincipal(-valores.getSaldoPrincipal());
                    valores.setSaldoPeriodico(-valores.getSaldoPeriodico());
                    valores.setSaldoBonus    (-valores.getSaldoBonus    ());
                    valores.setSaldoSMS      (-valores.getSaldoSMS      ());
                    valores.setSaldoGPRS     (-valores.getSaldoGPRS     ());
                }
            }
            
            //Inserindo os dados do assinante.
            if(assinante != null)
                ajuste.setAssinante(assinante);
            else
                ajuste.setAssinante(new ConsultaAssinante(super.logId).consultarAssinantePlataforma(msisdn));

            // Modifica as datas de expiração
            InsercaoDatasExpiracao datas = new InsercaoDatasExpiracao();
            ajuste = datas.modificaDatasExpiracao(ajuste, tipoTransacao);
            
            //Decidindo qual valor de recarga e qual metodo de recarga deve ser executado baseado na natureza do 
            //assinante.
            if(ajuste.getAssinante().getNaturezaAcesso().equals("GSM")) 
                result = this.executarAjusteMultiplosSaldos(ajuste, isAjusteNormal);
            else
                result = this.executarAjusteMASC(ajuste, isAjusteNormal);
        }
        catch(Exception e)
        {
            super.log(Definicoes.ERRO , "executaAjuste", "MSISDN: " + msisdn + " - Excecao: " + e);
            result = Definicoes.RET_ERRO_TECNICO;
        }

        return result;
    }
	
	/**
	 *	Executa o processo de ajuste para acessos da plataforma Tecnomen.
	 *
	 *	@param		ajuste					Parametros do ajuste.
	 *	@param		isAjusteNormal			Flag indicativa de ajuste normal ou simples inclusao de registro na tabela.
	 *	@return		Codigo de retorno da operacao.
	 */
	private short executarAjusteMultiplosSaldos(ParametrosRecarga ajuste, boolean isAjusteNormal)
	{
		short result = Definicoes.RET_OPERACAO_OK;
		
		try
		{
			super.log(Definicoes.DEBUG, "executarAjusteMultiplosSaldos", ajuste.toString());
			
			//Validando os parametros da recarga.
			result = super.validarParametros(ajuste);
					
			if(result == Definicoes.RET_OPERACAO_OK)
				//Executando o ajuste para o assinante.
				result = super.executarAjuste(ajuste, isAjusteNormal);
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "executaRecargaMultiplosSaldos", ajuste.toString() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			//Caso a operacao nao tenha sido OK, e necessario inserir o registro na tabela de recargas com erro. 
			if(result != Definicoes.RET_OPERACAO_OK) 
				if(!RecargaDAO.incrementarRecargaNok(ajuste, result, super.logId))
					super.log(Definicoes.WARN, "executaAjusteMultiplosSaldos", ajuste.toString() + " - Nao foi possivel inserir registro de erro de ajuste.");

			super.log(Definicoes.INFO, "executaAjusteMultiplosSaldos", ajuste.toString() + " - Codigo de retorno da operacao: " + result);
		}
		
		return result;
	}
	
	/**
	 *	Executa o ajuste via MASC.
	 *
	 *	@param		ajuste					Parametros do ajuste.
	 *	@param		isAjusteNormal			Flag indicativa de ajuste normal ou simples inclusao de registro na tabela.
	 *	@return		Codigo de retorno da operacao. 
	 */
	private short executarAjusteMASC(ParametrosRecarga ajuste, boolean isAjusteNormal)
	{
		short		result		= Definicoes.RET_OPERACAO_OK;
		PREPConexao	conexaoPrep	= null;
		
		super.log(Definicoes.DEBUG, "executarAjusteMASC", ajuste.toString());

		try
		{
			String	msisdn			= ajuste.getMSISDN();
			double	valor			= ajuste.getValores().getSaldoPrincipal();
			String	idRecarga		= ajuste.getIdentificacaoRecarga();

			//Obtendo conexao com o banco de dados.
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(super.logId);
			
			if(isAjusteNormal)
			{
				DadosRecarga retornoMasc = super.insereCreditosTFPP(msisdn, valor, 0, idRecarga); 
				result = (short)retornoMasc.getCodigoErro();
			}
			
			if(result == Definicoes.RET_OPERACAO_OK)
			{
				//Atualizando os saldos do assinante de acordo com os valores da recarga.
				ajuste.getAssinante().atualizarSaldos(ajuste.getValores());
				//Inserindo o registro da recarga.
				RecargaDAO.inserirRecargaTFPP(ajuste, conexaoPrep);
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO, "executarAjusteMASC", ajuste.toString() + " - Excecao: " + e);
			result = Definicoes.RET_ERRO_TECNICO;
		}
		finally
		{
			//Caso a operacao nao tenha sido OK, e necessario inserir o registro na tabela de recargas com erro. 
			if(result != Definicoes.RET_OPERACAO_OK) 
				if(!RecargaDAO.incrementarRecargaNok(ajuste, result, super.logId))
					super.log(Definicoes.WARN, "executarAjusteMASC", ajuste.toString() + " - Nao foi possivel inserir registro de erro de ajuste.");
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, super.logId);
			super.log(Definicoes.INFO,"executarAjusteMASC", ajuste.toString() + " - Codigo de retorno da operacao: " + result);
		}
		
		return result;
	}
	
}
