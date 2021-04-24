package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class CommandsTest {

    @Test
    public void canCreateLatestXPrices(){
        TimestampPrice price1 = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276720000l).setPrice(1.1111f).setVolume(1000.01f));
        List<TimestampPrice> latestPrices = new ArrayList<>();
        List<TimestampPrice> expectedOutput = new ArrayList<>();
        expectedOutput.add(price1);

        assertThat(Commands.updateLastXPrices(2, price1, latestPrices), is(expectedOutput));
    }

    @Test
    public void canUpdateLatestXPrices(){
        TimestampPrice price1 = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276720000l).setPrice(1.1111f).setVolume(1000.01f));
        TimestampPrice price2 = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276780000l).setPrice(1.1112f).setVolume(1000.02f));
        TimestampPrice price3 = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276840000l).setPrice(1.1113f).setVolume(1000.03f));
        List<TimestampPrice> latestTwoPrices = new ArrayList<>();
        latestTwoPrices.addAll(Arrays.asList(price1, price2));
        List<TimestampPrice> expectedOutput = new ArrayList<>();
        expectedOutput.addAll(Arrays.asList(price2, price3));

        assertThat(Commands.updateLastXPrices(2, price3, latestTwoPrices), is(expectedOutput));
    }
}
