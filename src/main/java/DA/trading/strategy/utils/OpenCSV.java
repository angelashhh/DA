package DA.trading.strategy.utils;

import DA.trading.strategy.data.TimestampPrice;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.List;

public class OpenCSV {

    public static TimestampPrice parsePrice(String[] line){
        return TimestampPrice.builder(block -> block.setTimestamp(line[1])
                .setTimeInMilliseconds(Integer.parseInt(line[2]))
                .setPrice(Float.parseFloat(line[3]))
                .setVolume(Float.parseFloat(line[4])));
    }

    public static void priceScanner() {
        String fileName = "/resources/Prices.csv";
        List<TimestampPrice> prices = new ArrayList<>();
        String line;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while ((line = reader.readLine()) != null) {
                String[] lineAsArray = line.split(",") ;
                prices.add(parsePrice(lineAsArray));
            }
            System.out.println("File import success.");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read price file.");
        }
    }
}
