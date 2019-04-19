package clientes;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import com.brt.gpp.componentes.gerenteGPP.orb.*;

public class ClientePingGPPNameService
{
	public static void main(String[] args) 
	{
		try
		{
			java.util.Properties props = System.getProperties();
			System.setProperties(props);
			
			// Inicializando o ORB
			org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init(args, props);
			System.out.println("Resolvendo referencia inicial");
			org.omg.CORBA.Object nsRef = orb.string_to_object("corbaloc:iiop:"+args[1]+":"+args[0]+"/NameService");
			NamingContext ncRef = NamingContextHelper.narrow(nsRef);
			
			System.out.println("Resolvendo referencia para gpp-componenteNegocioGerenteGPP");
			NameComponent nc[] = {new NameComponent("gpp",""),new NameComponent("ComponenteNegociosGerenteGPP", "")};
			gerenteGPP gerGPP = gerenteGPPHelper.narrow(ncRef.resolve(nc));
			
			System.out.println("Chamando metodo do consulta GPP");
			System.out.println(gerGPP.ping());
		}
		catch (Exception e)
		{
			// Finaliza o cliente GPP indicando erro no processamento
			e.printStackTrace();
			System.exit(1);		
		}
	}
}