package me.kyledulce.kengine.window.drawing.shader;

import lombok.Getter;
import static org.lwjgl.opengl.GL20.*;

public enum ShaderType {
    VERTEX(GL_VERTEX_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER);

    @Getter
    private final int shaderTypeId;
    ShaderType(int shaderTypeId) {
        this.shaderTypeId = shaderTypeId;
    }
}
