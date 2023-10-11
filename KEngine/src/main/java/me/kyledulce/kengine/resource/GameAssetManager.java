package me.kyledulce.kengine.resource;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import me.kyledulce.kengine.scheduler.TaskScheduler;
import me.kyledulce.kengine.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Resource Manager for managing Assets
 */
@Singleton
public class GameAssetManager implements AssetManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameAssetManager.class);

    @Getter(AccessLevel.PACKAGE)
    private final Map<String, GameAsset> loadedResources;
    private final List<GameAssetFactory> resourceFactories;
    private final TaskScheduler taskScheduler;

    @Inject
    public GameAssetManager(List<GameAssetFactory> factories, TaskScheduler taskScheduler) {
        loadedResources = Collections.synchronizedMap(new HashMap<>());
        resourceFactories = factories;
        this.taskScheduler = taskScheduler;
    }

    /**
     * Gets Resource factory from type
     * @param type Type to find a factory for
     * @param <T> The type
     * @return Resource factory for resource type. null if not found
     */
    @SuppressWarnings("unchecked cast")
    private <T extends GameAsset> GameAssetFactory<T> getResourceFactory(Class<T> type) {
        for(GameAssetFactory<?> factory : resourceFactories) {
            if(factory.getResourceType() == type) {
                return (GameAssetFactory<T>) factory;
            }
        }
        LOGGER.error("Factory for type {} not found", type);
        return null;
    }

    /**
     * Loads a resource given a resource factory
     * @param resourcePath The location of resource in jar excluding the asset folder
     * @param gameAssetFactory the resource factory
     * @param <T> the type of the resource
     * @return optional containing resource that was loaded
     */
    @SuppressWarnings("unchecked cast")
    private <T extends GameAsset> Optional<T> loadResource(String resourcePath, GameAssetFactory<T> gameAssetFactory) {
        if(gameAssetFactory == null) {
            LOGGER.error("Factory not found");
            return Optional.empty();
        }

        if(loadedResources.containsKey(resourcePath)) {
            GameAsset resource = loadedResources.get(resourcePath);
            if(gameAssetFactory.isResourceInstanceOfType(resource)) {
                LOGGER.warn("Attempted to load resource that is already loaded");
                return Optional.of((T) resource);
            } else {
                LOGGER.error("Attempted to load already loaded resource as a different type");
                return Optional.empty();
            }
        }

        String normalizedResourcePath = PathUtils.normalizeResourcePath(resourcePath);
        normalizedResourcePath = "/assets/".concat(normalizedResourcePath);

        InputStream resourceInputStream = getClass().getResourceAsStream(normalizedResourcePath);

        if(resourceInputStream == null) {
            LOGGER.error("Cannot find resource {} at {}", resourcePath, normalizedResourcePath);
            return Optional.empty();
        }

        LOGGER.debug("Loading resource: '{}'", resourcePath);
        Optional<T> resource = gameAssetFactory.readResource(resourceInputStream);

        if(resource.isEmpty()) {
            LOGGER.error("Failed to load resource {} at {}", resourcePath, normalizedResourcePath);
            return Optional.empty();
        }

        loadedResources.put(resourcePath, resource.get());
        return resource;
    }

    @Override
    public <T extends GameAsset> Optional<T> loadResource(Class<T> type, String resourcePath) {
        return loadResource(resourcePath, getResourceFactory(type));
    }

    @Override
    public <T extends GameAsset> void loadResourceSync(Class<T> type, String resourcePath) {
        taskScheduler.scheduleSynchronousTask(
                () -> loadResource(type, resourcePath)
        );
    }

    @Override
    public <T extends GameAsset> void loadResourceSync(Class<T> type, String resourcePath, Consumer<Optional<T>> onComplete) {
        taskScheduler.scheduleSynchronousTask(
                () -> onComplete.accept(
                    loadResource(type, resourcePath))
        );
    }

    @Override
    public <T extends GameAsset> void loadResourceAsync(Class<T> type, String resourcePath, Consumer<Optional<T>> onComplete) {
        taskScheduler.scheduleAsynchronousTask(
                () -> loadResource(type, resourcePath),
                () -> onComplete.accept(getResource(type, resourcePath))
        );
    }

    @Override
    public <T extends GameAsset> LoadedResource<T>[] loadResources(Class<T> type, String[] resourcePaths) {
        GameAssetFactory<T> factory = getResourceFactory(type);
        LoadedResource<T>[] result = new LoadedResource[resourcePaths.length];

        for(int x = 0; x < resourcePaths.length; x++) {
            Optional<T> loadedResource = loadResource(resourcePaths[x], factory);
            result[x] = new LoadedResource<>(resourcePaths[x], loadedResource);
        }

        return result;
    }

    @Override
    public <T extends GameAsset> void loadResourcesSync(Class<T> type, String[] resourcePaths) {
        taskScheduler.scheduleSynchronousTask(
                () -> loadResources(type, resourcePaths)
        );
    }

    @Override
    public <T extends GameAsset> void loadResourcesSync(Class<T> type, String[] resourcePaths, Consumer<LoadedResource<T>[]> onComplete) {
        taskScheduler.scheduleSynchronousTask(
                () -> onComplete.accept(loadResources(type, resourcePaths))
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadResourcesAsync(ResourceRequest[] resourcePaths, Consumer<LoadedResource<GameAsset>[]> onComplete) {
        AtomicInteger value = new AtomicInteger();
        value.set(resourcePaths.length);

        LoadedResource<GameAsset>[] result = new LoadedResource[resourcePaths.length];
        for(int x = 0; x < resourcePaths.length; x++) {
            int finalX = x;
            taskScheduler.scheduleAsynchronousTask(
                    () -> {
                        GameAssetFactory factory = getResourceFactory(resourcePaths[finalX].resourceType());
                        Optional<? extends GameAsset> loadedResource = loadResource(resourcePaths[finalX].resourceId(), factory);
                        result[finalX] = new LoadedResource(resourcePaths[finalX].resourceId(), loadedResource);
                    },
                    value::getAndDecrement
            );
        }

        final AtomicInteger taskId = new AtomicInteger();
        taskId.set(taskScheduler.scheduleRepeatingSynchronousTask(
                () -> {
                    if(value.get() <= 0) {
                        taskScheduler.cancelTask(taskId.get());
                        onComplete.accept(result);
                    }
                },
        0));
    }

    @Override
    public void unloadResource(String resourcePath) {
        if (!loadedResources.containsKey(resourcePath)) {
            return;
        }

        GameAsset resource = loadedResources.get(resourcePath);
        GameAssetFactory<?> factory = getResourceFactory(resource.getClass());

        if(factory == null) {
            LOGGER.error("Failed to unload resource due to resource factory missing");
            return;
        }

        LOGGER.debug("Unloading resource '{}'", resourcePath);
        factory.unloadResource(resource);
        loadedResources.remove(resourcePath);
    }

    @Override
    public void unloadResourceSync(String resourcePath) {
        taskScheduler.scheduleSynchronousTask(() -> unloadResource(resourcePath));
    }

    @Override
    public void unloadResourceAsync(String resourcePath) {
        taskScheduler.scheduleAsynchronousTask(() -> unloadResource(resourcePath), null);
    }

    @Override
    public void unloadResources(String[] resourcePaths) {
        for(String resource : resourcePaths) {
            unloadResource(resource);
        }
    }

    @Override
    public void unloadResourcesSync(String[] resourcePaths) {
        taskScheduler.scheduleSynchronousTask(() -> unloadResources(resourcePaths));
    }

    @Override
    public void unloadResourcesAsync(String[] resourcePaths) {
        for(String resource : resourcePaths) {
            taskScheduler.scheduleAsynchronousTask(() -> unloadResource(resource), null);
        }
    }

    @Override
    public void loadAndClearResourcesAsync(ResourceRequest[] requiredResourcePaths, Runnable onComplete) {
        taskScheduler.scheduleAsynchronousTask(() -> loadAndClearResourcesTask(requiredResourcePaths, onComplete), null);
    }

    /**
     * Task for loadAndClearResourcesAsync
     * @param requiredResourcePaths param from previous method
     * @param onComplete param from previous method
     */
    private void loadAndClearResourcesTask(ResourceRequest[] requiredResourcePaths, Runnable onComplete) {
        Set<String> resourcesToDelete = new HashSet<>(loadedResources.keySet());
        Set<ResourceRequest> resourcesToLoad = new HashSet<>();

        for(ResourceRequest resourceRequest : requiredResourcePaths) {
            // If resource is found, then do nothing, remove from delete list
            // If resource is not found, it must be loaded
            // If resource is never looked for, it remains in set and gets deleted
            if(resourcesToDelete.contains(resourceRequest.resourceId()) && !resourcesToLoad.contains(resourceRequest.resourceId())) {
                resourcesToDelete.remove(resourceRequest.resourceId());
            } else {
                resourcesToLoad.add(resourceRequest);
            }
        }

        unloadResourcesAsync(resourcesToDelete.toArray(new String[0]));
        loadResourcesAsync(resourcesToLoad.toArray(new ResourceRequest[0]), (param) -> onComplete.run());
    }

    @Override
    public <T extends GameAsset> Optional<T> getResource(Class<T> type, String resourcePath) {
        if(loadedResources.containsKey(resourcePath)) {
            GameAssetFactory<T> factory = getResourceFactory(type);
            GameAsset resource = loadedResources.get(resourcePath);
            if(factory != null && factory.isResourceInstanceOfType(resource)) {
                return Optional.of((T) resource);
            }
        }
        return Optional.empty();
    }
}
