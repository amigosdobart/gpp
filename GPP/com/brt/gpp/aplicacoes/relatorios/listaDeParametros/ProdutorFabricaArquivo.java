package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import java.io.File;

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.interfaceEscrita.InterfaceConfiguracao;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
import com.brt.gpp.comum.operacaoArquivo.ArquivoLeitura;
import com.brt.gpp.comum.operacaoArquivo.OperadorDeArquivo;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;

/**
*	Classe produtora da fábrica de relatórios.
*	<BR>Consome um arquivo e gera uma série de VOs para serem processados pelos consumidores.</BR>
 *	@author	Magno Batista Corrêa
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Vergne (saída e erro em pasta temporária)
 *  Data: 06/02/2007
 *
 *  Atualizado por Bernardo Vergne (compactacao de saida)
 *  Data: 26/09/2007
 *  
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descrição: Reestruturação completa, vários fixes
 *  Data: 15/10/2007
 */
public class ProdutorFabricaArquivo extends ProdutorFabrica implements ProcessoBatchProdutor
{
    private String idRelatorioSaida;
    private InterfaceConfiguracao configArquivo;
    private String delimitadorArqEntrada;
    
	public ProdutorFabricaArquivo(long logId)
	{
		super(logId, Definicoes.CL_PRODUTOR_FABRICA);

		this.numRegistros	= 0;
		this.status			= Definicoes.PROCESSO_SUCESSO;
		this.mensagem		= "Numero de registros processados: ";
	}

    /**
     *
     *	@param		String[]	params	Lista de parametros.<br>
     *	[0]idRelatorio;
     *  [1]arquivoEntrada;
     *  [2]nomeArquivoSaida;
     *  [3]nomeArquivoErro;
     *  [4]pathSaida;
     *  [5]pathErro;
     *  [6]pathTemp;
     *  [7]delimitador;</br>
     *	@throws		Exception
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#startup(java.lang.String[])
     */
	public void startup(String[] params) throws Exception
	{
		super.log(Definicoes.INFO, "Produtor.startup", "Inicio");
		this.idRelatorioSaida			= params[0];
		String pathArquivoEntrada 		= params[1];
		String pathArquivoSaidaTemp 	= params[6] + params[2];
		String pathArquivoErroTemp 		= params[6] + params[3];
		this.nomeArquivoSaida 			= params[2];
		this.nomeArquivoErro 			= params[3];
		this.pathSaida 					= params[4];
		this.pathErro 					= params[5];
		this.delimitadorArqEntrada		= params[7];

		try
		{
			this.conexaoPrep = this.gerenteBancoDados.getConexaoPREP(super.logId);
			super.log(Definicoes.INFO, "ProdutorFabricaArquivo.startup", "Processando : " + pathArquivoEntrada);

		    this.relatorio		= geraRelatorio(this.idRelatorioSaida);
			this.configArquivo  = this.relatorio.getFormatoArquivo();
			this.configArquivo.setAtributo("path", pathArquivoSaidaTemp);

			this.arquivoEntrada		= new ArquivoLeitura(pathArquivoEntrada);
			this.arquivoErroTemp	= new ArquivoEscrita(pathArquivoErroTemp);
		    this.arquivoErroTemp.escrever("<ROOT>");

		    this.arquivoSaidaTemp	= this.configArquivo.getInterfaceEscrita();

		    this.arquivoSaidaTemp.abrir();
		    this.configArquivo.setAtributo("observacao", "Processo executado com SUCESSO!");
		} 
		catch (Exception e)
		{
			this.status = Definicoes.PROCESSO_ERRO;
			this.mensagem = "Excecao: " + e;
			throw e;
		} 
		finally
		{
			super.log(Definicoes.INFO, "ProdutorFabricaArquivo.startup", "Fim");
		}
	}

    /**
	 * Retorna para as threads consumidoras o proximo registro a ser processado.
	 */
	public Object next() throws Exception
	{
		super.log(Definicoes.DEBUG, "Produtor.next", "Inicio");
		VOFabrica VO = null;
		try
		{
	    	String[] parametros = OperadorDeArquivo.leRegistroDelimitado(
	    			this.arquivoEntrada, this.delimitadorArqEntrada);
	    	
		    if(parametros != null)
		    {
			    VO = new VOFabrica(this.montarParametros(
				    		parametros, 
				    		this.relatorio.getCamposEntrada().length,
				    		this.relatorio.getCamposEntradaPosicoes()));
		        this.numRegistros++;
		    }
		}
		finally
		{
		    super.log(Definicoes.DEBUG, "Produtor.next", "Fim");
		    this.status = Definicoes.PROCESSO_ERRO;
		}
		return VO;
	}

    /**
     *	@see		com.brt.gpp.comum.produtorConsumidor.Produtor#finish()
     */
	public void finish() throws Exception
	{
		super.log(Definicoes.INFO, "Produtor.finish", "Inicio");
		try
		{
		    this.finalizacoes();
		    this.mensagem = this.mensagem.concat(String.valueOf(this.numRegistros));
		}
		catch(Exception e)
		{
		    super.log(Definicoes.ERRO, "Produtor.finish", "Excecao: " + e);
		    this.status = Definicoes.PROCESSO_ERRO;
		    this.mensagem = "Excecao: " + e;
		}
		finally
		{
			//Liberando a conexao
			this.gerenteBancoDados.liberaConexaoPREP(this.conexaoPrep, super.logId);
		    super.log(Definicoes.INFO, "Produtor.finish", "Fim");
		}
	}

	/**
     *	Realiza as finalizacoes necessarias dentro do finish.
     *	Nao acrescenta as informacoes de log nos devidos arquivos.
     *	Finaliza o arquivo de erro e caso ele não possua nenhuma entrada
     *	o exclui. Fecha os arquivos: <code>this.arquivoErro</code>, <code>this.arquivoEntrada</code>,
     *	<code>this.arquivoSaida</code>.
     */
	protected void finalizacoes() throws Exception
	{
	    this.arquivoErroTemp.escrever("</ROOT>");
		this.arquivoErroTemp.fechar();
		this.arquivoSaidaTemp.fechar();
		this.arquivoEntrada.fechar();

		removerArquivo(this.pathSaida + this.nomeArquivoSaida);

		if(!this.status.equals(Definicoes.PROCESSO_SUCESSO))
			this.configArquivo.setAtributo("observacao", "Processo executado com ERRO!");

		if (this.relatorio != null && this.relatorio.isArquivoCompactado())
		{
			try
			{
				OperadorDeArquivo.compactaArquivo((String)this.configArquivo.getAtributo("path"),this.pathSaida);
				removerArquivo((String)this.configArquivo.getAtributo("path"));
			}
			catch (Exception e)
			{
				OperadorDeArquivo.moverArquivo((String)this.configArquivo.getAtributo("path"),this.pathSaida);
			}
		}
		else
		{
			OperadorDeArquivo.moverArquivo((String)this.configArquivo.getAtributo("path"),this.pathSaida);
		}

		File fileErro = new File(this.arquivoErroTemp.getPath());

		//Exclui os arquivos de erro no caso de estarem vazios
		long bits = "<ROOT></ROOT>".getBytes().length;
		if (fileErro.length() == bits)
		{
			fileErro.delete();
		}
		else
		{
			removerArquivo(this.pathErro + this.nomeArquivoErro);
			OperadorDeArquivo.moverArquivo(this.arquivoErroTemp.getPath(),this.pathErro);
		}
	}

	public int getIdProcessoBatch()
	{
		return Definicoes.IND_FABRICA_RELATORIO;
	}

	public void handleException()
	{
	}
}