package br.com.brasiltelecom.ppp.portal.relatorio.resultado;

//Imports Java
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//Imports WPP
import br.com.brasiltelecom.ppp.portal.relatorio.RelatorioException;
import br.com.brasiltelecom.ppp.portal.relatorio.consulta.ResultSet;

/**
 *	Formatador de campos obtidos a partir do result set da consulta na fonte de dados
 * 
 *	@author		Daniel Ferreira
 *	@since		24/06/2005
 */
public class FormatadorCampo  
{

	/**
	 *	Retorna o valor do campo formatado de acordo com o tipo de dados e a mascara informados por parametro
	 * 
	 *	@param		ResultSet	dados			Objeto contendo o resultado da pesquisa SQL. OBS: O cursor do 
	 *											result set nao e movido.
	 *	@param		String		name			Nome do campo do result set a ser lido
	 *	@param		String		type			Tipo de dados para formatacao do valor a ser obtido do result set.			
	 *											Os tipos de dados possiveis sao os listados abaixo:
	 *											BOOLEAN:		Formato booleano.
	 *								 			CHAR:			Formato caractere.
	 *								 			DATE:			Formato de data.
	 *											DOUBLE:			Formato de ponto flutuante de precisao dupla.
	 *											FLOAT:			Formato de ponto flutuante.
	 *											INTEGER (INT):	Formato inteiro.
	 *											LONG:			Formato inteiro longo.
	 *											STRING:			Formato string (sequencia de caracteres).
	 *											TIMESTAMP:		Formato de data com informacoes de hora.
	 *								 			Se o tipo de dados for omitido ou for invalido, sera assumido 
	 *											como do tipo String.
	 *	@param		String		mask			Mascara a ser aplicada ao valor. As mascaras dependem do tipo de dados
	 *											e podem assumir os valores abaixo:
	 *											BOOLEAN:		XXXXX/XXXXX, onde a primeira string antes de '/' 
	 *															corresponde ao valor true. A segunda string 
	 *															corresponde ao valor false. 
	 *															Ex: TRUE/FALSE, YES/NO, SIM/NAO,
	 *																VERDADEIRO/FALSO, E NOIS/NAO E.
	 *								 			CHAR:			Nao possui mascara (sera ignorada).
	 *								 			DATE:			Mascara deve ser igual as aplicadas para a classe
	 *								 							SimpleDateFormat.
	 *											DOUBLE:			Mascara deve ser igual as aplicadas para a classe
	 *															DecimalFormat.
	 *											FLOAT:			Mascara deve ser igual as aplicadas para a classe
	 *															DecimalFormat.
	 *											INTEGER (INT):	Mascara deve ser igual as aplicadas para a classe
	 *															DecimalFormat.
	 *											LONG:			Mascara deve ser igual as aplicadas para a classe
	 *															DecimalFormat.
	 *											STRING:			Nao possui mascara (sera ignorada).
	 *											TIMESTAMP:		Mascara deve ser igual as aplicadas para a classe 
	 *															SimpleDateFormat.
	 *								 			Se a mascara for omitida, sera aplicada uma mascara default para 
	 *								 			cada tipo.
	 *	@return		String						Corpo (area de dados) do relatorio		
	 *	@throws		RelatorioException
	 */
	public static String formataCampo(ResultSet dados, String name, String type, String mask) 
		throws RelatorioException
	{	
		//Tipo de dados booleano
		if(type.equalsIgnoreCase("BOOLEAN"))
		{
			boolean value = dados.getBoolean(name);
			
			if(mask != null)
			{
				try
				{
					return (value) ? mask.split("/")[0] : mask.split("/")[1];
				}
				catch(NullPointerException e)
				{
					throw new RelatorioException("Mascara invalida para tipo 'BOOLEAN'");
				}
			}
			else
			{
				return String.valueOf(value);
			}
		}
		
		//Tipo de dados caractere 
		if(type.equalsIgnoreCase("CHAR"))
		{
			String value = dados.getString(name);
			
			if((value != null) && (value.length() > 0))
			{
				return String.valueOf(value.charAt(0));
			}
			else
			{
				return "";
			}
		}
		
		//Tipo de dados data
		if(type.equalsIgnoreCase("DATE"))
		{
			Date value = dados.getDate(name);
			SimpleDateFormat conversorData = null;
			
			if(value != null)
			{
				if(mask != null)
				{
					try
					{
						conversorData = new SimpleDateFormat(mask);
					}
					catch(IllegalArgumentException e)
					{
						throw new RelatorioException("Mascara invalida para tipo 'DATE'");
					}
				}
				else
				{
					conversorData = new SimpleDateFormat();
				}
			
				return conversorData.format(value);
			}
			else
			{
				return "";
			}
		}
		
		//Tipo de dados double
		if(type.equalsIgnoreCase("DOUBLE"))
		{
			double value = dados.getDouble(name);
			DecimalFormat conversorValor = null;
			
			if(mask != null)
			{
				try
				{
					conversorValor = new DecimalFormat(mask);
				}
				catch(IllegalArgumentException e)
				{
					throw new RelatorioException("Mascara invalida para tipo 'DOUBLE'");
				}
			}
			else
			{
				conversorValor = new DecimalFormat();
			}
			
			return conversorValor.format(value);
		}
		
		//Tipo de dados float
		if(type.equalsIgnoreCase("FLOAT"))
		{
			float value = dados.getFloat(name);
			DecimalFormat conversorValor = null;
			
			if(mask != null)
			{
				try
				{
					conversorValor = new DecimalFormat(mask);
				}
				catch(IllegalArgumentException e)
				{
					throw new RelatorioException("Mascara invalida para tipo 'FLOAT'");
				}
			}
			else
			{
				conversorValor = new DecimalFormat();
			}
			
			return conversorValor.format(value);
		}
		
		//Tipo de dados inteiro
		if(type.equalsIgnoreCase("INTEGER") || (type.equalsIgnoreCase("INT")))
		{
			int value = dados.getInt(name);
			DecimalFormat conversorValor = null;
			
			if(mask != null)
			{
				try
				{
					conversorValor = new DecimalFormat(mask);
				}
				catch(IllegalArgumentException e)
				{
					throw new RelatorioException("Mascara invalida para tipo 'INTEGER'");
				}
			}
			else
			{
				conversorValor = new DecimalFormat();
			}
			
			return conversorValor.format(value);
		}
		
		//Tipo de dados long
		if(type.equalsIgnoreCase("LONG"))
		{
			long value = dados.getLong(name);
			DecimalFormat conversorValor = null;
			
			if(mask != null)
			{
				try
				{
					conversorValor = new DecimalFormat(mask);
				}
				catch(IllegalArgumentException e)
				{
					throw new RelatorioException("Mascara invalida para tipo 'LONG'");
				}
			}
			else
			{
				conversorValor = new DecimalFormat();
			}
			
			return conversorValor.format(value);
		}
		
		//Tipo de dados String
		if(type.equalsIgnoreCase("STRING"))
		{
			String value = dados.getString(name);
			
			if(value != null)
			{
				return value;
			}
			else
			{
				return "";
			}
		}
		
		//Tipo de dados timestamp
		if(type.equalsIgnoreCase("TIMESTAMP"))
		{
			Timestamp value = dados.getTimestamp(name);
			SimpleDateFormat conversorData = null;

			if(value != null)
			{
				if(mask != null)
				{
					try
					{
						conversorData = new SimpleDateFormat(mask);
					}
					catch(IllegalArgumentException e)
					{
						throw new RelatorioException("Mascara invalida para tipo 'TIMESTAMP'");
					}
				}
				else
				{
					conversorData = new SimpleDateFormat();
				}
			
				return conversorData.format(value);
			}
			else
			{
				return "";
			}
		}
		
		String value = dados.getString(name);
		
		if(value != null)
		{
			return value;
		}
		else
		{
			return "";
		}
	}

}
