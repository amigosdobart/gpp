package br.com.brasiltelecom.wig.action;

import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.sql.Connection;

import br.com.brasiltelecom.wig.entity.Conteudo;
import br.com.brasiltelecom.wig.entity.Servico;
import br.com.brasiltelecom.wig.entity.Resposta;
import br.com.brasiltelecom.wig.exception.NaoSubiuTSDException;
import br.com.brasiltelecom.wig.dao.RespostaDAO;
import br.com.brasiltelecom.wig.dao.ValidadorConteudo;

/**
 * @author Joao Carlos
 * Data..: 16/06/2005
 *
 */
public class WIGWmlConstrutor
{
	private String	msisdn;
	private String	iccid;
	public static String PROLOGUE_WML   =  "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" +
										    "<!DOCTYPE wml PUBLIC \"-//SmartTrust//DTD WIG-WML 4.0//EN\"\n" +
										    "\"http://www.smarttrust.com/DTD/WIG-WML4.0.dtd\">\n";
	
	public static String WML_TSD		= 	"<wml>\n" +
											"<card>\n" +
											"<plugin name=\"*PAD\" destvar=\"\" params=\"U\\x10\\x03\"/>\n" +
											"<go href=\"wiblet://smarttrust.com/uem/tsd\"/>\n" +
											"</card>\n" +
											"</wml>\n";
	
	public WIGWmlConstrutor(String msisdn, String iccid)
	{
		this.msisdn 		= msisdn;
		this.iccid			= iccid;
	}

	/**
	 * Metodo....:validacaoConteudo
	 * Descricao.:Este metodo realiza a validacao do conteudo utilizando as classes filtros
	 *            para retornar uma resposta a ser utilizado por este construtor de WML. Caso
	 *            nao haja validadores ou todos retornem nulo entao a resposta default sera
	 * 			  utilizada
	 * @param servico	- Servico a ser utilizado
	 * @param conteudo	- Conteudo a ser utilizado
	 * @param con		- Conexao de banco de dados a ser utilizada
	 * @param Map		- Map contendo os valores de parametros da querystring sendo processada
	 * @return Resposta - Resposta alternativa retornada pelos filtros ou a resposta default
	 *                    retornada pelo proprio objeto conteudo
	 */
	private Resposta validacaoConteudo(Servico servico, Conteudo conteudo, Connection con, Map parameters) throws NaoSubiuTSDException
	{
		// A resposta a ser retornada eh a resposta default contida no conteudo
		Resposta resposta = conteudo.getResposta();
		// Realiza a iteracao nos validadores de conteudo para retornar o objeto
		// resposta que sera utilizado
		for (Iterator i=conteudo.getValidadores().iterator(); i.hasNext();)
		{
			ValidadorConteudo validador = (ValidadorConteudo)i.next();
			Resposta resp = validador.getResposta(servico,conteudo,msisdn,iccid,con,parameters);
			// Se o validador retornou uma resposta entao termina a execucao dos validadores
			// pois a ordem jah esta na prioridade correta a ser executada. A menor prioridade
			// tem preferencia
			if (resp != null)
			{
				resposta = resp;
				break;
			}
		}
		// Se a resposta default ou a retornada apos a validacao possuir
		// um wml como conteudo entao os filtros podem ser aplicados
		if (resposta.getTipoResposta()==RespostaDAO.RESPOSTA_TIPO_WML)
			resposta = aplicaFiltros(conteudo,resposta,con,parameters);
		return resposta;
	}

