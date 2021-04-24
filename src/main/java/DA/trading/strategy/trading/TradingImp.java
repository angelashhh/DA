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
    public static TradeRecord lastTradeRecord = new TradeRecord();

    public static void makeTradeDecision(int m, int n, int rowNumber,
                                         List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices,
                                         TimestampPrice latestPrice){
        SumPrices currSums;
        SMAPrices smaPrices = new SMAPrices();

        //1. calculate sum for m and n, and then update them as lastSum
        currSums = calcSums(n, rowNumber, lastSums, latestPrice, lastMPrices, lastNPrices);
        lastSums = currSums;

        //2. calculate average for m and n
        //TODO: change this!
        smaPrices.setSmaForM(currSums.getSumForM() / m);
        smaPrices.setSmaForN(currSums.getSumForN() / n);

        //3. decide whether to trade
        TradeRecord tradeRecord = decideTrade(smaPrices, lastTradeRecord, latestPrice);

        //4. if trade, add trade to tradebook
        //5. update temp pnl for buy trade
        if (tradeRecord != null) {
            tradeBook.add(tradeRecord);
            updateTempPnlIfBuyTrade(tradeRecord);
        }
    }

    public static TradeRecord decideTrade(SMAPrices smaPrices, TradeRecord lastTradeRecord, TimestampPrice latestPrice){
        if (smaPrices.getSmaForM() > smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){ //buyflag = true => can buy
                return TradeRecord.createBuyRecord(lastTradeRecord, latestPrice);
        } else if (smaPrices.getSmaForM() < smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){
                return TradeRecord.createSellRecord(lastTradeRecord, latestPrice);
        } else {return null;}
    }

    public static void updateTempPnlIfBuyTrade(TradeRecord tradeRecord) {
        if(tradeRecord.getBuyFlag()) {
            TradeRecord buyRecordWithTempPnl = tradeRecord.setPnlForCurrTrade(lastTradeRecord.getPnlForCurrTrade() - 20)
                    .setNetPnl(lastTradeRecord.getPnlForCurrTrade() - 20);
            lastTradeRecord = buyRecordWithTempPnl;
        }
    }

    public static SumPrices calcSums(int n, int lineNumber, SumPrices lastSums, TimestampPrice latestPrice,
                                     List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices){
        SumPrices currSums = new SumPrices();
        if (lineNumber > n) {
            currSums.setSumForM(quickSum(lastSums.getSumForM(), latestPrice, lastMPrices.get(0)));
            currSums.setSumForN(quickSum(lastSums.getSumForN(), latestPrice, lastNPrices.get(0)));
        } else {
            currSums.setSumForM(slowSum(lastMPrices, latestPrice));
            currSums.setSumForN(slowSum(lastNPrices, latestPrice));
        }
        return currSums;
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
