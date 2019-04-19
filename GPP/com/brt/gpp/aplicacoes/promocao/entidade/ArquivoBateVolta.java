package com.brt.gpp.aplicacoes.promocao.entidade;

//Imports Java.

import java.text.SimpleDateFormat;
import java.util.Date;

//Imports GPP.

import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.GPPData;
import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 *	Classe que contém os dados a ser inseridos em cada linha do arquivo Bate Volta 
 *	
 * 
 *	@author	Marcelo Alves Araujo
 *	@since	20/10/2006
 */
public class ArquivoBateVolta implements Entidade
{
	private String msisdn          = null;
	private long   duracao         = 0;
	private Date   data            = null;
	private long   contador		   = 0;
	private String mesReferencia   = null;
	private String numArquivo      = null;
	private String numRegistro     = null;
	private String ddd             = null;
	private String telefone        = null;
	private String dataServico     = null;
	private String duracaoHoras    = null;
	private String numChamado	   = null;
	private String cnlOrigem       = null;
	private String cnlDestino      = null;
	private String numOriginal     = null;
    private String tipoChamada     = null;
	
	private SimpleDateFormat	conversorData;
	
	//Constantes internas.
	
	public static final String EMPRESA				= "000";
	public static final String TIPO_ARQUIVO			= "000";
	public static final String EMPRESA_ORIGEM		= "000";
	public static final String LOCALIDADE_FONE		= "    ";
	public static final String HISTORICO			= "                                                      ";
	public static final String VALOR_SERVICO		= "00000000,00000";
	public static final String HORA_SERVICO			= "00000000";
	public static final String DEGRAU				= "01";
	public static final String GRUPO_HORARIO		= "1";
	public static final String NATUREZA				= "00510";
	public static final String EOT_DESTINO			= "000";
	public static final String CNL_C				= "00000";
	public static final String NATUREZA_SERVICO		= "C";
	public static final String CODIGO_SERVICO		= "00000";
	public static final String CODIGO_ERRO			= "00000";
	public static final String TELECARD				= "00000000000";
	public static final String CONTRATO				= "00000000000";
	public static final String GRUPO_FAT			= "000";
	public static final String GRUPO_TRIB			= "0";
	public static final String VALOR_CONTA			= "0000000000000,00";
	public static final String CSP					= "014";
	public static final String SEPARADOR			= ";";
	public static final char   COMPLEMENTO_NUMERO	= '0';
	public static final char   COMPLEMENTO_TEXTO	= ' ';
	public static final String COMPLEMENTO_CONTROLE = "3;0";
    public static final String TIPO_CHAMADA_OFFNET  = "B";    
    public static final String TIPO_CHAMADA_ONNET   = "P";
	
	//Construtores.
	
	/**
	 * Construtor da classe
	 */
	public ArquivoBateVolta()
	{
		msisdn        = null;
		duracao       = 0;
		data		  = null;
		mesReferencia = null;
		numArquivo    = null;
		numRegistro   = null;
		ddd           = null;
		telefone      = null;
		dataServico   = null;
		duracaoHoras  = null;
		numChamado	  = null;
		cnlOrigem     = null;
		cnlDestino    = null;
		numOriginal   = null;
		
		conversorData = new SimpleDateFormat(Definicoes.MASCARA_DAT_DIA);
	}	

	
	// Implentacao de Entidade.
	// Getters
	
	/**
	 * @return cnlDestino
	 */
	public String getCnlDestino()
    {
    	return cnlDestino;
    }

	/**
	 * @return cnlOrigem
	 */
	public String getCnlOrigem()
    {
    	return cnlOrigem;
    }


	/**
	 * @return data Data de geração do arquivo
	 */
	public Date getData()
    {
    	return data;
    }


	/**
	 * @return dataServico Data de geração do arquivo
	 */
	public String getDataServico()
    {
    	return dataServico;
    }


	/**
	 * @return ddd
	 */
	public String getDdd()
    {
    	return ddd;
    }


	/**
	 * @return duracao Duração total das chamadas
	 */
	public long getDuracao()
    {
    	return duracao;
    }

	/**
	 * @return duracaoHoras Duração total das chamadas em hhhmmss
	 */
	public String getDuracaoHoras()
    {
    	return duracaoHoras;
    }

	/**
	 * @return mesReferencia Mês passado como referência
	 */
	public String getMesReferencia()
    {
    	return mesReferencia;
    }

	/**
	 * @return msisdn
	 */
	public String getMsisdn()
    {
    	return msisdn;
    }

