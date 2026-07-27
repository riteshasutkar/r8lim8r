import entities.User;
import policies.RateLimitPolicy;
import policyResolver.PolicyResolver;

public class RateLimiter {
    private final PolicyResolver policyResolver;

    public RateLimiter(PolicyResolver policyResolver) {
        this.policyResolver = policyResolver;
    }

    public boolean isRequestAllowed(User user) {
        RateLimitPolicy policy = policyResolver.resolve(user);
        return policy.getStrategy().isRequestAllowed(user, policy);
    }
}
