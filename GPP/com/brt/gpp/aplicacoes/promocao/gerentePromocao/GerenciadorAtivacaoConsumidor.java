package com.brt.gpp.aplicacoes.promocao.gerentePromocao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.brt.gpp.aplicacoes.promocao.controle.ControlePromocao;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Classe consumidora responsavel pela execucao do processo de cadastro de novos assinantes em promocoes disponiveis
 *	na data de execucao. O consumidor recebe registros do produtor, obtem as promocoes disponiveis e cadastra os
 *	assinantes nestas promocoes, caso sejam validados.
 *
 *	@author		Daniel Ferreira
 *	@since		07/06/2006
 */
public final class GerenciadorAtivacaoConsumidor extends ControlePromocao implements ProcessoBatchConsumidor
{

    //Atributos.
    
    /**
     *	Conexao com o banco de dados.
     */
    private PREPConexao conexaoPrep;
    
    /**
     *	Data de referencia para cadastro de assinantes em promocoes. Corresponde ao dia anterior a data de
     *	processamento do produtor.
     */
    private Date dataReferencia;
    
    //Construtores.
    
    /**
     *	Construtor da classe.
     */
	public GerenciadorAtivacaoConsumidor()
	{
		super(GerentePoolLog.getInstancia(GerenciadorPendenciaRecargaConsumidor.class).getIdProcesso(Definicoes.CL_PROMOCAO_GER_ATIV_PROD), 
		      Definicoes.CL_PROMOCAO_GER_ATIV_CONS);
		
		this.conexaoPrep	= null;
		this.dataReferencia	= null;
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
     *	Executa o processo de gerenciamento de promocoes de assinantes pendentes de primeira recarga.
     *
     *	@return		Object					obj							MSISDN do assinante a ser processado. Fornecido pelo produtor.
     *	@throws		Exception
     */
	public void execute(Object obj) throws Exception
	{
		super.log(Definicoes.DEBUG, "execute", "Inicio");

		try
		{
		    //Obtendo os parametros para a chamada do metodo de cadastro de assinantes nas promocoes vigentes.
		    String		msisdn				= (String)obj;
		    Timestamp	dataProcessamento	= new Timestamp(Calendar.getInstance().getTimeInMillis());
		    String		operador			= super.nomeClasse;
		    int			motivo				= Definicoes.CTRL_PROMOCAO_MOTIVO_ATIVACAO;
		    
		    //Cadastrando o assinante em promocoes.
		    super.inserePromocoesAssinante(msisdn, this.dataReferencia, dataProcessamento, operador, motivo, this.conexaoPrep);
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "execute", "Excecao: " + e);
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "execute", "Fim");
		}
	}

    /**
     *	Finaliza a execucao do processo batch. Nao utilizado pelo Estorno de Bonus Pula-Pula por Fraude.
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
	    
	    //Obtendo a data de referencia. Para calcula-la e necessario obter a data de processamento do produtor e
	    //diminuir um dia. Isto porque os assinantes mudam de status no dia anterior e sao processados no dia seguinte.
	    Calendar calReferencia = Calendar.getInstance();
	    calReferencia.setTime(new SimpleDateFormat(Definicoes.MASCARA_DATE).parse(produtor.getDataProcessamento()));
	    calReferencia.add(Calendar.DAY_OF_MONTH, -1);
	    this.dataReferencia	= calReferencia.getTime();
	}
	
}