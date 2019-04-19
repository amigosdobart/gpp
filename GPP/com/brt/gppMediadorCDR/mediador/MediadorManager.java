package com.brt.gppMediadorCDR.mediador;

/*
 * Created on 07/04/2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import com.brt.gppMediadorCDR.formatoArquivos.*;
import java.io.*;
import java.text.*;
import java.net.*;
import java.util.*;

public class MediadorManager {

	private static String DIR_IMPORTACAO;
	private static String DIR_EXPORTACAO;
	private static String DIR_PROCESSADO;
	private static String PATTERN_FORMATOA;
	private static String PATTERN_FORMATOAX;
	private static String PATTERN_FORMATOB;
	private static String PATTERN_FORMATOC;
	private static String NOME_ARQUIVO_EXPORTD;
	private static String NOME_ARQUIVO_EXPORTE;
	
	private ExportFormatoD arquivoD;
	private ExportFormatoE arquivoE;
	
	public MediadorManager()
	{
		try
		{
			Properties props = new Properties();
			URL recurso = ClassLoader.getSystemResource("mediador.properties");
			props.load(recurso.openStream());
			
			DIR_IMPORTACAO       = props.getProperty("DIR_IMPORTACAO");
			DIR_EXPORTACAO       = props.getProperty("DIR_EXPORTACAO");
			DIR_PROCESSADO       = props.getProperty("DIR_PROCESSADO");
			PATTERN_FORMATOA     = props.getProperty("PATTERN_FORMATOA");
			PATTERN_FORMATOAX    = props.getProperty("PATTERN_FORMATOAX");
			PATTERN_FORMATOB     = props.getProperty("PATTERN_FORMATOB");
			PATTERN_FORMATOC     = props.getProperty("PATTERN_FORMATOC");
			NOME_ARQUIVO_EXPORTD = props.getProperty("NOME_ARQUIVO_EXPORTD");
			NOME_ARQUIVO_EXPORTE = props.getProperty("NOME_ARQUIVO_EXPORTE");
		}
		catch(Exception e)
		{
			System.out.println("Nao foi possivel encontrar o arquivo de propriedades.");
		}
	}
	
	private File[] getFiles(String aMascara) throws IOException{
		File f = new File(MediadorManager.DIR_IMPORTACAO);
		if (!f.isDirectory())
			throw new IOException(MediadorManager.DIR_IMPORTACAO + " não é um diretório válido");
			
		return f.listFiles(new MediadorFileFilter(aMascara));
	}
	
	private void readFile(File f, FormatoDeArquivoCDR cdr) throws FileNotFoundException,IOException{
		FileReader 		fReader = new FileReader(f);
		BufferedReader 	buffer 	= new BufferedReader(fReader);
		
		String linha;
		while ( (linha=buffer.readLine()) != null)
			if (linha.length() > 2){
				cdr.parse(linha);
				if (cdr.accept()){
					if (cdr.getDestinationFormat() == 'B'){ //Ambos Formatos
						exportToD(cdr);
						exportToE(cdr);
					}
					else if (cdr.getDestinationFormat() == ExportArquivoCDR.ARQUIVO_DADOS_E_VOZ)
							exportToD(cdr);
					else if (cdr.getDestinationFormat() == ExportArquivoCDR.ARQUIVO_EVENTOS_RECARGAS)
							exportToE(cdr);						
				}
			}

		buffer.close();
		fReader.close();
	}

	private ExportFormatoD createArquivoD() throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");	
		String fileName = 	MediadorManager.DIR_EXPORTACAO 			+
							System.getProperty("file.separator")	+ 
							sdf.format(new java.util.Date()) 		+ 
							MediadorManager.NOME_ARQUIVO_EXPORTD;

		System.out.println("Criando arquivo.. " + fileName);
		return new ExportFormatoD(fileName); 
	}
	
	private void exportToD(FormatoDeArquivoCDR cdr) throws IOException{
		if (arquivoD == null)
			arquivoD = createArquivoD();
			
		if (cdr instanceof ArquivoFormatoA)
			arquivoD.convertAndWriteFromFormatoA( (ArquivoFormatoA)cdr );
			
		if (cdr instanceof ArquivoFormatoB)
			arquivoD.convertAndWriteFromFormatoB( (ArquivoFormatoB)cdr );
			
		if (cdr instanceof ArquivoFormatoC)
			arquivoD.convertAndWriteFromFormatoC( (ArquivoFormatoC)cdr );
	}

	private ExportFormatoE createArquivoE() throws IOException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");	
		String fileName = 	MediadorManager.DIR_EXPORTACAO 			+
							System.getProperty("file.separator")	+ 
							sdf.format(new java.util.Date()) 		+ 
							MediadorManager.NOME_ARQUIVO_EXPORTE;

		System.out.println("Criando arquivo.. " + fileName);
		return new ExportFormatoE(fileName);
	}

	private void exportToE(FormatoDeArquivoCDR cdr) throws IOException{
		if (arquivoE == null)
			arquivoE = createArquivoE();
			
		if (cdr instanceof ArquivoFormatoA)
			arquivoE.convertAndWriteFromFormatoA( (ArquivoFormatoA)cdr );
	}

//	private void closeExportFiles() throws IOException{
//		if (arquivoD != null)
//			arquivoD.closeFile();
//
//		System.out.println("Fechando arquivo de export.");
//	}

	private boolean moveArquivo(File f) throws IOException{
		File fDest = new File(MediadorManager.DIR_PROCESSADO + 
							  System.getProperty("file.separator") +  
							  f.getName());
		
		return f.renameTo(fDest);
	}

 	public void processaArquivos() throws IOException,FileNotFoundException{
 		FormatoDeArquivoCDR formatoA = ArquivoFormatoA.getInstance();
		((ArquivoFormatoA)formatoA).setTipoCDR("PP");
		File arqFormatoA[] = getFiles(MediadorManager.PATTERN_FORMATOA);
		for (int i=0; i < arqFormatoA.length; i++){
			System.out.println("Processando arquivo.. " + arqFormatoA[i].getAbsolutePath());
			readFile(arqFormatoA[i],formatoA);
			if (!moveArquivo(arqFormatoA[i]))
				System.out.println("Nao foi possivel mover o arquivo : " + arqFormatoA[i].getAbsolutePath());
		}

		((ArquivoFormatoA)formatoA).setTipoCDR("MO");
		File arqFormatoAX[] = getFiles(MediadorManager.PATTERN_FORMATOAX);
		for (int i=0; i < arqFormatoAX.length; i++){
			System.out.println("Processando arquivo.. " + arqFormatoAX[i].getAbsolutePath());
			readFile(arqFormatoAX[i],formatoA);
			if (!moveArquivo(arqFormatoAX[i]))
				System.out.println("Nao foi possivel mover o arquivo : " + arqFormatoAX[i].getAbsolutePath());
		}
		
		FormatoDeArquivoCDR formatoB = ArquivoFormatoB.getInstance();
		((ArquivoFormatoB)formatoB).setTipoCDR("MMS");
		File arqFormatoB[] = getFiles(MediadorManager.PATTERN_FORMATOB);
		for (int i=0; i < arqFormatoB.length; i++){
			System.out.println("Processando arquivo.. " + arqFormatoB[i].getAbsolutePath());
			readFile(arqFormatoB[i],formatoB);
			if (!moveArquivo(arqFormatoB[i]))
				System.out.println("Nao foi possivel mover o arquivo : " + arqFormatoB[i].getAbsolutePath());
		}
		
		FormatoDeArquivoCDR formatoC = ArquivoFormatoC.getInstance();
		((ArquivoFormatoC)formatoC).setTipoCDR("MT");
		File arqFormatoC[] = getFiles(MediadorManager.PATTERN_FORMATOC);
		for (int i=0; i < arqFormatoC.length; i++){
			System.out.println("Processando arquivo.. " + arqFormatoC[i].getAbsolutePath());
			readFile(arqFormatoC[i],formatoC);
			if (!moveArquivo(arqFormatoC[i]))
				System.out.println("Nao foi possivel mover o arquivo : " + arqFormatoC[i].getAbsolutePath());
		}
 	}

	public static void main(String args[])
	{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
			System.out.println("Inicio do Processamento: " + sdf.format(new java.util.Date()));

			MediadorManager mediador = new MediadorManager();
			mediador.processaArquivos();
			
			System.out.println("Fim do processamento: " + sdf.format(new java.util.Date()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
