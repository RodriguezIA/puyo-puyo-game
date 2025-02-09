package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel {
    private static final int BOARD_WIDTH = 6;
    private static final int BOARD_HEIGHT = 12;
    private static final int CELL_SIZE = 40;

    private Color[][] board;
    private Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};
    private Puyo currentPair;  // Declaración de currentPair
    private boolean gameOver;
    private Timer timer;

    // Clase interna Puyo
    private class Puyo {
        int x1, y1; // First sphere
        int x2, y2; // Second sphere
        Color color1, color2;

        public Puyo() {
            // Start position at top center
            x1 = BOARD_WIDTH / 2 - 1;
            y1 = 0;
            x2 = BOARD_WIDTH / 2;
            y2 = 0;

            // Random colors
            Random rand = new Random();
            color1 = colors[rand.nextInt(colors.length)];
            color2 = colors[rand.nextInt(colors.length)];
        }

        public void rotate() {
            // Calculate new position after rotation
            int newX2 = x1 - (y2 - y1);
            int newY2 = y1 + (x2 - x1);

            // Check if rotation is valid
            if (newX2 >= 0 && newX2 < BOARD_WIDTH && newY2 >= 0 && newY2 < BOARD_HEIGHT
                    && board[newY2][newX2] == null) {
                x2 = newX2;
                y2 = newY2;
            }
        }
    }

    public GamePanel() {
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        setupGame();
        setupControls();
    }

    private void setupGame() {
        gameOver = false;
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
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
                        case KeyEvent.VK_UP:
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
        return newX1 >= 0 && newX1 < BOARD_WIDTH && newX2 >= 0 && newX2 < BOARD_WIDTH
                && board[currentPair.y1][newX1] == null
                && board[currentPair.y2][newX2] == null;
    }

    private void moveDown() {
        if (currentPair == null) {
            currentPair = new Puyo();
            if (!isValidPosition(currentPair.x1, currentPair.y1) ||
                    !isValidPosition(currentPair.x2, currentPair.y2)) {
                gameOver = true;
                timer.stop();
                return;
            }
        }

        if (canFall(currentPair.x1, currentPair.y1) && canFall(currentPair.x2, currentPair.y2)) {
            currentPair.y1++;
            currentPair.y2++;
        } else {
            placePuyo();
            checkMatches();
            currentPair = null;
        }
    }

    private boolean canFall(int x, int y) {
        return y + 1 < BOARD_HEIGHT && board[y + 1][x] == null;
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < BOARD_WIDTH && y >= 0 && y < BOARD_HEIGHT && board[y][x] == null;
    }

    private void placePuyo() {
        board[currentPair.y1][currentPair.x1] = currentPair.color1;
        board[currentPair.y2][currentPair.x2] = currentPair.color2;
    }

    private void checkMatches() {
        // TODO: Implementar lógica de coincidencias
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw board
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                g.setColor(Color.GRAY);
                g.drawRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

                if (board[y][x] != null) {
                    g.setColor(board[y][x]);
                    g.fillOval(x * CELL_SIZE + 2, y * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                }
            }
        }

        // Draw current pair
        if (currentPair != null) {
            g.setColor(currentPair.color1);
            g.fillOval(currentPair.x1 * CELL_SIZE + 2, currentPair.y1 * CELL_SIZE + 2,
                    CELL_SIZE - 4, CELL_SIZE - 4);
            g.setColor(currentPair.color2);
            g.fillOval(currentPair.x2 * CELL_SIZE + 2, currentPair.y2 * CELL_SIZE + 2,
                    CELL_SIZE - 4, CELL_SIZE - 4);
        }

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", getWidth() / 4, getHeight() / 2);
        }
    }
}