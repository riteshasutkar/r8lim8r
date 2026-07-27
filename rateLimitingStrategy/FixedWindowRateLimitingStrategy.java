package rateLimitingStrategy;

import entities.User;
import policies.RateLimitPolicy;
import stateStore.FixedWindowState;
import stateStore.FixedWindowStore;

public class FixedWindowRateLimitingStrategy implements RateLimitingStrategy {

    private final FixedWindowStore stateStore;

    public FixedWindowRateLimitingStrategy(FixedWindowStore stateStore) {
        this.stateStore = stateStore;
    }

    public boolean isRequestAllowed(User user, RateLimitPolicy policy) {
        long currentTime = System.currentTimeMillis();

        FixedWindowState state = stateStore.getOrCreate(
                user.getId(), () -> new FixedWindowState(currentTime));

        // Has the current window expired?
        if (currentTime - state.getWindowStartTime()
                >= policy.getWindowSizeMillis()) {

            state.setWindowStartTime(currentTime);
            state.setRequestCount(0);
        }

        // Limit reached?
        if (state.getRequestCount() >= policy.getLimit()) {
            return false;
        }

        // Consume one request
        state.setRequestCount(state.getRequestCount() + 1);

        stateStore.save(user.getId(), state);

        return true;
    }
}
