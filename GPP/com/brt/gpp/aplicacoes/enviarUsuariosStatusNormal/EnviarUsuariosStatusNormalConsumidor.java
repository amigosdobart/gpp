package com.brt.gpp.aplicacoes.enviarUsuariosStatusNormal;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe consumidora responsavel pela atualizacao de status de processamento na TBL_INT_STATUS_OUT
 *  dos assinantes que realizaram recarga e estao com idt_status_processamento = 'G'.
 *  
 *  @author     Jorge Abreu
 *  @since      01/10/2007 
 */
public class EnviarUsuariosStatusNormalConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{

    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    /**
     *  SQL de update dos assinantes que fizeram recarga e mudaram de status 1 p/ 2
     */
    private String sql_update = "UPDATE TBL_INT_STATUS_OUT tblInt	   "+				   				  
    							"   SET IDT_STATUS_PROCESSAMENTO = ?,  "+				   				   
                                "       DAT_PROCESSAMENTO = ?          "+  			   				  
                                " WHERE IDT_STATUS_PROCESSAMENTO = ?   "+				   				   
                                "   AND IDT_MSISDN = ?                 ";
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     */
	public EnviarUsuariosStatusNormalConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnviarUsuariosStatusNormalConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_NORMAL_PROD), 
		      Definicoes.CL_ENVIO_USUARIO_NORMAL_CONS);
		
	}

	//Implementacao de Consumidor.
	
    /**
     *	Inicializa o objeto.  
     *
     *	@throws		Exception
     */
	public void startup() throws Exception
	{
	}

    /**
     *	Inicializa o objeto. 
     *
     *	@param		Produtor				produtor					Produtor de registros para execucao.
     *	@throws		Exception
     */
	public void startup(Produtor produtor) throws Exception
	{
	}
	
    /**
     *	Executa o processo de atualizacao na TBL_INT_STATUS_OUT dos assinantes que efetuaram recarga.
     *
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
		EnviarUsuariosStatusNormalVO usuario = (EnviarUsuariosStatusNormalVO)obj;
		
		try
		{
			
			Object param1[] = {
								Definicoes.S_NORMAL, 						// Parametro 1
								usuario.getDatProcessamento(),     			// Parametro 2
								Definicoes.IND_LINHA_DISPONIBILIZADA, 		// Parametro 3
								usuario.getIdtMsisdn(),                     // Parametro 4
							  };
				  

			conexaoPrep.executaPreparedUpdate(sql_update, param1, super.logId);
		    
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Erro ao atualizar assinante "+usuario.getIdtMsisdn()+". " + e);
		}
	}

    /**
     *	Finaliza a execucao do processo batch. 
     */
	public void finish()
	{
	}

	//Implementacao de ProcessoBatchConsumidor.
	
    /**
     *	Inicializa o objeto. Obtem a conexao com o banco de dados fornecida pelo produtor. 
     *
     *	@param		produtor				Produtor de registros para consumo.
     *	@throws		Exception
     */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
	    this.conexaoPrep = produtor.getConexao();
	}
	
}