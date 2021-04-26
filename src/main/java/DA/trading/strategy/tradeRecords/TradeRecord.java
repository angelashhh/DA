package DA.trading.strategy.tradeRecords;

import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.utils.Constants;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class TradeRecord {
    private int tradeNumber;
    private String tradeTimestamp;
    private long tradeTimeInMilliSecond;
    private float executedPrice;
    private boolean buyFlag;
    private float tradeQuantity;
    private float pnlForCurrTrade;
    private float netPnl;

    public int getTradeNumber() {return tradeNumber;}
    public TradeRecord setTradeNumber(int tradeNumber) {
        this.tradeNumber = tradeNumber;
        return this;
    }

    public String getTradeTimestamp() {return tradeTimestamp;}
    public TradeRecord setTradeTimestamp(String tradeTimestamp) {
        this.tradeTimestamp = tradeTimestamp;
        return this;
    }

    public long getTradeTimeInMilliSecond() {return tradeTimeInMilliSecond;}
    public TradeRecord setTradeTimeInMilliSecond(long tradeTimeInMilliSecond) {
        this.tradeTimeInMilliSecond = tradeTimeInMilliSecond;
        return this;
    }

    public float getExecutedPrice() {return executedPrice;}
    public TradeRecord setExecutedPrice(float executedPrice) {
        this.executedPrice = executedPrice;
        return this;
    }

    public boolean getBuyFlag() {return buyFlag;}
    public TradeRecord setBuyFlag(boolean buyFlag){
        this.buyFlag = buyFlag;
        return this;
    }

    public float getTradeQuantity() {return tradeQuantity;}
    public TradeRecord setTradeQuantity(float tradeQuantity){
        this.tradeQuantity = tradeQuantity;
        return this;
    }

    public float getPnlForCurrTrade() {return pnlForCurrTrade;}
    public TradeRecord setPnlForCurrTrade(float pnlForCurrTrade){
        this.pnlForCurrTrade = pnlForCurrTrade;
        return this;
    }

    public float getNetPnl() {return netPnl;}
    public TradeRecord setNetPnl(float netPnl){
        this.netPnl = netPnl;
        return this;
    }

    public static TradeRecord builder(Consumer<TradeRecord> block) {
        TradeRecord data = new TradeRecord();
        block.accept(data);
        return data;
    }

    public static TradeRecord createTradeRecord (TradeRecord lastTradeRecord, TimestampPrice latestPrice, String option) {
        TradeRecord record =  builder(block -> block.setTradeTimestamp(latestPrice.getTimestamp())
                        .setTradeTimeInMilliSecond(latestPrice.getTimeInMilliSeconds())
                        .setExecutedPrice(latestPrice.getPrice())
                        .setTradeQuantity(getTradeQuantity(latestPrice)));
        if (option.equals(Constants.BUY)){
            return record.setBuyFlag(true).setTradeNumber(lastTradeRecord.getTradeNumber());
        } else {
            return record.setBuyFlag(false).setTradeNumber(lastTradeRecord.getTradeNumber() + 1);
        }
    }

    private static float getTradeQuantity (TimestampPrice latestPrice){
        return TradeRecord.toFourDecimal(Constants.FIXED_NOTION / latestPrice.getPrice());
    }

    private static float toFourDecimal(float x){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return Float.parseFloat(decimalFormat.format(x));
    }
}
