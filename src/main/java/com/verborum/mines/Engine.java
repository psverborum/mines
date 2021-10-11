package com.verborum.mines;

import com.verborum.mines.Objects.Field;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

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
    private ArrayList<Field> fieldsList;
    private Random rand;
    private boolean unhideMines;
    private int upperbound = 15;
    private GLFWMouseButtonCallback mouseCallback;
    private GLFWCursorPosCallback posCallback;
    private int posX;
    private int posY;

    public Engine(int windowWidth, int windowHeight, String title) {
        this.windowWidth  = windowWidth;
        this.windowHeight = windowHeight;
        this.title        = title;
        this.fieldsList   = new ArrayList<>();
        this.rand         = new Random();
        this.unhideMines  = false;
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
                    case GLFW_KEY_RIGHT_ALT -> this.unhideMines = !this.unhideMines;
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

        glfwSetCursorPosCallback(window, posCallback = GLFWCursorPosCallback.create((window, xpos, ypos) -> {
                posX = (int) (xpos / (this.windowWidth / this.size));
                posY = (int) ((this.windowHeight - ypos) / (this.windowHeight / this.size));
                System.out.println(posX + " - " + posY + " || " + this.indexByMousePos());
        }));

        glfwSetMouseButtonCallback(window, mouseCallback = GLFWMouseButtonCallback.create((window, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                this.openField(this.indexByMousePos());
            } else if (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_RELEASE) {
                this.flagField(this.indexByMousePos());
            }
        }));

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    private int indexByMousePos() {
        return this.posX * this.size + this.posY;
    }

    private int indexByCoor(int x, int y) {
        return x * this.size + y;
    }

    private void openField(int index) {
        this.fieldsList.get(index).setOpened(true);
        this.fieldsList.get(index).setFlag(false);
        if (this.fieldsList.get(index).getMinesCount() == 0) {
            for (int i = -10; i <= 10; i = i + 10){
                for (int j = -1; j <= 1; j++){
                    if ((i == 0) && (j == 0))
                        continue;
                    try {
                        if (!this.fieldsList.get(index + i + j).isOpened()) {
                            if (this.fieldsList.get(index + i + j).getMinesCount() == 0 ) {
                                this.openField(index + i + j);
                            } else {
                                this.fieldsList.get(index + i + j).setOpened(true);
                                this.fieldsList.get(index + i + j).setFlag(false);
                            }
                        }
                    } catch (Exception ignored){}
                }
            }
        }
    }

    private void flagField(int index) {
        if (!this.fieldsList.get(index).isOpened()) {
            this.fieldsList.get(index).setFlag(true);
        }
    }

    private int countMines(int x, int y, boolean[][] minesArray) {
        int result = 0;
        for (int i = x - 1; i <= x + 1; i++){
            for (int j = y - 1; j <= y + 1; j++){
                if ((i == x) && (j == y))
                    continue;
                try {
                    if (minesArray[i][j]) {
                        result++;
                    }
                } catch (Exception ignored){}
            }
        }
        return result;
    }

    private void generateFields() {
        this.fieldsList.clear();

        boolean[][] minesArray = new boolean[this.size][this.size];

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                minesArray[x][y] = rand.nextInt(this.upperbound) < (this.upperbound / 5);
            }
        }

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                this.fieldsList.add(new Field(x, y, this.countMines(x, y, minesArray), minesArray[x][y], this.size));
            }
        }
    }

    private void draw() {
        GL.createCapabilities();
        GL11.glClearColor(0.7f, 1.0f, 0.7f, 0.0f);
        GL11.glTranslatef(-1.0f, -1.0f, 0.0f);

        this.generateFields();

        while ( !glfwWindowShouldClose(window) ) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            for (Field field : this.fieldsList) {
                field.draw(this.unhideMines);
            }

            org.lwjgl.glfw.GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }
}
