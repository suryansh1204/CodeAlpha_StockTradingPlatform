import java.util.List;
import java.util.Scanner;

/**
 * CodeAlpha Internship — Task 2: Stock Trading Platform
 * Console-based simulated stock trading environment.
 *
 * Features:
 *  - Live market data display with simulated price fluctuations
 *  - Buy & sell operations with balance validation
 *  - Portfolio tracking with P/L calculation
 *  - Transaction history
 *  - OOP design: Stock, Portfolio, Market, Transaction classes
 */
public class StockTradingApp {

    private static final Scanner   scanner   = new Scanner(System.in);
    private static final Market    market    = new Market();
    private static       Portfolio portfolio = null;

    public static void main(String[] args) {
        printBanner();
        setupUser();
        boolean running = true;

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> { market.simulateMarketTick(); market.printMarketBoard(); }
                case "2" -> buyStock();
                case "3" -> sellStock();
                case "4" -> portfolio.printPortfolioReport(market.getStockMap());
                case "5" -> viewTransactionHistory();
                case "6" -> running = false;
                default  -> System.out.println("  [!] Invalid option. Enter 1–6.");
            }
        }

        System.out.println("\n  Thank you for using the Stock Trading Platform!");
        System.out.println("  CodeAlpha Internship Project — Task 2");
        scanner.close();
    }

    // ─── Setup ───────────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║       STOCK TRADING PLATFORM — CodeAlpha         ║");
        System.out.println("║   Simulated Market | Real OOP Logic               ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    private static void setupUser() {
        System.out.print("\n  Enter your name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = "Trader";

        double balance = 10_000.00;
        System.out.printf("  Welcome, %s! Starting balance: $%,.2f%n%n", name, balance);
        portfolio = new Portfolio(name, balance);
        market.printMarketBoard();
    }

    private static void printMenu() {
        System.out.println("\n  ┌───────────────────────────────┐");
        System.out.println("  │          TRADING MENU         │");
        System.out.println("  ├───────────────────────────────┤");
        System.out.println("  │  1. Refresh Market Data       │");
        System.out.println("  │  2. Buy Stock                 │");
        System.out.println("  │  3. Sell Stock                │");
        System.out.println("  │  4. View Portfolio            │");
        System.out.println("  │  5. Transaction History       │");
        System.out.println("  │  6. Exit                      │");
        System.out.println("  └───────────────────────────────┘");
        System.out.printf ("  Cash Balance: $%,.2f%n", portfolio.getBalance());
        System.out.print  ("  Enter choice: ");
    }

    // ─── Trading Actions ─────────────────────────────────────────────────────

    private static void buyStock() {
        market.printMarketBoard();
        System.out.print("  Enter stock symbol to BUY: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("  [!] Stock '" + symbol + "' not found.");
            return;
        }

        System.out.printf("  %s is currently at $%.2f%n", stock.getSymbol(), stock.getPrice());
        System.out.print("  Enter quantity: ");

        try {
            int qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) throw new NumberFormatException();

            double cost = stock.getPrice() * qty;
            System.out.printf("  Total cost: $%.2f | Your balance: $%.2f%n", cost, portfolio.getBalance());
            System.out.print("  Confirm purchase? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("  [x] Purchase cancelled.");
                return;
            }

            portfolio.buy(stock, qty);
            System.out.printf("  [✓] Bought %d shares of %s at $%.2f each.%n", qty, symbol, stock.getPrice());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid quantity.");
        } catch (IllegalStateException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    private static void sellStock() {
        if (portfolio.getHoldings().isEmpty()) {
            System.out.println("  [!] You have no holdings to sell.");
            return;
        }

        System.out.println("  Your holdings:");
        portfolio.getHoldings().forEach((sym, qty) -> {
            Stock s = market.getStock(sym);
            double price = s != null ? s.getPrice() : 0;
            System.out.printf("    %-6s — %d shares @ $%.2f (Value: $%.2f)%n",
                    sym, qty, price, price * qty);
        });

        System.out.print("  Enter stock symbol to SELL: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);

        if (stock == null) {
            System.out.println("  [!] Stock '" + symbol + "' not found.");
            return;
        }

        int owned = portfolio.getShares(symbol);
        if (owned == 0) {
            System.out.println("  [!] You don't own any shares of " + symbol + ".");
            return;
        }

        System.out.printf("  You own %d shares of %s @ $%.2f%n", owned, symbol, stock.getPrice());
        System.out.print("  Enter quantity to sell: ");

        try {
            int qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) throw new NumberFormatException();

            double proceeds = stock.getPrice() * qty;
            System.out.printf("  Proceeds: $%.2f%n", proceeds);
            System.out.print("  Confirm sale? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("  [x] Sale cancelled.");
                return;
            }

            portfolio.sell(stock, qty);
            System.out.printf("  [✓] Sold %d shares of %s at $%.2f each.%n", qty, symbol, stock.getPrice());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid quantity.");
        } catch (IllegalStateException e) {
            System.out.println("  [!] " + e.getMessage());
        }
    }

    private static void viewTransactionHistory() {
        List<Transaction> txns = portfolio.getTransactions();
        if (txns.isEmpty()) {
            System.out.println("  No transactions yet.");
            return;
        }
        String line = "=".repeat(72);
        System.out.println("\n" + line);
        System.out.println("                    TRANSACTION HISTORY");
        System.out.println(line);
        for (Transaction t : txns) {
            System.out.println("  " + t);
        }
        System.out.println(line);
        System.out.printf("  Total Transactions: %d%n", txns.size());
        System.out.println(line);
    }
}
