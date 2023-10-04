package me.kyledulce.randomgame;

import io.avaje.inject.BeanScope;
import me.kyledulce.randomgame.game.GameController;

public class RandomGame {
    public static void main(String[] args) {
        BeanScope beanScope = BeanScope.builder().build();

        GameController application = beanScope.get(GameController.class);
        application.run();
    }
}