	/**
	 * Metodo....:aplicaFiltros
	 * Descricao.:Aplica os filtros de conteudo existentes no conteudo sendo processado
	 * @param conteudo	- Conteudo sendo processado
	 * @param resposta  - Resposta a ser utilizada para anexar os filtros
	 * @param con		- Conexao com o banco de dados
	 * @param parameters - Parametros recebidos pela servlet
	 * @return Resposta	 - Resposta alternativa apos a aplicacao dos filtros
	 */
	private Resposta aplicaFiltros(Conteudo conteudo, Resposta respDefault, Connection con, Map parameters)
	{
		Resposta resposta = respDefault;
		// Verifica se o conteudo possui respostasFiltros. Caso positivo entao
		// realiza a aplicacao desses para os parametros para verificar se a resposta
		// deve ser anexada.
		if (conteudo.getRespostasFiltros().size() > 0)
		{
			// Busca as respostas anexas e verifica se existe alguma opcao. Caso positivo
			// entao um outro objeto resposta serah iniciado para conter tais valores, senao
			// a resposta default serah utilizada sozinha.
			String respostaAnexa = conteudo.getRespostasAnexas(con,parameters);
			String mensagemAnexa = conteudo.getMensagensAnexas(con, parameters);
			if ( respostaAnexa.indexOf("<option") > -1 || (mensagemAnexa != null && !"".equals(mensagemAnexa)) )
				resposta = criaRespostaAlternativa(conteudo,respDefault,respostaAnexa,mensagemAnexa);
		}
		return resposta;
	}

	/**
	 * Metodo....:criaRespostaAlternativa
	 * Descricao.:Cria um objeto resposta que sera alternativa a resposta default do conteudo
	 * @param conteudo		 - Conteudo a ser analisado
	 * @param respDefault    - Resposta default a ser utilizada para insercao das novas opcoes
	 * @param respostaAnexa  - Valor da resposta a ser anexado
	 * @param mensagemAnexa	 - Mensagem a ser anexada a resposta antes das opcoes
	 * @return Resposta		 - Objeto resposta contendo os novos valores
	 */
	private Resposta criaRespostaAlternativa(Conteudo conteudo, Resposta respDefault, String respostaAnexa, String mensagemAnexa)
	{
		StringBuffer buffResp = new StringBuffer(WIGWmlConstrutor.PROLOGUE_WML+"\n<wml>\n");
		// Busca todos os valores de Cards existentes na resposta default
		String cards[] = WIGWmlParser.getCardsFromWml(respDefault.getDescricaoResposta());
		// Para cada card existente busca-se somente no primeiro a existencia da tag da escolha de
		// opcoes <SELECT>. Caso essa tag nao exista entao uma serah criada para as respostas anexas
		// caso existir.
		for (int i=0; i < cards.length; i++)
		{
			// Somente o primeiro card eh alterado, os demais sera incluidos sem nenhuma
			// alteracao diretamente na nova resposta
			if (i == 0)
			{
				buffResp.append("<card id=\"m" + conteudo.getResposta().getCodigoResposta() + "\">\n");
				// Busca os paragrafos definidos dentro do card e para cada paragrafo realiza a 
				// verificacao da tag <SELECT> que serah utilizada para a resposta alternativa
				String parags[] = WIGWmlParser.getParagraphsFromCard(cards[i]);
				for (int j=0; j < parags.length; j++)
				{
					// As mensagens de midia que podem ser cadastradas na visualizacao do menu
					// de opcoes somente eh possivel no primeiro card do WML sendo processado.
					// Portanto no primeiro verifica se ha mensagem a ser anexada se caso existir
					// entao cria um paragrafo para ser utilizado antes de montar as opcoes
					if (j == 0 && mensagemAnexa != null && !"".equals(mensagemAnexa))
						buffResp.append(mensagemAnexa);
					
					String[] optionsSelect = WIGWmlParser.getElementoSelect(parags[j],respostaAnexa);
					if (optionsSelect.length > 0)
					{
						buffResp.append("<p>\n");
						for (int k=0; k < optionsSelect.length; k++)
							buffResp.append(optionsSelect[k]+"\n");
						buffResp.append("</p>\n");
					}
					else buffResp.append(parags[j]+"\n");
				}
				buffResp.append("</card>\n");
			}
			else
				buffResp.append(cards[i]);
		}
		buffResp.append("</wml>");
		// Cria o objeto resposta alternativo a resposta default
		Resposta resposta = new Resposta(conteudo.getResposta().getCodigoResposta());
		resposta.setDescricaoResposta(buffResp.toString());
		resposta.setAgrupaConteudo(false);
		resposta.setExecutaResposta(false);
		resposta.setTipoResposta(RespostaDAO.RESPOSTA_TIPO_WML);

		return resposta;
	}

