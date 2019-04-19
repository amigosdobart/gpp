function comparadorDatas(dataIni,dataFim)
{
	// Define as propriedades do objeto
	this.dataIni 	= dataIni;
	this.dataFim	= dataFim;
	
	// Define os metodos do objeto
	this.compararMes=compararMes;
}

// Esta funcao retorna a diferenca
// entre as duas datas informadas 
// em milisegundos
function diferenca(dataIni,dataFim)
{
	return converteData(dataFim).getTime() - converteData(dataIni).getTime();
}

function diferencaEmDias(dataIni,dataFim)
{
	return diferenca(dataIni,dataFim)/(3600*24*1000);
	
}

// Esta funcao converte uma string
// em um objeto Date mas a string
// deve estar no formato dd/mm/yyyy
function converteData(datastr)
{

	var dataobj = new Date();
	dataobj.setDate   (datastr.substr(0,2));
	dataobj.setMonth  (datastr.substr(3,2));
	dataobj.setYear   (datastr.substr(6,2));
	dataobj.setSeconds(0);
	dataobj.setMinutes(0);
	dataobj.setHours  (0);
	
	return dataobj;
}