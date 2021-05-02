package DA.trading.strategy.trading;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.SumPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class TradingImpTest {

    @Test
    public void canDecideBuy(){
        float largerSma = 2.0000f;
        float smallerSma = 1.0000f;
        TradeRecord lastTradeRecord = TradeRecord.builder(block -> block.setBuyFlag(false));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(largerSma).setSmaForN(smallerSma));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setTimestamp("2020-12-01"));

        TradeRecord expectedBuyRecord = TradeRecord.builder(block -> block.setBuyFlag(true).setTradeQuantity(5.0000f)
                .setExecutedPrice(latestPrice.getPrice()).setTradeNumber(0)
                .setTradeTimeInMilliSecond(1234567890l).setTradeTimestamp("2020-12-01"));
        TradeRecord actualTrade = TradingImp.decideTrade(smaPrices, latestPrice, lastTradeRecord);

        assertThat(actualTrade, samePropertyValuesAs(expectedBuyRecord));
    }
    @Test
    public void canDecideSell(){
        float largerSma = 2.0000f;
        float smallerSma = 1.0000f;
        TradeRecord lastTradeRecord = TradeRecord.builder(block -> block.setBuyFlag(true));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(smallerSma).setSmaForN(largerSma));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setTimestamp("2020-12-01"));

        TradeRecord expectedBuyRecord = TradeRecord.builder(block -> block.setBuyFlag(false).setTradeQuantity(5.0000f)
                .setExecutedPrice(latestPrice.getPrice()).setTradeNumber(1)
                .setTradeTimeInMilliSecond(1234567890l).setTradeTimestamp("2020-12-01"));
        TradeRecord actualTrade = TradingImp.decideTrade(smaPrices, latestPrice, lastTradeRecord);

        assertThat(actualTrade, samePropertyValuesAs(expectedBuyRecord));
    }

    @Test
    public void canDecideToNotBuyWithoutOpenPosition(){
        float largerSma = 2.0000f;
        float smallerSma = 1.0000f;
        TradeRecord lastTradeRecord = TradeRecord.builder(block -> block.setBuyFlag(true));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(largerSma).setSmaForN(smallerSma));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setTimestamp("2020-12-01"));
        TradeRecord expectedBuyRecord = null;
        TradeRecord actualTrade = TradingImp.decideTrade(smaPrices, latestPrice, lastTradeRecord);

        assertThat(actualTrade, is(expectedBuyRecord));
    }

    @Test
    public void canDecideToNotSellWithoutOpenPosition(){
        float largerSma = 2.0000f;
        float smallerSma = 1.0000f;
        TradeRecord lastTradeRecord = TradeRecord.builder(block -> block.setBuyFlag(false));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(smallerSma).setSmaForN(largerSma));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setTimestamp("2020-12-01"));
        TradeRecord expectedBuyRecord = null;
        TradeRecord actualTrade = TradingImp.decideTrade(smaPrices, latestPrice, lastTradeRecord);

        assertThat(actualTrade, is(expectedBuyRecord));
    }
}

