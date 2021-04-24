package DA.trading.strategy.prices;

public class SumPrices {

    private float sumForM;
    private float sumForN;

    public SumPrices getSumPrices() {return this;}
    public SumPrices setSumPrices(SumPrices sumPrices){
        return sumPrices;
    }

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

}
