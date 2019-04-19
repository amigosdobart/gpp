package com.brt.gpp.aplicacoes.relatorios.listaDeParametros;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.StringFormat;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Campo;
import com.brt.gpp.comum.fabricaDeRelatorio.entidade.Relatorio;
import com.brt.gpp.comum.interfaceEscrita.InterfaceEscrita;
import com.brt.gpp.comum.operacaoArquivo.ArquivoEscrita;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 *	Consumidor de F�brica
 *	Esta classe � respons�vel pelo consumo dos VOs passados pelo produtor
 *  e a gera��o das sa�das dos processamentos para os arquivos.
 *	@author	Magno Batista Corr�a
 *	@since	2006/07/10 (yyyy/mm/dd)
 *
 *  Atualizado por Bernardo Vergne e Leone Parise
 *  Descri��o: Reestrutura��o completa, v�rios fixes
 *  Data: 15/10/2007
 *
 *  Atualizado por Leone Parise
 *  Descri��o: Bug fixes nos formatadores e multi-thread
 *  Data: 21/20/2007
 */
public final class ConsumidorFabrica extends Aplicacoes implements ProcessoBatchConsumidor
{
	private PREPConexao					conexaoPrep;
	private Relatorio					relatorio;
	private InterfaceEscrita			arquivoSaida;
	private ArquivoEscrita				arquivoErro;
	private Format[]					formatadoresSaida;
	private Format[]					formatadoresEntrada;

	public ConsumidorFabrica()
	{
		super(GerentePoolLog.getInstancia(ConsumidorFabrica.class).getIdProcesso(Definicoes.CL_PRODUTOR_FABRICA),
		      Definicoes.CL_CONSUMIDOR_FABRICA);
		this.conexaoPrep = null;
	}

	/**
	 * Torna um array de objetos em um array de strings de acordo com uma dada mascara
	 */
	private String[] formatarObjeto(Format[] formatador, Object[] dados)
	{
		String[] saida = new String[formatador.length];

		for (int i = 0 ; i < formatador.length ; i ++)
		{
			 if(dados[i] != null)
			 {
				 if (formatador[i] == null)
					 saida[i] = dados[i].toString();
				 else
					 saida[i] = formatador[i].format(dados[i]);
			 }
			 else
				 saida[i] = "";
		}
		return saida;
	}

