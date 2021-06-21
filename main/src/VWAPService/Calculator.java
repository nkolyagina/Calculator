package VWAPService;

import java.util.HashMap;
import java.util.Map;

public interface Calculator {

    TwoWayPrice applyMarketUpdate(final MarketUpdate twoWayMarketPrice);
}


