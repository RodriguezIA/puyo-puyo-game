package game;

import java.awt.*;

/**
 * Clase final que contiene las constantes globales utilizadas en el juego.
 * Al ser final, no puede ser extendida, lo que ayuda a garantizar que sus valores
 * permanezcan inalterados en todo el proyecto.
 */
public final class Constants {
    // Ancho del tablero (número de celdas horizontales)
    public static final int BOARD_WIDTH = 6;
    // Altura del tablero (número de celdas verticales)
    public static final int BOARD_HEIGHT = 12;
    // Tamaño en píxeles de cada celda del tablero
    public static final int CELL_SIZE = 40;
    // Número mínimo de puyos conectados necesarios para formar una coincidencia (match)
    public static final int MIN_MATCH = 4;
    // Colores disponibles para los puyos que se usarán en el juego
    public static final Color[] PUYO_COLORS = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };


    private Constants() {
    }
}
