package DA.trading.strategy;

import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.ReadCSV;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] intervals = Commands.readInputAsIntervals(scanner);
        int m = intervals[0];
        int n = intervals[1];
        int lineNumber = 0;
        List<TimestampPrice> lastMPrices = new ArrayList<>();
        List<TimestampPrice> lastNPrices = new ArrayList<>();
        BufferedReader priceReader = ReadCSV.priceReader();

        try {
            while (priceReader.readLine() != null) {
                TimestampPrice latestPrice = ReadCSV.readLineAsPrice(priceReader.readLine());
                lineNumber += 1;
                if (lineNumber >= n){
                  TradingImp.makeTradeDecision(m, n, lineNumber, lastMPrices, lastNPrices, latestPrice);
                }
                //lastXPrices does not include current price
                lastMPrices = Commands.updateLastXPrices(m, latestPrice, lastMPrices);
                lastNPrices = Commands.updateLastXPrices(n, latestPrice, lastNPrices);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read price file.");
        }
    }
}
