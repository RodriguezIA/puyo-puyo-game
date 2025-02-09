package game;

import java.awt.*;

public class FallingPair {
    private Sphere[] pair;
    private int rotationState;

    public FallingPair(Sphere[] pair) {
        this.pair = pair;
    }

    public Sphere[] getSpheres() {
        return pair;
    }

    public void rotate() {
        Sphere main = pair[0];
        Sphere secondary = pair[1];

        int newX = main.getX();
        int newY = main.getY();

        switch (rotationState % 4) {
            case 0: // Vertical a horizontal
                newX = main.getX() + 1;
                newY = main.getY();
                break;
            case 1: // Horizontal a vertical invertida
                newX = main.getX();
                newY = main.getY() + 1;
                break;
            case 2: // Vertical invertida a horizontal invertida
                newX = main.getX() - 1;
                newY = main.getY();
                break;
            case 3: // Horizontal invertida a vertical
                newX = main.getX();
                newY = main.getY() - 1;
                break;
        }

        // Verificar límites del tablero
        if (newX >= 0 && newX < Board.getCOLUMNS() && newY >= 0 && newY < Board.getROWS()) {
            secondary.setPosition(newX, newY);
            rotationState++;
        }
    }
}
