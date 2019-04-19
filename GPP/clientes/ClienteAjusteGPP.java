package clientes;

import com.brt.gpp.componentes.recarga.orb.*;
import com.brt.gpp.comum.Definicoes;

import java.io.*;
import java.text.SimpleDateFormat;

/**
 * Formato do arquivo
 * MSISDN;TIPO TRANSANCAO;VALOR
 * 
 */


public class ClienteAjusteGPP
{
	public ClienteAjusteGPP ( )
	{
		System.out.println ("Cliente de recarga...");
	}
	
	public static void main(String[] args) throws Exception	{

		SimpleDateFormat formatoSaida = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss" );
		System.out.println(formatoSaida.format(new java.util.Date()) + " Inicio do processo");
		java.util.Properties props = System.getProperties();
		props.put("vbroker.agent.port", args[0]);
		props.put("vbroker.agent.addr", args[1]);
		System.setProperties ( props );	
		
		// Inicializando o ORB
		org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
		
		byte[] managerId = "ComponenteNegociosRecarga".getBytes();
		
		recarga pPOA = recargaHelper.bind(orb, "/AtivaComponentesPOA", managerId);

//		int userOption = 0;
//		boolean exit = true;
		String tipoCredito = Definicoes.TIPO_CREDITO_REAIS;
		String sistemaOrigem = Definicoes.SO_GPP;
		String operador = Definicoes.GPP_OPERADOR;
		SimpleDateFormat formatoGPP = new SimpleDateFormat("yyyyMMddHHmmss");
		long cont=0;

		try{
			BufferedReader buffer = new BufferedReader(new FileReader(args[2]));
			PrintStream out = new PrintStream(new FileOutputStream(args[3]));
			String linha = null;
			while((linha = buffer.readLine()) != null){
				String campos[] = linha.split(";");			

				try{
					//String tipoAjuste = null;
					double valor = Double.parseDouble(campos[2]);
					
					if(valor >= 0.0d){
						//tipoAjuste = Definicoes.TIPO_AJUSTE_CREDITO;
					}
					else{
						//tipoAjuste = Definicoes.TIPO_AJUSTE_DEBITO;
						//valor = Math.abs(valor);
					}
					
					
					short ret = pPOA.executaAjuste(	campos[0], 
									campos[1],  //"05007"
									tipoCredito, 
									valor, 
									Definicoes.TIPO_AJUSTE_CREDITO, 
									formatoGPP.format(new java.util.Date()), 
									sistemaOrigem, 
									operador, 
									"");
					out.println(formatoSaida.format(new java.util.Date()) + ";"+campos[0]+";"+ campos[1] +";"+campos[2]+";"+ret);
					out.flush();
					cont++;
					
				}
				catch(Exception e){
					out.println(formatoSaida.format(new java.util.Date()) + ";"+campos[0]+";"+ campos[1] +";"+campos[2]+";"+e.getMessage());
					out.flush();
				}
			}
			buffer.close();
			out.close();
		}
		catch (Exception e) 
		{
			System.out.println("Erro:" + e);
		}
		System.out.println(formatoSaida.format(new java.util.Date()) + " Fim do processo, foram processados : " + cont );
	}
}