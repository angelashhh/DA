package DA.trading.strategy;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.SumPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.ReadCSV;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main (String[] args) {
        int[] intervals = Commands.readInputAsIntervals();
        int m = intervals[0];
        int n = intervals[1];

        int lineNumber = 0;
        List<TimestampPrice> lastMPrices = new ArrayList<>();
        List<TimestampPrice> lastNPrices = new ArrayList<>();

        BufferedReader priceReader = ReadCSV.priceReader();
        String line;
        try {
            while ((line = priceReader.readLine()) != null) {
                TimestampPrice latestPrice = ReadCSV.readLineAsPrice(line);
                if (latestPrice!=null) {
                    lineNumber += 1;
                    if (lineNumber >= m){
                        SumPrices currSums = TradingImp.updateSums(m, n, lineNumber, lastMPrices, lastNPrices, latestPrice);
//                        System.out.println("currSums" + currSums.getSumForM() + "|" + currSums.getSumForN());
                        if (lineNumber >= n){
                            SMAPrices currSMAs = TradingImp.updateSMAs(currSums, m, n);
//                            System.out.println("currSMAs" + currSMAs.getSmaForM() + "|" + currSMAs.getSmaForN());
                            TradeRecord tradeRecord = TradingImp.decideTrade(currSMAs, latestPrice);
                            if (tradeRecord != null){
                                TradingImp.executeTrade(tradeRecord);
                            }
                        }
                        //lastXPrices does not include current price
                        lastMPrices = Commands.updateLastXPrices(m, latestPrice, lastMPrices);
                        lastNPrices = Commands.updateLastXPrices(n, latestPrice, lastNPrices);
                    }
                }
            }
            System.out.println(lineNumber); // to delete, just to confirm all has been through
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: price file not found.");
        } catch (IOException e) {
            System.out.println("ERROR: cannot read price file.");
        }
    }
}
