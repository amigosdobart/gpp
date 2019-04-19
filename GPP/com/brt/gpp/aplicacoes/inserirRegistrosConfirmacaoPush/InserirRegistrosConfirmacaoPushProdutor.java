package com.brt.gpp.aplicacoes.inserirRegistrosConfirmacaoPush;

import java.io.BufferedReader;
import java.io.FileReader;

import com.brt.gpp.aplicacoes.Aplicacoes;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/**
*
* Essa classe refere-se ao processo de inserção de dados 
* para o processo de envio de confirmacao de push do WIG 
* 
* @Autor: 			Geraldo Palmeira
* Data: 			01/03/2007
*/
public class InserirRegistrosConfirmacaoPushProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{
	// Parametros necessarios para o processo batch
	private int			numRegistros;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	private String      idPush = null;
	private String 		codigo = null;
	
	private BufferedReader	buffReader	= null;
	private FileReader		fReader		= null;
	private PREPConexao	conexaoPrep;
	
	/**
	 * @param aLogId
	 * @param aNomeClasse
	 */
	public InserirRegistrosConfirmacaoPushProdutor(long logId) 
	{
		super(logId, Definicoes.CL_INSERIR_REGISTROS_PUSH);
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch() 
	{
		return 0;
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso() 
	{
	    if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
			return numRegistros + " Registros inseridos na tabela WIGC_CONFIRMACAO_PUSH.";
	    else
	        return "Erro no Processo.";
	}

	/**
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
		statusProcesso = status;
	}
	
	public void incrementaRegistos()
	{
		numRegistros++;
	}

	public void parseParametros(String params[]) throws Exception
	{
		if (params == null || params.length == 0 || params[0] == null)
			throw new GPPInternalErrorException("Insira o ultimo codigo sequencial inserido na tabela!");

		idPush = params[1];
		codigo = params[2];
	}
	
	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception 
	{
		parseParametros(params);
		
		fReader		= new FileReader(params[0]);
		buffReader	= new BufferedReader(fReader);
		
		conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
	}

	/**
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception
	{
		InserirRegistrosConfirmacaoPushVO vo = null;
		try
		{
			String linha  = buffReader.readLine();
			String dados[]= null;
			
			if (linha != null)
			{
				dados = linha.split(";");
				
				vo = new InserirRegistrosConfirmacaoPushVO();
				vo.setMsisdn  (dados[0]); 
				vo.setNome    (dados[1]); 
				vo.setCpf  	  (dados[2]); 
				vo.setPromocao(dados[3]); 
				vo.setBonus	  (dados[4]); 
				vo.setIdPush  (idPush);
				vo.setCod     (codigo);
				
				codigo = incrementaCodigo(codigo); 
			}
		}
		catch(Exception e)
		{
			super.log(Definicoes.ERRO,"Produtor.next","Erro ao processar proximo registro no produtor. Erro"+e);
			throw new GPPInternalErrorException(e.toString());
		}
		return vo;
	}
	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception 
	{
		buffReader.close();
		fReader.close();
		// Libera a conexao de banco de dados
		GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#handleException()
	 */
	public void handleException() 
	{

	}
	
	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#getDataProcessamento()
	 */
	public String getDataProcessamento()
	{
		return null;
	}

    /* (non-Javadoc)
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getConexao()
     */
    public PREPConexao getConexao()
    {
        return conexaoPrep;
    }
    
    /**
	 * Metodo....:incrementaCodigo
	 * Descricao.:Este metodo gera um codigo sequencial para ser utilizado
	 *            pelo processo de envio de push
	 * @param  String codigo - O ultimo codigo inserido na tabela
	 * @return String
	 */
    public static String incrementaCodigo(String codigo)
	{
		// Variáveis para incrementar o codigo
		int[] digito = new int[4];
		int[] magnitude = {26,26,26,10};
		int[] offset = {'A','A','A','0'};
		String result = "";
		
		// Converte a string em inteiro 
		for (int k = 0; k < 4; k++)	
		{
			digito[k] = codigo.charAt(k) - offset[k];
		}
		
		// Calcula o proximo digito
		digito[3]++;
		for (int k = 3; k > 0; k--)
		{
			digito[k - 1] += digito[k] / magnitude[k];
			digito[k] = digito[k] % magnitude[k];
		}
		
		// Converte pra string novamente
		for (int k = 0; k < 4; k++)
		{
			digito[k] += offset[k];
			result += (char)digito[k];
		}
		
		return result;
	}
}
