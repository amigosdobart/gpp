/*
 * Created on 18/04/2005
 *
 */
package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author	Daniel Ferreira
 * @since	18/04/2005
 *
 * Alteracao: 05/05/2005 por Joao Carlos
 * 			  implementacao dos metodos equals,hashCode e toString e alteracao
 *            na criacao de metodos para adicionar um valor a variavel representando o valor acumulado
 * 
 */
public class TotalizacaoPulaPula implements Cloneable
{
	
		private String idtMsisdn;
		private String datMes;
		private Double minCredito;
		private Double minFF;
		private Double minTarifaReduzida;
		private SimpleDateFormat formatadorDatMes = new SimpleDateFormat("yyyyMM");
			
		public TotalizacaoPulaPula()
		{
			idtMsisdn = null;
			datMes = null;
			minCredito = new Double(0);
			minFF = new Double(0);
			minTarifaReduzida = new Double(0);
		}
		
		/**
		 * @return Returns the datMes.
		 */
		public String getDatMes() 
		{
			return datMes;
		}
		
		/**
		 * @param datMes The datMes to set.
		 */
		public void setDatMes(String datMes) 
		{
			this.datMes = datMes;
		}
		
		public void setDatMes(Date datMes)
		{
			this.datMes = formatadorDatMes.format(datMes);
		}

		/**
		 * @return Returns the idtMsisdn.
		 */
		public String getIdtMsisdn() 
		{
			return idtMsisdn;
		}
		
		/**
		 * @param idtMsisdn The idtMsisdn to set.
		 */
		public void setIdtMsisdn(String idtMsisdn) 
		{
			this.idtMsisdn = idtMsisdn;
		}
		
		/**
		 * @return Returns the minCredito.
		 */
		public Double getMinCredito() 
		{
			return minCredito;
		}
		
		/**
		 * @param minCredito The minCredito to set.
		 */
		public void setMinCredito(Double minCredito) 
		{
			this.minCredito = minCredito;
		}
		
		/**
		 * @return Returns the minFF.
		 */
		public Double getMinFF() 
		{
			return minFF;
		}
		
		/**
		 * @param minFF The minFF to set.
		 */
		public void setMinFF(Double minFF) 
		{
			this.minFF = minFF;
		}
		
		public Double getMinTarifaReduzida()
		{
			return minTarifaReduzida;
		}

		public void setMinTarifaReduzida(Double minTarifaReduzida)
		{
			this.minTarifaReduzida = minTarifaReduzida;
		}

		public void addMinutosPulaPula(double minCredito)
		{
			setMinCredito(new Double(getMinCredito().doubleValue()+minCredito));
		}

		public void addMinutosFriendsFamily(double minFF)
		{
			setMinFF(new Double(getMinFF().doubleValue()+minFF));
		}

		public void addMinutosTarifaReduzida(double minReduzida)
		{
			setMinTarifaReduzida(new Double(getMinTarifaReduzida().doubleValue()+ minReduzida));
		}
		
		public void limpaValores()
		{
			this.idtMsisdn 	= null;
			this.datMes 	= null;
			this.minCredito = new Double(0);
			this.minFF		= new Double(0);
			this.minTarifaReduzida = new Double(0);
		}

		/**
		 * Retorna uma copia do objeto.
		 */
		public Object clone() 
		{
			TotalizacaoPulaPula result = new TotalizacaoPulaPula();
			
			result.setIdtMsisdn(new String(this.idtMsisdn));
			result.setDatMes(new String(this.datMes));
			result.setMinCredito((this.minCredito != null) ? new Double(this.minCredito.doubleValue()) : null);
			result.setMinFF((this.minFF != null) ? new Double(this.minFF.doubleValue()) : null);
			
			return result;
		}
	
		public int hashCode()
		{
			return (idtMsisdn+datMes).hashCode();
		}
		
		public boolean equals(Object obj)
		{
			if ( !(obj instanceof TotalizacaoPulaPula) )
				return false;
			
			if ( ((TotalizacaoPulaPula)obj).getDatMes().equals(this.getDatMes()) &&
				 ((TotalizacaoPulaPula)obj).getIdtMsisdn().equals(this.getIdtMsisdn())
			   )
				return true;
			else return false;
		}
		
		public String toString()
		{
			return getIdtMsisdn() + " - " + getDatMes() + " - " + minCredito;
		}
}
