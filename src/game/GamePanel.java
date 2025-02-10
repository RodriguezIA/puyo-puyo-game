package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Clase GamePanel que extiende JPanel y se encarga de administrar la lógica y el renderizado del juego.
 * Esta clase gestiona el tablero, el movimiento de los puyos, los controles de teclado y el dibujo en pantalla.
 */
public class GamePanel extends JPanel {
    private Board board;
    private Puyo currentPair;
    private boolean gameOver;
    private Timer timer;
    private HashMap<Color, Image> puyoImages = new HashMap<>();


    public GamePanel() {
        board = new Board();
        // Se establece el tamaño del panel basado en el ancho, alto y tamaño de cada celda definidos en Constants
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH * Constants.CELL_SIZE,
                Constants.BOARD_HEIGHT * Constants.CELL_SIZE));
        setupGame();
        setupControls();
        loadImages();
    }

    /**
     * Carga las imágenes de los puyos desde archivos.
     * Si ocurre algún error, se muestra un mensaje en la consola.
     */
    private void loadImages() {
        try {
            puyoImages.put(Color.RED, ImageIO.read(new File("src/assets/puyo_red.png")));
            puyoImages.put(Color.BLUE, ImageIO.read(new File("src/assets/puyo_blue.png")));
            puyoImages.put(Color.GREEN, ImageIO.read(new File("src/assets/puyo_green.png")));
            puyoImages.put(Color.YELLOW, ImageIO.read(new File("src/assets/puyo_yellow.png")));
        } catch (IOException e) {
            System.err.println("Error al cargar las imágenes: " + e.getMessage());
        }
    }

    /**
     * Configura el estado inicial del juego.
     * Inicializa el tablero, el par actual de puyos y el temporizador que controla el movimiento.
     */
    private void setupGame() {
        gameOver = false;
        board = new Board();
        currentPair = null;

        // Crea un temporizador que cada 1000 ms (1 segundo) ejecuta el movimiento hacia abajo
        timer = new Timer(1000, e -> {
            if (!gameOver) {
                moveDown();
                repaint();
            }
        });
        timer.start();
    }

    /**
     * Configura los controles de teclado.
     * Se añade un KeyAdapter para gestionar los eventos de teclado y mover o rotar el par de puyos.
     */
    private void setupControls() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && currentPair != null) {
                    // Procesa la tecla presionada
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            // Mueve el par hacia la izquierda si es posible
                            if (canMove(-1)) {
                                currentPair.x1--;
                                currentPair.x2--;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            // Mueve el par hacia la derecha si es posible
                            if (canMove(1)) {
                                currentPair.x1++;
                                currentPair.x2++;
                            }
                            break;
                        case KeyEvent.VK_Z:
                            // Rota el par de puyos usando la grilla actual para validar la rotación
                            currentPair.rotate(board.getGrid());
                            break;
                        case KeyEvent.VK_DOWN:
                            // Fuerza el movimiento hacia abajo
                            moveDown();
                            break;
                    }
                    repaint();
                }
            }
        });
    }

    /**
     * Verifica si el par actual se puede mover horizontalmente en la dirección indicada.
     *
     * @param dx El desplazamiento horizontal (-1 para izquierda, 1 para derecha).
     * @return true si el movimiento es válido; false en caso contrario.
     */
    private boolean canMove(int dx) {
        int newX1 = currentPair.x1 + dx;
        int newX2 = currentPair.x2 + dx;
        Color[][] grid = board.getGrid();

        // Si las esferas están en alturas diferentes, se mueve la que está más arriba (la que aún no ha colisionado)
        if (currentPair.y1 != currentPair.y2) {
            if (currentPair.y1 < currentPair.y2) {
                return newX1 >= 0 && newX1 < Constants.BOARD_WIDTH && grid[currentPair.y1][newX1] == null;
            } else {
                return newX2 >= 0 && newX2 < Constants.BOARD_WIDTH && grid[currentPair.y2][newX2] == null;
            }
        }

        // Si ambas esferas están a la misma altura, se verifican ambas posiciones
        return newX1 >= 0 && newX1 < Constants.BOARD_WIDTH &&
                newX2 >= 0 && newX2 < Constants.BOARD_WIDTH &&
                grid[currentPair.y1][newX1] == null &&
                grid[currentPair.y2][newX2] == null;
    }

    /**
     * Mueve el par actual de puyos hacia abajo.
     * Si el par no puede seguir cayendo, se coloca en el tablero, se verifican las coincidencias y se prepara el siguiente par.
     */
    private void moveDown() {
        // Si no existe un par actual, se crea uno nuevo
        if (currentPair == null) {
            currentPair = new Puyo();
            // Si la posición inicial ya está ocupada, termina el juego
            if (!board.isValidPosition(currentPair.x1, currentPair.y1) ||
                    !board.isValidPosition(currentPair.x2, currentPair.y2)) {
                gameOver = true;
                timer.stop();
                return;
            }
        }

        Color[][] grid = board.getGrid();
        // Verifica si cada esfera del par puede caer hacia abajo
        boolean canFall1 = board.canFall(currentPair.x1, currentPair.y1);
        boolean canFall2 = board.canFall(currentPair.x2, currentPair.y2);

        if (canFall1 && canFall2) {
            // Si ambas esferas pueden caer, se incrementa la coordenada y de ambas
            currentPair.y1++;
            currentPair.y2++;
            repaint();
            return;
        } else {
            // Si solo una de las esferas puede caer, se mueve esa esfera
            if (canFall1) {
                currentPair.y1++;
                repaint();
                return;
            }
            if (canFall2) {
                currentPair.y2++;
                repaint();
                return;
            }
            // Si ninguna esfera puede caer, se coloca el par en el tablero,
            // se verifican las coincidencias y se prepara el siguiente par
            board.placePuyo(currentPair);
            board.checkMatches();
            currentPair = null;
        }
    }

    /**
     * Método de renderizado del panel.
     * Se encarga de dibujar la grilla del tablero, los puyos colocados y el par actual.
     *
     * @param g El objeto Graphics utilizado para dibujar.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color[][] grid = board.getGrid();


        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                g.setColor(Color.GRAY);
                g.drawRect(x * Constants.CELL_SIZE, y * Constants.CELL_SIZE,
                           Constants.CELL_SIZE, Constants.CELL_SIZE);
            }
        }


        // Dibujar los puyos que ya están colocados en la grilla
        for (int y = 0; y < Constants.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Constants.BOARD_WIDTH; x++) {
                if (grid[y][x] != null) {
                    Image image = puyoImages.get(grid[y][x]);
                    if (image != null) {
                        g.drawImage(image, x * Constants.CELL_SIZE + 2, y * Constants.CELL_SIZE + 2,
                                Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
                    } else {
                        g.setColor(grid[y][x]);
                        g.fillOval(x * Constants.CELL_SIZE + 2, y * Constants.CELL_SIZE + 2,
                                Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
                    }
                }
            }
        }

        // Dibujar el par actual de puyos (el que se encuentra en movimiento)
        if (currentPair != null) {
            // Dibujar la primera esfera
            Image image1 = puyoImages.get(currentPair.color1);
            if (image1 != null) {
                g.drawImage(image1, currentPair.x1 * Constants.CELL_SIZE + 2,
                        currentPair.y1 * Constants.CELL_SIZE + 2,
                        Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
            } else {
                g.setColor(currentPair.color1);
                g.fillOval(currentPair.x1 * Constants.CELL_SIZE + 2,
                        currentPair.y1 * Constants.CELL_SIZE + 2,
                        Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
            }

            // Dibujar la segunda esfera
            Image image2 = puyoImages.get(currentPair.color2);
            if (image2 != null) {
                g.drawImage(image2, currentPair.x2 * Constants.CELL_SIZE + 2,
                        currentPair.y2 * Constants.CELL_SIZE + 2,
                        Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4, this);
            } else {
                g.setColor(currentPair.color2);
                g.fillOval(currentPair.x2 * Constants.CELL_SIZE + 2,
                        currentPair.y2 * Constants.CELL_SIZE + 2,
                        Constants.CELL_SIZE - 4, Constants.CELL_SIZE - 4);
            }
        }

        // Mostrar mensaje de "Game Over" si el juego ha finalizado
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
