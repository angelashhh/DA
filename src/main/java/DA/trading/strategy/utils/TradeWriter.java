package DA.trading.strategy.utils;

import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.trading.Constants;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.InputStream;

public class TradeWriter {

    public static CSVWriter createPriceWriter(String fileName){
        try {
            FileWriter output = new FileWriter(fileName);
            return new CSVWriter(output);
        } catch (Exception e) {
            System.out.println("ERROR: No Output CSV can be found.");
            return null;
        }
    }

    public static void writeHeader(CSVWriter csvWriter){
        if (csvWriter != null){
            String[] header = {"TradeNumber", "TradeTime", "TradeTimeInMilliSecond",
                    "TradeSide", "TradeQuantity", "TradePrice", "PnlForCurrTrade", "NetPnl"};
            csvWriter.writeNext(header);
        } else {
            System.out.println("ERROR: No Output CSV can be found.");
        }
    }

    public static void writeRecord(CSVWriter csvWriter, TradeRecord tradeRecord){
        if (csvWriter != null){
          csvWriter.writeNext(parseTradeRecord(tradeRecord));
        } else {
            System.out.println("ERROR: No Output CSV can be found.");
        }
    }

    public static String[] parseTradeRecord(TradeRecord tr){
        String[] tradeToWrite = {String.valueOf(tr.getTradeNumber()),
                tr.getTradeTimestamp(), String.valueOf(tr.getTradeTimeInMilliSecond()),
                parseBuyFlagToString(tr.getBuyFlag()),String.valueOf(tr.getTradeQuantity()), String.valueOf(tr.getExecutedPrice()),
                String.valueOf(tr.getPnlForCurrTrade()), String.valueOf(tr.getNetPnl())};
        return tradeToWrite;
    }

    private static String parseBuyFlagToString (boolean buyFlag){
        if (buyFlag) {return Constants.BUY;}
        else {return Constants.SELL;}
    }
}
