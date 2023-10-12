package me.kyledulce.kengine.window.drawing.shader;

import jakarta.inject.Singleton;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;
import java.util.Optional;

@Singleton
public class ShaderProgramHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderProgramHandler.class);
    private static final String[] SHADER_ATTRIBUTES = new String[] {
            "vertices", "textures"
    };

    public Optional<ShaderProgram> createProgram(ShaderAsset[] shadersToInclude) {
        int[] shaders = new int[shadersToInclude.length];
        // validate shaders
        for(int x = 0; x < shadersToInclude.length; x++) {
            ShaderAsset shaderAsset = shadersToInclude[x];
            if(shaderAsset.getShaderId() == 0) {
                LOGGER.error("Provided shader is not loaded! cannot create program");
                return Optional.empty();
            }
            shaders[x] = shaderAsset.getShaderId();
        }

        int programId = GL20.glCreateProgram();
        ShaderProgram program = new ShaderProgram(shaders);
        program.setProgramId(programId);

        for(ShaderAsset shaderAsset : shadersToInclude) {
            GL20.glAttachShader(programId, shaderAsset.getShaderId());
        }

        for(int x = 0; x < SHADER_ATTRIBUTES.length; x++) {
            GL20.glBindAttribLocation(programId, x, SHADER_ATTRIBUTES[x]);
        }

        GL20.glLinkProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            LOGGER.error("Failed to link shader program\n\n{}", GL20.glGetProgramInfoLog(programId));
            deleteProgram(program);
            return Optional.empty();
        }

        GL20.glValidateProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            LOGGER.error("Failed to validate shader program\n\n{}", GL20.glGetProgramInfoLog(programId));
            deleteProgram(program);
            return Optional.empty();
        }

        return Optional.of(program);
    }

    public void deleteProgram(ShaderProgram shaderProgram) {
        GL20.glDeleteProgram(shaderProgram.getProgramId());
        shaderProgram.setProgramId(0);
    }

    public void setUniform(ShaderProgram shaderProgram, String name, int value) {
        int uniformLocation = GL20.glGetUniformLocation(shaderProgram.getProgramId(), name);
        if(uniformLocation != -1) {
            GL20.glUniform1i(uniformLocation, value);
        }
    }

    public void setUniform(ShaderProgram shaderProgram, String name, Matrix4f value) {
        int uniformLocation = GL20.glGetUniformLocation(shaderProgram.getProgramId(), name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if(uniformLocation != -1) {
            GL20.glUniformMatrix4fv(uniformLocation, false, buffer);
        }
    }

    public void bindProgram(ShaderProgram shaderProgram) {
        if(shaderProgram.getProgramId() != 0) {
            GL20.glUseProgram(shaderProgram.getProgramId());
        }
    }

    public void clearBind() {
        GL20.glUseProgram(0);
    }
}
