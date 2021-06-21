package VWAPService;

import org.junit.Assert;

import static org.junit.Assert.*;

public class CalculatorImplTest {
    public static final double OFFER_PRICE = 0.4;
    public static final double BID_PRICE = 0.3;
    public static final double BID_AMOUNT = 100.0;
    public static final double OFFER_AMOUNT = 200.0;

    public static final double BID_PRICE1 = 0.2;
    public static final double OFFER_PRICE1 = 0.3;
    public static final double BID_AMOUNT1 = 110.0;
    public static final double OFFER_AMOUNT1 = 300.0;

    public static final double BID_PRICE2 = 0.5;
    public static final double OFFER_PRICE2 = 0.7;
    public static final double BID_AMOUNT2 = 200.0;
    public static final double OFFER_AMOUNT2 = 300.0;

    public static final double BID_PRICE3 = 0.2;
    public static final double OFFER_PRICE3 = 0.3;
    public static final double BID_AMOUNT3 = 110.0;
    public static final double OFFER_AMOUNT3 = 30.0;

    private Calculator calculator;


    @org.junit.Before
    public void setUp() throws Exception {
        calculator = new CalculatorImplArray();
    }

    @org.junit.Test
    public void applyMarketUpdate() {
        MarketUpdate update = new MarketUpdateImpl(Market.MARKET0, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, BID_PRICE, OFFER_PRICE,
                BID_AMOUNT, OFFER_AMOUNT));

        TwoWayPrice result = calculator.applyMarketUpdate(update);

        Assert.assertEquals(BID_AMOUNT, result.getBidAmount(),   0.0001);
        Assert.assertEquals(OFFER_AMOUNT, result.getOfferAmount(),   0.0001);
        Assert.assertEquals(BID_PRICE, result.getBidPrice(),  0.0001);
        Assert.assertEquals(OFFER_PRICE, result.getOfferPrice(),   0.0001);
        Assert.assertEquals(Instrument.INSTRUMENT0, result.getInstrument());
        Assert.assertEquals(State.FIRM, result.getState());

        MarketUpdate update2 = new MarketUpdateImpl(Market.MARKET2, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.INDICATIVE, BID_PRICE1, OFFER_PRICE1,
                BID_AMOUNT1, OFFER_AMOUNT1));
        double expectedBidQty =  BID_AMOUNT + BID_AMOUNT1;
        double expectedOfferQty = OFFER_AMOUNT + OFFER_AMOUNT1;
        double expectedBidPrice = (BID_AMOUNT * BID_PRICE + BID_AMOUNT1 * BID_PRICE1) / expectedBidQty;
        double expectedOfferPrice = (OFFER_AMOUNT * OFFER_PRICE + OFFER_AMOUNT1 * OFFER_PRICE1)/ expectedOfferQty;

        result = calculator.applyMarketUpdate(update2);

        Assert.assertEquals(expectedBidQty, result.getBidAmount(),  0.0001);
        Assert.assertEquals(expectedOfferQty, result.getOfferAmount(), 0.0001);
        Assert.assertEquals(expectedBidPrice, result.getBidPrice(), 0.0001);
        Assert.assertEquals(expectedOfferPrice, result.getOfferPrice(), 0.0001);
        Assert.assertEquals(Instrument.INSTRUMENT0, result.getInstrument());
        Assert.assertEquals(State.INDICATIVE, result.getState());

        MarketUpdate update3 = new MarketUpdateImpl(Market.MARKET2, new TwoWayPriceImpl(Instrument.INSTRUMENT0, State.FIRM, BID_PRICE2, OFFER_PRICE2,
                BID_AMOUNT2, OFFER_AMOUNT2));
        expectedBidQty =  BID_AMOUNT + BID_AMOUNT2;
        expectedOfferQty = OFFER_AMOUNT + OFFER_AMOUNT2;
        expectedBidPrice = (BID_AMOUNT * BID_PRICE + BID_AMOUNT2 * BID_PRICE2) / expectedBidQty;
        expectedOfferPrice = (OFFER_AMOUNT * OFFER_PRICE + OFFER_AMOUNT2 * OFFER_PRICE2)/ expectedOfferQty;

        result = calculator.applyMarketUpdate(update3);

        Assert.assertEquals(expectedBidQty, result.getBidAmount(),  0.0001);
        Assert.assertEquals(expectedOfferQty, result.getOfferAmount(), 0.0001);
        Assert.assertEquals(expectedBidPrice, result.getBidPrice(), 0.0001);
        Assert.assertEquals(expectedOfferPrice, result.getOfferPrice(), 0.0001);
        Assert.assertEquals(Instrument.INSTRUMENT0, result.getInstrument());
        Assert.assertEquals(State.FIRM, result.getState());

        MarketUpdate update4 = new MarketUpdateImpl(Market.MARKET2, new TwoWayPriceImpl(Instrument.INSTRUMENT3, State.INDICATIVE, BID_PRICE3,
                OFFER_PRICE3,
                BID_AMOUNT3, OFFER_AMOUNT3));

        result = calculator.applyMarketUpdate(update4);

        Assert.assertEquals(BID_PRICE3,  result.getBidPrice(), 0.0001);
        Assert.assertEquals(OFFER_PRICE3, result.getOfferPrice(), 0.0001);
        Assert.assertEquals(Instrument.INSTRUMENT3, result.getInstrument());
        Assert.assertEquals(State.INDICATIVE, result.getState());

        MarketUpdate update5 = new MarketUpdateImpl(Market.MARKET2, new TwoWayPriceImpl(Instrument.INSTRUMENT4, State.INDICATIVE, BID_PRICE3,
                Double.NaN,
                BID_AMOUNT3, OFFER_AMOUNT3));

        result = calculator.applyMarketUpdate(update5);

        Assert.assertEquals(BID_PRICE3,  result.getBidPrice(), 0.0001);
        Assert.assertEquals(0.0, result.getOfferPrice(), 0.0001);
        Assert.assertEquals(Instrument.INSTRUMENT4, result.getInstrument());
        Assert.assertEquals(State.INDICATIVE, result.getState());
    }
}