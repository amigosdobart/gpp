<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="MKTATENDENTE">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_1">
		<table border="1">
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	
	<xsl:template match="G_1">
			<xsl:apply-templates/>
	</xsl:template>
	
	<xsl:template match="ID_USUARIO">
			<tr bgcolor="#009900">
				<th>USUARIO</th>
				<td colspan="2">
					<xsl:apply-templates/>
				</td>
			</tr>
	</xsl:template>
	<xsl:template match="LIST_G_SUB_ID">
			<tr>
				<th>Assinante</th>
				<th>Positivo</th>
				<th>Negativo</th>
			</tr>
				<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="G_SUB_ID">
			<tr>
				<xsl:apply-templates/>
			</tr>
	</xsl:template>
	
	<xsl:template match="SUB_ID">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="POSITIVOS">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NEGATIVOS">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="SUMPOSITIVOSPERID_USUARIO">
		<tr>
			<td>Positivos:</td><td colspan="2"><xsl:apply-templates/></td>
		</tr>
	</xsl:template>

	<xsl:template match="SUMNEGATIVOSPERID_USUARIO">
		<tr>
			<td>Negativos:</td><td colspan="2"><xsl:apply-templates/></td>
		</tr>
	</xsl:template>
	
	<xsl:template match="SUMPOSITIVOSPERREPORT">
			<b>Total Positivos:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	<xsl:template match="SUMNEGATIVOSPERREPORT">
			<b>Total Negativos:</b><xsl:apply-templates/> <br></br>
	</xsl:template>
	
</xsl:stylesheet>