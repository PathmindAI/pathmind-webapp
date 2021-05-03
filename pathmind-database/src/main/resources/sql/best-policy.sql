with
    E as (
        select id  from experiment E where E.model_id = ?
    )
select distinct on (experiment_id) experiment_id, run_id, policy_id, score, iteration
from (
         select
             distinct on (P.id) P.id as policy_id, R.experiment_id, R.id as run_id, RW.mean as score, RW.iteration
         from
             E left join run R on (E.id = R.experiment_id)
               left join policy P on (R.id = P.run_id)
               left join reward_score RW on (P.id = RW.policy_id)
         order by P.id, RW.iteration desc
     ) T
order by experiment_id, score desc