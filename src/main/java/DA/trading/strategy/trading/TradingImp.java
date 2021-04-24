package DA.trading.strategy.trading;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.SumPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;

import java.util.ArrayList;
import java.util.List;

public class TradingImp {

    private static List<TradeRecord> tradeBook = new ArrayList<>();
    private static SumPrices lastSums = new SumPrices();
    private static SMAPrices currSMAPrices = new SMAPrices();
    public static TradeRecord lastTradeRecord = new TradeRecord();

    public static SumPrices updateSums(int m, int n, int rowNumber,
                                  List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices,
                                  TimestampPrice latestPrice){
        SumPrices currSums = calcSums(m, n, rowNumber, lastSums, latestPrice, lastMPrices, lastNPrices);
        lastSums = currSums;
        return currSums;
    }

    public static SMAPrices updateSMAs(SumPrices currSums, int m, int n){
        currSMAPrices.setSmaForM(currSums.getSumForM() / m);
        currSMAPrices.setSmaForN(currSums.getSumForN() / n);
        return currSMAPrices;
    }

    public static TradeRecord decideTrade(SMAPrices smaPrices, TimestampPrice latestPrice){
        if (smaPrices.getSmaForM() > smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){ //buyflag = true => can buy
            return TradeRecord.createBuyRecord(lastTradeRecord, latestPrice);
        } else if (smaPrices.getSmaForM() < smaPrices.getSmaForN() && lastTradeRecord.getBuyFlag()){
            return TradeRecord.createSellRecord(lastTradeRecord, latestPrice);
        } else {return null;}
    }

    public static void executeTrade(TradeRecord tradeRecord){
        if (tradeRecord != null) {
            tradeBook.add(tradeRecord);
            lastTradeRecord = updateTempPnlIfBuyTrade(tradeRecord);
        }
    }

    public static TradeRecord updateTempPnlIfBuyTrade(TradeRecord tradeRecord) {
        if(tradeRecord.getBuyFlag()) {
            return tradeRecord.setPnlForCurrTrade(lastTradeRecord.getPnlForCurrTrade() - 20)
                    .setNetPnl(lastTradeRecord.getPnlForCurrTrade() - 20);
        } else { return tradeRecord; }
    }

    public static SumPrices calcSums(int m, int n, int lineNumber, SumPrices lastSums, TimestampPrice latestPrice,
                                     List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices){
        SumPrices currSums = new SumPrices();
        currSums.setSumForM(calcSum(m, lineNumber, lastSums.getSumForM(), latestPrice, lastMPrices));
        currSums.setSumForN(calcSum(n, lineNumber, lastSums.getSumForN(), latestPrice, lastNPrices));
        return currSums;
    }

    public static float calcSum(int x, int lineNumber, float lastSum,
                                TimestampPrice latestPrice, List<TimestampPrice> lastPrices){
        if (lineNumber > x){
            return TimestampPrice.toFourDecimal(quickSum(lastSum, latestPrice, lastPrices.get(0)));
        } else {
            return TimestampPrice.toFourDecimal(slowSum(lastPrices, latestPrice));
        }
    }

    public static float quickSum(float sumForLastXPrices, TimestampPrice latestPrice, TimestampPrice firstPriceInX){
        return sumForLastXPrices + latestPrice.getPrice() - firstPriceInX.getPrice();
    }

    public static float slowSum(List<TimestampPrice> lastPrices, TimestampPrice latestPrice){
        float sum = 0;
        for (TimestampPrice p: lastPrices){
            sum += p.getPrice();
        }
        return sum + latestPrice.getPrice();
    }

}
