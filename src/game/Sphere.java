package game;

import java.awt.*;

public class Sphere {
    private int x, y;
    private Color color;

    public Sphere(int x, int y, Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void fall(){
        this.y += 1;
    }
}
