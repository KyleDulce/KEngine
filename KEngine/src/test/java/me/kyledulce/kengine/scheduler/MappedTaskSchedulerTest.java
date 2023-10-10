package me.kyledulce.kengine.scheduler;

import me.kyledulce.kengine.game.GameTime;
import me.kyledulce.kengine.resource.SystemResourceManager;
import me.kyledulce.kengine.resource.TaskFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MappedTaskSchedulerTest {

    @Mock
    GameTime gameTime;

    @Mock
    SystemResourceManager systemResourceManager;

    MappedTaskScheduler mappedTaskScheduler;

    @BeforeEach
    public void beforeEach() {
        mappedTaskScheduler = new MappedTaskScheduler(gameTime, systemResourceManager);
    }

    @Test
    public void testScheduleSynchronousTask_singleParam_success() {
        int actualId = mappedTaskScheduler.scheduleSynchronousTask(() -> {});
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(0, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertFalse(task.isRepeating());
    }

    @Test
    public void testScheduleSynchronousTask_dualParam_success() {
        long expectedDelayMillis = 1000;

        int actualId = mappedTaskScheduler.scheduleSynchronousTask(() -> {}, expectedDelayMillis);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(expectedDelayMillis, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertFalse(task.isRepeating());
    }

    @Test
    public void testScheduleRepeatingSynchronousTask_dualParam_success() {
        long expectedIntervalMillis = 1000;

        int actualId = mappedTaskScheduler.scheduleRepeatingSynchronousTask(() -> {}, expectedIntervalMillis);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(0, task.getDelayMillis());
        assertEquals(expectedIntervalMillis, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertTrue(task.isRepeating());
    }

    @Test
    public void testScheduleRepeatingSynchronousTask_triParam_success() {
        long expectedIntervalMillis = 1000;
        long expectedDelayMillis = 2000;

        int actualId = mappedTaskScheduler.scheduleRepeatingSynchronousTask(() -> {}, expectedIntervalMillis, expectedDelayMillis);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(expectedDelayMillis, task.getDelayMillis());
        assertEquals(expectedIntervalMillis, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertTrue(task.isRepeating());
    }

    @Test
    public void testScheduleAsynchronousTask_triParam_success() {
        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, () -> {});
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(-1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(-1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(0, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertTrue(task.isRepeating());
        verify(systemResourceManager, times(1)).submitTask(any());
    }

    @Test
    public void testScheduleAsynchronousTask_triParamNullOnComplete_success() {
        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, null);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(-1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(-1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(0, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertTrue(task.isRepeating());
        verify(systemResourceManager, times(1)).submitTask(any());
    }

    @Test
    public void testScheduleAsynchronousTask_dualParam_success() {
        long delayMillis = 1000;
        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, () -> {}, delayMillis);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(-1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(-1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(delayMillis, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertFalse(task.isRepeating());
    }

    @Test
    public void testScheduleAsynchronousTask_dualParamNullOnComplete_success() {
        long delayMillis = 1000;
        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, null, delayMillis);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);

        assertEquals(-1, actualId);
        assertEquals(2, mappedTaskScheduler.getNextTaskId());
        assertNotNull(task);
        assertEquals(-1, task.getId());
        assertNotNull(task.getProgram());
        assertEquals(delayMillis, task.getDelayMillis());
        assertEquals(0, task.getIntervalMillis());
        assertEquals(0,task.getScheduledTime());
        assertFalse(task.isRepeating());
    }

    @Test
    public void testScheduleAsynchronousTask_syncTaskRuns_shouldNotCancelTaskWhenNotDone() {
        Runnable mockRunnableComplete = mock(Runnable.class);
        TaskFuture mockTaskFuture = mock(TaskFuture.class);
        when(systemResourceManager.submitTask(any()))
                .thenReturn(mockTaskFuture);
        when(mockTaskFuture.isComplete())
                .thenReturn(false);

        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, mockRunnableComplete);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);
        task.getProgram().run();

        verify(systemResourceManager, times(1)).submitTask(any());
        verify(mockRunnableComplete, never()).run();
        assertEquals(1, mappedTaskScheduler.getWaitingTasks().size());
    }

    @Test
    public void testScheduleAsynchronousTask_syncTaskComplete_shouldRunCompleteAndCancel() {
        Runnable mockRunnableComplete = mock(Runnable.class);
        TaskFuture mockTaskFuture = mock(TaskFuture.class);
        when(systemResourceManager.submitTask(any()))
                .thenReturn(mockTaskFuture);
        when(mockTaskFuture.isComplete())
                .thenReturn(true);

        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, mockRunnableComplete);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);
        task.getProgram().run();

        verify(systemResourceManager, times(1)).submitTask(any());
        verify(mockRunnableComplete, times(1)).run();
        assertEquals(0, mappedTaskScheduler.getWaitingTasks().size());
    }

    @Test
    public void testScheduleAsynchronousTask_syncTaskCompleteNullComplete_ShouldCancel() {
        TaskFuture mockTaskFuture = mock(TaskFuture.class);
        when(systemResourceManager.submitTask(any()))
                .thenReturn(mockTaskFuture);
        when(mockTaskFuture.isComplete())
                .thenReturn(true);

        int actualId = mappedTaskScheduler.scheduleAsynchronousTask(() -> {}, null);
        ScheduledTask task = mappedTaskScheduler.getWaitingTasks().get(actualId);
        task.getProgram().run();

        verify(systemResourceManager, times(1)).submitTask(any());
        assertEquals(0, mappedTaskScheduler.getWaitingTasks().size());
    }

    @Test
    public void testCancelTask_success() {
        int expectedId = 5;
        ScheduledTask expectedTask = new ScheduledTask(expectedId, () -> {}, 0, 0, 0, false);
        mappedTaskScheduler.getWaitingTasks().put(expectedId, expectedTask);

        mappedTaskScheduler.cancelTask(expectedId);

        assertFalse(mappedTaskScheduler.getWaitingTasks().containsKey(expectedId));
    }

    @Test
    public void testCancelTask_asyncTask_doNothing() {
        int expectedId = -5;
        ScheduledTask expectedTask = new ScheduledTask(expectedId, () -> {}, 0, 0, 0, true);
        mappedTaskScheduler.getWaitingTasks().put(expectedId, expectedTask);

        mappedTaskScheduler.cancelTask(expectedId);

        assertTrue(mappedTaskScheduler.getWaitingTasks().containsKey(expectedId));
    }

    @Test
    public void testIsTaskInProgress_success() {
        int expectedId = 5;
        ScheduledTask expectedTask = new ScheduledTask(expectedId, () -> {}, 0, 0, 0, false);
        mappedTaskScheduler.getWaitingTasks().put(expectedId, expectedTask);

        boolean actual = mappedTaskScheduler.isTaskInProgress(expectedId);

        assertTrue(actual);
    }

    @Test
    public void testIsTaskInProgress_noTask_success() {
        boolean actual = mappedTaskScheduler.isTaskInProgress(5);

        assertFalse(actual);
    }

    @Test
    public void testTaskId_shouldResetToZeroOnOverflow() {
        mappedTaskScheduler.setNextTaskId(Integer.MAX_VALUE);
        int actualId = mappedTaskScheduler.scheduleSynchronousTask(() -> {});

        assertEquals(Integer.MAX_VALUE, actualId);
        assertEquals(1, mappedTaskScheduler.getNextTaskId());
    }

    @Test
    public void testGetTasksToRunAndUpdate_success() {
        when(gameTime.getCurrentTimeMillis())
                .thenReturn(0L);

        Runnable[] expected = new Runnable[] {
                () -> System.out.println("Test0"),
                () -> System.out.println("Test1")
        };
        mappedTaskScheduler.getWaitingTasks()
                .put(1, new ScheduledTask(1, expected[0], 0, 0, 0, false));
        mappedTaskScheduler.getWaitingTasks()
                .put(2, new ScheduledTask(2, expected[1], 0, 0, 0, false));

        Runnable[] actual = mappedTaskScheduler.getTasksToRunAndUpdate();

        assertEquals(expected.length, actual.length);
        assertEquals(0, mappedTaskScheduler.getWaitingTasks().size());
    }

    @Test
    public void testGetTasksToRunAndUpdate_notCorrectTime_success() {
        when(gameTime.getCurrentTimeMillis())
                .thenReturn(0L);
        Runnable[] expected = new Runnable[] {
                () -> System.out.println("Test0"),
                () -> System.out.println("Test1")
        };
        mappedTaskScheduler.getWaitingTasks()
                .put(1, new ScheduledTask(1, expected[0], 10, 0, 0, false));
        mappedTaskScheduler.getWaitingTasks()
                .put(2, new ScheduledTask(2, expected[1], 10, 0, 0, false));

        Runnable[] actual = mappedTaskScheduler.getTasksToRunAndUpdate();

        assertEquals(0, actual.length);
        assertEquals(2, mappedTaskScheduler.getWaitingTasks().size());
    }

    @Test
    public void testGetTasksToRunAndUpdate_repeatingTask_success() {
        when(gameTime.getCurrentTimeMillis())
                .thenReturn(0L);

        long expectedNewDelay = 5;
        Runnable[] expected = new Runnable[] {
                () -> System.out.println("Test0"),
                () -> System.out.println("Test1")
        };
        mappedTaskScheduler.getWaitingTasks()
                .put(1, new ScheduledTask(1, expected[0], 0, expectedNewDelay, 0, true));
        mappedTaskScheduler.getWaitingTasks()
                .put(2, new ScheduledTask(2, expected[1], 0, expectedNewDelay, 0, true));

        Runnable[] actual = mappedTaskScheduler.getTasksToRunAndUpdate();

        assertEquals(expected.length, actual.length);
        assertEquals(2, mappedTaskScheduler.getWaitingTasks().size());
        assertEquals(expectedNewDelay, mappedTaskScheduler.getWaitingTasks().get(1).getDelayMillis());
    }
}
