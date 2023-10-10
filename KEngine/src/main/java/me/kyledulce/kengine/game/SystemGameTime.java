package me.kyledulce.kengine.game;

import jakarta.inject.Singleton;

@Singleton
public class SystemGameTime implements GameTime {

    private long gameStartTime = 0;

    @Override
    public void setStartTime() {
        gameStartTime = getCurrentTimeMillis();
    }

    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public long getCurrentTimeMillisSinceStart() {
        return getCurrentTimeMillis() - gameStartTime;
    }
}
