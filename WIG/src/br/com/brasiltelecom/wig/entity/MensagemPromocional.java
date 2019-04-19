package br.com.brasiltelecom.wig.entity;

public class MensagemPromocional
{
	private int 	codPromocao;
	private int		codServico;
	private int		codConteudo;
	private String 	mensagemPromocional;
	
	public MensagemPromocional(int cPromocao, int cServico, int cConteudo)
	{
		this.codPromocao = cPromocao;
		this.codServico	 = cServico;
		this.codConteudo = cConteudo;
	}

	public int getCodPromocao()
	{
		return codPromocao;
	}

	public String getMensagemPromocional()
	{
		return mensagemPromocional;
	}

	public void setMensagemPromocional(String mensagemPromocional)
	{
		this.mensagemPromocional = mensagemPromocional;
	}

	public int getCodConteudo()
	{
		return codConteudo;
	}

	public int getCodServico()
	{
		return codServico;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return getCodServico()+getCodConteudo()+getCodPromocao();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return getMensagemPromocional();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof MensagemPromocional) )
			return false;
		
		if ( ((MensagemPromocional)obj).getCodPromocao() == this.getCodPromocao() &&
			 ((MensagemPromocional)obj).getCodServico() == this.getCodServico() &&
			 ((MensagemPromocional)obj).getCodConteudo() == this.getCodConteudo()
			)
			return true;
		
		return false;
	}
}
