package DA.trading.strategy.prices;

import java.util.function.Consumer;

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

    public static SMAPrices builder(Consumer<SMAPrices> block) {
        SMAPrices data = new SMAPrices();
        block.accept(data);
        return data;
    }
}
