<%@ page language="java" contentType="text/xml; charset=ISO-8859-1" 
    import = "javax.naming.*, java.util.*, java.text.* "
    import = "java.sql.Connection, br.com.brasiltelecom.wig.entity.EstoqueMicrosiga"
    import = "br.com.brasiltelecom.wig.dao.EstoqueMicrosigaDAO, javax.sql.DataSource"
    import = "javax.servlet.ServletException, javax.servlet.http.HttpServlet"
	import = "javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse"
	import = "br.com.brasiltelecom.wig.action.WIGWmlConstrutor"
    errorPage=""
%><%!
	private InitialContext ictx = null;
	private static final long serialVersionUID = 1L;
	public void jspInit()
	{
		try
		{
			ictx = new InitialContext();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
%><%
	// Dados para conexão com o Banco de Dados
	DataSource ds = (DataSource) ictx.lookup("java:/comp/env/jdbc/WPP_WIGC");
	Connection con = ds.getConnection();
	
	// Criacao do objeto estoque
	EstoqueMicrosiga estoque 		= new EstoqueMicrosiga();

	// Instância da classe EstoqueMicrosigaDAO, 
	// para acesso ao Banco de Dados
	EstoqueMicrosigaDAO consulta 	= new EstoqueMicrosigaDAO();
	
	// Coleção para armazenar os vários objetos estoque
	Collection colecaoEstoque 		= null;
	
	// Variável que contém a variável do 
	// WIGControl dentro da plataforma OTA
	String WIGControl = (String)application.getAttribute("VariavelWIGControl");

	// Verifica se algun dos parâmetros está em branco e,
	// caso sim, retorna uma mensagem de "Dados incorretos"
	if ( (request.getParameter("canal").equals("")) ||
		 (request.getParameter("tipo").equals("")) ||
		 (request.getParameter("dado").equals("")) )
	{%><%=WIGWmlConstrutor.PROLOGUE_WML%>
		<wml>
			<card id="Erro">
				<p>
					Os dados enviados estao incorretos. <br/>
					Clique OK e tente novamente.
					<do type="accept">
        				<go href="<%=WIGControl%>?bts=2&amp;btc=7005"/>
    				</do>
				</p>
			</card>
		</wml>
  <%}
	// Caso os parâmetros estejam ok,
	// segue o fluxo para a consulta
	else
	{
		// Armazenameno dos parâmetros nas respectivas variáveis
		int canal = Integer.parseInt(request.getParameter("canal"));
		String tipo = request.getParameter("tipo");
		String dado = request.getParameter("dado");
		
		// Verificação do tipo de consulta
		// M > Modelo
		// S > Simcard
		if (tipo.equalsIgnoreCase("M"))
		{
			// Consulta no estoque considerando o nome do produto
			colecaoEstoque = consulta.findByModel(canal,dado,con);
			con.close();
		}
		else
		{
			// Consulta no estoque considerando o Código do produto
			colecaoEstoque = consulta.findByCode(canal,Integer.parseInt(dado),con);
			con.close();
		}
	%><%=WIGWmlConstrutor.PROLOGUE_WML%>
		<wml>
			<card id="Result">
				<p>
					<%
					
					// Verifica se há algum objeto contido na coleção, caso não,
					// retorna uma mensagem de "Item não encontrado"
					if (colecaoEstoque.isEmpty())
					{%>
					Item nao encontrado. <br/>
					Clique OK e tente novamente.
					<do type="accept">
        				<go href="<%=WIGControl%>?bts=2&amp;btc=7005"/>
    				</do>
					<%}
					
					// Caso exista algum item na coleção,
					// os mesmos serão mostrados a seguir
					else
					{%>
					Resultado: <br/>
					<%
					
					// Loop para mostrar todos os resultados contidos
					// na coleção de objetos
					boolean primeiro = true;
					for (Iterator it = colecaoEstoque.iterator(); it.hasNext();)
					{
						estoque = (EstoqueMicrosiga) it.next();
						
						// Mostrando o Nome (Mnemônico) do produto pesquisado
						// Apenas um produto por consulta
						if(primeiro){
					%>
					    
					    Codigo: <%=estoque.getCodAparelho()%> <br/>
					    <%=tipo.equalsIgnoreCase("M")?"Modelo: ":"Simcard: "%><%=estoque.getIdtMnemonico()%> <br/>
					<%
							primeiro = false;
						}
						// Mostrando o Centro de Distribuição e a
						// quantidade do produto em estoque no local
					%>
						Estoque<%=estoque.getCd()%>:<%=estoque.getQtdLivre()%><br/>
				  <%}
					// Abaixo, seguem as opções de:
						// > Nova Consulta
						// > Sair
				  %>
					Clique OK
					<select title="Cons. Estoque">
						<option onpick="<%=WIGControl%>?bts=2&amp;btc=7005">Nova Consulta</option>
						<option>Sair</option>
					</select>
				  <%}%>
				</p>
			</card>
		</wml>
<%	}
%>