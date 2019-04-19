package com.brt.gpp.comum.mapeamentos;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.comum.mapeamentos.Mapeamento; 
import com.brt.gpp.comum.mapeamentos.MapCategoria;
import com.brt.gpp.comum.mapeamentos.MapTipoSaldo;
import com.brt.gpp.comum.mapeamentos.ValoresRecarga;
import com.brt.gpp.comum.mapeamentos.chave.ChaveTreeMap;
import com.brt.gpp.comum.mapeamentos.chave.ChaveVigencia;
import com.brt.gpp.comum.mapeamentos.entidade.Categoria;
import com.brt.gpp.comum.mapeamentos.entidade.PlanoPreco;
import com.brt.gpp.comum.mapeamentos.entidade.TipoSaldo;
import com.brt.gpp.comum.mapeamentos.entidade.ValorRecarga;

/**
 *	Este arquivo contem a classe que faz o mapeamento dos valores possiveis de recarga existentes no BD Oracle em 
 *	memoria no GPP.
 *
 *	@version		1.0		08/06/2004		Primeira versao.
 *	@author 		Denys Oliveira
 *
 *	@version		1.1		21/09/2004		Alteracao para mapear objeto ValoresRecarga no hash devido conter varias 
 *											informacoes a respeito do valor
 *	@author			Joao Carlos
 *
 *	@version		2.0		30/4/2007		Normalizacao dos valores de adaptacao para o Controle Total.
 *	@author			Daniel Ferreira
 */
public class MapValoresRecarga extends Mapeamento
{
	
	/**
	 *	Instancia do singleton. 
	 */
	private static MapValoresRecarga instance;
	
	/**
	 *	Container de entidades para pesquisa por valor. 
	 */
	private Map containerValor;
	
	/**
	 *	Container de entidades para pesquisa por valores de face. Os valores de face correspodem a valores de recarga 
	 *	de cartao.
	 */
	private Map containerValorFace;
	
