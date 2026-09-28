import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simulated stock market that holds all available stocks.
 */
public class Market {

    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public Market() {
        // Pre-load market with well-known tech & blue-chip stocks
        addStock(new Stock("AAPL",  "Apple Inc.",           189.30, 55_000_000));
        addStock(new Stock("GOOGL", "Alphabet Inc.",        175.50, 22_000_000));
        addStock(new Stock("MSFT",  "Microsoft Corp.",      415.20, 18_000_000));
        addStock(new Stock("AMZN",  "Amazon.com Inc.",      183.10, 32_000_000));
        addStock(new Stock("TSLA",  "Tesla Inc.",           248.60, 40_000_000));
        addStock(new Stock("META",  "Meta Platforms Inc.",  508.90, 14_000_000));
        addStock(new Stock("NVDA",  "NVIDIA Corp.",         873.20, 35_000_000));
        addStock(new Stock("NFLX",  "Netflix Inc.",         625.40, 5_000_000));
        addStock(new Stock("BRK",   "Berkshire Hathaway",   543.00, 3_000_000));
        addStock(new Stock("JPM",   "JPMorgan Chase & Co.", 198.50, 9_000_000));
    }

    private void addStock(Stock s) {
        stocks.put(s.getSymbol(), s);
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public Collection<Stock> getAllStocks() {
        return stocks.values();
    }

    public Map<String, Stock> getStockMap() {
        return stocks;
    }

    /**
     * Simulates market price changes for all stocks.
     */
    public void simulateMarketTick() {
        for (Stock s : stocks.values()) {
            s.simulatePriceChange();
        }
    }

    /**
     * Prints the live market data board.
     */
    public void printMarketBoard() {
        String line = "=".repeat(76);
        String dash = "-".repeat(76);
        System.out.println("\n" + line);
        System.out.println("                       LIVE MARKET DATA BOARD");
        System.out.println(line);
        System.out.printf("  %-6s | %-22s | %-10s | %-14s | %-12s%n",
                "Symbol", "Company", "Price", "Change", "Volume");
        System.out.println(dash);
        for (Stock s : stocks.values()) {
            System.out.println("  " + s);
        }
        System.out.println(line);
    }
}
