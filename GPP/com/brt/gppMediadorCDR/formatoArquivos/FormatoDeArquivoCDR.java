package com.brt.gppMediadorCDR.formatoArquivos;

/*
 * Created on 07/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author ex619898
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.*;

public interface FormatoDeArquivoCDR {

	public Map parse(String linha);
	public boolean accept();
	public char getDestinationFormat();
	public Date getTimestamp();
	public long getStartTime();
}
