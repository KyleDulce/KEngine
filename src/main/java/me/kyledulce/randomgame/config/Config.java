package me.kyledulce.randomgame.config;

import ch.qos.logback.classic.Level;
import jakarta.inject.Singleton;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.util.Iterator;

@Singleton
public class Config {

    private XMLConfiguration config;

    public Config() {
        Configurations configs = new Configurations();

        try {
            this.config = configs.xml("config.xml");
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
}
