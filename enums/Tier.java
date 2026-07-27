package enums;

public enum Tier {
    FREE,
    PREMIUM,
    ENTERPRISE;
}





//    old code -> Easy way but not appropriate (no need for policyResolver in this case)
//    FREE(new RateLimitPolicy().setLimit(2).setStrategy(new FixedWindowRateLimitingStrategy())),
//    PREMIUM(new RateLimitPolicy().setLimit(4).setStrategy(new SlidingWindowRateLimitingStrategy())),
//    ENTERPRISE(new RateLimitPolicy().setLimit(10).setStrategy(new TokenBucketRateLimitingStrategy()));

//    public final RateLimitPolicy rateLimitPolicy;

//    Tier(RateLimitPolicy rateLimitPolicy) {
//        this.rateLimitPolicy = rateLimitPolicy;
//    }