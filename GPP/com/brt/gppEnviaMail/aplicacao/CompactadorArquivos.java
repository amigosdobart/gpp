package com.brt.gppEnviaMail.aplicacao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//import com.brt.gppEnviaMail.conexoes.Configuracao;

public class CompactadorArquivos
{
	private static final int BUFFER_SIZE=1024;
	
	public static File getArquivoCompactado(String fileName, File arquivos[]) throws IOException
	{
		//Configuracao conf = Configuracao.getInstance();
		//Stream de saida aponta para o arquivo compactado a ser criado
		FileOutputStream output = new FileOutputStream(fileName);
		//Conectado o Stream responsável pela compactacao ao Stream de saida
		ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(output));

		for (int i=0; i < arquivos.length; i++)
		{
			//Stream de entrada aponta para o arquivo que vai ser compactado
			FileInputStream input = new FileInputStream(arquivos[i]);
			ZipEntry zipEntry = new ZipEntry(arquivos[i].getName());
			zipOutput.putNextEntry(zipEntry);
			
			//Criando o buffer de leitura
			//int bufferSize = Integer.parseInt(conf.getPropriedade("com.brt.gppEnviaMail.fileReadBufferSize"));
			byte[] buffer = new byte[BUFFER_SIZE];
			
			//Escrevendo no arquivo compactado
			int count = 0;
			while( (count=input.read(buffer, 0, BUFFER_SIZE)) != -1)
				zipOutput.write(buffer, 0, count);

			input.close();
		}		
		zipOutput.close();
		
		return new File(fileName);
	}	
}
