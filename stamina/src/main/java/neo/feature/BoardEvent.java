package neo.feature;

public interface BoardEvent {
    public void setLastEventTime(long lastEventTime);
    public void setCoolDown(Double staminaCoolDown);
    public long getLastEventTime();
    public Double getCoolDown();
    public void cancelScheduler();
    public void runScheduler();
}
