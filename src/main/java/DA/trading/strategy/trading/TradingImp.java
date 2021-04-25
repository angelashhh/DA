package DA.trading.strategy.trading;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.SumPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.utils.ReadCSV;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TradingImp {

    private static List<TradeRecord> tradeBook = new ArrayList<>();
    private static SumPrices lastSums = new SumPrices();
    private static float netPosition = 0;
    private static TradeRecord lastTradeRecord = new TradeRecord();
    public static List<TimestampPrice> lastMPrices = new ArrayList<>();
    public static List<TimestampPrice> lastNPrices = new ArrayList<>();

    public static List<TradeRecord> getTradeBook() {return tradeBook;}

    public  static void decideTradePerPrice(BufferedReader priceReader, int m, int n){
        int lineNumber = 0;
        String line;

        try {
            while ((line = priceReader.readLine()) != null) {
                TimestampPrice latestPrice = ReadCSV.readLineAsPrice(line);
                if (latestPrice != null) {
                    lineNumber += 1;
                    tradeUsingSMA(lineNumber, m, n, latestPrice);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read price file.");
        }
        System.out.println("Finished processing " +lineNumber + " trades.");
    }

    public static void tradeUsingSMA(int lineNumber, int m, int n, TimestampPrice latestPrice) {
        if (lineNumber >= m) {
            SumPrices currSums = calcSums(m, n, lineNumber, lastMPrices, lastNPrices, latestPrice);
            if (lineNumber >= n) {
                TradingImp.trade(currSums, m, n, latestPrice);
            }
        }
        updateLastPrices(m, n, latestPrice);
    }

    public static void trade(SumPrices currSums, int m, int n, TimestampPrice latestPrice) {
        SMAPrices currSMAs = calcSMAs(currSums, m, n);
        TradeRecord deal = buyOrSell(currSMAs, latestPrice);
        if (deal != null){
            executeTrade(deal);
        }
    }

    public static TradeRecord buyOrSell (SMAPrices smaPrices, TimestampPrice latestPrice){
        if (smaPrices.getSmaForM() > smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){ //buyflag = true => can buy
            return TradeRecord.createBuyRecord(lastTradeRecord, latestPrice);
        } else if (smaPrices.getSmaForM() < smaPrices.getSmaForN() && lastTradeRecord.getBuyFlag()){
            return TradeRecord.createSellRecord(lastTradeRecord, latestPrice);
        } else {return null;}
    }

    public static void executeTrade(TradeRecord tradeRecord){
        updateNetPosition(tradeRecord);
        tradeRecord = updateCurrAndNetPNL(tradeRecord);
        System.out.println("tradeRecord " + tradeRecord.getTradeQuantity() + "|" + tradeRecord.getTimestampPrice().getTimestamp()
                + "|" + tradeRecord.getTimestampPrice().getPrice() + "|" + tradeRecord.getBuyFlag() + "|" + tradeRecord.getNetPnl());
        tradeBook.add(tradeRecord);
        lastTradeRecord = tradeRecord;
    }

    public static SumPrices calcSums(int m, int n, int rowNumber,
                                  List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices,
                                  TimestampPrice latestPrice){
        SumPrices currSums = new SumPrices();
        currSums.setSumForM(calcSum(m, rowNumber, lastSums.getSumForM(), latestPrice, lastMPrices));
        currSums.setSumForN(calcSum(n, rowNumber, lastSums.getSumForN(), latestPrice, lastNPrices));
        lastSums = currSums;
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

    public static SMAPrices calcSMAs(SumPrices currSums, int m, int n){
        SMAPrices currSMAPrices = new SMAPrices();
        currSMAPrices.setSmaForM(currSums.getSumForM() / m);
        currSMAPrices.setSmaForN(currSums.getSumForN() / n);
        return currSMAPrices;
    }

    public static void updateNetPosition(TradeRecord tradeRecord){
        if (tradeRecord.getBuyFlag()) {
            netPosition += tradeRecord.getTradeQuantity();
        } else {
            netPosition -= tradeRecord.getTradeQuantity();
        }
    }

    public static TradeRecord updateCurrAndNetPNL(TradeRecord tradeRecord){
        if (!tradeRecord.getBuyFlag()) {
            tradeRecord.setPnlForCurrTrade(calcCurrPNL(tradeRecord)).setNetPnl(calcNetPNL(tradeRecord));
        }
        return tradeRecord;
    }

    public static float calcCurrPNL(TradeRecord tradeRecord){
        return tradeRecord.getTimestampPrice().getPrice() * (
                tradeRecord.getTradeQuantity()- lastTradeRecord.getTradeQuantity());
    }

    public static float calcNetPNL(TradeRecord tradeRecord){
        return netPosition * tradeRecord.getTimestampPrice().getPrice();
    }

    private static void updateLastPrices(int m, int n, TimestampPrice latestPrice){
        updateLastXPrices(m, latestPrice, lastMPrices);
        updateLastXPrices(n, latestPrice, lastNPrices);
    }

    private static void updateLastXPrices(int x, TimestampPrice latestPrice, List<TimestampPrice> xPrices){
        if (xPrices.size() >= x){
            xPrices.remove(0);
        }
        xPrices.add(latestPrice);
    }

    //below functions are created only for testing use
    public float getNetPosition() {return netPosition;}

    public TradingImp setLastTradeRecord(TradeRecord lastTradeRecord) {
        this.lastTradeRecord = lastTradeRecord;
        return this;
    }
}
