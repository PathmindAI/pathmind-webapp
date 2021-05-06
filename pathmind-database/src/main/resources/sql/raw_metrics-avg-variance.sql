select policy_id, index, avg(value) avg, variance(value) variance
from metrics_raw
where policy_id = ?
group by policy_id, index
order by index;