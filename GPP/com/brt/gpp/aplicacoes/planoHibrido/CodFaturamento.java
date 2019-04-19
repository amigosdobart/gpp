package com.brt.gpp.aplicacoes.planoHibrido;

import com.brt.gpp.comum.StringFormat;

/**
 * Classe responsavel por conter os dados da coluna
 * COD_FATURAMENTO utilizada na interface de Recarga Recorrente
 * entre o SAG e o GPP para concessao de Franquia e/ou Bonus
 * dos assinantes do Plano Controle
 * 
 * @author Joao Paulo Galvagni Junior
 * @since  14/03/2008
 *
 */
public class CodFaturamento
{
	private String matricula;			// Alpha	- 08
    private String locTel;				// Alpha	- 12
    private String contAutf;			// Alpha	- 10
    private String nomeAssinante;		// Alpha	- 40
    private double valor;				// Numeric	- 11,2 (13)
    private String vencimento;			// Alpha	- 08 (yyyyMMdd)
    private String categoria;			// Alpha	- 02
    private String sequencial;			// Numeric	- 17
    private String numFaturaFDO;		// Alpha	- 12
    private String xerox;				// Alpha	- 01
    private String codDocumento;		// Alpha	- 02
    private String status;				// Alpha	- 02
    private String msgErro;				// Alpha	- 25
    private String statusProcesso;		// Alpha	- 03
    private String msgProcesso;			// Alpha	- 15
    private double valorNovoBonus;		// Numeric	- 34
    private String filler;				// Alpha	- 01
    private double valorBonusFaleMais;	// Numeric	- 05
    private String planoSag;			// Alpha	- 04
    
    								    // Total	- 214 caracteres 
    private boolean isBonusFaleMais;
	
	/**
	 * O construtor ja monta a Entidade de acordo com a
	 * String recebida na coluna COD_FATURAMENTO da interface
	 * de recarga recorrente
	 * 
	 * @param codFaturamento
	 */
	public CodFaturamento(String codFaturamento)
    {
    	this.matricula 			= codFaturamento.substring(0 ,8 );
    	this.locTel				= codFaturamento.substring(8 ,20);
    	this.contAutf			= codFaturamento.substring(20,30);
    	this.nomeAssinante		= codFaturamento.substring(30,70);
    	try
    	{
    		this.valor				= Double.parseDouble(codFaturamento.substring(70,83))/100;
    	}
    	catch (NumberFormatException e)
    	{
    		this.valor = 0;
    	}
    	this.vencimento			= codFaturamento.substring(83,91);
    	this.categoria			= codFaturamento.substring(91,93);
    	this.sequencial			= codFaturamento.substring(93,110);
    	this.numFaturaFDO		= codFaturamento.substring(110,122);
    	this.xerox				= codFaturamento.substring(122,123);
    	this.codDocumento		= codFaturamento.substring(123,125);
    	this.status				= codFaturamento.substring(125,127);
    	this.msgErro			= codFaturamento.substring(127,152);
    	
    	// A partir desse ponto, nao ha mais String, pois a String
    	// enviada pelo SAG tem o tamanho maximo de 152, portanto,
    	// nao ha como continuar a construcao da entidade atraves
    	// de substring.
    	// Os 2 valores abaixo sao vazios, pois nunca sao preenchidos
    	// para uso do SAG, sendo necessario apenas mais 18 caracteres,
    	this.statusProcesso		= "   "; 			 // 3  caracteres
    	this.msgProcesso		= "               "; // 15 caracteres
    	
    	// O valor do bonus Pula-Pula (valorNovoBonus) so sera preenchido se
    	// o assinante receber algum valor, entao por default eh 0.0. Ao final
    	// esse valor sera formatado com ate 6 digitos, somando-se espacos a
    	// esquerda ate completar o tamanho final de 34 caracteres
    	// Ex.: 1.259,75 -> "                            125975"
    	this.valorNovoBonus		= 0.0;
    	// O Filler eh um campo para utilizacao extra, caso
    	// seja necessario, setado sempre em branco
    	this.filler				= " "; // 1 caracter
    	// O booleano isBonusFaleMais eh setado de acordo com o identificador
    	// que o SAG enviar na posicao, ou seja, caso o SAG envie o numero 1
    	// na posicao 206.
    	// O valor do BonusFaleMais eh preenchido inicialmente com o valor 0.0,
    	// ate que o mesmo seja calculado e preenchido com o valor em reais,
    	// tendo o tamanho total de 5 caracteres a partir da posicao 206
    	// Ex.: 998,65 -> 99865
    	this.valorBonusFaleMais = 0.0;
    	try
    	{
    		String indicadorBonusFaleMais = codFaturamento.substring(205, 206);
    		if ("1".equalsIgnoreCase(indicadorBonusFaleMais))
    			this.setBonusFaleMais(true);
    	}
    	catch (StringIndexOutOfBoundsException e)
    	{
    	}
    	
    	// O SAG ira enviar nesse campo o plano do assinante
    	// para identificacao do mesmo no retorno da bonificacao
    	try
    	{
    		planoSag = codFaturamento.substring(210, 214);
    	}
    	catch (StringIndexOutOfBoundsException ex)
    	{
    		planoSag = "    ";
    	}
    }
    
