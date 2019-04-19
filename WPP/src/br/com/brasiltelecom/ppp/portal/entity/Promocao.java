/*
 * Created on 02/08/2004
 *
 */
package br.com.brasiltelecom.ppp.portal.entity;

import java.util.Date;

/**
 * @author	Daniel Ferreira
 * @since	21/02/2005
 *
 */
public class Promocao 
{
	
		private Integer idtPromocao;
		private String nomPromocao;
		private String desPromocao;
		private Date datInicio;
		private Date datFim;
		//private Integer indHibrido;
		private Date datInicioValidade;
		private Date datFimValidade;
		//private String indTipo;
		//private Integer valPorMinuto;
		//private Integer indMesFechado;
		//private Integer valDias;
		private Double vlrBonus;
		private Double vlrMaxCreditoBonus;
		private Integer idtCategoria;
		private String tipEspelhamento;
		private Integer indLimiteDinamico;
		private Integer indPermiteIsencaoLimite;
		private Integer indZerarSaldoBonus;
		private Integer indZerarSaldoSms;
		private Integer indZerarSaldoGprs;
		
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
		public String getNomPromocao() 
		{
			return nomPromocao;
		}

		/**
		 * @return
		 */
		public String getDesPromocao() 
		{
			return desPromocao;
		}

		/**
		 * @return
		 */
		public Date getDatInicio() 
		{
			return datInicio;
		}

		/**
		 * @return
		 */
		public Date getDatFim() 
		{
			return datFim;
		}

		/**
		 * @return
		 */
		/*public boolean isHibrido() 
		{
			return (indHibrido.intValue() == 0) ? false : true;
		}*/

		/**
		 * @return
		 */
		/*public Integer getIndHibrido()
		{
			return indHibrido;
		}*/

		/**
		 * @return
		 */
		public Date getDatInicioValidade() 
		{
			return datInicioValidade;
		}

		/**
		 * @return
		 */
		public Date getDatFimValidade() 
		{
			return datFimValidade;
		}

		/**
		 * @return
		 */
		/*public String getIndTipo() 
		{
			return indTipo;
		}*/

		/*public Integer getValPorMinuto() 
		{
			return valPorMinuto;
		}*/

		/**
		 * @return
		 */
		/*public boolean isMesFechado() 
		{
			return (indMesFechado.intValue() == 0) ? false : true;
		}*/

		/**
		 * @return
		 */
		/*public Integer getIndMesFechado() 
		{
			return indMesFechado;
		}*/

		/**
		 * @return
		 */
		/*public Integer getValDias()
		{
			return valDias;
		}*/

		/**
		 * @return
		 */
		public Double getVlrBonus()
		{
			return vlrBonus;
		}

		/**
		 * @return
		 */
		public Double getVlrMaxCreditoBonus()
		{
			return vlrMaxCreditoBonus;
		}

