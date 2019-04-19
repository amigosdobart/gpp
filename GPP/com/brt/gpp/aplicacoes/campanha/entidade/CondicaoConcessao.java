package com.brt.gpp.aplicacoes.campanha.entidade;

import java.util.Date;
import com.brt.gpp.aplicacoes.campanha.entidade.AssinanteCampanha;
import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;

/**
 * Esta interface define os metodos necessarios para que uma classe seja 
 * considerada uma implementacao das condicoes de concessao de creditos para uma 
 * campanha promocional. Uma campanha poderah conter varias condicoes, entao a 
 * classe implementando essa interface deverah realizar o trabalho de investigacao 
 * e retorno dos valores de bonus caso um assinante seja bonificado pela campanha
 * 
 * @author Joao Carlos
 * @since 30-Janeiro-2006
 */
public interface CondicaoConcessao 
{
   /**
    * Este metodo retorna se um determinado assinante deverah ser bonificado para uma 
    * determinada campanha.
    * 
    * @param assinante a ser processado para verificacao se este
    * deverah ser bonificado
    * @param conexaoprep de banco de dados a ser utilizada
    * @return boolean - Indica se o assinante deve ou nao ser bonificado
    */
   public boolean deveSerBonificado(AssinanteCampanha assinante, PREPConexao conexaoPrep);
   
   /**
    * Este metodo define uma alternativa para que um procedimento pos bonificacao
    * seja executado. Assim como define o nome este metodo serah executado somente
    * se o assinante for bonificado pelo metodo acima
    * 
    * @param assinante Assinante campanha ser utilizado
    * @param conexaoPrep conexao de banco de dados a ser utilizada
    */
   public void executarPosBonificacao(AssinanteCampanha assinante, PREPConexao conexaoPrep);
   
   /**
    * Este metodo retorna o nome da condicao que a classe implementa. Essa informacao 
    * serah utilizada somente para identificacao de qual condicao realizou a concessao 
    * 
    * de credito para o assinante e em qual campanha.
    * 
    * @return String - Nome da condicao
    */
   public String getNomeCondicao();
   
   /**
    * Retorna o valor do bonus a ser concedido no saldo de bonus se o assinante for 
    * bonificado pela condicao de concessao sendo processada.
    * 
    * @return double - Valor de bonus no saldo de bonus a ser concedido
    */
   public double getValorConcederBonus();
   
   /**
    * Este metodo retorna o valor de bonus no saldo de SMS a ser bonficado para o 
    * assinante. O valor retornado eh o parametro a ser utilizado na concessao, porem 
    * deverah ser consultado o metodo deveSerBonficado para identificar se o assinante 
    * 
    * realmente irah receber o bonus
    * 
    * @return double - Valor do bonus para o saldo de SMS
    */
   public double getValorConcederSM();
   
   /**
    * Retorna o valor do bonus a ser concedido no saldo de Dados se o assinante for 
    * bonificado pela condicao de concessao sendo processada.
    * 
    * @return double - Valor de bonus no saldo de Dados a ser concedido
    */
   public double getValorConcederDados();
   
   /**
    * Retorna a data que a condicao foi satisfeita. Para a maioria dos processos
    * serah a data da execucao da condicao, porem pode-se indicar outra data
    * dependendo da necessidade
    * 
    * @return Date - Data da satisfacao da condicao de concessao
    */
   public Date getDataSatisfacaoCondicao();
}
