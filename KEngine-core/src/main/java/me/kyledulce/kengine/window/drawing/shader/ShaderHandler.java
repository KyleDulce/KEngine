package me.kyledulce.kengine.window.drawing.shader;

import me.kyledulce.kengine.annotations.AssetFactory;
import me.kyledulce.kengine.resource.GameAsset;
import me.kyledulce.kengine.resource.GameAssetFactory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@AssetFactory
public class ShaderHandler implements GameAssetFactory<ShaderAsset> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShaderHandler.class);

    @Override
    public Class<ShaderAsset> getResourceType() {
        return ShaderAsset.class;
    }

    @Override
    public boolean isResourceInstanceOfType(GameAsset resource) {
        return resource instanceof ShaderAsset;
    }

    @Override
    public Optional<ShaderAsset> readResource(InputStream inputStream) {
        try {
            String shaderDefinition = new String(inputStream.readAllBytes());
            return Optional.of(new ShaderAsset(shaderDefinition));
        } catch (IOException e) {
            LOGGER.error("Failed to load shader definition", e);
            return Optional.empty();
        }
    }

    @Override
    public void unloadResource(GameAsset resource) {
        if(!(resource instanceof ShaderAsset shaderAsset)) {
            return;
        }

        deleteShaders(shaderAsset);
    }

    public void generateShaders(ShaderAsset shaderAsset, ShaderType type) {
        if(shaderAsset.getShaderId() != 0) {
            return;
        }

        int shaderId = GL20.glCreateShader(type.getShaderTypeId());
        shaderAsset.setShaderId(shaderId);
        shaderAsset.setShaderType(type);
        GL20.glShaderSource(shaderId, shaderAsset.getShaderDefinition());
        GL20.glCompileShader(shaderId);

        if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE) {
            LOGGER.error("Failed to compile shaders: {}\n\n{}",
                    shaderAsset.getShaderDefinition(),
                    GL20.glGetShaderInfoLog(shaderId));
        }
    }

    public void deleteShaders(ShaderAsset shaderAsset) {
        if(shaderAsset.getShaderId() != 0) {
            GL20.glDeleteShader(shaderAsset.getShaderId());
        }
    }
}
