package com.brt.gpp.comum.interfaceEscrita;

import java.util.LinkedHashMap;
import java.util.Map;

import com.brt.gpp.gerentesPool.GerentePoolLog;

/**
 * Interface de Configuracao no qual define o novo modelo de escrita de arquivos do GPP.
 * Deve ser utilizado como complemento a interface <code>InterfaceEscrita</code> onde a
 * classe de Configuracao contem os atributos e configuracoes basicas para o funcionamento
 * da classe de Escrita.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 03/10/2007
 */
public abstract class InterfaceConfiguracao
{
	protected Map atributos;
	protected InterfaceEscrita escrita;
	protected GerentePoolLog log;
	protected long idProcesso;
	protected String classe;
	/**
	 * Construtor padrao da Classe Abstratada.
	 * Inicializa o mapa de Atributos, o Log e o idProcesso.
	 */
	public InterfaceConfiguracao()
	{
		this.atributos = new LinkedHashMap();
		// Captura o nome da classe
		StringBuffer sb = new StringBuffer(this.getClass().getName());
		sb.delete(0, sb.lastIndexOf(".")+1);

		this.classe = sb.toString();
		this.log = GerentePoolLog.getInstancia(this.getClass());
		this.idProcesso = log.getIdProcesso(this.classe);
	}
	/**
	 * Retorna a Interface de Escrita atrelada a Interface de Configuracao
	 * A classe que implementa a InterfaceEscrita nao deve ser instanciada
	 * sem a classe de configuracao adequada.
	 *
	 * @return InterfaceEscrita
	 */
	public abstract InterfaceEscrita getInterfaceEscrita();

	/**
	 * Configura o valor de um atributo em tempo de execucao.<br>
	 * <b>OBSERVACAO: </b>Os atributos devem estar explicitos no Javadoc da classe de Escrita!
	 *
	 * @param atributo      			 Nome do atributo
	 * @param valor      				 Valor do atributo
	 * @throws IllegalArgumentException  Caso a atributo nao exista
	 */
	public void setAtributo(Object atributo, Object valor) throws IllegalArgumentException
	{
		atributos.remove(atributo);
		atributos.put(atributo, valor);
	}
	/**
	 * Retorna o valor de um atributo em tempo de execucao.<br>
	 * <b>OBSERVACAO: </b>Os atributos devem estar explicitos no Javadoc da classe de Escrita!
	 *
	 * @param  atributo Nome do atributo
	 * @return Valor do atributo
	 */
	public Object getAtributo(Object atributo)
	{
		if(!atributos.containsKey(atributo))
			return null;
		return atributos.get(atributo);
	}
	public long getIdProcesso()
	{
		return this.idProcesso;
	}
}