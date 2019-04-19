SELECT a.sub_id, a.valor, a.contador
  FROM tbl_tmp_ajuste a
  order by valor desc
  
  DELETE FROM TBL_TMP_AJUSTE WHERE VALOR = 0
