package me.kyledulce.kengine.resource;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import me.kyledulce.kengine.scheduler.TaskScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameAssetManagerTest {

    private static final String TEST_FILE_1_ID = "GameAssetTest/testFile.txt";
    private static final String TEST_FILE_2_ID = "GameAssetTest/rand/otherTestFile.txt";
    private static final String TEST_FILE_1_CONTENT = "Some Text";
    private static final String TEST_FILE_2_CONTENT = "Some Other Text";

    private static final String FAKE_TEST_FILE_ID = "fakeFile";
    private static final String FAKE_TEST_FILE_CONTENT = "Content";

    private List<GameAssetFactory> factories;

    @Mock
    private TaskScheduler taskScheduler;

    private GameAssetManager gameAssetManager;

    @BeforeEach
    public void beforeEach() {
        factories = new ArrayList<>();
        factories.add(new TestResourceFactory());
        gameAssetManager = new GameAssetManager(factories, taskScheduler);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResource_success(String resourceId, String expectedContent) {
        Optional<TestResourceType> actual = gameAssetManager.loadResource(TestResourceType.class, resourceId);

        assertTrue(actual.isPresent());
        assertEquals(expectedContent, actual.get().getContent());
        assertFileLoaded(resourceId, expectedContent);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResourceSync_dual_success(String resourceId, String expectedContent) {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);

        gameAssetManager.loadResourceSync(TestResourceType.class, resourceId);
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        assertFileLoaded(resourceId, expectedContent);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResourceSync_tridual_success(String resourceId, String expectedContent) {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        Consumer<Optional<TestResourceType>> onComplete = (resource) -> assertEquals(expectedContent, resource.get().getContent());

        gameAssetManager.loadResourceSync(TestResourceType.class, resourceId, onComplete);
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        assertFileLoaded(resourceId, expectedContent);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResourceAsync_success(String resourceId, String expectedContent) {
        final ArgumentCaptor<Runnable> argumentCaptor0 = ArgumentCaptor.forClass(Runnable.class);
        final ArgumentCaptor<Runnable> argumentCaptor1 = ArgumentCaptor.forClass(Runnable.class);
        Consumer<Optional<TestResourceType>> onComplete = (resource) -> assertEquals(expectedContent, resource.get().getContent());

        gameAssetManager.loadResourceAsync(TestResourceType.class, resourceId, onComplete);
        verify(taskScheduler).scheduleAsynchronousTask(argumentCaptor0.capture(), argumentCaptor1.capture());
        argumentCaptor0.getValue().run();
        argumentCaptor1.getValue().run();

        assertFileLoaded(resourceId, expectedContent);
    }

    @Test
    public void testLoadResource_nullFactory_emptyOptional() {
        Optional<TestResourceOtherType> actual = gameAssetManager.loadResource(TestResourceOtherType.class, FAKE_TEST_FILE_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLoadResource_cached_success() {
        TestResourceType expectedResource = new TestResourceType(FAKE_TEST_FILE_CONTENT);
        gameAssetManager.getLoadedResources().put(FAKE_TEST_FILE_ID, expectedResource);

        Optional<TestResourceType> actual = gameAssetManager.loadResource(TestResourceType.class, FAKE_TEST_FILE_ID);

        assertTrue(actual.isPresent());
        assertEquals(expectedResource, actual.get());
        assertEquals(FAKE_TEST_FILE_CONTENT, actual.get().getContent());
    }

    @Test
    public void testLoadResource_cached_badType_emptyOptional() {
        factories.add(new TestResourceOtherFactory());
        TestResourceType expectedResource = new TestResourceType(FAKE_TEST_FILE_CONTENT);
        gameAssetManager.getLoadedResources().put(FAKE_TEST_FILE_ID, expectedResource);

        Optional<TestResourceOtherType> actual = gameAssetManager.loadResource(TestResourceOtherType.class, FAKE_TEST_FILE_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLoadResource_nullResourceStream_emptyOptional() {
        Optional<TestResourceType> actual = gameAssetManager.loadResource(TestResourceType.class, FAKE_TEST_FILE_ID);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void testLoadResource_failedResourceRead_emptyOptional() {
        TestResourceOtherFactory factory = spy(new TestResourceOtherFactory());
        factories.add(factory);

        Optional<TestResourceOtherType> actual = gameAssetManager.loadResource(TestResourceOtherType.class, TEST_FILE_1_ID);

        assertTrue(actual.isEmpty());
        verify(factory, times(1)).readResource(any());
    }

    @Test
    public void loadResources_success() {
        AssetManager.LoadedResource<TestResourceType>[] actual = gameAssetManager.loadResources(TestResourceType.class, new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        });

        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
        for(AssetManager.LoadedResource<TestResourceType> resource : actual) {
            assertTrue(resource.getResource().isPresent());
        }
    }

    @Test
    public void loadResourcesSync_dual_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);

        gameAssetManager.loadResourcesSync(TestResourceType.class, new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        });
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
    }

    @Test
    public void loadResourcesSync_tridual_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        Consumer<AssetManager.LoadedResource<TestResourceType>[]> onComplete = (resources) -> {
            for(AssetManager.LoadedResource<TestResourceType> resource : resources) {
                assertTrue(resource.getResource().isPresent());
            }
        };

        gameAssetManager.loadResourcesSync(TestResourceType.class, new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        }, onComplete);
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
    }

    @Test
    public void loadResourcesAsync_success() {
        final ArgumentCaptor<Runnable> argumentCaptorAsync0Task = ArgumentCaptor.forClass(Runnable.class);
        final ArgumentCaptor<Runnable> argumentCaptorAsync1Task = ArgumentCaptor.forClass(Runnable.class);
        final ArgumentCaptor<Runnable> argumentCaptorCheckingTask = ArgumentCaptor.forClass(Runnable.class);
        final boolean[] calledComplete = {false};

        Consumer<AssetManager.LoadedResource<GameAsset>[]> onComplete = (resources) -> {
            for(AssetManager.LoadedResource<GameAsset> resource : resources) {
                assertTrue(resource.getResource().isPresent());
            }
            calledComplete[0] = true;
        };

        gameAssetManager.loadResourcesAsync(new ResourceRequest[] {
                new ResourceRequest(TestResourceType.class, TEST_FILE_1_ID),
                new ResourceRequest(TestResourceType.class, TEST_FILE_2_ID),
        }, onComplete);
        verify(taskScheduler, times(2)).scheduleAsynchronousTask(argumentCaptorAsync0Task.capture(), argumentCaptorAsync1Task.capture());
        verify(taskScheduler, times(1)).scheduleRepeatingSynchronousTask(argumentCaptorCheckingTask.capture(), anyLong());

        List<Runnable> loadTasks = argumentCaptorAsync0Task.getAllValues();
        assertEquals(2, loadTasks.size());
        loadTasks.forEach(Runnable::run);

        //Verify oncomplete not run until end
        List<Runnable> incrementTasks = argumentCaptorAsync1Task.getAllValues();
        Runnable checkingTask = argumentCaptorCheckingTask.getValue();
        assertEquals(2, incrementTasks.size());

        checkingTask.run();
        incrementTasks.get(0).run();
        checkingTask.run();
        incrementTasks.get(1).run();
        checkingTask.run();

        verify(taskScheduler, times(1)).cancelTask(anyInt());

        assertTrue(calledComplete[0]);
        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
    }

    @Test
    public void testUnloadResource_success() {
        TestResourceFactory factoryMock = setupUnloadResourceTest();

        gameAssetManager.unloadResource(FAKE_TEST_FILE_ID);

        verify(factoryMock, times(1)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(FAKE_TEST_FILE_ID));
    }

    @Test
    public void testUnloadResourceSync_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        TestResourceFactory factoryMock = setupUnloadResourceTest();

        gameAssetManager.unloadResourceSync(FAKE_TEST_FILE_ID);
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        verify(factoryMock, times(1)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(FAKE_TEST_FILE_ID));
    }

    @Test
    public void testUnloadResourceAsync_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        TestResourceFactory factoryMock = setupUnloadResourceTest();

        gameAssetManager.unloadResourceAsync(FAKE_TEST_FILE_ID);
        verify(taskScheduler).scheduleAsynchronousTask(argumentCaptor.capture(), any());
        argumentCaptor.getValue().run();

        verify(factoryMock, times(1)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(FAKE_TEST_FILE_ID));
    }

    @Test
    public void testUnloadResource_doesNotContainResource_doesNothing() {
        gameAssetManager.unloadResource(FAKE_TEST_FILE_ID);

        // no error
    }

    @Test
    public void testUnloadResource_noFactory_doesNothing() {
        gameAssetManager.getLoadedResources().put(FAKE_TEST_FILE_ID, new TestResourceType(FAKE_TEST_FILE_CONTENT));
        factories.clear();

        gameAssetManager.unloadResource(FAKE_TEST_FILE_ID);

        // no error
        assertFileLoaded(FAKE_TEST_FILE_ID, FAKE_TEST_FILE_CONTENT);
    }

    @Test
    public void testUnloadResources_success() {
        TestResourceFactory factory = setupUnloadResourceMultipleTest();

        gameAssetManager.unloadResources(new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        });

        verify(factory, times(2)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_1_ID));
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_2_ID));
    }

    @Test
    public void testUnloadResourcesSync_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        TestResourceFactory factory = setupUnloadResourceMultipleTest();

        gameAssetManager.unloadResourcesSync(new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        });
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        verify(factory, times(2)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_1_ID));
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_2_ID));
    }

    @Test
    public void testUnloadResourcesAsync_success() {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        TestResourceFactory factory = setupUnloadResourceMultipleTest();

        gameAssetManager.unloadResourcesAsync(new String[] {
                TEST_FILE_1_ID, TEST_FILE_2_ID
        });
        verify(taskScheduler,atLeastOnce()).scheduleAsynchronousTask(argumentCaptor.capture(), any());
        argumentCaptor.getAllValues().forEach(Runnable::run);

        verify(factory, times(2)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_1_ID));
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_2_ID));
    }

    @Test
    public void testLoadAndClearResourcesAsync_loadAll_loads() {
        final boolean[] calledComplete = {false};

        Runnable onComplete = () -> calledComplete[0] = true;

        gameAssetManager.loadAndClearResourcesAsync(new ResourceRequest[] {
                new ResourceRequest(TestResourceType.class, TEST_FILE_1_ID),
                new ResourceRequest(TestResourceType.class, TEST_FILE_2_ID),
        }, onComplete);

        runAllTasksInLoadAndClearResourcesAsync();

        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
        assertTrue(calledComplete[0]);
    }

    @Test
    public void testLoadAndClearResourcesAsync_unloadAll_unloads() {
        final boolean[] calledComplete = {false};
        gameAssetManager.getLoadedResources().put(TEST_FILE_1_ID, new TestResourceType(TEST_FILE_1_CONTENT));
        gameAssetManager.getLoadedResources().put(TEST_FILE_2_ID, new TestResourceType(TEST_FILE_2_CONTENT));

        Runnable onComplete = () -> calledComplete[0] = true;

        gameAssetManager.loadAndClearResourcesAsync(new ResourceRequest[] {}, onComplete);

        runAllTasksInLoadAndClearResourcesAsync();

        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_1_ID));
        assertFalse(gameAssetManager.getLoadedResources().containsKey(TEST_FILE_2_ID));
        assertTrue(calledComplete[0]);
    }

    @Test
    public void testLoadAndClearResourcesAsync_someLoadSomeUnloadSomeKeep_success() {
        factories.clear();
        TestResourceFactory factory = spy(new TestResourceFactory());
        factories.add(factory);
        final boolean[] calledComplete = {false};
        // To unload
        gameAssetManager.getLoadedResources().put(FAKE_TEST_FILE_ID, new TestResourceType(FAKE_TEST_FILE_CONTENT));
        // To Keep
        gameAssetManager.getLoadedResources().put(TEST_FILE_2_ID, new TestResourceType(TEST_FILE_2_CONTENT));
        // To load File 1

        Runnable onComplete = () -> calledComplete[0] = true;

        gameAssetManager.loadAndClearResourcesAsync(new ResourceRequest[] {
                new ResourceRequest(TestResourceType.class, TEST_FILE_1_ID),
                new ResourceRequest(TestResourceType.class, TEST_FILE_2_ID),
        }, onComplete);

        runAllTasksInLoadAndClearResourcesAsync();

        verify(factory, times(1)).readResource(any());
        verify(factory, times(1)).unloadResource(any());
        assertFalse(gameAssetManager.getLoadedResources().containsKey(FAKE_TEST_FILE_ID));
        assertFileLoaded(TEST_FILE_1_ID, TEST_FILE_1_CONTENT);
        assertFileLoaded(TEST_FILE_2_ID, TEST_FILE_2_CONTENT);
        assertTrue(calledComplete[0]);
    }

    private void assertFileLoaded(String resourceId, String expectedContent) {
        GameAsset resource = gameAssetManager.getLoadedResources().get(resourceId);

        assertInstanceOf(TestResourceType.class, resource);
        assertEquals(expectedContent, ((TestResourceType) resource).getContent());
    }

    private TestResourceFactory setupUnloadResourceTest() {
        gameAssetManager.getLoadedResources().put(FAKE_TEST_FILE_ID, new TestResourceType(FAKE_TEST_FILE_CONTENT));
        factories.clear();
        TestResourceFactory factory = spy(new TestResourceFactory());
        factories.add(factory);
        return factory;
    }

    private TestResourceFactory setupUnloadResourceMultipleTest() {
        gameAssetManager.getLoadedResources().put(TEST_FILE_1_ID, new TestResourceType(TEST_FILE_1_CONTENT));
        gameAssetManager.getLoadedResources().put(TEST_FILE_2_ID, new TestResourceType(TEST_FILE_2_CONTENT));
        factories.clear();
        TestResourceFactory factory = spy(new TestResourceFactory());
        factories.add(factory);
        return factory;
    }

    private void runAllTasksInLoadAndClearResourcesAsync() {
        final ArgumentCaptor<Runnable> argumentCaptorAsync0 = ArgumentCaptor.forClass(Runnable.class);
        final ArgumentCaptor<Runnable> argumentCaptorAsync1 = ArgumentCaptor.forClass(Runnable.class);

        verify(taskScheduler, atLeastOnce()).scheduleAsynchronousTask(argumentCaptorAsync0.capture(), argumentCaptorAsync1.capture());
        argumentCaptorAsync0.getAllValues().forEach(Runnable::run);

        verify(taskScheduler, atLeast(0)).scheduleAsynchronousTask(argumentCaptorAsync0.capture(), argumentCaptorAsync1.capture());
        argumentCaptorAsync0.getAllValues().forEach(Runnable::run);
        argumentCaptorAsync1.getAllValues().forEach((runnable) -> {
            if(runnable != null) {
                runnable.run();
            }
        });

        final ArgumentCaptor<Runnable> argumentCaptorSyncRepeating = ArgumentCaptor.forClass(Runnable.class);
        verify(taskScheduler, atLeastOnce()).scheduleRepeatingSynchronousTask(argumentCaptorSyncRepeating.capture(), anyLong());
        argumentCaptorSyncRepeating.getAllValues().forEach(Runnable::run);
    }

    private static Stream<Arguments> provideResourceParams() {
        return Stream.of(
                Arguments.of(TEST_FILE_1_ID, TEST_FILE_1_CONTENT),
                Arguments.of(TEST_FILE_2_ID, TEST_FILE_2_CONTENT)
        );
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    private class TestResourceType implements GameAsset {
        private String content;
    }

    private class TestResourceOtherType implements GameAsset {
        private String content;
    }

    private class TestResourceFactory implements GameAssetFactory<TestResourceType> {

        @Override
        public Class<TestResourceType> getResourceType() {
            return TestResourceType.class;
        }

        @SneakyThrows
        @Override
        public Optional<TestResourceType> readResource(InputStream inputStream) {
            String data = new String(inputStream.readAllBytes());

            return Optional.of(new TestResourceType(data.toString()));
        }

        @Override
        public void unloadResource(GameAsset resource) {

        }

        @Override
        public boolean isResourceInstanceOfType(GameAsset resource) {
            return resource instanceof TestResourceType;
        }
    }

    private class TestResourceOtherFactory implements GameAssetFactory<TestResourceOtherType> {

        @Override
        public Class<TestResourceOtherType> getResourceType() {
            return TestResourceOtherType.class;
        }

        @SneakyThrows
        @Override
        public Optional<TestResourceOtherType> readResource(InputStream inputStream) {
            return Optional.empty();
        }

        @Override
        public void unloadResource(GameAsset resource) {

        }

        @Override
        public boolean isResourceInstanceOfType(GameAsset resource) {
            return resource instanceof TestResourceOtherType;
        }
    }
}
