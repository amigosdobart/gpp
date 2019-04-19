package br.com.brasiltelecom.wig.entity;

/**
 * Esta classe define a estrutura de armazenamento dos dados referentes ao estoque
 * de aparelhos e simcards do Microsiga. Apos a consulta ser realizada, as variaveis
 * serao preenchidas com os valores da base de dados.
 * 
 * @author Joao Paulo Galvagni
 * Data..: 14/12/2005
 *
 */
public class EstoqueMicrosiga
{
	private int 	codAparelho;
	private String  cd;
	private String	idtMnemonico;
	private String	desAparelho;
	private String	desCanal;
	private int 	qtdEstoque;
	private int 	qtdLivre;
	private int 	qtdReservada;

	public int getCodAparelho() {
		return codAparelho;
	}
	public void setCodAparelho(int codAparelho) {
		this.codAparelho = codAparelho;
	}
	public String getDesAparelho() {
		return desAparelho;
	}
	public void setDesAparelho(String desAparelho) {
		this.desAparelho = desAparelho;
	}
	public String getDesCanal() {
		return desCanal;
	}
	public void setDesCanal(String idtCanal) {
		this.desCanal = idtCanal;
	}
	public String getIdtMnemonico() {
		return idtMnemonico;
	}
	public void setIdtMnemonico(String idtMnemonico) {
		this.idtMnemonico = idtMnemonico;
	}
	public int getQtdEstoque() {
		return qtdEstoque;
	}
	public void setQtdEstoque(int qtdEstoque) {
		this.qtdEstoque = qtdEstoque;
	}
	public int getQtdLivre() {
		return qtdLivre;
	}
	public void setQtdLivre(int qtdLivre) {
		this.qtdLivre = qtdLivre;
	}
	public int getQtdReservada() {
		return qtdReservada;
	}
	public void setQtdReservada(int qtdReservada) {
		this.qtdReservada = qtdReservada;
	}
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
}

