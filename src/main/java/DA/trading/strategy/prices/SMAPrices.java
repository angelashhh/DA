package DA.trading.strategy.prices;

import java.util.function.Consumer;

public class SMAPrices {

    float smaForM;
    float smaForN;

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

    public static SMAPrices builder(Consumer<SMAPrices> block) {
        SMAPrices data = new SMAPrices();
        block.accept(data);
        return data;
    }

    public static SMAPrices calcSMAs(SumPrices currSums, int m, int n){
        SMAPrices currSMAPrices = new SMAPrices();
        currSMAPrices.setSmaForM(currSums.getSumForM() / m);
        currSMAPrices.setSmaForN(currSums.getSumForN() / n);
        return currSMAPrices;
    }
}
