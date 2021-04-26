package DA.trading.strategy;

import DA.trading.strategy.tradeRecords.TradeRecord;
import DA.trading.strategy.trading.TradingImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class AppController {
    private static final String template = "Hello, %s!";

    @GetMapping("/greeting")
    public String test(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    @PostMapping("/intervals")
    public void intervals(
            @RequestBody List<String> intervals) {
        System.out.println("WOWOWOW " + intervals);
    }

//    @GetMapping("/result")
//    public List<TradeRecord> tradeResult() {
//        Application.main();
//        return TradingImp.getTradeBook();
//    }

}
