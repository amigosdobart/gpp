<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="BSATENDENTESTATUS">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	
	<xsl:template match="LIST_G_CANAL"/>	
	<xsl:template match="G_DES_CANAL_ORIGEM_BS"/>	
	<xsl:template match="DES_CANAL_ORIGEM_BS"/>

	<xsl:template match="SUMQTDESTATUSPERREPORT"/>	
	<xsl:template match="SUMQTDESTATUS1PERREPORT"/>	
	<xsl:template match="SUMQTDESTATUS2PERREPORT"/>
	<xsl:template match="SUMQTDESTATUS3PERREPORT"/>		
	<xsl:template match="SUMQTDESTATUS4PERREPORT"/>
	<xsl:template match="SUMQTDESTATUS5PERREPORT"/>
	<xsl:template match="SUMQTDESTATUS6PERREPORT"/>
	
	<xsl:template match="LIST_G_BS_ATENDENTE_STATUS"/>	
	<xsl:template match="LIST_G_ATENDENTE">	
		<table border="1">
			<tr>
				<th>Atendente</th>
				<th>Canal de Origem</th>
				<th>Quantidade</th>
				<th>Procedente</th>
				<th>Improcedente</th>
				<th>Parc. Procedente</th>
				<th>Aberto</th>
				<th>Ajuste Solicitado</th>
				<th>Ajuste Concedido</th>
				<th>Cliente</th>
			</tr>
			<xsl:apply-templates/>
		</table>
	</xsl:template>
	<xsl:template match="G_ATENDENTE">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="ATENDENTE">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="CANAL_ORIGEM">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_G_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_A_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_B_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_C_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_D_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_E_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="NVL_F_QTDESTATUS_0">
		<td><xsl:apply-templates/></td>
	</xsl:template>
	<xsl:template match="MSISDN">
		<td><xsl:apply-templates/></td>
	</xsl:template>
		
</xsl:stylesheet>