import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks a user's holdings and transaction history.
 */
public class Portfolio {

    private final String                  ownerName;
    private double                        balance;       // cash balance
    private final Map<String, Integer>    holdings;      // symbol -> quantity
    private final Map<String, Double>     avgBuyPrice;   // symbol -> weighted avg buy price
    private final List<Transaction>       transactions;

    public Portfolio(String ownerName, double initialBalance) {
        this.ownerName    = ownerName;
        this.balance      = initialBalance;
        this.holdings     = new HashMap<>();
        this.avgBuyPrice  = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public String getOwnerName() { return ownerName; }
    public double getBalance()   { return balance; }

    /**
     * Buys shares of a stock. Deducts cost from cash balance.
     */
    public void buy(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (totalCost > balance) {
            throw new IllegalStateException(
                String.format("Insufficient funds. Need $%.2f, have $%.2f.", totalCost, balance));
        }

        // Update weighted average buy price
        int existing = holdings.getOrDefault(stock.getSymbol(), 0);
        double existingAvg = avgBuyPrice.getOrDefault(stock.getSymbol(), 0.0);
        double newAvg = ((existing * existingAvg) + (quantity * stock.getPrice())) / (existing + quantity);

        holdings.put(stock.getSymbol(), existing + quantity);
        avgBuyPrice.put(stock.getSymbol(), Math.round(newAvg * 100.0) / 100.0);
        balance -= totalCost;
        balance  = Math.round(balance * 100.0) / 100.0;

        transactions.add(new Transaction(Transaction.Type.BUY, stock.getSymbol(), quantity, stock.getPrice()));
    }

    /**
     * Sells shares of a stock. Adds proceeds to cash balance.
     */
    public void sell(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.getSymbol(), 0);
        if (owned < quantity) {
            throw new IllegalStateException(
                String.format("Insufficient shares. Own %d, trying to sell %d.", owned, quantity));
        }

        double proceeds = stock.getPrice() * quantity;
        holdings.put(stock.getSymbol(), owned - quantity);
        if (holdings.get(stock.getSymbol()) == 0) {
            holdings.remove(stock.getSymbol());
            avgBuyPrice.remove(stock.getSymbol());
        }
        balance += proceeds;
        balance  = Math.round(balance * 100.0) / 100.0;

        transactions.add(new Transaction(Transaction.Type.SELL, stock.getSymbol(), quantity, stock.getPrice()));
    }

    public int getShares(String symbol) {
        return holdings.getOrDefault(symbol.toUpperCase(), 0);
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }

    public double getAvgBuyPrice(String symbol) {
        return avgBuyPrice.getOrDefault(symbol.toUpperCase(), 0.0);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Calculates total portfolio value = cash + market value of all holdings.
     */
    public double getTotalValue(Map<String, Stock> market) {
        double total = balance;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock s = market.get(entry.getKey());
            if (s != null) total += s.getPrice() * entry.getValue();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    /**
     * Prints a detailed portfolio performance report.
     */
    public void printPortfolioReport(Map<String, Stock> market) {
        String line = "=".repeat(70);
        String dash = "-".repeat(70);
        System.out.println("\n" + line);
        System.out.printf("  PORTFOLIO REPORT — %s%n", ownerName);
        System.out.println(line);
        System.out.printf("  Cash Balance : $%,.2f%n", balance);

        if (holdings.isEmpty()) {
            System.out.println("  No holdings currently.");
        } else {
            System.out.println(dash);
            System.out.printf("  %-8s | %-6s | %-10s | %-10s | %-10s | P/L%n",
                    "Symbol", "Qty", "Avg Buy", "Cur Price", "Mkt Value");
            System.out.println(dash);
            double totalMktValue = 0;
            double totalCostBasis = 0;

            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                String sym = entry.getKey();
                int    qty = entry.getValue();
                Stock  s   = market.get(sym);
                if (s == null) continue;

                double avgBuy    = getAvgBuyPrice(sym);
                double curPrice  = s.getPrice();
                double mktValue  = curPrice * qty;
                double costBasis = avgBuy * qty;
                double pl        = mktValue - costBasis;

                totalMktValue  += mktValue;
                totalCostBasis += costBasis;

                System.out.printf("  %-8s | %6d | $%9.2f | $%9.2f | $%9.2f | %s$%.2f%n",
                        sym, qty, avgBuy, curPrice, mktValue, pl >= 0 ? "+" : "", pl);
            }
            System.out.println(dash);
            double totalPL = totalMktValue - totalCostBasis;
            System.out.printf("  Total Market Value : $%,.2f%n", totalMktValue);
            System.out.printf("  Total Cost Basis   : $%,.2f%n", totalCostBasis);
            System.out.printf("  Unrealized P/L     : %s$%,.2f%n", totalPL >= 0 ? "+" : "", totalPL);
        }

        System.out.printf("  Total Portfolio    : $%,.2f%n", getTotalValue(market));
        System.out.println(line);
    }
}
