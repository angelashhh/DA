package DA.trading.strategy.prices;

public class SMAPrices {

    private float smaForM;
    private float smaForN;

    public float getSmaForM() {return smaForM;}
    public SMAPrices setSmaForM(float smaForM) {
        this.smaForM = smaForM;
        return this;
    }

    public float getSmaForN() {return smaForN;}
    public SMAPrices setSmaForN(float smaForN) {
        this.smaForN = smaForN;
        return this;
    }
}
