package me.kyledulce.kengine.window.drawing.shader;

import lombok.Getter;
import static org.lwjgl.opengl.GL20.*;

public enum ShaderType {
    VERTEX(GL_VERTEX_SHADER, "vertices"),
    FRAGMENT(GL_FRAGMENT_SHADER, "textures");

    @Getter
    private final int shaderTypeId;
    @Getter
    private final String attribLocationName;
    ShaderType(int shaderTypeId, String attribLocationName) {
        this.shaderTypeId = shaderTypeId;
        this.attribLocationName = attribLocationName;
    }
}
