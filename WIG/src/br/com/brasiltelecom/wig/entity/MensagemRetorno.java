package br.com.brasiltelecom.wig.entity;

import br.com.brasiltelecom.wig.util.Definicoes;

public class MensagemRetorno
{
	private int codRetornoNum;
	private String codRetorno;
	private String mensagem;
	private String codErroSAC;
	private String tagSMS;

	/**
	 * @return Returns the tagSMS.
	 */
	public String getTagSMS()
	{
		return tagSMS;
	}

	/**
	 * @param tagSMS The tagSMS to set.
	 */
	public void setTagSMS(String msisdn, String mensagem)
	{
		StringBuffer wml = new StringBuffer("<wigplugin name=\"sendserversm\">\n");
		wml.append("<param name=\"userdata\" value=\""+mensagem+"\"/>\n");
		wml.append("<param name=\"destaddress\" value=\""+msisdn+"\"/>\n");
		wml.append("</wigplugin>\n");
		this.tagSMS = wml.toString();
	}
	
	/**
	 * @return Returns the mensagem.
	 */
	public String getMensagem()
	{
		return mensagem;
	}
	
	/**
	 * @param mensagem The mensagem to set.
	 */
	public void setMensagem(String mensagem)
	{
		this.mensagem = mensagem;
	}
	
	public boolean houveErro()
	{
		return !Definicoes.WIG_RET_OK_STR.equals(getCodRetorno());
	}

	/**
	 * @return Returns the codErroSAC.
	 */
	public String getCodErroSAC()
	{
		return codErroSAC;
	}

	/**
	 * @return Returns the codRetorno.
	 */
	public String getCodRetorno()
	{
		return codRetorno;
	}

	/**
	 * @param codErroSAC The codErroSAC to set.
	 */
	public void setCodErroSAC(String codErroSAC)
	{
		this.codErroSAC = codErroSAC;
	}

	/**
	 * @param codRetorno The codRetorno to set.
	 */
	public void setCodRetorno(String codRetorno)
	{
		this.codRetorno = codRetorno;
	}

	/**
	 * @return Returns the codRetornoNum.
	 */
	public int getCodRetornoNum()
	{
		return codRetornoNum;
	}

	/**
	 * @param codRetornoNum The codRetornoNum to set.
	 */
	public void setCodRetornoNum(int codRetornoNum)
	{
		this.codRetornoNum = codRetornoNum;
	}
}
