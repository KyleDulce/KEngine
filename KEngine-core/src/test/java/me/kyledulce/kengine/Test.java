package me.kyledulce.kengine;

import java.io.File;
import java.net.URL;

public class Test {
    @org.junit.jupiter.api.Test
    public void test() {
        URL t = Test.class.getClassLoader().getResource("assets/GameAssetTest/testFile");
        System.out.println(t);

        File f = new File(String.valueOf(t));
        System.out.println(f.getName());
    }
}
