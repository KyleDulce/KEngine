package me.kyledulce.kengine.scheduler;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.kyledulce.kengine.game.GameTime;
import me.kyledulce.kengine.resource.SystemResourceManager;
import me.kyledulce.kengine.resource.TaskFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Class that Schedules Tasks to the Game loop using a map
 * Tasks are given id numbers on creation. If the task is negative, then the task is asynchronous,
 * otherwise it is a synchronous task
 */
@Singleton
public class MappedTaskScheduler implements TaskScheduleHandler {

    @Getter(onMethod = @__({@TestOnly}), value = AccessLevel.PACKAGE)
    private final HashMap<Integer, ScheduledTask> waitingTasks = new HashMap<>();
    @Getter(onMethod = @__({@TestOnly}), value = AccessLevel.PACKAGE)
    @Setter(onMethod = @__({@TestOnly}), value = AccessLevel.PACKAGE)
    private int nextTaskId = 1;

    private final GameTime gameTime;
    private final SystemResourceManager systemResourceManager;

    @Inject
    public MappedTaskScheduler(GameTime gameTime, SystemResourceManager systemResourceManager) {
        this.gameTime = gameTime;
        this.systemResourceManager = systemResourceManager;
    }

    public int scheduleSynchronousTask(@NotNull Runnable task) {
        return scheduleSynchronousTask(task, 0);
    }

    public int scheduleSynchronousTask(@NotNull Runnable task, long delayMillis) {
        return scheduleSynchronousTask(task, 0, delayMillis, false);
    }

    public int scheduleRepeatingSynchronousTask(@NotNull Runnable task, long intervalMillis) {
        return scheduleRepeatingSynchronousTask(task, intervalMillis, 0);
    }

    public int scheduleRepeatingSynchronousTask(@NotNull Runnable task, long intervalMillis, long delayMillis) {
        return scheduleSynchronousTask(task, intervalMillis, delayMillis, true);
    }

    /**
     * Schedules generic Synchronous task
     *
     * @param task           task to run
     * @param intervalMillis time in milliseconds to between execution of the task
     * @param delayMillis    time in milliseconds to delay the initial task execution
     * @param repeating      if true, schedules as a repeating task. if false will execute once
     * @return the id of the scheduled task
     */
    private int scheduleSynchronousTask(@NotNull Runnable task, long intervalMillis, long delayMillis, boolean repeating) {
        int id = generateTaskId(true);
        scheduleSynchronousTask(id, task, intervalMillis, delayMillis, repeating);
        return id;
    }

    /**
     * Schedules generic Synchronous task given an id. If the id is already present, will override old task
     *
     * @param id             the id of the task to schedule
     * @param task           task to run
     * @param intervalMillis time in milliseconds to between execution of the task
     * @param delayMillis    time in milliseconds to delay the initial task execution
     * @param repeating      if true, schedules as a repeating task. if false will execute once
     */
    private void scheduleSynchronousTask(int id, @NotNull Runnable task, long intervalMillis, long delayMillis, boolean repeating) {
        ScheduledTask scheduledTask = new ScheduledTask(
                id,
                task,
                delayMillis,
                intervalMillis,
                gameTime.getCurrentTimeMillis(),
                repeating);

        waitingTasks.put(id, scheduledTask);
    }

    public int scheduleAsynchronousTask(@NotNull Runnable task, Runnable onComplete) {
        int id = generateTaskId(false);
        scheduleAsynchronousTask(id, task, onComplete);
        return id;
    }

    public int scheduleAsynchronousTask(@NotNull Runnable task, Runnable onComplete, long delayMillis) {
        int id = generateTaskId(false);
        scheduleSynchronousTask(id, () -> scheduleAsynchronousTask(id, task, onComplete), 0, delayMillis, false);
        return id;
    }

    /**
     * Schedules Asynchronous task
     *
     * @param id         the id of the task to schedule
     * @param task       task to run. must not be null
     * @param onComplete task to run on completion
     */
    private void scheduleAsynchronousTask(int id, @NotNull Runnable task, Runnable onComplete) {
        TaskFuture taskFuture = systemResourceManager.submitTask(task);

        scheduleSynchronousTask(id, () -> {
            if (taskFuture.isComplete()) {
                if (onComplete != null) {
                    onComplete.run();
                }
                waitingTasks.remove(id);
            }
        }, 0, 0, true);
    }

    public void cancelTask(int taskId) {
        if (taskId < 0) {
            return;
        }
        waitingTasks.remove(taskId);
    }

    public boolean isTaskInProgress(int taskId) {
        return waitingTasks.containsKey(taskId);
    }

    public Runnable[] getTasksToRunAndUpdate() {
        long currentTime = gameTime.getCurrentTimeMillis();

        ScheduledTask[] tasks = waitingTasks.values().toArray(new ScheduledTask[0]);
        LinkedList<Runnable> result = new LinkedList<>();

        for (ScheduledTask task : tasks) {
            long timeToRun = task.getScheduledTime() + task.getDelayMillis();
            if (currentTime < timeToRun) {
                continue;
            }

            result.push(task.getProgram());

            if (task.isRepeating()) {
                task.setScheduledTime(currentTime);
                task.setDelayMillis(task.getIntervalMillis());
            } else {
                waitingTasks.remove(task.getId());
            }
        }

        return result.toArray(new Runnable[0]);
    }

    /**
     * Generates a task id number
     *
     * @param isSynchronous true if it is a scynchrous task
     * @return generated id number
     */
    private synchronized int generateTaskId(boolean isSynchronous) {

        int value;
        do {
            value = nextTaskId++ * (isSynchronous ? 1 : -1);
            if (nextTaskId < 0) {
                nextTaskId = 1;
            }
        } while (waitingTasks.containsKey(value));

        return value;
    }
}
