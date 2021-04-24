package DA.trading.strategy.tradingImp;

import DA.trading.strategy.prices.PriceFormatter;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;

import java.util.ArrayList;
import java.util.List;

public class TradingImp {

    List<TradeRecord> tradeBook = new ArrayList<>();

    public static void trade (int m, int n, List<TimestampPrice> prices){
        TradeRecord currentTradeRecord = new TradeRecord();
        currentTradeRecord.setBuyFlat(true);
        for (int rowNumber = n; rowNumber <= prices.size(); rowNumber++){          //m<n
            float smaForIntervalM = getSMA(rowNumber, m, prices);
            float smaForIntervalN = getSMA(rowNumber, n, prices);
            //buy condition: when sma(m)>sma(n), buy=false(sell has been done)
            if (smaForIntervalM > smaForIntervalN && currentTradeRecord.getBuyFlag()){
//                buy(currentTradeRecord, prices.get(rowNumber-1));
            }
            if (smaForIntervalM < smaForIntervalN && !currentTradeRecord.getBuyFlag()){
//                sell();
            }
        }
    }

    public static void buy (TradeRecord currTradeRecord, TimestampPrice timestampPrice) {
        // create a new TradeRecord object by the following actions
        // hold trade number
        // record trade side
        // extract tradetime, price info from timestampPrice
        // calculate trade quantity from price
        // leave currentTrade PNL as null
        // update net PNL
        // add the constructed tradeRecord into tradeBook
    }

    public static void sell() {
        // update trade number by 1
        // record trade side
        // extract tradetime, price info from timestampPrice
        // calculate trade quantity from price
        // update currentTrade PNL
        // add the constructed tradeRecord into tradeBook
    }

    public static float getSMA (int startingRow, int interval, List<TimestampPrice> prices){
        float sum = 0;
        for (int i = startingRow; i > startingRow-interval; i--) {
            sum += prices.get(i-1).getPrice();
        }
        return PriceFormatter.toFourDecimal(sum / (float) interval);
    }

}
