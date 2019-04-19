function validaUpload(frmDados,campo1,campo2) {

	var arrFile1;
	var erro = false;
	arrFile1= frmDados.img1.value.split(".");
 	if (frmDados.img1.value == '') {
		alert('O campo ' + campo1 + ' precisa ser preenchido.');
		frmDados.img1.focus()
		erro = true;
		return false;
	}
 	if (arrFile1.length > 0) {
		if (arrFile1[arrFile1.length - 1].toLowerCase() != 'jpg' ){
			alert('O campo ' + campo1 + ' possui  arquivo de tipo inválido. A extensão deve ser: \'.jpg\' \n Este arquivo tem extensão: \'' + arrFile1[ arrFile1.length - 1 ].toLowerCase() + '\'' );
			erro = true;
			return false;
		}
	}

	var arrFile2;
	arrFile2= frmDados.img2.value.split(".");
 	if (frmDados.img2.value == '') {
		alert('O campo ' + campo2 + ' precisa ser preenchido.');
		frmDados.img2.focus()
		erro = true;		
		return false;
	}
 	if (arrFile2.length > 0) {
		if (arrFile2[arrFile2.length - 1].toLowerCase() != 'jpg' ){
			alert('O campo ' + campo2 + ' possui  arquivo de tipo inválido. A extensão deve ser: \'.jpg\' \n Este arquivo tem extensão: \'' + arrFile2[ arrFile2.length - 1 ].toLowerCase() + '\'' );
			erro = true;			
			return false;
		}
	}
	
	if (!erro) {
		return true;
	}
	
}

function validaUpload2(frmDados,campo1) {

	var arrFile1;
	var erro = false;
	arrFile1= frmDados.img1.value.split(".");
 	if (frmDados.img1.value == '') {
		alert('O campo ' + campo1 + ' precisa ser preenchido.');
		frmDados.img1.focus()
		erro = true;
		return false;
	}
 	if (arrFile1.length > 0) {
		if (arrFile1[arrFile1.length - 1].toLowerCase() != 'jpg' ){
			alert('O campo ' + campo1 + ' possui  arquivo de tipo inválido. A extensão deve ser: \'.jpg\' \n Este arquivo tem extensão: \'' + arrFile1[ arrFile1.length - 1 ].toLowerCase() + '\'' );
			erro = true;
			return false;
		}
	}

	if (!erro) {
		return true;
	}
	
}
