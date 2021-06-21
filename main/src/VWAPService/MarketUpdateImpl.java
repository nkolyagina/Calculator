package VWAPService;

public class MarketUpdateImpl implements MarketUpdate {

    private final Market market;
    private final TwoWayPrice price;

    public MarketUpdateImpl(Market market, TwoWayPrice price) {
        this.market = market;
        this.price = price;
    }

    @Override
    public Market getMarket() {
        return market;
    }

    @Override
    public TwoWayPrice getTwoWayPrice() {
        return price;
    }
}
