package com.brt.gpp.comum.produtorConsumidor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.brt.gpp.comum.mapeamentos.entidade.Entidade;

/**
 * @author Joao Carlos
 * Data..: 19/08/2005
 *
 */
public class ProcessoBatch implements Entidade
{
	private int 	idProcessoBatch;
	private String	nomeProcessoBatch;
	private String	classeProdutor;
	private String	classeConsumidor;
	private int		numeroThreads;
	private boolean	execucaoParalelo;
	private String  servidorGPP;
	
	public ProcessoBatch(int idProcessoBatch)
	{
		setIdProcessoBatch(idProcessoBatch);
	}
	
	public Class getClasseConsumidor() throws ClassNotFoundException
	{
		if (getNomeClasseConsumidor() != null)
			return Class.forName(getNomeClasseConsumidor());
		
		return null;
	}
	
	public String getNomeClasseConsumidor()
	{
		return classeConsumidor;
	}

	public String getClasseProdutor()
	{
		return classeProdutor;
	}
	
	public ProcessoBatchProdutor getProdutor(long idProcesso) 
	   throws ClassNotFoundException,InstantiationException,IllegalAccessException,NoSuchMethodException,InvocationTargetException
	{
		Class prodClass         = Class.forName(getClasseProdutor());
		Class  initParamClass[] = {long.class};
		Object initParamValue[] = {new Long(idProcesso)};
		Constructor construtor   = prodClass.getConstructor(initParamClass);
		return (ProcessoBatchProdutor)construtor.newInstance(initParamValue);
	}

	public int getIdProcessoBatch()
	{
		return idProcessoBatch;
	}

	public String getNomeProcessoBatch()
	{
		return nomeProcessoBatch;
	}

	public int getNumeroThreads()
	{
		return numeroThreads;
	}

	public boolean getExecucaoParalelo()
	{
		return execucaoParalelo;
	}
	
	public String getServidorGPP()
	{
		return servidorGPP;
	}
	
	public void setClasseConsumidor(String classeConsumidor)
	{
		this.classeConsumidor = classeConsumidor;
	}

	public void setClasseProdutor(String classeProdutor)
	{
		this.classeProdutor = classeProdutor;
	}

	public void setIdProcessoBatch(int idProcessoBatch)
	{
		this.idProcessoBatch = idProcessoBatch;
	}

	public void setNomeProcessoBatch(String nomeProcessoBatch)
	{
		this.nomeProcessoBatch = nomeProcessoBatch;
	}

	public void setNumeroThreads(int numeroThreads)
	{
		this.numeroThreads = numeroThreads;
	}
	public void setExecucaoParalelo(boolean execucaoParalelo)
	{
		this.execucaoParalelo= execucaoParalelo;
	}
	
	public void setServidorGPP(String servidorGPP)
	{
		this.servidorGPP= servidorGPP;
	}

	public Object clone()
	{
	   ProcessoBatch result = new ProcessoBatch(this.idProcessoBatch);
	   
	   result.setNomeProcessoBatch(this.nomeProcessoBatch);
	   result.setClasseProdutor(this.classeProdutor);
	   result.setClasseConsumidor(this.classeConsumidor);
	   result.setNumeroThreads(this.numeroThreads);
	   result.setExecucaoParalelo(this.execucaoParalelo);
	   result.setServidorGPP(this.servidorGPP);
	   
	   return result;
	}
	
	public int hashCode()
	{
		return idProcessoBatch;
	}
	
	public boolean equals(Object obj)
	{
		if ( !(obj instanceof ProcessoBatch) )
			return false;
		return  ((ProcessoBatch)obj).getIdProcessoBatch()==this.getIdProcessoBatch();
	}
	
	public String toString()
	{
		return getIdProcessoBatch() + " - " + getNomeProcessoBatch();
	}
}
