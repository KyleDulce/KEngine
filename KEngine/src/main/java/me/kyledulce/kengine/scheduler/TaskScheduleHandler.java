package me.kyledulce.kengine.scheduler;

public interface TaskScheduleHandler extends TaskScheduler {
    /**
     * Grabs tasks to run at current time and removes them as executed
     * @return Tasks to run in current loop and time
     */
    Runnable[] getTasksToRunAndUpdate();
}
