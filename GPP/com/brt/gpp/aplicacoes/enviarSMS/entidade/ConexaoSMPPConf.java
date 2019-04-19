package com.brt.gpp.aplicacoes.enviarSMS.entidade;

/**
 * A classe <code>ConexaoSMPPConf</code> que representa uma linha
 * da tabela <code>TBL_GER_CONEXAO_SMPP</code>.<br>
 * Tambem utilizada na abertura de novas conexoes (SMPP) com a
 * plataforma SMSC.
 *
 * @author Leone Parise Vieira da Silva
 * @since  1.0, 02/01/2008
 */
public class ConexaoSMPPConf
{
	private int    idConexao;
	private String ipMaquina;
	private int    porta;
	private String usuario;
	private String senha;
	private String tipoSistema;

	public int getIdConexao()
	{
		return idConexao;
	}
	public void setIdConexao(int idConexao)
	{
		this.idConexao = idConexao;
	}
	public String getIpMaquina()
	{
		return ipMaquina;
	}
	public void setIpMaquina(String ipMaquina)
	{
		this.ipMaquina = ipMaquina;
	}
	public int getPorta()
	{
		return porta;
	}
	public void setPorta(int porta)
	{
		this.porta = porta;
	}
	public String getSenha()
	{
		return senha;
	}
	public void setSenha(String senha)
	{
		this.senha = senha;
	}
	public String getTipoSistema()
	{
		return tipoSistema;
	}
	public void setTipoSistema(String tipoSistema)
	{
		this.tipoSistema = tipoSistema;
	}
	public String getUsuario()
	{
		return usuario;
	}
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	public boolean equals(Object obj)
	{
		if(obj != null && obj instanceof ConexaoSMPPConf)
			return ((ConexaoSMPPConf)obj).getIdConexao() == this.idConexao;

		return false;
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ ConexaoSMPPConf ]");
		sb.append(" -idConexao: ").append(this.idConexao);
		sb.append(" -ipMaquina: ").append(this.ipMaquina);
		sb.append(" -porta: ").append(this.porta);
		sb.append(" -usuario: ").append(this.usuario);
		sb.append(" -senha: ").append(this.senha);
		sb.append(" -tipoSistema: ").append(this.tipoSistema);

		return sb.toString();
	}
}