		/**
		 * @return
		 */
		public Integer getIdtCategoria()
		{
			return idtCategoria;
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
		public void setNomPromocao(String nomPromocao)
		{
			this.nomPromocao = nomPromocao;
		}

		/**
		 * @param 
		 */
		public void setDesPromocao(String desPromocao)
		{
			this.desPromocao = desPromocao;
		}

		/**
		 * @param 
		 */
		public void setDatInicio(Date datInicio)
		{
			this.datInicio = datInicio;
		}

		/**
		 * @param 
		 */
		public void setDatFim(Date datFim)
		{
			this.datFim = datFim;
		}

		/**
		 * @param
		 */
		/*public void setIsHibrido(boolean isHibrido)
		{
			this.indHibrido = new Integer((isHibrido) ? 1 : 0);
		}*/

		/**
		 * @param
		 */
		/*public void setIndHibrido(Integer indHibrido)
		{
			this.indHibrido = indHibrido;
		}*/

		/**
		 * @param 
		 */
		public void setDatInicioValidade(Date datInicioValidade)
		{
			this.datInicioValidade = datInicioValidade;
		}

		/**
		 * @param 
		 */
		public void setDatFimValidade(Date datFimValidade)
		{
			this.datFimValidade = datFimValidade;
		}

		/**
		 * @param 
		 */
		/*public void setIndTipo(String indTipo)
		{
			this.indTipo = indTipo;
		}*/

		/**
		 * @param
		 */
		/*public void setValPorMinuto(Integer valPorMinuto)
		{
			this.valPorMinuto = valPorMinuto;
		}*/

		/**
		 * @param 
		 */
		/*public void setIsMesFechado(boolean isMesFechado)
		{
			this.indMesFechado = new Integer((isMesFechado) ? 1 : 0);
		}*/

		/**
		 * @param 
		 */
		/*public void setIndMesFechado(Integer indMesFechado)
		{
			this.indMesFechado = indMesFechado;
		}*/

		/**
		 * @param 
		 */
		/*public void setValDias(Integer valDias) 
		{
			this.valDias = valDias;
		}*/

		/**
		 * @param 
		 */
		public void setVlrBonus(Double vlrBonus) 
		{
			this.vlrBonus = vlrBonus;
		}

		/**
		 * @param 
		 */
		public void setVlrMaxCreditoBonus(Double vlrMaxCreditoBonus) 
		{
			this.vlrMaxCreditoBonus = vlrMaxCreditoBonus;
		}
		
		/**
		 * @param 
		 */
		public void setIdtCategoria(Integer idtCategoria) 
		{
			this.idtCategoria = idtCategoria;
		}

		/**
		 * @return indLimiteDinamico.
		 */
		public Integer getIndLimiteDinamico()
		{
			return indLimiteDinamico;
		}

		/**
		 * @return indPermiteIsencaoLimite.
		 */
		public Integer getIndPermiteIsencaoLimite()
		{
			return indPermiteIsencaoLimite;
		}

		/**
		 * @return indZerarSaldoBonus.
		 */
		public Integer getIndZerarSaldoBonus()
		{
			return indZerarSaldoBonus;
		}

		/**
		 * @return indZerarSaldoGprs.
		 */
		public Integer getIndZerarSaldoGprs()
		{
			return indZerarSaldoGprs;
		}

		/**
		 * @return indZerarSaldoSms.
		 */
		public Integer getIndZerarSaldoSms()
		{
			return indZerarSaldoSms;
		}

		/**
		 * @return tipEspelhamento.
		 */
		public String getTipEspelhamento()
		{
			return tipEspelhamento;
		}

		/**
		 * @param indLimiteDinamico
		 */
		public void setIndLimiteDinamico(Integer indLimiteDinamico)
		{
			this.indLimiteDinamico = indLimiteDinamico;
		}

		/**
		 * @param indPermiteIsencaoLimite
		 */
		public void setIndPermiteIsencaoLimite(Integer indPermiteIsencaoLimite)
		{
			this.indPermiteIsencaoLimite = indPermiteIsencaoLimite;
		}

		/**
		 * @param indZerarSaldoBonus
		 */
		public void setIndZerarSaldoBonus(Integer indZerarSaldoBonus)
		{
			this.indZerarSaldoBonus = indZerarSaldoBonus;
		}

		/**
		 * @param indZerarSaldoGprs
		 */
		public void setIndZerarSaldoGprs(Integer indZerarSaldoGprs)
		{
			this.indZerarSaldoGprs = indZerarSaldoGprs;
		}

		/**
		 * @param indZerarSaldoSms
		 */
		public void setIndZerarSaldoSms(Integer indZerarSaldoSms)
		{
			this.indZerarSaldoSms = indZerarSaldoSms;
		}

		/**
		 * @param tipEspelhamento
		 */
		public void setTipEspelhamento(String tipEspelhamento)
		{
			this.tipEspelhamento = tipEspelhamento;
		}

}