	/**
	 *	Construtor da classe.
	 *
	 *	@throws		GPPInternalErrorException
	 */
	private MapValoresRecarga() throws GPPInternalErrorException
	{
		super();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapValoresRecarga getInstancia()
	{
		return MapValoresRecarga.getInstance();
	}
	
	/**
	 *	Retorna a instancia do singleton.
	 *
	 *	@return		Instancia do singleton.
	 */
	public static MapValoresRecarga getInstance()
	{
		try
		{
			if(MapValoresRecarga.instance == null)
				MapValoresRecarga.instance = new MapValoresRecarga();
			
			return MapValoresRecarga.instance;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 *	@see		com.brt.gpp.comum.mapeamentos.Mapeamento#load()
	 */
	protected void load() throws GPPInternalErrorException
	{
		PREPConexao	conexaoPrep	= null;
		
		try
		{
			//Inicializando o container padrao como TreeMap. Isto e necessario devido ao fato de a chave ser composta
			//tambem de um periodo de vigencia, exigindo comparacoes de data de execucao durante o acesso a chave.
			super.values = Collections.synchronizedMap(new TreeMap());
			
			String sqlQuery = 
				"SELECT id_valor, " +
				"       idt_categoria, " +
				"       idt_tipo_saldo, " +
				"       dat_ini_vigencia, " +
				"       dat_fim_vigencia, " +
				"       ind_valor_face, " +
				"       vlr_efetivo_pago, " +
				"       vlr_credito, " +
				"       vlr_bonus, " +
				"       num_dias_expiracao " +
				"  FROM tbl_rec_valor " +
				" ORDER BY id_valor, " +
				"          idt_categoria, " +
				"          idt_tipo_saldo, " +
				"          dat_ini_vigencia ";
			
			conexaoPrep = super.gerenteBancoDados.getConexaoPREP(0);
			ResultSet registros = conexaoPrep.executaQuery(sqlQuery, 0);
			
			while(registros.next())
			{
				double		idValor			= registros.getDouble("id_valor");
				Categoria	categoria		= MapCategoria.getInstance().getCategoria(registros.getInt("idt_categoria"));
				TipoSaldo	tipoSaldo		= MapTipoSaldo.getInstance().getTipoSaldo(registros.getShort("idt_tipo_saldo"));
				Date		datIniVigencia	= registros.getDate("dat_ini_vigencia");
				Date		datFimVigencia	= registros.getDate("dat_fim_vigencia");
				boolean		indValorFace	= (registros.getInt("ind_valor_face") == 1);
				double		vlrEfetivoPago	= registros.getDouble("vlr_efetivo_pago");
				double		vlrCredito		= registros.getDouble("vlr_credito");
				double		vlrBonus		= registros.getDouble("vlr_bonus");
				short		numDiasExp		= registros.getShort("num_dias_expiracao");
				
				ValorRecarga valor = new ValorRecarga(idValor,
													  categoria,
													  tipoSaldo,
													  datIniVigencia,
													  datFimVigencia,
													  indValorFace,
													  vlrEfetivoPago,
													  vlrCredito,
													  vlrBonus,
													  numDiasExp);
				
				//Inserindo a entidade no container padrao.
				ChaveVigencia	vigencia	= new ChaveVigencia(new Object[]{datIniVigencia, datFimVigencia});
				ChaveTreeMap	chave		= new ChaveTreeMap(new Object[]{new Double(idValor), categoria, vigencia});
				Collection		valores		= (Collection)super.values.get(chave);
				
				if(valores == null)
				{
					valores = Collections.synchronizedCollection(new ArrayList());
					super.values.put(chave, valores);
				}
				
				valores.add(valor);
			}
			
			registros.close();
			
			//Criando os containers de acesso por valores de credito e valor de face.
			this.criarContainerValores();
			this.criarContainerValoresFace();
		}
		catch(Exception e)
		{
			throw new GPPInternalErrorException("Excecao: " + e);
		}
		finally
		{
			super.gerenteBancoDados.liberaConexaoPREP(conexaoPrep, 0);
		}
	}

	/**
	 *	Cria o container para acesso por valores de recarga.
	 */
	private void criarContainerValores()
	{
		this.containerValor = Collections.synchronizedMap(new TreeMap());
		
		//Percorrendo o container padrao. Cada elemento no container corresponde a uma lista de valores de recarga, 
		//onde cada elemento corresponde ao valor creditado em um saldo. Cada uma destas listas corresponde aos 
		//valores necessarios para a criacao de um objeto ValoresRecarga.
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			Collection		listaValores	= (Collection)iterator.next();
			Categoria		categoria		= ((ValorRecarga)listaValores.iterator().next()).getCategoria();
			Date			datIniVigencia	= ((ValorRecarga)listaValores.iterator().next()).getDatIniVigencia();
			Date			datFimVigencia	= ((ValorRecarga)listaValores.iterator().next()).getDatFimVigencia();
			ValoresRecarga	valores			= new ValoresRecarga(listaValores);
			
			//Os objetos ValoresRecarga serao inseridos no container para acesso por valor. A chave corresponde aos 
			//valores creditados em cada saldo, junto com a categoria de plano de preco do assinante e ao periodo de
			//vigencia.
			Double vlrPrincipal = new Double(valores.getSaldoPrincipal() + valores.getValorBonusPrincipal()); 
			Double vlrPeriodico = new Double(valores.getSaldoPeriodico() + valores.getValorBonusPeriodico());
			Double vlrBonus     = new Double(valores.getSaldoBonus    () + valores.getValorBonusBonus    ());
			Double vlrTorpedos  = new Double(valores.getSaldoSMS      () + valores.getValorBonusSMS      ());
			Double vlrDados     = new Double(valores.getSaldoGPRS     () + valores.getValorBonusGPRS     ());

			ChaveVigencia	vigencia	= new ChaveVigencia(new Object[]{datIniVigencia, datFimVigencia});
			ChaveTreeMap	chave = new ChaveTreeMap(new Object[]{vlrPrincipal,
																  vlrPeriodico,
																  vlrBonus,
																  vlrTorpedos,
																  vlrDados,
																  categoria,
																  vigencia});
			
			this.containerValor.put(chave, valores);
		}
	}
	
	/**
	 *	Cria o container de valores de recarga que correspondem a valores de face, ou seja, valores que correspondem 
	 *	a recargas de cartao unico.
	 */
	private void criarContainerValoresFace()
	{
		//Percorrendo o container padrao. Cada elemento no container corresponde a uma lista de valores de recarga, 
		//onde cada elemento corresponde ao valor creditado em um saldo. Cada uma destas listas corresponde aos 
		//valores necessarios para a criacao de um objeto ValoresRecarga.
		this.containerValorFace = Collections.synchronizedMap(new TreeMap());
		
		for(Iterator iterator = super.values.values().iterator(); iterator.hasNext();)
		{
			//O primeiro elemento da lista de valores de recarga corresponde ao valor creditado no Saldo Principal (a 
			//clausula ORDER BY garante esta afirmacao). Caso este valor corresponder a um valor de face, o objeto 
			//ValoresRecarga sera criado e inserido no container.
			Collection		listaValores	= (Collection)iterator.next();
			ValorRecarga	valor			= (ValorRecarga)listaValores.iterator().next();
			
			if(valor.isValorFace())
			{
				Double			idValor			= new Double(valor.getIdValor());
				Date			datIniVigencia	= valor.getDatIniVigencia();
				Date			datFimVigencia	= valor.getDatFimVigencia();
				ValoresRecarga	valores			= new ValoresRecarga(listaValores);
				
				//Os objetos ValoresRecarga serao inseridos no container para acesso a recargas de valor de face. A
				//chave corresponde ao identificador do valor e ao periodo de vigencia.
				ChaveVigencia	vigencia	= new ChaveVigencia(new Object[]{datIniVigencia, datFimVigencia});
				ChaveTreeMap	chave		= new ChaveTreeMap(new Object[]{idValor, vigencia});
				
				this.containerValorFace.put(chave, valores);
			}
		}
	}
	
	/**
	 *	Retorna a lista de valores de recarga.
	 *
	 *	@param		idValor					Identificador do valor.
	 *	@param		categoria				Categoria do plano de preco.
	 *	@param		dataExecucao			Data de processamento da operacao.
	 *	@return		Lista de valores de recarga.
	 */
	public Collection getListaValoresRecarga(double idValor, Categoria categoria, Date dataExecucao)
	{
		ArrayList result = new ArrayList();
		
		ChaveTreeMap chave = new ChaveTreeMap(new Object[]{new Double(idValor), categoria, dataExecucao});
		Collection valores = (Collection)super.values.get(chave);
		
		if(valores != null)
			result.addAll(valores);
		
		return result;
	}
	
	/**
	 *	Retorna objeto (nao normalizado) contendo os valores de recarga.
	 *
	 *	@param		idValor					Identificador do valor.
	 *	@param		categoria				Categoria do plano de preco.
	 *	@param		dataExecucao			Data de processamento da operacao.
	 *	@return		Objeto contendo os valores de recarga.
	 */
	public ValoresRecarga getValoresRecarga(double idValor, Categoria categoria, Date dataExecucao)
	{
		Collection listaValores = this.getListaValoresRecarga(idValor, categoria, dataExecucao);
		return (listaValores.size() > 0) ? new ValoresRecarga(listaValores) : null;
	}
	
	/**
	 *	Retorna objeto (nao normalizado) contendo os valores de recarga com o acesso feito por valores de credito.
	 *
	 *	@param		vlrPrincipal			Valor creditado no Saldo Principal.
	 *	@param		vlrPeriodico			Valor creditado no Saldo Periodico.
	 *	@param		vlrBonus				Valor creditado no Saldo de Bonus.
	 *	@param		vlrTorpedos				Valor creditado no Saldo de Torpedos.
	 *	@param		vlrDados				Valor creditado no Saldo de Dados.
	 *	@param		planoPreco				Plano de preco do assinante.
	 *	@param		dataExecucao			Data de processamento da operacao.
	 *	@return		Objeto contendo os valores de recarga.
	 */
	public ValoresRecarga getValoresRecarga(double	vlrPrincipal, 
											double	vlrPeriodico, 
											double	vlrBonus, 
											double	vlrTorpedos,
											double	vlrDados,
											int		planoPreco,
											Date	dataExecucao)
	{
		Double		principal	= new Double(vlrPrincipal);
		Double		periodico	= new Double(vlrPeriodico);
		Double		bonus		= new Double(vlrBonus    );
		Double		torpedos	= new Double(vlrTorpedos );
		Double		dados		= new Double(vlrDados    );
		PlanoPreco	infoPlano	= MapPlanoPreco.getInstance().getPlanoPreco(planoPreco);
		Categoria	categoria	= (infoPlano != null) ? infoPlano.getCategoria() : null;
		
		ChaveTreeMap chave = new ChaveTreeMap(new Object[]{principal,
														   periodico,
														   bonus    ,
														   torpedos ,
														   dados    ,
														   categoria,
														   dataExecucao});
		
		return (ValoresRecarga)this.containerValor.get(chave);
	}
	
	/**
	 *	Retorna objeto (nao normalizado) contendo os valores de recarga que correspondem a valores de face. 
	 *
	 *	@param		idValor					Identificador do valor.
	 *	@param		dataExecucao			Data de processamento da operacao.
	 *	@return		Objeto contendo os valores de recarga.
	 */
	public ValoresRecarga getValoresRecarga(double idValor, Date dataExecucao)
	{
		ChaveTreeMap chave = new ChaveTreeMap(new Object[]{new Double(idValor), dataExecucao});
		return (ValoresRecarga)this.containerValorFace.get(chave);
	}
	
}
