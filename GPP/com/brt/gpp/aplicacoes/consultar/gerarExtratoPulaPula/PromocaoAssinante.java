package com.brt.gpp.aplicacoes.consultar.gerarExtratoPulaPula;

import java.util.Date;

/**
 * Classe que representa a entidade PromocaoAssinante da tabela TBL_GER_PROMOCAO_ASSINANTE
 * 
 * @author	Daniel Ferreira
 * @since	28/04/2005
 */
public class PromocaoAssinante 
{
	
		private Integer idtPromocao;
		private String idtMsisdn;
		private Date datExecucao;
		private Integer numMesExecucao;
		private Date datEntradaPromocao;
		private Date datSaidaPromocao;
		private Integer indSuspenso;
		private Date datInicioAnalise;
		private Date datFimAnalise;
		private Integer indIsentoLimite;
		
		/**
		 * Construtor
		 */
		public PromocaoAssinante()
		{
			idtPromocao = null;
			idtMsisdn = null;
			datExecucao = null;
			numMesExecucao = null;
			datEntradaPromocao = null;
			datSaidaPromocao = null;
			indSuspenso = null;
			datInicioAnalise = null;
			datFimAnalise = null;
			indIsentoLimite = null;
		}
		
		/**
		 * @return Returns the datEntradaPromocao.
		 */
		public Date getDatEntradaPromocao() 
		{
			return datEntradaPromocao;
		}
		
		/**
		 * @param datEntradaPromocao The datEntradaPromocao to set.
		 */
		public void setDatEntradaPromocao(Date datEntradaPromocao) 
		{
			this.datEntradaPromocao = datEntradaPromocao;
		}
		
		/**
		 * @return Returns the datExecucao.
		 */
		public Date getDatExecucao() 
		{
			return datExecucao;
		}
		
		/**
		 * @param datExecucao The datExecucao to set.
		 */
		public void setDatExecucao(Date datExecucao) 
		{
			this.datExecucao = datExecucao;
		}
		
		/**
		 * @return Returns the datFimAnalise.
		 */
		public Date getDatFimAnalise() 
		{
			return datFimAnalise;
		}
		
		/**
		 * @param datFimAnalise The datFimAnalise to set.
		 */
		public void setDatFimAnalise(Date datFimAnalise) 
		{
			this.datFimAnalise = datFimAnalise;
		}
		
		/**
		 * @return Returns the datInicioAnalise.
		 */
		public Date getDatInicioAnalise() 
		{
			return datInicioAnalise;
		}
		
		/**
		 * @param datInicioAnalise The datInicioAnalise to set.
		 */
		public void setDatInicioAnalise(Date datInicioAnalise) 
		{
			this.datInicioAnalise = datInicioAnalise;
		}
		
		/**
		 * @return Returns the datSaidaPromocao.
		 */
		public Date getDatSaidaPromocao() 
		{
			return datSaidaPromocao;
		}
		
		/**
		 * @param datSaidaPromocao The datSaidaPromocao to set.
		 */
		public void setDatSaidaPromocao(Date datSaidaPromocao) 
		{
			this.datSaidaPromocao = datSaidaPromocao;
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
		 * @return Returns the idtPromocao.
		 */
		public Integer getIdtPromocao() 
		{
			return idtPromocao;
		}
		
		/**
		 * @param idtPromocao The idtPromocao to set.
		 */
		public void setIdtPromocao(Integer idtPromocao) 
		{
			this.idtPromocao = idtPromocao;
		}
		
		/**
		 * @return Returns the indIsentoLimite.
		 */
		public Integer getIndIsentoLimite() 
		{
			return indIsentoLimite;
		}
		
		/**
		 * @param indIsentoLimite The indIsentoLimite to set.
		 */
		public void setIndIsentoLimite(Integer indIsentoLimite) 
		{
			this.indIsentoLimite = indIsentoLimite;
		}
		
		/**
		 * @return Returns the indSuspenso.
		 */
		public Integer getIndSuspenso() 
		{
			return indSuspenso;
		}
		
		/**
		 * @param indSuspenso The indSuspenso to set.
		 */
		public void setIndSuspenso(Integer indSuspenso) 
		{
			this.indSuspenso = indSuspenso;
		}
		
		/**
		 * @return Returns the numMesExecucao.
		 */
		public Integer getNumMesExecucao() 
		{
			return numMesExecucao;
		}
		
		/**
		 * @param numMesExecucao The numMesExecucao to set.
		 */
		public void setNumMesExecucao(Integer numMesExecucao) 
		{
			this.numMesExecucao = numMesExecucao;
		}
		
}
