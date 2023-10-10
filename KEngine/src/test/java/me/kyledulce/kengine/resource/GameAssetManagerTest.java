package me.kyledulce.kengine.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import me.kyledulce.kengine.scheduler.TaskScheduler;
import org.junit.jupiter.api.BeforeEach;
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

    private List<GameAssetFactory> factories;

    @Mock
    private TaskScheduler taskScheduler;

    private GameAssetManager gameAssetManager;

    @BeforeEach
    public void beforeEach() {
        factories = new ArrayList<>();
        gameAssetManager = new GameAssetManager(factories, taskScheduler);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResource_success(String resourceId, String expectedContent) {
        factories.add(new TestResourceFactory());
        Optional<TestResourceType> actual = gameAssetManager.loadResource(TestResourceType.class, resourceId);

        assertTrue(actual.isPresent());
        assertEquals(expectedContent, actual.get().getContent());
        assertFileLoaded(resourceId, expectedContent);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResourceSync_dual_success(String resourceId, String expectedContent) {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        factories.add(new TestResourceFactory());

        gameAssetManager.loadResourceSync(TestResourceType.class, resourceId);
        verify(taskScheduler).scheduleSynchronousTask(argumentCaptor.capture());
        argumentCaptor.getValue().run();

        assertFileLoaded(resourceId, expectedContent);
    }

    @ParameterizedTest
    @MethodSource("provideResourceParams")
    public void testLoadResourceSync_tridual_success(String resourceId, String expectedContent) {
        final ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
        factories.add(new TestResourceFactory());
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
        factories.add(new TestResourceFactory());
        Consumer<Optional<TestResourceType>> onComplete = (resource) -> assertEquals(expectedContent, resource.get().getContent());

        gameAssetManager.loadResourceAsync(TestResourceType.class, resourceId, onComplete);
        verify(taskScheduler).scheduleAsynchronousTask(argumentCaptor0.capture(), argumentCaptor1.capture());
        argumentCaptor0.getValue().run();
        argumentCaptor1.getValue().run();

        assertFileLoaded(resourceId, expectedContent);
    }

    private void assertFileLoaded(String resourceId, String expectedContent) {
        GameAsset resource = gameAssetManager.getLoadedResources().get(resourceId);

        assertInstanceOf(TestResourceType.class, resource);
        assertEquals(expectedContent, ((TestResourceType) resource).getContent());
    }

    private static Stream<Arguments> provideResourceParams() {
        return Stream.of(
                Arguments.of(TEST_FILE_1_ID, TEST_FILE_1_CONTENT),
                Arguments.of(TEST_FILE_2_ID, TEST_FILE_2_CONTENT)
        );
    }

    @Getter
    @AllArgsConstructor
    private class TestResourceType implements GameAsset {
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
}
