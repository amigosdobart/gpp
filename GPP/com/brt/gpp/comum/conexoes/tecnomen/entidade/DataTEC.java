package com.brt.gpp.comum.conexoes.tecnomen.entidade;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.omg.CORBA.Any;

import TINC.TDateTime;
import TINC.TDateTimeHelper;

/**
 *	Conversor entre tipos de dados de data da Tecnomen e do Java.
 *
 *	@author		Daniel Ferreira
 *	@since		05/03/2007
 */
public class DataTEC 
{

	/**
	 *	Data encapsulada.
	 */
	private Date data;
	
	/**
	 *	Construtor da classe.
	 */
	public DataTEC()
	{
		this.data = null;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		data					Data encapsulada por classe Java.
	 */
	public DataTEC(Date data)
	{
		this();
		this.data = data;
	}
	
	/**
	 *	Construtor da classe.
	 *
	 *	@param		data					Data encapsulada por classe da Tecnomen.
	 */
	public DataTEC(TDateTime data)
	{
		this();
		this.data = this.extrairData(data);
	}
	
	/**
	 *	Extrai a data a partir do tipo de dados da Tecnomen.
	 *
	 *	@param		data					Data encapsulada por classe da Tecnomen.
	 *	@return		Data encapsulada por classe Java.
	 */
	private Date extrairData(TDateTime data)
	{
		if(data != null)
		{
			Calendar calData = Calendar.getInstance();
			
			calData.set(Calendar.YEAR        , data.year     );
			calData.set(Calendar.MONTH       , data.month - 1);
			calData.set(Calendar.DAY_OF_MONTH, data.day      );
			calData.set(Calendar.HOUR_OF_DAY , data.hour     );
			calData.set(Calendar.MINUTE      , data.minute   );
			calData.set(Calendar.SECOND      , data.second   );
			calData.set(Calendar.MILLISECOND , 0             );
			
			return calData.getTime(); 
		}
		
		return null;
	}
	
	/**
	 *	Extrai a data a partir do objeto Java.
	 *
	 *	@param		data					Data encapsulada por classe Java.
	 *	@return		Data encapsulada por classe da Tecnomen.
	 */
	private TDateTime extrairData(Date data)
	{
		if(data != null)
		{
			Calendar calData = Calendar.getInstance();
			calData.setTime(data);
			
			return new TDateTime((short)calData.get (Calendar.DAY_OF_MONTH),
					             (short)(calData.get(Calendar.MONTH) + 1  ),
								 (short)calData.get (Calendar.YEAR        ),
								 (short)calData.get (Calendar.MINUTE      ),
								 (short)calData.get (Calendar.SECOND      ),
								 (short)calData.get (Calendar.HOUR        ));
		}
		
		return null;
	}
	
	/**
	 *	Converte e retorna a data encapsulada por classe Java.
	 *
	 *	@return		Data encapsulada por classe Java.
	 */
	public Date toDate()
	{
		return this.data;
	}
	
	/**
	 *	Converte e retorna a data encapsulada por classe da Tecnomen.
	 *
	 *	@return		Data encapsulada por classe da Tecnomen.
	 */
	public TDateTime toTDateTime()
	{
		return this.extrairData(this.data);
	}
	
	/**
	 *	Insere a data no objeto Any CORBA.
	 *
	 *	@param		any						Objeto Any CORBA.
	 */
	public void inserir(Any any)
	{
		if(any != null)
			TDateTimeHelper.insert(any, this.toTDateTime());
	}
	
	/**
	 *	Extrai a data a partir do objeto Any CORBA.
	 *
	 *	@param		any						Objeto Any CORBA.
	 */
	public void extrair(Any any)
	{
		if(any != null)
			this.data = this.extrairData(TDateTimeHelper.extract(any));
	}
	
	/**
	 *	@see		java.lang.Object#toString()
	 */
	public String toString()
	{
		return (this.data != null) ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.data) : ""; 
	}
	
}
