package com.brt.gppEnviaMail.aplicacao;

import java.io.File;
import java.io.IOException;

public interface ProcessadorArquivo
{
	public StringBuffer getResultadoComoBuffer(String sql, String separador) throws IOException;
	public File getResultadoComoArquivo(String sql, String nomeArquivo, String separador) throws IOException;
}
