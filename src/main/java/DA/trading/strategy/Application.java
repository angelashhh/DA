package DA.trading.strategy;

import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.trading.Constants;
import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Commands;
import DA.trading.strategy.utils.CsvReader;
import DA.trading.strategy.utils.TradeWriter;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Application {

//    public static void trade (int m, int n) {
//        BufferedReader priceReader = CsvReader.fileReader(Constants.INPUT_PRICES_CSV);
//        TradingImp.tradeAsPriceIsRead(priceReader, m, n);
//    }
//}

//    Uncomment the below code to replace the above to create a command window programme
    public static void main (String[] args) {
        int[] intervals = Commands.readInputAsIntervals();
        int m = intervals[0];
        int n = intervals[1];
        BufferedReader priceReader = CsvReader.fileReader(Constants.INPUT_PRICES_CSV);
        TradingImp.tradeAsPriceIsRead(priceReader, m, n);
        }
}