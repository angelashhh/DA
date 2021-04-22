package DA.trading.strategy.data;

import java.util.function.Consumer;

public class TimestampPrice {

    private String timestamp;
    private int timeInMilliseconds;
    private float price;
    private float volume;

    public String getTimestamp() {return timestamp;}
    public TimestampPrice setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getTimeInMilliSeconds() {return timeInMilliseconds;}
    public TimestampPrice setTimeInMilliseconds(int timeInMilliseconds) {
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

}
