package DA.trading.strategy;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.trading.TradingImp;
import DA.trading.strategy.utils.Constants;
import DA.trading.strategy.utils.CsvReader;
import DA.trading.strategy.utils.CsvWriter;
import DA.trading.strategy.utils.Intervals;
import com.opencsv.CSVWriter;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppController {

    @PostMapping("/intervals")
    public void tradeWithDefinedIntervals(@RequestBody Intervals intervals) {
        System.out.println("Received M and N: " + intervals.getM() + "|" + intervals.getN());
        Application.trade(intervals.getM(), intervals.getN());
    }

    @GetMapping("/prices")
    public List<TimestampPrice> readPrices() throws IOException {
        BufferedReader br = CsvReader.fileReader(Constants.INPUT_PRICES_CSV);
        List<TimestampPrice> prices = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            TimestampPrice price = CsvReader.readLineAsPrice(line);
            if (price != null ) {
                prices.add(price);
            }
        }
        return prices;
    }

    @GetMapping("/results")
    public  List<TradeRecord> readTradeResults() throws IOException {
        String fileName = "/Users/apple/Downloads/trading-strategy/Trades.csv";
        String line;
        BufferedReader br = new BufferedReader(new FileReader (fileName));
        List<TradeRecord> tradeBook = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            TradeRecord tradeRecord = CsvReader.readLineAsTradeRecord(line);
            if (tradeRecord != null ) {
                tradeBook.add(tradeRecord);
            }
        }
        return tradeBook;
    }

}
