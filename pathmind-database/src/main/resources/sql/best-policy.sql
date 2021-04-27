with
    E as (
        select id  from experiment E where E.model_id = ?
    ),
    last_policy_score as
        (
            select  policy_id, run_id, experiment_id, score, iteration
            from
                (select
                     R.experiment_id, R.id as run_id, P.id as policy_id, RW.mean as score, RW.iteration,
                     rank() over(partition by P.id order by RW.iteration desc) as policy_iter_rnk
                 from
                     E left join run R on (E.id = R.experiment_id)
                       left join policy P on (R.id = P.run_id)
                       left join reward_score RW on (P.id = RW.policy_id)
                ) A
            where A.policy_iter_rnk = 1
        )
select *
from
    (
        select
            LP.*,
            row_number() over (partition by LP.experiment_id order by LP.score desc) as rnk
        from
            last_policy_score LP

    ) B
where B.rnk = 1

