package me.kyledulce.kengine.resource;

/**
 * An object with the ability to specify what game assets should be loaded
 * for the use of this object
 */
public interface AssetLoadingClient {
    String[] requestRequiredResources();
}
