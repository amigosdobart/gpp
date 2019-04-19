create table tbl_tmp_dif_pp_final 
as
SELECT a.idt_msisdn, trunc(a.bonus_devido,2) devido, a.creditado,  b.ind_isento_limite,
    case
        when b.ind_isento_limite = 1 then
            bonus_devido - creditado 
        when bonus_devido > c.vlr_max_credito_bonus then
            c.vlr_max_credito_bonus - creditado 
        else
            bonus_devido - creditado
        end valor_devido,
    case
        when b.ind_isento_limite = 1 then
            1
        when bonus_devido > c.vlr_max_credito_bonus then
            2
        else
            3
        end regra


  FROM tbl_tmp_diferenca_pula a,
  tbl_ger_promocao_assinante@prepr_gppuser b, 
  tbl_ger_promocao@prepr_gppuser c
  where
  bonus_devido - creditado  > 1 and
  b.idt_msisdn = a.idt_msisdn and
  c.idt_promocao = b.idt_promocao and
  (b.ind_isento_limite = 1 or a.creditado < c.vlr_max_credito_bonus )

 
 
 
 
  select valor, count(1), sum(valor_devido) from (
  select 
  case
    when valor_devido >= 500 then
        50
    else
        trunc(valor_devido/10) 
    end valor, valor_devido
   from tbl_tmp_dif_pp_final
  )
  where
  valor >= 0 
  group by valor
  
  drop table tbl_tmp_dif_pp_final
  
  select a.*, creditado+valor_devido from tbl_tmp_dif_pp_final a order by 5 desc

