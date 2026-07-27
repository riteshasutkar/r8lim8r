package policyResolver;

import entities.User;
import enums.Tier;
import policies.RateLimitPolicy;
import rateLimitingStrategy.FixedWindowRateLimitingStrategy;
import stateStore.FixedWindowStore;

import java.util.Map;

/**
 * DefaultPolicyResolver is a concrete implementation of the PolicyResolver interface.
 * It resolves the appropriate RateLimitPolicy for a given User based on their Tier.
 *
 * We can easily add new PolicyResolvers in the future if we want to support different resolution strategies.
 * For example, for BLACK FRIDAY SALE all users get new rate limits,
 * User Info related - User from company X should have a different policy.
 * If we want to add new tiers in the future, we can simply extend the Tier enum and update this resolver accordingly.
 */
public class DefaultPolicyResolver implements PolicyResolver{

    private final Map<Tier, RateLimitPolicy> policies;

    public DefaultPolicyResolver(){
        FixedWindowStore fixedWindowStore = new FixedWindowStore();
//    SlidingWindowStore slidingWindowStore = new SlidingWindowStore();
//    TokenBucketStore tokenBucketStore = new TokenBucketStore();

        policies = Map.of(
                Tier.FREE,
                new RateLimitPolicy()
                        .setLimit(2)
                        .setWindowSize(2000)
                        .setStrategy(new FixedWindowRateLimitingStrategy(fixedWindowStore)),
                Tier.PREMIUM,
                new RateLimitPolicy()
                        .setLimit(4)
                        .setWindowSize(2000)
                        .setStrategy(new FixedWindowRateLimitingStrategy(fixedWindowStore)),
                Tier.ENTERPRISE,
                new RateLimitPolicy()
                        .setLimit(10)
                        .setWindowSize(2000)
                        .setStrategy(new FixedWindowRateLimitingStrategy(fixedWindowStore))
        );
    }

    public RateLimitPolicy resolve(User user) {
        return policies.get(user.getTier());
    }
}
