package com.brt.gpp.aplicacoes.campanha.entidade;

import java.util.Date;

/**
 * Esta classe armazena as informacoes de historico de concessao de creditos de uma 
 * 
 * 
 * 
 * determinada campanha para um assinante. O Historico tem como objetivo registrar 
 * os valores concedidos, datas de concessao para serem utilizados em 
 * acompanhamento da efetividade da campanha.
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public class HistoricoConcessao 
{
   private long idCondicaoConcessao;
   private Date dataSatisfacaoCondicao;
   private double valorBonusSMConcedido;
   private double valorBonusDadosConcedido;
   
   /**
    * Access method for the idCondicaoConcessao property.
    * 
    * @return   the current value of the idCondicaoConcessao property
    */
   public long getIdCondicaoConcessao() 
   {
      return idCondicaoConcessao;
   }
   
   /**
    * Sets the value of the idCondicaoConcessao property.
    * 
    * @param aIdCondicaoConcessao the new value of the idCondicaoConcessao property
    */
   public void setIdCondicaoConcessao(long aIdCondicaoConcessao) 
   {
      idCondicaoConcessao = aIdCondicaoConcessao;
   }
   
   /**
    * Access method for the dataSatisfacaoCondicao property.
    * 
    * @return   the current value of the dataSatisfacaoCondicao property
    */
   public Date getDataSatisfacaoCondicao() 
   {
      return dataSatisfacaoCondicao;
   }
   
   /**
    * Sets the value of the dataSatisfacaoCondicao property.
    * 
    * @param aDataSatisfacaoCondicao the new value of the dataSatisfacaoCondicao property
    */
   public void setDataSatisfacaoCondicao(Date aDataSatisfacaoCondicao) 
   {
      dataSatisfacaoCondicao = aDataSatisfacaoCondicao;
   }
   
   /**
    * Access method for the valorBonusSMConcedido property.
    * 
    * @return   the current value of the valorBonusSMConcedido property
    */
   public double getValorBonusSMConcedido() 
   {
      return valorBonusSMConcedido;
   }
   
   /**
    * Sets the value of the valorBonusSMConcedido property.
    * 
    * @param aValorBonusSMConcedido the new value of the valorBonusSMConcedido property
    */
   public void setValorBonusSMConcedido(double aValorBonusSMConcedido) 
   {
      valorBonusSMConcedido = aValorBonusSMConcedido;
   }
   
   /**
    * Access method for the valorBonusDadosConcedido property.
    * 
    * @return   the current value of the valorBonusDadosConcedido property
    */
   public double getValorBonusDadosConcedido() 
   {
      return valorBonusDadosConcedido;
   }
   
   /**
    * Sets the value of the valorBonusDadosConcedido property.
    * 
    * @param aValorBonusDadosConcedido the new value of the valorBonusDadosConcedido property
    */
   public void setValorBonusDadosConcedido(double aValorBonusDadosConcedido) 
   {
      valorBonusDadosConcedido = aValorBonusDadosConcedido;
   }
}
