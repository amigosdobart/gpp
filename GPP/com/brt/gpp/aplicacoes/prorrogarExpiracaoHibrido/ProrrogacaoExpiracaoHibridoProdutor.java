package com.brt.gpp.aplicacoes.prorrogarExpiracaoHibrido;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*
*@author Marcos C. Magalhães
*
*/

public class ProrrogacaoExpiracaoHibridoProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	//Parametros necessarios para o processo Batch
	private String				dataProcessamento;
	private int					numRegistros;
	private String				statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	//Recursos utilizados no produtor
	private ResultSet 			listaUsuarios;
	private PREPConexao			conexaoPrep;
	
	/**
	 * Metodo....: ProrrogacaoExpiracaoHibridoProdutor
	 * Descricao.: Construtor de classe do processo Batch
	 * @param logId - Id do processo
	 */
	
	public ProrrogacaoExpiracaoHibridoProdutor (long logId)
	{
		super(logId, Definicoes.CL_PRORROGACAO_EXPIRACAO_HIBRIDO);	
	}
	
	/**
	 * Metodo....: parseParametros
	 * Descricao.: Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	
	private void parseParametros(String params[]) throws Exception
	{
	    // Para o processo batch de prorrogacao da data de expiracao dos saldos dos
		// hibridos, um dos parametros utilizados eh a data de processamento. Portanto a verificacao
		// feita nos parametros sao em relacao a esta data sendo que basta ser
		// uma data valida
		if(params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			dataProcessamento = params[0];
		}
		catch(ParseException pe)
		{
			throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor:"+params[0]);
		}
	}
	
	private void verificaImportacaoAssinantes() throws Exception
	{
		// Select para verificar a importacao de dados de D-1 onde D eh a data
		// passada como parametro
				
		String sql = "  select 1 " +
						" from (select count(1) as qtd" +
						"       from tbl_apr_assinante_tecnomen" +
						"       where dat_importacao = trunc(sysdate)" +
						"       ) a," +
						"      (select count(1) as qtd" +
						"       from tbl_apr_assinante_tecnomen" +
						"       where dat_importacao = trunc(sysdate-1)" +
						"       ) b" +
						" where abs((a.qtd-b.qtd)/b.qtd) < 5 " +
						" and b.qtd != 0 " ;
		
		Object param[] ={};		
		
		ResultSet rs = conexaoPrep.executaPreparedQuery(sql,param,super.getIdLog());
		if (!rs.next())
			// Se nao teve dados importados entao envia excecao para indicar
			// falha no procedimento
			throw new GPPInternalErrorException("Prorrogacao de expiracao de hibridos: erro na importacao de assinantes");
	}
	
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.DEBUG, "ProrrogacaoExpiracaoHibridoProdutor", "Inicio");
		//Executa a verificacao de parametros
		parseParametros(params);
		
		//Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa dos
		//assinante que entram no status SHUTDOWN. O resultSet eh atualizado ficando disponivel
		//para as threads atraves do metodo next.
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		verificaImportacaoAssinantes();
	
		String sql = " select sub_id, expiry " +
				     " from (select sub_id, expiry " +
				     "              ,nvl((select account_status" +
				     "					  from tbl_apr_assinante_tecnomen b" +
				     "                    where b.dat_importacao = ass.dat_importacao - 1" +
				     "                      and b.sub_id           = ass.sub_id" +
				     "                   ), ?) as sta_dia_anterior" +
				     "       from tbl_apr_assinante_tecnomen ass" +
				     "            ,tbl_ger_plano_preco plano" +
				     "       where account_status        = ?" +
				     "         and dat_importacao        = to_date(?, 'dd/MM/yyyy')" +
				     "         and plano.idt_plano_preco = profile_id" +
				     "         and plano.idt_categoria   = ? " +
				     "       )" +
				     " where sta_dia_anterior = ? ";
		
			
		Object param[] = {new Short(Definicoes.FIRST_TIME_USER), new Short(Definicoes.NORMAL), dataProcessamento ,new Integer(Definicoes.CATEGORIA_HIBRIDO), new Short(Definicoes.FIRST_TIME_USER)};
		listaUsuarios = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
	}
	
	
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_PRORROGACAO_EXPIRACAO_HIBRIDO;
	}
	
	public String getDescricaoProcesso()
	{
	    if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return "Foram prorrogadas " + numRegistros + " datas de expiracao de hibridos";
	    else
	        return "Erro no processo de prorrogação de datas de expiracao";
	}
	
	public String getStatusProcesso()
	{
		return statusProcesso;
	}
	
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}
	
	public String getDataProcessamento()
	{
		return this.dataProcessamento;
	}
	
	
	
	public Object next() throws Exception {
	
		UsuariosHibridosAtivadosVO UsrHibridoAtivados = null;
		short numDias = Short.parseShort(MapConfiguracaoGPP.getInstance().getMapValorConfiguracaoGPP("NUM_DIAS_PRORROGACAO_EXP_HIBRIDO"));
		//Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
		if(listaUsuarios.next())
		{
			//O primeiro campo do resultSet informa o numero do assinante
			UsrHibridoAtivados = new UsuariosHibridosAtivadosVO(listaUsuarios.getString(1), listaUsuarios.getDate(2), numDias);
			numRegistros++;
		}
		return UsrHibridoAtivados;
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		super.log(Definicoes.DEBUG, "ProrrogacaoExpiracaoHibridoProdutor", "Fim");
	}
	
	public void handleException()
	{
	}
	
	public PREPConexao getConexao()
	{
		return conexaoPrep;
	}

}











