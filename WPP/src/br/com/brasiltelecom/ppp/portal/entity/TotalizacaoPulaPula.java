/*
 * Created on 18/04/2005
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author	Daniel Ferreira
 * @since	18/04/2005
 *
 */
public class TotalizacaoPulaPula 
{
	
		private String idtMsisdn;
		private String datMes;
		private Double minCredito;
		private Double minFF;
				
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
		
}
