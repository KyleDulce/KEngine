package me.kyledulce.kengine.window.drawing.texture;

import me.kyledulce.kengine.annotations.AssetFactory;
import me.kyledulce.kengine.resource.GameAsset;
import me.kyledulce.kengine.resource.GameAssetFactory;

import java.io.InputStream;
import java.util.Optional;

@AssetFactory
public class TextureConfigHandler implements GameAssetFactory<TextureConfigAsset> {
    @Override
    public Class<TextureConfigAsset> getResourceType() {
        return null;
    }

    @Override
    public Optional<TextureConfigAsset> readResource(InputStream inputStream) {
        return Optional.empty();
    }

    @Override
    public void unloadResource(GameAsset resource) {

    }

    @Override
    public boolean isResourceInstanceOfType(GameAsset resource) {
        return false;
    }
}
