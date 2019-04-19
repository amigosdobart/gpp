package com.brt.gppMediadorCDR.mediador;

import java.io.File;
import java.io.FileFilter;

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
public class MediadorFileFilter implements FileFilter {

	private String pattern;

	public MediadorFileFilter(String aPattern){
		this.pattern = aPattern;
	}
	
	public String getPattern(){
		return this.pattern;
	}
	
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.getAbsolutePath().indexOf(getPattern()) > -1)
			return true;
		return false;
	}

}
