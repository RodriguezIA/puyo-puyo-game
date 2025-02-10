// Esta clase manejara la logica del juego incluyendo el movimiento de filas y columnas y el game loop
package game;


import java.awt.*;


public class Board {
    private final Color[][] grid;

    public Board(){
        grid = new Color[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
    }

    public Color getColorAt(int x, int y){
        return grid[y][x];
    }

    public void setColorAt(int x, int y, Color color){
        grid[y][x] = color;
    }

    public boolean isValidPosition(int x, int y){
        return x >= 0 && x < Constants.BOARD_WIDTH &&
                y >= 0 && y < Constants.BOARD_HEIGHT &&
                grid[y][x] == null;
    }

    public boolean canFall (int x, int y){
        return y + 1 < Constants.BOARD_HEIGHT && grid[y + 1][x] == null;
    }

    public void applyGravity(){
        for(int x = 0; x < Constants.BOARD_WIDTH; x++){
            int emptySpace = 0;

            for(int y = Constants.BOARD_HEIGHT - 1; y >= 0; y--){
                if(grid[y][x] == null){
                    emptySpace++;
                } else if (emptySpace > 0){
                    grid[y + emptySpace][x] = grid[y][x];
                    grid[y][x] = null;
                }
            }
        }
    }
}
