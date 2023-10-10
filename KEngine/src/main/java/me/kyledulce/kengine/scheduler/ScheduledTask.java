package me.kyledulce.kengine.scheduler;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ScheduledTask {
    private int id;
    @NotNull private Runnable program;
    @Setter private long delayMillis;
    private long intervalMillis;
    @Setter private long scheduledTime;
    private boolean repeating;
}
