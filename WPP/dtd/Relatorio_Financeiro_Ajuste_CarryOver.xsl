<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="FINAJSTCARRYOVER">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_FILIAL">
		<table border="1">
				<tr>
					<th>Filial</th>
					<th>Valor</th>
					<th>Valor Imposto</th>
					<th>Valor Líquido</th>
				</tr>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_FILIAL">
			<tr >
				<xsl:apply-templates/>
			</tr>
	</xsl:template>

	<xsl:template match="FILIAL">
			<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VALOR">
					<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VALOR_ALIQUOTA">
					<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="VALOR_VALOR_ALIQUOTA">
					<td><xsl:apply-templates/></td>
	</xsl:template>
	
	<xsl:template match="SUMVALORPERREPORT">
			<b>Valor Total:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	<xsl:template match="SUMVALOR_ALIQUOTAPERREPORT">
			<b>Valor Alíquota Total:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	<xsl:template match="SUMVALOR_VALOR_ALIQUOTAPERREPO">
			<b>Valor Líquido Total:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	
</xsl:stylesheet>