package com.brt.gpp.aplicacoes.enviarUsuariosStatusNormal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;

/** 
* Produtor responsavel por verificar quais assinantes mudaram de status 1 p/ 2 e 
* quais assinantes efetuaram recarga para o intervalo de 1 dia.
* 
* @author:	Jorge Abreu
* @since:   01/10/2007
*/
public class EnviarUsuariosStatusNormalProdutor extends Aplicacoes implements ProcessoBatchProdutor 
{

	// Parametros necessarios para o processo batch
	private String 		aData;
	private java.sql.Timestamp data;
	private int			qtdAtivacoes;
	private String		statusProcesso = Definicoes.TIPO_OPER_SUCESSO;
	
	// Recuros utilizados no produtor
	private PREPConexao	conexaoPrep;
	
	// Conjunto de registros da consulta.
	private ResultSet registros;
	
	// SQL que realiza a pesquisa dos assinantes que entram no status Normal.
	String sql = "INSERT INTO TBL_INT_STATUS_OUT (DAT_PROCESSAMENTO, IDT_MSISDN, IDT_STATUS_ASSINANTE, IDT_STATUS_PROCESSAMENTO)" +
							
				  			"select ?, a.sub_id, ?, ? 														"+ //Parâmetros 1, 2 e 3
							"from 																			"+
							"tbl_apr_assinante_tecnomen a,													"+
				  			"tbl_apr_assinante_tecnomen b,													"+
				  			"tbl_ger_plano_preco c        													"+
				  			"where                        													"+
							"a.sub_id = b.sub_id and														"+
				  			"a.dat_importacao = ? - 1 and	                                    			"+ //Parâmetro 4
				  			"b.dat_importacao = ? and											  			"+ //Parâmetro 5
				  			"a.account_status = ? and											  			"+ //Parâmetro 6
				  			"b.account_status = ? and											  			"+ //Parâmetro 7
							"b.profile_id = c.idt_plano_preco and 								 			"+ //verfica se o cara é pre-pago
							"c.idt_categoria = ? and 											  			"+ //Parâmetro 8
							"not exists (select 1 from tbl_int_status_out where idt_msisdn = b.sub_id)      "; //Verifica se já não foi incluído na tbl_int_status_out
	
	
	//SQL p/ verificar quem fez recarga
	String sql_consulta = "SELECT /*+ index(b TISO_I1)*/              "+
                          "       a.idt_msisdn as IDT_MSISDN          "+
                          "  FROM tbl_rec_recargas a,                 "+
                          "       tbl_int_status_out b                "+
                          " WHERE b.idt_msisdn = a.idt_msisdn         "+
                          "   AND a.dat_inclusao >= ? - 1             "+
                          "   AND a.dat_inclusao < ?                  "+
                          "   AND a.id_tipo_recarga = ?               "+
                          "   AND b.idt_status_processamento = ?      ";
	
