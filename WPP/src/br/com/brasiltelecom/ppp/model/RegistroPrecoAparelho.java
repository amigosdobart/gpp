package br.com.brasiltelecom.ppp.model;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author tr060308
 *
 */
public class RegistroPrecoAparelho
{
	String codigoSAP;
	String fabricante;
	String modelo;
	String precoRegiao4;
	String precoRegiao5;
	String precoRegiao6;
	String modificaRegiao4;
	String modificaRegiao5;
	String modificaRegiao6;	
	String registro;
	Date   dataInicioVigencia;	
	
	/**
     * Atribui valores de acordo com o registro de um arquivo
	 * @throws ParseException 
     */
    public RegistroPrecoAparelho(String registroPrecoAparelho) throws ParseException
    {
    	String conteudoRegistro[] = registroPrecoAparelho.split(";");
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	registro 			= registroPrecoAparelho;
    	codigoSAP 			= conteudoRegistro[0];
    	fabricante 			= conteudoRegistro[1];
    	modelo 				= conteudoRegistro[2];
    	precoRegiao6 		= conteudoRegistro[3];
    	precoRegiao4 		= conteudoRegistro[4];
    	precoRegiao5 		= conteudoRegistro[5];
    	modificaRegiao6 	= conteudoRegistro[6];
    	modificaRegiao4 	= conteudoRegistro[7];
    	modificaRegiao5		= conteudoRegistro[8];
    	dataInicioVigencia	= new Date(sdf.parse(conteudoRegistro[9]).getTime());
    }
    
	/**
     * @return codigoSAP codigoSAP
     */
    public String getCodigoSAP()
    {
    	return codigoSAP;
    }
	
    /**
     * @param codigoSAP codigoSAP
     */
    public void setCodigoSAP(String codigoSAP)
    {
    	this.codigoSAP = codigoSAP;
    }
	
    /**
     * @return fabricante fabricante
     */
    public String getFabricante()
    {
    	return fabricante;
    }
	
    /**
     * @param fabricante fabricante
     */
    public void setFabricante(String fabricante)
    {
    	this.fabricante = fabricante;
    }
	
    /**
     * @return modelo modelo
     */
    public String getModelo()
    {
    	return modelo;
    }
	
    /**
     * @param modelo modelo
     */
    public void setModelo(String modelo)
    {
    	this.modelo = modelo;
    }
	
    /**
     * @return modificaRegiao4 Flag que indica se os preços da região 4 devem ser alterados
     */
    public String getModificaRegiao4()
    {
    	return modificaRegiao4;
    }
	
    /**
     * @param modificaRegiao4 Flag que indica se os preços da região 4 devem ser alterados
     */
    public void setModificaRegiao4(String modificaRegiao4)
    {
    	this.modificaRegiao4 = modificaRegiao4;
    }
	
    /**
     * @return modificaRegiao5 Flag que indica se os preços da região 5 devem ser alterados
     */
    public String getModificaRegiao5()
    {
    	return modificaRegiao5;
    }
	
    /**
     * @param modificaRegiao5 Flag que indica se os preços da região 5 devem ser alterados
     */
    public void setModificaRegiao5(String modificaRegiao5)
    {
    	this.modificaRegiao5 = modificaRegiao5;
    }
	
    /**
     * @return modificaRegiao6 Flag que indica se os preços da região 6 devem ser alterados
     */
    public String getModificaRegiao6()
    {
    	return modificaRegiao6;
    }
	
    /**
     * @param modificaRegiao6 Flag que indica se os preços da região 6 devem ser alterados
     */
    public void setModificaRegiao6(String modificaRegiao6)
    {
    	this.modificaRegiao6 = modificaRegiao6;
    }
	
    /**
     * @return precoRegiao4 precoRegiao4
     */
    public String getPrecoRegiao4()
    {
    	return precoRegiao4;
    }
	
    /**
     * @param precoRegiao4 precoRegiao4
     */
    public void setPrecoRegiao4(String precoRegiao4)
    {
    	this.precoRegiao4 = precoRegiao4;
    }
	
    /**
     * @return precoRegiao5 precoRegiao5
     */
    public String getPrecoRegiao5()
    {
    	return precoRegiao5;
    }
	
    /**
     * @param precoRegiao5 precoRegiao5
     */
    public void setPrecoRegiao5(String precoRegiao5)
    {
    	this.precoRegiao5 = precoRegiao5;
    }
	
    /**
     * @return precoRegiao6 precoRegiao6
     */
    public String getPrecoRegiao6()
    {
    	return precoRegiao6;
    }
	
    /**
     * @param precoRegiao6 precoRegiao6
     */
    public void setPrecoRegiao6(String precoRegiao6)
    {
    	this.precoRegiao6 = precoRegiao6;
    }

	/**
     * @return registro registro
     */
    public String getRegistro()
    {
    	return registro;
    }

	/**
     * @param registro registro
     */
    public void setRegistro(String registro)
    {
    	this.registro = registro;
    }

	/**
     * @return dataInicioVigencia dataInicioVigencia
     */
    public Date getDataInicioVigencia()
    {
    	return dataInicioVigencia;
    }

	/**
     * @param dataInicioVigencia dataInicioVigencia
     */
    public void setDataInicioVigencia(Date dataInicioVigencia)
    {
    	this.dataInicioVigencia = dataInicioVigencia;
    }	    
}