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
public class BonusPulaPula 
{
	
		private Integer idCodigoNacional;
		private Double vlrBonusMinuto;
		private Double vlrBonusMinutoFF;
		
		/**
		 * @return Returns the idCodigoNacional.
		 */
		public Integer getIdCodigoNacional() 
		{
			return idCodigoNacional;
		}
		
		/**
		 * @param idCodigoNacional The idCodigoNacional to set.
		 */
		public void setIdCodigoNacional(Integer idCodigoNacional) 
		{
			this.idCodigoNacional = idCodigoNacional;
		}
		
		/**
		 * @return Returns the vlrBonusMinuto.
		 */
		public Double getVlrBonusMinuto() 
		{
			return vlrBonusMinuto;
		}
		
		/**
		 * @param vlrBonusMinuto The vlrBonusMinuto to set.
		 */
		public void setVlrBonusMinuto(Double vlrBonusMinuto) 
		{
			this.vlrBonusMinuto = vlrBonusMinuto;
		}
		
		/**
		 * @return Returns the vlrBonusMinutoFF.
		 */
		public Double getVlrBonusMinutoFF() 
		{
			return vlrBonusMinutoFF;
		}
		
		/**
		 * @param vlrBonusMinutoFF The vlrBonusMinutoFF to set.
		 */
		public void setVlrBonusMinutoFF(Double vlrBonusMinutoFF) 
		{
			this.vlrBonusMinutoFF = vlrBonusMinutoFF;
		}
				
}
