
- Time intervals:
  - http://localhost:8080/api/history?fromDate=2025-05-27T23:41:56
  - http://localhost:8080/api/history?fromDate=2025-05-27T23:41:56&toDate=2025-05-27T23:42:43

- Amount range and sorted:
  - http://localhost:8080/api/history?minAmount=3000&maxAmount=30000&sort=amount,desc

- Invalid sorting:
  - http://localhost:8080/api/history?minAmount=3000&maxAmount=30000&sort=amount,invalid
  
- Invalid Query:
  - http://localhost:8080/api/history?minAmount=3000maxAmount=30000

- Pagination:
  - http://localhost:8080/api/history?page=0&size=5
  - http://localhost:8080/api/history?page=1&size=5
  - http://localhost:8080/api/history?page=1&size=5&sort=timestamp,desc
