package DA.trading.strategy.tradeRecords;

import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.trading.Constants;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class TradeRecord {
    private int tradeNumber;
    private TimestampPrice timestampPrice;
    private boolean buyFlag;
    private float tradeQuantity;
    private float pnlForCurrTrade;
    private float netPnl;

    public int getTradeNumber() {return tradeNumber;}
    public TradeRecord setTradeNumber(int tradeNumber) {
        this.tradeNumber = tradeNumber;
        return this;
    }

    public TimestampPrice getTimestampPrice() {return timestampPrice;}
    public TradeRecord setTimestampPrice(TimestampPrice timestampPrice){
        this.timestampPrice = timestampPrice;
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

    public static TradeRecord createBuyRecord (TradeRecord lastTradeRecord, TimestampPrice latestPrice) {
        return builder(block ->
                block.setTradeNumber(lastTradeRecord.getTradeNumber())
                        .setBuyFlag(true)
                        .setTradeQuantity(getTradeQuantity(latestPrice))
                        .setTimestampPrice(latestPrice));
    }

    //to do: set PNL!!!!
    public static TradeRecord createSellRecord (TradeRecord lastTradeRecord, TimestampPrice latestPrice) {
        return builder(block ->
                block.setTradeNumber(lastTradeRecord.getTradeNumber() + 1)
                        .setBuyFlag(false)
                        .setTradeQuantity(getTradeQuantity(latestPrice))
                        .setTimestampPrice(latestPrice));
    }

    private static float getTradeQuantity (TimestampPrice latestPrice){
        return TradeRecord.toFourDecimal(Constants.FIXED_NOTION / latestPrice.getPrice());
    }

    private static float toFourDecimal(float x){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return Float.parseFloat(decimalFormat.format(x));
    }
}
