package DA.trading.strategy.trading;

import DA.trading.strategy.prices.SMAPrices;
import DA.trading.strategy.prices.TimestampPrice;
import DA.trading.strategy.tradeRecords.TradeRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class TradingImpTest {

    @Test
    public void quickSumTest(){
        float sumForLastXPrices = 10.0000f;
        TimestampPrice latestPrice = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276960000l).setPrice(2.0000f).setVolume(4444.44f));
        TimestampPrice firstPrice = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276960000l).setPrice(1.0000f).setVolume(4444.44f));
        assertThat(TradingImp.quickSum(sumForLastXPrices, latestPrice, firstPrice), is(11.0000f));
    }

    @Test
    public void slowSumTest(){
        TimestampPrice price1= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276720000l).setPrice(1.0000f).setVolume(1111.11f));
        TimestampPrice price2= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276780000l).setPrice(1.0000f).setVolume(2222.22f));
        List<TimestampPrice> lastPrices = Arrays.asList(price1, price2);
        TimestampPrice latestPrice = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276960000l).setPrice(1.0000f).setVolume(4444.44f));
        assertThat(TradingImp.slowSum(lastPrices, latestPrice), is(3.0000f));
    }

    @Test
    public void updateNetPositionTest(){
        TradingImp tradingImp = new TradingImp();
        TradeRecord buyTrade = TradeRecord.builder(block -> block.setBuyFlag(true).setTradeQuantity(100.0000f));
        TradeRecord sellTrade = TradeRecord.builder(block -> block.setBuyFlag(false).setTradeQuantity(20.0000f));
        TradingImp.updateNetPosition(buyTrade);
        assertThat(tradingImp.getNetPosition(), is(100.0000f));
        TradingImp.updateNetPosition(sellTrade);
        assertThat(tradingImp.getNetPosition(), is(80.0000f));
    }

    @Test
    public void canDecideBuy(){
        TradingImp tradingImp = new TradingImp();
        tradingImp.setLastTradeRecord(TradeRecord.builder(block -> block.setBuyFlag(false)));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(2.0000f).setSmaForN(1.0000f));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setVolume(10));
        TradeRecord expectedBuyRecord = TradeRecord.builder(block -> block.setBuyFlag(true).setTradeQuantity(5f)
                .setTimestampPrice(latestPrice).setTradeNumber(0));
        TradeRecord actualTrade = TradingImp.buyOrSell(smaPrices, latestPrice);
        assertThat(actualTrade, samePropertyValuesAs(expectedBuyRecord));
    }

    @Test
    public void canDecideSell(){
        TradingImp tradingImp = new TradingImp();
        tradingImp.setLastTradeRecord(TradeRecord.builder(block -> block.setBuyFlag(true)));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(1.0000f).setSmaForN(2.0000f));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setVolume(10));
        TradeRecord expectedBuyRecord = TradeRecord.builder(block -> block.setBuyFlag(false).setTradeQuantity(5f)
                .setTimestampPrice(latestPrice).setTradeNumber(1));
        TradeRecord actualTrade = TradingImp.buyOrSell(smaPrices, latestPrice);
        assertThat(actualTrade, samePropertyValuesAs(expectedBuyRecord));
    }

    @Test
    public void canDecideToNotBuyWithoutOpenPosition(){
        TradingImp tradingImp = new TradingImp();
        tradingImp.setLastTradeRecord(TradeRecord.builder(block -> block.setBuyFlag(true)));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(2.0000f).setSmaForN(1.0000f));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setVolume(10));
        TradeRecord expectedBuyRecord = null;
        TradeRecord actualTrade = TradingImp.buyOrSell(smaPrices, latestPrice);
        assertThat(actualTrade, is(expectedBuyRecord));
    }

    @Test
    public void canDecideToNotSellWithoutOpenPosition(){
        TradingImp tradingImp = new TradingImp();
        tradingImp.setLastTradeRecord(TradeRecord.builder(block -> block.setBuyFlag(false)));
        SMAPrices smaPrices = SMAPrices.builder(block -> block.setSmaForM(1.0000f).setSmaForN(2.0000f));
        TimestampPrice latestPrice = TimestampPrice.builder(block ->
                block.setPrice(4.0000f).setTimeInMilliseconds(1234567890l).setVolume(10));
        TradeRecord expectedBuyRecord = null;
        TradeRecord actualTrade = TradingImp.buyOrSell(smaPrices, latestPrice);
        assertThat(actualTrade, is(expectedBuyRecord));
    }
}

