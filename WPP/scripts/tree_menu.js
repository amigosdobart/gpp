<!--

   /*
	* TreeMenu 
	*
	* Criado por: Bernardo Vergne Dias
	* Data: 17/01/2007
	* Versao: 1.0
	* Compatibilidade: IE 7, Firefox 1.0 
	*
 	* Descricao: Menu em forma de árvore (multiníveis) com suporte a itens Checkbox
 	* Uso: Dentro de formularios HTML nos quais a entrada de dados é um conjunto de opcoes agrupadas em níveis hierarquicos
 	* 
	* Exemplo: Para utilizar o TreeMenu importe esse JS em sua página (mais o arquivo CSS) e utilize-o como abaixo:
	*
	*  <form method="?" action="?">
	* 	  <script>renderTreeMenu(dados);<//script>
	* 	  <!-- demais campos -->
	*  </form>
	*
	* A instrucao renderTreeMenu(dados) irá escrever dentro do Form o TreeMenu. 
	* O parametro 'dados' é uma string XML que descreve o menu, conforme o modelo:
	*
	* var dados = "<menu id='?'>"
	* +"  <opcao texto='Item 1' nome='?' valor='?' />"
	* +"  <opcao texto='Item 2' nome='?' valor='?' ativo='true' />"
	* +"  <opcao texto='Item 3' nome='?' valor='?' />"
	* +"  <grupo texto='Item 4'>"
	* +"      <opcao texto='Item 4.1' nome='?' valor='?' />"
	* +"  	  <opcao texto='Item 4.2' nome='?' valor='?' />"
	* +"  	  <grupo texto='Item 4.3'>"
	* +"    	    <opcao texto='Item 4.3.1' nome='?' valor='?' ativo='true' />"
	* +"  		    <opcao texto='Item 4.3.2' nome='?' valor='?' ativo='true' />"
	* +" 	  </grupo>"
	* +"  </grupo>"
	* +"  <opcao texto='Item 5' nome='?' valor='?' />"
	* +"</menu>";
	*
	* Observe que um menu possui um 'id' e uma colecao de itens. Cada item pode ser do tipo 'opcao' ou do
	* tipo 'grupo'.
	*
	* 'opcao' representa um item do menu com um campo checkbox do formulario. Os atributos 'name' e 'value' 
	* estao mapeados em 'nome' e 'valor' no XML. Opcionalmente pode-se definir ativo='true' para que o 
	* campo inicie marcado por padrao.
	*
	* 'grupo' representa um item de agrupamento do menu (usuário pode abrir e fechar). Todo grupo pode conter
	* mais grupos e/ou opcoes.
	*
	* O atributo 'texto' é o texto de exibicao do item de menu.
	*
	* Veja o arquivo tree_menu_sample.htm para visualizar um exemplo funcional.
	*
	* Variaveis de configuraçao:
	* 
	* NOME				DESCRICAO				TIPO			EXEMPLO
	* treeMenuImgMais 	: url da figura 'mais'  String			treeMenuImgMais = 'img/plus.gif';
	* treeMenuImgMenos 	: url da figura 'menos' String			treeMenuImgMais = 'img/minus.gif';
	*
	* 
	* Alguns metodos podem user usados na pagina para fazer validacao e outras operacoes comuns:
	*
	* limparGrupoTreeMenu       -> limpa todos os itens de um grupo do menu
	* limparTreeMenu	        -> limpa todo o menu
	* validarGrupoTreeMenu      -> retorna true se pelo menos 1 item dentro do grupo especificado está marcado
	* validarTreeMenu	   		-> retorna true se pelo menos 1 item do menu está marcado
	*
	* OBS: vide comentario das funcoes acima no final do arquivo
	*/	

	// -------- CONFIGURACAO -------- //

	treeMenuImgMais = "img/tree_menu_mais.gif";
	treeMenuImgMenos = "img/tree_menu_menos.gif";
	
	// -------- METODOS DE REDENRIZACAO -------- //

	/*
	* Inicia a redenrizacao do menu.
	* xmlString: string XML que representa a estrutura do menu (vide exemplo)
	* Pode-se utilizar várias instancias do menu, desde que o ID sejam diferentes.
	* NAO UTILIZAR hifen ou virgulas no ID !
	*/
	function renderTreeMenu(xmlString)
	{
		var xml;
		
		// Parse XML (para IE)
		if (window.ActiveXObject)
		{
			xml = new ActiveXObject("Microsoft.XMLDOM");
			xml.async = "false";
			xml.loadXML(xmlString);
		}
		
		// Parse XML (Mozilla, Firefox, Opera)
		else
		{
			parser = new DOMParser();
			xml = parser.parseFromString(xmlString, "text/xml");
		}
		
		/* Captura a raiz do XML */
		var root = xml.documentElement;

		var idMenu = root.getAttribute("id");
		
		/* Cria o DIV principal do menu e redenriza os nodos do XML */
		treeMenuWrite("<div id='?' class='treeMenu'>", new Array(idMenu));
 		renderChilds(root.childNodes, idMenu);
		treeMenuWrite("</div>", null);
		
		treeMenuAtualizar(idMenu);
	}
	
	/*
	* Redenriza a lista de nodos XML.
	* 'nodes'	: array de nodos XML
	* 'id' 		: prefixo dos elementos HTML a serem gerados
	*/
	function renderChilds(nodes, id)
	{
		var index = 1;
		
		for (var j = 0; j < nodes.length; j++)
		{
			if (nodes[j].nodeName == "opcao")
			{
				renderOpcao(nodes[j], id + "," + index++);
			}
			else if (nodes[j].nodeName == "grupo")
			{
				renderGrupo(nodes[j], id + "," + index++);
			}
		}
	}
	
	/*
	* Redenriza um item do tipo 'opcao' do treeMenu.
	* 'item' 	: elemento do XML
	* 'id' 		: prefixo dos elementos HTML a serem gerados
	*
	* Estrutura básica:
	* <div id=xxx_opcao>
	*   <input id=xxx_campo type=checkbox>
	*   <a>Nome da Opcao</a>
	* </div>  
	*
	* xxx = Nome do menu | underline | hierarquia do item na arvore (ex: 4,1,3)
	*/
	function renderOpcao(item, id)
	{
		var divId = id + "_opcao";
		var inputId = id + "_campo";
		var inputName = item.getAttribute("nome");
		var inputValue = item.getAttribute("valor");
		var inputChecked = item.getAttribute("ativo") == "true" ? "checked": "";
		var inputOnclick = "treeMenuAtualizarGruposSup(\""+id+"\")";
		var aHref = "javascript:treeMenuToogleOpcao(\""+id+"\")";
		var aText = " " + item.getAttribute("texto");
		
		var inputParams = new Array(inputId, inputName, inputValue, inputChecked, inputOnclick);
	
		treeMenuWrite("<div id='?' class='opcao' >", new Array(divId));
		treeMenuWrite(" <input type=checkbox id='?' name='?' value='?' ? onclick='?'>", inputParams);
		treeMenuWrite(" <a href='?'>?</a>",new Array(aHref, aText));
		treeMenuWrite("</div>", null);
	}

	/*
	* Redenriza um item do tipo 'grupo' do treeMenu (colecao de opcoes e outros grupos)
	* 'grupo' 	: elemento do XML
	* 'id' 		: prefixo dos elementos HTML a serem gerados
	*
	* Estrutura básica:
	* <div id=xxx_grupo>
	*   <a><img id=xxx_imagem src=mais.png></a>
	*   <div><input id=xxx_campo type=checkbox><div>
	*   <a>Nome do Grupo</a>
	*   <div id=xxx_grupoContainer>
	*     lista de opcoes e grupos
	*   </div>
	* </div>  
	*
	* xxx = Nome do menu | underline | hierarquia do item na arvore (ex: 4,1,3)
	*/	
	function renderGrupo(grupo, id)
	{
		var divId = id + "_grupo";
		var aHref = "javascript:treeMenuToogleGrupo(\""+id+"\")";
		var aText = " " + grupo.getAttribute("texto");
		var imgId = id + "_imagem";
		var imgSrc = treeMenuImgMais;
	 	var inputId = id + "_campo";
		var inputOnclick = "treeMenuAplicaOpcaoGrupo(\""+id+"\")";
		var containerId = id + "_grupoContainer";

		treeMenuWrite("<div id='?' class='grupo' >", new Array(divId));
		treeMenuWrite(" <a href='?'><img id='?' border='0' src='?'></a>",new Array(aHref, imgId, imgSrc));
		treeMenuWrite(" <span><input type=checkbox id='?' onclick='?'></span>", new Array(inputId, inputOnclick));
		treeMenuWrite(" <a href='?'>?</a>",new Array(aHref, aText));
		treeMenuWrite(" <div id='?' style='display:none' class='container'>", new Array(containerId));
		
		renderChilds(grupo.childNodes, id);
		
		treeMenuWrite(" </div>", null);
		treeMenuWrite("</div>", null);
	}
	
	// -------- METODOS DE USO GERAL -------- //
	
	/* Retorna o id real dado o id de um elemento HTML.
	   O id de um elemento HTML é o id real mais um 
	   sufixo para identificar o tipo de elemento */
	   	
	function getRealId(id) 
	{
		return id.substring(0, id.lastIndexOf("_"));
	}
	
	/* Retorna o id do elemento pai (um grupo) */
	
	function getParentId(id) 
	{
		var parentId = id.substring(0, id.lastIndexOf(","));
		if (parentId.lastIndexOf(",") < 0) return null;
		
		return parentId;
	}
	
	/* Insere os paramentros na string */
	
	function bind(str, params)
	{
		tokens = str.split("?");
		buff = "";
		
		for (var i = 0; i < tokens.length - 1; i++)
		{
			buff += tokens[i] + params[i];
		}
		
		buff += tokens[tokens.length - 1];
		return buff;
	}
	
	/* Escreve um codigo HTML parametrizado */
	
	function treeMenuWrite(str, params)
	{
		document.writeln(bind(str, params));
	}
	
	// -------- METODOS DISPARADOS POR EVENTOS DO USUARIO -------- //
	
	/* Alterna exibicao de um grupo. */
	
	function treeMenuToogleGrupo(id) 
	{
		var div = document.getElementById(id + "_grupoContainer");
		var img = document.getElementById(id + "_imagem");
		
		if (div.style.display == "none")
		{
			div.style.display = "block";
			img.src = treeMenuImgMenos;
		}
		else
		{
			div.style.display = "none";
			img.src = treeMenuImgMais;
		}
	}
	
	/* Alterna o campo checkbox da opcao */
	
	function treeMenuToogleOpcao(id) 
	{
		var chk = document.getElementById(id + "_campo");
		
		if (chk.checked == true) 
			chk.checked = false;
		else
			chk.checked = true;
			
		treeMenuAtualizarGruposSup(id);
	}
	
	/* Atualiza o campo checkbox dos grupos de hierarquia superior,
	   incluindo o grupo especificado no id */	

	function treeMenuAtualizarGruposSup(id)
	{
		var parentId = getParentId(id);
		if (parentId == null) return;
		
		var itens = document.getElementById(parentId + "_grupoContainer").childNodes;
		var chk = document.getElementById(parentId + "_campo");
		
		treeMenuAtualizarGrupo(itens, chk);
		
		treeMenuAtualizarGruposSup(parentId);
	}

	/* Atualiza o campo checkbox dos grupos de hierarquia inferior,
	   incluindo o grupo especificado no id */
			
	function treeMenuAtualizarGruposInf(id) 
	{
	
		var itens = document.getElementById(id + "_grupoContainer").childNodes;
		var chk = document.getElementById(id + "_campo");
	
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			if (itens[i].getAttribute("id") && itens[i].getAttribute("id").indexOf("grupo") > 0)
			{
				treeMenuAtualizarGruposInf(getRealId(itens[i].id));
			}					
		}
		
		treeMenuAtualizarGrupo(itens, chk);
	}

	/* Atualiza o campo checkbox ('chkbox') de acordo com o 
	   status (marcado/desmarcado/semi marcado) dos 'itens' */
	
	function treeMenuAtualizarGrupo(itens, chkbox) 
	{
		var marcados = 0;
		var desmarcados = 0;
		var idAtual;
		var campoAtual;
		
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			idAtual = itens[i].getAttribute("id");
			
			if (idAtual != null)
			{
				if (idAtual.indexOf("opcao") > 0 || idAtual.indexOf("grupo") > 0)
				{
					campoAtual = document.getElementById(getRealId(idAtual) + "_campo");
					if (campoAtual.checked) marcados++;
					else desmarcados++;
		
					if (campoAtual.parentNode.className == "semicheck") desmarcados++;
				}
			}	
		}
		
		if (marcados == 0)
		{
			chkbox.checked = false;
			chkbox.parentNode.className = "";	
		}
		else if (desmarcados == 0)
		{
			chkbox.checked = true;
			chkbox.parentNode.className = "";
		}
		else
		{
			chkbox.checked = true;
			chkbox.parentNode.className = "semicheck";
		}	
	}	

	/* Aplica o valor do grupo especificado no 'id' para todos 
	   os campos internos e atualiza o checkbox dos grupos
	   de hierarquia superior */
	
	function treeMenuAplicaOpcaoGrupo(id) 
	{
		treeMenuAplicaOpcaoGruposInf(id);		
		treeMenuAtualizarGruposSup(id);
	}
	
	/* Aplica o valor do grupo especificado no 'id' para todos 
	   os campos internos, incluindo o do 'id' */

	function treeMenuAplicaOpcaoGruposInf(id)
	{
		var itens = document.getElementById(id + "_grupoContainer").childNodes;
		var chk = document.getElementById(id + "_campo");
		var idAtual;
		
		chk.parentNode.className = "";
		
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			idAtual = itens[i].getAttribute("id");
			
			if (idAtual != null)
			{
				if (idAtual.indexOf("opcao") > 0 || idAtual.indexOf("grupo") > 0)
				{
					campoAtual = document.getElementById(getRealId(idAtual) + "_campo");
					campoAtual.checked = chk.checked;
					
					if (idAtual.indexOf("grupo") > 0) treeMenuAplicaOpcaoGruposInf(getRealId(idAtual));
				}
			}					
		}
	}
	
	/* 
	* Inicializa o treeMenu.
	* Acao1: atualiza o campo checkbox ('chkbox') de todos os grupos da hierarquia
	*/	
	function treeMenuAtualizar(idMenu)
	{
		var itens = document.getElementById(idMenu).childNodes;
		var idAtual;
		
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			idAtual = itens[i].getAttribute("id");
		
			if (idAtual != null && idAtual.indexOf("grupo") > 0)
				treeMenuAtualizarGruposInf(getRealId(idAtual));	
		}	
	}
	
	/* 
	* Marca/desmarca todos os itens do menu
	*/	
	function defineEstadoTreeMenu(idMenu, marcar)
	{
		var itens = document.getElementById(idMenu).childNodes;
		var idAtual;
		
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			idAtual = itens[i].getAttribute("id");
		
			if (idAtual != null)
			{
				if (idAtual.indexOf("opcao") > 0 || idAtual.indexOf("grupo") > 0)
				{
					campoAtual = document.getElementById(getRealId(idAtual) + "_campo");
					campoAtual.checked = marcar;
					
					if (idAtual.indexOf("grupo") > 0) treeMenuAplicaOpcaoGruposInf(getRealId(idAtual));
				}
			}	
		}	
	}
	
	// -------- API DO TREEMENU - METODOS PARA USO DENTRO DA PAGINA -------- //
	
	/* 
	* Desmarca todos os itens do menu
	*/	
	function limparTreeMenu(idMenu)
	{
		defineEstadoTreeMenu(idMenu, false);
	}
	
	/* 
	* Marca todos os itens do menu
	*/	
	function marcarTreeMenu(idMenu)
	{
		defineEstadoTreeMenu(idMenu, true);
	}
	
	/** 
	* Desmarca todos os itens de um grupo do menu
	*
	* @param nivel : Parametro que especifica a posicao do grupo na hierarquia. 
	* Exemplo: nivel = "2,3" significa o grupo na posicao 2.3 do menu (veja seta abaixo).
	*
	* Menu:
	*   item 1 (opcao)
	*   item 2 (grupo)
	*     item 2.1 (opcao)
	*     item 2.2 (opcao) 
	*     item 2.3 (grupo)  <<<-------
	*		 item 2.3.1 (opcao)
	*		 item 2.3.2 (opcao)
	*     item 2.4 (opcao)
	*	item 3 (opcao)
	*
	*/	
	function limparGrupoTreeMenu(idMenu, nivel)
	{
		limparTreeMenu(idMenu + "," + nivel + "_grupo");
	}

	/* 
	*  retorna true se pelo menos 1 item do menu está marcado
	*/	
	function validarTreeMenu(idMenu)
	{
		var itens = document.getElementById(idMenu).childNodes;
		var idAtual;
		var validado = false;
		
		for (var i = 0; i < itens.length; i++)
		{
			if (itens[i].nodeType !== 1) continue;
			idAtual = itens[i].getAttribute("id");
		
			if (idAtual != null)
			{
				if (idAtual.indexOf("opcao") > 0 || idAtual.indexOf("grupo") > 0)
				{
					campoAtual = document.getElementById(getRealId(idAtual) + "_campo");
					if (campoAtual.checked)
					{
						validado = true;
						break;
					} 
				}
			}	
		}
		
		return validado;	
	}
	
	/** 
	* retorna true se pelo menos 1 item dentro do grupo especificado está marcado
	*
	* @param nivel : Parametro que especifica a posicao do grupo na hierarquia. 
	* Exemplo: nivel = "2,3" significa o grupo na posicao 2.3 do menu (veja seta abaixo).
	*
	* Menu:
	*   item 1 (opcao)
	*   item 2 (grupo)
	*     item 2.1 (opcao)
	*     item 2.2 (opcao) 
	*     item 2.3 (grupo)  <<<-------
	*		 item 2.3.1 (opcao)
	*		 item 2.3.2 (opcao)
	*     item 2.4 (opcao)
	*	item 3 (opcao)
	*
	*/	
	function validarGrupoTreeMenu(idMenu, nivel)
	{
		return validarTreeMenu(idMenu + "," + nivel + "_grupo");
	}	
-->