# Task 2: Stock Trading Platform
### CodeAlpha Java Programming Internship

A console-based simulated stock trading environment built with full OOP design. Users can view live market data, buy and sell stocks, track portfolio performance, and review transaction history.

---

## Features
- **Live market board** with 10 pre-loaded stocks (AAPL, GOOGL, MSFT, TSLA, etc.)
- **Simulated price fluctuations** (±5% per tick) with trend arrows (▲/▼)
- **Buy / Sell** operations with balance and share validation
- **Portfolio tracking** with weighted average buy price and unrealized P/L
- **Transaction history** with timestamps
- Starting balance of **$10,000** per user
- Confirmation prompts before every trade

---

## Project Structure
```
Task2_StockTradingPlatform/
├── src/
│   ├── Stock.java            # Stock model with price simulation
│   ├── Transaction.java      # Individual buy/sell record
│   ├── Portfolio.java        # User holdings, balance, P/L
│   ├── Market.java           # All stocks + market tick simulation
│   └── StockTradingApp.java  # Main app / console UI
└── README.md
```

---

## How to Compile & Run

```bash
# Compile
javac -d Task2_StockTradingPlatform Task2_StockTradingPlatform/src/*.java

# Run
java -cp Task2_StockTradingPlatform StockTradingApp
```

---

## Sample Market Board
```
════════════════════════════════════════════════════════════════════════════
                       LIVE MARKET DATA BOARD
════════════════════════════════════════════════════════════════════════════
  Symbol | Company                | Price    | Change        | Volume
────────────────────────────────────────────────────────────────────────────
  AAPL   | Apple Inc.             | $ 191.45 | ▲ +2.15 (+1.14%) | 55,000,000
  GOOGL  | Alphabet Inc.          | $ 173.20 | ▼ -2.30 (-1.31%) | 22,000,000
  ...
```

---

## OOP Design
| Class | Responsibility |
|-------|---------------|
| `Stock` | Price, symbol, company; simulates market fluctuation |
| `Transaction` | Immutable buy/sell record with timestamp |
| `Portfolio` | Holdings map, cash balance, avg buy price, P/L |
| `Market` | Stock registry, market tick, board display |
| `StockTradingApp` | Console UI, menu loop, trade flow |

---

## Technologies
- Java SE 17+
- `HashMap` / `LinkedHashMap` for portfolio and market data
- `java.time.LocalDateTime` for transaction timestamps
- OOP: encapsulation, composition, abstraction
