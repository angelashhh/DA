package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

//    public static BufferedReader priceReader (){
//        ReadCSV obj = new ReadCSV();
//        InputStream priceFile = obj.getClass().getClassLoader().getResourceAsStream("Prices.csv");
//        if (priceFile != null){
//            return new BufferedReader(new InputStreamReader(priceFile));
//        } else {
//            System.out.println("Failed to read price file.");
//            return null;
//        }
//    }

//    public static TimestampPrice readLineAsPrice(String line) {
//        if (line != null){
//            String[] lineAsArray = line.split(",") ;
//            return parsePrice(lineAsArray);
//        } else {return null;}
//    }

    public static List<TimestampPrice> readPricesFromFile(InputStream priceFile) {
        BufferedReader priceReader = new BufferedReader(new InputStreamReader(priceFile));
        List<TimestampPrice> prices = new ArrayList<>();
        String line;
        try {
            while ((line = priceReader.readLine()) != null) {
                String[] lineAsArray = line.split(",") ;
                prices.add(parsePrice(lineAsArray));
            }
            System.out.println("***Price import success***");
            return prices;
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
            return null;
        } catch (IOException e) {
            System.out.println("ERROR: cannot read price file.");
            return null;
        }
    }

    public static List<TimestampPrice> getPrices() {
        System.out.println("***Price import starting***");

        ReadCSV obj = new ReadCSV();
        InputStream priceFile = obj.getClass().getClassLoader().getResourceAsStream("Prices.csv");

        return readPricesFromFile(priceFile);
    }
}
