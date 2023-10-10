package me.kyledulce.kengine.config;

import io.avaje.inject.Factory;
import jakarta.inject.Inject;

@Factory
public class InjectConfig {

    private final Config config;

    @Inject
    InjectConfig(Config config) {
        this.config = config;
    }
}