	/**
	 * Metodo....:escreveConteudosAgrupados
	 * Descricao.:Este metodo realiza a escrita dos cartoes de conteudos agrupados que possivelmente
	 *            existam para este conteudo
	 * @param servico		- Servico a ser processado
	 * @param conteudo		- Conteudo a ser processado
	 * @param maxLength		- Tamanho maximo do WML a ser devolvido
	 * @param listaResposta	- Lista de respostas jah agrupados utilizados na recursividade da chamada
	 * @param wml			- String contendo o wml a ser devolvido
	 * @param Map			- Map contendo os parametros da querystring
	 * @param con			- Conexao de banco de dados para ser utilizada na validacao
	 */
	private void escreveConteudosAgrupados(Servico servico,
											Conteudo conteudo,
											Collection listaResposta,
											StringBuffer wml,
											Connection con,
											int maxLength,
											Map parameters) throws NaoSubiuTSDException
	{
		// Verifica se a lista de respostas eh nula. Se isso for verdade entao
		// esse conteudo eh o conteudo inicial que ira devolver o WML completo. Para
		// isso ele adiciona todas as respostas processadas na lista para serem passados
		// recursivamente para os conteudos evitando que uma reposta que jah esteja em um
		// conteudo superior seja adicionado repetidamente
		if (listaResposta == null)
			listaResposta = new LinkedHashSet();

		// Para cada elemento agrupado pega o card criado por este e anexa ao WML
		for (Iterator i=conteudo.getAgrupamentoConteudo().iterator(); i.hasNext();)
		{
			Conteudo conteudoAgrupado = (Conteudo)i.next();
			// Assim como o Conteudo inicial os conteudos agrupados tambem sao validados
			// e caso possuam uma resposta diferente da default esta serah utilizada
			Resposta resposta = validacaoConteudo(servico,conteudoAgrupado,con,parameters);
			// Verfica se a resposta jah existe na lista, se verdadeiro entao o
			// card contendo as informacoes jah foram adicionadas e nao ha necessidade
			// de se construir novamente tais informacoes. Entao processa somente respostas
			// que nao estao na lista
			if (!listaResposta.contains(resposta))
			{
				// Adiciona a resposta para indicar que para a proxima chamada que este jah foi processado
				// A resposta adicionada eh a do conteudo devido ser esta utilizada para montar o ID da tag
				// card e esta lista serve justamente para evitar que o mesmo card seja inserido mais de uma
				// vez
				listaResposta.add(conteudoAgrupado.getResposta());
				// Se o conteudo agrupado do atual conteudo tambem agrupa outros conteudos
				// entao busca o wml resultado desse conteudo e faz-se o parse dos cards existentes
				// se o conteudo nao agrupa entao a resposta dele eh encorporada ao wml atual
				if (resposta.agrupaConteudo())
					// Utiliza o metodo do parser para retornar todos os cards existentes na tag <WML> 
					wml.append(WIGWmlParser.getElementoWml(getWml(servico,conteudoAgrupado,con,maxLength,listaResposta,parameters)));
				else
					wml.append(getCard(conteudoAgrupado,resposta,maxLength));
			}
		}
	}

