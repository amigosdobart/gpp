<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="BSCLIENTESTATUS">
	<html>
		<table border="1">
			<xsl:apply-templates/>
		</table>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_CLIENTE">
		
			<tr>
				<th>Cliente</th>
				<th>Quantidade</th>
				<th>Procedente</th>
				<th>Improcedente</th>
				<th>Aberto</th>
				<th>Ajuste Solicitado</th>
				<th>Ajuste Concedido</th>
				<th>Data da Recarga</th>
				<th>Matricula de Abertura</th>
				<th>Matricula de Fechamento</th>
				<th>Data da Recarga</th>
				<th>Data da Reclamacao</th>
			</tr>
			<xsl:apply-templates/>
			
	</xsl:template>
	
	<xsl:template match="G_CLIENTE">
		<tr>
				<xsl:apply-templates/>
		</tr>
	</xsl:template>

	<xsl:template match="CLIENTE">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NUM_BS">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_A_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_B_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_D_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_H_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_E_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_F_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="DATA_RECARGA">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="MATRICULAA">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="MATRICULAF">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="DATA_RECARGA">
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