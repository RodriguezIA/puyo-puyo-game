package game;

import javax.swing.*;

/**
 * Clase Frame que extiende de JFrame y representa la ventana principal del juego.
 * Aquí se configura la ventana.
 */
public class Frame extends JFrame {


    public Frame() {
        //establecemos el título de la ventana.
        super("Puyo Puyo");

        // - Al cerrar la ventana, se termina la ejecución de la aplicación.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);


        GamePanel gamePanel = new GamePanel();
        add(gamePanel);
        // Ajusta el tamaño de la ventana al contenido del panel (usa el tamaño preferido definido en el panel).
        pack();

        // Centra la ventana en la pantalla.
        setLocationRelativeTo(null);
        setVisible(true);

        // Se asegura que el panel tenga el foco para poder capturar los eventos de teclado.
        gamePanel.requestFocusInWindow();
    }
}