	/**
	 * Metodo responsavel por montar a String completa que compoe
	 * o campo COD_FATURAMENTO enviado ao SAG pelo GPP. O total de
	 * caracteres deve ser de 214
	 * 
	 */
    public String toString()
    {
    	StringBuffer codFaturamento = new StringBuffer();
    	
    	codFaturamento.append(this.matricula);										// 08 caracteres
    	codFaturamento.append(this.locTel);											// 12 caracteres
    	codFaturamento.append(this.contAutf);										// 10 caracteres
    	codFaturamento.append(this.nomeAssinante);									// 40 caracteres
    	codFaturamento.append(new StringFormat("13r0").format(Integer.toString((int)(this.valor*100)))); // 13 caracteres
    	codFaturamento.append(this.vencimento);										// 08 caracteres
    	codFaturamento.append(this.categoria);										// 08 caracteres
    	codFaturamento.append(this.sequencial);										// 17 caracteres
    	codFaturamento.append(this.numFaturaFDO);									// 12 caracteres
    	codFaturamento.append(this.xerox);											// 01 caracteres
    	codFaturamento.append(this.codDocumento);									// 02 caracteres
    	codFaturamento.append(this.status);											// 02 caracteres
    	codFaturamento.append(this.msgErro);										// 25 caracteres
    	codFaturamento.append(this.statusProcesso);									// 03 caracteres
    	codFaturamento.append(this.msgProcesso);									// 15 caracteres
    	
    	codFaturamento.append(new StringFormat("34r ").format(Integer.toString((int)(this.valorNovoBonus*100))));    // 34 caracteres
    	codFaturamento.append(this.filler);																			 // 01 caracteres
    	codFaturamento.append(new StringFormat("5r0").format(Integer.toString((int)(this.valorBonusFaleMais*100)))); // 05 caracteres
    	codFaturamento.append(this.planoSag); // 04 caracteres
    	
    	return codFaturamento.toString(); // TOTAL DE 214 CARACTERES
    }
    
