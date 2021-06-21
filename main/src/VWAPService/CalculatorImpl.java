package VWAPService;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class CalculatorImpl implements Calculator{

    Map<Market, Map<Instrument, TwoWayPrice>> prices = new HashMap<>();

    @Override
    public TwoWayPrice applyMarketUpdate(MarketUpdate marketUpdate) {
        requireNonNull(marketUpdate, "Can't process null market update.");
        Market updateMarket = marketUpdate.getMarket();
        requireNonNull(updateMarket, "MarketName missing.");
        TwoWayPrice updatePrice  = marketUpdate.getTwoWayPrice();
        requireNonNull(updatePrice, "Invalid market update for " + updateMarket);
        Instrument updateInstrument = updatePrice.getInstrument();
        var marketPrices = prices.computeIfAbsent(updateMarket, market -> new HashMap<>());
        marketPrices.put(updateInstrument, updatePrice);
        return calculateVWAP(updateInstrument);
    }

    TwoWayPrice calculateVWAP(Instrument instrument) {
        double bid = 0.0;
        double bidAmount = 0.0;
        double offer = 0.0;
        double offerAmount = 0.0;
        boolean firm = true;

        for (Market market: prices.keySet()) {
           var mPrices = prices.computeIfAbsent(market, m->new HashMap<>());
           var instrumentPrice = mPrices.get(instrument);
           if (Objects.nonNull(instrumentPrice)) {
               if (!Double.isNaN(instrumentPrice.getBidPrice()) && !Double.isNaN(instrumentPrice.getBidAmount()) && instrumentPrice.getBidAmount() > 0) { // or other validation
                   bid += instrumentPrice.getBidPrice() * instrumentPrice.getBidAmount();
                   bidAmount += instrumentPrice.getBidAmount();
               }

               if (!Double.isNaN(instrumentPrice.getOfferPrice()) && !Double.isNaN(instrumentPrice.getOfferAmount()) && instrumentPrice.getOfferAmount() > 0) {
                   offer += instrumentPrice.getOfferPrice() * instrumentPrice.getOfferAmount();
                   offerAmount += instrumentPrice.getOfferAmount();
               }
               firm = firm && instrumentPrice.getState() == State.FIRM;
           }
        }
        var bidPrice = bid/bidAmount;
        var offerPrice = offer/offerAmount;

        return new TwoWayPriceImpl(instrument, firm ? State.FIRM: State.INDICATIVE, Double.isNaN(bidPrice) ? 0.0 : bidPrice,
                Double.isNaN(offerPrice) ? 0.0 : offerPrice, bidAmount, offerAmount );
    }
}
