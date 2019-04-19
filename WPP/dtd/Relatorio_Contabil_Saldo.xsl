<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="CONTABILSALDOS">
	<html>
		<xsl:apply-templates/>
	</html>
	</xsl:template>
	<xsl:template match="LIST_G_PERÍODO">
		<table border="1">
		 <th>Período</th>
		 <th>CN</th>
		 <th>Código de Serviço</th>
		 <th>Descrição</th>
		 <th>Categoria</th>
		 <th>Documento</th>
		 <th>Quantidade</th>
		 <th>Total(R$)</th>
		 <th>Alíquota ICMS(%)</th>
		 <th>Valor do ICMS(R$)</th>
		 <th>Valor Líquido(R$)</th>
			<xsl:apply-templates/>
			</table>
	</xsl:template>
	<xsl:template match="G_PERÍODO">
		<tr>
			<xsl:apply-templates/>
		</tr>
	</xsl:template>
	<xsl:template match="PERÍODO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CN">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CÓDIGO_SERVIÇO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DESCRIÇÃO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="CATEGORIA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="DOCUMENTO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="QTD_RECARGAS">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="TOTAL">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="ALIQUOTA">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="IMPOSTO_PAGO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="LIQUIDO">
		<td>
			<xsl:apply-templates/>
		</td>
	</xsl:template>
	<xsl:template match="LIST_G_NULL">
	</xsl:template>
	<xsl:template match="SUMQTD_RECARGASPERREPORT">
					Total:<br></br>
							<b>Quantidade:</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMTOTALPERREPORT">
			<b>Total(R$):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMIMPOSTO_PAGOPERREPORT">
			<b>Valor do ICMS(%):</b><xsl:apply-templates/><br></br>
	</xsl:template>
	<xsl:template match="SUMLIQUIDOPERREPORT">
				<b>Valor Líquido:</b><xsl:apply-templates/><br></br>
	</xsl:template>
</xsl:stylesheet>