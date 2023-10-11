package me.kyledulce.kengine.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.function.Consumer;

public interface AssetManager {
    /**
     * Loads a resource into the manager
     * @param <T> The type of resource
     * @param resourcePath The location of resource in jar excluding the asset folder
     * @return optional containing the resource if successfully loaded
     */
    <T extends GameAsset> Optional<T> loadResource(Class<T> type, String resourcePath);

    /**
     * Schedules a synchronous task to be executed on next game tick to load resource
     * @param <T> The type of resource
     * @param resourcePath The location of resource in jar excluding the asset folder
     */
    <T extends GameAsset> void loadResourceSync(Class<T> type, String resourcePath);

    /**
     * Schedules a synchronous task to be executed on next game tick to load resource
     * @param <T> The type of resource
     * @param resourcePath The location of resource in jar excluding the asset folder
     * @param onComplete Consumer to be executed once the resource is loaded. Optional will return empty if failed
*                   to load resource.
     */
    <T extends GameAsset> void loadResourceSync(Class<T> type, String resourcePath, Consumer<Optional<T>> onComplete);

    /**
     * Schedules an asynchronous task to load the resource
     * @param <T> The type of resource
     * @param resourcePath The location of resource in jar excluding the asset folder
     * @param onComplete Consumer to be executed once the resource is loaded. Optional will return empty if failed
*                   to load resource.
     */
    <T extends GameAsset> void loadResourceAsync(Class<T> type, String resourcePath, Consumer<Optional<T>> onComplete);

    /**
     * Loads multiple resources of same type into the manager
     * @param <T> The type of resource
     * @param resourcePaths The location of resource in jar excluding the asset folder
     * @return array of optionals containing the resource if successfully loaded
     */
    <T extends GameAsset> LoadedResource<T>[] loadResources(Class<T> type, String[] resourcePaths);

    /**
     * Schedules a synchronous task to be executed on next game tick to load multiple resources of same type into
     * the manager
     * @param <T> The type of resource
     * @param resourcePaths The location of resource in jar excluding the asset folder
     */
    <T extends GameAsset> void loadResourcesSync(Class<T> type, String[] resourcePaths);

    /**
     * Schedules a synchronous task to be executed on next game tick to load multiple resources of same type into
     *      * the manager
     * @param <T> The type of resource
     * @param resourcePaths The location of resource in jar excluding the asset folder
     * @param onComplete Consumer to be executed once the resource is loaded. Optional will return empty if failed
     */
    <T extends GameAsset> void loadResourcesSync(Class<T> type, String[] resourcePaths, Consumer<LoadedResource<T>[]> onComplete);

    /**
     * Schedules an asynchronous task to load the multiple resources of same type into
     * the manager
     * @param resourcePaths The location of resource in jar excluding the asset folder
     * @param onComplete Consumer to be executed once the resource is loaded. Optional will return empty if failed
     */
    void loadResourcesAsync(ResourceRequest[] resourcePaths, Consumer<LoadedResource<GameAsset>[]> onComplete);

    /**
     * Unloads a resource from Resource Manager. If resource is currently being used, there is unpredictable behavior
     * @param resourcePath Resource to unload
     */
    void unloadResource(String resourcePath);

    /**
     * Schedules a synchronous task to be executed on next game tick to unload a resource from Resource Manager.
     * If resource is currently being used, there is unpredictable behavior
     * @param resourcePath The location of resource in jar excluding the asset folder
     */
    void unloadResourceSync(String resourcePath);

    /**
     * Schedules an asynchronous task to unload a resource from Resource Manager.
     * If resource is currently being used, there is unpredictable behavior
     * @param resourcePath The location of resource in jar excluding the asset folder
     */
    void unloadResourceAsync(String resourcePath);

    /**
     * Unloads multiple resources of Resource Manager. If resource is currently being used,
     * there is unpredictable behavior
     * @param resourcePaths The location of resource in jar excluding the asset folder
     */
    void unloadResources(String[] resourcePaths);

    /**
     * Schedules a synchronous task to be executed on next game tick to unload multiple resources from Resource Manager.
     * If resource is currently being used, there is unpredictable behavior
     * @param resourcePaths The location of resource in jar excluding the asset folder
     */
    void unloadResourcesSync(String[] resourcePaths);

    /**
     * Schedules an asynchronous task to unload multiple resources from Resource Manager.
     * If resource is currently being used, there is unpredictable behavior
     * @param resourcePaths The location of resource in jar excluding the asset folder
     */
    void unloadResourcesAsync(String[] resourcePaths);

    /**
     * Schedules asynchronous tasks to load all required resources and unload resources not provided.
     * If a requested resource is already loaded, nothing occurs. If a resource is not provided, it is unloaded
     * @param requiredResourcePaths resources to have in the Resource Manager
     * @param onComplete callback when complete
     */
    void loadAndClearResourcesAsync(ResourceRequest[] requiredResourcePaths, Runnable onComplete);

    /**
     * Gets resource by type. Will return empty optional if resource is not loaded or resource is loaded as a
     * different type
     * @param <T> Type of the resource
     * @param type resource type
     * @param resourcePath The location of resource in jar excluding the asset folder
     * @return Optional containing resource. Empty if resource is not loaded or is a different type
     */
    <T extends GameAsset> Optional<T> getResource(Class<T> type, String resourcePath);

    @AllArgsConstructor
    @Getter
    class LoadedResource<T> {
        private final String resourceId;
        private final Optional<T> resource;
    }
}