	/**
	 * Metodo....:getWml
	 * Descricao.:Este metodo constroi o WML que sera enviado para o assinante.
	 * @param servico	- Servico a ser utilizado
	 * @param conteudo	- Conteudo a ser utilizado
	 * @param con		- Conexao de banco de dados para utilizacao na validacao
	 * @param int		- Maximo tamanho da WML a ser construido
	 * @param Collection- Lista de respostas jah processados
	 * @param Map		- Map contendo os parametros da querystring
	 * @return String	- WML contendo a resposta para o assinante
	 */
	public String getWml(Servico servico, Conteudo conteudo, Connection con, int maxLength, Collection listaResposta,Map parameters)
	{
		// Define o inicio do WML a ser retornado
		StringBuffer wml = new StringBuffer(PROLOGUE_WML+"<wml>\n");
		try
		{
			// Realiza a validacao do conteudo definindo a resposta a ser utilizada
			// para este conteudo
			Resposta resposta = validacaoConteudo(servico,conteudo,con,parameters);
			// Busca o card do atual conteudo sendo processado utilizando a resposta do conteudo validado
			wml.append(getCard(conteudo,resposta,maxLength-wml.length()));
			// Caso o tipo de resposta desse conteudo seja para agrupar conteudos entao realiza
			// o agrupamento dos conteudos em um mesmo WML
			if (resposta.agrupaConteudo())
				// Realiza a iteracao dos conteudos agrupados e realiza o append das informacoes no buffer
				// passando a lista de respostas jah agrupados recebida como parametro
				// OBS: O primeiro conteudo no qual o WigControl esta chamando o metodo recebe a lista 
				// como um parametro nulo
				escreveConteudosAgrupados(servico,conteudo,listaResposta,wml,con,maxLength-wml.length(),parameters);
			
			// Define o fechamento do WML
			wml.append("</wml>");
		}
		catch(NaoSubiuTSDException e)
		{
			// Caso a excecao de nao subiu TSD acontecer entao o conteudo do WML
			// sera um WML especifico para a inicializacao do TSD do aparelho do 
			// assinante.
			wml = new StringBuffer(PROLOGUE_WML+WML_TSD);
		}
		return wml.toString();
	}
	
	/**
	 * Metodo....:getCard
	 * Descricao.:Retorna a tag CARD do WML de acordo com a resposta a ser oferecida
	 * @param conteudo	- Conteudo sendo processado
	 * @param resposta	- Resposta a ser processada
	 * @return String	- Tag Card a ser anexado ao WML
	 */
	public String getCard(Conteudo conteudo, Resposta resposta, int maxLength)
	{
		String cards[] = null;
		// Define o Card. O id sera o codigo da resposta (unico) para cada resposta
		// OBS: A resposta recebida como parametro sera utilizada para processamento
		//      porem devido ao menu se basear na resposta original (dentro do conteudo)
		//      entao esta resposta sera utilizada para montar o id do card
		StringBuffer card = new StringBuffer("<card id=\"m" + conteudo.getResposta().getCodigoResposta() + "\">\n");

		// Se o tipo da resposta for igual a 0 entao o conteudo da propriedade
		// descricaoResposta sera anexado ao card
		if (resposta.getTipoResposta() == 0)
			cards = WIGWmlParser.getCardsFromWml(resposta.getDescricaoResposta());

		// Para o tipo de resposta igual a 2 o conteudo da propriedade descricaoResposta
		// eh uma URL sendo entao executada a URL e a resposta eh anexada ao card
		// Caso o tamanho maximo parametrizado para envio de WML seja atingido entao o card
		// sera somente uma referencia para o JSP que respondera pela requisicao
		// Na tabela de resposta existe um parametro que independente do tamanho do WML indica
		// que a resposta sera executada ao ser anexada no card ou nao
		if (resposta.getTipoResposta() == 2)
		{
			String respostaURL = getURLResposta(resposta.getDescricaoResposta());
			if (respostaURL.length() < maxLength && resposta.executaResposta())
				cards = WIGWmlParser.getCardsFromWml(respostaURL);
			else
				card.append("<go href=\""+ resposta.getDescricaoResposta() + "\"/>\n");
		}

		// Para o tipo de resposta 3 nada eh acrescentado pois nesse tipo o conteudo ira
		// agrupar os outros conteudos para a formacao do WML
		if (resposta.getTipoResposta() == 3)
			card.append(WIGWmlParser.getElementoCard(getCardMenu(conteudo,resposta)));
		
		// Se existe cards entao o primeiro sera modificado pelo card atualmente sendo processado
		// porem o restante deve ser adicionado ao final do card principal
		if (cards != null)
		{
			// Adiciona o valor do primeiro card ao card principal fazendo o parse para retirar a tag <CARD>
			card.append(WIGWmlParser.getElementoCard(cards[0]) + "\n"+"</card>\n");
			// Adiciona os outros cards que existem na resposta apos o fechamento do card principal
			for (int i=1; i < cards.length; i++)
				card.append(cards[i]+"\n");
		}
		else
			// Define o fechamento do elemento card 
			card.append("</card>\n");

		return card.toString();
	}
	
