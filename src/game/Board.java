// Esta clase manejara la logica del juego incluyendo el movimiento de filas y columnas y el game loop
package game;


import java.awt.*;
import java.util.Random;

public class Board {
    private static final int COLUMNS = 6;
    private static final int ROWS = 12;
    private static final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

    private Sphere[][] board;

    public Board(){
        board = new Sphere[ROWS][COLUMNS];
    }

    public static int getCOLUMNS() {
        return COLUMNS;
    }

    public static int getROWS() {
        return ROWS;
    }

    public boolean isCellEmpty(int row, int col){
        return board[row][col] == null;
    }

    public void pleaceSphere(Sphere sphere){
        board[sphere.getY()][sphere.getX()] = sphere;
    }

    public Sphere[] generateSpherePair(){
        Random random = new Random();
        int col = random.nextInt(COLUMNS-1);
        Color color1 = COLORS[random.nextInt(COLORS.length)];
        Color color2 = COLORS[random.nextInt(COLORS.length)];

        return new Sphere[]{
                new Sphere(col, 0, color1),
                new Sphere(col, 1,  color2)
        };
    }

    public Sphere getSphere(int row, int col) {
        if (row >= 0 && row < ROWS && col >= 0 && col < COLUMNS ){
            return board[row][col];
        }
        return null;
    }
}
