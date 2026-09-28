import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Records a single buy or sell transaction.
 */
public class Transaction {

    public enum Type { BUY, SELL }

    private final Type   type;
    private final String symbol;
    private final int    quantity;
    private final double pricePerShare;
    private final LocalDateTime timestamp;

    public Transaction(Type type, String symbol, int quantity, double pricePerShare) {
        this.type          = type;
        this.symbol        = symbol;
        this.quantity      = quantity;
        this.pricePerShare = pricePerShare;
        this.timestamp     = LocalDateTime.now();
    }

    public Type   getType()          { return type; }
    public String getSymbol()        { return symbol; }
    public int    getQuantity()      { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalValue()    { return quantity * pricePerShare; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %-4s | %-6s | Qty: %4d | @$%8.2f | Total: $%10.2f",
                timestamp.format(fmt), type, symbol, quantity, pricePerShare, getTotalValue());
    }
}
