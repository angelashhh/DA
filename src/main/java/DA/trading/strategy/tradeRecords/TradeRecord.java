package DA.trading.strategy.tradeRecords;

import DA.trading.strategy.prices.TimestampPrice;

import java.util.function.Consumer;

public class TradeRecord {
    private int tradeNumber;
    private TimestampPrice timestampPrice;
    private boolean buyFlag;
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
    public TradeRecord setBuyFlat(boolean buyFlag){
        this.buyFlag = buyFlag;
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
}
