select *
from transactions
where transaction_date >= '2023-01-01'
  and transaction_date < '2024-01-01'
order by transaction_date;
