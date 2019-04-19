
create table tbl_tmp_ajuste_F_F
as
select sub_id, sum(call_value) valor, count(1) contador
from tbl_ger_cdr@prod a, tbl_lix_ff b
where a.timestamp > sysdate -1 
and a.sub_id=b.msisdn
and a.call_value >0
and a.percent_discount_applied =0 and
(
a.call_id = '0'||b.ff1 or 
a.call_id = '0'||b.ff2 or 
a.call_id = '0'||b.ff3 or 
a.call_id = '0'||b.ff4 or 
a.call_id = '0'||b.ff5 or 
a.call_id = '0'||b.ff6 or 
a.call_id = '0'||b.ff7 )
group by sub_id


SELECT * FROM tbl_tmp_ajuste_F_F
