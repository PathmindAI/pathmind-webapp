select distinct on (policy_id, index)
    policy_id, index, min, mean, max, iteration, agent
from metrics M
where M.policy_id = ?
order by policy_id, index, iteration desc;