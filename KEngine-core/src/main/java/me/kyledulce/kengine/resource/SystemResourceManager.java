package me.kyledulce.kengine.resource;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.kyledulce.kengine.config.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class that manages pooled system resources like ThreadPools
 */
@Singleton
public class SystemResourceManager {

    ExecutorService executorService;

    @Inject
    public SystemResourceManager(Config config) {
        executorService = new ThreadPoolExecutor(0,
                config.getMaxPoolThreads(),
                config.getThreadTimeoutSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

    public TaskFuture submitTask(Runnable task) {
        return new TaskFuture(
                executorService.submit(task)
        );
    }
}
