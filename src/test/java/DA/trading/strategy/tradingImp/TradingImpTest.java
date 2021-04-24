package DA.trading.strategy.tradingImp;

import DA.trading.strategy.prices.TimestampPrice;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TradingImpTest {
    @Test
    public void canCalculateAndRoundAverage(){
        int staringPosition = 4;
        int interval = 3;
        // TODO: mock the prices under resource folders to reuse
        // TODO: create a function that takes the four values to create TimestampPrice directly
        TimestampPrice price1= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276720000l).setPrice(1.1111f).setVolume(1111.11f));
        TimestampPrice price2= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276780000l).setPrice(1.1112f).setVolume(2222.22f));
        TimestampPrice price3= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276840000l).setPrice(1.1113f).setVolume(3333.33f));
        TimestampPrice price4= TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276960000l).setPrice(1.1118f).setVolume(4444.44f));
        List<TimestampPrice> prices = Arrays.asList(price1, price2, price3, price4);
        float expectedOutput = 1.1114f;
        assertThat(TradingImp.getSMA(4, 3, prices), is(expectedOutput));
    }
}
