package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class GamePanel extends JPanel {

    private Color[][] board;
    private Puyo currentPair;  // Declaración de currentPair
    private boolean gameOver;
    private Timer timer;

    private HashMap<Color, Image> puyoImages = new HashMap<>();

    // Clase interna Puyo
    private class Puyo {
        int x1, y1; // First sphere
        int x2, y2; // Second sphere
        int rotationState = 0; // 0: derecha, 1: abajo, 2: izquierda 3: arriba
        Color color1, color2;

        public Puyo() {
            // Start position at top center
            x1 = Constants.BOARD_WIDTH / 2 - 1;
            y1 = 0;
            x2 = Constants.BOARD_WIDTH / 2;
            y2 = 0;

            // Random colors
            Random rand = new Random();
            color1 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
            color2 = Constants.PUYO_COLORS[rand.nextInt(Constants.PUYO_COLORS.length)];
        }

        public void rotate() {
            int nextRotation = (rotationState + 1) % 4;

            int newX2 = x1;
            int newY2 = y1;

            switch (nextRotation){
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

            // verificar si la rotacion es valida
            if(isValidPosition(newX2, newY2)){
                x2 = newX2;
                y2 = newY2;
                rotationState = nextRotation;
            }

            // verificacion si la rotacion golpe la derecha del panel
            if(newX2 >= Constants.BOARD_WIDTH){
                if(isValidPosition(x1 - 1, y1) && isValidPosition(newX2 - 1, newY2 )){
                    x1--;
                    x2 = newX2 - 1;
                    y2 = newY2;
                    rotationState = nextRotation;
                    return;
                }
            }

            // verificacion si la rotacion golpe la izquierda del panel
            if(newX2 < 0){
                if(isValidPosition(x1 + 1, y1) && isValidPosition(newX2 + 1, newY2 )){
                    x1++;
                    x2 = newX2 + 1;
                    y2 = newY2;
                    rotationState = nextRotation;
                    return;
                }
            }

            // si el puyo golpea el suelo u otro elemento intnetar subir
            if (newY2 >= Constants.BOARD_HEIGHT || (newY2 >= 0 && board[newY2][newX2] != null)) {
                if (y1 > 0 && isValidPosition(x1, y1 - 1) && isValidPosition(newX2, newY2 - 1)) {
                    y1--;
                    x2 = newX2;
                    y2 = newY2 - 1;
                    rotationState = nextRotation;
                }
            }
        }
    }

    public GamePanel() {
        board = new Color[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH * Constants.CELL_SIZE, Constants.BOARD_HEIGHT * Constants.CELL_SIZE));
        setupGame();
        setupControls();
        loadImages();
    }

    private void loadImages() {
        try {
            puyoImages.put(Color.RED, ImageIO.read(new File("src/assets/puyo_red.png")));
            puyoImages.put(Color.BLUE, ImageIO.read(new File("src/assets/puyo_blue.png")));
            puyoImages.put(Color.GREEN, ImageIO.read(new File("src/assets/puyo_green.png")));
            puyoImages.put(Color.YELLOW, ImageIO.read(new File("src/assets/puyo_yellow.png")));
        } catch (IOException e) {
            System.err.println("Error al cargar las imágenes: " + e.getMessage());
            // Manejar el error, por ejemplo, usando imágenes por defecto o cerrando el juego
        }
    }

    private void setupGame() {
        gameOver = false;
        board = new Color[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
        currentPair = null;

        timer = new Timer(1000, e -> {
            if (!gameOver) {
                moveDown();
                repaint();
            }
        });
        timer.start();
    }

    private void setupControls() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && currentPair != null) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            if (canMove(-1)) {
                                currentPair.x1--;
                                currentPair.x2--;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (canMove(1)) {
                                currentPair.x1++;
                                currentPair.x2++;
                            }
                            break;
                        case KeyEvent.VK_Z:
                            currentPair.rotate();
                            break;
                        case KeyEvent.VK_DOWN:
                            moveDown();
                            break;
                    }
                    repaint();
                }
            }
        });
    }

    private boolean canMove(int dx) {
        int newX1 = currentPair.x1 + dx;
        int newX2 = currentPair.x2 + dx;

        // Si las esferas están a diferentes alturas, solo mover la que no ha colisionado
        if (currentPair.y1 != currentPair.y2) {
            if (currentPair.y1 < currentPair.y2) {
                // La primera esfera está más arriba, solo mover esa
                return newX1 >= 0 && newX1 < Constants.BOARD_WIDTH && board[currentPair.y1][newX1] == null;
            } else {
                // La segunda esfera está más arriba, solo mover esa
                return newX2 >= 0 && newX2 < Constants.BOARD_WIDTH && board[currentPair.y2][newX2] == null;
            }
        }

        // Si están a la misma altura, verificar ambas
        return newX1 >= 0 && newX1 < Constants.BOARD_WIDTH && newX2 >= 0 && newX2 < Constants.BOARD_WIDTH
                && board[currentPair.y1][newX1] == null
                && board[currentPair.y2][newX2] == null;
    }

    private void moveDown() {
        if (currentPair == null) {
            currentPair = new Puyo();
            if (!isValidPosition(currentPair.x1, currentPair.y1) || !isValidPosition(currentPair.x2, currentPair.y2)) {
                gameOver = true;
                timer.stop();
                return;
            }
        }

        boolean canFall1 = canFall(currentPair.x1, currentPair.y1);
        boolean canFall2 = canFall(currentPair.x2, currentPair.y2);

        if (canFall1 && canFall2) {
            currentPair.y1++;
            currentPair.y2++;
            repaint();
            return;
        } else {
            if(canFall1){
                currentPair.y1++;
                repaint();
                return;
            }
            if(canFall2){
                currentPair.y2++;
                repaint();
                return;
            }
            if(!canFall1 && !canFall2){
                placePuyo();
                checkMatches();
                currentPair = null;
            }
        }
    }

    private boolean canFall(int x, int y) {
        return y + 1 < Constants.BOARD_HEIGHT && board[y + 1][x] == null;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < Constants.BOARD_WIDTH && y >= 0 && y < Constants.BOARD_HEIGHT && board[y][x] == null;
    }

    private void placePuyo() {
        // Verificar que las posiciones son válidas antes de colocar
        if (isValidPosition(currentPair.x1, currentPair.y1) && board[currentPair.y1][currentPair.x1] == null) {
            board[currentPair.y1][currentPair.x1] = currentPair.color1;
        }
        if (isValidPosition(currentPair.x2, currentPair.y2) && board[currentPair.y2][currentPair.x2] == null) {
            board[currentPair.y2][currentPair.x2] = currentPair.color2;
        }
    }

    private void checkMatches() {
        printBoard();
        Set<Point> matchedPuyos = new HashSet<>();
        boolean[][] visited = new boolean[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];

        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (board[y][x] != null && !visited[y][x]) {
                    Color currentColor = board[y][x];
                    Set<Point> currentGroup = new HashSet<>();
                    Queue<Point> queue = new LinkedList<>();
                    queue.add(new Point(x, y));
                    visited[y][x] = true;

                    while (!queue.isEmpty()) {
                        Point p = queue.poll();
                        currentGroup.add(p);

                        // Explorar en todas las direcciones (arriba, abajo, izquierda, derecha)
                        // Arriba
                        checkAdjacent(p.x, p.y - 1, currentColor, visited, queue);
                        // Abajo
                        checkAdjacent(p.x, p.y + 1, currentColor, visited, queue);
                        // Izquierda
                        checkAdjacent(p.x - 1, p.y, currentColor, visited, queue);
                        // Derecha
                        checkAdjacent(p.x + 1, p.y, currentColor, visited, queue);
                    }

                    if (currentGroup.size() >= 4) {
                        matchedPuyos.addAll(currentGroup);
                    }
                }
            }
        }

        // Eliminar Puyos coincidentes y aplicar gravedad
        if (!matchedPuyos.isEmpty()) {
            for (Point p : matchedPuyos) {
                board[p.y][p.x] = null;
            }
            applyGravity();
            checkMatches(); // Verificar nuevas coincidencias después de aplicar gravedad
        }
    }

    private void checkAdjacent(int x, int y, Color targetColor, boolean[][] visited, Queue<Point> queue) {
        if (x >= 0 && x < Constants.BOARD_WIDTH && y >= 0 && y < Constants.BOARD_HEIGHT) {
            if (!visited[y][x] && board[y][x] == targetColor) {
                visited[y][x] = true;
                queue.add(new Point(x, y));
            }
        }
    }

    private void applyGravity() {
        for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
            int emptySpace = 0;
            // Recorrer de abajo hacia arriba
            for (int y = Constants.BOARD_HEIGHT - 1; y >= 0; y--) {
                if (board[y][x] == null) {
                    emptySpace++;
                } else if (emptySpace > 0) {
                    // Mover el Puyo hacia abajo
                    board[y + emptySpace][x] = board[y][x];
                    board[y][x] = null;
                }
            }
        }
        repaint();
    }

    private void printBoard() {
        System.out.println("Current Board State:");
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (board[y][x] == null) {
                    System.out.print(" . ");
                } else {
                    System.out.print(" X ");  // Puedes usar diferentes letras para los colores
                }
            }
            System.out.println();
        }
        System.out.println();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el tablero
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                g.setColor(Color.GRAY);
                g.drawRect(x * Constants.CELL_SIZE, y * Constants.CELL_SIZE, Constants.CELL_SIZE, Constants.CELL_SIZE);

                if (board[y][x] != null) {
                    Image image = puyoImages.get(board[y][x]); // Obtener imagen por color
                    if (image != null) {
                        g.drawImage(image, x * Constants.CELL_SIZE + 2, y * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
                    } else {
                        g.setColor(board[y][x]);
                        g.fillOval(x * Constants.CELL_SIZE + 2, y * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
                    }
                }
            }
        }

        // Dibujar el par actual (CORREGIDO)
        if (currentPair != null) {
            Image image1 = puyoImages.get(currentPair.color1); // Obtener imagen por color
            if (image1 != null) {
                g.drawImage(image1, currentPair.x1 * Constants.CELL_SIZE + 2, currentPair.y1 * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
            } else {
                g.setColor(currentPair.color1);
                g.fillOval(currentPair.x1 * Constants.CELL_SIZE + 2, currentPair.y1 * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
            }

            Image image2 = puyoImages.get(currentPair.color2); // Obtener imagen por color
            if (image2 != null) {
                g.drawImage(image2, currentPair.x2 * Constants.CELL_SIZE + 2, currentPair.y2 * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
            } else {
                g.setColor(currentPair.color2);
                g.fillOval(currentPair.x2 * Constants.CELL_SIZE + 2, currentPair.y2 * Constants.CELL_SIZE + 2, Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
            }
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String message = "Game Over!";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(message)) / 2;
            int y = getHeight() / 2;
            g.drawString(message, x, y);
        }
    }
}