    /**
	 * @return the categoria
	 */
	public String getCategoria()
	{
		return categoria;
	}
	
	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria)
	{
		this.categoria = categoria;
	}
	
	/**
	 * @return the codDocumento
	 */
	public String getCodDocumento()
	{
		return codDocumento;
	}
	
	/**
	 * @param codDocumento the codDocumento to set
	 */
	public void setCodDocumento(String codDocumento)
	{
		this.codDocumento = codDocumento;
	}
	
	/**
	 * @return the contAutf
	 */
	public String getContAutf()
	{
		return contAutf;
	}
	
	/**
	 * @param contAutf the contAutf to set
	 */
	public void setContAutf(String contAutf)
	{
		this.contAutf = contAutf;
	}
	
	/**
	 * @return the sequencial
	 */
	public String getSequencial()
	{
		return sequencial;
	}
	
	/**
	 * @param sequencial the sequencial to set
	 */
	public void setSequencial(String sequencial)
	{
		this.sequencial = sequencial;
	}
	
	/**
	 * @return the filler
	 */
	public String getFiller()
	{
		return filler;
	}
	
	/**
	 * @param filler the filler to set
	 */
	public void setFiller(String filler)
	{
		this.filler = filler;
	}
	
	/**
	 * @return the locTel
	 */
	public String getLocTel()
	{
		return locTel;
	}
	
	/**
	 * @param locTel the locTel to set
	 */
	public void setLocTel(String locTel)
	{
		this.locTel = locTel;
	}
	
	/**
	 * @return the matricula
	 */
	public String getMatricula()
	{
		return matricula;
	}
	
	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula)
	{
		this.matricula = matricula;
	}
	
	/**
	 * @return the msgErro
	 */
	public String getMsgErro()
	{
		return msgErro;
	}
	
	/**
	 * @param msgErro the msgErro to set
	 */
	public void setMsgErro(String msgErro)
	{
		this.msgErro = msgErro;
	}
	
	/**
	 * @return the msgProcesso
	 */
	public String getMsgProcesso()
	{
		return msgProcesso;
	}
	
	/**
	 * @param msgProcesso the msgProcesso to set
	 */
	public void setMsgProcesso(String msgProcesso)
	{
		this.msgProcesso = msgProcesso;
	}
	
	/**
	 * @return the nomeAssinante
	 */
	public String getNomeAssinante()
	{
		return nomeAssinante;
	}
	
	/**
	 * @param nomeAssinante the nomeAssinante to set
	 */
	public void setNomeAssinante(String nomeAssinante)
	{
		this.nomeAssinante = nomeAssinante;
	}
	
	/**
	 * @return the numFaturaFDO
	 */
	public String getNumFaturaFDO()
	{
		return numFaturaFDO;
	}
	
	/**
	 * @param numFaturaFDO the numFaturaFDO to set
	 */
	public void setNumFaturaFDO(String numFaturaFDO)
	{
		this.numFaturaFDO = numFaturaFDO;
	}
	
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}
	
	/**
	 * @return the statusProcesso
	 */
	public String getStatusProcesso()
	{
		return statusProcesso;
	}
	
	/**
	 * @param statusProcesso the statusProcesso to set
	 */
	public void setStatusProcesso(String statusProcesso)
	{
		this.statusProcesso = statusProcesso;
	}
	
	/**
	 * @return the valor
	 */
	public double getValor()
	{
		return valor;
	}
	
	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}
	
	/**
	 * @return the valorBonusFaleMais
	 */
	public double getValorBonusFaleMais()
	{
		return valorBonusFaleMais;
	}
	
	/**
	 * @param valorBonusFaleMais the valorBonusFaleMais to set
	 */
	public void setValorBonusFaleMais(double valorBonusFaleMais)
	{
		this.valorBonusFaleMais = valorBonusFaleMais;
	}
	
	/**
	 * @return the valorNovoBonus
	 */
	public double getValorNovoBonus()
	{
		return valorNovoBonus;
	}
	
	/**
	 * @param valorNovoBonus the valorNovoBonus to set
	 */
	public void setValorNovoBonus(double valorNovoBonus)
	{
		this.valorNovoBonus = valorNovoBonus;
	}
	
	/**
	 * @return the vencimento
	 */
	public String getVencimento()
	{
		return vencimento;
	}
	
	/**
	 * @param vencimento the vencimento to set
	 */
	public void setVencimento(String vencimento)
	{
		this.vencimento = vencimento;
	}
	
	/**
	 * @return the xerox
	 */
	public String getXerox()
	{
		return xerox;
	}
	
	/**
	 * @param xerox the xerox to set
	 */
	public void setXerox(String xerox)
	{
		this.xerox = xerox;
	}
	
	/**
	 * @return the isBonusFaleMais
	 */
	public boolean isBonusFaleMais()
	{
		return isBonusFaleMais;
	}
	
	/**
	 * @param isBonusFaleMais the isBonusFaleMais to set
	 */
	public void setBonusFaleMais(boolean isBonusFaleMais)
	{
		this.isBonusFaleMais = isBonusFaleMais;
	}
}