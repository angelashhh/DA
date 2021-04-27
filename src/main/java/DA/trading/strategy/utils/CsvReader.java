package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;

import java.io.*;

public class CsvReader {

    public static BufferedReader fileReader(String fileName){
        CsvReader obj = new CsvReader();
        InputStream priceFile = obj.getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(priceFile));
    }

    public static TimestampPrice readLineAsPrice(String line){
        String[] lineAsArray = line.split(",");
        if (!lineAsArray[0].equals("timestamp")){
            return parsePrice(lineAsArray);
        } else {return null;}
    }

    public static TimestampPrice parsePrice(String[] line){
        return TimestampPrice.builder(block -> block.setTimestamp(line[0])
                .setTimeInMilliseconds(Long.parseLong(line[1]))
                .setPrice(Float.parseFloat(line[2]))
                .setVolume(Float.parseFloat(line[3])));
    }

    public static TradeRecord readLineAsTradeRecord(String line){
        String[] lineAsArray = line.split(",");
        if (!lineAsArray[0].equals("TradeNumber")){
            return parseTradeRecord(lineAsArray);
        } else {return null;}
    }

    public static TradeRecord parseTradeRecord(String[] line){
        return TradeRecord.builder(block -> block.setTradeNumber(Integer.parseInt(line[0]))
                .setTradeTimestamp(line[1])
                .setTradeTimeInMilliSecond(Long.parseLong(line[2]))
                .setBuyFlag(parseTradeSideToBuyFlag(line[3]))
                .setTradeQuantity(Float.parseFloat(line[4]))
                .setExecutedPrice(Float.parseFloat(line[5]))
                .setPnlForCurrTrade(Float.parseFloat(line[6]))
                .setNetPnl(Float.parseFloat(line[7])));
    }

    public static boolean parseTradeSideToBuyFlag(String tradeSide){
        return tradeSide.equals(Constants.BUY);
    }
}
