package DA.trading.strategy;

import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.ReadCSV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

public class Application {

//    public static void main (String[] args) {
//        int[] intervals = Commands.readInputAsIntervals();
//        int m = intervals[0];
//        int n = intervals[1];
public static void main () {
        int m = 2;
        int n = 3;
        BufferedReader priceReader = ReadCSV.priceReader();
        TradingImp.decideTradePerPrice(priceReader, m, n);
    }
}