package DA.trading.strategy.trading;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.SumPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.utils.Constants;
import DA.trading.strategy.utils.CsvReader;
import DA.trading.strategy.utils.CsvWriter;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class TradingImp {

    public static void tradeAsPriceIsRead(BufferedReader priceReader, int m, int n){
        CSVWriter tradeWriter = CsvWriter.createPriceWriter(Constants.OUTPUT_TRADES_CSV);
        CsvWriter.writeHeader(tradeWriter, Constants.OUTPUT_HEADERS);

        int lineNumber = 0;
        String line;
        List<TimestampPrice> lastMPrices =  TimestampPrice.buildEmptyPrices(m);
        List<TimestampPrice> lastNPrices = TimestampPrice.buildEmptyPrices(n);
        SumPrices currSums = SumPrices.builder(block -> block.setSumForM(0).setSumForN(0));
        TradeRecord lastTradeRecord = new TradeRecord();
        float netPNL = 0;

        try {
            while ((line = priceReader.readLine()) != null) {
                TimestampPrice latestPrice = CsvReader.readLineAsPrice(line);
                if (latestPrice != null) {
                    lineNumber += 1;
                        currSums.setSumPrices(calcSums(lastMPrices, lastNPrices, latestPrice, currSums));
                        TradeRecord tradeRecord = decideTrade(currSums, m, n, lineNumber, latestPrice, lastTradeRecord);
                    if (tradeRecord != null){
                        //maybe write the below four lines as post-trade functions?
                       float currPNl = calcCurrPNL(tradeRecord, lastTradeRecord);
                       netPNL += currPNl;
                       TradeRecord tradeRecordWithPnl = updatePnls(tradeRecord, currPNl, netPNL);
                       CsvWriter.writeTradingRecord(tradeWriter, tradeRecordWithPnl);
                       lastTradeRecord = tradeRecordWithPnl;
                    }
                    updateLastPrices(m, n, latestPrice, lastMPrices, lastNPrices);
                }
            }
            tradeWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read or write price file.");
        }
        System.out.println("Finished processing " +lineNumber + " trades.");
    }

    public static TradeRecord decideTrade(SumPrices currSums, int m, int n, int lineNumber,
                                          TimestampPrice latestPrice, TradeRecord lastTradeRecord) {
        if (lineNumber >= n && currSums != null) {
            SMAPrices currSMAs = calcSMAs(currSums, m, n);
            return buyOrSell(currSMAs, latestPrice, lastTradeRecord);
        } else {return null;}
    }

    public static TradeRecord buyOrSell(SMAPrices smaPrices, TimestampPrice latestPrice, TradeRecord lastTradeRecord){
        if (smaPrices.getSmaForM() > smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){
            return TradeRecord.createTradeRecord(lastTradeRecord, latestPrice, Constants.BUY);
        } else if (smaPrices.getSmaForM() < smaPrices.getSmaForN() && lastTradeRecord.getBuyFlag()){
            return TradeRecord.createTradeRecord(lastTradeRecord, latestPrice, Constants.SELL);
        } else {return null;}
    }

    public static SumPrices calcSums(List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices,
                                  TimestampPrice latestPrice, SumPrices currSums){
        currSums.setSumForM(calcSum(currSums.getSumForM(), latestPrice, lastMPrices));
        currSums.setSumForN(calcSum(currSums.getSumForN(), latestPrice, lastNPrices));
        return currSums;
    }

    public static float calcSum(float lastSum, TimestampPrice latestPrice, List<TimestampPrice> lastPrices){
        return TimestampPrice.toFourDecimal(lastSum + latestPrice.getPrice() - lastPrices.get(0).getPrice());
    }

    public static SMAPrices calcSMAs(SumPrices currSums, int m, int n){
        SMAPrices currSMAPrices = new SMAPrices();
        currSMAPrices.setSmaForM(currSums.getSumForM() / m);
        currSMAPrices.setSmaForN(currSums.getSumForN() / n);
        return currSMAPrices;
    }

    public static float updateNetPosition(TradeRecord tradeRecord, float netPosition){
        if (tradeRecord.getBuyFlag()) {
            netPosition += tradeRecord.getTradeQuantity();
        } else {
            netPosition -= tradeRecord.getTradeQuantity();
        }
        return netPosition;
    }

    public static TradeRecord updateCurrAndNetPNL(TradeRecord tradeRecord, TradeRecord lastTradeRecord, float netPNL){
        if (!tradeRecord.getBuyFlag()) {
            float currPNL = calcCurrPNL(tradeRecord, lastTradeRecord);
            tradeRecord.setPnlForCurrTrade(currPNL).setNetPnl(calcNetPNL(currPNL, netPNL));
        }
        return tradeRecord;
    }

    public static float calcCurrPNL(TradeRecord tradeRecord, TradeRecord lastTradeRecord){
        return  tradeRecord.getTradeQuantity() * (
                tradeRecord.getExecutedPrice() - lastTradeRecord.getExecutedPrice());
    }

    public static TradeRecord updatePnls(TradeRecord trade, float currPnl, float netPnl){
        if (!trade.getBuyFlag()) {return trade.setPnlForCurrTrade(currPnl)
                .setNetPnl(netPnl); }
        else {return trade; }
    }

    public static float calcNetPNL(float currPNL, float netPNL){
        return netPNL + currPNL;
    }

    private static void updateLastPrices(int m, int n, TimestampPrice latestPrice,
                                         List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices){
        updateLastXPrices(m, latestPrice, lastMPrices);
        updateLastXPrices(n, latestPrice, lastNPrices);
    }

    private static void updateLastXPrices(int x, TimestampPrice latestPrice, List<TimestampPrice> xPrices){
        if (xPrices.size() >= x){
            xPrices.remove(0);
        }
        xPrices.add(latestPrice);
    }

}
