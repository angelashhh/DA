package DA.trading.strategy.utils;

public class Constants {
    public static final int FIXED_NOTION = 20;
    public static final int STARTING_BALANCE = 1000;
    public static final String BUY = "buy";
    public static final String SELL = "sell";
    public static final String INPUT_PRICES_CSV = "prices.csv";
    public static final String OUTPUT_TRADES_CSV = "/Users/apple/Downloads/trading-strategy/Trades.csv";
    public static final String[] OUTPUT_HEADERS = {"TradeNumber", "TradeTime", "TradeTimeInMilliSecond",
            "TradeSide", "TradeQuantity", "TradePrice", "PnlForCurrTrade", "NetPnl"};
}
