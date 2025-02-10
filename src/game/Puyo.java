package game;

import java.awt.Color;
import java.util.Random;

public class Puyo {
    private int x1, y1;
    private int x2, y2;
    private int rotationState;
    private final Color color1, color2;

    public Puyo(int x, int y, Color color){
        x1 = Constants.BOARD_WIDTH / 2 - 1;
        y1 = 0;

        x2 = Constants.BOARD_WIDTH / 2;
        y2 = 0;
        rotationState = 0;

        Random rand = new Random();
        color1 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
        color2 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
    }

    public int getX1() {
        return x1;
    }
    public int getY1() {
        return y1;
    }
    public int getX2() {
        return x2;
    }
    public int getY2() {
        return y2;
    }
    public Color getColor1() {
        return color1;
    }
    public Color getColor2() {
        return color2;
    }

    public void moveLeft(Board board) {
        if(canMove(-1, board)){
            x1--;
            x2--;
        }
    }

    public void moveRight(Board board) {
        if(canMove(1, board)){
            x1++;
            x2++;
        }
    }

    public void rotate(Board board){
        int nextRotation = (rotationState + 1 ) % 4;
        int newX2 = x1;
        int newY2 = y1;

        switch (nextRotation) {
            case 0: newX2 = x1 + 1; break;
            case 1: newY2 = y1 + 1; break;
            case 2: newX2 = x1 - 1; break;
            case 3: newY2 = y1 - 1; break;
        }

        if(handleRotationCollisions(newX2, newY2, board)){
            x2 = newX2;
            y2 = newY2;
            rotationState = nextRotation;
        }
    }

    private boolean handleRotationCollisions(int newX2, int newY2, Board board){
        if(newX2 >= Constants.BOARD_WIDTH){
            if (board.isValidPosition(x1 - 1, y1) && board.isValidPosition(newX2 - 1, newY2)){
                x1--;
                return true;
            }
            return false;
        }

        if(newX2 < 0){
            if (board.isValidPosition(x1 + 1, y1) && board.isValidPosition(newX2 + 1, newY2)) {
                x1++;
                return true;
            }
            return false;
        }
        return board.isValidPosition(newX2, newY2);
    }


    private boolean canMove(int dx, Board board){
        int newX1 = x1 + dx;
        int newX2 = x2 + dx;

        if(y1 != y2){
            return y1 < y2 ?
                    board.isValidPosition(newX1, y1) :
                    board.isValidPosition(newX2, y2);
        }
        return board.isValidPosition(newX1, y1) && board.isValidPosition(newX2, y2);
    }
}
