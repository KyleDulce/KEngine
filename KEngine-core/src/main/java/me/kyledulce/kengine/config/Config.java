package me.kyledulce.kengine.config;

import ch.qos.logback.classic.Level;
import jakarta.inject.Singleton;
import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.Iterator;

@Singleton
public class Config {

    private final AbstractConfiguration config;

    public Config() {
        CompositeConfiguration config = new CompositeConfiguration();
        this.config = config;

        try {
            config.addConfiguration(new Configurations().xml("config.xml"));
            config.addConfiguration(new Configurations().xml("defaultconfig.xml"));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getAllConfig() {
        StringBuilder result = new StringBuilder();
        for (Iterator<String> it = config.getKeys(); it.hasNext(); ) {
            String key = it.next();

            result.append(key);
            result.append(": ");
            result.append(config.getString(key));
            result.append("\n");
        }
        return result.toString();
    }

    public Level getLoggingLevel() {
        String level = config.getString("logging.level");
        return Level.toLevel(level);
    }

    public boolean getResizable() {
        return config.getBoolean("window.resizable");
    }

    public boolean getMaximized() {
        return config.getBoolean("window.maximized");
    }

    public String getTitle() {
        return config.getString("window.title");
    }

    public int getMaxPoolThreads() {
        try {
            return Integer.parseInt(config.getString("resources.threadPool.maxThreads"));
        } catch (NumberFormatException e) {
            return Runtime.getRuntime().availableProcessors();
        }
    }

    public int getThreadTimeoutSeconds() {
        return config.getInteger("resources.threadPool.threadTimeoutSeconds", 60);
    }
}
