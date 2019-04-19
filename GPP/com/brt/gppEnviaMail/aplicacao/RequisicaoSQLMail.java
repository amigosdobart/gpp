package com.brt.gppEnviaMail.aplicacao;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import com.brt.gppEnviaMail.GPPEnviaMailDefinicoes;
import com.brt.gppEnviaMail.conexoes.Configuracao;

public class RequisicaoSQLMail
{
	private String[] 	enderecosDestino;
	private String		assuntoMail;
	private String		mensagem;
	private boolean		compactarArquivos;
	private String		nomeArquivoCompactado;
	private Collection	listaComandosSQL;
	
	public RequisicaoSQLMail()
	{
		Configuracao conf 	  = Configuracao.getInstance();
		enderecosDestino  	  = conf.getPropriedade("com.brt.gppEnviaMail.enderecosDestino").split(",");
		assuntoMail		  	  = conf.getPropriedade("com.brt.gppEnviaMail.assuntoMensagem");
		compactarArquivos 	  = false;
		nomeArquivoCompactado = "Resultado-"+assuntoMail.hashCode()+GPPEnviaMailDefinicoes.EXTENSAO_ARQUIVO_COMPACTADO;
		listaComandosSQL 	  = new LinkedList();
	}

	public String getAssuntoMail()
	{
		return RequisicaoSQLMail.converteVariavelData(assuntoMail);
	}

	public String getMensagem()
	{
		return RequisicaoSQLMail.converteVariavelData(mensagem);
	}

	public String[] getEnderecosDestino()
	{
		return enderecosDestino;
	}

	public Collection getListaComandosSQL()
	{
		return listaComandosSQL;
	}

	public boolean compactarArquivos()
	{
		return compactarArquivos;
	}
	
	public String getNomeArquivoCompactado()
	{
		return RequisicaoSQLMail.converteVariavelData(nomeArquivoCompactado);
	}

	public void setAssuntoMail(String assuntoMail)
	{
		if (assuntoMail != null || !assuntoMail.equals(""))
			this.assuntoMail = assuntoMail;
	}

	public void setMensagem(String mensagem)
	{
		if (mensagem == null || mensagem.equals(""))
			this.mensagem = getAssuntoMail();

		this.mensagem = mensagem;
	}

	public void setEnderecosDestino(String[] enderecosDestino)
	{
		if (enderecosDestino != null || enderecosDestino.length > 0)
			this.enderecosDestino = enderecosDestino;
	}

	public void setCompactarArquivos(boolean compactar)
	{
		compactarArquivos = compactar;
	}

	public void setNomeArquivoCompactado(String nomeArquivo)
	{
		if (nomeArquivo != null || !nomeArquivo.equals(""))
			nomeArquivoCompactado = nomeArquivo;
	}

	public boolean addComandosSQL(Collection listaComandosSQL)
	{
		return this.listaComandosSQL.addAll(listaComandosSQL);
	}
	
	public boolean addComandoSQL(ComandoSQL comando)
	{
		return listaComandosSQL.add(comando);
	}
	
	public boolean haResultadoNaoAnexo()
	{
		boolean result = false;
		for (Iterator i = getListaComandosSQL().iterator(); i.hasNext();)
		{
			ComandoSQL comando = (ComandoSQL)i.next();
			if (!comando.enviarComoAnexo())
			{
				result=true;
				break;
			}
		}
		return result;
	}
	
	private static String getStringEntreTags(String origem, String tagIni, String tagFim)
	{
		String resultado = origem;
		int posIni = origem.indexOf(tagIni);
		if (posIni > -1)
		{
			int posFim = posIni +1 + origem.substring(posIni+1).indexOf(tagFim);
			resultado = origem.substring(posIni+1,posFim);
		}
		return resultado;
	}

	public static String converteVariavelData(String str)
	{
		String resultado=str;
		// Procura pela variavel na string desejada
		int posIni = str.indexOf(GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_VARIAVEL+
				                 GPPEnviaMailDefinicoes.VARIAVEL_DATA);
		// Caso tenha encontrado entao substitui pela data corrente no sistema
		// de acordo com o formato definido como parametro existente entre os 
		// caracters "(" e ")"
		if (posIni > -1)
		{
			// A posicao final da string sera o primeiro caracter separador da variavel
			// apos a primeira aparicao deste. Soma-se 2 para posicionar ao comeco da 
			// nova string
			int posFim 		= posIni+2 + str.substring(posIni+1).indexOf(GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_VARIAVEL);
			// Define as variaveis contendo os valores da variavel e posteriormente do valor
			// do parametro definido para esta
			getStringEntreTags(str, GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_VARIAVEL
					                                , GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_VARIAVEL);
			String paramVar = getStringEntreTags(str, GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_PARAM_INI
					                                , GPPEnviaMailDefinicoes.CARACTER_DEFINIDOR_PARAM_FIM);

			try
			{
				// Tenta realizar a formatacao da data corrente utilizando o parametro informado
				// caso nao consiga devido o formato estar invalido entao nada e modificado
				// caso tenha sucesso entao cria a nova string sendo que recursivamente chama
				// o proprio metodo para substituir todos os valores
				SimpleDateFormat sdf = new SimpleDateFormat(paramVar);
				resultado = str.substring(0,posIni);
				resultado += sdf.format(Calendar.getInstance().getTime());
				resultado += str.substring(posFim);
				
				// Chama novamente o metodo para substituir novas ocorrencias
				resultado = RequisicaoSQLMail.converteVariavelData(resultado);
			}
			catch(IllegalArgumentException e)
			{
				System.out.println("Formato da variavel data no XML nao e valido portanto nao sera considerada.");
			}
		}
		return resultado;
	}
}
