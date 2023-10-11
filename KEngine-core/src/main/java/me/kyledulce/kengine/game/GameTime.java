package me.kyledulce.kengine.game;

public interface GameTime {
    void setStartTime();
    long getCurrentTimeMillis();
    long getCurrentTimeMillisSinceStart();
}
