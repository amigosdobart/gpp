<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="TESTE2">
	<html>
		<table border="1">
			<xsl:apply-templates/>
		</table>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_FILIAL">
		
			<tr>
				<th>Filial</th>
				<th>Quantidade</th>
				<th>Procedente</th>
				<th>Improcedente</th>
				<th>Aberto</th>
				<th>Ajuste Solicitado</th>
				<th>Ajuste Concedido</th>
				<th>Data da Recarga</th>
				<th>Matricula</th>
				<th>Data da Reclamacao</th>
			</tr>
			<xsl:apply-templates/>
			
	</xsl:template>
	
	<xsl:template match="G_FILIAL">
		<tr>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="FILIAL">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="QTDESTATUS1">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="QTDESTATUS2">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="QTDESTATUS3">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="QTDESTATUS4">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="QTDESTATUS5">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="QTDESTATUS6">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="DATA_RECARGA">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="MATRICULA">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="DATA_RECLAMACAO">
		<td><xsl:apply-templates/></td>
	</xsl:template>

	<xsl:template match="LIST_G_DES_CANAL_ORIGEM_BS">
		<td bgcolor="#007700">Total para o Canal <xsl:apply-templates/></td>
	</xsl:template>
	

	<xsl:template match="SUMQTDESTATUS1PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUMQTDESTATUS2PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUMQTDESTATUS3PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUMQTDESTATUS4PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUMQTDESTATUS5PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="SUMQTDESTATUS6PERREPORT">
		<td bgcolor="#007700"><xsl:apply-templates/></td>
	</xsl:template>
	
	
</xsl:stylesheet>