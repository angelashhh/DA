package DA.trading.strategy.utils;

import DA.trading.strategy.tradeRecords.TradeRecord;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CsvWriterTest {
    @Test
    public void ParseTradeRecordToStringArrayTest() {
        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setTradeTimestamp("2020-06-27").setTradeTimeInMilliSecond(1234567890l).setExecutedPrice(1.1111f)
                .setTradeQuantity(2.2222f).setBuyFlag(true).setTradeNumber(1)
                .setPnlForCurrTrade(0.1234f).setNetPnl(3.3333f);
        String[] expectedRecordToWrite = {"1", "2020-06-27", "1234567890", "buy", "2.2222", "1.1111", "0.1234", "3.3333"};
        assertThat(CsvWriter.parseTradeRecord(tradeRecord), is(expectedRecordToWrite));

    }
}
