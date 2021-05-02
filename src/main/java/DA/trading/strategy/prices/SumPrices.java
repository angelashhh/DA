package DA.trading.strategy.prices;

import java.util.List;

public class SumPrices {

    private float sumForM;
    private float sumForN;

    public float getSumForM() {return sumForM;}
    public SumPrices setSumForM(float sumForM) {
        this.sumForM = sumForM;
        return this;
    }

    public float getSumForN() {return sumForN;}
    public SumPrices setSumForN(float sumForN) {
        this.sumForN = sumForN;
        return this;
    }

    public static SumPrices calcSums(List<TimestampPrice> lastMPrices, List<TimestampPrice> lastNPrices,
                                     TimestampPrice latestPrice, SumPrices lastSums){
        SumPrices currSums = new SumPrices();
        currSums.setSumForM(calcSum(lastSums.getSumForM(), latestPrice, lastMPrices));
        currSums.setSumForN(calcSum(lastSums.getSumForN(), latestPrice, lastNPrices));
        return currSums;
    }

    private static float calcSum(float lastSum, TimestampPrice latestPrice, List<TimestampPrice> lastPrices){
        return TimestampPrice.toFourDecimal(lastSum + latestPrice.getPrice() - lastPrices.get(0).getPrice());
    }

}