	/**
	 * Metodo....:getCardMenu
	 * Descricao.:Retorna o card inicial contendo o menu para o conteudo que agrupa outros conteudos
	 * @return String - elemento card contendo o menu de opcoes para este conteudo
	 */
	private String getCardMenu(Conteudo conteudo, Resposta resposta)
	{
		StringBuffer card = new StringBuffer("<card><p>\n");
		// Realiza a iteracao entre os conteudos agrupados para a definicao
		// do menu de opcoes, sendo o codigo do conteudo a chave para os diferentes
		// cartoes a serem criados e a descricao do conteudo sera o texto para o menu
		card.append("<select name=\"op\">\n");
		for (Iterator i=conteudo.getAgrupamentoConteudo().iterator(); i.hasNext();)
		{
			Conteudo conteudoAgrupado = (Conteudo)i.next();
			card.append("<option value=\""+ conteudoAgrupado.getResposta().getCodigoResposta() +
				           "\" onpick=\"#m"+ conteudoAgrupado.getResposta().getCodigoResposta()+"\">" + 
					       conteudoAgrupado.getDescricaoConteudo() +
						"</option>\n");
		}
		card.append("</select>\n</p>\n</card>\n");
		return card.toString();
	}

	/**
	 * Metodo....:getURLResposta
	 * Descricao.:Chama a URL passada como parametro e realiza a leitura da execucao
	 * @param urlString	- URL a ser executada
	 */
	public String getURLResposta(String urlString)
	{
		StringBuffer resposta = new StringBuffer();
		try
		{
			// Acerta a URL passando como parametro o Msisdn do assinante. Para passar como
			// parametro primeiro verifica se algum parametro jah foi passado na URL pelo 
			// caracter '?' caso jah exista entao o parametro eh incluido na querystring
			// com o '&' senao ele vai com o caracter '?' indicando que eh o primeiro parametro
			char charParam = urlString.indexOf("?") == -1 ? '?' : '&';
			// Executa a URL passada como parametro e realiza a leitura da resposta.
			URL url = new URL(urlString+charParam+"MSISDN="+msisdn);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String retorno = null;
			while ( (retorno=br.readLine())!= null )
				resposta.append(retorno+"\n");
			
			br.close();
		}
		catch(Exception e)
		{
			return getWMLErro("Consulta indisponivel no momento");
		}
		return resposta.toString();
	}
	
	/**
	 * Metodo....:getWMLErro
	 * Descricao.:Cria um WML contendo alguma informacao de erro
	 * @param erro - Mensagem contendo o erro informando o problema
	 * @return String - WML contendo o erro
	 */
	public static String getWMLErro(String erro)
	{
		return getWMLInfo(erro);
	}
	
	/**
	 * Metodo....:getWMLInfo
	 * Descricao.:Cria um WML contendo alguma informacao
	 * @param info - Texto a ser utilizado como informacao no WML
	 * @return String - WML contendo o texto informativo
	 */
	public static String getWMLInfo(String info)
	{
		String wml = WIGWmlConstrutor.PROLOGUE_WML;
		wml += "<wml>\n<card>\n<p>\n" + info + "\n</p>\n</card>\n</wml>";
		return wml;
	}
	
	/**
	 * Metodo....:getWMLSM
	 * Descricao.:Retorna o WML de plugin para envio de SMS
	 * @param msisdn	- Msisdn destino da mensagem
	 * @param mensagem	- Mensagem a ser enviada
	 * @return WML plugin a ser enviado para o cliente que irah forcar o envio de um SM
	 */
	public static String getWMLSM(String msisdn, String mensagem)
	{
		StringBuffer wml = new StringBuffer(WIGWmlConstrutor.PROLOGUE_WML);
		wml.append("<wml>\n");
		wml.append("<wigplugin name=\"sendserversm\">\n"); 
		wml.append("<param name=\"userdata\" value=\"").append(mensagem).append("\"/>\n");
		wml.append("<param name=\"destaddress\" value=\"").append(msisdn).append("\"/>\n");
		wml.append("</wigplugin>\n");
		wml.append("<wigplugin name=\"noresponse\"/>\n");
		wml.append("<card>\n");
		wml.append("</card>\n");
		wml.append("</wml>");
		return wml.toString();
	}
}