	/**
	 * @return numArquivo Identificador do arquivo
	 */
	public String getNumArquivo()
    {
    	return numArquivo;
    }

	/**
	 * @return numOriginal
	 */
	public String getNumOriginal()
    {
    	return numOriginal;
    }

	/**
	 * @return numRegistro Sequencial de registros do arquivo formatado
	 */
	public String getNumRegistro()
    {
    	return numRegistro;
    }

	/**
	 * @return telefone
	 */
	public String getTelefone()
    {
    	return telefone;
    }
	
	/**
	 * @return contador Sequencial de registros do arquivo
	 */
	public long getContador()
    {
    	return contador;
    }
	
	/**
	 * @return numChamado Número do telefone formatado
	 */
	public String getNumChamado()
    {
    	return numChamado;
    }


	// Setters
	/**
	 * @param cnlDestino
	 */
	public void setCnlDestino(String cnlDestino)
    {
    	this.cnlDestino = cnlDestino;
    }

	/**
	 * @param cnlOrigem
	 */
	public void setCnlOrigem(String cnlOrigem)
    {
    	this.cnlOrigem = cnlOrigem;
    }

	/**
	 * @param data Data de geração do arquivo
	 */
	public void setData(Date data)
    {
    	this.data = data;
    }

	/**
	 * @param dataServico Data de geração do arquivo
	 */
	public void setDataServico(String dataServico)
    {
    	this.dataServico = dataServico;
    }

	/**
	 * @param ddd
	 */
	public void setDdd(String ddd)
    {
    	this.ddd = ddd;
    }

	/**
	 * @param duracaoHoras Duração total das chamadas
	 */
	public void setDuracao(long duracao)
    {
    	this.duracao = duracao;
    }

	/**
	 * @param duracaoHoras Duração total das chamadas em hhhmmss
	 */
	public void setDuracaoHoras(String duracaoHoras)
    {
    	this.duracaoHoras = duracaoHoras;
    }

	/**
	 * @param mesReferencia Mês passado como referência
	 */
	public void setMesReferencia(String mesReferencia)
    {
    	this.mesReferencia = mesReferencia;
    }

	/**
	 * @param msisdn
	 */
	public void setMsisdn(String msisdn)
    {
    	this.msisdn = msisdn;
    }

	/**
	 * @param numArquivo Identificador do arquivo
	 */
	public void setNumArquivo(String numArquivo)
    {
    	this.numArquivo = numArquivo;
    }

	/**
	 * @param numOriginal
	 */
	public void setNumOriginal(String numOriginal)
    {
    	this.numOriginal = numOriginal;
    }

	/**
	 * @param numRegistro Sequencial de registros do arquivo formatado
	 */
	public void setNumRegistro(String numRegistro)
    {
    	this.numRegistro = numRegistro;
    }

	/**
	 * @param telefone
	 */
	public void setTelefone(String telefone)
    {
    	this.telefone = telefone;
    }
	
	/**
	 * @param contador Sequencial de registros do arquivo
	 */
	public void setContador(long contador)
    {
    	this.contador = contador;
    }
	
	/**
	 * @param numChamado Número do telefone formatado
	 */
	public void setNumChamado(String numChamado)
    {
    	this.numChamado = numChamado;
    }
	
	public String getTipoChamada()
    {
        return tipoChamada;
    }


    public void setTipoChamada(String tipoChamada)
    {
        this.tipoChamada = tipoChamada;
    }

    /**
	 * Configura todos os parâmetros do registro
	 */
	public void setParametros()
    {
		numArquivo    = alinharString(mesReferencia, true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 7);
		numRegistro   = alinharString(String.valueOf(contador), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 7);
		ddd           = alinharString(msisdn.substring(2, 4), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 4);
		telefone      = alinharString(msisdn.substring(4), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 10);
		dataServico   = conversorData.format(data);
		duracaoHoras  = alinharString(GPPData.segundosParaHorasFormatado(duracao), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 7);
		numChamado    = alinharString(msisdn.substring(2), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 17);
		cnlOrigem     = alinharString(msisdn.substring(2, 4), false, ArquivoBateVolta.COMPLEMENTO_NUMERO, 5);
		cnlDestino    = alinharString(msisdn.substring(2, 4), false, ArquivoBateVolta.COMPLEMENTO_NUMERO, 5);
		numOriginal   = alinharString(msisdn.substring(2), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 11);
    }

	

