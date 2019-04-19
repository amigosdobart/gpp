package br.com.brasiltelecom.ppp.action.cadastroPrecoAparelho;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.exolab.castor.jdo.Database;
import org.exolab.castor.jdo.PersistenceException;

import br.com.brasiltelecom.ppp.action.base.ActionPortal;
import br.com.brasiltelecom.ppp.home.CodigoNacionalHome;
import br.com.brasiltelecom.ppp.home.ModeloSAPHome;
import br.com.brasiltelecom.ppp.home.PrecoAparelhoHome;
import br.com.brasiltelecom.ppp.model.RegistroPrecoAparelho;
import br.com.brasiltelecom.ppp.portal.entity.CodigoNacional;
import br.com.brasiltelecom.ppp.portal.entity.PrecoAparelho;
import br.com.brasiltelecom.ppp.portal.servlet.Constantes;

/**
 * Efetua o cadastro de preços de aparelho na base
 * @author Marcelo Alves Araujo
 * @since 13/11/2006
 */
public class CadastroPrecoAparelho extends ActionPortal
{	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#getOperacao()
	 */
	public String getOperacao()
	{
		return Constantes.COD_CADASTRAR_PRECO_APARELHO;
	}
	
	/**
	 * @see br.com.brasiltelecom.ppp.action.base.ActionPortal#performPortal(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.exolab.castor.jdo.Database)
	 */
	public ActionForward performPortal(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response, Database db) throws Exception
	{
		try
		{
			if(carregarArquivoPreco(request))
			{		
				// Arquivo de leitura dos dados			
				FileReader arquivo = new FileReader(servlet.getServletContext().getRealPath("/WEB-INF") + java.io.File.separator + Constantes.ARQUIVO_IMPORTACAO_PRECO + Constantes.EXTENSAO_TXT);
				BufferedReader canalLeitura = new BufferedReader(arquivo);
				
				FileOutputStream arquivoSaida = new FileOutputStream(servlet.getServletContext().getRealPath("/WEB-INF")+java.io.File.separator + Constantes.ARQUIVO_ERRO_IMPORTACAO + Constantes.EXTENSAO_TXT);
				
				String registroAparelho = null;
				
				// Pega os códigos nacionais da Brasil Telecom
				db.begin();
				Collection codigosNacionais = CodigoNacionalHome.findAllBrt(db);
				db.commit();
				
				while((registroAparelho = canalLeitura.readLine()) != null)
				{
					// Transforma o registro em objeto
					RegistroPrecoAparelho dadosAparelho = new RegistroPrecoAparelho(registroAparelho);
					
					// Cadastra os preços para todos os estados
					if(	Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao4()) ||
						Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao5()) ||	
						Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao6()))
					{
						String registroErro = cadastrarPrecoAparelho(db, dadosAparelho, codigosNacionais);
						
						// Caso haja erro no arquivo de saída
						if(registroErro != null)
							arquivoSaida.write(registroErro.getBytes());
					}
				}
				
				arquivoSaida.close();
				
