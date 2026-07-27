package stateStore;

public class FixedWindowState {

    private long windowStartTime;
    private int requestCount;

    public FixedWindowState(long windowStartTime) {
        this.windowStartTime = windowStartTime;
        this.requestCount = 0;
    }

    public long getWindowStartTime() {
        return windowStartTime;
    }

    public void setWindowStartTime(long windowStartTime) {
        this.windowStartTime = windowStartTime;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

}
