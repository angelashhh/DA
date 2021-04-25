package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;

import java.io.*;

public class ReadCSV {

    public static TimestampPrice parsePrice(String[] line){
        if (!line[0].equals("timestamp")){
            return TimestampPrice.builder(block -> block.setTimestamp(line[0])
                    .setTimeInMilliseconds(Long.parseLong(line[1]))
                    .setPrice(Float.parseFloat(line[2]))
                    .setVolume(Float.parseFloat(line[3])));
        } else {return null;}
    }

    public static BufferedReader priceReader(){
        ReadCSV obj = new ReadCSV();
        InputStream priceFile = obj.getClass().getClassLoader().getResourceAsStream("Prices.csv");
        return new BufferedReader(new InputStreamReader(priceFile));
    }

    public static TimestampPrice readLineAsPrice(String line){
        String[] lineAsArray = line.split(",");
        if (!lineAsArray[0].equals("timestamp")){
            return parsePrice(lineAsArray);
        } else {return null;}
    }
}
