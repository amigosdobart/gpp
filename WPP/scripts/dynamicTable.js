function dynamicTable(idTable,doc)
{
	// Define as propriedades do objeto
	this.idTable 	= idTable;
	this.doc		= doc;
	this.rows		= new Array();
	
	// Define os metodos do objeto
	this.insertRow=insertRow;
	this.removeLastRow=removeLastRow;
	this.getValues=getValues;
}

// Essa funcao serah utilizada para inserir uma linha
// na tabela. O argumento necessario eh um array de valores
// contendo as celulas que serao inseridas, ou seja os elementos
// Cada elemento no array serah inserido em uma celula
function insertRow(cellArray,keys)
{
	// Verifica se o campo chave da linha jah existe
	// com o valor especificado. Caso verdadeiro entao
	// nenhuma linha eh criada, caso contrario a linha
	// eh inserida na tabela
	if (!exists(this.doc,keys,this.rows))
	{
		// Primeiro pega a referencia da tabela
		var tbl = this.doc.getElementById(this.idTable);
		var row = tbl.insertRow(tbl.rows.length);
		// Realiza a iteracao entre os elementos que deverao
		// ser inseridos na tabela. No final armazena a linha
		// em um vetor no objeto
		for (var i=0; i < cellArray.length; i++)
		{
			var cell = row.insertCell(i);
			cell.appendChild(cellArray[i]);
		}
		this.rows.push(cellArray);
	}
}

// Essa funcao retorna um boolean para indicar se
// o campo passado como parametro existe e possui
// o valor tambem indicado como parametro
function exists(doc,keys,tRows)
{
	// Esse procedimento abaixo eh para contar quantos
	// campos existem definidos na chave. Isso eh feito
	// dessa forma pois o keys.length sempre retorna zero
	var keysLength=0;
	for (key in keys)
		keysLength++;

	var count=0;
	// Realiza uma pesquisa no array de linhas existentes
	// para verificar se uma determinada linha possui
	// todos os valores que estao sendo inseridos
	for (var i=0; i < tRows.length; i++)
	{
		var collection=tRows[i];
		// Zera o contado para processar somente a linha
		count = 0;
		for (var j=0; j < collection.length; j++)
		{
			// Para cada linha da tabela, verifica se os valores
			// sao iguais ao do vetor. Se o numero de campos que
			// sao iguais batem com o numero de campos das chaves
			// entao a linha jah existe
			if (collection[j].name != null && (collection[j].value == keys[collection[j].name]))
				count++;
				
			if (count == keysLength)
				return true;
		}
	}
		
	return false;
}

// Essa funcao remove a ultima linha de uma tabela
// sendo que a primeira linha sempre eh mantida devido
// poder ser uma linha de cabecalho.
function removeLastRow()
{
  	var tbl = this.doc.getElementById(this.idTable);
  	if (tbl.rows.length > 1)
	  	tbl.deleteRow(tbl.rows.length - 1);
	
	// Apos a remocao da ultima linha da tabela
	// realiza a remocao do objeto no array de celulas
	// retirando o ultimo objeto inserido
	this.rows.pop();
}

// Essa funcao retorna os valores existentes na tabela
// em forma de parametros da forma campo=valor separando
// estes com o & para serem enviados via http para o servidor
function getValues()
{
	var values = "";
	for (var i=0; i < this.rows.length; i++)
		values = values + getValuesFromArray(this.rows[i]);
	
	return values;
}

function createInputElement(doc,tipo,nome,valor,tamanho,desabilitado)
{
  	var elemento = doc.createElement('input');
  	elemento.setAttribute('type'	,tipo);
  	elemento.setAttribute('name'	,nome);
  	elemento.setAttribute('size'	,tamanho);
  	elemento.setAttribute('value'	,valor);
	elemento.disabled = desabilitado;
	
	return elemento;
}

function createEmptyCell(doc)
{
  	return doc.createTextNode('\u00A0');
}

function getValuesFromArray(collection)
{
	var values="";
	for (var i=0; i < collection.length; i++)
		values = values + collection[i].name + "=" + collection[i].value + "&";
		
	return values;
}
