package game;

import java.awt.Color;
import java.util.Random;

public class Puyo {
    // Posiciones de las dos esferas
    public int x1, y1; // Primera esfera
    public int x2, y2; // Segunda esfera
    public int rotationState = 0; // 0: derecha, 1: abajo, 2: izquierda, 3: arriba
    public Color color1, color2;

    public Puyo() {
        // Posición inicial en la parte superior, centrado
        x1 = Constants.BOARD_WIDTH / 2 - 1;
        y1 = 0;
        x2 = Constants.BOARD_WIDTH / 2;
        y2 = 0;

        // Colores aleatorios para cada esfera
        Random rand = new Random();
        color1 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
        color2 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
    }

    /**
     * Rota el par de Puyos. Se debe pasar el tablero actual para verificar que la nueva
     * posición sea válida.
     *
     * @param board El tablero del juego (arreglo bidimensional de Color)
     */
    public void rotate(Color[][] board) {
        int nextRotation = (rotationState + 1) % 4;
        int newX2 = x1;
        int newY2 = y1;

        switch (nextRotation) {
            case 0:
                newX2 = x1 + 1;
                newY2 = y1;
                break;
            case 1:
                newX2 = x1;
                newY2 = y1 + 1;
                break;
            case 2:
                newX2 = x1 - 1;
                newY2 = y1;
                break;
            case 3:
                newX2 = x1;
                newY2 = y1 - 1;
                break;
        }

        // Verificar si la rotación es válida
        if (isValidPosition(newX2, newY2, board)) {
            x2 = newX2;
            y2 = newY2;
            rotationState = nextRotation;
        }

        // Verificar si la rotación golpea el borde derecho del panel
        if (newX2 >= Constants.BOARD_WIDTH) {
            if (isValidPosition(x1 - 1, y1, board) && isValidPosition(newX2 - 1, newY2, board)) {
                x1--;
                x2 = newX2 - 1;
                y2 = newY2;
                rotationState = nextRotation;
                return;
            }
        }

        // Verificar si la rotación golpea el borde izquierdo del panel
        if (newX2 < 0) {
            if (isValidPosition(x1 + 1, y1, board) && isValidPosition(newX2 + 1, newY2, board)) {
                x1++;
                x2 = newX2 + 1;
                y2 = newY2;
                rotationState = nextRotation;
                return;
            }
        }

        // Si el puyo golpea el suelo u otro elemento, intentar subir
        if (newY2 >= Constants.BOARD_HEIGHT || (newY2 >= 0 && board[newY2][newX2] != null)) {
            if (y1 > 0 && isValidPosition(x1, y1 - 1, board) && isValidPosition(newX2, newY2 - 1, board)) {
                y1--;
                x2 = newX2;
                y2 = newY2 - 1;
                rotationState = nextRotation;
            }
        }
    }

    /**
     * Verifica si la posición (x, y) es válida en el tablero.
     *
     * @param x     La coordenada x
     * @param y     La coordenada y
     * @param board El tablero del juego
     * @return true si la posición es válida; false en caso contrario
     */
    private boolean isValidPosition(int x, int y, Color[][] board) {
        return x >= 0 && x < Constants.BOARD_WIDTH &&
                y >= 0 && y < Constants.BOARD_HEIGHT &&
                board[y][x] == null;
    }
}
