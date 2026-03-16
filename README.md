# DFinance — feature todo

## v1

### Accounts

- [ ] Create and manage multiple accounts (cash, bank, credit card — each with name, type, currency,
  and initial balance)
- [ ] Account balance overview (total net worth derived from all accounts combined)
- [ ] Account balance history chart (line chart showing each account's balance over time)

### Transactions

- [ ] Add / edit / delete transactions (amount, date, account, category, optional note)
- [ ] Transaction types: income, expense, transfer (transfers move money between accounts without
  affecting totals)
- [ ] Categories and subcategories (user-defined hierarchy, e.g. Food > Groceries, Food > Dining
  out)
- [ ] Recurring transactions (define frequency: daily, weekly, monthly, yearly — auto-create future
  entries or remind user)
- [ ] Transaction list with filters (filter by account, category, date range, type)

### Budgeting

- [ ] Monthly budget per category (set a spending limit for each category each month)
- [ ] Budget vs actual tracking (real-time comparison of budgeted vs spent per category)
- [ ] Rollover unspent budget (carry leftover amounts to the next month per category, toggleable per
  category)

### Savings goals

- [ ] Create goals with target amount (name, target amount, optional deadline)
- [ ] Manual progress updates (user logs contributions toward a goal manually)
- [ ] Progress visualization (progress bar + % complete + amount remaining + projected completion
  date)

### Dashboard & reports

- [ ] Spending by category (pie or bar chart for selected month)
- [ ] Income vs expenses over time (bar chart grouped by month, showing net balance per month)
- [ ] Budget vs actual summary (card-based overview of all categories for current month)

### Data & portability

- [ ] CSV export (export transactions with all fields, filterable by date range or account)
- [ ] CSV import (map columns to dfinance fields, preview before confirming)

### Architecture foundations

- [ ] Local SQLite database via SQLDelight (schema designed with sync in mind: UUIDs as PKs,
  `created_at` / `updated_at` / `deleted_at` on all tables)
- [ ] Repository pattern in shared KMP module (data layer abstracted so a remote source can be
  swapped in later)
- [ ] Soft deletes (records marked `deleted_at` instead of hard-deleted, required for future sync
  conflict resolution)

---

## Future / post-v1

- [ ] Self-hosted sync backend (conflict-free sync using the soft-delete + UUID schema already in
  place)
- [ ] iOS support (KMP shared logic already works — just add Compose Multiplatform iOS target)
- [ ] Receipt photo attachments
- [ ] Spending alerts / push notifications for recurring transactions
- [ ] Bank statement import (OFX/QIF)