				return actionMapping.findForward(Constantes.STATUS_SUCESSO);
			}
			
			request.setAttribute(Constantes.MENSAGEM, "O arquivo não existe!");
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			request.setAttribute(Constantes.MENSAGEM, "Arquivo fora do padrão!");
		}		
		
		return actionMapping.findForward(Constantes.STATUS_ERRO);		
	}
	
	/**
	 * Carrega o arquivo na máquina de produção
	 * @param request Requisição do Servlet
	 * @return sucesso true caso haja sucesso e false em caso contrário
	 */
	private boolean carregarArquivoPreco(HttpServletRequest request)
	{
		boolean sucesso = false;
		try
		{
			//Testa se o arquivo existe
			if(FileUploadBase.isMultipartContent(request))
			{
				//Carrega o arquivo
				DiskFileUpload upload = new DiskFileUpload();
				List lista = upload.parseRequest(request);
				//Percorre a lista de itens do arquivo
				Iterator i = lista.iterator();
				while (i.hasNext()) 
				{
					FileItem item = (FileItem) i.next();
				    
					// Salva em outro arquivo o que foi lido
				    if(!item.isFormField())
				    {
				    	InputStream file = item.getInputStream();
				    	if (file.available() > 0)
				    	{
					    	FileOutputStream output = new FileOutputStream(servlet.getServletContext().getRealPath("/WEB-INF")+java.io.File.separator + Constantes.ARQUIVO_IMPORTACAO_PRECO + Constantes.EXTENSAO_TXT);
					    	
					    	byte buffer[] = new byte[1024];
					    	int c = 0;
					    	
					    	// Grava o arquivo no servidor para leitura
					    	while((c = file.read(buffer)) > 0)
					    		output.write(buffer, 0, c);
					    	
					    	file.close();
					    	output.close();
					    	sucesso = true;
				    	}
				    }
				}
			}				
		}
		catch (Exception e) 
		{
			sucesso = false;
		} 
		
		return sucesso;
	}
	
	/**
	 * Testa a validade dos dados, insere no banco e atualiza registros antigos
	 * @param db 				Instância do banco de dados
	 * @param dadosAparelho		Dados de preço de aparelhos
	 * @param dataAbertura		Data do novo período de vigência
	 * @param dataFechamento	Data de fim do período anterior de vigência
	 * @param listaCN			Lista de CN da BrT
	 * @return erroRegistro 	null caso haja sucesso e o registro a ser inserido caso haja erro
	 * @throws PersistenceException 
	 * @throws NumberFormatException 
	 */
	private String cadastrarPrecoAparelho(Database db, RegistroPrecoAparelho dadosAparelho, Collection listaCN) throws NumberFormatException, PersistenceException
	{
		String erroRegistro = null;
		
		// Testa se o código sap possui um registro associado na base do GPP
		if(ModeloSAPHome.findByCodigoSAP(db, Integer.parseInt(dadosAparelho.getCodigoSAP())) != null)
		{
			// Pega a data anterior à nova data de vigência
			Calendar dataFechamento = Calendar.getInstance();
			dataFechamento.setTime(dadosAparelho.getDataInicioVigencia());
			dataFechamento.add(Calendar.DAY_OF_MONTH, -1);
			
			// Busca os CN para o qual se aplicam os preços
			Iterator cnBrt = listaCN.iterator();
			
			// Para cada CN registra o novo preço
			while(cnBrt.hasNext())
			{				
				int codigoNacional = ((CodigoNacional)cnBrt.next()).getIdtCodigoNacional();
				
				// Fecha o período anterior e abre o novo período de acordo com a CN
				if(codigoNacional/10 == 4 && Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao4()))
					atualizarRegistro(db, dadosAparelho, dataFechamento.getTime(), codigoNacional, Double.parseDouble(dadosAparelho.getPrecoRegiao4()));
				else if(codigoNacional/10 == 5 && Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao5()))
					atualizarRegistro(db, dadosAparelho, dataFechamento.getTime(), codigoNacional, Double.parseDouble(dadosAparelho.getPrecoRegiao5()));
				else if(codigoNacional/10 == 6 && Constantes.IND_MODIFICA_PRECO.equals(dadosAparelho.getModificaRegiao6()))
					atualizarRegistro(db, dadosAparelho, dataFechamento.getTime(), codigoNacional, Double.parseDouble(dadosAparelho.getPrecoRegiao6()));
			}
		}
		else
			// Salva o registro de aparelho não encontrado
			erroRegistro = dadosAparelho.getRegistro() + "\n";
		
		return erroRegistro;
	}	
	
	
	/**
	 * Testa a validade dos dados, insere no banco e atualiza registros antigos
	 * @param db 				Instância do banco de dados
	 * @param dadosAparelho		Dados de preço de aparelhos
	 * @param dataAbertura		Data do novo período de vigência
	 * @param dataFechamento	Data de fim do período anterior de vigência
	 * @param codigoNacional	Código nacional
	 * @param precoAparelho		Preço do aparelho para o código nacional específico
	 * @return erroRegistro 	null caso haja sucesso e o registro a ser inserido caso haja erro
	 * @throws PersistenceException 
	 * @throws NumberFormatException 
	 */
	private void atualizarRegistro(Database db, RegistroPrecoAparelho dadosAparelho, Date dataFechamento, int codigoNacional, double precoAparelho) throws NumberFormatException, PersistenceException
	{
		// Pega o preço atual
		db.begin();
		PrecoAparelho preco = PrecoAparelhoHome.findByCodigoSAP(db, Integer.parseInt(dadosAparelho.getCodigoSAP()), codigoNacional);
		
		// Fecha o período atual
		if(preco == null)
		{
			db.commit();
			preco = new PrecoAparelho();
			preco.setCodigoNacional(codigoNacional);
			preco.setCodigoSAP(Integer.parseInt(dadosAparelho.getCodigoSAP()));
			preco.setDataFimValidade(null);
			preco.setDataInicioValidade(dadosAparelho.getDataInicioVigencia());
			preco.setValorPreco(precoAparelho);			
			PrecoAparelhoHome.inserirPrecoAparelho(db, preco);
		}
		else if(!preco.getDataInicioValidade().equals(dadosAparelho.getDataInicioVigencia()))
		{
			preco.setDataFimValidade(dataFechamento);
			db.commit();
			
			// Insere o novo preço do aparelho
			preco.setDataInicioValidade(dadosAparelho.getDataInicioVigencia());
			preco.setDataFimValidade(null);
			
			// Insere novo registro
			preco.setValorPreco(precoAparelho);
			PrecoAparelhoHome.inserirPrecoAparelho(db, preco);
		}
		else
			db.commit();
	}	
}