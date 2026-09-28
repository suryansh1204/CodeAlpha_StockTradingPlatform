/**
 * Represents a stock listed on the simulated market.
 */
public class Stock {
    private String symbol;
    private String companyName;
    private double price;
    private double previousPrice;
    private long volume;

    public Stock(String symbol, String companyName, double initialPrice, long volume) {
        this.symbol        = symbol.toUpperCase();
        this.companyName   = companyName;
        this.price         = initialPrice;
        this.previousPrice = initialPrice;
        this.volume        = volume;
    }

    public String getSymbol()      { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getPrice()       { return price; }
    public double getPreviousPrice(){ return previousPrice; }
    public long   getVolume()      { return volume; }

    /**
     * Simulates a market price fluctuation within ±5%.
     */
    public void simulatePriceChange() {
        previousPrice = price;
        double change = (Math.random() * 10 - 5) / 100.0; // -5% to +5%
        price = Math.max(0.01, price * (1 + change));
        price = Math.round(price * 100.0) / 100.0;
    }

    /** Price change in absolute terms. */
    public double getPriceChange() {
        return Math.round((price - previousPrice) * 100.0) / 100.0;
    }

    /** Price change as a percentage. */
    public double getChangePercent() {
        if (previousPrice == 0) return 0;
        return Math.round(((price - previousPrice) / previousPrice * 100.0) * 100.0) / 100.0;
    }

    /** Trend arrow: ▲ for gain, ▼ for loss, — for flat. */
    public String getTrend() {
        if (price > previousPrice)      return "▲";
        else if (price < previousPrice) return "▼";
        else                            return "—";
    }

    @Override
    public String toString() {
        return String.format("%-6s | %-22s | $%8.2f | %s %+.2f (%+.2f%%) | Vol: %,d",
                symbol, companyName, price, getTrend(), getPriceChange(), getChangePercent(), volume);
    }
}
