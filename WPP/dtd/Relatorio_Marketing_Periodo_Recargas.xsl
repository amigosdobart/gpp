<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MKTPERIODORECARGAS">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_PROFILE_ID">
		<table border="1">
			<tr>
				<th>Produto</th>
				<th>Quantidade</th>
			</tr>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	
	<xsl:template match="G_PROFILE_ID">
		<tr>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="IDT_PLANO_PRECO">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="COUNT_IDT_MSISDN">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="SUMCOUNT_IDT_MSISDNPERREPORT">
			<b>Qtd. Total </b><xsl:apply-templates/> <br></br>
	</xsl:template>
	
</xsl:stylesheet>