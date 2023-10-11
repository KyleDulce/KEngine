package me.kyledulce.kengine.window.drawing.texture;

import me.kyledulce.kengine.annotations.AssetFactory;
import me.kyledulce.kengine.resource.GameAsset;
import me.kyledulce.kengine.resource.GameAssetFactory;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

@AssetFactory
public class TextureHandler implements GameAssetFactory<TextureAsset> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextureHandler.class);

    @Override
    public Class<TextureAsset> getResourceType() {
        return TextureAsset.class;
    }

    @Override
    public boolean isResourceInstanceOfType(GameAsset resource) {
        return resource instanceof TextureAsset;
    }

    @Override
    public Optional<TextureAsset> readResource(InputStream inputStream) {
        try {
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            int imageArea = height * width;

            int[] pixelsRaw = new int[imageArea];

            pixelsRaw = bufferedImage.getRGB(0,0, width, height, pixelsRaw, 0, width);

            ByteBuffer pixelsProcessed = BufferUtils.createByteBuffer(imageArea);

            for(int pixel : pixelsRaw) {
                // Extract raw color data
                // Bitshift to color bits, then reduce to a single byte, then cast to byte
                pixelsProcessed.put((byte) ((pixel >> 16) & 0xFF)); // Red
                pixelsProcessed.put((byte) ((pixel >> 8) & 0xFF));  // Green
                pixelsProcessed.put((byte) (pixel & 0xFF));         // Blue
                pixelsProcessed.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }

            pixelsProcessed.flip();
            return Optional.of(new TextureAsset(pixelsProcessed, height, width));
        } catch (IOException e) {
            LOGGER.error("Failed to read texture file", e);
            return Optional.empty();
        }
    }

    @Override
    public void unloadResource(GameAsset resource) {
        if(!(resource instanceof TextureAsset textureAsset)) {
            return;
        }

        deleteTextures(textureAsset);
        textureAsset.setUnloaded(true);
    }

    /**
     * Generates and loads textures into video memory
     * @param texture texture to generate
     */
    public void generateTextures(TextureAsset texture) {
        if(texture.getId() != 0 || texture.isUnloaded()) {
            return;
        }

        texture.setId(GL11.glGenTextures());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture.getImgBuffer());
    }

    /**
     * Removes textures from video memory
     * @param texture texture to remove
     */
    public void deleteTextures(TextureAsset texture) {
        if(texture.getId() != 0) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL11.glDeleteTextures(texture.getId());
            texture.setId(0);
        }
    }
}
