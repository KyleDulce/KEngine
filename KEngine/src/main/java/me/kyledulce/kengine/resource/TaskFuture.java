package me.kyledulce.kengine.resource;

import lombok.AllArgsConstructor;

import java.util.concurrent.Future;

@AllArgsConstructor
public class TaskFuture {
    private Future<?> taskFuture;

    public boolean isComplete() {
        return taskFuture.isDone();
    }
}