	/**
	 *	Retorna uma copia do objeto.
	 * 
	 *	@return		Object					result						Copia do objeto.
	 */
	public Object clone()
	{
		ArquivoBateVolta result = new ArquivoBateVolta();	
		
		result.setCnlDestino(cnlDestino);
		result.setCnlOrigem(cnlOrigem);
		result.setContador(contador);
		result.setData(data);
		result.setDataServico(dataServico);
		result.setDdd(ddd);
		result.setDuracao(duracao);
		result.setDuracaoHoras(duracaoHoras);
		result.setMesReferencia(mesReferencia);
		result.setMsisdn(msisdn);
		result.setNumArquivo(numArquivo);
		result.setNumOriginal(numOriginal);
		result.setNumRegistro(numRegistro);
		result.setTelefone(telefone);
		
		return result;
	}
	
	/**
	 *	Verifica se o objeto atual corresponde ao passado por parametro.
	 * 
	 *	@param		Object					object						Objeto a ser comparado com o atual.
	 *	@return		boolean												True se for igual e false se for diferente.
	 */
	public boolean equals(Object object)
	{
		if(object == null)
		{
			return false;
		}
		
		if(!(object instanceof ArquivoBateVolta))
		{
			return false;
		}
		
		if(this.hashCode() != ((ArquivoBateVolta)object).hashCode())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 *	Retorna o hash do objeto.
	 * 
	 *	@return		int													Hash do objeto.
	 */
	public int hashCode()
	{
		StringBuffer result = new StringBuffer();
		
		result.append((msisdn != null) ? msisdn : "NULL");
		result.append("||");
		result.append((mesReferencia != null) ? mesReferencia : "NULL");
		
		return result.toString().hashCode();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto, no formato a ser inserido no arquivo
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString()
	{
		StringBuffer result = new StringBuffer();
	
		result.append(ArquivoBateVolta.EMPRESA);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.TIPO_ARQUIVO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(mesReferencia);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(numArquivo);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(numRegistro);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.EMPRESA_ORIGEM);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.LOCALIDADE_FONE);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ddd);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(telefone);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(dataServico);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.HISTORICO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.VALOR_SERVICO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.HORA_SERVICO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(duracaoHoras);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.DEGRAU);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.GRUPO_HORARIO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.NATUREZA);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.EOT_DESTINO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(numChamado);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(cnlOrigem);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(cnlDestino);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.CNL_C);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.NATUREZA_SERVICO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(tipoChamada);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.CODIGO_SERVICO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(numOriginal);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.CODIGO_ERRO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.TELECARD);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.CONTRATO);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.GRUPO_FAT);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.GRUPO_TRIB);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.VALOR_CONTA);
		result.append(ArquivoBateVolta.SEPARADOR);
		result.append(ArquivoBateVolta.CSP);
		
		return result.toString();
	}
	
	/**
	 *	Retorna uma representacao em formato String do objeto, no formato a ser inserido no arquivo
	 * 
	 *	@return		String												Representacao em formato String do objeto.
	 */
	public String toString(String nomeArquivo, long tamanhoArquivo)
	{
		String registroControle = alinharString(nomeArquivo, false, ArquivoBateVolta.COMPLEMENTO_TEXTO, 47) 
								+ ArquivoBateVolta.SEPARADOR 
								+ alinharString(Long.toString(tamanhoArquivo), true, ArquivoBateVolta.COMPLEMENTO_NUMERO, 17) 
								+ ArquivoBateVolta.SEPARADOR 
								+ ArquivoBateVolta.COMPLEMENTO_CONTROLE;
		
		return registroControle;		
	}
	
	//Outros metodos.
    /**
	 * Alinha uma String de acordo com os parâmetros
	 * @param  original					- String original
	 * @param  esquerda					- true : Preencher à esquerda
	 * 									- false: Preencher à direita
	 * @param  caracterPreenchimento	- Caracter com que será preenchida a String resultante
	 * @param  tamanhoString			- Número de caracteres da String resultante
	 * @return resultante				- Resultado do alinhamento
	 */
	public String alinharString(String original, boolean esquerda, char caracterPreenchimento, int tamanhoString)
	{
	    String resultante = original;
	    
	    // Se alinhamento for true, completa a string à esquerda
	    if(esquerda)
	        while(resultante.length() < tamanhoString)
	            resultante = caracterPreenchimento + resultante;
	    // Se alinhamento for false, completa a string à direita
	    else
	        while(resultante.length() < tamanhoString)
	            resultante = resultante + caracterPreenchimento;
	    return resultante;
	}
}
