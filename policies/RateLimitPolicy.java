package policies;

import rateLimitingStrategy.RateLimitingStrategy;

public class RateLimitPolicy {
    private int limit;
    private long windowSizeMillis;
    private RateLimitingStrategy strategy;

    public RateLimitPolicy(int limit,long windowSize, RateLimitingStrategy strategy) {
        this.limit = limit;
        this.windowSizeMillis = windowSize;
        this.strategy = strategy;
    }

    public RateLimitPolicy() {
    }

    public RateLimitPolicy setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public RateLimitPolicy setStrategy(RateLimitingStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public RateLimitPolicy setWindowSize(long windowSize) {
        this.windowSizeMillis = windowSize;
        return this;
    }

    public RateLimitingStrategy getStrategy() {
        return strategy;
    }

    public int getLimit() {
        return limit;
    }

    public long getWindowSizeMillis() {
        return windowSizeMillis;
    }
}
