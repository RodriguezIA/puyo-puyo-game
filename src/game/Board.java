package game;

import java.awt.Color;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Board {
    private Color[][] grid;

    public Board() {
        grid = new Color[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
    }

    /**
     * Devuelve la grilla del tablero.
     *
     * @return la grilla (matriz bidimensional de Color)
     */
    public Color[][] getGrid() {
        return grid;
    }

    /**
     * Verifica si la posición (x, y) es válida (es decir, está dentro del tablero y la celda está vacía).
     *
     * @param x coordenada x
     * @param y coordenada y
     * @return true si es válida; false en caso contrario
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < Constants.BOARD_WIDTH &&
                y >= 0 && y < Constants.BOARD_HEIGHT &&
                grid[y][x] == null;
    }

    /**
     * Verifica si en la posición (x, y) se puede caer (es decir, si la celda inferior está vacía).
     *
     * @param x coordenada x
     * @param y coordenada y
     * @return true si se puede caer; false en caso contrario
     */
    public boolean canFall(int x, int y) {
        return y + 1 < Constants.BOARD_HEIGHT && grid[y + 1][x] == null;
    }

    /**
     * Coloca un par de puyos en la grilla, verificando que las posiciones sean válidas.
     *
     * @param puyo el par de puyos a colocar
     */
    public void placePuyo(Puyo puyo) {
        if (isValidPosition(puyo.x1, puyo.y1)) {
            grid[puyo.y1][puyo.x1] = puyo.color1;
        }
        if (isValidPosition(puyo.x2, puyo.y2)) {
            grid[puyo.y2][puyo.x2] = puyo.color2;
        }
    }

    /**
     * Aplica la gravedad haciendo que los puyos caigan a las posiciones libres.
     */
    public void applyGravity() {
        for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
            int emptySpace = 0;
            // Recorremos de abajo hacia arriba
            for (int y = Constants.BOARD_HEIGHT - 1; y >= 0; y--) {
                if (grid[y][x] == null) {
                    emptySpace++;
                } else if (emptySpace > 0) {
                    grid[y + emptySpace][x] = grid[y][x];
                    grid[y][x] = null;
                }
            }
        }
    }

    /**
     * Busca coincidencias en la grilla: si existen grupos de 4 o más puyos del mismo color, los elimina,
     * aplica gravedad y vuelve a verificar coincidencias.
     */
    public void checkMatches() {
        printBoard();
        Set<Point> matchedPuyos = new HashSet<>();
        boolean[][] visited = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];

        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (grid[y][x] != null && !visited[y][x]) {
                    Color currentColor = grid[y][x];
                    Set<Point> currentGroup = new HashSet<>();
                    Queue<Point> queue = new LinkedList<>();
                    queue.add(new Point(x, y));
                    visited[y][x] = true;

                    while (!queue.isEmpty()) {
                        Point p = queue.poll();
                        currentGroup.add(p);

                        // Explorar en las cuatro direcciones
                        checkAdjacent(p.x, p.y - 1, currentColor, visited, queue);
                        checkAdjacent(p.x, p.y + 1, currentColor, visited, queue);
                        checkAdjacent(p.x - 1, p.y, currentColor, visited, queue);
                        checkAdjacent(p.x + 1, p.y, currentColor, visited, queue);
                    }

                    if (currentGroup.size() >= 4) {
                        matchedPuyos.addAll(currentGroup);
                    }
                }
            }
        }

        if (!matchedPuyos.isEmpty()) {
            for (Point p : matchedPuyos) {
                grid[p.y][p.x] = null;
            }
            applyGravity();
            checkMatches(); // Verificar recursivamente nuevas coincidencias
        }
    }

    /**
     * Método auxiliar para explorar posiciones adyacentes en la búsqueda de coincidencias.
     */
    private void checkAdjacent(int x, int y, Color targetColor, boolean[][] visited, Queue<Point> queue) {
        if (x >= 0 && x < Constants.BOARD_WIDTH &&
                y >= 0 && y < Constants.BOARD_HEIGHT) {
            if (!visited[y][x] && grid[y][x] == targetColor) {
                visited[y][x] = true;
                queue.add(new Point(x, y));
            }
        }
    }

    /**
     * Imprime en consola el estado actual del tablero.
     */
    public void printBoard() {
        System.out.println("Current Board State:");
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                System.out.print(grid[y][x] == null ? " . " : " X ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
