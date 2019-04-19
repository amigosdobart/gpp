package com.brt.gpp.aplicacoes.relatorios;

import java.io.Reader;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.MapConfiguracaoGPP;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
import com.brt.gpp.comum.GPPData;

/**
 *
 * Este arquivo refere-se a classe RelatoriosProdutor, responsável pela consulta
 * à tabela de solicitações de relatórios, que retorna as consultas a processar
 * pelo RelatoriosConsumidor. Ao terminar o processo do Consumidor, o Produtor
 * limpa os processos que já foram processados a algum tempo 
 *
 * @version					1.0
 * @author:					Marcelo Alves Araujo
 * @since		 			30/08/2005
 *
 */
public class RelatoriosProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	// Recursos utilizados no produtor
	private ResultSet 	consultas;
	private PREPConexao	conexaoPrep;
	
	/**
	 * <P><b>Metodo....:</b> RelatoriosProdutor
	 * <P><b>Descricao.:</b> Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public RelatoriosProdutor (long logId)
	{
		super(logId, Definicoes.CL_RELATORIOS_PRODUTOR);
	}
	
	/**
	 * <P><b>Metodo....:</b> startup
	 * <P><b>Descricao.:</b> Retorna as consultas a executar
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * 
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String params[]) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio");
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa das
		// consultas a serem realizadas. O resultSet é atualizado ficando disponivel
		// para as threads consumidoras atraves do metodo next
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		String sql = 	"SELECT " +
						" id_solicitacao AS id, " +
						" des_relatorio AS relatorio, " +
						" dat_agendamento AS agendamento, " +
						" decode(idt_tipo_solicitacao," +
						"        ?,to_char(sysdate,'yyyy_mm_dd')||'_'," +
						"        '')||nom_arquivo AS arquivo, " +
						" idt_tipo_solicitacao AS tipo," +
						" nom_operador AS operador, " +
						" idt_email AS mail " +
						"FROM " +
						" tbl_rel_solicitacoes_relatorio " +
						"WHERE ";
		
		if( params[0].equals(Definicoes.TIPO_SOLICITACAO_TEMPORARIA) )
		{
			sql = sql +	" idt_status = ? and dat_agendamento < sysdate";
			Object parametros[] = {	Definicoes.TIPO_SOLICITACAO_PERMANENTE,
									Definicoes.IDT_PROCESSAMENTO_NOT_OK};
			consultas = conexaoPrep.executaPreparedQuery(sql, parametros, super.getIdLog());
		}
		else if( params[0].equals(Definicoes.TIPO_SOLICITACAO_PERMANENTE) )
		{
			sql = sql +	" idt_status = ? AND idt_tipo_solicitacao = ?";
			Object parametros[] = {	Definicoes.TIPO_SOLICITACAO_PERMANENTE,
									Definicoes.IDT_PROCESSO_PERMANENTE,
									Definicoes.TIPO_SOLICITACAO_PERMANENTE};
			consultas = conexaoPrep.executaPreparedQuery(sql, parametros, super.getIdLog());
		}
	}
	
	/**
	 * <P><b>Metodo....:</b> next
	 * <P><b>Descricao.:</b> Retorna um objeto com os dados de uma linha do ResultSet
	 * 
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws GPPInternalErrorException
	{
		RelatoriosVO rel = null;
		
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
			if (consultas.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				// O campo operador passa a ter o grupo do usuário
				rel = new RelatoriosVO(	consultas.getInt("id"),this.getDadosClob(consultas.getClob("relatorio")),
										consultas.getString("agendamento"),consultas.getString("arquivo"),
										consultas.getString("tipo"),consultas.getString("operador"),
										consultas.getString("mail") );
				numRegistros++;
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro: "+se);
			throw new GPPInternalErrorException(se.toString());
		}
		return rel;
	}
	
	public void handleException()
	{
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_RELATORIOS;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
	    if(Definicoes.TIPO_OPER_SUCESSO.equals(getStatusProcesso()))
	        return "Foram executados " + numRegistros + " relatorios";
	    
	    return "Erro no processo";
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso()
	{
		return statusProcesso;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}
	
	/**
	 * <P><b>Metodo....:</b> finish
	 * <P><b>Descricao.:</b> Libera a conexão com o banco e limpa os registros com agendamento antigo
	 * 
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// Apaga todos os registros processados com sucesso 
		limpaProcessosAntigos();
		
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
				
		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return GPPData.dataFormatada();
	}	
	
	/**
	 * <P><b>Metodo....:</b> limpaProcessosAntigos
	 * <P><b>Descricao.:</b> Remove registros ja processados na tabela
	 * @throws GPPInternalErrorException
	 */
	private void limpaProcessosAntigos () throws GPPInternalErrorException
	{
		super.log(Definicoes.DEBUG, "Produtor.limpaProcessosAntigos", "Inicio");
		
		// Pega configuração GPP -> Para pegar o tempo até que a solicitação seja retirada da tabela.
		MapConfiguracaoGPP map = MapConfiguracaoGPP.getInstancia(); 
		
		// Deleta da tabela os usuarios processados com sucesso e que estão há mais de n dias na tabela
		String sql_limpa = 	"DELETE FROM tbl_rel_solicitacoes_relatorio " +
							"WHERE dat_execucao < SYSDATE - ? " +
							"AND idt_status = ? ";
		
		Object parametros[] = {map.getMapValorConfiguracaoGPP("NUM_DIAS_EXPIRACAO_RELATORIO"),Definicoes.IDT_PROCESSAMENTO_OK};
		
		conexaoPrep.executaPreparedUpdate(sql_limpa,parametros, super.logId);
	}
	
	/**
	 * <P><b>Metodo....:</b> getDadosClob
	 * <P><b>Descricao.:</b> Converte o clob em string 
	 * @param relatorio		- Clob a ser convertido em string
	 * @return SolicitacaoAtivacao - Dados da solicitacao
	 */
	private String getDadosClob(Clob relatorio)
	{
		char chr_buffer[] = null;
		
		try
		{
			// Realiza a leitura do objeto CLOB
		    chr_buffer = new char[(int)relatorio.length()];
		    
		    Reader chr_instream = relatorio.getCharacterStream();
			chr_instream.read(chr_buffer);
		}
		catch(Exception se)
		{
			super.log(Definicoes.WARN,"Produtor.getDadosClob","Erro ao converter clob para string. Erro: " + se);
		}
		return new String(chr_buffer);
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
}
