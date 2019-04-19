package br.com.brasiltelecom.ppp.portal.entity;

/**
 * Classe que implementa a entidade que mapeia
 * a tabela HSID_MODELO_SAP
 * 
 * @author	Marcelo Alves Araujo
 * @since	10/11/2006
 */
public class ModeloSAP
{
	private int		codigoModelo;
	private int		codigoSAP;
	private String	fabricante;
	private String	modelo;
	
	/**
     * @return codigoModelo C�digo do modelo
     */
    public int getCodigoModelo()
    {
    	return codigoModelo;
    }
	/**
     * @param codigoModelo C�digo do modelo
     */
    public void setCodigoModelo(int codigoModelo)
    {
    	this.codigoModelo = codigoModelo;
    }
	/**
     * @return codigoSAP C�digo SAP
     */
    public int getCodigoSAP()
    {
    	return codigoSAP;
    }
	/**
     * @param codigoSAP C�digo SAP
     */
    public void setCodigoSAP(int codigoSAP)
    {
    	this.codigoSAP = codigoSAP;
    }
	/**
     * @return fabricante Fabricante do aparelho
     */
    public String getFabricante()
    {
    	return fabricante;
    }
	/**
     * @param fabricante Fabricante do aparelho
     */
    public void setFabricante(String fabricante)
    {
    	this.fabricante = fabricante;
    }
	/**
     * @return modelo Modelo do aparelho
     */
    public String getModelo()
    {
    	return modelo;
    }
	/**
     * @param modelo Modelo do aparelho
     */
    public void setModelo(String modelo)
    {
    	this.modelo = modelo;
    }	
}