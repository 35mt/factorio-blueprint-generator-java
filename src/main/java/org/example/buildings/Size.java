package org.example.buildings;

public class Size {
    public final int x;
    public final int y;

    public Size(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Size xShift(int shift) {
        return new Size(this.x + shift, this.y);
    }

    public Size yShift(int shift) {
        return new Size(this.x, this.y + shift);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Size size) {
            return this.x == size.x && this.y == size.y;
        }
        return false;
    }
}
