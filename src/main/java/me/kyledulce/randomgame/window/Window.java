package me.kyledulce.randomgame.window;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import me.kyledulce.randomgame.config.Config;
import me.kyledulce.randomgame.exception.WindowInitializeFailure;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.system.MemoryUtil.NULL;

@Singleton
public class Window {
    private Config config;
    private long windowId;

    @Inject
    public Window(Config config) {
        this.config = config;
    }

    public void initializeWindow() {
        // Initialize glfw
        if(!GLFW.glfwInit()) {
            throw new WindowInitializeFailure("Failed to start glfw");
        }

        // Display on primary monitor
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, config.getResizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, config.getMaximized() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        // Create Window
        windowId = GLFW.glfwCreateWindow(vidMode.width(), vidMode.height(), config.getTitle(), GLFW.glfwGetPrimaryMonitor(), NULL);

        if(windowId == NULL) {
            throw new WindowInitializeFailure("Failed to create window");
        }

        GLFW.glfwShowWindow(windowId);
        GLFW.glfwMakeContextCurrent(windowId);

        GL.createCapabilities();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void shutdownWindow() {
        GLFW.glfwDestroyWindow(windowId);
        windowId = 0;

        GLFW.glfwTerminate();
    }
}
