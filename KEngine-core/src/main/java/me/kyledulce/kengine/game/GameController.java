package me.kyledulce.kengine.game;

import ch.qos.logback.classic.Level;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.kyledulce.kengine.config.Config;
import me.kyledulce.kengine.window.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GameController implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final Config config;
    private final Window window;
    private final GameTime gameTime;

    @Inject
    public GameController(Config config, Window window, GameTime gameTime) {
        this.config = config;
        this.window = window;
        this.gameTime = gameTime;
    }

    public void run() {
        start();
        gameLoop();
        shutdown();
    }

    private void start() {
        // Set Logging level
        Level logLevel = config.getLoggingLevel();
        ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(logLevel);

        LOGGER.info("Logging Level is {}", logLevel.toString());
        LOGGER.info("Config: \n{}", config.getAllConfig());

        gameTime.setStartTime();

        // Setup window
        window.initializeWindow();
    }

    private void gameLoop() {
        while (true) {
            System.out.println("test");
        }
    }

    private void shutdown() {
        // Shutdown window
        window.shutdownWindow();
    }
}
