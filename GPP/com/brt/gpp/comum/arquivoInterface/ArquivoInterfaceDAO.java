package com.brt.gpp.comum.arquivoInterface;

import java.sql.ResultSet;

import com.brt.gpp.comum.conexoes.bancoDados.PREPConexao;
import com.brt.gpp.comum.gppExceptions.GPPInternalErrorException;
import com.brt.gpp.gerentesPool.GerentePoolBancoDados;
/**
 * DAO responsavel por manipular os dados da Tabela TBL_GER_ARQUIVO_INTERFACE
 *
 * @author Leone Parise Vieira da Silva
 * @since  27/09/2007
 */
public class ArquivoInterfaceDAO
{
	private PREPConexao conexaoPrep;
	private long idProcesso;

	public ArquivoInterfaceDAO(long idProcesso) throws GPPInternalErrorException
	{
		this.idProcesso = idProcesso;
		getConexaoPrep();
	}

	public ArquivoInterfaceDAO(PREPConexao conexaoPrep){
		this.conexaoPrep = conexaoPrep;
	}

	public long getIdProcesso()
	{
		return this.idProcesso;
	}
	/**
	 * Abre uma conexao com o banco de dados caso nao exista.
	 *
	 * @return	Conexao aberta
	 * @throws	GPPInternalErrorException
 	 */
	public PREPConexao getConexaoPrep() throws GPPInternalErrorException
	{
		if(this.conexaoPrep == null)
			this.conexaoPrep = GerentePoolBancoDados.getInstancia(this.idProcesso).getConexaoPREP(this.idProcesso);

		return this.conexaoPrep;
	}
	/**
	 * Inclui uma nova entidade ArquivoInterface no banco de dados.
	 *
	 * @param arquivo	Entidade a ser incluida no banco
	 * @throws GPPInternalErrorException
	 */
	public void incluirArquivoInterface(ArquivoInterface arquivo)  throws GPPInternalErrorException
	{
		String sql = "INSERT INTO tbl_ger_arquivo_interface(nom_interface, idt_mascara_arquivo, idt_path_trabalho) " +
					 "VALUES(?, ?, ?)";
		Object[] params = {arquivo.getNomeInterface(),
						   arquivo.getMascaraArquivo(),
						   arquivo.getPathTrabalho()};
		getConexaoPrep().executaPreparedUpdate(sql, params, this.idProcesso);
	}
	/**
	 * Altera uma entidade ArquivoInterface no banco de dados.
	 *
	 * @param arquivo	Entidade a ser alterada no banco.
	 * @throws GPPInternalErrorException
	 */
	public void alterarArquivoInterface(ArquivoInterface arquivo) throws GPPInternalErrorException
	{
		String sql = "UPDATE tbl_ger_arquivo_interface SET num_arquivo=?, idt_mascara_arquivo=?, idt_path_trabalho=? " +
					 "WHERE nom_interface=?";
		Object[] params = {new Integer(arquivo.getNumeroArquivo()),
						   arquivo.getMascaraArquivo(),
						   arquivo.getPathTrabalho(),
						   arquivo.getNomeInterface()};
		getConexaoPrep().executaPreparedUpdate(sql, params, this.idProcesso);
	}
	/**
	 * Atualiza o numero do arquivo da interface dada retorna um entidade
	 * ArquivoInterfaceAtualizada.
	 *
	 * @param arquivo	Nome da Interface a ser atualizada do banco
	 * @throws GPPInternalErrorException
	 */
	public ArquivoInterface atualizarArquivoInterface(String nomeInterface) throws GPPInternalErrorException
	{
		ArquivoInterface arquivo = findByNomeInterface(nomeInterface);
		arquivo.setNumeroArquivo(arquivo.getNumeroArquivo()+1);
		alterarArquivoInterface(arquivo);

		return arquivo;
	}
	/**
	 * Exclui uma entidadr ArquivoInterface no banco
	 *
	 * @param nomeInterface		Nome da Interface a ser excluida do banco
	 * @throws GPPInternalErrorException
	 */
	public void excluirArquivoInterface(String nomeInterface) throws GPPInternalErrorException
	{
		String sql = "DELETE FROM tbl_ger_arquivo_interface WHERE nom_interface = ?";
		Object[] params = {nomeInterface};
		getConexaoPrep().executaPreparedUpdate(sql, params, this.idProcesso);
	}
	/**
	 * Retorna uma entidade do tipo ArquivoInterface procurando pelo nome da interface.
	 * Incrementa o numero de arquivo em 1.
	 *
	 * @param 	nomeInterface
	 * @return	ArquivoInterface preenchido ou nulo caso nao exista
	 * @throws 	GPPInternalErrorException
	 */
	public ArquivoInterface findByNomeInterface(String nomeInterface) throws GPPInternalErrorException
	{
		ArquivoInterface arquivo = null;

		String sql = "SELECT nom_interface, num_arquivo, idt_mascara_arquivo, idt_path_trabalho " +
					 "FROM tbl_ger_arquivo_interface WHERE nom_interface = ?";
		Object[] params = {nomeInterface};
		ResultSet rs = getConexaoPrep().executaPreparedQuery(sql, params, this.idProcesso);

		try
		{
			if(rs.next())
			{
				arquivo = new ArquivoInterface();
				arquivo.setNomeInterface(rs.getString(1));
				arquivo.setNumeroArquivo(rs.getInt(2));
				arquivo.setMascaraArquivo(rs.getString(3));
				arquivo.setPathTrabalho(rs.getString(4));
			}
		}
		catch (Exception e)
		{
			throw new GPPInternalErrorException(e.toString());
		}

		return arquivo;
	}
	/**
	 * Fecha conexao.
	 *
	 * @throws GPPInternalErrorException
	 */
	public void fecharConexao() throws GPPInternalErrorException
	{
		getConexaoPrep().liberaConexao(this.idProcesso);
		this.conexaoPrep = null;
	}
}
