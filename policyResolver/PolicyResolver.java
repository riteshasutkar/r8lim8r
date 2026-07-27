package policyResolver;

import entities.User;
import policies.RateLimitPolicy;

public interface PolicyResolver {

    RateLimitPolicy resolve(User user);

}
