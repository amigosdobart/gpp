package com.brt.gpp.comum.conexoes.tecnomen;

import com.brt.gpp.comum.arquivoConfiguracaoGPP.ArquivoConfiguracaoGPP;

// TecnomenRecargaFactory
//	Esta classe realiza a construcao de conexoes de recarga com a plataforma Tecnomen
//  A motivacao da criacao desta classe eh devido as conexoes com a plataforma serem
//  divididas em multiplas "PaymentEngine". Portanto este delegate realiza a distribuicao
//  das conexoes que estao sendo criadas pelo GerentePool em relacao aos ID's existentes
//  A configuracao reside no numero de conexoes disponiveis de Recarga pela Tecnomen
//  os IDs comecam a partir do numero 100. A ideia eh distribuir as conexoes entre os engines
//  criados. Ex: 5 Engines disponiveis e 16 conexoes de recarga a serem criadas no GPP
//
//	-------------------------------
//  | 100 | 101 | 102 | 103 | 104 | IDs de engine da PaymentInterface
//  -------------------------------
//  | 4   | 3   | 3   | 3   | 3   | Numero de conexoes do GPP
//  -------------------------------
//
//

public class TecnomenRecargaFactory
{
	private static TecnomenRecargaFactory instance;
	private int numeroEnginesPayment;
	private int idPaymentEngine = ConexaoTecnomen.RECARGA;
	
	private TecnomenRecargaFactory()
	{
		// Define o numero de engines disponiveis configurados na Tecnomen
		ArquivoConfiguracaoGPP conf = ArquivoConfiguracaoGPP.getInstance();
		
		numeroEnginesPayment = conf.getInt("TECNOMEN_ENGINES_RECARGA");
	}
	
	public static TecnomenRecargaFactory getInstance()
	{
		if (instance == null)
			instance = new TecnomenRecargaFactory();
		
		return instance;
	}
	
	/**
	 * Metodo....:incrementaPaymentEngine
	 * Descricao.:Realiza o incremento do numero do ID da engine a ser utilizado
	 *            para a proxima conexao a ser criada.
	 *
	 */
	private void incrementaIdPayment()
	{
		// Como o ID inicial eh fixo no valor 100 e eh definido o numero de conexoes
		// disponiveis, entao o sistema distribui a conexao entre os numeros inicial e
		// inicial + numero de conexoes. Portanto se o incremento for maior que o valor
		// total de conexoes, volta-se a utilizar o valor inicial igual a 100
		idPaymentEngine++;
		
		if (idPaymentEngine >= (ConexaoTecnomen.RECARGA + numeroEnginesPayment) )
			idPaymentEngine = ConexaoTecnomen.RECARGA;
	}
	
	/**
	 * Metodo....:newTecnomenRecarga
	 * Descricao.:Cria uma conexao de recarga associando a um ID disponivel da PaymentEngine
	 * @return TecnomenRecarga - Conexao de Recarga
	 */
	public TecnomenEngineRecarga newTecnomenRecarga()
	{
		// Realiza a criacao da conexao de recarga com o ID da Payment
		// distribuido nas multiplas Engines configuradas e realiza o
		// incremento para indicar qual o proximo ID a ser utilizado
		// para a proxima conexao
		TecnomenEngineRecarga recarga = new TecnomenEngineRecarga(idPaymentEngine);
		incrementaIdPayment();
		
		return recarga;
	}
}
