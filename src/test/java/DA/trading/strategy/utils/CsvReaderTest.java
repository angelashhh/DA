package DA.trading.strategy.utils;

import DA.trading.strategy.prices.TimestampPrice;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;


public class CsvReaderTest {

    @Test
    public void canParsePrice(){
        String[] line = {"2020-06-27", "1593276720000", "4.4972", "4328.29"};
        TimestampPrice expectedOutput = TimestampPrice.builder(block -> block.setTimestamp("2020-06-27")
                .setTimeInMilliseconds(1593276720000l)
                .setPrice(4.4972f)
                .setVolume(4328.29f));
        assertThat(CsvReader.parsePrice(line), samePropertyValuesAs(expectedOutput));
    }
}
