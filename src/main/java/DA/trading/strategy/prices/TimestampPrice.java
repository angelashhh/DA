package DA.trading.strategy.prices;

import java.text.DecimalFormat;
import java.util.function.Consumer;

public class TimestampPrice {

    private String timestamp;
    private Long timeInMilliseconds;
    private float price;
    private float volume;

    public String getTimestamp() {return timestamp;}
    public TimestampPrice setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Long getTimeInMilliSeconds() {return timeInMilliseconds;}
    public TimestampPrice setTimeInMilliseconds(Long timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
        return this;
    }

    public float getPrice() {return price;}
    public TimestampPrice setPrice(float price) {
        this.price = price;
        return this;
    }

    public float getVolume() {return volume;}
    public TimestampPrice setVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public static TimestampPrice builder(Consumer<TimestampPrice> block) {
        TimestampPrice data = new TimestampPrice();
        block.accept(data);
        return data;
    }

    public static float toFourDecimal(float x){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return Float.parseFloat(decimalFormat.format(x));
    }

}
