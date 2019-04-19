/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

/**
 * @author	Daniel Ferreira
 * @since	21/02/2005
 *
 */
public class PromocaoDiaExecucao 
{
	
		private Integer idtPromocao;
		private Integer numDiaEntrada;
		private Integer numDiaExecucao;
		private Integer numDiaExecucaoRecarga;
		
		/**
		 * @return
		 */
		public Integer getIdtPromocao() 
		{
			return idtPromocao;
		}

		/**
		 * @return
		 */
		public Integer getNumDiaEntrada() 
		{
			return numDiaEntrada;
		}

		/**
		 * @return
		 */
		public Integer getNumDiaExecucao() 
		{
			return numDiaExecucao;
		}

		/**
		 * @return
		 */
		public Integer getNumDiaExecucaoRecarga() 
		{
			return numDiaExecucaoRecarga;
		}

		/**
		 * @param 
		 */
		public void setIdtPromocao(Integer idtPromocao) 
		{
			this.idtPromocao = idtPromocao;
		}

		/**
		 * @param 
		 */
		public void setNumDiaEntrada(Integer numDiaEntrada)
		{
			this.numDiaEntrada = numDiaEntrada;
		}

		/**
		 * @param 
		 */
		public void setNumDiaExecucao(Integer numDiaExecucao)
		{
			this.numDiaExecucao = numDiaExecucao;
		}

		/**
		 * @param 
		 */
		public void setNumDiaExecucaoRecarga(Integer numDiaExecucaoRecarga)
		{
			this.numDiaExecucaoRecarga = numDiaExecucaoRecarga;
		}

}
