<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MKTATENDENTECONSOLIDADO">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_ID_USUARIO">
		<table border="1">
			<tr>
				<th>Usuário</th>
				<th>Quantidade</th>
				<th>Positivo</th>
				<th>Negativo</th>
			</tr>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	
	<xsl:template match="G_ID_USUARIO">
		<tr>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="ID_USUARIO">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="COUNT_1">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="POSITIVOS">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NEGATIVOS">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="SUMCOUNT_1PERREPORT">
			<b>Qtd. Total </b><xsl:apply-templates/> <br></br>
	</xsl:template>
	<xsl:template match="SUMPOSITIVOSPERREPORT">
			<b>Total Positivos:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	<xsl:template match="SUMNEGATIVOSPERREPORT">
			<b>Total Negativos:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	
</xsl:stylesheet>