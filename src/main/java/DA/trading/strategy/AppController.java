package DA.trading.strategy;
import DA.trading.strategy.utils.Intervals;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AppController {

    @PostMapping("/intervals")
    public void tradeWithDefinedIntervals(@RequestBody Intervals intervals) {
        System.out.println("Received M and N: " + intervals.getM() + "|" + intervals.getN());
//        Application.trade(intervals.getM(), intervals.getN());
    }

//    @GetMapping("/results")
//    public List<TradeRecord> tradeResult() {
//        return TradingImp.getTradeBook();
//    }

}
