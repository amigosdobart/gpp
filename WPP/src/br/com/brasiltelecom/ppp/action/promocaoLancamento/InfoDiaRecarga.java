/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.action.promocaoLancamento;

/**
 * @author	Daniel Ferreira
 * @since	21/02/2005
 *
 */
public class InfoDiaRecarga 
{

		String msisdn;
		private String idtPromocao;
		private String diaRecarga;
		
		/**
		 * @return
		 */
		public String getMsisdn() 
		{
			return msisdn;
		}

		/**
		 * @return
		 */
		public String getIdtPromocao() 
		{
			return idtPromocao;
		}

		/**
		 * @return
		 */
		public String getDiaRecarga() 
		{
			return diaRecarga;
		}

		/**
		 * @param 
		 */
		public void setMsisdn(String msisdn) 
		{
			this.msisdn = msisdn;
		}

		/**
		 * @param 
		 */
		public void setIdtPromocao(String idtPromocao) 
		{
			this.idtPromocao = idtPromocao;
		}

		/**
		 * @param 
		 */
		public void setDiaRecarga(String diaRecarga)
		{
			this.diaRecarga = diaRecarga;
		}

}
