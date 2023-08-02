package org.htw.prog2.aufgabe2;

public class DICOMFrameMark {
    private int x;
    private int y;
    private int size;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }

    public DICOMFrameMark(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public DICOMFrameMark(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 10;
    }

    public boolean isInMark(int x, int y){
        return (this.x <= x && x <= this.x + size) && (this.y <= y && y <= this.y + size);
    }
}
