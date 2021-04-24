package DA.trading.strategy.prices;

import java.text.DecimalFormat;

public class PriceFormatter {
    public static float toFourDecimal(float price){
        DecimalFormat decimalFormat = new DecimalFormat("#.####");
        return Float.parseFloat(decimalFormat.format(price));
    }
}
