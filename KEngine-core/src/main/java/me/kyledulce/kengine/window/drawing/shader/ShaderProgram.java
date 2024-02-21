package me.kyledulce.kengine.window.drawing.shader;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor
public class ShaderProgram {
    private final int[] shaders;

    @Setter(AccessLevel.PACKAGE)
    private int programId = 0;

    private final HashMap<String, Integer> attributeToIndex = new HashMap<>();
}
