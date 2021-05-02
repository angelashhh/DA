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
        SumPrices currSums = new SumPrices();
        TradeRecord lastTradeRecord = new TradeRecord();
        float netPNL = 0;

        try {
            while ((line = priceReader.readLine()) != null) {
                TimestampPrice latestPrice = CsvReader.readLineAsPrice(line);
                if (latestPrice != null) {
                    lineNumber += 1;
                    currSums = SumPrices.calcSums(lastMPrices, lastNPrices, latestPrice, currSums);
                    if (lineNumber > n) {
                        SMAPrices currSMAs = SMAPrices.calcSMAs(currSums, m, n);
                        TradeRecord tradeRecord = decideTrade(currSMAs, latestPrice, lastTradeRecord);
                        if (tradeRecord != null){
                            TradeRecord tradeRecordWithPnl = enrichTradeRecordWithPnl(tradeRecord, lastTradeRecord, netPNL);
                            netPNL += tradeRecordWithPnl.getPnlForCurrTrade();
                            CsvWriter.writeTradingRecord(tradeWriter, tradeRecordWithPnl);
                            lastTradeRecord = tradeRecordWithPnl;
                        }
                    }
                    updateLastPrices(latestPrice, lastMPrices, lastNPrices);
                }
            }
            tradeWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read or write price file.");
        }
        System.out.println("Finished processing " + lineNumber + " trades.");
    }

    public static TradeRecord decideTrade(SMAPrices smaPrices, TimestampPrice latestPrice, TradeRecord lastTradeRecord){
        if (smaPrices.getSmaForM() > smaPrices.getSmaForN() && !lastTradeRecord.getBuyFlag()){
            return TradeRecord.createTradeRecord(lastTradeRecord, latestPrice, Constants.BUY);
        } else if (smaPrices.getSmaForM() < smaPrices.getSmaForN() && lastTradeRecord.getBuyFlag()){
            return TradeRecord.createTradeRecord(lastTradeRecord, latestPrice, Constants.SELL);
        } else {return null;}
    }

    private static TradeRecord enrichTradeRecordWithPnl(TradeRecord tradeRecord, TradeRecord lastTradeRecord, float netPnl) {
        if (!tradeRecord.getBuyFlag()){
            float currPnl = tradeRecord.getTradeQuantity() * (tradeRecord.getExecutedPrice() - lastTradeRecord.getExecutedPrice());
            return tradeRecord.setPnlForCurrTrade(currPnl).setNetPnl(netPnl + currPnl);
        }
        else {return tradeRecord;}
    }

    private static void updateLastPrices(TimestampPrice latestPrice,
                                         List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices){
        updateLastXPrices(latestPrice, lastMPrices);
        updateLastXPrices(latestPrice, lastNPrices);
    }

    private static void updateLastXPrices(TimestampPrice latestPrice, List<TimestampPrice> xPrices) {
        xPrices.remove(0);
        xPrices.add(latestPrice);
    }

}
