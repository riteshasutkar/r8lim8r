package rateLimitingStrategy;

import entities.User;
import policies.RateLimitPolicy;

public interface RateLimitingStrategy {
    boolean isRequestAllowed(User user, RateLimitPolicy policy);
}
