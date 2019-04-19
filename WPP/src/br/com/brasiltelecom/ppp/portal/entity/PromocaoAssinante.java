/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author Luciano Vilela
 *
 */
public class PromocaoAssinante 
{
		
		private String rowId;
		private int promocaoId;
		private String MSISDN;
		private Date dataExecucao;
		private Date dataEntradaPromocao;
		private Date inicioAnalise;
		private Date fimAnalise;
		private int indIsentoLimite;
		private Promocao promocao;
		
		/**
		 * @param lancamento
		 */
		public void setPromocao(Promocao promocao) {
			this.promocao = promocao;
		}
		
		/**
		 * @return
		 */
		public Promocao getPromocao() {
			return promocao;
		}
		
		
		/**
		 * @return Returns the rowId.
		 */
		public String getRowId() 
		{
			return rowId;
		}
		
		/**
		 * @param rowId The rowId to set.
		 */
		public void setRowId(String rowId) 
		{
			this.rowId = rowId;
		}
		
		/**
		 * @return
		 */
		public Date getDataEntradaPromocao() {
			return dataEntradaPromocao;
		}

		/**
		 * @return
		 */
		public Date getDataExecucao() {
			return dataExecucao;
		}

		/**
		 * @return
		 */
		public Date getFimAnalise() {
			return fimAnalise;
		}

		/**
		 * @return
		 */
		public Date getInicioAnalise() {
			return inicioAnalise;
		}

		/**
		 * @return
		 */
		public String getMSISDN() {
			return MSISDN;
		}

		public int getIndIsentoLimite()
		{
			return indIsentoLimite;
		}
		
		/**
		 * @param date
		 */
		public void setDataEntradaPromocao(Date date) {
			dataEntradaPromocao = date;
		}

		/**
		 * @param date
		 */
		public void setDataExecucao(Date date) {
			dataExecucao = date;
		}


		/**
		 * @param date
		 */
		public void setFimAnalise(Date date) {
			fimAnalise = date;
		}

		/**
		 * @param date
		 */
		public void setInicioAnalise(Date date) {
			inicioAnalise = date;
		}

		/**
		 * @param string
		 */
		public void setMSISDN(String string) {
			MSISDN = string;
		}

		/**
		 * @return
		 */
		public int getPromocaoId() {
			return promocaoId;
		}

		/**
		 * @param i
		 */
		public void setPromocaoId(int i) {
			promocaoId = i;
		}

		
		public void setIndIsentoLimite(int indIsentoLimite)
		{
			this.indIsentoLimite = indIsentoLimite;
		}

}
