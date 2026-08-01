package org.example.buildings;

public record Point2D (int x, int y) {
    public Point2D xShift(int shift) {
        return new Point2D(this.x + shift, this.y);
    }

    public Point2D yShift(int shift) {
        return new Point2D(this.x, this.y + shift);
    }
}
