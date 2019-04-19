<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="ESTATISTICAEVENTOS">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_GRUPO_DATE">
		<table border="1">
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="GRUPO_DATE">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="DAT_APROVISIONAMENTO">
			<tr bgcolor="#009900">
				<th>DIA</th>
				<td colspan="2">
					<xsl:apply-templates/>
				</td>
			</tr>
	</xsl:template>
	<xsl:template match="LIST_GRUPO_REGIAO">
				<tr>
					<th>CN</th>
					<th>Quantidade</th>
				</tr>
			<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="TOTAL_DATA">
				<tr>
					<th colspan="2">Total</th>
					<th><xsl:apply-templates/></th>
				</tr>
	</xsl:template>

	<xsl:template match="GRUPO_REGIAO">
		<tr>
			<xsl:apply-templates/>
		</tr>			
	</xsl:template>
	<xsl:template match="CN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="UNIDADE">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="LIST_G_DES_EVENTO">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="G_DES_EVENTO">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="DES_EVENTO">
					<b>Evento:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="TOTAL_GERAL">
			<b>Qtd. Total:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
</xsl:stylesheet>