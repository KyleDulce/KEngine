package me.kyledulce.kengine.resource;

import java.io.InputStream;
import java.util.Optional;

public interface GameAssetFactory<T extends GameAsset> {
    Class<T> getResourceType();
    Optional<T> readResource(InputStream inputStream);
    void unloadResource(GameAsset resource);
    boolean isResourceInstanceOfType(GameAsset resource);
}
