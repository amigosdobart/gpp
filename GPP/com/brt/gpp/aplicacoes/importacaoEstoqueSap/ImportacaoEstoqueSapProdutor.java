package com.brt.gpp.aplicacoes.importacaoEstoqueSap;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * Este arquivo contem a definicao da classe de ImportacaoEstoqueSapProdutor.
 * Esta classe importa os dados inseridos pelo ETI na TBL_INT_ETI_IN, separa as informações
 * do campo DES_DADOS e os insere na TBL_SAP_ESTOQUE acrescentando a data atual.
 * 
 * <P> Versao:	1.0
 *
 * @Autor:		Diego Luitgards
 * Data:		04/10/2006
 *
 */
public class ImportacaoEstoqueSapProdutor extends Aplicacoes implements ProcessoBatchProdutor
{
	// Parametros necessarios para o processo batch
	private String 		dataProcessamento;
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;

	// Recuros utilizados no produtor
	private ResultSet 	listaImei;
	private PREPConexao	conexaoPrep;

	/**
	 * Metodo....:ImportacaoEstoqueSapProdutor
	 * Descricao.:Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public ImportacaoEstoqueSapProdutor (long logId)
	{
		super(logId, Definicoes.CL_IMPORTACAO_ENTRADA_ETI);
	}
		
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String params[]) throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio DATA: "+dataProcessamento);
		
		// Busca uma conexao disponivel no pool de banco de dados e realiza a pesquisa dos
		// assinantes que entram no status DISCONNECTED. O resultSet é atualizado ficando 
		// disponivel para as threads consumidoras atraves do metodo next
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
		
		conexaoPrep.setAutoCommit( false );
		
		// Faz a pesquisa no banco para saber os dados da importacao de usuarios 
		String sql = "SELECT ID_SEQUENCIAL, DES_DADOS FROM TBL_INT_ETI_IN  " +
							  "WHERE IDT_STATUS_PROCESSAMENTO = ?" +
							  " AND IDT_EVENTO_NEGOCIO = ?";

		Object param[] = {	Definicoes.IDT_PROCESSAMENTO_NOT_OK
							,Definicoes.IDT_EVT_NEGOCIO_ESTOQUE_SAP };
		
		listaImei = conexaoPrep.executaPreparedQuery(sql, param, super.getIdLog());
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws GPPInternalErrorException
	{
		// Cria objeto para armazenar os dados
		DadosEstoqueSap dadosEstoqueSap = null;
		
		try
		{
			// Pega o proximo registro no resultSet e cria o VO que irá armazenar seus dados
			if (listaImei.next())
			{
				// O primeiro campo do resultSet informa o numero do assinante
				// O segundo o status do assinante
				// O terceiro o plano de preço
				// O quarto a data de processamento
				//idProcessamento = listaImei.getLong("ID_SEQUENCIAL");
				dadosEstoqueSap = this.parseDados(listaImei.getString("DES_DADOS"), listaImei.getLong("ID_SEQUENCIAL"));
				numRegistros++;
			}
		}
		catch(SQLException se)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro: " + se);
			throw new GPPInternalErrorException(se.toString());
		}
		return dadosEstoqueSap;
	}

	/**
	 * .Método que importa os dados do campo DES_DADOS da TBL_INT_ETI_IN que estão separados por ;
	 * e os grava em um objeto java (DadosEntradaEti) para serem inseridos na TBL_SAP_ESTOQUE.
	 * 
	 * @param dados
	 * @return DadosEntradaEti
	 */
	private DadosEstoqueSap parseDados(String dados, long idProcessamento )
	{
		// Cria objeto para armazenar os dados
		DadosEstoqueSap dadosEstoqueSap= null;
		
		try
		{
			// Pega os campos
			dadosEstoqueSap= new DadosEstoqueSap();
			dadosEstoqueSap.setDataImportacao(Calendar.getInstance().getTime());
			dadosEstoqueSap.setIdProcessamento(idProcessamento);
			dadosEstoqueSap.setImei(dados.substring(0,15));
			dadosEstoqueSap.setStatus(dados.substring(15,17));
			dadosEstoqueSap.setCodigoLoja(dados.substring(17));
			
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"parseDados","Erro de Formato dos Dados Mandados pelo SAP IND linha: " + idProcessamento +" "+ e);
			//throw new Exception(e);
		}
		
		return dadosEstoqueSap;
	}
	
	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch()
	{
		return Definicoes.IND_ESTOQUE_SAP;
	}

	/**
	 *  (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso()
	{
		if(getStatusProcesso().equals(Definicoes.PROCESSO_SUCESSO))
			return "Quantidade de Importacoes: " + numRegistros;
		return "Erro ao executar registro";
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
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status)
	{
		this.statusProcesso = status;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception
	{
		// Deleta todos os registros processados com sucesso 
		//limpaProcessoOk();
		// Fecha o ResultSet
		listaImei.close();
		
		// Grava no banco as atualizações
		conexaoPrep.commit();
		
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());

		super.log(Definicoes.DEBUG, "Produtor.finish", "Fim");
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return dataProcessamento;
	}

    /**
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
    
    /**
     * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
     */
    public void handleException()
    {
        try
        {
            conexaoPrep.rollback();
        }
        catch(SQLException sqlException)
        {  
        	super.log(Definicoes.ERRO, "handleException", "Erro ao executar o rollback");
        }
    }
}
