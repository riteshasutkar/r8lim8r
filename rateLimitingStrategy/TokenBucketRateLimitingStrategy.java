package rateLimitingStrategy;

import entities.User;
import policies.RateLimitPolicy;

public class TokenBucketRateLimitingStrategy implements RateLimitingStrategy {

    public boolean isRequestAllowed(User user, RateLimitPolicy policy) {
//        int tokens = user.getTokens();
//        if (tokens > 0) {
//            user.setTokens(tokens - 1);
//            return true;
//        }
        return false;
    }
}
