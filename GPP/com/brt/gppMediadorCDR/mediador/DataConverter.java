package com.brt.gppMediadorCDR.mediador;

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

import com.brt.gppMediadorCDR.formatoArquivos.*;
import java.io.*;

public interface DataConverter {

	public void convertAndWriteFromFormatoA(ArquivoFormatoA forA) throws IOException;
	public String convertFromFormatoA(ArquivoFormatoA forA);

	public void convertAndWriteFromFormatoB(ArquivoFormatoB forB) throws IOException;
	public String convertFromFormatoB(ArquivoFormatoB forB);

	public void convertAndWriteFromFormatoC(ArquivoFormatoC forC) throws IOException;
	public String convertFromFormatoC(ArquivoFormatoC forC);
}
