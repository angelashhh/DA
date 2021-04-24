package DA.trading.strategy;

import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.OpenCSV;

import java.util.List;
import java.util.Scanner;

public class Application {

    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] intervals = Commands.readInputAsIntervals(scanner);
        int m = intervals[0];
        int n = intervals[1];
        List<TimestampPrice> prices = OpenCSV.getPrices();
//        TradingImp.trade(intervals, prices)
    }
}
