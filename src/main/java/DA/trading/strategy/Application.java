package DA.trading.strategy;

import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.ReadCSV;

import java.io.*;

public class Application {

    public static void main (String[] args) {
        int[] intervals = Commands.readInputAsIntervals();
        int m = intervals[0];
        int n = intervals[1];

        BufferedReader priceReader = ReadCSV.priceReader();
        TradingImp.decideTradePerPrice(priceReader, m, n);
    }
}
