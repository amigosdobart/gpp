package com.brt.gppMediadorCDR.mediador;

/*
 * Created on 07/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.io.*;

public abstract class ExportArquivoCDR {

	private File 		exportedFile;
	private FileWriter 	fWriter;
	
	public static final char 	FIELD_SEPARATOR=',';
	public static final char	ARQUIVO_DADOS_E_VOZ='D';
	public static final char	ARQUIVO_EVENTOS_RECARGAS='E';
	public static final String 	RETURN="\n";
	

	public ExportArquivoCDR(String fileName) throws IOException{
		exportedFile 	= new File(fileName);
		fWriter			= new FileWriter(exportedFile);
	}
	
	public void writeLine(String line) throws IOException{
		fWriter.write(line);
		fWriter.flush();
	}
	
	public void closeFile() throws IOException{
		if (fWriter != null)
			fWriter.close();

		exportedFile=null;
	}
	
	public char getFieldSeparator(){
		return ExportArquivoCDR.FIELD_SEPARATOR;
	}
}
