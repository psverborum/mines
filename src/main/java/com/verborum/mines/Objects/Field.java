package com.verborum.mines.Objects;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Random;

public class Field {
    private final ArrayList<Cell> fieldsList;
    private final int size;
    private final int upperbound;
    private final Random rand;
    private boolean unhideMines;
    private int freeCount;

    public Field(int size, int upperbound) {
        this.fieldsList  = new ArrayList<>();
        this.rand        = new Random();
        this.upperbound  = upperbound;
        this.size        = size;
        this.unhideMines = false;
        this.freeCount   = 0;
    }

    public void draw() {
        for (Cell cell : this.fieldsList) {
            cell.draw(this.unhideMines);
        }
        if (this.freeCount == 0) {
            this.win();
        }
    }

    public void generateFields() {
        this.fieldsList.clear();

        boolean[][] minesArray = new boolean[this.size][this.size];

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                minesArray[x][y] = rand.nextInt(this.upperbound) < (this.upperbound / 6);
                if (!minesArray[x][y]) {
                    this.freeCount++;
                }
            }
        }

        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                this.fieldsList.add(new Cell(x, y, this.countMines(x, y, minesArray), minesArray[x][y], this.size));
            }
        }
    }

    public void openFieldsFrom(int index) {
        if (this.fieldsList.get(index).isOpened()) {
            return;
        }
        this.openField(index);
        if (this.fieldsList.get(index).getMinesCount() == 0) {
            for (int openIndex : this.getCorrectFieldIndex(index)) {
                if (this.fieldsList.get(openIndex).getMinesCount() == 0) {
                    this.openFieldsFrom(openIndex);
                } else {
                    if (!this.fieldsList.get(openIndex).isOpened()) {
                        this.openField(openIndex);
                    }
                }
            }
        }
    }

    public void flagField(int index) {
        if (!this.fieldsList.get(index).isOpened()) {
            this.fieldsList.get(index).setFlag(!this.fieldsList.get(index).isFlag());
        }
    }

    public void invertUnhideMines() {
        this.unhideMines = !this.unhideMines;
    }

    private void openField(int index) {
        this.fieldsList.get(index).setOpened(true);
        this.fieldsList.get(index).setFlag(false);
        if (this.fieldsList.get(index).isMine()) {
            this.gameOver();
        } else {
            this.freeCount--;
        }
    }

    private ArrayList<Integer> getCorrectFieldIndex(int index) {
        int x = index / this.size;
        int y = index % this.size;
        ArrayList<Integer> resultList = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ( (i == 0) && (j == 0) ) {
                    continue;
                }
                if ( (x + i >= 0) && (x + i < this.size) && (y + j >= 0) && (y + j < this.size) ) {
                    resultList.add((x + i) * this.size + y + j);
                }
            }
        }
        return resultList;
    }

    private void gameOver() {
        for (Cell cell : fieldsList) {
            cell.setOpened(true);
            cell.setFlag(false);
        }
    }

    private void win() {
        for (Cell cell : fieldsList) {
            cell.setOpened(true);
            cell.setFlag(false);
        }
        this.drawWin();
    }

    private void drawWin() {
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glColor3f(0.9f, 0.3f, 0.3f);
        GL11.glVertex3f(1.0f, 1.33333f, 0.0f);
        GL11.glVertex3f(0.83333f, 1.5f, 0.0f);
        GL11.glColor3f(0.9f, 0.03f, 0.05f);
        GL11.glVertex3f(0.5f, 1.5f, 0.0f);
        GL11.glVertex3f(0.5f, 1.0f, 0.0f);
        GL11.glVertex3f(1.0f, 0.5f, 0.0f);
        GL11.glVertex3f(1.5f, 1.0f, 0.0f);
        GL11.glVertex3f(1.5f, 1.5f, 0.0f);
        GL11.glVertex3f(1.16666f, 1.5f, 0.0f);
        GL11.glEnd();
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
}
