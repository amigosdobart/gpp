//Cliente de Aprovisionamento.java
package clientes;

import com.brt.gpp.componentes.gerenteGPP.orb.*;
import com.brt.gpp.comum.Definicoes;

public class ClienteStatusGPP
{
	public ClienteStatusGPP ( )
	{
		System.out.println ("Ativando cliente de statusGPP...");
	}
	
	public static void main(String[] args) 
	{
//		String confPorta;
//		String confMaquina;
		gerenteGPP pPOA = null;
		
		if (args.length != 1)
		{
			System.out.println("Uso: StatusGPP <D/H/P/T> onde, D = Desenvolvimento, H = Homologacao, P = Producao, T = Todos");
			System.exit(1);
		}
		
		// Converte para maiusculo
		args[0] = args[0].trim();
		
		if (!( args[0].equals("D") || args[0].equals("H") || args[0].equals("P") || args[0].equals("T")))
		{
			System.out.println("Uso: StatusGPP <D/H/P/T> onde, D = Desenvolvimento, H = Homologacao, P = Producao, T = Todos");
			System.exit(2);
		}
		
		try
		{
			// Mostra o header
			mostraHeader();
			
			// DESENVOLVIMENTO
			if ( args[0].equals("D") || args[0].equals("T") )
			{
				pPOA = conecta (args, "12000", "10.61.176.109");
				mostraDados (pPOA, "12000");
			}

			// HOMOLOGACAO
			if ( args[0].equals("H") || args[0].equals("T") )
			{		
				pPOA = conecta (args, "13002", "10.61.176.111");
				mostraDados (pPOA, "13002");
			}
			
			// PRODUCAO
			if ( args[0].equals("P") || args[0].equals("T") )
			{
				pPOA = conecta (args, "15000", "10.61.176.118");
				mostraDados (pPOA, "15000");

				pPOA = conecta (args, "15002", "10.61.176.117");
				mostraDados (pPOA, "15002");
				
				pPOA = conecta (args, "15001", "10.61.177.71");
				mostraDados (pPOA, "15001");
			}
		}
		catch (Exception e)
		{
			System.out.println("Erro na criacao de conexao ORB" + e);
		}
	}
	
	public static gerenteGPP conecta (String[] args, String porta, String maquina)
	{
		gerenteGPP pPOA = null;
		
		try
		{
			java.util.Properties props = System.getProperties();
			props.put("vbroker.agent.port", porta);
			props.put("vbroker.agent.addr", maquina);
			System.setProperties ( props );	
			
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			
			byte[] managerId = "ComponenteNegociosGerenteGPP".getBytes();
	
			pPOA = gerenteGPPHelper.bind(orb, "/AtivaComponentesPOA", managerId);
		}
		catch (Exception e)
		{
		}
		
		return pPOA;
	}

	public static void mostraHeader ()
	{
		System.out.println("\n");
		System.out.println ("\t                  Numero de Conexoes                    |               Conexoes Disponiveis");
		System.out.println ("\tAtivo   Aprov.  Recar.  Voucher Admin   Agent      BD   | Aprov.  Recar.  Voucher Admin   Agent      BD");
		System.out.println ("\t------- ------- ------- ------- ------- ------- ------- | ------- ------- ------- ------- ------- -------");
	}
	public static void mostraDados (gerenteGPP pPOA, String aPorta)
	{
		System.out.print(aPorta);

		if (pPOA == null)
		{
			System.out.println("\tNAO");
		}
		else
		{
			try
			{
				try
				{
					pPOA.ping();
	
					System.out.print("\tSIM");
					
					short retAPRd = pPOA.getNumerodeConexoes( (short)Definicoes.CO_TIPO_TECNOMEN_APR );
					imprimeValor (retAPRd);
			
					short retRECd = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_REC );
					imprimeValor (retRECd);
			
					short retVOUd = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_VOU );
					imprimeValor (retVOUd);
			
					short retADMd = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_ADM );
					imprimeValor (retADMd);
			
					short retAGEd = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_TECNOMEN_AGE );
					imprimeValor (retAGEd);
			
					short retBCOd = pPOA.getNumerodeConexoes( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP );
					imprimeValor (retBCOd);
			
					// Utilizadas
					short retAPRu  = pPOA.getNumeroConexoesDisponiveis( (short)Definicoes.CO_TIPO_TECNOMEN_APR );
					//String val = retAPRu + " " + ((long)(retAPRu/retAPRd) * 100) + "%";
					//System.out.print("\t ");
					imprimeValor (retAPRu);
			
					short retRECu = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_REC );
					//val = " " + retRECu + " " + ((long)(retRECu/retRECd) * 100) + "%";
					//System.out.print("  " + val);
					imprimeValor (retRECu);
			
					short retVOUu = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_VOU );
					//val = retVOUu + " " + ((long)(retVOUu/retVOUd) * 100) + "%";
					//System.out.print("\t" + val);
					imprimeValor (retVOUu);
			
					short retADMu = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_ADM );
					//val = retADMu + " " + ((long)(retADMu/retADMd) * 100) + "%";
					//System.out.print("\t" + val);
					imprimeValor (retADMu);
			
					short retAGEu = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_TECNOMEN_AGE );
					//val = retAGEu + " " + ((long)(retAGEu/retAGEd) * 100) + "%";
					//System.out.print("\t" + val);
					imprimeValor (retAGEu);
			
					short retBCOu = pPOA.getNumeroConexoesDisponiveis( (short) Definicoes.CO_TIPO_BANCO_DADOS_PREP );
					//val = retBCOu + " " + ((long)(retBCOu/retBCOd) * 100) + "%";
					//System.out.print("\t " + val);
					imprimeValor (retBCOu);
					
					System.out.println("");
				}
				catch (Exception e)
				{
					System.out.println("\t\tNao");
				}
	
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	public static void imprimeValor (int ret)
	{
		String val = (new Integer (ret)).toString();
		
		if (val.length() < 7)
		{
			for (int i=0; i<(7-val.length()); i++)
			val = " " + val;
		}
		System.out.print ("\t " + val );
	}

	public static void imprimeValor (String ret)
	{
		if (ret.length() <= 7)
		{
			for (int i=0; i<=(7-ret.length()); i++)
			ret = " " + ret;
		}
		System.out.print ( ret );
	}
}
