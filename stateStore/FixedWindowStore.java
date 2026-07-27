package stateStore;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class FixedWindowStore {

    private ConcurrentHashMap<String, FixedWindowState> stateMap = new ConcurrentHashMap<>();

    public FixedWindowState getOrCreate(String userId, Supplier<FixedWindowState> supplier) {
        return stateMap.computeIfAbsent(userId, key -> supplier.get());
    }

    public void save(String userId, FixedWindowState state) {
        stateMap.put(userId, state);
    }

    public FixedWindowState get(String userId) {
        return stateMap.get(userId);
    }

    public void remove(String userId) {
        stateMap.remove(userId);
    }

    public void clear() {
        stateMap.clear();
    }
}
