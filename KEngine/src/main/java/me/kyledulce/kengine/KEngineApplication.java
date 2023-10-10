package me.kyledulce.kengine;

import io.avaje.inject.BeanScope;
import me.kyledulce.kengine.game.GameController;

public class KEngineApplication {

    public static void run() {
        BeanScope beanScope = BeanScope.builder().build();

        GameController application = beanScope.get(GameController.class);
        application.run();
    }
}
