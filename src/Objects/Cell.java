package Objects;

import org.lwjgl.opengl.GL11;

public class Cell {
    private final int x;
    private final int y;
    private final int minesCount;
    private final float size;
    private final boolean isMine;

    private boolean isOpened;
    private boolean isFlag;

    public Cell(int x, int y, int minesCount, boolean isMine, int size) {
        this.x          = x;
        this.y          = y;
        this.isMine     = isMine;
        this.minesCount = minesCount;
        this.size       = 2.0f / (float) size;
    }

    public void draw(boolean unhideMines) {
        if (unhideMines || this.isOpened) {
            if (this.isMine) {
                GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                    GL11.glColor3f(0.7f, 0.0f, 0.0f);
                    GL11.glVertex2f(0.0f + (this.x * this.size), this.size + (this.y * this.size));
                    GL11.glColor3f(0.6f, 0.0f, 0.0f);
                    GL11.glVertex2f(this.size + (this.x * this.size), this.size + (this.y * this.size));
                    GL11.glVertex2f(0.0f + (this.x * this.size), 0.0f + (this.y * this.size));
                    GL11.glColor3f(0.5f, 0.0f, 0.0f);
                    GL11.glVertex2f(this.size + (this.x * this.size), 0.0f + (this.y * this.size));
                GL11.glEnd();
            } else {
                if (this.minesCount == 0) {
                    GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                        GL11.glColor3f(0.5f, 1.0f, 0.0f);
                        GL11.glVertex2f(0.0f + (this.x * this.size), this.size + (this.y * this.size));
                        GL11.glColor3f(0.4f, 0.9f, 0.0f);
                        GL11.glVertex2f(this.size + (this.x * this.size), this.size + (this.y * this.size));
                        GL11.glVertex2f(0.0f + (this.x * this.size), 0.0f + (this.y * this.size));
                        GL11.glColor3f(0.3f, 0.8f, 0.0f);
                        GL11.glVertex2f(this.size + (this.x * this.size), 0.0f + (this.y * this.size));
                    GL11.glEnd();
                } else {
                    GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                        GL11.glColor3f(0.3f, 0.7f, 0.0f);
                        GL11.glVertex2f(0.0f + (this.x * this.size), this.size + (this.y * this.size));
                        GL11.glColor3f(0.2f, 0.6f, 0.0f);
                        GL11.glVertex2f(this.size + (this.x * this.size), this.size + (this.y * this.size));
                        GL11.glVertex2f(0.0f + (this.x * this.size), 0.0f + (this.y * this.size));
                        GL11.glColor3f(0.1f, 0.5f, 0.0f);
                        GL11.glVertex2f(this.size + (this.x * this.size), 0.0f + (this.y * this.size));
                    GL11.glEnd();
                    this.drawNum();
                }
            }
        } else {
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
                GL11.glColor3f(0.9f, 0.9f, 0.9f);
                GL11.glVertex2f(0.0f + (this.x * this.size), this.size + (this.y * this.size));
                GL11.glColor3f(0.8f, 0.8f, 0.8f);
                GL11.glVertex2f(this.size + (this.x * this.size), this.size + (this.y * this.size));
                GL11.glVertex2f(0.0f + (this.x * this.size), 0.0f + (this.y * this.size));
                GL11.glColor3f(0.7f, 0.7f, 0.7f);
                GL11.glVertex2f(this.size + (this.x * this.size), 0.0f + (this.y * this.size));
            GL11.glEnd();
        }
        if (this.isFlag) {
            GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
            GL11.glColor3f(0.5f, 0.0f, 1.0f);
            GL11.glVertex2f(0.0f + (this.x * this.size), this.size + (this.y * this.size));
            GL11.glColor3f(0.4f, 0.0f, 0.9f);
            GL11.glVertex2f(this.size + (this.x * this.size), this.size + (this.y * this.size));
            GL11.glVertex2f(0.0f + (this.x * this.size), 0.0f + (this.y * this.size));
            GL11.glColor3f(0.3f, 0.0f, 0.8f);
            GL11.glVertex2f(this.size + (this.x * this.size), 0.0f + (this.y * this.size));
            GL11.glEnd();
        }
    }

    public int getMinesCount() {
        return this.minesCount;
    }

    public boolean isOpened() {
        return this.isOpened;
    }

    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    public boolean isFlag() {
        return this.isFlag;
    }

    public void setFlag(boolean flag) {
        this.isFlag = flag;
    }

    public boolean isMine() {
        return this.isMine;
    }

    private void drawNum() {
        GL11.glPointSize((30.0f * this.size) * 2.0f);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glColor3f(0.0f, 0.0f, 0.0f);
        switch (this.minesCount) {
            case 1 -> {
                GL11.glVertex2f(this.size / 2.0f + (this.x * this.size), this.size / 2.0f + (this.y * this.size));
            }
            case 2 -> {
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), this.size / 2.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), this.size / 2.0f + (this.y * this.size));
            }
            case 3 -> {
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 2.0f + (this.x * this.size), this.size / 2.0f + (this.y * this.size));
            }
            case 4 -> {
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
            }
            case 5 -> {
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 2.0f + (this.x * this.size), this.size / 2.0f + (this.y * this.size));
            }
            case 6 -> {
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f((this.size * 3.0f) / 4.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 2.0f + (this.x * this.size), (this.size * 3.0f) / 4.0f + (this.y * this.size));
                GL11.glVertex2f(this.size / 2.0f + (this.x * this.size), this.size / 4.0f + (this.y * this.size));
            }
        }
        GL11.glEnd();
    }
}