    /**
     *	Com base no VO, adquire os par�metros e executa a query definida em
     *	this.sql, a mesma contida no objeto relat�rio.
     *	Depois monta a linha de sa�da contendo os dados do registro processado
     *	e nos formatos determinados dentro do objeto relat�rio do produtor,
     *	escrevendo esta linha no arquivo de sa�da do produtor.
     *	Se ocorrer erro, escreve o registro com falha.
     *
     *	@return		obj		VO a ser processado. Fornecido pelo produtor.
     */
	public void execute(Object obj) throws Exception
	{
		Object[] valores = ((VOFabrica)obj).getParametros();

		int sizeEntrada			= relatorio.getCamposEntrada().length;
		int sizeSaida  			= relatorio.getCamposSaida().length;

		String[] linha = null;

		try
		{
			// Se o SQL � nulo, entao nao se deve realizar qualquer processamento no consumidor.
			// O dados recebidos do VO sao apenas formatados e gravados no arquivo de saida.
			if (ProdutorFabricaSQL.NULO.equals(relatorio.getIdRelatorio()))
			{
				if (relatorio.getFlagEscreveParametroSaida())
				{
					Object[] valoresEntrada  = new Object[sizeEntrada];
					Object[] valoresSaida = new Object[sizeSaida];
					String[] linhaEntrada = new String[sizeEntrada];
					String[] linhaSaida = new String[sizeSaida];

					System.arraycopy(valores, 0, valoresEntrada, 0, sizeEntrada);
					System.arraycopy(valores, sizeEntrada, valoresSaida, 0, sizeSaida);
					linhaEntrada = formatarObjeto(formatadoresEntrada, valoresEntrada);
					linhaSaida = formatarObjeto(formatadoresSaida, valoresSaida);

					linha = new String[sizeSaida + sizeEntrada];
					System.arraycopy(linhaEntrada, 0, linha, 0, sizeEntrada);
					System.arraycopy(linhaSaida, 0, linha, sizeEntrada, sizeSaida);
				}
				else
					linha = formatarObjeto(formatadoresSaida, valores);

				arquivoSaida.escrever(linha);
			}
			else
			{
				ResultSet registros		= conexaoPrep.executaPreparedQuery(relatorio.getSql(), valores, 0);
				Campo[] campoSaida		= relatorio.getCamposSaida();

				while (registros.next())
				{
					Object[] resultado = new Object[sizeSaida];
					for (int i = 0 ; i < sizeSaida ; i ++)
						resultado[i]= registros.getObject(campoSaida[i].getNomeInterno());

					// Formatando os parametros de entrada e de sa�da.
					// Tamb�m verifica o flagEscreveParametroSaida para decidir se escreve
					// ou n�o os par�metros de sa�da.
					String[] parametroMascaraFormatado = formatarObjeto(this.formatadoresEntrada, valores);
					String[] registroMascaraFormatado  = formatarObjeto (this.formatadoresSaida, resultado);

					if (relatorio.getFlagEscreveParametroSaida())
					{
						linha = new String[sizeEntrada + sizeSaida];
						System.arraycopy(parametroMascaraFormatado, 0, linha, 0, sizeEntrada);
						System.arraycopy(registroMascaraFormatado, 0, linha, sizeEntrada, sizeSaida);
					}
					else
						linha = registroMascaraFormatado;

					arquivoSaida.escrever(linha);
				}

				registros.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		    super.log(Definicoes.ERRO, "ConsumidorFabrica.execute", "Excecao: " + e);
		    this.arquivoErro.escrever(obj.toString()+"<erro>"+e+"</erro>\n");
		}
	}

    /**
     *	Inicializa o objeto, adquirindo informa��es do produtor.
     *	O <code>Relat�rio</code> � desnormalizado para que os dados de sql e m�scara do registro fiquem
     *	com um �nico e r�pido acesso.
      */
	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.conexaoPrep				= produtor.getConexao();
		ProdutorFabrica produtorFabrica = (ProdutorFabrica)produtor;
		this.arquivoSaida				= produtorFabrica.getArquivoSaida();
		this.arquivoErro				= produtorFabrica.getArquivoErro();
		this.relatorio					= produtorFabrica.getRelatorio();
		this.formatadoresSaida			= getFormatadores(this.relatorio.getCamposSaida());
		this.formatadoresEntrada		= getFormatadores(this.relatorio.getCamposEntrada());

	}

    /**
     *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor
     *  #startup(com.brt.gpp.comum.produtorConsumidor.Produtor)
     */
  	public void startup(Produtor produtor) throws Exception
	{

	}

  	/**
	 *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#startup()
	 */
	public void startup() throws Exception
	{
	}

	/**
 	 *	@see com.brt.gpp.comum.produtorConsumidor.Consumidor#finish()
	 */
	public void finish()
	{

	}
	/**
	 * Gera um array de formatadores de acordo com as informacoes contidas no array de campos
	 *
	 * @param campo	Array de campos
	 * @return		Array de formatadores
	 */
	private Format[] getFormatadores(Campo[] campo)
	{
		Format[] formatador = new Format[campo.length];

		for(int i = 0; i < campo.length; i++)
		{
			if(campo[i] == null || campo[i].getTipo() == null || campo[i].getMascaraFormato() == null)
			{
				formatador[i] = null;
				continue;
			}

			if(Campo.DATE.equals(campo[i].getTipo()))
			{
				formatador[i] = new SimpleDateFormat(campo[i].getMascaraFormato());
				continue;
			}

			if(Campo.STRING.equals(campo[i].getTipo()))
			{
				formatador[i] =  new StringFormat(campo[i].getMascaraFormato());
				continue;
			}

			if(Campo.NUMBER.equals(campo[i].getTipo()))
			{
				DecimalFormat df = new DecimalFormat(campo[i].getMascaraFormato());
				df.setGroupingUsed(false); //Retira o separador de milhar

				DecimalFormatSymbols dfs = new DecimalFormatSymbols();
				dfs.setDecimalSeparator('.'); //Seta o '.' como separador de decimal
				df.setDecimalFormatSymbols(dfs);

				formatador[i] = df;
				continue;
			}
		}
		return formatador;
	}
}