	/**
	 * Metodo....:EnvioUsuariosNormalProdutor
	 * Descricao.:Construtor da classe do processo batch
	 * @param logId - Id do processo
	 */
	public EnviarUsuariosStatusNormalProdutor(long logId) 
	{
		super(logId, Definicoes.CL_ENVIO_USUARIO_NORMAL);
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getIdProcessoBatch()
	 */
	public int getIdProcessoBatch() 
	{
		return Definicoes.IND_USUARIO_NORMAL;
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDescricaoProcesso()
	 */
	public String getDescricaoProcesso() 
	{
		if(Definicoes.TIPO_OPER_SUCESSO.equals(this.getStatusProcesso()))
	        return qtdAtivacoes + " Registros de Ativacoes";
	    else
	        return "Erro ao Procurar por Ativacoes";
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getStatusProcesso()
	 */
	public String getStatusProcesso() 
	{
		return statusProcesso;
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#setStatusProcesso(java.lang.String)
	 */
	public void setStatusProcesso(String status) 
	{
		this.statusProcesso = status;
	}

	/**
	 * Metodo....:parseParametros
	 * Descricao.:Este metodo realiza a verificacao de parametros
	 * @param params - Array de valores contendo os parametros para a execucao do processo
	 * @throws GPPInternalErrorException
	 */
	public void parseParametros(String params[]) throws Exception
	{

		if (params == null || params.length == 0 || params[0] == null)
		{
		    super.log(Definicoes.ERRO, "Produtor.parseParametros", "Parametro de data obrigatorio para o processo.");
		    setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException("Parametro de data obrigatorio para o processo.");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try
		{
			sdf.parse(params[0]);
			aData = params[0];
		}
		catch(ParseException pe)
		{
		    super.log(Definicoes.ERRO, "Produtor.parseParametros", "Data invalida ou esta no formato invalido.");
		    this.setStatusProcesso(Definicoes.TIPO_OPER_ERRO);
			throw new GPPInternalErrorException("Data invalida ou esta no formato invalido. Valor:" + params[0]);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
	 */
	public void startup(String[] params) throws Exception 
	{
		super.log(Definicoes.DEBUG, "Produtor.startup", "Inicio DATA: "+aData);
		
		try
		{
			// Executa a verificacao de parametros
			parseParametros(params);
			
			// Gera timestamp referente à data parametro
			data = GPPData.stringToTimestamp(aData);
			
			// Busca uma conexao disponivel no pool de banco de dados 
			conexaoPrep = GerentePoolBancoDados.getInstancia(super.getIdLog()).getConexaoPREP(super.getIdLog());
			
			conexaoPrep.setAutoCommit(false);
						
			Object param1[] = {	data, 										// Parametro 1
							    Definicoes.S_NORMAL, 						// Parametro 2
							    Definicoes.IND_LINHA_DISPONIBILIZADA, 		// Parametro 3
							    data,						     			// Parametro 4 
							    data,                       				// Parametro 5
							    new Short(Definicoes.FIRST_TIME_USER), 		// Parametro 6
							    new Short(Definicoes.NORMAL), 				// Parametro 7
							    new Integer(Definicoes.COD_CAT_PREPAGO)}; 	// Parametro 8
							  
			
			qtdAtivacoes = conexaoPrep.executaPreparedUpdate(sql, param1, super.logId);
			conexaoPrep.commit();
			super.log(Definicoes.INFO, "Produtor.startup", "Verificacao dos assinantes que passaram de status 1 para 2 realizada com sucesso. "+qtdAtivacoes+" assinantes alterados.");
			
			Object param2[] = { data,
					            data,
					            Definicoes.TIPO_RECARGA,
					            Definicoes.IND_LINHA_DISPONIBILIZADA};

			registros = conexaoPrep.executaPreparedQuery(sql_consulta, param2, super.logId);
			
			conexaoPrep.commit();	
			conexaoPrep.setAutoCommit(true);
			super.log(Definicoes.INFO, "Produtor.startup", "Verificacao dos assinantes que realizaram recarga em "+data+" realizada com sucesso. ");			
			
		}		
		catch (Exception e)
		{
			super.log(Definicoes.ERRO, "Produtor.startup", "Erro ao realizar verificacao dos assinantes que passaram do status 1 para 2 ou que fizeram recarga em "+data+" :"+ e);
			statusProcesso = Definicoes.TIPO_OPER_ERRO;
			throw new GPPInternalErrorException ("Excecao GPP:" + e.toString());				
		}
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#next()
	 */
	public Object next() throws Exception 
	{
		if(registros.next())
		{
			EnviarUsuariosStatusNormalVO obj = new EnviarUsuariosStatusNormalVO();
			
			obj.setDatProcessamento(data);
			obj.setIdtMsisdn(registros.getString("IDT_MSISDN"));	
			return obj;			
		}
		
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
	 */
	public void finish() throws Exception 
	{
		// Libera a conexao de banco de dados e fecha os ResultSets
			
		try
		{
		    if(this.registros != null)
		        this.registros.close();
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "finish", "Erro ao fechar o ResultSet. " + e);
		}
		finally
		{
			GerentePoolBancoDados.getInstancia(super.getIdLog()).liberaConexaoPREP(conexaoPrep,super.getIdLog());
		}
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
        	super.log(Definicoes.ERRO, "finish", "Erro ao realizar o Rollback. " + sqlException);
        }
    }

    /* (non-Javadoc)
     * @see com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor#getDataProcessamento()
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
}
