import Objects.Field;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {
    private int size = 10;
    private long window;
    private final int windowWidth;
    private final int windowHeight;
    private final String title;
    private int upperbound = 15;
    private Field field;
    private int posX;
    private int posY;

    public Engine(int windowWidth, int windowHeight, String title) {
        this.windowWidth  = windowWidth;
        this.windowHeight = windowHeight;
        this.title        = title;
    }

    public void run() {
        init();
        draw();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(this.windowWidth, this.windowHeight, this.title, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( action == GLFW_RELEASE ) {
                switch (key) {
                    case GLFW_KEY_ESCAPE    -> glfwSetWindowShouldClose(window, true);
                    case GLFW_KEY_RIGHT_ALT -> this.field.invertUnhideMines();
                    case GLFW_KEY_N         -> this.generateFields();
                    case GLFW_KEY_LEFT      -> {
                        this.size--;
                        if (this.size < 5) {
                            this.size = 5;
                        }
                        this.generateFields();
                    }
                    case GLFW_KEY_RIGHT     -> {
                        this.size++;
                        this.generateFields();
                    }
                    case GLFW_KEY_UP        -> {
                        this.upperbound++;
                        this.generateFields();
                    }
                    case GLFW_KEY_DOWN      -> {
                        this.upperbound--;
                        if (this.upperbound < 5) {
                            this.upperbound = 5;
                        }
                        this.generateFields();
                    }
                }
            }
        });

        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
                posX = (int) (xpos / (this.windowWidth / this.size));
                posY = (int) ((this.windowHeight - ypos) / (this.windowHeight / this.size));
        });

        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                this.field.openFieldsFrom(this.indexByMousePos());
            } else if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_RELEASE) {
                this.field.flagField(this.indexByMousePos());
            }
        });

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);

        this.generateFields();
    }

    private void generateFields() {
        this.field = new Field(this.size, this.upperbound);
        this.field.generateFields();
    }

    private int indexByMousePos() {
        return this.posX * this.size + this.posY;
    }

    private void draw() {
        GL.createCapabilities();
        GL11.glClearColor(0.7f, 1.0f, 0.7f, 0.0f);
        GL11.glTranslatef(-1.0f, -1.0f, 0.0f);

        while ( !glfwWindowShouldClose(window) ) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            this.field.draw();

            org.lwjgl.glfw.GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }
}
