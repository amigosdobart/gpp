<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTINGENCIACRM">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_NOM_USUARIO">
		<table border="1">
		 <th>Data</th>
		 <th>Plano</th>
		 <th>Produto</th>
		 <th>Número</th>
		 <th>Operação</th>
		 <th>Nome</th>
		 <th>Endereço</th>
		 <th>Cidade</th>
		 <th>UF</th>
		 <th>Bairro</th>
		 <th>CEP</th>
		 <th>Telefone Contato</th>
		 <th>Data de Nascimento</th>
		 <th>RG</th>
		 <th>CPF</th>
		 <th>Número B.O.</th>
		 <th>Data B.O.</th>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_NOM_USUARIO">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	
	<xsl:template match="DATA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="PLANO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="PRODUTO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="IDT_MSISDN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DES_OPERACAO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NOM_USUARIO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NOM_LOGRADOURO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NOM_CIDADE">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="IDT_UF">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NOM_BAIRRO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NUM_CEP">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NUM_TELEF_CONTATO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DAT_NASCIMENTO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NUM_RG">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NUM_CPF">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="NUM_BO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DAT_BO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="ID_ATIVIDADE">
			
	</xsl:template>

	<xsl:template match="LIST_G_DATAINICIAL_A_DATAFINAL">
	</xsl:template>
	<xsl:template match="ATENDENTE1">
					<b>Atendente:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="G_ATENDENTE1">
			<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="LIST_G_ATENDENTE1">
			<xsl:apply-templates/>
	</xsl:template>
</xsl:stylesheet>