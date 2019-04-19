package com.brt.gpp.aplicacoes.enviarUsuariosStatus;

import java.io.IOException;

import com.brt.gpp.aplicacoes.Aplicacoes;
import com.brt.gpp.aplicacoes.enviarUsuariosDisconnected.EnvioUsuariosDisconnectedConsumidor;
import com.brt.gpp.comum.Definicoes;
import com.brt.gpp.comum.interfaceEscrita.InterfaceEscrita;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchConsumidor;
import com.brt.gpp.comum.produtorConsumidor.ProcessoBatchProdutor;
import com.brt.gpp.comum.produtorConsumidor.Produtor;
import com.brt.gpp.gerentesPool.GerentePoolLog;
/**
 * Consumidor responsavel por gravar os usuarios no arquivo de saida.
 *
 * @author Leone Parise Vieria da Silva
 * @since  27/09/2007
 */
public class EnvioUsuariosStatusConsumidor extends Aplicacoes implements ProcessoBatchConsumidor
{
	private ProcessoBatchProdutor produtor;

	public EnvioUsuariosStatusConsumidor()
	{
		super(GerentePoolLog.getInstancia(EnvioUsuariosDisconnectedConsumidor.class).getIdProcesso(Definicoes.CL_ENVIO_USUARIO_STATUS_CONSUMIDOR),
				Definicoes.CL_ENVIO_USUARIO_STATUS_CONSUMIDOR);
	}

	public void startup(ProcessoBatchProdutor produtor) throws Exception
	{
		this.produtor = produtor;
        startup();
	}
	/**
	 * Grava os dados do usuario numa linha do arquivo.
	 *
	 * @param Object	Usuario a ser gravado
	 */
	public void execute(Object obj) throws Exception
	{
		UsuariosStatusVO usuario = (UsuariosStatusVO)obj;

		String campos[] = {usuario.getMsisdn(), usuario.getStatus(), usuario.getData()};
		EnvioUsuariosStatusProdutor produtor = (EnvioUsuariosStatusProdutor)this.produtor;
		InterfaceEscrita arqInterfaceEscrita = produtor.getArquivoInterfaceEscrita();
		try{
			arqInterfaceEscrita.escrever(campos);
		}
		catch(IOException e)
		{
			super.log(Definicoes.ERRO, "execute", "Erro ao gravar usuario "+usuario.getMsisdn()+" em arquivo: "+e.getMessage());
		}
	}

	public void finish()
	{

	}

	public void startup() throws Exception
	{

	}

	public void startup(Produtor produtor) throws Exception
	{

	}
}
