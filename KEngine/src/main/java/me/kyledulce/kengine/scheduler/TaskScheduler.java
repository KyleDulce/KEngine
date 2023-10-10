package me.kyledulce.kengine.scheduler;

import org.jetbrains.annotations.NotNull;

/**
 * Class that Schedules Tasks to the Game loop
 * Tasks are given id numbers on creation. If the task is negative, then the task is asynchronous,
 * otherwise it is a synchronous task
 */
public interface TaskScheduler {
    /**
     * Schedules Synchronous task
     * @param task task to run
     * @return the Id of the scheduled task
     */
    int scheduleSynchronousTask(@NotNull Runnable task);

    /**
     * Schedules Synchronous task
     * @param task task to run
     * @param delayMillis time in milliseconds to delay the task execution
     * @return the Id of the scheduled task
     */
    int scheduleSynchronousTask(@NotNull Runnable task, long delayMillis);

    /**
     * Schedules repeating Synchronous task
     * @param task task to run
     * @param intervalMillis time in milliseconds to between execution of the task
     * @return the Id of the scheduled task
     */
    int scheduleRepeatingSynchronousTask(@NotNull Runnable task, long intervalMillis);

    /**
     * Schedules repeating Synchronous task
     * @param task task to run
     * @param intervalMillis time in milliseconds to between execution of the task
     * @param delayMillis time in milliseconds to delay the initial task execution
     * @return the Id of the scheduled task
     */
    int scheduleRepeatingSynchronousTask(@NotNull Runnable task, long intervalMillis, long delayMillis);

    /**
     * Schedules Asynchronous task
     * @param task task to run. must not be null
     * @param onComplete task to run on completion
     * @return the id of the generated task.
     */
    int scheduleAsynchronousTask(@NotNull Runnable task, Runnable onComplete);

    /**
     * Schedules Asynchronous task
     * @param task task to run. must not be null
     * @param onComplete task to run on completion
     * @param delayMillis time in milliseconds to delay the initial task execution
     * @return the id of the generated task.
     */
    int scheduleAsynchronousTask(@NotNull Runnable task, Runnable onComplete, long delayMillis);

    /**
     * Cancels provided task. If task does not exist, it does nothing. Asynchronous tasks cannot be canceled
     * @param taskId the task id to cancel.
     */
    void cancelTask(int taskId);

    /**
     * Returns true if task is currently waiting or executing. If task does not exist, false is returned
     * @param taskId the task id to check
     * @return true if task is waiting or executing. False otherwise
     */
    boolean isTaskInProgress(int taskId);
}
