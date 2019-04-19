use db_bradesco
---------- Carregamento basico das empresas cadastradas --------------*/
/*
*/
insert tb_empresa values (405, 'Brt - RS Fixa', 'HT9840005 ', 1)
insert tb_empresa values (411, 'Brt - AC Fixa', 'HT9840011 ', 1)
insert tb_empresa values (414, 'Brt - DF Fixa', 'HT9840014 ', 1)
insert tb_empresa values (416, 'Brt - GO Fixa', 'HT9840016 ', 1)
insert tb_empresa values (417, 'Brt - MT Fixa', 'HT9840017 ', 1)
insert tb_empresa values (419, 'Brt - MS Fixa', 'HT9840019 ', 1)
insert tb_empresa values (420, 'Brt - PR Fixa', 'HT9840020 ', 1)
insert tb_empresa values (426, 'Brt - RO Fixa', 'HT9840026 ', 1)
insert tb_empresa values (427, 'Brt - SC Fixa', 'HT9840027 ', 1)
insert tb_empresa values (402, 'Brt - CRT Fixa', 'HT9842002 ', 1)
go


---------- Carregamento basico de grupos x empresas --------------*/
/*
*/
insert tb_empresa_grupo values (405, 1)
insert tb_empresa_grupo values (411, 1)
insert tb_empresa_grupo values (414, 1)
insert tb_empresa_grupo values (416, 1)
insert tb_empresa_grupo values (417, 1)
insert tb_empresa_grupo values (419, 1)
insert tb_empresa_grupo values (420, 1)
insert tb_empresa_grupo values (426, 1)
insert tb_empresa_grupo values (427, 1)
insert tb_empresa_grupo values (402, 1)
go

---------- Carregamento basico das configuracoes da empresa ----------*/
/*
*/
--Mainframe...
insert tb_configuracao values (405, 9, 'HT98400051 ')

--Unix...
insert tb_configuracao values (405, 14, 'c:\bradesco\Brt RS Fixa')
insert tb_configuracao values (405, 15, '/Brt_RS_Fixa')

--SQL Server...
insert tb_configuracao values (405, 22, 'c:\bradesco\Brt RS Fixa\sql_local')
insert tb_configuracao values (405, 23, 'c:\bradesco\Brt RS Fixa\sql_remoto')
go

insert tb_configuracao values (411, 9, 'HT98400111 ')

--Unix...
insert tb_configuracao values (411, 14, 'c:\bradesco\Brt AC Fixa')
insert tb_configuracao values (411, 15, '/Brt_AC_Fixa')

--SQL Server...
insert tb_configuracao values (411, 22, 'c:\bradesco\Brt AC Fixa\sql_local')
insert tb_configuracao values (411, 23, 'c:\bradesco\Brt AC Fixa\sql_remoto')
go

insert tb_configuracao values (414, 9, 'HT98400141 ')

--Unix...
insert tb_configuracao values (414, 14, 'c:\bradesco\Brt DF Fixa')
insert tb_configuracao values (414, 15, '/Brt_DF_Fixa')

--SQL Server...
insert tb_configuracao values (414, 22, 'c:\bradesco\Brt DF Fixa\sql_local')
insert tb_configuracao values (414, 23, 'c:\bradesco\Brt DF Fixa\sql_remoto')
go

insert tb_configuracao values (416, 9, 'HT98400161 ')

--Unix...
insert tb_configuracao values (416, 14, 'c:\bradesco\Brt GO Fixa')
insert tb_configuracao values (416, 15, '/Brt_GO_Fixa')

--SQL Server...
insert tb_configuracao values (416, 22, 'c:\bradesco\Brt GO Fixa\sql_local')
insert tb_configuracao values (416, 23, 'c:\bradesco\Brt GO Fixa\sql_remoto')
go

insert tb_configuracao values (417, 9, 'HT98400171 ')

--Unix...
insert tb_configuracao values (417, 14, 'c:\bradesco\Brt MT Fixa')
insert tb_configuracao values (417, 15, '/Brt_MT_Fixa')

--SQL Server...
insert tb_configuracao values (417, 22, 'c:\bradesco\Brt MT Fixa\sql_local')
insert tb_configuracao values (417, 23, 'c:\bradesco\Brt MT Fixa\sql_remoto')
go

insert tb_configuracao values (419, 9, 'HT98400191 ')

--Unix...
insert tb_configuracao values (419, 14, 'c:\bradesco\Brt MS Fixa')
insert tb_configuracao values (419, 15, '/Brt_MS_Fixa')

--SQL Server...
insert tb_configuracao values (419, 22, 'c:\bradesco\Brt MS Fixa\sql_local')
insert tb_configuracao values (419, 23, 'c:\bradesco\Brt MS Fixa\sql_remoto')
go

insert tb_configuracao values (420, 9, 'HT98400201 ')

--Unix...
insert tb_configuracao values (420, 14, 'c:\bradesco\Brt PR Fixa')
insert tb_configuracao values (420, 15, '/Brt_PR_Fixa')

--SQL Server...
insert tb_configuracao values (420, 22, 'c:\bradesco\Brt PR Fixa\sql_local')
insert tb_configuracao values (420, 23, 'c:\bradesco\Brt PR Fixa\sql_remoto')
go

insert tb_configuracao values (426, 9, 'HT98400261 ')

--Unix...
insert tb_configuracao values (426, 14, 'c:\bradesco\Brt RO Fixa')
insert tb_configuracao values (426, 15, '/Brt_RO_Fixa')

--SQL Server...
insert tb_configuracao values (426, 22, 'c:\bradesco\Brt RO Fixa\sql_local')
insert tb_configuracao values (426, 23, 'c:\bradesco\Brt RO Fixa\sql_remoto')
go

insert tb_configuracao values (427, 9, 'HT98400271 ')

--Unix...
insert tb_configuracao values (427, 14, 'c:\bradesco\Brt SC Fixa')
insert tb_configuracao values (427, 15, '/Brt_SC_Fixa')

--SQL Server...
insert tb_configuracao values (427, 22, 'c:\bradesco\Brt SC Fixa\sql_local')
insert tb_configuracao values (427, 23, 'c:\bradesco\Brt SC Fixa\sql_remoto')
go

insert tb_configuracao values (402, 9, 'HT98420021 ')

--Unix...
insert tb_configuracao values (402, 14, 'c:\bradesco\Brt CRT Fixa')
insert tb_configuracao values (402, 15, '/Brt_CRT_Fixa')

--SQL Server...
insert tb_configuracao values (402, 22, 'c:\bradesco\Brt CRT Fixa\sql_local')
insert tb_configuracao values (402, 23, 'c:\bradesco\Brt CRT Fixa\sql_remoto')
go