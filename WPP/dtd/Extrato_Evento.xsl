<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template match="GPPExtrato">
	<GPPExtrato>
		<xsl:apply-templates/>
	</GPPExtrato>
	</xsl:template>
	<xsl:template match="mensagemComprovante">
	</xsl:template>
	<xsl:template match="dadosCorrespondencia">
	</xsl:template>
	<xsl:template match="dadosCadastrais">
	</xsl:template>
	<xsl:template match="mensagemServicos">
	</xsl:template>
	<xsl:template match="detalhe">
	</xsl:template>
	<xsl:template match="totais">
	</xsl:template>
	<xsl:template match="evento">
		<evento>
			<xsl:apply-templates/>
		</evento>
	</xsl:template>
	<xsl:template match="numeroLinha">
		<numeroLinha>
			<xsl:apply-templates/>
		</numeroLinha>
	</xsl:template>
	<xsl:template match="descricao">
		<descricao>
			<xsl:apply-templates/>
		</descricao>
	</xsl:template>
	<xsl:template match="data">
		<data>
			<xsl:apply-templates/>
		</data>
	</xsl:template>
	<xsl:template match="hora">
		<hora>
			<xsl:apply-templates/>
		</hora>
	</xsl:template>
</xsl:stylesheet>