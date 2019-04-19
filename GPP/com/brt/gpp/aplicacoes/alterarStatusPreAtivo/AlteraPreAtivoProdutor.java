package com.brt.gpp.aplicacoes.alterarStatusPreAtivo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.GPPData;

/**
 *
 * Este arquivo refere-se a classe AlteraPreAtivoProdutor, responsável 
 * pela consulta dos assinantes que estão no status 1 há 91 dias sem 
 * gerar tráfego e os que estão no status 1 e realizaram tráfego não 
 * tarifado. O resultado dessa consulta é passado para o Consumidor
 * para trabalhar o resultado  
 *
 * @version				1.0
 * @author				Marcelo Alves Araujo
 * @since	 			10/11/2005
 *
 */
public class AlteraPreAtivoProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private int			numRegistrosNormal;
	private int			numRegistrosDesligado;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	// Recursos utilizados no produtor
	private ResultSet 	consultas;
	private PREPConexao	conexao;
	
	/**
	 * Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public AlteraPreAtivoProdutor (long logId)
	{		
		super(logId, Definicoes.CL_ALTERA_PREATIVO);
		
		numRegistrosNormal = 0;
		numRegistrosDesligado = 0;
	}
	
	/**
	 * Retorna as consultas a executar
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String params[]) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio");
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa das
		// consultas a serem realizadas. O resultSet é atualizado ficando disponivel
		// para as threads consumidoras atraves do metodo next
		conexao = super.gerenteBancoDados.getConexaoPREP(super.getIdLog());
		
		// Remove todos os registros desnecessários ao processo
		limparHotLine(conexao);
		
		// Query para consultar os assinantes 
		//em status 1 que estão com o hotline
		//bloqueado
		String sql =	"SELECT " +
						" ass.idt_msisdn AS msisdn " +
						",ass.idt_plano_preco AS plano " +
						",blo.dat_atualizacao AS dataAtualizacao " +
						"FROM " +
						" tbl_apr_assinante ass " +
						",tbl_apr_bloqueio_servico blo " +
						",tbl_ger_plano_preco gpp " +
						"WHERE " +
						" blo.id_servico = ? AND " +
						" blo.idt_msisdn = ass.idt_msisdn AND " +
						" ass.idt_status = ? AND " +
						" ass.idt_plano_preco = gpp.idt_plano_preco AND " +
						" gpp.idt_categoria = ?";
		
		Object parametros[] = {	Definicoes.SERVICO_BLOQ_HOTLINE,
        						new Integer(Definicoes.FIRST_TIME_USER),
        						new Integer(Definicoes.CATEGORIA_PREPAGO)};
		consultas = conexao.executaPreparedQuery(sql, parametros, super.getIdLog());
	}
	
	/**
	 * Retorna um objeto com os dados de uma linha do ResultSet
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next()
	{
		super.log(Definicoes.DEBUG, "Produtor.next", "Inicio");
		
		AlteraPreAtivoVO rel = null;
		
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que ira armazenar seus dados
			if (consultas.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				rel = new AlteraPreAtivoVO(consultas.getString("msisdn"),consultas.getInt("plano"),consultas.getDate("dataAtualizacao"));
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro: "+se);
		}
		return rel;
	}
	
	public void handleException()
	{
		// Não há tratamento de exceções
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_ALTERA_INATIVO;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		return "Foram executadas " + numRegistrosNormal + " mudancas para 2 e " + numRegistrosDesligado  + " mudancas para 5";
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
	 * Libera a conexão com o banco e limpa os registros com agendamento antigo
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.finish", "Inicio");
		
		// Libera a conexao de banco de dados
		consultas.close();
		
		super.gerenteBancoDados.liberaConexaoPREP(conexao,super.getIdLog());
	}
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento(java.lang.String)
	 */
	public String getDataProcessamento()
	{
		return GPPData.dataFormatada();
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexao;
    }
    
    /**
     * Incrementa o total de migrações de 1 para 2 
     */
    public void incrementarTotalNormal()
    {
    	numRegistrosNormal++;
    }
    
    
    /**
     * Incrementa o total de migrações de 1 para 5 
     */
    public void incrementarTotalDesligado()
    {
    	numRegistrosDesligado++;
    }    
    
    /**
     * Remove todos os registros de clientes que não estão no status 1
     * ou a data de ativação é posterior à retirada o hotline 
     * 
     * @param conexao
     */
    public void limparHotLine(PREPConexao conexao)
    {
    	String sql 	= "DELETE tbl_apr_bloqueio_servico a                           " 
			    	+ " WHERE id_servico = ?                                       " 
			    	+ "   AND EXISTS (SELECT 1                                     " 
			    	+ "                 FROM tbl_apr_assinante b                   " 
			    	+ "                WHERE a.idt_msisdn = b.idt_msisdn           " 
			    	+ "                  AND (b.idt_status <> ?                    " 
			    	+ "                   OR  a.dat_atualizacao < b.dat_ativacao)) ";

		
		Object parametros[] = {	Definicoes.SERVICO_BLOQ_HOTLINE,
        						new Integer(Definicoes.FIRST_TIME_USER)};
		try 
		{
			conexao.executaPreparedUpdate(sql, parametros, super.getIdLog());
		} 
		catch (GPPInternalErrorException e) 
		{
			super.log(Definicoes.ERRO,"limparHotLine","Erro ao remover registros. Erro: "+ e);
		}
    }
}
